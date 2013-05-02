package com.redhat.ceylon.compiler.java.codegen;

import com.redhat.ceylon.compiler.java.codegen.AbstractTransformer.BoxingStrategy;
import com.redhat.ceylon.compiler.java.codegen.ClassTransformer.AnnotationInstantiationArgument;
import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.InvocationExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.PositionalArgument;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCAssign;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCNewArray;
import com.sun.tools.javac.util.ListBuffer;

class AnnotationInvocationVisitor extends Visitor {

    /**
     * 
     */
    private final ExpressionTransformer exprGen;
    private final Class annotationClass;
    private final Method annotationConstructor;
    
    private ListBuffer<JCExpression> exprs = null;
    private String parameterName;
    private ListBuffer<JCExpression> annotationArguments = ListBuffer.lb();

    public AnnotationInvocationVisitor(
            ExpressionTransformer expressionTransformer, InvocationExpression invocation) {
        exprGen = expressionTransformer;
        Declaration declaration = ((Tree.BaseMemberOrTypeExpression)invocation.getPrimary()).getDeclaration();
        if (declaration instanceof Method) {
            annotationConstructor = (Method)declaration;
            annotationClass = annotationConstructor.getAnnotationClass();
        } else if (declaration instanceof Class) {
            annotationConstructor = null;
            annotationClass  = (Class)declaration;
        } else {
            throw Assert.fail();
        }
        Assert.that(annotationClass != null, "Annotation class for invocation of " + declaration + " could not be determined");
    }
    
    public Class getAnnotationClass() {
        return annotationClass;
    }
    
    public JCAnnotation transform(Tree.InvocationExpression invocation) {
        visit(invocation);
        return (JCAnnotation) ((JCAssign) annotationArguments.first()).rhs;
    }
    
    public void visit(Tree.InvocationExpression invocation) {
        // Is it a class instantiation or a constructor invocation?
        Tree.Primary primary = invocation.getPrimary();
        if (primary instanceof Tree.BaseMemberExpression) {
            Tree.BaseMemberExpression ctor = (Tree.BaseMemberExpression)primary;    
            if (!Decl.isAnnotationConstructor(ctor.getDeclaration())) {
                append(exprGen.makeErroneous(primary, "Not an annotation constructor"));
            }
            AnnotationInvocationVisitor visitor = new AnnotationInvocationVisitor(exprGen, invocation);
            append(visitor.transformConstructor(invocation));
        } else if (primary instanceof Tree.BaseTypeExpression) {
            Tree.BaseTypeExpression bte = (Tree.BaseTypeExpression)primary;    
            if (!Decl.isAnnotationClass(bte.getDeclaration())) {
                append(exprGen.makeErroneous(primary, "Not an annotation class"));
            }
            AnnotationInvocationVisitor visitor = new AnnotationInvocationVisitor(exprGen, invocation);
            append(visitor.transformInstantiation(invocation));
        } else {
            append(exprGen.makeErroneous(primary, "Not an annotation constructor or annotation class"));
        }        
    }
    
    public JCAnnotation transformInstantiation(Tree.InvocationExpression invocation) {        
        if (invocation.getPositionalArgumentList() != null) {
            for (Tree.PositionalArgument arg : invocation.getPositionalArgumentList().getPositionalArguments()) {
                parameterName = arg.getParameter().getName();
                arg.visit(this);
            }
        } 
        if (invocation.getNamedArgumentList() != null) {
            for (Tree.NamedArgument arg : invocation.getNamedArgumentList().getNamedArguments()) {
                parameterName = arg.getParameter().getName();
                arg.visit(this);
            }
        }
        return exprGen.at(invocation).Annotation(exprGen.makeJavaType(annotationClass.getType(), ExpressionTransformer.JT_ANNOTATION), annotationArguments.toList());
    }
    
