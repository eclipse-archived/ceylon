/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */

package com.redhat.ceylon.compiler.java.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;

import com.redhat.ceylon.common.FileUtil;
import com.redhat.ceylon.javax.lang.model.element.Modifier;
import com.redhat.ceylon.javax.lang.model.element.NestingKind;
import com.redhat.ceylon.javax.tools.JavaFileObject;

public class JarEntryFileObject implements JavaFileObject {

    private File jarTempFolder;
    private String fileName;
    private File file;

    public JarEntryFileObject(File jarTempFolder, String fileName) {
        super();
        this.jarTempFolder = jarTempFolder;
        this.fileName = fileName;
        this.file = new File(jarTempFolder, fileName);
    }

    /*
     * This is the only method used in the class, the rest is just there to satisfy
     * the type system.
     */
    @Override
    public OutputStream openOutputStream() throws IOException {
        FileUtil.mkdirs(file.getParentFile());
        return new FileOutputStream(file);
    }

    @Override
    public int hashCode() {
        int ret = 17;
        ret = (37 * ret) + jarTempFolder.hashCode();
        ret = (37 * ret) + fileName.hashCode();
        return ret;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof JarEntryFileObject) {
            JarEntryFileObject r = (JarEntryFileObject)obj;
            return jarTempFolder.equals(r.jarTempFolder)
                    && fileName.equals(r.fileName);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return jarTempFolder+":"+fileName;
    }
    
    //
    // All the following methods are just boilerplate and never called
    
    @Override
    public URI toUri() {
        return null;
    }

    @Override
    public String getName() {
        return fileName;
    }

    @Override
    public InputStream openInputStream() throws IOException {
        // FIXME: perhaps allow reading from the previous car?
        return new FileInputStream(file);
    }

    @Override
    public Reader openReader(boolean ignoreEncodingErrors)
            throws IOException {
        return null;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors)
            throws IOException {
        return null;
    }

    @Override
    public Writer openWriter() throws IOException {
        return null;
    }

    @Override
    public long getLastModified() {
        return 0;
    }

    @Override
    public boolean delete() {
        return false;
    }

    @Override
    public Kind getKind() {
        return null;
    }

    @Override
    public boolean isNameCompatible(String simpleName, Kind kind) {
        return false;
    }

    @Override
    public NestingKind getNestingKind() {
        return null;
    }

    @Override
    public Modifier getAccessLevel() {
        return null;
    }
}
