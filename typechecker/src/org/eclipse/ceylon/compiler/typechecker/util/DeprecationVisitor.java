/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.typechecker.util;

import org.eclipse.ceylon.compiler.typechecker.analyzer.Warning;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree;
import org.eclipse.ceylon.compiler.typechecker.tree.Visitor;
import org.eclipse.ceylon.model.typechecker.model.Class;
import org.eclipse.ceylon.model.typechecker.model.Constructor;
import org.eclipse.ceylon.model.typechecker.model.Declaration;
import org.eclipse.ceylon.model.typechecker.model.Module;
import org.eclipse.ceylon.model.typechecker.model.TypeDeclaration;

public class DeprecationVisitor extends Visitor {
    
    @Override
    public void visit(Tree.MemberOrTypeExpression that) {
        super.visit(that);
        Declaration d = that.getDeclaration();
        if (d!=null && d.isDeprecated()) {
            that.addUsageWarning(Warning.deprecation,
                    "declaration is deprecated: '" + 
                    d.getName() + 
                    "' is annotated 'deprecated' in " +
                    module(d));
        }
        if (d instanceof Class 
                && that.getDirectlyInvoked()) {
            Class c = (Class) d;
            Constructor dc = c.getDefaultConstructor();
            if (dc!=null && dc.isDeprecated()) {
                that.addUsageWarning(Warning.deprecation,
                        "declaration is deprecated: default constructor of '" + 
                        d.getName() + 
                        "' is annotated 'deprecated' in " +
                        module(d));
            }
        }
    }
    @Override
    public void visit(Tree.SimpleType that) {
        super.visit(that);
        TypeDeclaration d = that.getDeclarationModel();
        if (d!=null && d.isDeprecated()) {
            that.addUsageWarning(Warning.deprecation, 
                    "type is deprecated: '" + 
                    d.getName() + 
                    "' is annotated 'deprecated' in " +
                    module(d));
        }
    }
    @Override
    public void visit(Tree.ImportMemberOrType that) {
        super.visit(that);
        Declaration d = that.getDeclarationModel();
        if (d!=null && d.isDeprecated()) {
            that.addUsageWarning(Warning.deprecation,
                    "imported declaration is deprecated: '" + 
                    d.getName() + 
                    "' is annotated 'deprecated' in " +
                    module(d));
        }
    }

    private static String module(Declaration d) {
        Module mod = d.getUnit().getPackage().getModule();
        return "'" + mod.getNameAsString() + 
                "' '\"" + mod.getVersion() + "\"'";
    }
    
    @Override
    public void visit(Tree.CompilerAnnotation that) {
        super.visit(that);
        that.addUsageWarning(Warning.compilerAnnotation,
                    "compiler annotations are an unsupported language feature");
    }
    
}
