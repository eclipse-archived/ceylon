package com.redhat.ceylon.compiler.loader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.common.Versions;
import com.redhat.ceylon.compiler.js.TypeUtils;
import com.redhat.ceylon.compiler.typechecker.model.Annotation;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.DeclarationKind;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.IntersectionType;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.MethodOrValue;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.ModuleImport;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ParameterList;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.TypeAlias;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.UnionType;
import com.redhat.ceylon.compiler.typechecker.model.UnknownType;
import com.redhat.ceylon.compiler.typechecker.model.Util;
import com.redhat.ceylon.compiler.typechecker.model.Value;

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
    public static final String KEY_ANNOTATIONS  = "$an";
    public static final String KEY_TYPE         = "$t";
    public static final String KEY_TYPES        = "$ts";
    public static final String KEY_TYPE_PARAMS  = "$tp";
    public static final String KEY_METATYPE     = "$mt";
    public static final String KEY_MODULE       = "$md";
    public static final String KEY_NAME         = "$nm";
    public static final String KEY_PACKAGE      = "$pk";
    public static final String KEY_PARAMS       = "$ps";
    public static final String KEY_SELF_TYPE    = "$st";
    public static final String KEY_IS_ANNOTATION= "$annot";
    public static final String KEY_SATISFIES    = "$sts";

    public static final String KEY_DEFAULT      = "$def";

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

    private final Map<String, Object> model = new HashMap<>();
    private final Module module;

    public MetamodelGenerator(Module module) {
        this.module = module;
        model.put("$mod-name", module.getNameAsString());
        model.put("$mod-version", module.getVersion());
        model.put("$mod-bin", Versions.JS_BINARY_MAJOR_VERSION+"."+Versions.JS_BINARY_MINOR_VERSION);
        if (!module.getImports().isEmpty()) {
            ArrayList<Object> imps = new ArrayList<>(module.getImports().size());
            for (ModuleImport mi : module.getImports()) {
                String impath = String.format("%s/%s", mi.getModule().getNameAsString(), mi.getModule().getVersion());
                if (mi.isOptional()) {
                    Map<String,Object> optimp = new HashMap<>(2);
                    optimp.put("path",impath);
                    optimp.put("opt",1);
                    imps.add(optimp);
                } else {
                    imps.add(impath);
                }
            }
            model.put("$mod-deps", imps);
        }
    }

    /** Returns the in-memory model as a collection of maps.
     * The top-level map represents the module. */
    public Map<String, Object> getModel() {
        return Collections.unmodifiableMap(model);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> findParent(Declaration d) {
        Map<String,Object> pkgmap = (Map<String,Object>)model.get(d.getUnit().getPackage().getNameAsString());
        if (pkgmap == null) {
            pkgmap = new HashMap<>();
            if (d.getUnit().getPackage().isShared()) {
                pkgmap.put("$pkg-shared", 1);
            }
            model.put(d.getUnit().getPackage().getNameAsString(), pkgmap);
        }
        if (d.isToplevel()) {
            return pkgmap;
        }
        List<String> names = TypeUtils.generateModelPath(Util.getContainingDeclaration(d));
        names.remove(0); //we don't need the package key
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

    /** Create a map for the specified ProducedType.
     * Includes name, package, module and type parameters, unless it's a union or intersection
     * type, in which case it contains a "comp" key with an "i" or "u" and a key "types" with
     * the list of types that compose it. */
    private Map<String, Object> typeMap(ProducedType pt, Declaration from) {
        if (pt==null) {
            pt = new UnknownType(module.getUnit()).getType();
        }
        TypeDeclaration d = pt.getDeclaration();
        Map<String, Object> m = new HashMap<>();
        if (d instanceof UnionType || d instanceof IntersectionType) {
            List<ProducedType> subtipos = d instanceof UnionType ? d.getCaseTypes() : d.getSatisfiedTypes();
            List<Map<String,Object>> subs = new ArrayList<>(subtipos.size());
            for (ProducedType sub : subtipos) {
                subs.add(typeMap(sub, from));
            }
            m.put("comp", d instanceof UnionType ? "u" : "i");
            m.put(KEY_TYPES, subs);
            return m;
        }
        if (d.isToplevel() || d instanceof TypeParameter) {
            m.put(KEY_NAME, d.getName());
        } else {
            final String qn = d.getQualifiedNameString();
            int p0 = qn.indexOf("::");
            m.put(KEY_NAME, p0>=0 ? qn.substring(p0+2) : qn);
        }
        if (d.getDeclarationKind()==DeclarationKind.TYPE_PARAMETER) {
            //For types that reference type parameters, we're done
            return m;
        }
        com.redhat.ceylon.compiler.typechecker.model.Package pkg = d.getUnit().getPackage();
        if (pkg.equals(from.getUnit().getPackage())) {
            addPackage(m, ".");
        } else {
            addPackage(m, pkg.getNameAsString());
        }
        if (!pkg.getModule().equals(module)) {
            m.put(KEY_MODULE, pkg.getModule().getNameAsString());
        }
        putTypeParameters(m, pt, from);
        return m;
    }

    /** Returns a map with the same info as {@link #typeParameterMap(ProducedType)} but with
     * an additional key "variance" if it's covariant ("out") or contravariant ("in"). */
    private Map<String, Object> typeParameterMap(TypeParameter tp, Declaration from) {
        Map<String, Object> map = new HashMap<>();
        map.put(MetamodelGenerator.KEY_NAME, tp.getName());
        if (tp.isCovariant()) {
            map.put("variance", "out");
        } else if (tp.isContravariant()) {
            map.put("variance", "in");
        }
        if (tp.getSelfType() != null) {
            map.put(KEY_SELF_TYPE, typeMap(tp.getSelfType(), from));
        }
        if (tp.getSatisfiedTypes() != null && !tp.getSatisfiedTypes().isEmpty()) {
            encodeTypes(tp.getSatisfiedTypes(), map, KEY_SATISFIES, from);
        } else if (tp.getCaseTypes() != null && !tp.getCaseTypes().isEmpty()) {
            encodeTypes(tp.getCaseTypes(), map, "of", from);
        }
        if (tp.getDefaultTypeArgument() != null) {
            map.put(KEY_DEFAULT, typeMap(tp.getDefaultTypeArgument(), from));
        }
        return map;
    }

    /** Create a map for the ProducedType, as a type parameter.
     * Includes name, package, module and type parameters, unless it's a union or intersection
     * type, in which case it will contain a "comp" key with an "i" or "u", and a list of the types
     * that compose it. */
    private Map<String, Object> typeParameterMap(ProducedType pt, Declaration from) {
        final Map<String, Object> m = new HashMap<>();
        final TypeDeclaration d = pt.getDeclaration();
        m.put(KEY_METATYPE, METATYPE_TYPE_PARAMETER);
        if (d instanceof UnionType || d instanceof IntersectionType) {
            List<ProducedType> subtipos = d instanceof UnionType ? d.getCaseTypes() : d.getSatisfiedTypes();
            List<Map<String,Object>> subs = new ArrayList<>(subtipos.size());
            for (ProducedType sub : subtipos) {
                subs.add(typeMap(sub, from));
            }
            m.put("comp", d instanceof UnionType ? "u" : "i");
            m.put(KEY_TYPES, subs);
            return m;
        }
        m.put(KEY_NAME, d.getName());
        if (d.getDeclarationKind()==DeclarationKind.TYPE_PARAMETER) {
            //Don't add package, etc
            return m;
        }
        com.redhat.ceylon.compiler.typechecker.model.Package pkg = d.getUnit().getPackage();
        if (pkg.equals(from.getUnit().getPackage())) {
            addPackage(m, ".");
        } else {
            addPackage(m, pkg.getNameAsString());
        }
        if (!pkg.getModule().equals(module)) {
            m.put(KEY_MODULE, d.getUnit().getPackage().getModule().getNameAsString());
        }
        putTypeParameters(m, pt, from);
        return m;
    }

    private void putTypeParameters(Map<String, Object> container, ProducedType pt, Declaration from) {
        if (pt.getTypeArgumentList() != null && !pt.getTypeArgumentList().isEmpty()) {
            List<Map<String, Object>> list = new ArrayList<>(pt.getTypeArgumentList().size());
            for (ProducedType tparm : pt.getTypeArgumentList()) {
                list.add(typeParameterMap(tparm, from));
            }
            container.put(KEY_TYPE_PARAMS, list);
        }
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
                final MethodOrValue parmtype = parm.getModel();
                if (parmtype != null && parmtype.getDeclarationKind()==DeclarationKind.TYPE_PARAMETER) {
                    pm.put(KEY_TYPE, parmtype.getName());
                } else if (parm.getType() != null) {
                    pm.put(KEY_TYPE, typeMap(parm.getType(), from));
                } else {
                    //Most likely a dynamic type, set it to Anything
                    pm.put(KEY_TYPE, typeMap(((TypeDeclaration)module.getLanguageModule().getDirectPackage(
                            "ceylon.language").getDirectMember("Anything", null, false)).getType(), from));
                }
                if (parm.isHidden()) {
                    pm.put("$hdn", 1);
                }
                if (parmtype instanceof Method) {
                    pm.put("$pt", "f");
                    List<List<Map<String, Object>>> _paramLists = new ArrayList<>(
                            ((Method)parmtype).getParameterLists().size());
                    for (ParameterList subplist : ((Method)parmtype).getParameterLists()) {
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
                encodeAnnotations(parm.getModel(), pm);
                p.add(pm);
            }
            return p;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public Map<String,Object> encodeMethod(Method d) {
        final Map<String, Object> m = new HashMap<>();
        m.put(KEY_METATYPE, METATYPE_METHOD);
        m.put(KEY_NAME, d.getName());
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

        //Annotations
        encodeAnnotations(d, m);
        Map<String, Object> parent= findParent(d);
        if (parent != null) {
            if (!d.isToplevel()) {
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

    /** Create and store the metamodel info for an attribute. */
    public void encodeAttribute(Value d) {
        Map<String, Object> m = encodeAttributeOrGetter(d);
        if (m != null && d.isVariable()) {
            m.put("var", 1);
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String,Object> encodeClass(com.redhat.ceylon.compiler.typechecker.model.Class d) {
        final Map<String, Object> m = new HashMap<>();
        m.put(KEY_METATYPE, METATYPE_CLASS);
        m.put(KEY_NAME, d.getName());

        //Type parameters
        List<Map<String, Object>> tpl = typeParameters(d.getTypeParameters(), d);
        if (tpl != null) {
            m.put(KEY_TYPE_PARAMS, tpl);
        }
        //self type
        if (d.getSelfType() != null) {
            m.put(KEY_SELF_TYPE, typeMap(d.getSelfType(), d));
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
        encodeAnnotations(d, m);
        if (d.isAbstract()) {
            m.put("abstract", 1);
        }
        if (d.isAnonymous()) {
            m.put("$anon", 1);
        }
        if (d.isAlias()) {
            m.put("$alias", 1);
        }
        Map<String, Object> parent = findParent(d);
        if (parent != null) {
            if (!d.isToplevel() || d.isMember()) {
                if (!parent.containsKey(KEY_CLASSES)) {
                    parent.put(KEY_CLASSES, new HashMap<>());
                }
                parent = (Map<String,Object>)parent.get(KEY_CLASSES);
            }
            parent.put(TypeUtils.modelName(d), m);
        }
        return m;
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
        //satisfies
        encodeTypes(d.getSatisfiedTypes(), m, KEY_SATISFIES, d);
        //Case types
        encodeTypes(d.getCaseTypes(), m, "of", d);
        //Annotations
        encodeAnnotations(d, m);
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
        if (!d.isToplevel()) {
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
        encodeAnnotations(d, m);
        parent.put(TypeUtils.modelName(d.getTypeDeclaration()), m);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> encodeAttributeOrGetter(MethodOrValue d) {
        Map<String, Object> m = new HashMap<>();
        Map<String, Object> parent;
        final String mname = TypeUtils.modelName(d);
        if (d.isToplevel() || d.isMember() || containsTypes(d)) {
            parent = findParent(d);
            if (parent == null) {
                return null;
            }
            if (!d.isToplevel()) {
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
        m.put(KEY_METATYPE, (d instanceof Value && ((Value)d).isTransient()) ? METATYPE_GETTER : METATYPE_ATTRIBUTE);
        m.put(KEY_TYPE, typeMap(d.getType(), d));
        parent.put(mname, m);
        encodeAnnotations(d, m);
        if (d instanceof Value && ((Value) d).getSetter() != null) {
            Map<String,Object> smap = (Map<String,Object>)m.get("$set");
            if (smap==null) {
                smap = new HashMap<>();
                m.put("$set", smap);
                smap.put(KEY_METATYPE, METATYPE_SETTER);
                encodeAnnotations(((Value)d).getSetter(), smap);
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
            m.put(KEY_SELF_TYPE, typeMap(d.getSelfType(), d));
        }
        m.put("$alias", typeMap(d.getExtendedType(), d));
        encodeTypes(d.getCaseTypes(), m, "of", d);
        encodeTypes(d.getSatisfiedTypes(), m, KEY_SATISFIES, d);
        encodeAnnotations(d, m);
        parent.put(TypeUtils.modelName(d), m);
        return m;
    }

    /** Encodes the list of types and puts them under the specified key in the map. */
    private void encodeTypes(List<ProducedType> types, Map<String,Object> m, String key, Declaration from) {
        if (types == null || types.isEmpty()) return;
        List<Map<String, Object>> sats = new ArrayList<>(types.size());
        for (ProducedType st : types) {
            sats.add(typeMap(st, from));
        }
        m.put(key, sats);
    }

    /** Encodes all annotations as a map which is then stored under the
     * {@link #KEY_ANNOTATIONS} key in the specified map. */
    private void encodeAnnotations(Declaration d, Map<String, Object> m) {
        HashMap<String, List<String>> anns = new HashMap<>();
        for (Annotation a : d.getAnnotations()) {
            String name = a.getName();
            List<String> args = a.getPositionalArguments();
            if (args == null) {
                args = Collections.emptyList();
            }
            anns.put(name, args);
        }
        if (d.isAnnotation()) {
            m.put(KEY_IS_ANNOTATION, 1);
        }
        if (!anns.isEmpty()) {
            m.put(KEY_ANNOTATIONS, anns);
        }
    }

    private void addPackage(final Map<String,Object> map, final String pkgName) {
        if (pkgName.equals("ceylon.language")) {
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

}
