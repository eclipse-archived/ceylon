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

import static org.eclipse.ceylon.compiler.java.codegen.AbstractTransformer.JT_CLASS_NEW;
import static org.eclipse.ceylon.compiler.java.codegen.AbstractTransformer.JT_COMPANION;
import static org.eclipse.ceylon.compiler.java.codegen.AbstractTransformer.JT_EXTENDS;
import static org.eclipse.ceylon.compiler.java.codegen.AbstractTransformer.JT_NO_PRIMITIVES;

import java.util.ArrayList;
import java.util.Collections;

import org.eclipse.ceylon.compiler.java.codegen.AbstractTransformer.BoxingStrategy;
import org.eclipse.ceylon.compiler.java.codegen.Naming.SyntheticName;
import org.eclipse.ceylon.compiler.typechecker.tree.Node;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree.QualifiedMemberOrTypeExpression;
import org.eclipse.ceylon.langtools.tools.javac.code.Flags;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCAnnotation;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCClassDecl;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCExpression;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCMethodDecl;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCMethodInvocation;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCNewClass;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCStatement;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCTypeParameter;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCVariableDecl;
import org.eclipse.ceylon.langtools.tools.javac.util.List;
import org.eclipse.ceylon.langtools.tools.javac.util.ListBuffer;
import org.eclipse.ceylon.langtools.tools.javac.util.Name;
import org.eclipse.ceylon.model.loader.NamingBase.Suffix;
import org.eclipse.ceylon.model.loader.NamingBase.Unfix;
import org.eclipse.ceylon.model.loader.model.FieldValue;
import org.eclipse.ceylon.model.typechecker.model.Class;
import org.eclipse.ceylon.model.typechecker.model.Constructor;
import org.eclipse.ceylon.model.typechecker.model.Declaration;
import org.eclipse.ceylon.model.typechecker.model.Function;
import org.eclipse.ceylon.model.typechecker.model.FunctionOrValue;
import org.eclipse.ceylon.model.typechecker.model.Functional;
import org.eclipse.ceylon.model.typechecker.model.Interface;
import org.eclipse.ceylon.model.typechecker.model.ModelUtil;
import org.eclipse.ceylon.model.typechecker.model.Parameter;
import org.eclipse.ceylon.model.typechecker.model.ParameterList;
import org.eclipse.ceylon.model.typechecker.model.Reference;
import org.eclipse.ceylon.model.typechecker.model.Type;
import org.eclipse.ceylon.model.typechecker.model.TypeParameter;
import org.eclipse.ceylon.model.typechecker.model.TypedDeclaration;
import org.eclipse.ceylon.model.typechecker.model.TypedReference;
import org.eclipse.ceylon.model.typechecker.model.Value;

/**
 * Constructs anonymous subclasses of AbstractCallable, required for higher 
 * order functions.
 * 
 * <ul>
 * <li>If the Callable does not need to encode defaulted parameter values, and 
 *     is not variadic we just generate the relevent {@code $call()}
 *     method(s). This includes the case of a method reference to something 
 *     with defaulted parameters, because the Callable itself doesn't need to 
 *     encode the default parameter values.
 * <li>If the Callable needs to encode defaulted parameter values itself (for
 *     example if it's a Callable for an anonymous function,
 *     or a method argument with defaulted parameters) then:
 *     <ul>
 *     <li>a {@code private $call$typed()} method is generated which encodes 
 *         the actual method code,</li>
 *     <li>{@code private $$paramName()} methods are generated for each 
 *         defaulted parameter, and</li>
 *     <li>the {@code public $call()} methods downcast their argument(s), 
 *         invoke the necessary {@code $$paramName()} and delegate to 
 *         {@code $call$typed()}.</li>
 *     <ul>
 * <li>If the Callable is variadic then:</li>
 *     <ul>
 *     <li>a {@code private $call$typed()} method is generated which encodes
 *         the actual method code,</li>
 *     <li>{@code public $call$variadic()} methods are generated for each 
 *         arity greater than the number of non-defaulted parameters and less 
 *         than or equal to 3, plus one which uses Javac varargs, and</li>
 *     <li>the {@code $call()} methods delegate to {@code $call$variadic()} methods,
 *         downcasting and obtaining default arguments if required,</li>
 *     <ul>
 * </ul>
 * 
 */
public class CallableBuilder {

    private static final int CALLABLE_MAX_FIZED_ARITY = 3;
    
    static interface DefaultValueMethodTransformation {
        public JCExpression makeDefaultValueMethod(AbstractTransformer gen, 
                Parameter defaultedParam, List<JCExpression> defaultMethodArgs);
    }
    
    public static final DefaultValueMethodTransformation DEFAULTED_PARAM_METHOD = new DefaultValueMethodTransformation() {
        @Override
        public JCExpression makeDefaultValueMethod(AbstractTransformer gen, 
                Parameter defaultedParam, 
                List<JCExpression> defaultMethodArgs) {
            return gen.make().Apply(null, 
                    gen.naming.makeDefaultedParamMethod(null, defaultedParam),
                    defaultMethodArgs);
        }
    };
    
    DefaultValueMethodTransformation defaultValueCall = DEFAULTED_PARAM_METHOD;
    
    private final AbstractTransformer gen;
    private final Node node;
    private final Type typeModel;
    private final ParameterList paramLists;
    private final int numParams;
    private final int minimumParams;
    private final int minimumArguments;
    private final boolean hasOptionalParameters;
    private final boolean isVariadic;
    private TypedReference functionalInterfaceMethod;
    private Type functionalInterface;

    /**
     * For deferred declarations the default parameter value methods are 
     * generated on the wrapper class, not on the Callable (#1177)
     */
    private boolean delegateDefaultedCalls = true;
    private java.util.List<Type> parameterTypes;
    
    private ListBuffer<MethodDefinitionBuilder> parameterDefaultValueMethods;
    
    private CallableTransformation transformation;
    
    private boolean companionAccess = false;

    private Naming.Substitution instanceSubstitution;

    private List<JCAnnotation> annotations;
    
    private CallableBuilder(CeylonTransformer gen, Node node, Type typeModel, ParameterList paramLists) {
        this.gen = gen;
        this.node = node;
        this.typeModel = typeModel;
        this.paramLists = paramLists;
        this.numParams = paramLists.getParameters().size();
        int minimumParams = 0;
        int minimumArguments = 0;
        for(Parameter p : paramLists.getParameters()){
            if(!p.isDefaulted())
                minimumParams++;
            if (!Strategy.hasDefaultParameterOverload(p)) {
                minimumArguments++;
            }
        }
        this.minimumParams = minimumParams;
        this.minimumArguments = minimumArguments;
        
        this.isVariadic = gen.isVariadicCallable(typeModel);
        this.hasOptionalParameters = minimumParams != numParams;
    }
    
    /**
     * Constructs an {@code AbstractCallable} suitable for wrapping a 
     * method reference. For example:
     * <pre>
     *   void someMethod() { ... }
     *   Anything() ref = someMethod;
     * </pre>
     */
    public static JCExpression methodReference(CeylonTransformer gen, 
            final Tree.StaticMemberOrTypeExpression forwardCallTo, ParameterList parameterList, 
            Type expectedType, Type callableType, boolean useParameterTypesFromCallableModel) {
        ListBuffer<JCStatement> letStmts = new ListBuffer<JCStatement>();
        CallableBuilder cb = new CallableBuilder(gen, forwardCallTo, callableType, parameterList);
        cb.parameterTypes = useParameterTypesFromCallableModel
                ? cb.getParameterTypesFromCallableModel()
                : cb.getParameterTypesFromParameterModels();
        Naming.SyntheticName instanceFieldName;
        boolean instanceFieldIsBoxed = false;
        if (forwardCallTo instanceof Tree.QualifiedMemberOrTypeExpression
                && !ExpressionTransformer.isSuperOrSuperOf(((Tree.QualifiedMemberOrTypeExpression) forwardCallTo).getPrimary())
                && !ExpressionTransformer.isPackageQualified((Tree.QualifiedMemberOrTypeExpression)forwardCallTo)) {
            if ((((Tree.QualifiedMemberOrTypeExpression)forwardCallTo).getMemberOperator() instanceof Tree.SpreadOp)) {
                instanceFieldIsBoxed = true;
                instanceFieldName = null;
            } else {
                Tree.QualifiedMemberOrTypeExpression qmte = (Tree.QualifiedMemberOrTypeExpression)forwardCallTo;
                boolean prevCallableInv = gen.expressionGen().withinSyntheticClassBody(true);
                try {
                    instanceFieldName = gen.naming.synthetic(Unfix.$instance$);
                    int varTypeFlags = Decl.isPrivateAccessRequiringCompanion(qmte) ? JT_COMPANION : 0;
                    Type primaryType;
                    if (Decl.isValueTypeDecl(qmte.getPrimary().getTypeModel())) {
                        primaryType = qmte.getPrimary().getTypeModel();
                    } else {
                        primaryType = qmte.getTarget().getQualifyingType();
                    }
                    if (((Tree.QualifiedMemberOrTypeExpression)forwardCallTo).getMemberOperator() instanceof Tree.SafeMemberOp) {
                        primaryType = gen.typeFact().getOptionalType(primaryType);
                    }
                    JCExpression primaryExpr = gen.expressionGen().transformQualifiedMemberPrimary(qmte);
                    Declaration dec = qmte.getDeclaration();
                    if (Decl.isPrivateAccessRequiringCompanion(qmte)) {
                        primaryExpr = gen.naming.makeCompanionAccessorCall(primaryExpr, (Interface)dec.getContainer());
                    }
                    Type varType = dec.isShared() ? primaryType : Decl.getPrivateAccessType(qmte);
                    
                    if (qmte.getPrimary().getUnboxed() == false) {
                        varTypeFlags |= JT_NO_PRIMITIVES;
                        instanceFieldIsBoxed = true;
                    }
                    letStmts.add(gen.makeVar(Flags.FINAL, 
                            instanceFieldName, 
                            gen.makeJavaType(varType, varTypeFlags), 
                            primaryExpr));
                    
                    if (qmte.getPrimary() instanceof Tree.MemberOrTypeExpression
                            && ((Tree.MemberOrTypeExpression)qmte.getPrimary()).getDeclaration() instanceof TypedDeclaration) {
                        cb.instanceSubstitution = gen.naming.addVariableSubst((TypedDeclaration)((Tree.MemberOrTypeExpression)qmte.getPrimary()).getDeclaration(), instanceFieldName.getName());
                    }
                } finally {
                    gen.expressionGen().withinSyntheticClassBody(prevCallableInv);
                }
            }
        } else {
            instanceFieldName = null;
        }
        
        CallableTransformation tx;
        cb.defaultValueCall = new DefaultValueMethodTransformation() {
            @Override
            public JCExpression makeDefaultValueMethod(AbstractTransformer gen, 
                    Parameter defaultedParam, 
                    List<JCExpression> defaultMethodArgs) {
                JCExpression fn = null;
                if (forwardCallTo instanceof Tree.BaseMemberOrTypeExpression) {
                    fn = gen.naming.makeDefaultedParamMethod(null, defaultedParam);
                } else if (forwardCallTo instanceof Tree.QualifiedMemberOrTypeExpression) {
                    JCExpression qualifier = gen.expressionGen().transformTermForInvocation(((Tree.QualifiedMemberOrTypeExpression)forwardCallTo).getPrimary(), null);
                    fn = gen.naming.makeDefaultedParamMethod(qualifier, defaultedParam);
                }
                return gen.make().Apply(null, 
                        fn,
                        defaultMethodArgs);
            }
        };
        if (cb.isVariadic) {
            tx = cb.new VariadicCallableTransformation(
                    cb.new CallMethodWithForwardedBody(instanceFieldName, instanceFieldIsBoxed, forwardCallTo, false, callableType));
        } else {
            tx = cb.new FixedArityCallableTransformation(cb.new CallMethodWithForwardedBody(instanceFieldName, instanceFieldIsBoxed, forwardCallTo, true, callableType), null);
        }
        cb.useTransformation(tx);
        
        cb.checkForFunctionalInterface(expectedType);
        
        return letStmts.isEmpty() ? cb.build() : gen.make().LetExpr(letStmts.toList(), cb.build());
    }

