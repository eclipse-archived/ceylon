package com.redhat.ceylon.compiler.analyzer;

import com.redhat.ceylon.compiler.model.Class;
import com.redhat.ceylon.compiler.model.ControlBlock;
import com.redhat.ceylon.compiler.model.Declaration;
import com.redhat.ceylon.compiler.model.Element;
import com.redhat.ceylon.compiler.model.Functional;
import com.redhat.ceylon.compiler.model.FunctionalParameter;
import com.redhat.ceylon.compiler.model.Getter;
import com.redhat.ceylon.compiler.model.Interface;
import com.redhat.ceylon.compiler.model.Method;
import com.redhat.ceylon.compiler.model.Package;
import com.redhat.ceylon.compiler.model.ParameterList;
import com.redhat.ceylon.compiler.model.Scope;
import com.redhat.ceylon.compiler.model.Setter;
import com.redhat.ceylon.compiler.model.TypeParameter;
import com.redhat.ceylon.compiler.model.Unit;
import com.redhat.ceylon.compiler.model.Value;
import com.redhat.ceylon.compiler.model.ValueParameter;
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
    
    private final Package pkg;
    private Scope scope;
    private Unit unit;
    private ParameterList parameterList;
    private Functional functional;
    
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
        Tree.Identifier id = that.getIdentifier();
        if (id==null || id.getText().startsWith("<missing")) {
            that.addError("missing declaration name");
        }
        else {
            model.setName(id.getText());
            checkForDuplicateDeclaration(that, model);
        }
        visitElement(that, model);
        that.setDeclarationModel(model);
        unit.getDeclarations().add(model);
        scope.getMembers().add(model);
    }

    private void checkForDuplicateDeclaration(Tree.Declaration that,
            Declaration model) {
        boolean found = false;
        String name = Util.name(that);
        for (Declaration m: scope.getMembers()) {
            String dname = m.getName();
            if (dname!=null && dname.equals(name)) {
                if (model instanceof Setter) {
                    if (m instanceof Getter) {
                        found = true;
                        continue;
                    }
                }
                //TODO: special exception where a parameter of
                //      a class can have same name as an attribute
                that.addError("duplicate declaration: " + name);
            }
        }
        if (!found && (model instanceof Setter)) {
            that.addError("setter with no matching getter: " + name);
        }
    }

    private void visitElement(Node that, Element model) {
        model.setUnit(unit);
        model.setContainer(scope);
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
        //that.setModelNode(unit);
        unit.setPackage(pkg);
        super.visit(that);
    }
    
    @Override
    public void visit(Tree.ClassDefinition that) {
        Class c = new Class();
        visitDeclaration(that, c);
        functional = c;
        Scope o = enterScope(c);
        super.visit(that);
        exitScope(o);
        functional = null;
        if (that.getParameterList()==null) {
            that.addError("missing parameter list in class declaration: " + 
                    Util.name(that) );
        }
    }

    @Override
    public void visit(Tree.InterfaceDefinition that) {
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
        functional = m;
        super.visit(that);
        functional = null;
        checkMethodParameters(that);
    }

    @Override
    public void visit(Tree.MethodDefinition that) {
        Method m = new Method();
        visitDeclaration(that, m);
        functional = m;
        Scope o = enterScope(m);
        super.visit(that);
        exitScope(o);
        functional = null;
        checkMethodParameters(that);
    }

    private void checkMethodParameters(Tree.Method that) {
        if (that.getParameterLists().isEmpty()) {
            that.addError("missing parameter list in method declaration: " + 
                    Util.name(that) );
        }
    }

    @Override
    public void visit(Tree.AttributeDeclaration that) {
        Value v = new Value();
        visitDeclaration(that, v);
        super.visit(that);
    }

    @Override
    public void visit(Tree.AttributeGetterDefinition that) {
        Getter g = new Getter();
        visitDeclaration(that, g);
        Scope o = enterScope(g);
        super.visit(that);
        exitScope(o);
    }
    
    @Override
    public void visit(Tree.AttributeSetterDefinition that) {
        Setter g = new Setter();
        visitDeclaration(that, g);
        Scope o = enterScope(g);
        super.visit(that);
        exitScope(o);
    }
    
    @Override
    public void visit(Tree.ValueParameter that) {
        ValueParameter p = new ValueParameter();
        visitDeclaration(that, p);
        super.visit(that);
        parameterList.getParameters().add(p);
    }

    @Override
    public void visit(Tree.FunctionalParameter that) {
        FunctionalParameter p = new FunctionalParameter();
        visitDeclaration(that, p);
        //Scope o = enterScope(p);
        Functional o = functional;
        functional = p;
        super.visit(that);
        functional = o;
        //exitScope(o);
        parameterList.getParameters().add(p);
    }

    @Override
    public void visit(Tree.ParameterList that) {
        ParameterList pl = parameterList;
        parameterList = new ParameterList();
        super.visit(that);
        //TODO: check that we are allowed to add!
        if (functional==null) {
            //TODO: this case is temporary until
            //      we add support for MethodArguments
        }
        else {
            if ( (functional instanceof Class) && 
                    !functional.getParameterLists().isEmpty() ) {
                that.addError("classes may have only one parameter list");
            }
            else {
                functional.addParameterList(parameterList);
            }
        }
        parameterList = pl;
    }
    
    @Override
    public void visit(Tree.ControlClause that) {
        ControlBlock c = new ControlBlock();
        visitElement(that, c);
        Scope o = enterScope(c);
        super.visit(that);
        exitScope(o);
    }
    
    @Override
    public void visit(Tree.Variable that) {
        Value v = new Value();
        visitDeclaration(that, v);
        super.visit(that);
        //TODO: what about callable variables?!
    }

}
