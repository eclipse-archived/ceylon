package com.redhat.ceylon.compiler.typechecker.tree;

import java.util.List;

import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.Unit;


public class Util {
    
    public static final String MISSING_NAME = "program element with missing name";

    public static String name(Tree.Identifier id) {
        if (id==null) {
            return MISSING_NAME;
        }
        else {
            return id.getText();
        }
    }

    public static boolean hasAnnotation(Tree.AnnotationList al, String name, Unit unit) {
        if (al!=null) {
            for (Tree.Annotation a: al.getAnnotations()) {
                Tree.BaseMemberExpression p = (Tree.BaseMemberExpression) a.getPrimary();
                if (p!=null) {
                    String an = name(p.getIdentifier());
                    String alias = unit==null ? name : //WTF?!
                        unit.getModifiers().get(name); 
                    if (an.equals(alias)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean hasUncheckedNulls(Tree.Term term) {
        return hasUncheckedNulls(term, false);
    }
    
    private static boolean hasUncheckedNulls(Tree.Term term, boolean invoking) {
        if (term instanceof Tree.MemberOrTypeExpression) {
            Declaration d = ((Tree.MemberOrTypeExpression) term).getDeclaration();
            return d instanceof TypedDeclaration 
                    && ((TypedDeclaration) d).hasUncheckedNullType()
                    // only consider method types when invoking them, because java method references can't be null
                    && (d instanceof Method == false || invoking);
        }
        else if (term instanceof Tree.QualifiedMemberOrTypeExpression) {
            return hasUncheckedNulls(((Tree.QualifiedMemberOrTypeExpression)term).getPrimary(), invoking);
        }
        else if (term instanceof Tree.InvocationExpression) {
            return hasUncheckedNulls(((Tree.InvocationExpression) term).getPrimary(), true);
        }
        else if (term instanceof Tree.DefaultOp) {
            return hasUncheckedNulls(((Tree.DefaultOp) term).getRightTerm(), invoking);
        }
        else if (term instanceof Tree.Expression) {
            return hasUncheckedNulls(((Tree.Expression)term).getTerm(), invoking);
        }
        else {
            return false;
        }
    }

    public static String formatPath(List<Tree.Identifier> nodes) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Node node: nodes) {
            if (first) {
                first = false;
            }
            else {
                sb.append(".");
            }
            sb.append(node.getText());
        }
        return sb.toString();
    }

}