    public static JCExpression callableToFunctionalInterface(CeylonTransformer gen, 
            final Tree.InvocationExpression node, 
            ParameterList parameterList, 
            Type expectedType, 
            Type callableType, 
            boolean useParameterTypesFromCallableModel,
            JCExpression primaryExpr) {
        ListBuffer<JCStatement> letStmts = new ListBuffer<JCStatement>();
        CallableBuilder cb = new CallableBuilder(gen, node, callableType, parameterList);
        cb.parameterTypes = useParameterTypesFromCallableModel
                ? cb.getParameterTypesFromCallableModel()
                : cb.getParameterTypesFromParameterModels();
        Naming.SyntheticName instanceFieldName;
        boolean instanceFieldIsBoxed = true;
        boolean prevCallableInv = gen.expressionGen().withinSyntheticClassBody(true);
        try {
            instanceFieldName = gen.naming.synthetic(Unfix.$instance$);
            letStmts.add(gen.makeVar(Flags.FINAL, 
                    instanceFieldName, 
                    gen.makeJavaType(callableType), 
                    primaryExpr));
                    
        } finally {
            gen.expressionGen().withinSyntheticClassBody(prevCallableInv);
        }
        
        CallableTransformation tx;
        cb.defaultValueCall = new DefaultValueMethodTransformation() {
            @Override
            public JCExpression makeDefaultValueMethod(AbstractTransformer gen, 
                    Parameter defaultedParam, 
                    List<JCExpression> defaultMethodArgs) {
                JCExpression fn = null;
                return gen.make().Apply(null, 
                        fn,
                        defaultMethodArgs);
            }
        };
        if (cb.isVariadic) {
            tx = cb.new VariadicCallableTransformation(
                    cb.new CallMethodWithForwardedBody(instanceFieldName, instanceFieldIsBoxed, node, false, callableType));
        } else {
            tx = cb.new FixedArityCallableTransformation(cb.new CallMethodWithForwardedBody(instanceFieldName, instanceFieldIsBoxed, node, true, callableType), null);
        }
        cb.useTransformation(tx);
        
        cb.checkForFunctionalInterface(expectedType);
        
        return letStmts.isEmpty() ? cb.build() : gen.make().LetExpr(letStmts.toList(), cb.build());
    }

    /**
     * Used for "static" method or class references. For example:
     * <pre>
     *     value x = Integer.plus;
     *     value y = Foo.method;
     *     value z = Outer.Inner;
     * </pre>
     */
    public static JCExpression unboundFunctionalMemberReference(
            CeylonTransformer gen,
            Tree.QualifiedMemberOrTypeExpression qmte,
            Type typeModel, 
            final Functional methodClassOrCtor, 
            Reference producedReference,
            Type expectedType) {
        final ParameterList parameterList = methodClassOrCtor.getFirstParameterList();
        Type type = typeModel;
        JCExpression target;
        boolean memberClassCtorRef = ModelUtil.getConstructor((Declaration)methodClassOrCtor) != null
                && !ModelUtil.getConstructedClass((Declaration)methodClassOrCtor).isToplevel()
                && qmte.getPrimary() instanceof Tree.QualifiedTypeExpression
                ;
        boolean hasOuter = !(Decl.isConstructor((Declaration)methodClassOrCtor)
                && gen.getNumParameterLists(typeModel) == 1);
        if (!hasOuter) {
            type = typeModel;
            if (memberClassCtorRef) {
                target = gen.naming.makeUnquotedIdent(Unfix.$instance$);
            } else {
                target = null;
            }
        } else {
            type = gen.getReturnTypeOfCallable(type);
            Type qualifyingType = qmte.getTarget().getQualifyingType();
            target = gen.naming.makeUnquotedIdent(Unfix.$instance$);
            target = gen.expressionGen().applyErasureAndBoxing(target, producedReference.getQualifyingType(), true, BoxingStrategy.BOXED, qualifyingType);
        }
        CallableBuilder inner = new CallableBuilder(gen, qmte, type, parameterList);
        inner.parameterTypes = inner.getParameterTypesFromCallableModel();//FromParameterModels();
        if (hasOuter) {
            inner.defaultValueCall = inner.new MemberReferenceDefaultValueCall(methodClassOrCtor);
        }
        CallBuilder callBuilder = CallBuilder.instance(gen);
        Type accessType = gen.getParameterTypeOfCallable(typeModel, 0);
        boolean needsCast = false;
        if (Decl.isConstructor((Declaration)methodClassOrCtor)) {
            Constructor ctor = ModelUtil.getConstructor((Declaration)methodClassOrCtor);
            Class cls = ModelUtil.getConstructedClass(ctor);
            if (Strategy.generateInstantiator(ctor)) {
                needsCast = Strategy.isInstantiatorUntyped(ctor);
                callBuilder.invoke(gen.naming.makeInstantiatorMethodName(target, cls));
            } else if (Decl.isJavaArrayWith(ctor)) {
                callBuilder.arrayWith(
                        gen.getReturnTypeOfCallable(typeModel).getQualifyingType(), 
                        gen.makeJavaType(gen.getReturnTypeOfCallable(typeModel), JT_CLASS_NEW));
            } else {
                callBuilder.instantiate( 
                        gen.makeJavaType(gen.getReturnTypeOfCallable(typeModel), JT_CLASS_NEW));
                if (!ctor.isShared()) {
                    accessType = Decl.getPrivateAccessType(qmte);
                }
            }
        } else if (methodClassOrCtor instanceof Function
                && ((Function)methodClassOrCtor).isParameter()) {
            callBuilder.invoke(gen.naming.makeQualifiedName(target, (Function)methodClassOrCtor, Naming.NA_MEMBER));
        } else if (methodClassOrCtor instanceof Function) {
            callBuilder.invoke(gen.naming.makeQualifiedName(target, (Function)methodClassOrCtor, Naming.NA_MEMBER));
            if (!((TypedDeclaration)methodClassOrCtor).isShared()) {
                accessType = Decl.getPrivateAccessType(qmte);
            }
        } else if (methodClassOrCtor instanceof Class) {
            Class cls = (Class)methodClassOrCtor;
            if (Strategy.generateInstantiator(cls)) {
                callBuilder.invoke(gen.naming.makeInstantiatorMethodName(target, cls));
            } else {
                callBuilder.instantiate(new ExpressionAndType(target, null),
                        gen.makeJavaType(cls.getType(), JT_CLASS_NEW | AbstractTransformer.JT_NON_QUALIFIED));
                if (!cls.isShared()) {
                    accessType = Decl.getPrivateAccessType(qmte);
                }
            }
        } else {
            throw BugException.unhandledDeclarationCase((Declaration)methodClassOrCtor, qmte);
        }
        ListBuffer<ExpressionAndType> reified = new ListBuffer<ExpressionAndType>();
        
        DirectInvocation.addReifiedArguments(gen, producedReference, reified);
        for (ExpressionAndType reifiedArgument : reified) {
            callBuilder.argument(reifiedArgument.expression);
        }
        
        if (Decl.isConstructor((Declaration)methodClassOrCtor)
                && !Decl.isDefaultConstructor(ModelUtil.getConstructor((Declaration)methodClassOrCtor))
                && !Decl.isJavaArrayWith((Constructor)methodClassOrCtor)) {
            // invoke the param class ctor
            Constructor ctor = ModelUtil.getConstructor((Declaration)methodClassOrCtor);
            callBuilder.argument(gen.naming.makeNamedConstructorName(ctor, false));
        }
        for (Parameter parameter : parameterList.getParameters()) {
            callBuilder.argument(gen.naming.makeQuotedIdent(Naming.getAliasedParameterName(parameter)));
        }
        JCExpression innerInvocation = callBuilder.build();
        if (needsCast) {
            innerInvocation = gen.make().TypeCast(gen.makeJavaType(gen.getReturnTypeOfCallable(type)), innerInvocation);
        }
        // Need to worry about boxing for Function and FunctionalParameter 
        if (methodClassOrCtor instanceof TypedDeclaration
                && !Decl.isConstructor((Declaration)methodClassOrCtor)) {
            // use the method return type since the function is actually applied
            Type returnType = gen.getReturnTypeOfCallable(type);
            innerInvocation = gen.expressionGen().applyErasureAndBoxing(innerInvocation, 
                    returnType,
                    // make sure we use the type erased info as it has not been passed to the expression since the
                    // expression is a Callable
                    CodegenUtil.hasTypeErased((TypedDeclaration)methodClassOrCtor),
                    !CodegenUtil.isUnBoxed((TypedDeclaration)methodClassOrCtor), 
                    BoxingStrategy.BOXED, returnType, 0);
        } else if (methodClassOrCtor instanceof Class 
                && Strategy.isInstantiatorUntyped((Class)methodClassOrCtor)) {
            // $new method declared to return Object, so needs typecast
            innerInvocation = gen.make().TypeCast(gen.makeJavaType(
                    ((Class)methodClassOrCtor).getType()), innerInvocation);
        }
        List<JCStatement> innerBody = List.<JCStatement>of(gen.make().Return(innerInvocation));
        inner.useDefaultTransformation(innerBody);
        
        JCExpression callable = inner.build();
        if (!hasOuter) {
            if (memberClassCtorRef) {
                ;
                JCVariableDecl def = gen.makeVar(Unfix.$instance$.toString(), 
                        gen.makeJavaType(((QualifiedMemberOrTypeExpression)qmte.getPrimary()).getPrimary().getTypeModel()), 
                        gen.expressionGen().transformQualifiedMemberPrimary(qmte));
                return gen.make().LetExpr(def,callable);
            }
            return callable;
        }
        
        ParameterList outerPl = new ParameterList();
        Parameter instanceParameter = new Parameter();
        instanceParameter.setName(Naming.name(Unfix.$instance$));
        Value valueModel = new Value();
        instanceParameter.setModel(valueModel);
        valueModel.setName(instanceParameter.getName());
        valueModel.setInitializerParameter(instanceParameter);
        valueModel.setType(accessType);
        valueModel.setUnboxed(false);
        outerPl.getParameters().add(instanceParameter);
        CallableBuilder outer = new CallableBuilder(gen, qmte, typeModel, outerPl);
        outer.parameterTypes = outer.getParameterTypesFromParameterModels();
        List<JCStatement> outerBody = List.<JCStatement>of(gen.make().Return(callable));
        outer.useDefaultTransformation(outerBody);
        outer.companionAccess = Decl.isPrivateAccessRequiringCompanion(qmte);
        if(expectedType != null)
            outer.checkForFunctionalInterface(expectedType);
            
        return outer.build();
    }
    
