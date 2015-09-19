package com.redhat.ceylon.compiler.js;

import java.util.HashSet;
import java.util.Set;

import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.model.typechecker.model.Declaration;

/** Determine when a function needs to be wrapped inside another one to fix
 * the stupid closure issues of JS lack of proper scopes.
 * 
 * @author Enrique Zamudio
 */
public class ClosureHelper extends Visitor {

    private final Set<Declaration> caps = new HashSet<>();

    public static Set<Declaration> declarationsInExpression(final Tree.Expression that) {
        final ClosureHelper ch = new ClosureHelper();
        that.visit(ch);
        return ch.caps;
    }

    public static Set<Declaration> declarationsInExpression(final Tree.Term that) {
        final ClosureHelper ch = new ClosureHelper();
        that.visit(ch);
        return ch.caps;
    }

    public void visit(Tree.BaseMemberExpression that) {
        if (that.getDeclaration() != null) {
            caps.add(that.getDeclaration());
        }
        super.visit(that);
    }

}
