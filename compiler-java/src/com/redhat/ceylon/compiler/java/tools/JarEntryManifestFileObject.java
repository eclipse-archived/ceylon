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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;

import com.redhat.ceylon.common.log.Logger;
import com.redhat.ceylon.javax.lang.model.element.Modifier;
import com.redhat.ceylon.javax.lang.model.element.NestingKind;
import com.redhat.ceylon.javax.tools.JavaFileObject;
import com.redhat.ceylon.model.loader.JdkProvider;
import com.redhat.ceylon.model.loader.OsgiUtil;
import com.redhat.ceylon.model.typechecker.model.Module;

public class JarEntryManifestFileObject implements JavaFileObject {

    private String jarFileName;
    private String fileName;
    private Module module;
    private String osgiProvidedBundles;
    private ByteArrayOutputStream baos;
	private JdkProvider jdkProvider;
	
    public JarEntryManifestFileObject(String jarFileName, String fileName, Module module, String osgiProvidedBundles,
    		JdkProvider jdkProvider) {
        super();
        this.jarFileName = jarFileName;
        this.fileName = fileName;
        this.module = module;
        this.osgiProvidedBundles = osgiProvidedBundles;
        this.jdkProvider = jdkProvider;
    }

    @Override
    public OutputStream openOutputStream() throws IOException {
        this.baos = new ByteArrayOutputStream();
        return new FilterOutputStream(this.baos) {
            @Override
            public void close() throws IOException {
                // If the last line of a MANIFEST.MF lacks a newline 
                // then it gets ignored, so let's add one here
                // to avoid that trap
                super.write('\n');
                super.close();
            }
        };
    }
    
    public void writeManifest(JarOutputStream jarFile, Logger log) throws IOException {
        if (baos != null) {
            writeManifestJarEntry(jarFile, readManifest(baos), log);
        }
    }

    private void writeManifestJarEntry(JarOutputStream jarFile, Manifest originalManifest, Logger log) throws IOException {
        Manifest manifest;
        if (module.isDefault()) {
            manifest = new OsgiUtil.DefaultModuleManifest().build();
        } else {
            manifest = new OsgiUtil.OsgiManifest(module, jdkProvider, osgiProvidedBundles, originalManifest, log).build();
        }
        jarFile.putNextEntry(new ZipEntry(fileName));
        manifest.write(jarFile);
    }

    private Manifest readManifest(ByteArrayOutputStream baos) throws IOException {
        return new Manifest(new ByteArrayInputStream(baos.toByteArray()));
    }

    @Override
    public int hashCode() {
        return jarFileName.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof JarEntryManifestFileObject) {
            JarEntryManifestFileObject r = (JarEntryManifestFileObject)obj;
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
