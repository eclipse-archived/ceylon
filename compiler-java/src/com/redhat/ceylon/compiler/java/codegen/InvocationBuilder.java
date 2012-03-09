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
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Expression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.MemberOrTypeExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SpecifierExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.StaticMemberOrTypeExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Term;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCTypeCast;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;


abstract class InvocationBuilder {

    private final AbstractTransformer gen;
    protected final Node node;    
    protected final Tree.Primary primary;
    protected final Declaration primaryDeclaration;
    protected final ProducedType returnType;
    protected boolean unboxed;
    protected BoxingStrategy boxingStrategy;
    
    protected final ListBuffer<JCVariableDecl> vars = ListBuffer.lb();
    protected final ListBuffer<JCExpression> args = ListBuffer.lb();
    protected final List<JCExpression> typeArgs;
    protected String callVarName;
    
    protected InvocationBuilder(AbstractTransformer gen, 
            Tree.Primary primary, Declaration primaryDeclaration,
            ProducedType returnType, /*boolean isBoxed,*/ Node node) {
        this.gen = gen;
        this.primary = primary;
        this.primaryDeclaration = primaryDeclaration;
        this.returnType = returnType;
        //this.isBoxed = isBoxed;
        this.node = node;
        typeArgs = transformTypeArguments(getTypeArguments());
    }
    
    protected abstract void compute();
    
    protected AbstractTransformer gen() {
        return gen;
    }
    
    protected Declaration getPrimaryDeclaration() {
        return primaryDeclaration;
    }

    public boolean isUnboxed() {
        return unboxed;
    }

    public void setUnboxed(boolean unboxed) {
        this.unboxed = unboxed;
    }

    public BoxingStrategy getBoxingStrategy() {
        return boxingStrategy;
    }

    public void setBoxingStrategy(BoxingStrategy boxingStrategy) {
        this.boxingStrategy = boxingStrategy;
    }

    protected java.util.List<ProducedType> getTypeArguments() {
        if (primary instanceof Tree.StaticMemberOrTypeExpression){
            return ((Tree.StaticMemberOrTypeExpression)primary).getTypeArguments().getTypeModels();
        }
        return null;
    }

    protected List<JCExpression> transformTypeArguments(java.util.List<ProducedType> typeArguments) {
        List<JCExpression> result = List.<JCExpression> nil();
        if(typeArguments != null){
            for (ProducedType arg : typeArguments) {
                // cancel type parameters and go raw if we can't specify them
                if(gen().willEraseToObject(arg)
                        || gen().isTypeParameter(arg))
                    return List.nil();
                result = result.append(gen().makeJavaType(arg, AbstractTransformer.TYPE_ARGUMENT));
            }
        }
        return result;
    }
    
    // Make a list of $arg0, $arg1, ... , $argN
    protected List<JCExpression> makeVarRefArgumentList(String varBaseName, int argCount) {
        List<JCExpression> names = List.<JCExpression> nil();
        for (int i = 0; i < argCount; i++) {
            names = names.append(gen().makeUnquotedIdent(varBaseName + "$" + i));
        }
        return names;
    }

    // Make a list of $arg$this$, $arg0, $arg1, ... , $argN
    protected List<JCExpression> makeThisVarRefArgumentList(String varBaseName, int argCount, boolean needsThis) {
        List<JCExpression> names = List.<JCExpression> nil();
        if (needsThis) {
            names = names.append(gen().makeUnquotedIdent(varBaseName + "$this$"));
        }
        names = names.appendList(makeVarRefArgumentList(varBaseName, argCount));
        return names;
    }
    
