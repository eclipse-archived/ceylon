package com.redhat.ceylon.compiler.java.codegen;

import com.redhat.ceylon.compiler.typechecker.tree.Tree.Body;
import com.sun.tools.javac.util.Context;

public class ClassErrorVisitor extends ErrorVisitor {

    public ClassErrorVisitor(Context context) {
        super(context);
    }

    @Override
    public void visit(Body that) {
        // don't get into the class body
    }
}
