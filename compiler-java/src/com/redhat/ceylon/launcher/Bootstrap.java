package com.redhat.ceylon.launcher;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.redhat.ceylon.common.Constants;

/**
 * This is the earliest bootstrap class for the Ceylon tool chain.
 * It does nothing more than trying to locate the system repository
 * and load an appropriate ceylon.bootstrap module.
 * Appropriate in this case means it will try to find the version this
 * class was compiled with (see <code>Versions.CEYLON_VERSION_NUMBER</code>)
 * or the version specified by the <code>CEYLON_VERSION</code> environment
 * variable.
 * After it locates the module it will pass the execution on to the
 * <code>Launcher.main()</code> it contains.
 * 
 * IMPORTANT This class should contain as little logic as possible and
 * delegate as soon as it can to the <code>Launcher</code> in the
 * ceylon.bootstrap module. This way we can maintain backward and forward
 * compatibility as much as possible.
 * 
 * @author Tako Schotanus
 */
public class Bootstrap {

    private static final int DOWNLOAD_TIMEOUT_READ = 30000;
    private static final int DOWNLOAD_TIMEOUT_CONNECT = 15000;
    private static final int DOWNLOAD_BUFFER_SIZE = 4096;

    public static void main(String[] args) throws Throwable {
        // we don't need to clean up the class loader when run from main because the JVM will either exit, or
        // keep running with daemon threads in which case it will keep needing this classloader open 
        int exit = run(args);
        // WARNING: NEVER CALL EXIT IF WE STILL HAVE DAEMON THREADS RUNNING AND WE'VE NO REASON TO EXIT WITH A NON-ZERO CODE
        if (exit != 0) {
            System.exit(exit);
        }
    }

    public static int run(String... args) throws Throwable {
        CeylonClassLoader cl = null;
        try {
            Integer result = -1;
            Method runMethod = null;
            try {
                String ceylonVersion;
                if (isDistBootstrap()) {
                    setupDistHome();
                    ceylonVersion = determineDistVersion();
                } else {
                    ceylonVersion = LauncherUtil.determineSystemVersion();
                }
                File module = CeylonClassLoader.getRepoJar("ceylon.bootstrap", ceylonVersion);
                cl = CeylonClassLoader.newInstance(Arrays.asList(module));
                Class<?> launcherClass = cl.loadClass("com.redhat.ceylon.launcher.Launcher");
                runMethod = launcherClass.getMethod("run", String[].class);
            } catch (Exception e) {
                System.err.println("Fatal: Ceylon command could not be executed");
                throw e;
            }
            try {
                result = (Integer)runMethod.invoke(null, (Object)args);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
            return result.intValue();
        } finally {
            if (cl != null) {
                cl.clearCache();
                try {
                    cl.close();
                } catch (IOException e) {
                    // Ignore
                }
            }
        }
    }
    
    private static boolean isDistBootstrap() throws URISyntaxException {
        File propsFile = getPropertiesFile();
        return propsFile.exists();
    }
    
    private static void setupDistHome() throws Exception {
        // Load properties
        Properties properties = loadBootstrapProperties();
        // Obtain dist download URL
        if (!properties.containsKey("distribution")) {
            throw new RuntimeException("Error in bootstrap properties file: missing 'distribution'");
        }
        URI distUri = new URI((String)properties.get("distribution"));
        // Hash the URI, it will be our distribution's folder name
        String hash = hash(distUri.toString());
        // If hash doesn't exist in dists folder we must download & install
        File distDir = getDistDir(hash);
        if (!distDir.exists()) {
            install(distUri, hash);
            if (!distDir.exists()) {
                throw new RuntimeException("Unable to install distribution");
            }
        }
        // Set the correct home folder
        System.setProperty(Constants.PROP_CEYLON_HOME_DIR, distDir.getAbsolutePath());
    }
    
    private static void install(URI distUri, String hash) throws IOException {
        // Start download of URL to temp file
        File tmpFile = File.createTempFile("ceylon-bootstrap-dist-", ".part");
        File tmpFolder = null;
        try {
            setupProxyAuthentication();
            download(distUri, tmpFile, null);
            // Unzip file to temp folder in dists folder
            mkdirs(getDistsDir());
            tmpFolder = Files.createTempDirectory(getDistsDir().toPath(), "ceylon-bootstrap-dist-").toFile();
            extractArchive(tmpFile, tmpFolder);
            // Rename temp folder to hash
            File distDir = getDistDir(hash);
            tmpFolder.renameTo(distDir);
        } finally {
            // Delete temp file and folder
            delete(tmpFile);
            if (tmpFolder != null) {
                delete(tmpFolder);
            }
        }
    }
    
    private static File getPropertiesFile() throws URISyntaxException {
        File jar = LauncherUtil.determineRuntimeJar();
        return new File(jar.getParentFile(), "ceylon-bootstrap.properties");
    }
    
    private static Properties loadBootstrapProperties() throws Exception {
        File propsFile = getPropertiesFile();
        FileInputStream fileInput = null;
        try {
            fileInput = new FileInputStream(propsFile);
            Properties properties = new Properties();
            properties.load(fileInput);
            return properties;
        } finally {
            if (fileInput != null) {
                fileInput.close();
            }
        }
    }
    
    private static File mkdirs(File dir) {
        if (!dir.exists() && !dir.mkdirs()) {
            throw new RuntimeException("Unable to create destination directory: " + dir);
        }
        return dir;
    }
    
    private static void delete(File f) {
        if (!delete_(f)) {
            // As a last resort
            f.deleteOnExit();
        }
    }
    
    private static boolean delete_(File f) {
        boolean ok = true;
        if (f.exists()) {
            if (f.isDirectory()) {
                for (File c : f.listFiles()) {
                    ok = ok && delete_(c);
                }
            }
            try {
                boolean deleted = f.delete();
                ok = ok && deleted;
            } catch (Exception ex) {
                ok = false;
            }
        }
        return ok;
    }
    
    private static File getDefaultUserDir() {
        String userHome = System.getProperty("user.home");
        return new File(userHome, ".ceylon");
    }

    private static File getUserDir() {
        String ceylonUserDir = System.getProperty(Constants.PROP_CEYLON_USER_DIR);
        if (ceylonUserDir != null) {
            return new File(ceylonUserDir);
        } else {
            return getDefaultUserDir();
        }
    }
    
    private static File getDistsDir() {
        return new File(getUserDir(), "dists");
    }
    
    private static File getDistDir(String hash) {
        return new File(getDistsDir(), hash);
    }
    
    private static void extractArchive(File zip, File dir) throws IOException {
        if (dir.exists()) {
            if (!dir.isDirectory()) {
                throw new RuntimeException("Error extracting archive: destination not a directory: " + dir);
            }
        } else {
            mkdirs(dir);
        }

        ZipFile zf = null;
        try {
            zf = new ZipFile(zip);
            Enumeration<? extends ZipEntry> entries = zf.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String entryName = stripRoot(entry.getName());
                try {
                    if (entryName.isEmpty()) {
                        continue;
                    }
                    File out = new File(dir, entryName);
                    if (entry.isDirectory()) {
                        mkdirs(out);
                        continue;
                    }
                    mkdirs(out.getParentFile());
                    InputStream zipIn = null;
                    try {
                        zipIn = zf.getInputStream(entry);
                        BufferedOutputStream fileOut = null;
                        try {
                            fileOut = new BufferedOutputStream(new FileOutputStream(out));
                            copyStream(zipIn, fileOut, false, false);
                        } finally {
                            fileOut.close();
                        }
                    } finally {
                        zipIn.close();
                    }
                } catch (IOException e) {
                    throw new RuntimeException("Error extracting archive", e);
                }
            }
        } finally {
            zf.close();
        }
    }
    
