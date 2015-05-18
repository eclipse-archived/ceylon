package com.redhat.ceylon.compiler.typechecker.analyzer;

import static com.redhat.ceylon.model.typechecker.model.Util.intersectionType;
import static com.redhat.ceylon.model.typechecker.model.Util.isNamed;
import static com.redhat.ceylon.model.typechecker.model.Util.isTypeUnknown;
import static com.redhat.ceylon.model.typechecker.model.Util.producedType;
import static com.redhat.ceylon.model.typechecker.model.Util.unionType;
import static java.util.Collections.emptyList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.compiler.typechecker.tree.Message;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Type;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.TypeArgumentList;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.model.typechecker.model.Annotation;
import com.redhat.ceylon.model.typechecker.model.Class;
import com.redhat.ceylon.model.typechecker.model.Constructor;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Interface;
import com.redhat.ceylon.model.typechecker.model.Module;
import com.redhat.ceylon.model.typechecker.model.Parameter;
import com.redhat.ceylon.model.typechecker.model.ProducedReference;
import com.redhat.ceylon.model.typechecker.model.ProducedType;
import com.redhat.ceylon.model.typechecker.model.Scope;
import com.redhat.ceylon.model.typechecker.model.TypeAlias;
import com.redhat.ceylon.model.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.model.typechecker.model.TypeParameter;
import com.redhat.ceylon.model.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.model.typechecker.model.Unit;
import com.redhat.ceylon.model.typechecker.model.Value;

/**
 * Bucket for some helper methods used by various
 * visitors.
 * 
 * @author Gavin King
 *
 */
public class Util {
    
    static TypedDeclaration getTypedMember(TypeDeclaration d, String name,
            List<ProducedType> signature, boolean ellipsis, Unit unit) {
        Declaration member = 
                d.getMember(name, unit, signature, ellipsis);
        if (member instanceof TypedDeclaration) {
            return (TypedDeclaration) member;
        }
        else {
            return null;
        }
    }

    static TypeDeclaration getTypeMember(TypeDeclaration d, String name,
            List<ProducedType> signature, boolean ellipsis, Unit unit) {
        Declaration member = 
                d.getMember(name, unit, signature, ellipsis);
        if (member instanceof TypeDeclaration) {
            return (TypeDeclaration) member;
        }
        else if (member instanceof TypedDeclaration) {
            return anonymousType(name, 
                    (TypedDeclaration) member);
        }
        else {
            return null;
        }
    }

    static TypedDeclaration getTypedDeclaration(Scope scope,
            String name, List<ProducedType> signature, boolean ellipsis,
            Unit unit) {
        Declaration result = 
                scope.getMemberOrParameter(unit, 
                        name, signature, ellipsis);
        if (result instanceof TypedDeclaration) {
        	return (TypedDeclaration) result;
        }
        else {
        	return null;
        }
    }
    
    static TypeDeclaration getTypeDeclaration(Scope scope,
            String name, List<ProducedType> signature, boolean ellipsis,
            Unit unit) {
        Declaration result = 
                scope.getMemberOrParameter(unit, 
                        name, signature, ellipsis);
        if (result instanceof TypeDeclaration) {
        	return (TypeDeclaration) result;
        }
        else if (result instanceof TypedDeclaration) {
            return anonymousType(name, 
                    (TypedDeclaration) result);
        }
        else {
        	return null;
        }
    }

    static TypedDeclaration getPackageTypedDeclaration(String name, 
            List<ProducedType> signature, boolean ellipsis,
            Unit unit) {
        Declaration result = 
                unit.getPackage().getMember(name, 
                        signature, ellipsis);
        if (result instanceof TypedDeclaration) {
            return (TypedDeclaration) result;
        }
        else {
            return null;
        }
    }
    