    class MemberReferenceDefaultValueCall implements DefaultValueMethodTransformation {
//        private Functional methodOrClass;
        MemberReferenceDefaultValueCall(final Functional methodOrClass) {
//            this.methodOrClass = methodOrClass;
        }
        @Override
        public JCExpression makeDefaultValueMethod(AbstractTransformer gen, Parameter defaultedParam, List<JCExpression> defaultMethodArgs) {
            JCExpression qualifier;
            Declaration decl = defaultedParam.getDeclaration();
            if (decl.isStatic()) {
                qualifier = gen.makeStaticQualifier(decl);
            } else {
                qualifier = gen.naming.makeUnquotedIdent(Unfix.$instance$);
            }
            JCExpression fn = gen.naming.makeDefaultedParamMethod(qualifier, 
                                                                  defaultedParam);
            return gen.make().Apply(null, 
                    fn,
                    defaultMethodArgs);
        }
    }
    
    public static CallableBuilder javaStaticMethodReference(CeylonTransformer gen,
            Node node,
            Type typeModel, 
            final Functional methodOrClass, 
            Reference producedReference,
            Type expectedType) {
        final ParameterList parameterList = methodOrClass.getFirstParameterList();
        CallableBuilder inner = new CallableBuilder(gen, node, typeModel, parameterList);
        
        ArrayList<Type> pt = new ArrayList<>();
        for (Parameter p : methodOrClass.getFirstParameterList().getParameters()) {
            pt.add(((Tree.MemberOrTypeExpression)node).getTarget().getTypedParameter(p).getType());
        }
        inner.parameterTypes = pt; 

        inner.defaultValueCall = inner.new MemberReferenceDefaultValueCall(methodOrClass);
        JCExpression innerInvocation = gen.expressionGen().makeJavaStaticInvocation(gen,
                methodOrClass, producedReference, parameterList);
        
        // Need to worry about boxing for Function and FunctionalParameter 
        if (methodOrClass instanceof TypedDeclaration) {
            innerInvocation = gen.expressionGen().applyErasureAndBoxing(innerInvocation, 
                    methodOrClass.getType(),
                    !CodegenUtil.isUnBoxed((TypedDeclaration)methodOrClass), 
                    BoxingStrategy.BOXED, methodOrClass.getType());
        } else if (Strategy.isInstantiatorUntyped((Class)methodOrClass)) {
            // $new method declared to return Object, so needs typecast
            innerInvocation = gen.make().TypeCast(gen.makeJavaType(
                    ((Class)methodOrClass).getType()), innerInvocation);
        }
        List<JCStatement> innerBody = List.<JCStatement>of(gen.make().Return(innerInvocation));
        inner.useDefaultTransformation(innerBody);
        if(expectedType != null)
            inner.checkForFunctionalInterface(expectedType);

        return inner;
    }
    
    /**
     * Used for "static" value references. For example:
     * <pre>
     *     value x = Integer.plus;
     *     value y = Foo.method;
     *     value z = Outer.Inner;
     * </pre>
     */
    public static CallableBuilder unboundValueMemberReference(
            CeylonTransformer gen,
            Tree.QualifiedMemberOrTypeExpression qmte,
            Type typeModel,
            final TypedDeclaration value,
            Type expectedType) {
        CallBuilder callBuilder = CallBuilder.instance(gen);
        Type qualifyingType = qmte.getTarget().getQualifyingType();
        JCExpression target = gen.naming.makeUnquotedIdent(Unfix.$instance$);
        target = gen.expressionGen().applyErasureAndBoxing(target, qmte.getPrimary().getTypeModel(), true, BoxingStrategy.BOXED, qualifyingType);
        if (gen.expressionGen().isThrowableMessage(qmte)) {
            callBuilder.invoke(gen.utilInvocation().throwableMessage());
            callBuilder.argument(target);
        } else if (gen.expressionGen().isThrowableSuppressed(qmte)) {
            callBuilder.invoke(gen.utilInvocation().suppressedExceptions());
            callBuilder.argument(target);
        } else {
            JCExpression memberName = gen.naming.makeQualifiedName(target, value, Naming.NA_GETTER | Naming.NA_MEMBER);
            if(value instanceof FieldValue){
                callBuilder.fieldRead(memberName);
            }else{
                callBuilder.invoke(memberName);
            }
        }
        JCExpression innerInvocation = callBuilder.build();
        // use the return type since the value is actually applied
        Type returnType = gen.getReturnTypeOfCallable(typeModel);
        innerInvocation = gen.expressionGen().applyErasureAndBoxing(innerInvocation, returnType, 
                // make sure we use the type erased info as it has not been passed to the expression since the
                // expression is a Callable
                qmte.getTypeErased(), !CodegenUtil.isUnBoxed(value), 
                BoxingStrategy.BOXED, returnType, 0);
        
        ParameterList outerPl = new ParameterList();
        Parameter instanceParameter = new Parameter();
        instanceParameter.setName(Naming.name(Unfix.$instance$));
        Value valueModel = new Value();
        instanceParameter.setModel(valueModel);
        Type accessType = gen.getParameterTypeOfCallable(typeModel, 0);;
        if (!value.isShared()) {
            accessType = Decl.getPrivateAccessType(qmte);
        }
        valueModel.setName(instanceParameter.getName());
        valueModel.setInitializerParameter(instanceParameter);
        valueModel.setType(accessType);
        valueModel.setUnboxed(false);
        outerPl.getParameters().add(instanceParameter);
        CallableBuilder outer = new CallableBuilder(gen, qmte, typeModel, outerPl);
        outer.parameterTypes = outer.getParameterTypesFromParameterModels();
        List<JCStatement> innerBody = List.<JCStatement>of(gen.make().Return(innerInvocation));
        outer.useDefaultTransformation(innerBody);
        outer.companionAccess = Decl.isPrivateAccessRequiringCompanion(qmte);
        if(expectedType != null)
            outer.checkForFunctionalInterface(expectedType);
        
        return outer;
    }
    
    /**
     * Constructs an {@code AbstractCallable} suitable for an anonymous function.
     */
    public static CallableBuilder anonymous(
            CeylonTransformer gen,
            Node node,
            FunctionOrValue model,
            Tree.Expression expr,  
            java.util.List<Tree.ParameterList> parameterListTree, 
            Type callableTypeModel, boolean delegateDefaultedCalls) {
        boolean prevSyntheticClassBody = gen.expressionGen().withinSyntheticClassBody(true);
        JCExpression transformedExpr = gen.expressionGen().transformExpression(expr, BoxingStrategy.BOXED, gen.getReturnTypeOfCallable(callableTypeModel));
        gen.expressionGen().withinSyntheticClassBody(prevSyntheticClassBody);
        final List<JCStatement> stmts = List.<JCStatement>of(gen.make().Return(transformedExpr));
        
        return methodArgument(gen, node, model, callableTypeModel, parameterListTree, stmts, delegateDefaultedCalls);
    }

    public static CallableBuilder methodArgument(
            CeylonTransformer gen,
            Node node,
            Function model,
            Type callableTypeModel,
            java.util.List<Tree.ParameterList> parameterListTree, 
            List<JCStatement> stmts) {
        return methodArgument(gen, node, model,callableTypeModel, parameterListTree, stmts, true);
    }
    
    /**
     * Transforms either a method argument in a named invocation, or an 
     * anonymous method. For example:
     * <pre>
     *     namedInvocation(
     *         methodArgument() {
     *         }
     *     );
     * </pre>
     * or
     * <pre>
     *     Integer() x;
     *     x => () => 1;
     * </pre>
     * 
     */
    private static CallableBuilder methodArgument(
            CeylonTransformer gen,
            Node node,
            FunctionOrValue model,
            Type callableTypeModel,
            java.util.List<Tree.ParameterList> parameterListTree, 
            List<JCStatement> stmts, boolean delegateDefaultedCalls) {
        
        for (int ii = parameterListTree.size()-1; ii > 0; ii-- ) {
            Tree.ParameterList pl = parameterListTree.get(ii);
            Type t = callableTypeModel;
            for (int jj = 0; jj < ii; jj++) {
                t = gen.getReturnTypeOfCallable(t);
            }
            CallableBuilder cb = new CallableBuilder(gen, node, t, pl.getModel());
            cb.parameterTypes = cb.getParameterTypesFromParameterModels();
            cb.parameterDefaultValueMethods(pl);
            cb.delegateDefaultedCalls = delegateDefaultedCalls;
            cb.useDefaultTransformation(stmts);
            stmts = List.<JCStatement>of(gen.make().Return(cb.build()));
        }
        CallableBuilder cb = new CallableBuilder(gen, node, callableTypeModel, parameterListTree.get(0).getModel());
        cb.parameterTypes = cb.getParameterTypesFromParameterModels();
        cb.parameterDefaultValueMethods(parameterListTree.get(0));
        cb.delegateDefaultedCalls = delegateDefaultedCalls;
        cb.useDefaultTransformation(stmts);
        cb.annotations = gen.makeAtMethod().prependList(gen.makeAtName(model.getName())).prependList(gen.makeAtLocalDeclaration(model.getQualifier(), false));
        return cb;
    }
    
    
    /**
     * Constructs an {@code AbstractCallable} suitable for use in a method 
     * definition with a multiple parameter lists (i.e. an intermediate result 
     * from method with multiple parameter lists). For example
     * <pre>
     *     String name(String first)(String second) => first + " " + second;
     * </pre>
     */
    public static CallableBuilder mpl(
            CeylonTransformer gen,
            Type typeModel,
            ParameterList parameterList,
            Tree.ParameterList parameterListTree,
            List<JCStatement> body) {

        CallableBuilder cb = new CallableBuilder(gen, parameterListTree, typeModel, parameterList);
        if (body == null) {
            body = List.<JCStatement>nil();
        }
        cb.parameterTypes = cb.getParameterTypesFromParameterModels();
        cb.parameterDefaultValueMethods(parameterListTree);
        cb.useDefaultTransformation(body);
        return cb;
    }

    public int getMinimumParameters() {
        return minimumParams;
    }
    
