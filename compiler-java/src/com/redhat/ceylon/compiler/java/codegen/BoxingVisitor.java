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

import static com.redhat.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.isIndirectInvocation;
import static com.redhat.ceylon.compiler.typechecker.tree.TreeUtil.unwrapExpressionUntilTerm;

import java.util.Stack;

import com.redhat.ceylon.common.BooleanUtil;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ArithmeticAssignmentOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ArithmeticOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AssignOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.BaseMemberExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Bound;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.CharLiteral;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.CompareOp;
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
import com.redhat.ceylon.compiler.typechecker.tree.Tree.PositionalArgument;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.PositiveOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.PostfixOperatorExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.PrefixOperatorExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.QualifiedMemberExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.StaticMemberOrTypeExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.StringLiteral;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.StringTemplate;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SwitchCaseList;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Term;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.TypeArguments;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.WithinOp;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.model.typechecker.model.Class;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Function;
import com.redhat.ceylon.model.typechecker.model.Interface;
import com.redhat.ceylon.model.typechecker.model.Reference;
import com.redhat.ceylon.model.typechecker.model.Scope;
import com.redhat.ceylon.model.typechecker.model.Type;
import com.redhat.ceylon.model.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.model.typechecker.model.TypeParameter;
import com.redhat.ceylon.model.typechecker.model.TypedDeclaration;

public abstract class BoxingVisitor extends Visitor {

    protected abstract boolean isBooleanTrue(Declaration decl);
    protected abstract boolean isBooleanFalse(Declaration decl);
    protected abstract boolean hasErasure(Type type);
    protected abstract boolean hasErasedTypeParameters(Reference producedReference);
    protected abstract boolean willEraseToObject(Type type);
    protected abstract boolean isTypeParameter(Type type);
    protected abstract boolean isRaw(Type type);
    protected abstract boolean needsRawCastForMixinSuperCall(TypeDeclaration declaration, Type type);

    private Stack<Boolean> nextPreferredExpressionBoxings = null;
    private Boolean preferredExpressionBoxing = null;
    
    @Override
    public void visit(BaseMemberExpression that) {
        super.visit(that);
        // handle errors gracefully
        if(that.getDeclaration() == null)
            return;
        TypedDeclaration decl = (TypedDeclaration) that.getDeclaration();
        if(CodegenUtil.isUnBoxed(decl)
                // special cases for true/false
                || isBooleanTrue(decl)
                || isBooleanFalse(decl))
            CodegenUtil.markUnBoxed(that);
        if(CodegenUtil.isRaw(decl))
            CodegenUtil.markRaw(that);
        if(CodegenUtil.hasTypeErased(decl))
            CodegenUtil.markTypeErased(that);
        if(CodegenUtil.hasUntrustedType(decl))
            CodegenUtil.markUntrustedType(that);
    }

