/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.js;

import org.eclipse.ceylon.common.Backend;
import org.eclipse.ceylon.compiler.typechecker.analyzer.UsageWarning;
import org.eclipse.ceylon.compiler.typechecker.tree.Message;
import org.eclipse.ceylon.compiler.typechecker.tree.Node;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree;
import org.eclipse.ceylon.compiler.typechecker.tree.Visitor;

public class ErrorVisitor extends Visitor {

    private boolean flag = false;
    private int checking;

    public boolean hasErrors(Node that) {
        flag = false;
        checking=0;
        that.visit(this);
        return flag;
    }

    private void check(Node that) {
        if (that.getErrors() != null && !that.getErrors().isEmpty()) {
            for (Message err : that.getErrors()) {
                if (!(err instanceof UsageWarning)) {
                    flag = true;
                    return;
                }
            }
        }
    }

    @Override
    public void visitAny(Node that) {
        if (checking>0)check(that);
        super.visitAny(that);
    }

    public void visit(Tree.TypedDeclaration that) {
        //Don't check for errors native stuff for other backends
        if (that.getDeclarationModel() != null && that.getDeclarationModel().isNativeImplementation()
                && !that.getDeclarationModel().getNativeBackends().supports(Backend.JavaScript)) {
            return;
        }
        checking++;
        check(that);
        super.visit(that);
        checking--;
    }
    public void visit(Tree.TypeDeclaration that) {
        //Don't check for errors native stuff for other backends
        if (that.getDeclarationModel() != null && that.getDeclarationModel().isNativeImplementation()
                && !that.getDeclarationModel().getNativeBackends().supports(Backend.JavaScript)) {
            return;
        }
        checking++;
        super.visit(that);
        checking--;
    }

}
