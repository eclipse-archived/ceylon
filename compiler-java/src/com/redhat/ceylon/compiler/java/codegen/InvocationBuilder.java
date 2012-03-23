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
import com.redhat.ceylon.compiler.java.codegen.ExpressionTransformer.TermTransformer;
import com.redhat.ceylon.compiler.java.util.Decl;
import com.redhat.ceylon.compiler.java.util.Util;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Functional;
import com.redhat.ceylon.compiler.typechecker.model.FunctionalParameter;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ParameterList;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.NamedArgumentList;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.PositionalArgumentList;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Primary;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;

abstract class InvocationBuilder {

    private final AbstractTransformer gen;
    protected final Node node;    
    protected final Tree.Primary primary;
    protected final Declaration primaryDeclaration;
    protected final ProducedType returnType;
    protected final List<JCExpression> transformedTypArguments;
    protected boolean unboxed;
    protected BoxingStrategy boxingStrategy;
    
    protected final ListBuffer<JCVariableDecl> vars = ListBuffer.lb();
    protected final ListBuffer<JCExpression> args = ListBuffer.lb();
    protected final java.util.List<ProducedType> typeArguments;
    protected String callVarName;
    
    protected InvocationBuilder(AbstractTransformer gen, 
            Tree.Primary primary, Declaration primaryDeclaration,
            ProducedType returnType, Node node) {
        this.gen = gen;
        this.primary = primary;
        this.primaryDeclaration = primaryDeclaration;
        this.returnType = returnType;
        this.node = node;
        if (primary instanceof Tree.StaticMemberOrTypeExpression){
            this.typeArguments = ((Tree.StaticMemberOrTypeExpression)primary).getTypeArguments().getTypeModels();
        } else {
            this.typeArguments = null;
        }
        this.transformedTypArguments = transformTypeArguments(gen, getTypeArguments());
    }
    
    static final List<JCExpression> transformTypeArguments(
            AbstractTransformer gen,
            java.util.List<ProducedType> typeArguments) {
        List<JCExpression> result = List.<JCExpression> nil();
        if(typeArguments != null){
            for (ProducedType arg : typeArguments) {
                // cancel type parameters and go raw if we can't specify them
                if(gen.willEraseToObject(arg))
                        //|| gen().isTypeParameter(arg))
                    return List.nil();
                result = result.append(gen.makeJavaType(arg, AbstractTransformer.TYPE_ARGUMENT));
            }
        }
        return result;
    }
    
    protected abstract void compute();
    
    protected final AbstractTransformer gen() {
        return gen;
    }
    
    protected final Declaration getPrimaryDeclaration() {
        return primaryDeclaration;
    }

    public final boolean isUnboxed() {
        return unboxed;
    }

    public final void setUnboxed(boolean unboxed) {
        this.unboxed = unboxed;
    }

    public final BoxingStrategy getBoxingStrategy() {
        return boxingStrategy;
    }

    public final void setBoxingStrategy(BoxingStrategy boxingStrategy) {
        this.boxingStrategy = boxingStrategy;
    }

    protected final java.util.List<ProducedType> getTypeArguments() {
        return this.typeArguments;
    }
    
    // Make a list of $arg0, $arg1, ... , $argN
    protected final List<JCExpression> makeVarRefArgumentList(String varBaseName, int argCount) {
        List<JCExpression> names = List.<JCExpression> nil();
        for (int i = 0; i < argCount; i++) {
            names = names.append(gen().makeUnquotedIdent(varBaseName + "$" + i));
        }
        return names;
    }
    
    // Make a list of $arg$this$, $arg0, $arg1, ... , $argN
    protected final List<JCExpression> makeThisVarRefArgumentList(String varBaseName, int argCount, boolean needsThis) {
        List<JCExpression> names = List.<JCExpression> nil();
        if (needsThis) {
            names = names.append(gen().makeUnquotedIdent(varBaseName + "$this$"));
        }
        names = names.appendList(makeVarRefArgumentList(varBaseName, argCount));
        return names;
    }
    
