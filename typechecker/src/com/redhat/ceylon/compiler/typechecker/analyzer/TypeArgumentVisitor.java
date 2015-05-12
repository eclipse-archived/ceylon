package com.redhat.ceylon.compiler.typechecker.analyzer;

import java.util.List;

import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.model.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.MethodOrValue;
import com.redhat.ceylon.model.typechecker.model.ProducedType;
import com.redhat.ceylon.model.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.model.typechecker.model.TypeParameter;
import com.redhat.ceylon.model.typechecker.model.TypedDeclaration;

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
        
    @Override public void visit(Tree.TypeConstraint that) {
        super.visit(that);
        TypeParameter dec = that.getDeclarationModel();
        if (dec!=null) {
            parameterizedDeclaration = dec.getDeclaration();
            flip();
            if (that.getSatisfiedTypes()!=null) {
                for (Tree.Type type: 
                        that.getSatisfiedTypes().getTypes()) {
                    //TODO: is "null" really correct here?!
                    check(type, false, null);
                }
            }
            flip();
            parameterizedDeclaration = null;
        }
    }
        
    @Override public void visit(Tree.TypedDeclaration that) {
        super.visit(that);
        TypedDeclaration dec = that.getDeclarationModel();
		if (!(that instanceof Tree.Variable)) {
            check(that.getType(), dec.isVariable(), dec);
        }
        if (dec.isParameter()) {
        	flip();
            boolean topLevel = 
                    parameterizedDeclaration==null; //i.e. toplevel parameter in a parameter declaration
            if (topLevel) {
            	parameterizedDeclaration = 
            	        ((MethodOrValue) dec).getInitializerParameter()
            	                .getDeclaration();
            }
			check(that.getType(), false, 
			        parameterizedDeclaration);
			super.visit(that);
			if (topLevel) {
				parameterizedDeclaration = null;
			}
        	flip();
        }
    }
    
    @Override public void visit(Tree.ClassOrInterface that) {
        super.visit(that);
        if (that.getSatisfiedTypes()!=null) {
            for (Tree.Type type: 
                    that.getSatisfiedTypes().getTypes()) {
                check(type, false, that.getDeclarationModel());
            }
        }
    }
    
    @Override public void visit(Tree.ClassDeclaration that) {
        super.visit(that);
        if (that.getClassSpecifier()!=null) {
            check(that.getClassSpecifier().getType(), false, 
                    that.getDeclarationModel());
        }
    }
    
    @Override public void visit(Tree.InterfaceDeclaration that) {
        super.visit(that);
        if (that.getTypeSpecifier()!=null) {
            check(that.getTypeSpecifier().getType(), false, 
                    that.getDeclarationModel());
        }
    }
    
    @Override public void visit(Tree.TypeAliasDeclaration that) {
        super.visit(that);
        if (that.getTypeSpecifier()!=null) {
            check(that.getTypeSpecifier().getType(), false, 
                    that.getDeclarationModel());
        }
    }
    
    @Override public void visit(Tree.AnyClass that) {
        super.visit(that);
        if (that.getExtendedType()!=null) {
            check(that.getExtendedType().getType(), false, 
                    that.getDeclarationModel());
        }
    }
    
    @Override public void visit(Tree.ObjectDefinition that) {
        super.visit(that);
        if (that.getExtendedType()!=null) {
            check(that.getExtendedType().getType(), false, 
                    that.getDeclarationModel());
        }
        if (that.getSatisfiedTypes()!=null) {
            for (Tree.Type type: 
                    that.getSatisfiedTypes().getTypes()) {
                check(type, false, that.getDeclarationModel());
            }
        }
    }
    
    @Override public void visit(Tree.ObjectExpression that) {
        super.visit(that);
        if (that.getExtendedType()!=null) {
            check(that.getExtendedType().getType(), false, 
                    that.getAnonymousClass());
        }
        if (that.getSatisfiedTypes()!=null) {
            for (Tree.Type type: 
                    that.getSatisfiedTypes().getTypes()) {
                check(type, false, that.getAnonymousClass());
            }
        }
    }
    
    private ClassOrInterface constructorClass;
    
    @Override public void visit(Tree.Constructor that) {
        ClassOrInterface occ = constructorClass;
        constructorClass = 
                that.getDeclarationModel()
                    .getExtendedTypeDeclaration();
        super.visit(that);
        constructorClass = occ;
    }
    
//    @Override public void visit(Tree.FunctionArgument that) {}

    private void check(Tree.Type that, boolean variable, 
            Declaration d) {
        if (that!=null) {
            check(that.getTypeModel(), variable, d, that);
        }
    }

    private void check(ProducedType type, boolean variable, 
            Declaration d, Node that) {
        if (type!=null) {
            List<TypeParameter> errors = 
                    type.checkVariance(!contravariant && !variable, 
                            contravariant && !variable, 
                            parameterizedDeclaration);
            displayErrors(that, type, errors, d);
        }
    }

    private void displayErrors(Node that, ProducedType type,
            List<TypeParameter> errors, Declaration d) {
        for (TypeParameter tp: errors) {
            Declaration declaration = tp.getDeclaration();
            if (d==null || d.isShared() || d.getOtherInstanceAccess() 
                    || declaration.equals(d))
            if (constructorClass==null ||
                    !declaration.equals(constructorClass)) {
                String var; String loc;
                if (tp.isContravariant()) {
                    var = "contravariant ('in')";
                    loc = "covariant or invariant";
                }
                else if (tp.isCovariant()) {
                    var = "covariant ('out')";
                    loc = "contravariant or invariant";
                }
                else {
                    throw new RuntimeException();
                }
                that.addError(var + " type parameter '" + tp.getName() + 
                        "' of '" + declaration.getName() +
                        "' appears in " + loc + " location in type: '" + 
                        type.getProducedTypeName(that.getUnit()) + "'");
            }
        }
    }
    
    @Override
    public void visit(Tree.SimpleType that) {
        super.visit(that);
        Tree.TypeArgumentList tal = 
                that.getTypeArgumentList();
        TypeDeclaration dec = that.getDeclarationModel();
        ProducedType type = that.getTypeModel();
        if (dec!=null && type!=null) {
            List<TypeParameter> params = 
                    dec.getTypeParameters();
            if (tal==null && 
                    !params.isEmpty() && 
                    !type.isTypeConstructor() &&
                    !that.getMetamodel()) {
                if (!params.get(0).isDefaulted()) {
                  that.addError("missing type arguments to generic type: '" + 
                          dec.getName(that.getUnit()) + 
                          "' declares required type parameters");

                }
                else {
                    that.addUsageWarning(Warning.syntaxDeprecation,
                            "implicit use of default type arguments is deprecated (change to '" + 
                            dec.getName(that.getUnit()) + "<>')");
                }
            }
            if (type.isTypeConstructor() && dec.isAlias()) {
                that.addUnsupportedError("type constructor reference to type alias not yet supported");
            }
        }
    }
    
}