/*
 * Copyright 2012 Red Hat inc. and third party contributors as noted
 * by the author tags.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Broadly based on http://qdolan.blogspot.com.es/2008/10/embedded-jar-classloader-in-under-100.html
 */
package com.redhat.ceylon.tools.assemble;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.Enumeration;
import java.util.jar.Attributes;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author Tako Schotanus (tako@ceylon-lang.org)
 */
public class CeylonAssemblyRunner {
    
    public static final String ATTR_ASSEMBLY_MAIN_CLASS = "X-Ceylon-Assembly-Main-Class";
    
    public static void main(String[] args) throws Exception {
        invokeMain(getAssemblyMainClass(), args);
    }

    public static String getAssemblyMainClass() throws IOException {
        ProtectionDomain protectionDomain = CeylonAssemblyRunner.class.getProtectionDomain();
        CodeSource codeSource = protectionDomain.getCodeSource();
        if (codeSource != null) {
            URL srcUrl = codeSource.getLocation();
            URL assemblyUrl = new URL("jar", "", srcUrl + "!/");
            JarURLConnection uc = (JarURLConnection)assemblyUrl.openConnection();
            Attributes attrs = uc.getMainAttributes();
            if (attrs != null) {
                return attrs.getValue(ATTR_ASSEMBLY_MAIN_CLASS);
            }
        }
        return null;
    }

    public static void invokeMain(String className, String[] args) throws Exception {
        try (CeylonAssemblyClassLoader loader = new CeylonAssemblyClassLoader(
                Thread.currentThread().getContextClassLoader())) {
            Class<?> clazz = loader.loadClass(className);
            Method method = clazz.getMethod("main", new Class[] { args.getClass() });
            method.setAccessible(true);
            int mods = method.getModifiers();
            if (method.getReturnType() != void.class || !Modifier.isStatic(mods)
                    || !Modifier.isPublic(mods)) {
                throw new NoSuchMethodException("'main' in class '" + className + "'");
            }
            method.invoke(null, new Object[] { args });
        }
    }

    public static class CeylonAssemblyClassLoader extends URLClassLoader {

        public CeylonAssemblyClassLoader(ClassLoader parent) {
            super(new URL[] {}, parent);
            ProtectionDomain protectionDomain = getClass().getProtectionDomain();
            CodeSource codeSource = protectionDomain.getCodeSource();
            if (codeSource != null) {
                File assembly = new File(codeSource.getLocation().getPath());
                // Create a temp folder to extract our assembly into
                File tmpAssemblyFolder = null;
                try {
                    tmpAssemblyFolder = Files.createTempDirectory("ceylon-assembly-").toFile();
                    extractArchive(assembly, tmpAssemblyFolder);
                    deleteOnExit(tmpAssemblyFolder);
                } catch (IOException ex) {
                    delete(tmpAssemblyFolder);
                    throw new RuntimeException(ex);
                }
            }
        }

        private void extractArchive(File zip, File dir) throws IOException {
            try (ZipFile zf = new ZipFile(zip)) {
                Enumeration<? extends ZipEntry> entries = zf.entries();
                while (entries.hasMoreElements()) {
                    ZipEntry entry = entries.nextElement();
                    String entryName = entry.getName();
                    File out = new File(dir, entryName);
                    if (entry.isDirectory()) {
                        continue;
                    }
                    if (!shouldInclude(entryName)) {
                        continue;
                    }
                    addURL(out.toURI().toURL());
                    mkdirs(out.getParentFile());
                    try (InputStream zipIn = zf.getInputStream(entry)) {
                        try (BufferedOutputStream fileOut = new BufferedOutputStream(new FileOutputStream(out))) {
                            copyStream(zipIn, fileOut, false, false);
                        }
                    }
                }
            }
        }
        
        private static boolean shouldInclude(String entryName) {
            entryName = entryName.toLowerCase();
            return !entryName.isEmpty() &&
                    (entryName.endsWith(".jar")
                            || entryName.endsWith(".car"));
        }

        private static File mkdirs(File dir) {
            if (!dir.exists() && !dir.mkdirs()) {
                throw new RuntimeException("Unable to create destination directory: " + dir);
            }
            return dir;
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

        public static void safeClose(Closeable c) {
            try {
                if (c != null) {
                    c.close();
                }
            } catch (IOException ignored) {
            }
        }

        // The normal File.deleteOnExit() doesn't work for directories that might
        // not be empty, so we need to add a shutdown hook to do it ourselves
        private static void deleteOnExit(final File repo) {
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    delete(repo);
                }
            });
        }
        
        // Recursively delete files and folders
        private static boolean delete(File f) {
            boolean ok = true;
            if (f != null && f.exists()) {
                if (f.isDirectory()) {
                    for (File c : f.listFiles()) {
                        ok = ok && delete(c);
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
    }
}

