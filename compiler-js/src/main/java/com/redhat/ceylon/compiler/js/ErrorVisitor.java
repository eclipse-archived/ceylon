package com.redhat.ceylon.compiler.js;

import com.redhat.ceylon.compiler.typechecker.analyzer.UsageWarning;
import com.redhat.ceylon.compiler.typechecker.tree.Message;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

public class ErrorVisitor extends Visitor {

    private boolean flag = false;
    private int checking;

    public boolean hasErrors(Node that) {
        flag = false;
        checking=0;
        that.visit(this);
        return flag;
    }

    private void check(Node that) {
        if (that.getErrors() != null && !that.getErrors().isEmpty()) {
            for (Message err : that.getErrors()) {
                if (!(err instanceof UsageWarning)) {
                    flag = true;
                    return;
                }
            }
        }
    }

    @Override
    public void visitAny(Node that) {
        if (checking>0)check(that);
        super.visitAny(that);
    }

    public void visit(Tree.TypedDeclaration that) {
        checking++;
        check(that);
        super.visit(that);
        checking--;
    }
    public void visit(Tree.TypeDeclaration that) {
        checking++;
        super.visit(that);
        checking--;
    }

}
