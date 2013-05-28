package com.redhat.ceylon.compiler.loader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.compiler.typechecker.model.Annotation;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.DeclarationKind;
import com.redhat.ceylon.compiler.typechecker.model.FunctionalParameter;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.IntersectionType;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.MethodOrValue;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.ModuleImport;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ParameterList;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.TypeAlias;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.UnionType;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.model.ValueParameter;

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

    public static final String KEY_DEFAULT      = "$def";

    public static final String METATYPE_CLASS           = "cls";
    public static final String METATYPE_INTERFACE       = "ifc";
    public static final String METATYPE_ALIAS           = "als";
    public static final String METATYPE_OBJECT          = "obj";
    public static final String METATYPE_METHOD          = "mthd";
    public static final String METATYPE_ATTRIBUTE       = "attr";
    public static final String METATYPE_GETTER          = "gttr";
    public static final String METATYPE_TYPE_PARAMETER  = "tpm";
    public static final String METATYPE_PARAMETER       = "prm";
    //public static final String METATYPE_TYPE_CONSTRAINT = "tc";

    private final Map<String, Object> model = new HashMap<String, Object>();
    private final Module module;

    public MetamodelGenerator(Module module) {
        this.module = module;
        model.put("$mod-name", module.getNameAsString());
        model.put("$mod-version", module.getVersion());
        if (!module.getImports().isEmpty()) {
            ArrayList<String> imps = new ArrayList<String>(module.getImports().size());
            for (ModuleImport mi : module.getImports()) {
                imps.add(String.format("%s/%s", mi.getModule().getNameAsString(), mi.getModule().getVersion()));
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
            pkgmap = new HashMap<String, Object>();
            if (d.getUnit().getPackage().isShared()) {
                pkgmap.put("$pkg-shared", "1");
            }
            model.put(d.getUnit().getPackage().getNameAsString(), pkgmap);
        }
        if (d.isToplevel()) {
            return pkgmap;
        }
        ArrayList<String> names = new ArrayList<String>();
        Scope sc = d.getContainer();
        while (sc.getContainer() != null) {
            if (sc instanceof TypeDeclaration) {
                names.add(0, ((TypeDeclaration) sc).getName());
            }
            sc = sc.getContainer();
        }
        Map<String, Object> last = pkgmap;
        for (String name : names) {
            if (last == null) {
                break;
            } else if (last == pkgmap) {
                last = (Map<String, Object>)last.get(name);
            } else if (last.containsKey(KEY_METHODS) && ((Map<String,Object>)last.get(KEY_METHODS)).containsKey(name)) {
                last = (Map<String,Object>)((Map<String,Object>)last.get(KEY_METHODS)).get(name);
            } else if (last.containsKey(KEY_ATTRIBUTES) && ((Map<String,Object>)last.get(KEY_ATTRIBUTES)).containsKey(name)) {
                last = (Map<String,Object>)((Map<String,Object>)last.get(KEY_ATTRIBUTES)).get(name);
            } else if (last.containsKey(KEY_CLASSES) && ((Map<String,Object>)last.get(KEY_CLASSES)).containsKey(name)) {
                last = (Map<String,Object>)((Map<String,Object>)last.get(KEY_CLASSES)).get(name);
            } else if (last.containsKey(KEY_INTERFACES) && ((Map<String,Object>)last.get(KEY_INTERFACES)).containsKey(name)) {
                last = (Map<String,Object>)((Map<String,Object>)last.get(KEY_INTERFACES)).get(name);
            } else if (last.containsKey(KEY_OBJECTS) && ((Map<String,Object>)last.get(KEY_OBJECTS)).containsKey(name)) {
                last = (Map<String,Object>)((Map<String,Object>)last.get(KEY_OBJECTS)).get(name);
            }
        }
        return last;
    }

    /** Create a map for the specified ProducedType.
     * Includes name, package, module and type parameters, unless it's a union or intersection
     * type, in which case it contains a "comp" key with an "i" or "u" and a key "types" with
     * the list of types that compose it. */
    private Map<String, Object> typeMap(ProducedType pt) {
        TypeDeclaration d = pt.getDeclaration();
        Map<String, Object> m = new HashMap<String, Object>();
        if (d instanceof UnionType || d instanceof IntersectionType) {
            List<ProducedType> subtipos = d instanceof UnionType ? d.getCaseTypes() : d.getSatisfiedTypes();
            List<Map<String,Object>> subs = new ArrayList<Map<String,Object>>(subtipos.size());
            for (ProducedType sub : subtipos) {
                subs.add(typeMap(sub));
            }
            m.put("comp", d instanceof UnionType ? "u" : "i");
            m.put(KEY_TYPES, subs);
            return m;
        }
        m.put(KEY_NAME, d.getName());
        if (d.getDeclarationKind()==DeclarationKind.TYPE_PARAMETER) {
            //For types that reference type parameters, we're done
            return m;
        }
        com.redhat.ceylon.compiler.typechecker.model.Package pkg = d.getUnit().getPackage();
        m.put(KEY_PACKAGE, pkg.getNameAsString());
        if (!pkg.getModule().equals(module)) {
            m.put(KEY_MODULE, pkg.getModule().getNameAsString());
        }
        putTypeParameters(m, pt);
        return m;
    }

    /** Returns a map with the same info as {@link #typeParameterMap(ProducedType)} but with
     * an additional key "variance" if it's covariant ("out") or contravariant ("in"). */
    private Map<String, Object> typeParameterMap(TypeParameter tp) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(MetamodelGenerator.KEY_NAME, tp.getName());
        if (tp.isCovariant()) {
            map.put("variance", "out");
        } else if (tp.isContravariant()) {
            map.put("variance", "in");
        }
        if (tp.getSelfType() != null) {
            map.put(KEY_SELF_TYPE, typeMap(tp.getSelfType()));
        }
        if (tp.getSatisfiedTypes() != null && !tp.getSatisfiedTypes().isEmpty()) {
            encodeTypes(tp.getSatisfiedTypes(), map, "satisfies");
        } else if (tp.getCaseTypes() != null && !tp.getCaseTypes().isEmpty()) {
            encodeTypes(tp.getCaseTypes(), map, "of");
        }
        if (tp.getDefaultTypeArgument() != null) {
            map.put(KEY_DEFAULT, typeMap(tp.getDefaultTypeArgument()));
        }
        return map;
    }

    /** Create a map for the ProducedType, as a type parameter.
     * Includes name, package, module and type parameters, unless it's a union or intersection
     * type, in which case it will contain a "comp" key with an "i" or "u", and a list of the types
     * that compose it. */
    private Map<String, Object> typeParameterMap(ProducedType pt) {
        Map<String, Object> m = new HashMap<String, Object>();
        TypeDeclaration d = pt.getDeclaration();
        m.put(KEY_METATYPE, METATYPE_TYPE_PARAMETER);
        if (d instanceof UnionType || d instanceof IntersectionType) {
            List<ProducedType> subtipos = d instanceof UnionType ? d.getCaseTypes() : d.getSatisfiedTypes();
            List<Map<String,Object>> subs = new ArrayList<Map<String,Object>>(subtipos.size());
            for (ProducedType sub : subtipos) {
                subs.add(typeMap(sub));
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
        m.put(KEY_PACKAGE, pkg.getNameAsString());
        if (!pkg.getModule().equals(module)) {
            m.put(KEY_MODULE, d.getUnit().getPackage().getModule().getNameAsString());
        }
        putTypeParameters(m, pt);
        return m;
    }

    private void putTypeParameters(Map<String, Object> container, ProducedType pt) {
        if (pt.getTypeArgumentList() != null && !pt.getTypeArgumentList().isEmpty()) {
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(pt.getTypeArgumentList().size());
            for (ProducedType tparm : pt.getTypeArgumentList()) {
                list.add(typeParameterMap(tparm));
            }
            container.put(KEY_TYPE_PARAMS, list);
        }
    }

    /** Create a list of maps from the list of type parameters.
     * @see #typeParameterMap(TypeParameter) */
    private List<Map<String, Object>> typeParameters(List<TypeParameter> tpl) {
        if (tpl != null && !tpl.isEmpty()) {
            List<Map<String, Object>> l = new ArrayList<Map<String,Object>>(tpl.size());
            for (TypeParameter tp : tpl) {
                l.add(typeParameterMap(tp));
            }
            return l;
        }
        return null;
    }

    /** Create a list of maps for the parameter list. Each map will be a parameter, including
     * name, type, default value (if any), and whether it's sequenced. */
    private List<Map<String,Object>> parameterListMap(ParameterList plist) {
        List<Parameter> parms = plist.getParameters();
        if (parms.size() > 0) {
            List<Map<String,Object>> p = new ArrayList<Map<String,Object>>(parms.size());
            for (Parameter parm : parms) {
                Map<String, Object> pm = new HashMap<String, Object>();
                pm.put(KEY_NAME, parm.getName());
                pm.put(KEY_METATYPE, METATYPE_PARAMETER);
                if (parm.isSequenced()) {
                    pm.put("seq", "1");
                }
                if (parm.isDefaulted()) {
                    pm.put(KEY_DEFAULT, "1");
                }
                if (parm.getTypeDeclaration().getDeclarationKind()==DeclarationKind.TYPE_PARAMETER) {
                    pm.put(KEY_TYPE, parm.getTypeDeclaration().getName());
                } else {
                    pm.put(KEY_TYPE, typeMap(parm.getType()));
                }
                if (parm instanceof ValueParameter) {
                    pm.put("$pt", "v");
                    if (((ValueParameter) parm).isHidden()) {
                        pm.put("$hdn", "1");
                    }
                } else if (parm instanceof FunctionalParameter) {
                    pm.put("$pt", "f");
                    List<List<Map<String, Object>>> _paramLists = new ArrayList<List<Map<String,Object>>>(
                            ((FunctionalParameter)parm).getParameterLists().size());
                    for (ParameterList subplist : ((FunctionalParameter)parm).getParameterLists()) {
                        List<Map<String,Object>> params = parameterListMap(subplist);
                        if (params == null) {
                            params = Collections.emptyList();
                        }
                        _paramLists.add(params);
                    }
                    if (_paramLists.size() > 1 || !_paramLists.get(0).isEmpty()) {
                        pm.put(KEY_PARAMS, _paramLists);
                    }
                } else {
                    throw new IllegalStateException("Unknown parameter type " + parm.getClass().getName());
                }
                //TODO do these guys need anything else?
                /*if (parm.getDefaultArgument() != null) {
                    //This could be compiled to JS...
                    pm.put(ANN_DEFAULT, parm.getDefaultArgument().getSpecifierExpression().getExpression().getTerm().getText());
                }*/
                p.add(pm);
            }
            return p;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public Map<String,Object> encodeMethod(Method d) {
        final Map<String, Object> m = new HashMap<String, Object>();
        m.put(KEY_METATYPE, METATYPE_METHOD);
        m.put(KEY_NAME, d.getName());
        List<Map<String, Object>> tpl = typeParameters(d.getTypeParameters());
        if (tpl != null) {
            m.put(KEY_TYPE_PARAMS, tpl);
        }
        m.put(KEY_TYPE, typeMap(d.getType()));
        List<List<Map<String, Object>>> paramLists = new ArrayList<List<Map<String,Object>>>(d.getParameterLists().size());
        for (ParameterList plist : d.getParameterLists()) {
            List<Map<String,Object>> params = parameterListMap(plist);
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
        Map<String, Object> parent;
        if (d.isToplevel() || d.isMember()) {
            parent = findParent(d);
            if (parent != null) {
                if (!d.isToplevel()) {
                    if (!parent.containsKey(KEY_METHODS)) {
                        parent.put(KEY_METHODS, new HashMap<String,Object>());
                    }
                    parent = (Map<String, Object>)parent.get(KEY_METHODS);
                }
                if (parent != null) {
                    parent.put(d.getName(), m);
                }
            }
        }
        return m;
    }

    /** Create and store the metamodel info for an attribute. */
    public void encodeAttribute(Value d) {
        Map<String, Object> m = encodeAttributeOrGetter(d);
        if (m != null && d.isVariable()) {
            m.put("var", "1");
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String,Object> encodeClass(com.redhat.ceylon.compiler.typechecker.model.Class d) {
        final Map<String, Object> m = new HashMap<String, Object>();
        m.put(KEY_METATYPE, METATYPE_CLASS);
        m.put(KEY_NAME, d.getName());

        //Type parameters
        List<Map<String, Object>> tpl = typeParameters(d.getTypeParameters());
        if (tpl != null) {
            m.put(KEY_TYPE_PARAMS, tpl);
        }
        //self type
        if (d.getSelfType() != null) {
            m.put(KEY_SELF_TYPE, typeMap(d.getSelfType()));
        }

        //Extends
        if (d.getExtendedType() != null) {
            m.put("super", typeMap(d.getExtendedType()));
        }
        //Satisfies
        encodeTypes(d.getSatisfiedTypes(), m, "satisfies");

        //Initializer parameters
        List<Map<String,Object>> inits = parameterListMap(d.getParameterList());
        if (inits != null && !inits.isEmpty()) {
            m.put(KEY_PARAMS, inits);
        }
        //Case types
        encodeTypes(d.getCaseTypes(), m, "of");

        //Annotations
        encodeAnnotations(d, m);
        if (d.isAbstract()) {
            m.put("abstract", "1");
        }
        if (d.isAnonymous()) {
            m.put("$anon", "1");
        }
        if (d.isAlias()) {
            m.put("$alias", "1");
        }
        Map<String, Object> parent = findParent(d);
        if (parent != null) {
            if (!d.isToplevel() || d.isMember()) {
                if (!parent.containsKey(KEY_CLASSES)) {
                    parent.put(KEY_CLASSES, new HashMap<String,Object>());
                }
                parent = (Map<String,Object>)parent.get(KEY_CLASSES);
            }
            parent.put(d.getName(), m);
        }
        return m;
    }

    @SuppressWarnings("unchecked")
    public Map<String,Object> encodeInterface(Interface d) {
        final Map<String, Object> m = new HashMap<String, Object>();
        m.put(KEY_METATYPE, METATYPE_INTERFACE);
        m.put(KEY_NAME, d.getName());

        //Type parameters
        List<Map<String, Object>> tpl = typeParameters(d.getTypeParameters());
        if (tpl != null) {
            m.put(KEY_TYPE_PARAMS, tpl);
        }
        //self type
        if (d.getSelfType() != null) {
            m.put(KEY_SELF_TYPE, d.getSelfType().getDeclaration().getName());
        }
        //satisfies
        encodeTypes(d.getSatisfiedTypes(), m, "satisfies");
        //Case types
        encodeTypes(d.getCaseTypes(), m, "of");
        //Annotations
        encodeAnnotations(d, m);
        if (d.isAlias()) {
            m.put("$alias", typeMap(d.getExtendedType()));
        }
        Map<String, Object> parent = findParent(d);
        if (parent != null) {
            if (!d.isToplevel() || d.isMember()) {
                if (!parent.containsKey(KEY_INTERFACES)) {
                    parent.put(KEY_INTERFACES, new HashMap<String,Object>());
                }
                parent = (Map<String,Object>)parent.get(KEY_INTERFACES);
            }
            parent.put(d.getName(), m);
        }
        return m;
    }

    @SuppressWarnings("unchecked")
    public void encodeObject(Value d) {
        Map<String, Object> parent = findParent(d);
        if (d.isMember()) {
            if (!parent.containsKey(KEY_OBJECTS)) {
                parent.put(KEY_OBJECTS, new HashMap<String, Object>());
            }
            parent = (Map<String,Object>)parent.get(KEY_OBJECTS);
        } else if (!d.isToplevel()) {
            return;
        }
        Map<String, Object> m = new HashMap<String, Object>();
        m.put(KEY_METATYPE, METATYPE_OBJECT);
        m.put(KEY_NAME, d.getName());
        //Extends
        m.put("super", typeMap(d.getTypeDeclaration().getExtendedType()));
        //Satisfies
        encodeTypes(d.getTypeDeclaration().getSatisfiedTypes(), m, "satisfies");

        //Certain annotations
        encodeAnnotations(d, m);
        parent.put(d.getName(), m);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> encodeAttributeOrGetter(MethodOrValue d) {
        Map<String, Object> m = new HashMap<String, Object>();
        Map<String, Object> parent;
        if (d.isToplevel() || d.isMember()) {
            parent = findParent(d);
            if (parent == null) {
                return null;
            }
            if (!d.isToplevel()) {
                if (!parent.containsKey(KEY_ATTRIBUTES)) {
                    parent.put(KEY_ATTRIBUTES, new HashMap<String, Object>());
                }
                parent = (Map<String,Object>)parent.get(KEY_ATTRIBUTES);
            }
        } else {
            //Ignore attributes inside control blocks, methods, etc.
            return null;
        }
        m.put(KEY_NAME, d.getName());
        m.put(KEY_METATYPE, (d instanceof Value && ((Value)d).isTransient()) ? METATYPE_GETTER : METATYPE_ATTRIBUTE);
        m.put(KEY_TYPE, typeMap(d.getType()));
        encodeAnnotations(d, m);
        parent.put(d.getName(), m);
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
                    parent.put(KEY_ATTRIBUTES, new HashMap<String, Object>());
                }
                parent = (Map<String,Object>)parent.get(KEY_ATTRIBUTES);
            }
        } else {
            //Ignore attributes inside control blocks, methods, etc.
            return null;
        }
        Map<String, Object> m = new HashMap<String, Object>();
        m.put(KEY_METATYPE, METATYPE_ALIAS);
        m.put(KEY_NAME, d.getName());
        List<Map<String, Object>> tpl = typeParameters(d.getTypeParameters());
        if (tpl != null) {
            m.put(KEY_TYPE_PARAMS, tpl);
        }
        if (d.getSelfType() != null) {
            m.put(KEY_SELF_TYPE, typeMap(d.getSelfType()));
        }
        m.put("$alias", typeMap(d.getExtendedType()));
        encodeTypes(d.getCaseTypes(), m, "of");
        encodeTypes(d.getSatisfiedTypes(), m, "satisfies");
        encodeAnnotations(d, m);
        parent.put(d.getName(), m);
        return m;
    }

    /** Encodes the list of types and puts them under the specified key in the map. */
    private void encodeTypes(List<ProducedType> types, Map<String,Object> m, String key) {
        if (types == null || types.isEmpty()) return;
        List<Map<String, Object>> sats = new ArrayList<Map<String,Object>>(types.size());
        for (ProducedType st : types) {
            sats.add(typeMap(st));
        }
        m.put(key, sats);
    }

    /** Encodes all annotations as a map which is then stored under the
     * {@link #KEY_ANNOTATIONS} key in the specified map. */
    private void encodeAnnotations(Declaration d, Map<String, Object> m) {
        HashMap<String, List<String>> anns = new HashMap<String, List<String>>();
        for (Annotation a : d.getAnnotations()) {
            String name = a.getName();
            List<String> args = a.getPositionalArguments();
            if (args == null) {
                args = Collections.emptyList();
            }
            anns.put(name, args);
        }
        if (!anns.isEmpty()) {
            m.put(KEY_ANNOTATIONS, anns);
        }
    }

}
