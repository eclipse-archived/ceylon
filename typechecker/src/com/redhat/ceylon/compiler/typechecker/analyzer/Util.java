package com.redhat.ceylon.compiler.typechecker.analyzer;

import static com.redhat.ceylon.compiler.typechecker.tree.Util.name;

import java.util.ArrayList;
import java.util.List;

import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.UnknownType;
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
class Util extends Visitor {
    
    static TypedDeclaration getBaseDeclaration(Tree.BaseMemberExpression bme) {
        Declaration result = bme.getScope().getMemberOrParameter(bme.getUnit(), 
                name(bme.getIdentifier()));
        if (result instanceof TypedDeclaration) {
        	return (TypedDeclaration) result;
        }
        else {
        	return null;
        }
    }
    
    static TypeDeclaration getBaseDeclaration(Tree.BaseType bt) {
        Declaration result = bt.getScope().getMemberOrParameter(bt.getUnit(), 
                name(bt.getIdentifier()));
        if (result instanceof TypeDeclaration) {
        	return (TypeDeclaration) result;
        }
        else {
        	return null;
        }
    }
    
    static TypeDeclaration getBaseDeclaration(Tree.BaseTypeExpression bte) {
        Declaration result = bte.getScope().getMemberOrParameter(bte.getUnit(), 
                name(bte.getIdentifier()));
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
                type.getProducedTypeName());
    }

    static List<ProducedType> getTypeArguments(Tree.TypeArguments tal) {
        List<ProducedType> typeArguments = new ArrayList<ProducedType>();
        if (tal instanceof Tree.TypeArgumentList) {
            for (Tree.Type ta: ( (Tree.TypeArgumentList) tal ).getTypes()) {
                ProducedType t = ta.getTypeModel();
                if (t==null) {
                    ta.addError("could not resolve type argument");
                    typeArguments.add(null);
                }
                else {
                    typeArguments.add(t);
                }
            }
        }
        return typeArguments;
    }
    
    static Tree.Statement getLastExecutableStatement(Tree.ClassBody that) {
        List<Tree.Statement> statements = that.getStatements();
        for (int i=statements.size()-1; i>=0; i--) {
            Tree.Statement s = statements.get(i);
            if (s instanceof Tree.ExecutableStatement) {
                return s;
            }
            else {
                if (s instanceof Tree.AttributeDeclaration) {
                    if ( ((Tree.AttributeDeclaration) s).getSpecifierOrInitializerExpression()!=null ) {
                        return s;
                    }
                }
                if (s instanceof Tree.MethodDeclaration) {
                    if ( ((Tree.MethodDeclaration) s).getSpecifierExpression()!=null ) {
                        return s;
                    }
                }
                if (s instanceof Tree.ObjectDefinition) {
                    Tree.ObjectDefinition o = (Tree.ObjectDefinition) s;
                    if (o.getExtendedType()!=null) {
                        ProducedType et = o.getExtendedType().getType().getTypeModel();
                        if (et!=null 
                                && !et.getDeclaration().equals(that.getUnit().getObjectDeclaration())
                                && !et.getDeclaration().equals(that.getUnit().getIdentifiableObjectDeclaration())) {
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
            
    static void checkAssignable(ProducedType type, ProducedType supertype, 
            Node node, String message) {
        if (type==null||supertype==null) {
        	//this is always a bug now, i suppose?
            node.addError(message);
        }
        else if (!type.isSubtypeOf(supertype)) {
        	if (type.getDeclaration() instanceof UnknownType) {
            	node.addError(message + ": type of expression cannot be determined");
            }
        	else {
	            node.addError(message + ": " + type.getProducedTypeName() + 
	                    " is not assignable to " + supertype.getProducedTypeName());
        	}
        }
    }

    static void checkAssignable(ProducedType type, ProducedType supertype, 
            TypeDeclaration td, Node node, String message) {
        if (type==null||supertype==null) {
            node.addError(message);
        }
        else if (!type.isSubtypeOf(supertype, td)) {
            node.addError(message + ": " + type.getProducedTypeName() + 
                    " is not assignable to " + supertype.getProducedTypeName());
        }
    }

    static void checkIsExactly(ProducedType type, ProducedType supertype, 
            Node node, String message) {
        if (type==null||supertype==null) {
            node.addError(message + ": type not known");
        }
        else if (!type.isExactly(supertype)) {
            node.addError(message + ": " + type.getProducedTypeName() + 
                    " is not exactly " + supertype.getProducedTypeName());
        }
    }

}
