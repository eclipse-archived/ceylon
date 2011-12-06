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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * I/O utils.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class IOUtils {

    static void safeClose(Closeable c) {
        try {
            c.close();
        } catch (IOException ignored) {
        }
    }

    static void copyStream(InputStream in, OutputStream out) throws IOException {
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

    static InputStream toInputStream(File file) {
        try {
            return new FileInputStream(file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static <T extends Serializable> InputStream toObjectStream(T content) throws IOException {
        if (content == null)
            throw new IllegalArgumentException("Null content");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
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
        ObjectInputStream ois = new ObjectInputStream(inputStream) {
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
}
