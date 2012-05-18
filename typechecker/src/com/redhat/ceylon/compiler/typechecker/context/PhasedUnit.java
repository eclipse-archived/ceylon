package com.redhat.ceylon.compiler.typechecker.context;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.antlr.runtime.CommonToken;

import com.redhat.ceylon.compiler.typechecker.analyzer.ControlFlowVisitor;
import com.redhat.ceylon.compiler.typechecker.analyzer.DeclarationVisitor;
import com.redhat.ceylon.compiler.typechecker.analyzer.DependedUponVisitor;
import com.redhat.ceylon.compiler.typechecker.analyzer.ExpressionVisitor;
import com.redhat.ceylon.compiler.typechecker.analyzer.ModuleManager;
import com.redhat.ceylon.compiler.typechecker.analyzer.ModuleVisitor;
import com.redhat.ceylon.compiler.typechecker.analyzer.RefinementVisitor;
import com.redhat.ceylon.compiler.typechecker.analyzer.SelfReferenceVisitor;
import com.redhat.ceylon.compiler.typechecker.analyzer.SpecificationVisitor;
import com.redhat.ceylon.compiler.typechecker.analyzer.TypeArgumentVisitor;
import com.redhat.ceylon.compiler.typechecker.analyzer.TypeHierarchyVisitor;
import com.redhat.ceylon.compiler.typechecker.analyzer.TypeVisitor;
import com.redhat.ceylon.compiler.typechecker.analyzer.ValueVisitor;
import com.redhat.ceylon.compiler.typechecker.io.VirtualFile;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.Setter;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.Unit;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Validator;
import com.redhat.ceylon.compiler.typechecker.util.AssertionVisitor;
import com.redhat.ceylon.compiler.typechecker.util.PrintVisitor;
import com.redhat.ceylon.compiler.typechecker.util.StatisticsVisitor;

