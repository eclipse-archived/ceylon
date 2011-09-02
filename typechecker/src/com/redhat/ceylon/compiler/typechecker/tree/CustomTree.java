package com.redhat.ceylon.compiler.typechecker.tree;

import java.util.ArrayList;
import java.util.List;

import org.antlr.runtime.Token;

public class CustomTree extends Tree {
    
    public static class AttributeDeclaration 
            extends Tree.AttributeDeclaration {
    	public AttributeDeclaration(Token token) {
    		super(token);
    	}
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
    }
    
    public static class MethodDeclaration 
            extends Tree.MethodDeclaration {
        public MethodDeclaration(Token token) {
        	super(token);
        }
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
    }
    
    public static class MethodDefinition 
            extends Tree.MethodDefinition {
        public MethodDefinition(Token token) {
        	super(token);
        }
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
    }
    
    public static class ClassDefinition 
            extends Tree.ClassDefinition {
        public ClassDefinition(Token token) {
        	super(token);
        }
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
    }
    
    public static class ValueParameterDeclaration 
            extends Tree.ValueParameterDeclaration {
        public ValueParameterDeclaration(Token token) {
        	super(token);
        }
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
            return ValueParameterDeclaration.class.getSimpleName();
        }
    }
    
    public static class FunctionalParameterDeclaration 
            extends Tree.FunctionalParameterDeclaration {
        public FunctionalParameterDeclaration(Token token) {
        	super(token);
        }
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
            return FunctionalParameterDeclaration.class.getSimpleName();
        }
    }
    
    public static class UnionType 
            extends Tree.UnionType {
        public UnionType(Token token) {
        	super(token);
        }
        @Override public String getNodeType() {
            return UnionType.class.getSimpleName();
        }
        @Override
        protected List<Node> getChildren() {
        	return new ArrayList<Node>(getStaticTypes());
        }
    }
    
    public static class IntersectionType 
            extends Tree.IntersectionType {
        public IntersectionType(Token token) {
        	super(token);
        }
        @Override public String getNodeType() {
            return IntersectionType.class.getSimpleName();
        }
        @Override
        protected List<Node> getChildren() {
        	return new ArrayList<Node>(getStaticTypes());
        }
    }
    
    public static class ExpressionList 
            extends Tree.ExpressionList {
        public ExpressionList(Token token) {
        	super(token);
        }
        @Override public String getNodeType() {
            return SequencedArgument.class.getSimpleName();
        }
        @Override
        protected List<Node> getChildren() {
        	return new ArrayList<Node>(getExpressions());
        }
    }
    
    public static class ImportList 
            extends Tree.ImportList {
        public ImportList(Token token) {
            super(token);
        }
        @Override public String getNodeType() {
            return ImportList.class.getSimpleName();
        }
        @Override
        protected List<Node> getChildren() {
            return new ArrayList<Node>(getImports());
        }
    }
    
    public static class ExtendedTypeExpression 
            extends Tree.ExtendedTypeExpression {
        public ExtendedTypeExpression(Token token) {
        	super(token);
        }
        @Override public String getNodeType() {
            return ExtendedTypeExpression.class.getSimpleName();
        }
        public void setExtendedType(SimpleType type) {
        	getChildren().add(type);
        }
    }
    
    //deliberately don't do this one, so that the span of 
    //a declaration doesn't include its annotations (but
    //is that really what we want??
    /*public static class AnnotationList extends Tree.AnnotationList {
        public AnnotationList(Token token) {
        	super(token);
        }
        @Override public String getNodeType() {
            return SequencedArgument.class.getSimpleName();
        }
        @Override
        protected List<Node> getChildren() {
        	return new ArrayList<Node>(getAnnotations());
        }
    }*/
    
    public static class ImportPath 
            extends Tree.ImportPath {
        public ImportPath(Token token) {
            super(token);
        }
        @Override public String getNodeType() {
            return ImportPath.class.getSimpleName();
        }
        @Override
        protected List<Node> getChildren() {
            return new ArrayList<Node>(getIdentifiers());
        }
    }

}