    protected JCExpression transformInvocation(JCExpression primaryExpr, String selector) {
        
        JCExpression actualPrimExpr = transformInvocationPrimary(primaryExpr, selector);
        
        JCExpression resultExpr;
        if (primary instanceof Tree.BaseTypeExpression) {
            ProducedType classType = (ProducedType)((Tree.BaseTypeExpression)primary).getTarget();
            resultExpr = gen().make().NewClass(null, null, gen().makeJavaType(classType, AbstractTransformer.CLASS_NEW), args.toList(), null);
        } else if (primary instanceof Tree.QualifiedTypeExpression) {
            resultExpr = gen().make().NewClass(actualPrimExpr, null, gen().makeQuotedIdent(selector), args.toList(), null);
        } else {
            if (primaryDeclaration instanceof FunctionalParameter) {
                if (primaryExpr != null) {
                    actualPrimExpr = gen().makeQualIdent(primaryExpr, primaryDeclaration.getName());
                } else {
                    actualPrimExpr = gen().makeUnquotedIdent(primaryDeclaration.getName());
                }
                selector = "$call";
            }
            resultExpr = gen().make().Apply(transformedTypArguments, gen().makeQuotedQualIdent(actualPrimExpr, selector), args.toList());
        }

        resultExpr = applyDefaultParameters(resultExpr);
        
        return resultExpr;
    }

    protected final JCExpression applyDefaultParameters(JCExpression resultExpr) {
        if (vars != null && !vars.isEmpty()) {
            if (returnType == null || gen().isVoid(returnType)) {
                // void methods get wrapped like (let $arg$1=expr, $arg$0=expr in call($arg$0, $arg$1); null)
                resultExpr = gen().make().LetExpr(vars.toList(), List.<JCStatement>of(gen().make().Exec(resultExpr)), gen().makeNull());
            } else {
                // all other methods like (let $arg$1=expr, $arg$0=expr in call($arg$0, $arg$1))
                resultExpr = gen().make().LetExpr(vars.toList(), resultExpr);
            }
        }
        return resultExpr;
    }

    private final JCExpression transformInvocationPrimary(JCExpression primaryExpr,
            String selector) {
        JCExpression actualPrimExpr;
        if (primary instanceof Tree.QualifiedTypeExpression
                && ((Tree.QualifiedTypeExpression)primary).getPrimary() instanceof Tree.BaseTypeExpression) {
            actualPrimExpr = gen().makeSelect(primaryExpr, "this");
        } else {
            actualPrimExpr = primaryExpr;
        }
        if (callVarName != null) {
            // Prepare the first argument holding the primary for the call
            JCExpression callVarExpr = gen().makeUnquotedIdent(callVarName);
            JCVariableDecl callVar;
            ProducedType type = ((Tree.MemberOrTypeExpression)primary).getTarget().getQualifyingType();
            if (primaryExpr != null) {
                callVar = gen().makeVar(callVarName, gen().makeJavaType(type, AbstractTransformer.NO_PRIMITIVES), actualPrimExpr);
            } else {
                callVar = gen().makeVar(callVarName, gen().makeJavaType(type, AbstractTransformer.NO_PRIMITIVES), gen().makeUnquotedIdent("this"));
            }
            vars.prepend(callVar);
            actualPrimExpr = callVarExpr;
        }
        return actualPrimExpr;
    }
    
    protected JCExpression makeInvocation() {
        gen().at(node);
        JCExpression result = gen().expressionGen().transformPrimary(primary, new TermTransformer() {
            @Override
            public JCExpression transform(JCExpression primaryExpr, String selector) {
                return transformInvocation(primaryExpr, selector);
            }
        });
        result = gen().expressionGen().applyErasureAndBoxing(result, returnType, 
                !unboxed, boxingStrategy, returnType);
        return result;
    }

    public final JCExpression build() {
        boolean prevFnCall = gen().expressionGen().isWithinInvocation();
        gen().expressionGen().setWithinInvocation(true);
        try {
            JCExpression invocation = makeInvocation();
            return invocation;
        } finally {
            gen().expressionGen().setWithinInvocation(prevFnCall);
        }
    }
    
    public static InvocationBuilder forSuperInvocation(AbstractTransformer gen,
            Tree.InvocationExpression invocation) {
        Declaration primaryDeclaration = ((Tree.MemberOrTypeExpression)invocation.getPrimary()).getDeclaration();
        java.util.List<ParameterList> paramLists = ((Functional)primaryDeclaration).getParameterLists();
        
        SuperInvocationBuilder builder = new SuperInvocationBuilder(gen,
                invocation,
                paramLists.get(0));
        builder.compute();
        return builder;
    }
    
