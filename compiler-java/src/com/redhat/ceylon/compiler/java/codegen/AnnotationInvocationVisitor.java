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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.redhat.ceylon.compiler.java.codegen.AbstractTransformer.BoxingStrategy;
import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Functional;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCAssign;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCNewArray;
import com.sun.tools.javac.util.ListBuffer;

class AnnotationInvocationVisitor extends Visitor {

    public static Class annoClass(Tree.InvocationExpression invocation) {
        Declaration declaration = ((Tree.BaseMemberOrTypeExpression)invocation.getPrimary()).getDeclaration();
        if (declaration instanceof Method) {
            return (Class)((AnnotationInvocation)((Method)declaration).getAnnotationConstructor()).getPrimary();
        } else if (declaration instanceof Class) {
            return (Class)declaration;
        } else {
            throw Assert.fail();
        }
    }
    
    public static Method annoCtor(Tree.InvocationExpression invocation) {
        Declaration declaration = ((Tree.BaseMemberOrTypeExpression)invocation.getPrimary()).getDeclaration();
        if (declaration instanceof Method) {
            return (Method)declaration;
        } else if (declaration instanceof Class) {
            return null;
        } else {
            throw Assert.fail();
        }
    }
    
    public static AnnotationInvocation annoCtorModel(Tree.InvocationExpression invocation) {
        Declaration declaration = ((Tree.BaseMemberOrTypeExpression)invocation.getPrimary()).getDeclaration();
        if (declaration instanceof Method) {
            return (AnnotationInvocation)((Method)declaration).getAnnotationConstructor();
        } else if (declaration instanceof Class) {
            // TODO Why doesn't the AnnotationModelVisitor do this? I guess because
            // an annotation Class's doesn't have a body, so there's no need for Visitor
            // But it could have defaulted arguments
            AnnotationInvocation in = new AnnotationInvocation();
            in.setPrimary((Class)declaration);
            java.util.List<AnnotationArgument> args = new ArrayList<>();
            for (Parameter p : ((Class)declaration).getParameterList().getParameters()) {
                AnnotationArgument arg = new AnnotationArgument();
                arg.setParameter(p);
                ParameterAnnotationTerm term = new ParameterAnnotationTerm();
                term.setSourceParameter(p);
                term.setSpread(false);
                arg.setTerm(term);
                args.add(arg);
            }
            in.getAnnotationArguments().addAll(args);
            in.setInterop(false);
            in.setConstructorDeclaration(null);
            return in;
        } else {
            throw Assert.fail();
        }
    }
    
    private final Node errorNode;
    private final ExpressionTransformer exprGen;
    private final AnnotationInvocation anno;
    
    private ListBuffer<JCExpression> arrayExprs = null;
    private Parameter parameter;
    private ProducedType expectedType;
    private ListBuffer<JCExpression> annotationArguments = ListBuffer.lb();

    private AnnotationInvocationVisitor(
            ExpressionTransformer expressionTransformer, Node errorNode, AnnotationInvocation anno) {
        this.exprGen = expressionTransformer;
        this.errorNode = errorNode;
        this.anno = anno;
    }
    
    public static JCAnnotation transform(ExpressionTransformer expressionTransformer, Tree.InvocationExpression invocation) {
        AnnotationInvocationVisitor visitor = new AnnotationInvocationVisitor(expressionTransformer, invocation, annoCtorModel(invocation));
        visitor.visit(invocation);
        return (JCAnnotation) ((JCAssign) visitor.annotationArguments.first()).rhs;
    }
    
    public void visit(Tree.InvocationExpression invocation) {
        // Is it a class instantiation or a constructor invocation?
        Tree.Primary primary = invocation.getPrimary();
        if (primary instanceof Tree.BaseMemberExpression) {
            Tree.BaseMemberExpression ctor = (Tree.BaseMemberExpression)primary;
            if (!Decl.isAnnotationConstructor(ctor.getDeclaration())) {
                append(exprGen.makeErroneous(primary, "Not an annotation constructor"));
            }
            append(transformConstructor(exprGen, invocation));
        } else if (primary instanceof Tree.BaseTypeExpression) {
            Tree.BaseTypeExpression bte = (Tree.BaseTypeExpression)primary;    
            if (!Decl.isAnnotationClass(bte.getDeclaration())) {
                append(exprGen.makeErroneous(primary, "Not an annotation class"));
            }
            append(transformInstantiation(exprGen, invocation));
        } else {
            append(exprGen.makeErroneous(primary, "Not an annotation constructor or annotation class"));
        }        
    }
    