    protected JCExpression transformInvocation(JCExpression primaryExpr, String selector) {
        JCExpression actualPrimExpr = null;
        if (primary instanceof Tree.QualifiedTypeExpression
                && ((Tree.QualifiedTypeExpression)primary).getPrimary() instanceof Tree.BaseTypeExpression) {
            actualPrimExpr = gen().makeSelect(primaryExpr, "this");
        } else {
            actualPrimExpr = primaryExpr;
        }
        if (vars != null && !vars.isEmpty() 
                && primaryExpr != null 
                && selector != null) {
            // Prepare the first argument holding the primary for the call
            JCExpression callVarExpr = gen().makeUnquotedIdent(callVarName);
            ProducedType type = ((Tree.QualifiedMemberOrTypeExpression)primary).getTarget().getQualifyingType();
            JCVariableDecl callVar = gen().makeVar(callVarName, gen().makeJavaType(type, AbstractTransformer.NO_PRIMITIVES), actualPrimExpr);
            vars.prepend(callVar);
            actualPrimExpr = callVarExpr;
        }
        
        JCExpression resultExpr;
        if (primary instanceof Tree.BaseTypeExpression) {
            ProducedType classType = (ProducedType)((Tree.BaseTypeExpression)primary).getTarget();
            resultExpr = gen().make().NewClass(null, null, gen().makeJavaType(classType, AbstractTransformer.CLASS_NEW), args.toList(), null);
        } else if (primary instanceof Tree.QualifiedTypeExpression) {
            resultExpr = gen().make().NewClass(actualPrimExpr, null, gen().makeQuotedIdent(selector), args.toList(), null);
        } else {
            Declaration decl = ((Tree.StaticMemberOrTypeExpression)primary).getDeclaration();
            if (decl instanceof FunctionalParameter) {
                if (primaryExpr != null) {
                    actualPrimExpr = gen().makeQualIdent(primaryExpr, decl.getName());
                } else {
                    actualPrimExpr = gen().makeQuotedIdent(decl.getName());
                }
                selector = "call";
            }
            resultExpr = gen().make().Apply(typeArgs, gen().makeQuotedQualIdent(actualPrimExpr, selector), args.toList());
        }

        if (vars != null && !vars.isEmpty()) {
            if (gen().isVoid(returnType)) {
                // void methods get wrapped like (let $arg$1=expr, $arg$0=expr in call($arg$0, $arg$1); null)
                return gen().make().LetExpr(vars.toList(), List.<JCStatement>of(gen().make().Exec(resultExpr)), gen().makeNull());
            } else {
                // all other methods like (let $arg$1=expr, $arg$0=expr in call($arg$0, $arg$1))
                return gen().make().LetExpr(vars.toList(), resultExpr);
            }
        } else {
            return resultExpr;
        }
    }
    
    protected JCExpression makeInvocation() {
        gen().at(node);
        JCExpression result = gen().expressionGen().transformPrimary(primary, new TermTransformer() {
            @Override
            public JCExpression transform(JCExpression primaryExpr, String selector) {
                return transformInvocation(primaryExpr, selector);
            }
        });
        return result;
    }

