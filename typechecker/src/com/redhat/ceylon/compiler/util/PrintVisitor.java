package com.redhat.ceylon.compiler.util;

import com.redhat.ceylon.compiler.tree.Node;
import com.redhat.ceylon.compiler.tree.Visitor;


public class PrintVisitor extends Visitor {
    int depth=0;

    protected void print(String str) {
        System.out.print(str);
    }

    void newline() {
        print("\n");
    }

    void indent() {
        for (int i = 0; i < depth; i++)
            print("|  ");
    }

    @Override
    public void visitAny(Node node) {
        if (depth>0) newline();
        indent();
        print("+ ");
        print(node);
        depth++;
        super.visitAny(node);
        depth--;
        if (depth==0) newline();
    }

    private void print(Node node) {
        print(node.getText() + 
                " [" + node.getClass().getSimpleName() + 
                "] (" + node.getAntlrTreeNode().getLine() + 
                ":" + node.getAntlrTreeNode().getCharPositionInLine()  + 
                ")");
        if (node.getTypeModel()!=null) {
            print(" -> " + node.getTypeModel().getProducedTypeName());
        }
        if (!node.getErrors().isEmpty()) {
            print(" <-- ** " + node.getErrors() + " **");
        }
    }
}