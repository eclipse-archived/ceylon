package com.redhat.ceylon.compiler.typechecker.tree;

import com.redhat.ceylon.compiler.typechecker.tree.Tree;

public class Util {
    
    public static String name(Tree.Identifier id) {
        if (id==null) {
            return "program element with missing name";
        }
        else {
            return id.getText();
        }
    }

}
