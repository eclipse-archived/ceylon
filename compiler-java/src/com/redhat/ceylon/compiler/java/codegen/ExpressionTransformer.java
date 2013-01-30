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

import static com.redhat.ceylon.compiler.typechecker.tree.Util.hasUncheckedNulls;
import static com.sun.tools.javac.code.Flags.PRIVATE;
import static com.sun.tools.javac.code.Flags.STATIC;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import com.redhat.ceylon.compiler.java.codegen.Invocation.TransformedInvocationPrimary;
import com.redhat.ceylon.compiler.java.codegen.Naming.Substitution;
import com.redhat.ceylon.compiler.java.codegen.Naming.SyntheticName;
import com.redhat.ceylon.compiler.java.codegen.Operators.AssignmentOperatorTranslation;
import com.redhat.ceylon.compiler.java.codegen.Operators.OperatorTranslation;
import com.redhat.ceylon.compiler.java.codegen.Operators.OptimisationStrategy;
import com.redhat.ceylon.compiler.java.codegen.StatementTransformer.Cond;
import com.redhat.ceylon.compiler.java.codegen.StatementTransformer.CondList;
import com.redhat.ceylon.compiler.loader.model.LazyMethod;
import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Functional;
import com.redhat.ceylon.compiler.typechecker.model.FunctionalParameter;
import com.redhat.ceylon.compiler.typechecker.model.Getter;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.NothingType;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ParameterList;
import com.redhat.ceylon.compiler.typechecker.model.ProducedReference;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.UnionType;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.BaseMemberExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Comprehension;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Condition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.DefaultArgument;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ExpressionComprehensionClause;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ForComprehensionClause;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.FunctionArgument;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.FunctionalParameterDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.IfComprehensionClause;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.InvocationExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.KeyValueIterator;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.NaturalLiteral;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.PositionalArgument;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.QualifiedMemberExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SequencedArgument;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SpecifierExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Super;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Term;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ValueIterator;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Variable;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.TypeTags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCConditional;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCLiteral;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.JCTree.JCNewArray;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCUnary;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Convert;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;

/**
 * This transformer deals with expressions only
 */
public class ExpressionTransformer extends AbstractTransformer {

    // flags for transformExpression
    public static final int EXPR_FOR_COMPANION = 1;
    public static final int EXPR_EXPECTED_TYPE_NOT_RAW = 1 << 1;
    public static final int EXPR_EXPECTED_TYPE_HAS_CONSTRAINED_TYPE_PARAMETERS = 1 << 2;
    public static final int EXPR_DOWN_CAST = 1 << 3;

    static{
        // only there to make sure this class is initialised before the enums defined in it, otherwise we
        // get an initialisation error
        Operators.init();
    }
    
    private boolean inStatement = false;
    private boolean withinInvocation = false;
    private boolean withinCallableInvocation = false;
    private Tree.ClassOrInterface withinSuperInvocation = null;
    private boolean outerCompanion;
    
    /** 
     * Whether there is an uninitialized object reference on the operand stack. 
     * See #929
     */
    private boolean uninitializedOperand = false;
    
    /**
     * The (or at least <em>a</em>) JCTree which 
     * employs a <em>backward branch</em> (jump to an earlier instruction, 
     * for example a loop of some kind) with an uninitialized reference on the 
     * operand stack.
     * See #929
     */
    private Node backwardBranchWithUninitialized = null;
    
    public static ExpressionTransformer getInstance(Context context) {
        ExpressionTransformer trans = context.get(ExpressionTransformer.class);
        if (trans == null) {
            trans = new ExpressionTransformer(context);
            context.put(ExpressionTransformer.class, trans);
        }
        return trans;
    }

	private ExpressionTransformer(Context context) {
        super(context);
    }

    /**
     * Declares that an uninitialized operand is on the operand stack
     * @param uninitializedOperand
     * @return The previous state
     * @see #hasUninitializedOperand()
     */
    private boolean uninitializedOperand(boolean uninitializedOperand) {
        boolean prev = this.uninitializedOperand;
        this.uninitializedOperand = uninitializedOperand;
        return prev;
    }
    
    /**
     * Whether there's an uninitialized operand is on the operand stack
     * @see #uninitializedOperand(boolean)
     */
    boolean hasUninitializedOperand() {
        return uninitializedOperand;
    }
    
    /**
     * Declares that a backward branch is being used.
     */
    private void backwardBranch(Node node) {
        if (hasUninitializedOperand()) {
            backwardBranchWithUninitialized = node;
        }
    }
    
    /**
     * Whether a backward branch has been {@linkplain #backwardBranch(Node) used} 
     * (or at least declared to be used) while an 
     * {@linkplain #uninitializedOperand(boolean) uninitialized operand} is 
     * on the stack
     * @return
     */
    boolean hasBackwardBranches() {
        return backwardBranchWithUninitialized != null;
    }
    
    private boolean stacksUninitializedOperand(Invocation invocation) {
        return invocation.getPrimary() instanceof Tree.BaseTypeExpression 
                || invocation.getPrimary() instanceof Tree.QualifiedTypeExpression
                || invocation instanceof SuperInvocation;
    }
	
	//
	// Statement expressions
	
    public JCStatement transform(Tree.ExpressionStatement tree) {
        // ExpressionStatements do not return any value, therefore we don't care about the type of the expressions.
        inStatement = true;
        backwardBranchWithUninitialized = null;
        JCStatement result = at(tree).Exec(transformExpression(tree.getExpression(), BoxingStrategy.INDIFFERENT, null));
        inStatement = false;
        return result;
    }
    
    public JCStatement transform(Tree.SpecifierStatement op) {
        // SpecifierStatement do not return any value, therefore we don't care about the type of the expressions.
        inStatement = true;
        backwardBranchWithUninitialized = null;
        JCStatement  result = at(op).Exec(transformAssignment(op, op.getBaseMemberExpression(), op.getSpecifierExpression().getExpression()));
        inStatement = false;
        return result;
    }
    
    //
    // Any sort of expression
    
    JCExpression transformExpression(final Tree.Term expr) {
        return transformExpression(expr, BoxingStrategy.BOXED, expr.getTypeModel());
    }

    JCExpression transformExpression(final Tree.Term expr, BoxingStrategy boxingStrategy, ProducedType expectedType) {
        return transformExpression(expr, boxingStrategy, expectedType, 0);
    }
    
    JCExpression transformExpression(final Tree.Term expr, BoxingStrategy boxingStrategy, 
            ProducedType expectedType, int flags) {
        if (expr == null) {
            return null;
        }
        
        at(expr);
        if (inStatement && boxingStrategy != BoxingStrategy.INDIFFERENT) {
            // We're not directly inside the ExpressionStatement anymore
            inStatement = false;
        }
        
        // Cope with things like ((expr))
        // FIXME: shouldn't that be in the visitor?
        Tree.Term term = expr;
        while (term instanceof Tree.Expression) {
            term = ((Tree.Expression)term).getTerm();
        }
        
        JCExpression result;
        if(term instanceof Tree.SequenceEnumeration){
            // special case to be able to pass expected type to sequences
            result = transform((Tree.SequenceEnumeration)term, expectedType);
        }else{
            CeylonVisitor v = gen().visitor;
            final ListBuffer<JCTree> prevDefs = v.defs;
            final boolean prevInInitializer = v.inInitializer;
            final ClassDefinitionBuilder prevClassBuilder = v.classBuilder;
            try {
                v.defs = new ListBuffer<JCTree>();
                v.inInitializer = false;
                v.classBuilder = gen().current();
                term.visit(v);
                if (v.hasResult()) {
                    result = v.getSingleResult();
                } else {
                    result = makeErroneous();
                }
            } finally {
                v.classBuilder = prevClassBuilder;
                v.inInitializer = prevInInitializer;
                v.defs = prevDefs;
            }
        }

        result = applyErasureAndBoxing(result, expr, boxingStrategy, expectedType, flags);
        if (expectedType != null && hasUncheckedNulls(expr)
                && expectedType.isSubtypeOf(typeFact().getObjectDeclaration().getType())) {
            result = makeUtilInvocation("checkNull", List.of(result), null);
        }

        return result;
    }
    
    JCExpression transform(FunctionArgument farg) {
        Method model = farg.getDeclarationModel();
        ProducedType callableType = farg.getTypeModel();
        // TODO MPL
        CallableBuilder callableBuilder = CallableBuilder.anonymous(
                gen(),
                farg.getExpression(),
                model.getParameterLists().get(0),
                farg.getParameterLists().get(0),
                callableType);
        return callableBuilder.build();
    }
    
    //
    // Boxing and erasure of expressions
    
    private JCExpression applyErasureAndBoxing(JCExpression result, Tree.Term expr, BoxingStrategy boxingStrategy, 
            ProducedType expectedType) {
        return applyErasureAndBoxing(result, expr, boxingStrategy, expectedType, 0);
    }
    
    private JCExpression applyErasureAndBoxing(JCExpression result, Tree.Term expr, BoxingStrategy boxingStrategy, 
                ProducedType expectedType, int flags) {
        ProducedType exprType = expr.getTypeModel();
        boolean exprBoxed = !CodegenUtil.isUnBoxed(expr);
        boolean exprErased = CodegenUtil.hasTypeErased(expr);
        return applyErasureAndBoxing(result, exprType, exprErased, exprBoxed, boxingStrategy, expectedType, flags);
    }
    
    JCExpression applyErasureAndBoxing(JCExpression result, ProducedType exprType,
            boolean exprBoxed,
            BoxingStrategy boxingStrategy, ProducedType expectedType) {
        return applyErasureAndBoxing(result, exprType, false, exprBoxed, boxingStrategy, expectedType, 0);
    }
    
    JCExpression applyErasureAndBoxing(JCExpression result, ProducedType exprType,
            boolean exprErased, boolean exprBoxed,
            BoxingStrategy boxingStrategy, ProducedType expectedType, 
            int flags) {
        
        boolean canCast = false;

        if (expectedType != null
                // don't add cast to an erased type 
                && !willEraseToObject(expectedType)) {

            // only try to cast boxed types, no point otherwise
            if(exprBoxed){

                boolean expectedTypeIsNotRaw = (flags & EXPR_EXPECTED_TYPE_NOT_RAW) != 0;
                boolean expectedTypeHasConstrainedTypeParameters = (flags & EXPR_EXPECTED_TYPE_HAS_CONSTRAINED_TYPE_PARAMETERS) != 0;
                boolean downCast = (flags & EXPR_DOWN_CAST) != 0;

                // special case for returning Null expressions
                if (isNull(exprType)){
                    // don't add cast for null
                    if(!isNullValue(exprType)){
                        // in some cases we may have an instance of Null, which is of type java.lang.Object, being
                        // returned in a context where we expect a String? (aka ceylon.language.String) so even though
                        // the instance at hand will really be null, we need a up-cast to it
                        // FIXME: this is always true
                        JCExpression targetType = makeJavaType(expectedType, AbstractTransformer.JT_RAW);
                        result = make().TypeCast(targetType, result);
                    }
                }else if(exprType.getDeclaration() instanceof NothingType){
                    // type param erasure
                    JCExpression targetType = makeJavaType(expectedType, AbstractTransformer.JT_RAW | AbstractTransformer.JT_NO_PRIMITIVES);
                    result = make().TypeCast(targetType, result);
                }else if(// expression was forcibly erased
                         exprErased
                         // some type parameter somewhere needs a cast
                         || needsCast(exprType, expectedType, expectedTypeIsNotRaw, expectedTypeHasConstrainedTypeParameters, downCast)
                         // if the exprType is raw and the expected type isn't
                         || (exprType.isRaw() && (expectedTypeIsNotRaw || !isTurnedToRaw(expectedType)))){

                    // save this before we simplify it because we lose that flag doing so
                    boolean exprIsRaw = exprType.isRaw();
                    boolean expectedTypeIsRaw = isTurnedToRaw(expectedType) && !expectedTypeIsNotRaw;

                    // simplify the type
                    // (without the underlying type, because the cast is always to a non-primitive)
                    exprType = simplifyType(expectedType).withoutUnderlyingType();

                    // We will need a raw cast if the expected type has type parameters, 
                    // unless the expr is already raw
                    if (!exprIsRaw && hasTypeParameters(expectedType)) {
                        JCExpression rawType = makeJavaType(expectedType, AbstractTransformer.JT_TYPE_ARGUMENT | AbstractTransformer.JT_RAW);
                        result = make().TypeCast(rawType, result);
                        // expr is now raw
                        exprIsRaw = true;
                    }

                    // if the expr is not raw, we need a cast
                    // if the expr is raw:
                    //  don't even try making an actual cast if there are bounded type parameters in play, because going raw is much safer
                    //  also don't try making the cast if the expected type is raw because anything goes
                    if(!exprIsRaw || (!expectedTypeHasConstrainedTypeParameters && !expectedTypeIsRaw)){
                        // Do the actual cast
                        JCExpression targetType = makeJavaType(expectedType, AbstractTransformer.JT_TYPE_ARGUMENT);
                        result = make().TypeCast(targetType, result);
                    }
                }else
                    canCast = true;
            }else
                canCast = true;
        }

        // we must do the boxing after the cast to the proper type
        JCExpression ret = boxUnboxIfNecessary(result, exprBoxed, exprType, boxingStrategy);
        
        // very special case for nothing that we need to "unbox" to a primitive type
        if(exprType.getDeclaration() instanceof NothingType
                && boxingStrategy == BoxingStrategy.UNBOXED){
            // in this case we have to use the expected type
            ret = unboxType(ret, expectedType);
        }
        
        // now check if we need variance casts
        if (canCast) {
            ret = applyVarianceCasts(ret, exprType, exprBoxed, boxingStrategy, expectedType,
                    (flags & EXPR_FOR_COMPANION) != 0);
        }
        ret = applySelfTypeCasts(ret, exprType, exprBoxed, boxingStrategy, expectedType);
        ret = applyJavaTypeConversions(ret, exprType, expectedType, boxingStrategy);
        return ret;
    }

    private boolean needsCast(ProducedType exprType, ProducedType expectedType, 
                              boolean expectedTypeNotRaw, boolean expectedTypeHasConstrainedTypeParameters,
                              boolean downCast) {
        // make sure we work on definite types
        exprType = simplifyType(exprType);
        expectedType = simplifyType(expectedType);
        // abort if both types are the same
        if(exprType.isExactly(expectedType)){
            // unless the expected type is parameterised with bounds because in that case we can't
            // really trust the expected type
            if(!expectedTypeHasConstrainedTypeParameters)
                return false;
        }

        // now see about erasure
        boolean eraseExprType = willEraseToObject(exprType);
        boolean eraseExpectedType = willEraseToObject(expectedType);
        
        // if we erase expected type we need no cast
        if(eraseExpectedType){
            // unless the expected type is parameterised with bounds that erasure to Object can't possibly satisfy
            if(!expectedTypeHasConstrainedTypeParameters)
                return false;
        }
        // if we erase the expr type we need a cast
        if(eraseExprType)
            return true;
        
        // special case for things that are erased to sequential, because for example Range|Empty does not have any super type
        // but we know that if expectedType is a sequential we will erase it not to Object but to Sequential 
        if(expectedType.getDeclaration() instanceof UnionType
                && willEraseToSequential(expectedType)){
            // consider the expected type to be Sequential then
            expectedType = typeFact().getDefiniteType(expectedType.getSupertype(typeFact().getSequentialDeclaration()));
        }

        // same thing for expr type, because we know it's not erased to Object, we already checked, but if it
        // will end up into a Sequential we need to know this
        if(exprType.getDeclaration() instanceof UnionType
                && willEraseToSequential(exprType)){
            // consider the expression type to be Sequential then
            exprType = typeFact().getDefiniteType(exprType.getSupertype(typeFact().getSequentialDeclaration()));
        }
        
        // find their common type
        ProducedType commonType = exprType.getSupertype(expectedType.getDeclaration());
        
        if(commonType == null || !(commonType.getDeclaration() instanceof ClassOrInterface)){
            // we did not find any common type, but we may be downcasting, in which case we need a cast
            return downCast;
        }
        
        // some times we can lose info due to an erased type parameter somewhere in the inheritance graph
        if(lostTypeParameterInInheritance(exprType, commonType))
            return true;
        
        if(!expectedTypeNotRaw){
            // the truth is that we don't really know if the expected type is raw or not, that flag only gets set
            // if we know for sure that the expected type is NOT raw. if it's false we've no idea but we can check:
            if(isTurnedToRaw(expectedType)){
                return false;
            }
            // if the expected type is exactly the common type, they must have the same erasure
            // note that we don't do that test if we know the expected type is not raw, because
            // the common type could be erased
            if(commonType.isExactly(expectedType))
                return false;
            
            
        }
        //special case for Callable because only the first type param exists in Java, the rest is completely suppressed
        boolean isCallable = isCeylonCallable(commonType);
        
        // now see if the type parameters match
        java.util.List<ProducedType> commonTypeArgs = commonType.getTypeArgumentList();
        java.util.List<ProducedType> expectedTypeArgs = expectedType.getTypeArgumentList();
        // check that we got them all otherwise we just don't know
        if(commonTypeArgs.size() != expectedTypeArgs.size())
            return false;
        for(int i=0,n=commonTypeArgs.size(); i < n ; i++){
            // apply the same logic to each type param: see if they would require a raw cast
            ProducedType commonTypeArg = commonTypeArgs.get(i);
            ProducedType expectedTypeArg = expectedTypeArgs.get(i);
            if(needsCast(commonTypeArg, expectedTypeArg, expectedTypeNotRaw, expectedTypeHasConstrainedTypeParameters, downCast))
                return true;
            // stop after the first one for Callable
            if(isCallable)
                break;
        }
        return false;
    }

    private boolean lostTypeParameterInInheritance(ProducedType exprType, ProducedType commonType) {
        if(exprType.getDeclaration() instanceof ClassOrInterface == false
                || commonType.getDeclaration() instanceof ClassOrInterface == false)
            return false;
        ClassOrInterface exprDecl = (ClassOrInterface) exprType.getDeclaration();
        ClassOrInterface commonDecl = (ClassOrInterface) commonType.getDeclaration();
        // do not search interfaces if the common declaration is a class, because interfaces cannot be subtypes of a class
        boolean searchInterfaces = commonDecl instanceof Interface;
        return lostTypeParameterInInheritance(exprDecl, commonDecl, searchInterfaces, false);
    }

