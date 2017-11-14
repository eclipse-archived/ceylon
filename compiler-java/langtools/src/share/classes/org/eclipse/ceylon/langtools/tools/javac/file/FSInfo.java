/*
 * Copyright (c) 2011, 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package org.eclipse.ceylon.langtools.tools.javac.file;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.eclipse.ceylon.langtools.tools.javac.util.Context;

/**
 * Get meta-info about files. Default direct (non-caching) implementation.
 * @see CacheFSInfo
 *
 * <p><b>This is NOT part of any supported API.
 * If you write code that depends on this, you do so at your own risk.
 * This code and its internal interfaces are subject to change or
 * deletion without notice.</b>
 */
public class FSInfo {

    /** Get the FSInfo instance for this context.
     *  @param context the context
     *  @return the Paths instance for this context
     */
    public static FSInfo instance(Context context) {
        FSInfo instance = context.get(FSInfo.class);
        if (instance == null)
            instance = new FSInfo();
        return instance;
    }

    protected FSInfo() {
    }

    protected FSInfo(Context context) {
        context.put(FSInfo.class, this);
    }

    public File getCanonicalFile(File file) {
        try {
            return file.getCanonicalFile();
        } catch (IOException e) {
            return file.getAbsoluteFile();
        }
    }

    public boolean exists(File file) {
        return file.exists();
    }

    public boolean isDirectory(File file) {
        return file.isDirectory();
    }

    public boolean isFile(File file) {
        return file.isFile();
    }

    public List<File> getJarClassPath(File file) throws IOException {
        String parent = file.getParent();
        JarFile jarFile = new JarFile(file);
        try {
            Manifest man = jarFile.getManifest();
            if (man == null)
                return Collections.emptyList();

            Attributes attr = man.getMainAttributes();
            if (attr == null)
                return Collections.emptyList();

            String path = attr.getValue(Attributes.Name.CLASS_PATH);
            if (path == null)
                return Collections.emptyList();

            List<File> list = new ArrayList<File>();

            for (StringTokenizer st = new StringTokenizer(path); st.hasMoreTokens(); ) {
                String elt = st.nextToken();
                File f = (parent == null ? new File(elt) : new File(parent, elt));
                list.add(f);
            }

            return list;
        } finally {
            jarFile.close();
        }
    }
}
