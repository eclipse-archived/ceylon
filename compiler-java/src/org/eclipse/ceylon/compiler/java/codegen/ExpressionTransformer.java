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

package org.eclipse.ceylon.compiler.java.codegen;

import static org.eclipse.ceylon.compiler.java.codegen.CodegenUtil.isDirectAccessVariable;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.isIndirectInvocation;
import static org.eclipse.ceylon.compiler.typechecker.tree.TreeUtil.eliminateParensAndWidening;
import static org.eclipse.ceylon.compiler.typechecker.tree.TreeUtil.hasUncheckedNulls;
import static org.eclipse.ceylon.compiler.typechecker.tree.TreeUtil.unwrapExpressionUntilTerm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.eclipse.ceylon.common.Backend;
import org.eclipse.ceylon.compiler.java.codegen.Invocation.TransformedInvocationPrimary;
import org.eclipse.ceylon.compiler.java.codegen.Naming.DeclNameFlag;
import org.eclipse.ceylon.compiler.java.codegen.Naming.Substitution;
import org.eclipse.ceylon.compiler.java.codegen.Naming.SyntheticName;
import org.eclipse.ceylon.compiler.java.codegen.Operators.AssignmentOperatorTranslation;
import org.eclipse.ceylon.compiler.java.codegen.Operators.OperatorTranslation;
import org.eclipse.ceylon.compiler.java.codegen.Operators.OptimisationStrategy;
import org.eclipse.ceylon.compiler.java.codegen.StatementTransformer.Cond;
import org.eclipse.ceylon.compiler.java.codegen.StatementTransformer.CondList;
import org.eclipse.ceylon.compiler.java.codegen.StatementTransformer.VarDefBuilder;
import org.eclipse.ceylon.compiler.java.codegen.StatementTransformer.VarTrans;
import org.eclipse.ceylon.compiler.java.codegen.recovery.HasErrorException;
import org.eclipse.ceylon.compiler.typechecker.analyzer.Warning;
import org.eclipse.ceylon.compiler.typechecker.tree.Node;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree.Expression;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree.LetExpression;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree.PositionalArgument;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree.Term;
import org.eclipse.ceylon.compiler.typechecker.tree.TreeUtil;
import org.eclipse.ceylon.langtools.tools.javac.code.Flags;
import org.eclipse.ceylon.langtools.tools.javac.code.TypeTag;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCAnnotation;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCBlock;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCExpression;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCLiteral;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCMethodDecl;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCMethodInvocation;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCNewArray;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCNewClass;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCReturn;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCStatement;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCTypeCast;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCUnary;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCVariableDecl;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.Tag;
import org.eclipse.ceylon.langtools.tools.javac.util.Context;
import org.eclipse.ceylon.langtools.tools.javac.util.Convert;
import org.eclipse.ceylon.langtools.tools.javac.util.List;
import org.eclipse.ceylon.langtools.tools.javac.util.ListBuffer;
import org.eclipse.ceylon.langtools.tools.javac.util.Name;
import org.eclipse.ceylon.model.loader.JvmBackendUtil;
import org.eclipse.ceylon.model.loader.NamingBase.Prefix;
import org.eclipse.ceylon.model.loader.NamingBase.Suffix;
import org.eclipse.ceylon.model.loader.model.FieldValue;
import org.eclipse.ceylon.model.loader.model.LazyInterface;
import org.eclipse.ceylon.model.loader.model.OutputElement;
import org.eclipse.ceylon.model.typechecker.model.Class;
import org.eclipse.ceylon.model.typechecker.model.ClassOrInterface;
import org.eclipse.ceylon.model.typechecker.model.Constructor;
import org.eclipse.ceylon.model.typechecker.model.Declaration;
import org.eclipse.ceylon.model.typechecker.model.Function;
import org.eclipse.ceylon.model.typechecker.model.FunctionOrValue;
import org.eclipse.ceylon.model.typechecker.model.Functional;
import org.eclipse.ceylon.model.typechecker.model.Import;
import org.eclipse.ceylon.model.typechecker.model.Interface;
import org.eclipse.ceylon.model.typechecker.model.ModelUtil;
import org.eclipse.ceylon.model.typechecker.model.Module;
import org.eclipse.ceylon.model.typechecker.model.Package;
import org.eclipse.ceylon.model.typechecker.model.Parameter;
import org.eclipse.ceylon.model.typechecker.model.ParameterList;
import org.eclipse.ceylon.model.typechecker.model.Reference;
import org.eclipse.ceylon.model.typechecker.model.Referenceable;
import org.eclipse.ceylon.model.typechecker.model.Scope;
import org.eclipse.ceylon.model.typechecker.model.Type;
import org.eclipse.ceylon.model.typechecker.model.TypeAlias;
import org.eclipse.ceylon.model.typechecker.model.TypeDeclaration;
import org.eclipse.ceylon.model.typechecker.model.TypeParameter;
import org.eclipse.ceylon.model.typechecker.model.TypedDeclaration;
import org.eclipse.ceylon.model.typechecker.model.TypedReference;
import org.eclipse.ceylon.model.typechecker.model.UnionType;
import org.eclipse.ceylon.model.typechecker.model.Unit;
import org.eclipse.ceylon.model.typechecker.model.Value;

/**
 * This transformer deals with expressions only
 */
public class ExpressionTransformer extends AbstractTransformer {

    // flags for transformExpression
    /** 
     * This implies inclusion of the JT_SATISFIES flags when 
     * constructing the type for a variance typecast.
     */
    public static final int EXPR_FOR_COMPANION = 1;
    /** 
     * The expected type has type parameters 
     * (so an extra typecast to the raw type will be required)
     */
    public static final int EXPR_EXPECTED_TYPE_NOT_RAW = 1 << 1;
    /** 
     * The expected type has type parameters with {@code satisfies} 
     * constraints (which may be erased, and thus a type cast may be required 
     * irrespective of the presence of type arguments)
     */
    public static final int EXPR_EXPECTED_TYPE_HAS_CONSTRAINED_TYPE_PARAMETERS = 1 << 2;
    /** 
     * Seems to be used when the expected and expression 
     * types have no supertype in common.
     */
    public static final int EXPR_DOWN_CAST = 1 << 3;
    /** 
     * Use this when the expression being passed contains code to check for nulls coming from Java
     */
    public static final int EXPR_HAS_NULL_CHECK_FENCE = 1 << 4;
    /** 
     * This implies inclusion of the JT_COMPANION flags when 
     * constructing the type casts.
     */
    public static final int EXPR_WANTS_COMPANION = 1 << 5;

    /** 
     * The expected type has type parameters with {@code satisfies} 
     * constraints which have a covariant type parameter that is used by other
     * type parameter bounds, so we will always generate types where it is fixed rather
     * than using wildcards and in some cases we need to cast to raw
     */
    public static final int EXPR_EXPECTED_TYPE_HAS_DEPENDENT_COVARIANT_TYPE_PARAMETERS = 1 << 6;

    
    /**
     * Usually if a {@code long} to {@code int}, {@code short} or {@code byte}
     * conversion is required we use a Util invocation so that a runtime check 
     * is performed. 
     * 
     * In some circumstances (e.g. annotations) we need to use a 
     * real typecast.
     */
    public static final int EXPR_UNSAFE_PRIMITIVE_TYPECAST_OK = 1 << 7;

    /** 
     * Use this when the expression is to be passed to a Java field/setter/parameter which does not require null-safety
     */
    public static final int EXPR_TARGET_ACCEPTS_NULL = 1 << 8;
    
    public static final int EXPR_WIDEN_PRIM = 1 << 9;

    /** 
     * Use this when the expression is guaranteed to not be a base member. This allows us to
     * differentiate `null` (BaseMemberExpression) from other things of type "null" which get
     * stored in temp vars erased to `Object`, for erasure and casts.
     */
    public static final int EXPR_IS_NOT_BASE_MEMBER = 1 << 10;

    /**
     * Use this when you want the resulting expression coerced
     */
    public static final int EXPR_IS_COERCED = 1 << 11;

    /**
     * Use this when you want to force casting
     */
    public static final int EXPR_FORCE_CAST = 1 << 12;

    static{
        // only there to make sure this class is initialised before the enums defined in it, otherwise we
        // get an initialisation error
        Operators.init();
    }
    
    private boolean inStatement = false;
    private boolean withinInvocation = false;
    private boolean withinSyntheticClassBody = false;
    /** The transformation for spread method references involves nested invocations of 
     * {@link #transformSpreadOperator(org.eclipse.ceylon.compiler.typechecker.tree.Tree.QualifiedMemberExpression, TermTransformer)}
     * and what we generate depends on the invocation. This field will be odd on 
     * the "outer" invocation (which generates the outer part of the tree) 
     * and even on the "inner" invocation (which generates the inner part of the tree)
     */
    private Tree.QualifiedMemberOrTypeExpression spreading = null;
    private Naming.SyntheticName memberPrimary = null;
    private ClassOrInterface withinSuperInvocation = null;
    private ClassOrInterface withinDefaultParameterExpression = null;
    /**
     * This is set when we're coercing a FunctionArgument to an FI that doesn't care about null returns
     */
    private Function coercedFunctionalInterfaceNeedsNoNullChecks;

    private Type expectedType;
    private boolean coerced;
    
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

    // Statement expressions
    
    public JCStatement transform(Tree.ExpressionStatement tree) {
        // ExpressionStatements do not return any value, therefore we don't care about the type of the expressions.
        inStatement = true;
        JCStatement result;
        HasErrorException error = errors().getFirstExpressionErrorAndMarkBrokenness(tree.getExpression());
        if (error != null) {
            result = this.makeThrowUnresolvedCompilationError(error);
        } else {
            result = at(tree).Exec(transformExpression(tree.getExpression(), BoxingStrategy.INDIFFERENT, null));
        }
        inStatement = false;
        return result;
    }
    
    public JCStatement transform(Tree.SpecifierStatement op) {
        // SpecifierStatement do not return any value, therefore we don't care about the type of the expressions.
        inStatement = true;
        JCStatement  result;
        HasErrorException error = errors().getFirstExpressionErrorAndMarkBrokenness(op.getBaseMemberExpression());
        if (error != null) {
            result = this.makeThrowUnresolvedCompilationError(error);
        } else if ((error = errors().getFirstExpressionErrorAndMarkBrokenness(op.getSpecifierExpression().getExpression())) != null) {
            result = this.makeThrowUnresolvedCompilationError(error);
        } else {
            result = at(op).Exec(transformAssignment(op, op.getBaseMemberExpression(), op.getSpecifierExpression().getExpression()));
        }
        inStatement = false;
        return result;
    }
    
    public JCExpression transform(Tree.SpecifierOrInitializerExpression expr,
            BoxingStrategy boxing, Type expectedType) {
         return transformExpression(expr.getExpression(), boxing, expectedType);
    }
    
    public JCExpression transform(Tree.SpecifierOrInitializerExpression expr,
            TypedDeclaration decl) {
        //TODO: is there missing logic for small here?
        //      see ClassTransformer.transform(AttributeDeclaration decl, ClassDefinitionBuilder classBuilder)
        return transformExpression(expr.getExpression(), 
                CodegenUtil.getBoxingStrategy(decl), decl.getType(), 
                decl.hasUncheckedNullType() ? EXPR_TARGET_ACCEPTS_NULL : 0);
    }
    
    //
    // Any sort of expression
    
    JCExpression transformExpression(final TypedDeclaration declaration, final Tree.Term expr) {
        // make sure we use the best declaration for boxing and type
        TypedReference typedRef = getTypedReference(declaration);
        TypedReference nonWideningTypedRef = nonWideningTypeDecl(typedRef);
        Type nonWideningType = nonWideningType(typedRef, nonWideningTypedRef);
        // If this is a return statement in a MPL method we want to know 
        // the non-widening type of the innermost callable
        if (declaration instanceof Functional
                && Decl.isMpl((Functional)declaration)) {
            for (int i = ((Functional)declaration).getParameterLists().size(); i > 1; i--) {
                nonWideningType = getReturnTypeOfCallable(nonWideningType);
            }
        }
        // respect the refining definition of optionality
        nonWideningType = propagateOptionality(declaration.getType(), nonWideningType);
        BoxingStrategy boxing = CodegenUtil.getBoxingStrategy(nonWideningTypedRef.getDeclaration());
        int flags = 0;
        if(declaration.hasUncheckedNullType()
                || declaration == coercedFunctionalInterfaceNeedsNoNullChecks)
            flags = ExpressionTransformer.EXPR_TARGET_ACCEPTS_NULL;
        if (CodegenUtil.downcastForSmall(expr, declaration)) 
            flags |= ExpressionTransformer.EXPR_UNSAFE_PRIMITIVE_TYPECAST_OK;
        return transformExpression(expr, boxing, nonWideningType, flags);
    }

    private Type propagateOptionality(Type type, Type nonWideningType) {
        if(!isNull(type)){
            if(isOptional(type)){
                if(!isOptional(nonWideningType)){
                    return typeFact().getOptionalType(nonWideningType);
                }
            }else{
                if(isOptional(nonWideningType)){
                    return typeFact().getDefiniteType(nonWideningType);
                }
            }
        }
        return nonWideningType;
    }
    
    JCExpression transformExpression(final Tree.Term expr) {
        return transformExpression(expr, 0);
    }

    JCExpression transformExpression(final Tree.Term expr, int flags) {
        return transformExpression(expr, BoxingStrategy.BOXED, expr.getTypeModel(), flags);
    }

    JCExpression transformExpression(final Tree.Term expr, BoxingStrategy boxingStrategy, Type expectedType) {
        return transformExpression(expr, boxingStrategy, expectedType, 0);
    }
    
    JCExpression transformExpression(final Tree.Term expr, BoxingStrategy boxingStrategy, 
            Type expectedType, int flags) {
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
        if(term instanceof Tree.IfExpression){
            flags |= EXPR_IS_NOT_BASE_MEMBER;
        }
        CeylonVisitor v = gen().visitor;
        final ListBuffer<JCTree> prevDefs = v.defs;
        final boolean prevInInitializer = v.inInitializer;
        final ClassDefinitionBuilder prevClassBuilder = v.classBuilder;
        final Type prevExpectedType = this.expectedType;
        final boolean prevCoerced = this.coerced;
        try {
            v.defs = new ListBuffer<JCTree>();
            v.inInitializer = false;
            v.classBuilder = gen().current();
            this.expectedType = expectedType;
            this.coerced = (flags & EXPR_IS_COERCED) != 0;
            term.visit(v);
            if (v.hasResult()) {
                result = v.getSingleResult();
                if (result == null) {
                    throw new BugException(term, "visitor yielded multiple results");
                }
            } else {
                throw new BugException(term, "visitor didn't yield any result");
            }
        } catch (BugException e) {
            result = e.makeErroneous(this, expr);
        } finally {
            v.classBuilder = prevClassBuilder;
            v.inInitializer = prevInInitializer;
            v.defs = prevDefs;
            this.coerced = prevCoerced;
            this.expectedType = prevExpectedType;
        }

        if ((flags & EXPR_TARGET_ACCEPTS_NULL) == 0
                && expectedType != null 
                && hasUncheckedNulls(expr)
                && expectedType.isSubtypeOf(typeFact().getObjectType())
                && !knownNullSafe(term)) {
            result = utilInvocation().checkNull(result);
            flags |= EXPR_HAS_NULL_CHECK_FENCE;
        }
        result = applyErasureAndBoxing(result, expr, boxingStrategy, expectedType, flags);

        return result;
    }

    private static boolean knownNullSafe(Tree.Term term) {
        if (term instanceof Tree.InvocationExpression) {
            Tree.InvocationExpression ie = 
                    (Tree.InvocationExpression) term;
            Tree.Term p = ie.getPrimary();
            if (p instanceof Tree.StaticMemberOrTypeExpression) {
                Tree.StaticMemberOrTypeExpression bme = 
                        (Tree.StaticMemberOrTypeExpression) p;
                Declaration dec = bme.getDeclaration();
                if (dec!=null) {
                    String qname = 
                            dec.getQualifiedNameString();
                    return knownNullSafe.contains(qname);
                }
            }
        }
        return false;
    }
    
    private static java.util.List<String> knownNullSafe = Arrays.asList(
            "java.util::Objects.toString",
            "java.util::Arrays.asList",
            "java.util::Arrays.copyOf",
            "java.util::Arrays.copyOfRange",
            "java.util::Arrays.deepToString",
            "java.util::Collections.unmodifiableList",
            "java.util::Collections.unmodifiableMap",
            "java.util::Collections.unmodifiableSet",
            "java.util::Collections.unmodifiableSortedMap",
            "java.util::Collections.unmodifiableSortedSet",
            "java.util::Collections.synchronizedList",
            "java.util::Collections.synchronizedMap",
            "java.util::Collections.synchronizedSet",
            "java.util::Collections.synchronizedSortedMap",
            "java.util::Collections.synchronizedSortedSet",
            "java.util::Collections.emptyIterator",
            "java.util::Collections.emptyListIterator",
            "java.util::Collections.emptyList",
            "java.util::Collections.emptyMap",
            "java.util::Collections.emptySet",
            "java.util::Collections.emptySortedMap",
            "java.util::Collections.emptySortedSet",
            "java.util::Collections.singleton",
            "java.util::Collections.singletonList",
            "java.util::Collections.singletonMap",
            "java.lang::String.substring",
            "java.lang::String.toLowerCase",
            "java.lang::String.toUpperCase",
            "java.lang::String.intern",
            "java.lang::String.replace",
            "java.lang::String.replaceAll",
            "java.lang::String.replaceFirst",
            "java.lang::String.toCharArray",
            "java.lang::String.getBytes",
            "java.lang::String.getChars",
            "java.lang::String.trim",
            "java.lang::String.format",
            "java.lang::String.join",
            "java.lang::String.split",
            "java.lang::String.copyValueOf",
            "java.lang::String.valueOf",
            "java.lang::Double.valueOf",
            "java.lang::Float.valueOf",
            "java.lang::Long.valueOf",
            "java.lang::Integer.valueOf",
            "java.lang::Short.valueOf",
            "java.lang::Byte.valueOf",
            "java.lang::Boolean.valueOf",
            "java.lang::Character.valueOf",
            "java.lang::Double.toString",
            "java.lang::Float.toString",
            "java.lang::Long.toString",
            "java.lang::Integer.toString",
            "java.lang::Short.toString",
            "java.lang::Byte.toString",
            "java.lang::Boolean.toString",
            "java.lang::Character.toString"
    );
    
    JCExpression transform(Tree.FunctionArgument functionArg, Type expectedType) {
        Type prevExpectedType = this.expectedType;
        try{
            this.expectedType = expectedType;
            return transform(functionArg);
        }finally{
            this.expectedType = prevExpectedType;
        }
    }
    
    JCExpression transform(Tree.FunctionArgument functionArg) {
        Function model = functionArg.getDeclarationModel();
        TypedReference functionalInterface = gen().checkForFunctionalInterface(expectedType);
        int flags = 0;
        // even if we are a Callable<X>, if the FI returns void don't check for non-null return types
        if(functionalInterface != null
                && Decl.isUnboxedVoid(functionalInterface.getDeclaration()))
            flags |= EXPR_TARGET_ACCEPTS_NULL;
        List<JCStatement> body;
        boolean prevNoExpressionlessReturn = statementGen().noExpressionlessReturn;
        boolean prevSyntheticClassBody = expressionGen().withinSyntheticClassBody(true);
        Function prevCoercedFunctionalInterfaceNeedsNoNullChecks = expressionGen().coercedFunctionalInterfaceNeedsNoNullChecks;
        try {
            statementGen().noExpressionlessReturn = isAnything(model.getType());
            expressionGen().coercedFunctionalInterfaceNeedsNoNullChecks = 
                    (flags & EXPR_TARGET_ACCEPTS_NULL) != 0 ? model : null;
            if (functionArg.getBlock() != null) {
                body = statementGen().transformBlock(functionArg.getBlock());
                if (!functionArg.getBlock().getDefinitelyReturns()) {
                    if (isAnything(model.getType())) {
                        body = body.append(make().Return(makeNull()));
                    } else {
                        body = body.append(make().Return(makeErroneous(functionArg.getBlock(), "compiler bug: non-void method does not definitely return")));
                    }
                }
            } else {
                Tree.Expression expr = functionArg.getExpression();
                JCExpression transExpr = expressionGen().transformExpression(expr, flags);
                JCReturn returnStat = make().Return(transExpr);
                body = List.<JCStatement>of(returnStat);
            }
        } finally {
            expressionGen().withinSyntheticClassBody(prevSyntheticClassBody);
            expressionGen().coercedFunctionalInterfaceNeedsNoNullChecks = prevCoercedFunctionalInterfaceNeedsNoNullChecks;
            statementGen().noExpressionlessReturn = prevNoExpressionlessReturn;
        }

        Type callableType = functionArg.getTypeModel();
        CallableBuilder callableBuilder = CallableBuilder.methodArgument(gen(),
                functionArg,
                model,
                callableType, 
                Collections.singletonList(functionArg.getParameterLists().get(0)),
                classGen().transformMplBody(functionArg.getParameterLists(), model, body));
        
        callableBuilder.checkForFunctionalInterface(expectedType);
        
        JCExpression result = callableBuilder.build();
        result = applyErasureAndBoxing(result, callableType, true, BoxingStrategy.BOXED, expectedType);
        return result;
    }

    //
    // Boxing and erasure of expressions
    
    private JCExpression applyErasureAndBoxing(JCExpression result, Tree.Term expr, BoxingStrategy boxingStrategy, 
            Type expectedType) {
        return applyErasureAndBoxing(result, expr, boxingStrategy, expectedType, 0);
    }
    
    private JCExpression applyErasureAndBoxing(JCExpression result, Tree.Term expr, BoxingStrategy boxingStrategy, 
                Type expectedType, int flags) {
        Type exprType = expr.getTypeModel();
        if ((flags & EXPR_HAS_NULL_CHECK_FENCE) != 0) {
            exprType = getNonNullType(exprType);
        } else if (hasUncheckedNulls(expr) && !isOptional(exprType)) {
            exprType = typeFact().getOptionalType(exprType);
        }
        boolean exprBoxed = !CodegenUtil.isUnBoxed(expr);
        boolean exprErased = CodegenUtil.hasTypeErased(expr);
        boolean exprUntrustedType = CodegenUtil.hasUntrustedType(expr);
        boolean exprSmall = CodegenUtil.isSmall(expr);
        return applyErasureAndBoxing(result, exprType, exprErased, exprBoxed, exprUntrustedType, exprSmall, boxingStrategy, expectedType, flags);
    }
    
    JCExpression applyErasureAndBoxing(JCExpression result, Type exprType,
            boolean exprBoxed,
            BoxingStrategy boxingStrategy, Type expectedType) {
        return applyErasureAndBoxing(result, exprType, false, exprBoxed, boxingStrategy, expectedType, 0);
    }
    
    JCExpression applyErasureAndBoxing(JCExpression result, Type exprType,
            boolean exprErased, boolean exprBoxed, 
            BoxingStrategy boxingStrategy, Type expectedType, 
            int flags) {
        return applyErasureAndBoxing(result, exprType, exprErased, exprBoxed, false, boxingStrategy, expectedType, flags);
    }
    
    JCExpression applyErasureAndBoxing(JCExpression result, Type exprType,
            boolean exprErased, boolean exprBoxed, boolean exprUntrustedType,
            BoxingStrategy boxingStrategy, Type expectedType, 
            int flags) {
        return applyErasureAndBoxing(result, exprType, exprErased, exprBoxed, exprUntrustedType, false,
                boxingStrategy, expectedType, flags);
    }
    
    JCExpression applyErasureAndBoxing(JCExpression result, Type exprType,
            boolean exprErased, boolean exprBoxed, boolean exprUntrustedType,
            boolean exprSmall,
            BoxingStrategy boxingStrategy, Type expectedType, 
            int flags) {
        
        if(exprType != null)
            exprType = exprType.resolveAliases();
        if(expectedType != null)
            expectedType = expectedType.resolveAliases();
        boolean canCast = false;
        boolean coerced = (flags & EXPR_IS_COERCED) != 0;

        if (expectedType != null
                // don't cast if we're coercing
                && !coerced
                // don't add cast to an erased type 
                && !willEraseToObject(expectedType)) {

            // only try to cast boxed types, no point otherwise
            if(exprBoxed){

                boolean expectedTypeIsNotRaw = (flags & EXPR_EXPECTED_TYPE_NOT_RAW) != 0;
                boolean expectedTypeHasConstrainedTypeParameters = (flags & EXPR_EXPECTED_TYPE_HAS_CONSTRAINED_TYPE_PARAMETERS) != 0;
                boolean expectedTypeHasDependentCovariantTypeParameters = (flags & EXPR_EXPECTED_TYPE_HAS_DEPENDENT_COVARIANT_TYPE_PARAMETERS) != 0;
                boolean downCast = (flags & EXPR_DOWN_CAST) != 0;
                boolean forceCast = (flags & EXPR_FORCE_CAST) != 0;
                int companionFlags = (flags & EXPR_WANTS_COMPANION) != 0 ? AbstractTransformer.JT_COMPANION : 0;

                // special case for returning Null expressions
                if (isNull(exprType)){
                    // don't add cast for null
                    if(!isNullValue(exprType)
                            // well, unless it's a complex expression which stores that null value in
                            // temp vars whose type erase to Object
                            || (flags & EXPR_IS_NOT_BASE_MEMBER) != 0
                            // include a cast even for null for interop and disambiguating bw overloads and null values
                            // of different types using the "of" operator
                            || downCast
                            // also add casts if we really want them
                            || forceCast){
                        // in some cases we may have an instance of Null, which is of type java.lang.Object, being
                        // returned in a context where we expect a String? (aka ceylon.language.String) so even though
                        // the instance at hand will really be null, we need a up-cast to it
                        JCExpression targetType = makeJavaType(expectedType, AbstractTransformer.JT_RAW | companionFlags);
                        result = make().TypeCast(targetType, result);
                    }
                }else if(exprType.isExactlyNothing()){
                    // type param erasure
                    JCExpression targetType = makeJavaType(expectedType, 
                            AbstractTransformer.JT_NO_PRIMITIVES | companionFlags);
                    result = make().TypeCast(make().QualIdent(syms().objectType.tsym), result);
                    result = make().TypeCast(targetType, result);
                }else if(// expression was forcibly erased
                         exprErased
                         // we want to cast
                         || forceCast
                         // expression type cannot be trusted to be true, most probably because we had to satisfy Java type parameter
                         // bounds that are different from what we think the expression type should be
                         || exprUntrustedType
                         // if we have a covariant type parameter which is dependent and whose type arg contains erased type parameters
                         // we need a raw cast because it will be fixed rather than using a wildcard and there's a good chance
                         // we can't use proper subtyping rules to assign to it
                         // see https://github.com/ceylon/ceylon-compiler/issues/1557
                         || expectedTypeHasDependentCovariantTypeParameters
                         // some type parameter somewhere needs a cast
                         || needsCast(exprType, expectedType, expectedTypeIsNotRaw, expectedTypeHasConstrainedTypeParameters, downCast)
                         // if the exprType is raw and the expected type isn't
                         || (exprType.isRaw() && (expectedTypeIsNotRaw || !isTurnedToRaw(expectedType)))){

                    // save this before we simplify it because we lose that flag doing so
                    boolean exprIsRaw = exprType.isRaw();
                    boolean expectedTypeIsRaw = isTurnedToRaw(expectedType) && !expectedTypeIsNotRaw;
                    
                    if (forceCast 
                            && !expectedType.isTypeParameter()
                            && !exprType.isSubtypeOf(expectedType)
                            && !expectedType.isSubtypeOf(exprType)) {
                        result = make().TypeCast(syms().objectType, result);
                    }
                    
                    // We will need a raw cast if either the expected type or the
                    // expression type has type parameters while the other hasn't 
                    // (unless the other type is already raw)
                    if ((!exprIsRaw && hasTypeParameters(expectedType))
                            || (downCast && !expectedTypeIsRaw && hasTypeParameters(exprType))) {
                        Type rawType = hasTypeParameters(expectedType) ? expectedType : exprType;
                        JCExpression rawTypeExpr = makeJavaType(rawType, 
                                AbstractTransformer.JT_TYPE_ARGUMENT | AbstractTransformer.JT_RAW | companionFlags);
                        result = make().TypeCast(rawTypeExpr, result);
                        // expr is now raw
                        exprIsRaw = true;
                        // let's not add another downcast if we got a cast: one is enough
                        downCast = false;
                        // same for forced erasure
                        exprErased = false;
                        exprUntrustedType = false;
                    }

                    // simplify the type
                    // (without the underlying type, because the cast is always to a non-primitive)
                    boolean wasOptional = isOptional(expectedType);
                    exprType = simplifyType(expectedType).withoutUnderlyingType();
                    // if the expected type was optional, respect that otherwise it fubars boxing
                    if(wasOptional){
                        exprType = typeFact().getOptionalType(exprType);
                    }

                    // if the expr is not raw, we need a cast
                    // if the expr is raw:
                    //  don't even try making an actual cast if there are bounded type parameters in play, because going raw is much safer
                    //  also don't try making the cast if the expected type is raw because anything goes
                    boolean needsTypedCast = !exprIsRaw 
                            || (!expectedTypeHasConstrainedTypeParameters
                                    && !expectedTypeHasDependentCovariantTypeParameters
                                    && !expectedTypeIsRaw);
                    if(needsTypedCast
                            // make sure that downcasts get at least one cast
                            || downCast
                            // same for forced erasure
                            || exprUntrustedType){
                        // forced erasure may require a previous cast to Object if we were not able to insert a raw cast
                        // because for instance Sequential<String> cannot be cast forcibly to Empty because Java is so smart
                        // it figures out that there's no intersection between the two types, but we know better
                        if(exprUntrustedType && !exprIsRaw){
                            result = make().TypeCast(syms().objectType, result);
                        }
                        // Do the actual cast
                        JCExpression targetType = makeJavaType(expectedType, 
                                AbstractTransformer.JT_TYPE_ARGUMENT | companionFlags);
                        result = make().TypeCast(targetType, result);
                    }
                }else
                    canCast = true;
            }else
                canCast = true;
        }

        // If expr type if Self<T> and expected type is T we need to cast before any unboxing
        boolean mightNeedCastToSelfType = expectedType != null 
                && exprBoxed 
                && !((boxingStrategy == BoxingStrategy.BOXED || boxingStrategy == BoxingStrategy.INDIFFERENT)
                        && exprType.isSubtypeOf(expectedType));
        if (mightNeedCastToSelfType) {
            Type selfType = getSelfType(exprType);
            if (selfType != null && expectedType.isExactly(selfType)) {
                result = applySelfTypeCasts(result, exprType, exprBoxed, BoxingStrategy.BOXED, 
                        expectedType, getSelfTypeParam(exprType));
                exprType = expectedType;
            }
        }
        // we must do the boxing after the cast to the proper type
        JCExpression ret = boxUnboxIfNecessary(result, exprBoxed ? BoxingStrategy.BOXED : BoxingStrategy.UNBOXED, exprType, boxingStrategy, expectedType);
        
        // very special case for nothing that we need to "unbox" to a primitive type
        if(exprType != null
                && exprType.isExactlyNothing()
                && boxingStrategy == BoxingStrategy.UNBOXED){
            // in this case we have to use the expected type
            ret = unboxType(ret, expectedType);
        }
        
        // now check if we need variance casts
        if (canCast) {
            ret = applyVarianceCasts(ret, exprType, exprBoxed, boxingStrategy, expectedType, flags);
        }
//        ret = applySelfTypeCasts(ret, exprType, exprBoxed, boxingStrategy, expectedType, selfTypeParam);
        ret = applyJavaTypeConversions(ret, exprType, expectedType, boxingStrategy, exprBoxed, exprSmall, flags);
        // Don't rely on coerced member because for SOME reason this is called
        // _after_ we reset it in transformExpression()
        if(coerced)
            ret = applyJavaCoercions(ret, exprType, expectedType);
        return ret;
    }
    
    static TypeParameter getSelfTypeParam(Type exprType) {
        TypeDeclaration dec = exprType.getDeclaration();
        if (dec.isComparable()) {
            return dec.getTypeParameters().get(0);
        }
        Type selfTypeParam = dec.getSelfType();
        if (selfTypeParam!=null && selfTypeParam.isTypeParameter()) {
            return (TypeParameter) selfTypeParam.getDeclaration();
        }
        return null;
    }

    static Type getSelfType(Type exprType) {
        TypeParameter selfTypeParam = getSelfTypeParam(exprType);
        if (selfTypeParam!=null) {
            return exprType.getTypeArguments().get(selfTypeParam);
        }
        return null;
    }

    private JCExpression applyJavaCoercions(JCExpression ret, Type exprType, Type expectedType) {
        if(expectedType == null)
            return ret;
        Type nonSimpleExprType = exprType;
        exprType = simplifyType(exprType);
        expectedType = simplifyType(expectedType);
        if(isCeylonString(exprType) && isJavaCharSequence(expectedType)){
            // FIXME: only do this if boxed, or rather, do not box in the first place
            if(isOptional(nonSimpleExprType)){
                Naming.SyntheticName varName = naming.temp();
                JCExpression test = make().Binary(JCTree.Tag.NE, varName.makeIdent(), makeNull());
                JCExpression convert = make().Apply(null, makeQualIdent(varName.makeIdent(), "toString"), List.<JCTree.JCExpression>nil());
                JCExpression cond = make().Conditional(test, convert, makeNull());
                JCExpression typeExpr = makeJavaType(typeFact().getObjectType());
                return makeLetExpr(varName, null, typeExpr, ret, cond);
            }else{
                return make().Apply(null, makeQualIdent(ret, "toString"), List.<JCTree.JCExpression>nil());
            }
        }
        //TODO: obsolete code?
        if(isJavaCharSequence(exprType) && expectedType.isExactly(typeFact().getStringDeclaration().getType())){
            return make().Apply(null, makeQualIdent(ret, "toString"), List.<JCTree.JCExpression>nil());
        }
        // end of obsolete code
        if (isCeylonArray(exprType) && isJavaArray(expectedType)) {
            JCExpression result;
            if(isOptional(nonSimpleExprType)){
                Naming.SyntheticName varName = naming.temp();
                JCExpression test = make().Binary(JCTree.Tag.NE, varName.makeIdent(), makeNull());
                JCExpression convert = make().Apply(null, makeQualIdent(varName.makeIdent(), "toArray"), List.<JCTree.JCExpression>nil());
                JCExpression cond = make().Conditional(test, convert, makeNull());
                JCExpression typeExpr = makeJavaType(typeFact().getObjectType());
                result = makeLetExpr(varName, null, typeExpr, ret, cond);
            }else{
                result = make().Apply(null, makeQualIdent(ret, "toArray"), List.<JCTree.JCExpression>nil());
            }
            JCExpression targetType = makeJavaType(expectedType, JT_NO_PRIMITIVES);
            return make().TypeCast(targetType, result);
        }
        if(isCeylonClassOrInterfaceModel(exprType)
                && isJavaClass(expectedType)
                && !(ret instanceof JCTree.JCFieldAccess && ret.toString().endsWith(".class"))){
            // FIXME: perhaps cast as RAW?
            JCTree arg = ret;
            // try to turn (.ceylon.language.meta.model.Class)
            //  .ceylon.language.meta.typeLiteral_.typeLiteral(
            //   .org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor.klass(
            //    .org.eclipse.ceylon.compiler.java.test.interop.LambdasJava.class))
            // into Util.classErasure(.org.eclipse.ceylon.compiler.java.test.interop.LambdasJava.class)
            while(arg instanceof JCTree.JCTypeCast)
                arg = ((JCTree.JCTypeCast)arg).getExpression();
            if(arg instanceof JCTree.JCMethodInvocation){
                JCExpression methodSelect = ((JCTree.JCMethodInvocation) arg).getMethodSelect();
                if(methodSelect instanceof JCTree.JCFieldAccess){
                    JCTree.JCFieldAccess methodField = (JCTree.JCFieldAccess) methodSelect;
                    if(methodField.getIdentifier().toString().equals("typeLiteral")
                            && methodField.getExpression() instanceof JCTree.JCFieldAccess
                            && ((JCTree.JCFieldAccess)methodField.getExpression()).toString().equals(".ceylon.language.meta.typeLiteral_")){
                        JCExpression classLiteral = extractClassLiteralFromTypeDescriptor(((JCTree.JCMethodInvocation) arg).getArguments().get(0));
                        if(classLiteral != null)
                            // FIXME: pass type arg explicitly?
                            return utilInvocation().classErasure(classLiteral);
                    }
                }
            }
            // make sure erasure doesn't get in the way of calling this method
            if(willEraseToObject(exprType))
                ret = make().TypeCast(makeJavaType(typeFact().getClassOrInterfaceModelType(typeFact().getObjectType())), ret);
            // FIXME: pass type arg explicitly?
            return utilInvocation().javaClassForModel(ret);
        }
        return ret;
    }

    private JCExpression extractClassLiteralFromTypeDescriptor(JCExpression arg) {
        if(arg instanceof JCTree.JCMethodInvocation){
            JCExpression methodSelect = ((JCTree.JCMethodInvocation) arg).getMethodSelect();
            if(methodSelect instanceof JCTree.JCFieldAccess){
                JCTree.JCFieldAccess methodField = (JCTree.JCFieldAccess) methodSelect;
                if(methodField.getIdentifier().toString().equals("klass")
                        && methodField.getExpression() instanceof JCTree.JCFieldAccess
                        && ((JCTree.JCFieldAccess)methodField.getExpression()).toString().equals(".org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor")){
                    return ((JCTree.JCMethodInvocation) arg).getArguments().get(0);
                }
                if(methodField.getIdentifier().toString().equals("member")
                        && methodField.getExpression() instanceof JCTree.JCFieldAccess
                        && ((JCTree.JCFieldAccess)methodField.getExpression()).toString().equals(".org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor")){
                    return extractClassLiteralFromTypeDescriptor(((JCTree.JCMethodInvocation) arg).getArguments().get(1));
                }
            }
        }
        return null;
    }

    boolean needsCast(Type exprType, Type expectedType, 
                              boolean expectedTypeNotRaw, 
                              boolean expectedTypeHasConstrainedTypeParameters,
                              boolean downCast) {
        // error handling
        if(exprType == null)
            return false;
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
        
        // find their common type
        Type commonType = exprType.getSupertype(expectedType.getDeclaration());
        
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
        java.util.List<Type> commonTypeArgs = commonType.getTypeArgumentList();
        java.util.List<TypeParameter> commonTps = commonType.getDeclaration().getTypeParameters();
        java.util.List<Type> expectedTypeArgs = expectedType.getTypeArgumentList();
        java.util.List<TypeParameter> expectedTps = expectedType.getDeclaration().getTypeParameters();
        // check that we got them all otherwise we just don't know
        if(commonTypeArgs.size() != expectedTypeArgs.size())
            return false;
        for(int i=0,n=commonTypeArgs.size(); i < n ; i++){
            // apply the same logic to each type param: see if they would require a raw cast
            Type commonTypeArg = commonTypeArgs.get(i);
            Type expectedTypeArg = expectedTypeArgs.get(i);
            
            if (hasDependentTypeParameters(commonTps, commonTps.get(i))
                    || hasDependentTypeParameters(expectedTps, expectedTps.get(i))) {
                // In this case makeJavaType() will have made the Java decl 
                // invariant in this type argument, so we will need a type cast 
                // if the type parameters are not identical:
                if (!simplifyType(commonTypeArg).isExactly(simplifyType(expectedTypeArg))) {
                    return true;
                }
            }
            
            if(needsCast(commonTypeArg, expectedTypeArg, expectedTypeNotRaw, 
                         expectedTypeHasConstrainedTypeParameters, 
                         downCast))
                return true;
            // stop after the first one for Callable
            if(isCallable)
                break;
        }
        if(commonType.getQualifyingType() != null
                && expectedType.getQualifyingType() != null){
            return needsCast(commonType.getQualifyingType(),
                    expectedType.getQualifyingType(),
                    expectedTypeNotRaw,
                    expectedTypeHasConstrainedTypeParameters,
                    downCast);
        }
        return false;
    }

    private boolean lostTypeParameterInInheritance(Type exprType, Type commonType) {
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
        if(Decl.equal(exprDecl, commonDecl))
            return lostTypeParameter;
        if(searchInterfaces){
            // find a match in interfaces
            for(Type pt : exprDecl.getSatisfiedTypes()){
                // FIXME: this is very heavy-handed because we consider that once we've lost a type parameter we've lost them all
                // but we could optimise this by checking:
                // 1/ which type parameter we've really lost
                // 2/ if the type parameters we're passing to our super type actually depend in any way from type parameters we've lost
                boolean lostTypeParameter2 = lostTypeParameter || isTurnedToRaw(pt);
                pt = simplifyType(pt);
                // skip unknown types
                if(pt.isUnknown())
                    continue;
                // it has to be an interface
                Interface interf = (Interface) pt.getDeclaration();
                if(lostTypeParameterInInheritance(interf, commonDecl, searchInterfaces, lostTypeParameter2))
                    return true;
            }
        }
        // search for super classes
        Type extendedType = exprDecl.getExtendedType();
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

    private boolean hasTypeParameters(Type type) {
        if (!type.getTypeArgumentList().isEmpty()) {
            return true;
        }
        if (type.getDeclaration() instanceof UnionType
                && type.getCaseTypes() != null) {
            for (Type ct : type.getCaseTypes()) {
                if (hasTypeParameters(ct)) {
                    return true;
                }
            }
        }
        if(type.getQualifyingType() != null)
            return hasTypeParameters(type.getQualifyingType());
        return false;
    }

    private JCExpression applyVarianceCasts(JCExpression result, Type exprType,
            boolean exprBoxed,
            BoxingStrategy boxingStrategy, Type expectedType, int flags) {
        // unboxed types certainly don't need casting for variance
        if(exprBoxed || boxingStrategy == BoxingStrategy.BOXED){
            VarianceCastResult varianceCastResult = getVarianceCastResult(expectedType, exprType);
            if(varianceCastResult != null){
                result = applyVarianceCasts(result, expectedType, varianceCastResult, flags);
            }
        }
        return result;
    }

    private JCExpression applyVarianceCasts(JCExpression result, Type expectedType, VarianceCastResult varianceCastResult,
            int flags) {
        // Types with variance types need a type cast, let's start with a raw cast to get rid
        // of Java's type system constraint (javac doesn't grok multiple implementations of the same
        // interface with different type params, which the JVM allows)
        int forCompanionMask = (flags & EXPR_FOR_COMPANION) != 0 ? JT_SATISFIES : 0;
        int wantsCompanionMask = (flags & EXPR_WANTS_COMPANION) != 0 ? JT_COMPANION : 0;
        JCExpression targetType = makeJavaType(expectedType, AbstractTransformer.JT_RAW | wantsCompanionMask);
        // do not change exprType here since this is just a Java workaround
        result = make().TypeCast(targetType, result);
        // now, because a raw cast is losing a lot of info, can we do better?
        if(varianceCastResult.isBetterCastAvailable()){
            // let's recast that to something finer than a raw cast
            targetType = makeJavaType(varianceCastResult.castType, AbstractTransformer.JT_TYPE_ARGUMENT | wantsCompanionMask | forCompanionMask);
            result = make().TypeCast(targetType, result);
        }
        return result;
    }
    
    private JCExpression applySelfTypeCasts(JCExpression result, Type exprType,
            boolean exprBoxed, BoxingStrategy boxingStrategy, 
            Type expectedType, final TypeParameter selfTypeParam) {
        if (expectedType == null || selfTypeParam == null) {
            return result;
        }
        if (selfTypeParam.getType().isExactly(exprType) // self-type within its own scope
                || !exprType.isExactly(expectedType)) {
            final Type castType = findTypeArgument(exprType, selfTypeParam);
            // the fact that the original expr was or not boxed doesn't mean the current result is boxed or not
            // as boxing transformations occur before this method
            boolean resultBoxed = boxingStrategy == BoxingStrategy.BOXED
                    || boxingStrategy == BoxingStrategy.INDIFFERENT && exprBoxed;
            JCExpression targetType = makeJavaType(castType, resultBoxed ? AbstractTransformer.JT_TYPE_ARGUMENT : 0);
            result = make().TypeCast(targetType, result);
        }
        return result;
    }

    private Type findTypeArgument(Type type, TypeDeclaration declaration) {
        if(type == null)
            return null;
        Type typeArgument = type.getTypeArguments().get(declaration);
        if(typeArgument != null)
            return typeArgument;
        return findTypeArgument(type.getQualifyingType(), declaration);
    }

    private JCExpression applyJavaTypeConversions(JCExpression ret, Type exprType, Type expectedType, 
            BoxingStrategy boxingStrategy, boolean exprBoxed, boolean exprSmall, int flags) {
        if(exprType == null || boxingStrategy != BoxingStrategy.UNBOXED)
            return ret;
        Type definiteExprType = simplifyType(exprType);
        if(definiteExprType == null)
            return ret;
        // ignore the underlying type of the expr type if it was boxed, since we must have unboxed it to
        // something with no underlying type first
        String convertFrom = exprBoxed ? null : definiteExprType.getUnderlyingType();

        Type definiteExpectedType = null;
        String convertTo = null;
        if (expectedType != null) {
            definiteExpectedType = simplifyType(expectedType);
            convertTo = definiteExpectedType.getUnderlyingType();
        }
        if (convertTo == null && exprSmall && definiteExpectedType != null) {
            if (definiteExpectedType.isInteger()) {
                convertTo = "int";
            } else if (definiteExpectedType.isFloat()) {
                convertTo = "float";
            } else if (definiteExpectedType.isCharacter()) {
                convertTo = "char";
            }
        }
        // check for identity conversion
        if (convertFrom != null && convertFrom.equals(convertTo)) {
            return ret;
        }
        if (isCeylonByte(definiteExpectedType) && isCeylonInteger(exprType)) {
            if ((flags & EXPR_UNSAFE_PRIMITIVE_TYPECAST_OK) == 0) {
                if(ret instanceof JCTree.JCUnary){
                    JCTree.JCUnary unary = (JCTree.JCUnary)ret;
                    if(unary.getTag() == JCTree.Tag.NEG
                            && unary.arg instanceof JCTree.JCLiteral){
                        Object value = ((JCTree.JCLiteral)unary.arg).value;
                        if(value instanceof Integer){
                            int val = (Integer)value;
                            // if it fits let's just leave it
                            if(val >= 0 && val <= -Byte.MIN_VALUE){
                                // in the case of -128 to 127 we don't need to cast to byte by using an int literal, but only for
                                // assignment, not for method calls, so it's simpler to always cast
                                return make().TypeCast(syms().byteType, ret);
                            }
                        }
                    }
                }
            }
            ret = make().TypeCast(syms().byteType, ret);
        } else {
            if (convertTo != null) {
                if(convertTo.equals("short")) {
                    if ((flags & EXPR_UNSAFE_PRIMITIVE_TYPECAST_OK) == 0) {
                        ret = utilInvocation().toShort(ret);
                    } else {
                        ret = make().TypeCast(syms().shortType, ret);
                    }
                } else if(convertTo.equals("int")) {
                    if ((flags & EXPR_UNSAFE_PRIMITIVE_TYPECAST_OK) == 0) {
                        ret = utilInvocation().toInt(ret);
                    } else {
                        ret = make().TypeCast(syms().intType, ret);
                    }
                } else if(convertTo.equals("float")) {
                    ret = make().TypeCast(syms().floatType, ret);
                } else if(convertTo.equals("char")) {
                    ret = make().TypeCast(syms().charType, ret);
                }
            } else if (convertFrom != null
                    && (flags & EXPR_WIDEN_PRIM) != 0) {
                if (isCeylonInteger(exprType)
                        && (convertFrom.equals("int")
                            || convertFrom.equals("short")
                            || convertFrom.equals("byte"))) {
                    ret = make().TypeCast(syms().longType, ret);
                } else if (isCeylonFloat(exprType)&& 
                        convertFrom.equals("float")) {
                    ret = make().TypeCast(syms().doubleType, ret);
                }
            }
        }
        return ret;
    }
    
    private final class InvocationTermTransformer implements TermTransformer {
        private final Invocation invocation;
        private final CallBuilder callBuilder;
        
        private InvocationTermTransformer(
                Invocation invocation,
                CallBuilder callBuilder) {
            this.invocation = invocation;
            this.callBuilder = callBuilder;
        }

        @Override
        public JCExpression transform(JCExpression primaryExpr, String selector) {
            TransformedInvocationPrimary transformedPrimary = invocation.transformPrimary(primaryExpr, selector);
            callBuilder.argumentsAndTypes(transformArguments(invocation, transformedPrimary, callBuilder));
            if (invocation instanceof NamedArgumentInvocation) {
                return transformNamedArgumentInvocationOrInstantiation((NamedArgumentInvocation)invocation, callBuilder, transformedPrimary);
            } else {
                return transformPositionalInvocationOrInstantiation(invocation, callBuilder, transformedPrimary);
            }
        }
    }

    private static class VarianceCastResult {
        Type castType;
        
        VarianceCastResult(Type castType){
            this.castType = castType;
        }
        
        private VarianceCastResult(){}
        
        boolean isBetterCastAvailable(){
            return castType != null;
        }
    }
    
    private static final VarianceCastResult RawCastVarianceResult = new VarianceCastResult();

    private VarianceCastResult getVarianceCastResult(Type expectedType, Type exprType) {
        // exactly the same type, doesn't need casting
        if(expectedType == null || exprType.isExactly(expectedType))
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
            if(expectedType.isContravariant(t) || expectedType.isCovariant(t)){
                hasVariance = true;
                break;
            }
        }
        if(!hasVariance)
            return null;
        // see if we're inheriting the interface twice with different type parameters
        java.util.List<Type> satisfiedTypes = new LinkedList<Type>();
        for(Type superType : simplifyType(exprType).getSupertypes()){
            if(Decl.equal(superType.getDeclaration(), expectedType.getDeclaration()))
                satisfiedTypes.add(superType);
        }
        // discard the supertypes that have the same erasure
        for(int i=0;i<satisfiedTypes.size();i++){
            Type pt = satisfiedTypes.get(i);
            for(int j=i+1;j<satisfiedTypes.size();j++){
                Type other = satisfiedTypes.get(j);
                if(pt.isExactly(other) || haveSameErasure(pt, other)){
                    satisfiedTypes.remove(j);
                    break;
                }
            }
        }
        // we need at least two instantiations
        if(satisfiedTypes.size() <= 1)
            return null;
        boolean needsCast = false;
        // we need at least one that differs
        for(Type superType : satisfiedTypes){
            if(!exprType.isExactly(superType)){
                needsCast = true;
                break;
            }
        }
        // no cast needed if they are all the same type
        if(!needsCast)
            return null;
        // find the better cast match
        for(Type superType : satisfiedTypes){
            if(expectedType.isExactly(superType))
                return new VarianceCastResult(superType);
        }
        // nothing better than a raw cast (Stef: not sure that can happen)
        return RawCastVarianceResult;
    }

    private boolean haveSameErasure(Type pt, Type other) {
        TypeDeclaration decl1 = pt.getDeclaration();
        TypeDeclaration decl2 = other.getDeclaration();
        if(decl1 == null || decl2 == null)
            return false;
        // do we erase both to object?
        boolean erased1 = willEraseToObject(pt);
        boolean erased2 = willEraseToObject(other);
        if(erased1)
            return erased2;
        if(erased2)
            return false;
        // declarations must be the same
        // (use simplifyType() so we ignore the difference between T and T?)
        if (!simplifyType(pt).getDeclaration().equals(simplifyType(other).getDeclaration())) {
            return false;
        }
        // now see their type arguments
        java.util.List<Type> tal1 = pt.getTypeArgumentList();
        java.util.List<Type> tal2 = other.getTypeArgumentList();
        if(tal1.size() != tal2.size())
            return false;
        for(int i=0;i<tal1.size();i++){
            if(!haveSameErasure(tal1.get(i), tal2.get(i)))
                return false;
        }
        // all the same
        return true;
    }
    
    //
    // Literals
    

    JCExpression ceylonLiteral(String s) {
        JCLiteral lit = make().Literal(s);
        return lit;
    }

    static String literalValue(Tree.StringLiteral string) {
        return string.getText();
    }
    
    static String literalValue(Tree.QuotedLiteral string) {
        return string.getText().substring(1, string.getText().length()-1);
    }
    
    static int literalValue(Tree.CharLiteral ch) {
        // codePoint is at index 1 because the text is `X` (including quotation marks, so we skip them)
        return ch.getText().codePointAt(1);
    }
    
    static double literalValue(Tree.FloatLiteral literal) throws ErroneousException {
        double value = Double.parseDouble(literal.getText());
        // Don't need to handle the negative infinity and negative zero cases 
        // because Ceylon Float literals have no sign
        if (value == Double.POSITIVE_INFINITY) {
            throw new ErroneousException(literal, "literal so large it is indistinguishable from infinity: '"+ literal.getText() + "' (use infinity)");
        } else if (value == 0.0 && !literal.getText().equals("0.0")) {
            literal.addUsageWarning(Warning.zeroFloatLiteral, "literal so small it is indistinguishable from zero: '" + literal.getText() + "' (use 0.0)");
        }
        return value;
    }
    
    static Number literalValue(Tree.NaturalLiteral literal) throws ErroneousException {
        return literalValue(literal, literal.getText());
    }
    
    static private Number literalValue(Tree.NaturalLiteral literal, String text) throws ErroneousException {
        long l;
        if(text.startsWith("#")){
            l = literalValue(literal, 16, "invalid hexadecimal literal: '" + text + "' has more than 64 bits");
        } else if(text.startsWith("$")){
            l = literalValue(literal, 2, "invalid binary literal: '" + text + "' has more than 64 bits");
        } else {
            try {
                l = Long.parseLong(text);
            } catch (NumberFormatException e) {
                throw new ErroneousException(literal, "literal outside representable range: '" + text + "' is too large to be represented as an 'Integer'");
            }
        }
        if (Integer.MIN_VALUE <= l && l <= Integer.MAX_VALUE) {
            return Integer.valueOf((int)l);
        } else {
            return l;
        }
        
    }
    
    /**
     * Returns the literal value of the given literal in the given radix, 
     * or throws an ErroneousException with the given error message 
     * if the literal cannot be represented.
     * 
     * @return An Integer (if the literal can be represented as an Integer)
     * or Long (if the literal cannot be represented as an Integer, but can be 
     * represented as a Long)
     * @throws ErroneousException If the literal cannot be represented as a long
     */
    private static long literalValue(Tree.NaturalLiteral literal, int radix, String error) throws ErroneousException{
        String value = literal.getText().substring(1);
        try{
            return Convert.string2long(value, radix);
        }catch(NumberFormatException x2){
            throw new ErroneousException(literal, error);
        }
    }
    
    static Number literalValue(Tree.NegativeOp op) throws ErroneousException {
        if (op.getTerm() instanceof Tree.NaturalLiteral) {
            // To cope with -9223372036854775808 we can't just parse the 
            // number separately from the sign
            String lit = op.getTerm().getText();
            if (!lit.startsWith("#") && !lit.startsWith("$")) { 
                return literalValue((Tree.NaturalLiteral)op.getTerm(), "-" + lit);
            }
        }
        return null;
    }
    
    static Number literalValue(Tree.PositiveOp op) throws ErroneousException {
        if (op.getTerm() instanceof Tree.NaturalLiteral) {
            String lit = op.getTerm().getText();
            if (!lit.startsWith("#") && !lit.startsWith("$")) { 
                return literalValue((Tree.NaturalLiteral)op.getTerm(), lit);
            }
        }
        return null;
    }
    
    public JCExpression transform(Tree.StringLiteral string) {
        at(string);
        return ceylonLiteral(string.getText());
    }

    public JCExpression transform(Tree.QuotedLiteral string) {
        at(string);
        return ceylonLiteral(literalValue(string));
    }
    
    public JCExpression transform(Tree.CharLiteral lit) {
        at(lit);
        if (lit.getSmall()) {
            return make().Literal(TypeTag.CHAR, literalValue(lit));
        } else {
            return make().Literal(TypeTag.INT, literalValue(lit));
        }
    }

    public JCExpression transform(Tree.FloatLiteral lit) {
        try {
            return make().Literal(literalValue(lit));
        } catch (ErroneousException e) {
            // We should never get here since the error should have been 
            // reported by the UnsupportedVisitor and the containing statement
            // replaced with a throw.
            return e.makeErroneous(this);
        }
    }
    
    public JCExpression transform(Tree.NaturalLiteral lit) {
        try {
            at(lit);
            if (lit.getSmall()) {
                return make().Literal((Integer)literalValue(lit));
            } else {
                return make().Literal(literalValue(lit).longValue());
            }
        } catch (ErroneousException e) {
            // We should never get here since the error should have been 
            // reported by the UnsupportedVisitor and the containing statement
            // replaced with a throw.
            return e.makeErroneous(this);
        }
    }
    
    JCExpression transform(Tree.Literal literal) {
        if (literal instanceof Tree.StringLiteral) {
            return transform((Tree.StringLiteral)literal);
        } else if (literal instanceof Tree.NaturalLiteral) {
            return transform((Tree.NaturalLiteral)literal);
        } else if (literal instanceof Tree.CharLiteral) {
            return transform((Tree.CharLiteral)literal);
        } else if (literal instanceof Tree.FloatLiteral) {
            return transform((Tree.FloatLiteral)literal);
        } else if (literal instanceof Tree.QuotedLiteral) {
            return transform((Tree.QuotedLiteral)literal);
        }
        throw BugException.unhandledNodeCase(literal);
    }

    public JCTree transform(Tree.PackageLiteral expr) {
        at(expr);
        
        Package pkg = (Package) expr.getImportPath().getModel();
        return makePackageLiteralCall(pkg);
    }

    public JCTree transform(Tree.ModuleLiteral expr) {
        at(expr);

        Module mod = (Module) expr.getImportPath().getModel();
        return makeModuleLiteralCall(mod);
    }

    public JCTree transform(Tree.MemberLiteral expr) {
        at(expr);
        
        Declaration declaration = expr.getDeclaration();
        if(declaration == null)
            return makeErroneous(expr, "compiler bug: missing declaration");
        if(declaration.isToplevel()){
            return makeTopLevelValueOrFunctionLiteral(expr);
        }else if(expr.getWantsDeclaration()){
            return makeMemberValueOrFunctionDeclarationLiteral(expr, declaration);
        }else{
            // get its produced ref
            Reference producedReference = expr.getTarget();
            // it's a member we get from its container type
            Type containerType = producedReference.getQualifyingType();
            // if we have no container type it means we have an object member
            boolean objectMember = containerType.getDeclaration().isAnonymous();
            
            JCExpression memberCall;

            if(objectMember){
                // We don't care about the type args for the cast, nor for the reified container expr, because
                // we take the real reified container type from the container instance, and that one has the type
                // arguments
                containerType = ((Class)declaration.getContainer()).getType();
            }
            JCExpression typeCall = makeTypeLiteralCall(containerType, false, expr.getTypeModel());
            // make sure we cast it to ClassOrInterface
            String metatypeName;
            if (Decl.isConstructor(declaration)) {
                Class constructedClass = ModelUtil.getConstructedClass(declaration);
                Declaration container = getDeclarationContainer(constructedClass);
                if (constructedClass.isToplevel() || container instanceof TypeDeclaration == false) {
                    metatypeName = "Class";
                } else {
                    metatypeName = "MemberClass";
                }
            } else {
                metatypeName = "ClassOrInterface";
            }
            TypeDeclaration classOrInterfaceDeclaration = (TypeDeclaration) typeFact().getLanguageModuleModelDeclaration(metatypeName);
            JCExpression classOrInterfaceTypeExpr = makeJavaType(
                    classOrInterfaceDeclaration.appliedReference(null, Arrays.asList(containerType)).getType());

            typeCall = make().TypeCast(classOrInterfaceTypeExpr, typeCall);
            // we will need a TD for the container
            // Note that we don't use Basic for the container for object members, because that's not how we represent
            // anonymous types.
            JCExpression reifiedContainerExpr = makeReifiedTypeArgument(containerType);
            
            // make a raw call and cast
            if (Decl.isConstructor(declaration)) {
                Type callableType = producedReference.getFullType();
                /*JCExpression reifiedArgumentsExpr;
                if (Decl.isEnumeratedConstructor(Decl.getConstructor(declaration))) {
                    reifiedArgumentsExpr = makeReifiedTypeArgument(typeFact().getCallableTuple(callableType.getQualifyingType()));
                } else {
                    reifiedArgumentsExpr = makeReifiedTypeArgument(typeFact().getCallableTuple(callableType));
                }*/
                JCExpression reifiedArguments;
                if (ModelUtil.isEnumeratedConstructor(ModelUtil.getConstructor(declaration))) {
                    reifiedArguments = makeReifiedTypeArgument(typeFact().getNothingType());
                } else {
                    reifiedArguments = makeReifiedTypeArgument(typeFact().getCallableTuple(callableType));
                }
                List<JCExpression> arguments = List.of(reifiedArguments, ceylonLiteral(declaration.getName()));
                JCExpression classModel = makeSelect(typeCall, "getDeclaredConstructor");
                memberCall = make().Apply(null, classModel, arguments);
            } else if(declaration instanceof Function){
                // we need to get types for each type argument
                JCExpression closedTypesExpr = null;
                if(expr.getTypeArgumentList() != null) {
                    java.util.List<Type> typeModels = expr.getTypeArgumentList().getTypeModels();
                    if (typeModels!=null) {
                        closedTypesExpr = getClosedTypesSequential(typeModels);
                    }
                }
                // we also need type descriptors for ret and args
                Type callableType = producedReference.getFullType();
                JCExpression reifiedReturnTypeExpr = makeReifiedTypeArgument(typeFact().getCallableReturnType(callableType));
                JCExpression reifiedArgumentsExpr = makeReifiedTypeArgument(typeFact().getCallableTuple(callableType));
                List<JCExpression> arguments;
                if(closedTypesExpr != null)
                    arguments = List.of(reifiedContainerExpr, reifiedReturnTypeExpr, reifiedArgumentsExpr, 
                            ceylonLiteral(declaration.getName()), closedTypesExpr);
                else
                    arguments = List.of(reifiedContainerExpr, reifiedReturnTypeExpr, reifiedArgumentsExpr, 
                            ceylonLiteral(declaration.getName()));
                memberCall = make().Apply(null, makeSelect(typeCall, "getMethod"), arguments);
            }else if(declaration instanceof Value){
                JCExpression reifiedGetExpr = makeReifiedTypeArgument(producedReference.getType());
                String getterName = "getAttribute";
                Type ptype;
                if(!((Value)declaration).isVariable())
                    ptype = typeFact().getNothingType();
                else
                    ptype = producedReference.getType();

                JCExpression reifiedSetExpr = makeReifiedTypeArgument(ptype);
                memberCall = make().Apply(null, makeSelect(typeCall, getterName), List.of(reifiedContainerExpr, reifiedGetExpr, reifiedSetExpr, 
                        ceylonLiteral(declaration.getName())));
            }else{
                return makeErroneous(expr, "Unsupported member type: "+declaration);
            }
//            if(objectMember){
//                // now get the instance and bind it
//                // I don't think we need any expected type since objects can't be erased
//                JCExpression object = transformExpression(expr.getObjectExpression());
//                // reset the location after we transformed the expression
//                memberCall = at(expr).Apply(null, makeSelect(memberCall, "bind"), List.of(object));
//            }
            // cast the member call because we invoke it with no Java generics
            memberCall = make().TypeCast(makeJavaType(expr.getTypeModel(), JT_RAW | JT_NO_PRIMITIVES), memberCall);
            memberCall = make().TypeCast(makeJavaType(expr.getTypeModel(), JT_NO_PRIMITIVES), memberCall);
            return memberCall;
        }
    }

    JCExpression makeMemberValueOrFunctionDeclarationLiteral(Node node, Declaration declaration) {
        return makeMemberValueOrFunctionDeclarationLiteral(node, declaration, true);
    }
    
    JCExpression makeMemberValueOrFunctionDeclarationLiteral(Node node, Declaration declaration, boolean f) {
        // it's a member we get from its container declaration
        if(declaration.getContainer() instanceof ClassOrInterface == false)
            return makeErroneous(node, "compiler bug: " + declaration.getContainer() + " is not a supported type parameter container");
        
        ClassOrInterface container = (ClassOrInterface) declaration.getContainer();
        // use the generated class to get to the declaration literal
        JCExpression metamodelCall = makeTypeDeclarationLiteral(container);
        JCExpression metamodelCast = makeJavaType(typeFact().getLanguageModuleDeclarationTypeDeclaration(
                Decl.isConstructor(declaration) ? "ClassDeclaration": "ClassOrInterfaceDeclaration").getType(), 
                JT_NO_PRIMITIVES);
        metamodelCall = make().TypeCast(metamodelCast, metamodelCall);

        String memberClassName;
        String memberAccessor;
        if(declaration instanceof Class)
            memberClassName = "ClassDeclaration";
        else if (Decl.isConstructor(declaration))
            memberClassName = "ConstructorDeclaration";
        else if(declaration instanceof Interface)
            memberClassName = "InterfaceDeclaration";
        else if(declaration instanceof Function)
            memberClassName = "FunctionDeclaration";
        else if(declaration instanceof Value){
            memberClassName = "ValueDeclaration";
        } else { 
            return makeErroneous(node, "compiler bug: " + declaration + " is not a supported declaration literal");
        }
        if (Decl.isConstructor(declaration))
            memberAccessor = "getConstructorDeclaration";
        else
            memberAccessor = f ? "getMemberDeclaration" : "getDeclaredMemberDeclaration";
        
        TypeDeclaration metamodelDecl = (TypeDeclaration) typeFact().getLanguageModuleDeclarationDeclaration(memberClassName);
        JCExpression memberType = makeJavaType(metamodelDecl.getType());
        JCExpression reifiedMemberType = makeReifiedTypeArgument(metamodelDecl.getType());
        JCExpression memberCall = make().Apply(List.of(memberType), 
                                               makeSelect(metamodelCall, memberAccessor), 
                                               List.of(reifiedMemberType, ceylonLiteral(declaration.getName())));
        return memberCall;
    }

    private JCExpression makeTopLevelValueOrFunctionDeclarationLiteral(Declaration declaration) {
        // toplevel method or attribute: we need to fetch them from their module/package
        Package pkg = ModelUtil.getPackageContainer(declaration.getContainer());

        // get the package
        JCExpression packageCall = makePackageLiteralCall(pkg);
        
        // now get the toplevel
        String getter = Decl.isMethod(declaration) ? "getFunction" : "getValue";
        JCExpression toplevelCall = make().Apply(null, makeSelect(packageCall, getter), 
                                                 List.<JCExpression>of(ceylonLiteral(declaration.getName())));
        
        return toplevelCall;
    }
    
    private JCTree makeTopLevelValueOrFunctionLiteral(Tree.MemberLiteral expr) {
        Declaration declaration = expr.getDeclaration();
        JCExpression toplevelCall = makeTopLevelValueOrFunctionDeclarationLiteral(declaration);
        
        if(!expr.getWantsDeclaration()){
            ListBuffer<JCExpression> closedTypeArgs = new ListBuffer<JCExpression>();
            // expr is of type Function<Type,Arguments> or Value<Get,Set> so we can get its type like that
            JCExpression reifiedType = makeReifiedTypeArgument(expr.getTypeModel().getTypeArgumentList().get(0));
            closedTypeArgs.append(reifiedType);
            if(Decl.isMethod(declaration)){
                // expr is of type Function<Type,Arguments> so we can get its arguments type like that
                Type argumentsType = typeFact().getCallableTuple(expr.getTypeModel());
                JCExpression reifiedArguments = makeReifiedTypeArgument(argumentsType);
                closedTypeArgs.append(reifiedArguments);
                if(expr.getTypeArgumentList() != null){
                    java.util.List<Type> typeModels = expr.getTypeArgumentList().getTypeModels();
                    if(typeModels!=null){
                        JCExpression closedTypesExpr = getClosedTypesSequential(typeModels);
                        // must apply it
                        closedTypeArgs.append(closedTypesExpr);
                    }
                }
            }else{
                JCExpression reifiedSet;
                Type ptype;
                if(!((Value)declaration).isVariable())
                    ptype = typeFact().getNothingType();
                else
                    ptype = expr.getTypeModel().getTypeArgumentList().get(0);
                
                reifiedSet = makeReifiedTypeArgument(ptype);
                closedTypeArgs.append(reifiedSet);
            }
            toplevelCall = make().Apply(null, 
                    makeSelect(toplevelCall, "apply"), 
                    closedTypeArgs.toList());
            // add cast
            Type exprType = expr.getTypeModel().resolveAliases();
            JCExpression typeClass = makeJavaType(exprType, JT_NO_PRIMITIVES);
            JCExpression rawTypeClass = makeJavaType(exprType, JT_NO_PRIMITIVES | JT_RAW);
            return make().TypeCast(typeClass, make().TypeCast(rawTypeClass, toplevelCall));
        }
        return toplevelCall;
    }

    private JCExpression makePackageLiteralCall(Package pkg) {
        // get the module
        Module module = pkg.getModule();
        JCExpression moduleCall = makeModuleLiteralCall(module);
        
        // now get the package
        return make().Apply(null, makeSelect(moduleCall, "findPackage"), 
                                             List.<JCExpression>of(ceylonLiteral(pkg.getNameAsString())));
    }

    private JCExpression makeModuleLiteralCall(Module module) {
        JCExpression modulesGetIdent = naming.makeFQIdent("ceylon", "language", "meta", "modules_", "get_");
        JCExpression modulesGet = make().Apply(null, modulesGetIdent, List.<JCExpression>nil());
        JCExpression call;
        if(module.isDefaultModule()){
            call = make().Apply(null, makeSelect(modulesGet, "getDefault"), List.<JCExpression>nil());
        }else{
            call = make().Apply(null, makeSelect(modulesGet, "find"), 
                                      List.<JCExpression>of(ceylonLiteral(module.getNameAsString()),
                                                            ceylonLiteral(module.getVersion())));
        }
        // make sure we handle missing modules gracefully
        String version = module.getVersion();
        return makeMetamodelInvocation("checkModule", List.of(call, ceylonLiteral(module.getNameAsString()), version == null ? makeNull() : ceylonLiteral(version)), null);
    }

    private JCExpression getClosedTypesSequential(java.util.List<Type> typeModels) {
        ListBuffer<JCExpression> closedTypes = new ListBuffer<JCExpression>();
        for (Type producedType : typeModels) {
            closedTypes.add(makeTypeLiteralCall(producedType));
        }
        Type elementType = typeFact().getMetamodelTypeDeclaration().appliedType(null, Arrays.asList(typeFact().getAnythingType()));
        // now wrap into a sequential
        return makeSequence(closedTypes.toList(), elementType, CeylonTransformer.JT_CLASS_NEW);
    }

    private JCExpression makeTypeLiteralCall(Type producedType) {
        JCExpression typeLiteralIdent = naming.makeFQIdent("ceylon", "language", "meta", "typeLiteral_", "typeLiteral");
        JCExpression reifiedTypeArgument = makeReifiedTypeArgument(producedType.resolveAliases());
        // note that we don't pass it a Java type argument since it's not used
        return make().Apply(null, typeLiteralIdent, List.of(reifiedTypeArgument));
    }

    JCExpression makeTypeLiteralCall(Type type, boolean addCast, Type exprType) {
        // construct a call to typeLiteral<T>() and cast if required
        JCExpression call = makeTypeLiteralCall(type);
        if(addCast){
            // if we have a type that is not nothingType and not Type, we need to cast
            exprType = exprType.resolveAliases();
            if(!exprType.isUnion()
                    && !exprType.isExactly(typeFact().getMetamodelNothingTypeDeclaration().getType())
                    && !exprType.isExactly(typeFact().getMetamodelTypeDeclaration().getType())){
                JCExpression typeClass = makeJavaType(exprType, JT_NO_PRIMITIVES);
                return make().TypeCast(typeClass, call);
            }
        }
        return call;
    }

    public JCTree transform(Tree.TypeLiteral expr) {
        at(expr);
        if(!expr.getWantsDeclaration()){
            if (expr.getDeclaration() instanceof Constructor) {
                JCExpression classLiteral = makeTypeLiteralCall(expr.getType().getTypeModel().getQualifyingType(), false, expr.getTypeModel());
                TypeDeclaration classModelDeclaration = (TypeDeclaration)typeFact().getLanguageModuleModelDeclaration(
                        expr.getType().getTypeModel().getQualifyingType().getDeclaration().isMember() ? "MemberClass" : "Class");
                JCTypeCast typeCast = make().TypeCast(
                        makeJavaType(classModelDeclaration.appliedType(null, 
                                List.of(expr.getType().getTypeModel().getQualifyingType(), 
                                        typeFact().getNothingType()))), 
                        classLiteral);
                Type callableType = expr.getTypeModel().getFullType();
                JCExpression reifiedArgumentsExpr = makeReifiedTypeArgument(typeFact().getCallableTuple(callableType));
                return make().Apply(null, 
                        naming.makeQualIdent(typeCast, "getConstructor"),
                        List.<JCExpression>of(
                                reifiedArgumentsExpr,
                                make().Literal(expr.getDeclaration().getName())));
            } else {
                if (coerced) {
                    Type t = expr.getType().getTypeModel();
                    if (!typeFact().isJavaObjectArrayType(t)
                            || t.getTypeArgumentList().get(0).isClassOrInterface()) {
                        return makeSelect(makeJavaType(t, JT_NO_PRIMITIVES | JT_RAW ), "class");
                    }
                }
                return makeTypeLiteralCall(expr.getType().getTypeModel(), true, expr.getTypeModel());
            }
        }else if(expr.getDeclaration() instanceof TypeParameter){
            // we must get it from its container
            TypeParameter declaration = (TypeParameter)expr.getDeclaration();
            Node node = expr;
            return makeTypeParameterDeclaration(node, declaration);
        }else if (expr.getDeclaration() instanceof Constructor
                || expr instanceof Tree.NewLiteral) {
            Constructor ctor;
            if (expr.getDeclaration() instanceof Constructor) {
                ctor = (Constructor)expr.getDeclaration();
            } else {
                ctor = ((Class)expr.getDeclaration()).getDefaultConstructor();
            }
            JCExpression metamodelCall = makeTypeDeclarationLiteral(ModelUtil.getConstructedClass(ctor));
            metamodelCall = make().TypeCast(
                    makeJavaType(typeFact().getClassDeclarationType(), JT_RAW), metamodelCall);
            metamodelCall = make().Apply(null, 
                    naming.makeQualIdent(metamodelCall, "getConstructorDeclaration"), 
                    List.<JCExpression>of(make().Literal(ctor.getName() == null ? "" : ctor.getName())));
            if (ModelUtil.isEnumeratedConstructor(ctor)) {
                metamodelCall = make().TypeCast(
                        makeJavaType(typeFact().getValueConstructorDeclarationType(), JT_RAW), metamodelCall);
            } /*else if (Decl.isDefaultConstructor(ctor)){
                metamodelCall = make().TypeCast(
                        makeJavaType(typeFact().getDefaultConstructorDeclarationType(), JT_RAW), metamodelCall);
            } */else {
                metamodelCall = make().TypeCast(
                        makeJavaType(typeFact().getCallableConstructorDeclarationType(), JT_RAW), metamodelCall);
            }
            return metamodelCall;
        }else if(expr.getDeclaration() instanceof ClassOrInterface
                 || expr.getDeclaration() instanceof TypeAlias){
            // use the generated class to get to the declaration literal
            JCExpression metamodelCall = makeTypeDeclarationLiteral((TypeDeclaration) expr.getDeclaration());
            Type exprType = expr.getTypeModel().resolveAliases();
            // now cast if required
            if(!exprType.isExactly(((TypeDeclaration)typeFact().getLanguageModuleDeclarationDeclaration("NestableDeclaration")).getType())){
                JCExpression type = makeJavaType(exprType, JT_NO_PRIMITIVES);
                return make().TypeCast(type, metamodelCall);
            }
            return metamodelCall;
        }else{
            return makeErroneous(expr, "compiler bug: " + expr.getDeclaration() + " is an unsupported declaration type");
        }
    }

    /**
     * Makes an expression equivalent to the result of {@code `given T`} 
     * @param node
     * @param declaration
     * @return
     */
    JCExpression makeTypeParameterDeclaration(Node node,
            TypeParameter declaration) {
        Scope container = declaration.getContainer();
        if(container instanceof Declaration){
            JCExpression containerExpr;
            Declaration containerDeclaration = (Declaration) container;
            if(containerDeclaration instanceof ClassOrInterface
                    || containerDeclaration instanceof TypeAlias){
                JCExpression metamodelCall = makeTypeDeclarationLiteral((TypeDeclaration) containerDeclaration);
                JCExpression metamodelCast = makeJavaType(typeFact().getLanguageModuleDeclarationTypeDeclaration("GenericDeclaration").getType(), JT_NO_PRIMITIVES);
                containerExpr = make().TypeCast(metamodelCast, metamodelCall);
            }else if(containerDeclaration.isToplevel()) {
                containerExpr = makeTopLevelValueOrFunctionDeclarationLiteral(containerDeclaration);
            }else{
                containerExpr = makeMemberValueOrFunctionDeclarationLiteral(node, containerDeclaration);
            }
            // now it must be a ClassOrInterfaceDeclaration or a FunctionDeclaration, both of which have the method we need
            return at(node).Apply(null, makeSelect(containerExpr, "getTypeParameterDeclaration"), List.of(ceylonLiteral(declaration.getName())));
        }else{
            return makeErroneous(node, "compiler bug: " + container + " is not a supported type parameter container");
        }
    }

    JCExpression makeTypeDeclarationLiteral(TypeDeclaration declaration) {
        JCExpression classLiteral = makeUnerasedClassLiteral(declaration);
        return makeMetamodelInvocation("getOrCreateMetamodel", List.of(classLiteral), null);
    }

    public JCExpression transformStringExpression(Tree.StringTemplate expr) {
        at(expr);
        JCExpression builder;
        builder = make().NewClass(null, null, naming.makeFQIdent("java","lang","StringBuilder"), List.<JCExpression>nil(), null);

        java.util.List<Tree.StringLiteral> literals = expr.getStringLiterals();
        java.util.List<Tree.Expression> expressions = expr.getExpressions();
        for (int ii = 0; ii < literals.size(); ii += 1) {
            Tree.StringLiteral literal = literals.get(ii);
            if (!literal.getText().isEmpty()) {// ignore empty string literals
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
            if (isCeylonBasicType(expression.getTypeModel())
                    && expression.getUnboxed()) {// TODO: Test should be erases to String, long, int, boolean, char, byte, float, double
                // If erases to a Java primitive just call append, don't box it just to call format. 
                String method = isCeylonCharacter(expression.getTypeModel()) ? "appendCodePoint" : "append";
                builder = make().Apply(null, makeSelect(builder, method), List.<JCExpression>of(
                        transformExpression(expression, BoxingStrategy.UNBOXED, null)));
            } else {
                JCMethodInvocation formatted = make().Apply(null, makeSelect(transformExpression(expression), "toString"), List.<JCExpression>nil());
                builder = make().Apply(null, makeSelect(builder, "append"), List.<JCExpression>of(formatted));
            }
        }

        return make().Apply(null, makeSelect(builder, "toString"), List.<JCExpression>nil());
    }

    JCExpression transform(Tree.SequenceEnumeration value) {
        at(value);
        if (value.getSequencedArgument() != null) {
            Tree.SequencedArgument sequencedArgument = value.getSequencedArgument();
            java.util.List<Tree.PositionalArgument> list = sequencedArgument.getPositionalArguments();
            if(list.isEmpty())
                return makeErroneous(value, "compiler bug: empty iterable literal with sequenced arguments: "+value);
            Type seqElemType = typeFact().getIteratedType(value.getTypeModel());
            seqElemType = wrapInOptionalForInterop(seqElemType, expectedType, containsUncheckedNulls(list));
            Type absentType = typeFact().getIteratedAbsentType(value.getTypeModel());
            if (Strategy.useConstantIterable(sequencedArgument)) {
                return makeConstantIterable(sequencedArgument, seqElemType, absentType, 0);
            } else {
                if(list.size() == 1 && list.get(0) instanceof Tree.Comprehension){
                    Type type = typeFact().getIterableType(seqElemType);
                    return expressionGen().transformComprehension((Tree.Comprehension) list.get(0), type);
                }
                return makeLazyIterable(sequencedArgument, seqElemType, absentType, 0);
            }
        } else {
            return makeEmpty();
        }
    }

    private boolean containsUncheckedNulls(java.util.List<Tree.PositionalArgument> list) {
        for(Tree.PositionalArgument arg : list){
            if(arg instanceof Tree.ListedArgument){
                if(containsUncheckedNulls(((Tree.ListedArgument) arg).getExpression()))
                    return true;
            }else if(arg instanceof Tree.Comprehension){
                if(containsUncheckedNulls((Tree.Comprehension)arg))
                    return true;
            }else if(arg instanceof Tree.SpreadArgument){
                if(containsUncheckedNulls(((Tree.SpreadArgument) arg).getExpression()))
                    return true;
            }
        }
        return false;
    }

    private boolean containsUncheckedNulls(Tree.Term term){
        if(term instanceof Tree.Expression){
            return containsUncheckedNulls(((Tree.Expression) term).getTerm());
        }else if(term instanceof Tree.SequenceEnumeration){
            Tree.SequencedArgument sequencedArgument = ((Tree.SequenceEnumeration) term).getSequencedArgument();
            if(sequencedArgument == null)
                return false;
            return containsUncheckedNulls(sequencedArgument.getPositionalArguments());
        }else
            return hasUncheckedNulls(term);
    }
    
    private boolean containsUncheckedNulls(Tree.Comprehension comp) {
        Tree.ComprehensionClause clause = comp.getInitialComprehensionClause();
        while(clause instanceof Tree.ExpressionComprehensionClause == false){
            if(clause instanceof Tree.ForComprehensionClause)
                clause = ((Tree.ForComprehensionClause) clause).getComprehensionClause();
            else if(clause instanceof Tree.IfComprehensionClause)
                clause = ((Tree.IfComprehensionClause) clause).getComprehensionClause();
            else
                return false;// error recovery
        }
        if(clause instanceof Tree.ExpressionComprehensionClause)
            return containsUncheckedNulls(((Tree.ExpressionComprehensionClause) clause).getExpression());
        return false;
    }

    public JCExpression transform(Tree.Tuple value) {
        Tree.SequencedArgument sequencedArgument = value.getSequencedArgument();
        if(sequencedArgument != null){
            java.util.List<Tree.PositionalArgument> args = sequencedArgument.getPositionalArguments();
            return makeTuple(value.getTypeModel(), args);
        }
        // nothing in there
        return makeEmpty();
    }

    private JCExpression sequentialEmptiness(JCExpression sequential, 
            Type expectedType, Type sequentialType) {
        int flags = 0;
        // make sure we detect that we're downcasting a sequential into a sequence if we know the comprehension is non-empty
        if(expectedType.getSupertype(typeFact().getSequenceDeclaration()) != null)
            flags = EXPR_DOWN_CAST;
        return applyErasureAndBoxing(sequential, sequentialType, false, true, BoxingStrategy.BOXED, expectedType, flags);
    }
    
    public JCExpression comprehensionAsSequential(Tree.Comprehension comprehension, Type expectedType) {
        JCExpression sequential = iterableToSequential(transformComprehension(comprehension));
        Type elementType = comprehension.getInitialComprehensionClause().getTypeModel();
        Type sequentialType = typeFact().getSequentialType(elementType);
        return sequentialEmptiness(sequential, expectedType, sequentialType);
    }
    
    private JCExpression makeTuple(Type tupleType, java.util.List<Tree.PositionalArgument> expressions) {
        if (typeFact().isEmptyType(tupleType)) {
            return makeEmpty();// A tuple terminated by empty
        }
        
        JCExpression tail = null;
        List<JCExpression> elems = List.<JCExpression>nil();
        for (int i = 0; i < expressions.size(); i++) {
            Tree.PositionalArgument expr = expressions.get(i);
            if (expr instanceof Tree.ListedArgument) {
                JCExpression elem = transformExpression(((Tree.ListedArgument) expr).getExpression());
                elems = elems.append(elem);
            } else if (expr instanceof Tree.SpreadArgument) {
                Tree.SpreadArgument spreadExpr = (Tree.SpreadArgument) expr;
                // make sure we get a spread part of the right type
                Type spreadType = spreadExpr.getExpression().getTypeModel();
                Type sequentialSpreadType = null;
                // try to get a Sequence
                if (typeFact().isNonemptyIterableType(spreadType))
                    sequentialSpreadType = spreadType.getSupertype(typeFact().getSequenceDeclaration());
                // failing that, try Sequential
                if(sequentialSpreadType == null)
                    sequentialSpreadType = spreadType.getSupertype(typeFact().getSequentialDeclaration());
                
                if(sequentialSpreadType != null){
                    tail = transformExpression(spreadExpr.getExpression(), BoxingStrategy.BOXED, sequentialSpreadType);
                }else {
                    // must at least be an Iterable, Java Iterable, or Java array then
                    Type iterableSpreadType;
                    Type iteratedType;
                    if (typeFact().isIterableType(spreadType)) {
                        iterableSpreadType = spreadType.getSupertype(typeFact().getIterableDeclaration());
                        iteratedType = typeFact().getIteratedType(iterableSpreadType);
                        tail = transformExpression(spreadExpr.getExpression(), BoxingStrategy.BOXED, iterableSpreadType);
                        tail = utilInvocation().sequentialOf(makeReifiedTypeArgument(iteratedType), tail);
                    } else if (typeFact().isJavaIterableType(spreadType)) {
                        iterableSpreadType = spreadType.getSupertype(typeFact().getJavaIterableDeclaration());
                        iteratedType = typeFact().getJavaIteratedType(iterableSpreadType);
                        tail = transformExpression(spreadExpr.getExpression(), BoxingStrategy.BOXED, iterableSpreadType);
                        tail = utilInvocation().toIterable(
                                makeJavaType(iteratedType, JT_TYPE_ARGUMENT), 
                                makeReifiedTypeArgument(iteratedType), tail);
                        tail = make().Apply(null, makeSelect(tail, "sequence"), List.<JCExpression>nil());
                    } else if (typeFact().isJavaArrayType(spreadType)) {
                        if (typeFact().isJavaObjectArrayType(spreadType)) {
                            iterableSpreadType = spreadType.getSupertype(typeFact().getJavaObjectArrayDeclaration());
                        } else {//primitive
                            iterableSpreadType = spreadType;
                        }
                        iteratedType = typeFact().getJavaArrayElementType(iterableSpreadType);
                        tail = transformExpression(spreadExpr.getExpression(), BoxingStrategy.BOXED, iterableSpreadType);
                        if (typeFact().isJavaObjectArrayType(spreadType)) {
                            tail = utilInvocation().toIterable(
                                    makeJavaType(iteratedType, JT_TYPE_ARGUMENT), 
                                    makeReifiedTypeArgument(iteratedType), tail);
                        } else {//primitive
                            tail = utilInvocation().toIterable(tail);
                        }
                        tail = make().Apply(null, makeSelect(tail, "sequence"), List.<JCExpression>nil());
                    } else {
                        throw BugException.unhandledTypeCase(spreadType);
                    }
                    
                    Type elementType = typeFact().getIteratedType(spreadExpr.getTypeModel());
                    Type sequentialType = typeFact().getSequentialType(elementType);
                    Type expectedType = spreadExpr.getTypeModel();
                    if (typeFact().isNonemptyIterableType(spreadExpr.getTypeModel())) {
                        expectedType = typeFact().getSequenceType(elementType);
                    } else if (typeFact().isIterableType(spreadExpr.getTypeModel())) {
                        expectedType = typeFact().getSequentialType(elementType);
                    }
                    tail = sequentialEmptiness(tail, expectedType, sequentialType);
                }
            } else if (expr instanceof Tree.Comprehension) {
                Tree.Comprehension comp = (Tree.Comprehension) expr;
                Type elementType = expr.getTypeModel(); 
                Type expectedType = comp.getInitialComprehensionClause().getPossiblyEmpty() 
                        ? typeFact().getSequentialType(elementType)
                        : typeFact().getSequenceType(elementType);
                tail = comprehensionAsSequential(comp, expectedType);
            } else {
                return makeErroneous(expr, "compiler bug: " + expr.getNodeType() + " is not a supported tuple argument");
            }
        }
        
        if (!elems.isEmpty()) {
            JCExpression reifiedTypeArg = makeReifiedTypeArgument(tupleType.getTypeArgumentList().get(0));
            List<JCExpression> args = List.<JCExpression>of(reifiedTypeArg);
            args = args.append(make().NewArray(make().Type(syms().objectType), List.<JCExpression>nil(), elems));
            if (tail != null) {
                args = args.append(tail);
            }
            JCExpression typeExpr = makeJavaType(tupleType, JT_TYPE_ARGUMENT);
            /* Tuple.instance(reifiedElement, new Object[]{elem, elem, elem}, tail) */
            return make().TypeCast(typeExpr, make().Apply(
                    List.<JCExpression>nil(), 
                    naming.makeQualIdent(make().QualIdent(syms().ceylonTupleType.tsym), "instance"), 
                    args));
        } else {
            return tail;
        }
    }
    
    public JCTree transform(Tree.This expr) {
        at(expr);
        if (needDollarThis(expr.getScope())) {
            return naming.makeQuotedThis();
        }
        if (isWithinSyntheticClassBody()) {
            return naming.makeQualifiedThis(makeJavaType(expr.getTypeModel()));
        } 
        return naming.makeThis();
    }

    public JCTree transform(Tree.Super expr) {
        throw new BugException(expr, "unreachable");
    }

    public JCTree transform(Tree.Outer expr) {
        at(expr);
        
        Type outerClass = org.eclipse.ceylon.model.typechecker.model.ModelUtil.getOuterClassOrInterface(expr.getScope());
        return makeOuterExpr(outerClass);
    }

    JCExpression makeOuterExpr(Type outerClass) {
        final TypeDeclaration outerDeclaration = outerClass.getDeclaration();
        if (outerDeclaration instanceof Interface) {
            return makeQualifiedDollarThis(outerClass);
        }
        return naming.makeQualifiedThis(makeJavaType(outerClass));
    }

    //
    // Unary and Binary operators that can be overridden
    
    //
    // Unary operators

    public JCExpression transform(Tree.NotOp op) {
        // No need for an erasure cast since Term must be Boolean and we never need to erase that
        JCExpression term = transformExpression(op.getTerm(), CodegenUtil.getBoxingStrategy(op), op.getTypeModel());
        JCUnary jcu = at(op).Unary(JCTree.Tag.NOT, term);
        return jcu;
    }

    public JCExpression transform(Tree.OfOp op) {
        if (op.getTerm() instanceof Tree.Super) {
            // This should be unreachable
            throw new BugException(op, "unreachable");
        } 
        Type expectedType = op.getType().getTypeModel();
        if (expectedType.isExactlyNothing()) {
            expectedType = typeFact().getNothingType();
        }
        return transformExpression(op.getTerm(), CodegenUtil.getBoxingStrategy(op), expectedType, EXPR_FORCE_CAST);
    }

    public JCExpression transform(Tree.IsOp op) {
        // make sure we do not insert null checks if we're going to allow testing for null
        Type expectedType = getOptionalTypeForInteropIfAllowed(op.getType().getTypeModel(), op.getTerm().getTypeModel(), op.getTerm());
        // we don't need any erasure type cast for an "is" test
        JCExpression expression = transformExpression(op.getTerm(), BoxingStrategy.BOXED, expectedType);
        at(op);
        Naming.SyntheticName varName = naming.temp();
        JCExpression test = makeOptimizedTypeTest(null, varName, op.getType().getTypeModel(), op.getTerm().getTypeModel());
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
        Type termType = op.getTerm().getTypeModel();
        if(!typeFact().isOptionalType(termType)){
            termType = typeFact().getOptionalType(termType);
        }
        JCExpression expression = transformExpression(op.getTerm(), BoxingStrategy.BOXED, termType);
        at(op);
        return  make().Binary(JCTree.Tag.NE, expression, makeNull());
    }
    
    public JCExpression transform(Tree.FlipOp op) {
        return transformOverridableUnaryOperator(op, op.getUnit().getBinaryDeclaration());
    }
    
    public JCExpression transform(Tree.PositiveOp op) {
        if (op.getTerm() instanceof Tree.NaturalLiteral) {
            try {
                Number l = literalValue(op);
                if (l != null) {
                    if (op.getSmall()) {
                        return make().Literal((Integer)l);
                    } else {
                        return make().Literal(l.longValue());
                    }
                }
            } catch (ErroneousException e) {
                // We should never get here since the error should have been 
                // reported by the UnsupportedVisitor and the containing statement
                // replaced with a throw.
                return e.makeErroneous(this);
            }
        }
        return transformOverridableUnaryOperator(op, op.getUnit().getInvertableDeclaration());
    }

    public JCExpression transform(Tree.NegativeOp op) {
        at(op);
        if (op.getTerm() instanceof Tree.NaturalLiteral) {
            try {
                Number l = literalValue(op);
                if (l != null) {
                    if (op.getSmall()) {
                        return make().Literal((Integer)l);
                    } else {
                        return make().Literal(l.longValue());
                    }
                }
            } catch (ErroneousException e) {
                // We should never get here since the error should have been 
                // reported by the UnsupportedVisitor and the containing statement
                // replaced with a throw.
                return e.makeErroneous(this);
            }
        }
        if(op.getTerm() instanceof Tree.QualifiedMemberExpression){
            JCExpression ret = checkForByteLiterals((Tree.QualifiedMemberExpression)op.getTerm(), true);
            if(ret != null)
                return ret;
        }
        return transformOverridableUnaryOperator(op, op.getUnit().getInvertableDeclaration());
    }

    public JCExpression transform(Tree.UnaryOperatorExpression op) {
        return transformOverridableUnaryOperator(op, (Type)null);
    }

    private JCExpression transformOverridableUnaryOperator(Tree.UnaryOperatorExpression op, Interface compoundType) {
        Type leftType = getSupertype(op.getTerm(), compoundType);
        return transformOverridableUnaryOperator(op, leftType);
    }
    
    private JCExpression transformOverridableUnaryOperator(Tree.UnaryOperatorExpression op, Type expectedType) {
        at(op);
        Tree.Term term = op.getTerm();

        OperatorTranslation operator = Operators.getOperator(op);
        if (operator == null) {
            return makeErroneous(op, "compiler bug: " + op.getClass() + " is an unhandled operator class");
        }

        JCExpression ret;
        if(operator.getUnOpOptimisationStrategy(op, op.getTerm(), this).useJavaOperator()){
            // optimisation for unboxed types
            JCExpression expr = transformExpression(term, BoxingStrategy.UNBOXED, expectedType, EXPR_WIDEN_PRIM);
            // unary + is essentially a NOOP
            if(operator == OperatorTranslation.UNARY_POSITIVE)
                return expr;
            ret = make().Unary(operator.javacOperator, expr);
            ret = unAutoPromote(ret, op.getTypeModel(), op.getSmall());
        } else {
            if(operator == OperatorTranslation.UNARY_POSITIVE) {
                // +x is essentiall a NOOP, but in this case the expected type
                // is the self type of Invertible, so use the type of op
                return transformExpression(term, BoxingStrategy.BOXED, op.getTypeModel());
            }
            ret = make().Apply(null, makeSelect(transformExpression(term, BoxingStrategy.BOXED, expectedType), 
                    Naming.getGetterName(operator.getCeylonMethodName())), List.<JCExpression> nil());
        }
        return ret;
    }

    //
    // Binary operators
    
    public JCExpression transform(Tree.NotEqualOp op) {
        JCExpression expr = transformNotEqualNeedsNegating(op).build();
        return at(op).Unary(JCTree.Tag.NOT, expr);
    }
    
    public BinOpTransformation transformNotEqualNeedsNegating(Tree.NotEqualOp op) {
        OperatorTranslation operator = Operators.OperatorTranslation.BINARY_EQUAL;
        //OptimisationStrategy optimisationStrategy = operator.getBinOpOptimisationStrategy(op, op.getLeftTerm(), op.getRightTerm(), this);
        
        // we want it unboxed only if the operator is optimised
        // we don't care about the left erased type, since equals() is on Object
        //JCExpression left = transformExpression(op.getLeftTerm(), optimisationStrategy.getBoxingStrategy(), null, EXPR_WIDEN_PRIM);
        // we don't care about the right erased type, since equals() is on Object
        return transformOverridableBinaryOperator(op, operator, op.getLeftTerm().getTypeModel(), op.getRightTerm().getTypeModel());
    }

    public JCExpression transform(Tree.EqualOp op) {
        // we don't care about the left/right type since they're both Object
        return transformOverridableBinaryOperator(op, op.getLeftTerm().getTypeModel(), op.getRightTerm().getTypeModel()).build();
    }

    public JCExpression transform(Tree.SegmentOp op) {
        // we need to get the range bound type
        final Type type = getTypeArgument(getSupertype(op.getLeftTerm(), op.getUnit().getEnumerableDeclaration()));
        JCExpression startExpr = transformExpression(op.getLeftTerm(), BoxingStrategy.BOXED, type);
        JCExpression lengthExpr = transformExpression(op.getRightTerm(), BoxingStrategy.UNBOXED, typeFact().getIntegerType());
        return make().Apply(List.<JCExpression>of(makeJavaType(type, JT_TYPE_ARGUMENT)), 
                naming.makeLanguageFunction("measure"), 
                List.<JCExpression>of(makeReifiedTypeArgument(type), startExpr, lengthExpr));
    }

    public JCExpression transform(Tree.RangeOp op) {
        // we need to get the range bound type
        final Type type = getTypeArgument(getSupertype(op.getLeftTerm(), op.getUnit().getEnumerableDeclaration()));
        JCExpression lower = transformExpression(op.getLeftTerm(), BoxingStrategy.BOXED, type);
        JCExpression upper = transformExpression(op.getRightTerm(), BoxingStrategy.BOXED, type);
        return make().Apply(List.<JCExpression>of(makeJavaType(type, JT_TYPE_ARGUMENT)), 
                naming.makeLanguageFunction("span"), 
                List.<JCExpression>of(makeReifiedTypeArgument(type), lower, upper));
    }

    public JCExpression transform(Tree.EntryOp op) {
        // no erasure cast needed for both terms
        JCExpression key = transformExpression(op.getLeftTerm());
        JCExpression elem = transformExpression(op.getRightTerm());
        Type leftType = op.getLeftTerm().getTypeModel();
        Type rightType = op.getRightTerm().getTypeModel();
        Type entryType = typeFact().getEntryType(leftType, rightType);
        JCExpression typeExpr = makeJavaType(entryType, CeylonTransformer.JT_CLASS_NEW);
        return at(op).NewClass(null, null, typeExpr , List.<JCExpression> of(makeReifiedTypeArgument(leftType), makeReifiedTypeArgument(rightType), key, elem), null);
    }

    public JCExpression transform(Tree.DefaultOp op) {
        Term elseTerm = unwrapExpressionUntilTerm(op.getRightTerm());
        Type typeModel = typeFact().denotableType(op.getTypeModel());
        // make sure we do not insert null checks if we're going to allow testing for null
        Type rightExpectedType = getOptionalTypeForInteropIfAllowed(expectedType, typeModel, elseTerm);
        if (unwrapExpressionUntilTerm(op.getLeftTerm()) instanceof Tree.ThenOp) {
            // Optimize cond then foo else bar (avoids unnecessary boxing in particular)
            Tree.ThenOp then = (Tree.ThenOp)unwrapExpressionUntilTerm(op.getLeftTerm());
            Term condTerm = then.getLeftTerm();
            Term thenTerm = then.getRightTerm();
            JCExpression cond = transformExpression(condTerm, BoxingStrategy.UNBOXED, condTerm.getTypeModel());
            JCExpression thenpart = transformExpression(thenTerm, CodegenUtil.getBoxingStrategy(op), 
                    rightExpectedType);
            JCExpression elsepart = transformExpression(elseTerm, CodegenUtil.getBoxingStrategy(op), 
                    rightExpectedType);
            return make().Conditional(cond, thenpart, elsepart);
        }
        JCExpression left = transformExpression(op.getLeftTerm(), BoxingStrategy.BOXED, typeFact().getOptionalType(typeModel));
        JCExpression right = transformExpression(elseTerm, BoxingStrategy.BOXED, rightExpectedType);
        Naming.SyntheticName varName = naming.temp();
        JCExpression varIdent = varName.makeIdent();
        JCExpression test = at(op).Binary(JCTree.Tag.NE, varIdent, makeNull());
        JCExpression cond = make().Conditional(test , varIdent, right);
        JCExpression typeExpr = makeJavaType(typeModel, JT_NO_PRIMITIVES);
        return makeLetExpr(varName, null, typeExpr, left, cond);
    }

    public JCTree transform(Tree.ThenOp op) {
        JCExpression left = transformExpression(op.getLeftTerm(), BoxingStrategy.UNBOXED, typeFact().getBooleanType());
        JCExpression right = transformExpression(op.getRightTerm(), CodegenUtil.getBoxingStrategy(op), op.getTypeModel());
        return make().Conditional(left , right, makeNull());
    }
    
    public JCTree transform(Tree.InOp op) {
        if (isCeylonInteger(op.getLeftTerm().getTypeModel())) {
            if (op.getRightTerm() instanceof Tree.RangeOp
                && isCeylonInteger(((Tree.RangeOp)op.getRightTerm()).getLeftTerm().getTypeModel())
                && isCeylonInteger(((Tree.RangeOp)op.getRightTerm()).getRightTerm().getTypeModel())) {
                return makeOptimizedInIntegerRange(op, syms().longType);
            } else if (op.getRightTerm() instanceof Tree.SegmentOp
                    && isCeylonInteger(((Tree.SegmentOp)op.getRightTerm()).getLeftTerm().getTypeModel())
                    && isCeylonInteger(((Tree.SegmentOp)op.getRightTerm()).getRightTerm().getTypeModel())) {
                // x in y:z with x, y, z all Integer
                return makeOptimizedInIntegerOrCharacterMeasure(op, syms().ceylonIntegerType, syms().longType);
            }
        } else if (isCeylonCharacter(op.getLeftTerm().getTypeModel())) {
            if (op.getRightTerm() instanceof Tree.RangeOp
                    && isCeylonCharacter(((Tree.RangeOp)op.getRightTerm()).getLeftTerm().getTypeModel())
                    && isCeylonCharacter(((Tree.RangeOp)op.getRightTerm()).getRightTerm().getTypeModel())) {
                // x in y..z with x, y, z all Character
                return makeOptimizedInCharacterRange(op);
            } else if (op.getRightTerm() instanceof Tree.SegmentOp
                    && isCeylonCharacter(((Tree.SegmentOp)op.getRightTerm()).getLeftTerm().getTypeModel())
                    && isCeylonInteger(((Tree.SegmentOp)op.getRightTerm()).getRightTerm().getTypeModel())) {
                // x in y:z with x, y both Character, z all Integer
                return makeOptimizedInIntegerOrCharacterMeasure(op, syms().ceylonCharacterType, syms().intType);
            }
        }
        JCExpression left = transformExpression(op.getLeftTerm(), BoxingStrategy.BOXED, typeFact().getObjectType());
        JCExpression right = transformExpression(op.getRightTerm(), BoxingStrategy.BOXED, op.getRightTerm().getTypeModel()
                .getSupertype(typeFact().getCategoryDeclaration()));
        Naming.SyntheticName varName = naming.temp();
        JCExpression varIdent = varName.makeIdent();
        JCExpression contains = at(op).Apply(null, makeSelect(right, "contains"), List.<JCExpression> of(varIdent));
        JCExpression typeExpr = makeJavaType(op.getLeftTerm().getTypeModel(), JT_NO_PRIMITIVES);
        return makeLetExpr(varName, null, typeExpr, left, contains);
    }

    protected JCTree makeOptimizedInIntegerOrCharacterMeasure(Tree.InOp op, org.eclipse.ceylon.langtools.tools.javac.code.Type ceylonType, org.eclipse.ceylon.langtools.tools.javac.code.Type javaType) {
        Tree.SegmentOp rangeOp = (Tree.SegmentOp)op.getRightTerm();
        SyntheticName xName = naming.temp("x");
        SyntheticName yName = naming.temp("y");
        SyntheticName zName = naming.temp("z");
        SyntheticName wName = naming.temp("w");
        JCExpression x = transformExpression(op.getLeftTerm(), BoxingStrategy.UNBOXED, typeFact().getObjectType());
        JCExpression y = transformExpression(rangeOp.getLeftTerm(), BoxingStrategy.UNBOXED, rangeOp.getLeftTerm().getTypeModel());
        JCExpression z = transformExpression(rangeOp.getRightTerm(), BoxingStrategy.UNBOXED, rangeOp.getRightTerm().getTypeModel());
        JCExpression w = make().Apply(null, 
                naming.makeSelect(make().QualIdent(ceylonType.tsym), "offset"),
                List.<JCExpression>of(xName.makeIdent(), 
                        yName.makeIdent()));
        return make().LetExpr(List.<JCStatement>of(
                makeVar(xName, make().Type(javaType), x),
                makeVar(yName, make().Type(javaType), y),
                makeVar(zName, make().Type(syms().longType), z),
                makeVar(wName, make().Type(syms().longType), w)),
                make().Binary(JCTree.Tag.AND, 
                        make().Binary(JCTree.Tag.GT, zName.makeIdent(), make().Literal(0L)),
                        make().Binary(JCTree.Tag.AND, 
                                make().Binary(JCTree.Tag.LE, make().Literal(0L), wName.makeIdent()),
                                make().Binary(JCTree.Tag.LT, wName.makeIdent(), zName.makeIdent()))
                        
                                ));
    }

    protected JCTree makeOptimizedInIntegerRange(Tree.InOp op, org.eclipse.ceylon.langtools.tools.javac.code.Type type) {
        // x in y..z with x, y, z all Integer
        org.eclipse.ceylon.langtools.tools.javac.code.Type ceylonType = syms().ceylonIntegerType;
        Tree.RangeOp rangeOp = (Tree.RangeOp)op.getRightTerm();
        JCExpression x = transformExpression(op.getLeftTerm(), BoxingStrategy.UNBOXED, typeFact().getObjectType());
        JCExpression first = transformExpression(rangeOp.getLeftTerm(), BoxingStrategy.UNBOXED, rangeOp.getLeftTerm().getTypeModel());
        JCExpression last = transformExpression(rangeOp.getRightTerm(), BoxingStrategy.UNBOXED, rangeOp.getRightTerm().getTypeModel());
        SyntheticName xName = naming.temp("x");
        SyntheticName firstName = naming.temp("y");
        SyntheticName lastName = naming.temp("z");
        SyntheticName recursiveName = naming.temp("recursive");
        return make().LetExpr(List.<JCStatement>of(
                makeVar(xName, make().Type(type), x),
                makeVar(firstName, make().Type(type), first),
                makeVar(lastName, make().Type(type), last),
                makeVar(recursiveName, make().Type(syms().booleanType), make().Binary(JCTree.Tag.AND,
                        make().Binary(JCTree.Tag.GT,
                                firstName.makeIdent(),
                                make().Binary(JCTree.Tag.PLUS, firstName.makeIdent(), make().Literal(1L))),
                        make().Binary(JCTree.Tag.GT,
                                make().Binary(JCTree.Tag.MINUS, lastName.makeIdent(), make().Literal(1L)),
                                lastName.makeIdent())))),
                make().Conditional(recursiveName.makeIdent(), 
                        // x.offset(first) <= last.offset(first)
                        make().Binary(JCTree.Tag.LE,
                                make().Apply(null, 
                                        naming.makeSelect(make().QualIdent(ceylonType.tsym), "offset"),
                                        List.<JCExpression>of(
                                                xName.makeIdent(),
                                                firstName.makeIdent())),
                                make().Apply(null, 
                                        naming.makeSelect(make().QualIdent(ceylonType.tsym), "offset"),
                                        List.<JCExpression>of(
                                                lastName.makeIdent(),
                                                firstName.makeIdent()))),
                        make().Binary(JCTree.Tag.OR, 
                                make().Binary(JCTree.Tag.AND, 
                                        make().Binary(JCTree.Tag.LE, firstName.makeIdent(), xName.makeIdent()),
                                        make().Binary(JCTree.Tag.LE, xName.makeIdent(), lastName.makeIdent())),
                                make().Binary(JCTree.Tag.AND, 
                                        make().Binary(JCTree.Tag.LE, lastName.makeIdent(), xName.makeIdent()),
                                        make().Binary(JCTree.Tag.LE, xName.makeIdent(), firstName.makeIdent())
                                        ))));
    }
    
    protected JCTree makeOptimizedInCharacterRange(Tree.InOp op) {
        org.eclipse.ceylon.langtools.tools.javac.code.Type type = syms().intType;
        org.eclipse.ceylon.langtools.tools.javac.code.Type ceylonType = syms().ceylonCharacterType;
        // x in y..z with x, y, z all Character
        Tree.RangeOp rangeOp = (Tree.RangeOp)op.getRightTerm();
        JCExpression x = transformExpression(op.getLeftTerm(), BoxingStrategy.UNBOXED, typeFact().getObjectType());
        JCExpression first = transformExpression(rangeOp.getLeftTerm(), BoxingStrategy.UNBOXED, rangeOp.getLeftTerm().getTypeModel());
        JCExpression last = transformExpression(rangeOp.getRightTerm(), BoxingStrategy.UNBOXED, rangeOp.getRightTerm().getTypeModel());
        SyntheticName xName = naming.temp("x");
        SyntheticName firstName = naming.temp("first");
        SyntheticName lastName = naming.temp("last");
        SyntheticName recursiveName = naming.temp("recursive");
        return make().LetExpr(List.<JCStatement>of(
                makeVar(xName, make().Type(type), x),
                makeVar(firstName, make().Type(type), first),
                makeVar(lastName, make().Type(type), last),
                // annoyingly then a Span evaluates `contains` (= `containsElement`)
                // it first evaluates `recursive`, 
                // which uses `successor` and `predecessor` which throw on overflow
                // so we have to replicate that **short-circuit** logic here
                makeVar(recursiveName, make().Type(syms().booleanType), make().Binary(JCTree.Tag.AND,
                        make().Binary(JCTree.Tag.GT,
                                firstName.makeIdent(),
                                make().Apply(null, 
                                            naming.makeSelect(make().QualIdent(ceylonType.tsym), "getSuccessor"),
                                            List.<JCExpression>of(firstName.makeIdent()))),
                        make().Binary(JCTree.Tag.GT,
                                make().Apply(null, 
                                            naming.makeSelect(make().QualIdent(ceylonType.tsym), "getPredecessor"),
                                            List.<JCExpression>of(lastName.makeIdent())),
                                lastName.makeIdent())))),
                make().Conditional(make().Binary(JCTree.Tag.LT, firstName.makeIdent(), lastName.makeIdent()),
                        make().Binary(JCTree.Tag.AND, 
                                make().Binary(JCTree.Tag.LE, 
                                        xName.makeIdent(), 
                                        lastName.makeIdent()),
                                make().Binary(JCTree.Tag.GE, 
                                        xName.makeIdent(), 
                                        firstName.makeIdent())),
                        make().Binary(JCTree.Tag.AND, 
                                make().Binary(JCTree.Tag.GE, 
                                        xName.makeIdent(), 
                                        lastName.makeIdent()),
                                make().Binary(JCTree.Tag.LE, 
                                        xName.makeIdent(), 
                                        firstName.makeIdent()))
                                ));
    }
    
    // Logical operators
    
    public JCExpression transform(Tree.LogicalOp op) {
        OperatorTranslation operator = Operators.getOperator(op);
        if(operator == null){
            return makeErroneous(op, "compiler bug: " + op.getNodeType() + " is not a supported logical operator");
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
        OptimisationStrategy optimisationStrategy = OperatorTranslation.BINARY_EQUAL.getBinOpOptimisationStrategy(op, op.getLeftTerm(), op.getRightTerm(), this);
        JCExpression left = transformExpression(op.getLeftTerm(), optimisationStrategy.getBoxingStrategy(), null);
        JCExpression right = transformExpression(op.getRightTerm(), optimisationStrategy.getBoxingStrategy(), null);
        return at(op).Binary(JCTree.Tag.EQ, left, right);
    }
    
    public JCExpression transform(Tree.ComparisonOp op) {
        return transformOverridableBinaryOperator(op, op.getUnit().getComparableDeclaration()).build();
    }

    public JCExpression transform(Tree.CompareOp op) {
        return transformOverridableBinaryOperator(op, op.getUnit().getComparableDeclaration()).build();
    }

    public JCExpression transform(Tree.WithinOp op) {
        WithinTransformation within = new WithinTransformation(op);
        SyntheticName middleName = naming.alias("middle");
        List<JCStatement> vars = List.<JCStatement>of(makeVar(middleName, 
                within.makeMiddleType(), 
                within.makeMiddle()));
        
        within.setLeft(within.makeLhs());
        within.setMiddleName(middleName);
        within.setRight(within.makeRhs());

        JCExpression andExpr = within.build();
        return make().LetExpr(vars, andExpr);
    }
    
    class WithinTransformation {
        private Tree.WithinOp op;
        private SyntheticName middleName;
        private JCExpression left;
        private JCExpression right;
        private Term middleTerm;
        private Tree.Bound lowerBound;
        private Tree.Bound upperBound;
        private OptimisationStrategy opt;
        private Type middleType;
        private Type lowerType;
        private Type upperType;

        public WithinTransformation(Tree.WithinOp op) {
            this.op = op;
            this.middleTerm = op.getTerm();
            this.lowerBound = op.getLowerBound();// < or <=
            this.upperBound = op.getUpperBound();// < or <=
            
            this.middleType = getComparableType(middleTerm);
            this.lowerType = getComparableType(lowerBound.getTerm());
            this.upperType = getComparableType(upperBound.getTerm());
            
            this.opt = getWithinOptimization(middleTerm, middleType, 
                    lowerBound, lowerType, 
                    upperBound, upperType);
        }
        
        public JCExpression makeLhs() {
            return transformExpression(lowerBound.getTerm(), opt.getBoxingStrategy(), null);
        }
        public JCExpression makeLhsType() {
            return makeJavaType(lowerType, opt.getBoxingStrategy() == BoxingStrategy.UNBOXED ? 0 : JT_NO_PRIMITIVES);
        }
        public BoxingStrategy getLhsTypeBoxed() {
            return opt.getBoxingStrategy();
        }
        public JCExpression makeRhs() {
            return transformExpression(upperBound.getTerm(), opt.getBoxingStrategy(), null);
        }
        public JCExpression makeRhsType() {
            return makeJavaType(upperType, opt.getBoxingStrategy() == BoxingStrategy.UNBOXED ? 0 : JT_NO_PRIMITIVES);
        }
        public BoxingStrategy getRhsTypeBoxed() {
            return opt.getBoxingStrategy();
        }
        public JCExpression makeMiddleType() {
            return makeJavaType(middleType, opt.getBoxingStrategy() == BoxingStrategy.UNBOXED ? 0 : JT_NO_PRIMITIVES);
        }
        public JCExpression makeMiddle() {
            return transformExpression(middleTerm, opt.getBoxingStrategy(), middleType);
        }
        public BoxingStrategy getMiddleBoxed() {
            return opt.getBoxingStrategy();
        }
        
        public SyntheticName getMiddleName() {
            return middleName;
        }
        
        public Type getLowerType() {
            return lowerType;
        }
        
        public Type getMiddleType() {
            return middleType;
        }

        public Type getUpperType() {
            return upperType;
        }

        public void setMiddleName(SyntheticName middleName) {
            this.middleName = middleName;
        }

        public JCExpression getLeft() {
            return left;
        }

        public void setLeft(JCExpression left) {
            this.left = left;
        }

        public JCExpression getRight() {
            return right;
        }

        public void setRight(JCExpression right) {
            this.right = right;
        }

        protected OperatorTranslation getOperatorTranslation(Tree.Bound lowerBound) {
            return lowerBound instanceof Tree.OpenBound ? 
                    OperatorTranslation.BINARY_SMALLER : 
                    OperatorTranslation.BINARY_SMALL_AS;
        }

        private JCExpression transformWithin(Tree.WithinOp op, 
                OptimisationStrategy opt, 
                Tree.Term middleTerm,
                SyntheticName middleName, 
                Type middleType, 
                JCExpression left, 
                Tree.Bound lowerBound,
                Type lowerType, 
                JCExpression right, 
                Tree.Bound upperBound, 
                Type upperType) {
            
            JCExpression lower = transformBound(lowerBound, middleName.makeIdent(), 
                    middleType, left, lowerType, getOperatorTranslation(lowerBound), opt, middleTerm,  false);
            JCExpression upper = transformBound(upperBound, middleName.makeIdent(), 
                    middleType, right, upperType, getOperatorTranslation(upperBound), opt, middleTerm, true);
            at(op);
            OperatorTranslation andOp = OperatorTranslation.BINARY_AND;
            OptimisationStrategy optimisationStrategy = OptimisationStrategy.OPTIMISE;
            JCExpression andExpr = transformOverridableBinaryOperator(op, andOp, 
                    optimisationStrategy, lower, upper, null, null, op.getTypeModel()).build();
            return andExpr;
        }

        protected OptimisationStrategy getWithinOptimization(Tree.Term middleTerm, Type middleType,
                Tree.Bound lowerBound, Type lowerType, Tree.Bound upperBound, 
                Type upperType) {
            // If any of the terms is optimizable, then use optimized
            Tree.Term lowerTerm = lowerBound.getTerm();
            OperatorTranslation lowerOp = getOperatorTranslation(lowerBound);
            Tree.Term upperTerm = upperBound.getTerm();
            OperatorTranslation upperOp = getOperatorTranslation(upperBound);
            
            OptimisationStrategy opt;
            boolean optimizeLower = lowerOp.isTermOptimisable(lowerTerm, lowerType, ExpressionTransformer.this) == OptimisationStrategy.OPTIMISE
                    || lowerOp.isTermOptimisable(middleTerm, middleType, ExpressionTransformer.this) == OptimisationStrategy.OPTIMISE;
            boolean optimizeUpper = upperOp.isTermOptimisable(middleTerm, middleType, ExpressionTransformer.this) == OptimisationStrategy.OPTIMISE
                    || upperOp.isTermOptimisable(upperTerm, upperType, ExpressionTransformer.this) == OptimisationStrategy.OPTIMISE;
            if ((lowerType.isExactly(middleType) 
                    && middleType.isExactly(upperType)
                    && (optimizeLower
                            ||optimizeUpper))// if all same type and any optimizable
                || (optimizeLower // otherwise onle if all optimizable
                    && optimizeUpper)) {
                opt = OptimisationStrategy.OPTIMISE;
                middleType.setUnderlyingType(middleTerm.getTypeModel().getUnderlyingType());
            } else {
                opt = OptimisationStrategy.NONE;
            }
            return opt;
        }
        
        private Type getComparableType(Tree.Term middleTerm) {
            final Type middleSuper = getSupertype(middleTerm, typeFact().getComparableDeclaration());
            // Simplify Comparable<X> to X
            return middleSuper.getTypeArgumentList().get(0);
        }
        
        private JCExpression transformBound(Tree.Bound bound,
                JCExpression endpoint1, Type middleType, JCExpression endpoint2, Type otherType, 
                final OperatorTranslation operator, final OptimisationStrategy optimisationStrategy, 
                Tree.Term middleTerm, boolean isUpper) {
            final JCExpression left;
            final JCExpression right;
            Type leftType;
            if (isUpper) {
                left = endpoint1;
                leftType = middleType;
                right = endpoint2; 
                
            } else {
                left = endpoint2; 
                leftType = otherType;
                right = endpoint1;
            }
            at(bound);
            return transformOverridableBinaryOperator(middleTerm, operator, 
                    optimisationStrategy, left, right, null, leftType, null, bound.getUnit().getBooleanType()).build();
        }
        
        public JCExpression build() {
            JCExpression andExpr = transformWithin(op, opt, 
                    middleTerm, middleName, middleType, 
                    left, lowerBound, lowerType, 
                    right, upperBound, upperType);
            return andExpr;
        }
    }

    public JCExpression transform(Tree.ScaleOp op) {
        OperatorTranslation operator = Operators.getOperator(op);
        Tree.Term scalableTerm = op.getRightTerm();
        Type scalableTermType = getSupertype(scalableTerm, typeFact().getScalableDeclaration());
        SyntheticName scaleableName = naming.alias("scalable");
        JCVariableDecl scaleable = makeVar(scaleableName, 
                makeJavaType(scalableTermType, JT_NO_PRIMITIVES), 
                transformExpression(scalableTerm, BoxingStrategy.BOXED, scalableTermType));
        
        Tree.Term scaleTerm = op.getLeftTerm();
        SyntheticName scaleName = naming.alias("scale");
        Type scaleType = getTypeArgument(scalableTermType, 0);
        JCExpression scaleValue;
        if (isCeylonInteger(scaleTerm.getTypeModel())
                && isCeylonFloat(scaleType)) {
            // Disgusting coercion
            scaleValue = transformExpression(scaleTerm, BoxingStrategy.UNBOXED, scalableTerm.getTypeModel());
            scaleValue = boxType(scaleValue, typeFact().getFloatType());
        } else {
            scaleValue = transformExpression(scaleTerm, BoxingStrategy.BOXED, scaleType);
        }
        JCVariableDecl scale = makeVar(scaleName, 
                makeJavaType(scaleType, JT_NO_PRIMITIVES),
                scaleValue);
        
        at(op);
        return make().LetExpr(List.<JCStatement>of(scale, scaleable), 
                transformOverridableBinaryOperator(op, operator, OptimisationStrategy.NONE, scaleableName.makeIdent(), scaleName.makeIdent(), null, null, op.getTypeModel()).build());
    }
    
    // Arithmetic operators
    
    public JCExpression transform(Tree.ArithmeticOp op) {
        return transformOverridableBinaryOperator(op, op.getUnit().getNumericDeclaration()).build();
    }
    
    public JCExpression transform(Tree.SumOp op) {
        return transformOverridableBinaryOperator(op, op.getUnit().getSummableDeclaration()).build();
    }

    public JCExpression transform(Tree.ProductOp op) {
        return transformOverridableBinaryOperator(op, op.getUnit().getMultiplicableDeclaration()).build();
    }

    public JCExpression transform(Tree.DifferenceOp op) {
        return transformOverridableBinaryOperator(op, op.getUnit().getInvertableDeclaration()).build();
    }

    public JCExpression transform(Tree.RemainderOp op) {
        return transformOverridableBinaryOperator(op, op.getUnit().getIntegralDeclaration()).build();
    }
    
    public JCExpression transform(Tree.PowerOp op) {
        if (Strategy.inlinePowerAsMultiplication(op)) {
            try {
                Number power = getIntegerLiteralPower(op);
                if(power != null)
                    return transformOptimizedIntegerPower(op.getLeftTerm(), power);
            } catch (ErroneousException e) {
                // fall through and let the default transformation handle this
            }
        }
        return transformOverridableBinaryOperator(op, op.getUnit().getExponentiableDeclaration(), 1).build();
    }

    /**
     * Returns the literal value of the power in the given power expression, 
     * or null if the power is not an integer literal (or negation of an 
     * integer literal)
     * @throws ErroneousException
     */
    static java.lang.Number getIntegerLiteralPower(Tree.PowerOp op)
            throws ErroneousException {
        java.lang.Number power;
        Tree.Term term = unwrapExpressionUntilTerm(op.getRightTerm());
        if (term instanceof Tree.NaturalLiteral) {
            power = literalValue((Tree.NaturalLiteral)term);
        } else if (term instanceof Tree.NegativeOp &&
                ((Tree.NegativeOp)term).getTerm() instanceof Tree.NaturalLiteral) {
            power = literalValue((Tree.NegativeOp)term);
        } else {
            power = null;
        }
        return power;
    }
    
    private JCExpression transformOptimizedIntegerPower(Tree.Term base,
            Number power_) {
        JCExpression baseExpr = transformExpression(base, BoxingStrategy.UNBOXED, base.getTypeModel());
        long power = power_.longValue();
        if (power == 1) {
            return baseExpr;
        }
        SyntheticName baseAlias = naming.alias("base");
        JCExpression multiplications = baseAlias.makeIdent(); 
        while (power > 1) {
            power--;
            multiplications = make().Binary(JCTree.Tag.MUL, multiplications, baseAlias.makeIdent());
        }
        return make().LetExpr(makeVar(baseAlias, 
                    makeJavaType(base.getTypeModel()), 
                    baseExpr), 
                multiplications);
    }

    public JCExpression transform(Tree.BitwiseOp op) {
        if (op.getBinary()) {
            return transformOverridableBinaryOperator(op, typeFact().getBinaryDeclaration()).build();
        }
        else {
            Interface sd = typeFact().getSetDeclaration();
            Type leftType = getSupertype(op.getLeftTerm(), sd);
            Type rightType = getSupertype(op.getRightTerm(), sd);
            return transformOverridableBinaryOperator(op, leftType, rightType).build();
        }
    }    

    // Overridable binary operators
    

    BinOpTransformation transformOverridableBinaryOperator(Tree.BinaryOperatorExpression op, Interface compoundType) {
        return transformOverridableBinaryOperator(op, compoundType, 0);
    }
    
    private BinOpTransformation transformOverridableBinaryOperator(Tree.BinaryOperatorExpression op, Interface compoundType, int typeArgumentToUse) {
        Type leftType;
        Type rightType;
        // we do have a special case which is when the LHS is Float and RHS is Integer and the typechecker allowed coercion
        Tree.Term leftTerm = op.getLeftTerm();
        Tree.Term rightTerm = op.getRightTerm();
        if(getSupertype(leftTerm, typeFact().getFloatDeclaration()) != null
                && getSupertype(rightTerm, typeFact().getIntegerDeclaration()) != null){
            // Also keep the LHS type as Float since it's final and the special methods wouldn't be found in any supertype of compountType
            leftType = typeFact().getFloatType();
            // keep the RHS type then, since floats are not integers, the whole thing is resolved in the Java impl of Float with
            // special hidden operator methods
            rightType = typeFact().getIntegerType();
        } else {
            leftType = leftTerm.getTypeModel();
            final Type leftSuper = getSupertype(leftTerm, compoundType);
            if (!leftType.isComparable() || Decl.isValueTypeDecl(leftType)) {
                // Comparable<X> can't be optimized
                Type leftSelf = getSelfType(leftSuper);
                if (leftSelf != null && leftSelf.isSubtypeOf(leftSuper)) {
                    // Simplify Summable<X> to X
                    leftType = leftSelf;
                }
            }

            // the right type always only depends on the LHS so let's not try to find it on the right side because it may
            // be undecidable: https://github.com/ceylon/ceylon-compiler/issues/1535
            rightType = getTypeArgument(leftSuper, typeArgumentToUse);
            // Another special case with coercion
            if (leftType.isInteger() &&
                    rightType.isInteger() && 
                    rightTerm.getTypeModel().isFloat()) {
                rightType = rightTerm.getTypeModel();
            }
        }
        return transformOverridableBinaryOperator(op, leftType, rightType);
    }

    BinOpTransformation transformOverridableBinaryOperator(Tree.BinaryOperatorExpression op, Type leftType, Type rightType) {
        OperatorTranslation operator = Operators.getOperator(op);
        return transformOverridableBinaryOperator(op, operator, leftType, rightType);
    }

    protected BinOpTransformation transformOverridableBinaryOperator(Tree.BinaryOperatorExpression op,
            OperatorTranslation operator, Type leftType, Type rightType) {
        OptimisationStrategy optimisationStrategy = operator.getBinOpOptimisationStrategy(op, 
                op.getLeftTerm(), leftType, op.getRightTerm(), rightType, this);

        at(op);
        JCExpression left = transformExpression(op.getLeftTerm(), optimisationStrategy.getBoxingStrategy(), leftType, EXPR_WIDEN_PRIM);
        JCExpression right = transformExpression(op.getRightTerm(), optimisationStrategy.getBoxingStrategy(), rightType, EXPR_WIDEN_PRIM);
        return transformOverridableBinaryOperator(op, operator, optimisationStrategy, left, right, op.getLeftTerm(), op.getRightTerm(), op.getTypeModel());
    }

    private JCExpression transformOverridableBinaryOperator(Tree.Term opExpr,
            Tree.Term leftTerm, Tree.Term rightTerm, Type rightType,
            OperatorTranslation operator, OptimisationStrategy optimisationStrategy, 
            JCExpression left, Type expectedType) {
        JCExpression right = transformExpression(rightTerm, optimisationStrategy.getBoxingStrategy(), rightType, EXPR_WIDEN_PRIM);
        return transformOverridableBinaryOperator(opExpr, operator, optimisationStrategy, left, right, leftTerm, rightTerm, expectedType).build();
    }

    private BinOpTransformation transformOverridableBinaryOperator(
            Tree.Term opExpr,
            OperatorTranslation originalOperator,
            OptimisationStrategy optimisationStrategy, 
            JCExpression left, JCExpression right,
            Tree.Term leftTerm, Tree.Term rightTerm, Type expectedType) {
        return transformOverridableBinaryOperator(opExpr, originalOperator, optimisationStrategy, left, right, leftTerm, leftTerm != null ? leftTerm.getTypeModel() : null, rightTerm, expectedType);
    }
    
    class BinOpTransformation {
        private Type expectedType;
        private Tree.Term opExpr;
        private OperatorTranslation  operator;
        private OptimisationStrategy optimisationStrategy;
        private Tree.Term leftTerm;
        private JCExpression left;
        private Type leftType;
        private Tree.Term rightTerm;
        private JCExpression right;
        //private Type rightType;
        
        public Tree.Term getOpExpr() {
            return opExpr;
        }

        public Type getExpectedType() {
            return expectedType;
        }

        public void setExpectedType(Type expectedType) {
            this.expectedType = expectedType;
        }

        public void setOpExpr(Tree.Term opExpr) {
            this.opExpr = opExpr;
        }

        public OperatorTranslation getOperator() {
            return operator;
        }

        public void setOperator(OperatorTranslation operator) {
            this.operator = operator;
        }

        public OptimisationStrategy getOptimisationStrategy() {
            return optimisationStrategy;
        }

        public void setOptimisationStrategy(OptimisationStrategy optimisationStrategy) {
            this.optimisationStrategy = optimisationStrategy;
        }

        public Tree.Term getLeftTerm() {
            return leftTerm;
        }

        public void setLeftTerm(Tree.Term leftTerm) {
            this.leftTerm = leftTerm;
        }

        public JCExpression getLeft() {
            return left;
        }

        public void setLeft(JCExpression left) {
            this.left = left;
        }

        public Type getLeftType() {
            return leftType;
        }

        public void setLeftType(Type leftType) {
            this.leftType = leftType;
        }

        public Tree.Term getRightTerm() {
            return rightTerm;
        }

        public void setRightTerm(Tree.Term rightTerm) {
            this.rightTerm = rightTerm;
        }

        public JCExpression getRight() {
            return right;
        }

        public void setRight(JCExpression right) {
            this.right = right;
        }

        public Type getRightType() {
            return getRightTerm().getTypeModel();
        }

    public JCExpression build() {
        JCExpression result = null;
        
        // optimise if we can
        if(optimisationStrategy.useJavaOperator()){
            result = make().Binary(operator.javacOperator, left, right);
            if (rightTerm != null) {
                result = unAutoPromote(result, expectedType, opExpr.getSmall());
            }
            return result;
        }
        
        List<JCExpression> args = List.of(right);
        List<JCExpression> typeArgs = null;
        
        // Set operators need reified generics
        if(operator == OperatorTranslation.BINARY_UNION 
                || operator == OperatorTranslation.BINARY_INTERSECTION
                || operator == OperatorTranslation.BINARY_COMPLEMENT){
            Type otherSetElementType = typeFact().getIteratedType(rightTerm.getTypeModel());
            args = args.prepend(makeReifiedTypeArgument(otherSetElementType));
            typeArgs = List.<JCExpression>of(makeJavaType(otherSetElementType, JT_TYPE_ARGUMENT));
        }
        
        if (optimisationStrategy.useValueTypeMethod()) {
            int flags = JT_NO_PRIMITIVES;
            if (optimisationStrategy == OptimisationStrategy.OPTIMISE_VALUE_TYPE) {
                Type selfType = getSelfType(leftType);
                if (selfType != null) {
                    leftType = selfType;
                }
            }
            
            result = at(opExpr).Apply(typeArgs, naming.makeQualIdent(makeJavaType(leftType, flags), operator.getCeylonValueTypeMethodName()), args.prepend(left));
        } else {
            if ((operator == OperatorTranslation.BINARY_COMPARE
                    || operator == OperatorTranslation.BINARY_SMALL_AS
                    || operator == OperatorTranslation.BINARY_LARGE_AS
                    || operator == OperatorTranslation.BINARY_SMALLER
                    || operator == OperatorTranslation.BINARY_LARGER)
                    && willEraseToObject(leftType)) {
                left = make().TypeCast(makeJavaType(typeFact().getComparableDeclaration().getType(), JT_RAW), left);
                args = List.<JCExpression>of(make().TypeCast(makeJavaType(typeFact().getComparableDeclaration().getType(), JT_RAW), right));
            }
            result = at(opExpr).Apply(typeArgs, makeSelect(left, operator.getCeylonMethodName()), args);
        }
        return result;
    }
    }
    
    private BinOpTransformation transformOverridableBinaryOperator(
            Tree.Term opExpr,
            OperatorTranslation operator,
            OptimisationStrategy optimisationStrategy, 
            JCExpression left, JCExpression right,
            Tree.Term leftTerm, Type leftType, Tree.Term rightTerm, Type expectedType) {
        BinOpTransformation binop = new BinOpTransformation();
        binop.setExpectedType(expectedType);
        binop.setLeft(left);
        binop.setLeftTerm(leftTerm);
        binop.setLeftType(leftType);
        binop.setOperator(operator);
        binop.setOpExpr(opExpr);
        binop.setOptimisationStrategy(optimisationStrategy);
        binop.setRight(right);
        binop.setRightTerm(rightTerm);
        return binop;
    }

    // Operator-Assignment expressions

    private Type getRightType(final Tree.Term leftTerm, final Tree.Term rightTerm, Interface compoundType) {
        Type rightSupertype = getSupertype(rightTerm, compoundType);
        Type leftSupertype = getSupertype(leftTerm, compoundType);
        
        // Normally we don't look at the RHS type because it can lead to unknown types, 
        // but if we want to extract its underlying type we have to, and we deal with 
        // any eventual unknown type. Presumably unknown types will not have any useful 
        // underlying type anyways. Note that looking at the RHS allows us to not have 
        // the issue of using the LHS type wrongly for the RHS type when the LHS type 
        // is Float and the RHS type is Integer with implicit Float coercion
        if (rightSupertype == null || rightSupertype.isUnknown()) {
            // supertype could be null if, e.g. right type is Nothing
            rightSupertype = leftSupertype;
        }
        Type selfType = getSelfType(rightSupertype);
        if (selfType == null || selfType.isUnknown()) {
            selfType = getSelfType(leftSupertype);
        }
        return getMostPreciseType(leftTerm, selfType);
    }

    public JCExpression transform(final Tree.ArithmeticAssignmentOp op){
        final AssignmentOperatorTranslation operator = Operators.getAssignmentOperator(op);
        if(operator == null){
            return makeErroneous(op, "compiler bug: " + op.getNodeType() 
                 + " is not a supported arithmetic assignment operator");
        }

        final Tree.Term leftTerm = op.getLeftTerm();
        final Tree.Term rightTerm = op.getRightTerm();

        // see if we can optimise it
        if(op.getUnboxed() && isDirectAccessVariable(leftTerm)){
            return optimiseAssignmentOperator(op, operator);
        }
        
        // we can use unboxed types if both operands are unboxed
        final boolean boxResult = !op.getUnboxed();
        
        // find the proper type
        Unit unit = op.getUnit();
        final Interface compoundType;
        if(op instanceof Tree.AddAssignOp){
            compoundType = unit.getSummableDeclaration();
        }else if(op instanceof Tree.MultiplyAssignOp){
            compoundType = unit.getMultiplicableDeclaration();
        }else if(op instanceof Tree.SubtractAssignOp){
            compoundType = unit.getInvertableDeclaration();
        }else if(op instanceof Tree.RemainderAssignOp){
            compoundType = unit.getIntegralDeclaration();
        }else{
            compoundType = unit.getNumericDeclaration();
        }
        
        final Type rightType = getRightType(leftTerm, rightTerm, compoundType);
        final Type resultType = getLeastPreciseType(leftTerm, rightTerm);
        Type leftType = leftTerm.getTypeModel();

        // we work on boxed types
        return transformAssignAndReturnOperation(op, leftTerm, boxResult, leftType, resultType, 
                new AssignAndReturnOperationFactory() {
            @Override
            public JCExpression getNewValue(JCExpression previousValue) {
                // make this call: previousValue OP RHS
                return transformOverridableBinaryOperator(op, leftTerm, rightTerm, rightType,
                        operator.binaryOperator, 
                        boxResult ? 
                                OptimisationStrategy.NONE : 
                                OptimisationStrategy.OPTIMISE, 
                        previousValue, op.getTypeModel());
            }
        });
    }

    //This is very nearly a copy/paste of Tree.ArithmeticAssignmentOp
    public JCExpression transform(final Tree.BitwiseAssignmentOp op){
        final AssignmentOperatorTranslation operator = Operators.getAssignmentOperator(op);
        if(operator == null){
            return makeErroneous(op, "compiler bug: " + op.getNodeType() 
                 + " is not a supported bitwise assignment operator");
        }

        final Tree.Term leftTerm = op.getLeftTerm();
        final Tree.Term rightTerm = op.getRightTerm();

        final boolean boxResult;
        final Interface compoundType;
        final Type leftType;
        final Type rightType;
        
        if(op.getBinary()) {
            // see if we can optimise it
            if(op.getUnboxed() && isDirectAccessVariable(leftTerm)){
                return optimiseAssignmentOperator(op, operator);
            }
            
            // we can use unboxed types if both operands are unboxed
            boxResult = !op.getUnboxed();
    
            compoundType = typeFact().getBinaryDeclaration();
    
            leftType = leftTerm.getTypeModel();
            rightType = getRightType(leftTerm, rightTerm, compoundType);
        }
        else {
            // we can use unboxed types if both operands are unboxed
            boxResult = true;
    
            compoundType = typeFact().getSetDeclaration();
    
            leftType = leftTerm.getTypeModel();
            rightType = getSupertype(rightTerm, compoundType);
        }
        
        final Type resultType = getLeastPreciseType(leftTerm, rightTerm);
        
        return transformAssignAndReturnOperation(op, leftTerm, boxResult, leftType, resultType, 
                new AssignAndReturnOperationFactory() {
            @Override
            public JCExpression getNewValue(JCExpression previousValue) {
                // make this call: previousValue OP RHS
                return transformOverridableBinaryOperator(op, leftTerm, rightTerm, rightType, 
                        operator.binaryOperator, 
                        boxResult ? 
                                OptimisationStrategy.NONE : 
                                OptimisationStrategy.OPTIMISE,
                        previousValue, op.getTypeModel());
            }
        });
    }

    public JCExpression transform(final Tree.LogicalAssignmentOp op){
        final AssignmentOperatorTranslation operator = Operators.getAssignmentOperator(op);
        if(operator == null){
            return makeErroneous(op, "compiler bug: "+op.getNodeType() + " is not a supported logical assignment operator");
        }
        
        final Tree.Term leftTerm = op.getLeftTerm();
        final Tree.Term rightTerm = op.getRightTerm();
        
        // optimise if we can
        if(isDirectAccessVariable(leftTerm)){
            return optimiseAssignmentOperator(op, operator);
        }
        
        Type valueType = leftTerm.getTypeModel();
        // we work on unboxed types
        return transformAssignAndReturnOperation(op, leftTerm, false, 
                valueType, valueType, new AssignAndReturnOperationFactory(){
            @Override
            public JCExpression getNewValue(JCExpression previousValue) {
                // make this call: previousValue OP RHS
                return transformLogicalOp(op, operator.binaryOperator, 
                        previousValue, rightTerm);
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
        OperatorTranslation operator = Operators.getOperator(expr);
        if(operator == null){
            return makeErroneous(expr, "compiler bug "+expr.getNodeType() + " is not yet supported");
        }
        
        OptimisationStrategy optimisationStrategy = operator.getUnOpOptimisationStrategy(expr, expr.getTerm(), this);
        boolean canOptimise = optimisationStrategy.useJavaOperator();
        
        // only fully optimise if we don't have to access the getter/setter
        if(canOptimise && isDirectAccessVariable(expr.getTerm())){
            JCExpression term = transformExpression(expr.getTerm(), BoxingStrategy.UNBOXED, expr.getTypeModel(), EXPR_WIDEN_PRIM);
            return at(expr).Unary(operator.javacOperator, term);
        }
        
        Tree.Term term = unwrapExpressionUntilTerm(expr.getTerm());
        
        Type returnType = term.getTypeModel();

        List<JCVariableDecl> decls = List.nil();
        List<JCStatement> stats = List.nil();
        JCExpression result = null;
        // we can optimise that case a bit sometimes
        boolean boxResult = !canOptimise;

        // attr++
        // (let $tmp = attr; attr = $tmp.getSuccessor(); $tmp;)
        if(term instanceof Tree.BaseMemberExpression
                // special case for java statics Foo.attr where Foo does not need to be evaluated
                || (term instanceof Tree.QualifiedMemberExpression
                        && ((Tree.QualifiedMemberExpression)term).getStaticMethodReference())){
            JCExpression getter;
            if(term instanceof Tree.BaseMemberExpression)
                getter = transform((Tree.BaseMemberExpression)term, null);
            else
                getter = transformMemberExpression((Tree.QualifiedMemberExpression)term, null, null);
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
                successor = make().Binary(operator == OperatorTranslation.UNARY_POSTFIX_INCREMENT ? JCTree.Tag.PLUS : JCTree.Tag.MINUS, 
                        make().Ident(varName), makeInteger(1));
                successor = unAutoPromote(successor, returnType, expr.getSmall());
            }else{
                successor = make().Apply(null, 
                                         makeSelect(make().Ident(varName), operator.getCeylonMethodName()), 
                                         List.<JCExpression>nil());
                // make sure the result is boxed if necessary, the result of successor/predecessor is always boxed
                successor = boxUnboxIfNecessary(successor, true, term.getTypeModel(), CodegenUtil.getBoxingStrategy(term));
            }
            JCExpression assignment = makeAssignment(expr, term, transformAssignmentLhs(expr, term), successor);
            stats = stats.prepend(at(expr).Exec(assignment));

            // $tmp
            result = make().Ident(varName);
        }
        else if(term instanceof Tree.QualifiedMemberExpression){
            // e.attr++
            // (let $tmpE = e, $tmpV = $tmpE.attr; $tmpE.attr = $tmpV.getSuccessor(); $tmpV;)
            Tree.QualifiedMemberExpression qualified = (Tree.QualifiedMemberExpression) term;
            boolean isSuper = isSuperOrSuperOf(qualified.getPrimary());
            boolean isPackage = isPackageQualified(qualified);
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
            JCExpression getter;
            if (isSuper) {
                getter = transformMemberExpression(qualified, transformSuper(qualified), null);
            } else if (isPackage) {
                getter = transformMemberExpression(qualified, null, null);
            } else {
                getter = transformMemberExpression(qualified, make().Ident(varEName), null);
            }
            // make sure we box the results if necessary
            getter = applyErasureAndBoxing(getter, term, boxResult ? BoxingStrategy.BOXED : BoxingStrategy.UNBOXED, returnType);
            JCVariableDecl tmpVVar = make().VarDef(make().Modifiers(0), varVName, attrType, getter);

            decls = decls.prepend(tmpVVar);
            if (!isSuper && !isPackage) {
                // define all the variables
                decls = decls.prepend(tmpEVar);
            }
            
            // $tmpE.attr = $tmpV.getSuccessor()
            JCExpression successor;
            if(canOptimise){
                // use +1/-1 if we can optimise a bit
                successor = make().Binary(operator == OperatorTranslation.UNARY_POSTFIX_INCREMENT ? JCTree.Tag.PLUS : JCTree.Tag.MINUS, 
                        make().Ident(varVName), makeInteger(1));
                successor = unAutoPromote(successor, returnType, expr.getSmall());
            }else{
                successor = make().Apply(null, 
                                         makeSelect(make().Ident(varVName), operator.getCeylonMethodName()), 
                                         List.<JCExpression>nil());
                //  make sure the result is boxed if necessary, the result of successor/predecessor is always boxed
                successor = boxUnboxIfNecessary(successor, true, term.getTypeModel(), CodegenUtil.getBoxingStrategy(term));
            }
            JCExpression assignment = makeAssignment(expr, term, 
                    qualifyLhs(expr, term, isSuper ? transformSuper(qualified) : make().Ident(varEName)), successor);
            stats = stats.prepend(at(expr).Exec(assignment));
            
            // $tmpV
            result = make().Ident(varVName);
        }else{
            return makeErroneous(term, "compiler bug: " + term.getNodeType() + " is not supported yet");
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
        final OperatorTranslation operator = Operators.getOperator(expr);
        if(operator == null){
            return makeErroneous(expr, "compiler bug: "+expr.getNodeType() + " is not supported yet");
        }
        
        OptimisationStrategy optimisationStrategy = operator.getUnOpOptimisationStrategy(expr, expr.getTerm(), this);
        final boolean canOptimise = optimisationStrategy.useJavaOperator();
        
        Tree.Term term = expr.getTerm();
        // only fully optimise if we don't have to access the getter/setter
        if(canOptimise && isDirectAccessVariable(term)){
            JCExpression jcTerm = transformExpression(term, BoxingStrategy.UNBOXED, expr.getTypeModel(), EXPR_WIDEN_PRIM);
            return at(expr).Unary(operator.javacOperator, jcTerm);
        }

//        Interface compoundType = expr.getUnit().getOrdinalDeclaration();
//        Type valueType = getSupertype(term, compoundType);
        final Type returnType = term.getTypeModel();//getMostPreciseType(term, getTypeArgument(valueType, 0));
        
        // we work on boxed types unless we could have optimised
        return transformAssignAndReturnOperation(expr, term, !canOptimise, 
                term.getTypeModel(), returnType, new AssignAndReturnOperationFactory(){
            @Override
            public JCExpression getNewValue(JCExpression previousValue) {
                // use +1/-1 if we can optimise a bit
                if(canOptimise){
                    JCExpression ret = make().Binary(operator == OperatorTranslation.UNARY_PREFIX_INCREMENT ? JCTree.Tag.PLUS : JCTree.Tag.MINUS, 
                            previousValue, makeInteger(1));
                    ret = unAutoPromote(ret, returnType, expr.getSmall());
                    return ret;
                }
                // make this call: previousValue.getSuccessor() or previousValue.getPredecessor()
                return make().Apply(null, makeSelect(previousValue, operator.getCeylonMethodName()), List.<JCExpression>nil());
            }
        });
    }
    
    //
    // Function to deal with expressions that have side-effects
    
    private interface AssignAndReturnOperationFactory {
        JCExpression getNewValue(JCExpression previousValue);
    }
    
    private JCExpression transformAssignAndReturnOperation(Node operator, Tree.Term term, 
            boolean boxResult, Type valueType, Type returnType, 
            AssignAndReturnOperationFactory factory){
        
        List<JCVariableDecl> decls = List.nil();
        List<JCStatement> stats = List.nil();
        JCExpression result = null;
        // attr
        // (let $tmp = OP(attr); attr = $tmp; $tmp)
        if(term instanceof Tree.BaseMemberExpression
            || (term instanceof Tree.IndexExpression)
            // special case for java statics Foo.attr where Foo does not need to be evaluated
            || (term instanceof Tree.QualifiedMemberExpression
                    && ((Tree.QualifiedMemberExpression)term).getStaticMethodReference())){
            JCExpression getter;
            if(term instanceof Tree.BaseMemberExpression)
                getter = transform((Tree.BaseMemberExpression)term, null);
            else if (term instanceof Tree.IndexExpression)
                getter = null;
            else
                getter = transformMemberExpression((Tree.QualifiedMemberExpression)term, null, null);
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
            BoxingStrategy boxingStrategy = CodegenUtil.getBoxingStrategy(term);
            value = applyErasureAndBoxing(value, returnType, boxResult, boxingStrategy, valueType);
            JCExpression assignment = makeAssignment(operator, term, transformAssignmentLhs(operator, term), value);
            stats = stats.prepend(at(operator).Exec(assignment));
            
            // $tmp
            // return, with the box type we asked for
            result = make().Ident(varName);
        }
        else if(term instanceof Tree.QualifiedMemberExpression){
            // e.attr
            // (let $tmpE = e, $tmpV = OP($tmpE.attr); $tmpE.attr = $tmpV; $tmpV;)
            Tree.QualifiedMemberExpression qualified = (Tree.QualifiedMemberExpression) term;
            boolean isSuper = isSuperOrSuperOf(qualified.getPrimary());
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
            JCExpression getter = transformMemberExpression(qualified, isSuper ? transformSuper(qualified) : make().Ident(varEName), null);
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
            BoxingStrategy boxingStrategy = CodegenUtil.getBoxingStrategy(term);
            value = applyErasureAndBoxing(value, returnType, boxResult, boxingStrategy, valueType);
            JCExpression assignment = makeAssignment(operator, term, 
                    qualifyLhs(operator, term, isSuper ? transformSuper(qualified) : make().Ident(varEName)), value);
            stats = stats.prepend(at(operator).Exec(assignment));
            
            // $tmpV
            // return, with the box type we asked for
            result = make().Ident(varVName);
        }else{
            return makeErroneous(operator, "compiler bug: " + term.getNodeType() + " is not a supported assign and return operator");
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
        
        if (Strategy.hasDefaultParameterValueMethod(param.getParameterModel())) {
            Tree.SpecifierOrInitializerExpression spec = Decl.getDefaultArgument(param);
            Scope container = param.getParameterModel().getModel().getContainer();
            boolean classParameter = container instanceof ClassOrInterface;
            ClassOrInterface oldWithinDefaultParameterExpression = withinDefaultParameterExpression;
            if(classParameter)
                withinDefaultParameterExpression((ClassOrInterface) container);
            if (param instanceof Tree.FunctionalParameterDeclaration) {
                Tree.FunctionalParameterDeclaration fpTree = (Tree.FunctionalParameterDeclaration) param;
                Tree.SpecifierExpression lazy = (Tree.SpecifierExpression)spec;
                Function fp = (Function)fpTree.getParameterModel().getModel();
                
                expr = CallableBuilder.anonymous(gen(), param, (Function)fpTree.getTypedDeclaration().getDeclarationModel(), lazy.getExpression(), 
                        ((Tree.MethodDeclaration)fpTree.getTypedDeclaration()).getParameterLists(),
                        getTypeForFunctionalParameter(fp),
                        true).build();
            } else {
                expr = transformExpression(spec.getExpression(), 
                        CodegenUtil.getBoxingStrategy(param.getParameterModel().getModel()), 
                        param.getParameterModel().getModel().getTypedReference().getFullType());
            }
            if(classParameter)
                withinDefaultParameterExpression(oldWithinDefaultParameterExpression);
        } else {
            expr = makeErroneous(param, "compiler bug: no default parameter value method");
        }
        //needDollarThis = false;
        return expr;
    }
    
    protected final JCExpression transformArg(SimpleInvocation invocation, int argIndex) {
        final Tree.Term expr = invocation.getArgumentExpression(argIndex);
        if (invocation.hasParameter(argIndex)) {
            Type type = invocation.getParameterType(argIndex);
            if (invocation.isParameterSequenced(argIndex)
                    // Java methods need their underlying type preserved
                    && !invocation.isJavaVariadicMethod()) {
                if (!invocation.isArgumentSpread(argIndex)) {
                    // If the parameter is sequenced and the argument is not ...
                    // then the expected type of the *argument* is the type arg to Iterator
                    type = typeFact().getIteratedType(type);
                } else  if (invocation.getArgumentType(argIndex).getSupertype(typeFact().getSequentialDeclaration())
                        == null) {
                    // On the other hand, if the parameter is sequenced and the argument is spread,
                    // but not sequential, then transformArguments() will use getSequence(),
                    // so we only need to expect an Iterable type
                    type = org.eclipse.ceylon.model.typechecker.model.ModelUtil.appliedType(
                            typeFact().getIterableDeclaration(),
                            typeFact().getIteratedType(type), typeFact().getIteratedAbsentType(type));
                }
            }
            BoxingStrategy boxingStrategy = invocation.getParameterBoxingStrategy(argIndex);
            int flags = 0;
            if(!invocation.isParameterRaw(argIndex))
                flags |= ExpressionTransformer.EXPR_EXPECTED_TYPE_NOT_RAW;
            if(invocation.isParameterWithConstrainedTypeParameters(argIndex))
                flags |= ExpressionTransformer.EXPR_EXPECTED_TYPE_HAS_CONSTRAINED_TYPE_PARAMETERS;
            if(invocation.isParameterWithDependentCovariantTypeParameters(argIndex))
                flags |= ExpressionTransformer.EXPR_EXPECTED_TYPE_HAS_DEPENDENT_COVARIANT_TYPE_PARAMETERS;
            if (invocation.erasedArgument(unwrapExpressionUntilTerm(expr))) {
                flags |= EXPR_DOWN_CAST;
            }
            if (!expr.getSmall() && invocation.getParameterSmall(argIndex)) {
                flags |=  ExpressionTransformer.EXPR_UNSAFE_PRIMITIVE_TYPECAST_OK;
            }
            boolean coerced = invocation.isParameterCoerced(argIndex);
            if(coerced){
                flags |=  ExpressionTransformer.EXPR_IS_COERCED;
                if(invocation.isParameterJavaVariadic(argIndex)
                        && type.isSequential()){
                    type = typeFact().getSequentialElementType(type);
                }
            }
            JCExpression ret = transformExpression(expr, 
                    boxingStrategy, 
                    type, flags);
            
            // We can coerce most SAMs in transformExpression, EXCEPT invocation
            // calls which we do here.
            Term term = TreeUtil.unwrapExpressionUntilTerm(expr);
            if (coerced
                    && term instanceof Tree.InvocationExpression
                    && isFunctionalResult(term.getTypeModel())
                    && checkForFunctionalInterface(type) != null) {
                return transformFunctionalInterfaceBridge((Tree.InvocationExpression)term, ret, type);
            }

            return ret;
        } else {
            // Overloaded methods don't have a reference to a parameter
            // so we have to treat them differently. Also knowing it's
            // overloaded we know we're dealing with Java code so we unbox
            Type type = expr.getTypeModel();
            return expressionGen().transformExpression(expr, 
                    BoxingStrategy.UNBOXED, 
                    type);
        }
    }
    
    private final List<ExpressionAndType> transformArguments(Invocation invocation,
            TransformedInvocationPrimary transformedPrimary, CallBuilder callBuilder) {
        ListBuffer<ExpressionAndType> result = new ListBuffer<ExpressionAndType>();
        withinInvocation(false);
        // Explicit arguments
        if (invocation instanceof SuperInvocation) {
            withinSuperInvocation(((SuperInvocation)invocation).getSub());
            // for super calls, implicit arguments must be within a super invocation guard
            appendImplicitArguments(invocation, transformedPrimary, result);
            result.addAll(transformArgumentsForSimpleInvocation((SimpleInvocation)invocation, callBuilder));
            withinSuperInvocation(null);
        } else {
            appendImplicitArguments(invocation, transformedPrimary, result);
            if (invocation instanceof NamedArgumentInvocation) {
                result.addAll(transformArgumentsForNamedInvocation((NamedArgumentInvocation)invocation));
            } else if (invocation instanceof CallableSpecifierInvocation) {
                result.addAll(transformArgumentsForCallableSpecifier((CallableSpecifierInvocation)invocation));
            } else if (invocation instanceof SimpleInvocation) {
                if(invocation.isUnknownArguments())
                    result.add(transformUnknownArguments((SimpleInvocation) invocation, callBuilder));
                else
                    result.addAll(transformArgumentsForSimpleInvocation((SimpleInvocation)invocation, callBuilder));
            } else {
                throw BugException.unhandledCase(invocation);
            }
        }
        withinInvocation(true);
        return result.toList();
    }

    private void appendImplicitArguments(
            Invocation invocation,
            TransformedInvocationPrimary transformedPrimary,
            ListBuffer<ExpressionAndType> result) {
        // Implicit arguments
        // except for Java array constructors
        Declaration primaryDeclaration = invocation.getPrimaryDeclaration();
        Tree.Term primary = invocation.getPrimary();
        if(!(primaryDeclaration instanceof Value)){
            if(!(primaryDeclaration instanceof Class)
                    || !isJavaArray(((Class) primaryDeclaration).getType())){
                invocation.addReifiedArguments(result);
            }
        }
        if (!(primary instanceof Tree.BaseTypeExpression)
                && !(primary instanceof Tree.QualifiedTypeExpression)
                && (!(primary instanceof Tree.QualifiedMemberExpression) || !(((Tree.QualifiedMemberExpression)primary).getMemberOperator() instanceof Tree.SpreadOp))
                && Invocation.onValueType(this, primary, primaryDeclaration) 
                && transformedPrimary != null) {
            result.add(new ExpressionAndType(transformedPrimary.expr,
                    makeJavaType(primary.getTypeModel())));   
        }
    }
    
    private ExpressionAndType transformUnknownArguments(SimpleInvocation invocation, CallBuilder callBuilder){

        // doesn't really matter, assume Object, it's not used
        Type iteratedType = typeFact().getObjectType();
        // the single spread argument which is allowed
        JCExpression rest = null;
        ListBuffer<JCExpression> initial = new ListBuffer<JCExpression>();
        for (int ii = 0; ii < invocation.getNumArguments(); ii++) {
            if (invocation.isArgumentSpread(ii)) {
                rest = invocation.getTransformedArgumentExpression(ii);
            } else {
                initial.add(invocation.getTransformedArgumentExpression(ii));
            }
        }
        JCExpression expr;
        if (initial.isEmpty()) {
            expr = make().TypeCast(makeJavaType(typeFact().getSequentialDeclaration().getType(), JT_RAW), rest);
        } else {
            expr = utilInvocation().sequentialInstance(null, makeReifiedTypeArgument(iteratedType), 
                    rest != null ? rest : makeEmptyAsSequential(true), initial.toList());
        }
        
        JCExpression type = makeJavaType(typeFact().getSequenceType(iteratedType).getType());
        return new ExpressionAndType(expr, type);
    }
    
    private List<ExpressionAndType> transformArgumentsForSimpleInvocation(SimpleInvocation invocation, CallBuilder callBuilder) {
        final Constructor superConstructor = invocation.getConstructor();
        CtorDelegation constructorDelegation;
        if (invocation instanceof SuperInvocation) {
            constructorDelegation = ((SuperInvocation)invocation).getDelegation();
        } else {
            constructorDelegation = null;
        }
        
        List<ExpressionAndType> result = List.<ExpressionAndType>nil();
        if (!(invocation instanceof SuperInvocation)
                || !((SuperInvocation)invocation).isDelegationDelegation() ) {
            int numArguments = invocation.getNumArguments();
            if (invocation.getNumParameters() == 0) {
                // skip transforming arguments
                // (Usually, numArguments would already be null, but it's possible to call a
                //  parameterless function with a *[] argument - see #1593.)
                numArguments = 0;
            }
            boolean wrapIntoArray = false;
            ListBuffer<JCExpression> arrayWrap = new ListBuffer<JCExpression>();
            
            for (int argIndex = 0; argIndex < numArguments; argIndex++) {
                BoxingStrategy boxingStrategy = invocation.getParameterBoxingStrategy(argIndex);
                Type parameterType = invocation.getParameterType(argIndex);
                // for Java methods of variadic primitives, it's better to wrap them ourselves into an array
                // to avoid ambiguity of foo(1,2) for foo(int...) and foo(Object...) methods
                if(!wrapIntoArray
                        && invocation.isParameterJavaVariadic(argIndex)
                        && boxingStrategy == BoxingStrategy.UNBOXED
                        && willEraseToPrimitive(typeFact().getDefiniteType(parameterType))
                        && !invocation.isSpread())
                    wrapIntoArray = true;
    
                ExpressionAndType exprAndType;
                if (invocation.isArgumentSpread(argIndex)) {
                    if (!invocation.isParameterSequenced(argIndex)) {
                        result = transformSpreadTupleArgument(invocation, callBuilder,
                                result, argIndex);
                        break;
                    }
                    if(invocation.isJavaVariadicMethod()){
                        // if it's a java method we need a special wrapping
                        exprAndType = transformSpreadArgument(invocation,
                                numArguments, argIndex, boxingStrategy,
                                parameterType);
                        argIndex = numArguments;
                    }else{
                        Type argType = invocation.getArgumentType(argIndex);
                        if (argType.getSupertype(typeFact().getSequentialDeclaration()) != null) {
                            exprAndType = transformArgument(invocation, argIndex,
                                    boxingStrategy);
                        } else if (argType.getSupertype(typeFact().getIterableDeclaration()) != null) {
                            exprAndType = transformArgument(invocation, argIndex,
                                    boxingStrategy);
                            JCExpression sequential = iterableToSequential(exprAndType.expression);
                            if(invocation.isParameterVariadicPlus(argIndex)){
                                Type iteratedType = typeFact().getIteratedType(argType);
                                sequential = utilInvocation().castSequentialToSequence(sequential, iteratedType);
                            }
                            exprAndType = new ExpressionAndType(sequential, exprAndType.type);
                        } else if (typeFact().isJavaArrayType(argType)) {
                            exprAndType = transformArgument(invocation, argIndex,
                                    boxingStrategy);
                            JCExpression iterable;
                            if (typeFact().isJavaPrimitiveArrayType(argType)) {
                                iterable = utilInvocation().toIterable(
                                        exprAndType.expression);
                            } else {
                                Type elementType = typeFact().getJavaArrayElementType(argType);
                                iterable = utilInvocation().toIterable(
                                        makeJavaType(elementType, JT_TYPE_ARGUMENT),
                                        makeReifiedTypeArgument(elementType),
                                        exprAndType.expression);
                            }
                            exprAndType = new ExpressionAndType(
                                    make().Apply(null,
                                    makeSelect(iterable, "sequence"),
                                    List.<JCExpression>nil()),
                                    makeJavaType(argType));
                        } else if (typeFact().isJavaIterableType(argType)) {
                            exprAndType = transformArgument(invocation, argIndex,
                                    boxingStrategy);
                            Type elementType = typeFact().getJavaIteratedType(argType);
                            JCExpression iterable = utilInvocation().toIterable(
                                    makeJavaType(elementType, JT_TYPE_ARGUMENT),
                                    makeReifiedTypeArgument(elementType),
                                    exprAndType.expression);
                            exprAndType = new ExpressionAndType(
                                    make().Apply(null,
                                    makeSelect(iterable, "sequence"),
                                    List.<JCExpression>nil()),
                                    makeJavaType(argType));
                        } else {
                            exprAndType = new ExpressionAndType(makeErroneous(invocation.getNode(), "compiler bug: unexpected spread argument"), makeErroneous(invocation.getNode(), "compiler bug: unexpected spread argument"));
                        }
                    }
                } else if (!invocation.isParameterSequenced(argIndex)
                        // if it's sequenced, Java and there's no spread at all, pass it along
                        || (invocation.isParameterJavaVariadic(argIndex) && !invocation.isSpread())) {
                    exprAndType = transformArgument(invocation, argIndex,
                            boxingStrategy);
                    // Callable has a variadic 1-param method that if you invoke it with a Java Object[] will confuse javac and give
                    // preference to the variadic method instead of the $call$(Object) one, so we force a cast to Object to resolve it
                    // This is not required for primitive arrays since they are not Object[]
                    if(numArguments == 1 
                            && invocation.isIndirect()){
                        Type argumentType = invocation.getArgumentType(0);
                        if(isJavaObjectArray(argumentType)
                                || isNull(argumentType)){
                            exprAndType = new ExpressionAndType(make().TypeCast(makeJavaType(typeFact().getObjectType()), exprAndType.expression),
                                    exprAndType.type);
                        }
                    }else if(invocation.isParameterJavaVariadic(argIndex) && !invocation.isSpread()){
                        // in fact, the very same problem happens when passing null or object arrays to a java variadic method
                        Type argumentType = invocation.getArgumentType(argIndex);
                        if(isJavaObjectArray(argumentType)
                                || isNull(argumentType)){
                            // remove any ambiguity
                            exprAndType = new ExpressionAndType(make().TypeCast(makeJavaType(parameterType), exprAndType.expression),
                                    exprAndType.type);
                        }
                    }
                } else {
                    // we must have a sequenced param
                    if(invocation.isSpread()){
                        exprAndType = transformSpreadArgument(invocation,
                                numArguments, argIndex, boxingStrategy,
                                parameterType);
                        argIndex = numArguments;
                    }else{
                        exprAndType = transformVariadicArgument(invocation,
                                numArguments, argIndex, parameterType);
                        argIndex = numArguments;
                    }
                }
                if(!wrapIntoArray) {
                    if (argIndex== 0 
                            && invocation.isCallable()
                            && !invocation.isArgumentSpread(numArguments-1)
                            ) {
                        exprAndType = new ExpressionAndType(
                                make().TypeCast(make().Type(syms().objectType), exprAndType.expression),
                                make().Type(syms().objectType));
                    }
                    result = result.append(exprAndType);
                } else {
                    arrayWrap.append(exprAndType.expression);
                }
            }
            if (invocation.isIndirect()
                    && invocation.getNumParameters() > numArguments
                    && invocation.isParameterSequenced(numArguments)
                    && !invocation.isArgumentSpread(numArguments-1)
                    ) {
                // Calling convention for indirect variadic invocation's requires
                // explicit variadic argument (can't use the overloading trick)
                result = result.append(new ExpressionAndType(makeEmptyAsSequential(true), make().Erroneous()));
            }
            if(wrapIntoArray){
                // must have at least one arg, so take the last one
                Type parameterType = invocation.getParameterType(numArguments-1);
                JCExpression arrayType = makeJavaType(parameterType, JT_RAW);
                
                JCNewArray arrayExpr = make().NewArray(arrayType, List.<JCExpression>nil(), arrayWrap.toList());
                JCExpression arrayTypeExpr = make().TypeArray(makeJavaType(parameterType, JT_RAW));
                result = result.append(new ExpressionAndType(arrayExpr, arrayTypeExpr));
            }
            
        } else {
            for (Parameter p : constructorDelegation.getConstructor().getParameterList().getParameters()) {
                result = result.append(new ExpressionAndType(naming.makeName(p.getModel(), Naming.NA_IDENT | Naming.NA_ALIASED), null));
            }
        }
        
        boolean concreteDelegation = invocation instanceof SuperInvocation
                && ((SuperInvocation)invocation).getDelegation().isConcreteSelfDelegation();
        Tree.Primary qmep = invocation.getQmePrimary();
        if (superConstructor == null && 
                concreteDelegation) {
            Constructor delegateTo = ((SuperInvocation)invocation).getDelegation().getConstructor();
            result = result.prepend(
                    new ExpressionAndType(naming.makeNamedConstructorName(delegateTo, true),
                            naming.makeNamedConstructorType(delegateTo, true)));
        } else if (
                superConstructor != null 
                    && constructorDelegation != null
                    && constructorDelegation.isSelfDelegation()) {
            result = result.prepend(
                    new ExpressionAndType(naming.makeNamedConstructorName(constructorDelegation.getExtendingConstructor(), concreteDelegation),
                            naming.makeNamedConstructorType(constructorDelegation.getExtendingConstructor(), concreteDelegation)));
        } else if (
                superConstructor != null 
                    && !Decl.isDefaultConstructor(superConstructor)
                    && !Decl.isJavaArrayWith(superConstructor)
                    && (!(qmep instanceof Tree.QualifiedTypeExpression)
                            //very important here to distinguish between static ref to 
                            //constructors of member classes, and ref to constructors of 
                            //static classes
                            || ((Tree.QualifiedTypeExpression)qmep).getStaticMethodReference()
                            && ((Tree.QualifiedTypeExpression)qmep).getDeclaration().isStatic()
                            || !isCeylonCallable(((Tree.QualifiedTypeExpression)qmep).getPrimary().getTypeModel()))) {
            result = result.prepend(
                    new ExpressionAndType(naming.makeNamedConstructorName(superConstructor, concreteDelegation),
                            naming.makeNamedConstructorType(superConstructor, concreteDelegation)));
        }
        
        return result;
    }

    private ExpressionAndType transformVariadicArgument(
            SimpleInvocation invocation, int numArguments, int argIndex,
            Type parameterType) {
        ExpressionAndType exprAndType;
        final Type iteratedType = typeFact().getIteratedType(parameterType);
        final JCExpression expr;
        final JCExpression type;
        // invoking f(a, b, c), where declared f(A a, B* b)
        // collect each remaining argument and box with an ArraySequence<T>
        List<JCExpression> x = List.<JCExpression>nil();
        for (int ii = argIndex ; ii < numArguments; ii++) {
            x = x.append(invocation.getTransformedArgumentExpression(ii));
        }
        expr = makeSequence(x, iteratedType, JT_TYPE_ARGUMENT);
        type = makeJavaType(typeFact().getSequenceType(iteratedType).getType());
        exprAndType = new ExpressionAndType(expr, type);
        return exprAndType;
    }

    private ExpressionAndType transformSpreadArgument(
            SimpleInvocation invocation, int numArguments, int argIndex,
            BoxingStrategy boxingStrategy, Type parameterType) {
        ExpressionAndType exprAndType;
        final Type iteratedType = typeFact().getIteratedType(parameterType);
        final JCExpression expr;
        final JCExpression type;
        // optimise "*javaArray.iterable" into "javaArray" for java variadic parameters, since we can pass them just along
        if(invocation.isJavaVariadicMethod()
                && numArguments == argIndex+1
                && !invocation.isArgumentComprehension(argIndex)){
            Expression argumentExpression = invocation.getArgumentExpression(argIndex);
            Term argument = TreeUtil.unwrapExpressionUntilTerm(argumentExpression);
            if (argument instanceof Tree.QualifiedMemberExpression) {
                Tree.QualifiedMemberExpression qualifiedMemberArgument = (Tree.QualifiedMemberExpression)argument;
                if ("iterable".equals(qualifiedMemberArgument.getIdentifier().getText())
                    && isJavaArray(qualifiedMemberArgument.getPrimary().getTypeModel())) {
                    // just pass the array as-is
                    // we don't care at all about unboxing or casting since we can't be dealing with boxing
                    // and we generate our own cast, at least for non-primitive arrays where it may be ambiguous,
                    // we could avoid the cast for non-type-parameter and non-Object arrays, but that's more expensive
                    // to check for
                    JCExpression primary = transformExpression(qualifiedMemberArgument.getPrimary());
                    type = makeJavaType(typeFact().getSequenceType(iteratedType).getType());
                    if(isJavaObjectArray(qualifiedMemberArgument.getPrimary().getTypeModel())){
                        expr = make().TypeCast(makeJavaType(qualifiedMemberArgument.getPrimary().getTypeModel()), primary);
                    }else{
                        expr = primary;
                    }
                    return new ExpressionAndType(expr, type);
                }
            }
        }
        // invoking f(a, *b), where declared f(A a, B* b)
        // we can have several remaining arguments and the last one is spread
        List<JCExpression> x = List.<JCExpression>nil();
        for (int ii = argIndex ; ii < numArguments; ii++) {
            JCExpression argExpr = invocation.getTransformedArgumentExpression(ii);
            // the last parameter is spread and must be put first
            if(ii < numArguments - 1){
                x = x.append(argExpr);
            }else{
                // convert to a Sequential if required
                Type argType = invocation.getArgumentType(ii);
                if (typeFact().isJavaArrayType(argType)) {
                    String methodName;
                    if (typeFact().getJavaIntArrayDeclaration().equals(argType.getDeclaration())) {
                        methodName = "org.eclipse.ceylon.compiler.java.language.IntArray.getIterable";
                    } else if (typeFact().getJavaShortArrayDeclaration().equals(argType.getDeclaration())) {
                        methodName = "org.eclipse.ceylon.compiler.java.language.ShortArray.getIterable";
                    } else if (typeFact().getJavaLongArrayDeclaration().equals(argType.getDeclaration())) {
                        methodName = "org.eclipse.ceylon.compiler.java.language.LongArray.getIterable";
                    } else if (typeFact().getJavaByteArrayDeclaration().equals(argType.getDeclaration())) {
                        methodName = "org.eclipse.ceylon.compiler.java.language.ByteArray.getIterable";
                    } else if (typeFact().getJavaBooleanArrayDeclaration().equals(argType.getDeclaration())) {
                        methodName = "org.eclipse.ceylon.compiler.java.language.BooleanArray.getIterable";
                    } else if (typeFact().getJavaCharArrayDeclaration().equals(argType.getDeclaration())) {
                        methodName = "org.eclipse.ceylon.compiler.java.language.CharArray.getIterable";
                    } else if (typeFact().getJavaFloatArrayDeclaration().equals(argType.getDeclaration())) {
                        methodName = "org.eclipse.ceylon.compiler.java.language.FloatArray.getIterable";
                    } else if (typeFact().getJavaDoubleArrayDeclaration().equals(argType.getDeclaration())) {
                        methodName = "org.eclipse.ceylon.compiler.java.language.DoubleArray.getIterable";
                    } else {
                        methodName = "org.eclipse.ceylon.compiler.java.language.ObjectArray.getIterable";
                    }
                    argExpr = make().Apply(null, naming.makeQuotedQualIdentFromString(methodName), List.<JCExpression>of(argExpr));
                    argExpr = iterableToSequential(argExpr);
                }else if(typeFact().isSequentialType(argType)){
                    // we're good
                }else if(typeFact().isIterableType(argType)){
                    argExpr = iterableToSequential(argExpr);
                } else if (typeFact().isJavaIterableType(argType)) {
                    argExpr = utilInvocation().toIterable(
                            makeJavaType(iteratedType, JT_TYPE_ARGUMENT), 
                            makeReifiedTypeArgument(iteratedType), argExpr);
                    argExpr = iterableToSequential(argExpr);
                }

                x = x.prepend(argExpr);
            }
        }
        if(invocation.isJavaVariadicMethod()){
            // collect all the initial arguments and wrap into a Java array
            // first arg is the spread part
            JCExpression last = x.head;
            // remove it from x
            x = x.tail;
            
            Type lastType = invocation.getArgumentType(numArguments-1);

            // must translate it into a Util call
            expr = sequenceToJavaArray(invocation, last, parameterType, boxingStrategy, lastType, x);
        }else{
            JCExpression typeExpr = makeJavaType(iteratedType, JT_TYPE_ARGUMENT);
            JCExpression sequentialExpr = utilInvocation().sequentialInstance(typeExpr, makeReifiedTypeArgument(iteratedType), x.head, x.tail);
            if (invocation.isParameterVariadicPlus(argIndex)) {
                expr = utilInvocation().castSequentialToSequence(sequentialExpr, iteratedType);
            } else {
                expr = sequentialExpr;
            }
        }
        type = makeJavaType(typeFact().getSequenceType(iteratedType).getType());
        exprAndType = new ExpressionAndType(expr, type);
        return exprAndType;
    }

    private List<ExpressionAndType> transformSpreadTupleArgument(
            SimpleInvocation invocation, CallBuilder callBuilder,
            List<ExpressionAndType> result, final int argIndex) {
        BoxingStrategy boxingStrategy;
        // Spread tuple Argument
        // invoking f(*args), where declared f(A a, B a) (last param not sequenced)
        final Tree.Expression tupleArgument = invocation.getArgumentExpression(argIndex);
        final int minimumTupleArguments = typeFact().getTupleMinimumLength(tupleArgument.getTypeModel());
        final boolean tupleUnbounded = typeFact().isTupleLengthUnbounded(tupleArgument.getTypeModel());
        final Type callableType = invocation.getPrimary().getTypeModel().getFullType();
        
        // Only evaluate the tuple expr once
        SyntheticName tupleAlias = naming.alias("tuple");
        JCExpression tupleType;
        JCExpression tupleExpr = transformExpression(tupleArgument, BoxingStrategy.BOXED, null);
        tupleType = makeJavaType(typeFact().getSequentialDeclaration().getType(), JT_RAW);
        if (typeFact().isIterableType(tupleArgument.getTypeModel())) {
            tupleExpr = make().TypeCast(makeJavaType(typeFact().getSequentialDeclaration().getType(), JT_RAW), tupleExpr);
        } else if (typeFact().isJavaIterableType(tupleArgument.getTypeModel())) {
            // need to convert j.l.Iterable to a c.l.Iterable
            Type iteratedType = typeFact().getJavaIteratedType(tupleArgument.getTypeModel());
            tupleExpr = utilInvocation().toIterable(
                    makeJavaType(iteratedType, JT_TYPE_ARGUMENT), 
                    makeReifiedTypeArgument(iteratedType), tupleExpr);
            tupleExpr = make().Apply(null, makeSelect(tupleExpr, "sequence"), List.<JCExpression>nil());
        } else if (typeFact().isJavaArrayType(tupleArgument.getTypeModel())) {
            Type iteratedType = typeFact().getJavaArrayElementType(tupleArgument.getTypeModel());
            if (typeFact().isJavaObjectArrayType(tupleArgument.getTypeModel())) {
                tupleExpr = utilInvocation().toIterable(
                        makeJavaType(iteratedType, JT_TYPE_ARGUMENT), 
                        makeReifiedTypeArgument(iteratedType), tupleExpr);
            } else {//primitive
                tupleExpr = utilInvocation().toIterable(tupleExpr);
            }
            tupleExpr = make().Apply(null, makeSelect(tupleExpr, "sequence"), List.<JCExpression>nil());
        } else {
            throw BugException.unhandledTypeCase(tupleArgument.getTypeModel());
        }
        
        callBuilder.appendStatement(makeVar(tupleAlias, tupleType, tupleExpr));
        
        if (callBuilder.getArgumentHandling() == 0) {
            // XXX Hack: Only do this if we're not already doing 
            // something funky with arguments e.g. SpreadOp
            callBuilder.argumentHandling(CallBuilder.CB_LET, naming.alias("spreadarg"));
        }
        callBuilder.voidMethod(invocation.getReturnType() == null 
                || Decl.isUnboxedVoid(invocation.getPrimaryDeclaration())
                || isWithinSuperInvocation()); 
        
        /* Cases:
            *[] -> () => nothing
            *[] -> (Integer=) => nothing
            *[] -> (Integer*) => nothing
            *[Integer] -> (Integer) => extract
            *[Integer] -> (Integer=) => extract
            *[Integer] -> (Integer*) => pass the tuple as-is
            *[Integer*] -> (Integer*) => pass the tuple as-is
            *[Integer+] -> (Integer*) => pass the tuple as-is
            *[Integer] -> (Integer, Integer*) => extract and drop the tuple
            *[Integer,Integer] -> (Integer, Integer) => extract
            *[Integer,Integer] -> (Integer=, Integer=) => extract
            *[Integer,Integer] -> (Integer, Integer*) => extract and pass the tuple rest
            *[Integer,Integer*] -> (Integer, Integer*) => extract and pass the tuple rest
            *[Integer,Integer+] -> (Integer, Integer*) => extract and pass the tuple rest
        */
        
        int spreadArgIndex = argIndex;
        final int maxParameters = getNumParametersOfCallable(callableType);
        boolean variadic = maxParameters > 0 && invocation.isParameterSequenced(maxParameters-1);
        // we extract from the tuple not more than we have tuple members, but even less than that if we don't
        // have enough parameters to put them in
        final int argumentsToExtract = Math.min(argIndex + minimumTupleArguments, variadic ? maxParameters - 1 : maxParameters); 
        for (; spreadArgIndex < argumentsToExtract; spreadArgIndex++) {
            boxingStrategy = invocation.getParameterBoxingStrategy(spreadArgIndex);
            Type paramType = getParameterTypeOfCallable(callableType, spreadArgIndex);
            JCExpression tupleIndex = boxType(make().Literal((long)spreadArgIndex-argIndex), 
                    typeFact().getIntegerType());
            JCExpression tupleElement = make().Apply(null, 
                    naming.makeQualIdent(tupleAlias.makeIdent(), "get"),
                    List.<JCExpression>of(tupleIndex));
            
            tupleElement = applyErasureAndBoxing(tupleElement, 
                    typeFact().getAnythingType(), 
                    true, boxingStrategy, paramType);
            JCExpression argType = makeJavaType(paramType, boxingStrategy == BoxingStrategy.BOXED ? JT_NO_PRIMITIVES : 0);
            result = result.append(new ExpressionAndType(tupleElement, argType));
        }
        // if we're variadic AND
        // - the tuple is unbounded (which means we must have an unknown number of elements left to pass)
        // - OR the tuple is bounded but we did not pass them all
        if (variadic 
                && (tupleUnbounded || argumentsToExtract < (minimumTupleArguments + argIndex))) {
            boxingStrategy = invocation.getParameterBoxingStrategy(spreadArgIndex);
            Type paramType = getParameterTypeOfCallable(callableType, spreadArgIndex);
            JCExpression tupleElement = tupleAlias.makeIdent();
            // argIndex = 1, tuple = [Integer], params = [Integer, Integer*], spreadArgIndex = 1 => no span
            // argIndex = 0, tuple = [Integer+], params = [Integer, Integer*], spreadArgIndex = 1 => spanFrom(1)
            if(spreadArgIndex - argIndex > 0){
                JCExpression tupleIndex = boxType(make().Literal((long)spreadArgIndex-argIndex), 
                        typeFact().getIntegerType());
                tupleElement = make().Apply(null, naming.makeQualIdent(tupleElement, "spanFrom"),
                        List.<JCExpression>of(tupleIndex));
            }
            tupleElement = applyErasureAndBoxing(tupleElement, 
                    typeFact().getAnythingDeclaration().getType(), 
                    true, boxingStrategy, paramType);
            JCExpression argType = makeJavaType(paramType, boxingStrategy == BoxingStrategy.BOXED ? JT_NO_PRIMITIVES : 0);
            
            JCExpression expr;
            if(invocation.isJavaVariadicMethod()){
                // no need to handle leading arguments since that is handled by transformSpreadArgument
                // if ever we have leading arguments we never end up in this method
                expr = sequenceToJavaArray(invocation, tupleElement, paramType, boxingStrategy, paramType, List.<JCExpression>nil());
            }else{
                expr = tupleElement;
            }
            result = result.append(new ExpressionAndType(expr, argType));
        } else if (variadic
                && invocation.isIndirect()
                && argumentsToExtract >= minimumTupleArguments
                && !tupleUnbounded) {
            result = result.append(new ExpressionAndType(makeEmptyAsSequential(true), makeJavaType(typeFact().getSequenceType(typeFact().getAnythingDeclaration().getType()), JT_RAW)));
        } else if (!variadic 
                && tupleUnbounded
                && !invocation.isIndirect()){
            result = result.append(new ExpressionAndType(tupleAlias.makeIdent(), tupleType));
        }
        return result;
    }

    private ExpressionAndType transformArgument(SimpleInvocation invocation,
            int argIndex, BoxingStrategy boxingStrategy) {
        ExpressionAndType exprAndType;
        JCExpression expr = invocation.getTransformedArgumentExpression(argIndex);
        Type paramType = invocation.getParameterType(argIndex);
        JCExpression type = makeJavaType(paramType, boxingStrategy == BoxingStrategy.BOXED ? JT_NO_PRIMITIVES : 0);
        Class ctedClass = ModelUtil.getConstructedClass(invocation.getPrimaryDeclaration());
        if (argIndex == 0
                && typeFact().isOptionalType(paramType)
                && invocation.getArgumentType(argIndex).isSubtypeOf(typeFact().getNullType())
                && ctedClass != null && (ctedClass.hasConstructors()|| ctedClass.isSerializable())) {
            // we've invoking the default constructor, whose first parameter has optional type
            // with a null argument: That will be ambiguous wrt any named constructors
            // with otherwise identical signitures, so we need a typecast to
            // disambiguate
            expr = make().TypeCast(makeJavaType(paramType, boxingStrategy == BoxingStrategy.BOXED ? JT_NO_PRIMITIVES : 0), expr);
        }
        exprAndType = new ExpressionAndType(expr, type);
        return exprAndType;
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
        for(Parameter parameter : invocation.getMethod().getFirstParameterList().getParameters()) {
            Type exprType = expressionGen().getTypeForParameter(parameter, null, TP_TO_BOUND);
            Parameter declaredParameter = invocation.getMethod().getFirstParameterList().getParameters().get(argIndex);
            
            JCExpression arg = naming.makeName(parameter.getModel(), Naming.NA_IDENT);
            
            arg = expressionGen().applyErasureAndBoxing(
                    arg, 
                    exprType, 
                    !parameter.getModel().getUnboxed(), 
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
                        (Tree.StaticMemberOrTypeExpression)invocation.getPrimary());
            }
            if (invocation instanceof CallableSpecifierInvocation) {
                return transformCallableSpecifierInvocation(callBuilder, (CallableSpecifierInvocation)invocation);
            } else {
                at(invocation.getNode());
                Tree.Term primary = TreeUtil.unwrapExpressionUntilTerm(invocation.getPrimary());
                JCExpression result = transformTermForInvocation(primary, new InvocationTermTransformer(invocation, callBuilder));
                return result;
                
            }
        } finally {
            withinInvocation(prevFnCall);
        }
    }

    protected JCExpression transformPositionalInvocationOrInstantiation(Invocation invocation, CallBuilder callBuilder, TransformedInvocationPrimary transformedPrimary) {
        JCExpression resultExpr;
        if (invocation.isMemberRefInvocation()) {
            resultExpr = transformInvocation(invocation, callBuilder, transformedPrimary);
        } else if (invocation.getPrimary() instanceof Tree.BaseTypeExpression) {
            resultExpr = transformBaseInstantiation(invocation, callBuilder, transformedPrimary);
        } else if (invocation.getPrimary() instanceof Tree.QualifiedTypeExpression) {
            resultExpr = transformQualifiedInstantiation(invocation, callBuilder, transformedPrimary);
        } else {   
            resultExpr = transformInvocation(invocation, callBuilder, transformedPrimary);
        }
        
        if(invocation.handleBoxing)
            resultExpr = applyErasureAndBoxing(resultExpr, invocation.getReturnType(), 
                    invocation.erased, !invocation.unboxed, invocation.boxingStrategy, invocation.getReturnType(), 0);
        return resultExpr;
    }

    private JCExpression transformInvocation(Invocation invocation, CallBuilder callBuilder,
            TransformedInvocationPrimary transformedPrimary) {
        invocation.location(callBuilder);
        boolean needsCast = false;
        if (Decl.isConstructorPrimary(invocation.getPrimary())) {
            Tree.StaticMemberOrTypeExpression qte = (Tree.StaticMemberOrTypeExpression)invocation.getPrimary();
            // instantiator
            Constructor ctor = ModelUtil.getConstructor(qte.getDeclaration());
            if (Strategy.generateInstantiator(ctor)) {
                needsCast = Strategy.isInstantiatorUntyped(ctor);
                if (qte instanceof Tree.QualifiedMemberExpression
                        && ((Tree.QualifiedMemberExpression)qte).getPrimary() instanceof Tree.QualifiedTypeExpression
                        && isCeylonCallable(getReturnTypeOfCallable(invocation.getPrimary().getTypeModel()))) {
                    callBuilder.invoke(naming.makeQualIdent(transformedPrimary.expr, "$call$"));
                } else {
                    callBuilder.typeArguments(List.<JCExpression>nil());
                    java.util.List<Type> typeModels = qte.getTypeArguments().getTypeModels();
                    if (typeModels!=null) {
                        for (Type tm : typeModels) {
                            callBuilder.typeArgument(makeJavaType(tm, AbstractTransformer.JT_TYPE_ARGUMENT));
                        }
                    }
                    callBuilder.invoke(naming.makeInstantiatorMethodName(transformedPrimary.expr, ModelUtil.getConstructedClass(ctor)));
                }
            } else if (typeFact().isJavaArrayType(ModelUtil.getConstructedClass(ctor).getType())) {
                callBuilder.arrayWith(
                        invocation.getReturnType().getQualifyingType(),
                        makeJavaType(invocation.getReturnType(), JT_CLASS_NEW));
            } else {
                if (ModelUtil.getConstructedClass(invocation.getPrimaryDeclaration()).isMember()
                        && invocation.getPrimary() instanceof Tree.QualifiedMemberOrTypeExpression
                        && !(((Tree.QualifiedMemberOrTypeExpression)invocation.getPrimary()).getPrimary() instanceof Tree.BaseTypeExpression)) {
                    callBuilder.instantiate(new ExpressionAndType(transformedPrimary.expr, null),
                            makeJavaType(invocation.getReturnType(), JT_CLASS_NEW | (transformedPrimary.expr ==  null ? 0 : JT_NON_QUALIFIED)));
                } else {
                    callBuilder.instantiate(
                            makeJavaType(invocation.getReturnType(), JT_CLASS_NEW)/*transformedPrimary.expr*/);
                }
            }
        } else
        if(invocation.getQmePrimary() != null 
                && isJavaArray(invocation.getQmePrimary().getTypeModel())
                && transformedPrimary.selector != null
                && (transformedPrimary.selector.equals("get")
                    || transformedPrimary.selector.equals("set"))){
            if(transformedPrimary.selector.equals("get"))
                callBuilder.arrayRead(transformedPrimary.expr);
            else if(transformedPrimary.selector.equals("set")){
                callBuilder.arrayWrite(transformedPrimary.expr);
                Type arrayType = invocation.getQmePrimary().getTypeModel().resolveAliases();
                if(isJavaObjectArray(arrayType) && invocation instanceof PositionalInvocation){
                    Type elementType = arrayType.getTypeArgumentList().get(0);
                    Type argumentType = ((PositionalInvocation)invocation).getArgumentType(1);
                    if(!argumentType.isSubtypeOf(typeFact().getOptionalType(elementType)))
                        callBuilder.javaArrayWriteNeedsCast(true);
                }
            }else
                return makeErroneous(invocation.getNode(), "compiler bug: extraneous array selector: "+transformedPrimary.selector);
        } else if (invocation.isUnknownArguments()) {
            // if we have an unknown parameter list, like Callble<Ret,Args>, need to prepend the callable
            // to the argument list, and invoke Util.apply
            // note that ATM the typechecker only allows a single argument to be passed in spread form in this
            // case so we don't need to look at parameter types
            JCExpression callableTypeExpr = makeJavaType(invocation.getPrimary().getTypeModel());
            ExpressionAndType callableArg = new ExpressionAndType(transformedPrimary.expr, callableTypeExpr);
            Type returnType = invocation.getReturnType();
            JCExpression returnTypeExpr = makeJavaType(returnType, JT_NO_PRIMITIVES);
            callBuilder.prependArgumentAndType(callableArg);
            callBuilder.typeArgument(returnTypeExpr);
            callBuilder.invoke(make().Select(make().QualIdent(syms().ceylonUtilType.tsym), 
                                             names().fromString("apply")));
        } else if (invocation.isOnValueType()) {
            JCExpression primTypeExpr = makeJavaType(invocation.getQmePrimary().getTypeModel(), JT_NO_PRIMITIVES | JT_VALUE_TYPE);
            callBuilder.invoke(naming.makeQuotedQualIdent(primTypeExpr, transformedPrimary.selector));

        } else {
            callBuilder.invoke(naming.makeQuotedQualIdent(transformedPrimary.expr, transformedPrimary.selector));
        }
        JCExpression result = callBuilder.build();
        if (needsCast) {
            result = make().TypeCast(makeJavaType(invocation.getReturnType()), result);
        }
        return result;
    }

    private JCExpression transformQualifiedInstantiation(Invocation invocation, CallBuilder callBuilder,
            TransformedInvocationPrimary transformedPrimary) {
        
        Tree.QualifiedTypeExpression qte = (Tree.QualifiedTypeExpression)invocation.getPrimary();
        Declaration declaration = qte.getDeclaration();
        invocation.location(callBuilder);
        if (Decl.isJavaStaticOrInterfacePrimary(invocation.getPrimary())) {
            callBuilder.instantiate(transformedPrimary.expr);
        } else if (!Strategy.generateInstantiator(declaration)) {
            if (Decl.isConstructorPrimary(invocation.getPrimary())) {
                if (ModelUtil.getConstructedClass(invocation.getPrimaryDeclaration()).isMember()
                        /*&& invocation.getPrimary() instanceof Tree.QualifiedTypeExpression
                        && !(((Tree.QualifiedTypeExpression)invocation.getPrimary()).getPrimary() instanceof Tree.BaseTypeExpression)*/) {
                    callBuilder.instantiate(new ExpressionAndType(transformedPrimary.expr, null),
                            makeJavaType(invocation.getReturnType(), JT_CLASS_NEW | (transformedPrimary.expr ==  null ? 0 : JT_NON_QUALIFIED)));
                } else {
                    callBuilder.instantiate(
                            makeJavaType(invocation.getReturnType(), JT_CLASS_NEW)/*transformedPrimary.expr*/);
                }
            } else { 
                JCExpression qualifier;
                JCExpression qualifierType;
                if (declaration.getContainer() instanceof Interface) {
                    // When doing qualified invocation through an interface we need
                    // to get the companion.
                    Interface qualifyingInterface = (Interface)declaration.getContainer();
                    qualifier = transformedPrimary.expr;
                    qualifierType = makeJavaType(qualifyingInterface.getType(), JT_COMPANION);
                } else {
                    qualifier = transformedPrimary.expr;
                    if (declaration.getContainer() instanceof TypeDeclaration) {
                        qualifierType = makeJavaType(((TypeDeclaration)declaration.getContainer()).getType());
                    } else {
                        qualifierType = null;
                    }
                }
                Type classType = (Type)qte.getTarget();
                JCExpression type;
                // special case for package-qualified things that are not really qualified
                if(qualifier == null){
                    type = makeJavaType(classType, AbstractTransformer.JT_CLASS_NEW);
                }else{
                    // Note: here we're not fully qualifying the class name because the JLS says that if "new" is qualified the class name
                    // is qualified relative to it
                    type = makeJavaType(classType, AbstractTransformer.JT_CLASS_NEW | AbstractTransformer.JT_NON_QUALIFIED);
                }
                callBuilder.instantiate(new ExpressionAndType(qualifier, qualifierType), type);
            }
        } else {
            // instantiator
            callBuilder.typeArguments(List.<JCExpression>nil());
            java.util.List<Type> typeModels = qte.getTypeArguments().getTypeModels();
            if (typeModels!=null) {
                for (Type tm : typeModels) {
                    callBuilder.typeArgument(makeJavaType(tm, AbstractTransformer.JT_TYPE_ARGUMENT));
                }
            }
            callBuilder.invoke(naming.makeInstantiatorMethodName(transformedPrimary.expr, ModelUtil.getConstructedClass(declaration)));
        }
        JCExpression result = callBuilder.build();
        if (Strategy.isInstantiatorUntyped(declaration)) {
            result = make().TypeCast(makeJavaType(invocation.getReturnType()), result);
        }
        return result;
    }

    private JCExpression transformBaseInstantiation(Invocation invocation, CallBuilder callBuilder,
            TransformedInvocationPrimary transformedPrimary) {
        JCExpression resultExpr;
        Tree.BaseTypeExpression type = (Tree.BaseTypeExpression)invocation.getPrimary();
        Declaration declaration = type.getDeclaration();
        invocation.location(callBuilder);
        if (Strategy.generateInstantiator(declaration)) {
            resultExpr = callBuilder
                    .typeArguments(List.<JCExpression>nil())
                    .invoke(naming.makeInstantiatorMethodName(transformedPrimary.expr, (Class)declaration))
                    .build();
            if (Strategy.isInstantiatorUntyped(declaration)) {
                // $new method declared to return Object, so needs typecast
                resultExpr = make().TypeCast(makeJavaType(
                        ((TypeDeclaration)declaration).getType()), resultExpr);
            }
        } else {
            Type classType = (Type)type.getTarget();
            if(isJavaArray(classType)){
                JCExpression typeExpr = makeJavaType(classType, AbstractTransformer.JT_CLASS_NEW | AbstractTransformer.JT_RAW);
                callBuilder.javaArrayInstance(typeExpr);
                if(isJavaObjectArray(classType)){
                    Type elementType = classType.getTypeArgumentList().get(0);
                    MultidimensionalArray multiArray = getMultiDimensionalArrayInfo(elementType);
                    if(multiArray != null)
                        elementType = multiArray.type;
                    // if it is an array of Foo<X> we need a raw instanciation and cast
                    // array of Foo is fine, array of Nothing too
                    if(elementType.getDeclaration() instanceof ClassOrInterface
                            || elementType.isNothing()){
                        if(!elementType.getTypeArgumentList().isEmpty())
                            callBuilder.javaArrayInstanceNeedsCast(makeJavaType(classType, AbstractTransformer.JT_NO_PRIMITIVES));
                    }else{
                        // if it's an array of union, intersection or type param we need a runtime allocation
                        callBuilder.javaArrayInstanceIsGeneric(makeReifiedTypeArgument(elementType), 
                                multiArray != null ? multiArray.dimension + 1 : 1);
                    }
                }
            }else{
                if (Decl.isConstructor(classType.getDeclaration())) {
                    classType = classType.getExtendedType();
                }
                JCExpression typeExpr = makeJavaType(classType, AbstractTransformer.JT_CLASS_NEW);
                callBuilder.instantiate(typeExpr);
            }
            resultExpr = callBuilder.build();
        }
        return resultExpr;
    }
    
    private JCExpression transformCallableSpecifierInvocation(CallBuilder callBuilder, CallableSpecifierInvocation invocation) {
        at(invocation.getNode());
        JCExpression result = callBuilder
            .invoke(naming.makeQuotedQualIdent(invocation.getCallable(), Naming.getCallableMethodName(invocation.getMethod())))
            .argumentsAndTypes(transformArguments(invocation, null, callBuilder))
            .build();
        if(invocation.handleBoxing)
            result = applyErasureAndBoxing(result, invocation.getReturnType(), 
                    invocation.erased, !invocation.unboxed, invocation.boxingStrategy, invocation.getReturnType(), 0);
        return result;
    }
    
    private final void transformTypeArguments(
            CallBuilder callBuilder,
            Tree.StaticMemberOrTypeExpression mte) {
        java.util.List<TypeParameter> tps = null;
        Declaration declaration = mte.getDeclaration();
        
        if (!mte.getTypeModel().isTypeConstructor()) {
            tps = Strategy.getEffectiveTypeParameters(declaration);
        } else {
            for (TypeParameter tp : Strategy.getEffectiveTypeParameters(declaration)) {
                callBuilder.typeArgument(makeJavaType(tp.getType(), JT_TYPE_ARGUMENT));
            }
            return;
        }
        if (tps != null) {
            for (TypeParameter tp : tps) {
                Reference target = mte.getTarget();
                Type ta = null;
                while (ta==null && target!=null) {
                    ta = target.getTypeArguments().get(tp);
                    target = target.getQualifyingType();
                }
                java.util.List<Type> bounds = null;
                boolean needsCastForBounds = false;
                if(!tp.getSatisfiedTypes().isEmpty()){
                    bounds = new ArrayList<Type>(tp.getSatisfiedTypes().size());
                    for(Type bound : tp.getSatisfiedTypes()){
                        // substitute the right type arguments
                        bound = substituteTypeArgumentsForTypeParameterBound(mte.getTarget(), bound);
                        bounds.add(bound);
                        needsCastForBounds |= needsCast(ta, bound, false, false, false);
                    }
                }
                boolean hasMultipleBounds;
                Type firstBound;
                if(bounds != null){
                    hasMultipleBounds = bounds.size() > 1;
                    firstBound = bounds.isEmpty() ? null : bounds.get(0);
                }else{
                    hasMultipleBounds = false;
                    firstBound = null;
                }
                if (willEraseToObject(ta) || needsCastForBounds) {
                    boolean boundsSelfDependent = isBoundsSelfDependant(tp);
                    if (hasDependentTypeParameters(tps, tp)
                            // if we must use the bounds and we have more than one, we cannot use one to satisfy them all
                            // and we cannot represent the intersection type in Java so give up
                            || hasMultipleBounds
                            // if we are going to use the first bound and it is self-dependent, we will make it raw
                            || boundsSelfDependent
                            || (firstBound != null && willEraseToObject(firstBound))) {
                        // we just can't satisfy the bounds if there are more than one, just pray,
                        // BUT REMEMBER THERE IS NO SUCH THING AS A RAW METHOD CALL IN JAVA
                        // so at some point we'll have to introduce an intersection type AST node to satisfy multiple bounds
                        if(hasMultipleBounds){
                            callBuilder.typeArguments(List.<JCExpression>nil());
                            return;
                        }
                        // if we have a bound
                        if(firstBound != null){
                            // if it's self-dependent we cannot satisfy it without a raw type
                            if(boundsSelfDependent)
                                callBuilder.typeArgument(makeJavaType(firstBound, JT_TYPE_ARGUMENT|JT_RAW));
                            else
                                callBuilder.typeArgument(makeJavaType(firstBound, JT_TYPE_ARGUMENT));
                        }else{
                            // no bound, let's go with Object then
                            callBuilder.typeArgument(makeJavaType(typeFact().getObjectType(), JT_TYPE_ARGUMENT));
                        }
                    }else if (firstBound == null) {
                        callBuilder.typeArgument(makeJavaType(ta, JT_TYPE_ARGUMENT));
                    } else {
                        callBuilder.typeArgument(makeJavaType(firstBound, JT_TYPE_ARGUMENT));
                    }
                } else {
                    callBuilder.typeArgument(makeJavaType(ta, JT_TYPE_ARGUMENT));
                }
            }
        }
    }

    boolean erasesTypeArguments(Reference producedReference) {
        for (TypeParameter tp : producedReference.getDeclaration().getTypeParameters()) {
            Type ta = producedReference.getTypeArguments().get(tp);
            java.util.List<Type> bounds = null;
            boolean needsCastForBounds = false;
            if(!tp.getSatisfiedTypes().isEmpty()){
                bounds = new ArrayList<Type>(tp.getSatisfiedTypes().size());
                for(Type bound : tp.getSatisfiedTypes()){
                    // substitute the right type arguments
                    bound = substituteTypeArgumentsForTypeParameterBound(producedReference, bound);
                    bounds.add(bound);
                    needsCastForBounds |= needsCast(ta, bound, false, false, false);
                }
            }
            if (willEraseToObject(ta) || needsCastForBounds) {
                return true;
            }
        }
        return false;
    }

    protected JCExpression transformNamedArgumentInvocationOrInstantiation(NamedArgumentInvocation invocation, 
            CallBuilder callBuilder,
            TransformedInvocationPrimary transformedPrimary) {
        JCExpression resultExpr = transformPositionalInvocationOrInstantiation(invocation, callBuilder, transformedPrimary);
        // apply the default parameters
        if (invocation.getVars() != null && !invocation.getVars().isEmpty()) {
            if ((invocation.getReturnType() == null 
                    || Decl.isUnboxedVoid(invocation.getPrimaryDeclaration()))
                    && !Decl.isMpl((Functional) invocation.getPrimaryDeclaration())) {
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
        HasErrorException error = errors().getFirstExpressionErrorAndMarkBrokenness(extendedType);
        if (error != null) {
            classBuilder.getInitBuilder().delegateCall(this.makeThrowUnresolvedCompilationError(error));
            return;
        }
        if (extendedType.getInvocationExpression() != null 
                && extendedType.getInvocationExpression().getPositionalArgumentList() != null) {
            Declaration primaryDeclaration = ((Tree.MemberOrTypeExpression)extendedType.getInvocationExpression().getPrimary()).getDeclaration();
            java.util.List<ParameterList> paramLists = ((Functional)primaryDeclaration).getParameterLists();
            if(paramLists.isEmpty()){
                classBuilder.getInitBuilder().delegateCall(at(extendedType).Exec(makeErroneous(extendedType, "compiler bug: missing parameter list in extends clause: " + primaryDeclaration.getName() + " must be invoked")));
            } else {
                boolean prevFnCall = withinInvocation(true);
                try {
                    JCStatement superExpr = transformConstructorDelegation(extendedType, 
                            new CtorDelegation(null, primaryDeclaration), 
                            extendedType.getInvocationExpression(), classBuilder, false);
                    classBuilder.getInitBuilder().delegateCall(superExpr);
                } finally {
                    withinInvocation(prevFnCall);
                }
            }
        }
    }

    /**
     * Transform a delegated constructor call ({@code extends XXX()})
     * which may be either a superclass initializer/constructor or a 
     * same-class constructor. 
     * @param extendedType
     * @param delegation The kind of delegation 
     * @param invocation
     * @param classBuilder
     * @return
     */
    JCStatement transformConstructorDelegation(Node extendedType,
            CtorDelegation delegation,
            Tree.InvocationExpression invocation, ClassDefinitionBuilder classBuilder, boolean forDelegationConstructor) {
        if (delegation != null && delegation.isError()) {
            return delegation.makeThrow(this);
        }
        Declaration primaryDeclaration = ((Tree.MemberOrTypeExpression)invocation.getPrimary()).getDeclaration();
        java.util.List<ParameterList> paramLists = ((Functional)primaryDeclaration).getParameterLists();
        if(paramLists.isEmpty()){
            classBuilder.getInitBuilder().delegateCall(at(extendedType).Exec(makeErroneous(extendedType, "compiler bug: super class " + primaryDeclaration.getName() + " is missing parameter list")));
            return null;
        }
        SuperInvocation builder = new SuperInvocation(this,
                classBuilder.getForDefinition(),
                delegation, invocation,
                paramLists.get(0), forDelegationConstructor);
        
        CallBuilder callBuilder = CallBuilder.instance(this);
        boolean prevFnCall = withinInvocation(true);
        try {
            if (invocation.getPrimary() instanceof Tree.StaticMemberOrTypeExpression){
                transformTypeArguments(callBuilder, 
                        (Tree.StaticMemberOrTypeExpression)invocation.getPrimary());
            }
            at(builder.getNode());
            JCExpression expr = null;
            Scope outerDeclaration;
            if (Decl.isConstructor(primaryDeclaration)) {
                outerDeclaration= builder.getPrimaryDeclaration().getContainer().getContainer();
            } else {
                outerDeclaration= builder.getPrimaryDeclaration().getContainer();
            }
            
            if ((Strategy.generateInstantiator(builder.getPrimaryDeclaration())
                    || builder.getPrimaryDeclaration() instanceof Class)
                    && outerDeclaration instanceof Interface
                    && !((Interface)outerDeclaration).isJava()) {
                // If the subclass is inner to an interface then it will be 
                // generated inner to the companion and we need to qualify the 
                // super(), *unless* the subclass is nested within the same 
                // interface as it's superclass.
                Scope outer = builder.getSub().getContainer();
                while (!(outer instanceof Package)) {
                    if (outer == outerDeclaration) {
                        expr = naming.makeSuper();
                        break;
                    }
                    outer = outer.getContainer();
                }
                if (expr == null) {
                    if (delegation.isSelfDelegation()) {
                        throw new BugException();
                    }
                    Interface iface = (Interface)outerDeclaration;
                    JCExpression superQual;
                    if (ModelUtil.getClassOrInterfaceContainer(classBuilder.getForDefinition(), false) instanceof Interface) {
                        superQual = naming.makeCompanionAccessorCall(naming.makeQuotedThis(), iface);
                    } else {
                        superQual = naming.makeCompanionFieldName(iface);
                    }
                    expr = naming.makeQualifiedSuper(superQual);
                }
            } else {
                expr = delegation.isSelfDelegation() ? naming.makeThis() : naming.makeSuper();
            }
            final List<JCExpression> superArguments = transformSuperInvocationArguments(
                    classBuilder, builder, callBuilder);
            JCExpression superExpr = callBuilder.invoke(expr)
                .arguments(superArguments)
                .build();
            return at(extendedType).Exec(superExpr);
            //classBuilder.getInitBuilder().superCall(at(extendedType).Exec(superExpr));
        } finally {
            withinInvocation(prevFnCall);
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
            ClassDefinitionBuilder classBuilder, SuperInvocation invocation, CallBuilder callBuilder) {
        // We could create a TransformedPrimary(expr, "super") here if needed
        List<ExpressionAndType> superArgumentsAndTypes = transformArguments(invocation, null, callBuilder);
        final List<JCExpression> superArguments = ExpressionAndType.toExpressionList(superArgumentsAndTypes);
        return superArguments;
    }
    
    public JCExpression transform(Tree.InvocationExpression ce) {
        JCExpression ret = checkForInvocationExpressionOptimisation(ce);
        if(ret != null)
            return ret;
        
        Tree.Term primary = TreeUtil.unwrapExpressionUntilTerm(ce.getPrimary());
        Declaration primaryDeclaration = null;
        Reference producedReference = null;
        if (primary instanceof Tree.MemberOrTypeExpression) {
            Tree.MemberOrTypeExpression mte = (Tree.MemberOrTypeExpression) primary;
            producedReference = mte.getTarget();
            primaryDeclaration = mte.getDeclaration();
        }
        Invocation invocation;
        if (ce.getPositionalArgumentList() != null) {
            if ((isIndirectInvocation(ce)
                    || isWithinDefaultParameterExpression(primaryDeclaration.getContainer()))
                    && !Decl.isJavaStaticOrInterfacePrimary(ce.getPrimary())){
                // indirect invocation
                invocation = new IndirectInvocation(this, 
                        primary, primaryDeclaration,
                        ce);
            } else {
                // direct invocation
                java.util.List<Parameter> parameters = ((Functional)primaryDeclaration).getFirstParameterList().getParameters();
                invocation = new PositionalInvocation(this, 
                        primary, primaryDeclaration,producedReference,
                        ce,
                        parameters);
            }
        } else if (ce.getNamedArgumentList() != null) {
            invocation = new NamedArgumentInvocation(this, 
                    primary, 
                    primaryDeclaration,
                    producedReference,
                    ce);
        } else {
            return makeErroneous(ce, "no arguments");
        }
        return transformInvocation(invocation);
    }

    public JCExpression transformFunctional(Tree.StaticMemberOrTypeExpression expr,
            Functional functional, Type expectedType) {
        return CallableBuilder.methodReference(gen(), expr, 
                    functional.getFirstParameterList(), expectedType, expr.getTypeModel(), true);
    }

    public JCExpression transformFunctionalInterfaceBridge(Tree.StaticMemberOrTypeExpression expr,
            Value functional, Type expectedType) {
        ParameterList paramList = new ParameterList();
        Type callableType = expr.getTypeModel().getSupertype(typeFact().getCallableDeclaration());
        int i=0;
        for(Type type : typeFact().getCallableArgumentTypes(callableType)){
            Parameter param = new Parameter();
            Value paramModel = new Value();
            param.setModel(paramModel);
            param.setName("arg"+i);
            paramModel.setName("arg"+i);
            paramModel.setType(type);
            paramList.getParameters().add(param);
            i++;
        }
        return CallableBuilder.methodReference(gen(), expr, 
                    paramList, expectedType, expr.getTypeModel(), false);
    }

    public JCExpression transformFunctionalInterfaceBridge(Tree.InvocationExpression expr,
            JCExpression primaryExpr, Type expectedType) {
        ParameterList paramList = new ParameterList();
        int i=0;
        Type callableType = expr.getTypeModel().getSupertype(typeFact().getCallableDeclaration());
        for(Type type : typeFact().getCallableArgumentTypes(callableType)){
            Parameter param = new Parameter();
            Value paramModel = new Value();
            param.setModel(paramModel);
            param.setName("arg"+i);
            paramModel.setName("arg"+i);
            paramModel.setType(type);
            paramList.getParameters().add(param);
            i++;
        }
        return CallableBuilder.callableToFunctionalInterface(gen(), expr, 
                    paramList, expectedType, expr.getTypeModel(), false, primaryExpr);
    }

    public JCExpression transformCallableBridge(Tree.StaticMemberOrTypeExpression expr,
            Value functional, Type expectedType) {
        ParameterList paramList = new ParameterList();
        // expr is a SAM
        // expectedType is a Callable
        TypedReference samRef = checkForFunctionalInterface(expr.getTypeModel());
        TypedDeclaration samDecl = samRef.getDeclaration();
        if(samDecl instanceof Value){
            Parameter param = new Parameter();
            Value paramModel = new Value();
            param.setModel(paramModel);
            param.setName("arg0");
            paramModel.setName("arg0");
            paramModel.setType(samRef.getType());
            paramModel.setUnboxed(samDecl.getUnboxed());
            // FIXME: other stuff like erasure?
            paramList.getParameters().add(param);
        }else{
            int i=0;
            for(Parameter samParam : ((Function)samDecl).getFirstParameterList().getParameters()){
                TypedReference typedSamParam = samRef.getTypedParameter(samParam);
                Parameter param = new Parameter();
                Value paramModel = new Value();
                param.setModel(paramModel);
                param.setName("arg"+i);
                paramModel.setName("arg"+i);
                paramModel.setType(typedSamParam.getFullType());
                // FIXME: other stuff like erasure?
                paramModel.setUnboxed(typedSamParam.getDeclaration().getUnboxed());
                paramList.getParameters().add(param);
                i++;
            }
        }
        // FIXME: this is cheating we should be assembling it from the SAM type
        Type callableType = expectedType.getSupertype(typeFact().getCallableDeclaration());
        return CallableBuilder.methodReference(gen(), expr, 
                    paramList, expectedType, callableType, false);
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
        Tree.Primary primary = expr.getPrimary();
        if (primary instanceof Tree.BaseTypeExpression) {
            return transformMemberReference(expr, (Tree.BaseTypeExpression)primary);
        } else if (primary instanceof Tree.QualifiedTypeExpression) {
            return transformMemberReference(expr, (Tree.QualifiedTypeExpression)primary);
        }
        return transform(expr, null);
    }

    JCExpression transformMemberReference(
            Tree.QualifiedMemberOrTypeExpression expr,
            Tree.MemberOrTypeExpression primary) {
        Declaration member = expr.getDeclaration();
        Type qualifyingType = primary.getTypeModel();
        Tree.TypeArguments typeArguments = expr.getTypeArguments();
        Type expectedTypeIfCoerced = coerced ? expectedType : null;
        boolean prevSyntheticClassBody = withinSyntheticClassBody(true);
        try {
            if (member.isStatic()) {
                if (member instanceof Function) {
                    Function method = (Function)member;
                    Reference producedReference = expr.getTarget();//method.appliedReference(qualifyingType, typeArguments.getTypeModels());
                    return CallableBuilder.javaStaticMethodReference(
                            gen(), 
                            expr,
                            expr.getTypeModel(), 
                            method, 
                            producedReference,
                            expectedTypeIfCoerced).build();
                } else if (member instanceof FieldValue) {
                    return naming.makeName(
                            (TypedDeclaration)member, Naming.NA_FQ | Naming.NA_WRAPPER_UNQUOTED);
                } else if (member instanceof Value) {
                    CallBuilder callBuilder = CallBuilder.instance(this);
                    JCExpression qualExpr = naming.makeTypeDeclarationExpression(null, (TypeDeclaration)member.getContainer(), DeclNameFlag.QUALIFIED);
                    Type primType = primary.getTarget().getType();
                    if (ModelUtil.isCeylonDeclaration(member)
                            && !primType.getTypeArgumentList().isEmpty()) {
                        for (Type pt : primType.getTypeArgumentList()) {
                            callBuilder.typeArgument(makeJavaType(pt, JT_TYPE_ARGUMENT));
                            callBuilder.argument(makeReifiedTypeArgument(pt));
                        }
                        
                    }
                    callBuilder.invoke(naming.makeQualifiedName(qualExpr, (TypedDeclaration)member, Naming.NA_GETTER | Naming.NA_MEMBER));
                    
                    return callBuilder.build();
                } else if (member instanceof Class) {
                    Reference producedReference = expr.getTarget();
                    return CallableBuilder.javaStaticMethodReference(
                            gen(), 
                            expr,
                            expr.getTypeModel(), 
                            (Class)member, 
                            producedReference,
                            expectedTypeIfCoerced).build();
                }
            } 
            if (member instanceof Value) {
                if (expr.getStaticMethodReference()
                        && ModelUtil.isEnumeratedConstructor((Value)member)) {
                    CallBuilder callBuilder = CallBuilder.instance(this);
                    JCExpression qualExpr;
                    Class class1 = (Class) member.getContainer();
                    if (class1.isToplevel() || class1.isStatic()) {
                        qualExpr = naming.makeTypeDeclarationExpression(null, class1.isStatic() ? (TypeDeclaration)class1.getContainer() : class1, DeclNameFlag.QUALIFIED);
                        callBuilder.invoke(naming.makeQualifiedName(qualExpr, (TypedDeclaration)member, Naming.NA_GETTER | Naming.NA_MEMBER));
                    } else if (class1.isMember()){
                        // Stef: this is fugly but I couldn't find better. This makes sure that Outer.Inner.enumeratedConstructor
                        // creates a Callable<Outer.Inner,[Outer]> that returns the enumeratedConstructor given an outer instance
                        if (primary instanceof Tree.QualifiedMemberOrTypeExpression 
                                && (((Tree.QualifiedMemberOrTypeExpression) primary).getPrimary() instanceof Tree.BaseTypeExpression
                                || ((Tree.QualifiedMemberOrTypeExpression) primary).getPrimary() instanceof Tree.QualifiedTypeExpression))
                            return CallableBuilder.unboundValueMemberReference(
                                gen(),
                                expr,
                                expr.getTypeModel(), 
                                ((TypedDeclaration)member),
                                expectedTypeIfCoerced).build();
                        else{
                            qualExpr = primary instanceof Tree.QualifiedMemberOrTypeExpression ? transformExpression(((Tree.QualifiedMemberOrTypeExpression)primary).getPrimary()) : null;
                            callBuilder.invoke(naming.makeQualifiedName(qualExpr, (TypedDeclaration)member, Naming.NA_GETTER | Naming.NA_MEMBER));
                        }
                    } else {
                        // Local enumerated constructor values are boxed
                        qualExpr = naming.makeQualifiedName(null, (TypedDeclaration)member, Naming.NA_Q_LOCAL_INSTANCE);
                        qualExpr = gen().makeSelect(qualExpr, naming.selector((TypedDeclaration)member));
                        callBuilder.fieldRead(qualExpr);
                    }
                    
                    return callBuilder.build();
                } else {
                    return CallableBuilder.unboundValueMemberReference(
                            gen(),
                            expr,
                            expr.getTypeModel(), 
                            ((TypedDeclaration)member),
                            expectedTypeIfCoerced).build();
                }
            } else if (Decl.isConstructor(member)) {
                Reference producedReference = expr.getTarget();
                return CallableBuilder.unboundFunctionalMemberReference(
                        gen(), 
                        expr,
                        expr.getTypeModel(), 
                        ModelUtil.getConstructor(member), 
                        producedReference,
                        expectedTypeIfCoerced);
            } else if (member instanceof Function) {
                Function method = (Function)member;
                if (!method.isParameter()) {
                    Reference producedReference = method.appliedReference(qualifyingType, typeArguments.getTypeModels());
                    return CallableBuilder.unboundFunctionalMemberReference(
                            gen(), 
                            expr,
                            expr.getTypeModel(), 
                            method, 
                            producedReference,
                            expectedTypeIfCoerced);
                } else {
                    Reference producedReference = method.appliedReference(qualifyingType, typeArguments.getTypeModels());
                    return CallableBuilder.unboundFunctionalMemberReference(
                            gen(), 
                            expr,
                            expr.getTypeModel(), 
                            method, 
                            producedReference,
                            expectedTypeIfCoerced);
                }
            } else if (member instanceof Class) {
                Reference producedReference = expr.getTarget();
                return CallableBuilder.unboundFunctionalMemberReference(
                        gen(), 
                        expr,
                        expr.getTypeModel(), 
                        (Class)member, 
                        producedReference,
                        expectedTypeIfCoerced);
            } else {
                return makeErroneous(expr, "compiler bug: member reference of " + expr + " not supported yet");
            }
        } finally {
            withinSyntheticClassBody(prevSyntheticClassBody);
        }
    }
    
    private JCExpression transform(Tree.QualifiedMemberExpression expr, TermTransformer transformer) {
        Tree.MemberOperator op = expr.getMemberOperator();
        if (op instanceof Tree.SafeMemberOp) {
            return transformSafeMemberOperator(expr, transformer);
        } else if (op instanceof Tree.SpreadOp) {
            return transformSpreadOperator(expr, transformer);
        } else {
            JCExpression primaryExpr = transformQualifiedMemberPrimary(expr);
            return transformMemberExpression(expr, primaryExpr, transformer);
        }
    }

    private JCExpression transformSafeMemberOperator(Tree.QualifiedMemberOrTypeExpression expr, TermTransformer transformer) {
        Naming.SyntheticName tmpVarName = naming.alias("safe");
        JCExpression typeExpr = makeJavaType(expr.getTarget().getQualifyingType(), JT_NO_PRIMITIVES);
        JCExpression transExpr = transformMemberExpression(expr, tmpVarName.makeIdent(), transformer);
        if (isFunctionalResult(expr.getTypeModel())) {
            return transExpr;
        }
        // the marker we get for boxing on a QME with a SafeMemberOp is always unboxed
        // since it returns an optional type, but that doesn't tell us if the underlying
        // expr is or not boxed
        boolean isBoxed = expr.getDeclaration() instanceof TypeDeclaration 
                || !CodegenUtil.isUnBoxed((TypedDeclaration)expr.getDeclaration());
        transExpr = boxUnboxIfNecessary(transExpr, isBoxed, expr.getTarget().getType(), BoxingStrategy.BOXED);
        JCExpression testExpr = make().Binary(JCTree.Tag.NE, tmpVarName.makeIdent(), makeNull());
        JCExpression condExpr = make().Conditional(testExpr, transExpr, makeNull());
        JCExpression primaryExpr = transformQualifiedMemberPrimary(expr);
        return makeLetExpr(tmpVarName, null, typeExpr, primaryExpr, condExpr);
    }

    private JCExpression transformSpreadOperator(final Tree.QualifiedMemberOrTypeExpression expr, TermTransformer transformer) {
        at(expr);
        
        boolean spreadMethodReferenceOuter = !expr.equals(spreading) && !isWithinInvocation() && isCeylonCallableSubtype(expr.getTypeModel());
        boolean spreadMethodReferenceInner = expr.equals(spreading) && isWithinInvocation();
        Tree.QualifiedMemberOrTypeExpression oldSpreading = spreading;
        if (spreadMethodReferenceOuter) {
            spreading = expr;
        }
        try {
            Naming.SyntheticName varBaseName = naming.alias("spread");
            ListBuffer<JCStatement> letStmts = new ListBuffer<JCStatement>();
            final Naming.SyntheticName srcIterableName;
            if (spreadMethodReferenceInner) {
                // use the var we initialized in the outer
                srcIterableName = this.memberPrimary;
            } else {
                srcIterableName = varBaseName.suffixedBy(Suffix.$iterable$);
            }
            if (spreadMethodReferenceOuter) {
                // if we're in the outer, note then name of the var for use in the inner.
                this.memberPrimary = srcIterableName;
            }
            Naming.SyntheticName srcIteratorName = varBaseName.suffixedBy(Suffix.$iterator$);
            Type srcElementType = expr.getTarget().getQualifyingType();
            JCExpression srcIterableExpr;
            boolean isSuperOrSuperOf = false;
            Type srcIterableType;
            if (typeFact().isIterableType(expr.getPrimary().getTypeModel())) {
                srcIterableType = typeFact().getIterableType(srcElementType);
            } else if (typeFact().isJavaIterableType(expr.getPrimary().getTypeModel())) {
                srcIterableType = typeFact().getJavaIterableDeclaration().appliedType(null,  Collections.singletonList(srcElementType));
            } else if (typeFact().isJavaArrayType(expr.getPrimary().getTypeModel())) {
                srcIterableType = expr.getPrimary().getTypeModel();
                srcElementType = typeFact().getJavaArrayElementType(srcIterableType);
            } else {
                return makeErroneous(expr, "unhandled iterable type");
            }
            if (spreadMethodReferenceInner) {
                srcIterableExpr = srcIterableName.makeIdent();
            } else {
                boolean isSuper = isSuper(expr.getPrimary());
                isSuperOrSuperOf = isSuper || isSuperOf(expr.getPrimary());
                if(isSuperOrSuperOf){
                    // in this case we can't capture the iterable because it may be a mixin impl class, but it's constant
                    // so we just refer to it later
                    if(isSuper){
                        Declaration member = expr.getPrimary().getTypeModel().getDeclaration().getMember("iterator", null, false);
                        srcIterableExpr = transformSuper(expr, (TypeDeclaration) member.getContainer());
                    }else
                        srcIterableExpr = transformSuperOf(expr, expr.getPrimary(), "iterator");
                }else{
                    srcIterableExpr = transformExpression(expr.getPrimary(), BoxingStrategy.BOXED, srcIterableType);
                }
            }
            // do not capture the iterable for super invocations: see above
            if (!spreadMethodReferenceInner && !isSuperOrSuperOf) {
                JCVariableDecl srcIterable = null;
                JCExpression srcIterableTypeExpr = makeJavaType(srcIterableType, JT_NO_PRIMITIVES);
                srcIterable = makeVar(Flags.FINAL, srcIterableName, srcIterableTypeExpr, srcIterableExpr);
                letStmts.prepend(srcIterable);
            }
            
            /* public Object next() {
             *     Object result;
             *     if (!((result = iterator.next()) instanceof Finished)) {
             *         result = transformedMember(result);
             *     }
             *     return result;
             */
            
            /* Any arguments in the member of the spread would get re-evaluated on each iteration
             * so we need to shift them to the scope of the Let to ensure they're evaluated once. 
             */
            boolean aliasArguments = transformer instanceof InvocationTermTransformer
                    && ((InvocationTermTransformer)transformer).invocation.getNode() instanceof Tree.InvocationExpression
                    && ((Tree.InvocationExpression)((InvocationTermTransformer)transformer).invocation.getNode()).getPositionalArgumentList() != null;
            if (aliasArguments) {
                ((InvocationTermTransformer)transformer).callBuilder.argumentHandling(
                        CallBuilder.CB_ALIAS_ARGS, varBaseName);
            }
            
            JCNewClass iterableClass;
            boolean prevSyntheticClassBody = expressionGen().withinSyntheticClassBody(true);
            try {
                Naming.SyntheticName iteratorResultName = varBaseName.suffixedBy(Suffix.$element$);
                JCExpression transformedElement = applyErasureAndBoxing(iteratorResultName.makeIdent(), typeFact().getAnythingType(), CodegenUtil.hasTypeErased(expr.getPrimary()),
                        true, BoxingStrategy.BOXED, 
                        srcElementType, 0);
                transformedElement = transformMemberExpression(expr, transformedElement, transformer);
                
                // This short-circuit is here for spread invocations
                // The code has been called recursively and the part after this if-statement will
                // be handled by the previous recursion
                if (spreadMethodReferenceOuter) {
                    return make().LetExpr(letStmts.toList(), transformedElement);
                }
                
                Type resultElementType = expr.getTarget().getType();
                final Type resultAbsentType;
                transformedElement = applyErasureAndBoxing(transformedElement, resultElementType, 
                        // don't trust the erased flag of expr, as it reflects the result type of the overall spread expr,
                        // not necessarily of the applied member
                        expr.getTarget().getDeclaration() instanceof TypedDeclaration
                         ? CodegenUtil.hasTypeErased((TypedDeclaration)expr.getTarget().getDeclaration())
                         : false, 
                        !CodegenUtil.isUnBoxed(expr), BoxingStrategy.BOXED, resultElementType, 0);
                MethodDefinitionBuilder nextMdb = MethodDefinitionBuilder.systemMethod(this, "next");
                nextMdb.isOverride(true);
                nextMdb.annotationFlags(Annotations.IGNORE);
                nextMdb.modifiers(Flags.PUBLIC | Flags.FINAL);
                nextMdb.resultType(new TransformedType(make().Type(syms().objectType)));
                final List<JCTree> l;
                if (typeFact().isIterableType(expr.getPrimary().getTypeModel())) {
                    // private Iterator<srcElementType> iterator = srcIterableName.iterator();
                    JCVariableDecl srcIterator = makeVar(Flags.FINAL, srcIteratorName, makeJavaType(typeFact().getIteratorType(srcElementType)), 
                            make().Apply(null,
                                    // for super we do not capture it because we can't and it's constant anyways
                                    naming.makeQualIdent(isSuperOrSuperOf ? srcIterableExpr : srcIterableName.makeIdent(), "iterator"),
                                    List.<JCExpression>nil()));
                    
                    resultAbsentType = typeFact().getIteratedAbsentType(expr.getPrimary().getTypeModel());
                    nextMdb.body(List.of(
                            makeVar(iteratorResultName, 
                                make().Type(syms().objectType), null),
                            make().If(
                                    make().Unary(JCTree.Tag.NOT, 
                                    make().TypeTest(make().Assign(
                                            iteratorResultName.makeIdent(), 
                                            make().Apply(null,
                                                    naming.makeQualIdent(srcIteratorName.makeIdent(), "next"),
                                                    List.<JCExpression>nil())), 
                                            make().Type(syms().ceylonFinishedType))), 
                                    make().Block(0, List.<JCStatement>of(make().Exec(make().Assign(iteratorResultName.makeIdent(), 
                                            transformedElement)))), 
                                    null),
                            make().Return(iteratorResultName.makeIdent())));
                    l = List.of(srcIterator, nextMdb.build());
                } else if (typeFact().isJavaIterableType(expr.getPrimary().getTypeModel())) {
                    // private Iterator<srcElementType> iterator = srcIterableName.iterator();
                    JCVariableDecl srcIterator = makeVar(Flags.PRIVATE|Flags.FINAL, srcIteratorName, makeJavaType(typeFact().getJavaIteratorType(srcElementType)), 
                            make().Apply(null,
                                    // for super we do not capture it because we can't and it's constant anyways
                                    naming.makeQualIdent(isSuperOrSuperOf ? srcIterableExpr : srcIterableName.makeIdent(), "iterator"),
                                    List.<JCExpression>nil()));
                    
                    resultAbsentType = typeFact().getNullType();
                    nextMdb.body(List.<JCStatement>of(
                            make().If(
                                    make().Apply(null,
                                            naming.makeQualIdent(srcIteratorName.makeIdent(), "hasNext"),
                                            List.<JCExpression>nil()), 
                                    make().Block(0, List.<JCStatement>of(
                                            makeVar(iteratorResultName, 
                                                    make().Type(syms().objectType), 
                                                    make().Apply(null,
                                                            naming.makeQualIdent(srcIteratorName.makeIdent(), "next"),
                                                    List.<JCExpression>nil())),
                                            make().Return(transformedElement))), 
                                    make().Return(makeFinished()))));
                    l = List.of(srcIterator, nextMdb.build());
                } else if (typeFact().isJavaArrayType(expr.getPrimary().getTypeModel())) {
                    resultAbsentType = typeFact().getNullType();
                    JCVariableDecl srcIndex = makeVar(Flags.PRIVATE, srcIteratorName, make().Type(syms().intType), 
                            make().Literal(0));
                    JCExpression indexed = make().Indexed(
                            srcIterableName.makeIdent(), 
                            make().Unary(Tag.POSTINC, 
                                    srcIteratorName.makeIdent()));
                    if (typeFact().isJavaPrimitiveArrayType(expr.getPrimary().getTypeModel())) {
                        indexed = applyErasureAndBoxing(indexed, srcElementType, false, BoxingStrategy.BOXED, srcElementType);
                    }
                    nextMdb.body(List.<JCStatement>of(
                            make().If(
                                    make().Binary(Tag.LT, 
                                            srcIteratorName.makeIdent(), 
                                            // for super we do not capture it because we can't and it's constant anyways
                                            naming.makeQualIdent(isSuperOrSuperOf ? srcIterableExpr : srcIterableName.makeIdent(), "length")),
                                    make().Block(0, List.<JCStatement>of(
                                            makeVar(iteratorResultName, 
                                                    make().Type(syms().objectType),
                                                    indexed),
                                            make().Return(transformedElement))), 
                                    make().Return(makeFinished()))));
                    l = List.of(srcIndex, nextMdb.build());
                } else {
                    return makeErroneous(expr, "unhandled iterable type");
                }
                // new AbstractIterator()
                JCNewClass iteratorClass = make().NewClass(null, 
                        null, 
                        make().TypeApply(make().QualIdent(syms().ceylonAbstractIteratorType.tsym),
                                List.of(makeJavaType(resultElementType, JT_TYPE_ARGUMENT))),
                        List.of(makeReifiedTypeArgument(resultElementType)),
                        make().AnonymousClassDef(make().Modifiers(0), l));
                        
                MethodDefinitionBuilder iteratorMdb = MethodDefinitionBuilder.systemMethod(this, "iterator");
                iteratorMdb.isOverride(true);
                iteratorMdb.annotationFlags(Annotations.IGNORE);
                iteratorMdb.modifiers(Flags.PUBLIC | Flags.FINAL);
                iteratorMdb.resultType(new TransformedType(makeJavaType(typeFact().getIteratorType(resultElementType)), makeAtNonNull())); 
                iteratorMdb.body(make().Return(iteratorClass));
                        
                // new AbstractIterable()
                iterableClass = make().NewClass(null, 
                        null, 
                        make().TypeApply(make().QualIdent(syms().ceylonAbstractIterableType.tsym),
                                List.of(makeJavaType(resultElementType, JT_TYPE_ARGUMENT), makeJavaType(resultAbsentType, JT_TYPE_ARGUMENT))),
                        List.of(makeReifiedTypeArgument(resultElementType), makeReifiedTypeArgument(resultAbsentType)), 
                        make().AnonymousClassDef(make().Modifiers(0), List.<JCTree>of(iteratorMdb.build())));
            } finally {
                expressionGen().withinSyntheticClassBody(prevSyntheticClassBody);
            }
            
            if (aliasArguments) {
                letStmts = letStmts.appendList(((InvocationTermTransformer)transformer).callBuilder.getStatements());
            }
            
            JCMethodInvocation result = make().Apply(null, 
                    naming.makeQualIdent(iterableClass, "sequence"),
                    List.<JCExpression>nil());
            JCExpression spread = letStmts.isEmpty() ? result : make().LetExpr(letStmts.toList(), result);
            
            // Do we *statically* know the result must be a Sequence 
            final boolean primaryIsSequence = typeFact().isNonemptyIterableType(expr.getPrimary().getTypeModel());
            Type returnElementType = expr.getTarget().getType();
            if(primaryIsSequence){
                int flags = EXPR_DOWN_CAST;
                spread = applyErasureAndBoxing(spread, 
                        typeFact().getSequentialType(returnElementType),
                        false,
                        true,
                        BoxingStrategy.BOXED, 
                        primaryIsSequence ? 
                                typeFact().getSequenceType(returnElementType) 
                                : typeFact().getSequentialType(returnElementType),
                                flags);
            }
            return spread;
        } finally {
            spreading = oldSpreading;
        }
        
    }

    JCExpression transformQualifiedMemberPrimary(Tree.QualifiedMemberOrTypeExpression expr) {
        Declaration exprDec = expr.getDeclaration();
        if(expr.getTarget() == null)
            return makeErroneous(expr, "compiler bug: "
                    // make sure we don't die of a missing declaration too
                    + (exprDec != null ? exprDec.getName() : expr)
                    + " has a null target");

        // do not consider the primary to be an invocation since in foo.x() we're invoking x, not foo.
        boolean previousWithinInvocation = withinInvocation(false);
        try{
            // consider package qualifiers as non-prefixed, we always qualify them anyways, this is
            // only useful for the typechecker resolving
            
            Tree.Primary primary = expr.getPrimary();
            if (Decl.isConstructor(exprDec)) {
//                Constructor ctor = Decl.getConstructor(expr.getDeclaration());
                if (primary instanceof Tree.BaseMemberExpression) {
                    // foo.member.Ctor => foo
                } else if (primary instanceof Tree.QualifiedMemberExpression) {
                    // foo.Class.Ctor => foo
                    Tree.QualifiedMemberExpression qte = (Tree.QualifiedMemberExpression)primary;
                    primary = qte.getPrimary();
                } else if (primary instanceof Tree.BaseTypeExpression) {
                    // Class.Ctor => null
                    return null;
                } else if (primary instanceof Tree.QualifiedTypeExpression) {
                    // foo.member.Ctor => foo
                    Tree.QualifiedTypeExpression qte = (Tree.QualifiedTypeExpression) primary;
                    //very important here to distinguish between static ref to 
                    //constructors of member classes, and ref to constructors of 
                    //static classes
                    primary = qte.getStaticMethodReference() && qte.getDeclaration().isStatic() ? null : qte.getPrimary();
                }
            }
            if(isPackage(primary))
                return null;
            
            Type type = expr.getTarget().getQualifyingType();
            boolean safeMemberOp = expr.getMemberOperator() instanceof Tree.SafeMemberOp;
            if(safeMemberOp && !isOptional(type)){
                Type optionalType = typeFact().getOptionalType(type);
                if (optionalType.isCached()) {
                    optionalType = optionalType.clone();
                }
                optionalType.setUnderlyingType(type.getUnderlyingType());
                type = optionalType;
            }
            BoxingStrategy boxing = !safeMemberOp 
                    && Decl.isValueTypeDecl(primary)
                    && CodegenUtil.isUnBoxed(primary)
                    ? BoxingStrategy.UNBOXED : BoxingStrategy.BOXED;
            JCExpression result;
            if (isSuper(primary)) {
                result = transformSuper(expr);
            } else if (isSuperOf(primary)) {
                result = transformSuperOf(expr, expr.getPrimary(), exprDec.getName());
            } else if (isThis(primary)
                    && !exprDec.isCaptured() 
                    && !exprDec.isShared()
                    && Decl.getDeclarationScope(expr.getScope()) instanceof Constructor) {
                result = null;
            } else if (Decl.isJavaStaticOrInterfacePrimary(primary)) {
                // Java static field or method access
                result = transformJavaStaticOrInterfaceMember((Tree.QualifiedMemberOrTypeExpression)primary, expr.getTypeModel());
            } else {
                result = transformExpression(primary, boxing, type);
            }
            return result;
        }finally{
            withinInvocation(previousWithinInvocation);
        }
    }

    private JCExpression transformJavaStaticOrInterfaceMember(Tree.QualifiedMemberOrTypeExpression qmte, Type staticType) {
        Declaration decl = qmte.getDeclaration();
        if (decl instanceof FieldValue) {
            Value member = (Value)decl;
            return naming.makeName(member, Naming.NA_FQ | Naming.NA_WRAPPER_UNQUOTED);
        } else if (decl instanceof Value) {
            Value member = (Value)decl;
            CallBuilder callBuilder = CallBuilder.instance(this);
            Type qualifyingType = ((TypeDeclaration)member.getContainer()).getType();
            callBuilder.invoke(naming.makeQualifiedName(
                    makeJavaType(qualifyingType, JT_RAW | JT_NO_PRIMITIVES),
                    member, 
                    Naming.NA_GETTER | Naming.NA_MEMBER));
            return utilInvocation().checkNull(callBuilder.build());
        } else if (decl instanceof Function) {
            Function method = (Function)decl;
            final ParameterList parameterList = method.getFirstParameterList();
            Type qualifyingType = qmte.getPrimary().getTypeModel();
            Tree.TypeArguments typeArguments = qmte.getTypeArguments();
            Reference producedReference = method.appliedReference(qualifyingType, typeArguments.getTypeModels());
            return utilInvocation().checkNull(makeJavaStaticInvocation(gen(),
                    method, producedReference, parameterList));
        } else if (decl instanceof Class) {
            Class class_ = (Class)decl;
            if (class_.isStatic()) {
                return naming.makeTypeDeclarationExpression(null, class_, Naming.DeclNameFlag.QUALIFIED);
            } else {
                final ParameterList parameterList = class_.getFirstParameterList();
                Reference producedReference = qmte.getTarget();
                return utilInvocation().checkNull(makeJavaStaticInvocation(gen(),
                        class_, producedReference, parameterList));
            }
        } else if (decl instanceof Interface) {
            return naming.makeTypeDeclarationExpression(null, (Interface)decl, Naming.DeclNameFlag.QUALIFIED);
        } else {
            return makeErroneous(qmte, "compiler bug: unsupported static");
        }
    }

    JCExpression makeJavaStaticInvocation(CeylonTransformer gen,
            final Functional methodOrClass,
            Reference producedReference,
            final ParameterList parameterList) {
        CallBuilder callBuilder = CallBuilder.instance(gen);
        if (methodOrClass instanceof Function) {
            JCExpression fn;
            Declaration dec = (Declaration)methodOrClass;
            fn = Decl.isJavaArrayFrom(dec) ? 
                    gen.makeUnwrapArray(dec) : naming.makeName(
                    (Function)methodOrClass, Naming.NA_FQ | Naming.NA_WRAPPER_UNQUOTED);
            callBuilder.invoke(fn);
        } else if (methodOrClass instanceof Class) {
            Class klass = (Class)methodOrClass;
            callBuilder.instantiate(
                    gen.makeJavaType(klass.getType(), JT_RAW | JT_NO_PRIMITIVES));
        }
        ListBuffer<ExpressionAndType> reified = new ListBuffer<ExpressionAndType>();
        
        DirectInvocation.addReifiedArguments(gen, producedReference, reified);
        for (ExpressionAndType reifiedArgument : reified) {
            callBuilder.argument(reifiedArgument.expression);
        }
        
        for (Parameter parameter : parameterList.getParameters()) {
            callBuilder.argument(gen.naming.makeQuotedIdent(parameter.getName()));
        }
        JCExpression innerInvocation = callBuilder.build();
        return innerInvocation;
    }
    
    /**
     * Removes the parentheses from the given term
     */
    static Tree.Term eliminateParens(Tree.Term term) {
        while (term instanceof Tree.Expression) {
            term = ((Tree.Expression) term).getTerm();
        }
        return term;
    }
    
    private static boolean isThis(Tree.Primary primary) {
        return eliminateParensAndWidening(primary) instanceof Tree.This;
    }
    
    static boolean isPackage(Tree.Primary primary) {
        return eliminateParens(primary) instanceof Tree.Package;
    }
    
    static boolean isPackageQualified(Tree.QualifiedMemberOrTypeExpression qmte) {
        return isPackage(qmte.getPrimary());
    }
    
    /** 
     * Is the given primary a {@code super of Foo}
     * expression (modulo parentheses and multiple {@code of} 
     */
    private static boolean isSuperOf(Tree.Primary primary) {
        return primary instanceof Tree.Expression
            && eliminateParensAndWidening(((Tree.Expression)primary).getTerm()) instanceof Tree.Super;
    }
    
    /** 
     * Is the given primary a {@code super} expression
     * (modulo parentheses)
     */
    private static boolean isSuper(Tree.Primary primary) {
        return eliminateParens(primary) instanceof Tree.Super;
    }
    
    /** 
     * Is the given primary a {@code super} or {@code super of Foo} 
     * expression (modulo parentheses and multiple {@code of}
     */
    static boolean isSuperOrSuperOf(Tree.Primary primary) {
        return isSuper(primary) || isSuperOf(primary);
    }
    
    private JCExpression transformSuperOf(Node node, Tree.Primary superPrimary, String forMemberName) {
        Tree.Term superOf = eliminateParens(superPrimary);
        if (!(superOf instanceof Tree.OfOp)) {
            throw new BugException();
        }
        Tree.Type superType = ((Tree.OfOp)superOf).getType();
        if (!(eliminateParens(((Tree.OfOp)superOf).getTerm()) instanceof Tree.Super)) {
            throw new BugException();
        }
        TypeDeclaration inheritedFrom = superType.getTypeModel().getDeclaration();
        if (inheritedFrom instanceof Interface) {
            inheritedFrom = (TypeDeclaration)inheritedFrom.getMember(forMemberName, null, false).getContainer();
        }
        return widenSuper(node, inheritedFrom);
    }

    private JCExpression widenSuper(
            Node superOfQualifiedExpr,
            TypeDeclaration inheritedFrom) {
        JCExpression result;
        if (inheritedFrom instanceof Class) {
            if(isWithinSyntheticClassBody()){
                // super refers to the closest ClassOrInterface
                Scope scope = superOfQualifiedExpr.getScope();
                while (!(scope instanceof Package)) {
                    if (scope instanceof ClassOrInterface) {
                        break;
                    }
                    scope = scope.getContainer();
                }
                if(scope instanceof ClassOrInterface)
                    result = naming.makeQualifiedSuper(makeJavaType(((ClassOrInterface) scope).getType(), JT_RAW));
                else
                    result = naming.makeSuper();
            }else{
                result = naming.makeSuper();
            }
        } else if (inheritedFrom instanceof Interface) {
            Interface iface = (Interface)inheritedFrom;
            JCExpression qualifier = null;
            if (inheritedFrom instanceof LazyInterface
                    && !((LazyInterface)inheritedFrom).isCeylon()) {
                result = naming.makeQualifiedSuper(makeJavaType(inheritedFrom.getType(), JT_RAW));
            } else 
            if (needDollarThis(superOfQualifiedExpr.getScope())) {
                qualifier = naming.makeQuotedThis();
                if (iface.equals(typeFact().getIdentifiableDeclaration())) {
                    result = naming.makeQualifiedSuper(qualifier);
                } else {
                    result = naming.makeCompanionAccessorCall(qualifier, iface);
                }
            } else {
                if (iface.equals(typeFact().getIdentifiableDeclaration())) {
                    result = naming.makeQualifiedSuper(qualifier);
                } else {
                    result = naming.makeCompanionFieldName(iface);
                }
            }
            
            if (Decl.isAncestorLocal(iface)) {
                result = make().TypeCast(makeJavaType(iface.getType(), JT_COMPANION), result);
            }
            
        } else {
            result = makeErroneous(superOfQualifiedExpr, "compiler bug: " + (inheritedFrom == null ? "null" : inheritedFrom.getClass().getName()) + " is an unhandled case in widen()");
        }
        return result;
    }

    public JCExpression transformSuper(Tree.QualifiedMemberOrTypeExpression expression) {
        TypeDeclaration inheritedFrom = (TypeDeclaration)expression.getDeclaration().getContainer();
        return transformSuper(expression, inheritedFrom);
    }
    
    public JCExpression transformSuper(Node node, TypeDeclaration superDeclaration) {
        return widenSuper(node, superDeclaration);
    }
    
    // Base members
    
    public JCExpression transform(Tree.BaseMemberExpression expr) {
        return transform(expr, null);
    }

    private JCExpression transform(Tree.BaseMemberOrTypeExpression expr, TermTransformer transformer) {
        return transformMemberExpression(expr, null, transformer);
    }

    // Type members
    
    public JCExpression transform(Tree.QualifiedTypeExpression expr) {
        Tree.Primary prim = expr.getPrimary();
        if (prim instanceof Tree.BaseTypeExpression) {
            Tree.BaseTypeExpression primary = (Tree.BaseTypeExpression)prim;
            return transformMemberReference(expr, primary);            
        } else if (prim instanceof Tree.QualifiedTypeExpression) {
            Tree.QualifiedTypeExpression primary = (Tree.QualifiedTypeExpression)prim;
            return transformMemberReference(expr, primary);
        }
        return transform(expr, null);
    }
    
    public JCExpression transform(Tree.BaseTypeExpression expr) {
        return transform(expr, null);
    }
    
    private JCExpression transform(Tree.QualifiedTypeExpression expr, TermTransformer transformer) {
        Tree.MemberOperator op = expr.getMemberOperator();
        if (op instanceof Tree.SafeMemberOp) {
            return transformSafeMemberOperator(expr, transformer);
        }
        else if (op instanceof Tree.SpreadOp) {
            return transformSpreadOperator(expr, transformer);
        }
        else {
            JCExpression primaryExpr = transformQualifiedMemberPrimary(expr);
            return transformMemberExpression(expr, primaryExpr, transformer);
        }
    }
    
    // Generic code for all primaries
    
    public JCExpression transformTermForInvocation(Tree.Term term, TermTransformer transformer) {
        if (term instanceof Tree.QualifiedMemberExpression) {
            return transform((Tree.QualifiedMemberExpression)term, transformer);
        } else if (term instanceof Tree.BaseMemberExpression) {
            return transform((Tree.BaseMemberExpression)term, transformer);
        } else if (term instanceof Tree.BaseTypeExpression) {
            return transform((Tree.BaseTypeExpression)term, transformer);
        } else if (term instanceof Tree.QualifiedTypeExpression) {
            return transform((Tree.QualifiedTypeExpression)term, transformer);
        } else {
            // do not consider our term to be part of an invocation, we want it to be a Callable
            boolean oldWi = withinInvocation(false);
            JCExpression primaryExpr;
            try{
                primaryExpr = transformExpression(term);
                if (transformer != null) {
                    primaryExpr = transformer.transform(primaryExpr, null);
                }
            }finally{
                withinInvocation(oldWi);
            }
            return primaryExpr;
        }
    }

    private JCExpression transformMemberExpression(Tree.StaticMemberOrTypeExpression expr, JCExpression primaryExpr, 
            TermTransformer transformer) {
        JCExpression result = null;

        // do not throw, an error will already have been reported
        Declaration decl = expr.getDeclaration();
        if (decl == null) {
            return makeErroneous(expr, "compiler bug: expression with no declaration");
        }
        
        // Try to find the original declaration, in case we have conditionals that refine the type of objects without us
        // creating a tmp variable (in which case we have a substitution for it)
        while(decl instanceof TypedDeclaration){
            if(!naming.isSubstituted(decl)) {
                TypedDeclaration typedDecl = (TypedDeclaration) decl;
                TypedDeclaration orig = typedDecl.getOriginalDeclaration();
                if (orig != null) {
                    decl = orig;
                    continue;
                }
            }
            break;
        }
        
        // Make sure we're using the correct declaration in case of natives
        // (the header might look like a field while the implementation is a getter)
        if (decl.isNativeHeader()) {
            Declaration d = ModelUtil.getNativeDeclaration(decl, Backend.Java);
            if (d != null) {
                decl = d;
            }
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
        // true for default parameter methods
        boolean mustUseParameter = false;
        if (decl instanceof Functional
                && (!(decl instanceof Class) || ((Class)decl).getParameterList() != null)
                && (!(decl instanceof Function) || !decl.isParameter() 
                        || functionalParameterRequiresCallable((Function)decl, expr)) 
                && isFunctionalResult(expr.getTypeModel())) {
            result = transformFunctional(expr, (Functional)decl, expectedType);
        } else if (coerced
                && decl instanceof Value
                && isFunctionalResult(expr.getTypeModel())
                && checkForFunctionalInterface(expectedType) != null) {
            result = transformFunctionalInterfaceBridge(expr, (Value)decl, expectedType);
        } else if (coerced
                && decl instanceof Functional
                && decl.isParameter()
                && isFunctionalResult(expr.getTypeModel())
                && checkForFunctionalInterface(expectedType) != null) {
            result = transformFunctional(expr, (Functional)decl, expectedType);
//        } else if (coerced
//                && decl instanceof Value
//                && isJavaFunctionalInterfaceResult(expr.getTypeModel())
//                && expectedType != null
//                && isCeylonCallable(expectedType)) {
//            result = transformCallableBridge(expr, (Value)decl, expectedType);
        } else if (Decl.isGetter(decl)) {
            // invoke the getter
            if (decl.isToplevel()) {
                primaryExpr = null;
                qualExpr = naming.makeName((Value)decl, Naming.NA_FQ | Naming.NA_WRAPPER | Naming.NA_MEMBER);
                selector = null;
            } else if (decl.isClassOrInterfaceMember() && !ModelUtil.isLocalToInitializer(decl)) {
                selector = naming.selector((Value)decl);
            } else {
                // method local attr
                if (!isRecursiveReference(expr)) {
                    primaryExpr = naming.makeQualifiedName(primaryExpr, (Value)decl, Naming.NA_Q_LOCAL_INSTANCE);
                }
                selector = naming.selector((Value)decl);
            }
        } else if (Decl.isValueOrSharedOrCapturedParam(decl)) {
            if (decl.isToplevel()) {
                // ERASURE
                if (isNullValue(decl)) {
                    result = makeNull();
                } else if (isBooleanTrue(decl)) {
                    result = makeBoolean(true);
                } else if (isBooleanFalse(decl)) {
                    result = makeBoolean(false);
                } else {
                    // it's a toplevel attribute
                    TypedDeclaration typedDecl = (TypedDeclaration)decl;
                    primaryExpr = naming.makeName(typedDecl, Naming.NA_FQ | Naming.NA_WRAPPER);
                    selector = naming.selector(typedDecl);
                }
            } else if (Decl.isClassAttribute(decl) || Decl.isClassParameter(decl)) {
                Scope container = decl.getContainer();
                mustUseField = Decl.isJavaField(decl)
                        || (isWithinSuperInvocation() 
                                && primaryExpr == null
                                && withinSuperInvocation == container);
                mustUseParameter = primaryExpr == null && isWithinDefaultParameterExpression(container);
                if (mustUseField || mustUseParameter){
                    if(decl instanceof FieldValue) {
                        selector = ((FieldValue)decl).getRealName();
                    } else if (decl.isParameter()) {
                        selector = Naming.getAliasedParameterName(((Value)decl).getInitializerParameter());
                    } else {
                        selector = decl.getName();
                    }
                } else {
                    // invoke the getter, using the Java interop form of Util.getGetterName because this is the only case
                    // (Value inside a Class) where we might refer to JavaBean properties
                    selector = naming.selector((TypedDeclaration)decl);
                }
            } else if (ModelUtil.isCaptured(decl)) {
                TypedDeclaration typedDecl = (TypedDeclaration)decl;
                TypeDeclaration typeDecl = typedDecl.getType().getDeclaration();
                mustUseField = Decl.isBoxedVariable(typedDecl);
                if (ModelUtil.isLocalNotInitializer(typeDecl)
                        && typeDecl.isAnonymous()
                        // we need the box if it's a captured object
                        && !typedDecl.isSelfCaptured()) {
                    // accessing a local 'object' declaration, so don't need a getter 
                } else if (decl.isCaptured() 
                        && !typedDecl.isVariable()
                        // captured objects are never variable but need the box
                        && !typedDecl.isSelfCaptured()) {
                    // accessing a local that is not getter wrapped
                } else {
                    primaryExpr = naming.makeQualifiedName(primaryExpr, typedDecl, Naming.NA_Q_LOCAL_INSTANCE);
                    selector = naming.selector(typedDecl);
                }
            }
        } else if (Decl.isMethodOrSharedOrCapturedParam(decl)) {
            mustUseParameter = primaryExpr == null
                    && decl.isParameter()
                    && isWithinDefaultParameterExpression(decl.getContainer());
            if (!decl.isParameter()
                    && (ModelUtil.isLocalNotInitializer(decl) || ModelUtil.isLocalToInitializer(decl) && ((Function)decl).isDeferred())) {
                primaryExpr = null;
                int flags = Naming.NA_MEMBER;
                if (!isRecursiveReference(expr)) {
                    // Only want to quote the method name 
                    // e.g. enum.$enum()
                    flags |= Naming.NA_WRAPPER_UNQUOTED;
                }else if(!isReferenceInSameScope(expr)){
                    // always qualify it with this
                    flags |= Naming.NA_WRAPPER | Naming.NA_WRAPPER_WITH_THIS;
                }
                qualExpr = naming.makeName((Function)decl, flags);
                selector = null;
            } else if (decl.isToplevel()) {
                primaryExpr = null;
                qualExpr = naming.makeName((Function)decl, Naming.NA_FQ | Naming.NA_WRAPPER | Naming.NA_MEMBER);
                selector = null;
            } else if (!isWithinInvocation()) {
                selector = null;
            } else if (decl.isClassOrInterfaceMember()){
                selector = naming.selector((Function)decl);
            } else {
                selector = null;
            }
        }
        boolean isCtor = decl instanceof Function 
                && ((Function)decl).getTypeDeclaration() instanceof Constructor;
        if (result == null) {
            boolean useGetter = !(decl instanceof Function || isCtor) && !mustUseField && !mustUseParameter;
            if (qualExpr == null && selector == null && !isCtor) {
                useGetter = Decl.isClassAttribute(decl) && CodegenUtil.isErasedAttribute(decl.getName());
                selector = useGetter ? naming.selector((TypedDeclaration)decl) : naming.substitute(decl);
            }
            
            if (qualExpr == null) {
                qualExpr = primaryExpr;
            }
            
            // FIXME: Stef has a strong suspicion that the four next methods
            // should be merged since they all add a this qualifier in different
            // cases
            if(!mustUseParameter){
                qualExpr = addQualifierForObjectMembersOfInterface(expr, decl, qualExpr);

                qualExpr = addInterfaceImplAccessorIfRequired(qualExpr, expr, decl);

                qualExpr = addThisOrObjectQualifierIfRequired(qualExpr, expr, decl);

                if (qualExpr == null 
                        && expr instanceof Tree.BaseMemberExpression
                        && needDollarThis(expr)) {
                    qualExpr = makeQualifiedDollarThis((Tree.BaseMemberExpression)expr);
                }
            }
            
            boolean isEnumeratedConstructorGetter = false;
            if(decl instanceof Value && ModelUtil.isEnumeratedConstructor((Value)decl)){
                Class constructedClass = ModelUtil.getConstructedClass(decl);
                // See CeylonVisitor.transformSingletonConstructor for that logic
                if(constructedClass.isToplevel() || constructedClass.isClassMember())
                    isEnumeratedConstructorGetter = true;
                else{
                    // Local enumerated constructor values are boxed
                    useGetter = false; // local class will use a field
                    qualExpr = naming.makeQualifiedName(primaryExpr, (TypedDeclaration)decl, Naming.NA_Q_LOCAL_INSTANCE);
                    selector = naming.selector((TypedDeclaration)decl);
                }
            }
            if (qualExpr == null 
                    && (decl.isStatic() || isEnumeratedConstructorGetter)
                    // make sure we only do this for things contained in a type, as otherwise
                    // it breaks for qualified calls to static methods in interfaces in Java 8
                    // it only breaks for interfaces because they are statically importable
                    // and not classes
                    && decl.getContainer() instanceof TypeDeclaration) {
                qualExpr = naming.makeTypeDeclarationExpression(null, (TypeDeclaration)decl.getContainer(), DeclNameFlag.QUALIFIED);
            }
            if (Decl.isPrivateAccessRequiringUpcast(expr)) {
                qualExpr = makePrivateAccessUpcast(expr, qualExpr);
            }
            
            if (transformer != null) {
                if (decl instanceof TypedDeclaration 
                        && ((TypedDeclaration)decl).getType().isTypeConstructor()) {
                    // This is a bit of a hack, but we're "invoking a type constructor" 
                    // so recurse to get the applied expression. 
                    qualExpr = transformMemberExpression(expr, qualExpr, null);
                    selector = null;
                }
                
                result = transformer.transform(qualExpr, selector);
            } else {
                boolean qualified = expr instanceof Tree.QualifiedMemberOrTypeExpression;
                boolean safeMemberOp;
                boolean memberOp;
                boolean javaArray;
                Tree.Primary qmePrimary;
                if (qualified) {
                    Tree.QualifiedMemberOrTypeExpression qmte = (Tree.QualifiedMemberOrTypeExpression)expr;
                    qmePrimary = qmte.getPrimary();
                    Tree.MemberOperator op = qmte.getMemberOperator();
                    memberOp = op instanceof Tree.MemberOp;
                    safeMemberOp = op instanceof Tree.SafeMemberOp;
                    javaArray = isJavaArray(qmePrimary.getTypeModel());
                }
                else {
                    qmePrimary = null;
                    memberOp = false;
                    safeMemberOp = false;
                    javaArray = false;
                }
                
                if ((Decl.isValueTypeDecl(qmePrimary) || safeMemberOp && javaArray)
                        // Safe operators always work on boxed things, so don't use value types
                        && (!qualified || memberOp || safeMemberOp && javaArray)
                        // We never want to use value types on boxed things, unless they are java arrays
                        && (CodegenUtil.isUnBoxed(qmePrimary) || javaArray)
                        // Java arrays length property does not go via value types
                        && !(javaArray && ("length".equals(selector) || "hashCode".equals(selector)))) {
                    JCExpression primTypeExpr = makeJavaType(qmePrimary.getTypeModel(), JT_NO_PRIMITIVES | JT_VALUE_TYPE);
                    result = makeQualIdent(primTypeExpr, selector);
                    result = make().Apply(List.<JCTree.JCExpression>nil(),
                            result,
                            List.<JCTree.JCExpression>of(qualExpr));
                } else if (qualified && isThrowableMessage((Tree.QualifiedMemberOrTypeExpression)expr)) {
                    result = utilInvocation().throwableMessage(qualExpr);
                } else if (qualified && isThrowableSuppressed((Tree.QualifiedMemberOrTypeExpression)expr)) {
                    result = utilInvocation().suppressedExceptions(qualExpr);
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
        
        if (transformer == null
                && decl instanceof TypedDeclaration 
                && ((TypedDeclaration)decl).getType().isTypeConstructor()
                && !expr.getTypeArguments().getTypeModels().isEmpty()) {
            // applying a type constructor
            ListBuffer<JCExpression> tds = new ListBuffer<JCExpression>();
            for (Type t : expr.getTypeArguments().getTypeModels()) {
                tds.add(makeReifiedTypeArgument(t));
            }
            result = make().Apply(
                    null,
                    makeQualIdent(result, Naming.Unfix.apply.toString()),
                    List.<JCExpression>of(make().NewArray(make().Type(syms().ceylonTypeDescriptorType),
                            List.<JCExpression>nil(),
                            tds.toList())));
        }
        
        return result;
    }

    /**
     * We may need to force a qualified this prefix (direct or outer) in the following cases:
     * 
     * - Required because of mixin inheritance with different type arguments (the same is already
     *   done for qualified references, but not for direct references)
     * - The compiler generates anonymous local classes for things like
     *   Callables and Comprehensions. When referring to a member foo 
     *   within one of those things we need a qualified {@code this}
     *   to ensure we're accessing the outer instances member, not 
     *   a member of the anonymous local class that happens to have the same name.
     */
    private JCExpression addThisOrObjectQualifierIfRequired(
            JCExpression qualExpr, Tree.StaticMemberOrTypeExpression expr,
            Declaration decl) {
        boolean constructor = Decl.isConstructor(decl);
        // find out the real target
        Declaration typeDecl = constructor ? ModelUtil.getConstructedClass(decl) : decl;
        Reference target = expr.getTarget();
        if (qualExpr == null 
                // statics are not members that can be inherited
                && !decl.isStatic()
                && (constructor ? !typeDecl.isStatic() : true)
                && typeDecl.isMember()
                // dodge variable refinements with assert/is (these will be turned to locals
                // and have a name mapping)
                && target.getDeclaration() == decl
                && !ModelUtil.isLocalToInitializer(typeDecl)
                && !isWithinSuperInvocation()) {
            // First check whether the expression is captured from an enclosing scope
            TypeDeclaration outer = Decl.getOuterScopeOfMemberInvocation(expr, typeDecl);
            if (outer != null) {
                Type targetType = target.getQualifyingType();
                Type declarationContainerType = ((TypeDeclaration)outer).getType();
                // check if we need a variance cast
                VarianceCastResult varianceCastResult = getVarianceCastResult(targetType, declarationContainerType);
                // if we are within a comprehension body, or if we need a variance cast
                if(isWithinSyntheticClassBody() || varianceCastResult != null){
                    if (decl.isShared() && outer instanceof Interface) {
                        // always prefer qualified
                        qualExpr = makeQualifiedDollarThis(declarationContainerType);
                    } else {
                        // Class or companion class,
                        qualExpr = naming.makeQualifiedThis(makeJavaType(((TypeDeclaration)outer).getType(), 
                                JT_RAW | (outer instanceof Interface ? JT_COMPANION : 0)));
                    }
                    // add the variance cast if required
                    if(varianceCastResult != null){
                        qualExpr = applyVarianceCasts(qualExpr, targetType, varianceCastResult, 0);
                    }
                }
            } else if (typeDecl.isClassOrInterfaceMember()){
                ClassOrInterface container;
                ClassOrInterface classOrInterface = 
                        (ClassOrInterface)
                            typeDecl.getContainer();
                if(classOrInterface.isAnonymous()
                    && classOrInterface.isToplevel()) {
                    // easy
                    container = classOrInterface;
                }else{
                    // find the import
                    Import foundImport = statementGen().findImport(expr, decl);
                    container = (Class) foundImport.getTypeDeclaration();
                }
                //NOTE: this line looks completely wrong to Gavin
                Value value = (Value)((Package)container.getContainer()).getMember(container.getName(), null, false);
                qualExpr = make().Apply(null,
                        naming.makeName(value, Naming.NA_FQ | Naming.NA_WRAPPER | Naming.NA_MEMBER),
                        List.<JCExpression>nil());    
            } else if (decl.isMember() && !expr.getStaticMethodReference()) {
                throw new BugException(expr, decl.getQualifiedNameString() + " was unexpectedly a member");
            }
        }
        return qualExpr;
    }

    /**
     * §3.2.2 Every interface is a subtype of c.l.Object, so 
     * within an Interface {@code string} means {@code $this.toString()}
     * @param expr
     * @param decl
     * @param qualExpr
     * @return
     */
    // Interface we must use $this's implementation of equals, hash and string
    private JCExpression addQualifierForObjectMembersOfInterface(
            Tree.StaticMemberOrTypeExpression expr, Declaration decl,
            JCExpression qualExpr) {
        if (expr instanceof Tree.BaseMemberExpression
                && qualExpr == null
                && typeFact().getObjectDeclaration().equals(ModelUtil.getClassOrInterfaceContainer(decl))) {
            Scope scope = expr.getScope();
            while (ModelUtil.isLocalNotInitializerScope(scope)) {
                scope = scope.getContainer();
            }
            if (scope instanceof Interface) {
                qualExpr = naming.makeQuotedThis();
            }
        }
        return qualExpr;
    }

    /**
     * Determines whether we need to generate an AbstractCallable when taking 
     * a method reference to a method that's declared as a FunctionalParameter
     */
    private boolean functionalParameterRequiresCallable(Function functionalParameter, Tree.StaticMemberOrTypeExpression expr) {
        if (!functionalParameter.isParameter()) {
            throw new BugException();
        }
        boolean hasMethod = JvmBackendUtil.createMethod(functionalParameter);
        if (!hasMethod) {
            // A functional parameter that's not method wrapped will already be Callable-wrapped
            return false;
        }
        // Optimization: If we're in a scope where the Callable field is visible
        // we don't need to create a method ref        
        Scope scope = expr.getScope();
        while (true) {
            if (scope instanceof Package) {
                break;
            }
            if (scope.equals(functionalParameter.getContainer())) {
                return false;
            }
            scope = scope.getContainer();
        }
        // Otherwise we do require an AbstractCallable.
        return true;
    }

    //
    // Array access

    private JCExpression addInterfaceImplAccessorIfRequired(JCExpression qualExpr, Tree.StaticMemberOrTypeExpression expr, Declaration decl) {
        // Partial fix for https://github.com/ceylon/ceylon-compiler/issues/1023
        // For interfaces we sometimes need to access either the interface instance or its $impl class
        if (decl instanceof Constructor) {
            decl = (Class) decl.getContainer();
        }
        Scope declContainer = decl.getContainer();
        if(qualExpr != null
                // this is only for interface containers
                && declContainer instanceof Interface
                // we only ever need the $impl if the declaration is not shared
                && !decl.isShared()
                && (!(expr instanceof Tree.QualifiedMemberExpression)
                || !isSuperOrSuperOf(((Tree.QualifiedMemberExpression)expr).getPrimary()))){
            Interface declaration = (Interface) declContainer;
            // access the interface $impl instance
            qualExpr = naming.makeCompanionAccessorCall(qualExpr, declaration);
            // When the interface is local the accessor returns Object
            // so we need to cast it to the type of the companion
            if (Decl.isAncestorLocal(declaration)) {
                Type type;
                // try to find the best type
                if(expr instanceof Tree.QualifiedMemberOrTypeExpression)
                    type = ((Tree.QualifiedMemberOrTypeExpression) expr).getPrimary().getTypeModel();
                else
                    type = declaration.getType();
                qualExpr = make().TypeCast(makeJavaType(type, JT_COMPANION), qualExpr);
            }
        }
        return qualExpr;
    }

    private JCExpression makeQualifiedDollarThis(Tree.BaseMemberExpression expr) {
        Declaration decl = expr.getDeclaration();
        Interface interf = (Interface) ModelUtil.getClassOrInterfaceContainer(decl);
        // find the target container interface that is or satisfies the given interface
        Scope scope = expr.getScope();
        boolean needsQualified = false;
        while(scope != null){
            if(scope instanceof Interface){
                if(Decl.equalScopeDecl(scope, interf) || ((Interface)scope).inherits(interf)){
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
        return makeQualifiedDollarThis(interf.getType());
    }
    
    private JCExpression makeQualifiedDollarThis(Type targetType){
        JCExpression qualifiedCompanionThis = naming.makeQualifiedThis(makeJavaType(targetType, JT_COMPANION | JT_RAW));
        return naming.makeQualifiedDollarThis(qualifiedCompanionThis);
    }

    boolean needDollarThis(Tree.StaticMemberOrTypeExpression expr) {
        if (expr instanceof Tree.BaseMemberExpression
                || expr instanceof Tree.BaseTypeExpression) {
            // We need to add a `$this` prefix to the member expression if:
            // * The member was declared on an interface I and
            // * The member is being used in the companion class of I or 
            //   // REMOVED: some subinterface of I, and
            //   some member type of I, and
            // * The member is shared (non-shared means its only on the companion class)
            // FIXME: https://github.com/ceylon/ceylon-compiler/issues/1019
            final Declaration decl = expr.getDeclaration();
            if(!decl.isInterfaceMember())
                return false;
            
            // Find the method/getter/setter where the expr is being used
            Scope scope = expr.getScope();
            while (scope != null){
                // Is it being used in an interface (=> impl)
                if(scope instanceof Interface && ((Interface) scope).getType().isSubtypeOf(scope.getDeclaringType(decl))) {
                    return decl.isShared();
                }
                scope = scope.getContainer();
            }
        }
        return false;
    }
    
    private boolean needDollarThis(Scope scope) {
        while (ModelUtil.isLocalNotInitializerScope(scope)) {
            scope = scope.getContainer();
        }
        return scope instanceof Interface;
    }

    /** 
     * Abstract method pattern for transforming 
     * indexed expressions: {@code foo[index]}
     */
    abstract class AbstractIndexTransformer {
        final Type primaryType;
        final Type rightType;
        final Type leftType;
        final Type elementType;
        
        AbstractIndexTransformer(Tree.IndexExpression access, 
                Type leftType, 
                Type rightType, 
                Type elementType) {
            this.primaryType = access.getPrimary().getTypeModel();
            this.leftType = leftType;
            this.rightType = rightType;
            this.elementType = elementType;
        }
        protected abstract String getGetterName();
        protected abstract BoxingStrategy getIndexBoxing();
        protected abstract Type leftTypeForGetCall();
        protected JCExpression transformPrimary(Tree.IndexExpression indexExpr) {
            final JCExpression lhs;
            final String getter = getGetterName();
            if(isSuper(indexExpr.getPrimary())) {
                Declaration member = primaryType.getDeclaration().getMember(getter, null, false);
                TypeDeclaration leftDeclaration = (TypeDeclaration) member.getContainer();
                lhs = transformSuper(indexExpr, leftDeclaration);
            } else if (isSuperOf(indexExpr.getPrimary())) {
                lhs = transformSuperOf(indexExpr, indexExpr.getPrimary(), getter);
            } else{
                Type leftTypeForGetCall = leftTypeForGetCall();
                lhs = transformExpression(indexExpr.getPrimary(), BoxingStrategy.BOXED, leftTypeForGetCall);
            }
            return lhs;
        }
        
        public JCExpression transform(Tree.IndexExpression indexExpr) {
            JCExpression result = transformIndexed(indexExpr);
            // Because tuple index access has the type of the indexed element
            // (not the union of types in the sequential) a typecast may be required.
            Type expectedType = indexExpr.getTypeModel();
            int flags = 0;
            if(!expectedType.isExactly(elementType)
                    // could be optional too, for regular Correspondence item access
                    && !expectedType.isExactly(typeFact().getOptionalType(elementType)))
                flags |= EXPR_DOWN_CAST;
            result = applyErasureAndBoxing(result, 
                                               elementType, 
                                               CodegenUtil.hasTypeErased(indexExpr), true, BoxingStrategy.BOXED, 
                                               expectedType, flags);
            return result;
        }
        protected JCExpression transformIndexed(Tree.IndexExpression indexExpr) {
            JCExpression primaryExpr = makeSelect(transformPrimary(indexExpr), getGetterName());
            JCExpression index = transformIndex((Tree.Element)indexExpr.getElementOrRange());
            JCExpression result = at(indexExpr).Apply(List.<JCTree.JCExpression>nil(), 
                                          primaryExpr, List.of(index));
            return result;
        }
        
        protected JCExpression transformIndex(Tree.Element element) {
            // do the index
            BoxingStrategy indexBs = getIndexBoxing();
            JCExpression index = transformExpression(element.getExpression(), indexBs, rightType);
            return index;
        }
    }

    class CorrespondenceIndexTransformer extends AbstractIndexTransformer {

        private boolean useGetFromFirst;

        CorrespondenceIndexTransformer(Tree.IndexExpression indexExpr, Type leftType, Type rightType, Type sequentialElementType) {
            super(indexExpr, leftType, rightType, sequentialElementType);
            boolean isOnList = primaryType.isSubtypeOf(typeFact().getListDeclaration().appliedType(
                    null, Collections.singletonList(typeFact().getAnythingType())));
            Tree.Primary primary = indexExpr.getPrimary();
            boolean isSuper = isSuper(primary);
            boolean isOnSuper = isSuper || isSuperOf(primary);
            if (isOnList) {
                // can we use getFromFirst() to avoid boxing the index?
                useGetFromFirst = true;
                if (isOnSuper) {
                    // this is super special: if we use the optim and call super we need to make sure that "getFromFirst" already
                    // has a concrete super implementation
                    Declaration member = primaryType.getDeclaration().getMember("getFromFirst", null, false);
                    if(member == null || member.isFormal()){
                        useGetFromFirst = false;
                    }
                }
            } else {
                useGetFromFirst = false;
            }
        }

        @Override
        protected String getGetterName() {
            return useGetFromFirst ? "getFromFirst": "get";
        }

        @Override
        protected BoxingStrategy getIndexBoxing() {
            return useGetFromFirst ? BoxingStrategy.UNBOXED : BoxingStrategy.BOXED;
        }

        @Override
        protected Type leftTypeForGetCall() {
            return useGetFromFirst ? primaryType.getSupertype(typeFact().getListDeclaration()) : leftType;
        }
    }
    
    class JavaListIndexTransformer extends AbstractIndexTransformer {

        JavaListIndexTransformer(Tree.IndexExpression indexExpr, Type leftType, Type rightType, Type sequentialElementType) {
            super(indexExpr, leftType, rightType, sequentialElementType);
        }

        @Override
        protected String getGetterName() {
            return "get";
        }

        @Override
        protected BoxingStrategy getIndexBoxing() {
            return BoxingStrategy.UNBOXED;
        }

        @Override
        protected Type leftTypeForGetCall() {
            return leftType;
        }
        
        protected JCExpression transformIndexed(Tree.IndexExpression indexExpr) {
            
            JCExpression listExpr = transformPrimary(indexExpr);
            JCExpression index = transformIndex((Tree.Element)indexExpr.getElementOrRange());
            
            SyntheticName listName = naming.temp("list");
            SyntheticName indexName = naming.temp("index");
            return at(indexExpr).LetExpr(
                    List.<JCStatement>of(
                            makeVar(listName, makeJavaType(leftType), listExpr),
                            makeVar(indexName, make().Type(syms().intType), index)
                    ), 
                    make().Conditional(
                        make().Binary(JCTree.Tag.AND, 
                                make().Binary(JCTree.Tag.GE, indexName.makeIdent(), make().Literal(0)),
                                make().Binary(JCTree.Tag.LT, indexName.makeIdent(), make().Apply(null,  
                                        naming.makeQualIdent(listName.makeIdent(), "size"),
                                        List.<JCExpression>nil()))), 
                        at(indexExpr).Apply(List.<JCTree.JCExpression>nil(), 
                                makeSelect(listName.makeIdent(), getGetterName()), 
                                List.<JCExpression>of(indexName.makeIdent())), 
                        makeNull()));
            
        }
    }
    
    class JavaMapIndexTransformer extends AbstractIndexTransformer {

        JavaMapIndexTransformer(Tree.IndexExpression indexExpr, Type leftType, Type rightType, Type elementType) {
            super(indexExpr, leftType, rightType, elementType);
        }

        @Override
        protected String getGetterName() {
            return "get";
        }

        @Override
        protected BoxingStrategy getIndexBoxing() {
            return BoxingStrategy.BOXED;
        }

        @Override
        protected Type leftTypeForGetCall() {
            return leftType;
        }
    }
    
    class JavaArrayIndexTransformer extends AbstractIndexTransformer {

        private Type returnType;

        JavaArrayIndexTransformer(Tree.IndexExpression indexExpr, Type leftType, Type rightType, Type elementType) {
            super(indexExpr, leftType, rightType, elementType);
            returnType = indexExpr.getTypeModel();
        }

        @Override
        protected JCExpression transformIndexed(Tree.IndexExpression indexExpr) {
            JCExpression arrayExpr = transformPrimary(indexExpr);
            JCExpression index = transformIndex((Tree.Element)indexExpr.getElementOrRange());
            
            if(typeFact().isOptionalType(returnType)){
                SyntheticName listName = naming.temp("array");
                SyntheticName indexName = naming.temp("index");
                return at(indexExpr).LetExpr(
                        List.<JCStatement>of(
                                makeVar(listName, makeJavaType(leftType, JT_NO_PRIMITIVES), arrayExpr),
                                makeVar(indexName, make().Type(syms().intType), index)
                                ), 
                        make().Conditional(
                                make().Binary(JCTree.Tag.AND, 
                                        make().Binary(JCTree.Tag.GE, indexName.makeIdent(), make().Literal(0)),
                                        make().Binary(JCTree.Tag.LT, indexName.makeIdent(),   
                                                naming.makeQualIdent(listName.makeIdent(), "length"))), 
                                make().Indexed(listName.makeIdent(), indexName.makeIdent()), 
                                makeNull()));
            }else{
                return at(indexExpr).Indexed(arrayExpr, index);
            }
        }
        
        @Override
        protected String getGetterName() {
            return null;
        }

        @Override
        protected BoxingStrategy getIndexBoxing() {
            return BoxingStrategy.UNBOXED;
        }

        @Override
        protected Type leftTypeForGetCall() {
            return leftType;
        }
    }
    
    public JCExpression transform(Tree.IndexExpression indexedExpr) {
        // depends on the operator
        Tree.ElementOrRange elementOrRange = indexedExpr.getElementOrRange();
        if (elementOrRange instanceof Tree.Element) {
            // foo[index] -- foo could be a Correspondence, Java List or Java Map
            final AbstractIndexTransformer transformer;
            final Type primaryType = indexedExpr.getPrimary().getTypeModel().resolveAliases();
            Type leftType = primaryType.getSupertype(typeFact().getCorrespondenceDeclaration());
            if (leftType != null) {
                Type rightType = getTypeArgument(leftType, 0);
                transformer = new CorrespondenceIndexTransformer(indexedExpr, leftType, rightType, getTypeArgument(leftType, 1));
            } else {
                leftType = primaryType.getSupertype(typeFact().getJavaListDeclaration());
                if (leftType != null) {
                    Type rightType = typeFact().getIntegerType();
                    if (rightType.isCached()) {
                        rightType = rightType.clone();
                    }
                    rightType.setUnderlyingType("int");
                    transformer = new JavaListIndexTransformer(indexedExpr, leftType, rightType, getTypeArgument(leftType, 0));
                } else {
                    leftType = primaryType.getSupertype(typeFact().getJavaMapDeclaration());
                    if (leftType != null) {
                        Type rightType = getTypeArgument(leftType, 0);
                        transformer = new JavaMapIndexTransformer(indexedExpr, leftType, rightType, getTypeArgument(leftType, 1));
                    } else if (isJavaArray(primaryType)) {
                        Type rightType = typeFact().getIntegerType();
                        if (rightType.isCached()) {
                            rightType = rightType.clone();
                        }
                        rightType.setUnderlyingType("int");
                        Type elementType;
                        if (isJavaObjectArray(primaryType)) {
                            leftType = primaryType.getSupertype(typeFact().getJavaObjectArrayDeclaration());
                            elementType = getTypeArgument(leftType, 0);
                        } else if (JvmBackendUtil.isJavaBooleanArray(primaryType.getDeclaration())) {
                            leftType = typeFact().getJavaBooleanArrayDeclaration().getType();
                            elementType = typeFact().getBooleanType();
                        } else if (JvmBackendUtil.isJavaByteArray(primaryType.getDeclaration())) {
                            leftType = typeFact().getJavaByteArrayDeclaration().getType();
                            elementType = typeFact().getByteType();
                        } else if (JvmBackendUtil.isJavaShortArray(primaryType.getDeclaration())) {
                            leftType = typeFact().getJavaShortArrayDeclaration().getType();
                            elementType = typeFact().getIntegerType();
                        } else if (JvmBackendUtil.isJavaIntArray(primaryType.getDeclaration())) {
                            leftType = typeFact().getJavaIntArrayDeclaration().getType();
                            elementType = typeFact().getIntegerType();
                        } else if (JvmBackendUtil.isJavaLongArray(primaryType.getDeclaration())) {
                            leftType = typeFact().getJavaLongArrayDeclaration().getType();
                            elementType = typeFact().getIntegerType();
                        } else if (JvmBackendUtil.isJavaFloatArray(primaryType.getDeclaration())) {
                            leftType = typeFact().getJavaFloatArrayDeclaration().getType();
                            elementType = typeFact().getFloatType();
                        } else if (JvmBackendUtil.isJavaDoubleArray(primaryType.getDeclaration())) {
                            leftType = typeFact().getJavaDoubleArrayDeclaration().getType();
                            elementType = typeFact().getFloatType();
                        } else if (JvmBackendUtil.isJavaCharArray(primaryType.getDeclaration())) {
                            leftType = typeFact().getJavaCharArrayDeclaration().getType();
                            elementType = typeFact().getCharacterType();
                        } else {
                            return makeErroneous(indexedExpr, "Unsupported primary for indexed expression");
                        }
                        transformer = new JavaArrayIndexTransformer(indexedExpr, leftType, rightType, elementType);
                    } else {
                        return makeErroneous(indexedExpr, "Unsupported primary for indexed expression");
                    }
                }
            }
            return transformer.transform(indexedExpr);
        } else {
            // foo[start:end] or foo[start:length]
            Type primaryType = indexedExpr.getPrimary().getTypeModel();
            Type leftType = primaryType.getSupertype(typeFact().getRangedDeclaration());
            Type rightType = getTypeArgument(leftType, 0);
            Tree.ElementRange range = (Tree.ElementRange) indexedExpr.getElementOrRange();
            
            // do the indices
            JCExpression start = transformExpression(range.getLowerBound(), BoxingStrategy.BOXED, rightType);

            // is this a span or segment?
            String method;
            final List<JCExpression> args;
            if (range.getLowerBound() != null 
                    && range.getLength() != null) {
                method = "measure";
                JCExpression length = transformExpression(range.getLength(), BoxingStrategy.UNBOXED, typeFact().getIntegerType());
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
                args = List.<JCExpression>of(makeErroneous(range, "compiler bug: unhandled range"));
            }

            JCExpression lhs;

            Tree.Primary primary = indexedExpr.getPrimary();
            boolean isSuper = isSuper(primary);
            if(isSuper || isSuperOf(primary)){
                Declaration member = primaryType.getDeclaration().getMember(method, null, false);
                TypeDeclaration leftDeclaration = (TypeDeclaration) member.getContainer();
                if(isSuper)
                    lhs = transformSuper(indexedExpr, leftDeclaration);
                else
                    lhs = transformSuperOf(indexedExpr, indexedExpr.getPrimary(), method);
            }else{
                int flags = 0;
                // this is pretty much disgusting, but all I found. Given a Ranged<Integer,Integer,Integer[]> type
                // we don't notice that this type can't be cast to without a raw cast because it has constrained
                // type params in its hierarchy. hasConstrainedTypeParameters only checks that type, and we want
                // to check Ranged<Index, Element, Subrange> instead, which has those constraints, which is why we
                // do it on the declaration type. Honestly this should probably rather be an inheritance check in
                // applyErasureAndCasts but that'd be costly and complex and likely introduce lots of other issues.
                // See https://github.com/ceylon/ceylon/issues/6365
                if(leftType.getDeclaration() instanceof ClassOrInterface
                        // even more disgusting: https://github.com/ceylon/ceylon/issues/6450
                        && !isCeylonString(primaryType)
                        && hasConstrainedTypeParameters(leftType.getDeclaration().getType()))
                    flags |= EXPR_EXPECTED_TYPE_HAS_CONSTRAINED_TYPE_PARAMETERS;
                lhs = transformExpression(indexedExpr.getPrimary(), BoxingStrategy.BOXED, leftType, flags);
            }
            
            JCExpression result;
            // Because tuple open span access has the type of the indexed element
            // (not a sequential of the union of types in the ranged) a typecast may be required.
            Type rangedSpanType = getTypeArgument(leftType, 2);
            Type expectedType = indexedExpr.getTypeModel();
            int flags = 0;
            if(!expectedType.isExactly(rangedSpanType)){
                flags |= EXPR_DOWN_CAST;
                // make sure we barf properly if we missed a heuristics
                if(method.equals("spanFrom")){
                    // make a "Util.<method>(lhs, start, end)" call
                    at(indexedExpr);
                    result = utilInvocation().tuple_spanFrom(args.prepend(lhs));
                }else{
                    result = makeErroneous(indexedExpr, "compiler bug: only the spanFrom method should be specialised for Tuples");
                }
            }else{
                // make a "lhs.<method>(start, end)" call
                result = at(indexedExpr).Apply(List.<JCTree.JCExpression>nil(), 
                        makeSelect(lhs, method), args);
            }
            result = applyErasureAndBoxing(result, 
                                               rangedSpanType, 
                                               CodegenUtil.hasTypeErased(indexedExpr), true, BoxingStrategy.BOXED, 
                                               expectedType, flags);
            return result;
        }
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
        
        // FIXME: can this be anything else than a Tree.MemberOrTypeExpression or Tree.ParameterizedExpression or Tree.IndexExpression?
        final JCExpression rhs;
        BoxingStrategy boxing;
        if (leftTerm instanceof Tree.MemberOrTypeExpression) {
            TypedDeclaration decl = (TypedDeclaration) ((Tree.MemberOrTypeExpression)leftTerm).getDeclaration();
            boxing = CodegenUtil.getBoxingStrategy(decl);
            if (decl instanceof Value) {
                Value val = (Value)decl;
                if (val.getSetter() != null && val.getSetter().getUnboxed() != null) {
                    boxing = CodegenUtil.getBoxingStrategy(val.getSetter());
                }
            }
            Type targetType = tmpInStatement ? leftTerm.getTypeModel() : rightTerm.getTypeModel();
            // if we're dealing with widening do not trust the type of the declaration and get the real type
            if(CodegenUtil.hasUntrustedType(decl)){
                TypedReference typedRef = (TypedReference) ((Tree.MemberOrTypeExpression)leftTerm).getTarget();
                TypedReference nonWideningTypedRef = nonWideningTypeDecl(typedRef);
                targetType = nonWideningType(typedRef, nonWideningTypedRef);
            }
            int flags = decl.hasUncheckedNullType() ? EXPR_TARGET_ACCEPTS_NULL : 0;
            flags |= leftTerm.getSmall() && !rightTerm.getSmall() ? EXPR_UNSAFE_PRIMITIVE_TYPECAST_OK : 0;
            if (decl.isMember()
                    && useFieldInAssignment(op, null, decl)
                    && !ModelUtil.isLocalToInitializer(decl)
                    && useJavaBox(decl, ((TypedDeclaration)decl.getRefinedDeclaration()).getType())) {
                boxing = BoxingStrategy.JAVA;
                flags |= EXPR_HAS_NULL_CHECK_FENCE;
            }
            rhs = transformExpression(rightTerm, boxing, targetType, flags);
        } else if (leftTerm instanceof Tree.IndexExpression) {
            Tree.IndexExpression idx = (Tree.IndexExpression)leftTerm;
            Unit unit = op.getUnit();
            Type pt = idx.getPrimary().getTypeModel();
            boxing = (unit.isJavaPrimitiveArrayType(pt)) ? BoxingStrategy.UNBOXED : BoxingStrategy.BOXED;
            rhs = transformExpression(rightTerm, boxing, idx.getTypeModel(), 0);
        } else if (leftTerm instanceof Tree.ParameterizedExpression) {
            boxing = CodegenUtil.getBoxingStrategy(leftTerm);
            Tree.ParameterizedExpression paramExpr = (Tree.ParameterizedExpression)leftTerm;
            FunctionOrValue decl = (FunctionOrValue) ((Tree.MemberOrTypeExpression)paramExpr.getPrimary()).getDeclaration();
            CallableBuilder callableBuilder = CallableBuilder.anonymous(
                    gen(),
                    paramExpr,
                    decl,
                    (Tree.Expression)rightTerm,
                    paramExpr.getParameterLists(),
                    paramExpr.getPrimary().getTypeModel(),
                    decl instanceof Function ? !((Function)decl).isDeferred() : false);
            rhs = callableBuilder.build();
        } else {
            return makeErroneous(leftTerm, "compiler bug: left term of type '" + leftTerm.getClass().getSimpleName() + "' is not yet supported");
        }

        if (tmpInStatement) {
            return makeAssignment(op, leftTerm, transformAssignmentLhs(op, leftTerm), rhs);
        } else {
            Type valueType = rightTerm.getTypeModel();
            if(isNull(valueType))
                valueType = leftTerm.getTypeModel();
            return transformAssignAndReturnOperation(op, leftTerm, boxing == BoxingStrategy.BOXED, 
                    leftTerm.getTypeModel(), valueType, new AssignAndReturnOperationFactory(){
                @Override
                public JCExpression getNewValue(JCExpression previousValue) {
                    return rhs;
                }
            });
        }
    }
    
    private JCExpression transformAssignmentLhs(final Node op, Tree.Term leftTerm) {
        // left hand side can be either BaseMemberExpression, QualifiedMemberExpression or array access (M2)
        // TODO: array access (M2)
        JCExpression lhs = null;
        if(leftTerm instanceof Tree.BaseMemberExpression) {
            if (needDollarThis((Tree.BaseMemberExpression)leftTerm)) {
                lhs = naming.makeQuotedThis();
            }
        } else if(leftTerm instanceof Tree.QualifiedMemberExpression) {
            Tree.QualifiedMemberExpression qualified = ((Tree.QualifiedMemberExpression)leftTerm);
            Tree.Primary primary = qualified.getPrimary();
            if (isPackageQualified(qualified)) {
                lhs = null;
            } else if (isSuper(primary)) {
                lhs = transformSuper(qualified);
            } else {
                Declaration qualifiedDec = qualified.getDeclaration();
                if (isSuperOf(primary)) {
                    lhs = transformSuperOf(qualified, primary, qualifiedDec.getName());
                } else if (isThis(primary)
                        && !qualifiedDec.isCaptured() 
                        && !qualifiedDec.isShared() ) {
                    lhs = null;
                } else if (!qualifiedDec.isStatic()) {
                    lhs = transformExpression(primary, BoxingStrategy.BOXED, qualified.getTarget().getQualifyingType());
                    if (Decl.isPrivateAccessRequiringUpcast(qualified)) {
                        lhs = makePrivateAccessUpcast(qualified, lhs);
                    }
                } else {
                    lhs = makeJavaType(((ClassOrInterface)qualifiedDec.getContainer()).getType(), JT_RAW);
                }
            }
        } else if(leftTerm instanceof Tree.ParameterizedExpression) {
            lhs = null;
        } else if(leftTerm instanceof Tree.IndexExpression) {
            lhs = null;
        } else {
            return makeErroneous(op, "compiler bug: "+op.getNodeType() + " is not yet supported");
        }
        
        return qualifyLhs(op, leftTerm, lhs);
    }

    protected JCExpression qualifyLhs(final Node op, Tree.Term leftTerm, JCExpression lhs) {
        TypedDeclaration decl;
        if (leftTerm instanceof Tree.StaticMemberOrTypeExpression) {
            decl = (TypedDeclaration) ((Tree.StaticMemberOrTypeExpression)leftTerm).getDeclaration();
            lhs = addInterfaceImplAccessorIfRequired(lhs, (Tree.StaticMemberOrTypeExpression) leftTerm, decl);
            lhs = addThisOrObjectQualifierIfRequired(lhs, (Tree.StaticMemberOrTypeExpression)leftTerm, decl);
        } else if (leftTerm instanceof Tree.IndexExpression) {
            // in this case lhs is null anyway, so let's discard it
            return lhs;
        } else if (leftTerm instanceof Tree.ParameterizedExpression) {
            // instanceof Tree.ParameterizedExpression
            decl = (TypedDeclaration) ((Tree.MemberOrTypeExpression)((Tree.ParameterizedExpression)leftTerm).getPrimary()).getDeclaration();
        } else {
            return makeErroneous(op, "Unexpected LHS in assignment: " + leftTerm.getNodeType());
        }
        if (decl.isToplevel()) {
            // must use top level setter
            lhs = naming.makeName(decl, Naming.NA_FQ | Naming.NA_WRAPPER);
        } else if (Decl.isGetter(decl)) {
            if (Decl.isTransient(decl) && !decl.isVariable()) {
                
            } else {
                // must use the setter
                if (Decl.isLocal(decl)) {
                    lhs = naming.makeQualifiedName(lhs, decl, Naming.NA_WRAPPER | Naming.NA_SETTER);
                } else if (decl.isStatic()) {
                    lhs = naming.makeTypeDeclarationExpression(null, (TypeDeclaration)decl.getContainer(), DeclNameFlag.QUALIFIED);
                }
            }
        } else if (decl instanceof Function && Decl.isDeferred(decl)) {
        } else if ((decl.isVariable() || decl.isLate()) && (Decl.isClassAttribute(decl))) {
        } else if (decl.isVariable() && ModelUtil.isCaptured(decl)) {
            // must use the qualified setter
            if (Decl.isBoxedVariable(decl)) {
                
            } else if (ModelUtil.isLocalNotInitializer(decl)) {
                lhs = naming.makeQualifiedName(lhs, decl, Naming.NA_WRAPPER);
            }
        }
        return lhs;
    }
    
    private JCExpression adjustRhs(TypedDeclaration decl, JCExpression rhs) {
        if (decl.isToplevel()) {
        } else if (Decl.isGetter(decl)) {
            if (Decl.isTransient(decl) && !decl.isVariable()) {
                rhs = gen().transformAttributeGetter(decl, rhs);
            }
        }
        return rhs;
    }
    
    private JCExpression makeAssignment(Node op, Tree.Term leftTerm, 
            final JCExpression lhs, 
            JCExpression rhs) {
        TypedDeclaration decl;
        if (leftTerm instanceof Tree.StaticMemberOrTypeExpression) {
            decl = (TypedDeclaration) ((Tree.StaticMemberOrTypeExpression)leftTerm).getDeclaration();
        } else if (leftTerm instanceof Tree.IndexExpression) {
            // in this case lhs is null anyway, so let's discard it
            return transformIndexAssignment(op, (Tree.IndexExpression)leftTerm, rhs);
        } else if (leftTerm instanceof Tree.ParameterizedExpression) {
            // instanceof Tree.ParameterizedExpression
            decl = (TypedDeclaration) ((Tree.MemberOrTypeExpression)((Tree.ParameterizedExpression)leftTerm).getPrimary()).getDeclaration();
        } else {
            return makeErroneous(op, "Unexpected LHS in assignment: " + leftTerm.getNodeType());
        }
        rhs = adjustRhs(decl, rhs);
        
        JCExpression varExpr = makeAssignmentVariable(op, lhs, decl);
        
        JCExpression result;
        if (varExpr != null) {
            result = at(op).Assign(varExpr, rhs);
        } else {
            ListBuffer<JCExpression> typeArguments =  new ListBuffer<JCExpression>();
            ListBuffer<JCExpression> reifiedTypeArguments =  new ListBuffer<JCExpression>();
            if (decl.isStatic()
                    && ModelUtil.isCeylonDeclaration(decl)) {
                Type primType = ((Tree.StaticMemberOrTypeExpression)leftTerm).getTarget().getQualifyingType();
                if (primType != null) {
                    for (Type pt : primType.getTypeArgumentList()) {
                        typeArguments.add(makeJavaType(pt, JT_TYPE_ARGUMENT));
                        reifiedTypeArguments.add(makeReifiedTypeArgument(pt));
                    }
                }
            }
            String selector = Naming.selector(decl, Naming.NA_SETTER);
            result = make().Apply(typeArguments.toList(),
                    makeQualIdent(lhs, selector),
                    reifiedTypeArguments.toList().append(rhs));
        }
        
        return result;
    }

    protected JCExpression makeAssignmentVariable(Node op, final JCExpression lhs, TypedDeclaration decl) {
        JCExpression varExpr = null;
        at(op);
        if (decl.isToplevel()) {
        } else if (Decl.isGetter(decl)) {
            if (Decl.isTransient(decl) && !decl.isVariable()) {
                varExpr = naming.makeQualifiedName(lhs, decl, Naming.NA_WRAPPER);
            }
        } else if (decl instanceof Function && Decl.isDeferred(decl)) {
            if (Decl.isLocal(decl)) {
                // Deferred method initialization of a local function
                // The Callable field has the same name as the method, so use NA_MEMBER
                varExpr = naming.makeQualifiedName(lhs, decl, Naming.NA_WRAPPER_UNQUOTED | Naming.NA_MEMBER);
            } else {
                // Deferred method initialization of a class function
                varExpr = naming.makeQualifiedName(lhs, decl, Naming.NA_MEMBER);
            }
        } else if ((decl.isVariable() || decl.isLate()) && (Decl.isClassAttribute(decl))) {
            // must use the setter, nothing to do, unless it's a java field
            if(Decl.isJavaField(decl)){
                if (decl.isStatic()) {
                    // static field
                    varExpr = naming.makeName(decl, Naming.NA_FQ | Naming.NA_WRAPPER_UNQUOTED);
                }else{
                    // normal field
                    varExpr = naming.makeQualifiedName(lhs, decl, Naming.NA_IDENT);
                }
            }
        } else if (decl.isVariable() && ModelUtil.isCaptured(decl)) {
            // must use the qualified setter
            if (Decl.isBoxedVariable(decl)) {
                varExpr = naming.makeName(decl, Naming.NA_Q_LOCAL_INSTANCE | Naming.NA_MEMBER | Naming.NA_SETTER);
            } else if (ModelUtil.isLocalNotInitializer(decl)) {
                
            } else if (isWithinSuperInvocation()
                    && decl.isCaptured()
                    && decl.isVariable()) {
                varExpr = naming.makeUnquotedIdent(Naming.getAliasedParameterName(((Value)decl).getInitializerParameter()));
            }
        } else {
            varExpr = naming.makeQualifiedName(lhs, decl, Naming.NA_IDENT);
        }
        return varExpr;
    }
    
    protected boolean useFieldInAssignment(Node op, final JCExpression lhs, TypedDeclaration decl) {
        at(op);
        if (decl.isToplevel()) {
        } else if (Decl.isGetter(decl)) {
            if (Decl.isTransient(decl) && !decl.isVariable()) {
                return true;
            }
        } else if (decl instanceof Function && Decl.isDeferred(decl)) {
            if (Decl.isLocal(decl)) {
                return true;
            } else {
                return true;
            }
        } else if ((decl.isVariable() || decl.isLate()) && (Decl.isClassAttribute(decl))) {
            // must use the setter, nothing to do, unless it's a java field
            if(Decl.isJavaField(decl)){
                if (decl.isStatic()) {
                    // static field
                    return true;
                }else{
                    // normal field
                    return true;
                }
            }
        } else if (decl.isVariable() && ModelUtil.isCaptured(decl)) {
            // must use the qualified setter
            if (Decl.isBoxedVariable(decl)) {
                return false;
            } else if (ModelUtil.isLocalNotInitializer(decl)) {
                
            } else if (isWithinSuperInvocation()
                    && decl.isCaptured()
                    && decl.isVariable()) {
                return true;
            }
        } else {
            return true;
        }
        return false;
    }

    private JCExpression transformIndexAssignment(Node op, Tree.IndexExpression idx, JCExpression rhs) {
        JCExpression result = null;
        if (idx.getElementOrRange() instanceof Tree.Element) {
            Unit unit = op.getUnit();
            Type pt = idx.getPrimary().getTypeModel();
            Tree.Element elem = (Tree.Element)idx.getElementOrRange();
            Type primaryType;
            if ((primaryType = pt.getSupertype(unit.getIndexedCorrespondenceMutatorDeclaration())) != null) {
                JCExpression iex = transformExpression(elem.getExpression(), BoxingStrategy.UNBOXED, elem.getExpression().getTypeModel());
                result = makeIndexAssignMethod(idx, "set", iex, rhs, primaryType);
            } else if ((primaryType = pt.getSupertype(unit.getKeyedCorrespondenceMutatorDeclaration())) != null) {
                    JCExpression iex = transformExpression(elem.getExpression(), BoxingStrategy.BOXED, elem.getExpression().getTypeModel());
                    result = makeIndexAssignMethod(idx, "put", iex, rhs, primaryType);
            } else if ((primaryType = pt.getSupertype(unit.getJavaListDeclaration())) != null) {
                JCExpression iex = transformExpression(elem.getExpression(), BoxingStrategy.UNBOXED, elem.getExpression().getTypeModel());
                if (!elem.getExpression().getSmall()) {
                    iex = utilInvocation().toInt(iex);
                }
                result = makeIndexAssignMethod(idx, "set", iex, rhs, primaryType);
            } else if ((primaryType = pt.getSupertype(unit.getJavaMapDeclaration())) != null) {
                JCExpression iex = transformExpression(elem.getExpression(), BoxingStrategy.BOXED, elem.getExpression().getTypeModel());
                result = makeIndexAssignMethod(idx, "put", iex, rhs, primaryType);
            } else if ((primaryType = pt.getSupertype(unit.getJavaObjectArrayDeclaration())) != null 
                    || (primaryType = pt.getSupertype(unit.getJavaIntArrayDeclaration())) != null
                    || (primaryType = pt.getSupertype(unit.getJavaShortArrayDeclaration())) != null
                    || (primaryType = pt.getSupertype(unit.getJavaLongArrayDeclaration())) != null
                    || (primaryType = pt.getSupertype(unit.getJavaByteArrayDeclaration())) != null
                    || (primaryType = pt.getSupertype(unit.getJavaCharArrayDeclaration())) != null
                    || (primaryType = pt.getSupertype(unit.getJavaBooleanArrayDeclaration())) != null
                    || (primaryType = pt.getSupertype(unit.getJavaFloatArrayDeclaration())) != null
                    || (primaryType = pt.getSupertype(unit.getJavaDoubleArrayDeclaration())) != null
                    ) {
                JCExpression lhs = transformExpression(idx.getPrimary(), BoxingStrategy.BOXED, primaryType);
                JCExpression iex = transformExpression(elem.getExpression(), BoxingStrategy.UNBOXED, elem.getExpression().getTypeModel());
                if (!elem.getExpression().getSmall()) {
                    iex = utilInvocation().toInt(iex);
                }
                return at(op).Assign(make().Indexed(lhs, iex), rhs);
            } else {
                return makeErroneous(idx, "compiler bug: index assignment for type '" + pt + "' is not supported");
            }
            return result;
        } else {
            return makeErroneous(idx, "compiler bug: ranged index assignment is not supported");
        }
    }
    
    private JCExpression makeIndexAssignMethod(Tree.IndexExpression leftTerm, String method, JCExpression index, JCExpression rhs, Type expectedPrimaryType) {
        JCExpression lhs = transformExpression(leftTerm.getPrimary(), BoxingStrategy.BOXED, expectedPrimaryType);
        return make().Apply(List.<JCTree.JCExpression>nil(),
                makeQualIdent(lhs, method),
                List.<JCTree.JCExpression>of(index, rhs));
    }
    
    /** Creates an anonymous class that extends Iterable and implements the specified comprehension.
     */
    public JCExpression transformComprehension(Tree.Comprehension comp) {
        return transformComprehension(comp, null);
    }

    JCExpression transformComprehension(Tree.Comprehension comp, Type expectedType) {
        Type elementType = comp.getInitialComprehensionClause().getTypeModel();
        // get rid of anonymous types
        elementType = typeFact().denotableType(elementType);
        elementType = wrapInOptionalForInterop(elementType, expectedType, containsUncheckedNulls(comp));
        return new ComprehensionTransformation(comp, elementType).transformComprehension();
    }

    private Type wrapInOptionalForInterop(Type elementType, Type expectedType, boolean containsUncheckedNull) {
        if(expectedType != null && containsUncheckedNull && iteratesOverOptional(expectedType) && !typeFact().isOptionalType(elementType))
            return typeFact().getOptionalType(elementType);
        return elementType;
    }

    private boolean iteratesOverOptional(Type iterableType) {
        Type seqElemType = typeFact().getIteratedType(iterableType);
        return isOptional(seqElemType);
    }
    
    enum IterType {
        CEYLON_ITERABLE {
            @Override
            JCExpression makeIteratorType(ExpressionTransformer gen, Type iterType) {
                return gen.makeJavaType(gen.typeFact().getIteratorType(
                        gen.typeFact().getIteratedType(iterType)));
            }
            @Override
            ListBuffer<JCStatement> makeContext(ExpressionTransformer gen, ComprehensionTransformation ct,
                    Tree.ForIterator forIterator,
                    SyntheticName iterVar, SyntheticName itemVar, SyntheticName tmpItem, ListBuffer<JCStatement> elseBody) {
                ListBuffer<JCStatement> contextBody = new ListBuffer<JCStatement>();
              //Assign the next item to an Object variable
                contextBody.add(gen.make().VarDef(gen.make().Modifiers(Flags.FINAL), tmpItem.asName(),
                        gen.makeJavaType(gen.typeFact().getObjectType()),
                        gen.make().Apply(null, gen.makeSelect(iterVar.makeIdent(), "next"), 
                                List.<JCExpression>nil())));
                //Then we check if it's exhausted
                contextBody.add(gen.make().Exec(gen.make().Assign(itemVar.suffixedBy(Suffix.$exhausted$).makeIdent(),
                        gen.make().Binary(JCTree.Tag.EQ, tmpItem.makeIdent(), gen.makeFinished()))));
                ListBuffer<JCStatement> innerBody = new ListBuffer<JCStatement>();
                if (ct.idx>0) {
                    //Subsequent contexts run once for every iteration of the previous loop
                    //This will reset our previous context by getting a new iterator if the previous loop isn't done
                    innerBody.add(gen.make().Exec(gen.make().Assign(iterVar.makeIdent(), gen.makeNull())));
                }else{
                    innerBody.add(gen.make().Return(gen.makeBoolean(false)));
                }
                //Assign the next item to the corresponding variables if not exhausted yet
                contextBody.add(gen.make().If(itemVar.suffixedBy(Suffix.$exhausted$).makeIdent(),
                        gen.make().Block(0, innerBody.toList()),
                        gen.make().Block(0, elseBody.toList())));
                return contextBody;
            }
        },
        JAVA_ITERABLE {
            @Override
            JCExpression makeIteratorType(ExpressionTransformer gen, Type iterType) {
                return gen.makeJavaType(gen.typeFact().getJavaIteratorType(
                        gen.typeFact().getJavaIteratedType(iterType)));
            }

            @Override
            ListBuffer<JCStatement> makeContext(ExpressionTransformer gen, ComprehensionTransformation ct,
                    Tree.ForIterator forIterator, 
                    SyntheticName iterVar, SyntheticName itemVar, SyntheticName tmpItem,
                    ListBuffer<JCStatement> elseBody) {
                ListBuffer<JCStatement> contextBody = new ListBuffer<JCStatement>();
                contextBody.add(gen.make().Exec(gen.make().Assign(itemVar.suffixedBy(Suffix.$exhausted$).makeIdent(),
                        gen.make().Unary(JCTree.Tag.NOT, gen.make().Apply(null, gen.makeSelect(iterVar.makeIdent(), "hasNext"), 
                                List.<JCExpression>nil())))));
                
                ListBuffer<JCStatement> innerBody = new ListBuffer<JCStatement>();
                if (ct.idx > 0) {
                    innerBody.add(gen.make().Exec(gen.make().Assign(iterVar.makeIdent(), gen.makeNull())));
                } else {
                    innerBody.add(gen.make().Return(gen.makeBoolean(false)));
                }
                
                JCExpression nextInvocation = gen.make().Apply(null, 
                        gen.makeSelect(iterVar.makeIdent(), "next"),
                        List.<JCExpression>nil());
                if (gen.statementGen().requiresNullCheck(forIterator)) {
                    nextInvocation = gen.utilInvocation().checkNull(nextInvocation);
                }
                
                contextBody.add(
                        gen.make().If(
                                itemVar.suffixedBy(Suffix.$exhausted$).makeIdent(), 
                                gen.make().Block(0, innerBody.toList()),
                                gen.make().Block(0,
                                        List.<JCStatement>of(
                                                gen.make().Exec(gen.make().Assign(itemVar.makeIdent(),
                                                        nextInvocation)),
                                                gen.make().Return(gen.makeBoolean(true))))));
                return contextBody;
            }
        },
        JAVA_ARRAY {
            @Override
            void makeInitial(ExpressionTransformer gen, ComprehensionTransformation ct, SyntheticName iterVar, Type iterType, JCExpression iterableExpr) {
                JCExpression iterTypeExpr = makeIteratorType(gen, iterType);
                SyntheticName arrayVar = arrayVar(gen, ct);
                ct.fields.add(gen.make().VarDef(gen.make().Modifiers(Flags.PRIVATE), iterVar.asName(), gen.make().Type(gen.syms().intType),
                        null));
                ct.fields.add(gen.make().VarDef(gen.make().Modifiers(Flags.PRIVATE | Flags.FINAL), arrayVar.asName(), iterTypeExpr,
                        iterableExpr));
                ct.initIterator = gen.make().Exec(gen.make().Assign(iterVar.makeIdent(), gen.make().Literal(0)));
            }

            protected SyntheticName arrayVar(ExpressionTransformer gen, ComprehensionTransformation ct) {
                return gen.naming.synthetic(Prefix.$array$, ct.idx);
            }
            
            @Override 
            ListBuffer<JCStatement> makeSubsequent(ExpressionTransformer gen, 
                    ComprehensionTransformation ct,
                    Type iterType, 
                    SyntheticName iterVar,
                    JCExpression iterableExpr) {
                SyntheticName arrayVar = arrayVar(gen, ct);
                ListBuffer<JCStatement> block = new ListBuffer<JCStatement>();
                ct.fields.add(gen.make().VarDef(gen.make().Modifiers(Flags.PRIVATE), iterVar.asName(), gen.make().Type(gen.syms().intType),
                        null));
                ct.fields.add(gen.make().VarDef(gen.make().Modifiers(Flags.PRIVATE), arrayVar.asName(), makeIteratorType(gen, iterType),
                        null));
                
                block.appendList(List.<JCStatement>of(
                        gen.make().If(gen.make().Binary(JCTree.Tag.NE, arrayVar.makeIdent(), gen.makeNull()),
                                gen.make().Return(gen.makeBoolean(true)),
                                null),
                        gen.make().If(gen.make().Unary(JCTree.Tag.NOT, gen.make().Apply(null, ct.ctxtName.makeIdentWithThis(), List.<JCExpression>nil())),
                                gen.make().Return(gen.makeBoolean(false)),
                                null)));
                block.appendList(ct.capture());
                block.appendList(List.<JCStatement>of(
                        gen.make().Exec(gen.make().Assign(arrayVar.makeIdent(), 
                                iterableExpr)),
                        gen.make().Exec(gen.make().Assign(iterVar.makeIdent(), gen.make().Literal(0))),
                        gen.make().Return(gen.makeBoolean(true))));
                return block;
            }

            @Override
            JCExpression makeIteratorType(ExpressionTransformer gen, Type iterType) {
                return gen.makeJavaType(iterType, JT_NO_PRIMITIVES);
            }

            @Override
            ListBuffer<JCStatement> makeContext(ExpressionTransformer gen, ComprehensionTransformation ct,
                    Tree.ForIterator forIterator,
                    SyntheticName iterVar, SyntheticName itemVar, SyntheticName tmpItem,
                    ListBuffer<JCStatement> elseBody) {
                ListBuffer<JCStatement> contextBody = new ListBuffer<JCStatement>();
                Tree.SpecifierExpression specexpr = forIterator.getSpecifierExpression();
                Type iterType = specexpr.getExpression().getTypeModel();
                SyntheticName arrayVar = arrayVar(gen, ct);
                contextBody.add(gen.make().Exec(gen.make().Assign(itemVar.suffixedBy(Suffix.$exhausted$).makeIdent(),
                        gen.make().Binary(JCTree.Tag.GE, iterVar.makeIdent(), gen.naming.makeSelect(arrayVar.makeIdent(), "length")))));
                
                ListBuffer<JCStatement> innerBody = new ListBuffer<JCStatement>();
                if (ct.idx > 0) {
                    innerBody.add(gen.make().Exec(gen.make().Assign(arrayVar.makeIdent(), gen.makeNull())));
                } else {
                    innerBody.add(gen.make().Return(gen.makeBoolean(false)));
                }
                
                JCExpression indexed = gen.make().Indexed(arrayVar.makeIdent(), 
                        gen.make().Unary(JCTree.Tag.POSTINC, iterVar.makeIdent()));

                if (gen.statementGen().requiresNullCheck(forIterator)) {
                    indexed = gen.utilInvocation().checkNull(indexed);
                }
                Value varModel = ((Tree.ValueIterator) forIterator).getVariable().getDeclarationModel();
                indexed = gen.applyErasureAndBoxing(indexed,
                        gen.typeFact().getJavaArrayElementType(iterType),
                        gen.typeFact().getJavaObjectArrayDeclaration().equals(iterType.resolveAliases().getDeclaration()),
                        CodegenUtil.getBoxingStrategy(varModel),
                        varModel.getType());
                
                
                contextBody.add(
                        gen.make().If(
                                itemVar.suffixedBy(Suffix.$exhausted$).makeIdent(), 
                                gen.make().Block(0, innerBody.toList()),
                                gen.make().Block(0,
                                        List.<JCStatement>of(
                                                gen.make().Exec(gen.make().Assign(itemVar.makeIdent(),
                                                indexed)),
                                                gen.make().Return(gen.makeBoolean(true))))));
                return contextBody;
            }
        };
        
        /** Make the type expression for the Iterator */
        abstract JCExpression makeIteratorType(ExpressionTransformer gen, Type iterType);
        
        /**
         * Transform the initial iterator, declaring and initializing fields within the 
         * given {@code ct}.
         */
        void makeInitial(ExpressionTransformer gen, 
                ComprehensionTransformation ct, SyntheticName iterVar, 
                Type iterType, JCExpression iterableExpr) {
            JCExpression iterTypeExpr = makeIteratorType(gen, iterType);
            ct.fields.add(gen.make().VarDef(gen.make().Modifiers(Flags.PRIVATE | Flags.FINAL), iterVar.asName(), iterTypeExpr,
                    null));
            ct.initIterator = gen.make().Exec(gen.make().Assign(iterVar.makeIdent(), gen.make().Apply(null, gen.makeSelect(iterableExpr, "iterator"), 
                    List.<JCExpression>nil())));
        }
        
        /**
         * Transform the non-initial iterator, declaring fields within the 
         * given {@code ct}.
         */
        ListBuffer<JCStatement> makeSubsequent(ExpressionTransformer gen, 
                ComprehensionTransformation ct,
                Type iterType, SyntheticName iterVar,
                JCExpression iterableExpr) {
            ListBuffer<JCStatement> block = new ListBuffer<JCStatement>();
            ct.fields.add(gen.make().VarDef(gen.make().Modifiers(Flags.PRIVATE), iterVar.asName(), makeIteratorType(gen, iterType), null));
            block.appendList(List.<JCStatement>of(
                    gen.make().If(gen.make().Binary(JCTree.Tag.NE, iterVar.makeIdent(), gen.makeNull()),
                            gen.make().Return(gen.makeBoolean(true)),
                            null),
                    gen.make().If(gen.make().Unary(JCTree.Tag.NOT, gen.make().Apply(null, ct.ctxtName.makeIdentWithThis(), List.<JCExpression>nil())),
                            gen.make().Return(gen.makeBoolean(false)),
                            null)));
            block.appendList(ct.capture());
            block.appendList(List.<JCStatement>of(
                    gen.make().Exec(gen.make().Assign(iterVar.makeIdent(), 
                            gen.make().Apply(null,
                                    gen.makeSelect(iterableExpr, "iterator"), 
                                   List.<JCExpression>nil()))),
                    gen.make().Return(gen.makeBoolean(true))
            ));
            return block;
        }
        
        abstract ListBuffer<JCStatement> makeContext(ExpressionTransformer gen, ComprehensionTransformation ct, 
                Tree.ForIterator forIterator, SyntheticName iterVar, SyntheticName itemVar, SyntheticName tmpItem, ListBuffer<JCStatement> elseBody);
    }
    
    class ComprehensionTransformation {
        private final Tree.Comprehension comp;
        final Type targetIterType;
        final Type absentIterType;
        int idx = 0;
        Tree.ExpressionComprehensionClause excc = null;
        Naming.SyntheticName prevItemVar = null;
        Naming.SyntheticName ctxtName = null;
        Naming.SyntheticName lastIteratorCtxtName = null;
        //Iterator fields
        final ListBuffer<JCTree> fields = new ListBuffer<JCTree>();
        final ListBuffer<Substitution> fieldSubst = new ListBuffer<Substitution>();
        private JCExpression error;
        private JCStatement initIterator;
        // A list of variable declarations local to next(), $next$N() 
        // and $iterator$N() methods so that
        // the variable captured by whatever gets transformed there holds the value
        // at *that point* on the iteration, and not the (variable) value of 
        // the iterator. See #986, #2304
        private ListBuffer<VarDefBuilder> valueCaptures = new ListBuffer<VarDefBuilder>();
        public ComprehensionTransformation(final Tree.Comprehension comp, Type elementType) {
            this.comp = comp;
            targetIterType = typeFact().getIterableType(elementType);
            absentIterType = comp.getInitialComprehensionClause().getFirstTypeModel();
        }
    
        public JCExpression transformComprehension() {
            at(comp);
            // make sure "this" will be qualified since we're introducing a new surrounding class
            boolean oldWithinSyntheticClassBody = withinSyntheticClassBody(true);
            try{
                Tree.ComprehensionClause clause = comp.getInitialComprehensionClause();
                while (clause != null) {
                    final Naming.SyntheticName iterVar = naming.synthetic(Prefix.$iterator$, idx);
                    Naming.SyntheticName itemVar = null;
                    if (clause instanceof Tree.ForComprehensionClause) {
                        final Tree.ForComprehensionClause fcl = (Tree.ForComprehensionClause)clause;
                        itemVar = transformForClause(fcl, iterVar);
                        if (error != null) {
                            return error;
                        }
                        clause = fcl.getComprehensionClause();
                    } else if (clause instanceof Tree.IfComprehensionClause) {
                        transformIfClause((Tree.IfComprehensionClause)clause);
                        if (error != null) {
                            return error;
                        }
                        clause = ((Tree.IfComprehensionClause)clause).getComprehensionClause();
                        itemVar = prevItemVar;
                    } else if (clause instanceof Tree.ExpressionComprehensionClause) {
                        //Just keep a reference to the expression
                        excc = (Tree.ExpressionComprehensionClause)clause;
                        at(excc);
                        clause = null;
                    } else {
                        return makeErroneous(clause, "compiler bug: comprehension clauses of type " + clause.getClass().getName() + " are not yet supported");
                    }
                    idx++;
                    if (itemVar != null) prevItemVar = itemVar;
                }

                Type iteratedType = typeFact().getIteratedType(targetIterType);

                //Define the next() method for the Iterator
                fields.add(makeNextMethod(iteratedType));
                //Define the inner iterator class

                JCMethodDecl getIterator = makeGetIterator(iteratedType);
                JCExpression iterable = makeAnonymousIterable(iteratedType, getIterator);
                for (Substitution subs : fieldSubst) {
                    subs.close();
                }
                return iterable;
            }finally{
                withinSyntheticClassBody(oldWithinSyntheticClassBody);
            }
        }
        
        List<JCStatement> capture() {
            List<JCStatement> result = List.<JCStatement>nil();
            for (VarDefBuilder var : valueCaptures) {
                // reverse order, but who cares?
                result = result.prepend(var.buildFromField());
            }
            return result;
        }
        
        /**
         * Builds the {@code next()} method of the {@code AbstractIterator}
         */
        private JCMethodDecl makeNextMethod(Type iteratedType) {
            List<JCStatement> of = List.<JCStatement>of(make().Return(transformExpression(excc.getExpression(), BoxingStrategy.BOXED, iteratedType)));
            of = of.prependList(capture());
            JCStatement stmt = make().If(
                    make().Apply(null,
                        ctxtName.makeIdentWithThis(), List.<JCExpression>nil()),
                    make().Block(0, of),
                    make().Return(makeFinished()));
            return make().MethodDef(make().Modifiers(Flags.PUBLIC | Flags.FINAL), names().fromString("next"),
                makeJavaType(typeFact().getObjectType()), List.<JCTree.JCTypeParameter>nil(),
                List.<JCTree.JCVariableDecl>nil(), List.<JCExpression>nil(), make().Block(0, List.<JCStatement>of(stmt)), null);
        }
        /**
         * Builds a {@code getIterator()} method which contains a local class 
         * extending {@code AbstractIterator} and initialises the iter$0 field
         * to a new instance of that local class.
         * 
         * Doesn't use an anonymous class due to #974.
         * @param iteratedType
         * @return
         */
        private JCMethodDecl makeGetIterator(Type iteratedType) {
            Type iteratorType = typeFact().getIteratorType(iteratedType);
            JCExpression iteratorTypeExpr = make().TypeApply(makeIdent(syms().ceylonAbstractIteratorType),
                    List.<JCExpression>of(makeJavaType(iteratedType, JT_TYPE_ARGUMENT)));
            JCExpression iterator = at(comp).NewClass(null, List.<JCExpression>nil(), iteratorTypeExpr, 
                    List.<JCExpression>of(makeReifiedTypeArgument(iteratedType)), 
                    make().AnonymousClassDef(make().Modifiers(0), 
                            fields.toList().prepend(
                                    make().Block(0L,
                                            initIterator == null ? List.<JCStatement>nil() : List.<JCStatement>of(initIterator)) 
                                    )));
            JCBlock iteratorBlock = make().Block(0, List.<JCStatement>of(
                    make().Return(iterator)));
            return make().MethodDef(make().Modifiers(Flags.PUBLIC | Flags.FINAL), names().fromString("iterator"),
                    makeJavaType(iteratorType, JT_CLASS_NEW|JT_EXTENDS),
                List.<JCTree.JCTypeParameter>nil(), List.<JCTree.JCVariableDecl>nil(), List.<JCExpression>nil(),
                iteratorBlock, null);
        }
        /**
         * Builds an anonymous subclass of AbstractIterable whose 
         * {@code getIterator()} uses the given getIteratorBody.
         * @param iteratedType
         * @param iteratorType
         * @param getIteratorBody
         * @return
         */
        private JCExpression makeAnonymousIterable(Type iteratedType,
                JCMethodDecl getIterator) {
            JCExpression iterable = make().NewClass(null, null,
                    make().TypeApply(makeIdent(syms().ceylonAbstractIterableType),
                        List.<JCExpression>of(makeJavaType(iteratedType, JT_TYPE_ARGUMENT),
                                makeJavaType(absentIterType, JT_NO_PRIMITIVES))),
                                List.<JCExpression>of(makeReifiedTypeArgument(iteratedType), 
                                        makeReifiedTypeArgument(absentIterType)), 
                    make().AnonymousClassDef(make().Modifiers(0), 
                            List.<JCTree>of(getIterator)));
            return iterable;
        }

        class IfComprehensionCondList extends CondList {

            private final ListBuffer<JCStatement> varDecls = new ListBuffer<JCStatement>();
            /**
             * A list of statements that are placed in the main body, before the conditions.
             */
            private final List<JCStatement> preCheck;
            /**
             * A list of statements that are placed in the innermost condition's body.
             */
            private final List<JCStatement> insideCheck;
            /**
             * A list of statements that are placed in the main body, after the conditions.
             */
            private final List<JCStatement> postCheck;
            
            /**
             * An IfComprehensionCondList suitable for "inner" if comprehension clauses.
             * Checks {@code condExpr} before checking the {@code conditions}, and {@code break;}s if the conditions apply.
             * Intended to be placed in a {@code while (true) } loop, to keep checking the conditions until they apply
             * or {@code condExpr} doesn't.
             */
            public IfComprehensionCondList(
                    java.util.List<Tree.Condition> conditions, 
                    JCExpression condExpr, 
                    Name breakLabel) {
                this(conditions,
                    // check condExpr before the conditions
                        capture().prepend(make().If(make().Unary(JCTree.Tag.NOT, condExpr), make().Break(breakLabel), null)),
                    // break if a condition matches
                    List.<JCStatement>of(make().Break(breakLabel)),
                    null);
            }
            
            /**
             * General-purpose constructor. Places {@code precheck} before the conditions and their variable declarations,
             * {@code insideCheck} in the body of the innermost condition (executed only if all {@code conditions} apply), and
             * {@code postCheck} after the conditions.
             */
            public IfComprehensionCondList(java.util.List<Tree.Condition> conditions,
                    List<JCStatement> preCheck, List<JCStatement> insideCheck, List<JCStatement> postCheck) {
                statementGen().super(conditions, (Tree.Block)null);
                if(preCheck == null) preCheck = List.<JCStatement>nil();
                if(insideCheck == null) insideCheck = List.<JCStatement>nil();
                if(postCheck == null) postCheck = List.<JCStatement>nil();
                this.preCheck = preCheck;
                this.insideCheck = insideCheck;
                this.postCheck = postCheck;
            }

            @Override
            protected List<JCStatement> transformInnermost(Tree.Condition condition) {
                Cond transformedCond = getConditionTransformer(condition);
                // The innermost condition's test should be transformed before
                // variable substitution
                
                JCExpression test = transformedCond.makeTest();
                List<VarDefBuilder> vars = addVarSubs(transformedCond.getVarTrans());
                return transformCommon(transformedCond.getVarTrans(),
                        test,
                        insideCheck,
                        vars);
            }
            
            protected List<JCStatement> transformIntermediate(Tree.Condition condition, java.util.List<Tree.Condition> rest) {
                Cond transformedCond = getConditionTransformer(condition);
                JCExpression test = transformedCond.makeTest();
                List<VarDefBuilder> vars = addVarSubs(transformedCond.getVarTrans());
                return transformCommon(transformedCond.getVarTrans(), test, transformList(rest), vars);
            }

            private List<VarDefBuilder> addVarSubs(VarTrans vartrans) {
                if (vartrans.hasResultDecl()) {
                    List<VarDefBuilder> vars = vartrans.getVarDefBuilders();
                    for (VarDefBuilder v : vars) {
                        fieldSubst.add(v.alias());
                    }
                    return vars;
                }
                return null;
            }
            
            protected List<JCStatement> transformCommon(VarTrans vartrans, 
                    JCExpression test, List<JCStatement> stmts,
                    List<VarDefBuilder> vars) {
                
                List<JCStatement> decl = vartrans.makeTestVarDecl(0, true);
                if (!decl.isEmpty()) {
                    varDecls.appendList(decl);
                }
                if (vars != null) {
                    for (VarDefBuilder v : vars) {
                        fields.add(v.buildField());
                        valueCaptures.add(v);
                        stmts = stmts.prepend(make().Exec(v.buildAssign()));
                    }
                }
                stmts = List.<JCStatement>of(make().If(
                        test, 
                        make().Block(0, stmts), 
                        null));
                return stmts;
            }
            
            public List<JCStatement> getResult() {
                List<JCStatement> stmts = transformList(conditions);
                ListBuffer<JCStatement> result = new ListBuffer<JCStatement>();
                result.appendList(preCheck);
                result.appendList(varDecls);
                result.appendList(stmts);
                result.appendList(postCheck);
                return result.toList();   
            }

        }
        
        private void transformIfClause(Tree.IfComprehensionClause clause) {
            List<JCStatement> body;
            if (prevItemVar == null) {
                List<JCStatement> initBlock;
                if (clause == comp.getInitialComprehensionClause()) {
                    //No previous context
                    assert (ctxtName == null);
                    ctxtName = naming.synthetic(Prefix.$next$, idx);
                    //define a variable that records if the expression was already evaluated
                    SyntheticName exhaustedName = ctxtName.suffixedBy(Suffix.$exhausted$);
                    JCVariableDecl exhaustedDef = make().VarDef(make().Modifiers(Flags.PRIVATE),
                            exhaustedName.asName(), makeJavaType(typeFact().getBooleanType()), null);
                    fields.add(exhaustedDef);
                    JCStatement returnIfExhausted = make().If(exhaustedName.makeIdent(), make().Return(makeBoolean(false)), null);
                    JCStatement setExhaustedTrue = make().Exec(make().Assign(exhaustedName.makeIdent(), makeBoolean(true)));
                    initBlock =  List.<JCStatement>of(
                            //if we already evaluated the expression, return
                            returnIfExhausted,
                            //record that we will have evaluated the expression
                            setExhaustedTrue);
                } else {
                    assert (ctxtName != null);
                    JCStatement returnIfExhausted = make().If(
                            //if the previous comprehension is false or was already evaluated...
                            make().Unary(JCTree.Tag.NOT, make().Apply(null,
                                    ctxtName.makeIdentWithThis(), List.<JCExpression>nil())),
                            //return false
                            make().Return(makeBoolean(false)), null);
                    ctxtName = naming.synthetic(Prefix.$next$, idx);
                    initBlock = List.<JCStatement>of(returnIfExhausted);
                }
                
                JCStatement returnTrue = make().Return(makeBoolean(true));
                JCStatement returnFalse = make().Return(makeBoolean(false));
                
                body = new IfComprehensionCondList(clause.getConditionList().getConditions(),
                    initBlock,
                    List.<JCStatement>of(
                        //if the conditions apply: return true
                        returnTrue),
                    List.<JCStatement>of(
                        //the conditions did not apply: return false
                        returnFalse)).getResult();
            } else {
                //Filter contexts need to check if the previous context applies and then check the condition
                JCExpression condExpr = make().Apply(null,
                    ctxtName.makeIdentWithThis(), List.<JCExpression>nil());
                ctxtName = naming.synthetic(Prefix.$next$, idx);
                Name label = names().fromString("ifcomp_"+idx);
                IfComprehensionCondList ifComprehensionCondList = new IfComprehensionCondList(clause.getConditionList().getConditions(), condExpr, label);
                List<JCStatement> ifs = ifComprehensionCondList.getResult();
                JCStatement loop = make().Labelled(label, make().WhileLoop(makeBoolean(true), make().Block(0, ifs)));
                body = List.<JCStatement>of(loop,
                    make().Return(make().Unary(JCTree.Tag.NOT, prevItemVar.suffixedBy(Suffix.$exhausted$).makeIdent())));
            }
            MethodDefinitionBuilder mb = MethodDefinitionBuilder.systemMethod(ExpressionTransformer.this, ctxtName.getName())
                .ignoreModelAnnotations()
                .modifiers(Flags.PRIVATE | Flags.FINAL)
                .resultType(new TransformedType(makeJavaType(typeFact().getBooleanType())))
                .body(body);
            fields.add(mb.build());
        }
        
        private SyntheticName transformForClause(final Tree.ForComprehensionClause clause,
                final Naming.SyntheticName iterVar) {
            final Tree.ForComprehensionClause fcl = clause;
            Tree.SpecifierExpression specexpr = fcl.getForIterator().getSpecifierExpression();
            Type iterType = specexpr.getExpression().getTypeModel();
            IterType tx = null;
            
            if (typeFact().getIteratedType(iterType) != null) {
                tx = IterType.CEYLON_ITERABLE;
            } else if (typeFact().getJavaIteratedType(iterType) != null) {
                tx = IterType.JAVA_ITERABLE;
            } else if (typeFact().isJavaArrayType(iterType)) {
                tx = IterType.JAVA_ARRAY;
            }
            Type iterableType = iterType.getSupertype(typeFact().getIterableDeclaration());
            JCExpression iterableExpr = transformExpression(specexpr.getExpression(), BoxingStrategy.BOXED, iterableType);
            if (clause == comp.getInitialComprehensionClause()) {
                //The first iterator can be initialized as a field
                tx.makeInitial(ExpressionTransformer.this, this, iterVar, iterType, iterableExpr);
            } else {
                //The subsequent iterators need to be inside a method,
                //in case they depend on the current element of the previous iterator
                ListBuffer<JCStatement> block = new ListBuffer<JCStatement>();
                if (lastIteratorCtxtName != null) {
                    block.append(make().If(lastIteratorCtxtName.suffixedBy(Suffix.$exhausted$).makeIdent(),
                            make().Return(makeBoolean(false)),
                            null));
                }
                block.appendList(tx.makeSubsequent(ExpressionTransformer.this, this, iterType, iterVar, iterableExpr));
                JCBlock body = make().Block(0l, block.toList());

                fields.add(make().MethodDef(make().Modifiers(Flags.PRIVATE | Flags.FINAL),
                        iterVar.asName(), makeJavaType(typeFact().getBooleanType()), 
                        List.<JCTree.JCTypeParameter>nil(),
                        List.<JCTree.JCVariableDecl>nil(), List.<JCExpression>nil(), body, null));
            }
            Naming.SyntheticName tmpItem = naming.temp("item");
            List<VarDefBuilder> vdbs = List.nil();
            ListBuffer<JCStatement> elseBody = new ListBuffer<JCStatement>();
            Tree.ForIterator forIterator = fcl.getForIterator();
            Naming.SyntheticName itemVar = naming.synthetic(forIterator);
            if (forIterator instanceof Tree.ValueIterator) {
                Tree.Variable variable = ((Tree.ValueIterator) forIterator).getVariable();
                VarDefBuilder vdb = statementGen().transformVariable(variable, tmpItem.makeIdent());
                vdbs = vdbs.append(vdb);
            } else if (forIterator instanceof Tree.PatternIterator) {
                Tree.PatternIterator patIter = (Tree.PatternIterator)forIterator;
                Tree.Pattern pat = patIter.getPattern();
                vdbs = vdbs.appendList(statementGen().transformPattern(pat, tmpItem.makeIdent()));
            } else {
                error = makeErroneous(fcl, "compiler bug: iterators of type " + forIterator.getNodeType() + " not yet supported");
                return null;
            }
            for (VarDefBuilder vdb : vdbs) {
                valueCaptures.append(vdb);
                fields.add(vdb.buildField());
                elseBody.add(make().Exec(vdb.buildAssign()));
            }
            fields.add(make().VarDef(make().Modifiers(Flags.PRIVATE), itemVar.suffixedBy(Suffix.$exhausted$).asName(),
                    makeJavaType(typeFact().getBooleanType()), null));
            elseBody.add(make().Return(makeBoolean(true)));
            
            //Now the context for this iterator
            ListBuffer<JCStatement> contextBody = tx.makeContext(ExpressionTransformer.this, this, forIterator, iterVar, itemVar, tmpItem, elseBody);
            
            List<JCTree.JCStatement> methodBody;
            if (idx>0) {
                //Subsequent iterators may depend on the item from the previous loop so we make sure we have one
                methodBody = List.<JCStatement>of(make().WhileLoop(make().Apply(null, iterVar.makeIdentWithThis(), List.<JCExpression>nil()),
                        make().Block(0, contextBody.toList())));
                // It can happen that we never get into the body because the outer iterator is exhausted, if so, mark
                // this one exhausted too
                if (lastIteratorCtxtName != null) {
                    // FIXME: this may actually not be useful to check for exhaustion because in theory we can only get out
                    // of the loop because the previous supplier is exhausted.
                    methodBody = methodBody.append(make().If(lastIteratorCtxtName.suffixedBy(Suffix.$exhausted$).makeIdent(), 
                            make().Exec(make().Assign(itemVar.suffixedBy(Suffix.$exhausted$).makeIdent(), makeBoolean(true))), 
                            null));
                }else{
                    methodBody = methodBody.append(make().Exec(make().Assign(itemVar.suffixedBy(Suffix.$exhausted$).makeIdent(), makeBoolean(true))));
                }
                methodBody = methodBody.append(make().Return(makeBoolean(false)));
            }else
                methodBody = contextBody.toList();
            //Create the context method that returns the next item for this iterator
            lastIteratorCtxtName = ctxtName = itemVar;
            fields.add(make().MethodDef(make().Modifiers(Flags.PRIVATE | Flags.FINAL), itemVar.asName(),
                makeJavaType(typeFact().getBooleanType()),
                List.<JCTree.JCTypeParameter>nil(), List.<JCTree.JCVariableDecl>nil(), List.<JCExpression>nil(),
                make().Block(0, methodBody), null));
            return itemVar;
        }
    }

    //
    // Type helper functions

    private Type getSupertype(Tree.Term term, TypeDeclaration compoundType){
        return term.getTypeModel().getSupertype(compoundType);
    }

    private Type getTypeArgument(Type leftType) {
        if (leftType!=null && leftType.getTypeArguments().size()==1) {
            return leftType.getTypeArgumentList().get(0);
        }
        return null;
    }

    private Type getTypeArgument(Type leftType, int i) {
        if (leftType!=null && leftType.getTypeArguments().size() > i) {
            return leftType.getTypeArgumentList().get(i);
        }
        return null;
    }

    private JCExpression unAutoPromote(JCExpression ret, Type returnType, boolean small) {
        // +/- auto-promotes to int, so if we're using java types we'll need a cast
        Type exprType;
        if (small && returnType.isInteger()) {
            exprType = typeFact().getIntegerType();
            if (exprType.isCached()) {
                exprType = exprType.clone();
            }
            exprType.setUnderlyingType("int");
        } else {
            exprType = typeFact().getIntegerType();
        }
        return applyJavaTypeConversions(ret, exprType, 
                returnType, BoxingStrategy.UNBOXED, false, small, 0);
    }

    private Type getMostPreciseType(Tree.Term term, Type defaultType) {
        // special case for interop when we're dealing with java types
        Type termType = term.getTypeModel();
        if(!term.getSmall() && termType.getUnderlyingType() != null) {
            return termType;
        }
        return defaultType;
    }
    
    private Type getLeastPreciseType(Tree.Term term, Tree.Term defaultType) {
        Type termType = term.getTypeModel();
        if(term.getSmall() || termType.getUnderlyingType() != null) {
            return defaultType.getTypeModel();
        }
        return termType;
    }

    //
    // Helper functions
    
    private boolean isRecursiveReference(Tree.StaticMemberOrTypeExpression expr) {
        Declaration decl = expr.getDeclaration();
        Scope s = expr.getScope();
        // do we have decl as our container anywhere in the scope?
        while (s != null && !Decl.equalScopeDecl(s, decl)) {
            s = s.getContainer();
        }
        return Decl.equalScopeDecl(s, decl);
    }

    private boolean isReferenceInSameScope(Tree.StaticMemberOrTypeExpression expr) {
        if (isWithinSyntheticClassBody()) {
            return false;
        }
        Declaration decl = expr.getDeclaration();
        Scope s = expr.getScope();
        // are we in the same Declaration container?
        while (s != null && s instanceof Declaration == false) {
            s = s.getContainer();
        }
        return Decl.equalScopeDecl(s, decl);
    }

    boolean isWithinInvocation() {
        return withinInvocation;
    }
    
    boolean isFunctionalResult(Type type) {
        return !isWithinInvocation()
            && !type.isNothing()
            && isCeylonCallableSubtype(type);   
    }
    
    boolean isJavaFunctionalInterfaceResult(Type type) {
        return !isWithinInvocation()
            && !type.isNothing()
            && checkForFunctionalInterface(type) != null;   
    }

    boolean withinInvocation(boolean withinInvocation) {
        boolean result = this.withinInvocation;
        this.withinInvocation = withinInvocation;
        return result;
    }

    boolean isWithinSyntheticClassBody() {
        return withinSyntheticClassBody;
    }

    boolean withinSyntheticClassBody(boolean withinSyntheticClassBody) {
        boolean result = this.withinSyntheticClassBody;
        this.withinSyntheticClassBody = withinSyntheticClassBody;
        return result;
    }

    boolean isWithinSuperInvocation() {
        return withinSuperInvocation != null;
    }

    boolean isWithinSuperInvocation(Scope container) {
        return Decl.equalScopeDecl(container, withinSuperInvocation);
    }

    void withinSuperInvocation(ClassOrInterface forDefinition) {
        this.withinSuperInvocation = forDefinition;
    }

    boolean isWithinDefaultParameterExpression(Scope container) {
        return Decl.equalScopeDecl(container, withinDefaultParameterExpression);
    }

    void withinDefaultParameterExpression(ClassOrInterface forDefinition) {
        this.withinDefaultParameterExpression = forDefinition;
    }

    //
    // Optimisations

    private JCExpression checkForQualifiedMemberExpressionOptimisation(Tree.QualifiedMemberExpression expr) {
        JCExpression ret = checkForBitwiseOperators(expr, expr, null);
        if(ret != null)
            return ret;
        ret = checkForCharacterAsInteger(expr);
        if(ret != null)
            return ret;
        ret = checkForByteLiterals(expr, false);
        if(ret != null)
            return ret;
        return null;
    }

    /*private JCExpression checkForArrayOnJavaArray(Tree.QualifiedMemberExpression expr) {
        if ("array".equals(expr.getIdentifier().getText())) {
            if (expr.getPrimary() instanceof Tree.BaseMemberExpression) {
                if (Decl.isJavaArray(expr.getPrimary().getTypeModel().getDeclaration())) {
                    return transform((Tree.BaseMemberExpression)expr.getPrimary());
                }
            }
        }
        return null;
    }*/

    boolean isThrowableSuppressed(Tree.QualifiedMemberOrTypeExpression expr) {
        return typeFact().getThrowableDeclaration().getDirectMember("suppressed", null, false).equals(
                expr.getDeclaration().getRefinedDeclaration());
    }

    boolean isThrowableMessage(Tree.QualifiedMemberOrTypeExpression expr) {
        return typeFact().getThrowableDeclaration().getDirectMember("message", null, false).equals(
                expr.getDeclaration().getRefinedDeclaration());
    }

    private JCExpression checkForInvocationExpressionOptimisation(Tree.InvocationExpression ce) {
        // FIXME: temporary hack for byte literals
        JCExpression ret = checkForByteLiterals(ce);
        if(ret != null)
            return ret;
        // FIXME: temporary hack for bitwise operators literals
        ret = checkForBitwiseOperators(ce);
        if(ret != null)
            return ret;
        return null;
    }

    private JCExpression checkForByteLiterals(Tree.QualifiedMemberExpression expr, boolean negative) {
        // must be a call on Integer
        Tree.Term left = expr.getPrimary();
        if(left == null || !isCeylonInteger(left.getTypeModel()))
            return null;
        // must be on "byte"
        if(!expr.getIdentifier().getText().equals("byte"))
            return null;
        // must be a normal member op "."
        if(expr.getMemberOperator() instanceof Tree.MemberOp == false)
            return null;
        // must be unboxed
        if(!expr.getUnboxed() || !left.getUnboxed())
            return null;
        // This can't be Tree.NegativeOp as the normal precedence of -1.byte is -(1.byte), not (-1).byte
        // and must be a number literal
        if(left instanceof Tree.NaturalLiteral == false)
            return null;
        // all good
        at(expr);
        try{
            long value = literalValue((Tree.NaturalLiteral) left).longValue();
            if(negative)
                value = -value;
            // in the case of -128 to 127 we don't need to cast to byte by using an int literal, but only for
            // assignment, not for method calls, so it's simpler to always cast
            return make().TypeCast(syms().byteType, make().Literal(value));
        } catch (ErroneousException e) {
            // We should never get here since the error should have been 
            // reported by the UnsupportedVisitor and the containing statement
            // replaced with a throw.
            return e.makeErroneous(this);
        }
    }
    
    private JCExpression checkForByteLiterals(Tree.InvocationExpression ce) {
        // same test as in BoxingVisitor.isByteLiteral()
        if(ce.getPrimary() instanceof Tree.BaseTypeExpression
                && ce.getPositionalArgumentList() != null){
            java.util.List<Tree.PositionalArgument> positionalArguments = ce.getPositionalArgumentList().getPositionalArguments();
            if(positionalArguments.size() == 1){
                PositionalArgument argument = positionalArguments.get(0);
                if(argument instanceof Tree.ListedArgument
                        && ((Tree.ListedArgument) argument).getExpression() != null){
                    Term term = ((Tree.ListedArgument)argument).getExpression().getTerm();
                    boolean negative = false;
                    if(term instanceof Tree.NegativeOp){
                        negative = true;
                        term = ((Tree.NegativeOp) term).getTerm();
                    }
                    if(term instanceof Tree.NaturalLiteral){
                        Declaration decl = ((Tree.BaseTypeExpression)ce.getPrimary()).getDeclaration();
                        if(decl instanceof Class){
                            if(((Class) decl).isByte()){
                                at(ce);
                                try{
                                    long value = literalValue((Tree.NaturalLiteral) term).longValue();
                                    if(negative)
                                        value = -value;
                                    // in the case of -128 to 127 we don't need to cast to byte by using an int literal, but only for
                                    // assignment, not for method calls, so it's simpler to always cast
                                    return make().TypeCast(syms().byteType, make().Literal(value));
                                } catch (ErroneousException e) {
                                    // We should never get here since the error should have been 
                                    // reported by the UnsupportedVisitor and the containing statement
                                    // replaced with a throw.
                                    return e.makeErroneous(this);
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private JCExpression checkForCharacterAsInteger(Tree.QualifiedMemberExpression expr) {
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

    private JCExpression checkForBitwiseOperators(Tree.InvocationExpression ce) {
        if(!(ce.getPrimary() instanceof Tree.QualifiedMemberExpression))
            return null;
        Tree.QualifiedMemberExpression qme = (Tree.QualifiedMemberExpression) ce.getPrimary();
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
        if(left == null) {
            return null;
        }
        String signature;
        Type binaryType;
        if (isCeylonInteger(left.getTypeModel())) {
            // must be a supported method/attribute
            binaryType = typeFact().getIntegerType();
            String name = qme.getIdentifier().getText();
            signature = "ceylon.language.Integer."+name;
        } else if (isCeylonByte(left.getTypeModel())) {
            binaryType = typeFact().getByteType();
            String name = qme.getIdentifier().getText();
            signature = "ceylon.language.Byte."+name;
        } else {
            return null;
        }
        // see if we have an operator for it
        OperatorTranslation operator = Operators.getOperator(signature);
        if(operator != null){
            JCExpression result;
            if(operator.getArity() == 2){
                if(right == null)
                    return null;
                OptimisationStrategy optimisationStrategy = operator.getBinOpOptimisationStrategy(node, left, right, this);
                // check that we can optimise it
                if(!optimisationStrategy.useJavaOperator())
                    return null;
                
                JCExpression leftExpr = transformExpression(left, optimisationStrategy.getBoxingStrategy(), binaryType, EXPR_WIDEN_PRIM);
                JCExpression rightExpr = transformExpression(right, optimisationStrategy.getBoxingStrategy(), binaryType, EXPR_WIDEN_PRIM);

                if (operator.leftValueMask != 0) {
                    leftExpr = make().Binary(JCTree.Tag.BITAND, leftExpr, makeInteger(operator.leftValueMask));
                }
                if (operator.rightValueMask != 0) {
                    rightExpr = make().Binary(JCTree.Tag.BITAND, rightExpr, makeInteger(operator.rightValueMask));
                }
                
                result =  make().Binary(operator.javacOperator, leftExpr, rightExpr);
            }else{
                // must be unary
                if(right != null)
                    return null;
                OptimisationStrategy optimisationStrategy = operator.getUnOpOptimisationStrategy(node, left, this);
                // check that we can optimise it
                if(!optimisationStrategy.useJavaOperator())
                    return null;
                
                JCExpression leftExpr = transformExpression(left, optimisationStrategy.getBoxingStrategy(), binaryType, EXPR_WIDEN_PRIM);

                if (operator.leftValueMask != 0) {
                    leftExpr = make().Binary(JCTree.Tag.BITAND, leftExpr, makeInteger(operator.leftValueMask));
                }
                
                result = make().Unary(operator.javacOperator, leftExpr);
            }
            if (isCeylonByte(binaryType)) {
                result = make().TypeCast(syms().byteType, result);
            }
            return result;
        }
        return null;
    }
    /** 
     * Transform the annotations on the given annotated declaration for 
     * inclusion on the given target element type 
     */
    public List<JCAnnotation> transformAnnotations(OutputElement target, 
            Tree.Declaration annotated) {
        EnumSet<OutputElement> outputs;
        if (annotated instanceof Tree.AnyClass) {
            outputs = AnnotationUtil.outputs((Tree.AnyClass)annotated);
        } else if (annotated instanceof Tree.AnyInterface) {
            outputs = AnnotationUtil.outputs((Tree.AnyInterface)annotated);
        } else if (annotated instanceof Tree.TypeAliasDeclaration) {
            outputs = AnnotationUtil.outputs((Tree.TypeAliasDeclaration)annotated);
        } else if (annotated instanceof Tree.Constructor) {
            outputs = AnnotationUtil.outputs((Tree.Constructor)annotated);
        } else if (annotated instanceof Tree.Enumerated) {
            outputs = AnnotationUtil.outputs((Tree.Enumerated)annotated);
        } else if (annotated instanceof Tree.AnyMethod) {
            outputs = AnnotationUtil.outputs((Tree.AnyMethod)annotated);
        } else if (annotated instanceof Tree.AttributeDeclaration) {
            outputs = AnnotationUtil.outputs((Tree.AttributeDeclaration)annotated);
        } else if (annotated instanceof Tree.AttributeGetterDefinition) {
            outputs = AnnotationUtil.outputs((Tree.AttributeGetterDefinition)annotated);
        } else if (annotated instanceof Tree.AttributeSetterDefinition) {
            outputs = AnnotationUtil.outputs((Tree.AttributeSetterDefinition)annotated);
        } else if (annotated instanceof Tree.ObjectDefinition) {
            outputs = AnnotationUtil.outputs((Tree.ObjectDefinition)annotated);
        } else {
            throw BugException.unhandledNodeCase(annotated);
        }
        return transform(annotated.getDeclarationModel(), target, annotated.getAnnotationList(), outputs);
    }
    /** 
     * Transform the annotations on the given package declaration for 
     * inclusion on the given target element type 
     */
    public List<JCAnnotation> transformAnnotations(
            OutputElement target, 
            Tree.PackageDescriptor annotated) {
        return transform(annotated.getUnit().getPackage(), target, 
                annotated.getAnnotationList(), AnnotationUtil.outputs((Tree.PackageDescriptor)annotated));
    }
    /** 
     * Transform the annotations on the given module import declaration for 
     * inclusion on the given target element type 
     */
    public List<JCAnnotation> transformAnnotations(
            OutputElement target, 
            Tree.ImportModule annotated) {
        return transform(annotated, target, 
                annotated.getAnnotationList(), AnnotationUtil.outputs((Tree.ImportModule)annotated));
    }
    /** 
     * Transform the annotations on the given module declaration for inclusion 
     * on the given target element type 
     */
    public List<JCAnnotation> transformAnnotations(
            OutputElement target, 
            Tree.ModuleDescriptor annotated) {
        return transform(annotated.getUnit().getPackage().getModule(), target, 
                annotated.getAnnotationList(), AnnotationUtil.outputs((Tree.ModuleDescriptor)annotated));
    }
    private List<JCAnnotation> transform(Object useSite, 
            OutputElement target, Tree.AnnotationList annotationList,
            EnumSet<OutputElement> outputs) {
        if (annotationList == null) {
            return List.nil();
        }
        if ((gen().disableAnnotations & CeylonTransformer.DISABLE_USER_ANNOS) != 0) {
            return List.nil();
        }
        LinkedHashMap<Class, ListBuffer<JCAnnotation>> annotationSet = new LinkedHashMap<>();
        if (annotationList != null) {
            
            if (annotationList.getAnonymousAnnotation() != null
                    && AnnotationUtil.isNaturalTarget((Function)typeFact().getLanguageModuleDeclaration("doc"),  useSite, target)) {
                transformAnonymousAnnotation(annotationList.getAnonymousAnnotation(), annotationSet);
            }
            if (annotationList.getAnnotations() != null) {
                for (Tree.Annotation annotation : annotationList.getAnnotations()) {
                    Function annoCtorDecl = ((Function)((Tree.BaseMemberExpression)annotation.getPrimary()).getDeclaration());
                    if (annoCtorDecl != null) {
                        String aname = annoCtorDecl.getQualifiedNameString();
                        if ("java.lang::transient".equals(aname)
                                || "java.lang::volatile".equals(aname)
                                || "java.lang::synchronized".equals(aname)
                                || "java.lang::native".equals(aname)
                                || "java.lang::strictfp".equals(aname)
                                || "java.lang::overloaded".equals(aname)
                                || "java.lang::nonbean".equals(aname)) {
                            continue;
                        }
                    }
                    boolean isNaturalTarget = AnnotationUtil.isNaturalTarget(annoCtorDecl, useSite, target);
                    EnumSet<OutputElement> possibleTargets = AnnotationUtil.interopAnnotationTargeting(
                            useSite instanceof Declaration ? isEe((Declaration)useSite) : false,
                            outputs, annotation, false, false, useSite instanceof Declaration ? (Declaration)useSite : null);
                    if ((isNaturalTarget
                            && possibleTargets == null)
                            || 
                            (possibleTargets != null 
                                && possibleTargets.equals(EnumSet.of(target)))) {
                        transformAnnotation(annotation, annotationSet);
                    }
                }
            }
        }
        ListBuffer<JCAnnotation> result = new ListBuffer<JCAnnotation>();
        for (Class annotationClass : annotationSet.keySet()) {
            ListBuffer<JCAnnotation> annotations = annotationSet.get(annotationClass);
            if (isSequencedAnnotation(annotationClass)) {
                JCAnnotation wrapperAnnotation = make().Annotation(
                        makeJavaType(annotationClass.getType(), JT_ANNOTATIONS), 
                        List.<JCExpression>of(make().NewArray(null,  null, upcastExprList(annotations.toList()))));
                result.append(wrapperAnnotation);
            } else if (isRepeatableAnnotation(annotationClass)) {
                Interface containerAnnotation = getRepeatableContainer(annotationClass);
                JCAnnotation wrapperAnnotation = make().Annotation(
                        makeJavaType(containerAnnotation.appliedType(null, Collections.<Type>emptyList())),
                        List.<JCExpression>of(make().NewArray(null,  null, upcastExprList(annotations.toList()))));
                result.append(wrapperAnnotation);
            }else {
                if (annotations.size() > 1) {
                    makeErroneous(annotationList, "compiler bug: multiple occurances of non-sequenced annotation class " + annotationClass.getQualifiedNameString());
                }
                result.appendList(annotations);
            }
        }
        
        // Special case: Generate a @java.lang.Deprecated() if Ceylon deprecated
        if (annotationList != null) {
            for (Tree.Annotation annotation : annotationList.getAnnotations()) {
                if (AnnotationUtil.isNaturalTarget((Function)typeFact().getLanguageModuleDeclaration("deprecated"), useSite, target) 
                        && isDeprecatedAnnotation(annotation.getPrimary())
                        && !(useSite instanceof Function)
                        && !(useSite instanceof Constructor)) {
                    result.appendList(makeAtDeprecated());
                }
            }
        }
        
        return result.toList();
    }
    
    void transformAnnotation(Tree.Annotation invocation, 
            Map<Class, ListBuffer<JCAnnotation>> annotationSet) {
        at(invocation);
        HasErrorException error = errors().getFirstExpressionErrorAndMarkBrokenness(invocation);
        if (error == null) {
            try {
                JCAnnotation annotation = AnnotationInvocationVisitor.transformConstructor(this, invocation);
                if (annotation != null) {
                    Class annotationClass = AnnotationInvocationVisitor.annoClass(invocation);
                    putAnnotation(annotationSet, annotation, annotationClass);
                }
            } catch (BugException e) {
                e.addError(invocation);
            }
        }
    }

    /**
     * Returns true if the given primary is {@code ceylon.language.deprecated()}
     */
    private boolean isDeprecatedAnnotation(Tree.Primary primary) {
        return primary instanceof Tree.BaseMemberExpression
                && typeFact().getLanguageModuleDeclaration("deprecated").equals(((Tree.BaseMemberExpression)primary).getDeclaration());
    }

    private void putAnnotation(
            Map<Class, ListBuffer<JCAnnotation>> annotationSet,
            JCAnnotation annotation, Class annotationClass) {
        ListBuffer<JCAnnotation> list = annotationSet.get(annotationClass);
        if (list == null) {
            list = new ListBuffer<JCAnnotation>();
        }
        annotationSet.put(annotationClass, list.append(annotation));
    }


    public void transformAnonymousAnnotation(Tree.AnonymousAnnotation annotation, Map<Class, ListBuffer<JCAnnotation>> annos) {
        Type docType = ((TypeDeclaration)typeFact().getLanguageModuleDeclaration("DocAnnotation")).getType();
        JCAnnotation docAnnotation = at(annotation).Annotation(
                makeJavaType(docType,  JT_ANNOTATION), 
                List.<JCExpression>of(make().Assign(naming.makeUnquotedIdent("description"),
                        transform(annotation.getStringLiteral()))));
        putAnnotation(annos, docAnnotation, (Class)docType.getDeclaration());
    }
    
    public JCExpression makeMetaLiteralStringLiteralForAnnotation(Tree.MetaLiteral literal) {
        String ref = getSerializedMetaLiteral(literal);
        if (ref != null) {
            return make().Literal(ref);
        }
        return makeErroneous(literal, "compiler bug: " + literal.getNodeType() + " is not a supported meta literal");
    }

    public static Referenceable getMetaLiteralReferenceable(Tree.MetaLiteral ml) {
        if (ml instanceof Tree.TypeLiteral) {
            return ml.getDeclaration();
        } else if (ml instanceof Tree.MemberLiteral) {
            return ml.getDeclaration();
        } else if (ml instanceof Tree.PackageLiteral) {
            return ((Tree.PackageLiteral)ml).getImportPath().getModel();
        } else if (ml instanceof Tree.ModuleLiteral) {
            return ((Tree.ModuleLiteral)ml).getImportPath().getModel();
        } 
        return null;
    }
    
    public static String getSerializedMetaLiteral(Tree.MetaLiteral ml) {
        if (ml instanceof Tree.ModuleLiteral) {
            Tree.ModuleLiteral mod = (Tree.ModuleLiteral) ml;
            Tree.ImportPath ip = mod.getImportPath();
            if (mod.getRestriction() || ip.getModel()==null) {
                return ip.getIdentifiers().isEmpty() ? 
                        ml.getUnit().getPackage().getModule().getNameAsString() : 
                        TreeUtil.formatPath(ip.getIdentifiers());
            }
        }
        if (ml instanceof Tree.PackageLiteral) {
            Tree.PackageLiteral pack = (Tree.PackageLiteral) ml;
            Tree.ImportPath ip = pack.getImportPath();
            if (ip.getModel()==null) {
                return ip.getIdentifiers().isEmpty() ? 
                        ml.getUnit().getPackage().getNameAsString() : 
                        TreeUtil.formatPath(ip.getIdentifiers());
            }
        }
        return serializeReferenceable(getMetaLiteralReferenceable(ml));
    }
    
    public static String serializeReferenceable(Referenceable ref) {
        StringBuilder sb = new StringBuilder();
        if (ref instanceof Declaration) {
            appendDeclarationLiteralForAnnotation((Declaration)ref, sb);
        } else if (ref instanceof Package) {
            appendDeclarationLiteralForAnnotation((Package)ref, sb);
        } else if (ref instanceof Module) {
            appendDeclarationLiteralForAnnotation((Module)ref, sb);
        }
        return sb.toString();
    }
    
    public JCExpression makeDeclarationLiteralForAnnotation(Package decl) {
        StringBuilder sb = new StringBuilder();
        appendDeclarationLiteralForAnnotation(decl, sb);
        return make().Literal(sb.toString());
    }
    
    public JCExpression makeDeclarationLiteralForAnnotation(Module decl) {
        StringBuilder sb = new StringBuilder();
        appendDeclarationLiteralForAnnotation(decl, sb);
        return make().Literal(sb.toString());
    }
    
    /*
 * ref              ::= version? module ;
 *                      // note: version is optional to support looking up the
 *                      // runtime version of a package, once we support this
 * version          ::= ':' SENTINEL ANYCHAR* SENTINEL ;
 * module           ::= dottedIdent package? ;
 * dottedIdent      ::= ident ('.' ident)* ;
 * package          ::= ':' ( relativePackage | absolutePackage ) ? ( ':' declaration ) ? ;
 *                      // note: if no absolute or relative package given, it's the 
 *                      // root package of the module
 * relativePackage  ::= dottedIdent ;
 * absolutePackage  ::= '.' dottedIdent ;
 *                      // note: to suport package names which don't start 
 *                      // with the module name
 * declaration      ::= type | function | value ;
 * type             ::= class | interface ;
 * class            ::= 'C' ident ( '.' member )?
 * interface        ::= 'I' ident ( '.' member )?
 * member           ::= declaration ;
 * function         ::= 'F' ident ;
 * value            ::= 'V' ident ;
     */
    /**
     * Appends into the given builder a String representation of the given 
     * module, suitable for parsing my the DeclarationParser.
     */
    private static void appendDeclarationLiteralForAnnotation(Module module,
            StringBuilder sb) {
        char sentinel = findSentinel(module);
        sb.append(":").append(sentinel).append(module.getVersion()).append(sentinel).append(module.getNameAsString());
    }

    /**
     * Computes a sentinel for the verion number
     */
    private static char findSentinel(Module module) {
        for (char ch : ":\"\'/#!$%\\@~+=*".toCharArray()) {
            if (module.getVersion().indexOf(ch) == -1) {
                return ch;
            }
        }
        // most unlikely end end up here
        char ch = 1;
        while (true) {
            if (module.getVersion().indexOf(ch) == -1) {
                return ch;
            }
            ch++;
        }
    }
    
    /**
     * Appends into the given builder a String representation of the given 
     * package, suitable for parsing my the DeclarationParser.
     */
    private static void appendDeclarationLiteralForAnnotation(Package pkg,
            StringBuilder sb) {
        appendDeclarationLiteralForAnnotation(pkg.getModule(), sb);
        sb.append(':');
        String moduleName = pkg.getModule().getNameAsString();
        String packageName = pkg.getNameAsString();
        if (packageName.equals(moduleName)) {
        } else if (packageName.startsWith(moduleName)) {
            sb.append(packageName.substring(moduleName.length()+1));
        } else {
            sb.append('.').append(packageName);
        }
    }
    
    /**
     * Appends into the given builder a String representation of the given 
     * declaration, suitable for parsing my the DeclarationParser.
     */
    private static void appendDeclarationLiteralForAnnotation(Declaration decl, StringBuilder sb) {
        Scope container = decl.getContainer();
        while (true) {
            if (container instanceof Declaration) {
                appendDeclarationLiteralForAnnotation((Declaration)container, sb);
                sb.append(".");
                break;
            } else if (container instanceof Package) {
                appendDeclarationLiteralForAnnotation((Package)container, sb);
                sb.append(":");
                break;
            }
            container = container.getContainer();
        }
        if (decl instanceof Class) {
            sb.append("C").append(decl.getName());
        } else if (decl instanceof Interface) {
            sb.append("I").append(decl.getName());
        } else if (decl instanceof TypeAlias) {
            sb.append("A").append(decl.getName());
        } else if (decl instanceof Value) {
            sb.append("V").append(decl.getName());
        } else if (decl instanceof Function) {
            sb.append("F").append(decl.getName());
        } else if (decl instanceof TypeParameter) {
            sb.append("P").append(decl.getName());
        } else if (decl instanceof Constructor) {
            sb.append("c").append(decl.getName());
        } else {
            throw BugException.unhandledDeclarationCase(decl);
        }
    }
    
    JCExpression makePrivateAccessUpcast(Tree.StaticMemberOrTypeExpression qmte, JCExpression qual) {
        Type pt = Decl.getPrivateAccessType(qmte);
        int flags = JT_RAW;
        // By definition the member has private access, so if it's an interface
        // member we want the companion.
        if(pt.getDeclaration() instanceof Interface)
            flags |= JT_COMPANION;
        return make().TypeCast(makeJavaType(pt, flags), qual);
    }

    public JCTree transform(Tree.ObjectExpression expr) {
        at(expr);
        List<JCTree> klass = classGen().transformObjectExpression(expr);
        at(expr);
        JCExpression newCall = make().NewClass(null, null, makeUnquotedIdent(Naming.escapeClassName(expr.getAnonymousClass().getName())+"_"), List.<JCTree.JCExpression>nil(), null);
        return make().LetExpr((List)klass, newCall);
    }

    public JCExpression transform(Tree.IfExpression op) {
        return transformIf(op, getExpectedTypeForJavaNullChecks(op, expectedType));
    }
    
    // this one trusts the expected type
    private JCExpression transformIf(Tree.IfExpression op, Type expectedType) {
        String tmpVar = naming.newTemp("ifResult");
        Tree.Expression thenPart = op.getIfClause().getExpression();
        Tree.Expression elsePart = op.getElseClause() != null ? op.getElseClause().getExpression() : null;
        Tree.Variable elseVar = op.getElseClause() != null ? op.getElseClause().getVariable() : null;
        java.util.List<Tree.Condition> conditions = op.getIfClause().getConditionList().getConditions();
        List<JCStatement> statements = statementGen().transformIf(conditions, thenPart, elseVar, elsePart, tmpVar, op, expectedType);
        at(op);
        // use the op model for the variable, not expected type, because expected type may be optional, where op
        // says not optional (even in case of java interop which may return null), so we allow null values in j.l.String (unboxed)
        // because the caller will insert the null check if the expected type is optional 
        Type typeModel = op.getTypeModel();
        if (willEraseToObject(typeModel)) {
            typeModel = typeFact().denotableType(typeModel);
        }
        JCExpression vartype = makeJavaType(typeModel, CodegenUtil.getBoxingStrategy(op) == BoxingStrategy.UNBOXED ? 0 : JT_NO_PRIMITIVES);
        return make().LetExpr(make().VarDef(make().Modifiers(0), names().fromString(tmpVar), vartype , null), statements, makeUnquotedIdent(tmpVar));
    }

    public JCExpression transform(Tree.SwitchExpression op) {
        return transformSwitch(op, getExpectedTypeForJavaNullChecks(op, expectedType));
    }
    
    // this one trusts the expected type
    private JCExpression transformSwitch(Tree.SwitchExpression op, Type expectedType) {
        String tmpVar = naming.newTemp("ifResult");
        JCStatement switchExpr = statementGen().transform(op, op.getSwitchClause(), op.getSwitchCaseList(), tmpVar, op, expectedType);
        at(op);
        // use the op model for the variable, not expected type, because expected type may be optional, where op
        // says not optional (even in case of java interop which may return null), so we allow null values in j.l.String (unboxed)
        // because the caller will insert the null check if the expected type is optional 
        JCExpression vartype = makeJavaType(op.getTypeModel(), CodegenUtil.getBoxingStrategy(op) == BoxingStrategy.UNBOXED ? 0 : JT_NO_PRIMITIVES);
        return make().LetExpr(make().VarDef(make().Modifiers(0), names().fromString(tmpVar), vartype , null), 
                              List.<JCStatement>of(switchExpr), makeUnquotedIdent(tmpVar));
    }

    public JCExpression transform(LetExpression op) {
        return transformLet(op, getExpectedTypeForJavaNullChecks(op, expectedType));
    }
    
    private Type getExpectedTypeForJavaNullChecks(Term op, Type expectedType) {
        // turns the expression type into an optional if it's not already, and if the
        // expected type allows nulls and the term contains unchecked nulls
        if(expectedType == null 
                || !isOptional(expectedType) 
                || !containsUncheckedNulls(op))
            return op.getTypeModel();
        else
            return typeFact().getOptionalType(op.getTypeModel());
    }

    // this one trusts the expected type
    private JCExpression transformLet(LetExpression op, Type expectedType) {
        ListBuffer<JCStatement> defs = new ListBuffer<JCStatement>();
        for(Tree.Statement stmt : op.getLetClause().getVariables()){
            defs.addAll(statementGen().transformVariableOrDestructure(stmt));
        }
        Tree.Term term = op.getLetClause().getExpression().getTerm();
        BoxingStrategy boxingStrategy = CodegenUtil.getBoxingStrategy(term);
        JCExpression expr = transformExpression(term, boxingStrategy, expectedType);
        at(op);
        if (isAnything(op.getTypeModel()) 
                && CodegenUtil.isUnBoxed(term)) {
            defs.add(make().Exec(expr));
            expr = makeNull();
        }
        return make().LetExpr(defs.toList(), expr);
    }
}
