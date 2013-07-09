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

import com.redhat.ceylon.compiler.java.codegen.AbstractTransformer.BoxingStrategy;
import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Functional;
import com.redhat.ceylon.compiler.typechecker.model.FunctionalParameter;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ParameterList;
import com.redhat.ceylon.compiler.typechecker.model.ProducedReference;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.model.ValueParameter;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.JCTree.JCNewClass;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;

public class CallableBuilder {

    static interface DefaultValueMethodTransformation {
        public JCExpression makeDefaultValueMethod(AbstractTransformer gen, Parameter defaultedParam);
    }
    
    public static final DefaultValueMethodTransformation DEFAULTED_PARAM_METHOD = new DefaultValueMethodTransformation() {
        @Override
        public JCExpression makeDefaultValueMethod(AbstractTransformer gen, Parameter defaultedParam) {
            return gen.makeUnquotedIdent(Naming.getDefaultedParamMethodName(null, defaultedParam));
        }
    };
    
    DefaultValueMethodTransformation defaultValueCall = DEFAULTED_PARAM_METHOD;
    
    private final AbstractTransformer gen;
    private final ProducedType typeModel;
    private final ParameterList paramLists;
    private boolean noDelegates;
    private int numParams;
    private int minimumParams;
    private boolean isVariadic;
    private boolean hasOptionalParameters;
    private java.util.List<ProducedType> parameterTypes;
    
    private CallableBuilder(CeylonTransformer gen, ProducedType typeModel, ParameterList paramLists) {
        this.gen = gen;
        this.typeModel = typeModel;
        this.paramLists = paramLists;
        this.numParams = paramLists.getParameters().size();
        this.minimumParams = 0;
        for(Parameter p : paramLists.getParameters()){
            if(p.isDefaulted() || p.isSequenced())
                break;
            this.minimumParams++;
        }
        this.isVariadic = numParams > 0 && paramLists.getParameters().get(numParams-1).isSequenced();
        this.hasOptionalParameters = minimumParams != numParams;
    }
    
    /**
     * Constructs an {@code AbstractCallable} suitable for wrapping a method reference.
     */
    public static CallableBuilder methodReference(CeylonTransformer gen, Tree.Term expr, ParameterList parameterList) {
        CallableBuilder cb = new CallableBuilder(gen, expr.getTypeModel(), parameterList);
        cb.parameterTypes = cb.getParameterTypesFromCallableModel();
        cb.forwardTo(expr);
        return cb;
    }
    