    public int getMinimumArguments() {
        return minimumArguments;
    }
    
    /**
     * Abstraction over things which can generate a method to add to 
     * the AbstractCallable we're building.
     */
    abstract class MethodWithArity {
        /**
         * Makes a method with the given (Java) arity.
         */
        abstract MethodDefinitionBuilder makeMethod(int arity);
        /**
         * <p>Makes a variable declaration for a variable which will be used as an 
         * argument to a call. The variable is initialized to either the 
         * corresponding parameter,</p> 
         * <pre>
         *     Foo foo = (Foo)arg0;
         * </pre>
         * <p>or the default value for the corresponding parameter</p>
         * <pre>
         *     Bar bar = $$bar(**previous-args**);
         * </pre>
         */
        protected void makeDowncastOrDefaultVar(
                ListBuffer<JCStatement> stmts,
                Naming.SyntheticName name,
                boolean boxed,
                final Parameter param, 
                final int a, final int arity) {
            JCExpression varInitialExpression = makeDowncastOrDefault(param, boxed, a, arity);
            makeVarForParameter(stmts, param, parameterTypes.get(a), 
                    name, boxed, varInitialExpression);
        }
        
        /**
         * Makes an expression, (appropriately downcasted or unboxed) for the
         * given parameter of the {@code $call()} method.
         * @param boxed 
         */
        private JCExpression makeCallParameterExpr(Parameter param, boolean boxed, int argIndex, boolean varargs) {
            Type paramType = gen.typeFact().denotableType(parameterTypes.get(Math.min(argIndex, numParams-1)));
            return makeParameterExpr(param, argIndex, paramType, boxed, varargs);
        }
        
        /**
         * Makes an expression, (appropriately downcasted or unboxed) for the
         * given parameter of the {@code $call()} method.
         */
        protected JCExpression makeParameterExpr(Parameter param, int argIndex, Type paramType, boolean boxed, boolean ellipsis){
            JCExpression argExpr;
            if (!ellipsis) {
                // The Callable has overridden one of the non-ellipsis $call$() 
                // methods
                argExpr = makeParamIdent(gen, argIndex);
            } else {
                // The Callable has overridden the ellipsis $call$() method
                // so we need to index into the ellipsis array
                argExpr = gen.make().Indexed(
                        makeParamIdent(gen, 0), 
                        gen.make().Literal(argIndex));
            }
            int ebFlags = ExpressionTransformer.EXPR_DOWN_CAST; // we're effectively downcasting it from Object
            BoxingStrategy boxingStrategy;
            if(!boxed && isValueTypeCall(param, paramType))
                boxingStrategy = BoxingStrategy.UNBOXED;
            else
                boxingStrategy = CodegenUtil.getBoxingStrategy(param.getModel());
            // if we have unchecked nulls, pretend we have an optional so unboxing doesn't throw
            if(param.getModel().hasUncheckedNullType())
                paramType = gen.typeFact().getOptionalType(paramType);
            argExpr = gen.expressionGen().applyErasureAndBoxing(argExpr, 
                    paramType, // it came in as Object, but we need to pretend its type
                    // is the parameter type because that's how unboxing determines how it has to unbox
                    true, // it's completely erased
                    true, // it came in boxed
                    boxingStrategy, // see if we need to box 
                    paramType, // see what type we need
                    ebFlags);
            if (companionAccess) {
                argExpr = gen.naming.makeCompanionAccessorCall(argExpr, (Interface)paramType.getType().getDeclaration());
            }
            return argExpr;
        }
        
        protected JCExpression makeDowncastOrDefault(final Parameter param,
                boolean boxed, final int a, final int arity) {
            // read the value
            JCExpression paramExpression = makeCallParameterExpr(param, boxed, a, arity > CALLABLE_MAX_FIZED_ARITY);
            JCExpression varInitialExpression;
            // TODO Suspicious
            if(param.isDefaulted() || param.isSequenced()){
                if(arity > CALLABLE_MAX_FIZED_ARITY){
                    // must check if it's defined
                    JCExpression test = gen.make().Binary(JCTree.Tag.GT, gen.makeSelect(getParamName(0), "length"), gen.makeInteger(a));
                    JCExpression elseBranch = makeDefaultValueCall(param, a);
                    varInitialExpression = gen.make().Conditional(test, paramExpression, elseBranch);
                }else if(a >= arity && Strategy.hasDefaultParameterValueMethod(param)){
                    // get its default value because we don't have it
                    varInitialExpression = makeDefaultValueCall(param, a);
                }else{
                    // we must have it
                    varInitialExpression = paramExpression;
                }
            }else{
                varInitialExpression = paramExpression;
            }
            return varInitialExpression;
        }
        /**
         * Makes an invocation of a default parameter value method (if the 
         * parameter is defaulted), or empty (if it's *variadic)
         */
        private JCExpression makeDefaultValueCall(Parameter defaultedParam, int i){
            if (Strategy.hasDefaultParameterValueMethod(defaultedParam)) {
                // add the default value
                ListBuffer<JCExpression> defaultMethodArgs = new ListBuffer<JCExpression>();
                // pass reified type arguments
                for (TypeParameter tp : Strategy.getEffectiveTypeParameters(defaultedParam.getDeclaration())) {
                    Type ta = ((Tree.MemberOrTypeExpression)node).getTarget().getTypeArguments().get(tp);
                    defaultMethodArgs.add(gen.makeReifiedTypeArgument(ta));
                }
                
                // pass all the previous values
                for(int a=0;a<i;a++){
                    Parameter param = paramLists.getParameters().get(a);
                    JCExpression previousValue = getCallableTempVarName(param).makeIdent();
                    defaultMethodArgs.add(previousValue);
                }
                

                // now call the default value method
                return defaultValueCall.makeDefaultValueMethod(gen, defaultedParam, defaultMethodArgs.toList());
            } else if (Strategy.hasEmptyDefaultArgument(defaultedParam)) {
                return gen.makeEmptyAsSequential(true);
            }
            throw new BugException(defaultedParam.getName() + " is not a defaulted parameter");
        }
        
        protected Naming.SyntheticName getCallableTempVarName(Parameter param) {
            return gen.naming.synthetic(Naming.getAliasedParameterName(param));
        }
        
        protected final void makeVarForParameter(ListBuffer<JCStatement> stmts,
                final Parameter param, Type parameterType,
                Naming.SyntheticName name, boolean boxed, JCExpression expr) {
            // store it in a local var
            int flags = 0;
            boolean castRequired = false;
            if ((parameterType.isExactlyNothing()
                    || gen.willEraseToObject(parameterType))) {
                Type et = param.getType();
                while (et.getDeclaration() instanceof TypeParameter
                    && !et.getSatisfiedTypes().isEmpty()) {
                    et = et.getSatisfiedTypes().get(0);
                }
                if (param.getType() != et) {
                    parameterType = et;
                    castRequired = true;
                    flags |= AbstractTransformer.JT_RAW;
                }
            }
            flags |= jtFlagsForParameter(param, parameterType, boxed);
            if (castRequired && !gen.willEraseToObject(parameterType)) {
                expr = gen.make().TypeCast(gen.makeJavaType(parameterType, flags), expr);
            }
            JCVariableDecl var = gen.make().VarDef(gen.make().Modifiers(param.getModel().isVariable() ? 0 : Flags.FINAL), 
                    name.asName(), 
                    gen.makeJavaType(parameterType, flags),
                    expr);
            stmts.append(var);
            if (ParameterDefinitionBuilder.isBoxedVariableParameter(param)) {
                stmts.append(gen.makeVariableBoxDecl(name.makeIdent(), param.getModel()));
            }
        }
        protected int jtFlagsForParameter(final Parameter param, Type parameterType, boolean boxed) {
            int flags = 0;
            if(!CodegenUtil.isUnBoxed(param.getModel()) && (!isValueTypeCall(param, parameterType) || boxed)){
                flags |= AbstractTransformer.JT_NO_PRIMITIVES;
            }
            if (companionAccess) {
                flags |= AbstractTransformer.JT_COMPANION;
            }
            return flags;
        }

        protected boolean isValueTypeCall(Parameter param, Type parameterType) {
            return false;
        }
    }
    
    class CallMethodWithGivenBody extends MethodWithArity {
        
        private List<JCStatement> body;
        private boolean usedBody = false;

        CallMethodWithGivenBody(List<JCStatement> body) {
            this.body = body;
        }

        @Override
        MethodDefinitionBuilder makeMethod(int arity) {
            if (arity < Math.min(getMinimumArguments(), CALLABLE_MAX_FIZED_ARITY+1)) {
                return null;
            }
            if (usedBody) {
                body = List.<JCStatement>of(gen.make().Exec(gen.makeErroneous(null, "compiler bug: tree reuse detected")));
            }
            usedBody = true;
            ListBuffer<JCStatement> stmts = new ListBuffer<JCStatement>();
            int a = 0;
            for(Parameter param : paramLists.getParameters()){
                // don't read default parameter values for forwarded calls
                makeDowncastOrDefaultVar(stmts, getCallableTempVarName(param), false, param, a, arity);
                a++;
            }
            return makeCallMethod(stmts.appendList(body).toList(), arity);
        }
    }
    
    class CallMethodWithForwardedBody extends MethodWithArity {
        
        final boolean isCallMethod;
        private final Tree.Term forwardCallTo;
        private final Naming.SyntheticName instanceFieldName;
        private final boolean instanceFieldIsBoxed;
        private final Type callableType;

        CallMethodWithForwardedBody(Naming.SyntheticName instanceFieldName, 
                boolean instanceFieldIsBoxed, 
                Tree.Term forwardCallTo, 
                boolean isCallMethod,
                Type callableType) {
            this.instanceFieldName = instanceFieldName;
            this.instanceFieldIsBoxed = instanceFieldIsBoxed;
            this.forwardCallTo = forwardCallTo;
            this.isCallMethod = isCallMethod;
            this.callableType = callableType;
        }

        @Override
        protected Naming.SyntheticName getCallableTempVarName(Parameter param) {
            // prefix them with $$ if we only forward, otherwise we need them to have the proper names
            return gen.naming.synthetic(Naming.getCallableTempVarName(param));
        }
        
        @Override
        protected int jtFlagsForParameter(final Parameter param, Type parameterType, boolean boxed) {
            int flags = super.jtFlagsForParameter(param, parameterType, boxed);
            // Always go raw if we're forwarding, because we're building the call ourselves and we don't get a chance to apply erasure and
            // casting to parameter expressions when we pass them to the forwarded method. Ideally we could set it up correctly so that
            // the proper erasure is done when we read from the Callable.call Object param, but since we store it in a variable defined here,
            // we'd need to duplicate some of the erasure logic here to make or not the type raw, and that would be worse.
            // Besides, named parameter invocation does the same.
            // See https://github.com/ceylon/ceylon-compiler/issues/1005
            flags |= AbstractTransformer.JT_RAW;
            return flags;
        }