    public static InvocationBuilder forInvocation(AbstractTransformer gen, 
            final Tree.InvocationExpression invocation) {
        
        Tree.Primary primary = invocation.getPrimary();
        Declaration primaryDeclaration = ((Tree.MemberOrTypeExpression)primary).getDeclaration();
        InvocationBuilder builder;
        if (invocation.getPositionalArgumentList() != null) {
            java.util.List<ParameterList> paramLists = ((Functional)primaryDeclaration).getParameterLists();
            builder = new PositionalInvocationBuilder(gen, 
                    primary, primaryDeclaration,
                    invocation,
                    paramLists.get(0));
        } else if (invocation.getNamedArgumentList() != null) {
            builder = new NamedArgumentInvocationBuilder(gen, 
                    primary, 
                    primaryDeclaration,
                    invocation);
        } else {
            throw new RuntimeException("Illegal State");
        }
        if (primaryDeclaration instanceof FunctionalParameter) {
            // Callables always have boxed return type
            builder.setBoxingStrategy(invocation.getUnboxed() ? BoxingStrategy.UNBOXED : BoxingStrategy.BOXED);
            builder.setUnboxed(false);
        } else {
            builder.setBoxingStrategy(BoxingStrategy.INDIFFERENT);
            builder.setUnboxed(invocation.getUnboxed());
        }
        builder.compute();
        return builder;
    }
    
    public static InvocationBuilder forCallableInvocation(
            AbstractTransformer gen, Tree.Term expr, final ParameterList parameterList) {
        final Tree.MemberOrTypeExpression primary;
        if (expr instanceof Tree.MemberOrTypeExpression) {
            primary = (Tree.MemberOrTypeExpression)expr;
        } else {
            throw new RuntimeException(expr+"");
        }

        TypeDeclaration primaryDeclaration = expr.getTypeModel().getDeclaration();
        
        CallableInvocationBuilder builder = new CallableInvocationBuilder (
                gen,
                primary,
                primaryDeclaration,
                gen.getCallableReturnType(expr.getTypeModel()),
                expr, 
                parameterList);
        builder.compute();
        return builder;
    }
    
    public static InvocationBuilder forSpecifierInvocation(
            CeylonTransformer gen, Tree.SpecifierExpression specifierExpression,
            Method method) {
        Tree.Primary primary = (Tree.Primary)specifierExpression.getExpression().getTerm();
        InvocationBuilder builder;
        if (primary instanceof Tree.MemberOrTypeExpression
                && ((Tree.MemberOrTypeExpression)primary).getDeclaration() instanceof Functional) {
            Declaration primaryDeclaration = ((Tree.MemberOrTypeExpression)primary).getDeclaration();
            builder = new StaticSpecifierInvocationBuilder(
                    gen, 
                    primary, 
                    primaryDeclaration,
                    method,
                    specifierExpression);
            
        } else if (gen.isCeylonCallable(primary.getTypeModel())) {
            builder = new CallableSpecifierInvocationBuilder(
                    gen, 
                    method, 
                    specifierExpression, 
                    primary);
            
        } else {
            throw new RuntimeException();
        }
        
        builder.compute();
        return builder;
    }

}

abstract class AbstractPositionalInvocationBuilder extends InvocationBuilder {

    protected AbstractPositionalInvocationBuilder(
            AbstractTransformer gen,
            Tree.Primary primary,
            Declaration primaryDeclaration,
            ProducedType returnType, 
            Node node) {
        super(gen, primary, primaryDeclaration, returnType, node);
    }
 
    protected final JCExpression transformArg(Tree.Term expr, Parameter parameter, boolean isRaw, java.util.List<ProducedType> typeArgumentModels) {
        if (parameter != null) {
            ProducedType type = gen().getTypeForParameter(parameter, isRaw, typeArgumentModels);
            return gen().expressionGen().transformExpression(expr, 
                    Util.getBoxingStrategy(parameter), 
                    type);
        } else {
            // Overloaded methods don't have a reference to a parameter
            // so we have to treat them differently. Also knowing it's
            // overloaded we know we're dealing with Java code so we unbox
            ProducedType type = expr.getTypeModel();
            return gen().expressionGen().transformExpression(expr, 
                    BoxingStrategy.UNBOXED, 
                    type);
        }
    }
    
