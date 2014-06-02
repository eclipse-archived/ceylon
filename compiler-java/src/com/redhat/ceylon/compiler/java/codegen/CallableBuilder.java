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

import static com.redhat.ceylon.compiler.java.codegen.AbstractTransformer.JT_CLASS_NEW;
import static com.redhat.ceylon.compiler.java.codegen.AbstractTransformer.JT_EXTENDS;
import static com.redhat.ceylon.compiler.java.codegen.AbstractTransformer.JT_NO_PRIMITIVES;

import java.util.ArrayList;
import java.util.Collections;

import com.redhat.ceylon.compiler.java.codegen.AbstractTransformer.BoxingStrategy;
import com.redhat.ceylon.compiler.java.codegen.Naming.Suffix;
import com.redhat.ceylon.compiler.java.codegen.Naming.SyntheticName;
import com.redhat.ceylon.compiler.java.codegen.Naming.Unfix;
import com.redhat.ceylon.compiler.loader.model.FieldValue;
import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Functional;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.MethodOrValue;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ParameterList;
import com.redhat.ceylon.compiler.typechecker.model.ProducedReference;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.JCTree.JCNewClass;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;

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
                    gen.makeUnquotedIdent(Naming.getDefaultedParamMethodName(null, defaultedParam)),
                    defaultMethodArgs);
        }
    };
    
    DefaultValueMethodTransformation defaultValueCall = DEFAULTED_PARAM_METHOD;
    
    private final AbstractTransformer gen;
    private final ProducedType typeModel;
    private final ParameterList paramLists;
    private final int numParams;
    private final int minimumParams;
    private final int minimumArguments;
    private final boolean hasOptionalParameters;
    private final boolean isVariadic;
    /**
     * For deferred declarations the default parameter value methods are 
     * generated on the wrapper class, not on the Callable (#1177)
     */
    private boolean delegateDefaultedCalls = true;
    private java.util.List<ProducedType> parameterTypes;
    
    private ListBuffer<MethodDefinitionBuilder> parameterDefaultValueMethods;
    
    private CallableTransformation transformation;
    
    private boolean companionAccess = false;
    
    private CallableBuilder(CeylonTransformer gen, ProducedType typeModel, ParameterList paramLists) {
        this.gen = gen;
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
    public static CallableBuilder methodReference(CeylonTransformer gen, 
            final Tree.StaticMemberOrTypeExpression forwardCallTo, ParameterList parameterList) {
        CallableBuilder cb = new CallableBuilder(gen, forwardCallTo.getTypeModel(), parameterList);
        cb.parameterTypes = cb.getParameterTypesFromCallableModel();
        CallableTransformation tx;
        cb.target = cb.new Target(forwardCallTo);
        cb.defaultValueCall = new DefaultValueMethodTransformation() {
            @Override
            public JCExpression makeDefaultValueMethod(AbstractTransformer gen, 
                    Parameter defaultedParam, 
                    List<JCExpression> defaultMethodArgs) {
                JCExpression fn = null;
                if (forwardCallTo instanceof Tree.BaseMemberOrTypeExpression) {
                    fn  = gen.makeUnquotedIdent(  
                            Naming.getDefaultedParamMethodName((Declaration)defaultedParam.getModel().getScope(), defaultedParam));
                } else if (forwardCallTo instanceof Tree.QualifiedMemberOrTypeExpression) {
                    fn = gen.makeQualIdent(
                            gen.expressionGen().transformTermForInvocation(((Tree.QualifiedMemberOrTypeExpression)forwardCallTo).getPrimary(), null),  
                            Naming.getDefaultedParamMethodName((Declaration)defaultedParam.getModel().getScope(), defaultedParam));
                }
                return gen.make().Apply(null, 
                        fn,
                        defaultMethodArgs);
            }
        };
        if (cb.isVariadic) {
            tx = cb.new VariadicCallableTransformation(
                    cb.new CallMethodWithForwardedBody(false));
        } else {
            tx = cb.new FixedArityCallableTransformation(cb.new CallMethodWithForwardedBody(true), null);
        }
        cb.useTransformation(tx);
        return cb;
    }
    
    /**
     * Used for "static" method or class references. For example:
     * <pre>
     *     value x = Integer.plus;
     *     value y = Foo.method;
     *     value z = Outer.Inner;
     * </pre>
     */
    public static CallableBuilder unboundFunctionalMemberReference(
            CeylonTransformer gen,
            Tree.QualifiedMemberOrTypeExpression qmte,
            ProducedType typeModel, 
            final Functional methodOrClass, 
            ProducedReference producedReference) {
        final ParameterList parameterList = methodOrClass.getParameterLists().get(0);
        final ProducedType type = gen.getReturnTypeOfCallable(typeModel);
        CallableBuilder inner = new CallableBuilder(gen, type, parameterList);
        inner.parameterTypes = inner.getParameterTypesFromCallableModel();//FromParameterModels();
        inner.defaultValueCall = inner.new MemberReferenceDefaultValueCall(methodOrClass);
        CallBuilder callBuilder = CallBuilder.instance(gen);
        ProducedType accessType = gen.getParameterTypeOfCallable(typeModel, 0);
        if (methodOrClass instanceof Method) {
            callBuilder.invoke(gen.naming.makeQualifiedName(gen.naming.makeUnquotedIdent(Unfix.$instance$), (Method)methodOrClass, Naming.NA_MEMBER));
            if (!((TypedDeclaration)methodOrClass).isShared()) {
                accessType = Decl.getPrivateAccessType(qmte);
            }
        } else if (methodOrClass instanceof Method
                && ((Method)methodOrClass).isParameter()) {
            callBuilder.invoke(gen.naming.makeQualifiedName(gen.naming.makeUnquotedIdent(Unfix.$instance$), (Method)methodOrClass, Naming.NA_MEMBER));
        } else if (methodOrClass instanceof Class) {
            if (Strategy.generateInstantiator((Class)methodOrClass)) {
                callBuilder.invoke(gen.naming.makeInstantiatorMethodName(gen.naming.makeUnquotedIdent(Unfix.$instance$), (Class)methodOrClass));
            } else {
                callBuilder.instantiate(new ExpressionAndType(gen.naming.makeUnquotedIdent(Unfix.$instance$), null), 
                        gen.makeJavaType(((Class)methodOrClass).getType(), JT_CLASS_NEW | AbstractTransformer.JT_NON_QUALIFIED));
                if (!((Class)methodOrClass).isShared()) {
                    accessType = Decl.getPrivateAccessType(qmte);
                }
            }
        } else {
            Assert.fail("Unhandled functional type " + methodOrClass.getClass().getSimpleName());
        }
        ListBuffer<ExpressionAndType> reified = ListBuffer.lb();
        
        DirectInvocation.addReifiedArguments(gen, producedReference, reified);
        for (ExpressionAndType reifiedArgument : reified) {
            callBuilder.argument(reifiedArgument.expression);
        }
        
        for (Parameter parameter : parameterList.getParameters()) {
            callBuilder.argument(gen.naming.makeQuotedIdent(parameter.getName()));
        }
        JCExpression innerInvocation = callBuilder.build();
        // Need to worry about boxing for Method and FunctionalParameter 
        if (methodOrClass instanceof TypedDeclaration) {
            // use the method return type since the function is actually applied
            ProducedType returnType = gen.getReturnTypeOfCallable(type);
            innerInvocation = gen.expressionGen().applyErasureAndBoxing(innerInvocation, 
                    returnType,
                    // make sure we use the type erased info as it has not been passed to the expression since the
                    // expression is a Callable
                    CodegenUtil.hasTypeErased((TypedDeclaration)methodOrClass),
                    !CodegenUtil.isUnBoxed((TypedDeclaration)methodOrClass), 
                    BoxingStrategy.BOXED, returnType, 0);
        } else if (Strategy.isInstantiatorUntyped((Class)methodOrClass)) {
            // $new method declared to return Object, so needs typecast
            innerInvocation = gen.make().TypeCast(gen.makeJavaType(
                    ((Class)methodOrClass).getType()), innerInvocation);
        }
        List<JCStatement> innerBody = List.<JCStatement>of(gen.make().Return(innerInvocation));
        inner.useDefaultTransformation(innerBody);
        
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
        CallableBuilder outer = new CallableBuilder(gen, typeModel, outerPl);
        outer.parameterTypes = outer.getParameterTypesFromParameterModels();
        List<JCStatement> outerBody = List.<JCStatement>of(gen.make().Return(inner.build()));
        outer.useDefaultTransformation(outerBody);
        outer.companionAccess = Decl.isPrivateAccessRequiringCompanion(qmte);
        
        return outer;
    }
    
    class MemberReferenceDefaultValueCall implements DefaultValueMethodTransformation {
        private Functional methodOrClass;
        MemberReferenceDefaultValueCall(final Functional methodOrClass) {
            this.methodOrClass = methodOrClass;
        }
        @Override
        public JCExpression makeDefaultValueMethod(AbstractTransformer gen, Parameter defaultedParam, List<JCExpression> defaultMethodArgs) {
            if (methodOrClass instanceof Method
                    && ((Method)methodOrClass).isParameter()) {
                // We can't generate a call to the dpm because there isn't one!
                // But since FunctionalParameters cannot currently have 
                // defaulted parameters this *must* be a variadic parameter
                // and it's default is always empty.
                return gen.makeEmptyAsSequential(true);
            }
            JCExpression fn = gen.makeQualIdent(gen.naming.makeUnquotedIdent(Unfix.$instance$), 
                    Naming.getDefaultedParamMethodName((Declaration)methodOrClass, defaultedParam));
            return gen.make().Apply(null, 
                    fn,
                    defaultMethodArgs);
        }
    }
    
    public static CallableBuilder javaStaticMethodReference(CeylonTransformer gen, 
            ProducedType typeModel, 
            final Functional methodOrClass, 
            ProducedReference producedReference) {
        final ParameterList parameterList = methodOrClass.getParameterLists().get(0);
        CallableBuilder inner = new CallableBuilder(gen, typeModel, parameterList);
        
        ArrayList<ProducedType> pt = new ArrayList<>();
        for (Parameter p : methodOrClass.getParameterLists().get(0).getParameters()) {
            pt.add(p.getType());
        }
        inner.parameterTypes = pt; 

        inner.defaultValueCall = inner.new MemberReferenceDefaultValueCall(methodOrClass);
        JCExpression innerInvocation = gen.expressionGen().makeJavaStaticInvocation(gen,
                methodOrClass, producedReference, parameterList);
        
        // Need to worry about boxing for Method and FunctionalParameter 
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
            ProducedType typeModel,
            final TypedDeclaration value) {
        CallBuilder callBuilder = CallBuilder.instance(gen);
        if (gen.expressionGen().isThrowableMessage(qmte)) {
            callBuilder.invoke(gen.utilInvocation().throwableMessage());
            callBuilder.argument(gen.naming.makeUnquotedIdent(Unfix.$instance$));
        } else if (gen.expressionGen().isThrowableSuppressed(qmte)) {
            callBuilder.invoke(gen.utilInvocation().suppressedExceptions());
            callBuilder.argument(gen.naming.makeUnquotedIdent(Unfix.$instance$));
        } else {
            JCExpression memberName = gen.naming.makeQualifiedName(gen.naming.makeUnquotedIdent(Unfix.$instance$), value, Naming.NA_GETTER | Naming.NA_MEMBER);
            if(value instanceof FieldValue){
                callBuilder.fieldRead(memberName);
            }else{
                callBuilder.invoke(memberName);
            }
        }
        JCExpression innerInvocation = callBuilder.build();
        // use the return type since the value is actually applied
        ProducedType returnType = gen.getReturnTypeOfCallable(typeModel);
        innerInvocation = gen.expressionGen().applyErasureAndBoxing(innerInvocation, returnType, 
                // make sure we use the type erased info as it has not been passed to the expression since the
                // expression is a Callable
                CodegenUtil.hasTypeErased(value), !CodegenUtil.isUnBoxed(value), 
                BoxingStrategy.BOXED, returnType, 0);
        
        ParameterList outerPl = new ParameterList();
        Parameter instanceParameter = new Parameter();
        instanceParameter.setName(Naming.name(Unfix.$instance$));
        Value valueModel = new Value();
        instanceParameter.setModel(valueModel);
        ProducedType accessType = gen.getParameterTypeOfCallable(typeModel, 0);;
        if (!value.isShared()) {
            accessType = Decl.getPrivateAccessType(qmte);
        }
        valueModel.setName(instanceParameter.getName());
        valueModel.setInitializerParameter(instanceParameter);
        valueModel.setType(accessType);
        valueModel.setUnboxed(false);
        outerPl.getParameters().add(instanceParameter);
        CallableBuilder outer = new CallableBuilder(gen, typeModel, outerPl);
        outer.parameterTypes = outer.getParameterTypesFromParameterModels();
        List<JCStatement> innerBody = List.<JCStatement>of(gen.make().Return(innerInvocation));
        outer.useDefaultTransformation(innerBody);
        outer.companionAccess = Decl.isPrivateAccessRequiringCompanion(qmte);
        
        return outer;
    }
    
    /**
     * Constructs an {@code AbstractCallable} suitable for an anonymous function.
     */
    public static CallableBuilder anonymous(
            CeylonTransformer gen, Tree.Expression expr, ParameterList parameterList, 
            Tree.ParameterList parameterListTree, 
            ProducedType callableTypeModel, boolean delegateDefaultedCalls) {
        boolean prevSyntheticClassBody = gen.expressionGen().withinSyntheticClassBody(true);
        JCExpression transformedExpr = gen.expressionGen().transformExpression(expr, BoxingStrategy.BOXED, gen.getReturnTypeOfCallable(callableTypeModel));
        gen.expressionGen().withinSyntheticClassBody(prevSyntheticClassBody);
        final List<JCStatement> stmts = List.<JCStatement>of(gen.make().Return(transformedExpr));
        return methodArgument(gen, callableTypeModel, parameterList, parameterListTree, stmts, delegateDefaultedCalls);
    }

    public static CallableBuilder methodArgument(
            CeylonTransformer gen,
            ProducedType callableTypeModel,
            ParameterList parameterList,
            Tree.ParameterList parameterListTree, 
            List<JCStatement> stmts) {
        return methodArgument(gen, callableTypeModel, parameterList, parameterListTree, stmts, true);
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
            ProducedType callableTypeModel,
            ParameterList parameterList,
            Tree.ParameterList parameterListTree, 
            List<JCStatement> stmts, boolean delegateDefaultedCalls) {
        
        CallableBuilder cb = new CallableBuilder(gen, callableTypeModel, parameterList);
        cb.parameterTypes = cb.getParameterTypesFromParameterModels();
        cb.parameterDefaultValueMethods(parameterListTree);
        cb.delegateDefaultedCalls = delegateDefaultedCalls;
        cb.useDefaultTransformation(stmts);
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
            ProducedType typeModel,
            ParameterList parameterList,
            Tree.ParameterList parameterListTree,
            List<JCStatement> body) {

        CallableBuilder cb = new CallableBuilder(gen, typeModel, parameterList);
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
                makeDowncastOrDefaultVar(stmts, target.getCallableTempVarName(param), param, a, arity);
                a++;
            }
            return makeCallMethod(stmts.appendList(body).toList(), arity);
        }
    }
    
    class CallMethodWithForwardedBody extends MethodWithArity {
        
        final boolean isCallMethod;

        CallMethodWithForwardedBody(boolean isCallMethod) {
            this.isCallMethod = isCallMethod;
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
                    makeDowncastOrDefaultVar(stmts, target.getCallableTempVarName(param), param, a, arity);
                    a++;
                }
            }
            
            JCExpression invocation = CallableBuilder.this.target.makeInvocation(arity, isCallMethod);
            stmts.append(gen.make().Return(invocation));
            
            
            return isCallMethod ? makeCallMethod(stmts.toList(), arity) : makeCallTypedMethod(stmts.toList());
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
                Assert.that(callTyped.params.size() == numParams);
                classBody.append(callTyped);
            }
        }
        
        protected abstract Iterable<MethodDefinitionBuilder> makeMethodsForArity(int arity);
        
        /**
         * Make the private $call$typed() method, whose arity must 
         * be {@link #numParams}
         * @return
         */
        abstract MethodDefinitionBuilder makeCallTypedMethod();
    }
    
    protected final MethodDefinitionBuilder makeCallMethod(List<JCStatement> body, int arity) {
        MethodDefinitionBuilder callMethod = MethodDefinitionBuilder.callable(gen);
        callMethod.isOverride(true);
        callMethod.modifiers(Flags.PUBLIC);
        ProducedType returnType = gen.getReturnTypeOfCallable(typeModel);
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
            
            JCExpression invocation = gen.make().Apply(null, 
                    gen.naming.makeUnquotedIdent(Naming.name(Unfix.$spreadVarargs$)), 
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
            SyntheticName name = target.getCallableTempVarName(param);
            return name;
        }
        
        protected final boolean parameterSequenced(int a) {
            return paramLists.getParameters().get(a).isSequenced();
        }
        
        protected Parameter getVariadicParameter() {
            return paramLists.getParameters().get(numParams - 1);
        }
        
        protected ProducedType getVariadicType() {
            return parameterTypes.get(numParams - 1);
        }
        
        protected ProducedType getVariadicIteratedType() {
            return gen.typeFact().getIteratedType(getVariadicType());
        }
        
        /** Makes an argument that's just the ident of the parameter {@code $param$n}*/
        protected final void makeParameterArgument(final int arity,
                ListBuffer<JCStatement> stmts, ListBuffer<JCExpression> args,
                int a) {
            SyntheticName name = parameterName(a);
            Parameter param = paramLists.getParameters().get(a);
            makeDowncastOrDefaultVar(stmts, 
                    name, param, a, arity);
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
            ListBuffer<JCExpression> varargs = ListBuffer.<JCExpression>lb();
            for (; a < arity; a++) {
                if (arity < numParams - 1) {
                    Parameter param1 = paramLists.getParameters().get(Math.min(a, numParams-1));
                    makeDowncastOrDefaultVar(stmts, 
                            parameterName(Math.min(a, numParams-1)), param1, a, arity);
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
            SyntheticName vname = target.getCallableTempVarName(getVariadicParameter()).suffixedBy(Suffix.$variadic$);
            args.append(vname.makeIdent());
            target.makeVar(stmts, getVariadicParameter(), getVariadicType(), 
                    vname, varargsSequence);
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
            ListBuffer<JCExpression> args = ListBuffer.lb();
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
                                gen.make().Binary(JCTree.MINUS, gen.naming.makeQualIdent(makeParamIdent(gen,  0), "length"), gen.make().Literal(numParams-1)),
                                makeParamIdent(gen,  0),
                                gen.makeEmpty())));
                
            }
        }
        
    }
    
    /**
     * Generates {@code $call$variadic()} methods for variadic Callables
     */
    class CallVariadicMethodForVariadic extends VariadicMethodWithArity {
        
        @Override
        MethodDefinitionBuilder makeMethod(final int arity) {
            final int arity1 = arity+1;// The arity including the Sequential parameter
            if (arity1 < getMinimumArguments()) {
                return null;
            }
            ListBuffer<JCStatement> stmts = new ListBuffer<JCStatement>();
            ListBuffer<JCExpression> args = ListBuffer.lb();
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
            callVaryMethod.isOverride(true);
            callVaryMethod.modifiers(Flags.PUBLIC);
            ProducedType returnType = gen.getReturnTypeOfCallable(typeModel);
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
            ListBuffer<JCExpression> variadicElements = ListBuffer.lb();
            for (; a < arity; a++) {
                Parameter param = paramLists.getParameters().get(Math.min(a, numParams-1));
                variadicElements.append(makeParameterExpr(param, a, getVariadicIteratedType(), false));
            }
            ListBuffer<JCExpression> spreadCallArgs = ListBuffer.lb();
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
                                gen.typeFact().getIntegerDeclaration().getType(), false, BoxingStrategy.BOXED, gen.typeFact().getIntegerDeclaration().getType())));
                get = gen.expressionGen().applyErasureAndBoxing(get, 
                        parameterTypes.get(a), 
                        true, true, BoxingStrategy.UNBOXED, 
                        parameterTypes.get(a), 0);
                Parameter param = paramLists.getParameters().get(a);
                target.makeVar(stmts, param, parameterTypes.get(a),
                        name, get);
                args.append(name.makeIdent());
            }
            // Get the rest of the sequential using spanFrom()
            SyntheticName name = parameterName(numParams-1);
            JCExpression spanFrom = gen.make().Apply(null, 
                    gen.makeQualIdent(makeParamIdent(gen, arity), "spanFrom"), 
                    List.<JCExpression>of(gen.expressionGen().applyErasureAndBoxing(gen.make().Literal(a), 
                            gen.typeFact().getIntegerDeclaration().getType(), false, BoxingStrategy.BOXED, gen.typeFact().getIntegerDeclaration().getType())));
            spanFrom = gen.expressionGen().applyErasureAndBoxing(spanFrom, 
                    parameterTypes.get(a), 
                    true, true, BoxingStrategy.UNBOXED, 
                    parameterTypes.get(a), 0);
            Parameter param = paramLists.getParameters().get(numParams-1);
            target.makeVar(stmts, param, parameterTypes.get(a),
                    name, spanFrom);
            args.append(name.makeIdent());
        }
        
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
            ListBuffer<JCExpression> lb = ListBuffer.lb();
            for (; a < arity1-1 && a < CALLABLE_MAX_FIZED_ARITY; a++) {
                Parameter param = paramLists.getParameters().get(Math.min(a, numParams-1));
                lb.append(makeParameterExpr(param, a, getVariadicIteratedType(), false));
            }
            ListBuffer<JCExpression> spreadCallArgs = ListBuffer.lb();
            spreadCallArgs.append(gen.makeReifiedTypeArgument(getVariadicIteratedType()));
            if (arity1 > CALLABLE_MAX_FIZED_ARITY+1) {
                spreadCallArgs.append(gen.make().Literal(getMinimumArguments()));
                spreadCallArgs.append(makeParamIdent(gen, 0));
            } else {
                spreadCallArgs.append(gen.make().Literal(0));
                spreadCallArgs.append(gen.make().Literal(lb.size()));
                spreadCallArgs.append(gen.make().NewArray(gen.make().QualIdent(gen.syms().objectType.tsym), List.<JCExpression>nil(), lb.toList()));
                spreadCallArgs.append(makeParamIdent(gen, a));
            }
            args.append(makeRespread(spreadCallArgs.toList()));
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
        
        /**
         * Make the $call$typed() method
         */
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
            parameterDefaultValueMethods = ListBuffer.lb();
        }
        for(Tree.Parameter p : parameterListTree.getParameters()){
            if(Decl.getDefaultArgument(p) != null){
                MethodDefinitionBuilder methodBuilder = gen.classGen().makeParamDefaultValueMethod(false, null, parameterListTree, p, null);
                this.parameterDefaultValueMethods.append(methodBuilder);
            }
        }
        return this;
    }
    
    public JCNewClass build() {
        // Generate a subclass of Callable
        ListBuffer<JCTree> classBody = new ListBuffer<JCTree>();
        
        if (parameterDefaultValueMethods != null) {
            for (MethodDefinitionBuilder mdb : parameterDefaultValueMethods) {
                classBody.append(mdb.build());
            }
        }
        
        transformation.appendMethods(classBody);
        
        JCClassDecl classDef = gen.make().AnonymousClassDef(gen.make().Modifiers(0), classBody.toList());
        
        int variadicIndex = isVariadic ? numParams - 1 : -1;
        JCNewClass instance = gen.make().NewClass(null, 
                null, 
                gen.makeJavaType(typeModel, JT_EXTENDS | JT_CLASS_NEW), 
                List.<JCExpression>of(gen.makeReifiedTypeArgument(typeModel.getTypeArgumentList().get(0)),
                                      gen.makeReifiedTypeArgument(typeModel.getTypeArgumentList().get(1)),
                                      gen.make().Literal(typeModel.getProducedTypeName(true)),
                                      gen.make().TypeCast(gen.syms().shortType, gen.makeInteger(variadicIndex))),
                classDef);
        return instance;
    }

    private java.util.List<ProducedType> getParameterTypesFromCallableModel() {
        java.util.List<ProducedType> parameterTypes = new ArrayList<ProducedType>(numParams);
        for(int i=0;i<numParams;i++) {
            parameterTypes.add(gen.getParameterTypeOfCallable(typeModel, i));
        }
        return parameterTypes;
    }

    private java.util.List<ProducedType> getParameterTypesFromParameterModels() {
        java.util.List<ProducedType> parameterTypes = new ArrayList<ProducedType>(numParams);
        // get them from our declaration
        for(Parameter p : paramLists.getParameters()){
            ProducedType pt;
            MethodOrValue pm = p.getModel();
            if(pm instanceof Method
                    && ((Method)pm).isParameter())
                pt = gen.getTypeForFunctionalParameter((Method) pm);
            else
                pt = p.getType();
            parameterTypes.add(pt);
        }
        return parameterTypes;
    }
    
    /**
     * Makes an expression, (appropriately downcasted or unboxed) for the
     * given parameter of the {@code $call()} method.
     */
    private JCExpression makeCallParameterExpr(Parameter param, int argIndex, boolean varargs) {
        ProducedType paramType = parameterTypes.get(Math.min(argIndex, numParams-1));
        return makeParameterExpr(param, argIndex, paramType, varargs);
    }
    
    /**
     * Makes an expression, (appropriately downcasted or unboxed) for the
     * given parameter of the {@code $call()} method.
     */
    private JCExpression makeParameterExpr(Parameter param, int argIndex, ProducedType paramType, boolean ellipsis){
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
        argExpr = gen.expressionGen().applyErasureAndBoxing(argExpr, 
                paramType, // it came in as Object, but we need to pretend its type
                // is the parameter type because that's how unboxing determines how it has to unbox
                true, // it's completely erased
                true, // it came in boxed
                CodegenUtil.getBoxingStrategy(param.getModel()), // see if we need to box 
                paramType, // see what type we need
                ebFlags);
        if (this.companionAccess) {
            argExpr = gen.naming.makeCompanionAccessorCall(argExpr, (Interface)paramType.getType().getDeclaration());
        }
        return argExpr;
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
        ProducedType returnType = gen.getReturnTypeOfCallable(typeModel);
        methodBuilder.resultType(gen.makeJavaType(returnType, JT_NO_PRIMITIVES), null);
        // add all parameters
        int i=0;
        for(Parameter param : paramLists.getParameters()){
            ParameterDefinitionBuilder parameterBuilder = ParameterDefinitionBuilder.systemParameter(gen, param.getName());
            JCExpression paramType = gen.makeJavaType(parameterTypes.get(i));
            parameterBuilder.modifiers(Flags.FINAL);
            parameterBuilder.type(paramType, null);
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
        pdb.type(type, null);
        return pdb;
    }
    
    private ParameterDefinitionBuilder makeCallableVaryParam(long flags, int ii) {
        ProducedType iteratedType = gen.typeFact().getIteratedType(parameterTypes.get(parameterTypes.size()-1));
        // $call$var()'s variadic parameter is *always* erasred to Sequential
        // even if it's a Variadic+ parameter
        JCExpression type = gen.makeJavaType(gen.typeFact().getSequentialType(iteratedType), AbstractTransformer.JT_RAW);
        ParameterDefinitionBuilder pdb = ParameterDefinitionBuilder.systemParameter(gen, getParamName(ii));
        pdb.modifiers(Flags.FINAL | flags);
        pdb.type(type, null);
        return pdb;
    }
    
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
            final Parameter param, 
            final int a, final int arity) {
        JCExpression varInitialExpression = makeDowncastOrDefault(param, a, arity);
        target.makeVar(stmts, param, parameterTypes.get(a), 
                name, varInitialExpression);
    }
    
    protected JCExpression makeDowncastOrDefault(final Parameter param,
            final int a, final int arity) {
        // read the value
        JCExpression paramExpression = makeCallParameterExpr(param, a, arity > CALLABLE_MAX_FIZED_ARITY);
        JCExpression varInitialExpression;
        // TODO Suspicious
        if(param.isDefaulted() || param.isSequenced()){
            if(arity > CALLABLE_MAX_FIZED_ARITY){
                // must check if it's defined
                JCExpression test = gen.make().Binary(JCTree.GT, gen.makeSelect(getParamName(0), "length"), gen.makeInteger(a));
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

    Target target = new Target(null);
    
    class Target {
        Tree.Term forwardCallTo;
        Target(Tree.Term forwardCallTo) {
            this.forwardCallTo = forwardCallTo;
        }
        
        public JCExpression makeInvocation(int arity, boolean isCallMethod) {
            ProducedReference target = getProducedReference();
            TypeDeclaration primaryDeclaration = getTypeModel().getDeclaration();;
            CallableInvocation invocationBuilder = new CallableInvocation (
                    gen,
                    forwardCallTo,
                    primaryDeclaration,
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

        public ProducedType getTypeModel() {
            return forwardCallTo.getTypeModel();
        }

        ProducedReference getProducedReference() {
            ProducedReference target;
            if (forwardCallTo instanceof Tree.MemberOrTypeExpression) {
                target = ((Tree.MemberOrTypeExpression)forwardCallTo).getTarget();
            } else if (forwardCallTo instanceof Tree.FunctionArgument) {
                Method method = ((Tree.FunctionArgument) forwardCallTo).getDeclarationModel();
                target = method.getProducedReference(null, Collections.<ProducedType>emptyList());
            } else {
                throw new RuntimeException(forwardCallTo.getNodeType());
            }
            return target;
        }
        
        Naming.SyntheticName getCallableTempVarName(Parameter param) {
            // prefix them with $$ if we only forward, otherwise we need them to have the proper names
            return gen.naming.synthetic(forwardCallTo != null ? Naming.getCallableTempVarName(param) : 
                Naming.getAliasedParameterName(param));
        }
        
        protected final void makeVar(ListBuffer<JCStatement> stmts,
                final Parameter param, ProducedType parameterType,
                Naming.SyntheticName name, JCExpression expr) {
            // store it in a local var
            int flags = 0;
            if(!CodegenUtil.isUnBoxed(param.getModel())){
                flags |= AbstractTransformer.JT_NO_PRIMITIVES;
            }
            if (companionAccess) {
                flags |= AbstractTransformer.JT_COMPANION;
            }
            // Always go raw if we're forwarding, because we're building the call ourselves and we don't get a chance to apply erasure and
            // casting to parameter expressions when we pass them to the forwarded method. Ideally we could set it up correctly so that
            // the proper erasure is done when we read from the Callable.call Object param, but since we store it in a variable defined here,
            // we'd need to duplicate some of the erasure logic here to make or not the type raw, and that would be worse.
            // Besides, named parameter invocation does the same.
            // See https://github.com/ceylon/ceylon-compiler/issues/1005
            if(forwardCallTo != null)
                flags |= AbstractTransformer.JT_RAW;
            JCVariableDecl var = gen.make().VarDef(gen.make().Modifiers(param.getModel().isVariable() ? 0 : Flags.FINAL), 
                    name.asName(), 
                    gen.makeJavaType(parameterType, flags),
                    expr);
            stmts.append(var);
            if (ParameterDefinitionBuilder.isBoxedVariableParameter(param)) {
                stmts.append(gen.makeVariableBoxDecl(name.makeIdent(), param.getModel()));
            }
        }
    }
    
    
    /**
     * Makes an invocation of a default parameter value method (if the 
     * parameter is defaulted), or empty (if it's *variadic)
     */
    private JCExpression makeDefaultValueCall(Parameter defaultedParam, int i){
        if (Strategy.hasDefaultParameterValueMethod(defaultedParam)) {
            // add the default value
            List<JCExpression> defaultMethodArgs = List.nil();
            // pass all the previous values
            for(int a=i-1;a>=0;a--){
                Parameter param = paramLists.getParameters().get(a);
                JCExpression previousValue = target.getCallableTempVarName(param).makeIdent();
                defaultMethodArgs = defaultMethodArgs.prepend(previousValue);
            }
            // now call the default value method
            return defaultValueCall.makeDefaultValueMethod(gen, defaultedParam, defaultMethodArgs);
        } else if (Strategy.hasEmptyDefaultArgument(defaultedParam)) {
            return gen.makeEmptyAsSequential(true);
        }
        throw Assert.fail("compiler bug: " + defaultedParam.getName() + " is not a defaulted parameter");
    }
}
