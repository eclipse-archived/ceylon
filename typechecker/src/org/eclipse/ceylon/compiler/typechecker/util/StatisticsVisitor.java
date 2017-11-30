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

import org.eclipse.ceylon.compiler.typechecker.tree.Visitor;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree.AnyAttribute;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree.AnyMethod;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree.ClassDefinition;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree.Declaration;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree.ExecutableStatement;

public class StatisticsVisitor extends Visitor {
    
    private int statements = 0;
    private int declarations = 0;
    private int classes = 0;
    private int methods = 0;
    private int attributes = 0;
    
    @Override
    public void visit(ExecutableStatement that) {
        statements++;
        super.visit(that);
    }
    
    @Override
    public void visit(Declaration that) {
        declarations++;
        super.visit(that);
    }
    
    @Override
    public void visit(ClassDefinition that) {
        classes++;
        super.visit(that);
    }

    @Override
    public void visit(AnyMethod that) {
        methods++;
        super.visit(that);
    }

    @Override
    public void visit(AnyAttribute that) {
        attributes++;
        super.visit(that);
    }
    
    public void print() {
        System.out.println(statements + " statements, " + 
                        declarations + " declarations, " + 
                        classes + " classes, " + 
                        methods + " functions, " + 
                        attributes + " values");
    }
    
}
