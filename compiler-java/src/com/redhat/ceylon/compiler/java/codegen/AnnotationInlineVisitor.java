package com.redhat.ceylon.compiler.java.codegen;

import java.util.ArrayList;

import com.redhat.ceylon.compiler.typechecker.model.AnnotationArgument;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.AnnotationInstantiation;
import com.redhat.ceylon.compiler.typechecker.model.LiteralAnnotationArgument;
import com.redhat.ceylon.compiler.typechecker.model.ParameterAnnotationArgument;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ValueParameter;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AnyMethod;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Term;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

public class AnnotationInlineVisitor extends Visitor {
    private Parameter classParameter;
    private AnyMethod annotationConstructor;
    private boolean spread;
    private boolean checkingInvocationPrimary;
    private boolean checkingArguments;
    private AnnotationInstantiation inlineInfo;
    
    @Override
    public void handleException(Exception e, Node node) {
        if (e instanceof RuntimeException) {
            throw (RuntimeException)e;
        } else {
            throw new RuntimeException(e);
        }
    }
    
    protected void error(Node node, String string) {
        node.addError(string);
    }
    
    @Override
    public void visit(Tree.MethodDefinition d) {
        if (Decl.isAnnotationConstructor(d)) {
            annotationConstructor = d;
            inlineInfo = new AnnotationInstantiation();
            inlineInfo.setArguments(new ArrayList<AnnotationArgument>());
        }
        super.visit(d);
        if (Decl.isAnnotationConstructor(d)) {
            d.getDeclarationModel().setInlineInfo(inlineInfo);
            inlineInfo = null;
            annotationConstructor = null;
        }
    }
    
    @Override
    public void visit(Tree.MethodDeclaration d) {
        if (Decl.isAnnotationConstructor(d)
                && d.getSpecifierExpression() != null) {
            annotationConstructor = d;
            inlineInfo = new AnnotationInstantiation();
            inlineInfo.setArguments(new ArrayList<AnnotationArgument>());
        }
        super.visit(d);
        if (Decl.isAnnotationConstructor(d)
                && d.getSpecifierExpression() != null) {
            d.getDeclarationModel().setInlineInfo(inlineInfo);
            inlineInfo = null;
            annotationConstructor = null;
        }
    }
    
    @Override
    public void visit(Tree.Statement d) {
        if (annotationConstructor != null) {
            if (annotationConstructor instanceof Tree.MethodDefinition 
                    && d instanceof Tree.Return) {
            } else if (d != annotationConstructor) {
                error(d, "Annotation constructors may only contain a return statement");
            }
        }
        super.visit(d);
    }
    
    @Override
    public void visit(Tree.AnnotationList al) {
        // Ignore statements in annotation lists
    }
    
    @Override
    public void visit(Tree.Parameter p) {
        // Ignore statements in parameters
    }
    
    @Override
    public void visit(Tree.DefaultArgument d) {
        if (annotationConstructor != null) {
            Declaration t = d.getUnit().getLanguageModuleDeclaration("true");
            Declaration f = d.getUnit().getLanguageModuleDeclaration("false");
            Term term = d.getSpecifierExpression().getExpression().getTerm();
            if (!(term instanceof Tree.Literal
                    || term instanceof Tree.BaseMemberExpression
                    && (((Tree.BaseMemberExpression)term).getDeclaration().equals(t)
                        || ((Tree.BaseMemberExpression)term).getDeclaration().equals(f)))) {
                error(d, "Only literal default parameters allowed");
            }
        }
    }
    
    @Override
    public void visit(Tree.InvocationExpression invocation) {
        if (annotationConstructor != null) {
            checkingInvocationPrimary = true;
            invocation.getPrimary().visit(this);
            checkingInvocationPrimary = false;
            checkingArguments = true;
            if (invocation.getPositionalArgumentList() != null) {
                invocation.getPositionalArgumentList().visit(this);
            }
            if (invocation.getNamedArgumentList() != null) {
                invocation.getNamedArgumentList().visit(this);
            }
            checkingArguments = false;
        } else {
            super.visit(invocation);
        }
    }
    
    @Override
    public void visit(Tree.Literal literal) {
        if (annotationConstructor != null) {
            if (checkingArguments){
                appendStaticArgument(literal);
            } else {
                error(literal, "Unsupported literal");
            }
        }
    }
    
    @Override
    public void visit(Tree.Expression term) {
        if (annotationConstructor != null) {
            term.visitChildren(this);
        }
    }
    
    @Override
    public void visit(Tree.Term term) {
        if (annotationConstructor != null) {
            error(term, "Unsupported term " + term.getClass().getSimpleName());
        }
    }
    
    @Override
    public void visit(Tree.BaseMemberExpression bme) {
        if (annotationConstructor != null) {
            if (checkingArguments){
                Declaration declaration = bme.getDeclaration();
                if (declaration instanceof ValueParameter) {
                    ValueParameter constructorParameter = (ValueParameter)declaration;
                    ParameterAnnotationArgument a = new ParameterAnnotationArgument();
                    a.setSpread(spread);
                    a.setTargetParameter(classParameter);
                    a.setSourceParameter(constructorParameter);
                    inlineInfo.getArguments().add(a);
                } else if ("ceylon.language::true".equals(declaration.getQualifiedNameString())
                        || "ceylon.language::false".equals(declaration.getQualifiedNameString())) {
                    appendStaticArgument(bme);
                }
            } else {
                error(bme, "Unsupported base member expression");
            }
        }
    }
    
    private void appendStaticArgument(Tree.Primary bme) {
        if (spread) {
            error(bme, "Spread static arguments not supported");
        }
        LiteralAnnotationArgument a = new LiteralAnnotationArgument();
        a.setTargetParameter(classParameter);
        inlineInfo.getArguments().add(a);
    }

    @Override
    public void visit(Tree.BaseTypeExpression bte) {
        if (annotationConstructor != null) {
            if (checkingInvocationPrimary) {
                if (Decl.isAnnotationClass(bte.getDeclaration())) {
                    inlineInfo.setPrimary(bte.getDeclaration());
                } else {
                    error(bte, "Not an annotation class");
                }
            } else {
                error(bte, "Unsupported base type expression");
            }
        }
    }
    
    @Override
    public void visit(Tree.PositionalArgument argument) {
        if (annotationConstructor != null) {
            error(argument, "Unsupported positional argument");
        }
    }
    
    @Override
    public void visit(Tree.SpreadArgument argument) {
        if (annotationConstructor != null) {
            classParameter = argument.getParameter();
            spread = true;
            argument.getExpression().visit(this);
            spread = false;
            classParameter = null;
        }
    }
    
    @Override
    public void visit(Tree.ListedArgument argument) {
        if (annotationConstructor != null) {
            classParameter = argument.getParameter();
            argument.getExpression().visit(this);
            classParameter = null;
        }
    }
    
    @Override
    public void visit(Tree.NamedArgument argument) {
        if (annotationConstructor != null) {
            error(argument, "Unsupported named argument");
        }
    }
    
    @Override
    public void visit(Tree.SpecifiedArgument argument) {
        if (annotationConstructor != null) {
            classParameter = argument.getParameter();
            argument.getSpecifierExpression().visit(this);
            classParameter = null;
        }
    }   
}

