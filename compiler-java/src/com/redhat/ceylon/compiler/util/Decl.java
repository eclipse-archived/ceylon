package com.redhat.ceylon.compiler.util;

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
        Scope container = decl.getDeclarationModel().getContainer();
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
        Scope container = decl.getDeclarationModel().getContainer();
        return container instanceof com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
    }
    
    public static boolean isShared(Tree.Declaration decl) {
        return decl.getDeclarationModel().isShared();
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
    
}