    static TypeDeclaration getPackageTypeDeclaration(String name, 
            List<ProducedType> signature, boolean ellipsis,
            Unit unit) {
        Declaration result = 
                unit.getPackage().getMember(name, 
                        signature, ellipsis);
        if (result instanceof TypeDeclaration) {
            return (TypeDeclaration) result;
        }
        else if (result instanceof TypedDeclaration) {
            return anonymousType(name, 
                    (TypedDeclaration) result);
        }
        else {
            return null;
        }
    }

    public static TypeDeclaration anonymousType(String name, 
            TypedDeclaration result) {
        ProducedType type = result.getType();
        if (type!=null) {
            TypeDeclaration typeDeclaration = 
                    type.getDeclaration();
            if (typeDeclaration instanceof Class &&
                    typeDeclaration.isAnonymous() &&
                    isNamed(name,typeDeclaration)) {
                return typeDeclaration;
            }
        }
        return null;
    }
    
    static List<ProducedType> getTypeArguments(Tree.TypeArguments tas,
    		ProducedType qt, List<TypeParameter> typeParameters) {
        if (tas instanceof Tree.TypeArgumentList) {
            int size = typeParameters.size();
            List<ProducedType> typeArguments = 
                    new ArrayList<ProducedType>(size);
            Map<TypeParameter,ProducedType> typeArgMap = 
                    new HashMap<TypeParameter,ProducedType>();
            if (qt!=null) {
                typeArgMap.putAll(qt.getTypeArguments());
            }
            if (tas instanceof Tree.TypeArgumentList) {
                TypeArgumentList tal = 
                        (Tree.TypeArgumentList) tas;
                List<Tree.Type> types = tal.getTypes();
                int count = types.size();
                for (int i=0; i<count; i++) {
                    Type type = types.get(i);
                    ProducedType t = 
                            type.getTypeModel();
                    if (t==null) {
                        typeArguments.add(null);
                    }
                    else {
                        typeArguments.add(t);
                        if (i<size) {
                            TypeParameter tp = 
                                    typeParameters.get(i);
                            if (tp.isTypeConstructor()) {
                                setTypeConstructor(type,tp);
                            }
                            typeArgMap.put(tp, t);
                        }
                    }
                }
            }
            else {
                List<ProducedType> types = 
                        tas.getTypeModels();
                int count = types.size();
                for (int i=0; i<count; i++) {
                    ProducedType t = types.get(i);
                    if (t==null) {
                        typeArguments.add(null);
                    }
                    else {
                        typeArguments.add(t);
                        if (i<size) {
                            TypeParameter tp = 
                                    typeParameters.get(i);
                            typeArgMap.put(tp, t);
                        }
                    }
                }
            }
            if (!(tas instanceof Tree.InferredTypeArguments)) {
                for (int i=typeArguments.size(); i<size; i++) {
                    TypeParameter tp = typeParameters.get(i);
                	ProducedType dta = 
                	        tp.getDefaultTypeArgument();
                	if (dta==null || 
                	        //necessary to prevent stack overflow
                	        //for illegal recursively-defined
                	        //default type argument
                	        dta.containsDeclaration(tp.getDeclaration())) {
                		break;
                	}
                	else {
                	    ProducedType da = 
                	            dta.substitute(typeArgMap);
                		typeArguments.add(da);
                		typeArgMap.put(tp, da);
                	}
                }
            }
            return typeArguments;
        }
        else {
            return emptyList();
        }
    }
    
    /*static List<ProducedType> getParameterTypes(Tree.ParameterTypes pts) {
        if (pts==null) return null;
        List<ProducedType> typeArguments = new ArrayList<ProducedType>();
        for (Tree.SimpleType st: pts.getSimpleTypes()) {
            ProducedType t = st.getTypeModel();
            if (t==null) {
                st.addError("could not resolve parameter type");
                typeArguments.add(null);
            }
            else {
                typeArguments.add(t);
            }
        }
        return typeArguments;
    }*/
    
    public static Tree.Statement getLastExecutableStatement(Tree.ClassBody that) {
        List<Tree.Statement> statements = that.getStatements();
        Unit unit = that.getUnit();
        for (int i=statements.size()-1; i>=0; i--) {
            Tree.Statement s = statements.get(i);
            if (isExecutableStatement(unit, s) || 
                    s instanceof Tree.Constructor) {
                return s;
            }
        }
        return null;
    }

