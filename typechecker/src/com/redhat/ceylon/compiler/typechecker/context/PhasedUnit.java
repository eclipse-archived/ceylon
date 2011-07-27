package com.redhat.ceylon.compiler.typechecker.context;

import com.redhat.ceylon.compiler.typechecker.analyzer.ControlFlowVisitor;
import com.redhat.ceylon.compiler.typechecker.analyzer.DeclarationVisitor;
import com.redhat.ceylon.compiler.typechecker.analyzer.ExpressionVisitor;
import com.redhat.ceylon.compiler.typechecker.analyzer.ModuleBuilder;
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
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.Setter;
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
    private final ModuleBuilder moduleBuilder;
    private final Context context;
    private final String pathRelativeToSrcDir;

    public PhasedUnit(VirtualFile unitFile, VirtualFile srcDir, Tree.CompilationUnit cu, Package p, ModuleBuilder moduleBuilder, Context context) {
        this.compilationUnit = cu;
        this.pkg = p;
        this.fileName = unitFile.getName();
        this.pathRelativeToSrcDir = computeRelativePath(unitFile, srcDir);
        this.moduleBuilder = moduleBuilder;
        this.context = context;
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

    public void buildModuleImport() {
        if ( ModuleBuilder.MODULE_FILE.equals(fileName) ) {
            final ModuleVisitor v = new ModuleVisitor(moduleBuilder, context);
            compilationUnit.visit(v);
        }
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
        compilationUnit.visit( new TypeVisitor(unit, context) );
    }

    public void analyseTypes() {
        //System.out.println("Run analysis phase for " + fileName);
        compilationUnit.visit(new ExpressionVisitor(context));
        compilationUnit.visit(new TypeArgumentVisitor());
        compilationUnit.visit(new TypeHierarchyVisitor());
    }

    public void analyseFlow() {
        //System.out.println("Validate control flow for " + fileName);
        compilationUnit.visit(new ControlFlowVisitor());
        //System.out.println("Validate self references for " + fileName);
        compilationUnit.visit(new SelfReferenceVisitor());
        //System.out.println("Validate specification for " + fileName);
        for (Declaration d: unit.getDeclarations()) {
            compilationUnit.visit(new SpecificationVisitor(d, context));
            if (d instanceof TypedDeclaration && !(d instanceof Setter)) {
                compilationUnit.visit(new ValueVisitor((TypedDeclaration) d));
            }
        }
    }

    public void validateRefinement() {
        //System.out.println("Validate member refinement for " + fileName);
        compilationUnit.visit(new RefinementVisitor());
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

}
