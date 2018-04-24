/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.js.loader;

import static org.eclipse.ceylon.model.typechecker.model.Module.LANGUAGE_MODULE_NAME;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.ceylon.common.Backend;
import org.eclipse.ceylon.common.Versions;
import org.eclipse.ceylon.model.typechecker.model.Annotation;
import org.eclipse.ceylon.model.typechecker.model.ClassAlias;
import org.eclipse.ceylon.model.typechecker.model.Constructor;
import org.eclipse.ceylon.model.typechecker.model.Declaration;
import org.eclipse.ceylon.model.typechecker.model.DeclarationKind;
import org.eclipse.ceylon.model.typechecker.model.Function;
import org.eclipse.ceylon.model.typechecker.model.FunctionOrValue;
import org.eclipse.ceylon.model.typechecker.model.Interface;
import org.eclipse.ceylon.model.typechecker.model.ModelUtil;
import org.eclipse.ceylon.model.typechecker.model.Module;
import org.eclipse.ceylon.model.typechecker.model.ModuleImport;
import org.eclipse.ceylon.model.typechecker.model.Parameter;
import org.eclipse.ceylon.model.typechecker.model.ParameterList;
import org.eclipse.ceylon.model.typechecker.model.SiteVariance;
import org.eclipse.ceylon.model.typechecker.model.Type;
import org.eclipse.ceylon.model.typechecker.model.TypeAlias;
import org.eclipse.ceylon.model.typechecker.model.TypeDeclaration;
import org.eclipse.ceylon.model.typechecker.model.TypeParameter;
import org.eclipse.ceylon.model.typechecker.model.Value;

import org.eclipse.ceylon.compiler.js.util.TypeUtils;

/** Generates the metamodel for all objects in a module.
 * This is used by the MetamodelVisitor.
 * 
 * @author Enrique Zamudio
 */
public class MetamodelGenerator {

    public static final String KEY_CLASSES      = "$c";
    public static final String KEY_INTERFACES   = "$i";
    public static final String KEY_OBJECTS      = "$o";
    public static final String KEY_METHODS      = "$m";
    public static final String KEY_ATTRIBUTES   = "$at";
    public static final String KEY_ANNOTATIONS  = "an";
    public static final String KEY_PACKED_ANNS  = "pa";
    public static final String KEY_TYPE         = "$t";
    public static final String KEY_RETURN_TYPE  = "$rt";
    public static final String KEY_TYPES        = "l";
    public static final String KEY_TYPE_PARAMS  = "tp";
    public static final String KEY_TYPE_ARGS    = "ta";
    public static final String KEY_METATYPE     = "mt";
    public static final String KEY_MODULE       = "md";
    public static final String KEY_NAME         = "nm";
    public static final String KEY_PACKAGE      = "pk";
    public static final String KEY_PARAMS       = "ps";
    public static final String KEY_SELF_TYPE    = "st";
    public static final String KEY_SATISFIES    = "sts";
    public static final String KEY_DS_VARIANCE  = "dv"; //declaration-site variance
    public static final String KEY_US_VARIANCE  = "uv"; //use-site variance
    public static final String KEY_CONSTRUCTOR  = "co";
    public static final String KEY_CONSTRUCTORS = "$cn";
    public static final String KEY_FLAGS        = "$ff";

    public static final String KEY_DEFAULT      = "def";
    public static final String KEY_DYNAMIC      = "dyn";

    // backend specific keys
    public static final String KEY_JS_TSENUM    = "$tsenum"; // case constructor is a TypeScript enum

    public static final String METATYPE_CLASS           = "c";
    public static final String METATYPE_INTERFACE       = "i";
    public static final String METATYPE_ALIAS           = "als";
    public static final String METATYPE_OBJECT          = "o";
    public static final String METATYPE_METHOD          = "m";
    public static final String METATYPE_ATTRIBUTE       = "a";
    public static final String METATYPE_GETTER          = "g";
    public static final String METATYPE_SETTER          = "s";
    public static final String METATYPE_TYPE_PARAMETER  = "tp";
    public static final String METATYPE_PARAMETER       = "prm";
    //DO NOT REARRANGE, only append
    public static final List<String> annotationBits = 
            Arrays.asList("shared", "actual", "formal", "default",
            "sealed", "final", "native", "late", "abstract", "annotation",
            "variable", "serializable", "static");

