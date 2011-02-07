package com.redhat.ceylon.compiler.analyzer;

import java.util.Stack;

import com.redhat.ceylon.compiler.model.Class;
import com.redhat.ceylon.compiler.model.CompilationUnit;
import com.redhat.ceylon.compiler.model.ControlBlock;
import com.redhat.ceylon.compiler.model.Declaration;
import com.redhat.ceylon.compiler.model.Getter;
import com.redhat.ceylon.compiler.model.Interface;
import com.redhat.ceylon.compiler.model.Method;
import com.redhat.ceylon.compiler.model.Package;
import com.redhat.ceylon.compiler.model.Parameter;
import com.redhat.ceylon.compiler.model.Scope;
import com.redhat.ceylon.compiler.model.SimpleValue;
import com.redhat.ceylon.compiler.model.Structure;
import com.redhat.ceylon.compiler.tree.Node;
import com.redhat.ceylon.compiler.tree.Tree;
import com.redhat.ceylon.compiler.tree.Visitor;

public class DeclarationVisitor extends Visitor {
	Stack<Scope<Structure>> declarationScopes = new Stack<Scope<Structure>>();
	CompilationUnit compilationUnit;
	
	public DeclarationVisitor(Package p) {
		declarationScopes.push(p);
	}
	
	private void visitDeclaration(Tree.Declaration that, Declaration model) {
		model.setName(that.getIdentifier().getText());
		visitStructure(that, model);
	}

	private void visitStructure(Node that, Structure model) {
		that.setModelNode(model);
		model.setTreeNode(that);
		model.setCompilationUnit(compilationUnit);
		Scope<Structure> scope = declarationScopes.peek();
		model.setContainer(scope);
		scope.getMembers().add(model); //TODO: do we really need to include control statements here?
	}

	public void visit(Tree.CompilationUnit that) {
		compilationUnit = new CompilationUnit();
		super.visit(that);
	}
	
	@Override
	public void visit(Tree.ClassDeclaration that) {
		Class c = new Class();
		visitDeclaration(that, c);
		declarationScopes.push(c);
		super.visit(that);
		declarationScopes.pop();
	}

	@Override
	public void visit(Tree.InterfaceDeclaration that) {
		Interface i = new Interface();
		visitDeclaration(that, i);
		declarationScopes.push(i);
		super.visit(that);
		declarationScopes.pop();
	}

	@Override
	public void visit(Tree.MethodDeclaration that) {
		Method m = new Method();
		visitDeclaration(that, m);
		declarationScopes.push(m);
		super.visit(that);
		declarationScopes.pop();
	}

	@Override
	public void visit(Tree.AttributeDeclaration that) {
		SimpleValue v = new SimpleValue();
		visitDeclaration(that, v);
		super.visit(that);
	}

	@Override
	public void visit(Tree.AttributeGetter that) {
		Getter g = new Getter();
		visitDeclaration(that, g);
		declarationScopes.push(g);
		super.visit(that);
		declarationScopes.pop();
	}
	
	@Override
	public void visit(Tree.Parameter that) {
		Parameter p = new Parameter();
		visitDeclaration(that, p);
		declarationScopes.push(p);
		super.visit(that);
		declarationScopes.pop();
	}
	
	//TODO: variables in try, catch, if, for, while blocks

	@Override
	public void visit(Tree.ControlClause that) {
		ControlBlock c = new ControlBlock();
		visitStructure(that, c);
		declarationScopes.push(c);
		super.visit(that);
		declarationScopes.pop();
	}
	
	@Override
	public void visit(Tree.Variable that) {
		//TODO: what about callable variables?!
		SimpleValue v = new SimpleValue();
		that.setModelNode(v);
		v.setCompilationUnit(compilationUnit);
		v.setName(that.getIdentifier().getText());
		Scope<Structure> scope = declarationScopes.peek();
		v.setContainer(scope);
		scope.getMembers().add(v);
	}

}
