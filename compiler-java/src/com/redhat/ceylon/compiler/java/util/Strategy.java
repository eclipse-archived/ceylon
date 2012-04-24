package com.redhat.ceylon.compiler.java.util;

import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.CustomTree.MethodDeclaration;

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
