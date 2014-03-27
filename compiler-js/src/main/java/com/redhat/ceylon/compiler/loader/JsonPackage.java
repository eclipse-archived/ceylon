package com.redhat.ceylon.compiler.loader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.compiler.js.CompilerErrorException;
import com.redhat.ceylon.compiler.js.JsCompiler;
import com.redhat.ceylon.compiler.typechecker.analyzer.ModuleManager;
import com.redhat.ceylon.compiler.typechecker.model.Annotation;
import com.redhat.ceylon.compiler.typechecker.model.NothingType;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
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
import com.redhat.ceylon.compiler.typechecker.model.Util;
import com.redhat.ceylon.compiler.typechecker.model.Value;

public class JsonPackage extends com.redhat.ceylon.compiler.typechecker.model.Package {

    //Ugly hack to have a ref to Basic at hand, to use as implicit supertype of classes
    private final static Map<String,Object> idobj = new HashMap<String, Object>();
    //This is to use as the implicit supertype of interfaces
    private final static Map<String,Object> objclass = new HashMap<String, Object>();
    //This is for type parameters
    private final static Map<String,Object> voidclass = new HashMap<String, Object>();
    private Map<String,Object> model;
    private final String pkgname;
    private boolean loaded = false;
    private final Unit u2 = new Unit();
    private NothingType nothing = new NothingType(u2);

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
    }

    public void setModule(com.redhat.ceylon.compiler.typechecker.model.Module module) {
        if (module instanceof JsonModule && model == null) {
            model = ((JsonModule)module).getModelForPackage(getNameAsString());
            u2.setPackage(this);
            u2.setFilename("");
            u2.setFullPath("");
            u2.setRelativePath("");
            addUnit(u2);
        }
        super.setModule(module);
    };
    Map<String,Object> getModel() { return model; }

    void loadDeclarations() {
        if (loaded) return;
        loaded = true;
        if (getModule().getLanguageModule() == getModule() && "ceylon.language".equals(pkgname)) {
            //Mark the language module as immediately available to bypass certain validations
            getModule().setAvailable(true);
        }
        //Ugly ass hack - add Nothing to the model
        nothing.setContainer(this);
        nothing.setUnit(u2);
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

    /** Loads a class from the specified map. To avoid circularities, when the class is being created it is
     * added to the map, and once it's been fully loaded, all other keys are removed. */
    @SuppressWarnings("unchecked")
    private com.redhat.ceylon.compiler.typechecker.model.Class loadClass(String name, Map<String, Object> m,
            Scope parent, final List<TypeParameter> existing) {
        com.redhat.ceylon.compiler.typechecker.model.Class cls;
        m.remove(MetamodelGenerator.KEY_NAME);
        if (m.get(MetamodelGenerator.KEY_METATYPE) instanceof com.redhat.ceylon.compiler.typechecker.model.Class) {
            cls = (com.redhat.ceylon.compiler.typechecker.model.Class)m.get(MetamodelGenerator.KEY_METATYPE);
            if (m.size() <= 3) {
                //It's been fully loaded
                return cls;
            }
        } else {
            //It's not there, so create it
            if (m.containsKey("$alias")) {
                cls = new com.redhat.ceylon.compiler.typechecker.model.ClassAlias();
            } else {
                cls = new com.redhat.ceylon.compiler.typechecker.model.Class();
            }
            cls.setAbstract(m.remove("abstract") != null);
            cls.setAnonymous(m.remove("$anon") != null);
            cls.setContainer(parent);
            cls.setName(name);
            cls.setUnit(u2);
            if (parent == this) {
                u2.addDeclaration(cls);
            }
            parent.addMember(cls);
            m.put(MetamodelGenerator.KEY_METATYPE, cls);
            setAnnotations(cls, (Map<String,List<String>>)m.remove(MetamodelGenerator.KEY_ANNOTATIONS));
            if (m.containsKey(MetamodelGenerator.KEY_IS_ANNOTATION)) {
                cls.setAnnotation(true);
            }
        }
        //Type parameters are about the first thing we need to load
        final List<TypeParameter> tparms = parseTypeParameters(
                (List<Map<String,Object>>)m.remove(MetamodelGenerator.KEY_TYPE_PARAMS), cls, existing);
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
            if (cls.getExtendedType() == null) {
                if (m.containsKey("super")) {
                    ProducedType father = getTypeFromJson((Map<String,Object>)m.get("super"),
                            parent instanceof Declaration ? (Declaration)parent : null, allparms);
                    if (father != null) {
                        m.remove("super");
                        cls.setExtendedType(father);
                    }
                } else {
                    cls.setExtendedType(getTypeFromJson(idobj,
                            parent instanceof Declaration ? (Declaration)parent : null, allparms));
                }
            }
        }

        ParameterList plist = parseParameters((List<Map<String,Object>>)m.remove(MetamodelGenerator.KEY_PARAMS),
                cls, allparms);
        plist.setNamedParametersSupported(true);
        cls.setParameterList(plist);
        if (m.containsKey("of") && cls.getCaseTypes() == null) {
            cls.setCaseTypes(parseTypeList((List<Map<String,Object>>)m.get("of"), allparms));
            m.remove("of");
        }
        if (m.containsKey(MetamodelGenerator.KEY_SATISFIES)) {
            for (ProducedType sat : parseTypeList((List<Map<String,Object>>)m.remove(MetamodelGenerator.KEY_SATISFIES), allparms)) {
                Util.addToIntersection(cls.getSatisfiedTypes(), sat, u2);
            }
        }
        if (m.containsKey(MetamodelGenerator.KEY_INTERFACES)) {
            for (Map.Entry<String,Map<String,Object>> inner : ((Map<String,Map<String,Object>>)m.remove(MetamodelGenerator.KEY_INTERFACES)).entrySet()) {
                loadInterface(inner.getKey(), inner.getValue(), cls, allparms);
            }
        }
        if (m.containsKey(MetamodelGenerator.KEY_CLASSES)) {
            for (Map.Entry<String,Map<String,Object>> inner : ((Map<String,Map<String,Object>>)m.remove(MetamodelGenerator.KEY_CLASSES)).entrySet()) {
                loadClass(inner.getKey(), inner.getValue(), cls, allparms);
            }
        }
        if (m.containsKey(MetamodelGenerator.KEY_OBJECTS)) {
            for (Map.Entry<String,Map<String,Object>> inner : ((Map<String,Map<String,Object>>)m.remove(MetamodelGenerator.KEY_OBJECTS)).entrySet()) {
                loadObject(inner.getKey(), inner.getValue(), cls, allparms);
            }
        }
        addAttributesAndMethods(m, cls, allparms);
        m.clear();
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
                if (d.getDirectMember(e.getKey(), null, false) == null) {
                    d.getMembers().add(loadAttribute(e.getKey(), e.getValue(), (Scope)d, tparms));
                }
            }
        }
        //Methods
        sub = (Map<String,Map<String,Object>>)m.get(MetamodelGenerator.KEY_METHODS);
        if (sub != null) {
            for(Map.Entry<String, Map<String,Object>> e : sub.entrySet()) {
                if (d.getDirectMember(e.getKey(), null, false) == null) {
                    d.getMembers().add(loadMethod(e.getKey(), e.getValue(), (Scope)d, tparms));
                }
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
    private List<TypeParameter> parseTypeParameters(List<Map<String,Object>> typeParams, final Declaration container,
            List<TypeParameter> existing) {
        if (typeParams == null) return Collections.emptyList();
        //New array with existing parms to avoid modifying that one
        List<TypeParameter> allparms = new ArrayList<TypeParameter>((existing == null ? 0 : existing.size()) + typeParams.size());
        if (existing != null && !existing.isEmpty()) {
            allparms.addAll(existing);
        }
        List<TypeParameter> tparms = new ArrayList<TypeParameter>(typeParams.size());
        //To avoid circularity, this is done in two phases:
        //First create the type parameters
        for (Map<String,Object> tp : typeParams) {
            final Declaration maybe;
            if (tp.get(MetamodelGenerator.KEY_METATYPE) instanceof TypeParameter) {
                maybe = (TypeParameter)tp.get(MetamodelGenerator.KEY_METATYPE);
            } else {
                maybe = container.getDirectMember((String)tp.get(MetamodelGenerator.KEY_NAME), null, false);
            }
            if (maybe instanceof TypeParameter) {
                //we already had it (from partial loading elsewhere)
                allparms.add((TypeParameter)maybe);
                tparms.add((TypeParameter)maybe);
                tp.put(MetamodelGenerator.KEY_METATYPE, maybe);
            } else {
                TypeParameter tparm = new TypeParameter();
                tparm.setUnit(u2);
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
                tparm.setDefaulted(tp.containsKey(MetamodelGenerator.KEY_DEFAULT));
                tparms.add(tparm);
                allparms.add(tparm);
                tp.put(MetamodelGenerator.KEY_METATYPE, tparm);
            }
        }
        if (container instanceof TypeDeclaration) {
            ((TypeDeclaration) container).setTypeParameters(tparms);
        } else if (container instanceof Method) {
            ((Method) container).setTypeParameters(tparms);
        }
        //Second, add defaults and heritage
        for (Map<String,Object> tp : typeParams) {
            TypeParameter tparm = (TypeParameter)tp.get(MetamodelGenerator.KEY_METATYPE);
            if (tparm.getExtendedType() == null) {
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
            if (tparm.isDefaulted()) {
                @SuppressWarnings("unchecked")
                final Map<String,Object> deftype = (Map<String,Object>)tp.get(MetamodelGenerator.KEY_DEFAULT);
                tparm.setDefaultTypeArgument(getTypeFromJson(deftype, container, existing));
            }
            if (tp.containsKey(MetamodelGenerator.KEY_SATISFIES)) {
                @SuppressWarnings("unchecked")
                final List<Map<String,Object>> stypes = (List<Map<String,Object>>)tp.get(MetamodelGenerator.KEY_SATISFIES);
                for (ProducedType sat : parseTypeList(stypes, allparms)) {
                    Util.addToIntersection(tparm.getSatisfiedTypes(), sat, u2);
                }
                tparm.setConstrained(true);
            } else if (tp.containsKey("of")) {
                @SuppressWarnings("unchecked")
                final List<Map<String,Object>> oftype = (List<Map<String,Object>>)tp.get("of");
                tparm.setCaseTypes(parseTypeList(oftype, allparms));
                tparm.setConstrained(true);
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
                Parameter param = new Parameter();
                final String paramtype = (String)p.get("$pt");
                param.setHidden(p.containsKey("$hdn"));
                param.setName((String)p.get(MetamodelGenerator.KEY_NAME));
                param.setDeclaration(owner);
                param.setDefaulted(p.containsKey(MetamodelGenerator.KEY_DEFAULT));
                param.setSequenced(p.containsKey("seq"));
                param.setAtLeastOne(p.containsKey("$min1"));
                if (paramtype == null || "v".equals(paramtype)) {
                    Value _v = new Value();
                    param.setModel(_v);
                } else if ("f".equals(paramtype)) {
                    @SuppressWarnings("unchecked")
                    List<List<Map<String,Object>>> paramLists = (List<List<Map<String,Object>>>)p.get(MetamodelGenerator.KEY_PARAMS);
                    Method _m = new Method();
                    param.setModel(_m);
                    if (paramLists == null) {
                        _m.addParameterList(new ParameterList());
                    } else {
                        boolean first = true;
                        for (List<Map<String,Object>> subplist : paramLists) {
                            ParameterList _params = parseParameters(subplist, _m, typeParameters);
                            if (first) {
                                first = false;
                            } else {
                                _params.setNamedParametersSupported(false);
                            }
                            _m.addParameterList(_params);
                        }
                    }
                } else {
                    throw new IllegalArgumentException("Unknown parameter type " + paramtype);
                }
                if (param.getModel() != null) {
                    param.getModel().setInitializerParameter(param);
                    param.getModel().setName(param.getName());
                    param.getModel().setUnit(u2);
                    if (owner instanceof Scope) {
                        param.getModel().setContainer((Scope)owner);
                    }
                    if (p.get(MetamodelGenerator.KEY_TYPE) instanceof Map) {
                        @SuppressWarnings("unchecked")
                        final Map<String,Object> ktype = (Map<String,Object>)p.get(MetamodelGenerator.KEY_TYPE);
                        param.getModel().setType(getTypeFromJson(ktype, owner, typeParameters));
                    } else {
                        //parameter type
                        for (TypeParameter tp : typeParameters) {
                            if (tp.getName().equals(p.get(MetamodelGenerator.KEY_TYPE))) {
                                param.getModel().setType(tp.getType());
                            }
                        }
                    }
                    @SuppressWarnings("unchecked")
                    final Map<String,List<String>> _anns = (Map<String,List<String>>)p.remove(MetamodelGenerator.KEY_ANNOTATIONS);
                    setAnnotations(param.getModel(), _anns);
                }
                //owner.getMembers().add(param);
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
        setAnnotations(md, (Map<String,List<String>>)m.remove(MetamodelGenerator.KEY_ANNOTATIONS));
        md.setUnit(u2);
        if (m.containsKey(MetamodelGenerator.KEY_IS_ANNOTATION)) {
            md.setAnnotation(true);
        }
        if (parent == this) {
            //Top-level declarations are directly added to the unit
            u2.addDeclaration(md);
            addMember(null);
        }
        final List<TypeParameter> tparms = parseTypeParameters(
                (List<Map<String,Object>>)m.get(MetamodelGenerator.KEY_TYPE_PARAMS), md, existing);
        final List<TypeParameter> allparms = JsonPackage.merge(tparms, existing);
        md.setType(getTypeFromJson((Map<String,Object>)m.remove(MetamodelGenerator.KEY_TYPE),
                parent instanceof Declaration ? (Declaration)parent : null, allparms));
        List<List<Map<String,Object>>> paramLists = (List<List<Map<String,Object>>>)m.remove(MetamodelGenerator.KEY_PARAMS);
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
        Value d = new Value();
        d.setTransient(MetamodelGenerator.METATYPE_GETTER.equals(metatype));
        d.setName(name);
        d.setContainer(parent);
        d.setUnit(u2);
        if (parent == this) {
            u2.addDeclaration(d);
            addMember(null);
        }
        @SuppressWarnings("unchecked")
        final Map<String,List<String>> _anns = (Map<String,List<String>>)m.remove(MetamodelGenerator.KEY_ANNOTATIONS);
        setAnnotations(d, _anns);
        if (m.containsKey("var")) {
            ((Value)d).setVariable(true);
        }
        @SuppressWarnings("unchecked")
        final Map<String,Object> ktype = (Map<String,Object>)m.get(MetamodelGenerator.KEY_TYPE);
        d.setType(getTypeFromJson(ktype, parent instanceof Declaration ? (Declaration)parent : null, typeParameters));
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
        m.remove(MetamodelGenerator.KEY_NAME);
        if (m.get(MetamodelGenerator.KEY_METATYPE) instanceof Interface) {
            t = (Interface)m.get(MetamodelGenerator.KEY_METATYPE);
            if (m.size() <= 3) {
                //it's been loaded
                return t;
            }
        } else {
            if (m.containsKey("$alias")) {
                t = new InterfaceAlias();
            } else {
                t = new Interface();
            }
            t.setContainer(parent);
            t.setName(name);
            t.setUnit(u2);
            if (parent == this) {
                u2.addDeclaration(t);
            }
            parent.addMember(t);
            m.put(MetamodelGenerator.KEY_METATYPE, t);
            setAnnotations(t, (Map<String,List<String>>)m.remove(MetamodelGenerator.KEY_ANNOTATIONS));
        }
        List<TypeParameter> tparms = t.getTypeParameters();
        List<Map<String,Object>> listOfMaps = (List<Map<String,Object>>)m.get(MetamodelGenerator.KEY_TYPE_PARAMS);
        if (listOfMaps != null && (tparms == null || tparms.size() < listOfMaps.size())) {
            tparms = parseTypeParameters(listOfMaps, t, existing);
            m.remove(MetamodelGenerator.KEY_TYPE_PARAMS);
        }
        final List<TypeParameter> allparms = JsonPackage.merge(tparms, existing);
        //All interfaces extend Object, except aliases
        if (t.getExtendedType() == null) {
            if (t.isAlias()) {
                t.setExtendedType(getTypeFromJson((Map<String,Object>)m.remove("$alias"),
                        parent instanceof Declaration ? (Declaration)parent : null, allparms));
            } else {
                t.setExtendedType(getTypeFromJson(objclass,
                        parent instanceof Declaration ? (Declaration)parent : null, null));
            }
        }
        if (m.containsKey(MetamodelGenerator.KEY_SELF_TYPE)) {
            for (TypeParameter _tp : tparms) {
                if (_tp.getName().equals(m.get(MetamodelGenerator.KEY_SELF_TYPE))) {
                    t.setSelfType(_tp.getType());
                    _tp.setSelfTypedDeclaration(t);
                }
            }
            m.remove(MetamodelGenerator.KEY_SELF_TYPE);
        }
        if (m.containsKey("of") && t.getCaseTypes() == null) {
            t.setCaseTypes(parseTypeList((List<Map<String,Object>>)m.remove("of"), allparms));
        }
        if (m.containsKey(MetamodelGenerator.KEY_SATISFIES)) {
            for (ProducedType s : parseTypeList((List<Map<String,Object>>)m.remove(MetamodelGenerator.KEY_SATISFIES), allparms)) {
                Util.addToIntersection(t.getSatisfiedTypes(), s, u2);
            }
        }
        if (m.containsKey(MetamodelGenerator.KEY_INTERFACES)) {
            for (Map.Entry<String,Map<String,Object>> inner : ((Map<String,Map<String,Object>>)m.remove(MetamodelGenerator.KEY_INTERFACES)).entrySet()) {
                loadInterface(inner.getKey(), inner.getValue(), t, allparms);
            }
        }
        if (m.containsKey(MetamodelGenerator.KEY_CLASSES)) {
            for (Map.Entry<String,Map<String,Object>> inner : ((Map<String,Map<String,Object>>)m.remove(MetamodelGenerator.KEY_CLASSES)).entrySet()) {
                loadClass(inner.getKey(), inner.getValue(), t, allparms);
            }
        }
        addAttributesAndMethods(m, t, allparms);
        m.clear();
        m.put(MetamodelGenerator.KEY_METATYPE, t);
        return t;
    }

    /** Loads an object declaration, creating it if necessary, and returns its type declaration. */
    @SuppressWarnings("unchecked")
    private TypeDeclaration loadObject(String name, Map<String, Object> m, Scope parent, List<TypeParameter> existing) {
        Value obj;
        if (m.get(MetamodelGenerator.KEY_METATYPE) instanceof Value) {
            obj = (Value)m.get(MetamodelGenerator.KEY_METATYPE);
        } else {
            obj = new Value();
            m.put(MetamodelGenerator.KEY_METATYPE, obj);
            obj.setName(name);
            obj.setContainer(parent);
            obj.setUnit(u2);
            com.redhat.ceylon.compiler.typechecker.model.Class type = new com.redhat.ceylon.compiler.typechecker.model.Class();
            type.setName(name);
            type.setAnonymous(true);
            type.setUnit(u2);
            type.setContainer(parent);
            if (parent == this) {
                u2.addDeclaration(obj);
                u2.addDeclaration(type);
                addMember(null);
            }
            obj.setType(type.getType());
            setAnnotations(obj, (Map<String,List<String>>)m.get(MetamodelGenerator.KEY_ANNOTATIONS));
            setAnnotations(obj.getTypeDeclaration(), (Map<String,List<String>>)m.remove(MetamodelGenerator.KEY_ANNOTATIONS));
            if (type.getExtendedType() == null) {
                if (m.containsKey("super")) {
                    type.setExtendedType(getTypeFromJson((Map<String,Object>)m.remove("super"),
                            parent instanceof Declaration ? (Declaration)parent : null, existing));
                } else {
                    type.setExtendedType(getTypeFromJson(idobj, parent instanceof Declaration ? (Declaration)parent : null, existing));
                }
            }
            if (m.containsKey(MetamodelGenerator.KEY_SATISFIES)) {
                for (ProducedType sat : parseTypeList((List<Map<String,Object>>)m.remove(MetamodelGenerator.KEY_SATISFIES), existing)) {
                    Util.addToIntersection(type.getSatisfiedTypes(), sat, u2);
                }
            }
            addAttributesAndMethods(m, type, existing);
        }
        return obj.getTypeDeclaration();
    }

    /** Load a type alias, creating it if necessary.
     * @return The TypeAlias declaration. */
    @SuppressWarnings("unchecked")
    private TypeAlias loadTypeAlias(String name, Map<String, Object> m, Scope parent, List<TypeParameter> existing) {
        TypeAlias alias;
        if (m.get(MetamodelGenerator.KEY_METATYPE) instanceof TypeAlias) {
            //It's been loaded already
            alias = (TypeAlias)m.get(MetamodelGenerator.KEY_METATYPE);
            if (m.size() == 1) {
                return alias;
            }
        } else {
            Declaration maybe = parent.getDirectMember(name, null, false);
            if (maybe == null) {
                alias = new TypeAlias();
                alias.setContainer(parent);
                alias.setName(name);
                alias.setUnit(u2);
                setAnnotations(alias, (Map<String,List<String>>)m.remove(MetamodelGenerator.KEY_ANNOTATIONS));
                if (parent == this) {
                    u2.addDeclaration(alias);
                }
                parent.addMember(alias);
                m.put(MetamodelGenerator.KEY_METATYPE, alias);
            } else if (maybe instanceof TypeAlias) {
                alias = (TypeAlias)maybe;
            } else {
                throw new IllegalStateException(maybe + " should be an TypeAlias");
            }
        }
        //All interfaces extend Object, except aliases
        if (alias.getExtendedType() == null) {
            alias.setExtendedType(getTypeFromJson((Map<String,Object>)m.get("$alias"),
                    parent instanceof Declaration ? (Declaration)parent : null, existing));
        }
        List<Map<String,Object>> listOfMaps = (List<Map<String,Object>>)m.get(MetamodelGenerator.KEY_TYPE_PARAMS);
        final List<TypeParameter> tparms;
        if (listOfMaps != null && alias.getTypeParameters().size()<listOfMaps.size()) {
            tparms = parseTypeParameters(listOfMaps, alias, existing);
        } else {
            tparms = alias.getTypeParameters();
        }
        final List<TypeParameter> allparms = JsonPackage.merge(tparms, existing);
        if (m.containsKey(MetamodelGenerator.KEY_SELF_TYPE)) {
            for (TypeParameter _tp : tparms) {
                if (_tp.getName().equals(m.get(MetamodelGenerator.KEY_SELF_TYPE))) {
                    alias.setSelfType(_tp.getType());
                }
            }
        }
        if (m.containsKey("of")) {
            alias.setCaseTypes(parseTypeList((List<Map<String,Object>>)m.remove("of"), allparms));
        }
        if (m.containsKey(MetamodelGenerator.KEY_SATISFIES)) {
            for (ProducedType sat :parseTypeList((List<Map<String,Object>>)m.remove(MetamodelGenerator.KEY_SATISFIES), allparms)) {
                Util.addToIntersection(alias.getSatisfiedTypes(), sat, u2);
            }
        }
        m.clear();
        m.put(MetamodelGenerator.KEY_METATYPE, alias);
        return alias;
    }

    /** Looks up a type from model data, creating it if necessary. The returned type will have its
     * type parameters substituted if needed. */
    private ProducedType getTypeFromJson(Map<String, Object> m, Declaration container, List<TypeParameter> typeParams) {
        TypeDeclaration td = null;
        if (m.get(MetamodelGenerator.KEY_METATYPE) instanceof TypeDeclaration) {
            td = (TypeDeclaration)m.get(MetamodelGenerator.KEY_METATYPE);
            if (td instanceof ClassOrInterface && td.getUnit().getPackage() instanceof JsonPackage) {
                ((JsonPackage)td.getUnit().getPackage()).load(td.getName(), typeParams);
            }
        }
        if (td == null && m.containsKey("comp")) {
            @SuppressWarnings("unchecked")
            final List<Map<String,Object>> tmaps = (List<Map<String,Object>>)m.get(MetamodelGenerator.KEY_TYPES);
            final ArrayList<ProducedType> types = new ArrayList<ProducedType>(tmaps.size());
            if ("u".equals(m.get("comp"))) {
                UnionType ut = new UnionType(u2);
                for (Map<String, Object> tmap : tmaps) {
                    Util.addToUnion(types, getTypeFromJson(tmap, container, typeParams));
                }
                ut.setCaseTypes(types);
                td = ut;
            } else if ("i".equals(m.get("comp"))) {
                IntersectionType it = new IntersectionType(u2);
                for (Map<String, Object> tmap : tmaps) {
                    Util.addToIntersection(types, getTypeFromJson(tmap, container, typeParams), u2);
                }
                it.setSatisfiedTypes(types);
                td = it;
            } else {
                throw new IllegalArgumentException("Invalid composite type '" + m.get("comp") + "'");
            }
        } else if (td == null) {
            final String tname = (String)m.get(MetamodelGenerator.KEY_NAME);
            final String pname = (String)m.get(MetamodelGenerator.KEY_PACKAGE);
            if (pname == null) {
                //Maybe it's a ref to a type parameter
                for (TypeParameter typeParam : typeParams) {
                    if (typeParam.getName().equals(tname)) {
                        td = typeParam;
                    }
                }
            } else {
                final String mname = (String)m.get(MetamodelGenerator.KEY_MODULE);
                com.redhat.ceylon.compiler.typechecker.model.Package rp;
                if (container != null) {
                    //First look in the nested declarations
                    Declaration d = container.getMember(tname, null, false);
                    if (d instanceof TypeDeclaration && d instanceof TypeParameter == false) {
                        td = (TypeDeclaration)d;
                    }
                }
                if (td == null) {
                    if ("$".equals(pname)) {
                        //Language module package
                        rp = "ceylon.language".equals(getNameAsString())? this :
                            getModule().getLanguageModule().getDirectPackage("ceylon.language");
                    } else if (mname == null) {
                        //local type
                        if (".".equals(pname)) {
                            rp = this;
                        } else {
                            rp = getModule().getDirectPackage(pname);
                        }
                    } else {
                        rp = getModule().getPackage(pname);
                    }
                    if (rp == null) {
                        throw new CompilerErrorException("Package not found: " + pname);
                    }
                    if (rp != this && rp instanceof JsonPackage && !((JsonPackage)rp).loaded) {
                        ((JsonPackage) rp).loadDeclarations();
                    }
                    //Then look in the top-level declarations
                    for (Declaration d : rp.getMembers()) {
                        if (d instanceof TypeDeclaration && tname.equals(d.getName())) {
                            td = (TypeDeclaration)d;
                        }
                    }
                    if (td == null && rp instanceof JsonPackage) {
                        td = (TypeDeclaration)((JsonPackage)rp).load(tname, typeParams);
                    }
                }
            }
        }
        @SuppressWarnings("unchecked")
        final List<Map<String,Object>> modelParms = (List<Map<String,Object>>)m.get(MetamodelGenerator.KEY_TYPE_PARAMS);
        if (td != null && modelParms != null) {
            //Substitute type parameters
            final HashMap<TypeParameter, ProducedType> concretes = new HashMap<TypeParameter, ProducedType>();
            if (td.getTypeParameters().size() < modelParms.size()) {
                if (td.getUnit().getPackage() == this) {
                    parseTypeParameters(modelParms, td, null);
                }
            }
            final Iterator<TypeParameter> viter = td.getTypeParameters().iterator();
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
                return td.getType().substitute(concretes);
            }
        }
        if (td == null) {
            try {
                if (container == null) {
                    throw new IllegalArgumentException(String.format("Couldn't find type %s::%s for %s in %s<%s> (FROM pkg %s)",
                            m.get(MetamodelGenerator.KEY_PACKAGE), m.get(MetamodelGenerator.KEY_NAME),
                            m.get(MetamodelGenerator.KEY_MODULE), m, typeParams, getNameAsString()));
                } else {
                    throw new IllegalArgumentException(String.format("Couldn't find type %s::%s or %s::%s.%s for %s in %s<%s> (FROM pkg %s)",
                            m.get(MetamodelGenerator.KEY_PACKAGE), m.get(MetamodelGenerator.KEY_NAME),
                            m.get(MetamodelGenerator.KEY_PACKAGE), container.getName(), m.get(MetamodelGenerator.KEY_NAME),
                            m.get(MetamodelGenerator.KEY_MODULE), m, typeParams, getNameAsString()));
                }
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            }
        }
        return td.getType();
    }

    /** Load a top-level declaration with the specified name, by parsing its model data. */
    Declaration load(String name, List<TypeParameter> existing) {
        if (model == null){
            System.out.println("AY NO MAMES " + getNameAsString() + " no tengo modelo para cargar " + name + java.util.Arrays.toString(Thread.currentThread().getStackTrace()).replace(',', '\n'));
        }
        @SuppressWarnings("unchecked")
        final Map<String,Object> map = model == null ? null : (Map<String,Object>)model.get(name);
        if (map == null) {
            if ("Nothing".equals(name)) {
                //Load Nothing from language module, regardless of what this package is
                return nothing;
            }
            throw new IllegalStateException("Cannot find " + pkgname + "::" + name + " in " + model.keySet());
        }
        Object metatype = map.get(MetamodelGenerator.KEY_METATYPE);
        if (metatype == null) {
            throw new IllegalArgumentException("Missing metatype from entry " + map);
        }
        if (metatype.equals(MetamodelGenerator.METATYPE_ATTRIBUTE)
                || metatype.equals(MetamodelGenerator.METATYPE_GETTER)) {
            return loadAttribute(name, map, this, null);
        } else if (metatype.equals(MetamodelGenerator.METATYPE_CLASS) || metatype instanceof com.redhat.ceylon.compiler.typechecker.model.Class) {
            return loadClass(name, map, this, existing);
        } else if (metatype.equals(MetamodelGenerator.METATYPE_INTERFACE) || metatype instanceof com.redhat.ceylon.compiler.typechecker.model.Interface) {
            return loadInterface(name, map, this, existing);
        } else if (metatype.equals(MetamodelGenerator.METATYPE_METHOD)) {
            return loadMethod(name, map, this, existing);
        } else if (metatype.equals(MetamodelGenerator.METATYPE_OBJECT) || metatype instanceof Value) {
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
            } else if ("final".equals(name)) {
                ((com.redhat.ceylon.compiler.typechecker.model.Class)d).setFinal(true);
            } else if ("late".equals(name) && d instanceof Value) {
                ((Value)d).setLate(true);
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
