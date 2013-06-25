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

import java.util.Collections;
import java.util.List;

import com.redhat.ceylon.compiler.java.codegen.AbstractTransformer.BoxingStrategy;
import com.redhat.ceylon.compiler.typechecker.model.AnnotationArgument;
import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Functional;
import com.redhat.ceylon.compiler.typechecker.model.LiteralAnnotationArgument;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ParameterAnnotationArgument;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.PositionalArgument;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCAssign;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCNewArray;
import com.sun.tools.javac.util.ListBuffer;

class AnnotationInvocationVisitor extends Visitor {

    private final ExpressionTransformer exprGen;
    private final Class annotationClass;
    private final Method annotationConstructor;
    
    private ListBuffer<JCExpression> exprs = null;
    private Parameter parameter;
    private ProducedType expectedType;
    private ListBuffer<JCExpression> annotationArguments = ListBuffer.lb();

    public AnnotationInvocationVisitor(
            ExpressionTransformer expressionTransformer, Tree.InvocationExpression invocation) {
        exprGen = expressionTransformer;
        Declaration declaration = ((Tree.BaseMemberOrTypeExpression)invocation.getPrimary()).getDeclaration();
        if (declaration instanceof Method) {
            annotationConstructor = (Method)declaration;
            annotationClass = (Class)annotationConstructor.getAnnotationInstantiation().getPrimary();
        } else if (declaration instanceof Class) {
            annotationConstructor = null;
            annotationClass  = (Class)declaration;
        } else {
            throw Assert.fail();
        }
        Assert.that(annotationClass != null, "Annotation class for invocation of " + declaration + " could not be determined", invocation);
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
                this.parameter = arg.getParameter();
                arg.visit(this);
            }
        } 
        if (invocation.getNamedArgumentList() != null) {
            for (Tree.NamedArgument arg : invocation.getNamedArgumentList().getNamedArguments()) {
                this.parameter = arg.getParameter();
                arg.visit(this);
            }
        }
        return exprGen.at(invocation).Annotation(makeAnnotationType(), annotationArguments.toList());
    }
    
    public JCAnnotation transformConstructor(Tree.InvocationExpression invocation) {
        
        List<AnnotationArgument> arguments = annotationConstructor.getAnnotationInstantiation().getArguments();
        java.util.List<Parameter> classParameters = annotationClass.getParameterList().getParameters();
        for (int classParameterIndex = 0; classParameterIndex < classParameters.size(); classParameterIndex++) {
            Parameter classParameter = classParameters.get(classParameterIndex);
            this.parameter = classParameter;
            if (classParameterIndex >= arguments.size()) {
                // => We're using the annotation class's defaulted parameter
                continue;
            }
            AnnotationArgument argument = arguments.get(classParameterIndex);
            
            if (argument instanceof ParameterAnnotationArgument) {
                ParameterAnnotationArgument parameterArgument = (ParameterAnnotationArgument)argument;
                int argumentIndex = ((Functional)parameterArgument.getSourceParameter().getContainer()).getParameterLists().get(0).getParameters().indexOf(parameterArgument.getSourceParameter());
                if (invocation.getPositionalArgumentList() != null) {
                    java.util.List<PositionalArgument> pa = invocation.getPositionalArgumentList().getPositionalArguments();
                    
                    if (parameterArgument.isSpread()) {
                        transformSpreadArgument(pa.subList(argumentIndex, pa.size()), classParameter);
                    } else {
                        if (0 <= argumentIndex && argumentIndex < pa.size()) {
                            PositionalArgument pargument = pa.get(argumentIndex);
                            if (pargument.getParameter().isSequenced()) {
                                transformVarargs(argumentIndex, pa);
                            } else {
                                transformArgument(pargument);
                            }
                        } else if (parameterArgument.getSourceParameter().isDefaulted()) {
                            // Use the default parameter from the constructor
                            appendConstructorDefaultParameter(parameterArgument);
                        } else if (parameterArgument.getSourceParameter().isSequenced()) {
                            append(endArray(startArray()));
                        }
                    }
                } else if (invocation.getNamedArgumentList() != null) {
                    if (parameterArgument.isSpread()) {
                        append(exprGen.makeErroneous(invocation, "Spread argument with named invocation not supported"));
                    } else {
                        boolean found = false;
                        for (Tree.NamedArgument na : invocation.getNamedArgumentList().getNamedArguments()) {
                            Parameter parameter = na.getParameter();
                            int parameterIndex = annotationConstructor.getParameterLists().get(0).getParameters().indexOf(parameter);
                            if (parameterIndex == argumentIndex) {
                                transformArgument(na);
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            if (parameterArgument.getSourceParameter().isDefaulted()) {
                                appendConstructorDefaultParameter(parameterArgument);
                            } else {
                                append(exprGen.makeErroneous(invocation, "Unable to find argument"));
                            }
                        }    
                    }
                }
            } else if (argument instanceof LiteralAnnotationArgument) {
                exprGen.at(invocation);
                append(exprGen.naming.makeQuotedQualIdent(
                                exprGen.naming.makeName(annotationConstructor, Naming.NA_FQ | Naming.NA_WRAPPER ),
                                this.parameter.getName()));
            } else {
                append(exprGen.makeErroneous(invocation, "Unable to find argument"));
            }
            
        }
        JCAnnotation annotation = exprGen.at(invocation).Annotation(makeAnnotationType(), annotationArguments.toList());
        return annotation;
    }

    private JCExpression makeAnnotationType() {
        if (Decl.isInteropAnnotationClass(annotationClass)) {
            return exprGen.makeJavaType(annotationConstructor.getType());
        } else {
            return exprGen.makeJavaType(annotationClass.getType(), ExpressionTransformer.JT_ANNOTATION);
        }
    }

    private void appendConstructorDefaultParameter(
            ParameterAnnotationArgument parameterArgument) {
        if (Decl.isInteropAnnotationClass(annotationClass)) {
            return;
        }
        String constructorParameterName = parameterArgument.getSourceParameter().getName();
        append(exprGen.naming.makeQuotedQualIdent(
                exprGen.naming.makeName(annotationConstructor, Naming.NA_FQ | Naming.NA_WRAPPER ),
                constructorParameterName));
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
            JCExpression memberName;
            if (this.parameter != null) {
                memberName = exprGen.naming.makeUnquotedIdent(
                exprGen.naming.selector(this.parameter, Naming.NA_ANNOTATION_MEMBER));
            } else {
                memberName = exprGen.makeErroneous();
            }
            annotationArguments.append(exprGen.make().Assign(
                    memberName, expr));
        }
    }

    public void transformSpreadArgument(java.util.List<PositionalArgument> arguments, Parameter classParameter) {
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
    
    public void visit(Tree.NegativeOp term) {
        if (term.getTerm() instanceof Tree.NaturalLiteral
                || term.getTerm() instanceof Tree.FloatLiteral) {
            append(exprGen.transformExpression(term, BoxingStrategy.UNBOXED, term.getTypeModel()));
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
        } else if (Decl.isInteropAnnotationClass(annotationClass)) {
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
        if (Decl.isInteropAnnotationClass(annotationClass)) {
            append(exprGen.naming.makeQualIdent(
                    exprGen.makeJavaType(((ClassOrInterface)term.getDeclaration()).getType()),
                    "class"));
        } else {
            append(exprGen.make().Literal(term.getDeclaration().getQualifiedNameString()));
        }
    }
    
    private ListBuffer<JCExpression> startArray() {
        ListBuffer<JCExpression> prevCollect = exprs;
        exprs = ListBuffer.<JCExpression>lb();
        expectedType = exprGen.typeFact().getIteratedType(parameter.getType());
        return prevCollect;
    }
    
    private JCNewArray endArray(ListBuffer<JCExpression> prevCollect) {
        ListBuffer<JCExpression> collected = exprs;
        exprs = prevCollect;
        expectedType = null;
        return exprGen.make().NewArray(null,  null, collected.toList());
    }

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