package com.redhat.ceylon.compiler.typechecker.context;

import com.redhat.ceylon.compiler.typechecker.analyzer.ControlFlowVisitor;
import com.redhat.ceylon.compiler.typechecker.analyzer.DeclarationVisitor;
import com.redhat.ceylon.compiler.typechecker.analyzer.ExpressionVisitor;
import com.redhat.ceylon.compiler.typechecker.analyzer.ModuleImportVisitor;
import com.redhat.ceylon.compiler.typechecker.analyzer.RefinementVisitor;
import com.redhat.ceylon.compiler.typechecker.analyzer.SpecificationVisitor;
import com.redhat.ceylon.compiler.typechecker.analyzer.TypeArgumentVisitor;
import com.redhat.ceylon.compiler.typechecker.analyzer.TypeVisitor;
import com.redhat.ceylon.compiler.typechecker.analyzer.ValueVisitor;
import com.redhat.ceylon.compiler.typechecker.io.VirtualFile;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.Setter;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.Unit;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.util.AssertionVisitor;
import com.redhat.ceylon.compiler.typechecker.util.PrintVisitor;

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
    private String path;
    private Context context;

    public PhasedUnit(VirtualFile unitFile, Tree.CompilationUnit cu, Package p, Context context) {
        this.compilationUnit = cu;
        this.pkg = p;
        this.fileName = unitFile.getName();
        this.path = unitFile.getPath();
        this.context = context;
    }
    
    public String getPath() {
        return path;
    }

    public void buildModuleImport() {
        if ( Context.MODULE_FILE.equals(fileName) ) {
            final ModuleImportVisitor v = new ModuleImportVisitor(context);
            compilationUnit.visit(v);
        }
    }

    public void scanDeclarations() {
        System.out.println("Scan declarations for " + fileName);
        DeclarationVisitor dv = new DeclarationVisitor(pkg, fileName);
        compilationUnit.visit(dv);
        unit = dv.getCompilationUnit();
    }

    public void scanTypeDeclarations() {
        System.out.println("Scan type declarations for " + fileName);
        compilationUnit.visit( new TypeVisitor(unit, context) );
    }

    public void analyseTypes() {
        System.out.println("Run analysis phase for " + fileName);
        compilationUnit.visit(new ExpressionVisitor(context));
        compilationUnit.visit(new TypeArgumentVisitor());
    }
    
    public void validateControlFlow() {
        System.out.println("Validate control flow for " + fileName);
        compilationUnit.visit(new ControlFlowVisitor());
    }

    public void validateRefinement() {
        System.out.println("Validate member refinement for " + fileName);
        compilationUnit.visit(new RefinementVisitor());
    }

    public void runAssertions() {
        System.out.println("Running assertions for " + fileName);
        compilationUnit.visit(new AssertionVisitor());
    }

    public void validateSpecification() {
        System.out.println("Validate specification for " + fileName);
        for (Declaration d: unit.getDeclarations()) {
            compilationUnit.visit(new SpecificationVisitor(d, context));
            if (d instanceof TypedDeclaration && !(d instanceof Setter)) {
                compilationUnit.visit(new ValueVisitor((TypedDeclaration) d, context));
            }
        }
    }

    public void display() {
        System.out.println("Display " + fileName);
        compilationUnit.visit(new PrintVisitor());
    }
    
    public Package getPackage() {
        return pkg;
    }
    
    public Unit getUnit() {
        return unit;
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
