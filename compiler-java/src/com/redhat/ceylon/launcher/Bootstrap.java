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

    public static final String FILE_BOOTSTRAP_PROPERTIES = "ceylon-bootstrap.properties";
    
    public static final String KEY_SHA256SUM = "sha256sum";
    public static final String KEY_INSTALLATION = "installation";
    public static final String KEY_DISTRIBUTION = "distribution";

    private static final String FOLDER_DISTS = "dists";
    
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
                if (e.getCause() != null) {
                    throw e;
                } else {
                    System.err.println("   --> " + e.getMessage());
                    return -1;
                }
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
        Config cfg = loadBootstrapConfig();
        // If hash doesn't exist in dists folder we must download & install
        if (!cfg.distributionDir.exists()) {
            install(cfg);
            if (!cfg.distributionDir.exists()) {
                throw new RuntimeException("Unable to install distribution");
            }
        }
        // Set the correct home folder
        System.setProperty(Constants.PROP_CEYLON_HOME_DIR, cfg.distributionDir.getAbsolutePath());
    }
    
    private static void install(Config cfg) throws Exception {
        File tmpFile = null;
        File tmpFolder = null;
        try {
            // Check if the distribution URI refers to a remote or a local file
            File zipFile;
            if (cfg.distribution.getScheme() != null) {
                // Start download of URL to temp file
                tmpFile = zipFile = File.createTempFile("ceylon-bootstrap-dist-", ".part");
                setupProxyAuthentication();
                download(cfg.distribution, zipFile, new ProgressMonitor() {
                    @Override
                    public void update(long read, long size) {
                        String progress;
                        if (size == -1) {
                            progress = String.valueOf(read / 1024L) + "K";
                        } else {
                            progress = String.valueOf(read * 100 / size) + "%";
                        }
                        System.out.print("Downloading Ceylon... " + progress + "\r");
                    }
                });
            } else {
                // It's a local file, no need to download
                zipFile = new File(cfg.properties.getParentFile(), cfg.distribution.getPath()).getAbsoluteFile();
            }
            // Verify zip file if we have a sha sum
            if (cfg.sha256sum != null) {
                String sum = calculateSha256Sum(zipFile);
                if (!sum.equals(cfg.sha256sum)) {
                    throw new RuntimeException("Error verifying Ceylon distribution archive: SHA sums do not match");
                }
            }
            // Unzip file to temp folder in dists folder
            mkdirs(cfg.resolvedInstallation);
            tmpFolder = Files.createTempDirectory(cfg.resolvedInstallation.toPath(), "ceylon-bootstrap-dist-").toFile();
            extractArchive(zipFile, tmpFolder);
            // Rename temp folder to hash
            tmpFolder.renameTo(cfg.distributionDir);
            // Clearing the download progress text on the console
            System.out.print("                              \r");
        } finally {
            // Delete temp file and folder
            if (tmpFile != null) {
                delete(tmpFile);
            }
            if (tmpFolder != null) {
                delete(tmpFolder);
            }
        }
    }
    
    private static File getPropertiesFile() throws URISyntaxException {
        String cbp = System.getProperty("ceylon.bootstrap.properties");
        if (cbp != null) {
            return new File(cbp);
        } else {
            File jar = LauncherUtil.determineRuntimeJar();
            return new File(jar.getParentFile(), FILE_BOOTSTRAP_PROPERTIES);
        }
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
    
    private static class Config {
        File properties;
        URI distribution;
        File installation;
        File resolvedInstallation;
        File distributionDir;
        String hash;
        String sha256sum;
    }
    
    private static Config loadBootstrapConfig() throws Exception {
        Properties properties = loadBootstrapProperties();
        Config cfg = new Config();
        
        cfg.properties = getPropertiesFile();
        
        // Obtain dist download URL
        if (!properties.containsKey(KEY_DISTRIBUTION)) {
            throw new RuntimeException("Error in bootstrap properties file: missing 'distribution'");
        }
        cfg.distribution = new URI(properties.getProperty(KEY_DISTRIBUTION));

        // Hash the URI, it will be our distribution's folder name
        cfg.hash = hash(cfg.distribution.toString());
        
        // See if the distribution should be installed in some other place than the default
        if (properties.containsKey(KEY_INSTALLATION)) {
            // Get the installation path
            String installString = properties.getProperty(KEY_INSTALLATION);
            // Do some simple variable expansion
            installString = installString
                    .replaceAll("^~", System.getProperty("user.home"))
                    .replace("${user.home}", System.getProperty("user.home"))
                    .replace("${ceylon.user.dir}", getUserDir().getAbsolutePath());
            cfg.installation = new File(installString);
            cfg.resolvedInstallation = cfg.properties.getParentFile().toPath().resolve(cfg.installation.toPath()).toFile().getAbsoluteFile();
        } else {
            cfg.resolvedInstallation = new File(getUserDir(), FOLDER_DISTS);
        }
        
        // The actual installation directory for the distribution
        cfg.distributionDir = new File(cfg.resolvedInstallation, cfg.hash);

        // If the properties contain a sha256sum store it for later
        cfg.sha256sum = properties.getProperty(KEY_SHA256SUM);

        return cfg;
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
    
    /**
     * This method calculates the SHA256 sum of the provided {@code file}
     * Copied from Gradle's Install
     */
    private static String calculateSha256Sum(File file) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        InputStream fis = null;
        try {
            fis = new FileInputStream(file);
            int n = 0;
            byte[] buffer = new byte[4096];
            while (n != -1) {
                n = fis.read(buffer);
                if (n > 0) {
                    md.update(buffer, 0, n);
                }
            }
            byte byteData[] = md.digest();
    
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i < byteData.length; i++) {
                String hex=Integer.toHexString(0xff & byteData[i]);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
    
            return hexString.toString();
        } finally {
            fis.close();
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
