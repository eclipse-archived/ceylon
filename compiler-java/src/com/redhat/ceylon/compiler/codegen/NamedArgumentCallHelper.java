package com.redhat.ceylon.compiler.codegen;

import java.util.ArrayList;
import java.util.Collections;

import com.redhat.ceylon.compiler.codegen.AbstractTransformer.BoxingStrategy;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ParameterList;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.BaseMemberOrTypeExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.BaseTypeExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Expression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.InvocationExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.NamedArgument;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Primary;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.QualifiedMemberOrTypeExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.QualifiedTypeExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SequencedArgument;
import com.redhat.ceylon.compiler.util.Util;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCNewClass;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCTypeParameter;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;

/**
 * <p>Builds a named argument call, which looks something like this:</p>
 * <pre>
 *    new NamedArgumentCall&lttINSTANCE_TYPE&gt;(BOX(INSTANCE), BOX(PASSED_ARGS...)) {
 *        public RESULT call$() {
 *            return this.instance.METHOD(UNBOX(POSTIONAL_ARGS));
 *        }
 *    }.UNBOX();
 * </pre>
 * @author tom
 */
class NamedArgumentCallHelper {
    
    private static final String CALL_METHOD_NAME = "call$";
    
    /**
     * 
     */
    private final ExpressionTransformer exprTransformer;
    
    /**
     * The invocation we're generating code for
     */
    private final InvocationExpression ce;
    
    /** The expression for the method, including any necessary 
     * qualification, along the lines of <code>this.instance.foo</code>. */ 
    private JCExpression method;
    
    /**
     * The instance the method is being invoked on.
     */
    private JCExpression instance;
    
    /** 
     * The type parameter <i>T</i> for (the subclass of) the 
     * <code>NamedArgumentCall&lt;T&gt;</code>, the (boxed) type of
     * {@link #instance}.  
     */
    private JCExpression synthClassTypeParam;
    
    public NamedArgumentCallHelper(ExpressionTransformer expressionTransformer, InvocationExpression ce) {
        exprTransformer = expressionTransformer;
        this.ce = ce;
        final Declaration primaryDecl = getPrimaryDeclaration();
        if (!(primaryDecl instanceof Method
                || primaryDecl instanceof com.redhat.ceylon.compiler.typechecker.model.Class)) {
            throw new RuntimeException("Illegal State: " + (primaryDecl != null ? primaryDecl.getClass() : "null"));
        }
    }
    
    private Primary getPrimary() {
        return ce.getPrimary();
    }
    
    private Declaration getPrimaryDeclaration() {
        return getPrimary().getDeclaration();
    }
    
    private String getMethodName() {
        final Declaration primaryDecl = getPrimaryDeclaration();
        if (primaryDecl instanceof Method) {
            Method methodDecl = (Method)primaryDecl;
            return methodDecl.getName();
        } else if (primaryDecl instanceof com.redhat.ceylon.compiler.typechecker.model.Class) {
            com.redhat.ceylon.compiler.typechecker.model.Class methodDecl = (com.redhat.ceylon.compiler.typechecker.model.Class)primaryDecl;
            return methodDecl.getName();
        }
        throw new RuntimeException("Illegal State: " + (primaryDecl != null ? primaryDecl.getClass() : "null"));            
    }
    
    private java.util.List<ParameterList> getParameterLists() {
        final Declaration primaryDecl = getPrimaryDeclaration();
        if (primaryDecl instanceof Method) {
            Method methodDecl = (Method)primaryDecl;
            return methodDecl.getParameterLists();
        } else if (primaryDecl instanceof com.redhat.ceylon.compiler.typechecker.model.Class) {
            com.redhat.ceylon.compiler.typechecker.model.Class methodDecl = (com.redhat.ceylon.compiler.typechecker.model.Class)primaryDecl;
            return methodDecl.getParameterLists();
        } 
        throw new RuntimeException("Illegal State: " + (primaryDecl != null ? primaryDecl.getClass() : "null"));
    }
    
    private ProducedType getTypeModel() {
        return ce.getTypeModel();
    }
    
    private SequencedArgument getSequencedArgument() {
        return ce.getNamedArgumentList().getSequencedArgument();
    }

    private java.util.List<NamedArgument> getNamedArguments() {
        return ce.getNamedArgumentList().getNamedArguments();
    }
    
    private boolean isGenerateNew() {
        return getPrimary() instanceof BaseTypeExpression
                || getPrimary() instanceof QualifiedTypeExpression;
    }
    
    private int getResultTypeSpec() {
        int spec = 0;
        if (exprTransformer.isTypeParameter(exprTransformer.determineExpressionType(ce))) {
            spec = AbstractTransformer.TYPE_ARGUMENT;
        } else if (isGenerateNew()) {
            spec = AbstractTransformer.CLASS_NEW;
        } else if (!ce.getUnboxed()) {
            spec = AbstractTransformer.NO_PRIMITIVES;
        }
        return spec;
    }