    /**
     * Used for "static" method or class references like
     *     value x = Integer.plus;
     *     value y = Foo.method;
     *     value z = Outer.Inner;
     */
    public static CallableBuilder unboundFunctionalMemberReference(CeylonTransformer gen, 
            ProducedType typeModel, 
            final Functional methodOrClass, 
            ProducedReference producedReference) {
        final String instanceName = "$instance";
        final ParameterList parameterList = methodOrClass.getParameterLists().get(0);
        final ProducedType type = gen.getReturnTypeOfCallable(typeModel);
        CallableBuilder inner = new CallableBuilder(gen, type, parameterList);
        inner.parameterTypes = inner.getParameterTypesFromCallableModel();//FromParameterModels();
        class InstanceDefaultValueCall implements DefaultValueMethodTransformation {            
            @Override
            public JCExpression makeDefaultValueMethod(AbstractTransformer gen, Parameter defaultedParam) {
                return gen.makeQualIdent(gen.naming.makeUnquotedIdent(instanceName), 
                        Naming.getDefaultedParamMethodName((Declaration)methodOrClass, defaultedParam));
            }
        }
        inner.defaultValueCall = new InstanceDefaultValueCall();
        CallBuilder callBuilder = CallBuilder.instance(gen);
        if (methodOrClass instanceof Method) {
            callBuilder.invoke(gen.naming.makeQualifiedName(gen.naming.makeUnquotedIdent(instanceName), (Method)methodOrClass, Naming.NA_MEMBER));
        } else if (methodOrClass instanceof Class) {
            if (Strategy.generateInstantiator((Class)methodOrClass)) {
                callBuilder.invoke(gen.naming.makeInstantiatorMethodName(gen.naming.makeUnquotedIdent(instanceName), (Class)methodOrClass));
            } else {
                callBuilder.instantiate(new ExpressionAndType(gen.naming.makeUnquotedIdent(instanceName), null), 
                        gen.makeJavaType(((Class)methodOrClass).getType(), JT_CLASS_NEW));
            }
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
        if (methodOrClass instanceof Method) {
            innerInvocation = gen.expressionGen().applyErasureAndBoxing(innerInvocation, methodOrClass.getType(), !((Method)methodOrClass).getUnboxed(), BoxingStrategy.BOXED, methodOrClass.getType());
        }
        inner.useBody(List.<JCStatement>of(gen.make().Return(innerInvocation)));
        
        ParameterList outerPl = new ParameterList();
        ValueParameter instanceParameter = new ValueParameter();
        instanceParameter.setName(instanceName);
        instanceParameter.setType(gen.getParameterTypeOfCallable(typeModel, 0));
        instanceParameter.setUnboxed(false);
        outerPl.getParameters().add(instanceParameter);
        CallableBuilder outer = new CallableBuilder(gen, typeModel, outerPl);
        outer.parameterTypes = outer.getParameterTypesFromParameterModels();
        outer.useBody(List.<JCStatement>of(gen.make().Return(inner.build())));
        
        return outer;
    }
    
    /**
     * Used for "static" value references like
     *     value x = Integer.plus;
     *     value y = Foo.method;
     *     value z = Outer.Inner;
     */
    public static CallableBuilder unboundValueMemberReference(CeylonTransformer gen, 
            ProducedType typeModel, 
            final Value value) {
        final String instanceName = "$instance";
        
        CallBuilder callBuilder = CallBuilder.instance(gen);
        callBuilder.invoke(gen.naming.makeQualifiedName(gen.naming.makeUnquotedIdent(instanceName), value, Naming.NA_MEMBER));
        JCExpression innerInvocation = callBuilder.build();
        innerInvocation = gen.expressionGen().applyErasureAndBoxing(innerInvocation, value.getType(), !value.getUnboxed(), BoxingStrategy.BOXED, value.getType());
        
        ParameterList outerPl = new ParameterList();
        ValueParameter instanceParameter = new ValueParameter();
        instanceParameter.setName(instanceName);
        instanceParameter.setType(gen.getParameterTypeOfCallable(typeModel, 0));
        instanceParameter.setUnboxed(false);
        outerPl.getParameters().add(instanceParameter);
        CallableBuilder outer = new CallableBuilder(gen, typeModel, outerPl);
        outer.parameterTypes = outer.getParameterTypesFromParameterModels();
        outer.useBody(List.<JCStatement>of(gen.make().Return(innerInvocation)));
        
        return outer;
    }
    
    /**
     * Constructs an {@code AbstractCallable} suitable for an anonymous function.
     */
    public static CallableBuilder anonymous(
            CeylonTransformer gen, Tree.Expression expr, ParameterList parameterList, 
            Tree.ParameterList parameterListTree, 
            ProducedType callableTypeModel, boolean noDelegates) {
        JCExpression transformedExpr = gen.expressionGen().transformExpression(expr, BoxingStrategy.BOXED, gen.getReturnTypeOfCallable(callableTypeModel));
        final List<JCStatement> stmts = List.<JCStatement>of(gen.make().Return(transformedExpr));
        return methodArgument(gen, callableTypeModel, parameterList, parameterListTree, stmts, noDelegates);
    }

    public static CallableBuilder methodArgument(
            CeylonTransformer gen,
            ProducedType callableTypeModel,
            ParameterList parameterList,
            Tree.ParameterList parameterListTree, 
            List<JCStatement> stmts) {
        return methodArgument(gen, callableTypeModel, parameterList, parameterListTree, stmts, false);
    }
    
    public static CallableBuilder methodArgument(
            CeylonTransformer gen,
            ProducedType callableTypeModel,
            ParameterList parameterList,
            Tree.ParameterList parameterListTree, 
            List<JCStatement> stmts, boolean noDelegates) {
        
        CallableBuilder cb = new CallableBuilder(gen, callableTypeModel, parameterList);
        cb.parameterTypes = cb.getParameterTypesFromParameterModels();
        cb.parameterDefaultValueMethods(parameterListTree);
        cb.noDelegates = noDelegates;
        cb.useBody(stmts);
        return cb;
    }
    
    /**
     * Constructs an {@code AbstractCallable} suitable for use in a method 
     * definition with a multiple parameter list.
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
        cb.useBody(body);
        return cb;
    }
    
    private ListBuffer<JCTree> parameterDefaultValueMethods;
    
    private ListBuffer<JCTree> callMethods = new ListBuffer<JCTree>();
    
    public CallableBuilder forwardTo(Tree.Term forwardCallTo) {
        if (forwardCallTo == null) {
            throw new RuntimeException();
        }
       
        // now generate a method for each supported minimum number of parameters below 4
        // which delegates to the $call$typed method if required
        for(int i=minimumParams,max = Math.min(numParams,4);i<max;i++){
            ListBuffer<JCStatement> stmts = new ListBuffer<JCStatement>();
            makeDefaultedCall(i, stmts, true);
            makeForwardedInvocation(i, stmts, forwardCallTo);
            callMethods.append(makeCallMethod(stmts.toList(), i));
        }
   
        // generate the $call method for the max number of parameters,
        // which delegates to the $call$typed method if required
        ListBuffer<JCStatement> stmts = new ListBuffer<JCStatement>();
        makeDefaultedCall(numParams, stmts, true);
        makeForwardedInvocation(numParams, stmts, forwardCallTo);
        callMethods.append(makeCallMethod(stmts.toList(), numParams));
        
        return this;
    }
    
    public CallableBuilder useBody(List<JCStatement> body) {
        if (!noDelegates) {
            // now generate a method for each supported minimum number of parameters below 4
            // which delegates to the $call$typed method if required
            for(int i=minimumParams,max = Math.min(numParams,4);i<max;i++){
                ListBuffer<JCStatement> stmts = new ListBuffer<JCStatement>();
                makeDefaultedCall(i, stmts, false);
                makeCallTypedCallOrBody(i, stmts, body);
                callMethods.append(makeCallMethod(stmts.toList(), i));
            }
        }
        // generate the $call method for the max number of parameters,
        // which delegates to the $call$typed method if required
        ListBuffer<JCStatement> stmts = new ListBuffer<JCStatement>();
        makeDefaultedCall(numParams, stmts, false);
        makeCallTypedCallOrBody(numParams, stmts, body);
        callMethods.append(makeCallMethod(stmts.toList(), numParams));
        // generate the $call$typed method if required
        if(hasOptionalParameters)
            callMethods.append(makeCallTypedMethod(body));
        return this;
    }
    
    public CallableBuilder parameterDefaultValueMethods(Tree.ParameterList parameterListTree) {
        if (parameterDefaultValueMethods == null) {
            parameterDefaultValueMethods = ListBuffer.lb();
        }
        for(Tree.Parameter p : parameterListTree.getParameters()){
            if(p.getDefaultArgument() != null || p.getDeclarationModel().isSequenced()){
                MethodDefinitionBuilder methodBuilder = gen.classGen().makeParamDefaultValueMethod(false, null, parameterListTree, p, null);
                this.parameterDefaultValueMethods.append(methodBuilder.build());
            }
        }
        return this;
    }
    
    public JCNewClass build() {
        // Generate a subclass of Callable
        ListBuffer<JCTree> classBody = new ListBuffer<JCTree>();
        
        if (parameterDefaultValueMethods != null) {
            classBody.appendList(parameterDefaultValueMethods);
        }
        
        classBody.appendList(callMethods);
        
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
            if(p instanceof FunctionalParameter)
                pt = gen.getTypeForFunctionalParameter((FunctionalParameter) p);
            else
                pt = p.getType();
            parameterTypes.add(pt);
        }
        return parameterTypes;
    }
    
    private JCExpression getTypedParameter(Parameter param, int argIndex, boolean varargs){
        JCExpression argExpr;
        if (!varargs) {
            // The Callable has overridden one of the non-varargs call() 
            // methods
            argExpr = gen.make().Ident(
                    makeParamName(gen, argIndex));
        } else {
            // The Callable has overridden the varargs call() method
            // so we need to index into the varargs array
            argExpr = gen.make().Indexed(
                    gen.make().Ident(makeParamName(gen, 0)), 
                    gen.make().Literal(argIndex));
        }
        // make sure we unbox it if required
        argExpr = gen.expressionGen().applyErasureAndBoxing(argExpr, 
                parameterTypes.get(argIndex), // it came in as Object, but we need to pretend its type
                // is the parameter type because that's how unboxing determines how it has to unbox
                true, // it's completely erased
                true, // it came in boxed
                CodegenUtil.getBoxingStrategy(param), // see if we need to box 
                parameterTypes.get(argIndex), // see what type we need
                ExpressionTransformer.EXPR_DOWN_CAST); // we're effectively downcasting it from Object
        return argExpr;
    }
    
    private void makeDefaultedCall(final int i, ListBuffer<JCStatement> stmts, boolean isForwarding) {
        // collect every parameter
        int a = 0;
        for(Parameter param : paramLists.getParameters()){
            // don't read default parameter values for forwarded calls
            if(isForwarding && i == a)
                break;
            stmts.append(makeArgumentVar(param, a, i, isForwarding));
            a++;
        }
    }
    private void makeCallTypedCallOrBody(final int i, ListBuffer<JCStatement> stmts, List<JCStatement> body) {
        if(hasOptionalParameters){
            // chain to n param typed method
            List<JCExpression> args = List.nil();
            // pass along the parameters
            for(int a=paramLists.getParameters().size()-1;a>=0;a--){
                Parameter param = paramLists.getParameters().get(a);
                args = args.prepend(gen.makeUnquotedIdent(getCallableTempVarName(param, false)));
            }
            JCMethodInvocation chain = gen.make().Apply(null, gen.makeUnquotedIdent(Naming.getCallableTypedMethodName()), args);
            stmts.append(gen.make().Return(chain));
        }else{
            // insert the method body directly
            stmts.appendList(body);
        }
        return;
    }

    /**
     * <p>Makes a variable declaration for variable which will be used as an 
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
    private JCVariableDecl makeArgumentVar(final Parameter param, 
            final int a, final int i, boolean isForwarding) {
        // read the value
        JCExpression paramExpression = getTypedParameter(param, a, i>3);
        JCExpression varInitialExpression;
        if(param.isDefaulted() || param.isSequenced()){
            if(i > 3){
                // must check if it's defined
                JCExpression test = gen.make().Binary(JCTree.GT, gen.makeSelect(getParamName(0), "length"), gen.makeInteger(a));
                JCExpression elseBranch = makeDefaultValueCall(param, a, isForwarding);
                varInitialExpression = gen.make().Conditional(test, paramExpression, elseBranch);
            }else if(a >= i){
                // get its default value because we don't have it
                varInitialExpression = makeDefaultValueCall(param, a, isForwarding);
            }else{
                // we must have it
                varInitialExpression = paramExpression;
            }
        }else{
            varInitialExpression = paramExpression;
        }
        // store it in a local var
        int flags = 0;
        if(!CodegenUtil.isUnBoxed(param)){
            flags |= AbstractTransformer.JT_NO_PRIMITIVES;
        }
        // Always go raw if we're forwarding, because we're building the call ourselves and we don't get a chance to apply erasure and
        // casting to parameter expressions when we pass them to the forwarded method. Ideally we could set it up correctly so that
        // the proper erasure is done when we read from the Callable.call Object param, but since we store it in a variable defined here,
        // we'd need to duplicate some of the erasure logic here to make or not the type raw, and that would be worse.
        // Besides, named parameter invocation does the same.
        // See https://github.com/ceylon/ceylon-compiler/issues/1005
        if(isForwarding)
            flags |= AbstractTransformer.JT_RAW;
        JCVariableDecl var = gen.make().VarDef(gen.make().Modifiers(Flags.FINAL), 
                gen.naming.makeUnquotedName(getCallableTempVarName(param, isForwarding)), 
                gen.makeJavaType(parameterTypes.get(a), flags),
                varInitialExpression);
        return var;
    }

    private void makeForwardedInvocation(int i, ListBuffer<JCStatement> stmts, Tree.Term forwardCallTo) {
        final Tree.MemberOrTypeExpression primary;
        if (forwardCallTo instanceof Tree.MemberOrTypeExpression) {
            primary = (Tree.MemberOrTypeExpression)forwardCallTo;
        } else {
            throw new RuntimeException(forwardCallTo.getNodeType());
        }
        TypeDeclaration primaryDeclaration = forwardCallTo.getTypeModel().getDeclaration();
        CallableInvocation invocationBuilder = new CallableInvocation (
                gen,
                primary,
                primaryDeclaration,
                primary.getTarget(),
                gen.getReturnTypeOfCallable(forwardCallTo.getTypeModel()),
                forwardCallTo, 
                paramLists,
                i);
        boolean prevCallableInv = gen.expressionGen().withinSyntheticClassBody(true);
        JCExpression invocation;
        try {
            invocation = gen.expressionGen().transformInvocation(invocationBuilder);
        } finally {
            gen.expressionGen().withinSyntheticClassBody(prevCallableInv);
        }
        stmts.append(gen.make().Return(invocation));
    }

    private String getCallableTempVarName(Parameter param, boolean isForwarding) {
        // prefix them with $$ if we only forward, otherwise we need them to have the proper names
        return isForwarding ? Naming.getCallableTempVarName(param) : param.getName();
    }

    private JCExpression makeDefaultValueCall(Parameter defaultedParam, int i, boolean isForwarding){
        // add the default value
        List<JCExpression> defaultMethodArgs = List.nil();
        // pass all the previous values
        for(int a=i-1;a>=0;a--){
            Parameter param = paramLists.getParameters().get(a);
            JCExpression previousValue = gen.makeUnquotedIdent(getCallableTempVarName(param, isForwarding));
            defaultMethodArgs = defaultMethodArgs.prepend(previousValue);
        }
        // now call the default value method
        return gen.make().Apply(null, 
                defaultValueCall.makeDefaultValueMethod(gen, defaultedParam), 
                defaultMethodArgs);
    }
    
    private JCTree makeCallMethod(List<JCStatement> body, int numParams) {
        MethodDefinitionBuilder callMethod = MethodDefinitionBuilder.callable(gen);
        callMethod.isOverride(true);
        callMethod.modifiers(Flags.PUBLIC);
        ProducedType returnType = gen.getReturnTypeOfCallable(typeModel);
        callMethod.resultType(gen.makeJavaType(returnType, JT_NO_PRIMITIVES), null);
        // Now append formal parameters
        switch (numParams) {
        case 3:
            callMethod.parameter(makeCallableCallParam(0, numParams-3));
            // fall through
        case 2:
            callMethod.parameter(makeCallableCallParam(0, numParams-2));
            // fall through
        case 1:
            callMethod.parameter(makeCallableCallParam(0, numParams-1));
            break;
        case 0:
            break;
        default: // use varargs
            callMethod.parameter(makeCallableCallParam(Flags.VARARGS, 0));
        }
        
        // Return the call result, or null if a void method
        callMethod.body(body);
        return callMethod.build();
    }

    private JCTree makeCallTypedMethod(List<JCStatement> body) {
        // make the method
        MethodDefinitionBuilder methodBuilder = MethodDefinitionBuilder.systemMethod(gen, Naming.getCallableTypedMethodName());
        methodBuilder.noAnnotations();
        methodBuilder.modifiers(Flags.PRIVATE);
        ProducedType returnType = gen.getReturnTypeOfCallable(typeModel);
        methodBuilder.resultType(gen.makeJavaType(returnType, JT_NO_PRIMITIVES), null);
        // add all parameters
        int i=0;
        for(Parameter param : paramLists.getParameters()){
            ParameterDefinitionBuilder parameterBuilder = ParameterDefinitionBuilder.instance(gen, param.getName());
            JCExpression paramType = gen.makeJavaType(parameterTypes.get(i));
            parameterBuilder.type(paramType, null);
            methodBuilder.parameter(parameterBuilder);
            i++;
        }
        // Return the call result, or null if a void method
        methodBuilder.body(body);
        return methodBuilder.build();
    }

    private static Name makeParamName(AbstractTransformer gen, int paramIndex) {
        return gen.names().fromString(getParamName(paramIndex));
    }

    private static String getParamName(int paramIndex) {
        return "$param$"+paramIndex;
    }
    
    private ParameterDefinitionBuilder makeCallableCallParam(long flags, int ii) {
        JCExpression type = gen.makeIdent(gen.syms().objectType);
        if ((flags & Flags.VARARGS) != 0) {
            type = gen.make().TypeArray(type);
        }
        ParameterDefinitionBuilder pdb = ParameterDefinitionBuilder.instance(gen, getParamName(ii));
        pdb.modifiers(Flags.FINAL | flags);
        pdb.type(type, null);
        return pdb;
        /*
        return gen.make().VarDef(gen.make().Modifiers(Flags.FINAL | Flags.PARAMETER | flags), 
                makeParamName(gen, ii), type, null);
                */
    }
}
