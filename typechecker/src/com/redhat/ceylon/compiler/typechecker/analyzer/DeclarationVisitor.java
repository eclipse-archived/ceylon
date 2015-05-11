package com.redhat.ceylon.compiler.typechecker.analyzer;

import static com.redhat.ceylon.compiler.typechecker.analyzer.TypeVisitor.getTupleType;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.buildAnnotations;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.getPackageTypeDeclaration;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.getTypeDeclaration;
import static com.redhat.ceylon.compiler.typechecker.model.Util.getContainingClassOrInterface;
import static com.redhat.ceylon.compiler.typechecker.model.Util.getTypeArgumentMap;
import static com.redhat.ceylon.compiler.typechecker.model.Util.intersectionOfSupertypes;
import static com.redhat.ceylon.compiler.typechecker.model.Util.isInNativeContainer;
import static com.redhat.ceylon.compiler.typechecker.model.Util.isNativeNoImpl;
import static com.redhat.ceylon.compiler.typechecker.parser.CeylonLexer.SPECIFY;
import static com.redhat.ceylon.compiler.typechecker.tree.Util.formatPath;
import static com.redhat.ceylon.compiler.typechecker.tree.Util.getAnnotation;
import static com.redhat.ceylon.compiler.typechecker.tree.Util.getAnnotationArgument;
import static com.redhat.ceylon.compiler.typechecker.tree.Util.getNativeBackend;
import static com.redhat.ceylon.compiler.typechecker.tree.Util.hasAnnotation;
import static com.redhat.ceylon.compiler.typechecker.tree.Util.name;
import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.common.Backend;
import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.ClassAlias;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.ConditionScope;
import com.redhat.ceylon.compiler.typechecker.model.Constructor;
import com.redhat.ceylon.compiler.typechecker.model.ControlBlock;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Element;
import com.redhat.ceylon.compiler.typechecker.model.Generic;
import com.redhat.ceylon.compiler.typechecker.model.ImportList;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.InterfaceAlias;
import com.redhat.ceylon.compiler.typechecker.model.IntersectionType;
import com.redhat.ceylon.compiler.typechecker.model.LazyProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.NamedArgumentList;
import com.redhat.ceylon.compiler.typechecker.model.Overloadable;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ParameterList;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.Setter;
import com.redhat.ceylon.compiler.typechecker.model.Specification;
import com.redhat.ceylon.compiler.typechecker.model.TypeAlias;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.UnionType;
import com.redhat.ceylon.compiler.typechecker.model.Unit;
import com.redhat.ceylon.compiler.typechecker.model.UnknownType;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.tree.NaturalVisitor;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Annotation;
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
public class DeclarationVisitor extends Visitor implements NaturalVisitor {
    
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
    
