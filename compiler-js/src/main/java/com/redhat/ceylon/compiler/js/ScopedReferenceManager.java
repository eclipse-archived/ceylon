package com.redhat.ceylon.compiler.js;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AttributeDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.BaseMemberExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Term;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Variable;

/** A component used by the GenerateJsVisitor to keep track of temporary variable names
 * that should be used instead of getter functions, depending on the scope of the function.
 * 
 * @author Enrique Zamudio
 */
public class ScopedReferenceManager {
    private final IdentityHashMap<Scope, Map<String, String>> scopeVars = new IdentityHashMap<Scope, Map<String, String>>();

    public void store(AttributeDeclaration ad, String tmpvar) {
        Map<String, String> sv = scopeVars.get(ad.getScope());
        if (sv == null) {
            sv = new HashMap<String, String>();
            scopeVars.put(ad.getScope(), sv);
        }
        sv.put(ad.getDeclarationModel().getName(), tmpvar);
    }

    public void store(Variable ad, String tmpvar) {
        Map<String, String> sv = scopeVars.get(ad.getScope());
        if (sv == null) {
            sv = new HashMap<String, String>();
            scopeVars.put(ad.getScope(), sv);
        }
        sv.put(ad.getDeclarationModel().getName(), tmpvar);
    }

    private String findVar(String var, Scope scope) {
        Map<String, String> m = scopeVars.get(scope);
        String r = m==null?null:m.get(var);
        while (r == null && scope != null) {
            scope = scope.getContainer();
            m = scope==null ? null : scopeVars.get(scope);
            r = m==null ? null : m.get(var);
        }
        return r;
    }
    public String get(BaseMemberExpression expr) {
        return findVar(expr.getDeclaration().getName(), expr.getScope());
    }
    public String get(Term term) {
        return findVar(term.getText(), term.getScope());
    }

    @Override
    public String toString() {
        return scopeVars.toString();
    }
}
