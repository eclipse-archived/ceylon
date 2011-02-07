package com.redhat.ceylon.compiler.analyzer;

import java.util.Stack;

import com.redhat.ceylon.compiler.model.Class;
import com.redhat.ceylon.compiler.model.CompilationUnit;
import com.redhat.ceylon.compiler.model.Declaration;
import com.redhat.ceylon.compiler.model.Getter;
import com.redhat.ceylon.compiler.model.Interface;
import com.redhat.ceylon.compiler.model.Method;
import com.redhat.ceylon.compiler.model.Parameter;
import com.redhat.ceylon.compiler.model.Scope;
import com.redhat.ceylon.compiler.model.SimpleValue;
import com.redhat.ceylon.compiler.tree.Tree;
import com.redhat.ceylon.compiler.tree.Visitor;
import com.redhat.ceylon.compiler.model.Package;

public class DeclarationVisitor extends Visitor {
	Stack<Scope<Declaration>> declarationScopes = new Stack<Scope<Declaration>>();
	CompilationUnit compilationUnit;
	
	public DeclarationVisitor(Package p) {
		declarationScopes.push(p);
	}
	
	public void visit(Tree.CompilationUnit that) {
		compilationUnit = new CompilationUnit();
		super.visit(that);
	}
	
	@Override
	public void visit(Tree.ClassDeclaration that) {
		Class c = new Class();
		c.setCompilationUnit(compilationUnit);
		c.setName(that.getTypeName().getText());
		Scope<Declaration> scope = declarationScopes.peek();
		c.setContainer(scope);
		scope.getMembers().add(c);
		declarationScopes.push(c);
		super.visit(that);
		declarationScopes.pop();
	}

	@Override
	public void visit(Tree.InterfaceDeclaration that) {
		Interface i = new Interface();
		i.setCompilationUnit(compilationUnit);
		i.setName(that.getTypeName().getText());
		Scope<Declaration> scope = declarationScopes.peek();
		i.setContainer(scope);
		scope.getMembers().add(i);
		declarationScopes.push(i);
		super.visit(that);
		declarationScopes.pop();
	}

	@Override
	public void visit(Tree.MethodDeclaration that) {
		Method m = new Method();
		m.setCompilationUnit(compilationUnit);
		m.setName(that.getMemberName().getText());
		Scope<Declaration> scope = declarationScopes.peek();
		m.setContainer(scope);
		scope.getMembers().add(m);
		declarationScopes.push(m);
		super.visit(that);
		declarationScopes.pop();
	}

	@Override
	public void visit(Tree.AttributeDeclaration that) {
		SimpleValue v = new SimpleValue();
		v.setCompilationUnit(compilationUnit);
		v.setName(that.getMemberName().getText());
		Scope<Declaration> scope = declarationScopes.peek();
		v.setContainer(scope);
		scope.getMembers().add(v);
		super.visit(that);
	}

	@Override
	public void visit(Tree.AttributeGetter that) {
		Getter g = new Getter();
		g.setCompilationUnit(compilationUnit);
		g.setName(that.getMemberName().getText());
		Scope<Declaration> scope = declarationScopes.peek();
		g.setContainer(scope);
		scope.getMembers().add(g);
		declarationScopes.push(g);
		super.visit(that);
		declarationScopes.pop();
	}
	
	@Override
	public void visit(Tree.Parameter that) {
		Parameter p = new Parameter();
		p.setCompilationUnit(compilationUnit);
		p.setName(that.getParameterName().getText());
		Scope<Declaration> scope = declarationScopes.peek();
		p.setContainer(scope);
		scope.getMembers().add(p);
		declarationScopes.push(p);
		super.visit(that);
		declarationScopes.pop();
	}
}
