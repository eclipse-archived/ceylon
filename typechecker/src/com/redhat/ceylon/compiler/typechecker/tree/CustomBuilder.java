package com.redhat.ceylon.compiler.typechecker.tree;

import org.antlr.runtime.tree.CommonTree;

import com.redhat.ceylon.compiler.typechecker.tree.Tree.AttributeDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.MethodDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Parameter;

public class CustomBuilder extends Builder {
    
    @Override
    public AttributeDeclaration buildAttributeDeclaration(CommonTree treeNode) {
        AttributeDeclaration node = new AttributeDeclaration(treeNode) {
            @Override
            public void visit(Visitor visitor) {
                if (visitor instanceof NaturalVisitor) {
                    super.visit(visitor);
                }
                else {
                    if (getSpecifierOrInitializerExpression()!=null)
                        getSpecifierOrInitializerExpression().visit(visitor);
                    super.visit(visitor);
                }
            }
            @Override
            public void visitChildren(Visitor visitor) {
                if (visitor instanceof NaturalVisitor) {
                    super.visitChildren(visitor);
                }
                else {
                    Walker.walkAttribute(visitor, this);
                }
            }
            
        };
        buildAttributeDeclaration(treeNode, node);
        return node;
    }
    
    @Override
    public MethodDeclaration buildMethodDeclaration(CommonTree treeNode) {
        MethodDeclaration node = new MethodDeclaration(treeNode) {
            @Override
            public void visit(Visitor visitor) {
                if (visitor instanceof NaturalVisitor) {
                    super.visit(visitor);
                }
                else {
                    if (getSpecifierExpression()!=null)
                        getSpecifierExpression().visit(visitor);
                    super.visit(visitor);
                }
            }
            @Override
            public void visitChildren(Visitor visitor) {
                if (visitor instanceof NaturalVisitor) {
                    super.visitChildren(visitor);
                }
                else {
                    Walker.walkMethod(visitor, this);
                }
            }
            
        };
        buildMethodDeclaration(treeNode, node);
        return node;
    }
    
    @Override
    public Parameter buildParameter(CommonTree treeNode) {
        Parameter node = new Parameter(treeNode) {
            @Override
            public void visit(Visitor visitor) {
                if (visitor instanceof NaturalVisitor) {
                    super.visit(visitor);
                }
                else {
                    if (getSpecifierExpression()!=null)
                        getSpecifierExpression().visit(visitor);
                    super.visit(visitor);
                }
            }
            @Override
            public void visitChildren(Visitor visitor) {
                if (visitor instanceof NaturalVisitor) {
                    super.visitChildren(visitor);
                }
                else {
                    Walker.walkTypedDeclaration(visitor, this);
                }
            }
            
        };
        buildParameter(treeNode, node);
        return node;
    }
    
}
