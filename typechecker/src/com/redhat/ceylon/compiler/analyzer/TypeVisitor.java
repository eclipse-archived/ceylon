package com.redhat.ceylon.compiler.analyzer;

import com.redhat.ceylon.compiler.model.Method;
import com.redhat.ceylon.compiler.model.SimpleValue;
import com.redhat.ceylon.compiler.model.Type;
import com.redhat.ceylon.compiler.tree.Tree;
import com.redhat.ceylon.compiler.tree.Visitor;

public class TypeVisitor extends Visitor {
	
	Type outerType;
	
	@Override
	public void visit(Tree.AnyAttributeDeclaration that) {
		super.visit(that);
		( (SimpleValue) that.getModelNode() )
			.setType( (Type) that.getTypeOrSubtype().getModelNode() );
	}
	
	@Override
	public void visit(Tree.MethodDeclaration that) {
		super.visit(that);
		( (Method) that.getModelNode() )
			.setType( (Type) that.getTypeOrSubtype().getModelNode() );
	}
	
	@Override public void visit(Tree.Type that) {
		Type type = new Type();
		that.setModelNode(type);
		type.setTreeNode(that);
		type.setGenericType( Util.getDeclaration(that) );
		//TODO: handle type arguments by substitution
		Type t = type;
		if (outerType!=null) {
			outerType.getTypeArguments().add(t);
		}
		Type o = outerType;
		outerType = t;
		super.visit(that);
		outerType = o;
		//System.out.println(t);
	}

	/**
	 * Suppress resolution of types that appear after the
	 * member selection operator "."
	 */
	@Override
	public void visit(Tree.MemberExpression that) {
	    that.getPrimary().visit(this);			
	}
	
}