    private boolean lostTypeParameterInInheritance(ClassOrInterface exprDecl, ClassOrInterface commonDecl, boolean searchInterfaces, boolean lostTypeParameter) {
        // stop if we found the common decl
        if(exprDecl == commonDecl)
            return lostTypeParameter;
        if(searchInterfaces){
            // find a match in interfaces
            for(ProducedType pt : exprDecl.getSatisfiedTypes()){
                // FIXME: this is very heavy-handed because we consider that once we've lost a type parameter we've lost them all
                // but we could optimise this by checking:
                // 1/ which type parameter we've really lost
                // 2/ if the type parameters we're passing to our super type actually depend in any way from type parameters we've lost
                boolean lostTypeParameter2 = lostTypeParameter || isTurnedToRaw(pt);
                pt = simplifyType(pt);
                // it has to be an interface
                Interface interf = (Interface) pt.getDeclaration();
                if(lostTypeParameterInInheritance(interf, commonDecl, searchInterfaces, lostTypeParameter2))
                    return true;
            }
        }
        // search for super classes
        ProducedType extendedType = exprDecl.getExtendedType();
        if(extendedType != null){
            // FIXME: see above
            boolean lostTypeParameter2 = lostTypeParameter || isTurnedToRaw(extendedType);
            extendedType = simplifyType(extendedType);
            // it has to be a Class
            Class extendedTypeDeclaration = (Class) extendedType.getDeclaration();
            // looks like Object's superclass is Object, so stop right there
            if(extendedTypeDeclaration != typeFact().getObjectDeclaration())
                return lostTypeParameterInInheritance(extendedTypeDeclaration, commonDecl, searchInterfaces, lostTypeParameter2);
        }
        // didn't find it
        return false;
    }

    private boolean hasTypeParameters(ProducedType type) {
        if (!type.getTypeArgumentList().isEmpty()) {
            return true;
        }
        if (type.getCaseTypes() != null) {
            for (ProducedType ct : type.getCaseTypes()) {
                if (hasTypeParameters(ct)) {
                    return true;
                }
            }
        }
        return false;
    }

    private JCExpression applyVarianceCasts(JCExpression result, ProducedType exprType,
            boolean exprBoxed,
            BoxingStrategy boxingStrategy, ProducedType expectedType, boolean forCompanion) {
        // unboxed types certainly don't need casting for variance
        if(exprBoxed || boxingStrategy == BoxingStrategy.BOXED){
            VarianceCastResult varianceCastResult = getVarianceCastResult(expectedType, exprType);
            if(varianceCastResult != null){
                // Types with variance types need a type cast, let's start with a raw cast to get rid
                // of Java's type system constraint (javac doesn't grok multiple implementations of the same
                // interface with different type params, which the JVM allows)
                JCExpression targetType = makeJavaType(expectedType, AbstractTransformer.JT_RAW);
                // do not change exprType here since this is just a Java workaround
                result = make().TypeCast(targetType, result);
                // now, because a raw cast is losing a lot of info, can we do better?
                if(varianceCastResult.isBetterCastAvailable()){
                    // let's recast that to something finer than a raw cast
                    targetType = makeJavaType(varianceCastResult.castType, AbstractTransformer.JT_TYPE_ARGUMENT | (forCompanion ? JT_SATISFIES : 0));
                    result = make().TypeCast(targetType, result);
                }
            }
        }
        return result;
    }
    
    private JCExpression applySelfTypeCasts(JCExpression result, ProducedType exprType,
            boolean exprBoxed,
            BoxingStrategy boxingStrategy, ProducedType expectedType) {
        if (expectedType == null) {
            return result;
        }
        final ProducedType selfType = exprType.getDeclaration().getSelfType();
        if (selfType != null) {
            if (selfType.isExactly(exprType) // self-type within its own scope
                    || !exprType.isExactly(expectedType)) {
                final ProducedType castType = findTypeArgument(exprType, selfType.getDeclaration());
                // the fact that the original expr was or not boxed doesn't mean the current result is boxed or not
                // as boxing transformations occur before this method
                boolean resultBoxed = boxingStrategy == BoxingStrategy.BOXED
                        || (boxingStrategy == BoxingStrategy.INDIFFERENT && exprBoxed);
                JCExpression targetType = makeJavaType(castType, resultBoxed ? AbstractTransformer.JT_TYPE_ARGUMENT : 0);
                result = make().TypeCast(targetType, result);
            }
        }
        return result;
    }

    private ProducedType findTypeArgument(ProducedType type, TypeDeclaration declaration) {
        if(type == null)
            return null;
        ProducedType typeArgument = type.getTypeArguments().get(declaration);
        if(typeArgument != null)
            return typeArgument;
        return findTypeArgument(type.getQualifyingType(), declaration);
    }

    private JCExpression applyJavaTypeConversions(JCExpression ret, ProducedType exprType, ProducedType expectedType, BoxingStrategy boxingStrategy) {
        ProducedType definiteExprType = simplifyType(exprType);
        String convertFrom = definiteExprType.getUnderlyingType();

        ProducedType definiteExpectedType = null;
        String convertTo = null;
        if (expectedType != null) {
            definiteExpectedType = simplifyType(expectedType);
            convertTo = definiteExpectedType.getUnderlyingType();
        }
        // check for identity conversion
        if (convertFrom != null && convertFrom.equals(convertTo)) {
            return ret;
        }
        boolean arrayUnbox = boxingStrategy == BoxingStrategy.UNBOXED && definiteExpectedType != null && isCeylonArray(definiteExpectedType);
        if (arrayUnbox && convertFrom != null) {
            convertTo = convertFrom;
        }
        if (convertTo != null) {
            if(convertTo.equals("byte")) {
                ret = make().TypeCast(syms().byteType, ret);
            } else if(convertTo.equals("short")) {
                ret = make().TypeCast(syms().shortType, ret);
            } else if(convertTo.equals("int")) {
                ret = make().TypeCast(syms().intType, ret);
            } else if(convertTo.equals("float")) {
                ret = make().TypeCast(syms().floatType, ret);
            } else if(convertTo.equals("char")) {
                ret = make().TypeCast(syms().charType, ret);
            } else if(convertTo.equals("byte[]")) {
                ret = make().TypeCast(make().TypeArray(make().TypeIdent(TypeTags.BYTE)), ret);
            } else if(convertTo.equals("short[]")) {
                ret = make().TypeCast(make().TypeArray(make().TypeIdent(TypeTags.SHORT)), ret);
            } else if(convertTo.equals("int[]")) {
                ret = make().TypeCast(make().TypeArray(make().TypeIdent(TypeTags.INT)), ret);
            } else if(convertTo.equals("long[]")) {
                ret = make().TypeCast(make().TypeArray(make().TypeIdent(TypeTags.LONG)), ret);
            } else if(convertTo.equals("float[]")) {
                ret = make().TypeCast(make().TypeArray(make().TypeIdent(TypeTags.FLOAT)), ret);
            } else if(convertTo.equals("double[]")) {
                ret = make().TypeCast(make().TypeArray(make().TypeIdent(TypeTags.DOUBLE)), ret);
            } else if(convertTo.equals("char[]")) {
                ret = make().TypeCast(make().TypeArray(make().TypeIdent(TypeTags.CHAR)), ret);
            } else if(convertTo.equals("boolean[]")) {
                ret = make().TypeCast(make().TypeArray(make().TypeIdent(TypeTags.BOOLEAN)), ret);
            } else if (arrayUnbox) {
                String ct = convertTo.substring(0, convertTo.length() - 2);
                ret = make().TypeCast(make().TypeArray(makeQuotedQualIdentFromString(ct)), ret);
            }
        } else if (arrayUnbox) {
            ProducedType ct = typeFact().getArrayElementType(definiteExpectedType);
            ret = make().TypeCast(make().TypeArray(makeJavaType(ct)), ret);
        }
        return ret;
    }
    
    private static class VarianceCastResult {
        ProducedType castType;
        
        VarianceCastResult(ProducedType castType){
            this.castType = castType;
        }
        
        private VarianceCastResult(){}
        
        boolean isBetterCastAvailable(){
            return castType != null;
        }
    }
    
    private static final VarianceCastResult RawCastVarianceResult = new VarianceCastResult();

    private VarianceCastResult getVarianceCastResult(ProducedType expectedType, ProducedType exprType) {
        // exactly the same type, doesn't need casting
        if(exprType.isExactly(expectedType))
            return null;
        // if we're not trying to put it into an interface, there's no need
        if(!(expectedType.getDeclaration() instanceof Interface))
            return null;
        // the interface must have type arguments, otherwise we can't use raw types
        if(expectedType.getTypeArguments().isEmpty())
            return null;
        // see if any of those type arguments has variance
        boolean hasVariance = false;
        for(TypeParameter t : expectedType.getTypeArguments().keySet()){
            if(t.isContravariant() || t.isCovariant()){
                hasVariance = true;
                break;
            }
        }
        if(!hasVariance)
            return null;
        // see if we're inheriting the interface twice with different type parameters
        java.util.List<ProducedType> satisfiedTypes = new LinkedList<ProducedType>();
        for(ProducedType superType : exprType.getSupertypes()){
            if(superType.getDeclaration() == expectedType.getDeclaration())
                satisfiedTypes.add(superType);
        }
        // we need at least two instantiations
        if(satisfiedTypes.size() <= 1)
            return null;
        boolean needsCast = false;
        // we need at least one that differs
        for(ProducedType superType : satisfiedTypes){
            if(!exprType.isExactly(superType)){
                needsCast = true;
                break;
            }
        }
        // no cast needed if they are all the same type
        if(!needsCast)
            return null;
        // find the better cast match
        for(ProducedType superType : satisfiedTypes){
            if(expectedType.isExactly(superType))
                return new VarianceCastResult(superType);
        }
        // nothing better than a raw cast (Stef: not sure that can happen)
        return RawCastVarianceResult;
    }

    //
    // Literals
    
    JCExpression ceylonLiteral(String s) {
        JCLiteral lit = make().Literal(s);
        return lit;
    }

    private JCExpression transformRadixLiteral(NaturalLiteral literal, int radix, String error){
        String value = literal.getText().substring(1);
        at(literal);
        try{
            long l = Convert.string2long(value, radix);
            return make().Literal(l);
        }catch(NumberFormatException x){
            return makeErroneous(literal, error);
        }
    }

    public JCExpression transform(Tree.StringLiteral string) {
        String value = string
                .getText()
                .substring(1, string.getText().length() - 1);
        at(string);
        return ceylonLiteral(value);
    }

    public JCExpression transform(Tree.QuotedLiteral string) {
        String value = string
                .getText()
                .substring(1, string.getText().length() - 1);
        at(string);
        return ceylonLiteral(value);
    }

    public JCExpression transform(Tree.CharLiteral lit) {
        // codePoint is at index 1 because the text is `X` (including quotation marks, so we skip them)
        int codePoint = lit.getText().codePointAt(1);
        return make().Literal(TypeTags.INT, codePoint);
    }

    public JCExpression transform(Tree.FloatLiteral lit) {
        double value = Double.parseDouble(lit.getText());
        // Don't need to handle the negative infinity and negative zero cases 
        // because Ceylon Float literals have no sign
        if (value == Double.POSITIVE_INFINITY) {
            return makeErroneous(lit, "Literal so large it is indistinguishable from infinity");
        } else if (value == 0.0 && !lit.getText().equals("0.0")) {
            return makeErroneous(lit, "Literal so small it is indistinguishable from zero");
        }
        JCExpression expr = make().Literal(value);
        return expr;
    }

    private JCExpression integerLiteral(Node node, String num) {
        try {
            return make().Literal(Long.parseLong(num));
        } catch (NumberFormatException e) {
            return makeErroneous(node, "Literal outside representable range");
        }
    }
    
    public JCExpression transform(Tree.NaturalLiteral lit) {
        String text = lit.getText();
        if(text.startsWith("#")){
            return transformRadixLiteral(lit, 16, "Invalid hexadecimal literal (must be unsigned and fit in 64 bits)");
        }
        if(text.startsWith("$")){
            return transformRadixLiteral(lit, 2, "Invalid binary literal (must be unsigned and fit in 64 bits)");
        }
        return integerLiteral(lit, text);
    }

    public JCExpression transformStringExpression(Tree.StringTemplate expr) {
        at(expr);
        JCExpression builder;
        builder = make().NewClass(null, null, naming.makeFQIdent("java","lang","StringBuilder"), List.<JCExpression>nil(), null);

        java.util.List<Tree.StringLiteral> literals = expr.getStringLiterals();
        java.util.List<Tree.Expression> expressions = expr.getExpressions();
        for (int ii = 0; ii < literals.size(); ii += 1) {
            Tree.StringLiteral literal = literals.get(ii);
            if (!"\"\"".equals(literal.getText())) {// ignore empty string literals
                at(literal);
                builder = make().Apply(null, makeSelect(builder, "append"), List.<JCExpression>of(transform(literal)));
            }
            if (ii == expressions.size()) {
                // The loop condition includes the last literal, so break out
                // after that because we've already exhausted all the expressions
                break;
            }
            Tree.Expression expression = expressions.get(ii);
            at(expression);
            // Here in both cases we don't need a type cast for erasure
            if (isCeylonBasicType(expression.getTypeModel())) {// TODO: Test should be erases to String, long, int, boolean, char, byte, float, double
                // If erases to a Java primitive just call append, don't box it just to call format. 
                String method = isCeylonCharacter(expression.getTypeModel()) ? "appendCodePoint" : "append";
                builder = make().Apply(null, makeSelect(builder, method), List.<JCExpression>of(transformExpression(expression, BoxingStrategy.UNBOXED, null)));
            } else {
                JCMethodInvocation formatted = make().Apply(null, makeSelect(transformExpression(expression), "toString"), List.<JCExpression>nil());
                builder = make().Apply(null, makeSelect(builder, "append"), List.<JCExpression>of(formatted));
            }
        }

        return make().Apply(null, makeSelect(builder, "toString"), List.<JCExpression>nil());
    }

    public JCExpression transform(Tree.SequenceEnumeration value) {
        return transform(value, null);
    }
    
    private JCExpression transform(Tree.SequenceEnumeration value, ProducedType expectedType) {
        at(value);
        if (value.getSequencedArgument() != null) {
            SequencedArgument sequencedArgument = value.getSequencedArgument();
            java.util.List<Tree.PositionalArgument> list = sequencedArgument.getPositionalArguments();
            if(list.isEmpty())
                return makeErroneous(value, "Empty iterable literal with sequenced arguments: "+value);
            ProducedType seqElemType = typeFact().getIteratedType(value.getTypeModel());
            seqElemType = wrapInOptionalForInterop(seqElemType, expectedType);
            return makeIterable(list, seqElemType);
        } else {
            return makeEmpty();
        }
    }

    public JCExpression transform(Tree.Tuple value) {
        SequencedArgument sequencedArgument = value.getSequencedArgument();
        if(sequencedArgument != null){
            java.util.List<PositionalArgument> args = sequencedArgument.getPositionalArguments();
            return makeTuple(args);
        }
        // nothing in there
        return makeEmpty();
    }

    public JCExpression comprehensionAsSequential(Comprehension comprehension, ProducedType expectedType) {
        JCExpression sequential = iterableToSequence(transformComprehension(comprehension));
        ProducedType elementType = comprehension.getForComprehensionClause().getTypeModel();
        ProducedType sequentialType = typeFact().getSequentialType(elementType);
        int flags = 0;
        // make sure we detect that we're downcasting a sequential into a sequence if we know the comprehension is non-empty
        if(expectedType.getSupertype(typeFact().getSequenceDeclaration()) != null)
            flags = EXPR_DOWN_CAST;
        return applyErasureAndBoxing(sequential, sequentialType, false, true, BoxingStrategy.BOXED, expectedType, flags);
    }
    
    private JCExpression makeTuple(java.util.List<Tree.PositionalArgument> expressions) {
        java.util.List<ProducedType> expressionTypes = new LinkedList<ProducedType>();
        for(Tree.PositionalArgument expr : expressions){
            expressionTypes.add(expr.getTypeModel());
        }
        boolean spread = false;
        // turn the last type from a sequence to its element type
        if(!expressions.isEmpty()){
            int lastIndex = expressions.size()-1;
            Tree.PositionalArgument last = expressions.get(lastIndex);
            if(last instanceof Tree.SpreadArgument){
                ProducedType lastType = expressionTypes.get(lastIndex);
                expressionTypes.set(lastIndex, typeFact().getIteratedType(lastType));
                spread = true;
            }else if(last instanceof Tree.Comprehension){
                spread = true;
            }
        }
        return makeTuple(expressions.iterator(), expressionTypes, spread);
    }
    
    private JCExpression makeTuple(Iterator<Tree.PositionalArgument> iter, java.util.List<ProducedType> expressionTypes, boolean spread) {
        if (iter.hasNext()) {
            // no erasure here? really?
            Tree.PositionalArgument expr = iter.next();
            JCExpression first;
            if(expr instanceof Tree.ListedArgument)
                first = transformExpression(((Tree.ListedArgument) expr).getExpression());
            else if(expr instanceof Tree.SpreadArgument){
                first = transformExpression(((Tree.SpreadArgument) expr).getExpression());
            }else{
                Tree.Comprehension comp = (Tree.Comprehension) expr;
                ProducedType elementType = expressionTypes.get(0);
                ProducedType expectedType = comp.getForComprehensionClause().getPossiblyEmpty() 
                        ? typeFact().getSequentialType(elementType)
                        : typeFact().getSequenceType(elementType);
                first = comprehensionAsSequential(comp, expectedType);
            }
            // last one with ellipsis is just a sequence
            if(!iter.hasNext() && spread)
                return first;
            // make the tuple type
            ProducedType tupleType = typeFact().getTupleType(expressionTypes, spread, false /* FIXME: What? */, -1);
            JCExpression typeExpr = makeJavaType(tupleType, CeylonTransformer.JT_CLASS_NEW);
            // get the rest of the tuple
            expressionTypes.remove(0);
            JCExpression rest = makeTuple(iter, expressionTypes, spread);
            // make the tuple
            return makeNewClass(typeExpr, List.of(first, rest));
        } else {
            // nothing in there
            return makeEmpty();
        }
    }
    
    public JCTree transform(Tree.This expr) {
        at(expr);
        if (needDollarThis(expr.getScope())) {
            return naming.makeQuotedThis();
        }
        if (isWithinCallableInvocation()) {
            return naming.makeQualifiedThis(makeJavaType(expr.getTypeModel()));
        } 
        return naming.makeThis();
    }

    public JCTree transform(Tree.Super expr) {
        at(expr);
        return naming.makeSuper();
    }

    public JCTree transform(Tree.Outer expr) {
        at(expr);
        
        ProducedType outerClass = com.redhat.ceylon.compiler.typechecker.model.Util.getOuterClassOrInterface(expr.getScope());
        if (outerCompanion) {
            return naming.makeQuotedThis();
        }
        final TypeDeclaration outerDeclaration = outerClass.getDeclaration();
        if (outerDeclaration instanceof Interface) {
            return naming.makeQualifiedThis(makeJavaType(outerClass, JT_COMPANION | JT_RAW));
        }
        return naming.makeQualifiedThis(makeJavaType(outerClass));
    }

