package com.redhat.ceylon.compiler.typechecker.tree;

import org.antlr.runtime.Token;

public class CustomTree extends Tree {
    
    public static class ExtendedTypeExpression 
            extends Tree.ExtendedTypeExpression {
        private SimpleType type;
        public ExtendedTypeExpression(Token token) {
            super(token);
        }
        public SimpleType getType() {
            return type;
        }
        public void setType(SimpleType type) {
            this.type = type;
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
