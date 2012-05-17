package com.redhat.ceylon.compiler.js;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.compiler.typechecker.model.DeclarationWithProximity;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

/** A visitor that can return a list of suggestions given a location on the AST.
 * 
 * @author Enrique Zamudio
 */
public class AutocompleteVisitor extends Visitor {

    private final int row;
    private final int col;
    private Node node;

    /** Create a new instance that will look for suggestions for the node at the specified location. */
    public AutocompleteVisitor(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() { return row; }
    public int getColumn() { return col; }

    @Override
    public void visitAny(Node that) {
        if (that.getToken().getLine() == row) {
            final int col0 = that.getToken().getCharPositionInLine();
            final int col1 = Math.max(col0+that.getText().length()-1, col0);
            if (col >= col0 && col <= col1) {
                node = that;
            }
        }
        super.visitAny(that);
    }

    /** Returns the node containing the specified location, if any. */
    public Node getNodeAtLocation() {
        return node;
    }

    public List<String> getCompletions() {
        if (node != null) {
            Map<String, DeclarationWithProximity> comps = node.getScope().getMatchingDeclarations(node.getUnit(), node.getText(), 1);
            for (Map.Entry<String, DeclarationWithProximity> e : comps.entrySet()) {
                System.out.println("Completion key: " + e.getKey());
                System.out.printf("Completion decl name %s prox %d%n", e.getValue().getName(), e.getValue().getProximity());
            }
            return Arrays.asList(comps.keySet().toArray(new String[0]));
        }
        return Collections.emptyList();
    }

}
