package com.redhat.ceylon.compiler.java.codegen;

import java.util.IdentityHashMap;

import com.sun.tools.javac.tree.JCTree;

public class TreeValidator<P> extends com.sun.tools.javac.tree.TreeScanner {

    IdentityHashMap<JCTree , Object> trees = new IdentityHashMap<JCTree , Object>();

    protected void check(JCTree node) {
        if (trees.containsKey(node)) {
            new RuntimeException("not a tree! " + node + " occurs twice", node.init).printStackTrace();;
        }
        trees.put(node, null);
    }
    public void scan(JCTree tree) {
        if(tree!=null) check(tree);
        super.scan(tree);
    }
    public void visitTopLevel(JCTree.JCCompilationUnit tree) {
        super.visitTopLevel(tree);
    }

    
}
