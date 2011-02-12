package com.redhat.ceylon.compiler.util;

import com.redhat.ceylon.compiler.tree.Node;
import com.redhat.ceylon.compiler.tree.Tree;
import com.redhat.ceylon.compiler.tree.Visitor;

public class AssertionVisitor extends Visitor {
    
    boolean broken = false;
    boolean foundError = false;

    @Override
    public void visit(Tree.TypedDeclaration that) {
        for (Tree.CompilerAnnotation c: that.getCompilerAnnotations()) {
            if (c.getIdentifier().getText().equals("type")) {
                String expectedType = c.getStringLiteral().getText();
                String actualType = that.getTypeOrSubtype().getTypeModel().getProducedTypeName();
                assert actualType.equals(expectedType.substring(1,expectedType.length()-1)) :
                    c.getIdentifier().getText()  + " is not of type " + expectedType + 
                    " at "+ that.getAntlrTreeNode().getLine() + ":" +
                    that.getAntlrTreeNode().getCharPositionInLine();;
            }
        }
        super.visit(that);
    }
    
    @Override
    public void visit(Tree.Statement that) {
        boolean b = broken;
        boolean f = foundError;
        broken = false;
        foundError = false;
        for (Tree.CompilerAnnotation c: that.getCompilerAnnotations()) {
            if (c.getIdentifier().getText().equals("broken")) {
                broken = true;
            }
        }
        super.visit(that);
        assert broken==foundError : "broken at " + 
            that.getAntlrTreeNode().getLine() + ":" +
            that.getAntlrTreeNode().getCharPositionInLine();
        broken = b;
        foundError = f;
    }
    
    @Override
    public void visitAny(Node that) {
        if ( !that.getErrors().isEmpty() )
            foundError = true;
        super.visitAny(that);
    }

    
}
