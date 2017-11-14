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
package org.eclipse.ceylon.compiler.java.test.issues.bug24xx.bug2414;

import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.installation.InstallRequest;
import org.eclipse.aether.installation.InstallResult;
import org.eclipse.aether.installation.InstallationException;
import org.eclipse.ceylon.model.cmr.RepositoryException;
import org.eclipse.ceylon.cmr.ceylon.ModuleCopycat;

class Bug2414 {
    public static void bug(Artifact jarArtifact) {
        
        final RepositorySystem system = null;//locator.getService(RepositorySystem.class);

        final DefaultRepositorySystemSession session = null;//MavenRepositorySystemUtils.newSession();
        ModuleCopycat copycat = new ModuleCopycat(rmb.buildManager(), new RepositoryManager() {
            @Override
            public void putArtifact(ArtifactContext artifactContext, File file) throws RepositoryException {
        
                try {
                    InstallRequest installRequest = new InstallRequest();
                    installRequest.addArtifact(jarArtifact)/*.addArtifact( pomArtifact )*/;
                    InstallResult result = system.install(session, installRequest);
                } catch (InstallationException e) {
                    throw new RepositoryException(e);
                }
            }
        });
    }
}