    private final Map<String, Object> model = new HashMap<>();
    private static final Map<String,Object> unknownTypeMap = new HashMap<>();
    private static final Map<String,Object> nothingTypeMap = new HashMap<>();
    private final Module module;

    public MetamodelGenerator(Module module) {
        this.module = module;
        model.put("$mod-name", module.getNameAsString());
        model.put("$mod-version", module.getVersion());
        for (Annotation a : module.getAnnotations()) {
            List<String> args = a.getPositionalArguments();
            if (args != null && !args.isEmpty() 
                    && a.getName().equals("label")) {
                model.put("$mod-label", args.get(0));
            }
        }
        model.put("$mod-bin", Versions.JS_BINARY_MAJOR_VERSION+"."+Versions.JS_BINARY_MINOR_VERSION);
        if (module.isNative()) {
            List<String> backends = new ArrayList<>(1);
            for(Backend backend : module.getNativeBackends()) {
                backends.add(backend.nativeAnnotation);
            }
            model.put("$mod-nat", backends);
        }
        encodeAnnotations(module.getAnnotations(), module, model);
        if (!module.getImports().isEmpty()) {
            ArrayList<Object> imps = new ArrayList<>(module.getImports().size());
            for (ModuleImport mi : module.getImports()) {
                if (!ModelUtil.isForBackend(mi.getNativeBackends(), Backend.JavaScript)) {
                    continue;
                }
                if (mi.getModule().getVersion() == null) { //#416
                    continue;
                }
                String impath = String.format("%s/%s", mi.getModule().getNameAsString(), mi.getModule().getVersion());
                if (mi.getNamespace() != null) {
                    impath = mi.getNamespace() + ":" + impath;
                }
                if (mi.isOptional() || mi.isExport() || mi.isNative()) {
                    Map<String,Object> optimp = new HashMap<>(3);
                    optimp.put("path",impath);
                    if (mi.isOptional()) {
                        optimp.put("opt", 1);
                    }
                    if (mi.isExport()) {
                        optimp.put("exp", 1);
                    }
                    if (mi.isNative()) {
                        List<String> backends = new ArrayList<>(1);
                        for(Backend backend : mi.getNativeBackends())
                            backends.add(backend.nativeAnnotation);
                        optimp.put("nat", backends);
                    }
                    imps.add(optimp);
                } else {
                    imps.add(impath);
                }
            }
            model.put("$mod-deps", imps);
        }
        if (unknownTypeMap.isEmpty()) {
            unknownTypeMap.put(KEY_NAME, "$U");
        }
        if (nothingTypeMap.isEmpty()) {
            nothingTypeMap.put(KEY_NAME, "Nothing");
            nothingTypeMap.put(KEY_PACKAGE, "$");
        }
    }

