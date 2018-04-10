/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.typechecker.analyzer;

import java.util.List;

import org.eclipse.ceylon.compiler.typechecker.tree.Node;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree;
import org.eclipse.ceylon.compiler.typechecker.tree.Visitor;
import org.eclipse.ceylon.model.typechecker.model.Declaration;
import org.eclipse.ceylon.model.typechecker.model.FunctionOrValue;
import org.eclipse.ceylon.model.typechecker.model.Type;
import org.eclipse.ceylon.model.typechecker.model.TypeDeclaration;
import org.eclipse.ceylon.model.typechecker.model.TypeParameter;
import org.eclipse.ceylon.model.typechecker.model.TypedDeclaration;

/**
 * Validates the position in which covariant and contravariant
 * type parameters appear in the schemas of declarations.
 * 
 * @author Gavin King
 *
 */
public class TypeArgumentVisitor extends Visitor {
    
    private boolean contravariant = false;
    private Declaration parameterizedDeclaration;
    
    private void flip() {
        contravariant = !contravariant;
    }
        
    @Override public void visit(Tree.TypeConstraint that) {
        super.visit(that);
        TypeParameter dec = that.getDeclarationModel();
        if (dec!=null) {
            parameterizedDeclaration = dec.getDeclaration();
            flip();
            Tree.SatisfiedTypes sts = 
                    that.getSatisfiedTypes();
            if (sts!=null) {
                for (Tree.Type type: sts.getTypes()) {
                    check(type, false, 
                    		parameterizedDeclaration);
                }
            }
            Tree.CaseTypes cts = 
                    that.getCaseTypes();
            if (cts!=null) {
                for (Tree.Type type: cts.getTypes()) {
                    check(type, false, 
                    		parameterizedDeclaration);
                }
            }
            flip();
            parameterizedDeclaration = null;
        }
    }
        
    @Override public void visit(Tree.TypedDeclaration that) {
        TypedDeclaration dec = that.getDeclarationModel();
        if (!(that instanceof Tree.Variable)) {
            check(that.getType(), dec.isVariable() || dec.isLate(), dec);
        }
        if (dec.isParameter()) {
            flip();
            boolean topLevel = 
                    parameterizedDeclaration==null; //i.e. toplevel parameter in a parameter declaration
            if (topLevel) {
                //TODO: to fix #1378 don't do this when the
                //      parameter dec occurs in any parameter
                //      list other than the first parameter
                //      list of the function
                FunctionOrValue fov = (FunctionOrValue) dec;
                parameterizedDeclaration = 
                        fov.getInitializerParameter()
                            .getDeclaration();
            }
            check(that.getType(), false, 
                    parameterizedDeclaration);
            super.visit(that);
            if (topLevel) {
                parameterizedDeclaration = null;
            }
            flip();
        }
        else {
            super.visit(that);            
        }
    }
    
    @Override public void visit(Tree.AnyClass that) {
        super.visit(that);
        if (that.getExtendedType()!=null) {
            check(that.getExtendedType().getType(), false, 
                    that.getDeclarationModel());
        }
        if (that.getSatisfiedTypes()!=null) {
            for (Tree.Type type: 
                    that.getSatisfiedTypes().getTypes()) {
                check(type, false, that.getDeclarationModel());
            }
        }
    }
    
    @Override public void visit(Tree.AnyInterface that) {
        super.visit(that);
        if (that.getSatisfiedTypes()!=null) {
            for (Tree.Type type: 
                    that.getSatisfiedTypes().getTypes()) {
                check(type, false, that.getDeclarationModel());
            }
        }
    }
    
    @Override public void visit(Tree.ClassDeclaration that) {
        super.visit(that);
        if (that.getClassSpecifier()!=null) {
            check(that.getClassSpecifier().getType(), false, 
                    that.getDeclarationModel());
        }
    }
    
    @Override public void visit(Tree.InterfaceDeclaration that) {
        super.visit(that);
        if (that.getTypeSpecifier()!=null) {
            check(that.getTypeSpecifier().getType(), false, 
                    that.getDeclarationModel());
        }
    }
    