    public static Tree.Constructor getLastConstructor(Tree.ClassBody that) {
        List<Tree.Statement> statements = that.getStatements();
        for (int i=statements.size()-1; i>=0; i--) {
            Tree.Statement s = statements.get(i);
            if (s instanceof Tree.Constructor) {
                return (Tree.Constructor) s;
            }
        }
        return null;
    }

    static boolean isExecutableStatement(Unit unit, Tree.Statement s) {
        if (s instanceof Tree.SpecifierStatement) {
            //shortcut refinement statements with => aren't really "executable"
            Tree.SpecifierStatement ss = 
                    (Tree.SpecifierStatement) s;
            return !(ss.getSpecifierExpression() 
                    instanceof Tree.LazySpecifierExpression) || 
                    !ss.getRefinement();
        }
        else if (s instanceof Tree.ExecutableStatement) {
            return true;
        }
        else {
            if (s instanceof Tree.AttributeDeclaration) {
                Tree.AttributeDeclaration ad = 
                        (Tree.AttributeDeclaration) s;
                Tree.SpecifierOrInitializerExpression sie = 
                        ad.getSpecifierOrInitializerExpression();
        		return sie!=null && 
        		        !(sie instanceof Tree.LazySpecifierExpression);
            }
            /*else if (s instanceof Tree.MethodDeclaration) {
                if ( ((Tree.MethodDeclaration) s).getSpecifierExpression()!=null ) {
                    return s;
                }
            }*/
            else if (s instanceof Tree.ObjectDefinition) {
                Tree.ObjectDefinition o = (Tree.ObjectDefinition) s;
                if (o.getExtendedType()!=null) {
                    ProducedType et = o.getExtendedType().getType().getTypeModel();
        			if (et!=null 
                            && !et.getDeclaration().equals(unit.getObjectDeclaration())
                            && !et.getDeclaration().equals(unit.getBasicDeclaration())) {
                        return true;
                    }
                }
                if (o.getClassBody()!=null) {
                    if (getLastExecutableStatement(o.getClassBody())!=null) {
                        return true;
                    }
                }
                return false;
            }
            else {
                return false;
            }
        }
    }
    
    private static String message(ProducedType type, 
            String problem, ProducedType otherType, 
            Unit unit) {
        String unknownTypeError = 
                type.getFirstUnknownTypeError(true);
        String typeName = 
                type.getProducedTypeName(unit);
        String otherTypeName = 
                otherType.getProducedTypeName(unit);
        if (otherTypeName.equals(typeName)) {
            typeName = type.getProducedTypeQualifiedName();
            otherTypeName = otherType.getProducedTypeQualifiedName();
        }
        return ": '" + typeName + "'" + problem + "'" + otherTypeName + "'"
                + (unknownTypeError != null ? ": " + unknownTypeError : "");
    }
    
    private static String message(ProducedType type, String problem, Unit unit) {
        String typeName = type.getProducedTypeName(unit);
        return ": '" + typeName + "'" + problem;
    }
    
    static boolean checkCallable(ProducedType type, Node node, String message) {
        Unit unit = node.getUnit();
        if (isTypeUnknown(type)) {
            addTypeUnknownError(node, type, message);
            return false;
        }
        else if (!unit.isCallableType(type)) {
            if (!hasError(node)) {
                String extra = 
                        message(type, 
                                " is not a subtype of 'Callable'", 
                                unit);
                if (node instanceof Tree.StaticMemberOrTypeExpression) {
                    Tree.StaticMemberOrTypeExpression smte = 
                            (Tree.StaticMemberOrTypeExpression) node;
                    Declaration d = smte.getDeclaration();
                    String name = d.getName();
                    if (d instanceof Interface) {
                        extra = ": '" + name + "' is an interface";
                    }
                    else if (d instanceof TypeAlias) {
                        extra = ": '" + name + "' is a type alias";
                    }
                    else if (d instanceof TypeParameter) {
                        extra = ": '" + name + "' is a type parameter";
                    }
                    else if (d instanceof Value) {
                        extra = ": value '" + name + "' has type '" + 
                                type.getProducedTypeName(unit) + 
                                "' which is not a subtype of 'Callable'";
                    }
                }
                node.addError(message + extra);
            }
            return false;
        }
        else {
            return true;
        }
    }

