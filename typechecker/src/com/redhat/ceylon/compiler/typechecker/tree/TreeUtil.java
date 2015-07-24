package com.redhat.ceylon.compiler.typechecker.tree;

import java.util.List;

import com.redhat.ceylon.common.Backend;
import com.redhat.ceylon.common.BackendSupport;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.CaseClause;
import com.redhat.ceylon.model.typechecker.model.Annotation;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Function;
import com.redhat.ceylon.model.typechecker.model.Parameter;
import com.redhat.ceylon.model.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.model.typechecker.model.Unit;


public class TreeUtil {
    
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
        else if (term instanceof Tree.LetExpression) {
            Tree.LetExpression e = (Tree.LetExpression) term;
            return hasUncheckedNulls(e.getLetClause().getExpression(), invoking);
        }
        else if (term instanceof Tree.IfExpression) {
            Tree.IfExpression e = (Tree.IfExpression) term;
            return hasUncheckedNulls(e.getIfClause().getExpression(), invoking)
                    || hasUncheckedNulls(e.getElseClause().getExpression(), invoking);
        }
        else if (term instanceof Tree.SwitchExpression) {
            Tree.SwitchExpression e = (Tree.SwitchExpression) term;
            for(CaseClause clause : e.getSwitchCaseList().getCaseClauses()){
                if(hasUncheckedNulls(clause.getExpression(), invoking))
                    return true;
            }
            if(e.getSwitchCaseList().getElseClause() != null)
                return hasUncheckedNulls(e.getSwitchCaseList().getElseClause().getExpression(), invoking);
            return false;
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

    public static Tree.Term eliminateParensAndWidening(Tree.Term term) {
        while (term instanceof Tree.OfOp ||
               term instanceof Tree.Expression) {
            if (term instanceof Tree.OfOp) {
                term = ((Tree.OfOp) term).getTerm();
            }
            else if (term instanceof Tree.Expression) {
                term = ((Tree.Expression) term).getTerm();
            }
        }
        return term;
    }
    
    public static Tree.Term unwrapExpressionUntilTerm(Tree.Term term){
        while (term instanceof Tree.Expression) {
            Tree.Expression e = (Tree.Expression) term;
            term = e.getTerm();
        }
        return term;
    }
    
    public static boolean hasErrorOrWarning(Node node) {
        return hasError(node, true);
    }

    public static boolean hasError(Node node) {
        return hasError(node, false);
    }

    static boolean hasError(Node node, 
            final boolean includeWarnings) {
        // we use an exception to get out of the visitor 
        // as fast as possible when an error is found
        // TODO: wtf?! because it's the common case that
        //       a node has an error? that's just silly
        @SuppressWarnings("serial")
        class ErrorFoundException extends RuntimeException {}
        class ErrorVisitor extends Visitor {
            @Override
            public void handleException(Exception e, Node that) {
                if (e instanceof ErrorFoundException) {
                    throw (ErrorFoundException) e;
                }
                super.handleException(e, that);
            }
            @Override
            public void visitAny(Node that) {
                if (that.getErrors().isEmpty()) {
                    super.visitAny(that);
                }
                else if (includeWarnings) {
                    throw new ErrorFoundException();
                }
                else {
                    // UsageWarning don't count as errors
                    for (Message error: that.getErrors()) {
                        if (!error.isWarning()) {
                            // get out fast
                            throw new ErrorFoundException();
                        }
                    }
                    // no real error, proceed
                    super.visitAny(that);
                }
            }
        }
        ErrorVisitor ev = new ErrorVisitor();
        try {
            node.visit(ev);
            return false;
        }
        catch (ErrorFoundException x) {
            return true;
        }
    }

    public static void buildAnnotations(Tree.AnnotationList al, 
            List<Annotation> annotations) {
        if (al!=null) {
            Tree.AnonymousAnnotation aa = 
                    al.getAnonymousAnnotation();
            if (aa!=null) {
                Annotation ann = new Annotation();
                ann.setName("doc");
                String text = aa.getStringLiteral().getText();
                ann.addPositionalArgment(text);
                annotations.add(ann);
            }
            for (Tree.Annotation a: al.getAnnotations()) {
                Annotation ann = new Annotation();
                Tree.BaseMemberExpression bma = 
                        (Tree.BaseMemberExpression) a.getPrimary();
                String name = bma.getIdentifier().getText();
                ann.setName(name);
                Tree.NamedArgumentList nal = 
                        a.getNamedArgumentList();
                if (nal!=null) {
                    for (Tree.NamedArgument na: 
                            nal.getNamedArguments()) {
                        if (na instanceof Tree.SpecifiedArgument) {
                            Tree.SpecifiedArgument sa = 
                                    (Tree.SpecifiedArgument) na;
                            Tree.SpecifierExpression sie = 
                                    sa.getSpecifierExpression();
                            Tree.Expression e = sie.getExpression();
                            if (e!=null) {
                                Tree.Term t = e.getTerm();
                                Parameter p = sa.getParameter();
                                if (p!=null) {
                                    String text = toString(t);
                                    if (text!=null) {
                                        ann.addNamedArgument(p.getName(), text);
                                    }
                                }
                            }
                        }                    
                    }
                }
                Tree.PositionalArgumentList pal = 
                        a.getPositionalArgumentList();
                if (pal!=null) {
                    for (Tree.PositionalArgument pa: 
                            pal.getPositionalArguments()) {
                        if (pa instanceof Tree.ListedArgument) {
                            Tree.ListedArgument la = 
                                    (Tree.ListedArgument) pa;
                            Tree.Term t = la.getExpression().getTerm();
                            String text = toString(t);
                            if (text!=null) {
                                ann.addPositionalArgment(text);
                            }
                        }
                    }
                }
                annotations.add(ann);
            }
        }
    }
    
    private static String toString(Tree.Term t) {
        if (t instanceof Tree.Literal) {
            return ((Tree.Literal) t).getText();
        }
        else if (t instanceof Tree.StaticMemberOrTypeExpression) {
            Tree.StaticMemberOrTypeExpression mte = 
                    (Tree.StaticMemberOrTypeExpression) t;
            String id = mte.getIdentifier().getText();
            if (mte instanceof Tree.QualifiedMemberOrTypeExpression) {
                Tree.QualifiedMemberOrTypeExpression qmte = 
                        (Tree.QualifiedMemberOrTypeExpression) mte;
                Tree.Primary p = qmte.getPrimary();
                if (p instanceof Tree.StaticMemberOrTypeExpression) {
                    Tree.StaticMemberOrTypeExpression smte = 
                            (Tree.StaticMemberOrTypeExpression) p;
                    return toString(smte) + '.' + id;
                }
                return null;
            }
            else {
                return id;
            }
        }
        else if (t instanceof Tree.TypeLiteral) {
            Tree.TypeLiteral tl = 
                    (Tree.TypeLiteral) t;
            Tree.StaticType type = tl.getType();
            if (type!=null) {
                return toString(type);
            }
            else {
                if (t instanceof Tree.InterfaceLiteral) {
                    return "interface";
                }
                if (t instanceof Tree.ClassLiteral) {
                    return "class";
                }
            }
            return null;
        }
        else if (t instanceof Tree.MemberLiteral) {
            Tree.MemberLiteral ml = 
                    (Tree.MemberLiteral) t;
            Tree.Identifier id = ml.getIdentifier();
            Tree.StaticType type = ml.getType();
            if (type!=null) {
                String qualifier = toString(type);
                if (qualifier!=null && id!=null) {
                    return qualifier + "." + id.getText();
                }
                return null;
            }
            return id.getText();
        }
        else if (t instanceof Tree.ModuleLiteral) {
            Tree.ModuleLiteral ml = (Tree.ModuleLiteral) t;
            String importPath = toString(ml.getImportPath());
            return importPath == null ? "module" : "module " + importPath;
        }
        else if (t instanceof Tree.PackageLiteral) {
            Tree.PackageLiteral pl = (Tree.PackageLiteral) t;
            String importPath = toString(pl.getImportPath());
            return importPath == null ? "package" : "package " + importPath;
        }
        else {
            return null;
        }
    }
    
    private static String toString(Tree.ImportPath importPath) {
        if (importPath != null) {
            StringBuilder sb = new StringBuilder();
            if (importPath.getIdentifiers() != null) {
                for (Tree.Identifier identifier : importPath.getIdentifiers()) {
                    if (sb.length() != 0) {
                        sb.append(".");
                    }
                    sb.append(identifier.getText());
                }
            }
            return sb.toString();
        }
        return null;
    }

    private static String toString(Tree.StaticType type) {
        // FIXME: we're discarding syntactic types and union/intersection types
        if (type instanceof Tree.BaseType){
            Tree.BaseType bt = (Tree.BaseType) type;
            return bt.getIdentifier().getText();
        }
        else if(type instanceof Tree.QualifiedType) {
            Tree.QualifiedType qt = 
                    (Tree.QualifiedType) type;
            String qualifier = toString(qt.getOuterType());
            if(qualifier != null) {
                Tree.SimpleType st = (Tree.SimpleType) type;
                return qualifier + "." + 
                        st.getIdentifier().getText();
            }
            return null;
        }
        return null;
    }

    public static boolean isSelfReference(Tree.Primary that) {
        return that instanceof Tree.This || 
                that instanceof Tree.Outer;
    }

    public static boolean isEffectivelyBaseMemberExpression(Tree.Term term) {
        return term instanceof Tree.BaseMemberExpression ||
                term instanceof Tree.QualifiedMemberExpression &&
                isSelfReference(((Tree.QualifiedMemberExpression) term)
                        .getPrimary());
    }

    public static boolean isInstantiationExpression(
            Tree.Expression e) {
        Tree.Term term = e.getTerm();
        if (term instanceof Tree.InvocationExpression) {
            Tree.InvocationExpression ie = 
                    (Tree.InvocationExpression) term;
            Tree.Primary p = ie.getPrimary();
            if (p instanceof Tree.BaseTypeExpression || 
                p instanceof Tree.QualifiedTypeExpression) {
                return true;
            }
        }
        return false;
    }

}
