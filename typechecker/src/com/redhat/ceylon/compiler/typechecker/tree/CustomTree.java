package com.redhat.ceylon.compiler.typechecker.tree;

import java.util.List;

import org.antlr.runtime.Token;

public class CustomTree extends Tree {
    
    public static class FunctionArgument 
            extends Tree.FunctionArgument {
        public FunctionArgument(Token token) {
            super(token);
        }
        @Override
        public void visitChildren(Visitor visitor) {
            if (getType()!=null)
                getType().visit(visitor);
            List<ParameterList> parameterLists = getParameterLists();
            for (int i=0,l=parameterLists.size();i<l;i++)
                parameterLists.get(i).visit(visitor);
            if (getExpression()!=null)
                getExpression().visit(visitor);
            if (getBlock()!=null)
                getBlock().visit(visitor);
        }
        @Override public String getNodeType() {
            return FunctionArgument.class.getSimpleName();
        }
    }

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
                if (getSpecifierOrInitializerExpression()!=null &&
                		!(getSpecifierOrInitializerExpression() instanceof LazySpecifierExpression))
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
                if (getSpecifierOrInitializerExpression() instanceof LazySpecifierExpression)
                	getSpecifierOrInitializerExpression().visit(visitor);
            }
        }
        @Override public String getNodeType() {
            return AttributeDeclaration.class.getSimpleName();
        }
    }
    
    public static class Variable 
            extends Tree.Variable {
        public Variable(Token token) {
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
                List<ParameterList> parameterLists = getParameterLists();
                for (int i=0,l=parameterLists.size();i<l;i++)
                    parameterLists.get(i).visit(visitor);
            }
        }
        @Override public String getNodeType() {
            return Variable.class.getSimpleName();
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
//                if (getSpecifierExpression()!=null &&
//                		!(getSpecifierExpression() instanceof LazySpecifierExpression))
//                    getSpecifierExpression().visit(visitor);
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
                List<ParameterList> parameterLists = getParameterLists();
                for (int i=0,l=parameterLists.size();i<l;i++)
                    parameterLists.get(i).visit(visitor);
//                if (getSpecifierExpression() instanceof LazySpecifierExpression)
                if (getSpecifierExpression()!=null)
                	getSpecifierExpression().visit(visitor);
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
                List<ParameterList> parameterLists = getParameterLists();
                for (int i=0,l=parameterLists.size();i<l;i++)
                    parameterLists.get(i).visit(visitor);
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
    
    public static class ExtendedTypeExpression 
            extends Tree.ExtendedTypeExpression {
        public ExtendedTypeExpression(Token token) {
            super(token);
        }
        @Override public String getNodeType() {
            return ExtendedTypeExpression.class.getSimpleName();
        }
        public void setExtendedType(SimpleType type) {
            connect(type);
        }
    }
        
    public static class IsCase
            extends Tree.IsCase {
        private Tree.Variable variable;
        public IsCase(Token token) {
            super(token);
        }
        @Override
        public void setVariable(Tree.Variable node) {
            variable = node;
        }
        @Override
        public Tree.Variable getVariable() {
            return variable;
        }
    }

}
