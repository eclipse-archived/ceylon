package com.redhat.ceylon.compiler.loader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.compiler.js.JsCompiler;
import com.redhat.ceylon.compiler.typechecker.analyzer.ModuleManager;
import com.redhat.ceylon.compiler.typechecker.model.Annotation;
import com.redhat.ceylon.compiler.typechecker.model.NothingType;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.FunctionalParameter;
import com.redhat.ceylon.compiler.typechecker.model.Getter;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.InterfaceAlias;
import com.redhat.ceylon.compiler.typechecker.model.IntersectionType;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.MethodOrValue;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ParameterList;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.TypeAlias;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.UnionType;
import com.redhat.ceylon.compiler.typechecker.model.Unit;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.model.ValueParameter;

public class JsonPackage extends com.redhat.ceylon.compiler.typechecker.model.Package {

    //Ugly hack to have a ref to Basic at hand, to use as implicit supertype of classes
    private final static Map<String,Object> idobj = new HashMap<String, Object>();
    //This is to use as the implicit supertype of interfaces
    private final static Map<String,Object> objclass = new HashMap<String, Object>();
    //This is for type parameters
    private final static Map<String,Object> voidclass = new HashMap<String, Object>();
    private Map<String,Object> model;
    private final Unit unit = new Unit();
    private final String pkgname;
    private boolean loaded = false;
    private NothingType nothing;

    static {
        idobj.put(MetamodelGenerator.KEY_NAME, "Basic");
        idobj.put(MetamodelGenerator.KEY_PACKAGE, "ceylon.language");
        idobj.put(MetamodelGenerator.KEY_MODULE, "ceylon.language");
        objclass.put(MetamodelGenerator.KEY_NAME, "Object");
        objclass.put(MetamodelGenerator.KEY_PACKAGE, "ceylon.language");
        objclass.put(MetamodelGenerator.KEY_MODULE, "ceylon.language");
        voidclass.put(MetamodelGenerator.KEY_NAME, "Anything");
        voidclass.put(MetamodelGenerator.KEY_PACKAGE, "ceylon.language");
        voidclass.put(MetamodelGenerator.KEY_MODULE, "ceylon.language");
    }
    public JsonPackage(String pkgname) {
        this.pkgname = pkgname;
        setName(ModuleManager.splitModuleName(pkgname));
        unit.setPackage(this);
        unit.setFilename("package.ceylon");
        addUnit(unit);
    }
    void setModel(Map<String, Object> metamodel) {
        model = metamodel;
    }

