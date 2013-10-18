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

import java.util.List;

import com.redhat.ceylon.compiler.typechecker.analyzer.Util;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.ProducedReference;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ArithmeticAssignmentOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ArithmeticOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AssignOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.BaseMemberExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Bound;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.CharLiteral;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ComparisonOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.EqualityOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Exists;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Expression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.FloatLiteral;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.IdenticalOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.InOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.IndexExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.InvocationExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.IsOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.LogicalAssignmentOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.LogicalOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.MemberOrTypeExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.NaturalLiteral;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.NegativeOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Nonempty;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.NotOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ParameterizedExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.PositiveOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.PostfixOperatorExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.PowerOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.PrefixOperatorExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.QualifiedMemberExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.StaticMemberOrTypeExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.StringLiteral;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.StringTemplate;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Term;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.WithinOp;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

public abstract class BoxingVisitor extends Visitor {

    protected abstract boolean isBooleanTrue(Declaration decl);
    protected abstract boolean isBooleanFalse(Declaration decl);
    protected abstract boolean hasErasure(ProducedType type);
    protected abstract boolean hasErasedTypeParameters(ProducedReference producedReference);
    protected abstract boolean isTypeParameter(ProducedType type);
    protected abstract boolean isRaw(ProducedType type);

    @Override
    public void visit(BaseMemberExpression that) {
        super.visit(that);
        // handle errors gracefully
        if(that.getDeclaration() == null)
            return;
        Declaration decl = that.getDeclaration();
        if(CodegenUtil.isUnBoxed((TypedDeclaration)decl)
                // special cases for true/false
                || isBooleanTrue(decl)
                || isBooleanFalse(decl))
            CodegenUtil.markUnBoxed(that);
        if(CodegenUtil.isRaw((TypedDeclaration) decl))
            CodegenUtil.markRaw(that);
        if(CodegenUtil.hasTypeErased((TypedDeclaration) decl))
            CodegenUtil.markTypeErased(that);
    }

    @Override
    public void visit(QualifiedMemberExpression that) {
        super.visit(that);
        // handle errors gracefully
        if(that.getDeclaration() == null)
            return;
        if(that.getMemberOperator() instanceof Tree.SafeMemberOp){
            if(CodegenUtil.hasTypeErased((TypedDeclaration) that.getDeclaration()))
                CodegenUtil.markTypeErased(that);
            // we must be boxed, since safe member op "?." returns an optional type
            return;
        }
        if (Decl.isValueTypeDecl(that.getPrimary()) && CodegenUtil.isUnBoxed(that.getPrimary())) {
            // it's unboxed iff it's an unboxable type
            if(Decl.isValueTypeDecl((TypedDeclaration)that.getDeclaration()))
                CodegenUtil.markUnBoxed(that);
            if(CodegenUtil.isRaw((TypedDeclaration) that.getDeclaration()))
                CodegenUtil.markRaw(that);
            if(CodegenUtil.hasTypeErased((TypedDeclaration) that.getDeclaration()))
                CodegenUtil.markTypeErased(that);
        } else {
            propagateFromDeclaration(that, (TypedDeclaration)that.getDeclaration());
        }
        // special case for spread op, because even if the primary is erased (ex: <T> T|String), its application may not
        // be (ex: <String>), and in that case we will generate a proper Sequential<String> which is not raw at all
        if(that.getMemberOperator() instanceof Tree.SpreadOp){
            // find the return element type
            ProducedType elementType = that.getTarget().getType();
            CodegenUtil.markTypeErased(that, hasErasure(elementType));
        }
        if(ExpressionTransformer.isSuperOrSuperOf(that.getPrimary())){
            // if the target is an interface whose type arguments have been turned to raw, make this expression
            // as erased
            ProducedReference target = that.getTarget();
            if(target != null
                    && target.getQualifyingType() != null
                    && target.getQualifyingType().getDeclaration() instanceof Interface
                    && isRaw(target.getQualifyingType())){
                CodegenUtil.markTypeErased(that);
            }
        }
    }