        @Override
        protected boolean isValueTypeCall(Parameter param, Type parameterType) {
            if(!param.isSequenced()
                    && forwardCallTo instanceof Tree.QualifiedMemberExpression){
                Tree.Primary primary = ((Tree.QualifiedMemberExpression) forwardCallTo).getPrimary();
                return Decl.isValueTypeDecl(primary.getTypeModel())
                        && Decl.isValueTypeDecl(parameterType);
            }
            return false;
        }
        
        @Override
        MethodDefinitionBuilder makeMethod(int arity) {
            if (arity < Math.min(getMinimumArguments(), CALLABLE_MAX_FIZED_ARITY+1)) {
                return null;
            }
            ListBuffer<JCStatement> stmts = new ListBuffer<JCStatement>();
            if (isCallMethod) {
                int a = 0;
                for(Parameter param : paramLists.getParameters()){
                    // don't read default parameter values for forwarded calls
                    // if we are in a call method below the variadic one,
                    // otherwise consume every parameter
                    if(arity <= CALLABLE_MAX_FIZED_ARITY 
                            /*&& forwardCallTo != null */&& arity == a)
                        break;
                    makeDowncastOrDefaultVar(stmts, getCallableTempVarName(param), instanceFieldIsBoxed, param, a, arity);
                    a++;
                }
            }
            
            JCExpression invocation = makeInvocation(arity, isCallMethod);
            stmts.append(gen.make().Return(invocation));
            
            return isCallMethod ? makeCallMethod(stmts.toList(), arity) : makeCallTypedMethod(stmts.toList());
        }
        private JCExpression makeInvocation(int arity, boolean isCallMethod) {
            Reference target = appliedReference();
            CallableInvocation invocationBuilder = new CallableInvocation (
                    gen,
                    instanceFieldName,
                    instanceFieldIsBoxed,
                    forwardCallTo,
                    target.getDeclaration(),
                    target,
                    gen.getReturnTypeOfCallable(getTypeModel()),
                    forwardCallTo, 
                    paramLists,
                    // if we are in a call method below the variadic one, respect arity, otherwise use the parameter list
                    // size to forward all the arguments
                    arity <= CALLABLE_MAX_FIZED_ARITY ? arity : paramLists.getParameters().size(), 
                    isCallMethod);
            boolean prevCallableInv = gen.expressionGen().withinSyntheticClassBody(true);
            JCExpression invocation;
            try {
                invocation = gen.expressionGen().transformInvocation(invocationBuilder);
            } finally {
                gen.expressionGen().withinSyntheticClassBody(prevCallableInv);
            }
            return invocation;
        }

        private Type getTypeModel() {
            return callableType;
        }

        private Reference appliedReference() {
            Reference target;
            if (forwardCallTo instanceof Tree.MemberOrTypeExpression) {
                target = ((Tree.MemberOrTypeExpression)forwardCallTo).getTarget();
            } else if (forwardCallTo instanceof Tree.FunctionArgument) {
                Function method = ((Tree.FunctionArgument) forwardCallTo).getDeclarationModel();
                target = method.appliedReference(null, Collections.<Type>emptyList());
            } else if (forwardCallTo instanceof Tree.InvocationExpression) {
                target = ((Tree.InvocationExpression)forwardCallTo).getTypeModel();
            } else {
                throw new RuntimeException(forwardCallTo.getNodeType());
            }
            return target;
        }
    }
    
    /**
     * Abstraction over various kinds of {@code $call()} method
     */
    abstract class CallableTransformation {
        
        final void appendMethods(ListBuffer<JCTree> classBody) {
            if (delegateDefaultedCalls) {
                // now generate a method for each supported minimum number of parameters below 4
                // which delegates to the $call$typed method if required
                for(int javaArity=0; javaArity <= CALLABLE_MAX_FIZED_ARITY+1; javaArity++){
                    for (MethodDefinitionBuilder mdb : makeMethodsForArity(javaArity)) {
                        if (mdb != null) {
                            classBody.append(mdb.build());
                        }
                    }
                }
            } else {
                // generate the $call method for the max number of parameters,
                // (which delegates to the $call$typed method if required)
                for (MethodDefinitionBuilder mdb : makeMethodsForArity(numParams)) {
                    classBody.append(mdb.build());
                }
            }
            
            // if required, generate the $call$typed method 
            MethodDefinitionBuilder callTypedMethod = makeCallTypedMethod();
            if (callTypedMethod != null) {
                JCMethodDecl callTyped = callTypedMethod.build();
                if (callTyped.params.size() != numParams) {
                    throw new BugException();
                }
                classBody.append(callTyped);
            }
            // if required, implement the functional interface method bridge
            if (functionalInterfaceMethod != null){
                MethodDefinitionBuilder mdb = makeFunctionalInterfaceMethod();
                classBody.append(mdb.build());
            }
        }
        
        /**
         * Make the public {@code $call$()} and {@code $call$variadic()} methods
         * with the given arity.
         * @param arity
         * @return
         */
        protected abstract Iterable<MethodDefinitionBuilder> makeMethodsForArity(int arity);
        
        /**
         * Make the private {@code $call$typed()} method, whose arity must 
         * be {@link #numParams}, or return null if 
         * no {@code $call$typed()} is required
         * @return
         */
        abstract MethodDefinitionBuilder makeCallTypedMethod();
    }
    
    protected final MethodDefinitionBuilder makeCallMethod(List<JCStatement> body, int arity) {
        MethodDefinitionBuilder callMethod = MethodDefinitionBuilder.callable(gen);
        // we only override it if we're not building a functional interface class
        callMethod.isOverride(functionalInterfaceMethod == null);
        callMethod.modifiers(Flags.PUBLIC);
        Type returnType = gen.getReturnTypeOfCallable(typeModel);
        callMethod.resultType(gen.makeJavaType(returnType, JT_NO_PRIMITIVES), null);
        // Now append formal parameters
        switch (arity) {
        case 3:
            callMethod.parameter(makeCallableCallParam(0, arity-3));
            // fall through
        case 2:
            callMethod.parameter(makeCallableCallParam(0, arity-2));
            // fall through
        case 1:
            callMethod.parameter(makeCallableCallParam(0, arity-1));
            break;
        case 0:
            break;
        default: // use varargs
            callMethod.parameter(makeCallableCallParam(Flags.VARARGS, 0));
        }
        
        // Return the call result, or null if a void method
        callMethod.body(body);
        return callMethod;
    }
    
    public MethodDefinitionBuilder makeFunctionalInterfaceMethod() {
        FunctionOrValue methodOrValue = (FunctionOrValue) functionalInterfaceMethod.getDeclaration();
        MethodDefinitionBuilder callMethod = 
                MethodDefinitionBuilder.method(gen, methodOrValue, 0);
        callMethod.isOverride(true);
        callMethod.modifiers(Flags.PUBLIC);
        Type returnType = functionalInterfaceMethod.getType();
        if(methodOrValue instanceof Value || !((Function)methodOrValue).isDeclaredVoid()){
            int flags = CodegenUtil.isUnBoxed(methodOrValue) 
                    ? 0 : JT_NO_PRIMITIVES;
            callMethod.resultType(gen.makeJavaType(returnType, flags), null);
        }
        ListBuffer<JCExpression> args = new ListBuffer<>();
        // this depends on how we're declared to Java
        boolean turnedToRaw = gen.rawSupertype(functionalInterface, JT_EXTENDS | JT_CLASS_NEW);
        boolean variadic = false;
        if(methodOrValue instanceof Function){
            int index = 0;
            java.util.List<Parameter> parameters = 
                    ((Function)methodOrValue)
                        .getFirstParameterList()
                        .getParameters();
            for (int i=0; i<parameters.size(); i++) {
                Parameter param = parameters.get(i);
                Parameter targetParam = paramLists.getParameters().get(i); 
                TypedReference typedParameter = functionalInterfaceMethod.getTypedParameter(param);
                ParameterDefinitionBuilder pdb = ParameterDefinitionBuilder.systemParameter(gen, param.getName());
                Type paramType = typedParameter.getType();
                if(param.isSequenced()){
                    paramType = gen.typeFact().getSequentialElementType(paramType);
                    variadic = true;
                }
                // This is very special-casey, but is required and I haven't found more subtle
                if(turnedToRaw && gen.simplifyType(param.getType()).isTypeParameter()){
                    paramType = gen.typeFact().getObjectType();
                }
                long flags = Flags.FINAL;
                // that looks fishy, perhaps we need to call non-widening rules instead
                JCExpression javaType = gen.makeJavaType(paramType, paramType.isRaw() ? AbstractTransformer.JT_RAW : 0);
                if(param.isSequenced()){
                    flags |= Flags.VARARGS;
                    javaType = gen.make().TypeArray(javaType);
                }
                pdb.type(new TransformedType(javaType));
                pdb.modifiers(flags);
                callMethod.parameter(pdb);
                JCExpression arg;
                if(param.isSequenced()){
                    arg = gen.javaVariadicToSequential(paramType, param);
                }else{
                    arg = gen.makeUnquotedIdent(param.getName());
                    Type argumentType = parameterTypes.get(index);
                    if(gen.isOptional(paramType)
                            && argumentType.isSubtypeOf(gen.typeFact().getObjectType())
                            && !targetParam.getModel().hasUncheckedNullType()){
                        arg = gen.utilInvocation().checkNull(arg);
                    }
                    Type simpleParamType = gen.simplifyType(paramType);
                    // make unboxed java strings pass for ceylon strings so we can box them
                    boolean unboxedString = gen.isJavaStringExactly(simpleParamType);
                    if(unboxedString)
                        simpleParamType = gen.typeFact().getStringType();
                    BoxingStrategy boxingStrategy = BoxingStrategy.BOXED;
                    // in rare cases we don't want boxes
                    if(unboxedString && gen.isJavaStringExactly(argumentType))
                        boxingStrategy = BoxingStrategy.UNBOXED;
                    arg = gen.expressionGen().applyErasureAndBoxing(arg, simpleParamType, 
                            !(CodegenUtil.isUnBoxed(param.getModel())
                                    || unboxedString), 
                            boxingStrategy,
                            argumentType);
                }
                args.append(arg);
                index++;
            }
        }else{
            // no-arg getter
        }
        String callMethodName;
        if(variadic)
            callMethodName = Naming.getCallableVariadicMethodName();
        else
            callMethodName = Naming.getCallableMethodName();
        JCExpression call = gen.make().Apply(null, gen.makeUnquotedIdent(callMethodName), args.toList());
        JCStatement body;
        if(methodOrValue instanceof Function 
                && ((Function)methodOrValue).isDeclaredVoid())
            body = gen.make().Exec(call);
        else{
            Type callableReturnType = gen.getReturnTypeOfCallable(typeModel);
            Type simpleReturnType = gen.simplifyType(returnType);
            boolean unboxed = CodegenUtil.isUnBoxed(methodOrValue)
                    || gen.isJavaStringExactly(simpleReturnType);
            if(unboxed){
                call = gen.expressionGen().applyErasureAndBoxing(call, 
                        callableReturnType, 
                        true, 
                        BoxingStrategy.UNBOXED,
                        returnType);
            }else if(callableReturnType.isNull()){
                // if the callable returns null and the SAM does not, we need a cast
                call = gen.expressionGen().applyErasureAndBoxing(call, 
                        callableReturnType, 
                        true, 
                        BoxingStrategy.INDIFFERENT,
                        returnType);
            }
            body = gen.make().Return(call);
        }
        callMethod.body(body);
        return callMethod;
    }

