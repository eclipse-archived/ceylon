package com.redhat.ceylon.compiler.tree;

import org.antlr.runtime.tree.Tree;

public class SourceLocation {
    public String path;
    public int line;
    public int column;

    public SourceLocation(Tree src, String path) {
        this.path = path;
        this.line = src.getLine();
        this.column = src.getCharPositionInLine();
    }
    
    public String toString() {
        return path + ":" + line;
    }
}