    @Override
    public void visit(Expression that) {
        super.visit(that);
        Term term = that.getTerm();
        propagateFromTerm(that, term);
        
        // Special case where a method reference surrounded
        // by an expression will be turned into a Callable
        // which will need to be marked boxed
        if (term instanceof MemberOrTypeExpression) {
            Tree.MemberOrTypeExpression expr = (Tree.MemberOrTypeExpression)term;
            if (expr.getDeclaration() instanceof Method) {
                that.setUnboxed(false);
            }
        }
        
    }
    
    @Override
    public void visit(InvocationExpression that) {
        super.visit(that);
        if (Util.isIndirectInvocation(that)
                && !Decl.isJavaStaticPrimary(that.getPrimary())) {
            // These are always boxed
            return;
        }
        propagateFromTerm(that, that.getPrimary());
        
        // Specifically for method invocations we check if the return type is
        // a type parameter and if so if one of the type arguments is erased,
        // in that case we mark the expressionitself as erased as well
        if (that.getPrimary() instanceof StaticMemberOrTypeExpression) {
            StaticMemberOrTypeExpression expr = (StaticMemberOrTypeExpression)that.getPrimary();
            if (expr.getDeclaration() instanceof Method) {
                Method mth = (Method)expr.getDeclaration();
                if (isTypeParameter(mth.getType()) 
                        && hasErasedTypeParameter(expr.getTarget(), expr.getTypeArguments().getTypeModels())) {
                    CodegenUtil.markTypeErased(that);
                    CodegenUtil.markUntrustedType(that);
                }
            }
        }
    }

    private boolean hasErasedTypeParameter(ProducedReference producedReference, List<ProducedType> typeArguments) {
        if (typeArguments != null){
            for (ProducedType arg : typeArguments) {
                if (hasErasure(arg) /*|| willEraseToSequential(param.getType())*/) {
                    return true;
                }
            }
        }
        return hasErasedTypeParameters(producedReference);
    }
    
    @Override
    public void visit(ParameterizedExpression that) {
        super.visit(that);
        propagateFromTerm(that, that.getPrimary());
    }

    @Override
    public void visit(IndexExpression that) {
        super.visit(that);
        // we need to propagate from the underlying method call (item/span)
        if(that.getPrimary() == null
                || that.getPrimary().getTypeModel() == null)
            return;
        ProducedType lhsModel = that.getPrimary().getTypeModel();
        if(lhsModel.getDeclaration() == null)
            return;
        String methodName = that.getElementOrRange() instanceof Tree.Element ? "get" : "span";
        // find the method from its declaration
        TypedDeclaration member = (TypedDeclaration) lhsModel.getDeclaration().getMember(methodName, null, false);
        if(member == null)
            return;
        propagateFromDeclaration(that, member);
    }

    @Override
    public void visit(NaturalLiteral that) {
        super.visit(that);
        CodegenUtil.markUnBoxed(that);
    }

    @Override
    public void visit(FloatLiteral that) {
        super.visit(that);
        CodegenUtil.markUnBoxed(that);
    }

    @Override
    public void visit(StringLiteral that) {
        super.visit(that);
        CodegenUtil.markUnBoxed(that);
    }

    @Override
    public void visit(CharLiteral that) {
        super.visit(that);
        CodegenUtil.markUnBoxed(that);
    }

    @Override
    public void visit(StringTemplate that) {
        super.visit(that);
        // for now we always produce an unboxed string in ExpressionTransformer
        CodegenUtil.markUnBoxed(that);
    }
    
    @Override
    public void visit(PositiveOp that) {
        super.visit(that);
        // we are unboxed if our term is
        propagateBoxingFromTerm(that, that.getTerm());
    }

