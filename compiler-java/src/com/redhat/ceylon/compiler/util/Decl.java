/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */

package com.redhat.ceylon.compiler.util;

import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;

public class Decl {
    private Decl() {
    }

    /**
     * Determines whether the declaration's containing scope is a method
     * @param decl The declaration
     * @return true if the declaration is within a method
     */
    public static boolean withinMethod(Tree.Declaration decl) {
        return withinMethod(decl.getDeclarationModel());
    }

    /**
     * Determines whether the declaration's containing scope is a method
     * @param decl The declaration
     * @return true if the declaration is within a method
     */
    public static boolean withinMethod(Declaration decl) {
        Scope container = decl.getContainer();
        return container instanceof Method;
    }
    
    /**
     * Determines whether the declaration's containing scope is a package
     * @param decl The declaration
     * @return true if the declaration is within a package
     */
    public static boolean withinPackage(Tree.Declaration decl) {
        Scope container = decl.getDeclarationModel().getContainer();
        return container instanceof com.redhat.ceylon.compiler.typechecker.model.Package;
    }
    
    /**
     * Determines whether the declaration's containing scope is a class
     * @param decl The declaration
     * @return true if the declaration is within a class
     */
    public static boolean withinClass(Tree.Declaration decl) {
        Scope container = decl.getDeclarationModel().getContainer();
        return container instanceof com.redhat.ceylon.compiler.typechecker.model.Class;
    }
    
    /**
     * Determines whether the declaration's containing scope is a class or interface
     * @param decl The declaration
     * @return true if the declaration is within a class or interface
     */
    public static boolean withinClassOrInterface(Tree.Declaration decl) {
        return withinClassOrInterface(decl.getDeclarationModel());
    }
    
    /**
     * Determines whether the declaration's containing scope is a class or interface
     * @param decl The declaration
     * @return true if the declaration is within a class or interface
     */
    public static boolean withinClassOrInterface(Declaration decl) {
        Scope container = decl.getContainer();
        return container instanceof com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
    }
    
    public static boolean isShared(Tree.Declaration decl) {
        return decl.getDeclarationModel().isShared();
    }
    
    public static boolean isCaptured(Tree.Declaration decl) {
    	// Shared elements are implicitely captured although the typechecker doesn't mark them that way
        return decl.getDeclarationModel().isCaptured() || decl.getDeclarationModel().isShared();
    }

    public static boolean isAbstract(Tree.ClassOrInterface decl) {
        return decl.getDeclarationModel().isAbstract();
    }

    public static boolean isDefault(Tree.Declaration decl) {
        return decl.getDeclarationModel().isDefault();
    }

    public static boolean isFormal(Tree.Declaration decl) {
        return decl.getDeclarationModel().isFormal();
    }

    public static boolean isActual(Tree.Declaration decl) {
        return decl.getDeclarationModel().isActual();
    }

    public static boolean isMutable(Tree.AttributeDeclaration decl) {
        return decl.getDeclarationModel().isVariable();
    }

    public static boolean isToplevel(Tree.Declaration decl) {
        return decl.getDeclarationModel().isToplevel();
    }
    
    public static boolean isInner(Tree.Declaration decl) {
        return decl.getDeclarationModel().getContainer() instanceof Method;
    }
    
    public static boolean isClassAttribute(Declaration decl) {
        return (withinClassOrInterface(decl)) && (decl.isCaptured() || decl.isShared());
    }
}
