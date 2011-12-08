/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
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
     * @param in input stream
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
