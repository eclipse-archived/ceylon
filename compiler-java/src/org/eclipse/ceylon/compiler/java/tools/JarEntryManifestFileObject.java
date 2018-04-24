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

package org.eclipse.ceylon.compiler.java.tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.util.jar.Manifest;

import org.eclipse.ceylon.common.log.Logger;
import org.eclipse.ceylon.javax.lang.model.element.Modifier;
import org.eclipse.ceylon.javax.lang.model.element.NestingKind;
import org.eclipse.ceylon.javax.tools.JavaFileObject;
import org.eclipse.ceylon.model.loader.JdkProvider;
import org.eclipse.ceylon.model.loader.OsgiUtil;
import org.eclipse.ceylon.model.typechecker.model.Module;

public class JarEntryManifestFileObject implements JavaFileObject {

    private File jarTempFolder;
    private String fileName;
    private Module module;
    private String osgiProvidedBundles;
    private ByteArrayOutputStream baos;
    private JdkProvider jdkProvider;
    
    public JarEntryManifestFileObject(File jarTempFolder, String fileName, Module module, String osgiProvidedBundles,
            JdkProvider jdkProvider) {
        super();
        this.jarTempFolder = jarTempFolder;
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
    
    public Manifest writeManifest(Logger log) throws IOException {
        if (baos != null) {
            return writeManifestJarEntry(readManifest(baos), log);
        }
        return null;
    }

    private Manifest writeManifestJarEntry(Manifest originalManifest, Logger log) throws IOException {
        Manifest manifest;
        if (module.isDefaultModule()) {
            manifest = new OsgiUtil.DefaultModuleManifest().build();
        } else {
            manifest = new OsgiUtil.OsgiManifest(module, jdkProvider, osgiProvidedBundles, originalManifest, log).build();
        }
        return manifest;
    }

    private Manifest readManifest(ByteArrayOutputStream baos) throws IOException {
        return new Manifest(new ByteArrayInputStream(baos.toByteArray()));
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
        if (obj instanceof JarEntryManifestFileObject) {
            JarEntryManifestFileObject r = (JarEntryManifestFileObject)obj;
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
