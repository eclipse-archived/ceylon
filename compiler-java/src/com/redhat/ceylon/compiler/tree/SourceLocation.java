package com.redhat.ceylon.compiler.tree;

import org.antlr.runtime.tree.Tree;

public class SourceLocation {
    public String path;
    public int line;
    public int column;

    private SourceLocation(Tree src, String path) {
        this.path = path;
        line = src.getLine();
        column = src.getCharPositionInLine();
   }

    static public SourceLocation instance (Tree src, String path) {
        if (src.getLine() != 0) {
            return new SourceLocation(src, path) ;
        } else {
            return null;
        }
    }
        
    public String toString() {
        return path + ":" + line;
    }
}
