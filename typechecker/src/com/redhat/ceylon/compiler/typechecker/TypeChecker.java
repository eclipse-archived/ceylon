package com.redhat.ceylon.compiler.typechecker;

import java.util.List;

import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.compiler.typechecker.analyzer.ModuleValidator;
import com.redhat.ceylon.compiler.typechecker.context.Context;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnits;
import com.redhat.ceylon.compiler.typechecker.io.VFS;
import com.redhat.ceylon.compiler.typechecker.io.VirtualFile;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.tree.Message;
import com.redhat.ceylon.compiler.typechecker.util.AssertionVisitor;
import com.redhat.ceylon.compiler.typechecker.util.ModuleManagerFactory;
import com.redhat.ceylon.compiler.typechecker.util.StatisticsVisitor;

/**
 * Executes type checking upon construction and retrieve a CompilationUnit object for a given File.
 *
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
//TODO make an interface?
public class TypeChecker {

    public static final String LANGUAGE_MODULE_VERSION = "1.0.0";

    private final boolean verbose;
    private final boolean statistics;
    private final Context context;
    private final PhasedUnits phasedUnits;
    private List<PhasedUnits> phasedUnitsOfDependencies;
    private final boolean verifyDependencies;
    private final AssertionVisitor assertionVisitor;
    private final StatisticsVisitor statsVisitor;

    //package level
    TypeChecker(VFS vfs, List<VirtualFile> srcDirectories, RepositoryManager repositoryManager, boolean verifyDependencies,
            AssertionVisitor assertionVisitor, ModuleManagerFactory moduleManagerFactory, boolean verbose, boolean statistics,
            List<String> moduleFilters, String encoding) {
        long start = System.nanoTime();
        this.verbose = verbose;
        this.statistics = statistics;
        this.context = new Context(repositoryManager, vfs);
        this.phasedUnits = new PhasedUnits(context, moduleManagerFactory);
        this.verifyDependencies = verifyDependencies;
        this.assertionVisitor = assertionVisitor;
        statsVisitor = new StatisticsVisitor();
        phasedUnits.setModuleFilters(moduleFilters);
        phasedUnits.setEncoding(encoding);
        phasedUnits.parseUnits(srcDirectories);
        long time = System.nanoTime()-start;
        if(statistics)
        	System.out.println("Parsed in " + time/1000000 + " ms");
    }

    public PhasedUnits getPhasedUnits() {
        return phasedUnits;
    }
    
    public List<PhasedUnits> getPhasedUnitsOfDependencies() {
        return phasedUnitsOfDependencies;
    }
    
    public void setPhasedUnitsOfDependencies(
            List<PhasedUnits> phasedUnitsOfDependencies) {
        this.phasedUnitsOfDependencies = phasedUnitsOfDependencies;
    }

    public Context getContext() {
        return context;
    }

    /**
     * Return the PhasedUnit for a given relative path.
     * The path is relative to the source directory
     * eg ceylon/language/Object.ceylon
     */
    public PhasedUnit getPhasedUnitFromRelativePath(String relativePath) {
        PhasedUnit phasedUnit = phasedUnits.getPhasedUnitFromRelativePath(relativePath);
        if (phasedUnit == null) {
            for (PhasedUnits units : phasedUnitsOfDependencies) {
                phasedUnit = units.getPhasedUnitFromRelativePath(relativePath);
                if (phasedUnit != null) {
                    return phasedUnit;
                }
            }
            return null;
        }
        else {
            return phasedUnit;
        }
    }

    public PhasedUnit getPhasedUnit(VirtualFile file) {
        PhasedUnit phasedUnit = phasedUnits.getPhasedUnit(file);
        if (phasedUnit == null) {
            for (PhasedUnits units : phasedUnitsOfDependencies) {
                phasedUnit = units.getPhasedUnit(file);
                if (phasedUnit != null) {
                    return phasedUnit;
                }
            }
            return null;
        }
        else {
            return phasedUnit;
        }
    }

    /*
     * Return the CompilationUnit for a given file.
     * May return null of the CompilationUnit has not been parsed.
     */
    /*public Tree.CompilationUnit getCompilationUnit(File file) {
        final PhasedUnit phasedUnit = phasedUnits.getPhasedUnit( context.getVfs().getFromFile(file) );
        return phasedUnit.getCompilationUnit();
    }*/

    public void process() throws RuntimeException {
        long start = System.nanoTime();
        executePhases(phasedUnits, false);
        long time = System.nanoTime()-start;
        if(statistics)
        	System.out.println("Type checked in " + time/1000000 + " ms");
    }

    private void executePhases(PhasedUnits phasedUnits, boolean forceSilence) {
        final List<PhasedUnit> listOfUnits = phasedUnits.getPhasedUnits();

        phasedUnits.getModuleManager().prepareForTypeChecking();
        phasedUnits.visitModules();
        phasedUnits.getModuleManager().modulesVisited();

        //By now le language module version should be known (as local)
        //or we should use the default one.
        Module languageModule = context.getModules().getLanguageModule();
        if (languageModule.getVersion() == null) {
            languageModule.setVersion(LANGUAGE_MODULE_VERSION);
        }

        final ModuleValidator moduleValidator = new ModuleValidator(context, phasedUnits);
        if (verifyDependencies) {
            moduleValidator.verifyModuleDependencyTree();
        }
        phasedUnitsOfDependencies = moduleValidator.getPhasedUnitsOfDependencies();

        for (PhasedUnit pu : listOfUnits) {
            pu.validateTree();
            pu.scanDeclarations();
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
        for (PhasedUnit pu: listOfUnits) {
            pu.analyseFlow();
        }
        for (PhasedUnit pu: listOfUnits) {
            pu.analyseUsage();
        }

        if (!forceSilence) {
            for (PhasedUnit pu : listOfUnits) {
                if (verbose) {
                    pu.display();
                }
                pu.generateStatistics(statsVisitor);
                pu.runAssertions(assertionVisitor);
            }
            if(verbose||statistics)
            	statsVisitor.print();
            assertionVisitor.print(verbose);
        }
        
    }
    
    public int getErrors(){
    	return assertionVisitor.getErrors();
    }

    public int getWarnings(){
    	return assertionVisitor.getWarnings();
    }
    
    public List<Message> getMessages(){
    	return assertionVisitor.getFoundErrors();
    }
}
