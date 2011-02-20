package com.redhat.ceylon.compiler.analyzer;

import java.util.ArrayList;
import java.util.List;

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
import com.redhat.ceylon.compiler.model.TypeDeclaration;
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
    private final String filename;
    private Scope scope;
    private Unit unit;
    private ParameterList parameterList;
    private Functional functional;
    private Declaration declaration;
    
    public DeclarationVisitor(Package p, String fn) {
        scope = p;
        pkg = p;
        filename = fn;
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
        if ( setModelName(that, model, id) ) {
            checkForDuplicateDeclaration(that, model);
        }
        visitElement(that, model);
        that.setDeclarationModel(model);
        unit.getDeclarations().add(model);
        scope.getMembers().add(model);
    }

    private void visitArgument(Tree.NamedArgument that, Declaration model) {
        Tree.Identifier id = that.getIdentifier();
        setModelName(that, model, id);
        visitElement(that, model);
        that.setDeclarationModel(model);
        unit.getDeclarations().add(model);
    }

    private boolean setModelName(Node that, Declaration model,
            Tree.Identifier id) {
        if (id==null || id.getText().startsWith("<missing")) {
            that.addError("missing declaration name");
            return false;
        }
        else {
            model.setName(internalName(that, model, id));
            return true;
            //TODO: check for dupe arg name
        }
    }

    private String internalName(Node that, Declaration model,
            Tree.Identifier id) {
        String n = id.getText();
        if ((that instanceof Tree.ObjectDeclaration||that instanceof Tree.ObjectArgument) 
                && model instanceof Class) {
            n = "Type_" + n;
        }
        return n;
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
        unit.setFilename(filename);
        super.visit(that);
    }
    
    @Override
    public void visit(Tree.TypeDeclaration that) {
        super.visit(that);
        TypeDeclaration d = (TypeDeclaration) that.getDeclarationModel();
        if (d==null) {
            //TODO: this case is temporary until we have type constraints!
        }
        else {
            d.setTypeParameters(getTypeParameters(that.getTypeParameterList()));
        }
    }
    
    @Override
    public void visit(Tree.Method that) {
        super.visit(that);
        ( (Method) that.getDeclarationModel() ).setTypeParameters(getTypeParameters(that.getTypeParameterList()));
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
        TypeParameter p = new TypeParameter();
        p.setDeclaration(declaration);
        if (that.getTypeVariance()!=null) {
            String v = that.getTypeVariance().getText();
            p.setCovariant("out".equals(v));
            p.setContravariant("in".equals(v));
        }
        visitDeclaration(that, p);
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

    @Override
    public void visit(Tree.MethodArgument that) {
        Method m = new Method();
        visitArgument(that, m);
        functional = m;
        Scope o = enterScope(m);
        super.visit(that);
        exitScope(o);
        functional = null;
        checkMethodArgumentParameters(that);
    }

    private void checkMethodParameters(Tree.Method that) {
        if (that.getParameterLists().isEmpty()) {
            that.addError("missing parameter list in method declaration: " + 
                    Util.name(that) );
        }
    }

    private void checkMethodArgumentParameters(Tree.MethodArgument that) {
        if (that.getParameterLists().isEmpty()) {
            that.addError("missing parameter list in named argument declaration: " + 
                    Util.name(that) );
        }
    }

    @Override
    public void visit(Tree.ObjectDeclaration that) {
        Class c = new Class();
        visitDeclaration(that, c);
        Value v = new Value();
        visitDeclaration(that, v);
        functional = c;
        Scope o = enterScope(c);
        super.visit(that);
        exitScope(o);
        functional = null;
        that.getTypeOrSubtype().setTypeModel(c.getType());
        v.setType(c.getType());
    }

    @Override
    public void visit(Tree.ObjectArgument that) {
        Class c = new Class();
        visitArgument(that, c);
        Value v = new Value();
        visitArgument(that, v);
        functional = c;
        Scope o = enterScope(c);
        super.visit(that);
        exitScope(o);
        functional = null;
        that.getTypeOrSubtype().setTypeModel(c.getType());
        v.setType(c.getType());
    }

    @Override
    public void visit(Tree.AttributeDeclaration that) {
        Value v = new Value();
        visitDeclaration(that, v);
        if (hasAnnotation(that.getAnnotationList(), "variable")) {
            v.setVariable(true);
        }
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
    public void visit(Tree.AttributeArgument that) {
        Getter g = new Getter();
        visitArgument(that, g);
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
        p.setDefaulted(that.getSpecifierExpression()!=null);
        p.setSequenced(that.getTypeOrSubtype() instanceof Tree.SequencedType);
        visitDeclaration(that, p);
        super.visit(that);
        parameterList.getParameters().add(p);
    }

    @Override
    public void visit(Tree.FunctionalParameter that) {
        FunctionalParameter p = new FunctionalParameter();
        p.setDefaulted(that.getSpecifierExpression()!=null);
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
        if (that.getSpecifierExpression()!=null) {
            Scope s = scope;
            scope = scope.getContainer();
            that.getSpecifierExpression().visit(this);
            scope = s;
        }
        Value v = new Value();
        visitDeclaration(that, v);
        that.getTypeOrSubtype().visit(this);
        that.getIdentifier().visit(this);
        if (that.getAnnotationList()!=null) {
            that.getAnnotationList().visit(this);
        }
        //TODO: parameters of callable variables?!
        that.setScope(scope);
        that.setUnit(unit);
    }

    private List<TypeParameter> getTypeParameters(Tree.TypeParameterList tpl) {
        List<TypeParameter> typeParameters = new ArrayList<TypeParameter>();
        if (tpl!=null) {
            for (Tree.TypeParameter tp: tpl.getTypeParameters()) {
                typeParameters.add((TypeParameter) tp.getDeclarationModel());
            }
        }
        return typeParameters;
    }
    
    private boolean hasAnnotation(Tree.AnnotationList al, String name) {
        if (al!=null) {
            for (Tree.Annotation a: al.getAnnotations()) {
                if ( ((Tree.Member) a.getPrimary()).getIdentifier().getText().equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override public void visit(Tree.Declaration that) {
        Declaration d = declaration;
        declaration = that.getDeclarationModel();
        super.visit(that);
        declaration = d;
    }
        
}