    //
    // Unary and Binary operators that can be overridden
    
    //
    // Unary operators

    public JCExpression transform(Tree.NotOp op) {
        // No need for an erasure cast since Term must be Boolean and we never need to erase that
        JCExpression term = transformExpression(op.getTerm(), CodegenUtil.getBoxingStrategy(op), null);
        JCUnary jcu = at(op).Unary(JCTree.NOT, term);
        return jcu;
    }

    public JCExpression transform(Tree.OfOp op) {
        ProducedType exprType = op.getTerm().getTypeModel();
        ProducedType expectedType = op.getType().getTypeModel();
        // Self type as a type arg:
        boolean selfTypeCast = exprType.getDeclaration().getSelfType() != null;
        if (selfTypeCast) {
            return transformExpression(op.getTerm(), CodegenUtil.getBoxingStrategy(op), expectedType);
        } else {
            return transformExpression(op.getTerm());
        }
    }

    public JCExpression transform(Tree.IsOp op) {
        // we don't need any erasure type cast for an "is" test
        JCExpression expression = transformExpression(op.getTerm());
        at(op);
        Naming.SyntheticName varName = naming.temp();
        JCExpression test = makeTypeTest(null, varName, op.getType().getTypeModel());
        return makeLetExpr(varName, List.<JCStatement>nil(), make().Type(syms().objectType), expression, test);
    }

    public JCTree transform(Tree.Nonempty op) {
        // we don't need any erasure type cast for a "nonempty" test
        JCExpression expression = transformExpression(op.getTerm());
        at(op);
        Naming.SyntheticName varName = naming.temp();
        JCExpression test = makeNonEmptyTest(varName.makeIdent());
        return makeLetExpr(varName, List.<JCStatement>nil(), make().Type(syms().objectType), expression, test);
    }

    public JCTree transform(Tree.Exists op) {
        // for the purpose of checking if something is null, we need it boxed and optional, otherwise
        // for some Java calls if we consider it non-optional we will get an unwanted null check
        ProducedType termType = op.getTerm().getTypeModel();
        if(!typeFact().isOptionalType(termType)){
            termType = typeFact().getOptionalType(termType);
        }
        JCExpression expression = transformExpression(op.getTerm(), BoxingStrategy.BOXED, termType);
        at(op);
        return  make().Binary(JCTree.NE, expression, makeNull());
    }

    public JCExpression transform(Tree.PositiveOp op) {
        return transformOverridableUnaryOperator(op, op.getUnit().getInvertableDeclaration());
    }

    public JCExpression transform(Tree.NegativeOp op) {
        if (op.getTerm() instanceof Tree.NaturalLiteral) {
            // To cope with -9223372036854775808 we can't just parse the 
            // number separately from the sign
            return integerLiteral(op.getTerm(), "-" + op.getTerm().getText());
        }
        return transformOverridableUnaryOperator(op, op.getUnit().getInvertableDeclaration());
    }

    public JCExpression transform(Tree.UnaryOperatorExpression op) {
        return transformOverridableUnaryOperator(op, (ProducedType)null);
    }

    private JCExpression transformOverridableUnaryOperator(Tree.UnaryOperatorExpression op, Interface compoundType) {
        ProducedType leftType = getSupertype(op.getTerm(), compoundType);
        return transformOverridableUnaryOperator(op, leftType);
    }
    
    private JCExpression transformOverridableUnaryOperator(Tree.UnaryOperatorExpression op, ProducedType expectedType) {
        at(op);
        Tree.Term term = op.getTerm();

        OperatorTranslation operator = Operators.getOperator(op.getClass());
        if (operator == null) {
            return makeErroneous(op);
        }

        if(operator.getOptimisationStrategy(op, this).useJavaOperator()){
            // optimisation for unboxed types
            JCExpression expr = transformExpression(term, BoxingStrategy.UNBOXED, expectedType);
            // unary + is essentially a NOOP
            if(operator == OperatorTranslation.UNARY_POSITIVE)
                return expr;
            return make().Unary(operator.javacOperator, expr);
        }
        
        return make().Apply(null, makeSelect(transformExpression(term, BoxingStrategy.BOXED, expectedType), 
                Naming.getGetterName(operator.ceylonMethod)), List.<JCExpression> nil());
    }

    //
    // Binary operators
    
    public JCExpression transform(Tree.NotEqualOp op) {
        OperatorTranslation operator = Operators.OperatorTranslation.BINARY_EQUAL;
        OptimisationStrategy optimisationStrategy = operator.getOptimisationStrategy(op, this);
        
        // we want it unboxed only if the operator is optimised
        // we don't care about the left erased type, since equals() is on Object
        JCExpression left = transformExpression(op.getLeftTerm(), optimisationStrategy.getBoxingStrategy(), null);
        // we don't care about the right erased type, since equals() is on Object
        JCExpression expr = transformOverridableBinaryOperator(op, operator, optimisationStrategy, left, null);
        return at(op).Unary(JCTree.NOT, expr);
    }

    public JCExpression transform(Tree.SegmentOp op) {
        // FIXME: https://github.com/ceylon/ceylon-compiler/issues/836
        return makeErroneous(op, "Segment operators not implemented yet");
    }

    public JCExpression transform(Tree.RangeOp op) {
        // we need to get the range bound type
        ProducedType comparableType = getSupertype(op.getLeftTerm(), op.getUnit().getComparableDeclaration());
        ProducedType paramType = getTypeArgument(comparableType);
        JCExpression lower = transformExpression(op.getLeftTerm(), BoxingStrategy.BOXED, paramType);
        JCExpression upper = transformExpression(op.getRightTerm(), BoxingStrategy.BOXED, paramType);
        ProducedType rangeType = typeFact().getRangeType(op.getLeftTerm().getTypeModel());
        JCExpression typeExpr = makeJavaType(rangeType, CeylonTransformer.JT_CLASS_NEW);
        return at(op).NewClass(null, null, typeExpr, List.<JCExpression> of(lower, upper), null);
    }

    public JCExpression transform(Tree.EntryOp op) {
        // no erasure cast needed for both terms
        JCExpression key = transformExpression(op.getLeftTerm());
        JCExpression elem = transformExpression(op.getRightTerm());
        ProducedType entryType = typeFact().getEntryType(op.getLeftTerm().getTypeModel(), op.getRightTerm().getTypeModel());
        JCExpression typeExpr = makeJavaType(entryType, CeylonTransformer.JT_CLASS_NEW);
        return at(op).NewClass(null, null, typeExpr , List.<JCExpression> of(key, elem), null);
    }

    public JCTree transform(Tree.DefaultOp op) {
        JCExpression left = transformExpression(op.getLeftTerm(), BoxingStrategy.BOXED, typeFact().getOptionalType(op.getTypeModel()));
        JCExpression right = transformExpression(op.getRightTerm(), BoxingStrategy.BOXED, op.getTypeModel());
        Naming.SyntheticName varName = naming.temp();
        JCExpression varIdent = varName.makeIdent();
        JCExpression test = at(op).Binary(JCTree.NE, varIdent, makeNull());
        JCExpression cond = make().Conditional(test , varIdent, right);
        JCExpression typeExpr = makeJavaType(op.getTypeModel(), JT_NO_PRIMITIVES);
        return makeLetExpr(varName, null, typeExpr, left, cond);
    }

    public JCTree transform(Tree.ThenOp op) {
        JCExpression left = transformExpression(op.getLeftTerm(), BoxingStrategy.UNBOXED, typeFact().getBooleanDeclaration().getType());
        JCExpression right = transformExpression(op.getRightTerm(), CodegenUtil.getBoxingStrategy(op), op.getTypeModel());
        return make().Conditional(left , right, makeNull());
    }
    
    public JCTree transform(Tree.InOp op) {
        JCExpression left = transformExpression(op.getLeftTerm(), BoxingStrategy.BOXED, typeFact().getObjectDeclaration().getType());
        JCExpression right = transformExpression(op.getRightTerm(), BoxingStrategy.BOXED, typeFact().getCategoryDeclaration().getType());
        Naming.SyntheticName varName = naming.temp();
        JCExpression varIdent = varName.makeIdent();
        JCExpression contains = at(op).Apply(null, makeSelect(right, "contains"), List.<JCExpression> of(varIdent));
        JCExpression typeExpr = makeJavaType(op.getLeftTerm().getTypeModel(), JT_NO_PRIMITIVES);
        return makeLetExpr(varName, null, typeExpr, left, contains);
    }

    // Logical operators
    
    public JCExpression transform(Tree.LogicalOp op) {
        OperatorTranslation operator = Operators.getOperator(op.getClass());
        if(operator == null){
            return makeErroneous(op, "Not supported yet: "+op.getNodeType());
        }
        // Both terms are Booleans and can't be erased to anything
        JCExpression left = transformExpression(op.getLeftTerm(), BoxingStrategy.UNBOXED, null);
        return transformLogicalOp(op, operator, left, op.getRightTerm());
    }

    private JCExpression transformLogicalOp(Node op, OperatorTranslation operator, 
            JCExpression left, Tree.Term rightTerm) {
        // Both terms are Booleans and can't be erased to anything
        JCExpression right = transformExpression(rightTerm, BoxingStrategy.UNBOXED, null);

        return at(op).Binary(operator.javacOperator, left, right);
    }

    // Comparison operators
    
    public JCExpression transform(Tree.IdenticalOp op){
        // The only thing which might be unboxed is boolean, and we can follow the rules of == for optimising it,
        // which are simple and require that both types be booleans to be unboxed, otherwise they must be boxed
        OptimisationStrategy optimisationStrategy = OperatorTranslation.BINARY_EQUAL.getOptimisationStrategy(op, this);
        JCExpression left = transformExpression(op.getLeftTerm(), optimisationStrategy.getBoxingStrategy(), null);
        JCExpression right = transformExpression(op.getRightTerm(), optimisationStrategy.getBoxingStrategy(), null);
        return at(op).Binary(JCTree.EQ, left, right);
    }
    
    public JCExpression transform(Tree.ComparisonOp op) {
        return transformOverridableBinaryOperator(op, op.getUnit().getComparableDeclaration());
    }

    public JCExpression transform(Tree.CompareOp op) {
        return transformOverridableBinaryOperator(op, op.getUnit().getComparableDeclaration());
    }

    // Arithmetic operators
    
    public JCExpression transform(Tree.ArithmeticOp op) {
        return transformOverridableBinaryOperator(op, op.getUnit().getNumericDeclaration());
    }
    
    public JCExpression transform(Tree.SumOp op) {
        return transformOverridableBinaryOperator(op, op.getUnit().getSummableDeclaration());
    }

    public JCExpression transform(Tree.RemainderOp op) {
        return transformOverridableBinaryOperator(op, op.getUnit().getIntegralDeclaration());
    }
    
    public JCExpression transform(Tree.BitwiseOp op) {
    	JCExpression result = transformOverridableBinaryOperator(op, null, null);
    	return result;
    }    

    // Overridable binary operators
    
    public JCExpression transform(Tree.BinaryOperatorExpression op) {
        return transformOverridableBinaryOperator(op, null, null);
    }

    private JCExpression transformOverridableBinaryOperator(Tree.BinaryOperatorExpression op, Interface compoundType) {
        ProducedType leftType = getSupertype(op.getLeftTerm(), compoundType);
        ProducedType rightType = getTypeArgument(leftType);
        return transformOverridableBinaryOperator(op, leftType, rightType);
    }

    private JCExpression transformOverridableBinaryOperator(Tree.BinaryOperatorExpression op, ProducedType leftType, ProducedType rightType) {
        OperatorTranslation operator = Operators.getOperator(op.getClass());
        if (operator == null) {
            return makeErroneous(op);
        }
        OptimisationStrategy optimisationStrategy = operator.getOptimisationStrategy(op, this);

        JCExpression left = transformExpression(op.getLeftTerm(), optimisationStrategy.getBoxingStrategy(), leftType);
        return transformOverridableBinaryOperator(op, operator, optimisationStrategy, left, rightType);
    }

    private JCExpression transformOverridableBinaryOperator(Tree.BinaryOperatorExpression op, 
            OperatorTranslation originalOperator, OptimisationStrategy optimisatonStrategy, 
            JCExpression left, ProducedType rightType) {
        JCExpression result = null;
        
        JCExpression right = transformExpression(op.getRightTerm(), optimisatonStrategy.getBoxingStrategy(), rightType);

        // optimise if we can
        if(optimisatonStrategy.useJavaOperator()){
            return make().Binary(originalOperator.javacOperator, left, right);
        }

        boolean loseComparison = 
                originalOperator == OperatorTranslation.BINARY_SMALLER 
                || originalOperator == OperatorTranslation.BINARY_SMALL_AS 
                || originalOperator == OperatorTranslation.BINARY_LARGER
                || originalOperator == OperatorTranslation.BINARY_LARGE_AS;

        // for comparisons we need to invoke compare()
        OperatorTranslation actualOperator = originalOperator;
        if (loseComparison) {
            actualOperator = Operators.OperatorTranslation.BINARY_COMPARE;
            if (actualOperator == null) {
                return makeErroneous();
            }
        }

        result = at(op).Apply(null, makeSelect(left, actualOperator.ceylonMethod), List.of(right));

        if (loseComparison) {
            result = at(op).Apply(null, makeSelect(result, originalOperator.ceylonMethod), List.<JCExpression> nil());
        }

        return result;
    }

    //
    // Operator-Assignment expressions

    public JCExpression transform(final Tree.ArithmeticAssignmentOp op){
        final AssignmentOperatorTranslation operator = Operators.getAssignmentOperator(op.getClass());
        if(operator == null){
            return makeErroneous(op, "Not supported yet: "+op.getNodeType());
        }

        // see if we can optimise it
        if(op.getUnboxed() && CodegenUtil.isDirectAccessVariable(op.getLeftTerm())){
            return optimiseAssignmentOperator(op, operator);
        }
        
        // we can use unboxed types if both operands are unboxed
        final boolean boxResult = !op.getUnboxed();
        
        // find the proper type
        Interface compoundType = op.getUnit().getNumericDeclaration();
        if(op instanceof Tree.AddAssignOp){
            compoundType = op.getUnit().getSummableDeclaration();
        }else if(op instanceof Tree.RemainderAssignOp){
            compoundType = op.getUnit().getIntegralDeclaration();
        }
        
        final ProducedType leftType = getSupertype(op.getLeftTerm(), compoundType);
        final ProducedType rightType = getMostPreciseType(op.getLeftTerm(), getTypeArgument(leftType, 0));

        // we work on boxed types
        return transformAssignAndReturnOperation(op, op.getLeftTerm(), boxResult, 
                leftType, rightType, 
                new AssignAndReturnOperationFactory(){
            @Override
            public JCExpression getNewValue(JCExpression previousValue) {
                // make this call: previousValue OP RHS
                JCExpression ret = transformOverridableBinaryOperator(op, operator.binaryOperator, 
                        boxResult ? OptimisationStrategy.NONE : OptimisationStrategy.OPTIMISE, 
                        previousValue, rightType);
                ret = unAutoPromote(ret, rightType);
                return ret;
            }
        });
    }

    public JCExpression transform(final Tree.BitwiseAssignmentOp op){
        final AssignmentOperatorTranslation operator = Operators.getAssignmentOperator(op.getClass());
        if(operator == null){
            return makeErroneous(op, "Not supported yet: "+op.getNodeType());
        }
    	
        ProducedType valueType = op.getLeftTerm().getTypeModel();
        
        return transformAssignAndReturnOperation(op, op.getLeftTerm(), false, valueType, valueType, new AssignAndReturnOperationFactory() {
            @Override
            public JCExpression getNewValue(JCExpression previousValue) {
            	JCExpression result = transformOverridableBinaryOperator(op, operator.binaryOperator, OptimisationStrategy.NONE, previousValue, null);
            	return result;
            }
        });
    }

    public JCExpression transform(final Tree.LogicalAssignmentOp op){
        final AssignmentOperatorTranslation operator = Operators.getAssignmentOperator(op.getClass());
        if(operator == null){
            return makeErroneous(op, "Not supported yet: "+op.getNodeType());
        }
        
        // optimise if we can
        if(CodegenUtil.isDirectAccessVariable(op.getLeftTerm())){
            return optimiseAssignmentOperator(op, operator);
        }
        
        ProducedType valueType = op.getLeftTerm().getTypeModel();
        // we work on unboxed types
        return transformAssignAndReturnOperation(op, op.getLeftTerm(), false, 
                valueType, valueType, new AssignAndReturnOperationFactory(){
            @Override
            public JCExpression getNewValue(JCExpression previousValue) {
                // make this call: previousValue OP RHS
                return transformLogicalOp(op, operator.binaryOperator, 
                        previousValue, op.getRightTerm());
            }
        });
    }

    private JCExpression optimiseAssignmentOperator(final Tree.AssignmentOp op, final AssignmentOperatorTranslation operator) {
        // we don't care about their types since they're unboxed and we know it
        JCExpression left = transformExpression(op.getLeftTerm(), BoxingStrategy.UNBOXED, null);
        JCExpression right = transformExpression(op.getRightTerm(), BoxingStrategy.UNBOXED, null);
        return at(op).Assignop(operator.javacOperator, left, right);
    }

    // Postfix operator
    
