package com.redhat.ceylon.compiler.tree;

import java.io.Writer;
import java.io.IOException;

public class CeylonTreePrinter extends CeylonTree.Visitor {
    private Writer out;
    private int depth;

    public CeylonTreePrinter(Writer out) {
        this.out = out;
    }

    private void print(String str) {
        try {
            out.append(str);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void newline() {
        print("\n");
    }

    private void indent() {
        for (int i = 0; i < depth; i++)
            print("  ");
    }

    public void visitDefault(CeylonTree tree) {
        int child_count = tree.children.size();
        if (child_count != 0) {
            newline();
            indent();
            print("(" + tree.getClassName());
            depth++;
            for (CeylonTree child : tree.children) {
                child.accept(this);
            }
            print(")");
            depth--;
        } else {
            print(" " + tree.getClassName());
        }
    }
}