    void loadDeclarations() {
        if (loaded) return;
        loaded = true;
        if (getModule().getLanguageModule() == getModule() && "ceylon.language".equals(pkgname)) {
            //Mark the language module as immediately available to bypass certain validations
            getModule().setAvailable(true);
            //Ugly ass hack - add Nothing to the model
            nothing = new NothingType(unit);
            nothing.setContainer(this);
            nothing.setUnit(unit);
        }
        setShared(model.get("$pkg-shared") != null);
        for (Map.Entry<String,Object> e : model.entrySet()) {
            String k = e.getKey();
            if (!k.startsWith("$pkg-")) {
                @SuppressWarnings("unchecked")
                Map<String,Object> m = (Map<String,Object>)e.getValue();
                if (m.get(MetamodelGenerator.KEY_METATYPE) instanceof String) {
                    String metatype = (String)m.get(MetamodelGenerator.KEY_METATYPE);
                    if (MetamodelGenerator.METATYPE_CLASS.equals(metatype)) {
                        refineMembers(loadClass(e.getKey(), m, this, null));
                    } else if (MetamodelGenerator.METATYPE_INTERFACE.equals(metatype)) {
                        refineMembers(loadInterface(e.getKey(), m, this, null));
                    } else if (metatype.equals(MetamodelGenerator.METATYPE_ATTRIBUTE)
                            || metatype.equals(MetamodelGenerator.METATYPE_GETTER)) {
                        loadAttribute(k, m, this, null);
                    } else if (metatype.equals(MetamodelGenerator.METATYPE_METHOD)) {
                        loadMethod(k, m, this, null);
                    } else if (metatype.equals(MetamodelGenerator.METATYPE_OBJECT)) {
                        refineMembers((com.redhat.ceylon.compiler.typechecker.model.Class)loadObject(k, m, this, null));
                    } else if (metatype.equals(MetamodelGenerator.METATYPE_ALIAS)) {
                        loadTypeAlias(k, m, this, null);
                    }
                } else if (m.get(MetamodelGenerator.KEY_METATYPE) == null) {
                    throw new IllegalArgumentException("Missing metatype from entry " + m);
                } else if (m.get(MetamodelGenerator.KEY_METATYPE) instanceof ClassOrInterface) {
                    refineMembers((ClassOrInterface)m.get(MetamodelGenerator.KEY_METATYPE));
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private com.redhat.ceylon.compiler.typechecker.model.Class loadClass(String name, Map<String, Object> m,
            Scope parent, final List<TypeParameter> existing) {
        com.redhat.ceylon.compiler.typechecker.model.Class cls;
        if (m.get(MetamodelGenerator.KEY_METATYPE) instanceof com.redhat.ceylon.compiler.typechecker.model.Class) {
            return (com.redhat.ceylon.compiler.typechecker.model.Class)m.get(MetamodelGenerator.KEY_METATYPE);
        } else {
            //Check if it's already been added first
            Declaration maybe = parent.getDirectMember(name, null, false);
            if (maybe == null) {
                //It's not there, so create it
                if (m.containsKey("$alias")) {
                    cls = new com.redhat.ceylon.compiler.typechecker.model.ClassAlias();
                } else {
                    cls = new com.redhat.ceylon.compiler.typechecker.model.Class();
                }
                cls.setAbstract(m.containsKey("abstract"));
                cls.setAnonymous(m.containsKey("$anon"));
                cls.setContainer(parent);
                cls.setName(name);
                cls.setUnit(unit);
                setAnnotations(cls, (Map<String,List<String>>)m.get(MetamodelGenerator.KEY_ANNOTATIONS));
                if (parent == this) {
                    unit.addDeclaration(cls);
                } else {
                    parent.getMembers().add(cls);
                }
            } else if (maybe instanceof com.redhat.ceylon.compiler.typechecker.model.Class) {
                return (com.redhat.ceylon.compiler.typechecker.model.Class)maybe;
            } else {
                throw new IllegalStateException(maybe  + " should be a Class");
            }
        }
        //Type parameters are about the first thing we need to load
        final List<TypeParameter> tparms = parseTypeParameters(
                (List<Map<String,Object>>)m.get(MetamodelGenerator.KEY_TYPE_PARAMS), cls, existing);
        final List<TypeParameter> allparms = JsonPackage.merge(tparms, existing);
        if (m.containsKey(MetamodelGenerator.KEY_SELF_TYPE)) {
            for (TypeParameter t : tparms) {
                if (t.getName().equals(m.get(MetamodelGenerator.KEY_SELF_TYPE))) {
                    cls.setSelfType(t.getType());
                }
            }
        }
        //This is to avoid circularity
        if (!(getModule().getLanguageModule()==getModule() && ("Nothing".equals(name) || "Anything".equals(name)))) {
            if (m.containsKey("super")) {
                cls.setExtendedType(getTypeFromJson((Map<String,Object>)m.get("super"),
                        parent instanceof Declaration ? (Declaration)parent : null, allparms));
            } else {
                cls.setExtendedType(getTypeFromJson(idobj,
                        parent instanceof Declaration ? (Declaration)parent : null, allparms));
            }
        }

        ParameterList plist = parseParameters((List<Map<String,Object>>)m.get(MetamodelGenerator.KEY_PARAMS),
                cls, allparms);
        plist.setNamedParametersSupported(true);
        cls.setParameterList(plist);
        if (m.containsKey("of")) {
            cls.setCaseTypes(parseTypeList((List<Map<String,Object>>)m.get("of"), allparms));
        }
        if (m.containsKey("satisfies")) {
            cls.setSatisfiedTypes(parseTypeList((List<Map<String,Object>>)m.get("satisfies"), allparms));
        }
        if (m.containsKey(MetamodelGenerator.KEY_INTERFACES)) {
            for (Map.Entry<String,Map<String,Object>> inner : ((Map<String,Map<String,Object>>)m.get(MetamodelGenerator.KEY_INTERFACES)).entrySet()) {
                loadInterface(inner.getKey(), inner.getValue(), cls, allparms);
            }
        }
        if (m.containsKey(MetamodelGenerator.KEY_CLASSES)) {
            for (Map.Entry<String,Map<String,Object>> inner : ((Map<String,Map<String,Object>>)m.get(MetamodelGenerator.KEY_CLASSES)).entrySet()) {
                loadClass(inner.getKey(), inner.getValue(), cls, allparms);
            }
        }
        if (m.containsKey(MetamodelGenerator.KEY_OBJECTS)) {
            for (Map.Entry<String,Map<String,Object>> inner : ((Map<String,Map<String,Object>>)m.get(MetamodelGenerator.KEY_OBJECTS)).entrySet()) {
                loadObject(inner.getKey(), inner.getValue(), cls, allparms);
            }
        }
        addAttributesAndMethods(m, cls, allparms);
        m.put(MetamodelGenerator.KEY_METATYPE, cls);
        return cls;
    }

    /** Parses the specified attributes and methods from JSON data and adds them to the specified declaration. */
    @SuppressWarnings("unchecked")
    private void addAttributesAndMethods(Map<String,Object> m, Declaration d, List<TypeParameter> tparms) {
        //Attributes
        Map<String, Map<String,Object>> sub = (Map<String,Map<String,Object>>)m.get(MetamodelGenerator.KEY_ATTRIBUTES);
        if (sub != null) {
            for(Map.Entry<String, Map<String,Object>> e : sub.entrySet()) {
                d.getMembers().add(loadAttribute(e.getKey(), e.getValue(), (Scope)d, tparms));
            }
        }
        //Methods
        sub = (Map<String,Map<String,Object>>)m.get(MetamodelGenerator.KEY_METHODS);
        if (sub != null) {
            for(Map.Entry<String, Map<String,Object>> e : sub.entrySet()) {
                d.getMembers().add(loadMethod(e.getKey(), e.getValue(), (Scope)d, tparms));
            }
        }
    }

    /** Creates a list of ProducedType from the references in the maps.
     * @param types A list of maps where each map is a reference to a type or type parameter.
     * @param typeParams The type parameters that can be referenced from the list of maps. */
    private List<ProducedType> parseTypeList(List<Map<String,Object>> types, List<TypeParameter> typeParams) {
        List<ProducedType> ts = new ArrayList<ProducedType>(types.size());
        for (Map<String,Object> st : types) {
            ts.add(getTypeFromJson(st, null, typeParams));
        }
        return ts;
    }

    /** Creates a list of TypeParameter from a list of maps.
     * @param typeParams The list of maps to create the TypeParameters.
     * @param container The declaration which owns the resulting type parameters.
     * @param existing A list of type parameters declared in the parent scopes which can be referenced from
     * the ones that have to be parsed. */
    private List<TypeParameter> parseTypeParameters(List<Map<String,Object>> typeParams, Declaration container,
            List<TypeParameter> existing) {
        if (typeParams == null) return null;
        //New array with existing parms to avoid modifying that one
        List<TypeParameter> allparms = new ArrayList<TypeParameter>((existing == null ? 0 : existing.size()) + typeParams.size());
        if (existing != null && !existing.isEmpty()) {
            allparms.addAll(existing);
        }
        List<TypeParameter> tparms = new ArrayList<TypeParameter>(typeParams.size());
        //To avoid circularity, this is done in two phases:
        //First create the type parameters
        for (Map<String,Object> tp : typeParams) {
            if (tp.get(MetamodelGenerator.KEY_METATYPE) instanceof TypeParameter) {
                System.out.println("WTF!!!!! this should never NEVER NEVER HAPPEN!!! " + container +"->" + tp.get(MetamodelGenerator.KEY_METATYPE));
                continue;
            }
            Declaration maybe = container.getDirectMemberOrParameter((String)tp.get(MetamodelGenerator.KEY_NAME), null, false);
            if (maybe instanceof TypeParameter) {
                //we already had it (from partial loading elsewhere)
                allparms.add((TypeParameter)maybe);
                tparms.add((TypeParameter)maybe);
                tp.put(MetamodelGenerator.KEY_METATYPE, maybe);
            } else {
                TypeParameter tparm = new TypeParameter();
                tparm.setUnit(unit);
                tparm.setDeclaration(container);
                container.getMembers().add(tparm);
                if (tp.containsKey(MetamodelGenerator.KEY_NAME)) {
                    tparm.setName((String)tp.get(MetamodelGenerator.KEY_NAME));
                } else if (!tp.containsKey(MetamodelGenerator.KEY_TYPES)) {
                    throw new IllegalArgumentException("Invalid type parameter map " + tp);
                }
                String variance = (String)tp.get("variance");
                if ("out".equals(variance)) {
                    tparm.setCovariant(true);
                } else if ("in".equals(variance)) {
                    tparm.setContravariant(true);
                }
                if (container instanceof Scope) {
                    tparm.setContainer((Scope)container);
                }
                tparms.add(tparm);
                allparms.add(tparm);
                tp.put(MetamodelGenerator.KEY_METATYPE, tparm);
                if (tp.containsKey(MetamodelGenerator.KEY_DEFAULT)) {
                    tparm.setDefaultTypeArgument(getTypeFromJson(
                            (Map<String,Object>)tp.get(MetamodelGenerator.KEY_DEFAULT), container, existing));
                    tparm.setDefaulted(true);
                }
            }
        }
        if (container instanceof TypeDeclaration) {
            ((TypeDeclaration) container).setTypeParameters(tparms);
        } else if (container instanceof Method) {
            ((Method) container).setTypeParameters(tparms);
        }
        //Second, add heritage
        for (Map<String,Object> tp : typeParams) {
            TypeParameter tparm = (TypeParameter)tp.get(MetamodelGenerator.KEY_METATYPE);
            if (tparm == null) {
                continue;
            } else if (tparm.getExtendedType() == null) {
                if (tp.containsKey(MetamodelGenerator.KEY_PACKAGE)) {
                    //Looks like this never happens but...
                    ProducedType subtype = getTypeFromJson(tp, container, allparms);
                    tparm.setExtendedType(subtype);
                } else if (tp.containsKey(MetamodelGenerator.KEY_TYPES)) {
                    if (!("u".equals(tp.get("comp")) || "i".equals(tp.get("comp")))) {
                        throw new IllegalArgumentException("Only union or intersection types are allowed as 'comp'");
                    }
                    ProducedType subtype = getTypeFromJson(tp, container, allparms);
                    tparm.setName(subtype.getProducedTypeName());
                    tparm.setExtendedType(subtype);
                } else {
                    tparm.setExtendedType(getTypeFromJson(voidclass, container, null));
                }
            }
            if (tp.containsKey("satisfies")) {
                tparm.setSatisfiedTypes(parseTypeList((List<Map<String,Object>>)tp.get("satisfies"), allparms));
            } else if (tp.containsKey("of")) {
                tparm.setCaseTypes(parseTypeList((List<Map<String,Object>>)tp.get("of"), allparms));
            }
        }
        return tparms;
    }

    /** Creates a parameter list from a list of maps where each map represents a parameter.
     * @param The list of maps to create the parameters.
     * @param owner The declaration to assign to each parameter.
     * @param typeParameters The type parameters which can be referenced from the parameters. */
    private ParameterList parseParameters(List<Map<String,Object>> params, Declaration owner, List<TypeParameter> typeParameters) {
        ParameterList plist = new ParameterList();
        if (params != null) {
            for (Map<String,Object> p : params) {
                Parameter param = null;
                String paramtype = (String)p.get("$pt");
                if ("v".equals(paramtype)) {
                    param = new ValueParameter();
                    ((ValueParameter)param).setHidden(p.containsKey("$hdn"));
                } else if ("f".equals(paramtype)) {
                    param = new FunctionalParameter();
                    @SuppressWarnings("unchecked")
                    List<List<Map<String,Object>>> paramLists = (List<List<Map<String,Object>>>)p.get(MetamodelGenerator.KEY_PARAMS);
                    if (paramLists == null) {
                        ((FunctionalParameter)param).addParameterList(new ParameterList());
                    } else {
                        boolean first = true;
                        for (List<Map<String,Object>> subplist : paramLists) {
                            ParameterList _params = parseParameters(subplist, param, typeParameters);
                            if (first) {
                                first = false;
                            } else {
                                _params.setNamedParametersSupported(false);
                            }
                            ((FunctionalParameter)param).addParameterList(_params);
                        }
                    }
                } else {
                    throw new IllegalArgumentException("Unknown parameter type " + paramtype);
                }
                param.setName((String)p.get(MetamodelGenerator.KEY_NAME));
                param.setUnit(unit);
                param.setDeclaration(owner);
                param.setDefaulted(p.containsKey(MetamodelGenerator.KEY_DEFAULT));
                param.setSequenced(p.containsKey("seq"));
                if (owner instanceof Scope) {
                    param.setContainer((Scope)owner);
                }
                owner.getMembers().add(param);
                if (p.get(MetamodelGenerator.KEY_TYPE) instanceof Map) {
                    param.setType(getTypeFromJson((Map<String,Object>)p.get(MetamodelGenerator.KEY_TYPE), owner, typeParameters));
                } else {
                    //parameter type
                    for (TypeParameter tp : typeParameters) {
                        if (tp.getName().equals(p.get(MetamodelGenerator.KEY_TYPE))) {
                            param.setType(tp.getType());
                        }
                    }
                }
                plist.getParameters().add(param);
            }
        }
        return plist;
    }

    @SuppressWarnings("unchecked")
    private Method loadMethod(String name, Map<String, Object> m, Scope parent, final List<TypeParameter> existing) {
        Method md = new Method();
        md.setName(name);
        md.setContainer(parent);
        setAnnotations(md, (Map<String,List<String>>)m.get(MetamodelGenerator.KEY_ANNOTATIONS));
        md.setUnit(unit);
        if (parent == this) {
            //Top-level declarations are directly added to the unit
            unit.addDeclaration(md);
        }
        final List<TypeParameter> tparms = parseTypeParameters(
                (List<Map<String,Object>>)m.get(MetamodelGenerator.KEY_TYPE_PARAMS), md, existing);
        final List<TypeParameter> allparms = JsonPackage.merge(tparms, existing);
        md.setType(getTypeFromJson((Map<String,Object>)m.get(MetamodelGenerator.KEY_TYPE),
                parent instanceof Declaration ? (Declaration)parent : null, allparms));
        List<List<Map<String,Object>>> paramLists = (List<List<Map<String,Object>>>)m.get(MetamodelGenerator.KEY_PARAMS);
        if (paramLists == null) {
            md.addParameterList(new ParameterList());
        } else {
            boolean first = true;
            for (List<Map<String,Object>> plist : paramLists) {
                ParameterList _params = parseParameters(plist, md, allparms);
                if (first) {
                    first = false;
                } else {
                    _params.setNamedParametersSupported(false);
                }
                md.addParameterList(_params);
            }
        }
        return md;
    }

    private MethodOrValue loadAttribute(String name, Map<String, Object> m, Scope parent,
            List<TypeParameter> typeParameters) {
        String metatype = (String)m.get(MetamodelGenerator.KEY_METATYPE);
        MethodOrValue d = MetamodelGenerator.METATYPE_GETTER.equals(metatype) ? new Getter() : new Value();
        d.setName(name);
        d.setContainer(parent);
        d.setUnit(unit);
        if (parent == this) {
            unit.addDeclaration(d);
        }
        setAnnotations(d, (Map<String,List<String>>)m.get(MetamodelGenerator.KEY_ANNOTATIONS));
        if (m.containsKey("var")) {
            ((Value)d).setVariable(true);
        }
        d.setType(getTypeFromJson((Map<String,Object>)m.get(MetamodelGenerator.KEY_TYPE),
                parent instanceof Declaration ? (Declaration)parent : null, typeParameters));
        return d;
    }

    /** Sets the refined declarations for the type's members. */
    private void refineMembers(ClassOrInterface coi) {
        //fill refined declarations
        for (Declaration d : coi.getMembers()) {
            if (d.isActual()) {
                Declaration refined = coi.getRefinedMember(d.getName(), null, false);
                d.setRefinedDeclaration(refined);
            }
            if (d instanceof ClassOrInterface) {
                refineMembers((ClassOrInterface)d);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private Interface loadInterface(String name, Map<String, Object> m, Scope parent, final List<TypeParameter> existing) {
        //Check if it's been loaded first
        //It hasn't been loaded, so create it
        Interface t;
        if (m.get(MetamodelGenerator.KEY_METATYPE) instanceof Interface) {
            //it's been loaded
            return (Interface)m.get(MetamodelGenerator.KEY_METATYPE);
        } else {
            Declaration maybe = parent.getDirectMember(name, null, false);
            if (maybe == null) {
                if (m.containsKey("$alias")) {
                    t = new InterfaceAlias();
                } else {
                    t = new Interface();
                }
                t.setContainer(parent);
                t.setName(name);
                t.setUnit(unit);
                setAnnotations(t, (Map<String,List<String>>)m.get(MetamodelGenerator.KEY_ANNOTATIONS));
                if (parent == this) {
                    unit.addDeclaration(t);
                } else {
                    parent.getMembers().add(t);
                }
            } else if (maybe instanceof Interface) {
                t = (Interface)maybe;
            } else {
                throw new IllegalStateException(maybe + " should be an Interface");
            }
        }
        final List<TypeParameter> tparms = parseTypeParameters(
                (List<Map<String,Object>>)m.get(MetamodelGenerator.KEY_TYPE_PARAMS), t, existing);
        final List<TypeParameter> allparms = JsonPackage.merge(tparms, existing);
        //All interfaces extend Object, except aliases
        if (t.isAlias()) {
            t.setExtendedType(getTypeFromJson((Map<String,Object>)m.get("$alias"),
                    parent instanceof Declaration ? (Declaration)parent : null, allparms));
        } else {
            t.setExtendedType(getTypeFromJson(objclass,
                    parent instanceof Declaration ? (Declaration)parent : null, null));
        }
        if (m.containsKey(MetamodelGenerator.KEY_SELF_TYPE)) {
            for (TypeParameter _tp : tparms) {
                if (_tp.getName().equals(m.get(MetamodelGenerator.KEY_SELF_TYPE))) {
                    t.setSelfType(_tp.getType());
                    _tp.setSelfTypedDeclaration(t);
                }
            }
        }
        if (m.containsKey("of")) {
            t.setCaseTypes(parseTypeList((List<Map<String,Object>>)m.get("of"), allparms));
        }
        if (m.containsKey("satisfies")) {
            t.setSatisfiedTypes(parseTypeList((List<Map<String,Object>>)m.get("satisfies"), allparms));
        }
        if (m.containsKey(MetamodelGenerator.KEY_INTERFACES)) {
            for (Map.Entry<String,Map<String,Object>> inner : ((Map<String,Map<String,Object>>)m.get(MetamodelGenerator.KEY_INTERFACES)).entrySet()) {
                loadInterface(inner.getKey(), inner.getValue(), t, allparms);
            }
        }
        if (m.containsKey(MetamodelGenerator.KEY_CLASSES)) {
            for (Map.Entry<String,Map<String,Object>> inner : ((Map<String,Map<String,Object>>)m.get(MetamodelGenerator.KEY_CLASSES)).entrySet()) {
                loadClass(inner.getKey(), inner.getValue(), t, allparms);
            }
        }
        addAttributesAndMethods(m, t, allparms);
        m.put(MetamodelGenerator.KEY_METATYPE, t);
        return t;
    }

    /** Loads an object declaration, creating it if necessary, and returns its type declaration. */
    @SuppressWarnings("unchecked")
    private TypeDeclaration loadObject(String name, Map<String, Object> m, Scope parent, List<TypeParameter> existing) {
        Declaration maybe = parent.getDirectMember(name, null, false);
        if (maybe instanceof Value) {
            return ((Value) maybe).getTypeDeclaration();
        }
        Value obj = new Value();
        obj.setName(name);
        obj.setContainer(parent);
        obj.setUnit(unit);
        setAnnotations(obj, (Map<String,List<String>>)m.get(MetamodelGenerator.KEY_ANNOTATIONS));
        com.redhat.ceylon.compiler.typechecker.model.Class type = new com.redhat.ceylon.compiler.typechecker.model.Class();
        type.setName(name);
        setAnnotations(type, (Map<String,List<String>>)m.get(MetamodelGenerator.KEY_ANNOTATIONS));
        type.setAnonymous(true);
        type.setUnit(unit);
        type.setContainer(parent);
        if (parent == this) {
            unit.addDeclaration(obj);
            unit.addDeclaration(type);
        }
        if (m.containsKey("super")) {
            type.setExtendedType(getTypeFromJson((Map<String,Object>)m.get("super"),
                    parent instanceof Declaration ? (Declaration)parent : null, existing));
        } else {
            type.setExtendedType(getTypeFromJson(idobj, parent instanceof Declaration ? (Declaration)parent : null, existing));
        }
        if (m.containsKey("satisfies")) {
            type.setSatisfiedTypes(parseTypeList((List<Map<String,Object>>)m.get("satisfies"), existing));
        }
        addAttributesAndMethods(m, type, existing);
        obj.setType(type.getType());
        return type;
    }

    /** Load a type alias, creating it if necessary.
     * @return The TypeAlias declaration. */
    @SuppressWarnings("unchecked")
    private TypeAlias loadTypeAlias(String name, Map<String, Object> m, Scope parent, List<TypeParameter> existing) {
        TypeAlias alias;
        if (m.get(MetamodelGenerator.KEY_METATYPE) instanceof TypeAlias) {
            //It's been loaded already
            alias = (TypeAlias)m.get(MetamodelGenerator.KEY_METATYPE);
        } else {
            Declaration maybe = parent.getDirectMember(name, null, false);
            if (maybe == null) {
                alias = new TypeAlias();
                alias.setContainer(parent);
                alias.setName(name);
                alias.setUnit(unit);
                setAnnotations(alias, (Map<String,List<String>>)m.get(MetamodelGenerator.KEY_ANNOTATIONS));
                if (parent == this) {
                    unit.addDeclaration(alias);
                } else {
                    parent.getMembers().add(alias);
                }
            } else if (maybe instanceof TypeAlias) {
                alias = (TypeAlias)maybe;
            } else {
                throw new IllegalStateException(maybe + " should be an TypeAlias");
            }
        }
        //All interfaces extend Object, except aliases
        alias.setExtendedType(getTypeFromJson((Map<String,Object>)m.get("$alias"),
                parent instanceof Declaration ? (Declaration)parent : null, existing));
        final List<TypeParameter> tparms = parseTypeParameters(
                (List<Map<String,Object>>)m.get(MetamodelGenerator.KEY_TYPE_PARAMS), alias, existing);
        final List<TypeParameter> allparms = JsonPackage.merge(tparms, existing);
        if (m.containsKey(MetamodelGenerator.KEY_SELF_TYPE)) {
            for (TypeParameter _tp : tparms) {
                if (_tp.getName().equals(m.get(MetamodelGenerator.KEY_SELF_TYPE))) {
                    alias.setSelfType(_tp.getType());
                }
            }
        }
        if (m.containsKey("of")) {
            alias.setCaseTypes(parseTypeList((List<Map<String,Object>>)m.get("of"), allparms));
        }
        if (m.containsKey("satisfies")) {
            alias.setSatisfiedTypes(parseTypeList((List<Map<String,Object>>)m.get("satisfies"), allparms));
        }
        m.put(MetamodelGenerator.KEY_METATYPE, alias);
        return alias;
    }

    /** Looks up a type from model data, creating it if necessary. The returned type will have its
     * type parameters substituted if needed. */
    private ProducedType getTypeFromJson(Map<String, Object> m, Declaration container, List<TypeParameter> typeParams) {
        ProducedType rval = null;
        if (m.containsKey("comp")) {
            @SuppressWarnings("unchecked")
            List<Map<String,Object>> tmaps = (List<Map<String,Object>>)m.get(MetamodelGenerator.KEY_TYPES);
            ArrayList<ProducedType> types = new ArrayList<ProducedType>(tmaps.size());
            for (Map<String, Object> tmap : tmaps) {
                types.add(getTypeFromJson(tmap, container, typeParams));
            }
            if ("u".equals(m.get("comp"))) {
                UnionType ut = new UnionType(unit);
                ut.setCaseTypes(types);
                rval = ut.getType();
            } else {
                IntersectionType it = new IntersectionType(unit);
                it.setSatisfiedTypes(types);
                rval = it.getType();
            }
        } else {
            String tname = (String)m.get(MetamodelGenerator.KEY_NAME);
            String pname = (String)m.get(MetamodelGenerator.KEY_PACKAGE);
            if (pname == null) {
                //Maybe it's a ref to a type parameter
                for (TypeParameter typeParam : typeParams) {
                    if (typeParam.getName().equals(tname)) {
                        rval = typeParam.getType();
                    }
                }
            } else {
                String mname = (String)m.get(MetamodelGenerator.KEY_MODULE);
                com.redhat.ceylon.compiler.typechecker.model.Package rp;
                if (container != null) {
                    //First look in the nested declarations
                    Declaration d = container.getMember(tname, null, false);
                    if (d instanceof TypeDeclaration) {
                        rval = ((TypeDeclaration)d).getType();
                    }
                }
                if (mname == null) {
                    //local type
                    rp = getModule().getDirectPackage(pname);
                } else if ("ceylon.language".equals(mname)) {
                    rp = getModule().getLanguageModule().getRootPackage();
                } else {
                    rp = getModule().getPackage(pname);
                }
                if (rval == null) {
                    //Then look in the top-level declarations
                    for (Declaration d : rp.getMembers()) {
                        if (d instanceof TypeDeclaration && tname.equals(d.getName())) {
                            rval = ((TypeDeclaration)d).getType();
                        }
                    }
                }
                if (rval == null && rp == this) {
                    rval = ((TypeDeclaration)load(tname, typeParams)).getType();
                }
            }
            if (rval != null && m.containsKey(MetamodelGenerator.KEY_TYPE_PARAMS)) {
                //Substitute type parameters
                HashMap<TypeParameter, ProducedType> concretes = new HashMap<TypeParameter, ProducedType>();
                Iterator<TypeParameter> viter = rval.getDeclaration().getTypeParameters().iterator();
                @SuppressWarnings("unchecked")
                List<Map<String,Object>> modelParms = (List<Map<String,Object>>)m.get(MetamodelGenerator.KEY_TYPE_PARAMS);
                for (Map<String,Object> ptparm : modelParms) {
                    TypeParameter _cparm = viter.next();
                    if (ptparm.containsKey(MetamodelGenerator.KEY_PACKAGE) || ptparm.containsKey(MetamodelGenerator.KEY_TYPES)) {
                        //Substitute for proper type
                        concretes.put(_cparm, getTypeFromJson(ptparm, container, typeParams));
                    } else if (ptparm.containsKey(MetamodelGenerator.KEY_NAME)) {
                        //Look for type parameter with same name
                        for (TypeParameter typeParam : typeParams) {
                            if (typeParam.getName().equals(ptparm.get(MetamodelGenerator.KEY_NAME))) {
                                concretes.put(_cparm, typeParam.getType());
                            }
                        }
                    }
                }
                if (!concretes.isEmpty()) {
                    rval = rval.substitute(concretes);
                }
            }
        }
        if (rval == null) {
            try {
                if (container == null) {
                    throw new IllegalArgumentException(String.format("Couldn't find type %s::%s for %s in %s<%s>",
                            m.get(MetamodelGenerator.KEY_PACKAGE), m.get(MetamodelGenerator.KEY_NAME),
                            m.get(MetamodelGenerator.KEY_MODULE), m, typeParams));
                } else {
                    throw new IllegalArgumentException(String.format("Couldn't find type %s::%s or %s::%s.%s for %s in %s<%s>",
                            m.get(MetamodelGenerator.KEY_PACKAGE), m.get(MetamodelGenerator.KEY_NAME),
                            m.get(MetamodelGenerator.KEY_PACKAGE), container.getName(), m.get(MetamodelGenerator.KEY_NAME),
                            m.get(MetamodelGenerator.KEY_MODULE), m, typeParams));
                }
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            }
        }
        return rval;
    }

    /** Load a top-level declaration with the specified name, by parsing its model data. */
    Declaration load(String name, List<TypeParameter> existing) {
        @SuppressWarnings("unchecked")
        Map<String,Object> map = (Map<String,Object>)model.get(name);
        if (map == null) {
            if ("Nothing".equals(name) && "ceylon.language".equals(pkgname)) {
                return nothing;
            }
            throw new IllegalStateException("Cannot find " + name + " in " + model.keySet());
        }
        String metatype = (String)map.get(MetamodelGenerator.KEY_METATYPE);
        if (metatype == null) {
            throw new IllegalArgumentException("Missing metatype from entry " + map);
        }
        if (metatype.equals(MetamodelGenerator.METATYPE_ATTRIBUTE)
                || metatype.equals(MetamodelGenerator.METATYPE_GETTER)) {
            return loadAttribute(name, map, this, null);
        } else if (metatype.equals(MetamodelGenerator.METATYPE_CLASS)) {
            return loadClass(name, map, this, existing);
        } else if (metatype.equals(MetamodelGenerator.METATYPE_INTERFACE)) {
            return loadInterface(name, map, this, existing);
        } else if (metatype.equals(MetamodelGenerator.METATYPE_METHOD)) {
            return loadMethod(name, map, this, existing);
        } else if (metatype.equals(MetamodelGenerator.METATYPE_OBJECT)) {
            return loadObject(name, map, this, existing);
        } else if (metatype.equals(MetamodelGenerator.METATYPE_ALIAS)) {
            return loadTypeAlias(name, map, this, existing);
        }
        System.out.println("WTF is this shit " + map);
        return null;
    }

    private void setAnnotations(Declaration d, Map<String,List<String>> m) {
        if (m == null) return;
        for (Map.Entry<String, List<String>> e : m.entrySet()) {
            String name = e.getKey();
            if ("shared".equals(name)) {
                d.setShared(true);
            } else if ("formal".equals(name)) {
                d.setFormal(true);
            } else if ("actual".equals(name)) {
                d.setActual(true);
            } else if ("default".equals(name)) {
                d.setDefault(true);
            } else if ("native".equals(name)) {
                d.setNative(true);
            }
            Annotation ann = new Annotation();
            ann.setName(name);
            for (String arg : e.getValue()) {
                ann.addPositionalArgment(arg);
            }
            d.getAnnotations().add(ann);
        }
        //This is to avoid problems with private declarations while
        //compiling the language module
        if (JsCompiler.isCompilingLanguageModule() && d.isToplevel() && !d.isShared()) {
            d.setShared(true);
        }
    }

    /** Create a new list that contains all the type parameters in the first list,
     * and the type parameters from the second list that don't have any names already present in the first list.
     */
    public static List<TypeParameter> merge(List<TypeParameter> l1, List<TypeParameter> l2) {
        int size = (l1 == null ? 0 : l1.size()) + (l2 == null ? 0 : l2.size());
        ArrayList<TypeParameter> merged = new ArrayList<TypeParameter>(size);
        HashSet<String> names = new HashSet<String>();
        if (l1 != null) {
            for (TypeParameter t : l1) {
                merged.add(t);
                names.add(t.getName());
            }
        }
        if (l2 != null) {
            for (TypeParameter t : l2) {
                if (!names.contains(t.getName())) {
                    merged.add(t);
                }
            }
        }
        return merged;
    }

}