    public JCExpression transform(Tree.PostfixOperatorExpression expr) {
        OperatorTranslation operator = Operators.getOperator(expr.getClass());
        if(operator == null){
            return makeErroneous(expr, "Not supported yet: "+expr.getNodeType());
        }
        
        OptimisationStrategy optimisationStrategy = operator.getOptimisationStrategy(expr, this);
        boolean canOptimise = optimisationStrategy.useJavaOperator();
        
        // only fully optimise if we don't have to access the getter/setter
        if(canOptimise && CodegenUtil.isDirectAccessVariable(expr.getTerm())){
            JCExpression term = transformExpression(expr.getTerm(), BoxingStrategy.UNBOXED, expr.getTypeModel());
            return at(expr).Unary(operator.javacOperator, term);
        }
        
        Tree.Term term = expr.getTerm();

        Interface compoundType = expr.getUnit().getOrdinalDeclaration();
        ProducedType valueType = getSupertype(expr.getTerm(), compoundType);
        ProducedType returnType = getMostPreciseType(term, getTypeArgument(valueType, 0));

        List<JCVariableDecl> decls = List.nil();
        List<JCStatement> stats = List.nil();
        JCExpression result = null;
        // we can optimise that case a bit sometimes
        boolean boxResult = !canOptimise;

        // attr++
        // (let $tmp = attr; attr = $tmp.getSuccessor(); $tmp;)
        if(term instanceof Tree.BaseMemberExpression){
            JCExpression getter = transform((Tree.BaseMemberExpression)term, null);
            at(expr);
            // Type $tmp = attr
            JCExpression exprType = makeJavaType(returnType, boxResult ? JT_NO_PRIMITIVES : 0);
            Name varName = naming.tempName("op");
            // make sure we box the results if necessary
            getter = applyErasureAndBoxing(getter, term, boxResult ? BoxingStrategy.BOXED : BoxingStrategy.UNBOXED, returnType);
            JCVariableDecl tmpVar = make().VarDef(make().Modifiers(0), varName, exprType, getter);
            decls = decls.prepend(tmpVar);

            // attr = $tmp.getSuccessor()
            JCExpression successor;
            if(canOptimise){
                // use +1/-1 if we can optimise a bit
                successor = make().Binary(operator == OperatorTranslation.UNARY_POSTFIX_INCREMENT ? JCTree.PLUS : JCTree.MINUS, 
                        make().Ident(varName), makeInteger(1));
                successor = unAutoPromote(successor, returnType);
            }else{
                successor = make().Apply(null, 
                                         makeSelect(make().Ident(varName), operator.ceylonMethod), 
                                         List.<JCExpression>nil());
                // make sure the result is boxed if necessary, the result of successor/predecessor is always boxed
                successor = boxUnboxIfNecessary(successor, true, term.getTypeModel(), CodegenUtil.getBoxingStrategy(term));
            }
            JCExpression assignment = transformAssignment(expr, term, successor);
            stats = stats.prepend(at(expr).Exec(assignment));

            // $tmp
            result = make().Ident(varName);
        }
        else if(term instanceof Tree.QualifiedMemberExpression){
            // e.attr++
            // (let $tmpE = e, $tmpV = $tmpE.attr; $tmpE.attr = $tmpV.getSuccessor(); $tmpV;)
            Tree.QualifiedMemberExpression qualified = (Tree.QualifiedMemberExpression) term;
            boolean isSuper = qualified.getPrimary() instanceof Super;
            // transform the primary, this will get us a boxed primary 
            JCExpression e = transformQualifiedMemberPrimary(qualified);
            at(expr);
            
            // Type $tmpE = e
            JCExpression exprType = makeJavaType(qualified.getTarget().getQualifyingType(), JT_NO_PRIMITIVES);
            Name varEName = naming.tempName("opE");
            JCVariableDecl tmpEVar = make().VarDef(make().Modifiers(0), varEName, exprType, e);

            // Type $tmpV = $tmpE.attr
            JCExpression attrType = makeJavaType(returnType, boxResult ? JT_NO_PRIMITIVES : 0);
            Name varVName = naming.tempName("opV");
            JCExpression getter = transformMemberExpression(qualified, isSuper ? naming.makeSuper() : make().Ident(varEName), null);
            // make sure we box the results if necessary
            getter = applyErasureAndBoxing(getter, term, boxResult ? BoxingStrategy.BOXED : BoxingStrategy.UNBOXED, returnType);
            JCVariableDecl tmpVVar = make().VarDef(make().Modifiers(0), varVName, attrType, getter);

            decls = decls.prepend(tmpVVar);
            if (!isSuper) {
                // define all the variables
                decls = decls.prepend(tmpEVar);
            }
            
            // $tmpE.attr = $tmpV.getSuccessor()
            JCExpression successor;
            if(canOptimise){
                // use +1/-1 if we can optimise a bit
                successor = make().Binary(operator == OperatorTranslation.UNARY_POSTFIX_INCREMENT ? JCTree.PLUS : JCTree.MINUS, 
                        make().Ident(varVName), makeInteger(1));
                successor = unAutoPromote(successor, returnType);
            }else{
                successor = make().Apply(null, 
                                         makeSelect(make().Ident(varVName), operator.ceylonMethod), 
                                         List.<JCExpression>nil());
                //  make sure the result is boxed if necessary, the result of successor/predecessor is always boxed
                successor = boxUnboxIfNecessary(successor, true, term.getTypeModel(), CodegenUtil.getBoxingStrategy(term));
            }
            JCExpression assignment = transformAssignment(expr, term, isSuper ? naming.makeSuper() : make().Ident(varEName), successor);
            stats = stats.prepend(at(expr).Exec(assignment));
            
            // $tmpV
            result = make().Ident(varVName);
        }else{
            return makeErroneous(term, "Not supported yet");
        }
        // e?.attr++ is probably not legal
        // a[i]++ is not for M1 but will be:
        // (let $tmpA = a, $tmpI = i, $tmpV = $tmpA.item($tmpI); $tmpA.setItem($tmpI, $tmpV.getSuccessor()); $tmpV;)
        // a?[i]++ is probably not legal
        // a[i1..i1]++ and a[i1...]++ are probably not legal
        // a[].attr++ and a[].e.attr++ are probably not legal

        return make().LetExpr(decls, stats, result);
    }
    
    // Prefix operator
    
    public JCExpression transform(final Tree.PrefixOperatorExpression expr) {
        final OperatorTranslation operator = Operators.getOperator(expr.getClass());
        if(operator == null){
            return makeErroneous(expr, "Not supported yet: "+expr.getNodeType());
        }
        
        OptimisationStrategy optimisationStrategy = operator.getOptimisationStrategy(expr, this);
        final boolean canOptimise = optimisationStrategy.useJavaOperator();
        
        Term term = expr.getTerm();
        // only fully optimise if we don't have to access the getter/setter
        if(canOptimise && CodegenUtil.isDirectAccessVariable(term)){
            JCExpression jcTerm = transformExpression(term, BoxingStrategy.UNBOXED, expr.getTypeModel());
            return at(expr).Unary(operator.javacOperator, jcTerm);
        }

        Interface compoundType = expr.getUnit().getOrdinalDeclaration();
        ProducedType valueType = getSupertype(term, compoundType);
        final ProducedType returnType = getMostPreciseType(term, getTypeArgument(valueType, 0));
        
        // we work on boxed types unless we could have optimised
        return transformAssignAndReturnOperation(expr, term, !canOptimise, 
                valueType, returnType, new AssignAndReturnOperationFactory(){
            @Override
            public JCExpression getNewValue(JCExpression previousValue) {
                // use +1/-1 if we can optimise a bit
                if(canOptimise){
                    JCExpression ret = make().Binary(operator == OperatorTranslation.UNARY_PREFIX_INCREMENT ? JCTree.PLUS : JCTree.MINUS, 
                            previousValue, makeInteger(1));
                    ret = unAutoPromote(ret, returnType);
                    return ret;
                }
                // make this call: previousValue.getSuccessor() or previousValue.getPredecessor()
                return make().Apply(null, makeSelect(previousValue, operator.ceylonMethod), List.<JCExpression>nil());
            }
        });
    }
    
    //
    // Function to deal with expressions that have side-effects
    
    private interface AssignAndReturnOperationFactory {
        JCExpression getNewValue(JCExpression previousValue);
    }
    
    private JCExpression transformAssignAndReturnOperation(Node operator, Tree.Term term, 
            boolean boxResult, ProducedType valueType, ProducedType returnType, 
            AssignAndReturnOperationFactory factory){
        
        List<JCVariableDecl> decls = List.nil();
        List<JCStatement> stats = List.nil();
        JCExpression result = null;
        // attr
        // (let $tmp = OP(attr); attr = $tmp; $tmp)
        if(term instanceof Tree.BaseMemberExpression){
            JCExpression getter = transform((Tree.BaseMemberExpression)term, null);
            at(operator);
            // Type $tmp = OP(attr);
            JCExpression exprType = makeJavaType(returnType, boxResult ? JT_NO_PRIMITIVES : 0);
            Name varName = naming.tempName("op");
            // make sure we box the results if necessary
            getter = applyErasureAndBoxing(getter, term, boxResult ? BoxingStrategy.BOXED : BoxingStrategy.UNBOXED, valueType);
            JCExpression newValue = factory.getNewValue(getter);
            // no need to box/unbox here since newValue and $tmpV share the same boxing type
            JCVariableDecl tmpVar = make().VarDef(make().Modifiers(0), varName, exprType, newValue);
            decls = decls.prepend(tmpVar);

            // attr = $tmp
            // make sure the result is unboxed if necessary, $tmp may be boxed
            JCExpression value = make().Ident(varName);
            value = boxUnboxIfNecessary(value, boxResult, term.getTypeModel(), CodegenUtil.getBoxingStrategy(term));
            JCExpression assignment = transformAssignment(operator, term, value);
            stats = stats.prepend(at(operator).Exec(assignment));
            
            // $tmp
            // return, with the box type we asked for
            result = make().Ident(varName);
        }
        else if(term instanceof Tree.QualifiedMemberExpression){
            // e.attr
            // (let $tmpE = e, $tmpV = OP($tmpE.attr); $tmpE.attr = $tmpV; $tmpV;)
            Tree.QualifiedMemberExpression qualified = (Tree.QualifiedMemberExpression) term;
            boolean isSuper = qualified.getPrimary() instanceof Super;
            // transform the primary, this will get us a boxed primary 
            JCExpression e = transformQualifiedMemberPrimary(qualified);
            at(operator);
            
            // Type $tmpE = e
            JCExpression exprType = makeJavaType(qualified.getTarget().getQualifyingType(), JT_NO_PRIMITIVES);
            Name varEName = naming.tempName("opE");
            JCVariableDecl tmpEVar = make().VarDef(make().Modifiers(0), varEName, exprType, e);

            // Type $tmpV = OP($tmpE.attr)
            JCExpression attrType = makeJavaType(returnType, boxResult ? JT_NO_PRIMITIVES : 0);
            Name varVName = naming.tempName("opV");
            JCExpression getter = transformMemberExpression(qualified, isSuper ? naming.makeSuper() : make().Ident(varEName), null);
            // make sure we box the results if necessary
            getter = applyErasureAndBoxing(getter, term, boxResult ? BoxingStrategy.BOXED : BoxingStrategy.UNBOXED, valueType);
            JCExpression newValue = factory.getNewValue(getter);
            // no need to box/unbox here since newValue and $tmpV share the same boxing type
            JCVariableDecl tmpVVar = make().VarDef(make().Modifiers(0), varVName, attrType, newValue);

            // define all the variables
            decls = decls.prepend(tmpVVar);
            if (!isSuper) {
                decls = decls.prepend(tmpEVar);
            }
            
            // $tmpE.attr = $tmpV
            // make sure $tmpV is unboxed if necessary
            JCExpression value = make().Ident(varVName);
            value = boxUnboxIfNecessary(value, boxResult, term.getTypeModel(), CodegenUtil.getBoxingStrategy(term));
            JCExpression assignment = transformAssignment(operator, term, isSuper ? naming.makeSuper() : make().Ident(varEName), value);
            stats = stats.prepend(at(operator).Exec(assignment));
            
            // $tmpV
            // return, with the box type we asked for
            result = make().Ident(varVName);
        }else{
            return makeErroneous(operator, "Not supported yet");
        }
        // OP(e?.attr) is probably not legal
        // OP(a[i]) is not for M1 but will be:
        // (let $tmpA = a, $tmpI = i, $tmpV = OP($tmpA.item($tmpI)); $tmpA.setItem($tmpI, $tmpV); $tmpV;)
        // OP(a?[i]) is probably not legal
        // OP(a[i1..i1]) and OP(a[i1...]) are probably not legal
        // OP(a[].attr) and OP(a[].e.attr) are probably not legal

        return make().LetExpr(decls, stats, result);
    }


    public JCExpression transform(Tree.Parameter param) {
        // Transform the expression marking that we're inside a defaulted parameter for $this-handling
        //needDollarThis  = true;
        JCExpression expr;
        at(param);
        DefaultArgument defaultArgument = param.getDefaultArgument();
        if (defaultArgument != null) {
            SpecifierExpression spec = defaultArgument.getSpecifierExpression();
            if (spec.getScope() instanceof FunctionalParameter) {
                Tree.FunctionalParameterDeclaration fpTree = (FunctionalParameterDeclaration) param;
                FunctionalParameter fp = (FunctionalParameter)spec.getScope();
                Tree.SpecifierExpression lazy = param.getDefaultArgument().getSpecifierExpression();
                expr = CallableBuilder.anonymous(gen(), lazy.getExpression(), 
                        fp.getParameterLists().get(0),
                        fpTree.getParameterLists().get(0),
                        getTypeForFunctionalParameter(fp)).build();
            } else {
                expr = expressionGen().transformExpression(spec.getExpression(), CodegenUtil.getBoxingStrategy(param.getDeclarationModel()), param.getDeclarationModel().getType());
            }
        } else if (param.getDeclarationModel().isSequenced()) {
            expr = makeEmptyAsSequential(true);
        } else {
            expr = makeErroneous(param, "No default and not sequenced");
        }
        //needDollarThis = false;
        return expr;
    }
    
    protected final JCExpression transformArg(SimpleInvocation invocation, int argIndex) {
        final Tree.Term expr = invocation.getArgumentExpression(argIndex);
        if (invocation.hasParameter(argIndex)) {
            ProducedType type = invocation.getParameterType(argIndex);
            if (invocation.isParameterSequenced(argIndex)
                    // Java methods need their underlying type preserved
                    && !invocation.isJavaMethod()
                    && !invocation.isArgumentSpread(argIndex)) {
                // If the parameter is sequenced and the argument is not ...
                // then the expected type of the *argument* is the type arg to Iterator
                type = typeFact().getIteratedType(type);
            }
            BoxingStrategy boxingStrategy = invocation.getParameterBoxingStrategy(argIndex);
            int flags = 0;
            if(!invocation.isParameterRaw(argIndex))
                flags |= ExpressionTransformer.EXPR_EXPECTED_TYPE_NOT_RAW;
            if(invocation.isParameterWithConstrainedTypeParameters(argIndex))
                flags |= ExpressionTransformer.EXPR_EXPECTED_TYPE_HAS_CONSTRAINED_TYPE_PARAMETERS;
            JCExpression ret = transformExpression(expr, 
                    boxingStrategy, 
                    type, flags);
            return ret;
        } else {
            // Overloaded methods don't have a reference to a parameter
            // so we have to treat them differently. Also knowing it's
            // overloaded we know we're dealing with Java code so we unbox
            ProducedType type = expr.getTypeModel();
            return expressionGen().transformExpression(expr, 
                    BoxingStrategy.UNBOXED, 
                    type);
        }
    }
    
    private boolean needsTypeInfoArgument(Invocation invocation) {
        if (invocation.getPrimaryDeclaration() instanceof LazyMethod) {
            if ("ceylon.language".equals(invocation.getPrimaryDeclaration().getContainer().getQualifiedNameString())) {
                String name = invocation.getPrimaryDeclaration().getName();
                return ("array".equals(name) || "arrayOfSize".equals(name));
            }
        }
        return false;
    }

    private JCExpression makeTypeInfoArgument(Invocation invocation) {
        Tree.BaseMemberExpression bme = (Tree.BaseMemberExpression) invocation.getPrimary();
        ProducedType type = bme.getTypeArguments().getTypeModels().get(0);
        ProducedType simpleType = simplifyType(type);
        JCExpression typeExpr;
        if (simpleType.getDeclaration() instanceof TypeParameter
                || typeFact().isUnion(simpleType)
                || typeFact().isIntersection(simpleType)) {
            return makeNull();
        } else {
            typeExpr = makeJavaType(type, JT_RAW);
            return makeSelect(typeExpr, "class");
        }
    }
    
    private final List<ExpressionAndType> transformArgumentList(Invocation invocation, TransformedInvocationPrimary transformedPrimary) {
        // don't try to work on broken stuff
        if (!invocation.validNumberOfParameters()) {
            return List.<ExpressionAndType>nil();
        }
        return transformArguments(invocation, transformedPrimary);   
    }
    
    private final List<ExpressionAndType> transformArguments(Invocation invocation,
            TransformedInvocationPrimary transformedPrimary) {
        List<ExpressionAndType> result = List.<ExpressionAndType>nil();
        withinInvocation(false);
        // Implicit arguments
        if (needsTypeInfoArgument(invocation)) {
            result = result.append(new ExpressionAndType(makeTypeInfoArgument(invocation), make().Type(syms().classType)));
        }
        if (!(invocation.getPrimary() instanceof Tree.BaseTypeExpression)
                && !(invocation.getPrimary() instanceof Tree.QualifiedTypeExpression)
                && invocation.isOnValueType() 
                && transformedPrimary != null) {
            result = result.append(new ExpressionAndType(transformedPrimary.expr,
                    makeJavaType(invocation.getPrimary().getTypeModel())));   
        }
        // Explicit arguments
        if (invocation instanceof SuperInvocation) {
            withinSuperInvocation(((SuperInvocation)invocation).getSub());
            result = result.appendList(transformArgumentsForSimpleInvocation((SimpleInvocation)invocation));
            withinSuperInvocation(null);
        } else if (invocation instanceof NamedArgumentInvocation) {
            result = result.appendList(transformArgumentsForNamedInvocation((NamedArgumentInvocation)invocation));
        } else if (invocation instanceof CallableSpecifierInvocation) {
            result = result.appendList(transformArgumentsForCallableSpecifier((CallableSpecifierInvocation)invocation));
        } else if (invocation instanceof SimpleInvocation) {
            result = result.appendList(transformArgumentsForSimpleInvocation((SimpleInvocation)invocation));
        } else {
            throw Assert.fail();
        }
        withinInvocation(true);
        return result;
    }
    