    private boolean isVoid() {
        boolean isVoid = getTypeModel().isExactly(exprTransformer.typeFact().getVoidDeclaration().getType());
        return isVoid;
    }
    
    private Expression getArgumentExpression(Tree.NamedArgument arg) {
        if (arg instanceof Tree.SpecifiedArgument) {
            Expression expr = ((Tree.SpecifiedArgument)arg).getSpecifierExpression().getExpression();
            return expr;
        } else if (arg instanceof Tree.TypedArgument) {
            throw new RuntimeException("Not yet implemented");
        } else {
            throw new RuntimeException("Illegal State");
        }
    }
    
    private JCExpression transformArgument(Tree.NamedArgument arg) {
        // named arguments get casted down the stack, so no need for erasure casts
        return exprTransformer.transformExpression(getArgumentExpression(arg));
    }

    private ProducedType getArgumentType(Tree.NamedArgument arg) {
        if (arg instanceof Tree.SpecifiedArgument) {
            return ((Tree.SpecifiedArgument)arg).getSpecifierExpression().getExpression().getTypeModel();
        } else if (arg instanceof Tree.TypedArgument) {
            throw new RuntimeException("Not yet implemented");
        } else {
            throw new RuntimeException("Illegal State");
        }
    }
    
    private ListBuffer<JCExpression> makePositionalArguments() {
        java.util.List<NamedArgument> namedArguments = getNamedArguments();
        java.util.List<Parameter> declaredParams = getParameterLists().get(0).getParameters();
        Parameter lastDeclared = declaredParams.size() > 0 ? declaredParams.get(declaredParams.size() - 1) : null;
        boolean boundSequenced = false;
        ArrayList<JCExpression> callArgsArray = new ArrayList<JCExpression>(Collections.<JCExpression>nCopies(namedArguments.size(), null));
        for (NamedArgument namedArg : namedArguments) {
            exprTransformer.at(namedArg);
            Parameter declaredParam = namedArg.getParameter();
            if (declaredParam == null 
                    || !declaredParam.getName().equals(namedArg.getIdentifier().getText())) {
                throw new RuntimeException();
            }
            if (declaredParam.isSequenced()) {
                boundSequenced = true;
            }
            int index = namedArguments.indexOf(namedArg);
            JCExpression argExpr = exprTransformer.make().Indexed(exprTransformer.makeSelect("this", "args"), exprTransformer.makeInteger(index));
            ProducedType type = declaredParam.getType();
            if (exprTransformer.isTypeParameter(type)) {
                type = getArgumentType(namedArg);
            }
            argExpr = exprTransformer.make().TypeCast(exprTransformer.makeJavaType(type, AbstractTransformer.TYPE_ARGUMENT), argExpr);
            if (Util.getBoxingStrategy(declaredParam) == BoxingStrategy.UNBOXED) {
                argExpr = exprTransformer.unboxType(argExpr, declaredParam.getType());
            }
            callArgsArray.set(declaredParams.indexOf(declaredParam), argExpr);
        }
        
        SequencedArgument sequencedArgument = getSequencedArgument();
        if (sequencedArgument != null) {
            int index = namedArguments.size();
            JCExpression argExpr = exprTransformer.make().Indexed(exprTransformer.makeSelect("this", "args"), exprTransformer.makeInteger(index));
            
            ProducedType type = lastDeclared.getType();
            argExpr = exprTransformer.make().TypeCast(exprTransformer.makeJavaType(type, AbstractTransformer.WANT_RAW_TYPE), argExpr);
            callArgsArray.add(namedArguments.size(), exprTransformer.unboxType(argExpr, type));
        } else if (lastDeclared != null 
                && lastDeclared.isSequenced() 
                && !boundSequenced) {
            callArgsArray.add(namedArguments.size(), exprTransformer.makeEmpty());
        }
        
        ListBuffer<JCExpression> positionalArguments = ListBuffer.<JCExpression>lb();
        for (JCExpression expr : callArgsArray) {
            positionalArguments.add(expr != null ? expr : exprTransformer.make().Erroneous());
        }
        return positionalArguments;
    }
    
    private ListBuffer<JCExpression> makePassedArguments() {
        ListBuffer<JCExpression> passedArguments = new ListBuffer<JCExpression>();
        for (NamedArgument namedArg : getNamedArguments()) {
            exprTransformer.at(namedArg);
            passedArguments.append(transformArgument(namedArg));
        }
        SequencedArgument sequencedArgument = getSequencedArgument();
        if (sequencedArgument != null) {
            exprTransformer.at(sequencedArgument);
            passedArguments.append(exprTransformer.makeSequenceRaw(sequencedArgument.getExpressionList().getExpressions()));   
        }
        return passedArguments;
    }

