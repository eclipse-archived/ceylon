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
        current.push(decl);
        inner(decl);
        current.pop();
        current.context.add(decl);
    }

    public void visit(CeylonTree.ImportDeclaration decl) {
        current.imports.append(decl);
        inner(decl);
        CeylonTree.CompilationUnit toplev = (CeylonTree.CompilationUnit) current.context;
        toplev.importDeclarations = current.imports;
        current.imports = null;
    }
    
    public void visit(CeylonTree.TypeDeclaration decl) {
        current.push(decl);
        inner(decl);
        current.pop();
        CeylonTree.ClassDeclaration classDecl = decl.classDecls.last();
        classDecl.annotations = decl.annotations;
        current.context.add(classDecl);
    }
    
    public void visit(CeylonTree.TypeName name) {
        current.push(name);
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
        current.push(ann);
        inner(ann);
        current.pop();
        current.context.add(ann);
    }
    
    public void visit(CeylonTree.LanguageAnnotation ann) {
        current.push(ann);
        current.context = ann;
        inner(ann);
        current.pop();
        current.context.add(ann);
    }
    
    public void visit(CeylonTree.AnnotationName name)
    {
        current.push(name);
        inner(name);
        current.pop();  
        current.context.setName(name.name);
    }
    
    @Override
        public void visit(CeylonTree.Public v) {
        current.context.add(v);     
        // ((CeylonTree.Declaration)current.context).setVisibility(v);
    }
    
    public void visit(CeylonTree.MemberDeclaration member)
    {
        // We don't know if this is going to be a method or a field.
        // We resolve this by saying if it has an arg list, it's a method.
        current.push(member);
        inner(member);
        current.pop();
        if (member.params != null) {
            CeylonTree.MethodType methodType = new CeylonTree.MethodType();
            methodType.formalParameters = member.params;
            methodType.returnType = member.type;
            member.type = methodType;
            CeylonTree.MethodDeclaration decl = new CeylonTree.MethodDeclaration();
            decl.setParameterList(member.params);
            decl.setType(methodType);
            decl.setName(member.name);
            current.context.add(decl);  
        } else {
            current.context.add(member);
        }
    }
    
    public void visit(CeylonTree.MemberName member)
    {
        current.push(member);
        inner(member);
        current.pop();
        current.context.setName(member.name);
    }
    
    public void visit(CeylonTree.MemberType type)
    {
        current.push(type);
        inner(type);
        current.pop();
        CeylonTree.MemberDeclaration decl = (CeylonTree.MemberDeclaration)current.context;
        decl.type = type;
    }
    
    public void visit(CeylonTree.Void v)
    {
        current.context.setType(v);
    }
    
    public void visit(CeylonTree.Type type) {
        inner(type);
        current.context.setType(type);
    }
    
    public void visit(CeylonTree.FormalParameterList list) {
        current.push(list);
        inner(list);
        current.pop();
        CeylonTree.Declaration decl = (CeylonTree.Declaration)current.context;
        decl.setParameterList(list.theList);
    }
    
    public void visit(CeylonTree.FormalParameter p) {
        current.push(p);
        inner(p);
        current.pop();
        CeylonTree.FormalParameterList l = (CeylonTree.FormalParameterList)current.context;
        l.addFormalParameter(p);
    }
    
    public void visit(CeylonTree.ArgumentName name) {
        current.push(name);
        inner(name);
        current.pop();
        current.context.setName(name.name);
    }
    
    void inner(CeylonTree t) {
        for (CeylonTree child : t.children)
            child.accept(this);     
    }   
}