    public JCAnnotation transformConstructor(Tree.InvocationExpression invocation) {
        int[] argumentIndices = annotationConstructor.getAnnotationArguments();
        java.util.List<Parameter> classParameters = annotationClass.getParameterList().getParameters();
        for (int classParameterIndex = 0; classParameterIndex < classParameters.size(); classParameterIndex++) {
            Parameter classParameter = classParameters.get(classParameterIndex);
            parameterName = classParameter.getName();
            if (classParameterIndex >= argumentIndices.length) {
                // => We're using the annotation class's defaulted parameter
                continue;
            }
            int encodedArgument = argumentIndices[classParameterIndex];
            
            int argumentIndex = AnnotationInstantiationArgument.decode((short)encodedArgument);
            switch (AnnotationInstantiationArgument.classify((short)encodedArgument)) {
            case PARAMETER_SPREAD:
                if (invocation.getPositionalArgumentList() != null) {
                    java.util.List<PositionalArgument> pa = invocation.getPositionalArgumentList().getPositionalArguments();
                    transformSpreadArgument(pa.subList(argumentIndex, pa.size()), classParameter);
                } else if (invocation.getNamedArgumentList() != null) {
                    append(exprGen.makeErroneous(invocation, "Spread argument with named invocation not supported"));
                }
                break;
            case PARAMETER:
                if (invocation.getPositionalArgumentList() != null) {
                    java.util.List<PositionalArgument> pa = invocation.getPositionalArgumentList().getPositionalArguments();
                    PositionalArgument argument = pa.get(argumentIndex);
                    if (argument.getParameter().isSequenced()) {
                        transformVarargs(argumentIndex, pa);
                    } else {
                        transformArgument(argument);
                    }
                } else if (invocation.getNamedArgumentList() != null) {
                    boolean found = false;
                    for (Tree.NamedArgument na : invocation.getNamedArgumentList().getNamedArguments()) {
                        Parameter parameter = na.getParameter();
                        int parameterIndex = annotationConstructor.getParameterLists().get(0).getParameters().indexOf(parameter);
                        if (parameterIndex == encodedArgument) {
                            transformArgument(na);
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        append(exprGen.makeErroneous(invocation, "Unable to find argument"));
                    }
                }
                break;
            case STATIC:
                exprGen.at(invocation);
                append(exprGen.naming.makeQuotedQualIdent(
                                exprGen.naming.makeName(annotationConstructor, Naming.NA_FQ | Naming.NA_WRAPPER ),
                                parameterName));
                break;
            default:
                append(exprGen.makeErroneous(invocation, "Unable to find argument"));
            }
            
        }
        JCAnnotation annotation = exprGen.at(invocation).Annotation(exprGen.makeJavaType(annotationClass.getType(), ExpressionTransformer.JT_ANNOTATION), annotationArguments.toList());
        return annotation;
    }

    private void transformVarargs(int argumentIndex,
            java.util.List<PositionalArgument> pa) {
        ListBuffer<JCExpression> prevCollect = startArray();
        try {
            for (int jj = argumentIndex; jj < pa.size(); jj++) {
                transformArgument(pa.get(jj));
            }
        } finally {
            append(endArray(prevCollect));
        }
    }

    private void append(JCExpression expr) {
        if (exprs != null) {
            exprs.append(expr);
        } else {
            annotationArguments.append(exprGen.make().Assign(
                    exprGen.naming.makeQuotedIdent(parameterName), expr));
        }
    }

    public void transformSpreadArgument(java.util.List<PositionalArgument> arguments, Parameter classParameter) {
        boolean varargs = classParameter.isSequenced() && arguments.size() > 1;
        ListBuffer<JCExpression> prevCollect = varargs ? startArray() : null;
        try {
            for (Tree.PositionalArgument arg : arguments) {
                exprGen.at(arg); 
                arg.visit(this);
            }
        } finally {
            if (varargs) {
                append(endArray(prevCollect));
            }
        }
    }
    
    private void transformArgument(Tree.NamedArgument node) {
        exprGen.at(node);
        node.visit(this);
    }
    
    private void transformArgument(Tree.PositionalArgument node) {
        exprGen.at(node);
        node.visit(this);
    }
    
    @Override
    public void handleException(Exception e, Node node) {
        if (e instanceof RuntimeException) {
            throw (RuntimeException)e;
        } else {
            throw new RuntimeException(e);
        }
    }
    
    public void visit(Tree.Expression term) {
        term.visitChildren(this);
    }
    
    public void visit(Tree.Term term) {
        append(exprGen.makeErroneous(term, "Unable to use that kind of term " + term.getClass().getSimpleName()));
    }
    
    public void visit(Tree.Literal term) {
        append(exprGen.transformExpression(term, BoxingStrategy.UNBOXED, term.getTypeModel()));
    }
    
    public void visit(Tree.BaseMemberExpression term) {
        if (exprGen.isBooleanTrue(term.getDeclaration())
                || exprGen.isBooleanFalse(term.getDeclaration())) {
            append(exprGen.transformExpression(term, BoxingStrategy.UNBOXED, term.getTypeModel()));
        } else {
            append(exprGen.makeErroneous(term, "Unable to use that kind of term (only booleans)"));
        }
    }
    
    private ListBuffer<JCExpression> startArray() {
        ListBuffer<JCExpression> prevCollect = exprs;
        exprs = ListBuffer.<JCExpression>lb();
        return prevCollect;
    }
    
    private JCNewArray endArray(ListBuffer<JCExpression> prevCollect) {
        ListBuffer<JCExpression> collected = exprs;
        exprs = prevCollect;
        return exprGen.make().NewArray(null,  null, collected.toList());
    }
    
    // TODO Instantiation of annotation classes
    // TODO invocation of annotation constructors?
    public void visit(Tree.SequenceEnumeration term) {
        ListBuffer<JCExpression> prevCollect = startArray();
        try {
            term.visitChildren(this);
        } finally {
            append(endArray(prevCollect));
        }
    }
    
    public void visit(Tree.Tuple term) {
        ListBuffer<JCExpression> prevCollect = startArray();
        try {
            term.visitChildren(this);
        } finally {
            append(endArray(prevCollect));
        }
    }
    
    public void visit(Tree.PositionalArgument arg) {
        append(exprGen.makeErroneous(arg, "Unable to find use that kind of positional argument"));
    }
    
    public void visit(Tree.ListedArgument arg) {
        arg.visitChildren(this);
    }
    
    public void visit(Tree.NamedArgument arg) {
        append(exprGen.makeErroneous(arg, "Unable to find use that kind of named argument"));
    }
    
    public void visit(Tree.SpecifiedArgument arg) {
        arg.visitChildren(this);
    }
    
}