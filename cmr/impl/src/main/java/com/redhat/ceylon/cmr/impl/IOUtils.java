/*
 * Copyright 2011 Red Hat inc. and third party contributors as noted 
 * by the author tags.
 * 
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
 */

package com.redhat.ceylon.cmr.impl;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import com.redhat.ceylon.cmr.api.ArtifactResult;

/**
 * I/O utils.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 * @author FroMage (for SHA1)
 */
public class IOUtils {

    private static final Logger log = Logger.getLogger(IOUtils.class.getName());
    private static final char[] Hexadecimal = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * Safe close.
     *
     * @param c the closeable
     */
    public static void safeClose(Closeable c) {
        try {
            if (c != null) {
                c.close();
            }
        } catch (IOException ignored) {
        }
    }

    public static IOException toIOException(Throwable t) {
        return (t instanceof IOException ? (IOException) t : new IOException(t));
    }

    /**
     * Delete files recursively.
     *
     * @param file the current file or folder to delete
     */
    public static void deleteRecursively(File file) {
        if (file.isDirectory()) {
            for (File f : file.listFiles())
                deleteRecursively(f);
        }
        //noinspection ResultOfMethodCallIgnored
        file.delete();
    }

    /**
     * Copy stream.
     *
     * @param in       input stream
     * @param out      output stream
     * @param closeIn  do we close input stream
     * @param closeOut do we close output stream
     * @throws IOException for any I/O error
     */
    public static void copyStream(InputStream in, OutputStream out, boolean closeIn, boolean closeOut) throws IOException {
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

    static InputStream toInputStream(File file) throws IOException {
        return new FileInputStream(file);
    }

    static <T extends Serializable> InputStream toObjectStream(T content) throws IOException {
        if (content == null)
            throw new IllegalArgumentException("Null content");

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(content);
        oos.flush();
        return new ByteArrayInputStream(baos.toByteArray());
    }

    static <T> T fromStream(Class<T> contentType, InputStream inputStream) throws IOException {
        if (contentType == null)
            throw new IllegalArgumentException("Null content type!");
        if (inputStream == null)
            throw new IllegalArgumentException("Null input stream!");

        final ClassLoader cl = contentType.getClassLoader();
        try (ObjectInputStream ois = new ObjectInputStream(inputStream) {
            protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
                return cl.loadClass(desc.getName());
            }
        }) {
            Object result = ois.readObject();
            return contentType.cast(result);
        } catch (ClassNotFoundException e) {
            throw new IOException(e);
        }
    }

    static void writeToFile(File file, InputStream inputStream) throws IOException {
        copyStream(inputStream, new FileOutputStream(file), false, true);
    }

    static String sha1(InputStream is) {
        final MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            // can't happen, specs say SHA-1 must be implemented
            log.warning("Failed to get a SHA-1 message digest, your JRE does not follow the specs. No SHA-1 signature will be made");
            return null;
        }

