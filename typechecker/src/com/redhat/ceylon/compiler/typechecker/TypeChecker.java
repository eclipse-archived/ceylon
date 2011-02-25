package com.redhat.ceylon.compiler.typechecker;

import java.io.File;
import java.util.List;

import com.redhat.ceylon.compiler.typechecker.context.Context;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnits;
import com.redhat.ceylon.compiler.typechecker.io.VFS;
import com.redhat.ceylon.compiler.typechecker.io.VirtualFile;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;

/**
 * Executes type checking upon construction and retrieve a CompilationUnit object for a given File.
 *
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
//TODO make an interface?
public class TypeChecker {

    private final boolean verbose;
    private final List<VirtualFile> srcDirectories;
    private final Context context;
    private final VFS vfs;
    private final PhasedUnits phasedUnits;

    //package level
    TypeChecker(VFS vfs, List<VirtualFile> srcDirectories, boolean verbose) {
        long start = System.nanoTime();
        this.vfs = vfs;
        this.srcDirectories = srcDirectories;
        this.verbose = verbose;
        this.context = new Context(vfs);
        this.phasedUnits = new PhasedUnits(context);
        process();
        long time = System.nanoTime()-start;
        System.out.println("Type checker ran in " + time/1000000 + " ms");
    }

    /**
     * Return the CompilationUnit for a given file.
     * May return null of the CompilationUnit has not been parsed.
     */
    public Tree.CompilationUnit getCompilationUnit(File file) {
        return phasedUnits.getPhasedUnit( vfs.getFromFile( file ) ).getCompilationUnit();
    }

    private void process() throws RuntimeException {
        phasedUnits.parseUnits(srcDirectories);
        executePhases(phasedUnits, false);
    }

    private void executePhases(PhasedUnits phasedUnits, boolean forceSilence) {
        final List<PhasedUnit> listOfUnits = phasedUnits.getPhasedUnits();
        for (PhasedUnit pu : listOfUnits) {
            pu.buildModuleImport();
        }

        context.verifyModuleDependencyTree();

        for (PhasedUnit pu : listOfUnits) {
            pu.scanDeclarations();
            pu.validateControlFlow();
            pu.validateSpecification();
        }
        for (PhasedUnit pu : listOfUnits) {
            pu.scanTypeDeclarations();
        }
        for (PhasedUnit pu: listOfUnits) {
            pu.validateRefinement();
        }
        for (PhasedUnit pu : listOfUnits) {
            pu.analyseTypes();
        }
        for (PhasedUnit pu : listOfUnits) {
            if (!forceSilence) {
                if (verbose) pu.display();
                pu.runAssertions();
            }
        }
    }
}