    static ProducedType checkSupertype(ProducedType type, 
            TypeDeclaration td, Node node, String message) {
        if (isTypeUnknown(type)) {
            addTypeUnknownError(node, type, message);
            return null;
        }
        else {
            ProducedType supertype = type.getSupertype(td);
            if (supertype==null) {
                node.addError(message + 
                        message(type, 
                                " is not a subtype of '" + 
                                        td.getName() + "'", 
                                node.getUnit()));
            }
            return supertype;
        }
    }

    static void checkAssignable(ProducedType type, 
            ProducedType supertype, Node node, String message) {
        if (isTypeUnknown(type)) {
        	addTypeUnknownError(node, type, message);
        }
        else if (isTypeUnknown(supertype)) {
            addTypeUnknownError(node, supertype, message);
        }
        else if (!type.isSubtypeOf(supertype)) {
        	node.addError(message + 
        	        message(type, 
        	                " is not assignable to ", 
        	                supertype, 
        	                node.getUnit()));
        }
    }

    static void checkAssignableWithWarning(ProducedType type, 
            ProducedType supertype, Node node, String message) {
        if (isTypeUnknown(type)) {
            addTypeUnknownError(node, type, message);
        }
        else if (isTypeUnknown(supertype)) {
            addTypeUnknownError(node, supertype, message);
        }
        else if (!type.isSubtypeOf(supertype)) {
        	node.addUnsupportedError(message + 
        	        message(type, 
        	                " is not assignable to ", 
        	                supertype, 
        	                node.getUnit()));
        }
    }

    static void checkAssignableToOneOf(ProducedType type, 
    		ProducedType supertype1, ProducedType supertype2, 
            Node node, String message, int code) {
        if (isTypeUnknown(type)) {
            addTypeUnknownError(node, type, message);
        }
        else if (isTypeUnknown(supertype1)) {
            addTypeUnknownError(node, supertype1, message);
        }
        else if (isTypeUnknown(supertype2)) {
            addTypeUnknownError(node, supertype2, message);
        }
        else if (!type.isSubtypeOf(supertype1)
                && !type.isSubtypeOf(supertype2)) {
            node.addError(message + 
                    message(type, 
                            " is not assignable to ", 
                            supertype1, 
                            node.getUnit()), 
                            code);
        }
    }

    static void checkAssignable(ProducedType type, 
            ProducedType supertype, Node node, 
            String message, int code) {
        if (isTypeUnknown(type)) {
            addTypeUnknownError(node, type, message);
        }
        else if (isTypeUnknown(supertype)) {
            addTypeUnknownError(node, supertype, message);
        }
        else if (!type.isSubtypeOf(supertype)) {
            node.addError(message + 
                    message(type, 
                            " is not assignable to ", 
                            supertype, 
                            node.getUnit()), 
                            code);
        }
    }

    /*static void checkAssignable(ProducedType type, ProducedType supertype, 
            TypeDeclaration td, Node node, String message) {
        if (isTypeUnknown(type) || isTypeUnknown(supertype)) {
            addTypeUnknownError(node, message);
        }
        else if (!type.isSubtypeOf(supertype)) {
            node.addError(message + message(type, " is not assignable to ", supertype, node.getUnit()));
        }
    }*/

    static void checkIsExactly(ProducedType type, 
            ProducedType supertype, Node node, String message) {
        if (isTypeUnknown(type)) {
            addTypeUnknownError(node, type, message);
        }
        else if (isTypeUnknown(supertype)) {
            addTypeUnknownError(node, supertype, message);
        }
        else if (!type.isExactly(supertype)) {
            node.addError(message + 
                    message(type, " is not exactly ", 
                            supertype, node.getUnit()));
        }
    }

