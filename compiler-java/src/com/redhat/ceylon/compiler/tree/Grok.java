package com.redhat.ceylon.compiler.tree;

import com.sun.tools.javac.util.*;

public class Grok extends CeylonTree.Visitor {
	Context current;

	class Context implements Cloneable {
		CeylonTree compilationUnit;
		CeylonTree methodDeclaration;
		CeylonTree block;
		Context prev;
		List<CeylonTree> accum;
		List<CeylonTree> annotations;
		List<CeylonTree.ImportDeclaration> imports;
		CeylonTree context;

		public void push() {
			try {
				Context c = (Context) clone();
				c.prev = this;
				current = c;
			} catch (CloneNotSupportedException e) {
				throw new Error(e);
			}
		}

		public void push(CeylonTree context) {
			push();
			current.context = context;
		}
		
		public void pop() {
			current = prev;
		}
	}

	public void visit(CeylonTree.CompilationUnit t) {
		current = new Context();
		current.compilationUnit = t;
		current.context = t;
		inner(t);
	}

	public void visit(CeylonTree.ClassDeclaration decl) {
		current.push();
		current.context = decl;
		inner(decl);
		current.pop();
		current.context.addClass(decl);
	}

	public void visit(CeylonTree.ImportDeclaration decl) {
		current.imports.append(decl);
		inner(decl);
		CeylonTree.CompilationUnit toplev = (CeylonTree.CompilationUnit) current.context;
		toplev.importDeclarations = current.imports;
		current.imports = null;
	}
	
	public void visit(CeylonTree.TypeDeclaration decl) {
		current.push();
		current.context = decl;
		inner(decl);
		current.pop();
		CeylonTree.ClassDeclaration classDecl = decl.enclosedClasses.last();
		classDecl.annotations = decl.annotations;
		current.context.addClass(classDecl);
	}
	
	public void visit(CeylonTree.TypeName name) {
		current.push();
		current.context = name;
		inner(name);
		current.pop();
		current.context.setName(name.name);
	}
	
	public void visit(CeylonTree.UIdentifier id) {
		current.context.setName(id.token.getText());
		inner(id);
	}
	
	public void visit(CeylonTree.LIdentifier id) {
		current.context.setName(id.token.getText());
		inner(id);
	}
	
	public void visit(CeylonTree.StatementList stmts) {
		inner(stmts);
	}
	
	public void visit(CeylonTree.AnnotationList list) {
		inner(list);
	}
	
	public void visit(CeylonTree.UserAnnotation ann) {
		current.push();
		current.context = ann;
		inner(ann);
		current.pop();
		current.context.addAnnotation(ann);
	}
	
	public void visit(CeylonTree.LanguageAnnotation ann) {
		current.push();
		current.context = ann;
		inner(ann);
		current.pop();
		current.context.addAnnotation(ann);
	}
	
	public void visit(CeylonTree.AnnotationName name)
	{
		current.push();
		current.context = name;
		inner(name);
		current.pop();	
		current.context.setName(name.name);
	}
	
	@Override
	public void visit(CeylonTree.Public v) {
		// ((CeylonTree.Declaration)current.context).setVisibility(v);
	}
	
	void inner(CeylonTree t) {
		for (CeylonTree child : t.children)
			child.accept(this);		
	}	
}
