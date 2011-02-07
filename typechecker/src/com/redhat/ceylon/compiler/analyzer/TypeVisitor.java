package com.redhat.ceylon.compiler.analyzer;

import com.redhat.ceylon.compiler.model.GenericType;
import com.redhat.ceylon.compiler.model.Scope;
import com.redhat.ceylon.compiler.model.Structure;
import com.redhat.ceylon.compiler.model.Type;
import com.redhat.ceylon.compiler.tree.Tree;
import com.redhat.ceylon.compiler.tree.Visitor;

public class TypeVisitor extends Visitor {
	
	Type outerType;
	
	@Override public void visit(Tree.Type that) {
		Type t = getModel(that);
		if (outerType!=null) {
			outerType.getTypeArguments().add(t);
		}
		Type o = outerType;
		outerType = t;
		super.visit(that);
		outerType = o;
		//System.out.println(t);
	}

	private Type getModel(Tree.Type that) {
		Type type = new Type();
		that.setModelNode(type);
		type.setTreeNode(that);
		Scope<Structure> scope = that.getScope();
		while (true) {
			for ( Structure s: scope.getMembers() ) {
				if (s instanceof GenericType) {
					GenericType d = (GenericType) s;
					if (d.getName().equals(that.getIdentifier().getText())) {
						type.setGenericType(d);
						return type;
					}
				}
			}
			scope = scope.getContainer();
			if (scope==null) {
				throw new RuntimeException("Type not found: " + that.getIdentifier().getText());
			}
		}
	}
}