    @Override public void visit(Tree.TypeAliasDeclaration that) {
        super.visit(that);
        if (that.getTypeSpecifier()!=null) {
            check(that.getTypeSpecifier().getType(), false, 
                    that.getDeclarationModel());
        }
    }
    
    @Override public void visit(Tree.TypeConstructor that) {
        super.visit(that);
        if (that.getType()!=null) {
            check(that.getType(), false, 
                    that.getDeclarationModel());
        }
    }
    
    @Override public void visit(Tree.FunctionArgument that) {
        super.visit(that);
        if (that.getType()!=null) {
            check(that.getType().getTypeModel(), false, 
                    that.getDeclarationModel(),
                    that.getExpression());
        }
    }
    
    @Override public void visit(Tree.ObjectDefinition that) {
        super.visit(that);
        if (that.getExtendedType()!=null) {
            check(that.getExtendedType().getType(), false, 
                    that.getDeclarationModel());
        }
        if (that.getSatisfiedTypes()!=null) {
            for (Tree.Type type: 
                    that.getSatisfiedTypes().getTypes()) {
                check(type, false, that.getDeclarationModel());
            }
        }
    }
    
    @Override public void visit(Tree.ObjectExpression that) {
        super.visit(that);
        if (that.getExtendedType()!=null) {
            check(that.getExtendedType().getType(), false, 
                    that.getAnonymousClass());
        }
        if (that.getSatisfiedTypes()!=null) {
            for (Tree.Type type: 
                    that.getSatisfiedTypes().getTypes()) {
                check(type, false, that.getAnonymousClass());
            }
        }
    }
    
    private void check(Tree.Type that, boolean variable, 
            Declaration d) {
        if (that!=null) {
            check(that.getTypeModel(), variable, d, that);
        }
    }

    private void check(Type type, boolean variable, 
            Declaration member, Node that) {
        if (type!=null) {
            List<TypeParameter> errors = 
                    type.checkVariance(
                            !contravariant && !variable, 
                            contravariant && !variable, 
                            parameterizedDeclaration,
                            member);
            displayErrors(that, type, errors, member);
        }
    }

    private void displayErrors(Node that, Type type,
            List<TypeParameter> errors, Declaration member) {
        for (TypeParameter tp: errors) {
            String var; String loc;
            if (tp.isContravariant()) {
                var = "contravariant ('in')";
                loc = "covariant or invariant";
            }
            else if (tp.isCovariant()) {
                var = "covariant ('out')";
                loc = "contravariant or invariant";
            }
            else {
                throw new RuntimeException();
            }
            String typename = 
                    type.asString(that.getUnit());
            that.addError(var 
                    + " type parameter '" + tp.getName() 
                    + "' of '" + tp.getDeclaration().getName() 
                    + "' occurs at a " + loc 
                    + " location in type: '" + typename 
                    + "'");
        }
    }

    @Override
    public void visit(Tree.SimpleType that) {
        super.visit(that);
        if (that.getTypeArgumentList()==null) {
            TypeDeclaration dec = that.getDeclarationModel();
            Type type = that.getTypeModel();
            if (dec!=null && type!=null 
                    && dec.isParameterized() 
                    && !type.isTypeConstructor() 
                    && !that.getMetamodel() 
                    && !(that.getStaticTypePrimary() 
                            && dec.isJava())) {
                String name = dec.getName(that.getUnit());
                List<TypeParameter> params = 
                        dec.getTypeParameters();
                if (!params.get(0).isDefaulted()) {
                    StringBuilder paramList = 
                            new StringBuilder();
                    for (TypeParameter tp: 
                            dec.getTypeParameters()) {
                        if (paramList.length()>0) {
                            paramList.append(", ");
                        }
                        paramList.append("'")
                            .append(tp.getName())
                            .append("'");
                    }
                    that.addError("missing type arguments to generic type: '" + 
                            name + "' declares type parameters " + 
                            paramList);
                }
                else {
                    that.addUsageWarning(Warning.syntaxDeprecation,
                            "implicit use of default type arguments is deprecated (change to '" + 
                            name + "<>')");
                }
            }
        }
    }
    
}