    /**
     * Builds a {@code $call()} method that
     * <ul>
     * <li>downcasts the parameters to their correct type,</li>
     * <li>adds any default arguments, and<li>
     * <li>delegates to the {@code $call$typed()} method</li>
     * </ul>
     */
    class FixedArityCallableTransformation extends CallableTransformation {
        
        private final MethodWithArity call;
        private final MethodWithArity callTyped;
        
        FixedArityCallableTransformation(MethodWithArity call, MethodWithArity callTyped) {
            this.call = call;
            this.callTyped = callTyped;
        }
        
        @Override
        protected List<MethodDefinitionBuilder> makeMethodsForArity(int arity) {
            if (arity > numParams) {
                return List.nil();
            }
            
            return List.of(call.makeMethod(arity));
        }
        
        @Override
        MethodDefinitionBuilder makeCallTypedMethod() {
            return callTyped != null ? callTyped.makeMethod(numParams) : null;
        }
    }
    
    abstract class VariadicMethodWithArity extends MethodWithArity {
        
        protected final JCExpression makeRespread(List<JCExpression> arguments) {
            JCExpression spreadVarargs;
            if(functionalInterface != null){
                spreadVarargs = gen.naming.makeQuotedQualIdent(gen.makeIdent(gen.syms().ceylonAbstractCallableType), 
                        Naming.name(Unfix.$spreadVarargs$));
            }else{
                spreadVarargs = gen.naming.makeUnquotedIdent(Naming.name(Unfix.$spreadVarargs$));
            }
            JCExpression invocation = gen.make().Apply(null, 
                    spreadVarargs, 
                    arguments);
            if (getVariadicParameter().isAtLeastOne()) {
                invocation = gen.make().TypeCast(
                        gen.makeJavaType(getVariadicType(), AbstractTransformer.JT_RAW), 
                        invocation);
            }
            return invocation;
        }
        
        protected final SyntheticName parameterName(int a) {
            Parameter param = paramLists.getParameters().get(a);
            SyntheticName name = getCallableTempVarName(param);
            return name;
        }
        
        protected final boolean parameterSequenced(int a) {
            return paramLists.getParameters().get(a).isSequenced();
        }
        
        protected Parameter getVariadicParameter() {
            return paramLists.getParameters().get(numParams - 1);
        }
        
        protected Type getVariadicType() {
            return parameterTypes.get(numParams - 1);
        }
        
        protected Type getVariadicIteratedType() {
            return gen.typeFact().getIteratedType(getVariadicType());
        }
        
        /** Makes an argument that's just the ident of the parameter {@code $param$n}*/
        protected final void makeParameterArgument(final int arity,
                ListBuffer<JCStatement> stmts, ListBuffer<JCExpression> args,
                int a) {
            SyntheticName name = parameterName(a);
            Parameter param = paramLists.getParameters().get(a);
            makeDowncastOrDefaultVar(stmts, 
                    name, false, param, a, arity);
            args.append(name.makeIdent());
        }
        
        /**
         * Makes a call to {@code $call$typed()} if required, otherwise uses the 
         * given body.
         * @return 
         */
        protected final JCMethodInvocation makeCallTypedCall(final int arity, List<JCExpression> args) {
            JCMethodInvocation chain = gen.make().Apply(null, gen.makeUnquotedIdent(Naming.getCallableTypedMethodName()), args);
            return chain;
        }
        
        /** Appends a single argument out of */
        protected final int makeSequencedArgument(final int arity,
                ListBuffer<JCStatement> stmts, ListBuffer<JCExpression> args,
                int a) {
            ListBuffer<JCExpression> varargs = new ListBuffer<JCExpression>();
            for (; a < arity; a++) {
                if (arity < numParams - 1) {
                    Parameter param1 = paramLists.getParameters().get(Math.min(a, numParams-1));
                    makeDowncastOrDefaultVar(stmts, 
                            parameterName(Math.min(a, numParams-1)), false, param1, a, arity);
                } else {
                    varargs.append(gen.make().Ident(makeParamName(gen, a)));
                }
            }
            JCExpression varargsSequence;
            if (varargs.isEmpty()) {
                varargsSequence = gen.makeEmptyAsSequential(true);
            } else {
                varargsSequence = gen.makeSequence(varargs.toList(), 
                        getVariadicIteratedType(), 0);
            }
            SyntheticName vname = getCallableTempVarName(getVariadicParameter()).suffixedBy(Suffix.$variadic$);
            args.append(vname.makeIdent());
            makeVarForParameter(stmts, getVariadicParameter(), getVariadicType(), 
                    vname, false, varargsSequence);
            return a;
        }
        
        /** 
         * Constructs an argument list for the target method as follows:
         * <ol>
         * <li>uses the declared parameters {@code $param$0}, {@code $param$1}, ...,
         * <li>if the declared target parameter is sequenced collects all remaining 
         *     parameters into a sequential target argument.
         * <li>TODO Does some other shit
         * </ol>
         */
        protected final void case3(final int arity, ListBuffer<JCStatement> stmts,
                ListBuffer<JCExpression> args) {
            int a = 0;
            for (; a <= arity; a++) {
                if (parameterSequenced(a)) {
                    // wrap this and all remaining parameters into a sequential
                    a = makeSequencedArgument(arity, stmts, args, a);
                    break;
                }
                if (a < getMinimumArguments()) {
                    makeParameterArgument(arity, stmts, args, a);
                } else {
                    break;
                }
            }
            for (; args.size() < numParams; a++) {
                if (parameterSequenced(a)) {
                    a = makeSequencedArgument(arity, stmts, args, a);
                } else {
                    makeParameterArgument(arity, stmts, args, a);
                }
            }
        }
    }
    
    /**
     * Generates {@code $call()} methods for variadic Callables
     */
    class CallMethodForVariadic extends VariadicMethodWithArity {
        
        /** 
         * Make the {@code $call()} method, which delegates to the $call$typed() 
         */
        @Override
        MethodDefinitionBuilder makeMethod(final int arity) {
            if (arity < Math.min(getMinimumArguments(), CALLABLE_MAX_FIZED_ARITY+1)) {
                return null;
            }
            ListBuffer<JCStatement> stmts = new ListBuffer<JCStatement>();
            ListBuffer<JCExpression> args = new ListBuffer<JCExpression>();
            if (arity <= CALLABLE_MAX_FIZED_ARITY) {
                case3(arity, stmts, args);
            } else { 
                makeEllipsisMethod(arity, stmts, args);
            }
            stmts.append(gen.make().Return(makeCallTypedCall(arity, args.toList())));
            MethodDefinitionBuilder callMethod = CallableBuilder.this.makeCallMethod(stmts.toList(), arity);
            return callMethod;
        }

        private void makeEllipsisMethod(final int arity,
                ListBuffer<JCStatement> stmts, ListBuffer<JCExpression> args) {
            // we're generating the $call(Object...) method
            // pass along the parameters
            int a = 0;
            for(;a < paramLists.getParameters().size(); a++){
                // don't read default parameter values for forwarded calls
                if(parameterSequenced(a)) {
                    break;
                }
                makeParameterArgument(arity, stmts, args, a);
            }
            if (a < paramLists.getParameters().size()) {
                // there are still parameters needing arguments...
                args.append(makeRespread( 
                        List.<JCExpression>of(
                                gen.makeReifiedTypeArgument(getVariadicIteratedType()),
                                gen.make().Literal(numParams-1),
                                gen.make().Binary(JCTree.Tag.MINUS, gen.naming.makeQualIdent(makeParamIdent(gen,  0), "length"), gen.make().Literal(numParams-1)),
                                makeParamIdent(gen,  0),
                                gen.makeEmpty())));
                
            }
        }
        
    }
    
    /**
     * Generates {@code $call$variadic()} methods for variadic Callables
     */
    class CallVariadicMethodForVariadic extends VariadicMethodWithArity {
        
        private void makeEllipsisMethod(final int arity1,
                ListBuffer<JCStatement> stmts, ListBuffer<JCExpression> args) {
            int a = 0;
            for(Parameter param : paramLists.getParameters()){
                if (param.isSequenced()) {
                    break;
                }
                makeParameterArgument(arity1, stmts, args, a);
                a++;
            }
            ListBuffer<JCExpression> lb = new ListBuffer<JCExpression>();
            for (; a < arity1-1 && a < CALLABLE_MAX_FIZED_ARITY; a++) {
                Parameter param = paramLists.getParameters().get(Math.min(a, numParams-1));
                lb.append(makeParameterExpr(param, a, getVariadicIteratedType(), false, false));
            }
            ListBuffer<JCExpression> spreadCallArgs = new ListBuffer<JCExpression>();
            spreadCallArgs.append(gen.makeReifiedTypeArgument(getVariadicIteratedType()));
            if (arity1 > CALLABLE_MAX_FIZED_ARITY+1) {
                spreadCallArgs.append(gen.make().Literal(args.size()));
                spreadCallArgs.append(makeParamIdent(gen, 0));
            } else {
                spreadCallArgs.append(gen.make().Literal(0));
                spreadCallArgs.append(gen.make().Literal(lb.size()));
                spreadCallArgs.append(gen.make().NewArray(gen.make().QualIdent(gen.syms().objectType.tsym), List.<JCExpression>nil(), lb.toList()));
                spreadCallArgs.append(makeParamIdent(gen, a));
            }
            args.append(makeRespread(spreadCallArgs.toList()));
        }
        