    private static JCAnnotation transformInstantiation(ExpressionTransformer exprGen, Tree.InvocationExpression invocation) {
        AnnotationInvocation ai = annoCtorModel(invocation);
        AnnotationInvocationVisitor visitor = new AnnotationInvocationVisitor(exprGen, invocation, annoCtorModel(invocation));
        if (invocation.getPositionalArgumentList() != null) {
            for (Tree.PositionalArgument arg : invocation.getPositionalArgumentList().getPositionalArguments()) {
                visitor.parameter = arg.getParameter();
                arg.visit(visitor);
            }
        } 
        if (invocation.getNamedArgumentList() != null) {
            for (Tree.NamedArgument arg : invocation.getNamedArgumentList().getNamedArguments()) {
                visitor.parameter = arg.getParameter();
                arg.visit(visitor);
            }
        }
        return exprGen.at(invocation).Annotation(
                ai.makeAnnotationType(exprGen),
                visitor.annotationArguments.toList());
    }
    
    public static JCAnnotation transformConstructor(ExpressionTransformer exprGen, Tree.InvocationExpression invocation) {
        AnnotationInvocation ai = annoCtorModel(invocation);
        return transformConstructor(exprGen, invocation, ai, com.sun.tools.javac.util.List.<AnnotationFieldName>nil());
    }
    
    private static JCAnnotation transformConstructor(
            ExpressionTransformer exprGen,
            Tree.InvocationExpression invocation, AnnotationInvocation ai, 
            com.sun.tools.javac.util.List<AnnotationFieldName> fieldPath) {
        ListBuffer<JCExpression> args = ListBuffer.<JCExpression>lb();
        
        List<Parameter> unbound = new ArrayList<Parameter>(ai.getClassParameters());
        outer: for (AnnotationArgument argument : ai.getAnnotationArguments()) {
            for (Parameter classParameter : ai.getClassParameters()) {
                if (classParameter.equals(argument.getParameter())) {
                    unbound.remove(classParameter);
                    args.appendList(transformConstructorArgument(exprGen, invocation, classParameter, argument, fieldPath));
                    continue outer;
                }
            }
        }
        outer2: for (Parameter classParameter : unbound) {
            // We didn't find an argument for that class parameter
            // If the callee is a ctor it might have a static or defaulted argument
            for (AnnotationArgument argument : ai.getAnnotationArguments()) {
                if (argument.getTerm() instanceof LiteralAnnotationTerm) {
                    // literal argument case
                    args.appendList(transformConstructorArgument(exprGen, invocation, classParameter, argument, fieldPath));
                    continue outer2;
                }
                // TODO What about term interanceof InvocationAnnotationTerm
                // TODO What about term interanceof ParameterAnnotationTerm (I think this is illegal)
            }
            
            // Defaulted argument
            if (ai.getPrimary() instanceof Method) {
                Method ac2 = (Method)ai.getPrimary();
                AnnotationInvocation i = (AnnotationInvocation)ac2.getAnnotationConstructor();
                for (AnnotationArgument aa : i.getAnnotationArguments()) {
                    if (aa.getParameter().equals(classParameter)) {
                        //visitor.parameter = classParameter;
                        //visitor.append(aa.getTerm().makeAnnotationArgumentValue(exprGen, i, com.sun.tools.javac.util.List.<AnnotationFieldName>of(aa)));
                        args.append(
                                i.makeAnnotationArgument(exprGen, i, classParameter, 
                                com.sun.tools.javac.util.List.<AnnotationFieldName>of(aa), aa.getTerm()));
                    }
                }
            }
            // TODO What about primary instanceof Class?
        }
        JCAnnotation annotation = exprGen.at(invocation).Annotation(
                ai.makeAnnotationType(exprGen),
                args.toList());
        return annotation;
    }
    
