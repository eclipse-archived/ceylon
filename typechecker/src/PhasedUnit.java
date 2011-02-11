import com.redhat.ceylon.compiler.analyzer.ControlFlowVisitor;
import com.redhat.ceylon.compiler.analyzer.DeclarationVisitor;
import com.redhat.ceylon.compiler.analyzer.ExpressionVisitor;
import com.redhat.ceylon.compiler.analyzer.SpecificationVisitor;
import com.redhat.ceylon.compiler.analyzer.TypeVisitor;
import com.redhat.ceylon.compiler.model.Declaration;
import com.redhat.ceylon.compiler.model.Method;
import com.redhat.ceylon.compiler.model.Package;
import com.redhat.ceylon.compiler.model.SimpleValue;
import com.redhat.ceylon.compiler.model.Unit;
import com.redhat.ceylon.compiler.tree.Tree;
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

    public PhasedUnit(Tree.CompilationUnit cu, Package p) {
        this.compilationUnit = cu;
        this.pkg = p;
    }

    void scanDeclarations() {
        DeclarationVisitor dv = new DeclarationVisitor(pkg);
        compilationUnit.visit(dv);
        unit = dv.getCompilationUnit();
    }

    void scanTypeDeclarations() {
        compilationUnit.visit( new TypeVisitor(unit) );
    }

    public void analyseTypes() {
        compilationUnit.visit(new ExpressionVisitor());
    }
    
    public void validateControlFlow() {
        compilationUnit.visit(new ControlFlowVisitor());
    }

    public void validateSpecification() {
        for (Declaration d: unit.getDeclarations()) {
            if (d instanceof SimpleValue) {
                if (d.getTreeNode() instanceof Tree.AttributeDeclaration) {
                    Tree.AttributeDeclaration ad = (Tree.AttributeDeclaration) d.getTreeNode();
                    compilationUnit.visit(new SpecificationVisitor((SimpleValue) d,
                            ad.getSpecifierOrInitializerExpression()!=null));
                }
                else {
                    //control structure "variables" always come with specifiers
                    compilationUnit.visit(new SpecificationVisitor((SimpleValue) d, true));
                }
            }
            if (d instanceof Method) {
                Tree.MethodDeclaration ad = (Tree.MethodDeclaration) d.getTreeNode();
                compilationUnit.visit(new SpecificationVisitor((Method) d,
                        ad.getSpecifierExpression()!=null || ad.getBlock()!=null));
            }
        }
    }

    public void display() {
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
