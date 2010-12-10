package com.redhat.ceylon.compiler.tree;

import org.antlr.runtime.tree.Tree;

public class SourceLocation {
    public String path;
    public int line;
    public int column;

    // This is a heuristic.  Synthetic tokens coming from ANTLR aren't
    // given a line number, so we use the last line number we saw.  We
    // don't have a clue about the column, so we set that to 0.
    static int lastKnownLine;

    private SourceLocation(Tree src, String path) {
        this.path = path;
        line = src.getLine();
        lastKnownLine = line;
        column = src.getCharPositionInLine();
    }

    private SourceLocation(int line, String path) {
        this.path = path;
        this.line = line;
        this.column = 0;
    }

    static public SourceLocation instance (Tree src, String path) {
        if (src.getLine() != 0) {
            return new SourceLocation(src, path) ;
        } else {
            return new SourceLocation(lastKnownLine, path) ;
        }
    }

    public String toString() {
        return path + ":" + line;
    }

    public static void reset() {
        lastKnownLine = 1;
    }
}
