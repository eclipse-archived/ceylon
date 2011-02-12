import com.redhat.ceylon.compiler.analyzer.ControlFlowVisitor;
import com.redhat.ceylon.compiler.analyzer.DeclarationVisitor;
import com.redhat.ceylon.compiler.analyzer.ExpressionVisitor;
import com.redhat.ceylon.compiler.analyzer.SpecificationVisitor;
import com.redhat.ceylon.compiler.analyzer.TypeVisitor;
import com.redhat.ceylon.compiler.model.Declaration;
import com.redhat.ceylon.compiler.model.Package;
import com.redhat.ceylon.compiler.model.Unit;
import com.redhat.ceylon.compiler.tree.Tree;
import com.redhat.ceylon.compiler.util.AssertionVisitor;
import com.redhat.ceylon.compiler.util.PrintVisitor;

/**
 * Represent a unit and each of the type checking phases
 *
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class PhasedUnit {
    private Tree.CompilationUnit compilationUnit;
    private Package pkg;
    private Unit unit;
    private String fileName;

    public PhasedUnit(String fileName, Tree.CompilationUnit cu, Package p) {
        this.compilationUnit = cu;
        this.pkg = p;
        this.fileName = fileName;
    }

    void scanDeclarations() {
        System.out.println("Scan declarations for " + fileName);
        DeclarationVisitor dv = new DeclarationVisitor(pkg);
        compilationUnit.visit(dv);
        unit = dv.getCompilationUnit();
    }

    void scanTypeDeclarations() {
        System.out.println("Scan type declarations for " + fileName);
        compilationUnit.visit( new TypeVisitor(unit) );
    }

    public void analyseTypes() {
        System.out.println("Run analysis phase for " + fileName);
        compilationUnit.visit(new ExpressionVisitor());
    }
    
    public void validateControlFlow() {
        System.out.println("Validate control flow for " + fileName);
        compilationUnit.visit(new ControlFlowVisitor());
    }

    public void runAssertions() {
        System.out.println("Running assertions for " + fileName);
        compilationUnit.visit(new AssertionVisitor());
    }

    public void validateSpecification() {
        System.out.println("Validate specification for " + fileName);
        //TODO: This is too strict - it does not account for cases where 
        //      a member *is* allowed to be called before it is declared!
        //      I think the only relevant cases are members of a class that
        //      occur in the declaration section, and members of interfaces.
        for (Declaration d: unit.getDeclarations()) {
            compilationUnit.visit(new SpecificationVisitor(d));
            //TODO: variable attributes (definite initialization)
        }
    }

    public void display() {
        System.out.println("Display " + fileName);
        compilationUnit.visit (new PrintVisitor() );
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("StagedUnit");
        sb.append("{compilationUnit=").append(unit);
        sb.append(", pkg=").append(pkg);
        sb.append('}');
        return sb.toString();
    }
}
