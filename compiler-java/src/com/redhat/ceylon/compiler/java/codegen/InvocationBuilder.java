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

import static com.redhat.ceylon.compiler.java.codegen.AbstractTransformer.JT_COMPANION;
import static com.redhat.ceylon.compiler.java.codegen.AbstractTransformer.JT_NO_PRIMITIVES;
import static com.redhat.ceylon.compiler.java.codegen.AbstractTransformer.JT_RAW;
import static com.redhat.ceylon.compiler.java.codegen.AbstractTransformer.JT_TYPE_ARGUMENT;
import static com.sun.tools.javac.code.Flags.FINAL;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.redhat.ceylon.compiler.java.codegen.AbstractTransformer.BoxingStrategy;
import com.redhat.ceylon.compiler.java.codegen.ExpressionTransformer.TermTransformer;
import com.redhat.ceylon.compiler.java.codegen.Naming.SyntheticName;
import com.redhat.ceylon.compiler.loader.model.LazyMethod;
import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.ClassAlias;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Functional;
import com.redhat.ceylon.compiler.typechecker.model.FunctionalParameter;
import com.redhat.ceylon.compiler.typechecker.model.Getter;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ParameterList;
import com.redhat.ceylon.compiler.typechecker.model.ProducedReference;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.ProducedTypedReference;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.BaseMemberExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Comprehension;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Expression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.FunctionArgument;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.InvocationExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.PositionalArgumentList;
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
    protected boolean handleBoxing;
    protected boolean unboxed;
    protected BoxingStrategy boxingStrategy;
    private final ListBuffer<JCExpression> args = ListBuffer.lb();
    protected final Map<TypeParameter, ProducedType> typeArguments;
    protected final Tree.Primary qmePrimary;
    protected final boolean onValueType;
    
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
        if (primary instanceof Tree.QualifiedMemberOrTypeExpression){
            this.qmePrimary = ((Tree.QualifiedMemberOrTypeExpression) primary).getPrimary();
            this.onValueType = Decl.isValueTypeDecl(qmePrimary);
        } else {
            this.qmePrimary = null;
            this.onValueType = false;
        }
    }

    static final List<JCExpression> transformTypeArguments(
            AbstractTransformer gen,
            java.util.List<ProducedType> typeArguments) {
        List<JCExpression> result = List.<JCExpression> nil();
        if(typeArguments != null){
            for (ProducedType arg : typeArguments) {
                // cancel type parameters and go raw if we can't specify them
                if(gen.willEraseToObject(arg) || gen.willEraseToSequential(arg))
                    return List.nil();
                result = result.append(gen.makeJavaType(arg, JT_TYPE_ARGUMENT));
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

    public void handleBoxing(boolean b) {
        handleBoxing = b;
    }

    public final void setBoxingStrategy(BoxingStrategy boxingStrategy) {
        this.boxingStrategy = boxingStrategy;
    }

    protected final Map<TypeParameter, ProducedType> getTypeArguments() {
        return this.typeArguments;
    }
    
    protected JCExpression transformInvocation(JCExpression primaryExpr, String selector, List<JCExpression> argExprs) {
        JCExpression actualPrimExpr = transformInvocationPrimary(primaryExpr, selector);
        JCExpression resultExpr;
        if (primary instanceof Tree.BaseTypeExpression) {
            Tree.BaseTypeExpression type = (Tree.BaseTypeExpression)primary;
            Declaration declaration = type.getDeclaration();
            if (Strategy.generateInstantiator(declaration)) {
                JCExpression qual;
                if (Decl.withinInterface(declaration)) {
                    qual = primaryExpr != null ? primaryExpr : gen.naming.makeQuotedThis();
                } else { 
                    qual = null;
                }
                resultExpr = gen.make().Apply(null, 
                        gen.naming.makeInstantiatorMethodName(qual, (Class)declaration), 
                        argExprs);
                if (Decl.isAncestorLocal(primaryDeclaration)) {
                    // $new method declared to return Object, so needs typecast
                    resultExpr = gen.make().TypeCast(gen.makeJavaType(
                            ((TypeDeclaration)declaration).getType()), resultExpr);
                }
            } else {
                ProducedType classType = (ProducedType)type.getTarget();
                resultExpr = gen.make().NewClass(null, null, gen.makeJavaType(classType, AbstractTransformer.JT_CLASS_NEW), argExprs, null);
            }
        } else if (primary instanceof Tree.QualifiedTypeExpression) {
            // When doing qualified invocation through an interface we need
            // to get the companion.
            Tree.QualifiedTypeExpression qte = (Tree.QualifiedTypeExpression)primary;
            Declaration declaration = qte.getDeclaration();
            if (declaration.getContainer() instanceof Interface
                    && !Strategy.generateInstantiator(declaration)
                    && !(qte.getPrimary() instanceof Tree.Outer)) {
                Interface qualifyingInterface = (Interface)declaration.getContainer();
                actualPrimExpr = gen.make().Apply(null, 
                        gen.makeSelect(actualPrimExpr, gen.getCompanionAccessorName(qualifyingInterface)), 
                        List.<JCExpression>nil());
                // But when the interface is local the accessor returns Object
                // so we need to cast it to the type of the companion
                if (Decl.isAncestorLocal(declaration)) {
                    actualPrimExpr = gen.make().TypeCast(gen.makeJavaType(qualifyingInterface.getType(), JT_COMPANION), actualPrimExpr);
                }
            }
            if (Strategy.generateInstantiator(declaration)) {
                resultExpr = gen.make().Apply(null, 
                        gen.naming.makeInstantiatorMethodName(actualPrimExpr, (Class)declaration), 
                        argExprs);
            } else {
                ProducedType classType = (ProducedType)qte.getTarget();
                // Note: here we're not fully qualifying the class name because the JLS says that if "new" is qualified the class name
                // is qualified relative to it
                JCExpression type = gen.makeJavaType(classType, AbstractTransformer.JT_CLASS_NEW | AbstractTransformer.JT_NON_QUALIFIED);
                resultExpr = gen.make().NewClass(actualPrimExpr, null, type, argExprs, null);
            }
        } else {
            if (this instanceof IndirectInvocationBuilder
                    && (primaryDeclaration instanceof Getter
                            || (primaryDeclaration instanceof Value)
                            && !Decl.isLocal(primaryDeclaration))) {
                actualPrimExpr = gen.make().Apply(null, 
                        gen.naming.makeQualIdent(primaryExpr, selector), 
                        List.<JCExpression>nil());
                selector = Naming.getCallableMethodName();
            } else if (primaryDeclaration instanceof FunctionalParameter
                    || (this instanceof IndirectInvocationBuilder)) {
                if (selector != null) {
                    actualPrimExpr = gen.naming.makeQualIdent(primaryExpr, selector);
                } else {
                    actualPrimExpr = gen.naming.makeQualifiedName(primaryExpr, (TypedDeclaration)primaryDeclaration, Naming.NA_MEMBER);
                }
                if (!gen.isCeylonCallable(primary.getTypeModel())) {                    
                    actualPrimExpr = gen.make().Apply(null, actualPrimExpr, List.<JCExpression>nil());
                }
                selector = Naming.getCallableMethodName();
            }
            if (onValueType) {
                JCExpression primTypeExpr = gen.makeJavaType(qmePrimary.getTypeModel(), JT_NO_PRIMITIVES);
                resultExpr = gen.make().Apply(primaryTypeArguments, 
                        gen.naming.makeQuotedQualIdent(primTypeExpr, selector), 
                        argExprs.prepend(actualPrimExpr));
            } else {
                resultExpr = gen.make().Apply(primaryTypeArguments, 
                        gen.naming.makeQuotedQualIdent(actualPrimExpr, selector), 
                        argExprs);
            }
        }
        
        if(handleBoxing)
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
        
        final List<JCExpression> args;
        if (needsTypeInfoArgument()) {
            JCExpression infoArg = makeTypeInfoArgument();
            args = argExprs.prepend(infoArg);
        } else {
            args = argExprs;
        }
        
        JCExpression result = gen.expressionGen().transformPrimary(primary, new TermTransformer() {
            @Override
            public JCExpression transform(JCExpression primaryExpr, String selector) {
                return transformInvocation(primaryExpr, selector, args);
            }
        });

        return result;
    }

    private boolean needsTypeInfoArgument() {
        if (primaryDeclaration instanceof LazyMethod) {
            if ("ceylon.language".equals(primaryDeclaration.getContainer().getQualifiedNameString())) {
                String name = primaryDeclaration.getName();
                return ("array".equals(name) || "arrayOfSome".equals(name) || "arrayOfNone".equals(name) || "arrayOfSize".equals(name));
            }
        }
        return false;
    }

    private JCExpression makeTypeInfoArgument() {
        Tree.BaseMemberExpression bme = (BaseMemberExpression) primary;
        ProducedType type = bme.getTypeArguments().getTypeModels().get(0);
        ProducedType simpleType = gen.simplifyType(type);
        JCExpression typeExpr;
        if (simpleType.getDeclaration() instanceof TypeParameter
                || gen.typeFact().isUnion(simpleType)
                || gen.typeFact().isIntersection(simpleType)) {
            return gen.makeNull();
        } else {
            typeExpr = gen.makeJavaType(type, JT_RAW);
            return gen.makeSelect(typeExpr, "class");
        }
    }

    public final JCExpression build() {
        boolean prevFnCall = gen.expressionGen().withinInvocation(true);
        try {
            return makeInvocation(args.toList());
        } finally {
            gen.expressionGen().withinInvocation(prevFnCall);
        }
    }
    
    public static InvocationBuilder forSuperInvocation(AbstractTransformer gen,
            Tree.InvocationExpression invocation, 
            com.redhat.ceylon.compiler.typechecker.tree.Tree.ClassOrInterface forDefinition) {
        // Because super() invocations cannot be nested there's no need to 
        // keep and restore the old withinSuperInvocation state.
        gen.expressionGen().withinSuperInvocation(forDefinition);
        try {
            Declaration primaryDeclaration = ((Tree.MemberOrTypeExpression)invocation.getPrimary()).getDeclaration();
            java.util.List<ParameterList> paramLists = ((Functional)primaryDeclaration).getParameterLists();
            SuperInvocationBuilder builder = new SuperInvocationBuilder(gen,
                    forDefinition,
                    invocation,
                    paramLists.get(0));
            builder.compute();
            return builder;
        } finally {
            gen.expressionGen().withinSuperInvocation(null);
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
                builder = new IndirectInvocationBuilder(gen, 
                        primary, primaryDeclaration,
                        invocation);
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
        builder.handleBoxing(true);
        builder.compute();
        return builder;
    }
    
    public static InvocationBuilder forSpecifierInvocation(
            CeylonTransformer gen, Tree.SpecifierExpression specifierExpression,
            Method method) {
        Tree.Term primary = Decl.unwrapExpressionsUntilTerm(specifierExpression.getExpression());
        boolean lazy = specifierExpression instanceof Tree.LazySpecifierExpression;
        boolean inlined = CodegenUtil.canOptimiseMethodSpecifier(primary, method);
        InvocationBuilder builder;
        if (lazy && primary instanceof InvocationExpression) {
            primary = ((InvocationExpression)primary).getPrimary();
        }
        if ((lazy || inlined)
                && primary instanceof Tree.MemberOrTypeExpression
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
        } else if (!lazy && !inlined) {
            // must be a callable we stored
            String name = gen.naming.getMethodSpecifierAttributeName(method);
            builder = new CallableSpecifierInvocationBuilder(
                    gen, 
                    method, 
                    gen.naming.makeUnquotedIdent(name),
                    primary);
        } else if (gen.isCeylonCallable(primary.getTypeModel())) {
            builder = new CallableSpecifierInvocationBuilder(
                    gen, 
                    method, 
                    gen.expressionGen().transformExpression(primary),
                    primary);
        } else {
            throw Assert.fail("Unhandled primary " + primary);
        }
        builder.handleBoxing(true);
        builder.compute();
        return builder;
    }
    
    public static InvocationBuilder forMethodInitializer(
            CeylonTransformer gen, 
            JCExpression callableExpr,
            Node node,
            Method method) {
        InvocationBuilder builder = new CallableSpecifierInvocationBuilder(
                gen,
                method,
                callableExpr,
                node);
      
        builder.handleBoxing(true);
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

    //protected abstract String getParameterName(int argIndex);
    protected abstract JCExpression getParameterExpression(int argIndex);

    protected abstract boolean getParameterUnboxed(int argIndex);

    protected abstract BoxingStrategy getParameterBoxingStrategy(int argIndex);

    protected abstract boolean hasParameter(int argIndex);

    // to be overridden
    protected boolean isParameterRaw(int argIndex) {
        return false;
    }

    /** Gets the number of arguments actually being supplied */
    protected abstract int getNumArguments();

    /**
     * Gets the transformed expression supplying the argument value for the 
     * given argument index
     */
    //protected abstract JCExpression getTransformedArgumentExpression(int argIndex);

    protected abstract boolean dontBoxSequence();

    @Override
    protected void compute() {
        int numArguments = getNumArguments();
        boolean wrapIntoArray = false;
        ListBuffer<JCExpression> arrayWrap = new ListBuffer<JCExpression>();
        for (int argIndex = 0; argIndex < numArguments; argIndex++) {
            final JCExpression expr;
            // for Java methods of variadic primitives, it's better to wrap them ourselves into an array
            // to avoid ambiguity of foo(1,2) for foo(int...) and foo(Object...) methods
            if(!wrapIntoArray
                    && isParameterSequenced(argIndex)
                    && isJavaMethod()
                    && getParameterBoxingStrategy(argIndex) == BoxingStrategy.UNBOXED
                    && gen.willEraseToPrimitive(gen.typeFact().getDefiniteType(getParameterType(argIndex)))
                    && !dontBoxSequence())
                wrapIntoArray = true;
            if (!isParameterSequenced(argIndex)
                    || dontBoxSequence()
                    || isJavaMethod()) {
                expr = this.getTransformedArgumentExpression(argIndex);
            } else {
                // box with an ArraySequence<T>
                List<JCExpression> x = List.<JCExpression>nil();
                final ProducedType iteratedType = gen.typeFact().getIteratedType(getParameterType(argIndex));
                for ( ; argIndex < numArguments; argIndex++) {
                    x = x.append(this.getTransformedArgumentExpression(argIndex));
                }
                expr = gen.makeSequence(x, iteratedType, JT_TYPE_ARGUMENT);
            }
            if(!wrapIntoArray)
                appendArgument(expr);
            else
                arrayWrap.append(expr);
        }
        if(wrapIntoArray){
            // must have at least one arg, so take the last one
            ProducedType parameterType = getParameterType(numArguments-1);
            JCExpression arrayType = gen.makeJavaType(parameterType);
            appendArgument(gen.make().NewArray(arrayType, List.<JCExpression>nil(), arrayWrap.toList()));
        }
    }

    /**
     * For subclasses if the target method doesn't support default values for variadic
     * using overloading.
     */
    protected boolean requiresEmptyForVariadic() {
        return false;
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
            int flags = 0;
            if(!isParameterRaw(argIndex))
                flags |= ExpressionTransformer.EXPR_EXPECTED_TYPE_NOT_RAW;
            JCExpression ret = gen.expressionGen().transformExpression(expr, 
                    boxingStrategy, 
                    type, flags);
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

/**
 * Generates calls to Callable methods. This is for regular {@code Callable<T>} objects and not
 * functional parameters, which have more info like parameter names and default values.
 */
class IndirectInvocationBuilder extends SimpleInvocationBuilder {

    private final java.util.List<ProducedType> parameterTypes;
    private final java.util.List<Tree.Expression> argumentExpressions;
    private Comprehension comprehension;
    private boolean variadic;
    private boolean spread;
    private int minimumParameters;

    public IndirectInvocationBuilder(
            AbstractTransformer gen, 
            Tree.Primary primary,
            Declaration primaryDeclaration,
            Tree.InvocationExpression invocation) {
        super(gen, primary, primaryDeclaration, invocation.getTypeModel(), invocation);

        // find the parameter types
        final java.util.List<ProducedType> tas = new ArrayList<>();
        ProducedType callableType = primary.getTypeModel();
        tas.add(gen.getReturnTypeOfCallable(callableType));
        for (int ii = 0, l = gen.getNumParametersOfCallable(callableType); ii < l; ii++) {
            tas.add(gen.getParameterTypeOfCallable(callableType, ii));
        }
        this.variadic = gen.isVariadicCallable(callableType);
        this.minimumParameters = gen.getMinimumParameterCountForCallable(callableType);
        //final java.util.List<ProducedType> tas = primary.getTypeModel().getTypeArgumentList();
        final java.util.List<ProducedType> parameterTypes = tas.subList(1, tas.size());
        
        PositionalArgumentList positionalArgumentList = invocation.getPositionalArgumentList();
        final java.util.List<Tree.Expression> argumentExpressions = new ArrayList<Tree.Expression>(tas.size());
        for (Tree.PositionalArgument argument : positionalArgumentList.getPositionalArguments()) {
            argumentExpressions.add(argument.getExpression());
        }
        this.argumentExpressions = argumentExpressions;
        this.parameterTypes = parameterTypes;
        this.spread = positionalArgumentList.getEllipsis() != null;
        
        if(positionalArgumentList.getComprehension() != null) {
            this.comprehension = positionalArgumentList.getComprehension();
        }else{
            this.comprehension = null;
        }
    }
    
    @Override
    protected void compute(){
        // don't try to work on broken stuff
        if (!validNumberOfParameters()) {
            return;
        }
        super.compute();
    }
    
    private boolean validNumberOfParameters() {
        int argumentCount = argumentExpressions.size();
        // check that we have enough
        if(argumentCount < minimumParameters)
            return false;
        // check that we don't have too many
        if(variadic)
            return true;
        return argumentCount <= parameterTypes.size();
    }

    @Override
    protected JCExpression makeInvocation(List<JCExpression> argExprs) {
        // don't try to work on broken stuff
        if (!validNumberOfParameters()) {
            return gen.makeErroneous(node, "Invalid number of parameters");
        }
        return super.makeInvocation(argExprs);
    }
    
    @Override
    protected boolean isParameterSequenced(int argIndex) {
        return variadic && argIndex >= parameterTypes.size() - 1;
    }

    @Override
    protected ProducedType getParameterType(int argIndex) {
        // in the Java code, all Callable.call() params are of type Object so let's not
        // pretend they are typed, this saves a lot of casting.
        // except for sequenced parameters where we do care about the iterated type
        if(isParameterSequenced(argIndex)){
            return parameterTypes.get(parameterTypes.size()-1);
        }
        return gen.typeFact().getObjectDeclaration().getType();
    }

    @Override
    protected JCExpression getParameterExpression(int argIndex) {
        return gen.naming.makeQuotedIdent("arg" + argIndex);
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
        return argumentExpressions.size() + (comprehension != null ? 1 : 0);
    }

    @Override
    protected boolean dontBoxSequence() {
        return comprehension != null || spread;
    }

    @Override
    protected Tree.Expression getArgumentExpression(int argIndex) {
        return argumentExpressions.get(argIndex);
    }
    
    @Override
    protected JCExpression getTransformedArgumentExpression(int argIndex) {
        if (argIndex == argumentExpressions.size() && comprehension != null) {
            ProducedType type = getParameterType(argIndex);
            return gen.expressionGen().comprehensionAsSequential(comprehension, type); 
        }
        Tree.Expression expr = getArgumentExpression(argIndex);
        if (expr.getTerm() instanceof FunctionArgument) {
            FunctionArgument farg = (FunctionArgument)expr.getTerm();
            return gen.expressionGen().transform(farg);
        }
        return transformArg(argIndex);
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
    protected JCExpression getParameterExpression(int argIndex) {
        return gen.naming.makeName(getParameter(argIndex), Naming.NA_MEMBER);
    }
    
    @Override
    protected boolean getParameterUnboxed(int argIndex) {
        return getParameter(argIndex).getUnboxed();
    }
    
    @Override
    protected BoxingStrategy getParameterBoxingStrategy(int argIndex) {
        Parameter param = getParameter(argIndex);
        if (onValueType && Decl.isValueTypeDecl(param)) {
            return BoxingStrategy.UNBOXED;
        }
        return CodegenUtil.getBoxingStrategy(param);
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
            ProducedType type = getParameterType(argIndex);
            return gen.expressionGen().comprehensionAsSequential(positional.getComprehension(), type); 
        }
        Tree.Expression expr = getArgumentExpression(argIndex);
        if (expr.getTerm() instanceof FunctionArgument) {
            FunctionArgument farg = (FunctionArgument)expr.getTerm();
            return gen.expressionGen().transform(farg);
        }
        return transformArg(argIndex);
    }
    protected Parameter getParameter(int argIndex) {
        // last parameter can be a comprehension which is not counted as part of positional arguments
        if(argIndex == positional.getPositionalArguments().size() && positional.getComprehension() != null)
            return parameters.get(argIndex);
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
    
    @Override
    protected boolean isParameterRaw(int argIndex){
        Parameter param = getParameter(argIndex);
        ProducedType type = param.getType();
        return type == null ? false : type.isRaw();
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
    
    static Declaration unaliasedPrimaryDeclaration(Tree.InvocationExpression invocation) {
        Declaration declaration = ((Tree.MemberOrTypeExpression)invocation.getPrimary()).getDeclaration();
        if (declaration instanceof ClassAlias) {
            declaration = ((ClassAlias) declaration).getExtendedTypeDeclaration();
        }
        return declaration;
    }
    
    private Tree.ClassOrInterface sub;
    
    SuperInvocationBuilder(AbstractTransformer gen,
            Tree.ClassOrInterface sub,
            Tree.InvocationExpression invocation,
            ParameterList parameterList) {
        super(gen, 
                invocation.getPrimary(), 
                unaliasedPrimaryDeclaration(invocation),
                ((Tree.MemberOrTypeExpression)invocation.getPrimary()).getTarget(),
                invocation,
                parameterList.getParameters());
        this.sub = sub;
    }
    @Override
    protected JCExpression makeInvocation(List<JCExpression> args) {
        gen.at(node);
        JCExpression expr = null;
        if (Strategy.generateInstantiator(primaryDeclaration)
                && primaryDeclaration.getContainer() instanceof Interface) {
            // If the subclass is inner to an interface then it will be 
            // generated inner to the companion and we need to qualify the 
            // super(), *unless* the subclass is nested within the same 
            // interface as it's superclass.
            Scope outer = sub.getDeclarationModel().getContainer();
            while (!(outer instanceof Package)) {
                if (outer == primaryDeclaration.getContainer()) {
                    expr = gen.naming.makeSuper();
                    break;
                }
                outer = outer.getContainer();
            }
            if (expr == null) {
                expr = gen.naming.makeQualifiedSuper(gen.naming.makeCompanionFieldName((Interface)primaryDeclaration.getContainer()));
            }
        } else {
            expr = gen.naming.makeSuper();
        }
        JCExpression result = gen.make().Apply(List.<JCExpression> nil(), expr, args);
        return result;
    }
}


/**
 * InvocationBuilder for constructing the invocation of a method reference 
 * used when implementing {@code Callable.call()}.
 * 
 * This will be used when you do:
 * <p>
 * <code>
 * void f(){
 *   value callable = f;
 * }
 * </code>
 * </p>
 * And will generate the code required to put inside the Callable's {@code $call} method to
 * invoke {@code f}: {@code f();}. The generation of the Callable or its methods is not done here.
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
        ProducedType argType = gen.getParameterTypeOfCallable(primary.getTypeModel(), argIndex);
        // FIXME
        return null;//CallableBuilder.unpickCallableParameter(gen, producedReference, null, argType, argIndex, functionalParameters.size());
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
 * InvocationBuilder for methods specified with a method reference. This builds the specifier invocation
 * within the body of the specified method.
 * 
 * For example for {@code void foo(); foo = f;} we generate: {@code f()} that you would then place into
 * the generated method for {@code foo}.
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
        JCExpression result = getParameterExpression(argIndex);
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
 * InvocationBuilder for methods specified eagerly with a Callable. This builds the Callable invocation
 * within the body of the specified method.
 * 
 * For example for {@code void foo(); foo = f;} we generate: {@code f.$call()} that you would then place into
 * the generated method for {@code foo}.
 */
class CallableSpecifierInvocationBuilder extends InvocationBuilder {
    
    private final Method method;
    private final JCExpression callable;
    public CallableSpecifierInvocationBuilder(
            AbstractTransformer gen, 
            Method method,
            JCExpression callableExpr,
            Node node) {
        super(gen, null, null, method.getType(), node);
        this.callable = callableExpr;
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
            
            JCExpression result = gen.naming.makeName(parameter, Naming.NA_MEMBER);
            
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
        JCExpression result = gen.make().Apply(primaryTypeArguments, gen.naming.makeQuotedQualIdent(callable, Naming.getCallableMethodName()), args);
        if(handleBoxing)
            result = gen.expressionGen().applyErasureAndBoxing(result, returnType, 
                    !unboxed, boxingStrategy, returnType);
        return result;
    }
}

/**
 * InvocationBuilder for 'normal' method and initializer invocations
 * using named arguments.
 */
class NamedArgumentInvocationBuilder extends InvocationBuilder {
    
    private final Tree.NamedArgumentList namedArgumentList;
    private final ListBuffer<JCStatement> vars = ListBuffer.lb();
    private final Naming.SyntheticName callVarName;
    private final Naming.SyntheticName varBaseName;
    private final Set<String> argNames = new HashSet<String>();
    private final TreeMap<Integer, Naming.SyntheticName> argsNamesByIndex = new TreeMap<Integer, Naming.SyntheticName>();
    private final Set<Parameter> bound = new HashSet<Parameter>();
    private final java.util.List<Naming.SyntheticName> sequencedElements = new LinkedList<Naming.SyntheticName>();
    private ProducedReference producedReference;
    private int sequencedParamIndex = 0;
    
    public NamedArgumentInvocationBuilder(
            AbstractTransformer gen, Tree.Primary primary,
            Declaration primaryDeclaration,
            ProducedReference producedReference,
            Tree.InvocationExpression invocation) {
        super(gen, primary, primaryDeclaration, invocation.getTypeModel(), invocation);
        this.producedReference = producedReference;
        namedArgumentList = invocation.getNamedArgumentList();
        varBaseName = gen.naming.alias("arg");
        callVarName = varBaseName.suffixedBy("$callable$");
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
        for (Naming.SyntheticName argName : this.argsNamesByIndex.values()) {
            appendArgument(argName.makeIdent());
        }
        if (hasDefaulted 
                && !Strategy.defaultParameterMethodStatic(primaryDeclaration)
                && !Strategy.defaultParameterMethodOnOuter(primaryDeclaration)) {
            vars.prepend(makeThis());
        }
    }
    
    private JCExpression makeDefaultedArgumentMethodCall(Parameter param) {
        JCExpression thisExpr = null;
        switch (Strategy.defaultParameterMethodOwner(param)) {
        case SELF:
        case STATIC:
        case OUTER:
            break;
        case OUTER_COMPANION:
            thisExpr = callVarName.makeIdent();
            break;
        case INIT_COMPANION:
            thisExpr = varBaseName.suffixedBy("$this$").makeIdent();
            if (onValueType) {
                thisExpr = gen.boxType(thisExpr, qmePrimary.getTypeModel());
            }
            break;
        }
        JCExpression defaultValueMethodName = gen.naming.makeDefaultedParamMethod(thisExpr, param);
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
            names.append(varBaseName.suffixedBy("$this$").makeIdent());
        }
        final int parameterIndex = parameterIndex(param);
        for (int ii = 0; ii < parameterIndex; ii++) {
            names.append(this.argsNamesByIndex.get(ii).makeIdent());
        }
        return names.toList();
    }
    
    /** Generates the argument name; namedArg may be null if no  
     * argument was given explicitly */
    private Naming.SyntheticName argName(Parameter param) {
        final int paramIndex = parameterIndex(param);
        //if (this.argNames.isEmpty()) {
            //this.argNames.addAll(Collections.<String>nCopies(parameterList(param).size(), null));
        //}
        String suffix = "$" + paramIndex;
        if(param.isSequenced())
            suffix += "$" + sequencedParamIndex ++;
        final Naming.SyntheticName argName = varBaseName.suffixedBy(suffix);
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
        if(declaredParam == null)
            return pt;
        ProducedType paramType = gen.getTypeForParameter(declaredParam, producedReference, flags);
        if(declaredParam.isSequenced())
            paramType = gen.typeFact().getSequentialElementType(paramType);
        return paramType;
    }
    
    private void appendVarsForNamedArguments(
            java.util.List<Tree.NamedArgument> namedArguments,
            java.util.List<Parameter> declaredParams) {
        // Assign vars for each named argument given
        for (Tree.NamedArgument namedArg : namedArguments) {
            gen.at(namedArg);
            Parameter declaredParam = namedArg.getParameter();
            Naming.SyntheticName argName = argName(declaredParam);
            ListBuffer<JCStatement> statements;
            if (namedArg instanceof Tree.SpecifiedArgument) {             
                Tree.SpecifiedArgument specifiedArg = (Tree.SpecifiedArgument)namedArg;
                Tree.Expression expr = specifiedArg.getSpecifierExpression().getExpression();
                ProducedType type = parameterType(declaredParam, expr.getTypeModel(), gen.TP_TO_BOUND);
                final BoxingStrategy boxType = getNamedParameterBoxingStrategy(declaredParam);
                // we can't just generate types like Foo<?> if the target type param is not raw because the bounds will
                // not match, so we go raw
                int flags = JT_RAW;
                if(boxType == BoxingStrategy.BOXED)
                    flags |= JT_TYPE_ARGUMENT;
                JCExpression typeExpr = gen.makeJavaType(type, flags);
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
                        methodArg.getParameterLists().get(0),
                        gen.classGen().transformMplBody(methodArg.getParameterLists(), model, body));
                JCNewClass callable = callableBuilder.build();
                JCExpression typeExpr = gen.makeJavaType(callableType);
                JCVariableDecl varDecl = gen.makeVar(argName, typeExpr, callable);
                
                statements = ListBuffer.<JCStatement>of(varDecl);
            } else if (namedArg instanceof Tree.ObjectArgument) {
                Tree.ObjectArgument objectArg = (Tree.ObjectArgument)namedArg;
                List<JCTree> object = gen.classGen().transformObjectArgument(objectArg);
                // No need to worry about boxing (it cannot be a boxed type) 
                JCVariableDecl varDecl = gen.makeLocalIdentityInstance(argName.getName(), Naming.quoteClassName(objectArg.getIdentifier().getText()), false);
                statements = toStmts(objectArg, object).append(varDecl);
            } else if (namedArg instanceof Tree.AttributeArgument) {
                Tree.AttributeArgument attrArg = (Tree.AttributeArgument)namedArg;
                final Getter model = attrArg.getDeclarationModel();
                final String name = model.getName();
                final Naming.SyntheticName alias = gen.naming.alias(name);
                final List<JCTree> attrClass = gen.gen().transformAttribute(model, alias.getName(), alias.getName(), attrArg.getBlock(), null, null);
                ProducedTypedReference typedRef = gen.getTypedReference(model);
                ProducedTypedReference nonWideningTypedRef = gen.nonWideningTypeDecl(typedRef);
                ProducedType nonWideningType = gen.nonWideningType(typedRef, nonWideningTypedRef);
                ProducedType type = parameterType(declaredParam, model.getType(), 0);
                final BoxingStrategy boxType = getNamedParameterBoxingStrategy(declaredParam);
                JCExpression initValue = gen.make().Apply(null, 
                        gen.makeSelect(alias.makeIdent(), Naming.getGetterName(model)),
                        List.<JCExpression>nil());
                initValue = gen.expressionGen().applyErasureAndBoxing(
                        initValue, 
                        nonWideningType, 
                        !CodegenUtil.isUnBoxed(nonWideningTypedRef.getDeclaration()),
                        boxType,
                        type);
                JCTree.JCVariableDecl var = gen.make().VarDef(
                        gen.make().Modifiers(FINAL, List.<JCAnnotation>nil()), 
                        argName.asName(), 
                        gen.makeJavaType(type, boxType==BoxingStrategy.BOXED ? JT_NO_PRIMITIVES : 0), 
                        initValue);
                statements = toStmts(attrArg, attrClass).append(var);
            } else {
                throw new RuntimeException("" + namedArg);
            }
            bind(declaredParam, argName, statements.toList());
        }
    }
    
    private void bind(Parameter param, Naming.SyntheticName argName, List<JCStatement> statements) {
        this.vars.appendList(statements);
        this.argsNamesByIndex.put(parameterIndex(param), argName);
        this.bound.add(param);
        if(param.isSequenced()){
            this.sequencedElements.add(argName);
        }
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
        if (getNamedParameterBoxingStrategy(param) == BoxingStrategy.BOXED) {
            flags |= JT_TYPE_ARGUMENT;
        }
        ProducedType type = gen.getTypeForParameter(param, producedReference, gen.TP_TO_BOUND);
        Naming.SyntheticName argName = argName(param);
        JCExpression typeExpr = gen.makeJavaType(type, flags);
        JCVariableDecl varDecl = gen.makeVar(argName, typeExpr, argExpr);
        bind(param, argName, List.<JCStatement>of(varDecl));
    }
    
    private BoxingStrategy getNamedParameterBoxingStrategy(Parameter param) {
        if (param != null) {
            if (onValueType && Decl.isValueTypeDecl(param)) {
                return BoxingStrategy.UNBOXED;
            }
            return CodegenUtil.getBoxingStrategy(param);
        } else {
            return BoxingStrategy.UNBOXED;
        }
    }
    
    private boolean appendVarsForDefaulted(java.util.List<Parameter> declaredParams) {
        boolean hasDefaulted = false;
        if (!Decl.isOverloaded(primaryDeclaration)) {
            // append any arguments for defaulted parameters
            for (Parameter param : declaredParams) {
                if (bound.contains(param) && !param.isSequenced()) {
                    continue;
                }
                final JCExpression argExpr;
                if (param.isDefaulted()) {
                    argExpr = makeDefaultedArgumentMethodCall(param);
                    hasDefaulted |= true;
                } else if (param.isSequenced()) {
                    if (!sequencedElements.isEmpty()) {
                        ListBuffer<JCExpression> sequencedExpressions = new ListBuffer<JCExpression>();
                        for(SyntheticName name : sequencedElements){
                            sequencedExpressions.append(name.makeIdent());
                        }
                        argExpr = gen.makeSequenceRaw(sequencedExpressions.toList());
                    } else if (namedArgumentList.getComprehension() != null) {
                        argExpr = gen.expressionGen().comprehensionAsSequential(namedArgumentList.getComprehension(), param.getType());
                    } else {
                        if (primaryDeclaration instanceof FunctionalParameter) {
                            // honestly I don't know if it needs a cast but it can't hurt
                            argExpr = gen.makeEmptyAsSequential(true);
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
                thisType = gen.makeJavaType(target.getQualifyingType(), JT_NO_PRIMITIVES);
                defaultedParameterInstance = gen.naming.makeThis();
            } else {
                // a local or toplevel function
                thisType = gen.naming.makeName((TypedDeclaration)primaryDeclaration, Naming.NA_WRAPPER);
                defaultedParameterInstance = gen.naming.makeName((TypedDeclaration)primaryDeclaration, Naming.NA_MEMBER);
            }
        } else if (primary instanceof Tree.BaseTypeExpression
                || primary instanceof Tree.QualifiedTypeExpression) {
            Map<TypeParameter, ProducedType> typeA = target.getTypeArguments();
            ListBuffer<JCExpression> typeArgs = ListBuffer.<JCExpression>lb();
            for (TypeParameter tp : ((TypeDeclaration)target.getDeclaration()).getTypeParameters()) {
                ProducedType producedType = typeA.get(tp);
                typeArgs.append(gen.makeJavaType(producedType, JT_TYPE_ARGUMENT));
            }
            ClassOrInterface declaration = (ClassOrInterface)((Tree.MemberOrTypeExpression) primary).getDeclaration();
            thisType = gen.makeJavaType(declaration.getType(), JT_COMPANION);
            defaultedParameterInstance = gen.make().NewClass(
                    null, 
                    null,
                    gen.makeJavaType(declaration.getType(), JT_COMPANION), 
                    List.<JCExpression>nil(), null);
        } else {
            if (onValueType) {
                thisType = gen.makeJavaType(target.getQualifyingType());
            } else {
                thisType = gen.makeJavaType(target.getQualifyingType(), JT_NO_PRIMITIVES);
            }
            defaultedParameterInstance = callVarName.makeIdent();
        }
        JCVariableDecl thisDecl = gen.makeVar(varBaseName.suffixedBy("$this$"), 
                thisType, 
                defaultedParameterInstance);
        return thisDecl;
    }
    
    @Override
    protected JCExpression transformInvocation(JCExpression primaryExpr, String selector, List<JCExpression> argExprs) {
        JCExpression resultExpr = super.transformInvocation(primaryExpr, selector, argExprs);
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
            if (onValueType) {
                varType = gen.makeJavaType(type);
            } else {
                if (primary instanceof QualifiedTypeExpression
                        && (((QualifiedTypeExpression)primary).getPrimary() instanceof Tree.Outer)) {
                    varType = gen.makeJavaType(type, JT_NO_PRIMITIVES | JT_COMPANION);
                } else {
                    varType = gen.makeJavaType(type, JT_NO_PRIMITIVES);
                }
            }
            vars.prepend(gen.makeVar(callVarName, varType, actualPrimExpr));
            actualPrimExpr = callVarName.makeIdent();
        }
        return actualPrimExpr;
    }
}