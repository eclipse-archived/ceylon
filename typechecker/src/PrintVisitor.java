import com.redhat.ceylon.compiler.tree.Node;
import com.redhat.ceylon.compiler.tree.Visitor;


public class PrintVisitor extends Visitor {
	int depth=0;

	void print(String str) {
		System.out.print(str);
	}

	void newline() {
	    print("\n");
	}

	void indent() {
	    for (int i = 0; i < depth; i++)
	        print("|  ");
	}

	@Override
	public void visitAny(Node node) {
	    if (depth>0) newline();
	    indent();
	    print("+ ");
	    print(node.getText() + " (" + node.getTreeNode().getLine() + ":" + node.getTreeNode().getCharPositionInLine()  + ")");
	    depth++;
	    super.visitAny(node);
	    depth--;
	    if (depth==0) newline();
	}
}