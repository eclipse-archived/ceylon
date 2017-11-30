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
package org.eclipse.ceylon.langtools.tools.javac.processing.wrappers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;

import org.eclipse.ceylon.javax.tools.FileObject;

public class FileObjectFacade implements javax.tools.FileObject {

    protected FileObject f;

    public FileObjectFacade(FileObject f) {
        this.f = f;
    }

    @Override
    public boolean delete() {
        return f.delete();
    }

    @Override
    public CharSequence getCharContent(boolean arg0) throws IOException {
        return f.getCharContent(arg0);
    }

    @Override
    public long getLastModified() {
        return f.getLastModified();
    }

    @Override
    public String getName() {
        return f.getName();
    }

    @Override
    public InputStream openInputStream() throws IOException {
        return f.openInputStream();
    }

    @Override
    public OutputStream openOutputStream() throws IOException {
        return f.openOutputStream();
    }

    @Override
    public Reader openReader(boolean arg0) throws IOException {
        return f.openReader(arg0);
    }

    @Override
    public Writer openWriter() throws IOException {
        return f.openWriter();
    }

    @Override
    public URI toUri() {
        return f.toUri();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof FileObjectFacade == false)
            return false;
        return f.equals(((FileObjectFacade)obj).f);
    }
    
    @Override
    public int hashCode() {
        return f.hashCode();
    }
    
    @Override
    public String toString() {
        return f.toString();
    }
}
