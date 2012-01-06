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

import com.redhat.ceylon.compiler.java.util.Util;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ArithmeticAssignmentOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ArithmeticOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AssignOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.BaseMemberExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.CharLiteral;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ComparisonOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.DefaultOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.EqualityOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Exists;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Expression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.FloatLiteral;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.IdenticalOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.InOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.InvocationExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.IsOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.LogicalAssignmentOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.LogicalOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.NaturalLiteral;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.NegativeOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Nonempty;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.NotOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.PositiveOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.PostfixOperatorExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.PowerOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.PrefixOperatorExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.QualifiedMemberExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.StringLiteral;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.StringTemplate;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Term;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

public class BoxingVisitor extends Visitor {

    private AbstractTransformer transformer;
    
    public BoxingVisitor(AbstractTransformer transformer){
        this.transformer = transformer;
    }

    @Override
    public void visit(BaseMemberExpression that) {
        super.visit(that);
        // handle errors gracefully
        if(that.getDeclaration() == null)
            return;
        Declaration decl = that.getDeclaration();
        if(Util.isUnBoxed((TypedDeclaration)decl)
                // special cases for true/false
                || transformer.isBooleanTrue(decl)
                || transformer.isBooleanFalse(decl))
            Util.markUnBoxed(that);
    }

    @Override
    public void visit(QualifiedMemberExpression that) {
        super.visit(that);
        // handle errors gracefully
        if(that.getDeclaration() == null)
            return;
        propagateFromDeclaration(that, (TypedDeclaration)that.getDeclaration());
    }

    @Override
    public void visit(Expression that) {
        super.visit(that);
        propagateFromTerm(that, that.getTerm());
    }

    @Override
    public void visit(InvocationExpression that) {
        super.visit(that);
        propagateFromTerm(that, that.getPrimary());
    }

    @Override
    public void visit(NaturalLiteral that) {
        super.visit(that);
        Util.markUnBoxed(that);
    }

    @Override
    public void visit(FloatLiteral that) {
        super.visit(that);
        Util.markUnBoxed(that);
    }

    @Override
    public void visit(StringLiteral that) {
        super.visit(that);
        Util.markUnBoxed(that);
    }

    @Override
    public void visit(CharLiteral that) {
        super.visit(that);
        Util.markUnBoxed(that);
    }

    @Override
    public void visit(StringTemplate that) {
        super.visit(that);
        // for now we always produce an unboxed string in ExpressionTransformer
        Util.markUnBoxed(that);
    }
    
    @Override
    public void visit(PositiveOp that) {
        super.visit(that);
        // we are unboxed if our term is
        propagateFromTerm(that, that.getTerm());
    }

    @Override
    public void visit(NegativeOp that) {
        super.visit(that);
        // we are unboxed if our term is
        propagateFromTerm(that, that.getTerm());
    }

    @Override
    public void visit(ArithmeticOp that) {
        super.visit(that);
        // can't optimise the ** operator in Java
        if(that instanceof PowerOp)
            return;
        // we are unboxed if both terms are
        if(that.getLeftTerm().getUnboxed()
                && that.getRightTerm().getUnboxed())
            Util.markUnBoxed(that);
    }

    @Override
    public void visit(ArithmeticAssignmentOp that) {
        super.visit(that);
        // we are unboxed if both terms are 
        if(that.getLeftTerm().getUnboxed()
                && that.getRightTerm().getUnboxed())
            Util.markUnBoxed(that);
    }

    @Override
    public void visit(PostfixOperatorExpression that) {
        super.visit(that);
        // we are unboxed if the term is
        propagateFromTerm(that, that.getTerm());
    }

    @Override
    public void visit(PrefixOperatorExpression that) {
        super.visit(that);
        // we are unboxed if the term is
        propagateFromTerm(that, that.getTerm());
    }

    @Override
    public void visit(NotOp that) {
        super.visit(that);
        // this is not conditional
        Util.markUnBoxed(that);
    }
    
    @Override
    public void visit(LogicalOp that) {
        super.visit(that);
        // this is not conditional
        Util.markUnBoxed(that);
    }

    @Override
    public void visit(AssignOp that) {
        super.visit(that);
        propagateFromTerm(that, that.getLeftTerm());
    }

    @Override
    public void visit(LogicalAssignmentOp that) {
        super.visit(that);
        // this is not conditional
        Util.markUnBoxed(that);
    }

    @Override
    public void visit(EqualityOp that) {
        super.visit(that);
        // this is not conditional
        Util.markUnBoxed(that);
    }

    @Override
    public void visit(IdenticalOp that) {
        super.visit(that);
        // this is not conditional
        Util.markUnBoxed(that);
    }

    @Override
    public void visit(ComparisonOp that) {
        super.visit(that);
        // this is not conditional
        Util.markUnBoxed(that);
    }

    @Override
    public void visit(InOp that) {
        super.visit(that);
        // this is not conditional
        Util.markUnBoxed(that);
    }

    @Override
    public void visit(IsOp that) {
        super.visit(that);
        // this is not conditional
        Util.markUnBoxed(that);
    }

    @Override
    public void visit(DefaultOp that) {
        super.visit(that);
        propagateFromTerm(that, that.getLeftTerm());
    }

    @Override
    public void visit(Nonempty that) {
        super.visit(that);
        // this is not conditional
        Util.markUnBoxed(that);
    }

    @Override
    public void visit(Exists that) {
        super.visit(that);
        // this is not conditional
        Util.markUnBoxed(that);
    }

    private void propagateFromDeclaration(Term that, TypedDeclaration term) {
        if(Util.isUnBoxed(term))
            Util.markUnBoxed(that);
    }

    private void propagateFromTerm(Term that, Term term) {
        if(Util.isUnBoxed(term))
            Util.markUnBoxed(that);
    }

}
