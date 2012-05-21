package com.redhat.ceylon.compiler.js;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.redhat.ceylon.compiler.typechecker.TypeChecker;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnits;
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
    private final TypeChecker checker;
    private Node node;

    /** Create a new instance that will look for suggestions for the node at the specified location. */
    public AutocompleteVisitor(int row, int col, TypeChecker tc) {
        this.row = row;
        this.col = col;
        checker = tc;
    }

    public int getRow() { return row; }
    public int getColumn() { return col; }

    public Node findNode() {
        //First pass
        for (PhasedUnit pu : checker.getPhasedUnits().getPhasedUnits()) {
            pu.getCompilationUnit().visit(this);
        }
        return node;
    }

    @Override
    public void visitAny(Node that) {
        if (node == null && !that.getText().isEmpty()) {
            if (that.getToken().getLine() == row) {
                final int col0 = that.getToken().getCharPositionInLine();
                final int col1 = Math.max(col0+that.getText().length()-1, col0);
                if (col >= col0 && col <= col1) {
                    node = that;
                }
            }
        }
        super.visitAny(that);
    }

    /** Returns the node containing the specified location, if any. */
    public Node getNodeAtLocation() {
        return node;
    }

    private void addCompletions(Map<String, DeclarationWithProximity> comps, Set<PhasedUnit> units,
            Set<com.redhat.ceylon.compiler.typechecker.model.Package> packs, PhasedUnit pu) {
        String s = node.getText();
        if (!packs.contains(pu.getPackage())) {
            Map<String, DeclarationWithProximity> c2 = pu.getPackage().getMatchingDeclarations(node.getUnit(), s, 100);
            comps.putAll(c2);
            packs.add(pu.getPackage());
        }
        if (!units.contains(pu)) {
            Map<String, DeclarationWithProximity> c2 = node.getScope().getMatchingDeclarations(pu.getUnit(), s, 100);
            comps.putAll(c2);
            units.add(pu);
        }
        for (PhasedUnit sub : pu.getDependentsOf()) {
            addCompletions(comps, units, packs, sub);
        }
    }
    /** Looks for declarations matching the node's text and returns them as strings. */
    public List<String> getCompletions() {
        Map<String, DeclarationWithProximity> comps = new HashMap<String, DeclarationWithProximity>();
        if (node != null) {
            HashSet<PhasedUnit> units = new HashSet<PhasedUnit>();
            HashSet<com.redhat.ceylon.compiler.typechecker.model.Package> packs = new HashSet<com.redhat.ceylon.compiler.typechecker.model.Package>();
            for (PhasedUnits pus : checker.getPhasedUnitsOfDependencies()) {
                for (PhasedUnit pu : pus.getPhasedUnits()) {
                    addCompletions(comps, units, packs, pu);
                }
            }
        }
        return Arrays.asList(comps.keySet().toArray(new String[0]));
    }

}
