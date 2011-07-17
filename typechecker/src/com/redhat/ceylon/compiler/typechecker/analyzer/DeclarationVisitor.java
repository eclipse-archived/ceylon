package com.redhat.ceylon.compiler.typechecker.analyzer;

import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.*;

import java.util.ArrayList;
import java.util.List;

import com.redhat.ceylon.compiler.typechecker.model.Annotation;
import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.ClassAlias;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.ControlBlock;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Element;
import com.redhat.ceylon.compiler.typechecker.model.Functional;
import com.redhat.ceylon.compiler.typechecker.model.FunctionalParameter;
import com.redhat.ceylon.compiler.typechecker.model.Getter;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.InterfaceAlias;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.ParameterList;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.Setter;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.Unit;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.model.ValueParameter;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

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
    
    public DeclarationVisitor(Package pkg, String filename) {
        scope = pkg;
        this.pkg = pkg;
        this.filename = filename;
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
        unit.getDeclarations().add(model);
        scope.getMembers().add(model);

        handleDeclarationAnnotations(that, model);        

        setVisibleScope(model);

        checkFormalMember(that, model);
    }

    private void visitArgument(Tree.NamedArgument that, Declaration model) {
        Tree.Identifier id = that.getIdentifier();
        setModelName(that, model, id);
        visitElement(that, model);
        //that.setDeclarationModel(model);
        unit.getDeclarations().add(model);
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
                //a setter must have a matching getter
                Declaration member = model.getContainer().getDirectMember( model.getName() );
                if (member==null) {
                    that.addError("setter with no matching getter: " + model.getName());
                }
                else if (!(member instanceof Getter)) {
                    that.addError("setter name does not resolve to matching getter: " + model.getName());
                }
                else {
                    Getter getter = (Getter) member;
                    ((Setter) model).setGetter(getter);
                    if (getter.isVariable()) {
                        that.addError("duplicate setter for getter: " + model.getName());
                    }
                    else {
                        getter.setVariable(true);
                    }
                }
            }
            else if ((model instanceof Getter || model instanceof Value) 
                        && model.isClassMember()) {
                //a getter or simple attribute is allowed to have the 
                //same name as a class initialization parameter
                Declaration member = model.getContainer().getDirectMember( model.getName() );
                if (member!=null) {
                    that.addError("duplicate declaration name: " + model.getName());
                }
            }
            else {
                Scope s = model.getContainer();
                boolean isControl;
                do {
                    if ( s.getDirectMemberOrParameter(model.getName())!=null ) {
                        that.addError("duplicate declaration name: " + model.getName());
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
        TypeDeclaration d = that.getDeclarationModel();
        if (d==null) {
            //TODO: this case is temporary until we have type constraints!
        }
        else {
            d.setTypeParameters(getTypeParameters(that.getTypeParameterList()));
        }
    }
    
    @Override
    public void visit(Tree.ClassOrInterface that) {
        super.visit(that);
        if ( that.getCaseTypes()!=null ) {
            that.addWarning("types with enumerated cases not yet supported");
        }
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
                    name(that.getIdentifier()) );
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
    }
    
    @Override
    public void visit(Tree.InterfaceDefinition that) {
        super.visit(that);
        if (that.getAdaptedTypes()!=null) {
            that.addWarning("introductions are not yet supported");
        }
    }
    
    @Override
    public void visit(Tree.TypeParameterDeclaration that) {
        TypeParameter p = new TypeParameter();
        p.setDeclaration(declaration);
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
        that.getDeclarationModel().setTypeParameters(getTypeParameters(that.getTypeParameterList()));
        if (that.getType() instanceof Tree.ValueModifier) {
            that.getType().addError("methods may not be declared using the keyword value");
        }
    }
    
    @Override
    public void visit(Tree.AnyAttribute that) {
        super.visit(that);
        if (that.getType() instanceof Tree.FunctionModifier) {
            that.getType().addError("attributes may not be declared using the keyword function");
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
    }

    private static void checkMethodParameters(Tree.AnyMethod that) {
        if (that.getParameterLists().isEmpty()) {
            that.addError("missing parameter list in method declaration: " + 
                    name(that.getIdentifier()) );
        }
        if ( that.getParameterLists().size()>1 ) {
            that.addWarning("higher-order methods are not yet supported");
        }
    }

    private static void checkMethodArgumentParameters(Tree.MethodArgument that) {
        if (that.getParameterLists().isEmpty()) {
            that.addError("missing parameter list in named argument declaration: " + 
                    name(that.getIdentifier()) );
        }
        if ( that.getParameterLists().size()>1 ) {
            that.addWarning("higher-order methods are not yet supported");
        }
    }

    @Override
    public void visit(Tree.ObjectDefinition that) {
        if (that.getClassBody()==null) {
            that.addError("missing object body");
        }
        Class c = new Class();
        visitDeclaration(that, c);
        Value v = new Value();
        that.setDeclarationModel(v);
        visitDeclaration(that, v);
        that.getType().setTypeModel(c.getType());
        v.setType(c.getType());
        Scope o = enterScope(c);
        super.visit(that);
        exitScope(o);
    }

    @Override
    public void visit(Tree.ObjectArgument that) {
        if (that.getClassBody()==null) {
            that.addError("missing named argument body");
        }
        Class c = new Class();
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
        visitDeclaration(that, v);
        super.visit(that);
        if ( v.isInterfaceMember() && !v.isFormal()) {
            that.addError("interfaces may not have simple attributes");
        }
        if ( v.isFormal() && that.getSpecifierOrInitializerExpression()!=null ) {
            that.addError("formal attributes may not have a value");
        }
    }

    @Override
    public void visit(Tree.MethodDeclaration that) {
        super.visit(that);
        if ( that.getSpecifierExpression()!=null ) {
            that.addWarning("method definition by reference is not yet supported");
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
        super.visit(that);
        exitScope(o);
    }
    
    @Override
    public void visit(Tree.ValueParameterDeclaration that) {
        ValueParameter p = new ValueParameter();
        p.setDeclaration(declaration);
        p.setDefaulted(that.getSpecifierExpression()!=null);
        p.setSequenced(that.getType() instanceof Tree.SequencedType);
        that.setDeclarationModel(p);
        visitDeclaration(that, p);
        super.visit(that);
        parameterList.getParameters().add(p);
    }

    @Override
    public void visit(Tree.FunctionalParameterDeclaration that) {
        FunctionalParameter p = new FunctionalParameter();
        p.setDeclaration(declaration);
        p.setDefaulted(that.getSpecifierExpression()!=null);
        that.setDeclarationModel(p);
        visitDeclaration(that, p);
        Scope o = enterScope(p);
        super.visit(that);
        exitScope(o);
        parameterList.getParameters().add(p);
        that.addWarning("higher order methods are not yet supported");
    }

    @Override
    public void visit(Tree.ParameterList that) {
        ParameterList pl = parameterList;
        parameterList = new ParameterList();
        super.visit(that);
        Functional f = (Functional) scope;
        if ( f instanceof Class && 
                !f.getParameterLists().isEmpty() ) {
            that.addError("classes may have only one parameter list");
        }
        else {
            f.addParameterList(parameterList);
        }
        parameterList = pl;
        
        boolean foundSequenced = false;
        boolean foundDefault = false;
        for (Tree.Parameter p: that.getParameters()) {
            if (p.getSpecifierExpression()!=null) {
                if (foundSequenced) {
                    p.addError("default parameter must occur before sequenced parameter");
                }
                foundDefault = true;
            }
            else if (p.getType() instanceof Tree.SequencedType) {
                foundSequenced = true;
            }
            else {
                if (foundDefault) {
                    p.addError("required parameter must occur before default parameters");
                }
                if (foundSequenced) {
                    p.addError("required parameter must occur before sequenced parameter");
                }
            }
        }
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
        that.setDeclarationModel(v);
        visitDeclaration(that, v, !(that.getType() instanceof Tree.SyntheticVariable));
        setVisibleScope(v);
        if (that.getType()!=null) {
            that.getType().visit(this);
        }
        if (that.getIdentifier()!=null) {
            that.getIdentifier().visit(this);
        }
        if (that.getAnnotationList()!=null) {
            that.getAnnotationList().visit(this);
        }
        //TODO: parameters of callable variables?!
        /*if (that.getParameterLists().size()==0) {
            if (that.getType() instanceof Tree.FunctionModifier) {
                that.getType().addError("variables with no parameters may not be declared using the keyword function");
            }
        }
        else {
            if (that.getType() instanceof Tree.ValueModifier) {
                that.getType().addError("variables with parameters may not be declared using the keyword value");
            }
        }*/
        that.setScope(scope);
        that.setUnit(unit);
    }
    
    private static List<TypeParameter> getTypeParameters(Tree.TypeParameterList tpl) {
        List<TypeParameter> typeParameters = new ArrayList<TypeParameter>();
        if (tpl!=null) {
            for (Tree.TypeParameterDeclaration tp: tpl.getTypeParameterDeclarations()) {
                typeParameters.add(tp.getDeclarationModel());
            }
        }
        return typeParameters;
    }
    
    private static boolean hasAnnotation(Tree.AnnotationList al, String name) {
        if (al!=null) {
            for (Tree.Annotation a: al.getAnnotations()) {
                Tree.BaseMemberExpression p = (Tree.BaseMemberExpression) a.getPrimary();
                if (p!=null) {
                    if ( p.getIdentifier().getText().equals(name)) {
                        return true;
                    }
                }
            }
        }
        return false;
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
            model.setShared(true);
        }
        if (hasAnnotation(al, "default")) {
            model.setDefault(true);
            if (that instanceof Tree.ObjectDefinition) {
                that.addError("object declarations may not be default");
            }
        }
        if (hasAnnotation(al, "formal")) {
            model.setFormal(true);
            if (that instanceof Tree.ObjectDefinition) {
                that.addError("object declarations may not be formal");
            }
        }
        if (hasAnnotation(al, "actual")) {
            model.setActual(true);
        }
        if (hasAnnotation(al, "abstract")) {
            if (model instanceof Class) {
                ((Class) model).setAbstract(true);
            }
            else {
                that.addError("declaration is not a class, and may not be abstract");
            }
        }
        if (hasAnnotation(al, "variable")) {
            if (model instanceof Value) {
                ((Value) model).setVariable(true);
            }
            else if (model instanceof ValueParameter) {
                that.addError("parameter may not be variable: " + model.getName());
            }
            else {
                that.addError("declaration is not a value, and may not be variable");
            }
        }
        
        buildAnnotations(al, model.getAnnotations());        
    }

    private static void setVisibleScope(Declaration model) {
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
        
        if ( d.isFormal() && 
                ( !(d.getContainer() instanceof ClassOrInterface) || 
                !( (ClassOrInterface) d.getContainer() ).isAbstract()) ) {
            that.addError("formal member does not belong to an interface or abstract class");
        }
        
        if ( d.isFormal() && 
                !(that instanceof Tree.AttributeDeclaration) && 
                !(that instanceof Tree.MethodDeclaration) &&
                !(that instanceof Tree.ClassDefinition)) {
            that.addError("formal member may not have a body");
        }
        
        if ( !d.isFormal() && 
                d.getContainer() instanceof Interface && 
                !(that instanceof Tree.TypeParameterDeclaration) &&
                !(that instanceof Tree.ClassDeclaration) &&
                !(that instanceof Tree.InterfaceDeclaration)) {
            that.addWarning("concrete members of interfaces not yet supported");
        }
        
    }

    private static void buildAnnotations(Tree.AnnotationList al, List<Annotation> annotations) {
        if (al!=null) {
            for (Tree.Annotation a: al.getAnnotations()) {
                Annotation ann = new Annotation();
                String name = ( (Tree.BaseMemberExpression) a.getPrimary() ).getIdentifier().getText();
                ann.setName(name);
                if (a.getNamedArgumentList()!=null) {
                    for ( Tree.NamedArgument na: a.getNamedArgumentList().getNamedArguments() ) {
                        if (na instanceof Tree.SpecifiedArgument) {
                            Tree.Term t = ((Tree.SpecifiedArgument) na).getSpecifierExpression().getExpression().getTerm();
                            String param = ((Tree.SpecifiedArgument) na).getIdentifier().getText();
                            if (t instanceof Tree.Literal) {
                                ann.addNamedArgument( param, ( (Tree.Literal) t ).getText() );
                            }
                            else if (t instanceof Tree.BaseTypeExpression) {
                                ann.addNamedArgument( param, ( (Tree.BaseTypeExpression) t ).getIdentifier().getText() );
                            }
                        }                    
                    }
                }
                if (a.getPositionalArgumentList()!=null) {
                    for ( Tree.PositionalArgument pa: a.getPositionalArgumentList().getPositionalArguments() ) {
                        Tree.Term t = pa.getExpression().getTerm();
                        if (t instanceof Tree.Literal) {
                            ann.addPositionalArgment( ( (Tree.Literal) t ).getText() );
                        }
                        else if (t instanceof Tree.BaseTypeExpression) {
                            ann.addPositionalArgment( ( (Tree.BaseTypeExpression) t ).getIdentifier().getText() );
                        }
                    }
                }
                annotations.add(ann);
            }
        }
    }
        
    @Override public void visit(Tree.TypedArgument that) {
        Declaration d = beginDeclaration(that.getDeclarationModel());
        super.visit(that);
        endDeclaration(d);
    }

    @Override
    public void visit(Tree.TypeConstraint that) {
        TypeParameter p = (TypeParameter) scope.getMemberOrParameter(unit, that.getIdentifier().getText());
        that.setDeclarationModel(p);
        if (p==null ) {
            that.addError("no matching type parameter for constraint: " + 
                    name(that.getIdentifier()));
            super.visit(that);
        }
        else {
            Scope o = enterScope(p);
            super.visit(that);
            exitScope(o);
        }
        if ( that.getAbstractedType()!=null ) {
            that.addWarning("lower bound type constraints are not yet supported");
        }
        if ( that.getCaseTypes()!=null ) {
            that.addWarning("enumerated type constraints are not yet supported");
        }
        if ( that.getParameterList()!=null ) {
            that.addWarning("initialization parameter specifications are not yet supported");
        }
    }
    
    @Override
    public void visit(Tree.TryCatchStatement that) {
        super.visit(that);
        that.addWarning("try statements are not yet supported");
    }

    @Override
    public void visit(Tree.SwitchStatement that) {
        super.visit(that);
        that.addWarning("switch statements are not yet supported");
    }

    @Override
    public void visit(Tree.SatisfiesCondition that) {
        super.visit(that);
        that.addWarning("satisfies conditions are not yet supported");
    }

    @Override
    public void visit(Tree.CaseTypes that) {
        super.visit(that);
        that.addWarning("of clause not yet supported");
    }

}
