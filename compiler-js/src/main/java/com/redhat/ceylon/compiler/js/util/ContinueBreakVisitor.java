package com.redhat.ceylon.compiler.js.util;

import java.util.IdentityHashMap;

import com.redhat.ceylon.compiler.js.BlockWithCaptureVisitor;
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
    private int level;
    private boolean ignore;
    private final IdentityHashMap<Tree.Directive, Boolean> dirs = new IdentityHashMap<>();

    public ContinueBreakVisitor(Tree.Block n, JsIdentifierNames names) {
        scope = n.getScope();
        this.names = names;
        n.visit(this);
    }

    public void visit(Tree.Block that) {
        level++;
        if (level>1) {
            ignore=new BlockWithCaptureVisitor(that).hasCapture();
        }
        super.visit(that);
        ignore=false;
        level--;
    }
    public void visit(Tree.Break n) {
        if (ignore) {
            return;
        }
        if (level < 3) {
            if (bname == null) {
                bname = names.createTempVariable();
            }
            breaks = true;
            dirs.put(n, true);
        }
        super.visit(n);
    }
    public void visit(Tree.Continue n) {
        if (ignore) {
            return;
        }
        if (level < 3) {
            if (cname == null) {
                cname = names.createTempVariable();
            }
            continues = true;
            dirs.put(n, true);
        }
        super.visit(n);
    }
    public void visit(Tree.Return n) {
        if (ignore) {
            return;
        }
        if (level < 3) {
            returns = true;
        }
        super.visit(n);
    }

    public boolean belongs(Tree.Directive dir) {
        return dirs.containsKey(dir);
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