        @Override
        MethodDefinitionBuilder makeMethod(final int arity) {
            final int arity1 = arity+1;// The arity including the Sequential parameter
            if (arity1 < getMinimumArguments()) {
                return null;
            }
            ListBuffer<JCStatement> stmts = new ListBuffer<JCStatement>();
            ListBuffer<JCExpression> args = new ListBuffer<JCExpression>();
            if (arity <= CALLABLE_MAX_FIZED_ARITY) {
                if (arity1 <  getMinimumParameters()) {
                    // We need to extract some arguments from the sequential parameter
                    destructureSequential(arity, stmts, args);
                } else if (arity1 < numParams) {
                    case3(arity, stmts, args);
                } else if (arity1 == numParams) {
                    // The arity of the $callvariadic$ method matches what we're calling
                    useDeclaredParameters(arity, stmts, args);
                } else { // arity1 > numParams
                    // We need to pack some of the last parameters into a sequential argument
                    respread(arity, stmts, args);
                }
            } else {
                makeEllipsisMethod(arity1, stmts, args);
            }
            MethodDefinitionBuilder callVaryMethod = MethodDefinitionBuilder.systemMethod(gen, Naming.getCallableVariadicMethodName());
            // we only override it if we're not building a functional interface class
            callVaryMethod.isOverride(functionalInterfaceMethod == null);
            callVaryMethod.modifiers(Flags.PUBLIC);
            Type returnType = gen.getReturnTypeOfCallable(typeModel);
            callVaryMethod.resultType(gen.makeJavaType(returnType, JT_NO_PRIMITIVES), null);
            // Now append formal parameters
            switch (arity1) {
            case 4:
                callVaryMethod.parameter(makeCallableCallParam(0, arity1-4));
                callVaryMethod.parameter(makeCallableCallParam(0, arity1-3));
                callVaryMethod.parameter(makeCallableCallParam(0, arity1-2));
                callVaryMethod.parameter(makeCallableVaryParam(0, arity1-1));
                break;
            case 3:
                callVaryMethod.parameter(makeCallableCallParam(0, arity1-3));
                callVaryMethod.parameter(makeCallableCallParam(0, arity1-2));
                callVaryMethod.parameter(makeCallableVaryParam(0, arity1-1));
                break;
            case 2:
                callVaryMethod.parameter(makeCallableCallParam(0, arity1-2));
                callVaryMethod.parameter(makeCallableVaryParam(0, arity1-1));
                break;
            case 1:
                callVaryMethod.parameter(makeCallableVaryParam(0, arity1-1));
                break;
            case 0:
                break;
            default: // use varargs
                callVaryMethod.parameter(makeCallableCallParam(Flags.VARARGS, 0));
            }
            
            // Return the call result, or null if a void method
            stmts.append(gen.make().Return(makeCallTypedCall(arity1, args.toList())));
            callVaryMethod.body(stmts.toList());
            return callVaryMethod;
        }

        /** 
         * Constructs an argument list for the target method as follows:
         * <ol>
         * <li>uses the declared parameters {@code $param$0}, {@code $param$1}, ...,
         *     so long as the parameter is not sequenced.
         * <li>if the declared target parameter is sequenced collects all remaining 
         *     parameters into a sequential target argument.
         * <li>TODO Does some other shit
         * <li>Calls {@code AbstractCallable.$spreadVarargs$} to construct a 
         * Sequential argument from the remaining parameters.
         * </ol>
         */
        private void respread(final int arity, ListBuffer<JCStatement> stmts,
                ListBuffer<JCExpression> args) {
            // respread
            int a = 0;
            for (; a < numParams; a++) {
                if (parameterSequenced(a)) {
                    break;
                }
                makeParameterArgument(arity, stmts, args, a);
            }
            ListBuffer<JCExpression> variadicElements = new ListBuffer<JCExpression>();
            for (; a < arity; a++) {
                Parameter param = paramLists.getParameters().get(Math.min(a, numParams-1));
                variadicElements.append(makeParameterExpr(param, a, getVariadicIteratedType(), false, false));
            }
            ListBuffer<JCExpression> spreadCallArgs = new ListBuffer<JCExpression>();
            spreadCallArgs.append(gen.makeReifiedTypeArgument(getVariadicIteratedType()));
            if (arity > CALLABLE_MAX_FIZED_ARITY) {
                spreadCallArgs.append(gen.make().Literal(getMinimumArguments()));
                spreadCallArgs.append(makeParamIdent(gen, 0));
            } else {
                spreadCallArgs.append(gen.make().Literal(0));
                spreadCallArgs.append(gen.make().Literal(variadicElements.size()));
                spreadCallArgs.append(gen.make().NewArray(gen.make().QualIdent(gen.syms().objectType.tsym), List.<JCExpression>nil(), variadicElements.toList()));
                spreadCallArgs.append(makeParamIdent(gen, a));
            }
            args.append(makeRespread(spreadCallArgs.toList()));
        }
        
        /** 
         * Constructs an argument list for the target method as follows:
         * <ol>
         * <li>uses the declared parameters {@code $param$0}, {@code $param$1}, ...,
         * </ol>
         */
        private void useDeclaredParameters(final int arity, ListBuffer<JCStatement> stmts,
                ListBuffer<JCExpression> args) {
            for (int a= 0; a < numParams; a++) {
                makeParameterArgument(arity, stmts, args, a);
            }
        }
        
        
        /** 
         * Constructs an argument list for the target method as follows:
         * <ol>
         * <li>uses the declared parameters {@code $param$0}, {@code $param$1}, ..., 
         * <li>then pulls further arguments from the sequential parameter 
         *     using {@code $param$n.get()}
         * <li>finally then uses {@code $param$n.spanFrom()} on the sequential parameter
         *     to get the sequential argument to the target method
         * </ol>
         */
        private void destructureSequential(final int arity, ListBuffer<JCStatement> stmts,
                ListBuffer<JCExpression> args) {
            // destructuring
            int a = 0;
            for (; a < getMinimumArguments()-1; a++) {
                makeParameterArgument(arity, stmts, args, a);
            }
            for (; a < numParams-1; a++) {
                // Extract from the sequential
                SyntheticName name = parameterName(a);
                JCExpression get = gen.make().Apply(null, 
                        gen.makeQualIdent(makeParamIdent(gen, arity), "get"), 
                        List.<JCExpression>of(gen.expressionGen().applyErasureAndBoxing(gen.make().Literal(a), 
                                gen.typeFact().getIntegerType(), false, BoxingStrategy.BOXED, gen.typeFact().getIntegerType())));
                Parameter param = paramLists.getParameters().get(a);
                get = gen.expressionGen().applyErasureAndBoxing(get, 
                        parameterTypes.get(a), 
                        true, true, 
                        (jtFlagsForParameter(param, parameterTypes.get(a), false) & JT_NO_PRIMITIVES) == 0 ? BoxingStrategy.UNBOXED : BoxingStrategy.BOXED , 
                        parameterTypes.get(a), 0);
                
                makeVarForParameter(stmts, param, parameterTypes.get(a),
                        name, false, get);
                args.append(name.makeIdent());
            }
            // Get the rest of the sequential using spanFrom()
            SyntheticName name = parameterName(numParams-1);
            JCExpression spanFrom = gen.make().Apply(null, 
                    gen.makeQualIdent(makeParamIdent(gen, arity), "spanFrom"), 
                    List.<JCExpression>of(gen.expressionGen().applyErasureAndBoxing(gen.make().Literal(a), 
                            gen.typeFact().getIntegerType(), false, BoxingStrategy.BOXED, gen.typeFact().getIntegerType())));
            spanFrom = gen.expressionGen().applyErasureAndBoxing(spanFrom, 
                    parameterTypes.get(a), 
                    true, true, BoxingStrategy.UNBOXED, 
                    parameterTypes.get(a), 0);
            Parameter param = paramLists.getParameters().get(numParams-1);
            makeVarForParameter(stmts, param, parameterTypes.get(a),
                    name, false, spanFrom);
            args.append(name.makeIdent());
        }
        
        
        
    }
    
    /**
     * <ul>
     * <li>a {@code private $call$typed()} method is generated which encodes
     *     the actual method code,</li>
     * <li>the {@code public $call$variadic()} methods delegate to the 
     *     {@code $call$typed()} method,
     *     A {@code public $call$variadic()} method is generated for each
     *     arity greater than the number of non-defaulted parameters and less
     *     than or equal to {@link #CALLABLE_MAX_FIZED_ARITY}, plus one 
     *     which uses Javac varargs</li>
     * <li>the {@code $call()} methods delegate to the {@code $call$typed()} method,
     *     downcasting and obtaining default arguments if required,</li>
     * <ul>
     */
    class VariadicCallableTransformation extends CallableTransformation {
        
        private final CallMethodForVariadic call;
        private final CallVariadicMethodForVariadic callVariadic;
        private final MethodWithArity callTyped;
        
        VariadicCallableTransformation(MethodWithArity callTyped) {
            this.call = new CallMethodForVariadic();
            this.callVariadic = new CallVariadicMethodForVariadic();
            this.callTyped = callTyped;
        }
        
        @Override
        protected List<MethodDefinitionBuilder> makeMethodsForArity(int arity) {
            List<MethodDefinitionBuilder> result = List.<MethodDefinitionBuilder>nil();
            MethodDefinitionBuilder callVaryMethod = callVariadic.makeMethod(arity);
            if (callVaryMethod != null) {
                result = result.prepend(callVaryMethod);
            }
            MethodDefinitionBuilder callMethod = call.makeMethod(arity);
            if (callMethod != null) {
                result = result.prepend(callMethod);
            }
            return result;
        }
        
        @Override
        MethodDefinitionBuilder makeCallTypedMethod() {
            return callTyped.makeMethod(numParams);
        }
    }
    
    private CallableBuilder useTransformation(CallableTransformation transformation) {
        this.transformation = transformation;
        return this;
    }
    
    
    private CallableBuilder useDefaultTransformation(List<JCStatement> stmts) {
        CallableTransformation tx;
        if (isVariadic) {
            MethodWithArity callTyped = new CallTypedMethod(stmts);
            tx = new VariadicCallableTransformation(callTyped);
        } else if (hasOptionalParameters) {
            MethodWithArity call = new CallMethodForVariadic();
            MethodWithArity callTyped = new CallTypedMethod(stmts);
            tx = new FixedArityCallableTransformation(call, callTyped);
        } else {
            MethodWithArity call = new CallMethodWithGivenBody(stmts);
            MethodWithArity callTyped = null;
            tx = new FixedArityCallableTransformation(call, callTyped);
        }
        useTransformation(tx);
        return this;
    }
    
    private CallableBuilder parameterDefaultValueMethods(Tree.ParameterList parameterListTree) {
        if (parameterDefaultValueMethods == null) {
            parameterDefaultValueMethods = new ListBuffer<MethodDefinitionBuilder>();
        }
        for(Tree.Parameter p : parameterListTree.getParameters()){
            if(Decl.getDefaultArgument(p) != null){
                MethodDefinitionBuilder methodBuilder = gen.classGen().makeParamDefaultValueMethod(false, null, parameterListTree, p);
                this.parameterDefaultValueMethods.append(methodBuilder);
            }
        }
        return this;
    }
    
