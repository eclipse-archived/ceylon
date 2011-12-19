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

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

/**
 * I/O utils.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 * @author FroMage (for SHA1)
 */
public class IOUtils {

    private static final Logger log = Logger.getLogger(IOUtils.class.getName());
    private static final char[] Hexadecimal = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    static void safeClose(Closeable c) {
        try {
            if (c != null)
                c.close();
        } catch (IOException ignored) {
        }
    }

    /**
     * Copy stream.
     *
     * @param in  input stream
     * @param out output stream
     * @throws IOException for any I/O error
     */
    public static void copyStream(InputStream in, OutputStream out) throws IOException {
        try {
            final byte[] bytes = new byte[8192];
            int cnt;
            while ((cnt = in.read(bytes)) != -1) {
                out.write(bytes, 0, cnt);
            }
            out.flush();
        } finally {
            safeClose(in);
            safeClose(out);
        }
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
        final ObjectInputStream ois = new ObjectInputStream(inputStream) {
            @Override
            protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
                return cl.loadClass(desc.getName());
            }
        };
        try {
            Object result = ois.readObject();
            return contentType.cast(result);
        } catch (ClassNotFoundException e) {
            throw new IOException(e);
        } finally {
            ois.close();
        }
    }

    static void writeToFile(File file, InputStream inputStream) throws IOException {
        copyStream(inputStream, new FileOutputStream(file));
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
}
