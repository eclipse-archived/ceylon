package com.redhat.ceylon.compiler.typechecker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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

    public static final String LANGUAGE_MODULE_VERSION = "0.6";

    private final boolean verbose;
    private final boolean statistics;
    private final List<VirtualFile> srcDirectories;
    private final Context context;
    private final PhasedUnits phasedUnits;
    private List<PhasedUnits> phasedUnitsOfDependencies;
    private final boolean verifyDependencies;
    private final AssertionVisitor assertionVisitor;
    private final StatisticsVisitor statsVisitor;
    private final boolean parallel;

    //package level
    TypeChecker(VFS vfs, List<VirtualFile> srcDirectories, RepositoryManager repositoryManager, boolean verifyDependencies,
            AssertionVisitor assertionVisitor, ModuleManagerFactory moduleManagerFactory, boolean verbose, boolean statistics,
            List<String> moduleFilters, boolean parallel) {
        long start = System.nanoTime();
        this.srcDirectories = srcDirectories;
        this.verbose = verbose;
        this.statistics = statistics;
        this.context = new Context(repositoryManager, vfs);
        this.phasedUnits = new PhasedUnits(context, moduleManagerFactory);
        this.verifyDependencies = verifyDependencies;
        this.assertionVisitor = assertionVisitor;
        statsVisitor = new StatisticsVisitor();
        phasedUnits.setModuleFilters(moduleFilters);
        phasedUnits.parseUnits(srcDirectories);
        long time = System.nanoTime()-start;
        this.parallel = parallel;
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

        if(parallel)
            justDoItInParallel(listOfUnits, forceSilence);
        else
            justDoIt(listOfUnits, forceSilence);
    }

    private void justDoItInParallel(List<PhasedUnit> listOfUnits, boolean forceSilence) {
        // we divide by 2 to discard HT
        ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()/2);
        List<Callable<Void>> callables = new ArrayList<Callable<Void>>(listOfUnits.size());
        for (final PhasedUnit pu : listOfUnits) {
            callables.add(new Callable<Void>(){
                @Override
                public Void call() throws Exception {
                    pu.validateTree();
                    pu.scanDeclarations();
                    return null;
                }});
        }
        execute(callables, threadPool);
        for (final PhasedUnit pu : listOfUnits) {
            callables.add(new Callable<Void>(){
                @Override
                public Void call() throws Exception {
                    pu.scanTypeDeclarations();
                    return null;
                }});
        }
        execute(callables, threadPool);
        for (final PhasedUnit pu: listOfUnits) {
            callables.add(new Callable<Void>(){
                @Override
                public Void call() throws Exception {
                    pu.validateRefinement();
                    return null;
                }});
        }
        execute(callables, threadPool);
        for (final PhasedUnit pu : listOfUnits) {
            callables.add(new Callable<Void>(){
                @Override
                public Void call() throws Exception {
                    pu.analyseTypes();
                    return null;
                }});
        }
        execute(callables, threadPool);
        for (final PhasedUnit pu: listOfUnits) {
            callables.add(new Callable<Void>(){
                @Override
                public Void call() throws Exception {
                    pu.analyseFlow();
                    return null;
                }});
        }
        execute(callables, threadPool);
        for (final PhasedUnit pu: listOfUnits) {
            callables.add(new Callable<Void>(){
                @Override
                public Void call() throws Exception {
                    pu.analyseUsage();
                    return null;
                }});
        }
        execute(callables, threadPool);

        if (!forceSilence) {
            for (final PhasedUnit pu : listOfUnits) {
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

    private void justDoIt(List<PhasedUnit> listOfUnits, boolean forceSilence) {
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

    private void execute(List<Callable<Void>> callables, ExecutorService threadPool) {
        try {
            List<Future<Void>> futures = threadPool.invokeAll(callables);
            for (Future<Void> future : futures) {
                future.get();
            }
            callables.clear();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e.getCause());
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
