package com.redhat.ceylon.compiler.typechecker.analyzer;

import static com.redhat.ceylon.compiler.typechecker.model.Util.isTypeUnknown;
import static com.redhat.ceylon.compiler.typechecker.tree.Util.name;

import java.util.ArrayList;
import java.util.List;

import com.redhat.ceylon.compiler.typechecker.model.Annotation;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.Unit;
import com.redhat.ceylon.compiler.typechecker.tree.Message;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Expression;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

/**
 * Bucket for some helper methods used by various
 * visitors.
 * 
 * @author Gavin King
 *
 */
class Util {
    
    static TypedDeclaration getBaseDeclaration(Tree.BaseMemberExpression bme, 
            List<ProducedType> signature, boolean ellipsis) {
        Declaration result = bme.getScope().getMemberOrParameter(bme.getUnit(), 
                name(bme.getIdentifier()), signature, ellipsis);
        if (result instanceof TypedDeclaration) {
        	return (TypedDeclaration) result;
        }
        else {
        	return null;
        }
    }
    
    static TypeDeclaration getBaseDeclaration(Tree.BaseType bt) {
        Declaration result = bt.getScope().getMemberOrParameter(bt.getUnit(), 
                name(bt.getIdentifier()), null, false);
        if (result instanceof TypeDeclaration) {
        	return (TypeDeclaration) result;
        }
        else {
        	return null;
        }
    }
    
    static TypeDeclaration getBaseDeclaration(Tree.BaseTypeExpression bte, 
            List<ProducedType> signature, boolean ellipsis) {
        Declaration result = bte.getScope().getMemberOrParameter(bte.getUnit(), 
                name(bte.getIdentifier()), signature, ellipsis);
        if (result instanceof TypeDeclaration) {
        	return (TypeDeclaration) result;
        }
        else {
        	return null;
        }
    }
    
    static void checkTypeBelongsToContainingScope(ProducedType type,
            Scope scope, Node that) {
        //TODO: this does not account for types 
        //      inherited by a containing scope!
        //TODO: what if the type arguments don't match?!
        while (scope!=null) {
            if (type.getDeclaration().getContainer()==scope) {
                return;
            }
            scope=scope.getContainer();
        }
        that.addError("illegal use of qualified type outside scope of qualifying type: " + 
                type.getProducedTypeName(that.getUnit()));
    }

    static List<ProducedType> getTypeArguments(Tree.TypeArguments tal,
    		List<TypeParameter> typeParameters) {
        List<ProducedType> typeArguments = new ArrayList<ProducedType>();
        if (tal instanceof Tree.TypeArgumentList) {
            for (Tree.Type ta: ((Tree.TypeArgumentList) tal).getTypes()) {
                ProducedType t = ta.getTypeModel();
                if (t==null) {
//                    ta.addError("could not resolve type argument");
                    typeArguments.add(null);
                }
                else {
                    typeArguments.add(t);
                }
            }
            for (int i=typeArguments.size(); 
            		i<typeParameters.size(); i++) {
            	ProducedType dta = typeParameters.get(i).getDefaultTypeArgument();
            	if (dta==null) {
            		break;
            	}
            	else {
            		//TODO: substitute previous args 
            		//      into the default arg
            		typeArguments.add(dta);
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
    
    static Tree.Statement getLastExecutableStatement(Tree.ClassBody that) {
        List<Tree.Statement> statements = that.getStatements();
        for (int i=statements.size()-1; i>=0; i--) {
            Tree.Statement s = statements.get(i);
            if (s instanceof Tree.SpecifierStatement) {
            	//shortcut refinement statements with => aren't really "executable"
            	Tree.SpecifierStatement ss = (Tree.SpecifierStatement) s;
				if (!(ss.getSpecifierExpression() instanceof Tree.LazySpecifierExpression) || 
						!ss.getRefinement()) {
            		return s;
            	}
            }
            else if (s instanceof Tree.ExecutableStatement) {
                return s;
            }
            else {
                if (s instanceof Tree.AttributeDeclaration) {
                    Tree.SpecifierOrInitializerExpression sie = ((Tree.AttributeDeclaration) s).getSpecifierOrInitializerExpression();
					if (sie!=null && !(sie instanceof Tree.LazySpecifierExpression)) {
                        return s;
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
                        Unit unit = that.getUnit();
						if (et!=null 
                                && !et.getDeclaration().equals(unit.getObjectDeclaration())
                                && !et.getDeclaration().equals(unit.getBasicDeclaration())) {
                            return s;
                        }
                    }
                    if (o.getClassBody()!=null) {
                        if (getLastExecutableStatement(o.getClassBody())!=null) {
                            return s;
                        }
                    }
                }
            }
        }
        return null;
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
        if (isTypeUnknown(type)) {
            addTypeUnknownError(node, message);
            return false;
        }
        else if (!type.getDeclaration().getUnit().isCallableType(type)) {
            if (!hasError(node)) {
                node.addError(message + message(type, " is not a subtype of Callable", node.getUnit()));
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

    static void checkAssignable(ProducedType type, ProducedType supertype, 
            TypeDeclaration td, Node node, String message) {
        if (isTypeUnknown(type) || isTypeUnknown(supertype)) {
            addTypeUnknownError(node, message);
        }
        else if (!type.isSubtypeOf(supertype)) {
            node.addError(message + message(type, " is not assignable to ", supertype, node.getUnit()));
        }
    }

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

    static String formatPath(List<Tree.Identifier> nodes) {
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

    static void buildAnnotations(Tree.AnnotationList al, List<Annotation> annotations) {
        if (al!=null) {
            for (Tree.Annotation a: al.getAnnotations()) {
                Annotation ann = new Annotation();
                String name = ( (Tree.BaseMemberExpression) a.getPrimary() ).getIdentifier().getText();
                ann.setName(name);
                if (a.getNamedArgumentList()!=null) {
                    for ( Tree.NamedArgument na: a.getNamedArgumentList().getNamedArguments() ) {
                        if (na instanceof Tree.SpecifiedArgument) {
                            Expression e = ((Tree.SpecifiedArgument) na).getSpecifierExpression().getExpression();
                            if (e!=null) {
                                Tree.Term t = e.getTerm();
                                String param = ((Tree.SpecifiedArgument) na).getIdentifier().getText();
                        		String text = toString(t);
                        		if (text!=null) {
                                    ann.addNamedArgument(param, text);
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
    	else {
    		return null;
    	}
    }

}