        final byte[] buffer = new byte[1024];
        int read;
        try {
            while ((read = is.read(buffer)) != -1) {
                digest.update(buffer, 0, read);
            }
        } catch (IOException e) {
            log.warning("Failed to read input stream, no SHA-1 signature will be made");
            return null;
        } finally {
            safeClose(is);
        }
        return toHexString(digest.digest());
    }

    static String readSha1(InputStream is) throws IOException {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        try {
            return reader.readLine();
        } finally {
            safeClose(reader);
        }
    }

    static String toHexString(byte[] bytes) {
        final char[] chars = new char[bytes.length * 2];
        for (int b = 0, c = 0; b < bytes.length; b++) {
            int v = (int) bytes[b] & 0xFF;
            chars[c++] = Hexadecimal[v / 16];
            chars[c++] = Hexadecimal[v % 16];
        }
        return new String(chars);
    }

    public static class ZipRoot {
        public final File root;
        public final String prefix;

        public ZipRoot(File root, String prefix) {
            this.root = root;
            this.prefix = prefix;
        }
    }

    public static File zipFolder(File root) throws IOException {
        return zipFolders(new ZipRoot(root, ""));
    }

    public static File zipFolders(ZipRoot... zipRoots) throws IOException {
        for (ZipRoot zipRoot : zipRoots) {
            if (!zipRoot.root.isDirectory())
                throw new IOException("Zip root must be a folder");
        }
        File zipFile = File.createTempFile("module-doc", ".zip");
        try {
            ZipOutputStream os = new ZipOutputStream(new FileOutputStream(zipFile));
            try {
                for (ZipRoot zipRoot : zipRoots) {
                    for (File f : zipRoot.root.listFiles()) {
                        zipInternal(zipRoot.prefix, f, os);
                    }
                }
            } finally {
                os.flush();
                os.close();
            }
            return zipFile;
        } catch (IOException x) {
            zipFile.delete();
            throw x;
        }
    }

    private static void zipInternal(String path, File file, ZipOutputStream os) throws IOException {
        String filePath;
        if(path.isEmpty())
            filePath = file.getName();
        else
            filePath = path + "/" + file.getName();
        if (file.isDirectory()) {
            for (File f : file.listFiles())
                zipInternal(filePath, f, os);
        } else {
            ZipEntry entry = new ZipEntry(filePath);
            os.putNextEntry(entry);
            try (FileInputStream in = new FileInputStream(file)) {
                copyStreamNoClose(in, os);
            }
            os.closeEntry();
        }
    }

    public enum UnzipFailure {
        DestinationNotDirectory,
        CannotCreateDestination,
        CopyError
    }
    
    @SuppressWarnings("serial")
    public static class UnzipException extends RuntimeException {
        public final UnzipFailure failure;
        public final File dir;
        public final String entryName;

        public UnzipException(UnzipFailure failure, File dir){
            this.failure = failure;
            this.dir = dir;
            this.entryName = null;
        }

        public UnzipException(UnzipFailure failure, String entryName, IOException e) {
            super(e);
            this.failure = failure;
            this.dir = null;
            this.entryName = entryName;
        }
    }
    
    public static void extractArchive(File zip, File dir) throws IOException {
        if (dir.exists()) {
            if (!dir.isDirectory()) {
                throw new UnzipException(UnzipFailure.DestinationNotDirectory, dir);
            }
        } else {
            mkdirs(dir);
        }

        try (ZipFile zf = new ZipFile(zip)) {
            Enumeration<? extends ZipEntry> entries = zf.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String entryName = entry.getName();
                try {
                    File out = new File(dir, entryName);
                    if (entry.isDirectory()) {
                        mkdirs(out);
                        continue;
                    }
                    mkdirs(out.getParentFile());
                    try (InputStream zipIn = zf.getInputStream(entry)) {
                        try (BufferedOutputStream fileOut = new BufferedOutputStream(new FileOutputStream(out))) {
                            IOUtils.copyStream(zipIn, fileOut, false, false);
                        }
                    }
                } catch (IOException e) {
                    throw new UnzipException(UnzipFailure.CopyError, entryName, e);
                }
            }
        }
    }
    
    private static File mkdirs(File dir) {
        if (!dir.exists() && !dir.mkdirs()) {
            throw new UnzipException(UnzipFailure.CannotCreateDestination, dir);
        }
        return dir;
    }

    public static InputStream findDescriptor(ArtifactResult result, String descriptorPath) {
        try {
            File file = result.artifact();
            try (ZipFile zipFile = new ZipFile(file)) {
                ZipEntry zipEntry = zipFile.getEntry(descriptorPath);
                if (zipEntry != null) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    copyStream(zipFile.getInputStream(zipEntry), baos, true, true);
                    return new ByteArrayInputStream(baos.toByteArray());
                } else {
                    return null;
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