    private void visitDeclaration(Tree.Declaration that, 
            Declaration model, boolean checkDupe) {
        visitElement(that, model);
        
        Declaration modelToAdd = model;
        if (setModelName(that, model, that.getIdentifier())) {
            if (checkDupe) {
                modelToAdd = checkForNativeAnnotation(that, model, scope);
                checkForDuplicateDeclaration(that, model, scope);
            }
        }
        //that.setDeclarationModel(model);
        if (modelToAdd != null) {
            unit.addDeclaration(modelToAdd);
            Scope sc = getContainer(that);
            sc.addMember(modelToAdd);
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
        setVisibleScope(model);
    }

    private void visitArgument(Tree.Term that, Declaration model) {
        visitElement(that, model);
        //that.setDeclarationModel(model);
        unit.addDeclaration(model);
        setVisibleScope(model);
    }

    private static boolean setModelName(Node that, Declaration model,
            Tree.Identifier id) {
        if (id==null || id.isMissingToken()) {
            if (model instanceof Constructor) {
                return true;                
            }
            else {
                that.addError("missing declaration or argument name");
                return false;
            }
        }
        else {
            model.setName(id.getText());
            return true;
            //TODO: check for dupe arg name
        }
    }
    
    private static Declaration checkForNativeAnnotation(
            final Tree.Declaration that,
            final Declaration model, Scope scope) {
        Declaration modelToAdd = model;
        
        Unit unit = model.getUnit();
        Annotation a = 
                getAnnotation(that.getAnnotationList(), 
                        "native", unit);
        if (a != null) {
            String backend = getAnnotationArgument(a, "");
            String name = model.getName();
            boolean canBeNativeModel = 
                    model instanceof Method || 
                    model instanceof Class || 
                    model instanceof Value;
            if (canBeNativeModel && model.isToplevel()) {
                if (!backend.isEmpty() && 
                        !Backend.validAnnotation(backend)) {
                    a.addError("illegal native backend name: '\"" + 
                            backend + "\"', must be either '\"jvm\"' or '\"js\"'");
                }
                model.setNative(backend);
                Declaration member = 
                        scope.getDirectMember(name, null, false);
                if (member == null) {
                    initOverloads(model, model);
                }
                else {
                    if (member.isNative()) {
                        List<Declaration> overloads = 
                                getOverloads(member);
                        if (hasOverload(overloads, backend)) {
                            if (backend.isEmpty()) {
                                that.addError("duplicate native header: '" + 
                                        name + "'");
                            }
                            else {
                                that.addError("duplicate native implementation: '" + 
                                        name + "'");
                            }
                        }
                        overloads.add(model);
                        setOverloads(model, overloads);
                    }
                    else {
                        if (backend.isEmpty()) {
                            that.addError("native header for non-native declaration: '" + 
                                    name + "'");
                        }
                        else {
                            that.addError("native implementation for non-native header: '" + 
                                    name + "'");
                        }
                    }
                    modelToAdd = null;
                }
// Re-enable when we support native member/inner declarations
//              Scope container = model.getContainer();
//              if (container instanceof Declaration) {
//                  Declaration ci = (Declaration) container;
//                  if (!ci.isNative()) {
//                      that.addError("native member belongs to a non-native declaration: '" + 
//                              model.getName() + "' of '" + ci.getName());
//                  } else if (!model.getNative().equals(ci.getNative())) {
//                      that.addError("native member and its native declaration are not for the same backend: '" + 
//                              model.getName() + "' of '" + ci.getName());
//                  }
//              }
            }
            else {
                if (isInNativeContainer(model)) {
                    String cbackend = ((Declaration)model.getContainer()).getNative();
                    if (!cbackend.equals(backend)) {
                        that.addError("native backend must be the same as its container: '" + 
                                name + "' of '" + ((Declaration)model.getContainer()).getName() + "'");
                    }
                }
                else if (!backend.isEmpty() && !(model instanceof Setter)) {
                    that.addError("native implementation is not a toplevel value, function, or class: '" + 
                                    name + "'");
                }
            }
        }
        return modelToAdd;
    }
    
    private static List<Declaration> initOverloads(Declaration decl, Declaration initial) {
        ArrayList<Declaration> al = 
                new ArrayList<Declaration>(3);
        al.add(initial);
        setOverloads(decl, al);
        return al;
    }

    private static void setOverloads(Declaration decl, 
            List<Declaration> overloads) {
        if (decl instanceof Method) {
            ((Method)decl).setOverloads(overloads);
        }
        else if (decl instanceof Value) {
            ((Value)decl).setOverloads(overloads);
        }
        else if (decl instanceof Class) {
            ((Class)decl).setOverloads(overloads);
        }
    }

    private static List<Declaration> getOverloads(Declaration decl)  {
        if (decl instanceof Overloadable) {
            return ((Overloadable)decl).getOverloads();
        }
        return null;
    }

    private static boolean hasOverload(List<Declaration> overloads, 
            String backend)  {
        if (overloads!=null) {
            for (Declaration d: overloads) {
                if (backend.equals(d.getNative())) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private static void checkForDuplicateDeclaration(Tree.Declaration that, 
            final Declaration model, Scope scope) {
        String name = model.getName();
        Unit unit = model.getUnit();
        if (name!=null) {
            if (model instanceof Setter) {
                Setter setter = (Setter) model;
                //a setter must have a matching getter
                Declaration member = 
                        getDirectMember(model.getContainer(),
                                name,
                                getNativeBackend(that.getAnnotationList(), unit));
                if (member==null) {
                    that.addError("setter with no matching getter: '" + 
                            name + "'");
                }
                else if (!(member instanceof Value)) {
                    that.addError("setter name does not resolve to matching getter: '" + 
                            name + "'");
                }
                else if (!((Value) member).isTransient() && !isNativeNoImpl(member)) {
                    that.addError("matching value is a reference or is forward-declared: '" + 
                            name + "'");
                }
                else {
                    Value getter = (Value) member;
                    setter.setGetter(getter);
                    if (getter.isVariable()) {
                        that.addError("duplicate setter for getter: '" + 
                                name + "'");
                    }
                    else {
                        getter.setSetter(setter);
                    }
                    setter.setNative(getter.getNative());
                }
            }
            else {
                // this isn't the correct scope for declaration
                // which follow an assertion, since it misses
                // condition scopes, so use the argument scope
//                Scope scope = model.getContainer();
                boolean isControl;
                do {
                    Declaration member = 
                            scope.getDirectMember(name, null, false);
                    if (member!=null) {
                        boolean dup = false;
                        boolean memberCanBeNative = 
                                member instanceof Method || 
                                member instanceof Value || 
                                member instanceof Class;
                        boolean modelCanBeNative = 
                                model instanceof Method || 
                                model instanceof Value || 
                                model instanceof Class;
                        if (member instanceof Method && 
                            model instanceof Method &&
                            scope instanceof ClassOrInterface) {
                            Method abstraction;
                            Method method = (Method) member;
                            if (!method.isAbstraction()) {
                                method.setOverloaded(true);
                                abstraction = new Method();
                                abstraction.setAbstraction(true);
                                abstraction.setType(new UnknownType(unit).getType());
                                abstraction.setName(name);
                                abstraction.setShared(true);
                                abstraction.setActual(true);
                                abstraction.setContainer(scope);
                                abstraction.setScope(scope);
                                abstraction.setUnit(unit);
                                abstraction.setOverloads(new ArrayList<Declaration>());
                                abstraction.getOverloads().add(member);
                                scope.addMember(abstraction);
                            }
                            else {
                                abstraction = (Method) member;
                            }
                            ((Method) model).setOverloaded(true);
                            abstraction.getOverloads().add(model);
                            dup = true;
                        }
                        else if (memberCanBeNative && modelCanBeNative &&
                                model.isNative()) {
                            // Just to make sure no error gets reported
                        }
                        else {
                            dup = true;
                            that.addError("duplicate declaration name: '" + 
                                    name + "'");
                        }
                        if (dup) {
                            unit.getDuplicateDeclarations()
                                .add(member);
                        }
                    }
                    isControl = scope instanceof ControlBlock;
                    scope = scope.getContainer();
                }
                while (isControl);
            }
        }
        else if (model instanceof Constructor) {
            Scope container = model.getContainer();
            if (container instanceof Class) {
                Class c = (Class) container;
                Constructor defaultConstructor = 
                        c.getDefaultConstructor();
                if (defaultConstructor!=null) {
                    that.addError("duplicate default constructor");
                    unit.getDuplicateDeclarations()
                        .add(defaultConstructor);
                }
            }
        }
    }

    // Find the named member and return it. In case a backend has been specified
    // and the found member is native return the proper backend declaration
    private static Declaration getDirectMember(Scope scope, String name, String backend) {
        Declaration member = scope.getDirectMember(name, null, false);
        if (member.isNative() && backend != null) {
            Declaration m = null;
            for (Declaration o : ((Overloadable)member).getOverloads()) {
                if (backend.equals(o.getNative())) {
                    m = o;
                    break;
                }
            }
            member = m;
        }
        return member;
    }
    
    private void visitElement(Node that, Element model) {
        model.setUnit(unit);
        model.setScope(scope);
        model.setContainer(getContainer(that));
    }

    /**
     * Get the containing scope, skipping any condition
     * scopes. 
     * 
     * @see com.redhat.ceylon.compiler.typechecker.model.ConditionScope
     */
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
        Node firstNonImportNode = null;
        int index = -1;
        for (Node d: that.getDeclarations()) {
            firstNonImportNode = d;
            index = d.getToken().getTokenIndex();
            break;
        }
        for (Tree.ModuleDescriptor md: that.getModuleDescriptors()) {
            if (index<0 || md.getToken().getTokenIndex()<index) {
                firstNonImportNode = md;
                index = md.getToken().getTokenIndex();
            }
            break;
        }
        for (Tree.PackageDescriptor pd: that.getPackageDescriptors()) {
            if (index<0 || pd.getToken().getTokenIndex()<index) {
                firstNonImportNode = pd;
                index = pd.getToken().getTokenIndex();
            }
            break;
        }
        if (firstNonImportNode!=null) {
            for (Tree.Import im: that.getImportList().getImports()) {
                if (im.getStopIndex()>firstNonImportNode.getStartIndex()) {
                    im.addError("import statement must occur before any declaration or descriptor");
                }
            }
        }
        boolean first=true;
        for (Tree.ModuleDescriptor md: that.getModuleDescriptors()) {
            if (!first) {
                md.addError("there may be only one module descriptor for a module");
            }
            first=false;
        }
        first=true;
        for (Tree.PackageDescriptor pd: that.getPackageDescriptors()) {
            if (!first) {
                pd.addError("there may be only one package descriptor for a module");
            }
            first=false;
        }
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
        
    private void defaultExtendedToBasic(Class c) {
        //default supertype for classes
        c.setExtendedType(new LazyProducedType(unit) {
            @Override
            public Map<TypeParameter, ProducedType> initTypeArguments() {
                return emptyMap();
            }
            @Override
            public TypeDeclaration initDeclaration() {
                return unit.getBasicDeclaration();
            }
        });
    }

    private void defaultExtendedToObject(Interface c) {
        //default supertype for interfaces
        c.setExtendedType(new LazyProducedType(unit) {
            @Override
            public Map<TypeParameter, ProducedType> initTypeArguments() {
                return emptyMap();
            }
            @Override
            public TypeDeclaration initDeclaration() {
                return unit.getObjectDeclaration();
            }
        });
    }

    private void defaultExtendedToAnything(TypeParameter c) {
        //default supertype for interfaces
        c.setExtendedType(new LazyProducedType(unit) {
            @Override
            public Map<TypeParameter, ProducedType> initTypeArguments() {
                return emptyMap();
            }
            @Override
            public TypeDeclaration initDeclaration() {
                return unit.getAnythingDeclaration();
            }
        });
    }
    
    @Override
    public void visit(Tree.ClassDefinition that) {
        Class c = new Class();
        String pname = unit.getPackage().getQualifiedNameString();
        if (!"ceylon.language".equals(pname) ||
            !"Anything".equalsIgnoreCase(name(that.getIdentifier()))) {
            defaultExtendedToBasic(c);
        }
        that.setDeclarationModel(c);
        super.visit(that);
        if (that.getParameterList()==null) {
            if (c.isClassOrInterfaceMember() && 
                    (c.isFormal() || c.isDefault() || c.isActual())) {
                that.addError("member class declared formal, default, or actual must have a parameter list");
            }
            if (hasAnnotation(that.getAnnotationList(), "sealed", unit)) {
                that.addError("class without parameter list may not be annotated sealed", 1800);
            }
        }
        if (c.isSealed() && c.isFormal() && 
                c.isClassOrInterfaceMember() && 
                !((ClassOrInterface) c.getContainer()).isSealed()) {
            that.addError("sealed formal member class does not belong to a sealed type", 1801);
        }
    }
    
    @Override
    public void visit(Tree.ClassDeclaration that) {
        Class c = new ClassAlias();
        that.setDeclarationModel(c);
        super.visit(that);
        if (that.getParameterList()==null) {
            that.addError("class alias must have a parameter list");
        }
    }
    
    @Override
    public void visit(Tree.AnyClass that) {
        Class c = that.getDeclarationModel();
        visitDeclaration(that, c);
        Scope o = enterScope(c);
        super.visit(that);
        exitScope(o);
        Tree.ParameterList pl = that.getParameterList();
        if (pl==null) {
//            that.addError("missing parameter list in class declaration: '" + 
//                    name(that.getIdentifier()) + "' must have a parameter list", 1000);
        }
        else {
            pl.getModel().setFirst(true);
            c.addParameterList(pl.getModel());
        }
        //TODO: is this still necessary??
        if (c.isClassOrInterfaceMember() && 
                c.getContainer() instanceof TypedDeclaration) {
            that.addUnsupportedError("nested classes of inner classes are not yet supported");
        }
        Tree.Identifier identifier = that.getIdentifier();
        if (c.isAbstract() && c.isFinal()) {
            that.addError("class may not be both abstract and final: '" + 
                    name(identifier) + "'");
        }
        if (c.isFormal() && c.isFinal()) {
            that.addError("class may not be both formal and final: '" + 
                    name(identifier) + "'");
        }
    }
    
    @Override
    public void visit(Tree.Constructor that) {
        Constructor c = new Constructor();
        if (scope instanceof Class) {
            Class clazz = (Class) scope;
            c.setExtendedType(clazz.getType());
            clazz.setConstructors(true);
            if (clazz.isAnonymous()) {
                that.addError("anonymous class may not have constructor");
            }
            
        }
        else {
            that.addError("constructor declaration must occur directly in the body of a class");
        }
        that.setDeclarationModel(c);
        visitDeclaration(that, c);
        Scope o = enterScope(c);
        super.visit(that);
        exitScope(o);
        if (that.getParameterList()==null) {
            that.addError("missing parameter list in constructor declaration: '" + 
                    name(that.getIdentifier()) + "' must have a parameter list", 1000);
        }
        else {
            that.getParameterList().getModel().setFirst(true);
            c.addParameterList(that.getParameterList().getModel());
        }
        if (that.getIdentifier()==null) {
            //default constructor
            if (!c.isShared()) {
                that.addError("default constructor must be annotated shared", 401);
            }
            if (c.isAbstract()) {
                that.addError("default constructor may not be annotated abstract", 1601);
            }
//        if (scope instanceof Class) {
//            Class clazz = (Class) scope;
//            //constructor of sealed class implicitly inherits sealed
//            if (clazz.isSealed()) {
//                c.setSealed(true);
//            }
//        }
        }
        if (c.isAbstract() && c.isShared()) {
            that.addError("abstract constructor may not be annotated shared", 1610);
        }
    }

    @Override
    public void visit(Tree.InterfaceDefinition that) {
        Interface i = new Interface();
        i.setDynamic(that.getDynamic());
        defaultExtendedToObject(i);
        that.setDeclarationModel(i);
        super.visit(that);
    }
    
    @Override
    public void visit(Tree.InterfaceDeclaration that) {
        InterfaceAlias i = new InterfaceAlias();
        that.setDeclarationModel(i);
        super.visit(that);
    }
    
    @Override
    public void visit(Tree.AnyInterface that) {
        Interface i = that.getDeclarationModel();
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
        Tree.TypeSpecifier ts = that.getTypeSpecifier();
        Tree.TypeVariance tv = that.getTypeVariance();
        TypeParameter p = new TypeParameter();
        defaultExtendedToAnything(p);
        p.setDeclaration(declaration);
        p.setDefaulted(ts!=null);
        if (tv!=null) {
            String v = tv.getText();
            p.setCovariant("out".equals(v));
            p.setContravariant("in".equals(v));
        }
        that.setDeclarationModel(p);
        visitDeclaration(that, p);
        super.visit(that);
        if (ts!=null) {
            Tree.StaticType type = ts.getType();
            if (type!=null) {
                p.setDefaultTypeArgument(type.getTypeModel());
            }
        }
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
        Tree.Type type = that.getType();
        m.setDeclaredVoid(type instanceof Tree.VoidModifier);
        if (type instanceof Tree.ValueModifier) {
            type.addError("functions may not be declared using the keyword value");
        }
        if (type instanceof Tree.DynamicModifier) {
            m.setDynamicallyTyped(true);
        }
    }

    private static void setParameterLists(Method m, List<Tree.ParameterList> paramLists, 
            Node that) {
        if (m!=null) {
            for (Tree.ParameterList pl: paramLists) {
                m.addParameterList(pl.getModel());
            }
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
        Tree.Type type = that.getType();
        if (type instanceof Tree.FunctionModifier) {
            type.addError("values may not be declared using the keyword function");
        }
        if (type instanceof Tree.DynamicModifier) {
            that.getDeclarationModel().setDynamicallyTyped(true);
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
        m.setDeclaredVoid(that.getType() instanceof Tree.VoidModifier);
    }
    
    private int fid=0;

    @Override
    public void visit(Tree.FunctionArgument that) {
        Method m = new Method();
        m.setName("anonymous#"+fid++);
        m.setAnonymous(true);
        that.setDeclarationModel(m);
        visitArgument(that, m);
        Scope o = enterScope(m);
        Declaration d = beginDeclaration(that.getDeclarationModel());
        super.visit(that);
        endDeclaration(d);
        exitScope(o);
        setParameterLists(m, that.getParameterLists(), that);
        m.setDeclaredVoid(that.getType() instanceof Tree.VoidModifier);
    }
    
    @Override
    public void visit(Tree.ObjectDefinition that) {
        /*if (that.getClassBody()==null) {
            that.addError("missing object body");
        }*/
        Class c = new Class();
        defaultExtendedToBasic(c);
        c.setAnonymous(true);
        that.setAnonymousClass(c);
        visitDeclaration(that, c, false);
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
        defaultExtendedToBasic(c);
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
    public void visit(Tree.ObjectExpression that) {
        /*if (that.getClassBody()==null) {
            that.addError("missing named argument body");
        }*/
        Class c = new Class();
        c.setName("anonymous#"+fid++);
        c.setNamed(false);
        defaultExtendedToBasic(c);
        c.setAnonymous(true);
        that.setAnonymousClass(c);
        visitArgument(that, c);
        Scope o = enterScope(c);
        super.visit(that);
        exitScope(o);
    }
    
    @Override
    public void visit(Tree.AttributeDeclaration that) {
        Value v = new Value();
        that.setDeclarationModel(v);
        Tree.SpecifierOrInitializerExpression sie = 
                that.getSpecifierOrInitializerExpression();
        v.setTransient(sie instanceof Tree.LazySpecifierExpression);
        visitDeclaration(that, v);
        super.visit(that);
        if (v.isInterfaceMember() && !v.isFormal() && !v.isNative()) {
            if (sie==null) {
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
        if (v.isFormal() && sie!=null) {
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
            else if (v.isShared()) {
                type.addError("shared value must explicitly specify a type", 200);
            }
        }
    }

    @Override
    public void visit(Tree.MethodDeclaration that) {
        super.visit(that);
        Tree.SpecifierExpression sie = 
                that.getSpecifierExpression();
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
            else if (m.isShared()) {
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
        Value g = new Value();
        g.setTransient(true);
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
        Value g = new Value();
        g.setTransient(true);
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
        sc.addMember(v);
        
        s.setParameter(p);
        super.visit(that);
        exitScope(o);
        
        if (that.getSpecifierExpression()==null &&
                that.getBlock()==null &&
                !isNativeNoImpl(s)) {
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

    @Override
    public void visit(Tree.Parameter that) {
        super.visit(that);
        Tree.SpecifierOrInitializerExpression sie = null;
        if (that instanceof Tree.ParameterDeclaration) {
            Tree.TypedDeclaration td = 
                    ((Tree.ParameterDeclaration) that).getTypedDeclaration();
            if (td instanceof Tree.AttributeDeclaration) {
                sie = ((Tree.AttributeDeclaration) td).getSpecifierOrInitializerExpression();
            }
            else if (td instanceof Tree.MethodDeclaration) {
                sie = ((Tree.MethodDeclaration) td).getSpecifierExpression();
            }
        }
        else if (that instanceof Tree.InitializerParameter) {
            sie = ((Tree.InitializerParameter) that).getSpecifierExpression();
        }
        if (sie!=null) {
            new Visitor() {
                public void visit(Tree.AssignmentOp that) {
                    that.addError("assignment may not occur in default argument expression");
                }
                @Override
                public void visit(Tree.PostfixOperatorExpression that) {
                    that.addError("postfix increment or decrement may not occur in default argument expression");
                }
                @Override
                public void visit(Tree.PrefixOperatorExpression that) {
                    that.addError("prefix increment or decrement may not occur in default argument expression");
                }
            }.visit(sie);
        }
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
        Tree.TypedDeclaration td = that.getTypedDeclaration();
        Tree.Type type = td.getType();
        p.setSequenced(type instanceof Tree.SequencedType);
        that.setParameterModel(p);
        super.visit(that);
        Value v = (Value) td.getDeclarationModel();
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
//        if (v.isVariable()) {
//            that.addError("parameter may not be annotated variable");
//        }
    }

    @Override
    public void visit(Tree.FunctionalParameterDeclaration that) {
        Parameter p = new Parameter();
        p.setDeclaration(declaration);
        p.setDefaulted(getSpecifier(that)!=null);
        Tree.TypedDeclaration td = that.getTypedDeclaration();
        Tree.Type type = td.getType();
        p.setDeclaredAnything(type instanceof Tree.VoidModifier);
        that.setParameterModel(p);
        super.visit(that);
        Method m = (Method) td.getDeclarationModel();
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
        cb.setLet(that instanceof Tree.LetClause);
        cb.setId(id++);
        that.setControlBlock(cb);
        visitElement(that, cb);
        Scope o = enterScope(cb);
        super.visit(that);
        exitScope(o);
    }
    
    @Override
    public void visit(Tree.SwitchStatement that) {
        ControlBlock cb = new ControlBlock();
        cb.setId(id++);
        that.setControlBlock(cb);
        visitElement(that, cb);
        Scope o = enterScope(cb);
        super.visit(that);
        exitScope(o);
    }
    
    @Override
    public void visit(Tree.SwitchExpression that) {
        ControlBlock cb = new ControlBlock();
        cb.setId(id++);
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
    public void visit(Tree.ExistsOrNonemptyCondition that) {
        super.visit(that);
        String op = that instanceof Tree.ExistsCondition ? 
                "exists" : "nonempty";
        if (that.getBrokenExpression()!=null) {
            that.getBrokenExpression()
                .addError("incorrect syntax: " + op + 
                        " conditions do not apply to arbitrary expressions, try using postfix " + 
                        op + " operator", 3100);
        }
        else if (that.getVariable()==null) {
            that.addError("missing variable or immutable value reference: " + op + 
                    " condition requires an operand");
        }
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
            Tree.Identifier identifier = na.getIdentifier();
            if (identifier!=null) {
                nal.getArgumentNames().add(identifier.getText());
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
        Tree.SpecifierExpression se = 
                that.getSpecifierExpression();
        if (se!=null) {
            Scope s = scope;
            if (scope instanceof ControlBlock &&
                    !((ControlBlock) scope).isLet()) {
                scope = scope.getContainer();
            }
            se.visit(this);
            scope = s;
        }
        
        Tree.Type type = that.getType();
        Value v = new Value();
        that.setDeclarationModel(v);
        visitDeclaration(that, v, 
                !(type instanceof Tree.SyntheticVariable));
        setVisibleScope(v);
        
        if (type!=null) {
            type.visit(this);
        }
        Tree.Identifier identifier = that.getIdentifier();
        if (identifier!=null) {
            identifier.visit(this);
        }
        
        //TODO: scope should be the variable, not the 
        //      containing control structure:
        Tree.AnnotationList al = that.getAnnotationList();
        if (al!=null) {
            al.visit(this);
        }
        List<Tree.ParameterList> pls = that.getParameterLists();
        for (Tree.ParameterList pl: pls) {
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
        
        if (pls.isEmpty()) {
            if (type instanceof Tree.FunctionModifier) {
                type.addError("variables with no parameters may not be declared using the keyword function");
            }
            if (type instanceof Tree.VoidModifier) {
                type.addError("variables with no parameters may not be declared using the keyword void");
            }
        }
        else {
            Tree.ParameterList pl = pls.get(0);
            pl.addUnsupportedError("variables with parameter lists are not yet supported");
            if (type instanceof Tree.ValueModifier) {
                type.addError("variables with parameters may not be declared using the keyword value");
            }
        }
                
        that.setScope(scope);
        that.setUnit(unit);
    }
    
    private static List<TypeParameter> getTypeParameters(Tree.TypeParameterList tpl) {
        List<TypeParameter> typeParameters = Collections.emptyList();
        if (tpl!=null) {
            boolean foundDefaulted=false;
            List<Tree.TypeParameterDeclaration> tpds = tpl.getTypeParameterDeclarations();
            typeParameters = new ArrayList<TypeParameter>(tpds.size());
            for (Tree.TypeParameterDeclaration tp: tpds) {
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
    
    @Override
    public void visit(Tree.ModuleDescriptor that) {
        if (!unit.getFilename().equals("module.ceylon")) {
            that.addError("module descriptor must occur in a compilation unit named module.ceylon");
        }
        super.visit(that);
    }
    
    @Override
    public void visit(Tree.PackageDescriptor that) {
        if (!unit.getFilename().equals("package.ceylon")) {
            that.addError("package descriptor must occur in a compilation unit named package.ceylon");
        }
        super.visit(that);
    }
    
    @Override public void visit(Tree.Declaration that) {
        if (unit.getFilename().equals("module.ceylon") || 
            unit.getFilename().equals("package.ceylon")) {
            that.addError("declaration may not occur in a module or package descriptor file");
        }
        Declaration model = that.getDeclarationModel();
        Declaration d = beginDeclaration(model);
        super.visit(that);
        endDeclaration(d);
        if (model.isClassOrInterfaceMember() &&
                ((ClassOrInterface) model.getContainer()).isFinal()) {
            if (model.isDefault()) {
                that.addError("member of final class may not be annotated default", 1350);
            }
        }
        if (model.isToplevel()) {
            if (model.getName()!=null && 
                model.getName().endsWith("_")) {
                that.addUnsupportedError("toplevel declaration name ending in _ not currently supported");
            }
            if (pkg.getNameAsString().endsWith("_")) {
                that.addUnsupportedError("toplevel declaration belonging to package with name ending in _ not currently supported");
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
        Tree.Annotation na = getAnnotation(al, "native", unit);
        if (na != null) {
            model.setNative(getAnnotationArgument(na, ""));
        }
        if (model.isFormal() && model.isDefault()) {
            that.addError("declaration may not be annotated both formal and default", 1320);
        }
        if (hasAnnotation(al, "actual", unit)) {
            model.setActual(true);
        }
        if (hasAnnotation(al, "abstract", unit)) {
            if (model instanceof Class) {
                ((Class) model).setAbstract(true);
            }
            else if (model instanceof Constructor) {
                ((Constructor) model).setAbstract(true);
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
        if (hasAnnotation(al, "sealed", unit)) {
            if (model instanceof ClassOrInterface) {
                ((ClassOrInterface) model).setSealed(true);
            }
            else if (model instanceof Constructor) {
                ((Constructor) model).setSealed(true);
            }
            else {
                that.addError("declaration is not a class or interface, and may not be annotated sealed", 1800);
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
                    that.addError("value is not an uninitialized reference, and may not be annotated late", 1900);
                }
            }
            else {
                that.addError("declaration is not a value, and may not be annotated late", 1900);
            }
        }
        if (model instanceof Value) {
            Value value = (Value) model;
            if (value.isVariable() && value.isTransient()) {
                that.addError("getter may not be annotated variable: '" + model.getName() + "'", 1501);
            }
        }
        if (hasAnnotation(al, "deprecated", unit)) {
            model.setDeprecated(true);
        }
        if (hasAnnotation(al, "annotation", unit)) {
            if (!(model instanceof Method) && !(model instanceof Class)) {
                that.addError("declaration is not a function or class, and may not be annotated annotation", 1950);
            }
            else if (!model.isToplevel()) {
                that.addError("declaration is not toplevel, and may not be annotated annotation", 1951);
            }
            else {
                model.setAnnotation(true);
            }
        }
        if (hasAnnotation(al, "serializable", unit)) {
            if (model instanceof Class) {
                ((Class) model).setSerializable(true);
            }
            else {
                that.addError("declaration is not a class, and may not be annotated serializable", 1600);
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
        
        Scope container = d.getContainer();
        if (d.isFormal()) {
            if (container instanceof ClassOrInterface) {
                //handled in RefinementVisitor
//                ClassOrInterface ci = (ClassOrInterface) d.getContainer();
//                if (!ci.isAbstract() && !ci.isFormal()) {
//                    that.addError("formal member belongs to a concrete class", 900);
//                }
            } 
            else {
                that.addError("formal member does not belong to an interface or abstract class", 1100);
            }
            if (!(that instanceof Tree.AttributeDeclaration) && 
                !(that instanceof Tree.MethodDeclaration) &&
                !(that instanceof Tree.AnyClass)) {
                that.addError("formal member may not have a body", 1101);
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
        TypeParameter p = (TypeParameter) 
                scope.getDirectMember(name, null, false);
        that.setDeclarationModel(p);
        if (p==null) {
            that.addError("no matching type parameter for constraint: '" + 
                    name + "'", 2500);
            p = new TypeParameter();
            p.setDeclaration(declaration);
            that.setDeclarationModel(p);
            visitDeclaration(that, p);
        }
        else {
        	if (p.isConstrained()) {
        		that.addError("duplicate constraint list for type parameter: '" +
        				name + "'");
        	}
        	p.setConstrained(true);
        }
        
        Scope o = enterScope(p);
        super.visit(that);
        exitScope(o);
        
        if (that.getAbstractedType()!=null) {
            that.addUnsupportedError("lower bound type constraints are not yet supported");
        }
        Tree.ParameterList pl = that.getParameterList();
        if (pl!=null) {
            that.addUnsupportedError("parameter bounds are not yet supported");
            pl.getModel().setFirst(true);
            p.addParameterList(pl.getModel());
        }
    }
    
    @Override
    public void visit(Tree.ParameterizedExpression that) {
        super.visit(that);
        setParameterLists(null, that.getParameterLists(), that);
        if (!that.getLeftTerm()) {
            that.addError("parameterized expression not the target of a specification statement");
        }
    }
    
    @Override
    public void visit(Tree.SpecifierStatement that) {
        Tree.Term lhs = that.getBaseMemberExpression();
        if (lhs instanceof Tree.ParameterizedExpression) {
            ((Tree.ParameterizedExpression) lhs).setLeftTerm(true);
        }
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
        that.addUnsupportedError("satisfies conditions are not yet supported");
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
    
    /*@Override
    public void visit(Tree.AnnotationList that) {
        Scope s = scope;
        if (declaration instanceof Scope) {
            scope = scope.getContainer();
        }
        super.visit(that);
        scope = s;
    }*/
    
    @Override
    public void visit(Tree.Annotation that) {
        super.visit(that);
        that.getPrimary().setScope(pkg);
    }
    
    @Override
    public void visit(Tree.TypeArgumentList that) {
        super.visit(that);
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
        if (that.getTryClause().getBlock()!=null &&
                that.getCatchClauses().isEmpty() && 
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
                formatPath(path.getIdentifiers()).equals(Module.LANGUAGE_MODULE_NAME)) {
            Tree.ImportMemberOrTypeList imtl = that.getImportMemberOrTypeList();
            if (imtl!=null) {
                for (Tree.ImportMemberOrType imt: imtl.getImportMemberOrTypes()) {
                    if (imt.getAlias()!=null && imt.getIdentifier()!=null) {
                        String name = name(imt.getIdentifier());
                        String alias = name(imt.getAlias().getIdentifier());
                        Map<String, String> mods = unit.getModifiers();
                        if (mods.containsKey(name)) {
//                            String curr = mods.get(alias);
//                            if (curr!=null && curr.equals(alias)) {
//                                mods.remove(alias);
//                            }
                            mods.put(name, alias);
                        }
                    }
                }
            }
        }
    }
    
    private boolean declarationReference=false;
    
    @Override
    public void visit(Tree.MetaLiteral that) {
        declarationReference = that instanceof Tree.ClassLiteral || 
                that instanceof Tree.InterfaceLiteral ||
                that instanceof Tree.NewLiteral ||
                that instanceof Tree.AliasLiteral ||
                that instanceof Tree.TypeParameterLiteral ||
                that instanceof Tree.ValueLiteral ||
                that instanceof Tree.FunctionLiteral;
        super.visit(that);
        declarationReference = false;
    }
    
    @Override
    public void visit(Tree.StaticType that) {
        that.setMetamodel(declarationReference);
        super.visit(that);
    }
    
    private boolean inExtends;
    
    @Override
    public void visit(final Tree.BaseType that) {
        super.visit(that);
        final Scope scope = that.getScope();
        final String name = name(that.getIdentifier());
        if (inExtends) {
            ProducedType t = new LazyProducedType(unit) {
                @Override
                public TypeDeclaration initDeclaration() {
                    return that.getPackageQualified() ?
                            getPackageTypeDeclaration(name, null, false, unit) :
                            getTypeDeclaration(scope, name, null, false, unit);
                }
                @Override
                public Map<TypeParameter, ProducedType> initTypeArguments() {
                    Tree.TypeArgumentList tal = 
                            that.getTypeArgumentList();
                    List<TypeParameter> tps = 
                            getDeclaration().getTypeParameters();
                    return getTypeArgumentMap(getDeclaration(), null, 
                            Util.getTypeArguments(tal, tps, null));
                }
            };
            that.setTypeModel(t);
        }
    }
    
    @Override
    public void visit(final Tree.QualifiedType that) {
        super.visit(that);
        final String name = name(that.getIdentifier());
        final ProducedType outerType = 
                that.getOuterType().getTypeModel();
        if (inExtends) {
            ProducedType t = new LazyProducedType(unit) {
                @Override
                public TypeDeclaration initDeclaration() {
                    if (outerType==null) {
                        return null;
                    }
                    else {
                        return Util.getTypeMember(outerType.getDeclaration(), 
                                name, null, false, unit);
                    }
                }
                @Override
                public Map<TypeParameter, ProducedType> initTypeArguments() {
                    Tree.TypeArgumentList tal = 
                            that.getTypeArgumentList();
                    if (outerType==null) {
                        return emptyMap();
                    }
                    else {
                        List<TypeParameter> tps = 
                                getDeclaration().getTypeParameters();
                        return getTypeArgumentMap(getDeclaration(), outerType, 
                                Util.getTypeArguments(tal, tps, outerType));
                    }
                }
            };
            that.setTypeModel(t);
        }
    }
    
    public void visit(final Tree.IterableType that) {
        super.visit(that);
        if (inExtends) {
            final ProducedType elementType;
            final boolean atLeastOne;
            Tree.Type elem = that.getElementType();
            if (elem==null) {
            	elementType = unit.getNothingDeclaration().getType();
            	atLeastOne = false;
            }
            else if (elem instanceof Tree.SequencedType) {
            	Tree.SequencedType set = (Tree.SequencedType) elem;
            	elementType = set.getType().getTypeModel();
            	atLeastOne = set.getAtLeastOne();
            }
            else {
            	elementType = null;
            	atLeastOne = false;
            }
            ProducedType t = new LazyProducedType(unit) {
                ProducedType iterableType() {
                    if (elementType!=null) {
                        return atLeastOne ? 
                                unit.getNonemptyIterableType(elementType) : 
                                unit.getIterableType(elementType);
                    }
                    else {
                        return unit.getIterableType(new UnknownType(unit).getType());
                    }
                }
                @Override
                public TypeDeclaration initDeclaration() {
                    return iterableType().getDeclaration();
                }
                @Override
                public Map<TypeParameter, ProducedType> initTypeArguments() {
                    return iterableType().getTypeArguments();
                }
            };
            that.setTypeModel(t);
        }
    }
    
    public void visit(final Tree.TupleType that) {
        super.visit(that);
        if (inExtends) {
            ProducedType t = new LazyProducedType(unit) {
                private ProducedType tupleType(final Tree.TupleType that) {
                    //TODO: this holds a hard reference to the Tree.Type
                    return getTupleType(that.getElementTypes(), unit);
                }
                @Override
                public TypeDeclaration initDeclaration() {
                    return tupleType(that).getDeclaration();
                }
                @Override
                public Map<TypeParameter, ProducedType> initTypeArguments() {
                    return tupleType(that).getTypeArguments();
                }
            };
            that.setTypeModel(t);
        }
    }
    
    public void visit(final Tree.OptionalType that) {
        super.visit(that);
        final ProducedType definiteType = 
                that.getDefiniteType().getTypeModel();
        if (inExtends) {
            ProducedType t = new LazyProducedType(unit) {
                @Override
                public TypeDeclaration initDeclaration() {
                    List<ProducedType> types = 
                            new ArrayList<ProducedType>(2);
                    types.add(unit.getType(unit.getNullDeclaration()));
                    if (definiteType!=null) types.add(definiteType);
                    UnionType ut = new UnionType(unit);
                    ut.setCaseTypes(types);
                    return ut;
                }
                @Override
                public Map<TypeParameter, ProducedType> initTypeArguments() {
                    return emptyMap();
                }
            };
            that.setTypeModel(t);
        }
    }
    
    public void visit(final Tree.UnionType that) {
        super.visit(that);
        List<Tree.StaticType> sts = that.getStaticTypes();
        final List<ProducedType> types = 
                new ArrayList<ProducedType>(sts.size());
        for (Tree.StaticType st: sts) {
            ProducedType t = st.getTypeModel();
            if (t!=null) {
                types.add(t);
            }
        }
        if (inExtends) {
            ProducedType t = new LazyProducedType(unit) {
                @Override
                public TypeDeclaration initDeclaration() {
                    UnionType ut = new UnionType(unit);
                    ut.setCaseTypes(types);
                    return ut;
                }
                @Override
                public Map<TypeParameter, ProducedType> initTypeArguments() {
                    return emptyMap();
                }
            };
            that.setTypeModel(t);
        }
    }
    
    public void visit(final Tree.IntersectionType that) {
        super.visit(that);
        if (inExtends) {
            List<Tree.StaticType> sts = that.getStaticTypes();
            final List<ProducedType> types = 
                    new ArrayList<ProducedType>(sts.size());
            for (Tree.StaticType st: sts) {
                ProducedType t = st.getTypeModel();
                if (t!=null) types.add(t);
            }
            ProducedType t = new LazyProducedType(unit) {
                @Override
                public TypeDeclaration initDeclaration() {
                    IntersectionType it = new IntersectionType(unit);
                    it.setSatisfiedTypes(types);
                    return it;
                }
                @Override
                public Map<TypeParameter, ProducedType> initTypeArguments() {
                    return emptyMap();
                }
            };
            that.setTypeModel(t);
        }
    }
    
    public void visit(final Tree.SequenceType that) {
        super.visit(that);
        if (inExtends) {
            final ProducedType elementType = 
                    that.getElementType().getTypeModel();
            final Tree.NaturalLiteral length = that.getLength();
            ProducedType t;
            if (length==null) {
                t = new LazyProducedType(unit) {
                    @Override
                    public TypeDeclaration initDeclaration() {
                        return unit.getSequentialDeclaration();
                    }
                    @Override
                    public Map<TypeParameter, ProducedType> initTypeArguments() {
                        List<TypeParameter> stps = 
                                unit.getSequentialDeclaration().getTypeParameters();
                        return singletonMap(stps.get(0), elementType);
                    }
                };
            }
            else {
                final int len;
                try {
                    len = Integer.parseInt(length.getText());
                }
                catch (NumberFormatException nfe) {
                    return;
                }
                if (len<1 || len>100) {
                    return;
                }
                else {
                    t = new StaticLengthSequenceType(elementType, len);
                }
            }
            that.setTypeModel(t);
        }
    }
    
    private final class StaticLengthSequenceType extends LazyProducedType {
        private final ProducedType elementType;
        private final int len;

        private StaticLengthSequenceType(ProducedType elementType, int len) {
            super(unit);
            this.elementType = elementType;
            this.len = len;
        }

        @Override
        public TypeDeclaration initDeclaration() {
            return unit.getTupleDeclaration();
        }

        @Override
        public Map<TypeParameter,ProducedType> initTypeArguments() {
            List<TypeParameter> stps = 
                    unit.getTupleDeclaration().getTypeParameters();
            Map<TypeParameter,ProducedType> args = 
                    new HashMap<TypeParameter,ProducedType>(3);
            args.put(stps.get(0), elementType);
            args.put(stps.get(1), elementType);
            args.put(stps.get(3), 
                    len==1 ? unit.getEmptyDeclaration().getType() : 
                             new StaticLengthSequenceType(elementType, len-1));
            return args;
        }
    }

    public void visit(final Tree.SequencedType that) {
        super.visit(that);
        if (inExtends) {
            final ProducedType type = 
                    that.getType().getTypeModel();
            ProducedType t = new LazyProducedType(unit) {
                @Override
                public TypeDeclaration initDeclaration() {
                    return that.getAtLeastOne() ? 
                            unit.getSequenceDeclaration() : 
                            unit.getSequentialDeclaration();
                }
                @Override
                public Map<TypeParameter, ProducedType> initTypeArguments() {
                    List<TypeParameter> stps = 
                            (that.getAtLeastOne() ? 
                                    unit.getSequenceDeclaration() : 
                                    unit.getSequentialDeclaration())
                            .getTypeParameters();
                    return singletonMap(stps.get(0), type);
                }
            };
            that.setTypeModel(t);
        }
    }
    
    public void visit(final Tree.EntryType that) {
        super.visit(that);
        if (inExtends) {
            final ProducedType keyType = 
                    that.getKeyType().getTypeModel();
            final ProducedType valueType = 
                    that.getValueType().getTypeModel();
            ProducedType t = new LazyProducedType(unit) {
                @Override
                public TypeDeclaration initDeclaration() {
                    return unit.getEntryDeclaration();
                }
                @Override
                public Map<TypeParameter, ProducedType> initTypeArguments() {
                    HashMap<TypeParameter, ProducedType> map = 
                            new HashMap<TypeParameter, ProducedType>();
                    List<TypeParameter> itps = 
                            unit.getEntryDeclaration().getTypeParameters();
                    map.put(itps.get(0), keyType);
                    map.put(itps.get(1), valueType);
                    return map;
                }
            };
            that.setTypeModel(t);
        }
    }
    
    public void visit(final Tree.FunctionType that) {
        super.visit(that);
        if (inExtends) {
            Tree.StaticType rt = that.getReturnType();
            if (rt!=null) {
                final ProducedType returnType = 
                        rt.getTypeModel();
                ProducedType t = new LazyProducedType(unit) {
                    @Override
                    public TypeDeclaration initDeclaration() {
                        return unit.getCallableDeclaration();
                    }
                    @Override
                    public Map<TypeParameter, ProducedType> initTypeArguments() {
                        HashMap<TypeParameter, ProducedType> map = 
                                new HashMap<TypeParameter, ProducedType>();
                        List<TypeParameter> ctps = 
                                unit.getCallableDeclaration().getTypeParameters();
                        map.put(ctps.get(0), returnType);
                        map.put(ctps.get(1),
                                //TODO: holds on to reference to Tree.Type
                                getTupleType(that.getArgumentTypes(), unit));
                        return map;
                    }
                };
                that.setTypeModel(t);
            }
        }
    }
    
    public void visit(final Tree.SuperType that) {
        super.visit(that);
        if (inExtends) {
            final Scope scope = that.getScope();
            ProducedType t = new LazyProducedType(unit) {
                @Override
                public TypeDeclaration initDeclaration() {
                    ClassOrInterface ci = 
                            getContainingClassOrInterface(scope);
                    if (ci==null) {
                        return null;
                    }
                    else {
                        if (ci.isClassOrInterfaceMember()) {
                            ClassOrInterface oci = 
                                    (ClassOrInterface) ci.getContainer();
                            return intersectionOfSupertypes(oci).getDeclaration();
                        }
                        else {
                            return null;
                        }
                    }
                }
                @Override
                public Map<TypeParameter, ProducedType> initTypeArguments() {
                    return emptyMap();
                }
            };
            that.setTypeModel(t);
        }
    }

    public void visit(Tree.GroupedType that) {
        super.visit(that);
        Tree.StaticType type = that.getType();
        if (type!=null) {
        	that.setTypeModel(type.getTypeModel());
        }
    }
    
    @Override
    public void visit(Tree.ExtendedType that) {
        inExtends = true;
        super.visit(that);
        inExtends = false;
        TypeDeclaration td = 
                (TypeDeclaration) that.getScope();
        if (!td.isAlias()) {
            Tree.SimpleType t = that.getType();
            if (t==null) {
                that.addError("missing extended type");
            }
            else {
                ProducedType type = t.getTypeModel();
                if (type!=null) {
                    td.setExtendedType(type);
                }
            }
        }
    }
    
    @Override
    public void visit(Tree.SatisfiedTypes that) {
        inExtends = true;
        super.visit(that);
        inExtends = false;
        TypeDeclaration td = 
                (TypeDeclaration) that.getScope();
        if (td.isAlias()) {
            return;
        }
        for (Tree.StaticType t: that.getTypes()) {
            if (t!=null) {
                ProducedType type = t.getTypeModel();
                if (type!=null) {
                    td.getSatisfiedTypes().add(type);
                }
            }
        }
    }
    
    @Override
    public void visit(Tree.ClassSpecifier that) {
        inExtends = true;
        super.visit(that);
        inExtends = false;
        Tree.SimpleType t = that.getType();
        if (t==null) {
            that.addError("missing aliased type");
        }
        else if (that.getInvocationExpression()==null) {
            that.addError("missing instantiation arguments");
        }
        else {
            TypeDeclaration td = 
                    (TypeDeclaration) that.getScope();
            ProducedType type = t.getTypeModel();
            if (type!=null) {
                td.setExtendedType(type);
            }
            if (that.getMainToken().getType()==SPECIFY) {
                that.addError("incorrect syntax: aliased class must be specified using =>", 1050);
            }
        }
    }
    
    @Override
    public void visit(Tree.TypeSpecifier that) {
        inExtends = true;
        super.visit(that);
        inExtends = false;
        Tree.StaticType t = that.getType();
        if (t==null) {
            that.addError("missing aliased type");
        }
        else if (!(that instanceof Tree.DefaultTypeArgument)) {
            TypeDeclaration td = 
                    (TypeDeclaration) that.getScope();
            ProducedType type = t.getTypeModel();
            if (type!=null) {
                td.setExtendedType(type);
            }
            if (that.getMainToken().getType()==SPECIFY) {
                that.addError("incorrect syntax: aliased type must be specified using =>", 1050);
            }
        }
    }
    
    @Override
    public void visit(Tree.LazySpecifierExpression that) {
        super.visit(that);
        if (that.getMainToken().getType()==SPECIFY) {
            that.addError("incorrect syntax: expression must be specified using =>", 1050);
        }
    }
    
}
