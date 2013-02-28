package com.redhat.ceylon.compiler.typechecker.analyzer;

import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.buildAnnotations;
import static com.redhat.ceylon.compiler.typechecker.tree.Util.hasAnnotation;
import static com.redhat.ceylon.compiler.typechecker.tree.Util.name;

import java.util.ArrayList;
import java.util.List;

import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.ClassAlias;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.ConditionScope;
import com.redhat.ceylon.compiler.typechecker.model.ControlBlock;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Element;
import com.redhat.ceylon.compiler.typechecker.model.Functional;
import com.redhat.ceylon.compiler.typechecker.model.FunctionalParameter;
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
import com.redhat.ceylon.compiler.typechecker.model.ValueParameter;
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
                else if (!(member instanceof Getter)) {
                    that.addError("setter name does not resolve to matching getter: " + model.getName());
                }
                else {
                    Getter getter = (Getter) member;
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
                    name(that.getIdentifier()), 1000);
        }
        //TODO: is this still necessary??
        if (c.isClassOrInterfaceMember() && 
                c.getContainer() instanceof TypedDeclaration) {
            that.addWarning("nested classes of inner classes are not yet supported");
        }
        /*if (c.isActual()) {
        	that.addWarning("member class refinement not yet supported");
        }*/
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
        checkMethodParameters(that);
        m.setDeclaredAnything(that.getType() instanceof Tree.VoidModifier);
        if (that.getType() instanceof Tree.ValueModifier) {
            that.getType().addError("functions may not be declared using the keyword value");
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
        checkMethodArgumentParameters(that);
        m.setDeclaredAnything(that.getType() instanceof Tree.VoidModifier);
    }

    @Override
    public void visit(Tree.FunctionArgument that) {
        Method m = new Method();
        that.setDeclarationModel(m);
        visitArgument(that, m);
        Scope o = enterScope(m);
        Declaration d = beginDeclaration(that.getDeclarationModel());
        super.visit(that);
        endDeclaration(d);
        exitScope(o);
        checkFunctionArgumentParameters(that);
        m.setDeclaredAnything(that.getType() instanceof Tree.VoidModifier);
        //that.addWarning("inline functions not yet supported");
    }

    private static void checkMethodParameters(Tree.AnyMethod that) {
        if (that.getParameterLists().isEmpty()) {
            that.addError("missing parameter list in function declaration: " + 
                    name(that.getIdentifier()), 1000 );
        }
        /*if ( that.getParameterLists().size()>1 ) {
            that.addWarning("higher-order methods are not yet supported");
        }*/
    }

    private static void checkFunctionArgumentParameters(Tree.FunctionArgument that) {
        if (that.getParameterLists().isEmpty()) {
            that.addError("missing parameter list in functional argument declaration");
        }
        /*if ( that.getParameterLists().size()>1 ) {
            that.addWarning("higher-order methods are not yet supported");
        }*/
    }

    private static void checkMethodArgumentParameters(Tree.MethodArgument that) {
        if (that.getParameterLists().isEmpty()) {
            that.addError("missing parameter list in named argument declaration: " + 
                    name(that.getIdentifier()) );
        }
        /*if ( that.getParameterLists().size()>1 ) {
            that.addWarning("higher-order methods are not yet supported");
        }*/
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
        if (that.getType() instanceof Tree.ValueModifier) {
            if (v.isToplevel()) {
                if (sie==null) {
                    that.getType().addError("toplevel values must explicitly specify a type");
                }
                else {
                    that.getType().addError("toplevel value must explicitly specify a type", 200);
                }
            }
            else if (v.isShared()) {
                that.getType().addError("shared value must explicitly specify a type", 200);
            }
            else if (sie==null) {
                that.getType().addError("value must specify an explicit type or definition", 200);
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
        if (that.getType() instanceof Tree.FunctionModifier) {
            if (m.isToplevel()) {
                if (sie==null) {
                    that.getType().addError("toplevel function must explicitly specify a return type");
                }
                else {
                    that.getType().addError("toplevel function must explicitly specify a return type", 200);
                }
            }
            else if (m.isShared()) {
                that.getType().addError("shared function must explicitly specify a return type", 200);
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
            else if (m.isShared()) {
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
                that.getType().addError("toplevel value must explicitly specify a type", 200);
            }
            else if (g.isShared()) {
                that.getType().addError("shared attribute must explicitly specify a type", 200);
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
        ValueParameter p = new ValueParameter();
        p.setName(s.getName());
        p.setDeclaration(s);
        visitElement(that, p);
        unit.addDeclaration(p);
        Scope sc = getContainer(that);
        if (!(sc instanceof Package)) {
        	sc.getMembers().add(p);
        }
        
        s.setParameter(p);
        super.visit(that);
        exitScope(o);
    }

    @Override
    public void visit(Tree.MissingDeclaration that) {
        Value value = new Value();
        that.setDeclarationModel(value);
        visitDeclaration(that, value);
        super.visit(that);
    }
    
    @Override
    public void visit(Tree.Parameter that) {
        super.visit(that);
        if (that.getDefaultArgument()!=null) {
            Tree.SpecifierExpression se = that.getDefaultArgument().getSpecifierExpression();
            if (se!=null) {
            	if (that.getScope() instanceof Specification) {
            		se.addError("parameter of specification statement may not define default value");
            	}
            	else {
            		Declaration d = that.getDeclarationModel().getDeclaration();
            		if (d.isActual()) {
            			se.addError("parameter of actual declaration may not define default value: parameter " +
            					name(that.getIdentifier()) + " of " + declaration.getName());
            		}
            		if (d instanceof Parameter) {
            			se.addError("parameter of callable parameter may not have default argument");
            		}
            	}
                /*if (declaration instanceof Method &&
                    !declaration.isToplevel() &&
                    !(declaration.isClassOrInterfaceMember() && 
                            ((Declaration) declaration.getContainer()).isToplevel())) {
                    se.addWarning("default arguments for parameters of inner methods not yet supported");
                }
                if (declaration instanceof Class && 
                        !declaration.isToplevel()) {
                    se.addWarning("default arguments for parameters of inner classes not yet supported");
                }*/
                    /*else {
                    se.addWarning("parameter default values are not yet supported");
                }*/
            }
        }
    }
    
    @Override
    public void visit(Tree.ValueParameterDeclaration that) {
        ValueParameter p = new ValueParameter();
        p.setDeclaration(declaration);
        p.setDefaulted(that.getDefaultArgument()!=null);
        p.setSequenced(that.getType() instanceof Tree.SequencedType);
        p.setHidden(that instanceof Tree.InitializerParameter);
        that.setDeclarationModel(p);
        visitDeclaration(that, p);
        super.visit(that);
        parameterList.getParameters().add(p);
        if (p.isSequenced() && p.isDefaulted()) {
        	that.getDefaultArgument()
        	    .addError("variadic parameter may not specify default argument");
        }
        if (p.isSequenced() && ((Tree.SequencedType) that.getType()).getAtLeastOne()) {
            that.getType().addWarning("nonempty variadic parameters are not yet supported");
        }
    }

    @Override
    public void visit(Tree.FunctionalParameterDeclaration that) {
        FunctionalParameter p = new FunctionalParameter();
        p.setDeclaration(declaration);
        Tree.DefaultArgument da = that.getDefaultArgument();
		p.setDefaulted(da!=null);
        p.setDeclaredAnything(that.getType() instanceof Tree.VoidModifier);
        that.setDeclarationModel(p);
        visitDeclaration(that, p);
        Scope o = enterScope(p);
        super.visit(that);
        exitScope(o);
        parameterList.getParameters().add(p);
        if (that.getType() instanceof Tree.SequencedType) {
        	that.getType().addError("functional parameter type may not be variadic");
        }
        if (that.getParameterLists().isEmpty()) {
        	that.addError("missing parameter list");
        }
    }

    @Override
    public void visit(Tree.ParameterList that) {
        ParameterList pl = parameterList;
        parameterList = new ParameterList();
        that.setModel(parameterList);
        super.visit(that);
        if (scope instanceof Functional) {
            Functional f = (Functional) scope;
            boolean first = f.getParameterLists().isEmpty();
            parameterList.setFirst(first);
            if (f instanceof Class && !first) {
                that.addError("classes may have only one parameter list");
            }
            else {
                f.addParameterList(parameterList);
            }
        }
        else {
        	if (!(scope instanceof Specification)) {
        		that.addError("may not have a parameter list");
        	}
            parameterList.setFirst(true);
        }
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
    }

    private static void handleDeclarationAnnotations(Tree.Declaration that,
            Declaration model) {
        Tree.AnnotationList al = that.getAnnotationList();
        if (hasAnnotation(al, "shared")) {
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
        if (hasAnnotation(al, "default")) {
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
        if (hasAnnotation(al, "formal")) {
            if (that instanceof Tree.ObjectDefinition) {
                that.addError("object declaration may not be annotated formal", 1312);
            }
            else if (that instanceof Tree.Parameter) {
            	that.addError("parameters may not be annotated formal", 1312);
            }
            else {
                model.setFormal(true);
            }
        }
        if (hasAnnotation(al, "native")) {
        	model.setNative(true);
        }
        if (model.isFormal() && model.isDefault()) {
            that.addError("declaration may not be annotated both formal and default");
        }
        if (hasAnnotation(al, "actual")) {
            model.setActual(true);
        }
        if (hasAnnotation(al, "abstract")) {
            if (model instanceof Class) {
                ((Class) model).setAbstract(true);
            }
            else {
                that.addError("declaration is not a class, and may not be annotated abstract", 1600);
            }
        }
        if (hasAnnotation(al, "final")) {
            if (model instanceof Class) {
                ((Class) model).setFinal(true);
            }
            else {
                that.addError("declaration is not a class, and may not be annotated final", 1700);
            }
        }
        if (hasAnnotation(al, "variable")) {
            if (model instanceof Value) {
                ((Value) model).setVariable(true);
            }
            else if (model instanceof ValueParameter) {
                that.addError("parameter may not be annotated variable: " + model.getName());
            }
            else {
                that.addError("declaration is not a value, and may not be annotated variable", 1500);
            }
        }
        if (hasAnnotation(al, "late")) {
            if (model instanceof Value) {
                ((Value) model).setLate(true);
            }
            else {
            	that.addError("declaration is not a reference value, and may not be annotated late");
            }
        }
        if (model instanceof Value) {
        	Value value = (Value) model;
        	if (value.isVariable() && value.isTransient()) {
        		that.addError("value may not be annotated both variable and transient: " + model.getName());
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
        TypeParameter p = (TypeParameter) scope.getMemberOrParameter(unit, 
        		name(that.getIdentifier()), null, false);
        that.setDeclarationModel(p);
        if (p==null) {
            that.addError("no matching type parameter for constraint: " + 
                    name(that.getIdentifier()));
            p = new TypeParameter();
            p.setDeclaration(declaration);
            that.setDeclarationModel(p);
            visitDeclaration(that, p);
        }
        else {
        	if (p.isConstrained()) {
        		that.addError("duplicate constraint list for type parameter: " +
        				p.getName());
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
        }
    }
    
    @Override
    public void visit(Tree.SpecifierStatement that) {
    	Specification s = new Specification();
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
	static final String hexGroups = "(\\d|[a-f]|[A-F]){1,4}(_(\\d|[a-f]|[A-F]){4})+";
	
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
    
}
