package com.redhat.ceylon.compiler.js.util;

import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.model.typechecker.model.Scope;

public class ContinueBreakVisitor extends Visitor {

    private boolean breaks;
    private boolean continues;
    private boolean returns;
    private final Scope scope;
    private final JsIdentifierNames names;
    private String bname;
    private String cname;
    private String rname;

    public ContinueBreakVisitor(Node n, JsIdentifierNames names) {
        scope = n.getScope();
        this.names = names;
        n.visit(this);
    }

    public void visit(Tree.Break n) {
        if (n.getScope() == scope) {
            if (bname == null) {
                bname = names.createTempVariable();
            }
            breaks = true;
        }
        super.visit(n);
    }
    public void visit(Tree.Continue n) {
        if (n.getScope() == scope) {
            if (cname == null) {
                cname = names.createTempVariable();
            }
            continues = true;
        }
        super.visit(n);
    }
    public void visit(Tree.Return n) {
        if (n.getScope() == scope) {
            returns = true;
        }
        super.visit(n);
    }

    public Scope getScope() {
        return scope;
    }
    public boolean isContinues() {
        return continues;
    }
    public boolean isReturns() {
        return returns;
    }
    public boolean isBreaks() {
        return breaks;
    }

    public String getContinueName() {
        return cname;
    }
    public String getBreakName() {
        return bname;
    }
    public String getReturnName() {
        if (rname == null) {
            rname = names.createTempVariable();
        }
        return rname;
    }
}
