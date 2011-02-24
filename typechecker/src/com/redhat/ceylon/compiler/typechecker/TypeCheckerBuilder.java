package com.redhat.ceylon.compiler.typechecker;

import com.redhat.ceylon.compiler.typechecker.io.VFS;
import com.redhat.ceylon.compiler.typechecker.io.VirtualFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Build a TypeChecker using the builder pattern
 * <code>
 *     TypeChecker typeChecker =
 *     new TypeCheckerBuilder()
 *         .addSrcDirectory(srcDirectory)
 *         .getTypeChecker();
 * </code>
 *
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class TypeCheckerBuilder {
    private boolean verbose = false;
    private List<VirtualFile> srcDirectories = new ArrayList<VirtualFile>();
    private final VFS vfs = new VFS();

    public TypeCheckerBuilder() {
    }

    public TypeCheckerBuilder addSrcDirectory(File srcDirectory) {
        srcDirectories.add( vfs.getFromFile( srcDirectory ) );
        return this;
    }


    public TypeCheckerBuilder verbose(boolean isVerbose) {
        this.verbose = isVerbose;
        return this;
    }

    public TypeChecker getTypeChecker() {
        return new TypeChecker(vfs, srcDirectories, verbose);
    }

}