    private static String stripRoot(String name) {
        int p = name.indexOf('/');
        if (p > 0) {
            name = name.substring(p + 1);
        }
        return name;
    }

    private static void copyStream(InputStream in, OutputStream out, boolean closeIn, boolean closeOut) throws IOException {
        try {
            copyStreamNoClose(in, out);
        } finally {
            if (closeIn) {
                safeClose(in);
            }
            if (closeOut) {
                safeClose(out);
            }
        }
    }

    private static void copyStreamNoClose(InputStream in, OutputStream out) throws IOException {
        final byte[] bytes = new byte[8192];
        int cnt;
        while ((cnt = in.read(bytes)) != -1) {
            out.write(bytes, 0, cnt);
        }
        out.flush();
    }
    
    private static void safeClose(Closeable c) {
        try {
            if (c != null) {
                c.close();
            }
        } catch (IOException ignored) {
        }
    }
    
    /**
     * This method computes a hash of the provided {@code string}.
     * Copied from Gradle's PathAssembler
     */
    private static String hash(String string) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] bytes = string.getBytes();
            messageDigest.update(bytes);
            return new BigInteger(1, messageDigest.digest()).toString(36);
        } catch (Exception e) {
            throw new RuntimeException("Error creating hash", e);
        }
    }
    
    private static interface ProgressMonitor {
        void update(long read, long size);
    }
    
    private static void download(URI uri, File file, ProgressMonitor progress) throws IOException {
        URLConnection connection = null;
        InputStream input = null;
        OutputStream output = null;
        try {
            URL url = uri.toURL();
            connection = url.openConnection();
            connection.setConnectTimeout(DOWNLOAD_TIMEOUT_CONNECT);
            connection.setReadTimeout(DOWNLOAD_TIMEOUT_READ);
            input = connection.getInputStream();
            output = new FileOutputStream(file);
            int n;
            long read = 0;
            long size = connection.getContentLength();
            byte[] buffer = new byte[DOWNLOAD_BUFFER_SIZE];
            while ((n = input.read(buffer)) != -1) {
                output.write(buffer, 0, n);
                read += n;
                if (progress != null) {
                    progress.update(read, size);
                }
            }
        } finally {
            if (output != null) {
                output.close();
            }
            if (input != null) {
                input.close();
            }
        }
    }
    
    /**
     * Sets up proxy authentication if the associated system properties
     * are available: "http.proxyUser" and "http.proxyPassword"
     * Copied from Gradle's Download
     */
    private static void setupProxyAuthentication() {
        if (System.getProperty("http.proxyUser") != null) {
            Authenticator.setDefault(new ProxyAuthenticator());
        }
    }
    
    private static class ProxyAuthenticator extends Authenticator {
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(
                    System.getProperty("http.proxyUser"),
                    System.getProperty("http.proxyPassword", "").toCharArray());
        }
    }

    private static String determineDistVersion() {
        File distHome = new File(System.getProperty(Constants.PROP_CEYLON_HOME_DIR));
        File distRepo = new File(distHome, "repo");
        File bootstrap = new File(new File(distRepo, "ceylon"), "bootstrap");
        File[] versions = bootstrap.listFiles(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory();
            }
        });
        if (versions == null || versions.length != 1) {
            throw new RuntimeException("Error in distribution: missing bootstrap in " + bootstrap.getAbsolutePath());
        }
        return versions[0].getName();
    }
}
