package com.redhat.ceylon.compiler.analyzer;

import java.util.ArrayList;
import java.util.List;

import com.redhat.ceylon.compiler.model.Class;
import com.redhat.ceylon.compiler.model.ControlBlock;
import com.redhat.ceylon.compiler.model.Declaration;
import com.redhat.ceylon.compiler.model.Functional;
import com.redhat.ceylon.compiler.model.Getter;
import com.redhat.ceylon.compiler.model.Interface;
import com.redhat.ceylon.compiler.model.Method;
import com.redhat.ceylon.compiler.model.Package;
import com.redhat.ceylon.compiler.model.Parameter;
import com.redhat.ceylon.compiler.model.Scope;
import com.redhat.ceylon.compiler.model.SimpleValue;
import com.redhat.ceylon.compiler.model.Structure;
import com.redhat.ceylon.compiler.model.TypeParameter;
import com.redhat.ceylon.compiler.model.Unit;
import com.redhat.ceylon.compiler.tree.Node;
import com.redhat.ceylon.compiler.tree.Tree;
import com.redhat.ceylon.compiler.tree.Visitor;

/**
 * First phase of type analysis.
 * Scan a compilation unit searching for declarations,
 * and builds up the model objects. At this point, all
 * we know is the name of the declaration and what
 * kind of declaration it is. The model objects do not
 * contain type information.
 * 
 * @author Gavin King
 *
 */
public class DeclarationVisitor extends Visitor {
    
    Scope scope;
    Unit unit;
    final Package pkg;
    List<Parameter> parameterList;
    
    public DeclarationVisitor(Package p) {
        scope = p;
        pkg = p;
    }
    
    public Unit getCompilationUnit() {
        return unit;
    }
    
    private Scope enterScope(Scope innerScope) {
        Scope outerScope = scope;
        scope = innerScope;
        return outerScope;
    }

    private void exitScope(Scope outerScope) {
        scope = outerScope;
    }

    private void visitDeclaration(Tree.Declaration that, Declaration model) {
        model.setName(that.getIdentifier().getText());
        visitStructure(that, model);
        unit.getDeclarations().add(model);
    }

    private void visitStructure(Node that, Structure model) {
        that.setModelNode(model);
        model.setTreeNode(that);
        model.setUnit(unit);
        model.setContainer(scope);
        scope.getMembers().add(model); //TODO: do we really need to include control statements here?
    }
    
    @Override
    public void visitAny(Node that) {
        that.setScope(scope);
        that.setUnit(unit);
        super.visitAny(that);
    }
    
    @Override
    public void visit(Tree.CompilationUnit that) {
        unit = new Unit();
        that.setModelNode(unit);
        unit.setTreeNode(that);
        unit.setPackage(pkg);
        super.visit(that);
    }
    
    @Override
    public void visit(Tree.ClassDeclaration that) {
        Class c = new Class();
        visitDeclaration(that, c);
        Scope o = enterScope(c);
        super.visit(that);
        exitScope(o);
        if (that.getParameterList()==null) {
            that.getErrors().add( new AnalysisError(that, 
                    "Missing parameter list in class declaration: " + 
                    that.getIdentifier().getText() ) );
        }
    }

    @Override
    public void visit(Tree.InterfaceDeclaration that) {
        Interface i = new Interface();
        visitDeclaration(that, i);
        Scope o = enterScope(i);
        super.visit(that);
        exitScope(o);
    }

    @Override
    public void visit(Tree.TypeParameter that) {
        TypeParameter t = new TypeParameter();
        visitDeclaration(that, t);
        super.visit(that);
    }

    @Override
    public void visit(Tree.MethodDeclaration that) {
        Method m = new Method();
        visitDeclaration(that, m);
        Scope o = enterScope(m);
        super.visit(that);
        exitScope(o);
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
        Scope o = enterScope(g);
        super.visit(that);
        exitScope(o);
    }
    
    @Override
    public void visit(Tree.Parameter that) {
        Parameter p = new Parameter();
        visitDeclaration(that, p);
        Scope o = enterScope(p);
        super.visit(that);
        exitScope(o);
        parameterList.add(p);
    }

    @Override
    public void visit(Tree.ParameterList that) {
        List<Parameter> pl = parameterList;
        if (scope instanceof Functional) {
            parameterList = new ArrayList<Parameter>();
            ( (Functional) scope ).getParameters().add(parameterList);
        }
        else {
            parameterList = ( (Class) scope ).getParameters();
        }
        super.visit(that);
        parameterList = pl;
    }
    
    @Override
    public void visit(Tree.ControlClause that) {
        ControlBlock c = new ControlBlock();
        visitStructure(that, c);
        Scope o = enterScope(c);
        super.visit(that);
        exitScope(o);
    }
    
    @Override
    public void visit(Tree.Variable that) {
        super.visit(that);
        //TODO: what about callable variables?!
        SimpleValue v = new SimpleValue();
        v.setName(that.getIdentifier().getText());
        visitStructure(that, v);
        unit.getDeclarations().add(v);
    }

}
