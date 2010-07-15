package com.redhat.ceylon.compiler.tree;

import java.io.PrintWriter;
import java.io.Writer;

import com.sun.tools.javac.util.List;

public class TreePrinter extends CeylonTree.Visitor {
    private PrintWriter out;

    public TreePrinter(Writer out) {
        this.out = new PrintWriter(out);
    }

    private int depth;

    private void indent() {
        out.println();
        for (int i = 0; i < depth; i++)
            out.print("  ");
    }

    private void inner(List children) {
        for (Object child: children) {
            ((CeylonTree) child).accept(this);
        }
    }
    
    private void process(String str, Object ... children) {
        indent();
        out.print("(" + str);
        depth++;

        for (Object child: children) {
            if (child != null) {
                if (child instanceof List) {
                    inner((List) child);
                }
                else if (child instanceof CeylonTree) {
                    ((CeylonTree) child).accept(this);
                }
                else if (child instanceof String) {
                    out.print(" \"" + (String) child + '"');
                }
                else {
                    throw new RuntimeException(child.getClass().getName());
                }
            }
        }

        depth--;
        out.print(')');
    }

    public void visit(CeylonTree.CompilationUnit cu) {
        process("CompilationUnit",
                cu.importDeclarations,
                cu.interfaceDecls,
                cu.classDecls);
    }

    public void visit(CeylonTree.ClassDeclaration cd) {
        process("ClassDeclaration",
                cd.name,
                cd.params,
                cd.annotations,
                cd.stmts);
    }

    public void visit(CeylonTree.FormalParameter fp) {
        process("FormalParameter",
                fp.type);
    }

    public void visit(CeylonTree.Type t) {
        process("Type",
                t.type);
    }

    public void visit(CeylonTree.StatementList sl) {
        process("StatementList",
                sl.stmts);
    }
}
