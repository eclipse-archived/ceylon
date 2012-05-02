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

import static com.sun.tools.javac.code.Flags.FINAL;

import java.util.Map;

import com.redhat.ceylon.compiler.java.codegen.AbstractTransformer.BoxingStrategy;
import com.redhat.ceylon.compiler.java.codegen.ExpressionTransformer.TermTransformer;
import com.redhat.ceylon.compiler.java.util.Decl;
import com.redhat.ceylon.compiler.java.util.Strategy;
import com.redhat.ceylon.compiler.java.util.Util;
import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Functional;
import com.redhat.ceylon.compiler.typechecker.model.FunctionalParameter;
import com.redhat.ceylon.compiler.typechecker.model.Getter;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ParameterList;
import com.redhat.ceylon.compiler.typechecker.model.ProducedReference;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.FunctionArgument;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.LocalModifier;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.PositionalArgument;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCNewClass;
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
    protected final List<JCExpression> primaryTypeArguments;
    protected boolean unboxed;
    protected BoxingStrategy boxingStrategy;
    
    protected final ListBuffer<JCStatement> vars = ListBuffer.lb();
    protected final ListBuffer<JCExpression> args = ListBuffer.lb();
    protected final Map<TypeParameter, ProducedType> typeArguments;
    protected String callVarName;
    protected String varBaseName;
    protected final boolean needsThis;
    protected boolean hasDefaultedArguments = false;
    
    protected InvocationBuilder(AbstractTransformer gen, 
            Tree.Primary primary, Declaration primaryDeclaration,
            ProducedType returnType, Node node) {
        this.gen = gen;
        this.primary = primary;
        this.primaryDeclaration = primaryDeclaration;
        this.returnType = returnType;
        this.node = node;
        if (primary instanceof Tree.MemberOrTypeExpression){
            this.typeArguments = ((Tree.MemberOrTypeExpression) primary).getTarget().getTypeArguments();
        } else {
            this.typeArguments = null;
        }
        if (primary instanceof Tree.StaticMemberOrTypeExpression){
            this.primaryTypeArguments = transformTypeArguments(gen, ((Tree.StaticMemberOrTypeExpression)primary).getTypeArguments().getTypeModels());
        } else {
            this.primaryTypeArguments = transformTypeArguments(gen, null);
        }
        
        needsThis = !Strategy.defaultParameterMethodStatic(primaryDeclaration);
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
    
    /**
     * Populates {@link #vars}, {@link #args} and {@link #callVarName}
     * ready for a call to {@link #build()}
     */
    protected abstract void compute();
    
    protected final JCExpression makeDefaultedArgument(int argIndex, Parameter param) {
        hasDefaultedArguments = true;
        
        String methodName = Util.getDefaultedParamMethodName(getPrimaryDeclaration(), param);
        JCExpression defaultValueMethodName;
        if (Strategy.defaultParameterMethodOnSelf(param)) {
            Declaration container = param.getDeclaration().getRefinedDeclaration();
            if (!container.isToplevel()) {
                container = (Declaration)container.getContainer();
            }
            String className = gen().getCompanionClassName(container);
            defaultValueMethodName = gen().makeQuotedQualIdent(gen().makeQuotedFQIdent(container.getQualifiedNameString()), className, methodName);
        } else if (Strategy.defaultParameterMethodStatic(param)) {
            Declaration container = param.getDeclaration().getRefinedDeclaration();
            if (!container.isToplevel()) {
                container = (Declaration)container.getContainer();
            }            
            defaultValueMethodName = gen().makeQuotedQualIdent(gen().makeQuotedFQIdent(container.getQualifiedNameString()), methodName);
        } else {
            defaultValueMethodName = gen.makeQuotedQualIdent(gen().makeQuotedIdent(varBaseName + "$this$"), methodName);
        }
        
        List<JCExpression> arglist;
        if (Strategy.defaultParameterMethodTakesThis(param)) {
            arglist = makeThisVarRefArgumentList(argIndex);
        } else {
            arglist = makeVarRefArgumentList(argIndex);
        }
        
        JCExpression argExpr = gen().at(node).Apply(null, defaultValueMethodName, arglist);
        return argExpr;
    }
    
    protected final void appendDefaulted(ProducedType type, int argIndex,
            JCExpression argExpr, int flags) {
        String varName = varBaseName + "$" + argIndex;
        JCExpression typeExpr = gen().makeJavaType(type, flags);
        JCVariableDecl varDecl = gen().makeVar(varName, typeExpr, argExpr);
        vars.append(varDecl);
    }
    
    protected final void appendDefaulted(Parameter param, int argIndex, boolean isRaw, JCExpression argExpr) {
        int flags = (Util.getBoxingStrategy(param) == BoxingStrategy.BOXED) ? AbstractTransformer.TYPE_ARGUMENT : 0;
        ProducedType type = gen().getTypeForParameter(param, isRaw, getTypeArguments());
        appendDefaulted(type, argIndex, argExpr, flags);
    }
    
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

    protected final Map<TypeParameter, ProducedType> getTypeArguments() {
        return this.typeArguments;
    }
    
    // Make a list of $arg0, $arg1, ... , $argN
    protected final List<JCExpression> makeVarRefArgumentList(int argCount) {
        List<JCExpression> names = List.<JCExpression> nil();
        for (int i = 0; i < argCount; i++) {
            names = names.append(gen().makeUnquotedIdent(varBaseName + "$" + i));
        }
        return names;
    }
    
    // Make a list of $arg$this$, $arg0, $arg1, ... , $argN
    protected final List<JCExpression> makeThisVarRefArgumentList(int argCount) {
        List<JCExpression> names = List.<JCExpression> nil();
        if (needsThis) {
            names = names.append(gen().makeUnquotedIdent(varBaseName + "$this$"));
        }
        names = names.appendList(makeVarRefArgumentList(argCount));
        return names;
    }

    protected final JCVariableDecl makeThis() {
        // first append $this
        JCExpression defaultedParameterInstance;
        // TODO Fix how we figure out the thisType, because it's doesn't 
        // handle type parameters correctly
        // we used to use thisType = gen().getThisType(getPrimaryDeclaration());
        final JCExpression thisType;
        ProducedReference target = ((Tree.MemberOrTypeExpression)primary).getTarget();
        if (primary instanceof Tree.BaseMemberExpression) {
            thisType = gen().makeJavaType(target.getQualifyingType(), AbstractTransformer.NO_PRIMITIVES);
            defaultedParameterInstance = gen().makeUnquotedIdent("this");
        } else if (primary instanceof Tree.BaseTypeExpression
                || primary instanceof Tree.QualifiedTypeExpression) {
            Map<TypeParameter, ProducedType> typeA = target.getTypeArguments();
            ListBuffer<JCExpression> typeArgs = ListBuffer.<JCExpression>lb();
            for (TypeParameter tp : ((TypeDeclaration)target.getDeclaration()).getTypeParameters()) {
                ProducedType producedType = typeA.get(tp);
                typeArgs.append(gen().makeJavaType(producedType, gen().TYPE_ARGUMENT));
            }
            ClassOrInterface declaration = (ClassOrInterface)((Tree.BaseTypeExpression) primary).getDeclaration();
            thisType = gen().makeCompanionType(declaration, typeArgs.toList());
            defaultedParameterInstance = gen().make().NewClass(
                    null, 
                    null,
                    gen().makeCompanionType(declaration, typeArgs.toList()), 
                    List.<JCExpression>nil(), null);
        } else {
            thisType = gen().makeJavaType(target.getQualifyingType(), AbstractTransformer.NO_PRIMITIVES);
            defaultedParameterInstance = gen().makeUnquotedIdent(callVarName);
        }
        
        JCVariableDecl thisDecl = gen().makeVar(varBaseName + "$this$", 
                thisType, 
                defaultedParameterInstance);
        return thisDecl;
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
            resultExpr = gen().make().Apply(primaryTypeArguments, gen().makeQuotedQualIdent(actualPrimExpr, selector), args.toList());
        }

        resultExpr = applyDefaultParameters(resultExpr);
        
        return resultExpr;
    }

    protected final JCExpression applyDefaultParameters(JCExpression resultExpr) {
        if (vars != null && !vars.isEmpty()) {
            if (returnType == null || gen().isVoid(returnType)) {
                // void methods get wrapped like (let $arg$1=expr, $arg$0=expr in call($arg$0, $arg$1); null)
                resultExpr = gen().make().LetExpr( 
                        vars.append(gen().make().Exec(resultExpr)).toList(), 
                        gen().makeNull());
            } else {
                // all other methods like (let $arg$1=expr, $arg$0=expr in call($arg$0, $arg$1))
                resultExpr = gen().make().LetExpr( 
                        vars.toList(),
                        resultExpr);
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
        if (hasDefaultedArguments && needsThis) {
            vars.prepend(makeThis());
        }
        if (vars != null 
                && !vars.isEmpty() 
                && primaryExpr != null
                && selector != null) {
            // Prepare the first argument holding the primary for the call
            ProducedType type = ((Tree.MemberOrTypeExpression)primary).getTarget().getQualifyingType();
            vars.prepend(gen().makeVar(callVarName, gen().makeJavaType(type, AbstractTransformer.NO_PRIMITIVES), actualPrimExpr));
            actualPrimExpr = gen().makeUnquotedIdent(callVarName);
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
            builder = new MethodReferenceSpecifierInvocationBuilder(
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

/**
 * An abstract implementation of InvocationBuilder support invocation 
 * via positional arguments. Supports with sequenced arguments but not 
 * defaulted arguments.
 */
abstract class SimpleInvocationBuilder extends InvocationBuilder {

    protected SimpleInvocationBuilder(
            AbstractTransformer gen,
            Tree.Primary primary,
            Declaration primaryDeclaration,
            ProducedType returnType, 
            Node node) {
        super(gen, primary, primaryDeclaration, returnType, node);
    }
 
    protected final JCExpression transformArg(Tree.Term expr, Parameter parameter, boolean isRaw, java.util.Map<TypeParameter, ProducedType> typeArgumentModels) {
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
    
    /** Gets the number of arguments actually being supplied */
    protected abstract int getNumArguments();

    /**
     * Gets the transformed expression supplying the argument value for the 
     * given argument index
     */
    protected abstract JCExpression getTransformedArgumentExpression(int argIndex, boolean isRaw, java.util.Map<TypeParameter, ProducedType> typeArgumentModels);
    
    protected abstract boolean dontBoxSequence();
    
    @Override
    protected void compute() {
        final boolean isRaw = primaryTypeArguments.isEmpty();
        int numParameters = getDeclaredParameters().size();
        int numArguments = getNumArguments();
        // => call to a varargs method
        // first, append the normal args
        if (numArguments < numParameters) {
            // Defaulted parameters
            for (int ii = 0; ii < numArguments; ii++) {
                args.append(this.getTransformedArgumentExpression(ii, isRaw, getTypeArguments()));
            }
            return;
        }
        for (int ii = 0; ii < numParameters - 1; ii++) {
            args.append(this.getTransformedArgumentExpression(ii, isRaw, getTypeArguments()));
        }
        
        Parameter lastDeclaredParam = getDeclaredParameters().isEmpty() ? null : getDeclaredParameters().get(getDeclaredParameters().size() - 1);
        
        if (lastDeclaredParam != null) {
            JCExpression boxed;
            if (!lastDeclaredParam.isSequenced()
                    || dontBoxSequence()) {
                // foo(sequence...) syntax => no need to box
                boxed = this.getTransformedArgumentExpression(numArguments-1, isRaw, getTypeArguments());
            } else if (numParameters -1 == numArguments) {
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
        }
    }
}

/**
 * InvocationBuilder used for 'normal' method and initializer invocations via 
 * positional arguments. Supports sequenced and defaulted arguments.
 */
class PositionalInvocationBuilder extends SimpleInvocationBuilder {
    
    private final Tree.PositionalArgumentList positional;
    private final java.util.List<Parameter> declaredParameters;
    private final java.util.List<Parameter> parameters;
    
    public PositionalInvocationBuilder(
            AbstractTransformer gen, 
            Tree.Primary primary,
            Declaration primaryDeclaration,
            Tree.InvocationExpression invocation,
            ParameterList parameterList) {
        super(gen, primary, primaryDeclaration, invocation.getTypeModel(), invocation);
        positional = invocation.getPositionalArgumentList();
        parameters = ((Functional)primaryDeclaration).getParameterLists().get(0).getParameters();    
        this.declaredParameters = parameterList.getParameters();
    }
    @Override
    protected java.util.List<Parameter> getDeclaredParameters() {
        return declaredParameters;
    }
    protected Tree.Expression getArgumentExpression(int argIndex) {
        return positional.getPositionalArguments().get(argIndex).getExpression();
    }
    @Override
    protected JCExpression getTransformedArgumentExpression(int argIndex, boolean isRaw, java.util.Map<TypeParameter, ProducedType> typeArgumentModels) {
        PositionalArgument arg = positional.getPositionalArguments().get(argIndex);
        if (arg.getExpression().getTerm() instanceof FunctionArgument) {
            FunctionArgument farg = (FunctionArgument)arg.getExpression().getTerm();
            Method model = farg.getDeclarationModel();
            ProducedType callableType = gen().typeFact().getCallableType(model.getType());
            // TODO MPL
            CallableBuilder callableBuilder = CallableBuilder.anonymous(
                    gen().gen(),
                    farg.getExpression(),
                    model.getParameterLists().get(0),
                    callableType);
            return callableBuilder.build();
        }
        return transformArg(
                getArgumentExpression(argIndex), 
                getParameter(argIndex), isRaw, typeArgumentModels);
    }
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
    protected boolean hasDefaultArgument(int ii) {
        return parameters.get(ii).isDefaulted();
    }
}

/**
 * InvocationBuilder used for constructing invocations of {@code super()}
 * when creating constructors.
 */
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


/**
 * InvocationBuilder for constructing the invocation of a method reference 
 * used when implementing {@code Callable.call()}
 */
class CallableInvocationBuilder extends SimpleInvocationBuilder {
    
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
            java.util.Map<TypeParameter, ProducedType> typeArgumentModels) {
        Parameter param = callableParameters.get(argIndex);
        ProducedType argType = primary.getTypeModel().getTypeArgumentList().get(argIndex+1);
        return CallableBuilder.unpickCallableParameter(gen(), isRaw, typeArgumentModels, param, argType, argIndex, functionalParameters.size());
    }
    @Override
    protected java.util.List<Parameter> getDeclaredParameters() {
        return functionalParameters;
    }
}

/**
 * InvocationBuilder for methods specifierd with a method reference. 
 */
class MethodReferenceSpecifierInvocationBuilder extends SimpleInvocationBuilder {
    
    private final Method method;

    public MethodReferenceSpecifierInvocationBuilder(
            AbstractTransformer gen, Tree.Primary primary,
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
    protected JCExpression getTransformedArgumentExpression(int argIndex,
            boolean isRaw,
            java.util.Map<TypeParameter, ProducedType> typeArgumentModels) {
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

    private Parameter getParameter(int argIndex) {
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

/**
 * InvocationBuilder for methods specified with a Callable 
 */
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
        boolean isRaw = primaryTypeArguments.isEmpty();
        Map<TypeParameter, ProducedType> typeArgumentModels = getTypeArguments();
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
        JCExpression result = gen().make().Apply(primaryTypeArguments, gen().makeQuotedQualIdent(callable, "$call"), args.toList());
        result = gen().expressionGen().applyErasureAndBoxing(result, returnType, 
                !unboxed, boxingStrategy, returnType);
        return result;
    }
}

/**
 * InvocationBuilder for 'normal' method and initializer invocations
 * using named arguments
 */
class NamedArgumentInvocationBuilder extends InvocationBuilder {
    
    private final Tree.NamedArgumentList namedArgumentList;
    private java.util.List<Parameter> parameters;
    
    public NamedArgumentInvocationBuilder(
            AbstractTransformer gen, Tree.Primary primary,
            Declaration primaryDeclaration,
            Tree.InvocationExpression invocation) {
        super(gen, primary, primaryDeclaration, invocation.getTypeModel(), invocation);
        parameters = ((Functional)primaryDeclaration).getParameterLists().get(0).getParameters();
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
        if (getPrimaryDeclaration() != null) {
            java.util.List<ParameterList> paramLists = ((Functional)getPrimaryDeclaration()).getParameterLists();
            java.util.List<Tree.NamedArgument> namedArguments = namedArgumentList.getNamedArguments();
            java.util.List<Parameter> declaredParams = paramLists.get(0).getParameters();
            Parameter lastDeclared = declaredParams.size() > 0 ? declaredParams.get(declaredParams.size() - 1) : null;
            varBaseName = gen().aliasName("arg");
            callVarName = varBaseName + "$callable$";
            
            int numDeclared = declaredParams.size();
            int numDeclaredFixed = (lastDeclared != null && lastDeclared.isSequenced()) ? numDeclared - 1 : numDeclared;
            int numPassed = namedArguments.size();
            
            boolean boundSequenced = appendVarsForNamedArguments(namedArguments, declaredParams);
            appendVarsForDefaulted(namedArguments,
                    declaredParams, numDeclaredFixed);
            appendVarsForSequenced(lastDeclared, boundSequenced, numDeclaredFixed);
            
            if (!Decl.isOverloaded(getPrimaryDeclaration())) {
                args.appendList(makeVarRefArgumentList(numDeclared));
            } else {
                // For overloaded methods (and therefore Java interop) we just pass the arguments we have
                args.appendList(makeVarRefArgumentList(numPassed));
            }
        }
    }
    private boolean appendVarsForNamedArguments(
            java.util.List<Tree.NamedArgument> namedArguments,
            java.util.List<Parameter> declaredParams) {
        boolean isRaw = primaryTypeArguments.isEmpty();
        boolean boundSequenced = false;
        int idx = 0;
        // Assign vars for each named argument given
        for (Tree.NamedArgument namedArg : namedArguments) {
            gen().at(namedArg);
            Parameter declaredParam = namedArg.getParameter();
            BoxingStrategy boxType;
            ProducedType type = null;
            int index;
            if (declaredParam != null) {
                boundSequenced = declaredParam.isSequenced();
                index = declaredParams.indexOf(declaredParam);
                boxType = Util.getBoxingStrategy(declaredParam);
                type = gen().getTypeForParameter(declaredParam, isRaw, getTypeArguments());
            } else {
                // Arguments of overloaded methods don't have a reference to parameter
                index = idx++;
                boxType = BoxingStrategy.UNBOXED;
            }
            String varName = varBaseName + "$" + index;
            if (namedArg instanceof Tree.SpecifiedArgument) {             
                Tree.SpecifiedArgument specifiedArg = (Tree.SpecifiedArgument)namedArg;
                Tree.Expression expr = specifiedArg.getSpecifierExpression().getExpression();
                if (declaredParam == null) {
                    type = expr.getTypeModel();
                }
                // if we can't pick up on the type from the declaration, revert to the type of the expression
                if(gen().isTypeParameter(gen().simplifyType(type)))
                    type = expr.getTypeModel();
                JCExpression typeExpr = gen().makeJavaType(type, (boxType == BoxingStrategy.BOXED) ? AbstractTransformer.TYPE_ARGUMENT : 0);
                JCExpression argExpr = gen().expressionGen().transformExpression(expr, boxType, type);
                JCVariableDecl varDecl = gen().makeVar(varName, typeExpr, argExpr);
                vars.append(varDecl);
            } else if (namedArg instanceof Tree.MethodArgument) {
                Tree.MethodArgument methodArg = (Tree.MethodArgument)namedArg;
                // TODO MPL
                Method model = methodArg.getDeclarationModel();
                ProducedType callableType = gen().typeFact().getCallableType(model.getType());
                CallableBuilder callableBuilder = CallableBuilder.methodArgument(gen().gen(), 
                        callableType, 
                        model.getParameterLists().get(0), 
                        gen().statementGen().transform(methodArg.getBlock()).getStatements());
                JCNewClass callable = callableBuilder.build();
                JCExpression typeExpr = gen().makeJavaType(callableType);
                JCVariableDecl varDecl = gen().makeVar(varName, typeExpr, callable);
                vars.append(varDecl);
            } else if (namedArg instanceof Tree.ObjectArgument) {
                Tree.ObjectArgument objectArg = (Tree.ObjectArgument)namedArg;
                List<JCTree> object = gen().classGen().transformObjectArgument(objectArg);
                // No need to worry about boxing (it cannot be a boxed type) 
                JCVariableDecl varDecl = gen().makeLocalIdentityInstance(varName, objectArg.getIdentifier().getText(), false);
                vars.appendList(toStmts(objectArg, object)).append(varDecl);
            } else if (namedArg instanceof Tree.AttributeArgument) {
                Tree.AttributeArgument attrArg = (Tree.AttributeArgument)namedArg;
                final Getter model = attrArg.getDeclarationModel();
                final String name = model.getName();
                final String alias = gen().aliasName(name);
                final List<JCTree> attrClass = gen().gen().transformAttribute(model, alias, alias, attrArg.getBlock(), null, null);
                // TODO Type params
                JCExpression initValue = gen().make().Apply(null, 
                        gen().makeSelect(alias, Util.getGetterName(name)),
                        List.<JCExpression>nil());
                // TODO Boxing
                initValue = gen().expressionGen().boxUnboxIfNecessary(initValue, 
                        true, 
                        model.getType(), 
                        BoxingStrategy.UNBOXED);
                JCTree.JCVariableDecl var = gen().make().VarDef(
                        gen().make().Modifiers(FINAL, List.<JCAnnotation>nil()), 
                        gen().names().fromString(varName), 
                        gen().makeJavaType(model.getType()), 
                        initValue);
                vars.appendList(toStmts(attrArg, attrClass)).append(var);
            } else {
                throw new RuntimeException("" + namedArg);
            }
        }
        return boundSequenced;
    }
    
    private List<JCStatement> toStmts(Tree.NamedArgument namedArg, final List<JCTree> listOfStatements) {
        final ListBuffer<JCStatement> result = ListBuffer.<JCStatement>lb();
        for (JCTree tree : listOfStatements) {
            if (tree instanceof JCStatement) {
                result.append((JCStatement)tree);
            } else {
                result.append(gen().make().Exec(gen().makeErroneous(namedArg, "Attempt to put a non-statement in a Let")));
            }
        }
        return result.toList();
    }
    
    private void appendVarsForDefaulted(
            java.util.List<Tree.NamedArgument> namedArguments,
            java.util.List<Parameter> declaredParams, 
            int numDeclaredFixed) {
        boolean isRaw = primaryTypeArguments.isEmpty();
        int numPassed = namedArguments.size();
        if (!Decl.isOverloaded(getPrimaryDeclaration()) && numPassed < numDeclaredFixed) {
            hasDefaultedArguments = true;
            // append any arguments for defaulted parameters
            for (int ii = 0; ii < numDeclaredFixed; ii++) {
                Parameter param = declaredParams.get(ii);
                if (containsParameter(namedArguments, param)) {
                    continue;
                }
                final JCExpression argExpr;
                if (!param.isSequenced()
                        || hasDefaultArgument(ii)) {
                    argExpr = makeDefaultedArgument(ii, param);
                } else {
                    argExpr = gen().makeEmpty();
                }
                appendDefaulted(param, ii, isRaw, argExpr);
            }
        }
    }
    
    private void appendVarsForSequenced(Parameter lastDeclared, boolean boundSequenced, int numDeclaredFixed) {
        JCExpression argExpr = null;
        Tree.SequencedArgument sequencedArgument = namedArgumentList.getSequencedArgument();
        if (sequencedArgument != null) {
            gen().at(sequencedArgument);
            argExpr = gen().makeSequenceRaw(sequencedArgument.getExpressionList().getExpressions());   
        } else if (lastDeclared != null 
                && lastDeclared.isSequenced() 
                && !boundSequenced) {
            if (parameters.get(parameters.size()-1).isDefaulted()) {
                Parameter sequencedParameter = parameters.get(parameters.size()-1);
                argExpr = makeDefaultedArgument(numDeclaredFixed, sequencedParameter);
            } else {
                argExpr = gen().makeEmpty();
            }
        }
        if (argExpr != null) {
            appendDefaulted(lastDeclared.getType(), numDeclaredFixed, argExpr, AbstractTransformer.WANT_RAW_TYPE);
        }
    }
    private boolean hasDefaultArgument(int ii) {
        return parameters.get(ii).isDefaulted();
    }
}