    protected abstract java.util.List<Parameter> getDeclaredParameters();
    
    /**
     * Gets the parameter for the given argument index
     */
    protected abstract Parameter getParameter(int argIndex);
    
    /** Gets the number of arguments actually being supplied */
    protected abstract int getNumArguments();
    /** 
     * Gets the expression supplying the argument value for the 
     * given argument index 
     */
    protected abstract Tree.Expression getArgumentExpression(int argIndex);
    /**
     * Gets the transformed expression supplying the argument value for the 
     * given argument index
     */
    protected abstract JCExpression getTransformedArgumentExpression(int argIndex, boolean isRaw, java.util.List<ProducedType> typeArgumentModels);
    
    protected abstract boolean dontBoxSequence();
    
    @Override
    protected final void compute() {
        
        final boolean isRaw = transformedTypArguments.isEmpty();
        int numParameters = getDeclaredParameters().size();
        int numArguments = this.getNumArguments();
        Parameter lastDeclaredParam = getDeclaredParameters().isEmpty() ? null : getDeclaredParameters().get(getDeclaredParameters().size() - 1); 
        if (lastDeclaredParam != null 
                && lastDeclaredParam.isSequenced()
                && !this.dontBoxSequence() // foo(sequence...) syntax => no need to box
                && numArguments >= (numParameters -1)) {
            // => call to a varargs method
            // first, append the normal args
            for (int ii = 0; ii < numParameters - 1; ii++) {
                args.append(this.getTransformedArgumentExpression(ii, isRaw, getTypeArguments()));
            }
            JCExpression boxed;
            // then, box the remaining passed arguments
            if (numParameters -1 == numArguments) {
                // box as Empty
                boxed = gen().makeEmpty();
            } else {
                // box with an ArraySequence<T>
                List<JCExpression> x = List.<JCExpression>nil();
                for (int ii = numParameters - 1; ii < numArguments; ii++) {
                    x = x.append(this.getTransformedArgumentExpression(ii, isRaw, getTypeArguments()));
                }
                boxed = gen().makeSequenceRaw(x);
            }
            args.append(boxed);
        } else if (numArguments < numParameters) {
            String varBaseName = gen().aliasName("arg");
            callVarName = varBaseName + "$callable$";
            boolean needsThis = false;
            if (Decl.withinClassOrInterface(getPrimaryDeclaration())) {
                // first append $this
                ProducedType thisType = gen().getThisType(getPrimaryDeclaration());
                vars.append(gen().makeVar(varBaseName + "$this$", gen().makeJavaType(thisType, AbstractTransformer.NO_PRIMITIVES), gen().makeUnquotedIdent(callVarName)));
                needsThis = true;
            }
            // append the normal args
            int idx = 0;
            for (int ii = 0; ii < this.getNumArguments(); ii++) {
                gen().at(this.getArgumentExpression(ii));
                Tree.Expression expr = this.getArgumentExpression(ii);
                Parameter declaredParam = this.getParameter(ii);
                int index;
                BoxingStrategy boxType;
                ProducedType type;
                if (declaredParam != null) {
                    index = getDeclaredParameters().indexOf(declaredParam);
                    boxType = Util.getBoxingStrategy(declaredParam);
                    type = gen().getTypeForParameter(declaredParam, isRaw, getTypeArguments());
                } else {
                    // Arguments of overloaded methods don't have a reference to parameter
                    index = idx++;
                    boxType = BoxingStrategy.UNBOXED;
                    type = expr.getTypeModel();
                }
                String varName = varBaseName + "$" + index;
                // if we can't pick up on the type from the declaration, revert to the type of the expression
                if(gen().isTypeParameter(type))
                    type = expr.getTypeModel();
                JCExpression typeExpr = gen().makeJavaType(type, (boxType == BoxingStrategy.BOXED) ? AbstractTransformer.TYPE_ARGUMENT : 0);
                JCExpression argExpr = gen().expressionGen().transformExpression(expr, boxType, type);
                JCVariableDecl varDecl = gen().makeVar(varName, typeExpr, argExpr);
                vars.append(varDecl);
            }
            if (!Decl.isOverloaded(getPrimaryDeclaration())) {
                // append any arguments for defaulted parameters
                for (int ii = numArguments; ii < numParameters; ii++) {
                    Parameter param = getDeclaredParameters().get(ii);
                    String varName = varBaseName + "$" + ii;
                    String methodName = Util.getDefaultedParamMethodName(getPrimaryDeclaration(), param);
                    List<JCExpression> arglist = makeThisVarRefArgumentList(varBaseName, ii, needsThis);
                    JCExpression argExpr;
                    if (!param.isSequenced()) {
                        Declaration container = param.getDeclaration().getRefinedDeclaration();
                        if (!container.isToplevel()) {
                            container = (Declaration)container.getContainer();
                        }
                        String className = Util.getCompanionClassName(container.getName());
                        argExpr = gen().at(node).Apply(null, gen().makeQuotedQualIdent(gen().makeQuotedFQIdent(container.getQualifiedNameString()), className, methodName), arglist);
                    } else {
                        argExpr = gen().makeEmpty();
                    }
                    BoxingStrategy boxType = Util.getBoxingStrategy(param);
                    ProducedType type = gen().getTypeForParameter(param, isRaw, getTypeArguments());
                    JCExpression typeExpr = gen().makeJavaType(type, (boxType == BoxingStrategy.BOXED) ? AbstractTransformer.TYPE_ARGUMENT : 0);
                    JCVariableDecl varDecl = gen().makeVar(varName, typeExpr, argExpr);
                    vars.append(varDecl);
                }
                args.appendList(makeVarRefArgumentList(varBaseName, numParameters));
            } else {
                // For overloaded methods (and therefore Java interop) we just pass the arguments we have
                args.appendList(makeVarRefArgumentList(varBaseName, numArguments));
            }
        } else {
            // append the normal args
            for (int ii = 0; ii < this.getNumArguments(); ii++) {
                args.append(this.getTransformedArgumentExpression(ii, 
                        isRaw, getTypeArguments()));
            }
        }
    }
}