    static void checkIsExactly(ProducedType type, 
            ProducedType supertype, Node node, String message, 
            int code) {
        if (isTypeUnknown(type)) {
            addTypeUnknownError(node, type, message);
        }
        else if (isTypeUnknown(supertype)) {
            addTypeUnknownError(node, supertype, message);
        }
        else if (!type.isExactly(supertype)) {
            node.addError(message + 
                    message(type, " is not exactly ", 
                            supertype, node.getUnit()), code);
        }
    }

    static void checkIsExactlyOneOf(ProducedType type, 
    		ProducedType supertype1, ProducedType supertype2, 
            Node node, String message) {
        if (isTypeUnknown(type)) {
            addTypeUnknownError(node, type, message);
        }
        else if (isTypeUnknown(supertype1)) {
            addTypeUnknownError(node, supertype1, message);
        }
        else if (isTypeUnknown(supertype2)) {
            addTypeUnknownError(node, supertype2, message);
        }
        else if (!type.isExactly(supertype1)
                && !type.isExactly(supertype2)) {
            node.addError(message + 
                    message(type, " is not exactly ", 
                            supertype1, node.getUnit()));
        }
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
                        if (!(error instanceof UsageWarning)) {
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

    private static void addTypeUnknownError(Node node, 
            ProducedType type, String message) {
        if (!hasError(node)) {
            node.addError(message + 
                    ": type cannot be determined" +
                    getTypeUnknownError(type));
        }
    }
    
    public static String getTypeUnknownError(ProducedType type) {
        if (type == null) {
            return "";
        }
        else {
            String error = type.getFirstUnknownTypeError();
            if (error != null) {
                return ": " + error;
            }
            else {
                return "";
            }
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

    static boolean inLanguageModule(Unit unit) {
        return unit.getPackage()
                .getQualifiedNameString()
                .startsWith(Module.LANGUAGE_MODULE_NAME);
    }

    static String typeDescription(TypeDeclaration td, Unit unit) {
        String name = td.getName();
        if (td instanceof TypeParameter) {
            Declaration container = 
                    (Declaration) td.getContainer();
            return "type parameter '" + name + "' of '" + 
                    container.getName(unit) + "'";
        }
        else {
            return "type '" + name + "'";
        }
    }

    static String typeNamesAsIntersection(
            List<ProducedType> upperBounds, Unit unit) {
        if (upperBounds.isEmpty()) {
            return "Anything";
        }
        StringBuilder sb = new StringBuilder();
        for (ProducedType st: upperBounds) {
            sb.append(st.getProducedTypeName(unit)).append(" & ");
        }
        if (sb.toString().endsWith(" & ")) {
            sb.setLength(sb.length()-3);
        }
        return sb.toString();
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

    static boolean isAlwaysSatisfied(Tree.ConditionList cl) {
        if (cl==null) return false;
        for (Tree.Condition c: cl.getConditions()) {
            if (c instanceof Tree.BooleanCondition) {
                Tree.BooleanCondition bc = 
                        (Tree.BooleanCondition) c;
                Tree.Expression ex = bc.getExpression();
                if (ex!=null) {
                    Tree.Term t = ex.getTerm();
                    //TODO: eliminate parens
                    //TODO: take into account conjunctions/disjunctions
                    if (t instanceof Tree.BaseMemberExpression) {
                        Tree.BaseMemberExpression bme = 
                                (Tree.BaseMemberExpression) t;
                        Declaration d = bme.getDeclaration();
                        if (isBooleanTrue(d)) {
                            continue;
                        }
                    }
                }
            }
            return false;
        }
        return true;
    }

    public static boolean isBooleanTrue(Declaration d) {
        return d!=null && d.getQualifiedNameString()
                .equals("ceylon.language::true");
    }

    public static boolean isBooleanFalse(Declaration d) {
        return d!=null && d.getQualifiedNameString()
                .equals("ceylon.language::false");
    }

    static boolean isNeverSatisfied(Tree.ConditionList cl) {
        if (cl==null) return false;
        for (Tree.Condition c: cl.getConditions()) {
            if (c instanceof Tree.BooleanCondition) {
                Tree.BooleanCondition bc = 
                        (Tree.BooleanCondition) c;
                Tree.Expression ex = 
                        bc.getExpression();
                if (ex!=null) {
                    Tree.Term t = ex.getTerm();
                    //TODO: eliminate parens
                    //TODO: take into account conjunctions/disjunctions
                    if (t instanceof Tree.BaseMemberExpression) {
                        Tree.BaseMemberExpression bme = 
                                (Tree.BaseMemberExpression) t;
                        Declaration d = bme.getDeclaration();
                        if (isBooleanFalse(d)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    static boolean isAtLeastOne(Tree.ForClause forClause) {
        Tree.ForIterator fi = forClause.getForIterator();
        if (fi!=null) {
            Tree.SpecifierExpression se = 
                    fi.getSpecifierExpression();
            if (se!=null) {
                Tree.Expression e = se.getExpression();
                if (e!=null) {
                    Unit unit = forClause.getUnit();
                    ProducedType at = 
                            unit.getAnythingDeclaration().getType();
                    ProducedType neit = 
                            unit.getNonemptyIterableType(at);
                    ProducedType t = e.getTypeModel();
                    return t!=null && t.isSubtypeOf(neit);
                }
            }
        }
        return false;
    }

    static boolean declaredInPackage(Declaration dec, Unit unit) {
        return dec.getUnit().getPackage().equals(unit.getPackage());
    }

    public static Tree.Term unwrapExpressionUntilTerm(Tree.Term term){
        while (term instanceof Tree.Expression) {
            term = ((Tree.Expression)term).getTerm();
        }
        return term;
    }
    
    public static boolean isIndirectInvocation(Tree.InvocationExpression that) {
        return isIndirectInvocation(that.getPrimary());
    }

    private static boolean isIndirectInvocation(Tree.Primary primary) {
        Tree.Term p = unwrapExpressionUntilTerm(primary);
        if (p instanceof Tree.MemberOrTypeExpression) {
            Tree.MemberOrTypeExpression mte = 
                    (Tree.MemberOrTypeExpression) p;
            return isIndirectInvocation(mte);
        }
        else {
           return true;
        }
    }

    private static boolean isIndirectInvocation(Tree.MemberOrTypeExpression that) {
        ProducedReference prf = that.getTarget();
        if (prf==null) {
            return true;
        }
        else {
            Declaration d = prf.getDeclaration();
            if (!prf.isFunctional() || 
                    //type parameters are not really callable 
                    //even though they are Functional
                    d instanceof TypeParameter) {
                return true;
            }
            if (that.getStaticMethodReference()) {
                if (d.isStaticallyImportable() || 
                        d instanceof Constructor) {
                    Tree.QualifiedMemberOrTypeExpression qmte = 
                            (Tree.QualifiedMemberOrTypeExpression) that;
                    return isIndirectInvocation(qmte.getPrimary());
                }
                else {
                    return true;
                }
            }
            return false;
        }
    }
    
    public static boolean isInstantiationExpression(Tree.Expression e) {
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
    
    public static boolean hasErrors(Tree.Declaration d) {
        class DeclarationErrorVisitor extends Visitor {
            boolean foundError;
            @Override
            public void visitAny(Node that) {
                super.visitAny(that);
                if (!that.getErrors().isEmpty()) {
                    foundError = true;
                }
            }
            @Override
            public void visit(Tree.Body that) {}
        }
        DeclarationErrorVisitor dev = 
                new DeclarationErrorVisitor();
        d.visit(dev);
        return dev.foundError;
    }

    public static boolean hasErrors(Tree.TypedArgument d) {
        class ArgErrorVisitor extends Visitor {
            boolean foundError;
            @Override
            public void visitAny(Node that) {
                super.visitAny(that);
                if (!that.getErrors().isEmpty()) {
                    foundError = true;
                }
            }
            @Override
            public void visit(Tree.Body that) {}
        }
        ArgErrorVisitor dev = 
                new ArgErrorVisitor();
        d.visit(dev);
        return dev.foundError;
    }

    public static boolean hasErrors(Tree.Body d) {
        class BodyErrorVisitor extends Visitor {
            boolean foundError;
            @Override
            public void visitAny(Node that) {
                super.visitAny(that);
                if (!that.getErrors().isEmpty()) {
                    foundError = true;
                }
            }
            @Override
            public void visit(Tree.Declaration that) {}
        }
        BodyErrorVisitor bev = 
                new BodyErrorVisitor();
        d.visit(bev);
        return bev.foundError;
    }

    static String message(Declaration dec) {
        String qualifier;
        Scope container = dec.getContainer();
        if (container instanceof Declaration) {
            String name = ((Declaration) container).getName();
            qualifier = " in '" + name + "'";
        }
        else {
            qualifier = "";
        }
        return "'" + dec.getName() + "'" + qualifier;
    }
    
    public static Node getParameterTypeErrorNode(Tree.Parameter p) {
        if (p instanceof Tree.ParameterDeclaration) {
            Tree.ParameterDeclaration pd = 
                    (Tree.ParameterDeclaration) p;
            return pd.getTypedDeclaration().getType();
        }
        else {
            return p;
        }
    }
    
    public static Node getTypeErrorNode(Node that) {
        if (that instanceof Tree.TypedDeclaration) {
            Tree.TypedDeclaration td = 
                    (Tree.TypedDeclaration) that;
            Tree.Type type = td.getType();
            if (type!=null) {
                return type;
            }
        }
        if (that instanceof Tree.TypedArgument) {
            Tree.TypedArgument ta = 
                    (Tree.TypedArgument) that;
            Tree.Type type = ta.getType();
            if (type!=null) {
                return type;
            }
        }
        if (that instanceof Tree.FunctionArgument) {
            Tree.FunctionArgument fa = 
                    (Tree.FunctionArgument) that;
            Tree.Type type = fa.getType();
            if (type!=null && type.getToken()!=null) {
                return type;
            }
        }
        return that;
    }

    static void checkIsExactlyForInterop(Unit unit, boolean isCeylon,  
            ProducedType parameterType, ProducedType refinedParameterType, 
            Node node, String message) {
        if (isCeylon) {
            // it must be a Ceylon method
            checkIsExactly(parameterType, refinedParameterType, 
                    node, message, 9200);
        }
        else {
            // we're refining a Java method
            ProducedType refinedDefiniteType = 
                    unit.getDefiniteType(refinedParameterType);
            checkIsExactlyOneOf(parameterType, 
                    refinedParameterType, refinedDefiniteType, 
            		node, message);
        }
    }

    public static ProducedType getTupleType(List<Tree.PositionalArgument> es, 
            Unit unit, boolean requireSequential) {
        ProducedType result = unit.getType(unit.getEmptyDeclaration());
        ProducedType ut = unit.getNothingDeclaration().getType();
        for (int i=es.size()-1; i>=0; i--) {
            Tree.PositionalArgument a = es.get(i);
            ProducedType t = a.getTypeModel();
            if (t!=null) {
                ProducedType et = t; //unit.denotableType(t);
                if (a instanceof Tree.SpreadArgument) {
                    /*if (requireSequential) { 
                        checkSpreadArgumentSequential((Tree.SpreadArgument) a, et);
                    }*/
                    ut = unit.getIteratedType(et);
                    result = spreadType(et, unit, requireSequential);
                }
                else if (a instanceof Tree.Comprehension) {
                    ut = et;
                    Tree.Comprehension c = (Tree.Comprehension) a;
                    Tree.InitialComprehensionClause icc = 
                            c.getInitialComprehensionClause();
                    result = icc.getPossiblyEmpty() ? 
                            unit.getSequentialType(et) : 
                            unit.getSequenceType(et);
                    if (!requireSequential) {
                        ProducedType it = 
                                producedType(unit.getIterableDeclaration(), 
                                        et, icc.getFirstTypeModel());
                        result = intersectionType(result, it, unit);
                    }
                }
                else {
                    ut = unionType(ut, et, unit);
                    result = producedType(unit.getTupleDeclaration(), 
                            ut, et, result);
                }
            }
        }
        return result;
    }
    
    public static ProducedType spreadType(ProducedType et, Unit unit,
            boolean requireSequential) {
        if (et==null) return null;
        if (requireSequential) {
            if (unit.isSequentialType(et)) {
                //if (et.getDeclaration() instanceof TypeParameter) {
                    return et;
                /*}
                else {
                    // if it's already a subtype of Sequential, erase 
                    // out extraneous information, like that it is a
                    // String, just keeping information about what
                    // kind of tuple it is
                    List<ProducedType> elementTypes = unit.getTupleElementTypes(et);
                    boolean variadic = unit.isTupleLengthUnbounded(et);
                    boolean atLeastOne = unit.isTupleVariantAtLeastOne(et);
                    int minimumLength = unit.getTupleMinimumLength(et);
                    if (variadic) {
                        ProducedType spt = elementTypes.get(elementTypes.size()-1);
                        elementTypes.set(elementTypes.size()-1, unit.getIteratedType(spt));
                    }
                    return unit.getTupleType(elementTypes, variadic, 
                            atLeastOne, minimumLength);
                }*/
            }
            else {
                // transform any Iterable into a Sequence without
                // losing the information that it is nonempty, in
                // the case that we know that for sure
                ProducedType it = unit.getIteratedType(et);
                ProducedType st = 
                        unit.isNonemptyIterableType(et) ?
                                unit.getSequenceType(it) :
                                unit.getSequentialType(it);
                // unless this is a tuple constructor, remember
                // the original Iterable type arguments, to
                // account for the possibility that the argument
                // to Absent is a type parameter
                //return intersectionType(et.getSupertype(unit.getIterableDeclaration()), st, unit);
                // for now, just return the sequential type:
                return st;
            }
        }
        else {
            return et;
        }
    }

    static boolean isSelfReference(Tree.Primary that) {
        return that instanceof Tree.This || 
                that instanceof Tree.Outer;
    }

    static boolean isEffectivelyBaseMemberExpression(Tree.Term term) {
        return term instanceof Tree.BaseMemberExpression ||
                term instanceof Tree.QualifiedMemberExpression &&
                isSelfReference(((Tree.QualifiedMemberExpression) term)
                        .getPrimary());
    }

    static boolean setTypeConstructor(Tree.Type t,
            TypeParameter typeParam) {
        ProducedType pt = t.getTypeModel();
        if (pt == null) {
            return false;
        }
        else {
            if (t instanceof Tree.IntersectionType) {
                Tree.IntersectionType it = 
                        (Tree.IntersectionType) t;
                for (Tree.StaticType st: it.getStaticTypes()) {
                    if (setTypeConstructor(st,typeParam)) {
                        pt.setTypeConstructor(true);
                        pt.setTypeConstructorParameter(typeParam);
                    }
                }
            }
            else if (t instanceof Tree.UnionType) {
                Tree.UnionType it = 
                        (Tree.UnionType) t;
                for (Tree.StaticType st: it.getStaticTypes()) {
                    if (setTypeConstructor(st,typeParam)) {
                        pt.setTypeConstructor(true);
                        pt.setTypeConstructorParameter(typeParam);
                    }
                }
            }
            else if (t instanceof Tree.SimpleType) {
                Tree.SimpleType s = 
                        (Tree.SimpleType) t;
                if (s.getTypeArgumentList()==null) {
                    pt.setTypeConstructor(true);
                    pt.setTypeConstructorParameter(typeParam);
                }
            }
            return pt.isTypeConstructor();
        }
    }
}
