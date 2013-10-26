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

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.tools.JavaFileObject;

public class JarEntryFileObject implements JavaFileObject {

    private JarOutputStream jarFile;
    private String fileName;
    private String jarFileName;

    public JarEntryFileObject(String jarFileName, JarOutputStream jarFile, String fileName) {
        super();
        this.jarFileName = jarFileName;
        this.jarFile = jarFile;
        this.fileName = fileName;
    }

    /*
     * This is the only method used in the class, the rest is just there to satisfy
     * the type system.
     */
    @Override
    public OutputStream openOutputStream() throws IOException {
        // we start to write at a new entry
        jarFile.putNextEntry(new ZipEntry(fileName));
        return new FilterOutputStream(jarFile){
            // we override the close() method to automagically close the current entry
            @Override
            public void close() throws IOException {
            }
        };
    }

    @Override
    public int hashCode() {
        return jarFileName.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof JarEntryFileObject) {
            JarEntryFileObject r = (JarEntryFileObject)obj;
            return jarFileName.equals(r.jarFileName);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return jarFileName+":"+fileName;
    }
    
    //
    // All the following methods are just boilerplate and never called
    
    @Override
    public URI toUri() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public InputStream openInputStream() throws IOException {
        return null;
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
