package com.redhat.ceylon.compiler.util;

import com.redhat.ceylon.compiler.tree.Node;
import com.redhat.ceylon.compiler.tree.Tree;
import com.redhat.ceylon.compiler.tree.Visitor;

public class AssertionVisitor extends Visitor {
    
    boolean expectingError = false;
    boolean foundError = false;

    @Override
    public void visit(Tree.TypedDeclaration that) {
        for (Tree.CompilerAnnotation c: that.getCompilerAnnotations()) {
            if (c.getIdentifier().getText().equals("type")) {
                String expectedType = c.getStringLiteral().getText();
                String actualType = that.getTypeOrSubtype().getTypeModel().getProducedTypeName();
                if ( !actualType.equals(expectedType.substring(1,expectedType.length()-1)) )
                    System.err.println(
                        c.getIdentifier().getText()  + 
                        " is not of type " + expectedType + 
                        " at "+ that.getAntlrTreeNode().getLine() + ":" +
                        that.getAntlrTreeNode().getCharPositionInLine());
            }
        }
        super.visit(that);
    }
    
    @Override
    public void visit(Tree.Statement that) {
        boolean b = expectingError;
        boolean f = foundError;
        expectingError = false;
        foundError = false;
        for (Tree.CompilerAnnotation c: that.getCompilerAnnotations()) {
            if (c.getIdentifier().getText().equals("error")) {
                expectingError = true;
            }
        }
        super.visit(that);
        if (expectingError!=foundError)
            System.err.println(
                (expectingError ? "no " : "") + "error encountered at " + 
                that.getAntlrTreeNode().getLine() + ":" +
                that.getAntlrTreeNode().getCharPositionInLine());
        expectingError = b;
        foundError = f;
    }
    
    @Override
    public void visitAny(Node that) {
        if ( !that.getErrors().isEmpty() )
            foundError = true;
        super.visitAny(that);
    }
    
}