    @Override
    public void visit(QualifiedMemberExpression that) {
        super.visit(that);
        // handle errors gracefully
        if(that.getDeclaration() == null)
            return;
        if(that.getMemberOperator() instanceof Tree.SafeMemberOp){
            TypedDeclaration decl = (TypedDeclaration) that.getDeclaration();
            if(CodegenUtil.isRaw(decl))
                CodegenUtil.markRaw(that);
            if(CodegenUtil.hasTypeErased(decl))
                CodegenUtil.markTypeErased(that);
            if(CodegenUtil.hasUntrustedType(decl) || hasTypeParameterWithConstraintsOutsideScope(decl.getType(), that.getScope()))
                CodegenUtil.markUntrustedType(that);
            // we must be boxed, since safe member op "?." returns an optional type
            //return;
        } else if (Decl.isValueTypeDecl(that.getPrimary()) && CodegenUtil.isUnBoxed(that.getPrimary())) {
            // it's unboxed if it's an unboxable type or it's declared void
            if (Decl.isValueTypeDecl((TypedDeclaration)that.getDeclaration())
                    || (that.getDeclaration() instanceof Function 
                        && ((Function)that.getDeclaration()).isDeclaredVoid()))
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
            Type elementType = that.getTarget().getType();
            CodegenUtil.markTypeErased(that, hasErasure(elementType));
        }
        if(ExpressionTransformer.isSuperOrSuperOf(that.getPrimary())){
            // if the target is an interface whose type arguments have been turned to raw, make this expression
            // as erased
            Reference target = that.getTarget();
            if(target != null
                    && target.getQualifyingType() != null
                    && target.getQualifyingType().getDeclaration() instanceof Interface){
                if(isRaw(target.getQualifyingType())){
                    CodegenUtil.markTypeErased(that);
                }
                // See note in ClassTransformer.makeDelegateToCompanion for a similar test
                else{
                    TypeDeclaration declaration = target.getQualifyingType().getDeclaration();
                    if(needsRawCastForMixinSuperCall(declaration, target.getType()))
                        CodegenUtil.markTypeErased(that);
                }
            }
        }
        Type primaryType;
        if (that.getPrimary() instanceof Tree.Package
                || that.getTarget() == null) {
            primaryType = that.getPrimary().getTypeModel();
        } else {
            primaryType = that.getTarget().getQualifyingType();
        }
        
        if(primaryType != null
                && isRaw(primaryType)
                && that.getTarget() != null
                && that.getTarget().getDeclaration() instanceof TypedDeclaration
                && CodegenUtil.containsTypeParameter(((TypedDeclaration)that.getTarget().getDeclaration()).getType())){
            CodegenUtil.markTypeErased(that);
        }
        if (isRaw(primaryType)
                && !that.getTypeModel().getDeclaration().getTypeParameters().isEmpty()) {
            CodegenUtil.markRaw(that);
        }
    }

    @Override
    public void visit(Expression that) {
        Stack<Boolean> npebs = setPEB();
        super.visit(that);
        resetPEB(npebs);
        
        Term term = that.getTerm();
        propagateFromTerm(that, term);
        
        // Special case where a method reference surrounded
        // by an expression will be turned into a Callable
        // which will need to be marked boxed
        if (term instanceof MemberOrTypeExpression) {
            Tree.MemberOrTypeExpression expr = (Tree.MemberOrTypeExpression)term;
            if (expr.getDeclaration() instanceof Function) {
                that.setUnboxed(false);
            }
        }
        
    }
    
    @Override
    public void visit(InvocationExpression that) {
        super.visit(that);
        if (isIndirectInvocation(that)
                && !Decl.isJavaStaticPrimary(that.getPrimary())) {
            // if the Callable is raw the invocation will be erased
            if(that.getPrimary().getTypeModel() != null
                    && isRaw(that.getPrimary().getTypeModel()))
                CodegenUtil.markTypeErased(that);
            
            // These are always boxed
            return;
        }
        if(isByteLiteral(that))
            CodegenUtil.markUnBoxed(that);
        else
            propagateFromTerm(that, that.getPrimary());
        
        // Specifically for method invocations we check if the return type is
        // a type parameter and if so 
        // * if any of the type arguments is erased, or
        // * if the invocation itself has a raw type
        // then we mark the expression itself as erased as well
        if (that.getPrimary() instanceof StaticMemberOrTypeExpression) {
            StaticMemberOrTypeExpression expr = (StaticMemberOrTypeExpression)that.getPrimary();
            if (expr.getDeclaration() instanceof Function) {
                Function mth = (Function)expr.getDeclaration();
                if (isTypeParameter(mth.getType()) 
                        && (hasErasedTypeParameter(expr.getTarget(), expr.getTypeArguments())
                        || CodegenUtil.isRaw(that))) {
                    CodegenUtil.markTypeErased(that);
                    CodegenUtil.markUntrustedType(that);
                }
            }
        }
    }

