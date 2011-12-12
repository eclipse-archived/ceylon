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

    public ArtifactProvider(VirtualFile homeRepo, VFS vfs) {
        this.homeRepo = homeRepo;
        this.vfs = vfs;
    }
    
    /**
     * Return the artifact or null if not present
     * Must be closed when done with it
     */
    public ClosableVirtualFile getArtifact(List<String> moduleName, String version, Iterable<String> extensions) {
        VirtualFile moduleDirectory = getModuleDirectory(moduleName, version);
        if (moduleDirectory == null) {
            return null;
        }
        for (String extension : extensions) {
            final VirtualFile child = getChild(moduleDirectory, getArtifactName(moduleName, version, extension) );
            if (child != null) {
                //build sha1 and compare it
                return vfs.openAsContainer(child);
            }
        }
        return null;
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

    @Override
    public String toString() {
        return homeRepo.getPath();
    }
}