class PositionalInvocationBuilder extends AbstractPositionalInvocationBuilder {
    
    private final  PositionalArgumentList positional;
    private final java.util.List<Parameter> declaredParameters;
    
    public PositionalInvocationBuilder(
            AbstractTransformer gen, 
            Primary primary,
            Declaration primaryDeclaration,
            Tree.InvocationExpression invocation,
            ParameterList parameterList) {
        super(gen, primary, primaryDeclaration, invocation.getTypeModel(), invocation);
        positional = invocation.getPositionalArgumentList();
        this.declaredParameters = parameterList.getParameters();
    }
    @Override
    protected java.util.List<Parameter> getDeclaredParameters() {
        return declaredParameters;
    }
    @Override
    protected Tree.Expression getArgumentExpression(int argIndex) {
        return positional.getPositionalArguments().get(argIndex).getExpression();
    }
    @Override
    protected JCExpression getTransformedArgumentExpression(int argIndex, boolean isRaw, java.util.List<ProducedType> typeArgumentModels) {
        return transformArg(
                getArgumentExpression(argIndex), 
                getParameter(argIndex), isRaw, typeArgumentModels);
    }
    @Override
    protected Parameter getParameter(int argIndex) {
        return positional.getPositionalArguments().get(argIndex).getParameter();
    }
    @Override
    protected int getNumArguments() {
        return positional.getPositionalArguments().size();
    }
    @Override
    protected boolean dontBoxSequence() {
        return positional.getEllipsis() != null;
    }
    
}

class SuperInvocationBuilder extends PositionalInvocationBuilder {
    
    SuperInvocationBuilder(AbstractTransformer gen,
            Tree.InvocationExpression invocation,
            ParameterList parameterList) {
        super(gen, 
                invocation.getPrimary(), 
                ((Tree.MemberOrTypeExpression)invocation.getPrimary()).getDeclaration(),
                invocation,
                parameterList);
    }
    protected JCExpression makeInvocation() {
        gen().at(node);
        JCExpression result = gen().make().Apply(List.<JCExpression> nil(), gen().make().Ident(gen().names()._super), args.toList());
        result = applyDefaultParameters(result);
        return result;
    }
}

