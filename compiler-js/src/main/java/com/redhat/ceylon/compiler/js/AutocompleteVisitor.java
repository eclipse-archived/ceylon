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
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.model.typechecker.model.DeclarationWithProximity;
import com.redhat.ceylon.model.typechecker.model.ProducedType;

/** A visitor that can return a list of suggestions given a location on the AST.
 * 
 * @author Enrique Zamudio
 */
public class AutocompleteVisitor {

    private final int row;
    private final int col;
    protected final TypeChecker checker;
    private String text;
    private Node node;

    /** Create a new instance that will look for suggestions for the node at the specified location. */
    public AutocompleteVisitor(int row, int col, TypeChecker tc) {
        this.row = row;
        this.col = col;
        checker = tc;
    }

    public int getRow() { return row; }
    public int getColumn() { return col; }

    public Node findNode(AutocompleteUnitValidator callback) {
        //First pass, to find the identifier
        for (PhasedUnit pu : checker.getPhasedUnits().getPhasedUnits()) {
            if (callback.processUnit(pu)) {
                pu.getCompilationUnit().visit(new FindIdentifierVisitor());
            }
        }
        //Second pass, to find its parent
        if (node != null) {
            for (PhasedUnit pu : checker.getPhasedUnits().getPhasedUnits()) {
                if (callback.processUnit(pu)) {
                    pu.getCompilationUnit().visit(new FindParentVisitor());
                }
            }
        }
        return node;
    }

    public Node findNode() {
        return findNode(new DefaultAutocompleteUnitValidator());
    }

    /** Returns the node containing the specified location, if any. */
    public Node getNodeAtLocation() {
        return node;
    }
    /** Returns the text belonging to the node at the specified location. */
    public String getTextAtLocation() {
        return text;
    }

    /** Looks for matching declarations in the specified phased unit, recursively navigating through its dependent units. */
    protected void addCompletions(Map<String, DeclarationWithProximity> comps, Set<PhasedUnit> units,
            Set<com.redhat.ceylon.model.typechecker.model.Package> packs, PhasedUnit pu) {
        if (!packs.contains(pu.getPackage())) {
            Map<String, DeclarationWithProximity> c2 = pu.getPackage().getMatchingDeclarations(node.getUnit(), text, 100);
            comps.putAll(c2);
            packs.add(pu.getPackage());
        }
        if (!units.contains(pu)) {
            Map<String, DeclarationWithProximity> c2 = node.getScope().getMatchingDeclarations(pu.getUnit(), text, 100);
            comps.putAll(c2);
            units.add(pu);
        }
        /* COMMENTING OUT until I figure out if I really need to do this and how to get the units by name/path
         * for (String sub : pu.getUnit().getDependentsOf()) {
            addCompletions(comps, units, packs, sub);
        }*/
    }

    /** Looks for declarations matching the node's text and returns them as strings. */
    public List<String> getCompletions() {
        Map<String, DeclarationWithProximity> comps = new HashMap<String, DeclarationWithProximity>();
        if (node != null) {
            HashSet<PhasedUnit> units = new HashSet<PhasedUnit>();
            HashSet<com.redhat.ceylon.model.typechecker.model.Package> packs = new HashSet<>();
            if (node instanceof Tree.QualifiedMemberExpression) {
                final Tree.QualifiedMemberExpression exp = (Tree.QualifiedMemberExpression)node;
                ProducedType type = exp.getPrimary().getTypeModel();
                Map<String, DeclarationWithProximity> c2 = type.getDeclaration().getMatchingMemberDeclarations(
                        node.getUnit(), null, text, 0);
                comps.putAll(c2);
            } else {
                for (PhasedUnits pus : checker.getPhasedUnitsOfDependencies()) {
                    for (PhasedUnit pu : pus.getPhasedUnits()) {
                        addCompletions(comps, units, packs, pu);
                    }
                }
            }
        }
        return Arrays.asList(comps.keySet().toArray(new String[0]));
    }

    /** Callbacks can implement this to tell the visitor if a unit should be processed or not. */
    public interface AutocompleteUnitValidator {
        public boolean processUnit(PhasedUnit pu);
    }

    protected class DefaultAutocompleteUnitValidator implements AutocompleteUnitValidator {
        @Override
        public boolean processUnit(PhasedUnit pu) {
            return true;
        }
    }

    protected class FindIdentifierVisitor extends Visitor {
        /** Checks if the identifier contains the location we're interested in. */
        @Override
        public void visit(final Tree.Identifier that) {
            if (that.getToken().getLine() == row) {
                final int col0 = that.getToken().getCharPositionInLine();
                final int col1 = Math.max(col0+that.getText().length()-1, col0);
                if (col >= col0 && col <= col1) {
                    node = that;
                    text = node.getText();
                }
            }
            super.visit(that);
        }
    }

    protected class FindParentVisitor extends Visitor {
        boolean found;
        public void visitAny(Node node) {
            if (found) return;
            super.visitAny(node);
        }
        @Override
        public void visit(final Tree.StaticMemberOrTypeExpression that) {
            if (found)return;
            if (that.getIdentifier() == node) {
                node = that;
                found = true;
                return;
            }
            super.visit(that);
        }
        public void visit(final Tree.ImportMemberOrType that) {
            if (found)return;
            if (that.getIdentifier() == node) {
                node = that;
                found = true;
                return;
            }
            super.visit(that);
        }
        public void visit(final Tree.Alias that) {
            if (found)return;
            if (that.getIdentifier() == node) {
                node = that;
                found = true;
                return;
            }
            super.visit(that);
        }
        public void visit(final Tree.Declaration that) {
            if (found)return;
            if (that.getIdentifier() == node) {
                node = that;
                found = true;
                return;
            }
            super.visit(that);
        }
        public void visit(final Tree.InitializerParameter that) {
            if (found)return;
            if (that.getIdentifier() == node) {
                node = that;
                found = true;
                return;
            }
            super.visit(that);
        }
        public void visit(final Tree.SimpleType that) {
            if (found)return;
            if (that.getIdentifier() == node) {
                node = that;
                found = true;
                return;
            }
            super.visit(that);
        }
        public void visit(final Tree.MemberLiteral that) {
            if (found)return;
            if (that.getIdentifier() == node) {
                node = that;
                found = true;
                return;
            }
            super.visit(that);
        }
        public void visit(final Tree.SatisfiesCondition that) {
            if (found)return;
            if (that.getIdentifier() == node) {
                node = that;
                found = true;
                return;
            }
            super.visit(that);
        }
        public void visit(final Tree.NamedArgument that) {
            if (found)return;
            if (that.getIdentifier() == node) {
                node = that;
                found = true;
                return;
            }
            super.visit(that);
        }
    }

}
