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

import com.redhat.ceylon.compiler.java.codegen.AbstractTransformer.BoxingStrategy;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ParameterList;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCNewClass;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCTypeCast;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;

public class CallableBuilder {

    private final AbstractTransformer gen;
    private ProducedType typeModel;
    private List<JCStatement> body;
    private ParameterList paramLists;
    
    private CallableBuilder(CeylonTransformer gen) {
        this.gen = gen;
    }
    
    public static CallableBuilder functional(CeylonTransformer gen, Tree.Term expr, ParameterList parameterList) {
        JCExpression fnCall;
        InvocationBuilder invocationBuilder = InvocationBuilder.invocationForCallable(gen, expr, parameterList);
        gen.expressionGen().setWithinCallableInvocation(true);
        try {
            fnCall = invocationBuilder.build();
        } finally {
            gen.expressionGen().setWithinInvocation(false);
        }
        
        CallableBuilder cb = new CallableBuilder(gen);
        cb.paramLists = parameterList;
        cb.typeModel = expr.getTypeModel();
        ProducedType returnType = gen.getCallableReturnType(cb.typeModel);
        if (gen.isVoid(returnType)) {
            cb.body = List.<JCStatement>of(gen.make().Exec(fnCall), gen.make().Return(gen.makeNull()));
        } else {
            cb.body = List.<JCStatement>of(gen.make().Return(fnCall));
        }
        
        return cb;
    }
    
    public static CallableBuilder mpl(
            CeylonTransformer gen,
            ProducedType typeModel,
            ParameterList parameterList,
            List<JCStatement> body) {

        CallableBuilder cb = new CallableBuilder(gen);
        cb.paramLists = parameterList;
        int ii =0;
        for (Parameter p : parameterList.getParameters()) {
            JCExpression init = unpickCallableParameter(gen, true, null, p, null, ii, parameterList.getParameters().size());
            JCVariableDecl varDef = gen.make().VarDef(gen.make().Modifiers(Flags.FINAL), 
                    gen.names().fromString(p.getName()), 
                    gen.makeJavaType(p.getType(), AbstractTransformer.NO_PRIMITIVES), 
                    init);
            body = body.prepend(varDef);
            ii++;
        }
        cb.typeModel = typeModel;
        cb.body = body;
        return cb;
    }
    
    public JCNewClass build() {
        // Generate a subclass of Callable
        MethodDefinitionBuilder callMethod = MethodDefinitionBuilder.method(gen, false, true, "$call");
        callMethod.isActual(true);
        callMethod.modifiers(Flags.PUBLIC);
        ProducedType returnType = gen.getCallableReturnType(typeModel);
        callMethod.resultType(gen.makeJavaType(returnType, AbstractTransformer.EXTENDS), null);
        // Now append formal parameters
        int numParams = paramLists.getParameters().size();
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
        
        JCClassDecl classDef = gen.make().AnonymousClassDef(gen.make().Modifiers(0), List.<JCTree>of(callMethod.build()));
        
        JCNewClass instance = gen.make().NewClass(null, 
                null, 
                gen.makeJavaType(typeModel, AbstractTransformer.EXTENDS | AbstractTransformer.CLASS_NEW), 
                List.<JCExpression>of(gen.make().Literal(typeModel.getProducedTypeName())), 
                classDef);
        return instance;
    }
    
    private static Name makeParamName(AbstractTransformer gen, int paramIndex) {
        return gen.names().fromString("$param$"+paramIndex);
    }
    
    private JCVariableDecl makeCallableCallParam(long flags, int ii) {
        JCExpression type = gen.makeIdent(gen.syms().objectType);
        if ((flags & Flags.VARARGS) != 0) {
            type = gen.make().TypeArray(type);
        }
        return gen.make().VarDef(gen.make().Modifiers(Flags.FINAL | flags), 
                makeParamName(gen, ii), type, null);
    }
    
    public static JCExpression unpickCallableParameter(AbstractTransformer gen,
            boolean isRaw,
            java.util.List<ProducedType> typeArgumentModels,
            Parameter param,
            ProducedType argType, 
            int argIndex,
            int numParameters) {
        JCExpression argExpr;
        if (numParameters <= 3) {
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
        ProducedType castType;
        if (argType != null) {
            castType = argType;
        } else {
            castType = gen.getTypeForParameter(param, isRaw, typeArgumentModels);
        }
        JCTypeCast cast = gen.make().TypeCast(gen.makeJavaType(castType, AbstractTransformer.NO_PRIMITIVES), argExpr);
        // TODO Should this be calling applyErasureAndBoxing() instead?
        BoxingStrategy boxingStrategy;
        if (param.getUnboxed() == null) {
            boxingStrategy = BoxingStrategy.INDIFFERENT;
        } else if (param.getUnboxed()) {
            boxingStrategy = BoxingStrategy.UNBOXED; 
        } else {
            boxingStrategy = BoxingStrategy.BOXED;
        }
        JCExpression boxed = gen.boxUnboxIfNecessary(cast, true, 
                param.getType(), boxingStrategy);
        return boxed;
    }

    
    
}
