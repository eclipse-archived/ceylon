package com.redhat.ceylon.compiler.typechecker;

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
    private List<File> srcDirectories = new ArrayList<File>();

    public TypeCheckerBuilder() {
    }

    public TypeCheckerBuilder addSrcDirectory(File srcDirectory) {
        srcDirectories.add(srcDirectory);
        return this;
    }


    public TypeCheckerBuilder verbose(boolean isVerbose) {
        this.verbose = isVerbose;
        return this;
    }

    public TypeChecker getTypeChecker() {
        return new TypeChecker(srcDirectories, verbose);
    }

}
