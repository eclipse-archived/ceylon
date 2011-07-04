package com.redhat.ceylon.compiler.typechecker;

import com.redhat.ceylon.compiler.typechecker.io.VFS;
import com.redhat.ceylon.compiler.typechecker.io.VirtualFile;
import com.redhat.ceylon.compiler.typechecker.util.AssertionVisitor;

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
    private boolean verifyDependencies = true;
    private AssertionVisitor assertionVisitor = new AssertionVisitor() { 
        @Override protected boolean includeWarnings() {
            return false;
        }
    };

    public TypeCheckerBuilder() {
    }

    public TypeCheckerBuilder addSrcDirectory(File srcDirectory) {
        srcDirectories.add( vfs.getFromFile( srcDirectory ) );
        return this;
    }

    /**
     * @deprecated this is bad and a temporary hack
     *
     * The problem (which is a temporary one) with this is that it will try to load required modules from the ceylon repo,
     * as source (such as the ceylon language module) and we want to load them using our javac process and from class files
     * (or source code to autocompile).
     * This is a temporary hack until we figure out how to do module loading properly in the compiler (including repos and stuff,
     * but really, later).
     *
     * @author Emmanuel Bernard <emmanuel@hibernate.org>
     * @author Stephane Epardaud <stephane.epardaud@gmail.com>
     *
     */
    public TypeCheckerBuilder skipDependenciesVerification() {
        this.verifyDependencies = false;
        return this;
    }

    public TypeCheckerBuilder assertionVisitor(AssertionVisitor visitor) {
        this.assertionVisitor = visitor;
        return this;
    }

    public TypeCheckerBuilder verbose(boolean isVerbose) {
        this.verbose = isVerbose;
        return this;
    }

    public TypeChecker getTypeChecker() {
        return new TypeChecker(vfs, srcDirectories, verifyDependencies, assertionVisitor, verbose);
    }

}
