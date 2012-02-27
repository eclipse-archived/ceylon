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
import java.io.IOException;
import java.util.List;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.compiler.java.util.Util;
import com.redhat.ceylon.compiler.typechecker.io.ArtifactProvider;
import com.redhat.ceylon.compiler.typechecker.io.ClosableVirtualFile;
import com.redhat.ceylon.compiler.typechecker.io.VFS;

public class RepositoryArtifactProvider implements ArtifactProvider {

    private RepositoryManager repository;
    private VFS vfs;

    public RepositoryArtifactProvider(RepositoryManager repository, VFS vfs) {
        this.repository = repository;
        this.vfs = vfs;
    }

    @Override
    public ClosableVirtualFile getArtifact(List<String> moduleName, String version, Iterable<String> extensions) {
        ArtifactContext context = new ArtifactContext();
        for(String ext : extensions){
            context.setName(Util.getName(moduleName));
            context.setVersion(version);
            context.setSuffix("."+ext);
            File artifact;
            try {
                artifact = repository.getArtifact(context);
            } catch (IOException e) {
                e.printStackTrace();
                // try next extension
                continue;
            }
            // we found it
            if(artifact != null)
                return vfs.getFromZipFile(artifact);
        }
        // not found
        return null;
    }

}
