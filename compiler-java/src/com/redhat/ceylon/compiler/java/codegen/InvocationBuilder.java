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

import static com.redhat.ceylon.compiler.java.codegen.AbstractTransformer.COMPANION;
import static com.redhat.ceylon.compiler.java.codegen.AbstractTransformer.NO_PRIMITIVES;
import static com.redhat.ceylon.compiler.java.codegen.AbstractTransformer.TYPE_ARGUMENT;
import static com.sun.tools.javac.code.Flags.FINAL;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.redhat.ceylon.compiler.java.codegen.AbstractTransformer.BoxingStrategy;
import com.redhat.ceylon.compiler.java.codegen.ExpressionTransformer.TermTransformer;
import com.redhat.ceylon.compiler.java.util.Util;
import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Functional;
import com.redhat.ceylon.compiler.typechecker.model.FunctionalParameter;
import com.redhat.ceylon.compiler.typechecker.model.Getter;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ParameterList;
import com.redhat.ceylon.compiler.typechecker.model.ProducedReference;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Comprehension;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Expression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.FunctionArgument;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Primary;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.QualifiedTypeExpression;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCNewClass;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;

abstract class InvocationBuilder {

    protected final AbstractTransformer gen;
    protected final Node node;    
    protected final Tree.Primary primary;
    protected final Declaration primaryDeclaration;
    protected final ProducedType returnType;
    protected final List<JCExpression> primaryTypeArguments;
    protected boolean unboxed;
    protected BoxingStrategy boxingStrategy;
    private final ListBuffer<JCExpression> args = ListBuffer.lb();
    protected final Map<TypeParameter, ProducedType> typeArguments;
    
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
    }
    
    static final List<JCExpression> transformTypeArguments(
            AbstractTransformer gen,
            java.util.List<ProducedType> typeArguments) {
        List<JCExpression> result = List.<JCExpression> nil();
        if(typeArguments != null){
            for (ProducedType arg : typeArguments) {
                // cancel type parameters and go raw if we can't specify them
                if(gen.willEraseToObject(arg))
                    return List.nil();
                result = result.append(gen.makeJavaType(arg, TYPE_ARGUMENT));
            }
        }
        return result;
    }
    
    /**
     * Makes calls to {@link #appendArgument(JCExpression)} ready for a 
     * call to {@link #build()}
     */
    protected abstract void compute();
    
    /**
     * Appends an argument
     * @param argExpr
     */
    protected final void appendArgument(JCExpression argExpr) {
        this.args.append(argExpr);
    }
    
    public final void setUnboxed(boolean unboxed) {
        this.unboxed = unboxed;
    }

    public final void setBoxingStrategy(BoxingStrategy boxingStrategy) {
        this.boxingStrategy = boxingStrategy;
    }

    protected final Map<TypeParameter, ProducedType> getTypeArguments() {
        return this.typeArguments;
    }
    
    protected JCExpression transformInvocation(JCExpression primaryExpr, String selector) {
        JCExpression actualPrimExpr = transformInvocationPrimary(primaryExpr, selector);
        JCExpression resultExpr;
        if (primary instanceof Tree.BaseTypeExpression) {
            ProducedType classType = (ProducedType)((Tree.MemberOrTypeExpression)primary).getTarget();
            resultExpr = gen.make().NewClass(null, null, gen.makeJavaType(classType, AbstractTransformer.CLASS_NEW), args.toList(), null);
        } else if (primary instanceof Tree.QualifiedTypeExpression) {
            // When doing qualified invocation through an interface we need
            // to get the companion.
            if (((Tree.QualifiedTypeExpression)primary).getDeclaration().getContainer() instanceof Interface
                    && !(((Tree.QualifiedTypeExpression)primary).getPrimary() instanceof Tree.Outer)) {
                Interface qualifyingInterface = (Interface)((Tree.QualifiedTypeExpression)primary).getDeclaration().getContainer();
                actualPrimExpr = gen.make().Apply(null, 
                        gen.makeSelect(actualPrimExpr, gen.getCompanionAccessorName(qualifyingInterface)), 
                        List.<JCExpression>nil());
                // But when the interface is local the accessor returns Object
                // so we need to cast it to the type of the companion
                if (Decl.isAncestorLocal(((Tree.QualifiedTypeExpression)primary).getDeclaration())) {
                    actualPrimExpr = gen.make().TypeCast(gen.makeJavaType(qualifyingInterface.getType(), COMPANION), actualPrimExpr);
                }
            }
            // Note: here we're not fully qualifying the class name because the JLS says that if "new" is qualified the class name
            // is qualified relative to it
            ProducedType classType = (ProducedType)((Tree.MemberOrTypeExpression)primary).getTarget();
            resultExpr = gen.make().NewClass(actualPrimExpr, null, gen.makeJavaType(classType, AbstractTransformer.CLASS_NEW | AbstractTransformer.NON_QUALIFIED), args.toList(), null);
        } else {
            if (primaryDeclaration instanceof FunctionalParameter
                    || (this instanceof IndirectInvocationBuilder)) {
                if (primaryDeclaration != null) {
                    if (primaryExpr != null) {
                        actualPrimExpr = gen.makeQualIdent(primaryExpr, primaryDeclaration.getName());
                    } else if (primaryDeclaration instanceof Getter){
                        actualPrimExpr = gen.make().Apply(null,  gen.makeUnquotedIdent(selector), List.<JCExpression>nil());
                    } else {
                        actualPrimExpr = gen.makeUnquotedIdent(primaryDeclaration.getName());
                    }
                } else {
                    // indirect with invocation as primary
                }
                selector = "$call";
            }
            resultExpr = gen.make().Apply(primaryTypeArguments, gen.makeQuotedQualIdent(actualPrimExpr, selector), args.toList());
        }
        
        resultExpr = gen.expressionGen().applyErasureAndBoxing(resultExpr, returnType, 
                !unboxed, boxingStrategy, returnType);
        
        return resultExpr;
    }

    protected JCExpression transformInvocationPrimary(JCExpression primaryExpr,
            String selector) {
        JCExpression actualPrimExpr;
        if (primary instanceof Tree.QualifiedTypeExpression
                && ((Tree.QualifiedTypeExpression)primary).getPrimary() instanceof Tree.BaseTypeExpression) {
            actualPrimExpr = gen.makeSelect(primaryExpr, "this");
        } else {
            actualPrimExpr = primaryExpr;
        }
        return actualPrimExpr;
    }
    
    protected JCExpression makeInvocation(List<JCExpression> argExprs) {
        gen.at(node);
        JCExpression result = gen.expressionGen().transformPrimary(primary, new TermTransformer() {
            @Override
            public JCExpression transform(JCExpression primaryExpr, String selector) {
                return transformInvocation(primaryExpr, selector);
            }
        });
        
        //result = gen.expressionGen().applyErasureAndBoxing(result, returnType, 
        //        !unboxed, boxingStrategy, returnType);
        
        return result;
    }

    public final JCExpression build() {
        boolean prevFnCall = gen.expressionGen().isWithinInvocation();
        gen.expressionGen().setWithinInvocation(true);
        try {
            return makeInvocation(args.toList());
        } finally {
            gen.expressionGen().setWithinInvocation(prevFnCall);
        }
    }
    
    public static InvocationBuilder forSuperInvocation(AbstractTransformer gen,
            Tree.InvocationExpression invocation) {
        gen.expressionGen().setWithinSuperInvocation(true);
        try {
            Declaration primaryDeclaration = ((Tree.MemberOrTypeExpression)invocation.getPrimary()).getDeclaration();
            java.util.List<ParameterList> paramLists = ((Functional)primaryDeclaration).getParameterLists();
            SuperInvocationBuilder builder = new SuperInvocationBuilder(gen,
                    invocation,
                    paramLists.get(0));
            builder.compute();
            return builder;
        } finally {
            gen.expressionGen().setWithinSuperInvocation(false);
        }
    }
    
    public static InvocationBuilder forInvocation(AbstractTransformer gen, 
            final Tree.InvocationExpression invocation) {
        
        Tree.Primary primary = invocation.getPrimary();
        Declaration primaryDeclaration = null;
        ProducedReference producedReference = null;
        if (primary instanceof Tree.MemberOrTypeExpression) {
            producedReference = ((Tree.MemberOrTypeExpression)primary).getTarget();
            primaryDeclaration = ((Tree.MemberOrTypeExpression)primary).getDeclaration();
        }
        InvocationBuilder builder;
        if (invocation.getPositionalArgumentList() != null) {
            if (primaryDeclaration instanceof Functional){
                // direct invocation
                java.util.List<Parameter> parameters = ((Functional)primaryDeclaration).getParameterLists().get(0).getParameters();
                builder = new PositionalInvocationBuilder(gen, 
                        primary, primaryDeclaration,producedReference,
                        invocation,
                        parameters);
            } else {
                // indirect invocation
                final java.util.List<ProducedType> tas = primary.getTypeModel().getTypeArgumentList();
                final java.util.List<ProducedType> parameterTypes = tas.subList(1, tas.size());
                final java.util.List<Tree.Expression> argumentExpressions = new ArrayList<Tree.Expression>(tas.size());
                for (Tree.PositionalArgument argument : invocation.getPositionalArgumentList().getPositionalArguments()) {
                    argumentExpressions.add(argument.getExpression());
                }
                builder = new IndirectInvocationBuilder(gen, 
                        primary, primaryDeclaration,
                        invocation,
                        parameterTypes, argumentExpressions);
            }
            
        } else if (invocation.getNamedArgumentList() != null) {
            builder = new NamedArgumentInvocationBuilder(gen, 
                    primary, 
                    primaryDeclaration,
                    producedReference,
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
                primary.getTarget(),
                gen.getReturnTypeOfCallable(expr.getTypeModel()),
                expr, 
                parameterList);
        builder.compute();
        return builder;
    }
    
    public static InvocationBuilder forSpecifierInvocation(
            CeylonTransformer gen, Tree.SpecifierExpression specifierExpression,
            Method method) {
        Tree.Term primary = specifierExpression.getExpression().getTerm();
        InvocationBuilder builder;
        if (primary instanceof Tree.MemberOrTypeExpression
                && ((Tree.MemberOrTypeExpression)primary).getDeclaration() instanceof Functional) {
            Declaration primaryDeclaration = ((Tree.MemberOrTypeExpression)primary).getDeclaration();
            ProducedReference producedReference = ((Tree.MemberOrTypeExpression)primary).getTarget();
            builder = new MethodReferenceSpecifierInvocationBuilder(
                    gen, 
                    (Tree.MemberOrTypeExpression)primary, 
                    primaryDeclaration,
                    producedReference,
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

abstract class SimpleInvocationBuilder extends InvocationBuilder {

    public SimpleInvocationBuilder(AbstractTransformer gen, Primary primary,
            Declaration primaryDeclaration, ProducedType returnType, Node node) {
        super(gen, primary, primaryDeclaration, returnType, node);
    }

    protected abstract boolean isParameterSequenced(int argIndex);

    protected abstract ProducedType getParameterType(int argIndex);

    protected abstract String getParameterName(int argIndex);

    protected abstract boolean getParameterUnboxed(int argIndex);

    protected abstract BoxingStrategy getParameterBoxingStrategy(int argIndex);

    protected abstract boolean hasParameter(int argIndex);

    /** Gets the number of arguments actually being supplied */
    protected abstract int getNumArguments();

    /**
     * Gets the transformed expression supplying the argument value for the 
     * given argument index
     */
    //protected abstract JCExpression getTransformedArgumentExpression(int argIndex);

    protected abstract boolean dontBoxSequence();

    @Override
    protected final void compute() {
        int numArguments = getNumArguments();
        for (int argIndex = 0; argIndex < numArguments; argIndex++) {
            final JCExpression expr;
            if (!isParameterSequenced(argIndex)
                    || dontBoxSequence()
                    || isJavaMethod()) {
                expr = this.getTransformedArgumentExpression(argIndex);
            } else {
                // box with an ArraySequence<T>
                List<JCExpression> x = List.<JCExpression>nil();
                for ( ; argIndex < numArguments; argIndex++) {
                    x = x.append(this.getTransformedArgumentExpression(argIndex));
                }
                expr = gen.makeSequenceRaw(x);
            }
            appendArgument(expr);
        }
    }

    protected boolean isJavaMethod() {
        if(primaryDeclaration instanceof Method) {
            return gen.isJavaMethod((Method) primaryDeclaration);
        } else if (primaryDeclaration instanceof Class) {
            return gen.isJavaCtor((Class) primaryDeclaration);
        }
        return false;
    }

    protected abstract Tree.Expression getArgumentExpression(int argIndex);
    
    protected JCExpression getTransformedArgumentExpression(int argIndex) {
        Tree.Expression expr = getArgumentExpression(argIndex);
        if (expr.getTerm() instanceof FunctionArgument) {
            FunctionArgument farg = (FunctionArgument)expr.getTerm();
            return gen.expressionGen().transform(farg);
        }
        return transformArg(argIndex);
    }
    
    protected final JCExpression transformArg(int argIndex) {
        final Tree.Term expr = getArgumentExpression(argIndex);
        if (hasParameter(argIndex)) {
            ProducedType type = getParameterType(argIndex);
            if (isParameterSequenced(argIndex)
                    && !isJavaMethod()
                    && !dontBoxSequence()) {
                // If the parameter is sequenced and the argument is not ...
                // then the expected type of the *argument* is the type arg to Iterator
                type = gen.typeFact().getIteratedType(type);
            }
            BoxingStrategy boxingStrategy = getParameterBoxingStrategy(argIndex);
            JCExpression ret = gen.expressionGen().transformExpression(expr, 
                    boxingStrategy, 
                    type);
            if(isParameterSequenced(argIndex)
                    && isJavaMethod()
                    && dontBoxSequence()){
                // must translate it into a Util call
                ret = gen.sequenceToJavaArray(ret, type, boxingStrategy, expr.getTypeModel());
            }
            return ret;
        } else {
            // Overloaded methods don't have a reference to a parameter
            // so we have to treat them differently. Also knowing it's
            // overloaded we know we're dealing with Java code so we unbox
            ProducedType type = expr.getTypeModel();
            return gen.expressionGen().transformExpression(expr, 
                    BoxingStrategy.UNBOXED, 
                    type);
        }
    }
    
}

class IndirectInvocationBuilder extends SimpleInvocationBuilder {

    private final java.util.List<ProducedType> parameterTypes;
    private final java.util.List<Tree.Expression> argumentExpressions;

    public IndirectInvocationBuilder(
            AbstractTransformer gen, 
            Tree.Primary primary,
            Declaration primaryDeclaration,
            Tree.InvocationExpression invocation,
            java.util.List<ProducedType> parameterTypes,
            java.util.List<Tree.Expression> argumentExpressions) {
        super(gen, primary, primaryDeclaration, invocation.getTypeModel(), invocation);
        if (parameterTypes.size() != argumentExpressions.size()) {
            throw new RuntimeException();
        }
        this.parameterTypes = parameterTypes;
        this.argumentExpressions = argumentExpressions;
    }
    

    @Override
    protected boolean isParameterSequenced(int argIndex) {
        return false;
    }

    @Override
    protected ProducedType getParameterType(int argIndex) {
        return parameterTypes.get(argIndex);
    }

    @Override
    protected String getParameterName(int argIndex) {
        return "arg" + argIndex;
    }

    @Override
    protected boolean getParameterUnboxed(int argIndex) {
        return false;
    }

    @Override
    protected BoxingStrategy getParameterBoxingStrategy(int argIndex) {
        return BoxingStrategy.BOXED;
    }

    @Override
    protected boolean hasParameter(int argIndex) {
        return true;
    }

    @Override
    protected int getNumArguments() {
        return parameterTypes.size();
    }

    @Override
    protected boolean dontBoxSequence() {
        return true;
    }


    @Override
    protected Tree.Expression getArgumentExpression(int argIndex) {
        return argumentExpressions.get(argIndex);
    }
    
}

/**
 * An abstract implementation of InvocationBuilder support invocation 
 * via positional arguments. Supports with sequenced arguments but not 
 * defaulted arguments.
 */
abstract class DirectInvocationBuilder extends SimpleInvocationBuilder {

    protected final ProducedReference producedReference;

    protected DirectInvocationBuilder(
            AbstractTransformer gen,
            Tree.Primary primary,
            Declaration primaryDeclaration,
            ProducedReference producedReference, ProducedType returnType, 
            Node node) {
        super(gen, primary, primaryDeclaration, returnType, node);
        this.producedReference = producedReference;
    }

    /**
     * Gets the Parameter corresponding to the given argument
     * @param argIndex
     * @return
     */
    protected abstract Parameter getParameter(int argIndex);
    
    @Override
    protected boolean isParameterSequenced(int argIndex) {
        return getParameter(argIndex).isSequenced();
    }
    
    @Override
    protected ProducedType getParameterType(int argIndex) {
        int flags = AbstractTransformer.TP_TO_BOUND;
        if(isParameterSequenced(argIndex)
                && isJavaMethod()
                && dontBoxSequence())
            flags |= AbstractTransformer.TP_SEQUENCED_TYPE;
        return gen.expressionGen().getTypeForParameter(getParameter(argIndex), producedReference, flags);
    }
    
    @Override
    protected String getParameterName(int argIndex) {
        return getParameter(argIndex).getName();
    }
    
    @Override
    protected boolean getParameterUnboxed(int argIndex) {
        return getParameter(argIndex).getUnboxed();
    }
    
    @Override
    protected BoxingStrategy getParameterBoxingStrategy(int argIndex) {
        return CodegenUtil.getBoxingStrategy(getParameter(argIndex));
    }
    
    @Override
    protected boolean hasParameter(int argIndex) {
        return getParameter(argIndex) != null;
    }
}

/**
 * InvocationBuilder used for 'normal' method and initializer invocations via 
 * positional arguments. Supports sequenced and defaulted arguments.
 */
class PositionalInvocationBuilder extends DirectInvocationBuilder {

    private final Tree.PositionalArgumentList positional;
    private final java.util.List<Parameter> parameters;

    public PositionalInvocationBuilder(
            AbstractTransformer gen, 
            Tree.Primary primary,
            Declaration primaryDeclaration,
            ProducedReference producedReference, Tree.InvocationExpression invocation,
            java.util.List<Parameter> parameters) {
        super(gen, primary, primaryDeclaration, producedReference, invocation.getTypeModel(), invocation);
        positional = invocation.getPositionalArgumentList();
        this.parameters = parameters;    
    }
    protected Tree.Expression getArgumentExpression(int argIndex) {
        return positional.getPositionalArguments().get(argIndex).getExpression();
    }
    @Override
    protected JCExpression getTransformedArgumentExpression(int argIndex) {
        if (argIndex == positional.getPositionalArguments().size() && positional.getComprehension() != null) {
            return gen.expressionGen().transformComprehension(positional.getComprehension());
        }
        Tree.Expression expr = getArgumentExpression(argIndex);
        if (expr.getTerm() instanceof FunctionArgument) {
            FunctionArgument farg = (FunctionArgument)expr.getTerm();
            return gen.expressionGen().transform(farg);
        }
        return transformArg(argIndex);
    }
    protected Parameter getParameter(int argIndex) {
        return positional.getPositionalArguments().get(argIndex).getParameter();
    }
    @Override
    protected int getNumArguments() {
        return positional.getPositionalArguments().size() + (positional.getComprehension()==null?0:1);
    }
    @Override
    protected boolean isParameterSequenced(int argIndex) {
        if (argIndex == positional.getPositionalArguments().size() && positional.getComprehension() != null) {
            return true;
        }
        return super.isParameterSequenced(argIndex);
    }
    @Override
    protected boolean dontBoxSequence() {
        return positional.getEllipsis() != null || positional.getComprehension() != null;
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
                ((Tree.MemberOrTypeExpression)invocation.getPrimary()).getTarget(),
                invocation,
                parameterList.getParameters());
    }
    @Override
    protected JCExpression makeInvocation(List<JCExpression> args) {
        gen.at(node);
        JCExpression result = gen.make().Apply(List.<JCExpression> nil(), gen.make().Ident(gen.names()._super), args);
        return result;
    }
}


/**
 * InvocationBuilder for constructing the invocation of a method reference 
 * used when implementing {@code Callable.call()}
 */
class CallableInvocationBuilder extends DirectInvocationBuilder {
    
    private final java.util.List<Parameter> callableParameters;
    
    private final java.util.List<Parameter> functionalParameters;
    
    public CallableInvocationBuilder(
            AbstractTransformer gen, Tree.MemberOrTypeExpression primary,
            Declaration primaryDeclaration, ProducedReference producedReference, ProducedType returnType,
            Tree.Term expr, ParameterList parameterList) {
        super(gen, primary, primaryDeclaration, producedReference, returnType, expr);
        callableParameters = ((Functional)primary.getDeclaration()).getParameterLists().get(0).getParameters();
        functionalParameters = parameterList.getParameters();
        setUnboxed(expr.getUnboxed());
        setBoxingStrategy(BoxingStrategy.BOXED);// Must be boxed because non-primitive return type
    }
    @Override
    protected int getNumArguments() {
        return functionalParameters.size();
    }
    @Override
    protected boolean dontBoxSequence() {
        return isParameterSequenced(getNumArguments() - 1);
    }
    @Override
    protected JCExpression getTransformedArgumentExpression(int argIndex) {
        Parameter param = callableParameters.get(argIndex);
        ProducedType argType = primary.getTypeModel().getTypeArgumentList().get(argIndex+1);
        return CallableBuilder.unpickCallableParameter(gen, producedReference, param, argType, argIndex, functionalParameters.size());
    }
    @Override
    protected Parameter getParameter(int index) {
        return functionalParameters.get(index);
    }
    @Override
    protected Expression getArgumentExpression(int argIndex) {
        throw new RuntimeException("I override getTransformedArgumentExpression(), so should never be called");
    }
}

/**
 * InvocationBuilder for methods specifierd with a method reference. 
 */
class MethodReferenceSpecifierInvocationBuilder extends DirectInvocationBuilder {
    
    private final Method method;

    public MethodReferenceSpecifierInvocationBuilder(
            AbstractTransformer gen, Tree.Primary primary,
            Declaration primaryDeclaration,
            ProducedReference producedReference, Method method, Tree.SpecifierExpression node) {
        super(gen, primary, primaryDeclaration, producedReference, method.getType(), node);
        this.method = method;
        setUnboxed(primary.getUnboxed());
        setBoxingStrategy(CodegenUtil.getBoxingStrategy(method));
    }

    @Override
    protected int getNumArguments() {
        return method.getParameterLists().get(0).getParameters().size();
    }
    
    @Override
    protected JCExpression getTransformedArgumentExpression(int argIndex) {
        ProducedType exprType = getParameterType(argIndex);
        Parameter declaredParameter = ((Functional)primaryDeclaration).getParameterLists().get(0).getParameters().get(argIndex);
        JCExpression result = gen.makeQuotedIdent(getParameterName(argIndex));
        result = gen.expressionGen().applyErasureAndBoxing(
                result, 
                exprType, 
                !getParameterUnboxed(argIndex), 
                CodegenUtil.getBoxingStrategy(declaredParameter), 
                declaredParameter.getType());
        return result;
    }
    @Override
    protected Parameter getParameter(int argIndex) {
        return method.getParameterLists().get(0).getParameters().get(argIndex);
    }
    @Override
    protected boolean dontBoxSequence() {
        return method.getParameterLists().get(0).getParameters().get(getNumArguments() - 1).isSequenced();
    }
    @Override
    protected Expression getArgumentExpression(int argIndex) {
        throw new RuntimeException("I override getTransformedArgumentExpression(), so should never be called");
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
        int argIndex = 0;
        for(Parameter parameter : method.getParameterLists().get(0).getParameters()) {
            ProducedType exprType = gen.expressionGen().getTypeForParameter(parameter, null, gen.TP_TO_BOUND);
            Parameter declaredParameter = method.getParameterLists().get(0).getParameters().get(argIndex);
            
            JCExpression result = gen.makeQuotedIdent(parameter.getName());
            
            result = gen.expressionGen().applyErasureAndBoxing(
                    result, 
                    exprType, 
                    !parameter.getUnboxed(), 
                    BoxingStrategy.BOXED,// Callables always have boxed params 
                    declaredParameter.getType());
            appendArgument(result);
            argIndex++;
        }
    }
    @Override
    protected JCExpression makeInvocation(List<JCExpression> args) {
        gen.at(node);
        JCExpression result = gen.make().Apply(primaryTypeArguments, gen.makeQuotedQualIdent(callable, "$call"), args);
        result = gen.expressionGen().applyErasureAndBoxing(result, returnType, 
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
    private final ListBuffer<JCStatement> vars = ListBuffer.lb();
    private final String callVarName;
    private final String varBaseName;
    private final Set<String> argNames = new HashSet<String>();
    private final TreeMap<Integer, String> argsNamesByIndex = new TreeMap<Integer, String>();
    private final Set<Parameter> bound = new HashSet<Parameter>();
    private ProducedReference producedReference;
    
    public NamedArgumentInvocationBuilder(
            AbstractTransformer gen, Tree.Primary primary,
            Declaration primaryDeclaration,
            ProducedReference producedReference,
            Tree.InvocationExpression invocation) {
        super(gen, primary, primaryDeclaration, invocation.getTypeModel(), invocation);
        this.producedReference = producedReference;
        namedArgumentList = invocation.getNamedArgumentList();
        varBaseName = gen.aliasName("arg");
        callVarName = varBaseName + "$callable$";
    }
    
    @Override
    protected void compute() {
        if (primaryDeclaration == null) {
            return;
        }
        java.util.List<Tree.NamedArgument> namedArguments = namedArgumentList.getNamedArguments();
        java.util.List<ParameterList> paramLists = ((Functional)primaryDeclaration).getParameterLists();
        java.util.List<Parameter> declaredParams = paramLists.get(0).getParameters();
        appendVarsForNamedArguments(namedArguments, declaredParams);
        boolean hasDefaulted = appendVarsForDefaulted(declaredParams);
        for (String argName : this.argsNamesByIndex.values()) {
            appendArgument(gen.makeUnquotedIdent(argName));
        }
        if (hasDefaulted 
                && !Strategy.defaultParameterMethodStatic(primaryDeclaration)) {
            vars.prepend(makeThis());
        }
    }
    
    private JCExpression makeDefaultedArgumentMethodCall(Parameter param) {
        final String methodName = CodegenUtil.getDefaultedParamMethodName(primaryDeclaration, param);
        JCExpression defaultValueMethodName;
        if (Strategy.defaultParameterMethodOnSelf(param)) {
            Declaration container = param.getDeclaration().getRefinedDeclaration();
            if (!container.isToplevel()) {
                container = (Declaration)container.getContainer();
            }
            String className = gen.getCompanionClassName(container);
            defaultValueMethodName = gen.makeQuotedQualIdent(gen.makeQuotedFQIdent(container.getQualifiedNameString()), className, methodName);
        } else if (Strategy.defaultParameterMethodStatic(param)) {
            Declaration container = param.getDeclaration().getRefinedDeclaration();
            if (!container.isToplevel()) {
                container = (Declaration)container.getContainer();
            }            
            defaultValueMethodName = gen.makeQuotedQualIdent(gen.makeQuotedFQIdent(container.getQualifiedNameString()), methodName);
        } else {
            defaultValueMethodName = gen.makeQuotedQualIdent(gen.makeQuotedIdent(varBaseName + "$this$"), methodName);
        }
        JCExpression argExpr = gen.at(node).Apply(null, 
                defaultValueMethodName, 
                makeVarRefArgumentList(param));
        return argExpr;
    }
    
    // Make a list of ($arg0, $arg1, ... , $argN)
    // or ($arg$this$, $arg0, $arg1, ... , $argN)
    private List<JCExpression> makeVarRefArgumentList(Parameter param) {
        ListBuffer<JCExpression> names = ListBuffer.<JCExpression> lb();
        if (!Strategy.defaultParameterMethodStatic(primaryDeclaration)
                && Strategy.defaultParameterMethodTakesThis(param)) {
            names.append(gen.makeUnquotedIdent(varBaseName + "$this$"));
        }
        final int parameterIndex = parameterIndex(param);
        for (int ii = 0; ii < parameterIndex; ii++) {
            names.append(gen.makeUnquotedIdent(this.argsNamesByIndex.get(ii)));
        }
        return names.toList();
    }
    
    /** Generates the argument name; namedArg may be null if no  
     * argument was given explicitly */
    private String argName(Parameter param) {
        final int paramIndex = parameterIndex(param);
        //if (this.argNames.isEmpty()) {
            //this.argNames.addAll(Collections.<String>nCopies(parameterList(param).size(), null));
        //}
        final String argName = varBaseName + "$" + paramIndex;
        if (this.argsNamesByIndex.containsValue(argName)) {
            throw new RuntimeException();
        }
        //if (!this.argNames.add(argName)) {
        //    throw new RuntimeException();
        //}
        return argName;
    }

    private java.util.List<Parameter> parameterList(Parameter param) {
        Functional functional = (Functional)param.getContainer();
        return functional.getParameterLists().get(0).getParameters();
    }
    
    private int parameterIndex(Parameter param) {
        return parameterList(param).indexOf(param);
    }

    private ProducedType parameterType(Parameter declaredParam, ProducedType pt, int flags) {
        return (declaredParam == null) ? pt : gen.getTypeForParameter(declaredParam, producedReference, flags);        
    }
    
    private void appendVarsForNamedArguments(
            java.util.List<Tree.NamedArgument> namedArguments,
            java.util.List<Parameter> declaredParams) {
        // Assign vars for each named argument given
        for (Tree.NamedArgument namedArg : namedArguments) {
            gen.at(namedArg);
            Parameter declaredParam = namedArg.getParameter();
            String argName = argName(declaredParam);
            ListBuffer<JCStatement> statements;
            if (namedArg instanceof Tree.SpecifiedArgument) {             
                Tree.SpecifiedArgument specifiedArg = (Tree.SpecifiedArgument)namedArg;
                Tree.Expression expr = specifiedArg.getSpecifierExpression().getExpression();
                ProducedType type = parameterType(declaredParam, expr.getTypeModel(), gen.TP_TO_BOUND);
                final BoxingStrategy boxType = declaredParam != null ? CodegenUtil.getBoxingStrategy(declaredParam) : BoxingStrategy.UNBOXED;
                JCExpression typeExpr = gen.makeJavaType(type, (boxType == BoxingStrategy.BOXED) ? TYPE_ARGUMENT : 0);
                JCExpression argExpr = gen.expressionGen().transformExpression(expr, boxType, type);
                JCVariableDecl varDecl = gen.makeVar(argName, typeExpr, argExpr);
                statements = ListBuffer.<JCStatement>of(varDecl);
            } else if (namedArg instanceof Tree.MethodArgument) {
                Tree.MethodArgument methodArg = (Tree.MethodArgument)namedArg;
                Method model = methodArg.getDeclarationModel();
                List<JCStatement> body;
                boolean prevNoExpressionlessReturn = gen.statementGen().noExpressionlessReturn;
                try {
                    gen.statementGen().noExpressionlessReturn = gen.isVoid(model.getType());
                    body = gen.statementGen().transform(methodArg.getBlock()).getStatements();
                    if (!methodArg.getBlock().getDefinitelyReturns()) {
                        if (gen.isVoid(model.getType())) {
                            body = body.append(gen.make().Return(gen.makeNull()));
                        } else {
                            body = body.append(gen.make().Return(gen.makeErroneous(methodArg.getBlock(), "non-void method doesn't definitely return")));
                        }
                    }
                } finally {
                    gen.statementGen().noExpressionlessReturn = prevNoExpressionlessReturn;
                }
                
                ProducedType callableType = gen.functionalType(model);                
                CallableBuilder callableBuilder = CallableBuilder.methodArgument(gen.gen(), 
                        callableType, 
                        model.getParameterLists().get(0), 
                        gen.classGen().transformMplBody(model, body));
                JCNewClass callable = callableBuilder.build();
                JCExpression typeExpr = gen.makeJavaType(callableType);
                JCVariableDecl varDecl = gen.makeVar(argName, typeExpr, callable);
                
                statements = ListBuffer.<JCStatement>of(varDecl);
            } else if (namedArg instanceof Tree.ObjectArgument) {
                Tree.ObjectArgument objectArg = (Tree.ObjectArgument)namedArg;
                List<JCTree> object = gen.classGen().transformObjectArgument(objectArg);
                // No need to worry about boxing (it cannot be a boxed type) 
                JCVariableDecl varDecl = gen.makeLocalIdentityInstance(argName, objectArg.getIdentifier().getText(), false);
                statements = toStmts(objectArg, object).append(varDecl);
            } else if (namedArg instanceof Tree.AttributeArgument) {
                Tree.AttributeArgument attrArg = (Tree.AttributeArgument)namedArg;
                final Getter model = attrArg.getDeclarationModel();
                final String name = model.getName();
                final String alias = gen.aliasName(name);
                final List<JCTree> attrClass = gen.gen().transformAttribute(model, alias, alias, attrArg.getBlock(), null, null);
                TypedDeclaration nonWideningTypeDeclaration = gen.nonWideningTypeDecl(model);
                ProducedType nonWideningType = gen.nonWideningType(model, nonWideningTypeDeclaration);
                ProducedType type = parameterType(declaredParam, model.getType(), 0);
                final BoxingStrategy boxType = declaredParam != null ? CodegenUtil.getBoxingStrategy(declaredParam) : BoxingStrategy.UNBOXED;
                JCExpression initValue = gen.make().Apply(null, 
                        gen.makeSelect(alias, CodegenUtil.getGetterName(model)),
                        List.<JCExpression>nil());
                initValue = gen.expressionGen().applyErasureAndBoxing(
                        initValue, 
                        nonWideningType, 
                        !CodegenUtil.isUnBoxed(nonWideningTypeDeclaration),
                        boxType,
                        type);
                JCTree.JCVariableDecl var = gen.make().VarDef(
                        gen.make().Modifiers(FINAL, List.<JCAnnotation>nil()), 
                        gen.names().fromString(argName), 
                        gen.makeJavaType(type, boxType==BoxingStrategy.BOXED ? NO_PRIMITIVES : 0), 
                        initValue);
                statements = toStmts(attrArg, attrClass).append(var);
            } else {
                throw new RuntimeException("" + namedArg);
            }
            bind(declaredParam, argName, statements.toList());
        }
    }
    
    private void bind(Parameter param, String argName, List<JCStatement> statements) {
        this.vars.appendList(statements);
        this.argsNamesByIndex.put(parameterIndex(param), argName);
        this.bound.add(param);
    }
    
    private ListBuffer<JCStatement> toStmts(Tree.NamedArgument namedArg, final List<JCTree> listOfStatements) {
        final ListBuffer<JCStatement> result = ListBuffer.<JCStatement>lb();
        for (JCTree tree : listOfStatements) {
            if (tree instanceof JCStatement) {
                result.append((JCStatement)tree);
            } else {
                result.append(gen.make().Exec(gen.makeErroneous(namedArg, "Attempt to put a non-statement in a Let")));
            }
        }
        return result;
    }
    
    private final void appendDefaulted(Parameter param, JCExpression argExpr) {
        int flags = 0;
        if (CodegenUtil.getBoxingStrategy(param) == BoxingStrategy.BOXED) {
            flags |= TYPE_ARGUMENT;
        }
        ProducedType type = gen.getTypeForParameter(param, producedReference, gen.TP_TO_BOUND);
        String argName = argName(param);
        JCExpression typeExpr = gen.makeJavaType(type, flags);
        JCVariableDecl varDecl = gen.makeVar(argName, typeExpr, argExpr);
        bind(param, argName, List.<JCStatement>of(varDecl));
    }
    
    private boolean appendVarsForDefaulted(java.util.List<Parameter> declaredParams) {
        boolean hasDefaulted = false;
        if (!Decl.isOverloaded(primaryDeclaration)) {
            // append any arguments for defaulted parameters
            for (Parameter param : declaredParams) {
                if (bound.contains(param)) {
                    continue;
                }
                final JCExpression argExpr;
                if (param.isDefaulted()) {
                    argExpr = makeDefaultedArgumentMethodCall(param);
                    hasDefaulted |= true;
                } else if (param.isSequenced()) {
                    Tree.SequencedArgument sequencedArgument = namedArgumentList.getSequencedArgument();
                    if (sequencedArgument != null) {
                        gen.at(sequencedArgument);
                        if (sequencedArgument.getEllipsis() == null) {
                            argExpr = gen.makeSequenceRaw(sequencedArgument.getExpressionList().getExpressions());
                        } else {
                            argExpr = gen.expressionGen().transformExpression(sequencedArgument.getExpressionList().getExpressions().get(0));
                        }
                    } else if (namedArgumentList.getComprehension() != null) {
                        argExpr = gen.expressionGen().transformComprehension(namedArgumentList.getComprehension());
                    } else {
                        if (primaryDeclaration instanceof FunctionalParameter) {
                            argExpr = gen.makeEmpty();
                        } else {
                            argExpr = makeDefaultedArgumentMethodCall(param);
                            hasDefaulted |= true;
                        }
                    }
                } else {
                    argExpr = gen.makeErroneous(this.node, "Missing argument, and parameter is not defaulted");
                }
                appendDefaulted(param, argExpr);
            }
        }
        return hasDefaulted;
    }
    
    private final JCVariableDecl makeThis() {
        // first append $this
        JCExpression defaultedParameterInstance;
        // TODO Fix how we figure out the thisType, because it's doesn't 
        // handle type parameters correctly
        // we used to use thisType = gen.getThisType(getPrimaryDeclaration());
        final JCExpression thisType;
        ProducedReference target = ((Tree.MemberOrTypeExpression)primary).getTarget();
        if (primary instanceof Tree.BaseMemberExpression) {
            if (Decl.withinClassOrInterface(primaryDeclaration)) {
                // a member method
                thisType = gen.makeJavaType(target.getQualifyingType(), NO_PRIMITIVES);
                defaultedParameterInstance = gen.makeUnquotedIdent("this");
            } else {
                // a local or toplevel function
                defaultedParameterInstance = gen.makeUnquotedIdent(primaryDeclaration.getName());
                thisType = gen.makeQuotedIdent(primaryDeclaration.getName());
            }
        } else if (primary instanceof Tree.BaseTypeExpression
                || primary instanceof Tree.QualifiedTypeExpression) {
            Map<TypeParameter, ProducedType> typeA = target.getTypeArguments();
            ListBuffer<JCExpression> typeArgs = ListBuffer.<JCExpression>lb();
            for (TypeParameter tp : ((TypeDeclaration)target.getDeclaration()).getTypeParameters()) {
                ProducedType producedType = typeA.get(tp);
                typeArgs.append(gen.makeJavaType(producedType, TYPE_ARGUMENT));
            }
            ClassOrInterface declaration = (ClassOrInterface)((Tree.BaseTypeExpression) primary).getDeclaration();
            thisType = gen.makeJavaType(declaration.getType(), COMPANION);
            defaultedParameterInstance = gen.make().NewClass(
                    null, 
                    null,
                    gen.makeJavaType(declaration.getType(), COMPANION), 
                    List.<JCExpression>nil(), null);
        } else {
            thisType = gen.makeJavaType(target.getQualifyingType(), NO_PRIMITIVES);
            defaultedParameterInstance = gen.makeUnquotedIdent(callVarName);
        }
        JCVariableDecl thisDecl = gen.makeVar(varBaseName + "$this$", 
                thisType, 
                defaultedParameterInstance);
        return thisDecl;
    }
    
    @Override
    protected JCExpression transformInvocation(JCExpression primaryExpr, String selector) {
        JCExpression resultExpr = super.transformInvocation(primaryExpr, selector);
        // apply the default parameters
        if (vars != null && !vars.isEmpty()) {
            if (returnType == null || Decl.isUnboxedVoid(primaryDeclaration)) {
                // void methods get wrapped like (let $arg$1=expr, $arg$0=expr in call($arg$0, $arg$1); null)
                resultExpr = gen.make().LetExpr( 
                        vars.append(gen.make().Exec(resultExpr)).toList(), 
                        gen.makeNull());
            } else {
                // all other methods like (let $arg$1=expr, $arg$0=expr in call($arg$0, $arg$1))
                resultExpr = gen.make().LetExpr( 
                        vars.toList(),
                        resultExpr);
            }
        }
        
        return resultExpr;
    }
    
    @Override
    protected JCExpression transformInvocationPrimary(JCExpression primaryExpr,
            String selector) {
        JCExpression actualPrimExpr = super.transformInvocationPrimary(primaryExpr, selector);
        if (vars != null 
                && !vars.isEmpty() 
                && primaryExpr != null
                && selector != null) {
            // Prepare the first argument holding the primary for the call
            ProducedType type = ((Tree.MemberOrTypeExpression)primary).getTarget().getQualifyingType();
            JCExpression varType;
            if (primary instanceof QualifiedTypeExpression
                    && (((QualifiedTypeExpression)primary).getPrimary() instanceof Tree.Outer)) {
                varType = gen.makeJavaType(type, NO_PRIMITIVES | COMPANION);
            } else {
                varType = gen.makeJavaType(type, NO_PRIMITIVES);
            }
            vars.prepend(gen.makeVar(callVarName, varType, actualPrimExpr));
            actualPrimExpr = gen.makeUnquotedIdent(callVarName);
        }
        return actualPrimExpr;
    }
}