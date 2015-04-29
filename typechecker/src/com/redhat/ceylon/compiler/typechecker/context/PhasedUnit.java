package com.redhat.ceylon.compiler.typechecker.context;

import static com.redhat.ceylon.compiler.typechecker.analyzer.ModuleManager.MODULE_FILE;
import static com.redhat.ceylon.compiler.typechecker.analyzer.ModuleManager.PACKAGE_FILE;
import static com.redhat.ceylon.compiler.typechecker.tree.Util.formatPath;

import java.lang.ref.WeakReference;
import java.util.EnumSet;
import java.util.List;

import org.antlr.runtime.CommonToken;

import com.redhat.ceylon.compiler.typechecker.analyzer.AliasVisitor;
import com.redhat.ceylon.compiler.typechecker.analyzer.AnnotationVisitor;
import com.redhat.ceylon.compiler.typechecker.analyzer.ControlFlowVisitor;
import com.redhat.ceylon.compiler.typechecker.analyzer.DeclarationVisitor;
import com.redhat.ceylon.compiler.typechecker.analyzer.DefaultTypeArgVisitor;
import com.redhat.ceylon.compiler.typechecker.analyzer.ExpressionVisitor;
import com.redhat.ceylon.compiler.typechecker.analyzer.InheritanceVisitor;
import com.redhat.ceylon.compiler.typechecker.analyzer.LiteralVisitor;
import com.redhat.ceylon.compiler.typechecker.analyzer.LocalDeclarationVisitor;
import com.redhat.ceylon.compiler.typechecker.analyzer.ModuleManager;
import com.redhat.ceylon.compiler.typechecker.analyzer.ModuleVisitor;
import com.redhat.ceylon.compiler.typechecker.analyzer.RefinementVisitor;
import com.redhat.ceylon.compiler.typechecker.analyzer.SelfReferenceVisitor;
import com.redhat.ceylon.compiler.typechecker.analyzer.SpecificationVisitor;
import com.redhat.ceylon.compiler.typechecker.analyzer.SupertypeVisitor;
import com.redhat.ceylon.compiler.typechecker.analyzer.TypeArgumentVisitor;
import com.redhat.ceylon.compiler.typechecker.analyzer.TypeHierarchyVisitor;
import com.redhat.ceylon.compiler.typechecker.analyzer.TypeVisitor;
import com.redhat.ceylon.compiler.typechecker.analyzer.VisibilityVisitor;
import com.redhat.ceylon.compiler.typechecker.analyzer.Warning;
import com.redhat.ceylon.compiler.typechecker.io.VirtualFile;
import com.redhat.ceylon.compiler.typechecker.io.impl.Helper;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.Unit;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ImportPath;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ModuleDescriptor;
import com.redhat.ceylon.compiler.typechecker.tree.Validator;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.compiler.typechecker.util.AssertionVisitor;
import com.redhat.ceylon.compiler.typechecker.util.DeprecationVisitor;
import com.redhat.ceylon.compiler.typechecker.util.PrintVisitor;
import com.redhat.ceylon.compiler.typechecker.util.ReferenceCounter;
import com.redhat.ceylon.compiler.typechecker.util.StatisticsVisitor;
import com.redhat.ceylon.compiler.typechecker.util.UnitFactory;
import com.redhat.ceylon.compiler.typechecker.util.UsageVisitor;

