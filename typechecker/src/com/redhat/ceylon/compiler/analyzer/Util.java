package com.redhat.ceylon.compiler.analyzer;

import com.redhat.ceylon.compiler.model.Declaration;
import com.redhat.ceylon.compiler.model.GenericType;
import com.redhat.ceylon.compiler.model.Scope;
import com.redhat.ceylon.compiler.model.Structure;
import com.redhat.ceylon.compiler.model.Typed;
import com.redhat.ceylon.compiler.tree.Tree;

class Util {

	static GenericType getDeclaration(Tree.Type that) {
		return getDeclaration(that.getScope(), that);
	}

	static GenericType getDeclaration(Scope scope, Tree.Type that) {
		return (GenericType) get(scope, that.getIdentifier().getText());
	}

	static Typed getDeclaration(Tree.Member that) {
		return getDeclaration(that.getScope(), that);
	}

	static Typed getDeclaration(Scope scope, Tree.Member that) {
		return (Typed) get(scope, that.getIdentifier().getText());
	}

	private static Declaration get(Scope scope, String name) {
		while (true) {
			for ( Structure s: scope.getMembers() ) {
				if (s instanceof Declaration) {
					Declaration d = (Declaration) s;
					if (d.getName().equals(name)) {
						return d;
					}
				}
			}
			scope = scope.getContainer();
			if (scope==null) {
				throw new RuntimeException("Member not found: " + name);
			}
		}
	}

}
