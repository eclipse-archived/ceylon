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

import com.redhat.ceylon.common.BooleanUtil;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ArithmeticOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.BaseMemberExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.BitwiseOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.CompilationUnit;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.InvocationExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.LogicalOp;
import com.redhat.ceylon.model.typechecker.model.Value;

public abstract class BoxingHeuristicsVisitor extends BoxingVisitor {

    protected abstract boolean isInvocationExpressionOptimizable(Tree.InvocationExpression ce);
    
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
    public void visit(ArithmeticOp that) {
        super.visit(that);
        // can't optimise the ** operator in Java
        // we are unboxed if any term is 
        if(that.getLeftTerm().getUnboxed()
                || that.getRightTerm().getUnboxed()
                || BooleanUtil.isFalse(getPEB()))
            CodegenUtil.markUnBoxed(that);
    }
    
    @Override
    public void visit(BitwiseOp that) {
        super.visit(that);
        // we are unboxed if any term is 
        if(that.getLeftTerm().getUnboxed()
                || that.getRightTerm().getUnboxed()
                || BooleanUtil.isFalse(getPEB()))
            CodegenUtil.markUnBoxed(that);
    }

    @Override
    public void visit(LogicalOp that) {
        super.visit(that);
        // we are unboxed if any term is 
        if(that.getLeftTerm().getUnboxed()
                || that.getRightTerm().getUnboxed()
                || BooleanUtil.isFalse(getPEB()))
            CodegenUtil.markUnBoxed(that);
    }

    @Override
    public void visit(InvocationExpression that) {
        super.visit(that);
        if (isInvocationExpressionOptimizable(that)) {
            CodegenUtil.markUnBoxed(that);
        }
    }

    public class ValueUsageInfo {
        private final Value value;
        private int unboxedAccessCount = 0;
        private int boxedAccessCount = 0;
        public ValueUsageInfo(Value value) {
            this.value = value;
        }
        public void analyse() {
            if (getPEB() != null) {
                if (getPEB() || BooleanUtil.isTrue(getPEB())) {
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