class CallableInvocationBuilder extends AbstractPositionalInvocationBuilder {
    
    private final java.util.List<Parameter> callableParameters;
    
    private final java.util.List<Parameter> functionalParameters;
    
    public CallableInvocationBuilder(
            AbstractTransformer gen, Tree.MemberOrTypeExpression primary,
            Declaration primaryDeclaration, ProducedType returnType,
            Tree.Term expr, ParameterList parameterList) {
        super(gen, primary, primaryDeclaration, returnType, expr);
        callableParameters = ((Functional)primary.getDeclaration()).getParameterLists().get(0).getParameters();
        functionalParameters = parameterList.getParameters();
        setUnboxed(expr.getUnboxed());
        setBoxingStrategy(BoxingStrategy.BOXED);// Must be boxed because non-primitive return type
    }
    @Override
    protected Tree.Expression getArgumentExpression(int argIndex) {
        throw new RuntimeException("Defaulted parameters are not defaulted when called via a method reference");
    }
    @Override
    protected Parameter getParameter(int argIndex) {
        return getDeclaredParameters().get(argIndex);
    }
    @Override
    protected int getNumArguments() {
        return getDeclaredParameters().size();
    }
    @Override
    protected boolean dontBoxSequence() {
        return getDeclaredParameters().get(getNumArguments() - 1).isSequenced();
    }
    @Override
    protected JCExpression getTransformedArgumentExpression(int argIndex,
            boolean isRaw,
            java.util.List<ProducedType> typeArgumentModels) {
        Parameter param = callableParameters.get(argIndex);
        ProducedType argType = primary.getTypeModel().getTypeArgumentList().get(argIndex+1);
        return CallableBuilder.unpickCallableParameter(gen(), isRaw, typeArgumentModels, param, argType, argIndex, functionalParameters.size());
    }
    @Override
    protected java.util.List<Parameter> getDeclaredParameters() {
        return functionalParameters;
    }
}

class StaticSpecifierInvocationBuilder extends AbstractPositionalInvocationBuilder {
    
    private final Method method;

    public StaticSpecifierInvocationBuilder(
            AbstractTransformer gen, Primary primary,
            Declaration primaryDeclaration,
            Method method, Tree.SpecifierExpression node) {
        super(gen, primary, primaryDeclaration, method.getType(), node);
        this.method = method;
        setUnboxed(primary.getUnboxed());
        setBoxingStrategy(method.getUnboxed() ? BoxingStrategy.UNBOXED : BoxingStrategy.BOXED);
    }

    @Override
    protected int getNumArguments() {
        return getDeclaredParameters().size();
    }
    
    @Override
    protected Tree.Expression getArgumentExpression(int argIndex) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    protected JCExpression getTransformedArgumentExpression(int argIndex,
            boolean isRaw,
            java.util.List<ProducedType> typeArgumentModels) {
        Parameter parameter = getParameter(argIndex);
        ProducedType exprType = gen().expressionGen().getTypeForParameter(parameter, isRaw, typeArgumentModels);
        Parameter declaredParameter = ((Functional)primaryDeclaration).getParameterLists().get(0).getParameters().get(argIndex);
        JCExpression result = gen().makeQuotedIdent(parameter.getName());
        result = gen().expressionGen().applyErasureAndBoxing(
                result, 
                exprType, 
                !parameter.getUnboxed(), 
                Util.getBoxingStrategy(declaredParameter), 
                declaredParameter.getType());
        return result;
    }
    
    @Override
    protected Parameter getParameter(int argIndex) {
        return getDeclaredParameters().get(argIndex);
    }
    
    @Override
    protected boolean dontBoxSequence() {
        return method.getParameterLists().get(0).getParameters().get(getNumArguments() - 1).isSequenced();
    }

    @Override
    protected java.util.List<Parameter> getDeclaredParameters() {
        return method.getParameterLists().get(0).getParameters();
    }
}

class CallableSpecifierInvocationBuilder extends InvocationBuilder {
    
