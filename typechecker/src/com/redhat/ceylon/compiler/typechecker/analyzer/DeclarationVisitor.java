package com.redhat.ceylon.compiler.typechecker.analyzer;

import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.buildAnnotations;
import static com.redhat.ceylon.compiler.typechecker.tree.Util.formatPath;
import static com.redhat.ceylon.compiler.typechecker.tree.Util.hasAnnotation;
import static com.redhat.ceylon.compiler.typechecker.tree.Util.name;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.ClassAlias;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.ConditionScope;
import com.redhat.ceylon.compiler.typechecker.model.ControlBlock;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Element;
import com.redhat.ceylon.compiler.typechecker.model.Generic;
import com.redhat.ceylon.compiler.typechecker.model.Getter;
import com.redhat.ceylon.compiler.typechecker.model.ImportList;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.InterfaceAlias;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.NamedArgumentList;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ParameterList;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.Setter;
import com.redhat.ceylon.compiler.typechecker.model.Specification;
import com.redhat.ceylon.compiler.typechecker.model.TypeAlias;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.Unit;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.compiler.typechecker.util.UnitFactory;

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
    private Declaration declaration;
    private String fullPath; 
    private String relativePath;
    private boolean dynamic;
    protected UnitFactory unitFactory;
    
    public DeclarationVisitor(Package pkg, String filename,
            String fullPath, String relativePath, UnitFactory unitFactory) {
        scope = pkg;
        this.pkg = pkg;
        this.filename = filename;
        this.fullPath = fullPath;
        this.relativePath = relativePath;
        this.unitFactory = unitFactory;
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
    
    private Declaration beginDeclaration(Declaration innerDec) {
        Declaration outerDec = declaration;
        declaration = innerDec;
        return outerDec;
    }

    private void endDeclaration(Declaration outerDec) {
        declaration = outerDec;
    }
    
    private void visitDeclaration(Tree.Declaration that, Declaration model) {
        visitDeclaration(that,  model, true);
    }
    
    private void visitDeclaration(Tree.Declaration that, Declaration model, boolean checkDupe) {
        visitElement(that, model);
        if ( setModelName(that, model, that.getIdentifier()) ) {
            if (checkDupe) checkForDuplicateDeclaration(that, model);
        }
        //that.setDeclarationModel(model);
        unit.addDeclaration(model);
        Scope sc = getContainer(that);
        if (!(sc instanceof Package)) {
            sc.getMembers().add(model);
        }
        
        handleDeclarationAnnotations(that, model);        
        
        setVisibleScope(model);
        
        checkFormalMember(that, model);
    }

    private void visitArgument(Tree.NamedArgument that, Declaration model) {
        Tree.Identifier id = that.getIdentifier();
        setModelName(that, model, id);
        visitElement(that, model);
        //that.setDeclarationModel(model);
        unit.addDeclaration(model);
    }

    private void visitArgument(Tree.FunctionArgument that, Declaration model) {
        visitElement(that, model);
        //that.setDeclarationModel(model);
        unit.addDeclaration(model);
    }

    private static boolean setModelName(Node that, Declaration model,
            Tree.Identifier id) {
        if (id==null || id.getText().startsWith("<missing")) {
            that.addError("missing declaration name");
            return false;
        }
        else {
            //model.setName(internalName(that, model, id));
            model.setName(id.getText());
            return true;
            //TODO: check for dupe arg name
        }
    }

    /*private String internalName(Node that, Declaration model,
            Tree.Identifier id) {
        String n = id.getText();
        if ((that instanceof Tree.ObjectDefinition||that instanceof Tree.ObjectArgument) 
                && model instanceof Class) {
            n = "#" + n;
        }
        return n;
    }*/

    private static void checkForDuplicateDeclaration(Tree.Declaration that, 
            final Declaration model) {
        if (model.getName()!=null) {
            if (model instanceof Setter) {
                Setter setter = (Setter) model;
                //a setter must have a matching getter
                Declaration member = model.getContainer().getDirectMember(model.getName(), null, false);
                if (member==null) {
                    that.addError("setter with no matching getter: " + model.getName());
                }
                else if (!(member instanceof Value)) {
                    that.addError("setter name does not resolve to matching getter: " + model.getName());
                }
                else if (!((Value) member).isTransient()) {
                    that.addError("matching value is a reference or is forward-declared: " + model.getName());
                }
                else {
                    Value getter = (Value) member;
                    setter.setGetter(getter);
                    if (getter.isVariable()) {
                        that.addError("duplicate setter for getter: " + model.getName());
                    }
                    else {
                        getter.setSetter(setter);
                    }
                }
            }
            else {
                Scope s = model.getContainer();
                boolean isControl;
                do {
                    Declaration member = s.getDirectMemberOrParameter(model.getName(), null, false);
                    if ( member !=null ) {
                        that.addError("duplicate declaration name: " + model.getName());
                        model.getUnit().getDuplicateDeclarations().add(member);
                    }
                    isControl = s instanceof ControlBlock;
                    s = s.getContainer();
                }
                while (isControl);
            }
        }
    }

    private void visitElement(Node that, Element model) {
        model.setUnit(unit);
        model.setScope(scope);
        model.setContainer(getContainer(that));
    }

    private Scope getContainer(Node that) {
        if (that instanceof Tree.Declaration &&
                !(that instanceof Tree.Parameter) &&
                !(that instanceof Tree.Variable)) {
            Scope s = scope;
            while (s instanceof ConditionScope) {
                s = s.getScope();
            }
            return s;
        } 
        else {
            return scope;
        }
    }
    
    @Override
    public void visitAny(Node that) {
        that.setScope(scope);
        that.setUnit(unit);
        super.visitAny(that);
    }
    
    @Override
    public void visit(Tree.DynamicStatement that) {
        boolean od = dynamic;
        dynamic = true;
        super.visit(that);
        dynamic = od;
    }
    
    @Override
    public void visit(Tree.CompilationUnit that) {
        unit = unitFactory.createUnit();
        //that.setModelNode(unit);
        unit.setPackage(pkg);
        unit.setFilename(filename);
        unit.setFullPath(fullPath);
        unit.setRelativePath(relativePath);
        pkg.removeUnit(unit);
        pkg.addUnit(unit);
        super.visit(that);
    }
    
    @Override
    public void visit(Tree.ImportMemberOrTypeList that) {
        ImportList il = new ImportList();
        unit.getImportLists().add(il);
        that.setImportList(il);
        il.setContainer(scope);
        Scope o = enterScope(il);
        super.visit(that);
        exitScope(o);
    }
    
    @Override
    public void visit(Tree.TypeParameterList that) {
        super.visit(that);
        ((Generic) declaration).setTypeParameters(getTypeParameters(that));
    }    
        
    @Override
    public void visit(Tree.AnyClass that) {
        Class c = that instanceof Tree.ClassDefinition ?
                new Class() : new ClassAlias();
        that.setDeclarationModel(c);
        visitDeclaration(that, c);
        Scope o = enterScope(c);
        super.visit(that);
        exitScope(o);
        if (that.getParameterList()==null) {
            that.addError("missing parameter list in class declaration: " + 
                    name(that.getIdentifier()) + " must have a parameter list", 1000);
        }
        else {
            c.addParameterList(that.getParameterList().getModel());
        }
        //TODO: is this still necessary??
        if (c.isClassOrInterfaceMember() && 
                c.getContainer() instanceof TypedDeclaration) {
            that.addWarning("nested classes of inner classes are not yet supported");
        }
        if (c.isAbstract() && c.isFinal()) {
            that.addError("class may not be both abstract and final: " + 
                    name(that.getIdentifier()));
        }
        if (c.isFormal() && c.isFinal()) {
            that.addError("class may not be both formal and final: " + 
                    name(that.getIdentifier()));
        }
    }

    @Override
    public void visit(Tree.AnyInterface that) {
        Interface i = that instanceof Tree.InterfaceDefinition ?
                new Interface() : new InterfaceAlias();
        that.setDeclarationModel(i);
        visitDeclaration(that, i);
        Scope o = enterScope(i);
        super.visit(that);
        exitScope(o);
        /*if (!i.isToplevel()) {
            that.addWarning("inner interfaces are not yet supported");
        }*/
        /*if ( that.getCaseTypes()!=null ) {
            that.addWarning("interfaces with enumerated cases not yet supported");
        }*/
    }
    
    @Override
    public void visit(Tree.InterfaceDefinition that) {
        super.visit(that);
        if (that.getAdaptedTypes()!=null) {
            that.addWarning("introductions are not yet supported");
        }
    }
    
    @Override
    public void visit(Tree.TypeAliasDeclaration that) {
        TypeAlias a = new TypeAlias();
        that.setDeclarationModel(a);
        visitDeclaration(that, a);
        Scope o = enterScope(a);
        super.visit(that);
        exitScope(o);
    }
    
    @Override
    public void visit(Tree.TypeParameterDeclaration that) {
        TypeParameter p = new TypeParameter();
        p.setDeclaration(declaration);
        p.setDefaulted(that.getTypeSpecifier()!=null);
        if (that.getTypeVariance()!=null) {
            String v = that.getTypeVariance().getText();
            p.setCovariant("out".equals(v));
            p.setContravariant("in".equals(v));
        }
        that.setDeclarationModel(p);
        visitDeclaration(that, p);
        super.visit(that);
    }
    
    @Override
    public void visit(Tree.AnyMethod that) {
        Method m = new Method();
        that.setDeclarationModel(m);
        visitDeclaration(that, m);
        Scope o = enterScope(m);
        super.visit(that);
        exitScope(o);
        setParameterLists(m, that.getParameterLists(), that);
        m.setDeclaredAnything(that.getType() instanceof Tree.VoidModifier);
        if (that.getType() instanceof Tree.ValueModifier) {
            that.getType().addError("functions may not be declared using the keyword value");
        }
    }

    private static void setParameterLists(Method m, List<Tree.ParameterList> paramLists, 
            Node that) {
        for (Tree.ParameterList pl: paramLists) {
            m.addParameterList(pl.getModel());
            pl.getModel().setFirst(false);
        }
        if (paramLists.isEmpty()) {
            that.addError("missing parameter list in function declaration", 1000);
        }
        else {
            paramLists.get(0).getModel().setFirst(true);
        }
    }
    
    @Override
    public void visit(Tree.AnyAttribute that) {
        super.visit(that);
        if (that.getType() instanceof Tree.FunctionModifier) {
            that.getType().addError("values may not be declared using the keyword function");
        }
    }

    @Override
    public void visit(Tree.MethodArgument that) {
        Method m = new Method();
        that.setDeclarationModel(m);
        visitArgument(that, m);
        Scope o = enterScope(m);
        super.visit(that);
        exitScope(o);
        setParameterLists(m, that.getParameterLists(), that);
        m.setDeclaredAnything(that.getType() instanceof Tree.VoidModifier);
    }
    
    private int fid=0;

    @Override
    public void visit(Tree.FunctionArgument that) {
        Method m = new Method();
        m.setName(Integer.toString(fid++));
        that.setDeclarationModel(m);
        visitArgument(that, m);
        Scope o = enterScope(m);
        Declaration d = beginDeclaration(that.getDeclarationModel());
        super.visit(that);
        endDeclaration(d);
        exitScope(o);
        setParameterLists(m, that.getParameterLists(), that);
        m.setDeclaredAnything(that.getType() instanceof Tree.VoidModifier);
    }

    @Override
    public void visit(Tree.ObjectDefinition that) {
        /*if (that.getClassBody()==null) {
            that.addError("missing object body");
        }*/
        Class c = new Class();
        c.setAnonymous(true);
        that.setAnonymousClass(c);
        visitDeclaration(that, c);
        Value v = new Value();
        that.setDeclarationModel(v);
        visitDeclaration(that, v);
        that.getType().setTypeModel(c.getType());
        v.setType(c.getType());
        Scope o = enterScope(c);
        super.visit(that);
        exitScope(o);
        if (c.isInterfaceMember()) {
            that.addError("object declaration may not occur directly in interface body");
        }
    }

    @Override
    public void visit(Tree.ObjectArgument that) {
        /*if (that.getClassBody()==null) {
            that.addError("missing named argument body");
        }*/
        Class c = new Class();
        c.setAnonymous(true);
        that.setAnonymousClass(c);
        visitArgument(that, c);
        Value v = new Value();
        that.setDeclarationModel(v);
        visitArgument(that, v);
        that.getType().setTypeModel(c.getType());
        v.setType(c.getType());
        Scope o = enterScope(c);
        super.visit(that);
        exitScope(o);
    }
    
    @Override
    public void visit(Tree.AttributeDeclaration that) {
        Value v = new Value();
        that.setDeclarationModel(v);
        v.setTransient(that.getSpecifierOrInitializerExpression() 
                instanceof Tree.LazySpecifierExpression);
        visitDeclaration(that, v);
        super.visit(that);
        if (v.isInterfaceMember() && !v.isFormal() && !v.isNative()) {
            if (that.getSpecifierOrInitializerExpression()==null) {
                that.addError("interface attribute must be annotated formal", 1400);
            }
            /*else {
                that.addError("interfaces may not have simple attributes");
            }*/
        }
        if (v.isLate()) {
            if (v.isFormal()) {
                that.addError("formal attribute may not be annotated late");
            }
            else if (!v.isClassOrInterfaceMember() && !v.isToplevel()) {
                that.addError("block-local value may not be annotated late");
            }
        }
        Tree.SpecifierOrInitializerExpression sie = that.getSpecifierOrInitializerExpression();
        if ( v.isFormal() && sie!=null ) {
            that.addError("formal attributes may not have a value", 1307);
        }
        Tree.Type type = that.getType();
        if (type instanceof Tree.ValueModifier) {
            if (v.isToplevel()) {
                if (sie==null) {
                    type.addError("toplevel values must explicitly specify a type");
                }
                else {
                    type.addError("toplevel value must explicitly specify a type", 200);
                }
            }
            else if (v.isShared() && !dynamic) {
                type.addError("shared value must explicitly specify a type", 200);
            }
        }
    }

    @Override
    public void visit(Tree.MethodDeclaration that) {
        super.visit(that);
        Tree.SpecifierExpression sie = that.getSpecifierExpression();
        if ( that.getDeclarationModel().isFormal() && sie!=null ) {
            that.addError("formal methods may not have a specification", 1307);
        }
        Method m = that.getDeclarationModel();
        Tree.Type type = that.getType();
        if (type instanceof Tree.FunctionModifier) {
            if (m.isToplevel()) {
                if (sie==null) {
                    type.addError("toplevel function must explicitly specify a return type");
                }
                else {
                    type.addError("toplevel function must explicitly specify a return type", 200);
                }
            }
            else if (m.isShared() && !dynamic) {
                type.addError("shared function must explicitly specify a return type", 200);
            }
        }
    }
            
    @Override
    public void visit(Tree.MethodDefinition that) {
        super.visit(that);
        Method m = that.getDeclarationModel();
        if (that.getType() instanceof Tree.FunctionModifier) {
            if (m.isToplevel()) {
                that.getType().addError("toplevel function must explicitly specify a return type", 200);
            }
            else if (m.isShared() && !dynamic) {
                that.getType().addError("shared function must explicitly specify a return type", 200);
            }
        }
    }
            
    @Override
    public void visit(Tree.AttributeGetterDefinition that) {
        Getter g = new Getter();
        that.setDeclarationModel(g);
        visitDeclaration(that, g);
        Scope o = enterScope(g);
        super.visit(that);
        exitScope(o);
        if (that.getType() instanceof Tree.ValueModifier) {
            if (g.isToplevel()) {
                that.getType().addError("toplevel getter must explicitly specify a type", 200);
            }
            else if (g.isShared() && !dynamic) {
                that.getType().addError("shared getter must explicitly specify a type", 200);
            }
        }
    }
    
    @Override
    public void visit(Tree.AttributeArgument that) {
        Getter g = new Getter();
        that.setDeclarationModel(g);
        visitArgument(that, g);
        Scope o = enterScope(g);
        super.visit(that);
        exitScope(o);
    }
    
    @Override
    public void visit(Tree.AttributeSetterDefinition that) {
        Setter s = new Setter();
        that.setDeclarationModel(s);
        visitDeclaration(that, s);
        Scope o = enterScope(s);
        Parameter p = new Parameter();
        p.setHidden(true);
        Value v = new Value();
        v.setInitializerParameter(p);
        p.setModel(v);
        v.setName(s.getName());
        p.setName(s.getName());
        p.setDeclaration(s);
        visitElement(that, v);
        unit.addDeclaration(v);
        Scope sc = getContainer(that);
        if (!(sc instanceof Package)) {
            sc.getMembers().add(v);
        }
        
        s.setParameter(p);
        super.visit(that);
        exitScope(o);
        
        if (that.getSpecifierExpression()==null &&
                that.getBlock()==null) {
            that.addError("setter declaration must have a body or => specifier");
        }
    }

    @Override
    public void visit(Tree.MissingDeclaration that) {
        Value value = new Value();
        that.setDeclarationModel(value);
        visitDeclaration(that, value);
        super.visit(that);
    }
    
    @Override
    public void visit(Tree.InitializerParameter that) {
        Parameter p = new Parameter();
        p.setDeclaration(declaration);
        p.setDefaulted(that.getSpecifierExpression()!=null);
        p.setHidden(true);
        p.setName(that.getIdentifier().getText());
        that.setParameterModel(p);
//        visitDeclaration(that, p);
        super.visit(that);
        parameterList.getParameters().add(p);
    }

    private static Tree.SpecifierOrInitializerExpression getSpecifier(
            Tree.ValueParameterDeclaration that) {
        return ((Tree.AttributeDeclaration) that.getTypedDeclaration())
                .getSpecifierOrInitializerExpression();
    }

    private static Tree.SpecifierExpression getSpecifier(
            Tree.FunctionalParameterDeclaration that) {
        return ((Tree.MethodDeclaration) that.getTypedDeclaration())
                .getSpecifierExpression();
    }
    
    @Override
    public void visit(Tree.ValueParameterDeclaration that) {
        Parameter p = new Parameter();
        p.setDeclaration(declaration);
        p.setDefaulted(getSpecifier(that)!=null);
        Tree.Type type = that.getTypedDeclaration().getType();
        p.setSequenced(type instanceof Tree.SequencedType);
        that.setParameterModel(p);
        super.visit(that);
        Value v = (Value) that.getTypedDeclaration().getDeclarationModel();
        p.setName(v.getName());
        p.setModel(v);
        v.setInitializerParameter(p);
        parameterList.getParameters().add(p);
        if (p.isSequenced() && p.isDefaulted()) {
            getSpecifier(that).addError("variadic parameter may not specify default argument");
        }
        if (p.isSequenced() && ((Tree.SequencedType) type).getAtLeastOne()) {
//            that.getType().addWarning("nonempty variadic parameters are not yet supported");
            p.setAtLeastOne(true);
        }
        if (v.isFormal()) {
            that.addError("parameters may not be annotated formal", 1312);
        }
        if (v.isVariable()) {
            that.addError("parameter may not be annotated variable");
        }
    }

    @Override
    public void visit(Tree.FunctionalParameterDeclaration that) {
        Parameter p = new Parameter();
        p.setDeclaration(declaration);
        p.setDefaulted(getSpecifier(that)!=null);
        Tree.Type type = that.getTypedDeclaration().getType();
        p.setDeclaredAnything(type instanceof Tree.VoidModifier);
        that.setParameterModel(p);
        super.visit(that);
        Method m = (Method) that.getTypedDeclaration().getDeclarationModel();
        p.setModel(m);
        p.setName(m.getName());
        m.setInitializerParameter(p);
        parameterList.getParameters().add(p);
        if (type instanceof Tree.SequencedType) {
            type.addError("functional parameter type may not be variadic");
        }
        if (m.isFormal()) {
            that.addError("parameters may not be annotated formal", 1312);
        }
    }

    @Override
    public void visit(Tree.ParameterList that) {
        ParameterList pl = parameterList;
        parameterList = new ParameterList();
        that.setModel(parameterList);
        super.visit(that);
        parameterList = pl;        
    }
    
    private int id=0;
    
    @Override
    public void visit(Tree.ControlClause that) {
        ControlBlock cb = new ControlBlock();
        cb.setId(id++);
        that.setControlBlock(cb);
        visitElement(that, cb);
        Scope o = enterScope(cb);
        super.visit(that);
        exitScope(o);
    }
    
    @Override
    public void visit(Tree.Condition that) {
        ConditionScope cb = new ConditionScope();
        cb.setId(id++);
        that.setScope(cb);
        visitElement(that, cb);
        enterScope(cb);
        super.visit(that);
    }
    
    @Override
    public void visit(Tree.Body that) {
        int oid=id;
        id=0;
        super.visit(that);
        id=oid;
    }
    
    @Override
    public void visit(Tree.NamedArgumentList that) {
        NamedArgumentList nal = new NamedArgumentList();
        nal.setId(id++);
        for (Tree.NamedArgument na: that.getNamedArguments()) {
            if (na.getIdentifier()!=null) {
                nal.getArgumentNames().add(na.getIdentifier().getText());
            }
        }
        that.setNamedArgumentList(nal);
        visitElement(that, nal);
        Scope o = enterScope(nal);
        super.visit(that);
        exitScope(o);
    }
    
    @Override
    public void visit(Tree.Variable that) {
        if (that.getSpecifierExpression()!=null) {
            Scope s = scope;
            if (scope instanceof ControlBlock) {
                scope = scope.getContainer();
            }
            that.getSpecifierExpression().visit(this);
            scope = s;
        }
        
        Value v = new Value();
        that.setDeclarationModel(v);
        visitDeclaration(that, v, !(that.getType() instanceof Tree.SyntheticVariable));
        setVisibleScope(v);
        
        if (that.getType()!=null) {
            that.getType().visit(this);
        }
        if (that.getIdentifier()!=null) {
            that.getIdentifier().visit(this);
        }
        
        //TODO: scope should be the variable, not the 
        //      containing control structure:
        if (that.getAnnotationList()!=null) {
            that.getAnnotationList().visit(this);
        }
        for (Tree.ParameterList pl: that.getParameterLists()) {
            pl.visit(this);
        }
        
        //TODO: parameters of callable variables are allowed 
        //      in for loops, according to the language spec
        /*Declaration od = beginDeclaration(v);
        Scope os = enterScope(v);
        if (that.getAnnotationList()!=null) {
            that.getAnnotationList().visit(this);
        }
        for (Tree.ParameterList pl: that.getParameterLists()) {
            pl.visit(this);
        }
        exitScope(os);
        endDeclaration(od);
        */
        
        if (that.getParameterLists().isEmpty()) {
            if (that.getType() instanceof Tree.FunctionModifier) {
                that.getType().addError("variables with no parameters may not be declared using the keyword function");
            }
            if (that.getType() instanceof Tree.VoidModifier) {
                that.getType().addError("variables with no parameters may not be declared using the keyword void");
            }
        }
        else {
            Tree.ParameterList pl = that.getParameterLists().get(0);
            pl.addWarning("variables with parameter lists are not yet supported");
            if (that.getType() instanceof Tree.ValueModifier) {
                that.getType().addError("variables with parameters may not be declared using the keyword value");
            }
        }
                
        that.setScope(scope);
        that.setUnit(unit);
    }
    
    private static List<TypeParameter> getTypeParameters(Tree.TypeParameterList tpl) {
        List<TypeParameter> typeParameters = new ArrayList<TypeParameter>();
        if (tpl!=null) {
            boolean foundDefaulted=false;
            for (Tree.TypeParameterDeclaration tp: tpl.getTypeParameterDeclarations()) {
                typeParameters.add(tp.getDeclarationModel());
                if (tp.getTypeSpecifier()==null) {
                    if (foundDefaulted) {
                        tp.addError("required type parameter follows defaulted type parameter");
                    }
                }
                else {
                    foundDefaulted=true;
                }
            }
        }
        return typeParameters;
    }
    
    @Override public void visit(Tree.Declaration that) {
        Declaration model = that.getDeclarationModel();
        Declaration d = beginDeclaration(model);
        super.visit(that);
        endDeclaration(d);
        if (model.isClassOrInterfaceMember() &&
                ((ClassOrInterface) model.getContainer()).isFinal()) {
            if (model.isDefault()) {
                that.addError("member of final class may not be annotated default");
            }
        }
    }

    private void handleDeclarationAnnotations(Tree.Declaration that,
            Declaration model) {
        Tree.AnnotationList al = that.getAnnotationList();
        if (hasAnnotation(al, "shared", unit)) {
            if (that instanceof Tree.AttributeSetterDefinition) {
                that.addError("setter may not be annotated shared", 1201);
            }
            /*else if (that instanceof Tree.TypedDeclaration && !(that instanceof Tree.ObjectDefinition)) {
                Tree.Type t =  ((Tree.TypedDeclaration) that).getType();
                if (t instanceof Tree.ValueModifier || t instanceof Tree.FunctionModifier) {
                    t.addError("shared declarations must explicitly specify a type", 200);
                }
                else {
                    model.setShared(true);
                }
            }*/
            else {
                model.setShared(true);
            }
        }
        if (hasAnnotation(al, "default", unit)) {
            if (that instanceof Tree.ObjectDefinition) {
                that.addError("object declaration may not be annotated default", 1313);
            }
            /*else if (that instanceof Tree.Parameter) {
                that.addError("parameters may not be annotated default", 1313);
            }*/
            else {
                model.setDefault(true);
            }
        }
        if (hasAnnotation(al, "formal", unit)) {
            if (that instanceof Tree.ObjectDefinition) {
                that.addError("object declaration may not be annotated formal", 1312);
            }
            else {
                model.setFormal(true);
            }
        }
        if (hasAnnotation(al, "native", unit)) {
            model.setNative(true);
        }
        if (model.isFormal() && model.isDefault()) {
            that.addError("declaration may not be annotated both formal and default");
        }
        if (hasAnnotation(al, "actual", unit)) {
            model.setActual(true);
        }
        if (hasAnnotation(al, "abstract", unit)) {
            if (model instanceof Class) {
                ((Class) model).setAbstract(true);
            }
            else {
                that.addError("declaration is not a class, and may not be annotated abstract", 1600);
            }
        }
        if (hasAnnotation(al, "final", unit)) {
            if (model instanceof Class) {
                ((Class) model).setFinal(true);
            }
            else {
                that.addError("declaration is not a class, and may not be annotated final", 1700);
            }
        }
        if (hasAnnotation(al, "variable", unit)) {
            if (model instanceof Value) {
                ((Value) model).setVariable(true);
            }
            else {
                that.addError("declaration is not a value, and may not be annotated variable", 1500);
            }
        }
        if (hasAnnotation(al, "late", unit)) {
            if (model instanceof Value) {
                if (that instanceof Tree.AttributeDeclaration && 
                        ((Tree.AttributeDeclaration) that).getSpecifierOrInitializerExpression()==null) {
                    ((Value) model).setLate(true);
                }
                else {
                    that.addError("value is not an uninitialized reference, and may not be annotated late");
                }
            }
            else {
                that.addError("declaration is not a value, and may not be annotated late");
            }
        }
        if (model instanceof Value) {
            Value value = (Value) model;
            if (value.isVariable() && value.isTransient()) {
                that.addError("value may not be annotated both variable and transient: " + model.getName());
            }
        }
        if (hasAnnotation(al, "deprecated", unit)) {
            model.setDeprecated(true);
        }
        if (hasAnnotation(al, "annotation", unit)) {
            if (!(model instanceof Method) && !(model instanceof Class)) {
                that.addError("declaration is not a function or class, and may not be annotated annotation");
            }
            else if (!model.isToplevel()) {
                that.addError("declaration is not toplevel, and may not be annotated annotation");
            }
            else {
                model.setAnnotation(true);
            }
        }
        
        buildAnnotations(al, model.getAnnotations());        
    }

    public static void setVisibleScope(Declaration model) {
        Scope s=model.getContainer();
        while (s!=null) {
            if (s instanceof Declaration) {
                if (model.isShared()) {
                    if (!((Declaration) s).isShared()) {
                        model.setVisibleScope(s.getContainer());
                        break;
                    }
                }
                else {
                    model.setVisibleScope(s);
                    break;
                }
            }
            else if (s instanceof Package) {
                //TODO: unshared packages!
                /*if (!((Package) s).isShared()) {
                    model.setVisibleScope(s);
                }*/
                if (!model.isShared()) {
                    model.setVisibleScope(s);
                }
                //null visible scope means visible everywhere
                break;
            }
            else {
                model.setVisibleScope(s);
                break;
            }    
            s = s.getContainer();
        }
    }

    private static void checkFormalMember(Tree.Declaration that, Declaration d) {
        
        if (d.isFormal()) {
            if (d.getContainer() instanceof ClassOrInterface) {
                ClassOrInterface ci = (ClassOrInterface) d.getContainer();
                if (!ci.isAbstract() && !ci.isFormal()) {
                    that.addError("formal member belongs to a concrete class", 900);
                }
            } 
            else {
                that.addError("formal member does not belong to an interface or abstract class", 1100);
            }
            if (!(that instanceof Tree.AttributeDeclaration) && 
                !(that instanceof Tree.MethodDeclaration) &&
                !(that instanceof Tree.AnyClass)) {
                that.addError("formal member may not have a body", 1100);
            }
        }
        
        if (d.isNative()) {
            if (d.getContainer() instanceof Declaration) {
                Declaration ci = (Declaration) d.getContainer();
                if (!ci.isNative()) {
                    that.addError("native member belongs to a non-native declaration");
                }
            }
        }
        
        /*if ( !d.isFormal() && 
                d.getContainer() instanceof Interface && 
                !(that instanceof Tree.TypeParameterDeclaration) &&
                !(that instanceof Tree.ClassDeclaration) &&
                !(that instanceof Tree.InterfaceDeclaration)) {
            that.addWarning("concrete members of interfaces not yet supported");
        }*/
        
    }
        
    @Override public void visit(Tree.TypedArgument that) {
        Declaration d = beginDeclaration(that.getDeclarationModel());
        super.visit(that);
        endDeclaration(d);
        //that.addWarning("declaration-style named arguments not yet supported");
    }

    @Override
    public void visit(Tree.TypeConstraint that) {
        String name = name(that.getIdentifier());
        TypeParameter p = (TypeParameter) scope.getMemberOrParameter(unit, 
        		name, null, false);
        that.setDeclarationModel(p);
        if (p==null) {
            that.addError("no matching type parameter for constraint: " + 
                    name);
            p = new TypeParameter();
            p.setDeclaration(declaration);
            that.setDeclarationModel(p);
            visitDeclaration(that, p);
        }
        else {
        	if (p.isConstrained()) {
        		that.addError("duplicate constraint list for type parameter: " +
        				name);
        	}
        	p.setConstrained(true);
        }
        
        Scope o = enterScope(p);
        super.visit(that);
        exitScope(o);

        if ( that.getAbstractedType()!=null ) {
            that.addWarning("lower bound type constraints are not yet supported");
        }
        /*if ( that.getCaseTypes()!=null ) {
            that.addWarning("enumerated type constraints are not yet supported");
        }*/
        if ( that.getParameterList()!=null ) {
            that.addWarning("parameter bounds are not yet supported");
            p.addParameterList(that.getParameterList().getModel());
        }
    }
    
    @Override
    public void visit(Tree.SpecifierStatement that) {
        Specification s = new Specification();
        s.setId(id++);
        visitElement(that, s);
        Scope o = enterScope(s);
        super.visit(that);
        exitScope(o);
    }

    /*@Override
    public void visit(Tree.SpecifiedArgument that) {
        Specification s = new Specification();
        visitElement(that, s);
        Scope o = enterScope(s);
        super.visit(that);
        exitScope(o);
    }*/
    
    @Override
    public void visit(Tree.SatisfiesCondition that) {
        super.visit(that);
        that.addWarning("satisfies conditions are not yet supported");
    }

    /*@Override
    public void visit(Tree.Comprehension that) {
        super.visit(that);
        that.addWarning("comprehensions are not yet supported");
    }*/

    @Override
    public void visit(Tree.Assertion that) {
        Declaration d = beginDeclaration(null);
        super.visit(that);
        endDeclaration(d);
    }    

    @Override
    public void visit(Tree.AnnotationList that) {
        Scope s = scope;
        if (declaration instanceof Scope) {
            scope = scope.getContainer();
        }
        super.visit(that);
        scope = s;
    }
    
    static final String digits = "\\d+";
    static final String groups = "\\d{1,3}(_\\d{3})+";
    static final String fractionalGroups = "(\\d{3}_)+\\d{1,3}";
    static final String magnitude = "k|M|G|T|P";
    static final String fractionalMagnitude = "m|u|n|p|f";
    static final String exponent = "(e|E)(\\+|-)?" + digits;

    static final String hexDigits = "(\\d|[a-f]|[A-F])+";
    static final String hexGroups = "(\\d|[a-f]|[A-F]){1,4}(_(\\d|[a-f]|[A-F]){4})+|(\\d|[a-f]|[A-F]){1,2}(_(\\d|[a-f]|[A-F]){2})+";
    
    static final String binDigits = "(0|1)+";
    static final String binGroups = "(0|1){1,4}(_(0|1){4})+";
    
    @Override
    public void visit(Tree.NaturalLiteral that) {
        super.visit(that);
        String text = that.getToken().getText();
        if (!text.matches("^(" + digits + "|" + groups + ")(" + magnitude + ")?$") &&
            !text.matches("#(" + hexDigits + "|" + hexGroups + ")") &&
            !text.matches("\\$(" + binDigits + "|" + binGroups + ")")) {
            that.addError("illegal integer literal format");
        }        
    }
    
    @Override
    public void visit(Tree.FloatLiteral that) {
        super.visit(that);
        String text = that.getToken().getText();
        if (!text.matches("^(" + digits + "|" + groups + ")(\\.(" + 
                digits + "|" + fractionalGroups  + ")(" + 
                magnitude + "|" + fractionalMagnitude + "|" + exponent + ")?|" +
                fractionalMagnitude + ")$")) {
            that.addError("illegal floating literal format");
        }
    }
    
    @Override
    public void visit(Tree.PositionalArgumentList that) {
        super.visit(that);
        checkPositionalArguments(that.getPositionalArguments());
    }

    @Override
    public void visit(Tree.SequencedArgument that) {
        super.visit(that);
        checkPositionalArguments(that.getPositionalArguments());
    }

    private void checkPositionalArguments(List<Tree.PositionalArgument> args) {
        for (int i=0; i<args.size()-1; i++) {
            Tree.PositionalArgument a = args.get(i);
            if (a instanceof Tree.SpreadArgument) {
                a.addError("spread argument must be the last argument in the argument list");
            }
            if (a instanceof Tree.Comprehension) {
                a.addError("comprehension must be the last argument in the argument list");
            }
        }
    }    
    
    @Override
    public void visit(Tree.TryCatchStatement that) {
        super.visit(that);
        if (that.getCatchClauses().isEmpty() && 
                that.getFinallyClause()==null && 
                that.getTryClause().getResourceList()==null) {
            that.addError("try must have a catch, finally, or resource expression");
        }
    }
    
    @Override
    public void visit(Tree.Import that) {
        super.visit(that);
        Tree.ImportPath path = that.getImportPath();
        if (path!=null && 
                formatPath(path.getIdentifiers()).equals("ceylon.language")) {
            Tree.ImportMemberOrTypeList imtl = that.getImportMemberOrTypeList();
            if (imtl!=null) {
                for (Tree.ImportMemberOrType imt: imtl.getImportMemberOrTypes()) {
                    if (imt.getAlias()!=null && imt.getIdentifier()!=null) {
                        String name = name(imt.getIdentifier());
                        String alias = name(imt.getAlias().getIdentifier());
                        Map<String, String> mods = unit.getModifiers();
                        if (mods.containsKey(name)) {
                            mods.put(name, alias);
                        }
                    }
                }
            }
        }
    }
}
