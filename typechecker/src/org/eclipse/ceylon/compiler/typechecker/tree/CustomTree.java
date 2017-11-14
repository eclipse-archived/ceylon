/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.typechecker.tree;

import org.antlr.runtime.Token;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree;

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
    
    public static class MatchCase
            extends Tree.MatchCase {
        private Tree.Variable variable;
        public MatchCase(Token token) {
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
    
    public static class GuardedVariable
            extends Tree.Variable {
        private Tree.ConditionList conditionList;
        private boolean reversed;
        public GuardedVariable(Token token) {
            super(token);
        }
        public Tree.ConditionList getConditionList() {
            return conditionList;
        }
        public void setConditionList(
                Tree.ConditionList condition) {
            this.conditionList = condition;
        }
        public boolean isReversed() {
            return reversed;
        }
        public void setReversed(boolean reversed) {
            this.reversed = reversed;
        }
    }

}