    public static ListBuffer<JCExpression> transformConstructorArgument(
            ExpressionTransformer exprGen,
            Tree.InvocationExpression invocation,
            Parameter classParameter, AnnotationArgument argument, 
            com.sun.tools.javac.util.List<AnnotationFieldName> fieldPath) {
        AnnotationInvocation anno = annoCtorModel(invocation);
        AnnotationInvocationVisitor visitor = new AnnotationInvocationVisitor(exprGen, invocation, anno);
        visitor.parameter = classParameter;
        AnnotationTerm term = argument.getTerm();
        if (term instanceof ParameterAnnotationTerm) {
            ParameterAnnotationTerm parameterArgument = (ParameterAnnotationTerm)term;
            Parameter sp = parameterArgument.getSourceParameter();
            int argumentIndex = ((Functional)sp.getDeclaration())
                    .getParameterLists().get(0).getParameters()
                    .indexOf(sp);
            if (invocation.getPositionalArgumentList() != null) {
                java.util.List<Tree.PositionalArgument> positionalArguments = invocation.getPositionalArgumentList().getPositionalArguments();
                
                if (parameterArgument.isSpread()) {
                    visitor.transformSpreadArgument(positionalArguments.subList(argumentIndex, positionalArguments.size()), classParameter);
                } else {
                    if (0 <= argumentIndex && argumentIndex < positionalArguments.size()) {
                        Tree.PositionalArgument pargument = positionalArguments.get(argumentIndex);
                        if (pargument.getParameter().isSequenced()) {
                            visitor.transformVarargs(argumentIndex, positionalArguments);
                        } else {
                            visitor.transformArgument(pargument);
                        }
                    } else if (sp.isDefaulted()) {
                        visitor.makeDefaultExpr(invocation, parameterArgument, sp);
                    } else if (sp.isSequenced()) {
                        visitor.appendBuiltArray(visitor.startArray());
                    }
                }
            } else if (invocation.getNamedArgumentList() != null) {
                if (parameterArgument.isSpread()) {
                    visitor.append(exprGen.makeErroneous(invocation, "Spread argument with named invocation not supported"));
                } else {
                    boolean found = false;
                    for (Tree.NamedArgument na : invocation.getNamedArgumentList().getNamedArguments()) {
                        Parameter parameter = na.getParameter();
                        int parameterIndex = anno.indexOfConstructorParameter(parameter);
                        if (parameterIndex == argumentIndex) {
                            visitor.transformArgument(na);
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        if (sp.isDefaulted()) {
                            visitor.makeDefaultExpr(invocation, parameterArgument, sp);
                        } else if (sp.isSequenced()) {
                            visitor.appendBuiltArray(visitor.startArray());
                        }else {
                            visitor.append(exprGen.makeErroneous(invocation, "Unable to find argument"));
                        }
                    }    
                }
            }
        } else if (term instanceof LiteralAnnotationTerm) {
            visitor.append(term.makeAnnotationArgumentValue(visitor.exprGen, visitor.anno, fieldPath.append(argument)));
        } else if (term instanceof InvocationAnnotationTerm) {
            AnnotationInvocation instantiation = ((InvocationAnnotationTerm)term).getInstantiation();
            visitor.append(transformConstructor(visitor.exprGen, invocation, instantiation, fieldPath.append(argument)));
        } else {
            visitor.append(visitor.exprGen.makeErroneous(invocation, "Unable to find argument"));
        }
        return visitor.annotationArguments;
    }
    
    
    private void makeDefaultExpr(Tree.InvocationExpression invocation,
            ParameterAnnotationTerm parameterArgument, Parameter sp) {
        // Use the default parameter from the constructor
        if (exprGen.isCeylonBasicType(sp.getType())) {
            JCExpression expr = parameterArgument.makePrimitiveDefaultExpr(exprGen, anno,
                    Collections.singletonList((AnnotationFieldName)parameterArgument));
            if (expr != null) {
                append(expr);
            }
        } else if (Decl.isAnnotationClass(sp.getType().getDeclaration())) {
            AnnotationConstructorParameter defaultedCtorParam = null;
            for (AnnotationConstructorParameter ctorParam : anno.getConstructorParameters()) {
                if (ctorParam.getParameter().equals(parameterArgument.getSourceParameter())) {
                    defaultedCtorParam = ctorParam;
                    break;
                }
            }
            if (defaultedCtorParam != null) {
                InvocationAnnotationTerm defaultedInvocation = (InvocationAnnotationTerm)defaultedCtorParam.getDefaultArgument();
                append(transformConstructor(exprGen, invocation, defaultedInvocation.getInstantiation(), 
                        com.sun.tools.javac.util.List.<AnnotationFieldName>of(defaultedCtorParam)));
            } else {
                append(exprGen.makeErroneous(invocation, "Couldn't find defaulted parameter for " + anno.getConstructorDeclaration().getName()));
            }
        }
    }
    
    private void transformVarargs(int argumentIndex,
            java.util.List<Tree.PositionalArgument> pa) {
        ListBuffer<JCExpression> prevCollect = startArray();
        try {
            for (int jj = argumentIndex; jj < pa.size(); jj++) {
                transformArgument(pa.get(jj));
            }
        } finally {
            appendBuiltArray(prevCollect);
        }
    }

    /**
     * If we're currently constructing an array then or append the given expression
     * Otherwise make an annotation argument for given expression and 
     * append it to the annotation arguments,
     */
    private void append(JCExpression expr) {
        if (arrayExprs != null) {
            arrayExprs.append(expr);
        } else {
            JCExpression memberName;
            if (this.parameter != null) {
                memberName = exprGen.naming.makeUnquotedIdent(
                Naming.selector(this.parameter.getModel(), Naming.NA_ANNOTATION_MEMBER));
            } else {
                memberName = exprGen.makeErroneous(errorNode);
            }
            annotationArguments.append(exprGen.make().Assign(
                    memberName, expr));
        }
    }

    private void transformSpreadArgument(java.util.List<Tree.PositionalArgument> arguments, Parameter classParameter) {
        boolean varargs = classParameter.isSequenced()
                && arguments.size() > 1 || arguments.size() == 0;
        ListBuffer<JCExpression> prevCollect = varargs ? startArray() : null;
        try {
            for (Tree.PositionalArgument arg : arguments) {
                exprGen.at(arg); 
                arg.visit(this);
            }
        } finally {
            if (varargs) {
                appendBuiltArray(prevCollect);
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
    
    public void visit(Tree.NegativeOp term) {
        if (term.getTerm() instanceof Tree.NaturalLiteral
                || term.getTerm() instanceof Tree.FloatLiteral) {
            append(exprGen.transformExpression(term, BoxingStrategy.UNBOXED, expectedType()));
        } else {
            append(exprGen.makeErroneous(term, "Unable to use that kind of term " + term.getClass().getSimpleName()));
        }
    }
    
    public void visit(Tree.Literal term) {
        append(exprGen.transformExpression(term, BoxingStrategy.UNBOXED, expectedType()));
    }

    private ProducedType expectedType() {
        return this.expectedType != null ?  this.expectedType : this.parameter.getType();
    }
    
    public void visit(Tree.BaseMemberExpression term) {
        if (exprGen.isBooleanTrue(term.getDeclaration())
                || exprGen.isBooleanFalse(term.getDeclaration())) {
            append(exprGen.transformExpression(term, BoxingStrategy.UNBOXED, term.getTypeModel()));
        } else if (anno.isInterop()) {
            ProducedType type = term.getTypeModel();
            Module jdkBaseModule = exprGen.loader().getJDKBaseModule();
            Package javaLang = jdkBaseModule.getPackage("java.lang");
            TypeDeclaration enumDecl = (TypeDeclaration)javaLang.getDirectMember("Enum", null, false);
            if (type.isSubtypeOf(enumDecl.getProducedType(null, Collections.singletonList(type)))) {
                // A Java enum
                append(exprGen.transformExpression(term, BoxingStrategy.UNBOXED, null));
            }
        } else {
            super.visit(term);
        }
    }
    
    public void visit(Tree.MemberOrTypeExpression term) {
        // Metamodel reference
        if (anno.isInterop()) {
            append(exprGen.naming.makeQualIdent(
                    exprGen.makeJavaType(((ClassOrInterface)term.getDeclaration()).getType()),
                    "class"));
        } else {
            append(exprGen.make().Literal(term.getDeclaration().getQualifiedNameString()));
        }
    }
    
    @Override
    public void visit(Tree.TypeLiteral tl){
        // FIXME: this is all temporary
        if(tl.getType() != null && tl.getType().getTypeModel() != null){
            if (anno.isInterop()) {
                append(exprGen.naming.makeQualIdent(
                        exprGen.makeJavaType(tl.getType().getTypeModel()),
                        "class"));
            } else {
                append(exprGen.make().Literal(tl.getType().getTypeModel().resolveAliases().getProducedTypeQualifiedName()));
            }
        }
    }
    
    @Override
    public void visit(Tree.MemberLiteral tl){
        // FIXME: this is all temporary
        append(exprGen.make().Literal(tl.getDeclaration().getQualifiedNameString()));
    }
    
    private ListBuffer<JCExpression> startArray() {
        ListBuffer<JCExpression> prevArray = arrayExprs;
        arrayExprs = ListBuffer.<JCExpression>lb();
        expectedType = exprGen.typeFact().getIteratedType(parameter.getType());
        return prevArray;
    }
    
    private JCNewArray endArray(ListBuffer<JCExpression> prevArray) {
        ListBuffer<JCExpression> collected = arrayExprs;
        arrayExprs = prevArray;
        expectedType = null;
        return exprGen.make().NewArray(null,  null, collected.toList());
    }
    
    private void appendBuiltArray(ListBuffer<JCExpression> prevArray) {
        append(endArray(prevArray));
    }

    public void visit(Tree.SequenceEnumeration term) {
        ListBuffer<JCExpression> prevCollect = startArray();
        try {
            term.visitChildren(this);
        } finally {
            appendBuiltArray(prevCollect);
        }
    }
    
    public void visit(Tree.Tuple term) {
        ListBuffer<JCExpression> prevCollect = startArray();
        try {
            term.visitChildren(this);
        } finally {
            appendBuiltArray(prevCollect);
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