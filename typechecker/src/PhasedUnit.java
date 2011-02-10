import com.redhat.ceylon.compiler.analyzer.DeclarationVisitor;
import com.redhat.ceylon.compiler.analyzer.DefiniteAssignmentControllerVisitor;
import com.redhat.ceylon.compiler.analyzer.ExpressionVisitor;
import com.redhat.ceylon.compiler.analyzer.TypeVisitor;
import com.redhat.ceylon.compiler.model.*;
import com.redhat.ceylon.compiler.model.Package;
import com.redhat.ceylon.compiler.tree.Tree;
import com.redhat.ceylon.compiler.util.PrintVisitor;

/**
 * Represent a unit and each of the type checking phases
 *
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class PhasedUnit {
    private Tree.CompilationUnit compilationUnitTree;
    private Package pkg;
    private Unit compilationUnit;

    public PhasedUnit(Tree.CompilationUnit cu, Package p) {
        this.compilationUnitTree = cu;
        this.pkg = p;
    }

    void visitDeclarations() {
        DeclarationVisitor dv = new DeclarationVisitor(pkg);
        compilationUnitTree.visit(dv);
        compilationUnit = dv.getCompilationUnit();
    }

    void visitTypes() {
        compilationUnitTree.visit( new TypeVisitor(compilationUnit) );
    }

    public void visitExpressions() {
        compilationUnitTree.visit(new ExpressionVisitor());
    }
    
    public void visitAssignments() {
        compilationUnitTree.visit(new DefiniteAssignmentControllerVisitor(compilationUnitTree));
    }

    public void visitAndPrint() {
        compilationUnitTree.visit (new PrintVisitor() );
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("StagedUnit");
        sb.append("{compilationUnit=").append(compilationUnit);
        sb.append(", pkg=").append(pkg);
        sb.append('}');
        return sb.toString();
    }
}
