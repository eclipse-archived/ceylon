package com.redhat.ceylon.compiler.analyzer;

import java.util.List;

import com.redhat.ceylon.compiler.model.Unit;
import com.redhat.ceylon.compiler.model.Import;
import com.redhat.ceylon.compiler.model.Method;
import com.redhat.ceylon.compiler.model.Module;
import com.redhat.ceylon.compiler.model.Package;
import com.redhat.ceylon.compiler.model.SimpleValue;
import com.redhat.ceylon.compiler.model.Type;
import com.redhat.ceylon.compiler.tree.Tree;
import com.redhat.ceylon.compiler.tree.Tree.Identifier;
import com.redhat.ceylon.compiler.tree.Visitor;

public class TypeVisitor extends Visitor {
	
	Unit unit;
	
	Type outerType;
	
	Tree.ImportPath importPath;
	
	public TypeVisitor(Unit cu) {
		unit = cu;
	}
	
	@Override
	public void visit(Tree.ImportPath that) {
		importPath = that;
	}
	
	Package getPackage(List<Identifier> importPath) {
		Module m = unit.getPackage().getModule();
		for (Package mp: m.getPackages()) {
			if ( hasName(importPath, mp) ) {
				return mp;
			}
		}
		for (Module d: m.getDependencies()) {
			for (Package dp: d.getPackages()) {
				if ( hasName(importPath, dp) ) {
					return dp;
				}
			}
		}
		throw new RuntimeException("Package not found");
	}

	private boolean hasName(List<Identifier> importPath, Package mp) {
		if (mp.getName().size()==importPath.size()) {
			for (int i=0; i<mp.getName().size(); i++) {
				if (!mp.getName().get(i).equals(importPath.get(i).getText())) {
					return false;
				}
			}
			return true;
		}
		else {
			return false;
		}
	}
	
	@Override
	public void visit(Tree.ImportMember that) {
		Import i = new Import();
		if (that.getMemberAlias()==null) {
			i.setAlias(that.getIdentifier().getText());
		}
		else {
			i.setAlias(that.getMemberAlias().getIdentifier().getText());
		}
		Package p = getPackage(importPath.getIdentifiers());
		i.setDeclaration( Util.getDeclaration(p, that.getIdentifier()) );
		unit.getImports().add(i);
	}
	
	@Override
	public void visit(Tree.ImportType that) {
		Import i= new Import();
		if (that.getTypeAlias()==null) {
			i.setAlias(that.getIdentifier().getText());
		}
		else {
			i.setAlias(that.getTypeAlias().getIdentifier().getText());
		}
		Package p = getPackage(importPath.getIdentifiers());
		i.setDeclaration( Util.getDeclaration(p, that.getIdentifier()) );
		unit.getImports().add(i);
	}
	
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
	
	@Override 
	public void visit(Tree.Type that) {
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
