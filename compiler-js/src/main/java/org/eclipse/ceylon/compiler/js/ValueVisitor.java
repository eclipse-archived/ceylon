/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.js;

import org.eclipse.ceylon.compiler.typechecker.tree.Tree;
import org.eclipse.ceylon.compiler.typechecker.tree.TreeUtil;
import org.eclipse.ceylon.compiler.typechecker.tree.Visitor;
import org.eclipse.ceylon.model.typechecker.model.Declaration;
import org.eclipse.ceylon.model.typechecker.model.FunctionOrValue;
import org.eclipse.ceylon.model.typechecker.model.ModelUtil;
import org.eclipse.ceylon.model.typechecker.model.Setter;
import org.eclipse.ceylon.model.typechecker.model.TypedDeclaration;
import org.eclipse.ceylon.model.typechecker.model.Value;

/**
 * Determines if a value is "captured" by 
 * block nested in the same containing scope.
 * 
 * For example, a captured value in a class
 * body is an attribute. A captured value in
 * a method body can outlive the execution of
 * the method.
 * 
 * @author Gavin King
 *
 */
public class ValueVisitor extends Visitor {
    
    private final TypedDeclaration declaration;
    private boolean inCapturingScope = false;
    private int sameScope;
    
    public ValueVisitor(TypedDeclaration declaration) {
        this.declaration = declaration;
    }
    
    private boolean enterCapturingScope() {
        boolean cs = inCapturingScope;
        inCapturingScope = true;
        return cs;
    }
    
    private void exitCapturingScope(boolean cs) {
        inCapturingScope = cs;
    }
    
    @Override public void visit(Tree.BaseMemberExpression that) {
        visitReference(that);
    }

    private void visitReference(Tree.Primary that) {
        if (inCapturingScope) {
            capture(that);
        }
    }

    private void capture(Tree.Primary that) {
        if (that instanceof Tree.MemberOrTypeExpression) {
            TypedDeclaration d = (TypedDeclaration) ((Tree.MemberOrTypeExpression) that).getDeclaration();
            if (d==declaration) {
                if (d.isParameter()) {
                    if (!d.getContainer().equals(that.getScope()) || sameScope>0) {
                        //a reference from a default argument 
                        //expression of the same parameter 
                        //list does not capture a parameter
                        ((FunctionOrValue) d).setJsCaptured(true);
                    }
                } else if (d instanceof Value && !ModelUtil.isConstructor(d) && !d.isToplevel()) {
                    ((Value) d).setJsCaptured(true);
                }
            }
        }
    }

    @Override
    public void visit(Tree.QualifiedMemberExpression that) {
        super.visit(that);
        if (TreeUtil.isSelfReference(that.getPrimary())) {
            visitReference(that);
        }
        else {
            capture(that);
        }
    }

    @Override public void visit(Tree.Declaration that) {
        Declaration dm = that.getDeclarationModel();
        if (dm==declaration.getContainer() 
                || dm==declaration
                || (dm instanceof Setter && ((Setter) dm).getGetter()==declaration)) {
            inCapturingScope = false;
        }
        super.visit(that);
    }
    
    private void captureContainer(Declaration d) {
        if (d == null || d.isAnonymous()) {
            return;
        }
        Declaration cd = ModelUtil.getContainingDeclaration(d);
        if (cd != null && !cd.isAnonymous() && !cd.isJsCaptured()) {
            if (cd instanceof FunctionOrValue) {
                ((FunctionOrValue) cd).setJsCaptured(true);
            }
        }
    }

    @Override public void visit(Tree.ClassDefinition that) {
        boolean cs = enterCapturingScope();
        super.visit(that.getClassBody());
        captureContainer(that.getDeclarationModel());
        exitCapturingScope(cs);
    }
    
    @Override public void visit(Tree.ObjectDefinition that) {
        boolean cs = enterCapturingScope();
        super.visit(that);
        exitCapturingScope(cs);
    }
    
    @Override public void visit(Tree.MethodDefinition that) {
        boolean cs = enterCapturingScope();
        super.visit(that);
        exitCapturingScope(cs);
    }
    
    @Override public void visit(Tree.AttributeGetterDefinition that) {
        boolean cs = enterCapturingScope();
        super.visit(that);
        exitCapturingScope(cs);
    }
    
    @Override public void visit(Tree.AttributeSetterDefinition that) {
        boolean cs = enterCapturingScope();
        super.visit(that);
        exitCapturingScope(cs);
    }
    
    @Override public void visit(Tree.TypedArgument that) {
        boolean cs = enterCapturingScope();
        super.visit(that);
        exitCapturingScope(cs);
    }
    
    @Override public void visit(Tree.FunctionArgument that) {
        boolean cs = enterCapturingScope();
        super.visit(that);
        exitCapturingScope(cs);
    }    
    
    @Override
    public void visit(Tree.LazySpecifierExpression that) {
        if(that.getExpression() == null)return;
        boolean cs = enterCapturingScope();
        sameScope++;
        that.getExpression().visit(this);
        sameScope--;
        exitCapturingScope(cs);
    }

    @Override
    public void visit(Tree.Parameter that) {
        //Mark all class initializer parameters as captured
        if (that.getParameterModel().getDeclaration() instanceof org.eclipse.ceylon.model.typechecker.model.Class) {
            that.getParameterModel().getModel().setJsCaptured(true);
        }
        super.visit(that);
    }

    public void visit(Tree.SequenceEnumeration that) {
        boolean cs = enterCapturingScope();
        if (that.getSequencedArgument() != null &&
                !SequenceGenerator.allLiterals(that.getSequencedArgument().getPositionalArguments())) {
            for (Tree.PositionalArgument arg : that.getSequencedArgument().getPositionalArguments()) {
                if (arg instanceof Tree.ListedArgument) {
                    ((Tree.ListedArgument) arg).getExpression().visit(this);
                } else if (arg instanceof Tree.SpreadArgument) {
                    ((Tree.SpreadArgument) arg).getExpression().visit(this);
                } else if (arg instanceof Tree.Comprehension) {
                    arg.visit(this);
                }
            }
        }
        exitCapturingScope(cs);
    }

}
