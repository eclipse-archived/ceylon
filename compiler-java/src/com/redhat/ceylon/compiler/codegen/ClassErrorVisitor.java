package com.redhat.ceylon.compiler.codegen;

import com.redhat.ceylon.compiler.typechecker.tree.Tree.Body;

public class ClassErrorVisitor extends ErrorVisitor {

    @Override
    public void visit(Body that) {
        // don't get into the class body
    }
}