    public JCExpression build() {
        // Generate a subclass of Callable
        ListBuffer<JCTree> classBody = new ListBuffer<JCTree>();
        gen.at(node);
        if (parameterDefaultValueMethods != null) {
            for (MethodDefinitionBuilder mdb : parameterDefaultValueMethods) {
                classBody.append(mdb.build());
            }
        }
        
        transformation.appendMethods(classBody);
        gen.at(node);
        JCClassDecl classDef = gen.make().AnonymousClassDef(gen.make().Modifiers(0, annotations != null ? annotations : List.<JCAnnotation>nil()), classBody.toList());
        
        int variadicIndex = isVariadic ? numParams - 1 : -1;
        
        Type callableType;
        if (typeModel.isTypeConstructor()) {
            callableType = typeModel.getDeclaration().getExtendedType();
        } else {
            callableType = typeModel;
        }
        
        JCNewClass callableInstance;
        if(functionalInterface == null){
            callableInstance = gen.at(node).NewClass(null, 
                    null, 
                    gen.makeJavaType(callableType, JT_EXTENDS | JT_CLASS_NEW), 
                    List.<JCExpression>of(gen.makeReifiedTypeArgument(callableType.getTypeArgumentList().get(0)),
                            gen.makeReifiedTypeArgument(callableType.getTypeArgumentList().get(1)),
                                          gen.make().Literal(callableType.asString(true)),
                                          gen.make().TypeCast(gen.syms().shortType, gen.makeInteger(variadicIndex))),
                    classDef);
        }else{
            callableInstance = gen.make().NewClass(null, 
                    null, 
                    gen.makeJavaType(functionalInterface, JT_EXTENDS | JT_CLASS_NEW), 
                    List.<JCExpression>nil(),
                    classDef);
        }

        JCExpression result;
        if (typeModel.isTypeConstructor()) {
            result = buildTypeConstructor(callableType, callableInstance);
        } else {
            result = callableInstance;
        }
        gen.at(null);
        if (instanceSubstitution != null) {
            instanceSubstitution.close();
        }
        
        return result;
    }

    protected JCExpression buildTypeConstructor(Type callableType,
            JCNewClass callableInstance) {
        JCExpression result;
        // Wrap in an anonymous TypeConstructor subcla
        
        MethodDefinitionBuilder rawApply = MethodDefinitionBuilder.systemMethod(gen, Naming.Unfix.apply.toString());
        rawApply.modifiers(Flags.PUBLIC);
        rawApply.isOverride(true);
        //for (TypeParameter tp : typeModel.getDeclaration().getTypeParameters()) {
        //    apply.typeParameter(tp);
        //}
        rawApply.resultType(new TransformedType(gen.makeJavaType(callableType, AbstractTransformer.JT_RAW)));
        {
            ParameterDefinitionBuilder pdb = ParameterDefinitionBuilder.systemParameter(gen, "applied");
            pdb.modifiers(Flags.FINAL);
            pdb.type(new TransformedType(gen.make().TypeArray(gen.make().Type(gen.syms().ceylonTypeDescriptorType))));
            rawApply.parameter(pdb);
        }
        rawApply.body(List.<JCStatement>of(
                gen.make().Return(gen.make().Apply(null, 
                        gen.naming.makeUnquotedIdent(Naming.Unfix.$apply$.toString()), 
                        List.<JCExpression>of(gen.naming.makeUnquotedIdent("applied"))))));
        
        
        MethodDefinitionBuilder typedApply = MethodDefinitionBuilder.systemMethod(gen, Naming.Unfix.$apply$.toString());
        typedApply.modifiers(Flags.PRIVATE);
        //for (TypeParameter tp : typeModel.getDeclaration().getTypeParameters()) {
        //    apply.typeParameter(tp);
        //}
        typedApply.resultType(new TransformedType(gen.makeJavaType(callableType)));
        {
            ParameterDefinitionBuilder pdb = ParameterDefinitionBuilder.systemParameter(gen, "applied");
            pdb.modifiers(Flags.FINAL);
            pdb.type(new TransformedType(gen.make().TypeArray(gen.make().Type(gen.syms().ceylonTypeDescriptorType))));
            typedApply.parameter(pdb);
        }
        ListBuffer<JCTypeParameter> typeParameters = new ListBuffer<JCTypeParameter>();
        for (TypeParameter typeParameter : Strategy.getEffectiveTypeParameters(typeModel.getDeclaration())) {
            Type typeArgument = typeModel.getTypeArguments().get(typeParameter);
            typeParameters.add(gen.makeTypeParameter(typeParameter, null));
            typedApply.body(gen.makeVar(Flags.FINAL, 
                    gen.naming.getTypeArgumentDescriptorName(typeParameter), 
                    gen.make().Type(gen.syms().ceylonTypeDescriptorType), 
                    gen.make().Indexed(gen.makeUnquotedIdent("applied"),
                            gen.make().Literal(typeModel.getTypeArgumentList().indexOf(typeArgument)))));
        }
        
        typedApply.body(gen.make().Return(callableInstance));
        //typedApply.body(body.toList());
        
        MethodDefinitionBuilder ctor = MethodDefinitionBuilder.constructor(gen, false);
        ctor.body(gen.make().Exec(gen.make().Apply(null, gen.naming.makeSuper(), List.<JCExpression>of(gen.make().Literal(typeModel.asString(true))))));
        
        
        SyntheticName n = gen.naming.synthetic(typeModel.getDeclaration().getName());
        
        JCClassDecl classDef = gen.make().ClassDef(
                gen.make().Modifiers(0, List.<JCAnnotation>nil()),
                n.asName(),//name,
                typeParameters.toList(),
                gen.make().QualIdent(gen.syms().ceylonAbstractTypeConstructorType.tsym),//extending
                List.<JCExpression>nil(),//implementing,
                List.<JCTree>of(ctor.build(), rawApply.build(), typedApply.build()));
        result = gen.make().LetExpr(
                List.<JCStatement>of(classDef),
                gen.make().NewClass(null, 
                        null, 
                        n.makeIdent(),
                        List.<JCExpression>nil(),
                        //List.<JCExpression>of(gen.make().Literal(typeModel.asString(true))),
                        null));
        return result;
    }

    private java.util.List<Type> getParameterTypesFromCallableModel() {
        java.util.List<Type> parameterTypes = new ArrayList<Type>(numParams);
        java.util.List<Parameter> parameters = paramLists.getParameters();
        for(int i=0;i<numParams;i++) {
            Type argType = gen.getParameterTypeOfCallable(typeModel, i);
            // getting parameter types out of the callable does not tell us if the
            // argument is small or not, so rectify that
            if(parameters.size() > i
                    && parameters.get(i).getModel().isSmall()){
                String underlyingType = argType.getUnderlyingType();
                if(argType.isInteger()){
                    if(!"int".equals(underlyingType)){
                        argType = argType.clone();
                        argType.setUnderlyingType("int");
                    }
                }else if(argType.isFloat()){
                    if(!"float".equals(underlyingType)){
                        argType = argType.clone();
                        argType.setUnderlyingType("float");
                    }
                }else if(argType.isCharacter()){
                    if(!"char".equals(underlyingType)){
                        argType = argType.clone();
                        argType.setUnderlyingType("char");
                    }
                }
            }
            parameterTypes.add(argType);
        }
        return parameterTypes;
    }

    private java.util.List<Type> getParameterTypesFromParameterModels() {
        java.util.List<Type> parameterTypes = new ArrayList<Type>(numParams);
        // get them from our declaration
        for(Parameter p : paramLists.getParameters()){
            Type pt;
            FunctionOrValue pm = p.getModel();
            if(pm instanceof Function
                    && ((Function)pm).isParameter())
                pt = gen.getTypeForFunctionalParameter((Function) pm);
            else
                pt = p.getType();
            parameterTypes.add(pt);
        }
        return parameterTypes;
    }
    
    
    

    class CallTypedMethod extends MethodWithArity {

        private final List<JCStatement> stmts;

        public CallTypedMethod(List<JCStatement> stmts) {
            this.stmts = stmts;
        }
        
        @Override
        MethodDefinitionBuilder makeMethod(int arity) {
            return makeCallTypedMethod(stmts);
        }
        
    }
    
    /**
     * Makes the {@code call$typed()} method, using the given body.
     */
    private MethodDefinitionBuilder makeCallTypedMethod(List<JCStatement> body) {
        // make the method
        MethodDefinitionBuilder methodBuilder = MethodDefinitionBuilder.systemMethod(gen, Naming.getCallableTypedMethodName());
        methodBuilder.noAnnotations();
        methodBuilder.modifiers(Flags.PRIVATE);
        Type returnType = gen.getReturnTypeOfCallable(typeModel);
        methodBuilder.resultType(gen.makeJavaType(returnType, JT_NO_PRIMITIVES), null);
        // add all parameters
        int i=0;
        for(Parameter param : paramLists.getParameters()){
            ParameterDefinitionBuilder parameterBuilder = ParameterDefinitionBuilder.systemParameter(gen, Naming.getAliasedParameterName(param));
            JCExpression paramType = gen.makeJavaType(parameterTypes.get(i));
            parameterBuilder.modifiers(Flags.FINAL);
            parameterBuilder.type(new TransformedType(paramType));
            methodBuilder.parameter(parameterBuilder);
            i++;
        }
        // Return the call result, or null if a void method
        methodBuilder.body(body);
        return methodBuilder;
    }

    private static Name makeParamName(AbstractTransformer gen, int paramIndex) {
        return gen.names().fromString(getParamName(paramIndex));
    }
    
    private static JCExpression makeParamIdent(AbstractTransformer gen, int paramIndex) {
        return gen.make().Ident(gen.names().fromString(getParamName(paramIndex)));
    }

    private static String getParamName(int paramIndex) {
        return "$param$"+paramIndex;
    }
    
    private ParameterDefinitionBuilder makeCallableCallParam(long flags, int ii) {
        JCExpression type = gen.makeIdent(gen.syms().objectType);
        if ((flags & Flags.VARARGS) != 0) {
            type = gen.make().TypeArray(type);
        }
        ParameterDefinitionBuilder pdb = ParameterDefinitionBuilder.systemParameter(gen, getParamName(ii));
        pdb.modifiers(Flags.FINAL | flags);
        pdb.type(new TransformedType(type));
        return pdb;
    }
    
    private ParameterDefinitionBuilder makeCallableVaryParam(long flags, int ii) {
        Type iteratedType = gen.typeFact().getIteratedType(parameterTypes.get(parameterTypes.size()-1));
        // $call$var()'s variadic parameter is *always* erasred to Sequential
        // even if it's a Variadic+ parameter
        JCExpression type = gen.makeJavaType(gen.typeFact().getSequentialType(iteratedType), AbstractTransformer.JT_RAW);
        ParameterDefinitionBuilder pdb = ParameterDefinitionBuilder.systemParameter(gen, getParamName(ii));
        pdb.modifiers(Flags.FINAL | flags);
        pdb.type(new TransformedType(type, null, gen.makeAtNonNull()));
        return pdb;
    }

    public void functionalInterface(Type interfaceType, TypedReference method) {
        this.functionalInterface = interfaceType;
        this.functionalInterfaceMethod = method;
    }

    public void checkForFunctionalInterface(Type expectedType) {
        TypedReference functionalInterface = gen.checkForFunctionalInterface(expectedType);
        if(functionalInterface != null){
            // It's important to ignore the qualifying type because it could be a supertype
            // of the expected type of the method parameter, which is the one we have to
            // implement, even if the SAM method comes from a supertype
            functionalInterface(expectedType, functionalInterface);
        }
    }
}