    public JCExpression build() {
        boolean prevFnCall = gen().expressionGen().isWithinInvocation();
        gen().expressionGen().setWithinInvocation(true);
        try {
            JCExpression invocation = makeInvocation();
            invocation = gen().boxUnboxIfNecessary(invocation, !unboxed, 
                    returnType, boxingStrategy);
            return invocation;
        } finally {
            gen().expressionGen().setWithinInvocation(prevFnCall);
        }
    }
    public static InvocationBuilder invocation(AbstractTransformer gen, 
            final Tree.InvocationExpression invocation) {
        
        Tree.Primary primary = invocation.getPrimary();
        Declaration primaryDeclaration = ((Tree.MemberOrTypeExpression)primary).getDeclaration();
        InvocationBuilder builder;
        if (invocation.getPositionalArgumentList() != null) {
            final Tree.PositionalArgumentList positional = invocation.getPositionalArgumentList();
            java.util.List<ParameterList> paramLists = ((Functional)primaryDeclaration).getParameterLists();
            builder = new PositionalInvocationBuilder(gen, 
                    primary, primaryDeclaration,
                    invocation.getTypeModel(),
                    invocation,
                    paramLists.get(0)) {
                
                @Override
                protected Expression getExpression(int argIndex) {
                    return positional.getPositionalArguments().get(argIndex).getExpression();
                }
                @Override
                protected JCExpression getTransformedExpression(int argIndex, boolean isRaw, java.util.List<ProducedType> typeArgumentModels) {
                    return gen().expressionGen().transformArg(
                            getExpression(argIndex), 
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
                
            };
        } else if (invocation.getNamedArgumentList() != null) {
            builder = new InvocationBuilder(gen, 
                    primary, 
                    primaryDeclaration, 
                    invocation.getTypeModel(),
                    invocation) {
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
                    boolean isRaw = typeArgs.isEmpty();
                    
                    if (getPrimaryDeclaration() != null) {
                        java.util.List<ParameterList> paramLists = ((Functional)getPrimaryDeclaration()).getParameterLists();
                        java.util.List<Tree.NamedArgument> namedArguments = invocation.getNamedArgumentList().getNamedArguments();
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
                            if(gen().isTypeParameter(type))
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
                        
                        Tree.SequencedArgument sequencedArgument = invocation.getNamedArgumentList().getSequencedArgument();
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
            };
        } else {
            throw new RuntimeException("Illegal State");
        }
        builder.setBoxingStrategy(BoxingStrategy.INDIFFERENT);
        builder.setUnboxed(invocation.getUnboxed());
        builder.compute();
        return builder;
    }
    
    public static InvocationBuilder invocationForCallable(
            AbstractTransformer gen, Term expr, ParameterList parameterList) {
        final Tree.MemberOrTypeExpression primary;
        if (expr instanceof Tree.MemberOrTypeExpression) {
            primary = (Tree.MemberOrTypeExpression)expr;
        } else {
            throw new RuntimeException(expr+"");
        }
        
        final java.util.List<Parameter> declaredParameters = ((Functional)primary.getDeclaration()).getParameterLists().get(0).getParameters();

        TypeDeclaration primaryDeclaration = expr.getTypeModel().getDeclaration();
        
        final java.util.List<Parameter> functionalParameters = parameterList.getParameters();
        
        
        PositionalInvocationBuilder builder = new PositionalInvocationBuilder(
                gen,
                primary,
                primaryDeclaration,
                gen.getCallableReturnType(expr.getTypeModel()),
                expr,
                parameterList) {
            
            @Override
            protected Expression getExpression(int argIndex) {
                throw new RuntimeException("Defaulted parameters are not defaulted when called via a method reference");
            }
            @Override
            protected Parameter getParameter(int argIndex) {
                return parameterList.getParameters().get(argIndex);
            }
            @Override
            protected int getNumArguments() {
                return parameterList.getParameters().size();
            }
            @Override
            protected boolean dontBoxSequence() {
                return parameterList.getParameters().get(getNumArguments() - 1).isSequenced();
            }
            @Override
            protected JCExpression getTransformedExpression(int argIndex,
                    boolean isRaw,
                    java.util.List<ProducedType> typeArgumentModels) {
                Parameter param = declaredParameters.get(argIndex);
                /*
                JCExpression argExpr;
                if (functionalParameters.size() <= 3) {
                    // The Callable has overridden one of the non-varargs call() 
                    // methods
                    argExpr = gen().make().Ident(
                            gen().names().fromString("arg"+argIndex));
                } else {
                    // The Callable has overridden the varargs call() method
                    // so we need to index into the varargs array
                    argExpr = gen().make().Indexed(
                            gen().make().Ident(gen().names().fromString("arg0")), 
                            gen().make().Literal(argIndex));
                }
                ProducedType castType = gen().getTypeForParameter(param, isRaw, getTypeArguments());
                JCTypeCast cast = gen().make().TypeCast(gen().makeJavaType(castType, AbstractTransformer.NO_PRIMITIVES), argExpr);
                // TODO Should this be applyErasureAndBoxing()?
                JCExpression boxed = gen().boxUnboxIfNecessary(cast, true, 
                        param.getType(), param.getUnboxed() ? BoxingStrategy.UNBOXED : BoxingStrategy.BOXED);
                */
                return CallableBuilder.unpickCallableParameter(gen(), isRaw, typeArgumentModels, param, argIndex, functionalParameters.size());
            }
        };
        builder.setUnboxed(expr.getUnboxed());
        builder.setBoxingStrategy(BoxingStrategy.BOXED);// Must be boxed because non-primitive return type
        builder.compute();
        return builder;
    }
    
    public static InvocationBuilder invocationForSpecifier(
            CeylonTransformer gen, SpecifierExpression specifierExpression,
            final Method method) {
        
        final Term term = specifierExpression.getExpression().getTerm();
        final ProducedType returnType = method.getType();
        Tree.Primary primary = (Tree.Primary)term;
        InvocationBuilder builder;
        if (term instanceof MemberOrTypeExpression
                && ((MemberOrTypeExpression)primary).getDeclaration() instanceof Functional) {
            Declaration primaryDeclaration = ((MemberOrTypeExpression)primary).getDeclaration();
            builder = new PositionalInvocationBuilder(
                    gen, 
                    primary, 
                    primaryDeclaration, 
                    returnType, 
                    specifierExpression, 
                    method.getParameterLists().get(0)) {
                @Override
                protected int getNumArguments() {
                    return parameterList.getParameters().size();
                }
                
                @Override
                protected Expression getExpression(int argIndex) {
                    // TODO Auto-generated method stub
                    return null;
                }
                
                @Override
                protected JCExpression getTransformedExpression(int argIndex,
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
                    return parameterList.getParameters().get(argIndex);
                }
                
                @Override
                protected boolean dontBoxSequence() {
                    return method.getParameterLists().get(0).getParameters().get(getNumArguments() - 1).isSequenced();
                }
            };
            builder.setUnboxed(primary.getUnboxed());
            builder.setBoxingStrategy(method.getUnboxed() ? BoxingStrategy.UNBOXED : BoxingStrategy.BOXED);
        } else if (gen.isCeylonCallable(term.getTypeModel())) {
            final JCExpression callable = gen.expressionGen().transformExpression(term);//gen.expressionGen().transformFunctional(term, method);
            
            builder = new InvocationBuilder(gen, null, null, returnType, specifierExpression) {
                @Override
                protected void compute() {
                    for(Parameter parameter : method.getParameterLists().get(0).getParameters()) {
                        this.args.append(gen().makeQuotedIdent(parameter.getName()));
                    }
                }
                @Override
                protected JCExpression makeInvocation() {
                    gen().at(node);
                    JCExpression result;
                    //result = transformInvocation(callable, "call");
                    result = gen().make().Apply(typeArgs, gen().makeQuotedQualIdent(callable, "call"), args.toList());
                    return result;
                }
            };
            // Because we're calling a callable, and they always return a 
            // boxed result
            builder.setUnboxed(false);
            builder.setBoxingStrategy(method.getUnboxed() ? BoxingStrategy.UNBOXED : BoxingStrategy.BOXED);
        } else {
            throw new RuntimeException();
        }
        
        builder.compute();
        return builder;
    }
}

abstract class PositionalInvocationBuilder extends InvocationBuilder {

    protected final ParameterList parameterList;
    
    protected PositionalInvocationBuilder(AbstractTransformer gen,
            Tree.Primary primary,
            Declaration primaryDeclaration,
            ProducedType returnType, 
            Node node,
            ParameterList parameterList) {
        super(gen, primary, primaryDeclaration, returnType, node);
        this.parameterList = parameterList;
    }
 
    /** Gets the number of arguments */
    protected abstract int getNumArguments();
    /** 
     * Gets the expression supplying the argument value for the 
     * given argument index 
     */
    protected abstract Expression getExpression(int argIndex);
    /**
     * Gets the transformed expression supplying the argument value for the 
     * given argument index
     */
    protected abstract JCExpression getTransformedExpression(int argIndex, boolean isRaw, java.util.List<ProducedType> typeArgumentModels);
    /**
     * Gets the parameter for the given argument index
     */
    protected abstract Parameter getParameter(int argIndex);
    protected abstract boolean dontBoxSequence();
    
    @Override
    protected void compute() {
        
        final boolean isRaw = typeArgs.isEmpty();
        java.util.List<Parameter> declaredParams = parameterList.getParameters();
        int numParameters = declaredParams.size();
        int numArguments = this.getNumArguments();
        Parameter lastDeclaredParam = declaredParams.isEmpty() ? null : declaredParams.get(declaredParams.size() - 1); 
        if (lastDeclaredParam != null 
                && lastDeclaredParam.isSequenced()
                && !this.dontBoxSequence() // foo(sequence...) syntax => no need to box
                && numArguments >= (numParameters -1)) {
            // => call to a varargs method
            // first, append the normal args
            for (int ii = 0; ii < numParameters - 1; ii++) {
                args.append(this.getTransformedExpression(ii, isRaw, getTypeArguments()));
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
                    x = x.append(this.getTransformedExpression(ii, isRaw, getTypeArguments()));
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
                gen().at(this.getExpression(ii));
                Tree.Expression expr = this.getExpression(ii);
                Parameter declaredParam = this.getParameter(ii);
                int index;
                BoxingStrategy boxType;
                ProducedType type;
                if (declaredParam != null) {
                    index = declaredParams.indexOf(declaredParam);
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
                    Parameter param = declaredParams.get(ii);
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
                args.append(this.getTransformedExpression(ii, 
                        isRaw, getTypeArguments()));
            }
        }
    }
}