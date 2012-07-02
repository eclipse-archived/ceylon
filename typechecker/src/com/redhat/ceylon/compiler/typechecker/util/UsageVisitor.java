/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.redhat.ceylon.compiler.typechecker.util;

import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

/**
 *
 * @author kulikov
 */
public class UsageVisitor extends Visitor {

    @Override
    public void visit(Tree.AnyAttribute that) {
        super.visit(that);
        TypedDeclaration declaration = that.getDeclarationModel();
        if (!declaration.isShared() && declaration.getRefCount() == 0) {
            that.addUsageWarning(String.format("Attribute %s declared but never used", declaration.getName()));
            System.out.println("Add warning");
        }
    }

    @Override
    public void visit(Tree.AnyMethod that) {
        super.visit(that);
        TypedDeclaration declaration = that.getDeclarationModel();
        if (!declaration.isShared() && declaration.getRefCount() == 0) {
            that.addUsageWarning(String.format("Method %s declared but never used", declaration.getName()));
        }
    }
}