    private List<ExpressionAndType> transformArgumentsForSimpleInvocation(SimpleInvocation invocation) {
        List<ExpressionAndType> result = List.<ExpressionAndType>nil();
        int numArguments = invocation.getNumArguments();
        boolean wrapIntoArray = false;
        ListBuffer<JCExpression> arrayWrap = new ListBuffer<JCExpression>();
        for (int argIndex = 0; argIndex < numArguments; argIndex++) {
            final JCExpression expr;
            final JCExpression type;
            BoxingStrategy boxingStrategy = invocation.getParameterBoxingStrategy(argIndex);
            ProducedType parameterType = invocation.getParameterType(argIndex);
            // for Java methods of variadic primitives, it's better to wrap them ourselves into an array
            // to avoid ambiguity of foo(1,2) for foo(int...) and foo(Object...) methods
            if(!wrapIntoArray
                    && invocation.isParameterSequenced(argIndex)
                    && invocation.isJavaMethod()
                    && boxingStrategy == BoxingStrategy.UNBOXED
                    && willEraseToPrimitive(typeFact().getDefiniteType(parameterType))
                    && !invocation.isSpread())
                wrapIntoArray = true;
            // if it's not sequenced it's not special
            if (!invocation.isParameterSequenced(argIndex)
                    // if it's spread and not Java, we pass it along
                    || (invocation.isArgumentSpread(argIndex) && !invocation.isJavaMethod())
                    // if it's sequenced, Java and there's no spread at all, pass it along
                    || (invocation.isParameterSequenced(argIndex) && invocation.isJavaMethod() && !invocation.isSpread())) {
                expr = invocation.getTransformedArgumentExpression(argIndex);
                type = makeJavaType(invocation.getParameterType(argIndex), boxingStrategy == BoxingStrategy.BOXED ? JT_NO_PRIMITIVES : 0);
            } else {
                final ProducedType iteratedType = typeFact().getIteratedType(parameterType);

                // we must have a sequenced param
                if(invocation.isSpread()){
                    // we can have several remaining arguments and the last one is spread
                    List<JCExpression> x = List.<JCExpression>nil();
                    for ( ; argIndex < numArguments; argIndex++) {
                        JCExpression argExpr = invocation.getTransformedArgumentExpression(argIndex);
                        // the last parameter is spread and must be put first
                        if(argIndex < numArguments - 1)
                            x = x.append(argExpr);
                        else
                            x = x.prepend(argExpr);
                    }
                    if(invocation.isJavaMethod()){
                        // collect all the initial arguments and wrap into a Java array
                        // first arg is the spread part
                        JCExpression last = x.head;
                        // remove it from x
                        x = x.tail;
                        
                        ProducedType lastType = invocation.getArgumentExpression(numArguments-1).getTypeModel();

                        // must translate it into a Util call
                        expr = sequenceToJavaArray(last, parameterType, boxingStrategy, lastType, x);
                    }else{
                        JCExpression typeExpr = makeJavaType(iteratedType, JT_TYPE_ARGUMENT);
                        expr = makeUtilInvocation("sequentialInstance", x, List.of(typeExpr));
                    }
                }else{
                    // collect each remaining argument and box with an ArraySequence<T>
                    List<JCExpression> x = List.<JCExpression>nil();
                    boolean prev = this.uninitializedOperand(true);
                    for ( ; argIndex < numArguments; argIndex++) {
                        x = x.append(invocation.getTransformedArgumentExpression(argIndex));
                    }
                    this.uninitializedOperand(prev);
                    expr = makeSequence(x, iteratedType, JT_TYPE_ARGUMENT);

                }
                type = makeJavaType(typeFact().getSequenceType(iteratedType).getType());
            }
            if(!wrapIntoArray)
                result = result.append(new ExpressionAndType(expr, type));
            else
                arrayWrap.append(expr);
        }
        if(wrapIntoArray){
            // must have at least one arg, so take the last one
            ProducedType parameterType = invocation.getParameterType(numArguments-1);
            JCExpression arrayType = makeJavaType(parameterType, JT_RAW);
            
            JCNewArray arrayExpr = make().NewArray(arrayType, List.<JCExpression>nil(), arrayWrap.toList());
            JCExpression arrayTypeExpr = make().TypeArray(makeJavaType(parameterType, JT_RAW));
            result = result.append(new ExpressionAndType(arrayExpr, arrayTypeExpr));
        }
        return result;
    }
    
    private List<ExpressionAndType> transformArgumentsForNamedInvocation(NamedArgumentInvocation invocation) {
        List<ExpressionAndType> result = List.<ExpressionAndType>nil();
        for (ExpressionAndType argAndType : invocation.getArgumentsAndTypes()) {
            result = result.append(argAndType);
        }
        return result;
    }
    
    private List<ExpressionAndType> transformArgumentsForCallableSpecifier(CallableSpecifierInvocation invocation) {
        List<ExpressionAndType> result = List.<ExpressionAndType>nil();
        int argIndex = 0;
        for(Parameter parameter : invocation.getMethod().getParameterLists().get(0).getParameters()) {
            ProducedType exprType = expressionGen().getTypeForParameter(parameter, null, this.TP_TO_BOUND);
            Parameter declaredParameter = invocation.getMethod().getParameterLists().get(0).getParameters().get(argIndex);
            
            JCExpression arg = naming.makeName(parameter, Naming.NA_MEMBER);
            
            arg = expressionGen().applyErasureAndBoxing(
                    arg, 
                    exprType, 
                    !parameter.getUnboxed(), 
                    BoxingStrategy.BOXED,// Callables always have boxed params 
                    declaredParameter.getType());
            result = result.append(new ExpressionAndType(arg, makeJavaType(declaredParameter.getType())));
            argIndex++;
        }
        return result;
    }
    
    public final JCExpression transformInvocation(final Invocation invocation) {
        boolean prevFnCall = withinInvocation(true);
        try {
            final CallBuilder callBuilder = CallBuilder.instance(this);
            if (invocation.getPrimary() instanceof Tree.StaticMemberOrTypeExpression){
                transformTypeArguments(callBuilder, 
                        ((Tree.StaticMemberOrTypeExpression)invocation.getPrimary()).getTypeArguments().getTypeModels());
            }
            // don't try to work on broken stuff
            if (!invocation.validNumberOfParameters()) {
                return makeErroneous(invocation.getNode(), "Invalid number of parameters");
            }
            if (invocation instanceof CallableSpecifierInvocation) {
                return transformCallableSpecifierInvocation(callBuilder, (CallableSpecifierInvocation)invocation);
            } else {
                at(invocation.getNode());
                JCExpression result = transformPrimary(invocation.getPrimary(), new TermTransformer() {
                    @Override
                    public JCExpression transform(JCExpression primaryExpr, String selector) {
                        TransformedInvocationPrimary transformedPrimary = invocation.transformPrimary(primaryExpr, selector);
                        boolean prev = uninitializedOperand(stacksUninitializedOperand(invocation));
                        callBuilder.argumentsAndTypes(transformArgumentList(invocation, transformedPrimary));
                        uninitializedOperand(prev);
                        JCExpression resultExpr;
                        if (invocation instanceof NamedArgumentInvocation) {
                            resultExpr = transformNamedArgumentInvocationOrInstantiation((NamedArgumentInvocation)invocation, callBuilder, transformedPrimary);
                        } else {
                            resultExpr = transformPositionalInvocationOrInstantiation(invocation, callBuilder, transformedPrimary);
                        }
                        return resultExpr;
                    }
                });
                return result;
                
            }
        } finally {
            withinInvocation(prevFnCall);
        }
    }

    protected JCExpression transformPositionalInvocationOrInstantiation(Invocation invocation, CallBuilder callBuilder, TransformedInvocationPrimary transformedPrimary) {
        JCExpression resultExpr;
        if (invocation.getPrimary() instanceof Tree.BaseTypeExpression) {
            resultExpr = transformBaseInstantiation(invocation, callBuilder, transformedPrimary);
        } else if (invocation.getPrimary() instanceof Tree.QualifiedTypeExpression) {
            resultExpr = transformQualifiedInstantiation(invocation, callBuilder, transformedPrimary);
        } else {   
            resultExpr = transformInvocation(invocation, callBuilder, transformedPrimary);
        }
        
        if(invocation.handleBoxing)
            resultExpr = applyErasureAndBoxing(resultExpr, invocation.getReturnType(), 
                    !invocation.unboxed, invocation.boxingStrategy, invocation.getReturnType());
        return resultExpr;
    }

    private JCExpression transformInvocation(Invocation invocation, CallBuilder callBuilder,
            TransformedInvocationPrimary transformedPrimary) {
        if (invocation.isOnValueType()) {
            JCExpression primTypeExpr = makeJavaType(invocation.getQmePrimary().getTypeModel(), JT_NO_PRIMITIVES);
            callBuilder.invoke(naming.makeQuotedQualIdent(primTypeExpr, transformedPrimary.selector));
        } else {
            callBuilder.invoke(naming.makeQuotedQualIdent(transformedPrimary.expr, transformedPrimary.selector));
        }
        return callBuilder.build();
    }

    private JCExpression transformQualifiedInstantiation(Invocation invocation, CallBuilder callBuilder,
            TransformedInvocationPrimary transformedPrimary) {
        
        Tree.QualifiedTypeExpression qte = (Tree.QualifiedTypeExpression)invocation.getPrimary();
        Declaration declaration = qte.getDeclaration();
        if (!Strategy.generateInstantiator(declaration)) {
            JCExpression qualifier;
            JCExpression qualifierType;
            if (declaration.getContainer() instanceof Interface
                    && !(qte.getPrimary() instanceof Tree.Outer)) {
                // When doing qualified invocation through an interface we need
                // to get the companion.
                Interface qualifyingInterface = (Interface)declaration.getContainer();
                qualifier = make().Apply(null, 
                        makeSelect(transformedPrimary.expr, getCompanionAccessorName(qualifyingInterface)), 
                        List.<JCExpression>nil());
                // But when the interface is local the accessor returns Object
                // so we need to cast it to the type of the companion
                if (Decl.isAncestorLocal(declaration)) {
                    qualifier = make().TypeCast(makeJavaType(qualifyingInterface.getType(), JT_COMPANION), qualifier);
                }
                qualifierType = makeJavaType(qualifyingInterface.getType(), JT_COMPANION);
            } else {
                qualifier = transformedPrimary.expr;
                if (declaration.getContainer() instanceof TypeDeclaration) {
                    qualifierType = makeJavaType(((TypeDeclaration)declaration.getContainer()).getType());
                } else {
                    qualifierType = null;
                }
            }
            ProducedType classType = (ProducedType)qte.getTarget();
            // Note: here we're not fully qualifying the class name because the JLS says that if "new" is qualified the class name
            // is qualified relative to it
            JCExpression type = makeJavaType(classType, AbstractTransformer.JT_CLASS_NEW | AbstractTransformer.JT_NON_QUALIFIED);
            callBuilder.evaluateArgumentsFirst(stacksUninitializedOperand(invocation) 
                    && hasBackwardBranches());
            callBuilder.instantiate(new ExpressionAndType(qualifier, qualifierType), type);
        } else {
            callBuilder.typeArguments(List.<JCExpression>nil());
            callBuilder.invoke(naming.makeInstantiatorMethodName(transformedPrimary.expr, (Class)declaration));
        }
        return callBuilder.build();
    }

    private JCExpression transformBaseInstantiation(Invocation invocation, CallBuilder callBuilder,
            TransformedInvocationPrimary transformedPrimary) {
        JCExpression resultExpr;
        Tree.BaseTypeExpression type = (Tree.BaseTypeExpression)invocation.getPrimary();
        Declaration declaration = type.getDeclaration();
        if (Strategy.generateInstantiator(declaration)) {
            resultExpr = callBuilder
                    .typeArguments(List.<JCExpression>nil())
                    .invoke(naming.makeInstantiatorMethodName(transformedPrimary.expr, (Class)declaration))
                    .build();
            if (Decl.isAncestorLocal(invocation.getPrimaryDeclaration())) {
                // $new method declared to return Object, so needs typecast
                resultExpr = make().TypeCast(makeJavaType(
                        ((TypeDeclaration)declaration).getType()), resultExpr);
            }
        } else {
            ProducedType classType = (ProducedType)type.getTarget();
            resultExpr = callBuilder
                    .instantiate(makeJavaType(classType, AbstractTransformer.JT_CLASS_NEW))
                    .evaluateArgumentsFirst(stacksUninitializedOperand(invocation) 
                            && hasBackwardBranches())
                    .build();
        }
        return resultExpr;
    }
    
    private JCExpression transformCallableSpecifierInvocation(CallBuilder callBuilder, CallableSpecifierInvocation invocation) {
        at(invocation.getNode());
        JCExpression result = callBuilder
            .invoke(naming.makeQuotedQualIdent(invocation.getCallable(), Naming.getCallableMethodName()))
            .argumentsAndTypes(transformArgumentList(invocation, null))
            .build();
        if(invocation.handleBoxing)
            result = applyErasureAndBoxing(result, invocation.getReturnType(), 
                    !invocation.unboxed, invocation.boxingStrategy, invocation.getReturnType());
        return result;
    }
    
    private final void transformTypeArguments(
            CallBuilder callBuilder,
            java.util.List<ProducedType> typeArguments) {
        if(typeArguments != null){
            for (ProducedType arg : typeArguments) {
                // cancel type parameters and go raw if we can't specify them
                if(willEraseToObject(arg) || willEraseToSequential(arg)) {
                    callBuilder.typeArguments(List.<JCExpression>nil());
                    return;
                }
                callBuilder.typeArgument(makeJavaType(arg, JT_TYPE_ARGUMENT));
            }
        }
    }
    
    protected JCExpression transformNamedArgumentInvocationOrInstantiation(NamedArgumentInvocation invocation, 
            CallBuilder callBuilder,
            TransformedInvocationPrimary transformedPrimary) {
        JCExpression resultExpr = transformPositionalInvocationOrInstantiation(invocation, callBuilder, transformedPrimary);
        // apply the default parameters
        if (invocation.getVars() != null && !invocation.getVars().isEmpty()) {
            if (invocation.getReturnType() == null || Decl.isUnboxedVoid(invocation.getPrimaryDeclaration())) {
                // void methods get wrapped like (let $arg$1=expr, $arg$0=expr in call($arg$0, $arg$1); null)
                resultExpr = make().LetExpr( 
                        invocation.getVars().append(make().Exec(resultExpr)).toList(), 
                        makeNull());
            } else {
                // all other methods like (let $arg$1=expr, $arg$0=expr in call($arg$0, $arg$1))
                resultExpr = make().LetExpr( 
                        invocation.getVars().toList(),
                        resultExpr);
            }
        }
        return resultExpr;
    }
    
    //
    // Invocations
    public void transformSuperInvocation(Tree.ExtendedType extendedType, ClassDefinitionBuilder classBuilder) {
        if (extendedType.getInvocationExpression().getPositionalArgumentList() != null) {
            InvocationExpression invocation = extendedType.getInvocationExpression();
            Declaration primaryDeclaration = ((Tree.MemberOrTypeExpression)invocation.getPrimary()).getDeclaration();
            java.util.List<ParameterList> paramLists = ((Functional)primaryDeclaration).getParameterLists();
            SuperInvocation builder = new SuperInvocation(this,
                    classBuilder.getForDefinition(),
                    invocation,
                    paramLists.get(0));
            
            CallBuilder callBuilder = CallBuilder.instance(this);
            boolean prevFnCall = withinInvocation(true);
            try {
                if (invocation.getPrimary() instanceof Tree.StaticMemberOrTypeExpression){
                    transformTypeArguments(callBuilder, 
                            ((Tree.StaticMemberOrTypeExpression)invocation.getPrimary()).getTypeArguments().getTypeModels());
                }
                at(builder.getNode());
                JCExpression expr = null;
                if (Strategy.generateInstantiator(builder.getPrimaryDeclaration())
                        && builder.getPrimaryDeclaration().getContainer() instanceof Interface) {
                    // If the subclass is inner to an interface then it will be 
                    // generated inner to the companion and we need to qualify the 
                    // super(), *unless* the subclass is nested within the same 
                    // interface as it's superclass.
                    Scope outer = builder.getSub().getDeclarationModel().getContainer();
                    while (!(outer instanceof Package)) {
                        if (outer == builder.getPrimaryDeclaration().getContainer()) {
                            expr = naming.makeSuper();
                            break;
                        }
                        outer = outer.getContainer();
                    }
                    if (expr == null) {                    
                        Interface iface = (Interface)builder.getPrimaryDeclaration().getContainer();
                        expr = naming.makeQualifiedSuper(naming.makeCompanionFieldName(iface));
                    }
                } else {
                    expr = naming.makeSuper();
                }
                final List<JCExpression> superArguments = transformSuperInvocationArguments(
                        extendedType, classBuilder, builder);
                JCExpression superExpr = callBuilder.invoke(expr)    
                    .arguments(superArguments)
                    .build();
                classBuilder.superCall(at(extendedType).Exec(superExpr));
            } finally {
                withinInvocation(prevFnCall);
            }
        }
    }

    /**
     * Transforms the arguments for the invocation of a superclass initializer 
     * (call to {@code super()}). 
     * 
     * This is complicated by the need to avoid 
     * #929, so when a backward branch is needed in the evaluation of any 
     * argument expression we generate methods on the companion class 
     * (one for each argument) to evaluate the arguments so that the uninitialized 
     * {@code this} is not on the operand stack. 
     */
    private List<JCExpression> transformSuperInvocationArguments(
            Tree.ExtendedType extendedType,
            ClassDefinitionBuilder classBuilder, SuperInvocation invocation) {
        boolean prev = this.uninitializedOperand(stacksUninitializedOperand(invocation));
        // We could create a TransformedPrimary(expr, "super") here if needed
        List<ExpressionAndType> superArgumentsAndTypes = transformArgumentList(invocation, null);
        this.uninitializedOperand(prev);
        final List<JCExpression> superArguments;
        if (stacksUninitializedOperand(invocation) 
                && hasBackwardBranches()) {
            at(extendedType);
            // Avoid backward branches when invoking superclass initializers
            Class subclass = (Class)extendedType.getScope();

            java.util.List<Parameter> classParameters = subclass.getParameterList().getParameters();
            int argumentNum = 0;
            ListBuffer<JCExpression> argMethodCalls = ListBuffer.<JCExpression>lb();
            // TODO This ought to be on the companion class, and not simply static
            if (!subclass.isToplevel()) {
                for (ExpressionAndType argument : superArgumentsAndTypes) {
                    argMethodCalls.append(makeErroneous(backwardBranchWithUninitialized, "" +
                    		"Use of expressions which imply a loop (or other backward branch) " +
                    		"in the invocation of a super class initializer are currently only " +
                    		"supported on top level classes"));
                }
                return argMethodCalls.toList();
            }            
            
            for (ExpressionAndType argument : superArgumentsAndTypes) {
                SyntheticName argMethodName = naming.synthetic("super$arg$"+argumentNum);
                argumentNum++;

                // Generate a static super$arg$N() method on the class
                MethodDefinitionBuilder argMethod = MethodDefinitionBuilder.systemMethod(this, argMethodName.getName());
                argMethod.modifiers(PRIVATE | STATIC);
                argMethod.ignoreAnnotations();
                for (TypeParameter typeParam : subclass.getTypeParameters()) {
                    argMethod.typeParameter(typeParam);
                }
                
                // We can't use argument.type because that's the type of the 
                // argument expression (and already a JCExpression): 
                // By this point the expression will already have been boxed
                //Class superclass = subclass.getExtendedTypeDeclaration();
                //Parameter superclassParameter = superclass.getParameterList().getParameters().get(argumentNum-1);
                //ProducedTypedReference p = superclass.getProducedType(null, subclass.getExtendedType().getTypeArgumentList()).getTypedParameter(superclassParameter);
                //XXX? JCExpression superclassParameterType = classGen().transformClassParameterType(p);
                //JCExpression superclassParameterType = makeJavaType(superclassParameter, p.getType(), 0);
                argMethod.resultType(null, argument.type);
                
                for (Parameter parameter : classParameters) {
                    // TODO Boxed type of parameter?!?!?
                    JCExpression paramType = classGen().transformClassParameterType(parameter);
                    argMethod.parameter(ParameterDefinitionBuilder.instance(this, parameter.getName())
                            .type(paramType, null));
                }
                argMethod.body(make().Return(argument.expression));
                classBuilder.method(argMethod);
            
                // for the super() invocation, replace the given arguments
                // with calls to the super$arg$N() methods
                ListBuffer<JCExpression> argMethodArgs = ListBuffer.<JCExpression>lb();
                for (Parameter parameter : classParameters) {
                    argMethodArgs.append(naming.makeName(parameter, Naming.NA_IDENT));
                }
                argMethodCalls.append(make().Apply(List.<JCExpression>nil(), 
                        argMethodName.makeIdent(), 
                        argMethodArgs.toList()));
            
            }
            superArguments = argMethodCalls.toList();
        } else {
            superArguments = ExpressionAndType.toExpressionList(superArgumentsAndTypes);   
        }
        return superArguments;
    }
    
