/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package com.redhat.ceylon.compiler.java.util;

import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.CustomTree.MethodDeclaration;

/**
 * Utility functions telling you about code generation strategies
 * @see Decl
 */
public class Strategy {
    private Strategy() {}
    
    /**
     * Determines whether the given Class def should have a {@code main()} method generated.
     * I.e. it's a concrete top level Class without initializer parameters
     * @param def
     */
    public static boolean generateMain(Tree.ClassOrInterface def) {
        return def instanceof Tree.AnyClass 
                && Decl.isToplevel(def) 
                && !Decl.isAbstract(def)
                && ((Class)def.getDeclarationModel()).getParameterList().getParameters().isEmpty();
    }
    
    /**
     * Determines whether the given Method def should have a {@code main()} method generated.
     * I.e. it's a top level method without parameters
     * @param def
     */
    public static boolean generateMain(Tree.AnyMethod def) {
        return  Decl.isToplevel(def) 
                && !def.getParameterLists().isEmpty() 
                && def.getParameterLists().get(0).getParameters().isEmpty();
    }
    
    public static boolean generateThisDelegates(Tree.AnyMethod def) {
        return Decl.withinInterface(def.getDeclarationModel())
            && def instanceof MethodDeclaration
            && ((MethodDeclaration) def).getSpecifierExpression() == null;
    }

    public static boolean needsOuterMethodInCompanion(ClassOrInterface model) {
        return !model.isToplevel();
    }
    
}
