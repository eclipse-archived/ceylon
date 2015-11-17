package com.redhat.ceylon.compiler.java.test.issues.bug24xx.bug2414;

import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.installation.InstallRequest;
import org.eclipse.aether.installation.InstallResult;
import org.eclipse.aether.installation.InstallationException;
import com.redhat.ceylon.model.cmr.RepositoryException;
import com.redhat.ceylon.cmr.ceylon.ModuleCopycat;

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
        };
    }
}