    public JCExpression transform(Tree.InvocationExpression ce) {
        JCExpression ret = checkForInvocationExpressionOptimisation(ce);
        if(ret != null)
            return ret;
        final boolean prevInv = withinInvocation(false);
        try {
            Tree.Primary primary = ce.getPrimary();
            Declaration primaryDeclaration = null;
            ProducedReference producedReference = null;
            if (primary instanceof Tree.MemberOrTypeExpression) {
                producedReference = ((Tree.MemberOrTypeExpression)primary).getTarget();
                primaryDeclaration = ((Tree.MemberOrTypeExpression)primary).getDeclaration();
            }
            Invocation invocation;
            if (ce.getPositionalArgumentList() != null) {
                if (primaryDeclaration instanceof Functional){
                    // direct invocation
                    java.util.List<Parameter> parameters = ((Functional)primaryDeclaration).getParameterLists().get(0).getParameters();
                    invocation = new PositionalInvocation(this, 
                            primary, primaryDeclaration,producedReference,
                            ce,
                            parameters);
                } else {
                    // indirect invocation
                    invocation = new IndirectInvocationBuilder(this, 
                            primary, primaryDeclaration,
                            ce);
                }
            } else if (ce.getNamedArgumentList() != null) {
                invocation = new NamedArgumentInvocation(this, 
                        primary, 
                        primaryDeclaration,
                        producedReference,
                        ce);
            } else {
                throw new RuntimeException("Illegal State");
            }
            return transformInvocation(invocation);
        } finally {
            withinInvocation(prevInv);
        }
    }

    public JCExpression transformFunctional(Tree.Term expr,
            Functional functional) {
        return CallableBuilder.methodReference(gen(), expr, functional.getParameterLists().get(0)).build();
    }

    //
    // Member expressions

    public static interface TermTransformer {
        JCExpression transform(JCExpression primaryExpr, String selector);
    }

    // Qualified members
    
    public JCExpression transform(Tree.QualifiedMemberExpression expr) {
        // check for an optim
        JCExpression ret = checkForQualifiedMemberExpressionOptimisation(expr);
        if(ret != null)
            return ret;
        return transform(expr, null);
    }
    
    private JCExpression transform(Tree.QualifiedMemberExpression expr, TermTransformer transformer) {
        JCExpression result;
        if (expr.getMemberOperator() instanceof Tree.SafeMemberOp) {
            JCExpression primaryExpr = transformQualifiedMemberPrimary(expr);
            Naming.SyntheticName tmpVarName = naming.alias("safe");
            JCExpression typeExpr = makeJavaType(expr.getTarget().getQualifyingType(), JT_NO_PRIMITIVES);
            JCExpression transExpr = transformMemberExpression(expr, tmpVarName.makeIdent(), transformer);
            if (isFunctionalResult(expr.getTypeModel())) {
                return transExpr;
            }
            // the marker we get for boxing on a QME with a SafeMemberOp is always unboxed
            // since it returns an optional type, but that doesn't tell us if the underlying
            // expr is or not boxed
            boolean isBoxed = !CodegenUtil.isUnBoxed((TypedDeclaration)expr.getDeclaration());
            transExpr = boxUnboxIfNecessary(transExpr, isBoxed, expr.getTarget().getType(), BoxingStrategy.BOXED);
            JCExpression testExpr = make().Binary(JCTree.NE, tmpVarName.makeIdent(), makeNull());
            JCExpression condExpr = make().Conditional(testExpr, transExpr, makeNull());
            result = makeLetExpr(tmpVarName, null, typeExpr, primaryExpr, condExpr);
        } else if (expr.getMemberOperator() instanceof Tree.SpreadOp) {
            result = transformSpreadOperator(expr, transformer);
        } else {
            JCExpression primaryExpr = transformQualifiedMemberPrimary(expr);
            result = transformMemberExpression(expr, primaryExpr, transformer);
        }
        return result;
    }

    private JCExpression transformSpreadOperator(Tree.QualifiedMemberExpression expr, TermTransformer transformer) {
        at(expr);
        
        // this holds the whole spread operation
        Naming.SyntheticName varBaseName = naming.alias("spread");
        
        // reset back here after transformExpression
        at(expr);

        // iterable
        Naming.SyntheticName srcIterableName = varBaseName.suffixedBy("$iterable");
        // make sure we get an Iterable<T> where T is the type we're going to invoke the member on, because
        // we might have a sequence of something erased to Object, like A&B, and we only invoke the member on A
        // which is not erased, so let's not even look at the contents of the sequence, but just the target type
        ProducedType srcElementType = expr.getTarget().getQualifyingType();
        JCExpression srcIterableTypeExpr = makeJavaType(typeFact().getIterableType(srcElementType), JT_NO_PRIMITIVES);
        JCExpression srcIterableExpr = transformExpression(expr.getPrimary(), BoxingStrategy.BOXED, typeFact().getIterableType(srcElementType));

        // sequenceBuilder
        Naming.SyntheticName builderVar = varBaseName.suffixedBy("$sequenceBuilder");
        ProducedType builderType = typeFact().getSequenceBuilderType(expr.getTarget().getType()).getType();
        JCExpression builderTypeExpr = makeJavaType(builderType);
        JCExpression builderInitExpr = make().NewClass(null, List.<JCExpression>nil(), makeJavaType(builderType), 
                List.<JCExpression>nil(), null);

        // element.member
        final SyntheticName elementVar = varBaseName.suffixedBy("$element");
        JCExpression elementExpr = elementVar.makeIdent();
        elementExpr = applyErasureAndBoxing(elementExpr, srcElementType, CodegenUtil.hasTypeErased(expr.getPrimary()),
                true, BoxingStrategy.BOXED, 
                srcElementType, 0);
        JCExpression appliedExpr = transformMemberExpression(expr, elementExpr, transformer);
        
        // This short-circuit is here for spread invocations
        // The code has been called recursively and the part after this if-statement will
        // be handled by the previous recursion
        if (isFunctionalResult(expr.getTypeModel())) {
            return appliedExpr;
        }
        
        // reset back here after transformMemberExpression
        at(expr);
        
        // SRC_ELEMENT_TYPE element = (SRC_ELEMENT_TYPE)iteration;
        final SyntheticName iterationVar = varBaseName.suffixedBy("$iteration");
        JCExpression iteration = iterationVar.makeIdent();
        if (!willEraseToObject(srcElementType)) {
            iteration = make().TypeCast(makeJavaType(srcElementType, JT_NO_PRIMITIVES), 
                    iteration);
        }
        JCVariableDecl elementVarDecl = makeVar(elementVar, 
                makeJavaType(srcElementType, JT_NO_PRIMITIVES), 
                iteration);
        
        // we always need to box to put in SequenceBuilder
        appliedExpr = applyErasureAndBoxing(appliedExpr, expr.getTarget().getType(), 
                // don't trust the erased flag of expr, as it reflects the result type of the overall spread expr,
                // not necessarily of the applied member
                CodegenUtil.hasTypeErased((TypedDeclaration)expr.getTarget().getDeclaration()), 
                !CodegenUtil.isUnBoxed(expr), BoxingStrategy.BOXED, expr.getTarget().getType(), 0);
        // sequenceBuilder.append(APPLIED_EXPR)
        JCStatement body = make().Exec(make().Apply(List.<JCExpression>nil(), 
                naming.makeQualIdent(builderVar.makeIdent(), "append"), 
                List.<JCExpression>of(appliedExpr)));
        
        // The for loop
        final Naming.SyntheticName srcIteratorName = varBaseName.suffixedBy("$iterator");
        backwardBranch(expr);
        List<JCStatement> forStmt = statementGen().transformIterableIteration(expr, 
                iterationVar, srcIteratorName, 
                srcElementType, srcIterableName.makeIdent(), 
                List.<JCStatement>of(elementVarDecl), 
                List.<JCStatement>of(body));
        
        // build the whole spread operation
        List<JCStatement> stmts = List.<JCStatement>of(
                makeVar(srcIterableName, srcIterableTypeExpr, srcIterableExpr),
                makeVar(builderVar, builderTypeExpr, builderInitExpr));
        stmts = stmts.appendList(forStmt);
        JCExpression spread = make().LetExpr(stmts, 
                make().Apply(
                        List.<JCExpression>nil(), 
                        naming.makeQualIdent(builderVar.makeIdent(), "getSequence"), 
                        List.<JCExpression>nil()));
        
        // Do we *statically* know the result must be a Sequence 
        final boolean primaryIsSequence = expr.getPrimary().getTypeModel().isSubtypeOf(
                typeFact().getSequenceType(typeFact().getAnythingDeclaration().getType()).getType());
        int flags = 0;
        // if we want a Sequence and SequenceBuilder returns a Sequential, we need to force the downcast
        if(primaryIsSequence)
            flags |= EXPR_DOWN_CAST;
        spread = applyErasureAndBoxing(spread, 
                typeFact().getSequentialType(expr.getTarget().getType()),// the type of SequenceBuilder.getSequence();
                false,
                true,
                BoxingStrategy.BOXED, 
                primaryIsSequence ? 
                        typeFact().getSequenceType(expr.getTarget().getType()) 
                        : typeFact().getSequentialType(expr.getTarget().getType()),
                flags);
        
        return spread;
    }

    private JCExpression transformQualifiedMemberPrimary(Tree.QualifiedMemberOrTypeExpression expr) {
        if(expr.getTarget() == null)
            return makeErroneous();
        boolean prevOuterCompanion = this.outerCompanion;
        this.outerCompanion = expr.getPrimary() instanceof Tree.Outer
            && expr.getDeclaration().isFormal();
        
        BoxingStrategy boxing = (Decl.isValueTypeDecl(expr.getPrimary())) ? BoxingStrategy.UNBOXED : BoxingStrategy.BOXED;
        JCExpression result = transformExpression(expr.getPrimary(), boxing, 
                expr.getTarget().getQualifyingType());
        this.outerCompanion = prevOuterCompanion;
        return result;
    }
    
    // Base members
    
    public JCExpression transform(Tree.BaseMemberExpression expr) {
        return transform(expr, null);
    }

    private JCExpression transform(Tree.BaseMemberOrTypeExpression expr, TermTransformer transformer) {
        JCExpression primaryExpr = makeSuperQualifier(expr);
        return transformMemberExpression(expr, primaryExpr, transformer);
    }

    private JCExpression makeSuperQualifier(Tree.BaseMemberOrTypeExpression expr) {
        JCExpression primaryExpr = null;
        if (expr.getSupertypeQualifier() != null) {
            ClassOrInterface supertype = (ClassOrInterface)expr.getDeclaration().getContainer();
            if (supertype instanceof Interface) {
                if (needDollarThis(expr.getScope())) {
                    // we're in an interface $impl class
                    // have to prefix it with $this and invoke it
                    primaryExpr = makeQualIdent(naming.makeQuotedThis(), getCompanionAccessorName((Interface) supertype));
                    primaryExpr = make().Apply(null, primaryExpr, List.<JCExpression>nil());
                }else{
                    // we're in a regular class
                    primaryExpr = naming.makeCompanionFieldName((Interface)supertype);
                }
            } else { // class
                primaryExpr = naming.makeSuper();
            }
        }
        return primaryExpr;
    }
    
    // Type members
    
    public JCExpression transform(Tree.QualifiedTypeExpression expr) {
        return transform(expr, null);
    }
    
    public JCExpression transform(Tree.BaseTypeExpression expr) {
        return transform(expr, null);
    }
    
    private JCExpression transform(Tree.QualifiedTypeExpression expr, TermTransformer transformer) {
        JCExpression primaryExpr = transformQualifiedMemberPrimary(expr);
        return transformMemberExpression(expr, primaryExpr, transformer);
    }
    
    // Generic code for all primaries
    
    public JCExpression transformPrimary(Tree.Primary primary, TermTransformer transformer) {
        if (primary instanceof Tree.QualifiedMemberExpression) {
            return transform((Tree.QualifiedMemberExpression)primary, transformer);
        } else if (primary instanceof Tree.BaseMemberExpression) {
            return transform((Tree.BaseMemberExpression)primary, transformer);
        } else if (primary instanceof Tree.BaseTypeExpression) {
            return transform((Tree.BaseTypeExpression)primary, transformer);
        } else if (primary instanceof Tree.QualifiedTypeExpression) {
            return transform((Tree.QualifiedTypeExpression)primary, transformer);
        } else if (primary instanceof Tree.InvocationExpression){
            JCExpression primaryExpr = transform((Tree.InvocationExpression)primary);
            if (transformer != null) {
                primaryExpr = transformer.transform(primaryExpr, null);
            }
            return primaryExpr;
        } else {
            return makeErroneous(primary, "Unhandled primary");
        }
    }
    
    private JCExpression transformMemberExpression(Tree.StaticMemberOrTypeExpression expr, JCExpression primaryExpr, TermTransformer transformer) {
        JCExpression result = null;

        // do not throw, an error will already have been reported
        Declaration decl = expr.getDeclaration();
        if (decl == null) {
            return makeErroneous();
        }
        
        // Explanation: primaryExpr and qualExpr both specify what is to come before the selector
        // but the important difference is that primaryExpr is used for those situations where
        // the result comes from the actual Ceylon code while qualExpr is used for those situations
        // where we need to refer to synthetic objects (like wrapper classes for toplevel methods)
        
        JCExpression qualExpr = null;
        String selector = null;
        // true for Java interop using fields, and for super constructor parameters, which must use
        // parameters rather than getter methods
        boolean mustUseField = false;
        if (decl instanceof Functional
                && !(decl instanceof FunctionalParameter) // A functional parameter will already be Callable-wrapped
                && isFunctionalResult(expr.getTypeModel())) {
            result = transformFunctional(expr, (Functional)decl);
        } else if (decl instanceof Getter) {
            // invoke the getter
            if (decl.isToplevel()) {
                primaryExpr = null;
                qualExpr = naming.makeName((Getter)decl, Naming.NA_FQ | Naming.NA_WRAPPER | Naming.NA_MEMBER);
                selector = null;
            } else if (decl.isClassMember()
                        || decl.isInterfaceMember()) {
                selector = naming.selector((Getter)decl);
            } else {
                // method local attr
                if (!isRecursiveReference(expr)) {
                    primaryExpr = naming.makeQualifiedName(primaryExpr, (Getter)decl, Naming.NA_Q_LOCAL_INSTANCE);
                }
                selector = naming.selector((Getter)decl);
            }
        } else if (decl instanceof Value) {
            if (decl.isToplevel()) {
                // ERASURE
                if ("null".equals(decl.getName())) {
                    // FIXME this is a pretty brain-dead way to go about erase I think
                    result = makeNull();
                } else if (isBooleanTrue(decl)) {
                    result = makeBoolean(true);
                } else if (isBooleanFalse(decl)) {
                    result = makeBoolean(false);
                } else {
                    // it's a toplevel attribute
                    primaryExpr = naming.makeName((Value)decl, Naming.NA_FQ | Naming.NA_WRAPPER);
                    selector = naming.selector((Value)decl);
                }
            } else if (Decl.isClassAttribute(decl)) {
                mustUseField = Decl.isJavaField(decl)
                        || (isWithinSuperInvocation() 
                                && primaryExpr == null
                                && withinSuperInvocation.getDeclarationModel() == decl.getContainer());
                if (mustUseField){
                    selector = decl.getName();
                } else {
                    // invoke the getter, using the Java interop form of Util.getGetterName because this is the only case
                    // (Value inside a Class) where we might refer to JavaBean properties
                    selector = naming.selector((Value)decl);
                }
            } else if (decl.isCaptured() || decl.isShared()) {
                TypeDeclaration typeDecl = ((Value)decl).getType().getDeclaration();
                mustUseField = Decl.isBoxedVariable((Value)decl);
                if (Decl.isLocal(typeDecl)
                        && typeDecl.isAnonymous()) {
                    // accessing a local 'object' declaration, so don't need a getter 
                } else if (decl.isCaptured() && !((Value) decl).isVariable()) {
                    // accessing a local that is not getter wrapped
                } else {
                    primaryExpr = naming.makeQualifiedName(primaryExpr, (Value)decl, Naming.NA_Q_LOCAL_INSTANCE);
                    selector = naming.selector((Value)decl);
                }
            }
        } else if (decl instanceof Method) {
            if (Decl.isLocal(decl)) {
                primaryExpr = null;
                int flags = Naming.NA_MEMBER;
                if (!isRecursiveReference(expr)) {
                    // Only want to quote the method name 
                    // e.g. enum.$enum()
                    flags |= Naming.NA_WRAPPER_UNQUOTED;
                }
                qualExpr = naming.makeName((Method)decl, flags);
                selector = null;
            } else if (decl.isToplevel()) {
                primaryExpr = null;
                qualExpr = naming.makeName((Method)decl, Naming.NA_FQ | Naming.NA_WRAPPER | Naming.NA_MEMBER);
                selector = null;
            } else {
                // not toplevel, not within method, must be a class member
                selector = naming.selector((Method)decl);
            }
        }
        if (result == null) {
            boolean useGetter = !(decl instanceof Method) && !mustUseField;
            if (qualExpr == null && selector == null) {
                useGetter = Decl.isClassAttribute(decl) && CodegenUtil.isErasedAttribute(decl.getName());
                if (useGetter) {
                    selector = naming.selector((TypedDeclaration)decl);
                } else {
                    selector = naming.substitute(decl);
                }
            }
            
            if (qualExpr == null) {
                qualExpr = primaryExpr;
            }
            
            if (qualExpr == null && needDollarThis(expr)) {
                qualExpr = makeQualifiedDollarThis((Tree.BaseMemberExpression)expr);
            }
            if (qualExpr == null && decl.isStaticallyImportable()) {
                qualExpr = naming.makeQuotedFQIdent(Decl.className((Declaration) decl.getContainer()));
            }
            
            if (transformer != null) {
                result = transformer.transform(qualExpr, selector);
            } else {
                Tree.Primary qmePrimary = null;
                if (expr instanceof Tree.QualifiedMemberOrTypeExpression) {
                    qmePrimary = ((Tree.QualifiedMemberOrTypeExpression)expr).getPrimary();
                }
                if (Decl.isValueTypeDecl(qmePrimary)) {
                    JCExpression primTypeExpr = makeJavaType(qmePrimary.getTypeModel(), JT_NO_PRIMITIVES);
                    result = makeQualIdent(primTypeExpr, selector);
                    result = make().Apply(List.<JCTree.JCExpression>nil(),
                            result,
                            List.<JCTree.JCExpression>of(qualExpr));
                } else {
                    result = makeQualIdent(qualExpr, selector);
                    if (useGetter) {
                        result = make().Apply(List.<JCTree.JCExpression>nil(),
                                result,
                                List.<JCTree.JCExpression>nil());
                    }
                }
            }
        }
        
        return result;
    }