/**
 * Represent a unit and each of the type checking phases
 *
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class PhasedUnit {
    private Tree.CompilationUnit compilationUnit;
    private Package pkg;
    private Unit unit;
    //must be the non qualified file name
    private String fileName;
    private final ModuleManager moduleManager;
    private final String pathRelativeToSrcDir;
    private VirtualFile unitFile;
    private final Set<PhasedUnit> dependentsOf = new HashSet<PhasedUnit>();
    private List<CommonToken> tokens;
    private boolean fullyTyped;
    private ModuleVisitor moduleVisitor;
    private VirtualFile srcDir;

    public VirtualFile getSrcDir() {
        return srcDir;
    }

    public PhasedUnit(VirtualFile unitFile, VirtualFile srcDir, Tree.CompilationUnit cu, 
            Package p, ModuleManager moduleManager, Context context, List<CommonToken> tokenStream) {
        this.compilationUnit = cu;
        this.pkg = p;
        this.unitFile = unitFile;
        this.srcDir = srcDir;
        this.fileName = unitFile.getName();
        this.pathRelativeToSrcDir = computeRelativePath(unitFile, srcDir);
        this.moduleManager = moduleManager;
        this.tokens = tokenStream;
    }

    @Deprecated
    protected PhasedUnit(VirtualFile unitFile, VirtualFile srcDir, Tree.CompilationUnit cu, 
            Package p, ModuleManager moduleManager, Context context) {
        this(unitFile, srcDir, cu, p, moduleManager, context, null);
    }
    
    private String computeRelativePath(VirtualFile unitFile, VirtualFile srcDir) {
        final String rawRelativePath = unitFile.getPath().substring( srcDir.getPath().length() );
        if ( rawRelativePath.startsWith("/") ) {
            return rawRelativePath.substring(1);
        }
        else if ( rawRelativePath.startsWith("!/") ) {
            return rawRelativePath.substring(2);
        }
        else {
            return rawRelativePath;
        }
    }

    public Module visitSrcModulePhase() {
        if ( ModuleManager.MODULE_FILE.equals(fileName) ||
                ModuleManager.PACKAGE_FILE.equals(fileName) ) {
            moduleVisitor = new ModuleVisitor(moduleManager, pkg);
            compilationUnit.visit(moduleVisitor);
            return moduleVisitor.getMainModule();
        }
        return null;
    }

    public void visitRemainingModulePhase() {
        if ( moduleVisitor != null ) {
            moduleVisitor.setPhase(ModuleVisitor.Phase.REMAINING);
            compilationUnit.visit(moduleVisitor);
        }
    }
    
    public boolean isFullyTyped() {
        return fullyTyped;
    }

    public void validateTree() {
        //System.out.println("Validating tree for " + fileName);
        compilationUnit.visit(new Validator());
    }

    public void scanDeclarations() {
        //System.out.println("Scan declarations for " + fileName);
        DeclarationVisitor dv = new DeclarationVisitor(pkg, fileName);
        compilationUnit.visit(dv);
        unit = dv.getCompilationUnit();
    }

    public void scanTypeDeclarations() {
        //System.out.println("Scan type declarations for " + fileName);
        compilationUnit.visit( new TypeVisitor() );
    }

    public void validateRefinement() {
        //System.out.println("Validate member refinement for " + fileName);
        compilationUnit.visit(new RefinementVisitor());
    }

    public void analyseTypes() {
        //System.out.println("Run analysis phase for " + fileName);
        compilationUnit.visit(new ExpressionVisitor());
        compilationUnit.visit(new TypeArgumentVisitor());
        compilationUnit.visit(new TypeHierarchyVisitor());
        fullyTyped = true;
    }

    public void collectUnitDependencies(PhasedUnits phasedUnits, List<PhasedUnits> phasedUnitsOfDependencies) {
        //System.out.println("Run collecting unit dependencies phase for " + fileName);
        compilationUnit.visit(new DependedUponVisitor(this, phasedUnits, phasedUnitsOfDependencies));
    }
    
    public void analyseFlow() {
        //System.out.println("Validate control flow for " + fileName);
        compilationUnit.visit(new ControlFlowVisitor());
        //System.out.println("Validate self references for " + fileName);
        //System.out.println("Validate specification for " + fileName);
        for (Declaration d: unit.getDeclarations()) {
            compilationUnit.visit(new SpecificationVisitor(d));
            if (d instanceof TypedDeclaration && !(d instanceof Setter)) {
                compilationUnit.visit(new ValueVisitor((TypedDeclaration) d));
            }
            else if (d instanceof TypeDeclaration) {
                compilationUnit.visit(new SelfReferenceVisitor((TypeDeclaration) d));
            }
        }
    }

    public void generateStatistics(StatisticsVisitor statsVisitor) {
        compilationUnit.visit(statsVisitor);
    }
    
    public void runAssertions(AssertionVisitor av) {
        //System.out.println("Running assertions for " + fileName);
        compilationUnit.visit(av);
    }

    public void display() {
        System.out.println("Displaying " + fileName);
        compilationUnit.visit(new PrintVisitor());
    }
    
    public Package getPackage() {
        return pkg;
    }
    
    public Unit getUnit() {
        return unit;
    }

    public String getPathRelativeToSrcDir() {
        return pathRelativeToSrcDir;
    }

    public VirtualFile getUnitFile() {
        return unitFile;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("PhasedUnit");
        sb.append("{filename=").append(fileName);
        sb.append(", compilationUnit=").append(unit);
        sb.append(", pkg=").append(pkg);
        sb.append('}');
        return sb.toString();
    }

    public Tree.CompilationUnit getCompilationUnit() {
        return compilationUnit;
    }

    /**
     * @return the dependentsOf
     */
    public Set<PhasedUnit> getDependentsOf() {
        return dependentsOf;
    }
    
    public List<CommonToken> getTokens() {
        return tokens;
    }

}