    private void initMethod() {
        exprTransformer.at(ce);
        Primary primary = getPrimary();
        if (primary instanceof BaseMemberOrTypeExpression) {
            BaseMemberOrTypeExpression memberExpr = (BaseMemberOrTypeExpression)primary;
            Declaration memberDecl = memberExpr.getDeclaration();
            if (memberDecl.isToplevel()) {
                instance = exprTransformer.makeNull();
                synthClassTypeParam = exprTransformer.makeIdent("java.lang.Void");
                method = exprTransformer.makeSelect(memberDecl.getName(), getMethodName());// TODO encapsulate this
            } else if (!memberDecl.isClassMember()) {// local
                instance = exprTransformer.makeIdent(memberDecl.getName()); // TODO Check it's as simple as this, and encapsulat
                synthClassTypeParam = exprTransformer.makeIdent(memberDecl.getName());// TODO: get the generated name somehow
                method = exprTransformer.makeSelect("this", "instance", getMethodName());
            } else {
                instance = exprTransformer.makeNull();
                synthClassTypeParam = exprTransformer.makeIdent("java.lang.Void");
                method = exprTransformer.makeIdent(getMethodName());
            }
        } else if (primary instanceof QualifiedMemberOrTypeExpression) {
            QualifiedMemberOrTypeExpression memberExpr = (QualifiedMemberOrTypeExpression)primary;
            List<JCExpression> typeArgs = exprTransformer.transformTypeArguments(ce);
            CeylonVisitor visitor = new CeylonVisitor(exprTransformer.gen(), typeArgs, makePositionalArguments());
            Primary primary2 = memberExpr.getPrimary();
            primary2.visit(visitor);
            if (visitor.hasResult()) {
                JCExpression result = (JCExpression)visitor.getSingleResult();
                result = exprTransformer.boxUnboxIfNecessary(result, primary2, primary2.getTypeModel(), BoxingStrategy.BOXED);    
                this.instance = result;
            }
            synthClassTypeParam = exprTransformer.makeJavaType(primary2.getTypeModel(), AbstractTransformer.TYPE_ARGUMENT);
            method = exprTransformer.makeSelect("this", "instance", getMethodName());
        } else {
            throw new RuntimeException("Not Implemented: Named argument calls only implemented on member and type expressions");
        }
    }
    
    private JCMethodDecl declareCall$Decl() {
        // Construct the call$() method
        MethodDefinitionBuilder callMethod = MethodDefinitionBuilder.method(exprTransformer.gen(), CALL_METHOD_NAME);
        callMethod.modifiers(Flags.PUBLIC);
        JCExpression resultType = exprTransformer.makeJavaType(getTypeModel(), getResultTypeSpec());
        callMethod.resultType(resultType);
        if (isGenerateNew()) {
            callMethod.body(exprTransformer.make().Return(exprTransformer.make().NewClass(null, null, resultType, makePositionalArguments().toList(), null)));
        } else {
            JCExpression expr = exprTransformer.make().Apply(null, method, makePositionalArguments().toList());
            if (isVoid()) {
                callMethod.body(List.<JCStatement>of(
                        exprTransformer.make().Exec(expr),
                        exprTransformer.make().Return(exprTransformer.makeNull())));
            } else {
                callMethod.body(exprTransformer.make().Return(expr));
            }
        }
        return callMethod.build();
    }
    
    private JCExpression makeSynthClassType() {
        return exprTransformer.make().TypeApply(exprTransformer.makeIdent(exprTransformer.syms().ceylonNamedArgumentCall),
                List.<JCExpression>of(synthClassTypeParam));
    }
    
    private JCClassDecl makeSynthClass() {
        return exprTransformer.make().ClassDef(exprTransformer.make().Modifiers(0),
                exprTransformer.names().empty,
                List.<JCTypeParameter>nil(),
                makeSynthClassType(),
                List.<JCExpression>nil(),
                List.<JCTree>of(declareCall$Decl()));
    }
    
    private JCNewClass newSynthClass() {
        ListBuffer<JCExpression> passedArguments = makePassedArguments();
        passedArguments.prepend(instance);
        return exprTransformer.make().NewClass(null,
                null,
                makeSynthClassType(),
                passedArguments.toList(),
                makeSynthClass());
    }

    private JCExpression applyCall$Method() {
        // Call the call$() method
        return exprTransformer.make().Apply(null,
                exprTransformer.makeSelect(newSynthClass(), CALL_METHOD_NAME), List.<JCExpression>nil());
    }
    
    public JCExpression generate() {
        initMethod();
        return applyCall$Method();
    }
}