    //
    // Array access

    private JCExpression makeQualifiedDollarThis(BaseMemberExpression expr) {
        Declaration decl = expr.getDeclaration();
        Interface interf = (Interface) Decl.getClassOrInterfaceContainer(decl);
        // find the target container interface that is or satisfies the given interface
        Scope scope = expr.getScope();
        boolean needsQualified = false;
        while(scope != null){
            if(scope instanceof Interface){
                if(scope == interf || ((Interface)scope).inherits(interf)){
                    break;
                }
                // we only need to qualify it if we're aiming for a $this of an outer interface than the interface we are caught in
                needsQualified = true;
            }
            scope = scope.getContainer();
        }
        if(!needsQualified)
            return naming.makeQuotedThis();
        interf = (Interface) scope;
        JCExpression qualifiedCompanionThis = naming.makeQualifiedThis(makeJavaType(interf.getType(), JT_COMPANION | JT_RAW));
        return naming.makeQualifiedDollarThis(qualifiedCompanionThis);
    }

    private boolean needDollarThis(Tree.StaticMemberOrTypeExpression expr) {
        if (expr instanceof Tree.BaseMemberExpression) {
            // We need to add a `$this` prefix to the member expression if:
            // * The member was declared on an interface I and
            // * The member is being used in the companion class of I or 
            //   // REMOVED: some subinterface of I, and
            //   some member type of I, and
            // * The member is shared (non-shared means its only on the companion class)
            // FIXME: https://github.com/ceylon/ceylon-compiler/issues/1019
            final Declaration decl = expr.getDeclaration();
            if(!Decl.withinInterface(decl))
                return false;
            
            // Find the method/getter/setter where the expr is being used
            Scope scope = expr.getScope();
            while (scope != null && scope instanceof Interface == false) {
                scope = scope.getContainer();
            }
            // Is it being used in an interface (=> impl)
            if (scope instanceof Interface) {
                return decl.isShared();
            }
        }
        return false;
    }
    
    private boolean needDollarThis(Scope scope) {
        while (Decl.isLocalScope(scope)) {
            scope = scope.getContainer();
        }
        return scope instanceof Interface;
    }

    public JCTree transform(Tree.IndexExpression access) {
        // depends on the operator
        Tree.ElementOrRange elementOrRange = access.getElementOrRange();
        boolean isElement = elementOrRange instanceof Tree.Element;
        
        // let's see what types there are
        ProducedType leftType = access.getPrimary().getTypeModel();
        // find the corresponding supertype
        Interface leftSuperTypeDeclaration;
        if(isElement)
            leftSuperTypeDeclaration = typeFact().getCorrespondenceDeclaration();
        else
            leftSuperTypeDeclaration = typeFact().getRangedDeclaration();
        ProducedType leftCorrespondenceOrRangeType = leftType.getSupertype(leftSuperTypeDeclaration);
        ProducedType rightType = getTypeArgument(leftCorrespondenceOrRangeType, 0);
        
        JCExpression lhs = transformExpression(access.getPrimary(), BoxingStrategy.BOXED, leftCorrespondenceOrRangeType);
        
        // now find the access code
        JCExpression safeAccess;
        
        if(isElement){
            Tree.Element element = (Tree.Element) elementOrRange;
            
            // do the index
            JCExpression index = transformExpression(element.getExpression(), BoxingStrategy.BOXED, rightType);

            // tmpVar.item(index)
            safeAccess = at(access).Apply(List.<JCTree.JCExpression>nil(), 
                                          makeSelect(lhs, "get"), List.of(index));
            // Because tuple index access has the type of the indexed element
            // (not the union of types in the sequential) a typecast may be required.
            ProducedType sequentialElementType = getTypeArgument(leftCorrespondenceOrRangeType, 1);
            ProducedType expectedType = access.getTypeModel();
            int flags = 0;
            if(!expectedType.isExactly(sequentialElementType)
                    // could be optional too, for regular Correspondence item access
                    && !expectedType.isExactly(typeFact().getOptionalType(sequentialElementType)))
                flags |= EXPR_DOWN_CAST;
            safeAccess = applyErasureAndBoxing(safeAccess, 
                                               sequentialElementType, 
                                               CodegenUtil.hasTypeErased(access), true, BoxingStrategy.BOXED, 
                                               expectedType, flags);
        }else{
            // do the indices
            Tree.ElementRange range = (Tree.ElementRange) elementOrRange;
            JCExpression start = transformExpression(range.getLowerBound(), BoxingStrategy.BOXED, rightType);

            // is this a span or segment?
            String method;
            final List<JCExpression> args;
            if (range.getLowerBound() != null 
                    && range.getLength() != null) {
                method = "segment";
                JCExpression length = transformExpression(range.getLength(), BoxingStrategy.UNBOXED, typeFact().getIntegerDeclaration().getType());
                args = List.of(start, length);
            } else if (range.getLowerBound() == null) {
                method = "spanTo";
                JCExpression end = transformExpression(range.getUpperBound(), BoxingStrategy.BOXED, rightType);
                args = List.of(end);
            } else if (range.getUpperBound() == null) {
                method = "spanFrom";
                args = List.of(start);
            } else if (range.getLowerBound() != null 
                    && range.getUpperBound() != null) {
                method = "span"; 
                JCExpression end = transformExpression(range.getUpperBound(), BoxingStrategy.BOXED, rightType);
                args = List.of(start, end);
            } else {
                method = "unknown";
                args = List.<JCExpression>of(makeErroneous());
            }
            // make a "lhs.<method>(start, end)" call
            safeAccess = at(access).Apply(List.<JCTree.JCExpression>nil(), 
                    makeSelect(lhs, method), args);

            // Because tuple open span access has the type of the indexed element
            // (not a sequential of the union of types in the ranged) a typecast may be required.
            ProducedType rangedSpanType = getTypeArgument(leftCorrespondenceOrRangeType, 1);
            ProducedType expectedType = access.getTypeModel();
            int flags = 0;
            if(!expectedType.isExactly(rangedSpanType))
                flags |= EXPR_DOWN_CAST;
            safeAccess = applyErasureAndBoxing(safeAccess, 
                                               rangedSpanType, 
                                               CodegenUtil.hasTypeErased(access), true, BoxingStrategy.BOXED, 
                                               expectedType, flags);
        }

        return safeAccess;
    }

    //
    // Assignment

    public JCExpression transform(Tree.AssignOp op) {
        return transformAssignment(op, op.getLeftTerm(), op.getRightTerm());
    }

    private JCExpression transformAssignment(Node op, Tree.Term leftTerm, Tree.Term rightTerm) {
        // Remember and disable inStatement for RHS
        boolean tmpInStatement = inStatement;
        inStatement = false;
        
        // FIXME: can this be anything else than a Tree.MemberOrTypeExpression or Tree.ParameterizedExpression?
        final JCExpression rhs;
        BoxingStrategy boxing;
        if (leftTerm instanceof Tree.MemberOrTypeExpression) {
            TypedDeclaration decl = (TypedDeclaration) ((Tree.MemberOrTypeExpression)leftTerm).getDeclaration();
            boxing = CodegenUtil.getBoxingStrategy(decl);
            rhs = transformExpression(rightTerm, boxing, leftTerm.getTypeModel());
        } else {
            // instanceof Tree.ParameterizedExpression
            boxing = CodegenUtil.getBoxingStrategy(leftTerm);
            Tree.ParameterizedExpression paramExpr = (Tree.ParameterizedExpression)leftTerm;
            Method decl = (Method) ((Tree.MemberOrTypeExpression)paramExpr.getPrimary()).getDeclaration();
            CallableBuilder callableBuilder = CallableBuilder.anonymous(
                    gen(),
                    (Tree.Expression)rightTerm,
                    decl.getParameterLists().get(0),
                    paramExpr.getParameterLists().get(0),
                    paramExpr.getPrimary().getTypeModel());
            rhs = callableBuilder.build();
        }

        if (tmpInStatement) {
            return transformAssignment(op, leftTerm, rhs);
        } else {
            ProducedType valueType = leftTerm.getTypeModel();
            return transformAssignAndReturnOperation(op, leftTerm, boxing == BoxingStrategy.BOXED, 
                    valueType, valueType, new AssignAndReturnOperationFactory(){
                @Override
                public JCExpression getNewValue(JCExpression previousValue) {
                    return rhs;
                }
            });
        }
    }
    
    private JCExpression transformAssignment(final Node op, Tree.Term leftTerm, JCExpression rhs) {
        // left hand side can be either BaseMemberExpression, QualifiedMemberExpression or array access (M2)
        // TODO: array access (M2)
        JCExpression expr = null;
        if(leftTerm instanceof Tree.BaseMemberExpression) {
            if (needDollarThis((Tree.BaseMemberExpression)leftTerm)) {
                expr = naming.makeQuotedThis();
            } else {
                expr = makeSuperQualifier((Tree.BaseMemberExpression)leftTerm);
            }
        } else if(leftTerm instanceof Tree.QualifiedMemberExpression) {
            Tree.QualifiedMemberExpression qualified = ((Tree.QualifiedMemberExpression)leftTerm);
            expr = transformExpression(qualified.getPrimary(), BoxingStrategy.BOXED, qualified.getTarget().getQualifyingType());
        } else if(leftTerm instanceof Tree.ParameterizedExpression) {
            // Nothing to do here
            expr = null;
        } else {
            return makeErroneous(op, "Not supported yet: "+op.getNodeType());
        }
        return transformAssignment(op, leftTerm, expr, rhs);
    }
    
    private JCExpression transformAssignment(Node op, Tree.Term leftTerm, JCExpression lhs, JCExpression rhs) {
        JCExpression result = null;

        // FIXME: can this be anything else than a Tree.MemberOrTypeExpression or Tree.ParameterizedExpression?
        TypedDeclaration decl;
        if (leftTerm instanceof Tree.MemberOrTypeExpression) {
            decl = (TypedDeclaration) ((Tree.MemberOrTypeExpression)leftTerm).getDeclaration();
        } else {
            // instanceof Tree.ParameterizedExpression
            decl = (TypedDeclaration) ((Tree.MemberOrTypeExpression)((Tree.ParameterizedExpression)leftTerm).getPrimary()).getDeclaration();
        }

        boolean variable = decl.isVariable();
        
        at(op);
        String selector = naming.selector(decl, Naming.NA_SETTER);
        if (decl.isToplevel()) {
            // must use top level setter
            lhs = naming.makeName(decl, Naming.NA_FQ | Naming.NA_WRAPPER);
        } else if ((decl instanceof Getter)) {
            // must use the setter
            if (Decl.isLocal(decl)) {
                lhs = naming.makeQualifiedName(lhs, decl, Naming.NA_WRAPPER | Naming.NA_SETTER);
            }
        } else if (decl instanceof Method
                && !Decl.withinClassOrInterface(decl)) {
            // Deferred method initialization of a local function
            // The Callable field has the same name as the method, so use NA_MEMBER
            result = at(op).Assign(naming.makeQualifiedName(lhs, decl, Naming.NA_WRAPPER_UNQUOTED | Naming.NA_MEMBER), rhs);
        } else if (variable && (Decl.isClassAttribute(decl))) {
            // must use the setter, nothing to do, unless it's a java field
            if(Decl.isJavaField(decl)){
                if (decl.isStaticallyImportable()) {
                    // static field
                    result = at(op).Assign(naming.makeName(decl, Naming.NA_FQ | Naming.NA_WRAPPER_UNQUOTED), rhs);
                }else{
                    // normal field
                    result = at(op).Assign(naming.makeQualifiedName(lhs, decl, Naming.NA_IDENT), rhs);
                }
            }
        } else if (variable && (decl.isCaptured() || decl.isShared())) {
            // must use the qualified setter
            if (Decl.isBoxedVariable(decl)) {
                result = at(op).Assign(naming.makeQualifiedName(lhs, decl, Naming.NA_WRAPPER | Naming.NA_MEMBER | Naming.NA_SETTER), rhs);
            } else if (Decl.isLocal(decl)) {
                lhs = naming.makeQualifiedName(lhs, decl, Naming.NA_WRAPPER);
            } else {
                lhs = naming.makeQualifiedName(lhs, decl, Naming.NA_IDENT);
            }
        } else {
            result = at(op).Assign(naming.makeQualifiedName(lhs, decl, Naming.NA_IDENT), rhs);
        }
        
        if (result == null) {
            result = make().Apply(List.<JCTree.JCExpression>nil(),
                    makeQualIdent(lhs, selector),
                    List.<JCTree.JCExpression>of(rhs));
        }
        
        return result;
    }

    /** Creates an anonymous class that extends Iterable and implements the specified comprehension.
     */
    public JCExpression transformComprehension(Comprehension comp) {
        return transformComprehension(comp, null);
    }

    JCExpression transformComprehension(Comprehension comp, ProducedType expectedType) {
        ProducedType elementType = comp.getForComprehensionClause().getTypeModel();
        elementType = wrapInOptionalForInterop(elementType, expectedType);
        return new ComprehensionTransformation(comp, elementType).transformComprehension();
    }

    private ProducedType wrapInOptionalForInterop(ProducedType elementType, ProducedType expectedType) {
        if(expectedType != null && iteratesOverOptional(expectedType) && !typeFact().isOptionalType(elementType))
            return typeFact().getOptionalType(elementType);
        return elementType;
    }

    private boolean iteratesOverOptional(ProducedType iterableType) {
        ProducedType seqElemType = typeFact().getIteratedType(iterableType);
        return isOptional(seqElemType);
    }

    class ComprehensionTransformation {
        private final Comprehension comp;
        final ProducedType targetIterType;
        final ProducedType absentIterType;
        int idx = 0;
        ExpressionComprehensionClause excc = null;
        Naming.SyntheticName prevItemVar = null;
        Naming.SyntheticName ctxtName = null;
        Naming.SyntheticName lastIteratorCtxtName = null;
        //Iterator fields
        final ListBuffer<JCTree> fields = new ListBuffer<JCTree>();
        final HashSet<String> fieldNames = new HashSet<String>();
        final ListBuffer<Substitution> fieldSubst = new ListBuffer<Substitution>();
        private JCExpression error;
        public ComprehensionTransformation(final Comprehension comp, ProducedType elementType) {
            this.comp = comp;
            targetIterType = typeFact().getIterableType(elementType);
            absentIterType = comp.getForComprehensionClause().getFirstTypeModel();
        }
    
        public JCExpression transformComprehension() {
            at(comp);
            Tree.ComprehensionClause clause = comp.getForComprehensionClause();
            while (clause != null) {
                final Naming.SyntheticName iterVar = naming.synthetic("iter$"+idx);
                Naming.SyntheticName itemVar = null;
                if (clause instanceof ForComprehensionClause) {
                    final ForComprehensionClause fcl = (ForComprehensionClause)clause;
                    itemVar = transformForClause(fcl, iterVar, itemVar);
                    if (error != null) {
                        return error;
                    }
                    clause = fcl.getComprehensionClause();
                } else if (clause instanceof IfComprehensionClause) {
                    transformIfClause((IfComprehensionClause)clause);
                    if (error != null) {
                        return error;
                    }
                    clause = ((IfComprehensionClause)clause).getComprehensionClause();
                    itemVar = prevItemVar;
                } else if (clause instanceof ExpressionComprehensionClause) {
                    //Just keep a reference to the expression
                    excc = (ExpressionComprehensionClause)clause;
                    at(excc);
                    clause = null;
                } else {
                    return makeErroneous(clause, "No support for comprehension clause of type " + clause.getClass().getName());
                }
                idx++;
                if (itemVar != null) prevItemVar = itemVar;
            }
    
            ProducedType iteratedType = typeFact().getIteratedType(targetIterType);
            
            //Define the next() method for the Iterator
            fields.add(make().MethodDef(make().Modifiers(Flags.PUBLIC | Flags.FINAL), names().fromString("next"),
                makeJavaType(typeFact().getObjectDeclaration().getType()), List.<JCTree.JCTypeParameter>nil(),
                List.<JCTree.JCVariableDecl>nil(), List.<JCExpression>nil(), make().Block(0, List.<JCStatement>of(
                    make().Return(
                        make().Conditional(
                            make().Apply(null, 
                                ctxtName.makeIdentWithThis(), List.<JCExpression>nil()),
                            transformExpression(excc.getExpression(), BoxingStrategy.BOXED, iteratedType),
                            makeFinished()))
            )), null));
            //Define the inner iterator class
            ProducedType iteratorType = typeFact().getIteratorType(iteratedType);
            JCExpression iteratorTypeExpr = make().TypeApply(makeIdent(syms().ceylonAbstractIteratorType),
                                                             List.<JCExpression>of(makeJavaType(iteratedType, JT_NO_PRIMITIVES)));
            JCExpression iterator = make().NewClass(null, null, iteratorTypeExpr,
                    List.<JCExpression>nil(), make().AnonymousClassDef(make().Modifiers(0), fields.toList()));
            //Define the anonymous iterable class
            JCExpression iterable = make().NewClass(null, null,
                    make().TypeApply(makeIdent(syms().ceylonAbstractIterableType),
                        List.<JCExpression>of(makeJavaType(iteratedType, JT_NO_PRIMITIVES),
                                              makeJavaType(absentIterType, JT_NO_PRIMITIVES))),
                    List.<JCExpression>nil(), make().AnonymousClassDef(make().Modifiers(0), List.<JCTree>of(
                        make().MethodDef(make().Modifiers(Flags.PUBLIC | Flags.FINAL), names().fromString("getIterator"),
                            makeJavaType(iteratorType, JT_CLASS_NEW|JT_EXTENDS),
                        List.<JCTree.JCTypeParameter>nil(), List.<JCTree.JCVariableDecl>nil(), List.<JCExpression>nil(),
                        make().Block(0, List.<JCStatement>of(make().Return(iterator))), null)
            )));
            for (Substitution subs : fieldSubst) {
                subs.close();
            }
            return iterable;
        }

