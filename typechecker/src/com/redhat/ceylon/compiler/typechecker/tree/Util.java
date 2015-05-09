package com.redhat.ceylon.compiler.typechecker.tree;

import java.util.List;

import com.redhat.ceylon.common.Backend;
import com.redhat.ceylon.common.BackendSupport;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.Unit;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Annotation;


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
        return getAnnotation(al, name, unit) != null;
    }

    public static Tree.Annotation getAnnotation(Tree.AnnotationList al, String name, Unit unit) {
        if (al!=null) {
            for (Tree.Annotation a: al.getAnnotations()) {
                Tree.BaseMemberExpression p = 
                        (Tree.BaseMemberExpression) a.getPrimary();
                if (p!=null) {
                    String an = name(p.getIdentifier());
                    String alias = unit==null ? name : //WTF?!
                        unit.getModifiers().get(name); 
                    if (an.equals(alias)) {
                        return a;
                    }
                }
            }
        }
        return null;
    }

    public static String getAnnotationArgument(Tree.Annotation a, String def) {
        String result = def;
        Tree.Expression expression = null;
        if (a.getPositionalArgumentList() != null && a.getPositionalArgumentList().getPositionalArguments().size() > 0) {
            Tree.PositionalArgument arg = a.getPositionalArgumentList().getPositionalArguments().get(0);
            if (arg instanceof Tree.ListedArgument) {
                expression = ((Tree.ListedArgument) arg).getExpression();
            }
        } else if (a.getNamedArgumentList() != null && a.getNamedArgumentList().getNamedArguments().size() > 0) {
            Tree.SpecifiedArgument arg = (Tree.SpecifiedArgument)a.getNamedArgumentList().getNamedArguments().get(0);
            expression = arg.getSpecifierExpression().getExpression();
        }
        if (expression != null) {
            Tree.Literal literal = (Tree.Literal)expression.getTerm();
            result = literal.getText();
            if (result.startsWith("\"") && result.endsWith("\"")) {
                result = result.substring(1, result.length() - 1);
            }
        }
        return result;
    }
    
    public static boolean isForBackend(Tree.AnnotationList al, Backend forBackend, Unit unit) {
        return isForBackend(al, forBackend.backendSupport, unit);
    }
    
    public static boolean isForBackend(Tree.AnnotationList al, BackendSupport backendSupport, Unit unit) {
        String be = getNativeBackend(al, unit);
        return isForBackend(be, backendSupport);
    }
    
    public static boolean isForBackend(String backendName, Backend forBackend) {
        return isForBackend(backendName, forBackend.backendSupport);
    }
    
    public static boolean isForBackend(String backendName, BackendSupport backendSupport) {
        if (backendName != null) {
            Backend backend = Backend.fromAnnotation(backendName);
            if (backend == null || !backendSupport.supportsBackend(backend)) {
                return false;
            }
        }
        return true;
    }
    
    public static String getNativeBackend(Tree.AnnotationList al, Unit unit) {
        Annotation a = getAnnotation(al, "native", unit);
        if (a != null) {
            return getAnnotationArgument(a, "");
        }
        return null;
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

    /**
     * Returns the best Node to attach errors to. This code 
     * is used by both backends.
     */
    public static Node getIdentifyingNode(Node node) {
        Node result = null;
        if (node instanceof Tree.Declaration) {
            result = ((Tree.Declaration) node).getIdentifier();
        }
        else if (node instanceof Tree.ModuleDescriptor) {
            result = ((Tree.ModuleDescriptor) node).getImportPath();
        }
        else if (node instanceof Tree.PackageDescriptor) {
            result = ((Tree.PackageDescriptor) node).getImportPath();
        }
        else if (node instanceof Tree.NamedArgument) {
            result = ((Tree.NamedArgument) node).getIdentifier();
        }
        else if (node instanceof Tree.StaticMemberOrTypeExpression) {
            result = ((Tree.StaticMemberOrTypeExpression) node).getIdentifier();
        }
        else if (node instanceof Tree.ExtendedTypeExpression) {
            //TODO: whoah! this is really ugly!
            result = ((CustomTree.ExtendedTypeExpression) node).getType()
                    .getIdentifier();
        }
        else if (node instanceof Tree.SimpleType) {
            result = ((Tree.SimpleType) node).getIdentifier();
        }
        else if (node instanceof Tree.ImportMemberOrType) {
            result = ((Tree.ImportMemberOrType) node).getIdentifier();
        }
        else {
            result = node;
        }
        if (result == null) {
            result = node;
        }
        return result;
    }
    
}
