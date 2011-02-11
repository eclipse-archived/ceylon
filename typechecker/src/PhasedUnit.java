import com.redhat.ceylon.compiler.analyzer.DeclarationVisitor;
import com.redhat.ceylon.compiler.analyzer.LazyDeclarationVisitor;
import com.redhat.ceylon.compiler.analyzer.ControlFlowVisitor;
import com.redhat.ceylon.compiler.analyzer.ExpressionVisitor;
import com.redhat.ceylon.compiler.analyzer.TypeVisitor;
import com.redhat.ceylon.compiler.model.Package;
import com.redhat.ceylon.compiler.model.Unit;
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

    void scanDeclarations() {
        DeclarationVisitor dv = new DeclarationVisitor(pkg);
        compilationUnitTree.visit(dv);
        compilationUnit = dv.getCompilationUnit();
    }

    void scanTypeDeclarations() {
        compilationUnitTree.visit( new TypeVisitor(compilationUnit) );
    }

    public void analyseTypes() {
        compilationUnitTree.visit(new ExpressionVisitor());
    }
    
    public void validateControlFlow() {
        compilationUnitTree.visit(new ControlFlowVisitor());
    }

    public void validateSpecification() {
        compilationUnitTree.visit(new LazyDeclarationVisitor(compilationUnitTree));
    }

    public void display() {
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
