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

import com.redhat.ceylon.compiler.java.codegen.AbstractTransformer.BoxingStrategy;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ParameterList;
import com.redhat.ceylon.compiler.typechecker.model.ProducedReference;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCBinary;
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

    private final AbstractTransformer gen;
    private ProducedType typeModel;
    private List<JCStatement> body;
    private ParameterList paramLists;
    private Tree.ParameterList parameterListTree;
    
    private CallableBuilder(CeylonTransformer gen) {
        this.gen = gen;
    }
    
    /**
     * Constructs an {@code AbstractCallable} suitable for wrapping a method reference.
     */
    public static CallableBuilder methodReference(CeylonTransformer gen, Tree.Term expr, ParameterList parameterList) {
        JCExpression fnCall;
        InvocationBuilder invocationBuilder = InvocationBuilder.forCallableInvocation(gen, expr, parameterList);
        boolean prevCallableInv = gen.expressionGen().withinCallableInvocation(true);
        try {
            fnCall = invocationBuilder.build();
        } finally {
            gen.expressionGen().withinCallableInvocation(prevCallableInv);
        }
        
        CallableBuilder cb = new CallableBuilder(gen);
        cb.paramLists = parameterList;
        cb.typeModel = expr.getTypeModel();
        cb.body = List.<JCStatement>of(gen.make().Return(fnCall));    
        
        return cb;
    }
    
    /**
     * Constructs an {@code AbstractCallable} suitable for an anonymous function.
     * @param parameterList2 
     */
    public static CallableBuilder anonymous(
            CeylonTransformer gen, Tree.Expression expr, ParameterList parameterList, 
            Tree.ParameterList parameterListTree, 
            ProducedType callableTypeModel) {
        JCExpression transformedExpr = gen.expressionGen().transformExpression(expr);
        final List<JCStatement> stmts = List.<JCStatement>of(gen.make().Return(transformedExpr));
        return methodArgument(gen, callableTypeModel, parameterList, parameterListTree, stmts);
    }

    public static CallableBuilder methodArgument(
            CeylonTransformer gen,
            ProducedType callableTypeModel,
            ParameterList parameterList,
            Tree.ParameterList parameterListTree, 
            List<JCStatement> stmts) {
        
        CallableBuilder cb = new CallableBuilder(gen);
        cb.paramLists = parameterList;
        cb.typeModel = callableTypeModel;
        cb.body = stmts;
        cb.parameterListTree = parameterListTree;
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

        CallableBuilder cb = new CallableBuilder(gen);
        cb.paramLists = parameterList;
        cb.typeModel = typeModel;
        if (body == null) {
            body = List.<JCStatement>nil();
        }
        cb.body = body;
        cb.parameterListTree = parameterListTree;
        return cb;
    }

    public JCNewClass build() {
        // Generate a subclass of Callable
        ListBuffer<JCTree> classBody = new ListBuffer<JCTree>();
        int numParams = paramLists.getParameters().size();
        int minimumParams = 0;
        for(Parameter p : paramLists.getParameters()){
            if(p.isDefaulted())
                break;
            minimumParams++;
        }
        boolean isVariadic = minimumParams != numParams;
        // generate a method for each defaulted param
        for(Tree.Parameter p : parameterListTree.getParameters()){
            if(p.getDefaultArgument() != null){
                MethodDefinitionBuilder methodBuilder = gen.classGen().makeParamDefaultValueMethod(false, null, parameterListTree, p);
                classBody.append(methodBuilder.build());
            }
        }
        
        // now generate a method for each supported minimum number of parameters below 4
        // which delegates to the $call$typed method if required
        for(int i=minimumParams,max = Math.min(numParams,4);i<max;i++){
            classBody.append(makeDefaultedCall(i, isVariadic));
        }
        // generate the $call method for the max number of parameters,
        // which delegates to the $call$typed method if required
        classBody.append(makeDefaultedCall(numParams, isVariadic));
        // generate the $call$typed method if required
        if(isVariadic)
            classBody.append(makeCallTypedMethod(body));
        
        JCClassDecl classDef = gen.make().AnonymousClassDef(gen.make().Modifiers(0), classBody.toList());
        
        JCNewClass instance = gen.make().NewClass(null, 
                null, 
                gen.makeJavaType(typeModel, JT_EXTENDS | JT_CLASS_NEW), 
                List.<JCExpression>of(gen.make().Literal(typeModel.getProducedTypeName(true))),
                classDef);
        return instance;
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
                gen.typeFact().getObjectDeclaration().getType(), // it came in as Object 
                true, // it came in boxed
                CodegenUtil.getBoxingStrategy(param), // see if we need to box 
                param.getType()); // see what type we need
        return argExpr;
    }
    
    private JCTree makeDefaultedCall(int i, boolean isVariadic) {
        // collect every parameter
        int a = 0;
        ListBuffer<JCStatement> stmts = new ListBuffer<JCStatement>();
        for(Parameter param : paramLists.getParameters()){
            // read the value
            JCExpression paramExpression = getTypedParameter(param, a, i>3);
            JCExpression varInitialExpression;
            if(param.isDefaulted()){
                if(i > 3){
                    // must check if it's defined
                    JCExpression test = gen.make().Binary(JCTree.GT, gen.makeSelect(getParamName(0), "length"), gen.makeInteger(a));
                    JCExpression elseBranch = makeDefaultValueCall(param, a);
                    varInitialExpression = gen.make().Conditional(test, paramExpression, elseBranch);
                }else if(a >= i){
                    // get its default value because we don't have it
                    varInitialExpression = makeDefaultValueCall(param, a);
                }else{
                    // we must have it
                    varInitialExpression = paramExpression;
                }
            }else{
                varInitialExpression = paramExpression;
            }
            // store it in a local var
            JCStatement var = gen.make().VarDef(gen.make().Modifiers(0), 
                    gen.naming.makeUnquotedName(param.getName()), 
                    gen.makeJavaType(param.getType()),
                    varInitialExpression);
            stmts.append(var);
            a++;
        }
        if(isVariadic){
            // chain to n param typed method
            List<JCExpression> args = List.nil();
            // pass along the parameters
            for(a=paramLists.getParameters().size()-1;a>=0;a--){
                Parameter param = paramLists.getParameters().get(a);
                args = args.prepend(gen.makeUnquotedIdent(param.getName()));
            }
            JCMethodInvocation chain = gen.make().Apply(null, gen.makeUnquotedIdent(Naming.getCallableTypedMethodName()), args);
            stmts.append(gen.make().Return(chain));
        }else{
            // insert the method body directly
            stmts.appendList(this.body);
        }
        List<JCStatement> body = stmts.toList();
        return makeCallMethod(body, i);
    }

    private JCExpression makeDefaultValueCall(Parameter defaultedParam, int i){
        // add the default value
        List<JCExpression> defaultMethodArgs = List.nil();
        // pass all the previous values
        for(int a=i-1;a>=0;a--){
            Parameter param = paramLists.getParameters().get(a);
            JCExpression previousValue = gen.makeUnquotedIdent(param.getName());
            defaultMethodArgs = defaultMethodArgs.prepend(previousValue);
        }
        // now call the default value method
        return gen.make().Apply(null, gen.makeUnquotedIdent(Naming.getDefaultedParamMethodName(null, defaultedParam)), defaultMethodArgs);
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
        MethodDefinitionBuilder methodBuilder = MethodDefinitionBuilder.method(gen, false, Naming.getCallableTypedMethodName());
        methodBuilder.modifiers(Flags.PRIVATE);
        ProducedType returnType = gen.getReturnTypeOfCallable(typeModel);
        methodBuilder.resultType(gen.makeJavaType(returnType, JT_NO_PRIMITIVES), null);
        // add all parameters
        for(Parameter param : paramLists.getParameters()){
            ParameterDefinitionBuilder parameterBuilder = ParameterDefinitionBuilder.instance(gen, param.getName());
            JCExpression paramType = gen.makeJavaType(param.getType());
            parameterBuilder.type(paramType, null);
            methodBuilder.parameter(parameterBuilder);
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
