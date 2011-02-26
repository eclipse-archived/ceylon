package com.redhat.ceylon.compiler.typechecker.tree;

import org.antlr.runtime.tree.CommonTree;

import com.redhat.ceylon.compiler.typechecker.tree.Tree.AttributeDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ClassDefinition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.FunctionalParameterDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.MethodDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.MethodDefinition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Parameter;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ParameterList;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ValueParameterDeclaration;

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
                    Walker.walkAnyAttribute(visitor, this);
                }
            }
            @Override public String getNodeType() {
                return AttributeDeclaration.class.getSimpleName();
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
                    if (getTypeParameterList()!=null)
                        getTypeParameterList().visit(visitor);
                    if (getTypeConstraintList()!=null)
                        getTypeConstraintList().visit(visitor);
                    Walker.walkTypedDeclaration(visitor, this);
                    for (ParameterList subnode: getParameterLists())
                        subnode.visit(visitor);
                }
            }
            @Override public String getNodeType() {
                return MethodDeclaration.class.getSimpleName();
            }
        };
        buildMethodDeclaration(treeNode, node);
        return node;
    }
    
    @Override
    public MethodDefinition buildMethodDefinition(CommonTree treeNode) {
        MethodDefinition node = new MethodDefinition(treeNode) {
            @Override
            public void visitChildren(Visitor visitor) {
                if (visitor instanceof NaturalVisitor) {
                    super.visitChildren(visitor);
                }
                else {
                    Walker.walkDeclaration(visitor, this);
                    if (getTypeParameterList()!=null)
                        getTypeParameterList().visit(visitor);
                    if (getTypeConstraintList()!=null)
                        getTypeConstraintList().visit(visitor);
                    if (getType()!=null)
                        getType().visit(visitor);
                    for (ParameterList subnode: getParameterLists())
                        subnode.visit(visitor);
                    if (getBlock()!=null)
                        getBlock().visit(visitor);
                }
            }
            @Override public String getNodeType() {
                return MethodDefinition.class.getSimpleName();
            }
        };
        buildMethodDefinition(treeNode, node);
        return node;
    }
    
    @Override
    public ClassDefinition buildClassDefinition(CommonTree treeNode) {
        ClassDefinition node = new ClassDefinition(treeNode) {
            @Override
            public void visitChildren(Visitor visitor) {
                if (visitor instanceof NaturalVisitor) {
                    Walker.walkDeclaration(visitor, this);
                    if (getTypeParameterList()!=null)
                        getTypeParameterList().visit(visitor);
                    if (getParameterList()!=null)
                        getParameterList().visit(visitor);
                    if (getCaseTypes()!=null)
                        getCaseTypes().visit(visitor);
                    if (getExtendedType()!=null)
                        getExtendedType().visit(visitor);
                    if (getSatisfiedTypes()!=null)
                        getSatisfiedTypes().visit(visitor);
                    if (getTypeConstraintList()!=null)
                        getTypeConstraintList().visit(visitor);
                    if (getClassBody()!=null)
                        getClassBody().visit(visitor);
                }
                else {
                    Walker.walkDeclaration(visitor, this);
                    if (getTypeParameterList()!=null)
                        getTypeParameterList().visit(visitor);
                    if (getTypeConstraintList()!=null)
                        getTypeConstraintList().visit(visitor);
                    if (getParameterList()!=null)
                        getParameterList().visit(visitor);
                    if (getCaseTypes()!=null)
                        getCaseTypes().visit(visitor);
                    if (getExtendedType()!=null)
                        getExtendedType().visit(visitor);
                    if (getSatisfiedTypes()!=null)
                        getSatisfiedTypes().visit(visitor);
                    if (getClassBody()!=null)
                        getClassBody().visit(visitor);
                }
            }
            @Override public String getNodeType() {
                return ClassDefinition.class.getSimpleName();
            }
        };
        buildClassDefinition(treeNode, node);
        return node;
    }
    
    @Override
    public ValueParameterDeclaration buildValueParameterDeclaration(CommonTree treeNode) {
        ValueParameterDeclaration node = new ValueParameterDeclaration(treeNode) {
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
            @Override public String getNodeType() {
                return Parameter.class.getSimpleName();
            }
        };
        buildParameter(treeNode, node);
        return node;
    }
    
    @Override
    public FunctionalParameterDeclaration buildFunctionalParameterDeclaration(CommonTree treeNode) {
        FunctionalParameterDeclaration node = new FunctionalParameterDeclaration(treeNode) {
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
                    for (ParameterList subnode: getParameterLists())
                        subnode.visit(visitor);
                }
            }
            @Override public String getNodeType() {
                return Parameter.class.getSimpleName();
            }
        };
        buildParameter(treeNode, node);
        return node;
    }
    
}