    private boolean isByteLiteral(Tree.InvocationExpression ce) {
        // same test as in ExpressionTransformer.checkForByteLiterals
        if(ce.getPrimary() instanceof Tree.BaseTypeExpression
                && ce.getPositionalArgumentList() != null){
            java.util.List<Tree.PositionalArgument> positionalArguments = ce.getPositionalArgumentList().getPositionalArguments();
            if(positionalArguments.size() == 1){
                PositionalArgument argument = positionalArguments.get(0);
                if(argument instanceof Tree.ListedArgument
                        && ((Tree.ListedArgument) argument).getExpression() != null){
                    Term term = ((Tree.ListedArgument)argument).getExpression().getTerm();
                    if(term instanceof Tree.NegativeOp){
                        term = ((Tree.NegativeOp) term).getTerm();
                    }
                    if(term instanceof Tree.NaturalLiteral){
                        Declaration decl = ((Tree.BaseTypeExpression)ce.getPrimary()).getDeclaration();
                        if(decl instanceof Class){
                            String name = decl.getQualifiedNameString();
                            if(name.equals("ceylon.language::Byte")){
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean hasErasedTypeParameter(Reference producedReference, TypeArguments typeArguments) {
        if (typeArguments != null && typeArguments.getTypeModels() != null){
            for (Type arg : typeArguments.getTypeModels()) {
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
        Type lhsModel = that.getPrimary().getTypeModel();
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
        // we are unboxed if any term is 
        if(that.getLeftTerm().getUnboxed()
                || that.getRightTerm().getUnboxed()
                || BooleanUtil.isFalse(preferredExpressionBoxing))
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
    
    public void visit(CompareOp that) {
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

    private void propagateFromDeclaration(Term that, TypedDeclaration decl) {
        if(CodegenUtil.isUnBoxed(decl))
            CodegenUtil.markUnBoxed(that);
        if(CodegenUtil.isRaw(decl))
            CodegenUtil.markRaw(that);
        if(CodegenUtil.hasTypeErased(decl))
            CodegenUtil.markTypeErased(that);
        if(CodegenUtil.hasUntrustedType(decl) || hasTypeParameterWithConstraintsOutsideScope(decl.getType(), that.getScope()))
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

    private boolean hasTypeParameterWithConstraintsOutsideScope(Type type, Scope scope) {
        return hasTypeParameterWithConstraintsOutsideScopeResolved(type != null ? type.resolveAliases() : null, scope);
    }
    
    private boolean hasTypeParameterWithConstraintsOutsideScopeResolved(Type type, Scope scope) {
        if(type == null)
            return false;
        if(type.isUnion()){
            java.util.List<Type> caseTypes = type.getCaseTypes();
            for(Type pt : caseTypes){
                if(hasTypeParameterWithConstraintsOutsideScopeResolved(pt, scope))
                    return true;
            }
            return false;
        }
        if(type.isIntersection()){
            java.util.List<Type> satisfiedTypes = type.getSatisfiedTypes();
            for(Type pt : satisfiedTypes){
                if(hasTypeParameterWithConstraintsOutsideScopeResolved(pt, scope))
                    return true;
            }
            return false;
        }
        TypeDeclaration declaration = type.getDeclaration();
        if(declaration == null)
            return false;
        if(type.isTypeParameter()){
            // only look at it if it is defined outside our scope
            Scope typeParameterScope = declaration.getContainer();
            while(scope != null){
                if (Decl.equalScopes(scope,  typeParameterScope))
                    return false;
                scope = scope.getContainer();
            }
            TypeParameter tp = (TypeParameter) declaration;
            Boolean nonErasedBounds = tp.hasNonErasedBounds();
            if(nonErasedBounds == null)
                visitTypeParameter(tp);
            return nonErasedBounds != null ? nonErasedBounds.booleanValue() : false;
        }
        
        // now check its type parameters
        for(Type pt : type.getTypeArgumentList()){
            if(hasTypeParameterWithConstraintsOutsideScopeResolved(pt, scope))
                return true;
        }
        // no problem here
        return false;
    }

    private void visitTypeParameter(TypeParameter typeParameter) {
        if(typeParameter.hasNonErasedBounds() != null)
            return;
        for(Type pt : typeParameter.getSatisfiedTypes()){
            if(!willEraseToObject(pt)){
                typeParameter.setNonErasedBounds(true);
                return;
            }
        }
        typeParameter.setNonErasedBounds(false);
        return;
    }
    
    @Override
    public void visit(Tree.TypeLiteral that) {
        super.visit(that);
        if (!that.getWantsDeclaration()) {
            CodegenUtil.markRaw(that);
        }
    }
    
    @Override
    public void visit(Tree.IfExpression that){
        super.visit(that);
        if(that.getIfClause() == null
                || that.getElseClause() == null)
            return;
        Tree.Expression ifExpr = that.getIfClause().getExpression();
        Tree.Expression elseExpr = that.getElseClause().getExpression();
        if(ifExpr == null || elseExpr == null)
            return;
        if(CodegenUtil.isUnBoxed(ifExpr) 
                && CodegenUtil.isUnBoxed(elseExpr)
                && !willEraseToObject(that.getUnit().denotableType(that.getTypeModel())))
            CodegenUtil.markUnBoxed(that);
        if (that.getTypeModel().isExactly(that.getUnit().getNullValueDeclaration().getType())) {
            CodegenUtil.markTypeErased(that);
        }
        // An If expression can never be raw, type erased or untrusted because
        // it uses a Let with a new variable declaration, so the rawness, 
        // erasedness and untrustedness of its branches cannot propagate further 
        // up the tree.
    }

    @Override
    public void visit(Tree.SwitchExpression that){
        super.visit(that);
        SwitchCaseList caseList = that.getSwitchCaseList();
        if(caseList == null || caseList.getCaseClauses() == null)
            return;
        boolean unboxed = true;
        for(Tree.CaseClause caseClause : caseList.getCaseClauses()){
            Expression expr = caseClause.getExpression();
            if(expr == null)
                return;
            // a single boxed one makes the whole switch boxed
            if(!CodegenUtil.isUnBoxed(expr))
                unboxed = false;
            // A Switch expression can never be raw, type erased or untrusted because
            // it uses a Let with a new variable declaration, so the rawness, 
            // erasedness and untrustedness of its branches cannot propagate further 
            // up the tree.
        }
        if(caseList.getElseClause() != null){
            Expression expr = caseList.getElseClause().getExpression();
            if(expr == null)
                return;
            // a single boxed one makes the whole switch boxed
            if(!CodegenUtil.isUnBoxed(expr))
                unboxed = false;
            // see comment about about why we don't propagate rawness etc here.
        }
        if(unboxed 
                && !willEraseToObject(that.getUnit().denotableType(that.getTypeModel())))
            CodegenUtil.markUnBoxed(that);
        if (that.getTypeModel().isExactly(that.getUnit().getNullValueDeclaration().getType())) {
            CodegenUtil.markTypeErased(that);
        }
    }
    
    @Override
    public void visit(Tree.LetExpression that) {
        super.visit(that);
        if(that.getLetClause() == null
                || that.getLetClause().getExpression() == null)
            return;
        propagateFromTerm(that, that.getLetClause().getExpression());
    }
    
    @Override
    public void visit(Tree.DefaultOp that) {
        super.visit(that);
        if (unwrapExpressionUntilTerm(that.getLeftTerm()) instanceof Tree.ThenOp) {
            Tree.ThenOp then = (Tree.ThenOp)unwrapExpressionUntilTerm(that.getLeftTerm());
            if (CodegenUtil.isUnBoxed(that.getRightTerm())
                    && CodegenUtil.isUnBoxed(then.getRightTerm())
                    && !willEraseToObject(that.getUnit().denotableType(that.getTypeModel()))) {
                        CodegenUtil.markUnBoxed(that);
            }
        }
    }
    
    // The following methods are only used to set the "Preferred Expression Boxing"
    
    @Override
    public void visit(Tree.Parameter that) {
        if (that.getParameterModel().getModel() == null)
            return;
        Boolean currentPEB = setNextPEBs(that.getParameterModel().getModel().getUnboxed());
        super.visit(that);
        preferredExpressionBoxing = currentPEB;
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
}