    /** Returns the in-memory model as a collection of maps.
     * The top-level map represents the module. */
    public Map<String, Object> getModel() {
        return Collections.unmodifiableMap(model);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> findParent(Declaration d) {
        Map<String,Object> pkgmap = getPackageMap(d.getUnit().getPackage());
        if (d.isToplevel()) {
            return pkgmap;
        }
        List<String> names = TypeUtils.generateModelPath(ModelUtil.getContainingDeclaration(d));
        names.remove(0); //we don't need the package key
        if (names.isEmpty()) {
            return pkgmap;
        }
        Map<String, Object> last = pkgmap;
        for (String name : names) {
            if (last == null) {
                break;
            }
            Map<String,Object> sub = (Map<String,Object>)last.get(name);
            if (sub == null && name.charAt(0)=='$') {
                sub = new HashMap<>();
                last.put(name, sub);
            }
            last = sub;
        }
        return last;
    }

    private Map<String,Object> tupleTypeMap(Type tt, Declaration from) {
        final Map<String, Object> m = new HashMap<>();
        m.put(KEY_NAME, "Tuple");
        m.put(KEY_PACKAGE, "$");
        List<Type> targs = tt.getTypeArgumentList(); //Element, First, Rest
        if (from.getUnit().isHomogeneousTuple(tt)) {
            int min = from.getUnit().getHomogeneousTupleLength(tt);
            m.put(KEY_TYPE, typeMap(targs.get(1), from));
            m.put("count", min);
        } else {
            encodeTypes(from.getUnit().getTupleElementTypes(tt), m, KEY_TYPES, from);
        }
        return m;
    }

    /** Create a map for the specified Type.
     * Includes name, package, module and type parameters, unless it's a union or intersection
     * type, in which case it contains a "comp" key with an "i" or "u" and a key "types" with
     * the list of types that compose it. */
    private Map<String, Object> typeMap(Type pt, Declaration from) {
        if (ModelUtil.isTypeUnknown(pt)) {
            return unknownTypeMap;
        } else if (pt.isNothing()) {
            return nothingTypeMap;
        }
        Map<String, Object> m = new HashMap<>();
        if (pt.isUnion() || pt.isIntersection()) {
            List<Type> subtipos = pt.isUnion() ? pt.getCaseTypes() : pt.getSatisfiedTypes();
            List<Map<String,Object>> subs = new ArrayList<>(subtipos.size());
            for (Type sub : subtipos) {
                subs.add(typeMap(sub, from));
            }
            m.put("comp", pt.isUnion() ? "u" : "i");
            m.put(KEY_TYPES, subs);
            return m;
        } else if (pt.isTuple() && !pt.involvesTypeParameters()) {
            return tupleTypeMap(pt, from);
        }
        TypeDeclaration d = pt.getDeclaration();
        if (d.isToplevel() || pt.isTypeParameter()) {
            m.put(KEY_NAME, d.getName());
        } else {
            String qn = d.getQualifiedNameString();
            int p0 = qn.indexOf("::");
            if (p0>=0) {
                qn = qn.substring(p0+2);
            }
            p0 = qn.indexOf('.');
            if (p0 >= 0) {
                StringBuilder nestedName = new StringBuilder(TypeUtils.modelName(d));
                Declaration pd = ModelUtil.getContainingDeclaration(d);
                while (pd != null) {
                    nestedName.insert(0, '.');
                    nestedName.insert(0, TypeUtils.modelName(pd));
                    pd = ModelUtil.getContainingDeclaration(pd);
                }
                qn = nestedName.toString();
            }
            m.put(KEY_NAME, qn);
        }
        if (d.getDeclarationKind()==DeclarationKind.TYPE_PARAMETER) {
            //For types that reference type parameters, we're done
            return m;
        }
        org.eclipse.ceylon.model.typechecker.model.Package pkg = d.getUnit().getPackage();
        if (pkg == null || pkg.equals(from.getUnit().getPackage())) {
            addPackage(m, ".");
        } else {
            addPackage(m, pkg.getNameAsString());
        }
        if (pkg != null && !pkg.getModule().equals(module)) {
            final Module mod = pkg.getModule();
            m.put(KEY_MODULE, mod.isLanguageModule()?"$":mod.getNameAsString());
        }
        putTypeArguments(m, pt, from);
        return m;
    }

    /** Returns a map with the same info as {@link #typeParameterMap(Type)} but with
     * an additional key {@value #KEY_DS_VARIANCE} if it's covariant ("out") or contravariant ("in"). */
    private Map<String, Object> typeParameterMap(TypeParameter tp, Declaration from) {
        Map<String, Object> map = new HashMap<>();
        map.put(MetamodelGenerator.KEY_NAME, tp.getName());
        if (tp.isCovariant()) {
            map.put(KEY_DS_VARIANCE, "out");
        } else if (tp.isContravariant()) {
            map.put(KEY_DS_VARIANCE, "in");
        }
        if (tp.getSelfType() != null) {
            map.put(KEY_SELF_TYPE, tp.getSelfType().getDeclaration().getName());
        }
        if (tp.getSatisfiedTypes() != null && !tp.getSatisfiedTypes().isEmpty()) {
            encodeTypes(tp.getSatisfiedTypes(), map, KEY_SATISFIES, from);
        }
        if (tp.getCaseTypes() != null && !tp.getCaseTypes().isEmpty()) {
            encodeTypes(tp.getCaseTypes(), map, "of", from);
        }
        if (tp.getDefaultTypeArgument() != null) {
            map.put(KEY_DEFAULT, typeMap(tp.getDefaultTypeArgument(), from));
        }
        return map;
    }

    private void putTypeArguments(Map<String, Object> container, Type pt, Declaration from) {
        int tparmSize = 0;
        Type t = pt;
        while (t != null) {
            tparmSize += t.getTypeArgumentList() == null ? 0 : t.getTypeArgumentList().size();
            t = t.getQualifyingType();
        }
        if (tparmSize > 0) {
            final Map<String,Map<String, Object>> targs = new HashMap<>(tparmSize);
            t = pt;
            while (t != null) {
                final Map<TypeParameter, SiteVariance> usv = t.getVarianceOverrides();
                if (t.getTypeArgumentList() != null && !t.getTypeArgumentList().isEmpty()) {
                    for (Map.Entry<TypeParameter, Type> targ : t.getTypeArguments().entrySet()) {
                        final Map<String, Object> tpmap = typeMap(targ.getValue(), from);
                        final SiteVariance variance = usv.get(targ.getKey());
                        if (variance != null) {
                            tpmap.put(MetamodelGenerator.KEY_US_VARIANCE, variance.ordinal());
                        }
                        targs.put(partiallyQualifiedName(targ.getKey().getDeclaration()) + "." + targ.getKey().getName(), tpmap);
                    }
                }
                t = t.getQualifyingType();
            }
            container.put(KEY_TYPE_ARGS, targs);
        }
    }

    public static String partiallyQualifiedName(Declaration d) {
        String qname = d.getQualifiedNameString();
        int idx = qname.indexOf("::");
        if (idx >= 0) {
            qname = qname.substring(idx+2);
        }
        return qname;
    }

    /** Create a list of maps from the list of type parameters.
     * @see #typeParameterMap(TypeParameter) */
    private List<Map<String, Object>> typeParameters(List<TypeParameter> tpl, Declaration from) {
        if (tpl != null && !tpl.isEmpty()) {
            List<Map<String, Object>> l = new ArrayList<>(tpl.size());
            for (TypeParameter tp : tpl) {
                l.add(typeParameterMap(tp, from));
            }
            return l;
        }
        return null;
    }

    /** Create a list of maps for the parameter list. Each map will be a parameter, including
     * name, type, default value (if any), and whether it's sequenced. */
    private List<Map<String,Object>> parameterListMap(ParameterList plist, Declaration from) {
        if (plist == null) {
            //Possibly an anonymous class for an anonymous object
            return null;
        }
        List<Parameter> parms = plist.getParameters();
        if (parms.size() > 0) {
            List<Map<String,Object>> p = new ArrayList<>(parms.size());
            for (Parameter parm : parms) {
                Map<String, Object> pm = new HashMap<>();
                pm.put(KEY_NAME, parm.getName());
                pm.put(KEY_METATYPE, METATYPE_PARAMETER);
                if (parm.isSequenced()) {
                    pm.put("seq", 1);
                }
                if (parm.isDefaulted()) {
                    pm.put(KEY_DEFAULT, 1);
                }
                if (parm.isAtLeastOne()) {
                    pm.put("$min1", 1);
                }
                final FunctionOrValue parmtype = parm.getModel();
                if (parmtype != null && parmtype.getDeclarationKind()==DeclarationKind.TYPE_PARAMETER) {
                    pm.put(KEY_TYPE, parmtype.getName());
                } else {
                    pm.put(KEY_TYPE, typeMap(parm.getType(), from));
                }
                if (parm.isHidden()) {
                    pm.put("$hdn", 1);
                }
                if (parmtype instanceof Function) {
                    pm.put("$pt", "f");
                    List<List<Map<String, Object>>> _paramLists = new ArrayList<>(
                            ((Function)parmtype).getParameterLists().size());
                    for (ParameterList subplist : ((Function)parmtype).getParameterLists()) {
                        List<Map<String,Object>> params = parameterListMap(subplist, from);
                        if (params == null) {
                            params = Collections.emptyList();
                        }
                        _paramLists.add(params);
                    }
                    if (_paramLists.size() > 1 || !_paramLists.get(0).isEmpty()) {
                        pm.put(KEY_PARAMS, _paramLists);
                    }
                }
                //TODO do these guys need anything else?
                /*if (parm.getDefaultArgument() != null) {
                    //This could be compiled to JS...
                    pm.put(ANN_DEFAULT, parm.getDefaultArgument().getSpecifierExpression().getExpression().getTerm().getText());
                }*/
                encodeAnnotations(parm.getModel().getAnnotations(), parm.getModel(), pm);
                p.add(pm);
            }
            return p;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public Map<String,Object> encodeMethod(Function d) {
        final Map<String, Object> m = new HashMap<>();
        m.put(KEY_METATYPE, METATYPE_METHOD);
        m.put(KEY_NAME, d.getName());
        if (d.isDynamic()) {
            m.put(KEY_DYNAMIC, 1);
        }
        List<Map<String, Object>> tpl = typeParameters(d.getTypeParameters(), d);
        if (tpl != null) {
            m.put(KEY_TYPE_PARAMS, tpl);
        }
        m.put(KEY_TYPE, typeMap(d.getType(), d));
        List<List<Map<String, Object>>> paramLists = new ArrayList<>(d.getParameterLists().size());
        for (ParameterList plist : d.getParameterLists()) {
            List<Map<String,Object>> params = parameterListMap(plist, d);
            if (params == null) {
                params = Collections.emptyList();
            }
            paramLists.add(params);
        }
        if (paramLists.size() > 1 || !paramLists.get(0).isEmpty()) {
            m.put(KEY_PARAMS, paramLists);
        }
        int ff = 0;
        if (d.isDeclaredVoid()) {
            ff |= 1;
        }
        if (d.isDeferred()) {
            ff |= 2;
        }
        if (ff > 0) {
            m.put(KEY_FLAGS, ff);
        }

        //Annotations
        encodeAnnotations(d.getAnnotations(), d, m);
        Map<String, Object> parent= findParent(d);
        if (parent != null) {
            if (parent != getPackageMap(d.getUnit().getPackage())) {
                if (!parent.containsKey(KEY_METHODS)) {
                    parent.put(KEY_METHODS, new HashMap<>());
                }
                parent = (Map<String, Object>)parent.get(KEY_METHODS);
            }
            if (parent != null) {
                parent.put(TypeUtils.modelName(d), m);
            }
        }
        return m;
    }

    @SuppressWarnings("unchecked")
    public Map<String,Object> encodeClass(org.eclipse.ceylon.model.typechecker.model.Class d) {
        final Map<String, Object> m = new HashMap<>();
        m.put(KEY_METATYPE, METATYPE_CLASS);
        m.put(KEY_NAME, TypeUtils.modelName(d));

        //Type parameters
        List<Map<String, Object>> tpl = typeParameters(d.getTypeParameters(), d);
        if (tpl != null) {
            m.put(KEY_TYPE_PARAMS, tpl);
        }
        //self type
        if (d.getSelfType() != null) {
            m.put(KEY_SELF_TYPE, d.getSelfType().getDeclaration().getName());
        }

        //Extends
        if (d.getExtendedType() != null) {
            m.put("super", typeMap(d.getExtendedType(), d));
        }
        //Satisfies
        encodeTypes(d.getSatisfiedTypes(), m, KEY_SATISFIES, d);

        //Initializer parameters
        final List<Map<String,Object>> inits = parameterListMap(d.getParameterList(), d);
        if (inits != null && !inits.isEmpty()) {
            m.put(KEY_PARAMS, inits);
        }
        //Case types
        encodeTypes(d.getCaseTypes(), m, "of", d);

        //Annotations
        encodeAnnotations(d.getAnnotations(), d, m);
        if (d.isAnonymous()) {
            m.put("$anon", 1);
        }
        if (d.isAlias()) {
            m.put("$alias", 1);
            TypeDeclaration constructor = ((ClassAlias)d).getConstructor();
            if (constructor instanceof Constructor) {
                m.put(KEY_CONSTRUCTOR, ((Constructor)constructor).getName());
            }
            // else, it's the default "constructor", and will be the (Class) d.getExtendedType().getDeclaration()
        }
        Map<String, Object> parent = findParent(d);
        if (parent != null) {
            if (parent != getPackageMap(d.getUnit().getPackage())) {
                if (!parent.containsKey(KEY_CLASSES)) {
                    parent.put(KEY_CLASSES, new HashMap<>());
                }
                parent = (Map<String,Object>)parent.get(KEY_CLASSES);
            }
            parent.put(TypeUtils.modelName(d), m);
        }
        return m;
    }

    public Map<String,Object> encodeConstructor(final Constructor d) {
        //First of all, find the class this thing belongs to
        Map<String,Object> c = findParent(d);
        final String mname = TypeUtils.modelName(d);
        if (c == null) {
            System.out.println("WTF no parent for Constructor " + d);
            return null;
        }
        Map<String,Object> m = new HashMap<>();
        if (d.getName() != null) {
            m.put(KEY_NAME, mname);
        }
        final ParameterList plist = d.getFirstParameterList();
        if (plist != null) {
            m.put(KEY_PARAMS, plist.getParameters().isEmpty() ? Collections.EMPTY_LIST : parameterListMap(plist, d));
        }
        encodeAnnotations(d.getAnnotations(), d, m);
        if (c.get(KEY_CONSTRUCTORS) == null) {
            c.put(KEY_CONSTRUCTORS, new HashMap<>());
        }
        @SuppressWarnings("unchecked")
        Map<String,Object> consmap = (Map<String,Object>)c.get(KEY_CONSTRUCTORS);
        consmap.put(d.getName() == null ? "$def" : mname, m);
        return null;
    }

    @SuppressWarnings("unchecked")
    public Map<String,Object> encodeInterface(Interface d) {
        final Map<String, Object> m = new HashMap<>();
        m.put(KEY_METATYPE, METATYPE_INTERFACE);
        m.put(KEY_NAME, d.getName());

        //Type parameters
        List<Map<String, Object>> tpl = typeParameters(d.getTypeParameters(), d);
        if (tpl != null) {
            m.put(KEY_TYPE_PARAMS, tpl);
        }
        //self type
        if (d.getSelfType() != null) {
            m.put(KEY_SELF_TYPE, d.getSelfType().getDeclaration().getName());
        }
        if (d.isDynamic()) {
            m.put(KEY_DYNAMIC, 1);
        }
        //satisfies
        encodeTypes(d.getSatisfiedTypes(), m, KEY_SATISFIES, d);
        //Case types
        encodeTypes(d.getCaseTypes(), m, "of", d);
        //Annotations
        encodeAnnotations(d.getAnnotations(), d, m);
        if (d.isAlias()) {
            m.put("$alias", typeMap(d.getExtendedType(), d));
        }
        Map<String, Object> parent = findParent(d);
        if (parent != null) {
            if (!d.isToplevel() || d.isMember()) {
                if (!parent.containsKey(KEY_INTERFACES)) {
                    parent.put(KEY_INTERFACES, new HashMap<>());
                }
                parent = (Map<String,Object>)parent.get(KEY_INTERFACES);
            }
            parent.put(TypeUtils.modelName(d), m);
        }
        return m;
    }

    @SuppressWarnings("unchecked")
    public void encodeObject(Value d) {
        Map<String, Object> parent = findParent(d);
        if (!d.isToplevel() && parent != null) {
            if (!parent.containsKey(KEY_OBJECTS)) {
                parent.put(KEY_OBJECTS, new HashMap<>());
            }
            parent = (Map<String,Object>)parent.get(KEY_OBJECTS);
        }
        Map<String, Object> m = new HashMap<>();
        m.put(KEY_METATYPE, METATYPE_OBJECT);
        m.put(KEY_NAME, d.getName());
        //Extends
        m.put("super", typeMap(d.getTypeDeclaration().getExtendedType(), d));
        //Satisfies
        encodeTypes(d.getTypeDeclaration().getSatisfiedTypes(), m, KEY_SATISFIES, d);

        //Certain annotations
        encodeAnnotations(d.getAnnotations(), d, m);
        if (parent != null) {
            parent.put(TypeUtils.modelName(d.getTypeDeclaration()), m);
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> encodeAttributeOrGetter(FunctionOrValue d) {
        Map<String, Object> m = new HashMap<>();
        Map<String, Object> parent;
        final String mname = TypeUtils.modelName(d);
        if (d.isToplevel() || d.isMember() || containsTypes(d)) {
            parent = findParent(d);
            if (parent == null) {
                return null;
            }
            if (parent != getPackageMap(d.getUnit().getPackage())) {
                final String _k = KEY_ATTRIBUTES;
                if (!parent.containsKey(_k)) {
                    parent.put(_k, new HashMap<>());
                }
                parent = (Map<String,Object>)parent.get(_k);
            }
            if (parent.containsKey(mname)) {
                //merge existing
                m = (Map<String, Object>)parent.get(mname);
            }
        } else {
            //Ignore attributes inside control blocks, methods, etc.
            return null;
        }
        m.put(KEY_NAME, d.getName());
        m.put(KEY_METATYPE, (d instanceof Value && d.isTransient()) ? METATYPE_GETTER : METATYPE_ATTRIBUTE);
        m.put(KEY_TYPE, typeMap(d.getType(), d));
        if (d.isDynamic()) {
            m.put(KEY_DYNAMIC, 1);
        }
        parent.put(mname, m);
        encodeAnnotations(d.getAnnotations(), d, m);
        if (d instanceof Value && ((Value) d).getSetter() != null) {
            Map<String,Object> smap = (Map<String,Object>)m.get("$set");
            if (smap==null) {
                smap = new HashMap<>();
                m.put("$set", smap);
                smap.put(KEY_METATYPE, METATYPE_SETTER);
                encodeAnnotations(((Value)d).getSetter().getAnnotations(), ((Value)d).getSetter(), smap);
            }
        }
        return m;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> encodeTypeAlias(TypeAlias d) {
        Map<String, Object> parent;
        if (d.isToplevel() || d.isMember()) {
            parent = findParent(d);
            if (parent == null) {
                return null;
            }
            if (!d.isToplevel()) {
                if (!parent.containsKey(KEY_ATTRIBUTES)) {
                    parent.put(KEY_ATTRIBUTES, new HashMap<>());
                }
                parent = (Map<String,Object>)parent.get(KEY_ATTRIBUTES);
            }
        } else {
            //Ignore attributes inside control blocks, methods, etc.
            return null;
        }
        Map<String, Object> m = new HashMap<>();
        m.put(KEY_METATYPE, METATYPE_ALIAS);
        m.put(KEY_NAME, d.getName());
        List<Map<String, Object>> tpl = typeParameters(d.getTypeParameters(), d);
        if (tpl != null) {
            m.put(KEY_TYPE_PARAMS, tpl);
        }
        if (d.getSelfType() != null) {
            m.put(KEY_SELF_TYPE, d.getSelfType().getDeclaration().getName());
        }
        m.put("$alias", typeMap(d.getExtendedType(), d));
        encodeTypes(d.getCaseTypes(), m, "of", d);
        encodeTypes(d.getSatisfiedTypes(), m, KEY_SATISFIES, d);
        encodeAnnotations(d.getAnnotations(), d, m);
        parent.put(TypeUtils.modelName(d), m);
        return m;
    }

    /** Encodes the list of types and puts them under the specified key in the map. */
    private void encodeTypes(List<Type> types, Map<String,Object> m, String key, Declaration from) {
        if (types == null || types.isEmpty()) return;
        List<Map<String, Object>> sats = new ArrayList<>(types.size());
        for (Type st : types) {
            sats.add(typeMap(st, from));
        }
        m.put(key, sats);
    }

    /** Encodes all annotations as a map which is then stored under the
     * {@link #KEY_ANNOTATIONS} key in the specified map.
     * If the map is null, only the bitset annotations are calculated and returned.
     * @return The bitmask for the bitset annotations. */
    public static int encodeAnnotations(List<Annotation> annotations, Object d, Map<String, Object> m) {
        List<Map<String, List<String>>> anns = m == null ? null : new ArrayList<Map<String, List<String>>>(annotations.size());
        int bits = 0;
        for (Annotation a : annotations) {
            String name = a.getName();
            int idx = "native".equals(name) ? -1 : annotationBits.indexOf(name);
            if (idx >= 0) {
                bits |= (1 << idx);
            } else if (anns != null) {
                List<String> args = a.getPositionalArguments();
                if (args == null) {
                    args = Collections.emptyList();
                }
                anns.add(Collections.singletonMap(name, args));
            }
        }
        if (d instanceof Value && ((Value)d).isVariable()) {
            //Sometimes the value is not annotated, it only has a defined Setter
            bits |= (1 << annotationBits.indexOf("variable"));
        } else if (d instanceof org.eclipse.ceylon.model.typechecker.model.Class
                && ((org.eclipse.ceylon.model.typechecker.model.Class)d).isAbstract()) {
            bits |= (1 << annotationBits.indexOf("abstract"));
        } else if (d instanceof Constructor && ((Constructor)d).isAbstract()) {
            bits |= (1 << annotationBits.indexOf("abstract"));
        }
        if (bits > 0 && m != null) {
            String key = d instanceof Module ? "$mod-pa" :
                d instanceof org.eclipse.ceylon.model.typechecker.model.Package ? "$pkg-pa" : KEY_PACKED_ANNS;
            m.put(key, bits);
        }
        if (anns != null && m != null && !anns.isEmpty()) {
            String key = d instanceof Module ? "$mod-anns" :
                d instanceof org.eclipse.ceylon.model.typechecker.model.Package ? "$pkg-anns" : KEY_ANNOTATIONS;
            m.put(key, anns);
        }
        return bits;
    }

    private void addPackage(final Map<String,Object> map, final String pkgName) {
        if (pkgName.equals(LANGUAGE_MODULE_NAME)) {
            map.put(KEY_PACKAGE, "$");
        } else {
            map.put(KEY_PACKAGE, pkgName);
        }
    }

    public boolean containsTypes(Declaration d) {
        for (Declaration m : d.getMembers()) {
            if (m instanceof Value && ((Value)m).getTypeDeclaration().isAnonymous())return true;
            if (m instanceof TypeDeclaration || containsTypes(m)) {
                return true;
            }
        }
        return false;
    }

    public Map<String,Object> getPackageMap(org.eclipse.ceylon.model.typechecker.model.Package p) {
        @SuppressWarnings("unchecked")
        Map<String,Object> pkgmap = (Map<String,Object>)model.get(p.getNameAsString());
        if (pkgmap == null) {
            pkgmap = new HashMap<>();
            encodeAnnotations(p.getAnnotations(), p, pkgmap);
            model.put(p.getNameAsString(), pkgmap);
        }
        return pkgmap;
    }

}
