package com.redhat.ceylon.compiler.typechecker.analyzer;

import com.redhat.ceylon.compiler.typechecker.tree.Tree;

public class Util {
    
    static String name(Tree.Identifier id) {
        if (id==null) {
            return "program element with missing name";
        }
        else {
            return id.getText();
        }
    }

}
