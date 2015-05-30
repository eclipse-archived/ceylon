package com.redhat.ceylon.compiler.typechecker.tree;

import java.util.List;

import com.redhat.ceylon.common.Backend;
import com.redhat.ceylon.common.BackendSupport;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Function;
import com.redhat.ceylon.model.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.model.typechecker.model.Unit;


public class Util {
    
    public static final String MISSING_NAME = 
            "program element with missing name";

    public static String name(Tree.Identifier id) {
        if (id==null) {
            return MISSING_NAME;
        }
        else {
            return id.getText();
        }
    }

    public static boolean hasAnnotation(Tree.AnnotationList al, 
            String name, Unit unit) {
        return getAnnotation(al, name, unit) != null;
    }

    public static Tree.Annotation getAnnotation(Tree.AnnotationList al, 
            String name, Unit unit) {
        if (al!=null) {
            for (Tree.Annotation a: al.getAnnotations()) {
                Tree.BaseMemberExpression p = 
                        (Tree.BaseMemberExpression) 
                            a.getPrimary();
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

    public static String getAnnotationArgument(Tree.Annotation ann, 
            String defaultValue) {
        String result = defaultValue;
        Tree.Expression expression = null;
        Tree.PositionalArgumentList pal = 
                ann.getPositionalArgumentList();
        if (pal!=null) {
            List<Tree.PositionalArgument> args = 
                    pal.getPositionalArguments();
            if (!args.isEmpty()) {
                Tree.PositionalArgument arg = args.get(0);
                if (arg instanceof Tree.ListedArgument) {
                    Tree.ListedArgument la = 
                            (Tree.ListedArgument) arg;
                    expression = la.getExpression();
                }
            }
        }
        Tree.NamedArgumentList nal = 
                ann.getNamedArgumentList();
        if (nal!=null) {
            List<Tree.NamedArgument> args = 
                    nal.getNamedArguments();
            if (!args.isEmpty()) {
                Tree.SpecifiedArgument arg = 
                        (Tree.SpecifiedArgument)
                            args.get(0);
                expression = 
                        arg.getSpecifierExpression()
                            .getExpression();
            }
        }
        if (expression!=null) {
            Tree.Literal literal = (Tree.Literal) 
                    expression.getTerm();
            result = literal.getText();
            if (result.startsWith("\"") && 
                    result.endsWith("\"")) {
                result = result.substring(1, 
                        result.length() - 1);
            }
        }
        return result;
    }
    
    public static boolean isForBackend(Tree.AnnotationList al, 
            Backend forBackend, Unit unit) {
        return isForBackend(al, 
                forBackend.backendSupport, 
                unit);
    }
    
    public static boolean isForBackend(Tree.AnnotationList al, 
            BackendSupport backendSupport, Unit unit) {
        String be = getNativeBackend(al, unit);
        return isForBackend(be, backendSupport);
    }
    
    public static boolean isForBackend(String backendName, 
            Backend forBackend) {
        return isForBackend(backendName, 
                forBackend.backendSupport);
    }
    
    public static boolean isForBackend(String backendName, 
            BackendSupport backendSupport) {
        if (backendName != null) {
            Backend backend = 
                    Backend.fromAnnotation(backendName);
            if (backend == null || 
                    !backendSupport.supportsBackend(backend)) {
                return false;
            }
        }
        return true;
    }
    
    public static String getNativeBackend(Tree.AnnotationList al, 
            Unit unit) {
        Tree.Annotation ann = 
                getAnnotation(al, "native", unit);
        return ann == null ? null : 
            getAnnotationArgument(ann, "");
    }
    
    public static boolean hasUncheckedNulls(Tree.Term term) {
        return hasUncheckedNulls(term, false);
    }
    
    private static boolean hasUncheckedNulls(Tree.Term term, 
            boolean invoking) {
        if (term instanceof Tree.MemberOrTypeExpression) {
            Tree.MemberOrTypeExpression mte = 
                    (Tree.MemberOrTypeExpression) term;
            Declaration d = mte.getDeclaration();
            if (d instanceof TypedDeclaration) {
                TypedDeclaration td = (TypedDeclaration) d;
                return td.hasUncheckedNullType() &&
                        // only consider method types when invoking them, 
                        // because java method references can't be null
                        (!(d instanceof Function) || invoking);
            }
            else {
                return false;
            }
        }
        else if (term instanceof Tree.QualifiedMemberOrTypeExpression) {
            Tree.QualifiedMemberOrTypeExpression qmte = 
                    (Tree.QualifiedMemberOrTypeExpression) term;
            return hasUncheckedNulls(qmte.getPrimary(), invoking);
        }
        else if (term instanceof Tree.InvocationExpression) {
            Tree.InvocationExpression ite = 
                    (Tree.InvocationExpression) term;
            return hasUncheckedNulls(ite.getPrimary(), true);
        }
        else if (term instanceof Tree.DefaultOp) {
            Tree.DefaultOp op = (Tree.DefaultOp) term;
            return hasUncheckedNulls(op.getRightTerm(), invoking);
        }
        else if (term instanceof Tree.Expression) {
            Tree.Expression e = (Tree.Expression) term;
            return hasUncheckedNulls(e.getTerm(), invoking);
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
            Tree.Declaration d = (Tree.Declaration) node;
            result = d.getIdentifier();
        }
        else if (node instanceof Tree.ModuleDescriptor) {
            Tree.ModuleDescriptor md = 
                    (Tree.ModuleDescriptor) node;
            result = md.getImportPath();
        }
        else if (node instanceof Tree.PackageDescriptor) {
            Tree.PackageDescriptor pd = 
                    (Tree.PackageDescriptor) node;
            result = pd.getImportPath();
        }
        else if (node instanceof Tree.NamedArgument) {
            Tree.NamedArgument na = (Tree.NamedArgument) node;
            result = na.getIdentifier();
        }
        else if (node instanceof Tree.StaticMemberOrTypeExpression) {
            Tree.StaticMemberOrTypeExpression smte = 
                    (Tree.StaticMemberOrTypeExpression) node;
            result = smte.getIdentifier();
        }
        else if (node instanceof Tree.ExtendedTypeExpression) {
            //TODO: whoah! this is really ugly!
            CustomTree.ExtendedTypeExpression ete = 
                    (CustomTree.ExtendedTypeExpression) node;
            result = ete.getType()
                    .getIdentifier();
        }
        else if (node instanceof Tree.SimpleType) {
            Tree.SimpleType st = (Tree.SimpleType) node;
            result = st.getIdentifier();
        }
        else if (node instanceof Tree.ImportMemberOrType) {
            Tree.ImportMemberOrType imt = 
                    (Tree.ImportMemberOrType) node;
            result = imt.getIdentifier();
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
