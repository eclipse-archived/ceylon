package com.redhat.ceylon.compiler.util;

import java.util.ArrayList;
import java.util.List;

import com.redhat.ceylon.compiler.analyzer.AnalysisError;
import com.redhat.ceylon.compiler.model.Type;
import com.redhat.ceylon.compiler.tree.Node;
import com.redhat.ceylon.compiler.tree.Tree;
import com.redhat.ceylon.compiler.tree.Visitor;

public class AssertionVisitor extends Visitor {
    
    boolean expectingError = false;
    List<AnalysisError> foundErrors = new ArrayList<AnalysisError>();

    @Override
    public void visit(Tree.TypedDeclaration that) {
        checkType(that, that.getTypeOrSubtype().getTypeModel());
        super.visit(that);
    }

    @Override
    public void visit(Tree.ExpressionStatement that) {
        checkType(that, that.getExpression().getTypeModel());
        super.visit(that);
    }
    
    private void checkType(Tree.Statement that, Type tm) {
        for (Tree.CompilerAnnotation c: that.getCompilerAnnotations()) {
            if (c.getIdentifier().getText().equals("type")) {
                String expectedType = c.getStringLiteral().getText();
                if (tm==null) {
                    System.err.println(
                            "type not known at "+ that.getAntlrTreeNode().getLine() + ":" +
                            that.getAntlrTreeNode().getCharPositionInLine());
                }
                else {
                    String actualType = tm.getProducedTypeName();
                    if ( !actualType.equals(expectedType.substring(1,expectedType.length()-1)) )
                        System.err.println("type " + actualType +
                                "not of expected type " + expectedType + 
                                " at "+ that.getAntlrTreeNode().getLine() + ":" +
                                that.getAntlrTreeNode().getCharPositionInLine());
                }
            }
        }
    }
    
    @Override
    public void visit(Tree.Statement that) {
        boolean b = expectingError;
        List<AnalysisError> f = foundErrors;
        expectingError = false;
        foundErrors = new ArrayList<AnalysisError>();
        for (Tree.CompilerAnnotation c: that.getCompilerAnnotations()) {
            if (c.getIdentifier().getText().equals("error")) {
                expectingError = true;
            }
        }
        super.visit(that);
        if (expectingError && foundErrors.size()==0)
            System.err.println(
                "no error encountered at " + 
                that.getAntlrTreeNode().getLine() + ":" +
                that.getAntlrTreeNode().getCharPositionInLine());
        if (!expectingError && foundErrors.size()>0)
            System.err.println(
                "errors encountered at " + 
                that.getAntlrTreeNode().getLine() + ":" +
                that.getAntlrTreeNode().getCharPositionInLine() + " " +
                foundErrors);
        expectingError = b;
        foundErrors = f;
    }
    
    @Override
    public void visitAny(Node that) {
        foundErrors.addAll(that.getErrors());
        super.visitAny(that);
    }
    
}
