package com.redhat.ceylon.compiler.typechecker.tree;

import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;


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
    
    public static boolean hasUncheckedNulls(Tree.Term term) {
        if (term instanceof Tree.MemberOrTypeExpression) {
            Declaration d = ((Tree.MemberOrTypeExpression) term).getDeclaration();
            return d instanceof TypedDeclaration && 
                    ((TypedDeclaration) d).hasUncheckedNullType();
        }
        else if (term instanceof Tree.InvocationExpression) {
            return hasUncheckedNulls(((Tree.InvocationExpression) term).getPrimary());
        }
        else {
            return false;
        }
    }

}
