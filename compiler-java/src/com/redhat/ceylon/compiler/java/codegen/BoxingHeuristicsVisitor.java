/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */

package com.redhat.ceylon.compiler.java.codegen;

import java.util.HashMap;
import java.util.Stack;

import com.redhat.ceylon.common.BooleanUtil;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ArithmeticOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.BaseMemberExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.BitwiseOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.CompilationUnit;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Expression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.InvocationExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.LogicalOp;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.model.typechecker.model.Value;

public abstract class BoxingHeuristicsVisitor extends Visitor {

    protected abstract boolean isInvocationExpressionOptimizable(Tree.InvocationExpression ce);
    
    private Stack<Boolean> nextPreferredExpressionBoxings = null;
    private Boolean preferredExpressionBoxing = null;
    
    private HashMap<Value, ValueUsageInfo> values;
    
    @Override
    public void visit(CompilationUnit that) {
        values = new HashMap<Value, ValueUsageInfo>();
        super.visit(that);
        for (ValueUsageInfo valInfo : values.values()) {
            valInfo.apply();
        }
    }

    @Override
    public void visit(BaseMemberExpression that) {
        analyseValueInfo(that);
        super.visit(that);
    }

    @Override
    public void visit(Expression that) {
        Stack<Boolean> npebs = setPEB();
        super.visit(that);
        resetPEB(npebs);
    }
    
    @Override
    public void visit(ArithmeticOp that) {
        super.visit(that);
        // can't optimise the ** operator in Java
        // we are unboxed if any term is 
        if(that.getLeftTerm().getUnboxed()
                || that.getRightTerm().getUnboxed()
                || BooleanUtil.isFalse(preferredExpressionBoxing))
            CodegenUtil.markUnBoxed(that);
    }
    
    @Override
    public void visit(BitwiseOp that) {
        super.visit(that);
        // we are unboxed if any term is 
        if(that.getLeftTerm().getUnboxed()
                || that.getRightTerm().getUnboxed()
                || BooleanUtil.isFalse(preferredExpressionBoxing))
            CodegenUtil.markUnBoxed(that);
    }

    @Override
    public void visit(LogicalOp that) {
        super.visit(that);
        // we are unboxed if any term is 
        if(that.getLeftTerm().getUnboxed()
                || that.getRightTerm().getUnboxed()
                || BooleanUtil.isFalse(preferredExpressionBoxing))
            CodegenUtil.markUnBoxed(that);
    }

    @Override
    public void visit(InvocationExpression that) {
        super.visit(that);
        if (isInvocationExpressionOptimizable(that)) {
            CodegenUtil.markUnBoxed(that);
        }
    }

    // The following methods are only used to set the "Preferred Expression Boxing"
    
    @Override
    public void visit(Tree.Parameter that) {
        Boolean currentPEB = setNextPEBs(that.getParameterModel().getModel().getUnboxed());
        super.visit(that);
        preferredExpressionBoxing = currentPEB;
    }
    
    @Override
    public void visit(Tree.ListedArgument that) {
        if (that.getParameter() != null) {
            Boolean currentPEB = setNextPEBs(that.getParameter().getModel().getUnboxed());
            super.visit(that);
            preferredExpressionBoxing = currentPEB;
        }else {
            super.visit(that);
        }
    }
    
    @Override
    public void visit(Tree.ElementRange that) {
        Boolean currentPEB = setNextPEBs(false, true);
        super.visit(that);
        preferredExpressionBoxing = currentPEB;
    }
    
    // Set the stack for to preferred boxing that shall be used
    // for the next N occurrences of Expression as the child
    // nodes of the current node (where N is the number of
    // booleans passed to this function)
    private Boolean setNextPEBs(Boolean... boxings) {
        nextPreferredExpressionBoxings = new Stack<Boolean>();
        for (Boolean b : boxings) {
            nextPreferredExpressionBoxings.push(b);
        }
        return preferredExpressionBoxing;
    }
    
    // Set the next preferred boxing to be the currently active one
    private Stack<Boolean> setPEB() {
        Stack<Boolean> npebs = nextPreferredExpressionBoxings;
        preferredExpressionBoxing = (npebs != null && !npebs.isEmpty()) ? npebs.pop() : null;
        nextPreferredExpressionBoxings = null;
        return npebs;
    }
    
    // Unset the currently active preferred boxing and reset the
    // list of next boxings to what's left of the list
    private void resetPEB(Stack<Boolean> npebs) {
        preferredExpressionBoxing = null;
        nextPreferredExpressionBoxings = npebs;
    }
    
    public class ValueUsageInfo {
        private final Value value;
        private int unboxedAccessCount = 0;
        private int boxedAccessCount = 0;
        public ValueUsageInfo(Value value) {
            this.value = value;
        }
        public void analyse() {
            if (preferredExpressionBoxing != null) {
                if (preferredExpressionBoxing || BooleanUtil.isTrue(preferredExpressionBoxing)) {
                    unboxedAccessCount++;
                } else {
                    boxedAccessCount++;
                }
            }
        }
        public void apply() {
            // TODO: apply what we found
            System.err.println(this);
            boolean isLocalValue = Decl.isLocal(value) && !Decl.isParameter(value) && !Decl.isTransient(value);
            if (isLocalValue && boxedAccessCount > unboxedAccessCount) {
                if (BooleanUtil.isNotFalse(value.getUnboxed())) {
                    System.err.println("Setting boxing state to BOXED for " + this);
                    value.setUnboxed(false);
                }
            }
        }
        @Override
        public String toString() {
            return "Value[name=" + value.getName() + ", unboxed=" + unboxedAccessCount + ", boxed=" + boxedAccessCount + "]";
        }
    }
    
    private void analyseValueInfo(Tree.MemberOrTypeExpression that) {
        if (that.getDeclaration() instanceof Value) {
            Value val = (Value) that.getDeclaration();
            ValueUsageInfo valInfo = values.get(val);
            if (valInfo == null) {
                valInfo = new ValueUsageInfo(val);
                values.put(val, valInfo);
            }
            valInfo.analyse();
        }
    }
    
}