/**
 * Represent a unit and each of the type checking phases
 *
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class PhasedUnit {
    
    private Tree.CompilationUnit rootNode;
    private Package pkg;
    private Unit unit;
    //must be the non qualified file name
    private String fileName;
    private WeakReference<ModuleManager> moduleManagerRef;
    private final String pathRelativeToSrcDir;
    private VirtualFile unitFile;
    private List<CommonToken> tokens;
    private ModuleVisitor moduleVisitor;
    private VirtualFile srcDir;
    private boolean treeValidated = false;
    private boolean declarationsScanned = false;
    private boolean scanningDeclarations = false;
    private boolean typeDeclarationsScanned = false;
    private boolean refinementValidated = false;
    private boolean flowAnalyzed = false;
    private boolean fullyTyped = false;
    private boolean usageAnalyzed = false;
    private boolean literalsProcessed = false;
    private boolean moduleVisited = false;
    private EnumSet<Warning> suppressedWarnings = 
            EnumSet.noneOf(Warning.class);
    public VirtualFile getSrcDir() {
        return srcDir;
    }

    public PhasedUnit(VirtualFile unitFile, 
            VirtualFile srcDir, 
            Tree.CompilationUnit rootNode, 
            Package p, 
            ModuleManager moduleManager, 
            Context context, 
            List<CommonToken> tokenStream) {
        this.rootNode = rootNode;
        this.pkg = p;
        this.unitFile = unitFile;
        this.srcDir = srcDir;
        this.fileName = unitFile.getName();
        this.pathRelativeToSrcDir = 
                Helper.computeRelativePath(unitFile, srcDir);
        this.moduleManagerRef = 
                new WeakReference<ModuleManager>
                    (moduleManager);
        this.tokens = tokenStream;
        unit = createUnit();
        unit.setFilename(fileName);
        unit.setFullPath(unitFile.getPath());
        unit.setRelativePath(pathRelativeToSrcDir);
        unit.setPackage(pkg);
        pkg.removeUnit(unit);
        pkg.addUnit(unit);
        rootNode.setUnit(unit);
    }

    public PhasedUnit(PhasedUnit other) {
        this.rootNode = other.rootNode;
        this.pkg = other.pkg;
        this.unit = other.unit;
        this.fileName = other.fileName;
        this.moduleManagerRef = 
                new WeakReference<ModuleManager>
                    (other.moduleManagerRef.get());
        this.pathRelativeToSrcDir = other.pathRelativeToSrcDir;
        this.unitFile = other.unitFile;
        this.tokens = other.tokens;
        this.moduleVisitor = other.moduleVisitor;
        this.srcDir = other.srcDir;
        this.treeValidated = other.treeValidated;
        this.declarationsScanned = other.declarationsScanned;
        this.scanningDeclarations = other.scanningDeclarations;
        this.typeDeclarationsScanned = other.typeDeclarationsScanned;
        this.fullyTyped = other.fullyTyped;
        this.refinementValidated = other.refinementValidated;
        this.fullyTyped = other.fullyTyped;
        this.flowAnalyzed = other.flowAnalyzed;
    }

    protected boolean reuseExistingDescriptorModels() {
        return false;
    }
    
    public Module visitSrcModulePhase() {
        if (ModuleManager.MODULE_FILE.equals(fileName) ||
            ModuleManager.PACKAGE_FILE.equals(fileName)) {
            if (!moduleVisited) {
                moduleVisited = true;
                processLiterals();
                moduleVisitor = 
                        new ModuleVisitor(moduleManagerRef.get(), 
                                pkg);
                moduleVisitor.setCompleteOnlyAST(reuseExistingDescriptorModels());
                rootNode.visit(moduleVisitor);
                return moduleVisitor.getMainModule();
            }
        }
        return null;
    }

    protected Unit createUnit() {
        return new Unit();
    }
    
    public void visitRemainingModulePhase() {
        if ( moduleVisitor != null ) {
            moduleVisitor.setPhase(ModuleVisitor.Phase.REMAINING);
            rootNode.visit(moduleVisitor);
            moduleVisitor = null;
        }
    }
    
    public boolean isFullyTyped() {
        return fullyTyped;
    }

    public void setFullyTyped(boolean fullyTyped) {
        this.fullyTyped = fullyTyped;
    }
    
    public boolean isFlowAnalyzed() {
        return flowAnalyzed;
    }

    public void setFlowAnalyzed(boolean flowAnalyzed) {
        this.flowAnalyzed = flowAnalyzed;
    }

    public boolean isTreeValidated() {
        return treeValidated;
    }

    public void setTreeValidated(boolean treeValidated) {
        this.treeValidated = treeValidated;
    }

    public boolean isDeclarationsScanned() {
        return declarationsScanned;
    }

    public void setDeclarationsScanned(boolean declarationsScanned) {
        this.declarationsScanned = declarationsScanned;
    }

    public boolean isTypeDeclarationsScanned() {
        return typeDeclarationsScanned;
    }

    public void setTypeDeclarationsScanned(boolean typeDeclarationsScanned) {
        this.typeDeclarationsScanned = typeDeclarationsScanned;
    }

    public boolean isRefinementValidated() {
        return refinementValidated;
    }

    public void setRefinementValidated(boolean refinementValidated) {
        this.refinementValidated = refinementValidated;
    }

    public void validateTree() {
        //System.out.println("Validating tree for " + fileName);
        if (!treeValidated) {
            String fn = unit.getRelativePath();
            for (int i=0; 
                    i<fn.length(); 
                    i = fn.offsetByCodePoints(i, 1)) {
                int cp = fn.codePointAt(i);
                if (cp>127) {
                    rootNode.addUsageWarning(
                            Warning.filenameNonAscii,
                            "source file name has non-ASCII characters: " + 
                            fn);
                }
            }
            String ufn = unit.getFilename();
            for (Unit u: unit.getPackage().getUnits()) {
                if (!u.equals(unit) && 
                        u.getFilename().equalsIgnoreCase(ufn)) {
                    if (u.getFilename().equals(ufn)) {
                        String errorMessage = 
                                "identical source files: " +
                                unit.getFullPath() + " and " + 
                                u.getFullPath();
                        if (u.getFilename().equals(MODULE_FILE) ||
                            u.getFilename().equals(PACKAGE_FILE)) {
                            errorMessage += " (a module/package descriptor should be defined only once, even in case of multiple source directories)";
                        }
                        rootNode.addError(errorMessage);                        
                    }
                    else {
                        rootNode.addUsageWarning(
                                Warning.filenameCaselessCollision,
                                "source file names differ only by case: " +
                                unit.getFullPath() + " and " + 
                                u.getFullPath());
                    }
                }
            }
            rootNode.visit(new Validator());
            rootNode.visit(new Visitor() {
                @Override
                public void visit(ModuleDescriptor that) {
                    super.visit(that);
                    ImportPath importPath = 
                            that.getImportPath();
                    if (importPath != null) {
                        String moduleName = 
                                formatPath(importPath.getIdentifiers());
                        ModuleManager moduleManager = 
                                moduleManagerRef.get();
                        if (moduleManager != null) {
                            for (Module otherModule: 
                                    moduleManager.getCompiledModules()) {
                                String otherModuleName = 
                                        otherModule.getNameAsString();
                                if (moduleName.startsWith(otherModuleName + ".") || 
                                    otherModuleName.startsWith(moduleName + ".")) {
                                    StringBuilder error = 
                                            new StringBuilder()
                                                .append("Found two modules within the same hierarchy: '")
                                                .append(otherModule.getNameAsString())
                                                .append("' and '")
                                                .append(moduleName)
                                                .append("'");
                                    that.addError(error.toString());
                                }
                            }
                        }
                    }
                }
            });
            treeValidated = true;
        }
    }

    public void scanDeclarations() {
        Boolean enabled = 
                ProducedTypeCache.setEnabled(false);
        try {
            if (!declarationsScanned) {
                processLiterals();
                scanningDeclarations = true;
                //System.out.println("Scan declarations for " + fileName);
                UnitFactory unitFactory = 
                        new UnitFactory() {
                    @Override
                    public Unit createUnit() {
                        return PhasedUnit.this.createUnit();
                    }
                };
                DeclarationVisitor dv = 
                        new DeclarationVisitor(pkg, 
                                fileName,
                                unitFile.getPath(), 
                                pathRelativeToSrcDir, 
                                unitFactory);
                rootNode.visit(dv);
                unit = dv.getCompilationUnit();

                rootNode.visit(new LocalDeclarationVisitor());

                declarationsScanned = true;
                scanningDeclarations = false;
            }
        }
        finally {
            ProducedTypeCache.setEnabled(enabled);
        }
    }

	private void processLiterals() {
		if (!literalsProcessed) {
			rootNode.visit(new LiteralVisitor());
			literalsProcessed = true;
		}
	}

    public void scanTypeDeclarations() {
        Boolean enabled = 
                ProducedTypeCache.setEnabled(false);
        try {
            if (!typeDeclarationsScanned) {
                //System.out.println("Scan type declarations for " + fileName);
                rootNode.visit(new DefaultTypeArgVisitor());
                rootNode.visit(new SupertypeVisitor(false)); //TODO: move to a new phase!
                rootNode.visit(new TypeVisitor());
                typeDeclarationsScanned = true;
            }
        }
        finally {
            ProducedTypeCache.setEnabled(enabled);
        }
    }

    public synchronized void validateRefinement() {
        Boolean enabled = 
                ProducedTypeCache.setEnabled(false);
        try {
            if (!refinementValidated) {
                ProducedType.depth.set(0);
                //System.out.println("Validate member refinement for " + fileName);
                rootNode.visit(new AliasVisitor());
                rootNode.visit(new SupertypeVisitor(true)); //TODO: move to a new phase!
                rootNode.visit(new InheritanceVisitor());
                rootNode.visit(new RefinementVisitor());
                refinementValidated = true;
            }
        }
        finally {
            ProducedTypeCache.setEnabled(enabled);
        }
    }

    public synchronized void analyseTypes() {
        if (!fullyTyped) {
            ProducedType.depth.set(-100);
            //System.out.println("Run analysis phase for " + fileName);
            rootNode.visit(new ExpressionVisitor(moduleManagerRef.get()));
            rootNode.visit(new VisibilityVisitor());
            rootNode.visit(new AnnotationVisitor());
            rootNode.visit(new TypeArgumentVisitor());
            fullyTyped = true;
        }
    }
    
    public synchronized void analyseFlow() {
        if (!flowAnalyzed) {
            rootNode.visit(new TypeHierarchyVisitor());
            //System.out.println("Validate control flow for " + fileName);
            rootNode.visit(new ControlFlowVisitor());
            //System.out.println("Validate self references for " + fileName);
            //System.out.println("Validate specification for " + fileName);
            for (Declaration d: unit.getDeclarations()) {
                if (d.getName()!=null) {
                    rootNode.visit(new SpecificationVisitor(d));
                    if (d instanceof TypeDeclaration) {
                        TypeDeclaration td = 
                                (TypeDeclaration) d;
                        rootNode.visit(new SelfReferenceVisitor(td));
                    }
                }
            }
            flowAnalyzed = true;
        }
    }

    public synchronized void analyseUsage() {
        if (! usageAnalyzed) {
            ReferenceCounter rc = new ReferenceCounter();
            rootNode.visit(rc);
            rootNode.visit(new UsageVisitor(rc));
            rootNode.visit(new DeprecationVisitor());
            usageAnalyzed = true;
        }
    }

    public void generateStatistics(StatisticsVisitor statsVisitor) {
        rootNode.visit(statsVisitor);
    }
    
    public void runAssertions(AssertionVisitor av) {
        //System.out.println("Running assertions for " + fileName);
        rootNode.visit(av);
    }

    public void display() {
        System.out.println("Displaying " + fileName);
        rootNode.visit(new PrintVisitor());
    }
    
    public Package getPackage() {
        return pkg;
    }
    
    public Unit getUnit() {
        return unit;
    }

    public List<Declaration> getDeclarations() {
        if (!declarationsScanned) {
            scanDeclarations();
        }
        return unit.getDeclarations();
    }

    public String getPathRelativeToSrcDir() {
        return pathRelativeToSrcDir;
    }

    public VirtualFile getUnitFile() {
        return unitFile;
    }

    @Override
    public String toString() {
        return new StringBuilder()
            .append("PhasedUnit")
            .append("[filename=").append(fileName)
            .append(", compilationUnit=").append(unit)
            .append(", pkg=").append(pkg)
            .append(']')
            .toString();
    }

    public Tree.CompilationUnit getCompilationUnit() {
        return rootNode;
    }

    public List<CommonToken> getTokens() {
        return tokens;
    }

    public boolean isScanningDeclarations() {
        return scanningDeclarations;
    }

    public void setSuppressedWarnings(EnumSet<Warning> suppressedWarnings) {
        this.suppressedWarnings = suppressedWarnings;
    }
    
    public EnumSet<Warning> getSuppressedWarnings() {
        return this.suppressedWarnings;
    }
}
