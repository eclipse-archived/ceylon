package com.redhat.ceylon.compiler.typechecker;

import com.redhat.ceylon.compiler.typechecker.context.Context;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnits;
import com.redhat.ceylon.compiler.typechecker.io.VFS;
import com.redhat.ceylon.compiler.typechecker.io.VirtualFile;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;

import java.io.File;
import java.util.List;

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
        process(context);
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

    private void process(Context context) throws RuntimeException {
        //ceylon.language must be built (parsed) before any other

        try {
            //FIXME hack to not parse ceylon.language twice: only works if a single directory is set
            if ( srcDirectories.size() == 1 ) {
                buildLanguageModule( context, srcDirectories.get(0) );
            }
            else {
                buildLanguageModule(context, null);
            }
        }
        catch (RuntimeException e) {
            //let it go
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException("Error while parsing the source directory: " + srcDirectories.get(0).toString() ,e);
        }
        phasedUnits.parseUnits(srcDirectories);
        executePhases(phasedUnits, false);
    }

    private void executePhases(PhasedUnits phasedUnits, boolean forceSilence) {
        final List<PhasedUnit> listOfUnits = phasedUnits.getPhasedUnits();
        for (PhasedUnit pu : listOfUnits) {
            pu.buildModuleImport();
        }

        /*
        At this stage we need to
         - resolve all non local modules (recursively) TODO
         - build the object model of these compiled modules TODO
         - declare a missing module as an error
         - detect circular dependencies
         */
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

    private void executeExternalModulePhases(PhasedUnits phasedUnits) {
        final List<PhasedUnit> listOfUnits = phasedUnits.getPhasedUnits();
        for (PhasedUnit pu : listOfUnits) {
            pu.buildModuleImport();
        }

        /*
        At this stage we need to
         - resolve all non local modules (recursively) TODO
         - build the object model of these compiled modules TODO
         - declare a missing module as an error
         - detect circular dependencies
         */
        context.verifyModuleDependencyTree();

        for (PhasedUnit pu : listOfUnits) {
            pu.scanDeclarations();
        }
        for (PhasedUnit pu : listOfUnits) {
            pu.scanTypeDeclarations();
        }
    }

    private void buildLanguageModule(Context context, VirtualFile master) throws Exception {
        //ceylon.language must be parsed before any other
        if ( master == null ||
                ! ( master.getName().equals("corpus")
                || master.getName().equals("corpus/ceylon")
                || master.getName().equals("corpus/ceylon/language") ) ) {
            VirtualFile file = vfs.getFromFile( new File("corpus/ceylon") );
            PhasedUnits languageUnits = new PhasedUnits(context);
            languageUnits.hackedParseUnit(file);
            executeExternalModulePhases(languageUnits);
        }
    }
}
