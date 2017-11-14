

package org.eclipse.ceylon.cmr.impl;

import java.io.File;

import org.eclipse.ceylon.cmr.api.RepositoryManager;
import org.eclipse.ceylon.model.cmr.Repository;
import org.eclipse.ceylon.model.cmr.RepositoryException;

/**
 * Existing file.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class FileArtifactResult extends AbstractCeylonArtifactResult {
    private final File file;
    private final String repositoryDisplayString;

    protected FileArtifactResult(Repository repository, RepositoryManager manager, String name, String version,
            File file, String repositoryDisplayString) {
        super(repository, manager, name, version);
        this.file = file;
        this.repositoryDisplayString = repositoryDisplayString;
    }

    protected File artifactInternal() throws RepositoryException {
        return file;
    }

    @Override
    public String repositoryDisplayString() {
        return repositoryDisplayString;
    }
}

