package com.redhat.ceylon.compiler.analyzer;

import java.util.Stack;

import com.redhat.ceylon.compiler.model.Class;
import com.redhat.ceylon.compiler.model.CompilationUnit;
import com.redhat.ceylon.compiler.model.Declaration;
import com.redhat.ceylon.compiler.model.Getter;
import com.redhat.ceylon.compiler.model.Interface;
import com.redhat.ceylon.compiler.model.Method;
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
		Interface c = new Interface();
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
	public void visit(Tree.MethodDeclaration that) {
		Method c = new Method();
		c.setCompilationUnit(compilationUnit);
		c.setName(that.getMemberName().getText());
		Scope<Declaration> scope = declarationScopes.peek();
		c.setContainer(scope);
		scope.getMembers().add(c);
		declarationScopes.push(c);
		super.visit(that);
		declarationScopes.pop();
	}

	@Override
	public void visit(Tree.AttributeDeclaration that) {
		SimpleValue c = new SimpleValue();
		c.setCompilationUnit(compilationUnit);
		c.setName(that.getMemberName().getText());
		Scope<Declaration> scope = declarationScopes.peek();
		c.setContainer(scope);
		scope.getMembers().add(c);
		super.visit(that);
	}

	@Override
	public void visit(Tree.AttributeGetter that) {
		Getter c = new Getter();
		c.setCompilationUnit(compilationUnit);
		c.setName(that.getMemberName().getText());
		Scope<Declaration> scope = declarationScopes.peek();
		c.setContainer(scope);
		scope.getMembers().add(c);
		declarationScopes.push(c);
		super.visit(that);
		declarationScopes.pop();
	}
}
