package com.redhat.ceylon.launcher;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Ceylon-specific class loader that knows how to find and add
 * all needed dependencies for compiler and runtime.
 * Implements child-first class loading to prevent mix-ups with
 * Java's own tool-chain.
 *
 * @author Tako Schotanus
 *
 */
public class CeylonClassLoader extends URLClassLoader {

    public CeylonClassLoader() throws URISyntaxException, MalformedURLException, FileNotFoundException {
        super(getClassPathUrls());
    }

    public CeylonClassLoader(ClassLoader parentLoader) throws URISyntaxException, MalformedURLException, FileNotFoundException {
        super(getClassPathUrls(), parentLoader);
    }

    private static URL[] getClassPathUrls() throws URISyntaxException, MalformedURLException, FileNotFoundException {
        List<File> cp = getClassPath();
        URL[] urls = new URL[cp.size()];
        int i = 0;
        for (File f : cp) {
            urls[i++] = f.toURI().toURL();
        }
        return urls;
    }

    public static List<File> getClassPath() throws URISyntaxException, FileNotFoundException {
        // Determine the necessary folders
        File ceylonHome = LauncherUtil.determineHome();
        File ceylonRepo = LauncherUtil.determineRepo();
        File ceylonLib = LauncherUtil.determineLibs();

        // Perform some sanity checks
        if (!ceylonHome.isDirectory()) {
            throw new FileNotFoundException("Could not determine the Ceylon home directory (" + ceylonHome + ")");
        }
        if (!ceylonRepo.isDirectory()) {
            throw new FileNotFoundException("The Ceylon system repository could not be found (" + ceylonRepo + ")");
        }
        if (!ceylonLib.isDirectory()) {
            throw new FileNotFoundException("The Ceylon system libraries could not be found (" + ceylonLib + ")");
        }

        List<File> archives = new LinkedList<File>();

        // List all the JARs we find in the LIB directory
        findLibraries(archives, ceylonLib);

        // List all the necessary Ceylon JARs and CARs
        String version = LauncherUtil.determineSystemVersion();
        archives.add(getRepoJar(ceylonRepo, "com.redhat.ceylon.compiler.java", version));
        archives.add(getRepoCar(ceylonRepo, "ceylon.language", version));
        archives.add(getRepoJar(ceylonRepo, "ceylon.runtime", version));
        archives.add(getRepoJar(ceylonRepo, "com.redhat.ceylon.compiler.js", version));
        archives.add(getRepoJar(ceylonRepo, "com.redhat.ceylon.typechecker", version));
        archives.add(getRepoJar(ceylonRepo, "com.redhat.ceylon.common", version));
        archives.add(getRepoJar(ceylonRepo, "com.redhat.ceylon.module-resolver", version));
        archives.add(getRepoJar(ceylonRepo, "org.jboss.jandex", "1.0.3.Final"));
        archives.add(getRepoJar(ceylonRepo, "org.jboss.modules", "1.1.3.GA"));
        archives.add(getRepoJar(ceylonRepo, "org.jboss.logmanager", "1.4.0.Final"));
        // Maven support for CMR
        archives.add(getRepoJar(ceylonRepo, "com.redhat.ceylon.maven-support", "1.0")); // optional
        // For the typechecker
        archives.add(getRepoJar(ceylonRepo, "org.antlr.runtime", "3.4"));
        // For the JS backend
        archives.add(getRepoJar(ceylonRepo, "net.minidev.json-smart", "1.1.1"));
        // For the "doc" tool
        archives.add(getRepoJar(ceylonRepo, "org.tautua.markdownpapers.core", "1.2.7"));
        archives.add(getRepoJar(ceylonRepo, "com.github.rjeschke.txtmark", "0.8-c0dcd373ce"));
        // For the --out http:// functionality of the compiler
        archives.add(getRepoJar(ceylonRepo, "com.googlecode.sardine", "314"));
        archives.add(getRepoJar(ceylonRepo, "org.apache.httpcomponents.httpclient", "4.1.1"));
        archives.add(getRepoJar(ceylonRepo, "org.apache.httpcomponents.httpcore", "4.1.1"));
        archives.add(getRepoJar(ceylonRepo, "org.apache.commons.logging", "1.1.1"));
        archives.add(getRepoJar(ceylonRepo, "org.apache.commons.codec", "1.4"));
        archives.add(getRepoJar(ceylonRepo, "org.slf4j.api", "1.6.1"));
        archives.add(getRepoJar(ceylonRepo, "org.slf4j.simple", "1.6.1")); // optional

        return archives;
    }

    private static File getRepoJar(File repo, String moduleName, String version) {
        return getRepoUrl(repo, moduleName, version, "jar");
    }

    private static File getRepoCar(File repo, String moduleName, String version) {
        return getRepoUrl(repo, moduleName, version, "car");
    }

    private static File getRepoUrl(File repo, String moduleName, String version, String extension) {
        return new File(repo, moduleName.replace('.', '/') + "/" + version + "/" + moduleName + "-" + version + "." + extension);
    }

    private static void findLibraries(List<File> libs, File folder) {
        File[] items = folder.listFiles();
        for (File f : items) {
            if (f.isDirectory()) {
                findLibraries(libs, f);
            } else if (f.getName().toLowerCase().endsWith(".jar")) {
                libs.add(f);
            }
        }
    }

    @Override
    protected synchronized Class<?> loadClass(String name, boolean resolve)
            throws ClassNotFoundException {
        // First, check if the class has already been loaded
        Class<?> c = findLoadedClass(name);
        if (c == null) {
            try {
                // checking local
                c = findClass(name);
            } catch (ClassNotFoundException e) {
                // checking parent
                // This call to loadClass may eventually call findClass again, in case the parent doesn't find anything.
                c = super.loadClass(name, resolve);
            }
        }
        if (resolve) {
            resolveClass(c);
        }
        return c;
    }

    @Override
    public URL getResource(String name) {
        URL url = findResource(name);
        if (url == null) {
            // This call to getResource may eventually call findResource again, in case the parent doesn't find anything.
            url = super.getResource(name);
        }
        return url;
    }

    @Override
    public Enumeration<URL> getResources(String name) throws IOException {
        /**
        * Similar to super, but local resources are enumerated before parent resources
        */
        Enumeration<URL> localUrls = findResources(name);
        Enumeration<URL> parentUrls = null;
        if (getParent() != null) {
            parentUrls = getParent().getResources(name);
        }
        final List<URL> urls = new ArrayList<URL>();
        if (localUrls != null) {
            while (localUrls.hasMoreElements()) {
                urls.add(localUrls.nextElement());
            }
        }
        if (parentUrls != null) {
            while (parentUrls.hasMoreElements()) {
                urls.add(parentUrls.nextElement());
            }
        }
        return new Enumeration<URL>() {
            Iterator<URL> iter = urls.iterator();

            public boolean hasMoreElements() {
                return iter.hasNext();
            }
            public URL nextElement() {
                return iter.next();
            }
        };
    }

    @Override
    public InputStream getResourceAsStream(String name) {
        URL url = getResource(name);
        try {
            return url != null ? url.openStream() : null;
        } catch (IOException e) {
        }
        return null;
    }

}
