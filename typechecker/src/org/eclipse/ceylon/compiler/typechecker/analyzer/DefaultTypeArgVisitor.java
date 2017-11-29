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

import org.eclipse.ceylon.compiler.typechecker.tree.Tree;
import org.eclipse.ceylon.compiler.typechecker.tree.Visitor;
import org.eclipse.ceylon.model.typechecker.model.DecidabilityException;
import org.eclipse.ceylon.model.typechecker.model.Declaration;
import org.eclipse.ceylon.model.typechecker.model.Type;
import org.eclipse.ceylon.model.typechecker.model.TypeParameter;

/**
 * Detects recursive default type arguments
 * 
 * @author Gavin King
 *
 */
public class DefaultTypeArgVisitor extends Visitor {
    
    @Override
    public void visit(Tree.TypeParameterDeclaration that) {
        Tree.TypeSpecifier ts = that.getTypeSpecifier();
        if (ts!=null) {
            TypeParameter tp = that.getDeclarationModel();
            Declaration dec = tp.getDeclaration();
            Type dta = tp.getDefaultTypeArgument();
            if (dta!=null) {
                try {
                    if (dta.involvesDeclaration(dec)) {
                        tp.setDefaultTypeArgument(null);
                    }
                }
                catch (DecidabilityException re) {
                    ts.addError("undecidable default type argument");
                    tp.setDefaultTypeArgument(null);
                }
            }
        }
        super.visit(that);
    }

}
