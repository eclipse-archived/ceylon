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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ceylon.compiler.typechecker.tree.Tree;
import org.eclipse.ceylon.compiler.typechecker.tree.Visitor;
import org.eclipse.ceylon.model.typechecker.model.Class;
import org.eclipse.ceylon.model.typechecker.model.ClassOrInterface;
import org.eclipse.ceylon.model.typechecker.model.Declaration;
import org.eclipse.ceylon.model.typechecker.model.Function;
import org.eclipse.ceylon.model.typechecker.model.Value;

/**
 * Visitor which attributes qualifiers to local types, such that they will have a unique qualifier
 * in their closest declaration container which is reified at runtime.
 * 
 * For example:
 * 
 * void m() {
 *   if(true){ 
 *     class Local(){}
 *   }else{
 *     class Local(){}
 *   }
 * }
 * 
 * Then at runtime, since the conditional scope is gone, they will both be named Local and have
 * the same method container, so equality would break. With this visitor the first one will have
 * a qualifier of 1, and the second of 2, which we test in equals/hashCode, so we fix runtime
 * equality for local types.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class LocalDeclarationVisitor extends Visitor {

    private Map<String,Integer> localNames;
    private String prefix;
    
    private void visitLocalDecl(Tree.Declaration that) {
        visitLocalDeclarationModel(that.getDeclarationModel());
    }

    private void visitLocalDecl(Tree.TypedArgument that) {
        visitLocalDeclarationModel(that.getDeclarationModel());
    }

    private void visitLocalDecl(Tree.ObjectExpression that) {
        visitLocalDeclarationModel(that.getAnonymousClass());
    }

    public static boolean isTopLevelObjectExpressionType(Declaration model) {
        return model instanceof Class
                && model.isAnonymous()
                && model.isToplevel()
                && !model.isNamed();
    }
    
    private void visitLocalDeclarationModel(Declaration model) {
        if (model!=null
                && (isTopLevelObjectExpressionType(model)
                        || (!model.isToplevel() && !model.isMember())) 
                && !(model instanceof Function && model.isParameter())
                && localNames!=null) {
            Integer counter = localNames.get(model.getName());
            if (counter == null) {
                counter = 1;
            }
            else {
                counter = counter + 1;
            }
            localNames.put(model.getName(), counter);
            String qualifier;
            if (prefix != null) {
                qualifier = prefix + counter.toString();
            }
            else {
                qualifier = counter.toString();
            }
            model.setQualifier(qualifier);
        }
    }

    @Override
    public void visit(Tree.TypeAliasDeclaration that) {
        // type aliases don't introduce new scopes
        visitLocalDecl(that);
        super.visit(that);
    }

    @Override
    public void visit(Tree.ClassOrInterface that) {
        ClassOrInterface model = that.getDeclarationModel();
        visitLocalDecl(that);

        Map<String,Integer> oldLocalNames = null;
        if (model != null && !model.isAlias()) {
            oldLocalNames = localNames;
            localNames = new HashMap<String,Integer>();
        }
        super.visit(that);
        if (model != null && !model.isAlias()) {
            localNames = oldLocalNames;
        }
    }
    
    @Override
    public void visit(Tree.ObjectDefinition that) {
        visitLocalDecl(that);
        // use the same qualifier for the object type
        Class c = that.getAnonymousClass();
        Value v = that.getDeclarationModel();
        if (c!=null && v!=null) {
            c.setQualifier(v.getQualifier());
        }
        
        Map<String,Integer> oldLocalNames = localNames;
        localNames = new HashMap<String,Integer>();

        super.visit(that);
        
        localNames = oldLocalNames;
    }

    @Override
    public void visit(Tree.ObjectArgument that) {
        visitLocalDecl(that);
        // use the same qualifier for the object type
        Class c = that.getAnonymousClass();
        Value v = that.getDeclarationModel();
        if (c != null && v != null) {
            c.setQualifier(v.getQualifier());
        }
        
        Map<String,Integer> oldLocalNames = localNames;
        localNames = new HashMap<String,Integer>();

        super.visit(that);
        
        localNames = oldLocalNames;
    }

    @Override
    public void visit(Tree.MethodArgument that) {
        visitLocalDecl(that);

        Map<String,Integer> oldLocalNames = localNames;
        localNames = new HashMap<String,Integer>();

        super.visit(that);
        
        localNames = oldLocalNames;
    }

    @Override
    public void visit(Tree.AttributeArgument that) {
        visitLocalDecl(that);

        Map<String,Integer> oldLocalNames = localNames;
        localNames = new HashMap<String,Integer>();

        super.visit(that);
        
        localNames = oldLocalNames;
    }
    
    @Override
    public void visit(Tree.ObjectExpression that) {
        visitLocalDecl(that);

        Map<String,Integer> oldLocalNames = localNames;
        localNames = new HashMap<String,Integer>();

        super.visit(that);
        
        localNames = oldLocalNames;
    }

    @Override
    public void visit(Tree.AnyMethod that) {
        visitLocalDecl(that);

        Map<String,Integer> oldLocalNames = localNames;
        localNames = new HashMap<String,Integer>();

        super.visit(that);
        
        localNames = oldLocalNames;
    }

    @Override
    public void visit(Tree.AttributeGetterDefinition that) {
        visitLocalDecl(that);

        Map<String,Integer> oldLocalNames = localNames;
        localNames = new HashMap<String,Integer>();

        super.visit(that);
        
        localNames = oldLocalNames;
    }

    @Override
    public void visit(Tree.AttributeSetterDefinition that) {
        // setters use the same prefix as the getter with a $setter$ prefix
        
        Map<String,Integer> oldLocalNames = localNames;
        localNames = new HashMap<String,Integer>();

        super.visit(that);
        
        localNames = oldLocalNames;
    }

    @Override
    public void visit(Tree.AttributeDeclaration that) {
        Value model = that.getDeclarationModel();
        /*
         * we need a prefix to local type qualifiers for values in toplevel attributes
         * so that we can make a difference between qualifiers for:
         * 
         * Anything() toplevel1 = void(){ class Local(){} }
         * Anything() toplevel2 = void(){ class Local(){} }
         * 
         * Since both local types have the same name and the same package container at runtime,
         * and there's no proper ordering in a package, so we use 1toplevel1$ as a prefix. It
         * starts with a number because there's heuristics in the runtime model that local types
         * must start with a number.
         */
        if (model != null && model.isToplevel()){
            Map<String,Integer> oldLocalNames = localNames;
            String oldPrefix = prefix;
            localNames = new HashMap<String,Integer>();
            prefix = "1"+model.getName()+"$";

            super.visit(that);
        
            localNames = oldLocalNames;
            prefix = oldPrefix;
        }
        else {
            super.visit(that);
        }
    }
}