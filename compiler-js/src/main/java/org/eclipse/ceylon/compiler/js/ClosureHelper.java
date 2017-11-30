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

import java.util.HashSet;
import java.util.Set;

import org.eclipse.ceylon.compiler.typechecker.tree.Tree;
import org.eclipse.ceylon.compiler.typechecker.tree.Visitor;
import org.eclipse.ceylon.model.typechecker.model.Declaration;

/** Determine when a function needs to be wrapped inside another one to fix
 * the stupid closure issues of JS lack of proper scopes.
 * 
 * @author Enrique Zamudio
 */
public class ClosureHelper extends Visitor {

    private final Set<Declaration> caps = new HashSet<>();

    public static Set<Declaration> declarationsInExpression(final Tree.Expression that) {
        final ClosureHelper ch = new ClosureHelper();
        that.visit(ch);
        return ch.caps;
    }

    public static Set<Declaration> declarationsInExpression(final Tree.Term that) {
        final ClosureHelper ch = new ClosureHelper();
        that.visit(ch);
        return ch.caps;
    }

    public void visit(Tree.BaseMemberExpression that) {
        if (that.getDeclaration() != null) {
            caps.add(that.getDeclaration());
        }
        super.visit(that);
    }

}