        class IfComprehensionCondList extends CondList {

            private final ListBuffer<JCStatement> varDecls = ListBuffer.lb();
            private final JCExpression condExpr;
            
            public IfComprehensionCondList(java.util.List<Condition> conditions, JCExpression condExpr) {
                statementGen().super(conditions, null);
                this.condExpr = condExpr;
            }     

            @Override
            protected List<JCStatement> transformInnermost(Condition condition) {
                Cond transformedCond = statementGen().transformCondition(condition, null);
                // The innermost condition's test should be transformed before
                // variable substitution
                
                JCExpression test = transformedCond.makeTest();
                SyntheticName resultVarName = addVarSubs(transformedCond);
                return transformCommon(transformedCond,
                        test,
                        List.<JCStatement>of(make().Break(null)),
                        resultVarName);
            }
            
            protected List<JCStatement> transformIntermediate(Condition condition, java.util.List<Condition> rest) {
                Cond transformedCond = statementGen().transformCondition(condition, null);
                JCExpression test = transformedCond.makeTest();
                SyntheticName resultVarName = addVarSubs(transformedCond);
                return transformCommon(transformedCond, test, transformList(rest), resultVarName);
            }

            private SyntheticName addVarSubs(Cond transformedCond) {
                if (transformedCond.hasResultDecl()) {
                    Variable var = transformedCond.getVariable();
                    SyntheticName resultVarName = naming.alias(transformedCond.getVariableName().getName());
                    fieldSubst.add(naming.addVariableSubst(var.getDeclarationModel(), resultVarName.getName()));
                    return resultVarName;
                }
                return null;
            }
            
            protected List<JCStatement> transformCommon(Cond transformedCond, 
                    JCExpression test, List<JCStatement> stmts,
                    SyntheticName resultVarName) {
                
                if (transformedCond.makeTestVarDecl(0, true) != null) {
                    varDecls.append(transformedCond.makeTestVarDecl(0, true));
                }
                if (transformedCond.hasResultDecl()) {
                    fields.add(make().VarDef(make().Modifiers(Flags.PRIVATE), 
                            resultVarName.asName(), transformedCond.makeTypeExpr(), null));
                    stmts = stmts.prepend(make().Exec(make().Assign(resultVarName.makeIdent(), transformedCond.makeResultExpr())));
                }
                stmts = List.<JCStatement>of(make().If(
                        test, 
                        make().Block(0, stmts), 
                        null));
                return stmts;
            }
            
            public List<JCStatement> getResult() {
                List<JCStatement> stmts = transformList(conditions);
                ListBuffer<JCStatement> result = ListBuffer.lb();
                result.append(make().If(make().Unary(JCTree.NOT, condExpr), make().Break(null), null));
                result.appendList(varDecls);
                result.appendList(stmts);
                return result.toList();   
            }

        }
        
        private void transformIfClause(IfComprehensionClause clause) {
            //Filter contexts need to check if the previous context applies and then check the condition
            JCExpression condExpr = make().Apply(null,
                ctxtName.makeIdentWithThis(), List.<JCExpression>nil());
            ctxtName = naming.synthetic("next"+idx);
            
            IfComprehensionCondList ifComprehensionCondList = new IfComprehensionCondList(clause.getConditionList().getConditions(), condExpr);
            List<JCStatement> ifs = ifComprehensionCondList.getResult();
            JCStatement loop = make().WhileLoop(makeBoolean(true), make().Block(0, ifs));
            MethodDefinitionBuilder mb = MethodDefinitionBuilder.systemMethod(ExpressionTransformer.this, ctxtName.getName())
                .ignoreAnnotations()
                .modifiers(Flags.PRIVATE | Flags.FINAL)
                .resultType(null, makeJavaType(typeFact().getBooleanDeclaration().getType()))
                .body(loop)
                .body(make().Return(make().Unary(JCTree.NOT, prevItemVar.suffixedBy("$exhausted").makeIdent())));
            fields.add(mb.build());
        }

        private SyntheticName transformForClause(final ForComprehensionClause clause,
                final Naming.SyntheticName iterVar,
                Naming.SyntheticName itemVar) {
            final ForComprehensionClause fcl = clause;
            SpecifierExpression specexpr = fcl.getForIterator().getSpecifierExpression();
            ProducedType iterType = specexpr.getExpression().getTypeModel();
            JCExpression iterTypeExpr = makeJavaType(typeFact().getIteratorType(
                    typeFact().getIteratedType(iterType)));
            if (clause == comp.getForComprehensionClause()) {
                //The first iterator can be initialized as a field
                fields.add(make().VarDef(make().Modifiers(Flags.PRIVATE | Flags.FINAL), iterVar.asName(), iterTypeExpr,
                    make().Apply(null, makeSelect(transformExpression(specexpr.getExpression()), "getIterator"), 
                            List.<JCExpression>nil())));
                fieldNames.add(iterVar.getName());
            } else {
                //The subsequent iterators need to be inside a method,
                //in case they depend on the current element of the previous iterator
                fields.add(make().VarDef(make().Modifiers(Flags.PRIVATE), iterVar.asName(), iterTypeExpr, null));
                fieldNames.add(iterVar.getName());
                JCBlock body = make().Block(0l, List.<JCStatement>of(
                        make().If(lastIteratorCtxtName.suffixedBy("$exhausted").makeIdent(),
                                  make().Return(makeBoolean(false)),
                                  null),
                        make().If(make().Binary(JCTree.NE, iterVar.makeIdent(), makeNull()),
                                  make().Return(makeBoolean(true)),
                                  null),
                        make().If(make().Unary(JCTree.NOT, make().Apply(null, ctxtName.makeIdentWithThis(), List.<JCExpression>nil())),
                                  make().Return(makeBoolean(false)),
                                  null),
                        make().Exec(make().Assign(iterVar.makeIdent(), 
                                                  make().Apply(null,
                                                               makeSelect(transformExpression(specexpr.getExpression()), "getIterator"), 
                                                               List.<JCExpression>nil()))),
                        make().Return(makeBoolean(true))
                ));
                fields.add(make().MethodDef(make().Modifiers(Flags.PRIVATE | Flags.FINAL),
                        iterVar.asName(), makeJavaType(typeFact().getBooleanDeclaration().getType()), 
                        List.<JCTree.JCTypeParameter>nil(),
                        List.<JCTree.JCVariableDecl>nil(), List.<JCExpression>nil(), body, null));
            }
            if (fcl.getForIterator() instanceof ValueIterator) {
    
                //Add the item variable as a field in the iterator
                Value item = ((ValueIterator)fcl.getForIterator()).getVariable().getDeclarationModel();
                itemVar = naming.synthetic(item.getName());
                fields.add(make().VarDef(make().Modifiers(Flags.PRIVATE), itemVar.asName(),
                        makeJavaType(item.getType(),JT_NO_PRIMITIVES), null));
                fieldNames.add(itemVar.getName());
    
            } else if (fcl.getForIterator() instanceof KeyValueIterator) {
                //Add the key and value variables as fields in the iterator
                KeyValueIterator kviter = (KeyValueIterator)fcl.getForIterator();
                Value kdec = kviter.getKeyVariable().getDeclarationModel();
                Value vdec = kviter.getValueVariable().getDeclarationModel();
                //But we'll use this as the name for the context function and base for the exhausted field
                itemVar = naming.synthetic("kv$" + kdec.getName() + "$" + vdec.getName());
                fields.add(make().VarDef(make().Modifiers(Flags.PRIVATE), names().fromString(kdec.getName()),
                        makeJavaType(kdec.getType(), JT_NO_PRIMITIVES), null));
                fields.add(make().VarDef(make().Modifiers(Flags.PRIVATE), names().fromString(vdec.getName()),
                        makeJavaType(vdec.getType(), JT_NO_PRIMITIVES), null));
                fieldNames.add(kdec.getName());
                fieldNames.add(vdec.getName());
            } else {
                error = makeErroneous(fcl, "No support yet for iterators of type " + fcl.getForIterator().getClass().getName());
                return null;
            }
            fields.add(make().VarDef(make().Modifiers(Flags.PRIVATE), itemVar.suffixedBy("$exhausted").asName(),
                    makeJavaType(typeFact().getBooleanDeclaration().getType()), null));
            
            //Now the context for this iterator
            ListBuffer<JCStatement> contextBody = new ListBuffer<JCStatement>();
    
            //Assign the next item to an Object variable
            Naming.SyntheticName tmpItem = naming.temp("item");
            contextBody.add(make().VarDef(make().Modifiers(Flags.FINAL), tmpItem.asName(),
                    makeJavaType(typeFact().getObjectDeclaration().getType()),
                    make().Apply(null, makeSelect(iterVar.makeIdent(), "next"), 
                            List.<JCExpression>nil())));
            //Then we check if it's exhausted
            contextBody.add(make().Exec(make().Assign(itemVar.suffixedBy("$exhausted").makeIdent(),
                    make().Binary(JCTree.EQ, tmpItem.makeIdent(), makeFinished()))));
            //Variables get assigned in the else block
            ListBuffer<JCStatement> elseBody = new ListBuffer<JCStatement>();
            if (fcl.getForIterator() instanceof ValueIterator) {
                ProducedType itemType = ((ValueIterator)fcl.getForIterator()).getVariable().getDeclarationModel().getType();
                elseBody.add(make().Exec(make().Assign(itemVar.makeIdent(),
                        make().TypeCast(makeJavaType(itemType,JT_NO_PRIMITIVES), tmpItem.makeIdent()))));
            } else {
                KeyValueIterator kviter = (KeyValueIterator)fcl.getForIterator();
                Value key = kviter.getKeyVariable().getDeclarationModel();
                Value item = kviter.getValueVariable().getDeclarationModel();
                //Assign the key and item to the corresponding fields with the proper type casts
                //equivalent to k=(KeyType)((Entry<KeyType,ItemType>)tmpItem).getKey()
                JCExpression castEntryExpr = make().TypeCast(
                    makeJavaType(typeFact().getIteratedType(iterType)),
                    tmpItem.makeIdent());
                elseBody.add(make().Exec(make().Assign(makeUnquotedIdent(key.getName()),
                    make().TypeCast(makeJavaType(key.getType(), JT_NO_PRIMITIVES),
                        make().Apply(null, makeSelect(castEntryExpr, "getKey"),
                            List.<JCExpression>nil())
                ))));
                //equivalent to v=(ItemType)((Entry<KeyType,ItemType>)tmpItem).getItem()
                elseBody.add(make().Exec(make().Assign(makeUnquotedIdent(item.getName()),
                    make().TypeCast(makeJavaType(item.getType(), JT_NO_PRIMITIVES),
                        make().Apply(null, makeSelect(castEntryExpr, "getItem"),
                            List.<JCExpression>nil())
                ))));
            }
            elseBody.add(make().Return(makeBoolean(true)));
            
            ListBuffer<JCStatement> innerBody = new ListBuffer<JCStatement>();
            if (idx>0) {
                //Subsequent contexts run once for every iteration of the previous loop
                //This will reset our previous context by getting a new iterator if the previous loop isn't done
                innerBody.add(make().Exec(make().Assign(iterVar.makeIdent(), makeNull())));
            }else{
                innerBody.add(make().Return(makeBoolean(false)));
            }
            //Assign the next item to the corresponding variables if not exhausted yet
            contextBody.add(make().If(itemVar.suffixedBy("$exhausted").makeIdent(),
                make().Block(0, innerBody.toList()),
                make().Block(0, elseBody.toList())));
            
            List<JCTree.JCStatement> methodBody;
            if (idx>0) {
                //Subsequent iterators may depend on the item from the previous loop so we make sure we have one
                methodBody = List.of(
                        make().WhileLoop(make().Apply(null, iterVar.makeIdentWithThis(), List.<JCExpression>nil()),
                                         make().Block(0, contextBody.toList())),
                        make().Return(makeBoolean(false)));
            }else
                methodBody = contextBody.toList();
            //Create the context method that returns the next item for this iterator
            lastIteratorCtxtName = ctxtName = itemVar;
            fields.add(make().MethodDef(make().Modifiers(Flags.PRIVATE | Flags.FINAL), itemVar.asName(),
                makeJavaType(typeFact().getBooleanDeclaration().getType()),
                List.<JCTree.JCTypeParameter>nil(), List.<JCTree.JCVariableDecl>nil(), List.<JCExpression>nil(),
                make().Block(0, methodBody), null));
            return itemVar;
        }
    }

    //
    // Type helper functions

    private ProducedType getSupertype(Tree.Term term, Interface compoundType){
        return term.getTypeModel().getSupertype(compoundType);
    }

    private ProducedType getTypeArgument(ProducedType leftType) {
        if (leftType!=null && leftType.getTypeArguments().size()==1) {
            return leftType.getTypeArgumentList().get(0);
        }
        return null;
    }

    private ProducedType getTypeArgument(ProducedType leftType, int i) {
        if (leftType!=null && leftType.getTypeArguments().size() > i) {
            return leftType.getTypeArgumentList().get(i);
        }
        return null;
    }

    private JCExpression unAutoPromote(JCExpression ret, ProducedType returnType) {
        // +/- auto-promotes to int, so if we're using java types we'll need a cast
        return applyJavaTypeConversions(ret, typeFact().getIntegerDeclaration().getType(), 
                returnType, BoxingStrategy.UNBOXED);
    }

    private ProducedType getMostPreciseType(Term term, ProducedType defaultType) {
        // special case for interop when we're dealing with java types
        ProducedType termType = term.getTypeModel();
        if(termType.getUnderlyingType() != null)
            return termType;
        return defaultType;
    }

    //
    // Helper functions
    
    private boolean isRecursiveReference(Tree.StaticMemberOrTypeExpression expr) {
        Declaration decl = expr.getDeclaration();
        Scope s = expr.getScope();
        while (!(s instanceof Declaration) && (s.getContainer() != s)) {
            s = s.getContainer();
        }
        return (s instanceof Declaration) && (s == decl);
    }

    boolean isWithinInvocation() {
        return withinInvocation;
    }
    
    boolean isFunctionalResult(ProducedType type) {
        return !isWithinInvocation()
            && isCeylonCallable(type);   
    }

    boolean withinInvocation(boolean withinInvocation) {
        boolean result = this.withinInvocation;
        this.withinInvocation = withinInvocation;
        return result;
    }

    boolean isWithinCallableInvocation() {
        return withinCallableInvocation;
    }

    boolean withinCallableInvocation(boolean withinCallableInvocation) {
        boolean result = this.withinCallableInvocation;
        this.withinCallableInvocation = withinCallableInvocation;
        return result;
    }

    boolean isWithinSuperInvocation() {
        return withinSuperInvocation != null;
    }

    void withinSuperInvocation(Tree.ClassOrInterface forDefinition) {
        this.withinSuperInvocation = forDefinition;
    }

    //
    // Optimisations

    private JCExpression checkForQualifiedMemberExpressionOptimisation(QualifiedMemberExpression expr) {
        JCExpression ret = checkForBitwiseOperators(expr, expr, null);
        if(ret != null)
            return ret;
        ret = checkForCharacterAsInteger(expr);
        if(ret != null)
            return ret;
        return null;
    }

    private JCExpression checkForInvocationExpressionOptimisation(InvocationExpression ce) {
        // FIXME: temporary hack for bitwise operators literals
        JCExpression ret = checkForBitwiseOperators(ce);
        if(ret != null)
            return ret;
        return null;
    }

    private JCExpression checkForCharacterAsInteger(QualifiedMemberExpression expr) {
        // must be a call on Character
        Tree.Term left = expr.getPrimary();
        if(left == null || !isCeylonCharacter(left.getTypeModel()))
            return null;
        // must be on "integer"
        if(!expr.getIdentifier().getText().equals("integer"))
            return null;
        // must be a normal member op "."
        if(expr.getMemberOperator() instanceof Tree.MemberOp == false)
            return null;
        // must be unboxed
        if(!expr.getUnboxed() || !left.getUnboxed())
            return null;
        // and must be a character literal
        if(left instanceof Tree.CharLiteral == false)
            return null;
        // all good
        return transform((Tree.CharLiteral)left);
    }

    private JCExpression checkForBitwiseOperators(InvocationExpression ce) {
        if(!(ce.getPrimary() instanceof Tree.QualifiedMemberExpression))
            return null;
        Tree.QualifiedMemberExpression qme = (QualifiedMemberExpression) ce.getPrimary();
        // must be a positional arg (FIXME: why?)
        if(ce.getPositionalArgumentList() == null
                || ce.getPositionalArgumentList().getPositionalArguments() == null
                || ce.getPositionalArgumentList().getPositionalArguments().size() != 1)
            return null;
        Tree.PositionalArgument arg = ce.getPositionalArgumentList().getPositionalArguments().get(0);
        if(arg instanceof Tree.ListedArgument == false)
            return null;
        Tree.Expression right = ((Tree.ListedArgument)arg).getExpression();
        return checkForBitwiseOperators(ce, qme, right);
    }
    
    private JCExpression checkForBitwiseOperators(Tree.Term node, Tree.QualifiedMemberExpression qme, Tree.Term right) {
        // must be a call on Integer
        Tree.Term left = qme.getPrimary();
        if(left == null || !isCeylonInteger(left.getTypeModel()))
            return null;
        // must be a supported method/attribute
        ProducedType integerType = typeFact().getIntegerDeclaration().getType();
        String name = qme.getIdentifier().getText();
        String signature = "ceylon.language.Integer."+name;
        
        // see if we have an operator for it
        OperatorTranslation operator = Operators.getOperator(signature);
        if(operator != null){
            if(operator.getArity() == 2){
                if(right == null)
                    return null;
                OptimisationStrategy optimisationStrategy = operator.getOptimisationStrategy(node, left, right, this);
                // check that we can optimise it
                if(!optimisationStrategy.useJavaOperator())
                    return null;
                
                JCExpression leftExpr = transformExpression(left, optimisationStrategy.getBoxingStrategy(), integerType);
                JCExpression rightExpr = transformExpression(right, optimisationStrategy.getBoxingStrategy(), integerType);

                return make().Binary(operator.javacOperator, leftExpr, rightExpr);
            }else{
                // must be unary
                if(right != null)
                    return null;
                OptimisationStrategy optimisationStrategy = operator.getOptimisationStrategy(node, left, this);
                // check that we can optimise it
                if(!optimisationStrategy.useJavaOperator())
                    return null;
                
                JCExpression leftExpr = transformExpression(left, optimisationStrategy.getBoxingStrategy(), integerType);

                return make().Unary(operator.javacOperator, leftExpr);
            }
        }
        return null;
    }
}
