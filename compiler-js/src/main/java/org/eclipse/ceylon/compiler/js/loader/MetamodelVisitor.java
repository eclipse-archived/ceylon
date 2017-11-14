/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.js.loader;


import java.util.Map;

import org.eclipse.ceylon.compiler.typechecker.analyzer.UsageWarning;
import org.eclipse.ceylon.compiler.typechecker.tree.Message;
import org.eclipse.ceylon.compiler.typechecker.tree.Node;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree;
import org.eclipse.ceylon.compiler.typechecker.tree.Visitor;
import org.eclipse.ceylon.model.typechecker.model.Annotation;
import org.eclipse.ceylon.model.typechecker.model.Declaration;
import org.eclipse.ceylon.model.typechecker.model.Function;
import org.eclipse.ceylon.model.typechecker.model.Module;
import org.eclipse.ceylon.model.typechecker.model.TypedDeclaration;
import org.eclipse.ceylon.model.typechecker.model.Value;

/** Generates the metamodel for all objects in a module.
 * 
 * @author Enrique Zamudio
 */
public class MetamodelVisitor extends Visitor {

    final MetamodelGenerator gen;

    public MetamodelVisitor(Module module) {
        this.gen = new MetamodelGenerator(module);
    }

    /** Returns the in-memory model as a collection of maps.
     * The top-level map represents the module. */
    public Map<String, Object> getModel() {
        return gen.getModel();
    }

    @Override public void visit(Tree.MethodDeclaration that) {
        if (!isNativeHeader(that.getDeclarationModel())) return;
        if (errorFree(that)) {
            gen.encodeMethod(that.getDeclarationModel());
        }
    }

    /** Create and store the model of a method definition. */
    @Override public void visit(Tree.MethodDefinition that) {
        if (!isNativeHeader(that.getDeclarationModel())) return;
        if (errorFree(that)) {
            gen.encodeMethod(that.getDeclarationModel());
            super.visit(that);
        }
    }

    /** Create and store the metamodel info for an attribute. */
    @Override public void visit(Tree.AttributeDeclaration that) {
        if (!isNativeHeader(that.getDeclarationModel())) return;
        if (errorFree(that)) {
            gen.encodeAttributeOrGetter(that.getDeclarationModel());
            super.visit(that);
        }
    }

    @Override
    public void visit(Tree.ClassDefinition that) {
        if (!isNativeHeader(that.getDeclarationModel())) return;
        if (errorFree(that)) {
            gen.encodeClass(that.getDeclarationModel());
            super.visit(that);
        }
    }
    @Override public void visit(final Tree.Constructor that) {
        if (errorFree(that)) {
            gen.encodeConstructor(that.getConstructor());
            super.visit(that);
        }
    }

    @Override public void visit(final Tree.Enumerated that) {
        if (errorFree(that)) {
            gen.encodeConstructor(that.getEnumerated());
        }
    }

    @Override
    public void visit(Tree.InterfaceDefinition that) {
        if (!isNativeHeader(that.getDeclarationModel())) return;
        if (errorFree(that)) {
            gen.encodeInterface(that.getDeclarationModel());
            super.visit(that);
        }
    }

    @Override
    public void visit(Tree.ObjectDefinition that) {
        if (!isNativeHeader(that.getDeclarationModel())) return;
        if (errorFree(that)) {
            gen.encodeObject(that.getDeclarationModel());
            super.visit(that);
        }
    }

    @Override
    public void visit(Tree.ObjectArgument that) {
        if (errorFree(that)) {
            gen.encodeObject(that.getDeclarationModel());
            super.visit(that);
        }
    }

    @Override
    public void visit(Tree.AttributeGetterDefinition that) {
        if (!isNativeHeader(that.getDeclarationModel())) return;
        if (errorFree(that)) {
            gen.encodeAttributeOrGetter(that.getDeclarationModel());
            super.visit(that);
        }
    }

    @Override
    public void visit(Tree.TypeAliasDeclaration that) {
        if (errorFree(that)) {
            gen.encodeTypeAlias(that.getDeclarationModel());
        }
    }
    @Override
    public void visit(Tree.ClassDeclaration that) {
        if (!isNativeHeader(that.getDeclarationModel())) return;
        if (errorFree(that)) {
            gen.encodeClass(that.getDeclarationModel());
        }
    }
    @Override
    public void visit(Tree.InterfaceDeclaration that) {
        if (!isNativeHeader(that.getDeclarationModel())) return;
        if (errorFree(that)) {
            gen.encodeInterface(that.getDeclarationModel());
        }
    }

    public boolean errorFree(Node node) {
        if (node.getErrors() != null) {
            for (Message m : node.getErrors()) {
                if (!(m instanceof UsageWarning)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void visit(Tree.SpecifierStatement st) {
        TypedDeclaration d = ((Tree.SpecifierStatement) st).getDeclaration();
        //Just add shared and actual annotations to this declaration
        if (!isNativeHeader(d))return;
        if (d != null) {
            Annotation ann = new Annotation();
            ann.setName("shared");
            d.getAnnotations().add(ann);
            ann = new Annotation();
            ann.setName("actual");
            d.getAnnotations().add(ann);
            if (d instanceof Function) {
                gen.encodeMethod((Function)d);
            } else if (d instanceof Value) {
                gen.encodeAttributeOrGetter((Value)d);
            } else {
                throw new RuntimeException("JS compiler doesn't know how to encode " +
                        d.getClass().getName() + " into model");
            }
        }
    }

    @Override
    public void visit(Tree.PackageDescriptor that) {
        super.visit(that);
        gen.getPackageMap(that.getUnit().getPackage());
    }

    public void visit(Tree.ObjectExpression that) {
        if (errorFree(that)) {
            gen.encodeClass(that.getAnonymousClass());
            super.visit(that);
        }
    }

    private static boolean isNativeHeader(Declaration d) {
        if (d != null) {
            if (d.isNative()) {
                return d.isNativeHeader();
            }
        }
        return true;
    }
}
