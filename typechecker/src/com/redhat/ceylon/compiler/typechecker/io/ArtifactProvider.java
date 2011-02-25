package com.redhat.ceylon.compiler.typechecker.io;

import java.io.File;
import java.util.List;

/**
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class ArtifactProvider {
    private final VirtualFile homeRepo;
    private final VFS vfs;

    public ArtifactProvider(VFS vfs) {
        File home = new File( System.getProperty("user.home") );
        File ceylon = new File( home, ".ceylon" );
        File repo = new File( ceylon, "repo" );
        repo.mkdirs();
        homeRepo = vfs.getFromFile(repo);
        this.vfs = vfs;
    }

    /**
     * Return the artifact or null if not present
     * Must be closed when done with it
     */
    public ClosableVirtualFile getArtifact(List<String> moduleName, String version, String extension) {
        VirtualFile moduleDirectory = getModuleDirectory(moduleName, version);
        if (moduleDirectory == null) {
            return null;
        }
        final VirtualFile child = getChild(moduleDirectory, getArtifactName(moduleName, version, extension) );
        //build sha1 and compare it
        return child == null ? null : vfs.openAsContainer(child);
    }

    /**
     * f.q.module.name-version.extension
     */
    public String getArtifactName(List<String> moduleName, String version, String extension) {
        StringBuilder name = new StringBuilder();
        for (String moduleRadix : moduleName) {
            name.append(moduleRadix).append(".");
        }
        name.deleteCharAt( name.length() - 1 ); //remove trailing dot
        name.append("-").append(version).append(".").append(extension);
        return name.toString();
    }

    /**
     * Navigate down the path down to the version directory
     * f.q.module.name.version
     */
    private VirtualFile getModuleDirectory(List<String> moduleName, String version) {
        VirtualFile current = homeRepo;
        for (String dir : moduleName) {
            current = getChild(current, dir);
            if (current == null) {
                return null;
            }
        }
        current = getChild(current, version);
        return current;
    }

    private VirtualFile getChild(VirtualFile parent, String name) {
        for ( VirtualFile child : parent.getChildren() ) {
            if ( child.getName().equals(name) ) {
                return child;
            }
        }
        return null;
    }
}