    @Override
    public void visit(NegativeOp that) {
        super.visit(that);
        // we are unboxed if our term is
        propagateBoxingFromTerm(that, that.getTerm());
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
            CodegenUtil.markUnBoxed(that);
    }

    @Override
    public void visit(ArithmeticAssignmentOp that) {
        super.visit(that);
        // we are unboxed if both terms are 
        if(that.getLeftTerm().getUnboxed()
                && that.getRightTerm().getUnboxed())
            CodegenUtil.markUnBoxed(that);
    }

    @Override
    public void visit(PostfixOperatorExpression that) {
        super.visit(that);
        // we are unboxed if the term is
        propagateBoxingFromTerm(that, that.getTerm());
    }

    @Override
    public void visit(PrefixOperatorExpression that) {
        super.visit(that);
        // we are unboxed if the term is
        propagateBoxingFromTerm(that, that.getTerm());
    }

    @Override
    public void visit(NotOp that) {
        super.visit(that);
        // this is not conditional
        CodegenUtil.markUnBoxed(that);
    }
    
    @Override
    public void visit(LogicalOp that) {
        super.visit(that);
        // this is not conditional
        CodegenUtil.markUnBoxed(that);
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
        CodegenUtil.markUnBoxed(that);
    }

    @Override
    public void visit(EqualityOp that) {
        super.visit(that);
        // this is not conditional
        CodegenUtil.markUnBoxed(that);
    }

    @Override
    public void visit(IdenticalOp that) {
        super.visit(that);
        // this is not conditional
        CodegenUtil.markUnBoxed(that);
    }

    @Override
    public void visit(ComparisonOp that) {
        super.visit(that);
        // this is not conditional
        CodegenUtil.markUnBoxed(that);
    }

    @Override
    public void visit(WithinOp that) {
        super.visit(that);
        // this is not conditional
        CodegenUtil.markUnBoxed(that);
    }

    @Override
    public void visit(Bound that) {
        super.visit(that);
        // this is not conditional
        CodegenUtil.markUnBoxed(that);
    }

    @Override
    public void visit(InOp that) {
        super.visit(that);
        // this is not conditional
        CodegenUtil.markUnBoxed(that);
    }

    @Override
    public void visit(IsOp that) {
        super.visit(that);
        // this is not conditional
        CodegenUtil.markUnBoxed(that);
    }

    @Override
    public void visit(Nonempty that) {
        super.visit(that);
        // this is not conditional
        CodegenUtil.markUnBoxed(that);
    }

    @Override
    public void visit(Exists that) {
        super.visit(that);
        // this is not conditional
        CodegenUtil.markUnBoxed(that);
    }

    private void propagateFromDeclaration(Term that, TypedDeclaration term) {
        if(CodegenUtil.isUnBoxed(term))
            CodegenUtil.markUnBoxed(that);
        if(CodegenUtil.isRaw(term))
            CodegenUtil.markRaw(that);
        if(CodegenUtil.hasTypeErased(term))
            CodegenUtil.markTypeErased(that);
        if(CodegenUtil.hasUntrustedType(term))
            CodegenUtil.markUntrustedType(that);
    }

    /**
     * Only for things that can't produce raw/erased types, such as math operators because
     * those are always casted
     */
    private void propagateBoxingFromTerm(Term that, Term term) {
        if(CodegenUtil.isUnBoxed(term))
            CodegenUtil.markUnBoxed(that);
    }
    
    private void propagateFromTerm(Term that, Term term) {
        if(CodegenUtil.isUnBoxed(term))
            CodegenUtil.markUnBoxed(that);
        if(CodegenUtil.isRaw(term))
            CodegenUtil.markRaw(that);
        if(CodegenUtil.hasTypeErased(term))
            CodegenUtil.markTypeErased(that);
        if(CodegenUtil.hasUntrustedType(term))
            CodegenUtil.markUntrustedType(that);
    }

}
