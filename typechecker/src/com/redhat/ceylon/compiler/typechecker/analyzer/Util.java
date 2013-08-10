package com.redhat.ceylon.compiler.typechecker.analyzer;

import static com.redhat.ceylon.compiler.typechecker.model.Util.isTypeUnknown;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.Map;

import com.redhat.ceylon.compiler.typechecker.model.Annotation;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ProducedReference;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.TypeAlias;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.Unit;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.tree.Message;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

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
        Declaration member = d.getMember(name, unit, signature, ellipsis);
        if (member instanceof TypedDeclaration)
            return (TypedDeclaration) member;
        else
            return null;
    }

    static TypeDeclaration getTypeMember(TypeDeclaration d, String name,
            List<ProducedType> signature, boolean ellipsis, Unit unit) {
        Declaration member = d.getMember(name, unit, signature, ellipsis);
        if (member instanceof TypeDeclaration)
            return (TypeDeclaration) member;
        else
            return null;
    }

    static TypedDeclaration getTypedDeclaration(Scope scope,
            String name, List<ProducedType> signature, boolean ellipsis,
            Unit unit) {
        Declaration result = scope.getMemberOrParameter(unit, 
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
        Declaration result = scope.getMemberOrParameter(unit, 
                name, signature, ellipsis);
        if (result instanceof TypeDeclaration) {
        	return (TypeDeclaration) result;
        }
        else {
        	return null;
        }
    }
    
    static List<ProducedType> getTypeArguments(Tree.TypeArguments tal,
    		List<TypeParameter> typeParameters, ProducedType qt) {
        List<ProducedType> typeArguments = new ArrayList<ProducedType>();
        if (tal instanceof Tree.TypeArgumentList) {
            Map<TypeParameter, ProducedType> typeArgMap = new HashMap<TypeParameter, ProducedType>();
            if (qt!=null) {
                typeArgMap.putAll(qt.getTypeArguments());
            }
            List<Tree.Type> types = ((Tree.TypeArgumentList) tal).getTypes();
            for (int i=0; i<types.size(); i++) {
                ProducedType t = types.get(i).getTypeModel();
                if (t==null) {
                    typeArguments.add(null);
                }
                else {
                    typeArguments.add(t);
                    if (i<typeParameters.size()) {
                        typeArgMap.put(typeParameters.get(i), t);
                    }
                }
            }
            for (int i=typeArguments.size(); 
            		i<typeParameters.size(); i++) {
                TypeParameter tp = typeParameters.get(i);
            	ProducedType dta = tp.getDefaultTypeArgument();
            	if (dta==null) {
            		break;
            	}
            	else {
            	    ProducedType da = dta.substitute(typeArgMap);
            		typeArguments.add(da);
            		typeArgMap.put(tp, da);
            	}
            }
        }
        return typeArguments;
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
            if (isExecutableStatement(unit, s)) {
                return s;
            }
        }
        return null;
    }

    static boolean isExecutableStatement(Unit unit, Tree.Statement s) {
        if (s instanceof Tree.SpecifierStatement) {
        	//shortcut refinement statements with => aren't really "executable"
        	Tree.SpecifierStatement ss = (Tree.SpecifierStatement) s;
        	if (!(ss.getSpecifierExpression() instanceof Tree.LazySpecifierExpression) || 
        			!ss.getRefinement()) {
        		return true;
        	}
        }
        else if (s instanceof Tree.ExecutableStatement) {
            return true;
        }
        else {
            if (s instanceof Tree.AttributeDeclaration) {
                Tree.SpecifierOrInitializerExpression sie = ((Tree.AttributeDeclaration) s).getSpecifierOrInitializerExpression();
        		if (sie!=null && !(sie instanceof Tree.LazySpecifierExpression)) {
                    return true;
                }
            }
            /*if (s instanceof Tree.MethodDeclaration) {
                if ( ((Tree.MethodDeclaration) s).getSpecifierExpression()!=null ) {
                    return s;
                }
            }*/
            if (s instanceof Tree.ObjectDefinition) {
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
            }
        }
        return false;
    }
    
    private static String message(ProducedType type, String problem, ProducedType otherType, Unit unit) {
        String typeName = type.getProducedTypeName(unit);
        String otherTypeName = otherType.getProducedTypeName(unit);
        if (otherTypeName.equals(typeName)) {
            typeName = type.getProducedTypeQualifiedName();
            otherTypeName = otherType.getProducedTypeQualifiedName();
        }
        return ": " + typeName + problem + otherTypeName;
    }
    
    private static String message(ProducedType type, String problem, Unit unit) {
        String typeName = type.getProducedTypeName(unit);
        return ": " + typeName + problem;
    }
    
    static boolean checkCallable(ProducedType type, Node node, String message) {
        Unit unit = node.getUnit();
        if (isTypeUnknown(type)) {
            addTypeUnknownError(node, message);
            return false;
        }
        else if (!unit.isCallableType(type)) {
            if (!hasError(node)) {
                String extra = message(type, " is not a subtype of Callable", unit);
                if (node instanceof Tree.StaticMemberOrTypeExpression) {
                    Declaration d = ((Tree.StaticMemberOrTypeExpression) node).getDeclaration();
                    if (d instanceof Interface) {
                        extra = ": " + d.getName() + " is an interface";
                    }
                    else if (d instanceof TypeAlias) {
                        extra = ": " + d.getName() + " is a type alias";
                    }
                    else if (d instanceof TypeParameter) {
                        extra = ": " + d.getName() + " is a type parameter";
                    }
                    else if (d instanceof Value) {
                        extra = ": value " + d.getName() + " has type " + 
                                type.getProducedTypeName(unit) + 
                                " which is not a subtype of Callable";
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

    static ProducedType checkSupertype(ProducedType pt, TypeDeclaration td, 
            Node node, String message) {
        if (isTypeUnknown(pt)) {
            addTypeUnknownError(node, message);
            return null;
        }
        else {
            ProducedType supertype = pt.getSupertype(td);
            if (supertype==null) {
                node.addError(message + message(pt, " is not a subtype of " + td.getName(), node.getUnit()));
            }
            return supertype;
        }
    }

    static void checkAssignable(ProducedType type, ProducedType supertype, 
            Node node, String message) {
        if (isTypeUnknown(type) || isTypeUnknown(supertype)) {
        	addTypeUnknownError(node, message);
        }
        else if (!type.isSubtypeOf(supertype)) {
        	node.addError(message + message(type, " is not assignable to ", supertype, node.getUnit()));
        }
    }

    static void checkAssignableWithWarning(ProducedType type, ProducedType supertype, 
            Node node, String message) {
        if (isTypeUnknown(type) || isTypeUnknown(supertype)) {
        	addTypeUnknownError(node, message);
        }
        else if (!type.isSubtypeOf(supertype)) {
        	node.addWarning(message + message(type, " is not assignable to ", supertype, node.getUnit()));
        }
    }

    static void checkAssignableToOneOf(ProducedType type, ProducedType supertype1, ProducedType supertype2, 
            Node node, String message) {
        if (isTypeUnknown(type) || isTypeUnknown(supertype1) || isTypeUnknown(supertype2)) {
            addTypeUnknownError(node, message);
        }
        else if (!type.isSubtypeOf(supertype1)
                && !type.isSubtypeOf(supertype2)) {
            node.addError(message + message(type, " is not assignable to ", supertype1, node.getUnit()));
        }
    }

    static void checkAssignable(ProducedType type, ProducedType supertype, 
            Node node, String message, int code) {
        if (isTypeUnknown(type) || isTypeUnknown(supertype)) {
            addTypeUnknownError(node, message);
        }
        else if (!type.isSubtypeOf(supertype)) {
            node.addError(message + message(type, " is not assignable to ", supertype, node.getUnit()), code);
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

    static void checkIsExactly(ProducedType type, ProducedType supertype, 
            Node node, String message) {
        if (isTypeUnknown(type) || isTypeUnknown(supertype)) {
            addTypeUnknownError(node, message);
        }
        else if (!type.isExactly(supertype)) {
            node.addError(message + message(type, " is not exactly ", supertype, node.getUnit()));
        }
    }

    static void checkIsExactlyOneOf(ProducedType type, ProducedType supertype1, ProducedType supertype2, 
            Node node, String message) {
        if (isTypeUnknown(type) || isTypeUnknown(supertype1) || isTypeUnknown(supertype2)) {
            addTypeUnknownError(node, message);
        }
        else if (!type.isExactly(supertype1)
                && !type.isExactly(supertype2)) {
            node.addError(message + message(type, " is not exactly ", supertype1, node.getUnit()));
        }
    }

    private static boolean hasError(Node node) {
        // we use an exception to get out of the visitor as fast as possible
        // when an error is found
        @SuppressWarnings("serial")
        class ErrorFoundException extends RuntimeException{}
        class ErrorVisitor extends Visitor {
            @Override
            public void handleException(Exception e, Node that) {
                if(e instanceof ErrorFoundException){
                    // rethrow
                    throw (RuntimeException)e;
                }
                super.handleException(e, that);
            }
            @Override
            public void visitAny(Node that) {
                if (that.getErrors().isEmpty()) {
                    super.visitAny(that);
                }
                else {
                    // UsageWarning don't count as errors
                    for(Message error : that.getErrors()){
                        if(!(error instanceof UsageWarning)){
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
        try{
            node.visit(ev);
            return false;
        }catch(ErrorFoundException x){
            return true;
        }
    }

    private static void addTypeUnknownError(Node node, String message) {
        if (!hasError(node)) {
            node.addError(message + ": type cannot be determined");
        }
    }

    static void buildAnnotations(Tree.AnnotationList al, List<Annotation> annotations) {
        if (al!=null) {
            Tree.AnonymousAnnotation aa = al.getAnonymousAnnotation();
            if (aa!=null) {
                Annotation ann = new Annotation();
                ann.setName("doc");
                ann.addPositionalArgment(aa.getStringLiteral().getText());
                annotations.add(ann);
            }
            for (Tree.Annotation a: al.getAnnotations()) {
                Annotation ann = new Annotation();
                String name = ( (Tree.BaseMemberExpression) a.getPrimary() ).getIdentifier().getText();
                ann.setName(name);
                if (a.getNamedArgumentList()!=null) {
                    for ( Tree.NamedArgument na: a.getNamedArgumentList().getNamedArguments() ) {
                        if (na instanceof Tree.SpecifiedArgument) {
                            Tree.Expression e = ((Tree.SpecifiedArgument) na).getSpecifierExpression().getExpression();
                            if (e!=null) {
                                Tree.Term t = e.getTerm();
                                Parameter p = ((Tree.SpecifiedArgument) na).getParameter();
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
                if (a.getPositionalArgumentList()!=null) {
                    for ( Tree.PositionalArgument pa: a.getPositionalArgumentList().getPositionalArguments() ) {
                    	if (pa instanceof Tree.ListedArgument) {
                    		Tree.Term t = ((Tree.ListedArgument) pa).getExpression().getTerm();
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
    		Tree.StaticMemberOrTypeExpression mte = (Tree.StaticMemberOrTypeExpression) t;
    		String id = mte.getIdentifier().getText();
    		if (mte instanceof Tree.QualifiedMemberOrTypeExpression) {
    			Tree.Primary p = ((Tree.QualifiedMemberOrTypeExpression) mte).getPrimary();
    			if (p instanceof Tree.StaticMemberOrTypeExpression) {
    				return toString((Tree.StaticMemberOrTypeExpression) p) + '.' + id;
    			}
    			return null;
    		}
    		else {
    			return id;
    		}
    	}
    	else if (t instanceof Tree.TypeLiteral) {
    	    Tree.TypeLiteral tl = (Tree.TypeLiteral) t;
    	    if(tl.getType() != null)
    	        return toString(tl.getType());
    	    return null;
    	}
        else if (t instanceof Tree.MemberLiteral) {
            Tree.MemberLiteral ml = (Tree.MemberLiteral) t;
            if(ml.getType() != null){
                String qualifier = toString(ml.getType());
                if(qualifier != null)
                    return qualifier + "." + ml.getIdentifier().getText();
                return null;
            }
            return ml.getIdentifier().getText();
        }
    	else {
    		return null;
    	}
    }

    private static String toString(Tree.StaticType type) {
        // FIXME: we're discarding syntactic types and union/intersection types
        if(type instanceof Tree.BaseType){
            return ((Tree.BaseType) type).getIdentifier().getText();
        }else if(type instanceof Tree.QualifiedType){
            String qualifier = toString(((Tree.QualifiedType) type).getOuterType());
            if(qualifier != null)
                return qualifier + "." + ((Tree.SimpleType)type).getIdentifier().getText();
            return null;
        }
        return null;
    }

    static boolean inLanguageModule(Unit unit) {
        return unit.getPackage().getQualifiedNameString()
                .startsWith(Module.LANGUAGE_MODULE_NAME);
    }

    static String typeDescription(TypeDeclaration td, Unit unit) {
        if (td instanceof TypeParameter) {
            Declaration container = (Declaration) td.getContainer();
            return "type parameter " + td.getName() + " of " + 
                    container.getName(unit);
        }
        else {
            return "type " + td.getName();
        }
    }

    static StringBuilder typeNamesAsIntersection(
            List<ProducedType> upperBounds, Unit unit) {
        StringBuilder sb = new StringBuilder();
        for (ProducedType st: upperBounds) {
            sb.append(st.getProducedTypeName(unit)).append(" & ");
        }
        if (sb.toString().endsWith(" & ")) {
            sb.setLength(sb.length()-3);
        }
        return sb;
    }

    public static ProducedType getParameterTypesAsTupleType(Unit unit, 
            List<Parameter> params, ProducedReference pr) {
        List<ProducedType> paramTypes = new ArrayList<ProducedType>();
        int max = params.size()-1;
        int firstDefaulted = -1;
        boolean sequenced = false;
        boolean atLeastOne = false;
        for (int i=0; i<=max; i++) {
            Parameter p = params.get(i);
            ProducedType ft = pr.getTypedParameter(p).getFullType();
            if (firstDefaulted<0 && p.isDefaulted()) {
                firstDefaulted = i;
            }
            if (i==max && p.isSequenced()) {
                sequenced = true;
                atLeastOne = p.isAtLeastOne();
                if (ft!=null) {
                    ft = unit.getIteratedType(ft);
                }
            }
            paramTypes.add(ft);
        }
        return unit.getTupleType(paramTypes, sequenced, atLeastOne, 
                firstDefaulted);
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
                Tree.Expression ex = ((Tree.BooleanCondition) c).getExpression();
                if (ex!=null) {
                    Tree.Term t = ex.getTerm();
                    //TODO: eliminate parens
                    //TODO: take into account conjunctions/disjunctions
                    if (t instanceof Tree.BaseMemberExpression) {
                        Declaration d = ((Tree.BaseMemberExpression) t).getDeclaration();
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

    static boolean isBooleanTrue(Declaration d) {
        return d!=null && d.getQualifiedNameString()
                .equals("ceylon.language::true");
    }

    static boolean isBooleanFalse(Declaration d) {
        return d!=null && d.getQualifiedNameString()
                .equals("ceylon.language::false");
    }

    static boolean isNeverSatisfied(Tree.ConditionList cl) {
        if (cl==null) return false;
        for (Tree.Condition c: cl.getConditions()) {
            if (c instanceof Tree.BooleanCondition) {
                Tree.Expression ex = ((Tree.BooleanCondition) c).getExpression();
                if (ex!=null) {
                    Tree.Term t = ex.getTerm();
                    //TODO: eliminate parens
                    //TODO: take into account conjunctions/disjunctions
                    if (t instanceof Tree.BaseMemberExpression) {
                        Declaration d = ((Tree.BaseMemberExpression) t).getDeclaration();
                        if (isBooleanFalse(d)) {
                            return true;
                        }
                    }
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
        Tree.Term p = unwrapExpressionUntilTerm(that.getPrimary());
        if (p instanceof Tree.MemberOrTypeExpression) {
            Tree.MemberOrTypeExpression mte = (Tree.MemberOrTypeExpression) p;
            ProducedReference prf = mte.getTarget();
            return mte.getStaticMethodReference() ||
                    prf==null || 
                    !prf.isFunctional() || 
                    //type parameters are not really callable even though they are Functional
                    prf.getDeclaration() instanceof TypeParameter;
        }
        else {
           return true;
        }
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
        DeclarationErrorVisitor dev = new DeclarationErrorVisitor();
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
        ArgErrorVisitor dev = new ArgErrorVisitor();
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
        BodyErrorVisitor bev = new BodyErrorVisitor();
        d.visit(bev);
        return bev.foundError;
    }

}
