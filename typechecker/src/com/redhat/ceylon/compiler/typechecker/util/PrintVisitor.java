package com.redhat.ceylon.compiler.typechecker.util;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.tree.NaturalVisitor;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;


public class PrintVisitor extends Visitor implements NaturalVisitor {
    
    int depth=0;
    Writer stream;

    public PrintVisitor() {
        stream = new OutputStreamWriter(System.out);
    }
    
    public PrintVisitor(Writer w) {
        stream = w;
    }
    
    protected void print(String str) {
        try {
            stream.write(str);
        }
        catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
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
        try {
            stream.flush();
        }
        catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    private void print(Node node) {
        print(node.getText() + 
                " [" + node.getNodeType() + 
                "] (" + node.getAntlrTreeNode().getLine() + 
                ":" + node.getAntlrTreeNode().getCharPositionInLine()  + 
                ")");
        if (node instanceof Tree.Primary) {
            Declaration d = ((Tree.Primary) node).getDeclaration();
            if (d!=null) {
                print("  #" + d);
            }
        }
        if (node instanceof Tree.Declaration) {
            Declaration d = ((Tree.Declaration) node).getDeclarationModel();
            if (d!=null) {
                print("  #" + d);
            }
        }
        if (node instanceof Tree.SimpleType) {
            Declaration d = ((Tree.SimpleType) node).getDeclarationModel();
            if (d!=null) {
                print("  #" + d);
            }
        }
        if (node instanceof Tree.Term) {
            ProducedType type = ((Tree.Term) node).getTypeModel();
            if (type!=null) {
                print(" -> " + type.getProducedTypeName());
            }
        }
        if (node instanceof Tree.Type) {
            ProducedType type = ((Tree.Type) node).getTypeModel();
            if (type!=null) {
                print(" -> " + type.getProducedTypeName());
            }
        }
        if (!node.getErrors().isEmpty()) {
            print(" <-- ** " + node.getErrors() + " **");
        }
    }
}