    private final Method method;
    private final JCExpression callable;
    public CallableSpecifierInvocationBuilder(
            AbstractTransformer gen, 
            Method method, 
            Tree.SpecifierExpression specifierExpression,
            Tree.Term term) {
        super(gen, null, null, method.getType(), term);
        this.callable = gen.expressionGen().transformExpression(term);
        this.method = method;
        // Because we're calling a callable, and they always return a 
        // boxed result
        setUnboxed(false);
        setBoxingStrategy(method.getUnboxed() ? BoxingStrategy.UNBOXED : BoxingStrategy.BOXED);
    }
    @Override
    protected void compute() {
        boolean isRaw = transformedTypArguments.isEmpty();
        java.util.List<ProducedType> typeArgumentModels = getTypeArguments();
        int argIndex = 0;
        for(Parameter parameter : method.getParameterLists().get(0).getParameters()) {
            ProducedType exprType = gen().expressionGen().getTypeForParameter(parameter, isRaw, typeArgumentModels);
            Parameter declaredParameter = method.getParameterLists().get(0).getParameters().get(argIndex);
            
            JCExpression result = gen().makeQuotedIdent(parameter.getName());
            
            result = gen().expressionGen().applyErasureAndBoxing(
                    result, 
                    exprType, 
                    !parameter.getUnboxed(), 
                    BoxingStrategy.BOXED,// Callables always have boxed params 
                    declaredParameter.getType());
            this.args.append(result);
            argIndex++;
        }
    }
    @Override
    protected JCExpression makeInvocation() {
        gen().at(node);
        JCExpression result = gen().make().Apply(transformedTypArguments, gen().makeQuotedQualIdent(callable, "$call"), args.toList());
        result = gen().expressionGen().applyErasureAndBoxing(result, returnType, 
                !unboxed, boxingStrategy, returnType);
        return result;
    }
}

class NamedArgumentInvocationBuilder extends InvocationBuilder {
    
    private final NamedArgumentList namedArgumentList;
    
