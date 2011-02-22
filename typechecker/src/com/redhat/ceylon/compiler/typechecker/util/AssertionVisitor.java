package com.redhat.ceylon.compiler.typechecker.util;

import java.util.ArrayList;
import java.util.List;

import com.redhat.ceylon.compiler.typechecker.analyzer.AnalysisError;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

public class AssertionVisitor extends Visitor {
    
    boolean expectingError = false;
    List<AnalysisError> foundErrors = new ArrayList<AnalysisError>();

    @Override
    public void visit(Tree.TypedDeclaration that) {
        checkType(that, that.getTypeOrSubtype());
        super.visit(that);
    }

    @Override
    public void visit(Tree.ExpressionStatement that) {
        checkType(that, that.getExpression());
        super.visit(that);
    }
    
    private void checkType(Tree.Statement that, Node typedNode) {
        for (Tree.CompilerAnnotation c: that.getCompilerAnnotations()) {
            if (c.getIdentifier().getText().equals("type")) {
                String expectedType = c.getStringLiteral().getText();
                if (typedNode==null || typedNode.getTypeModel()==null || 
                        typedNode.getTypeModel().getDeclaration()==null) {
                    System.err.println(
                            "type not known at "+ that.getAntlrTreeNode().getLine() + ":" +
                            that.getAntlrTreeNode().getCharPositionInLine() + " of " +
                            that.getUnit().getFilename());
                }
                else {
                    String actualType = typedNode.getTypeModel().getProducedTypeName();
                    if ( !actualType.equals(expectedType.substring(1,expectedType.length()-1)) )
                        System.err.println("type " + actualType +
                                " not of expected type " + expectedType + 
                                " at "+ that.getAntlrTreeNode().getLine() + ":" +
                                that.getAntlrTreeNode().getCharPositionInLine() + " of " +
                                that.getUnit().getFilename());
                }
            }
        }
    }
    
    @Override
    public void visit(Tree.StatementOrArgument that) {
        boolean b = expectingError;
        List<AnalysisError> f = foundErrors;
        expectingError = false;
        foundErrors = new ArrayList<AnalysisError>();
        initExpectingError(that);
        super.visit(that);
        checkErrors(that);
        expectingError = b;
        foundErrors = f;
    }

    private void checkErrors(Node that) {
        if (expectingError && foundErrors.size()==0)
            System.err.println(
                "no error encountered at " + 
                that.getAntlrTreeNode().getLine() + ":" +
                that.getAntlrTreeNode().getCharPositionInLine() + " of " +
                that.getUnit().getFilename());
        if (!expectingError && foundErrors.size()>0)
            System.err.println(
                "errors encountered at " + 
                that.getAntlrTreeNode().getLine() + ":" +
                that.getAntlrTreeNode().getCharPositionInLine() + " of " +
                that.getUnit().getFilename() + " " +
                foundErrors);
    }

    private void initExpectingError(Tree.StatementOrArgument that) {
        for (Tree.CompilerAnnotation c: that.getCompilerAnnotations()) {
            if (c.getIdentifier().getText().equals("error")) {
                expectingError = true;
            }
        }
    }
    
    @Override
    public void visitAny(Node that) {
        foundErrors.addAll(that.getErrors());
        super.visitAny(that);
    }
    
}
