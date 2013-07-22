package com.redhat.ceylon.compiler.typechecker.analyzer;

import java.util.List;

import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Type;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

/**
 * Validates the position in which covariant and contravariant
 * type parameters appear in the schemas of declarations.
 * 
 * @author Gavin King
 *
 */
public class TypeArgumentVisitor extends Visitor {
    
    private boolean contravariant = false;
    private Declaration parameterizedDeclaration;
    
    private void flip() {
        contravariant = !contravariant;
    }
    
    @Override public void visit(Tree.ParameterList that) {
        flip();
        super.visit(that);
        flip();
    }
    
    @Override public void visit(Tree.TypeConstraint that) {
        super.visit(that);
        TypeParameter dec = that.getDeclarationModel();
        if (dec!=null) {
            parameterizedDeclaration = dec.getDeclaration();
            flip();
            if (that.getSatisfiedTypes()!=null) {
                for (Tree.Type type: that.getSatisfiedTypes().getTypes()) {
                    check(type.getTypeModel(), type, false, null);
                    checkSupertype(type);
                }
            }
            flip();
            parameterizedDeclaration = null;
        }
    }
    
    @Override public void visit(Tree.Parameter that) {
        boolean topLevel = parameterizedDeclaration==null;
        if (topLevel) {
            parameterizedDeclaration = that.getParameterModel().getDeclaration();
        }
        super.visit(that);
        check(that.getParameterModel().getType(), that, false, parameterizedDeclaration);
        if (topLevel) {
            parameterizedDeclaration = null;
        }
    }
    
    @Override public void visit(Tree.TypedDeclaration that) {
        super.visit(that);
        if (!(that instanceof Tree.Variable)) {
            Type type = that.getType();
            if (type!=null) {
                check(type.getTypeModel(), 
                        type, 
                        that.getDeclarationModel().isVariable(), 
                        that.getDeclarationModel());
            }
        }
    }
    
    @Override public void visit(Tree.ClassOrInterface that) {
        super.visit(that);
        if (that.getSatisfiedTypes()!=null) {
            for (Tree.Type type: that.getSatisfiedTypes().getTypes()) {
                check(type.getTypeModel(), type, false, null);
                checkSupertype(type);
            }
        }
    }
    
    @Override public void visit(Tree.ClassDeclaration that) {
        super.visit(that);
        if (that.getClassSpecifier()!=null) {
            Tree.SimpleType type = that.getClassSpecifier().getType();
            check(type.getTypeModel(), type, false, null);
        }
    }
    
    @Override public void visit(Tree.InterfaceDeclaration that) {
        super.visit(that);
        if (that.getTypeSpecifier()!=null) {
            Tree.StaticType type = that.getTypeSpecifier().getType();
            check(type.getTypeModel(), type, false, null);
        }
    }
    
    @Override public void visit(Tree.TypeAliasDeclaration that) {
        super.visit(that);
        if (that.getTypeSpecifier()!=null) {
            Tree.StaticType type = that.getTypeSpecifier().getType();
            check(type.getTypeModel(), type, false, null);
        }
    }
    
    @Override public void visit(Tree.AnyClass that) {
        super.visit(that);
        if (that.getExtendedType()!=null) {
            Tree.SimpleType type = that.getExtendedType().getType();
            check(type.getTypeModel(), type, false, null);
            checkSupertype(type);
        }
    }
    
    @Override public void visit(Tree.FunctionArgument that) {}

    private void check(ProducedType type, Node that, boolean variable, Declaration d) {
        if (that!=null && (d==null || d.isShared() || d.getOtherInstanceAccess())) {
			if (type!=null) {
				List<TypeParameter> errors = type.checkVariance(!contravariant && !variable, 
						contravariant && !variable, parameterizedDeclaration);
			    displayErrors(that, type, errors);
			}
        }
    }

	private void displayErrors(Node that, ProducedType type,
			List<TypeParameter> errors) {
		for (TypeParameter td: errors) {
			String var; String loc;
			if ( td.isContravariant() ) {
				var = "contravariant (in)";
				loc = "covariant";
			}
			else if ( td.isCovariant() ) {
				var = "covariant (out)";
				loc = "contravariant";
			}
			else {
				throw new RuntimeException();
			}
		    that.addError(var + " type parameter " + td.getName() + 
		    		" appears in " + loc + " location in type: " + 
		    		type.getProducedTypeName(that.getUnit()));
		}
	}
    
    private void checkSupertype(Tree.Type that) {
        if (that!=null) {
            checkSupertype(that.getTypeModel(), that);
        }
    }
    
    private void checkSupertype(ProducedType type, Node that) {
        if (type!=null) {
        	List<TypeDeclaration> errors = type.resolveAliases().checkDecidability();
            for (TypeDeclaration td: errors) {
                that.addError("type with contravariant type parameter " + td.getName() + 
                		" appears in contravariant location in supertype: " + 
                		type.getProducedTypeName(that.getUnit()));
            }
        }
    }
    
}