    public NamedArgumentInvocationBuilder(
            AbstractTransformer gen, Primary primary,
            Declaration primaryDeclaration,
            Tree.InvocationExpression invocation) {
        super(gen, primary, primaryDeclaration, invocation.getTypeModel(), invocation);
        namedArgumentList = invocation.getNamedArgumentList();
    }
    protected boolean containsParameter(java.util.List<Tree.NamedArgument> namedArguments, Parameter param) {
        for (Tree.NamedArgument namedArg : namedArguments) {
            Parameter declaredParam = namedArg.getParameter();
            if (param == declaredParam) {
                return true;
            }
        }
        return false;
    }
    @Override
    protected void compute() {
        java.util.List<ProducedType> typeArgumentModels = getTypeArguments();
        boolean isRaw = transformedTypArguments.isEmpty();
        
        if (getPrimaryDeclaration() != null) {
            java.util.List<ParameterList> paramLists = ((Functional)getPrimaryDeclaration()).getParameterLists();
            java.util.List<Tree.NamedArgument> namedArguments = namedArgumentList.getNamedArguments();
            java.util.List<Parameter> declaredParams = paramLists.get(0).getParameters();
            Parameter lastDeclared = declaredParams.size() > 0 ? declaredParams.get(declaredParams.size() - 1) : null;
            boolean boundSequenced = false;
            String varBaseName = gen().aliasName("arg");
            callVarName = varBaseName + "$callable$";
            
            int numDeclared = declaredParams.size();
            int numDeclaredFixed = (lastDeclared != null && lastDeclared.isSequenced()) ? numDeclared - 1 : numDeclared;
            int numPassed = namedArguments.size();
            int idx = 0;
            for (Tree.NamedArgument namedArg : namedArguments) {
                gen().at(namedArg);
                Tree.Expression expr = ((Tree.SpecifiedArgument)namedArg).getSpecifierExpression().getExpression();
                Parameter declaredParam = namedArg.getParameter();
                int index;
                BoxingStrategy boxType;
                ProducedType type;
                if (declaredParam != null) {
                    if (declaredParam.isSequenced()) {
                        boundSequenced = true;
                    }
                    index = declaredParams.indexOf(declaredParam);
                    boxType = Util.getBoxingStrategy(declaredParam);
                    type = gen().getTypeForParameter(declaredParam, isRaw, typeArgumentModels);
                } else {
                    // Arguments of overloaded methods don't have a reference to parameter
                    index = idx++;
                    boxType = BoxingStrategy.UNBOXED;
                    type = expr.getTypeModel();
                }
                String varName = varBaseName + "$" + index;
                // if we can't pick up on the type from the declaration, revert to the type of the expression
                if(gen().isTypeParameter(gen().simplifyType(type)))
                    type = expr.getTypeModel();
                
                JCExpression typeExpr = gen().makeJavaType(type, (boxType == BoxingStrategy.BOXED) ? AbstractTransformer.TYPE_ARGUMENT : 0);
                JCExpression argExpr = gen().expressionGen().transformExpression(expr, boxType, type);
                JCVariableDecl varDecl = gen().makeVar(varName, typeExpr, argExpr);
                vars.append(varDecl);
            }
            
            if (!Decl.isOverloaded(getPrimaryDeclaration()) && numPassed < numDeclaredFixed) {
                boolean needsThis = false;
                if (Decl.withinClassOrInterface(getPrimaryDeclaration())) {
                    // first append $this
                    ProducedType thisType = gen().getThisType(getPrimaryDeclaration());
                    vars.append(gen().makeVar(varBaseName + "$this$", gen().makeJavaType(thisType, AbstractTransformer.NO_PRIMITIVES), gen().makeUnquotedIdent(callVarName)));
                    needsThis = true;
                }
                // append any arguments for defaulted parameters
                for (int ii = 0; ii < numDeclaredFixed; ii++) {
                    Parameter param = declaredParams.get(ii);
                    if (containsParameter(namedArguments, param)) {
                        continue;
                    }
                    String varName = varBaseName + "$" + ii;
                    String methodName = Util.getDefaultedParamMethodName(getPrimaryDeclaration(), param);
                    List<JCExpression> arglist = makeThisVarRefArgumentList(varBaseName, ii, needsThis);
                    JCExpression argExpr;
                    if (!param.isSequenced()) {
                        Declaration container = param.getDeclaration().getRefinedDeclaration();
                        if (!container.isToplevel()) {
                            container = (Declaration)container.getContainer();
                        }
                        String className = Util.getCompanionClassName(container.getName());
                        argExpr = gen().at(node).Apply(null, gen().makeQuotedQualIdent(gen().makeQuotedFQIdent(container.getQualifiedNameString()), className, methodName), arglist);
                    } else {
                        argExpr = gen().makeEmpty();
                    }
                    BoxingStrategy boxType = Util.getBoxingStrategy(param);
                    ProducedType type = gen().getTypeForParameter(param, isRaw, typeArgumentModels);
                    JCExpression typeExpr = gen().makeJavaType(type, (boxType == BoxingStrategy.BOXED) ? AbstractTransformer.TYPE_ARGUMENT : 0);
                    JCVariableDecl varDecl = gen().makeVar(varName, typeExpr, argExpr);
                    vars.append(varDecl);
                }
            }
            
            Tree.SequencedArgument sequencedArgument = namedArgumentList.getSequencedArgument();
            if (sequencedArgument != null) {
                gen().at(sequencedArgument);
                String varName = varBaseName + "$" + numDeclaredFixed;
                JCExpression typeExpr = gen().makeJavaType(lastDeclared.getType(), AbstractTransformer.WANT_RAW_TYPE);
                JCExpression argExpr = gen().makeSequenceRaw(sequencedArgument.getExpressionList().getExpressions());
                JCVariableDecl varDecl = gen().makeVar(varName, typeExpr, argExpr);
                vars.append(varDecl);
            } else if (lastDeclared != null 
                    && lastDeclared.isSequenced() 
                    && !boundSequenced) {
                String varName = varBaseName + "$" + numDeclaredFixed;
                JCExpression typeExpr = gen().makeJavaType(lastDeclared.getType(), AbstractTransformer.WANT_RAW_TYPE);
                JCVariableDecl varDecl = gen().makeVar(varName, typeExpr, gen().makeEmpty());
                vars.append(varDecl);
            }
            
            if (!Decl.isOverloaded(getPrimaryDeclaration())) {
                args.appendList(makeVarRefArgumentList(varBaseName, numDeclared));
            } else {
                // For overloaded methods (and therefore Java interop) we just pass the arguments we have
                args.appendList(makeVarRefArgumentList(varBaseName, numPassed));
            }
        }
    }
}