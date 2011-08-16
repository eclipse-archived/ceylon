package com.redhat.ceylon.compiler.typechecker.tree;


public class Util {
    
    public static String name(Tree.Identifier id) {
        if (id==null) {
            return "program element with missing name";
        }
        else {
            return id.getText();
        }
    }

    public static boolean hasAnnotation(Tree.AnnotationList al, String name) {
        if (al!=null) {
            for (Tree.Annotation a: al.getAnnotations()) {
                Tree.BaseMemberExpression p = (Tree.BaseMemberExpression) a.getPrimary();
                if (p!=null) {
                    if ( name(p.getIdentifier()).equals(name) ) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
}
