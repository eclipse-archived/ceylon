package com.redhat.ceylon.compiler.loader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.DeclarationKind;
import com.redhat.ceylon.compiler.typechecker.model.FunctionalParameter;
import com.redhat.ceylon.compiler.typechecker.model.Getter;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.IntersectionType;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.ModuleImport;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ParameterList;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.UnionType;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.model.ValueParameter;

/** Generates the metamodel for all objects in a module.
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

    public static final String ANN_DEFAULT      = "$def";
    public static final String ANN_SHARED       = "$shr";
    public static final String ANN_ACTUAL       = "$act";
    public static final String ANN_FORMAL       = "$fml";

    public static final String METATYPE_CLASS           = "cls";
    public static final String METATYPE_INTERFACE       = "ifc";
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
            if (last == pkgmap) {
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
        if (tp.isSequenced()) {
            map.put("seq", "1");
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
                if (parm.getTypeDeclaration().getDeclarationKind()==DeclarationKind.TYPE_PARAMETER) {
                    pm.put(KEY_TYPE, parm.getTypeDeclaration().getName());
                } else {
                    pm.put(KEY_TYPE, typeMap(parm.getType()));
                }
                if (parm instanceof ValueParameter) {
                    pm.put("$pt", "v");
                } else if (parm instanceof FunctionalParameter) {
                    pm.put("$pt", "f");
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
    public void encodeMethod(Method d) {
        Map<String, Object> parent;
        if (d.isToplevel() || d.isMember()) {
            parent = findParent(d);
            if (parent == null) {
                //System.out.println("orphaned method - How the hell did this happen? " + that.getLocation() + " @ " + that.getUnit().getFilename());
                return;
            }
            if (!d.isToplevel()) {
                if (!parent.containsKey(KEY_METHODS)) {
                    parent.put(KEY_METHODS, new HashMap<String,Object>());
                }
                parent = (Map<String, Object>)parent.get(KEY_METHODS);
            }
        } else {
            return;
        }
        Map<String, Object> m = new HashMap<String, Object>();
        m.put(KEY_METATYPE, METATYPE_METHOD);
        m.put(KEY_NAME, d.getName());
        List<Map<String, Object>> tpl = typeParameters(d.getTypeParameters());
        if (tpl != null) {
            m.put(KEY_TYPE_PARAMS, tpl);
        }
        Map<String, Object> returnType = typeMap(d.getType());
        if (d.getParameterLists().size() > 1) {
            //Calculate return type for nested functions
            for (int i = d.getParameterLists().size()-1; i>0; i--) {
                ParameterList plist = d.getParameterLists().get(i);
                List<Map<String, Object>> paramtypes = new ArrayList<Map<String,Object>>(plist.getParameters().size()+1);
                paramtypes.add(returnType);
                for (Parameter p : plist.getParameters()) {
                    paramtypes.add(typeMap(p.getType()));
                }
                returnType = new HashMap<String, Object>();
                returnType.put(KEY_NAME, "Callable");
                returnType.put(KEY_PACKAGE, "ceylon.language");
                returnType.put(KEY_MODULE, "ceylon.language");
                returnType.put(KEY_TYPE_PARAMS, paramtypes);
            }
        }
        m.put(KEY_TYPE, returnType);

        //Now the parameters
        List<Map<String,Object>> parms = parameterListMap(d.getParameterLists().get(0));
        if (parms != null && parms.size() > 0) {
            m.put(KEY_PARAMS, parms);
        }
        //Certain annotations
        encodeSharedActualFormalDefault(d, m);
        parent.put(d.getName(), m);
        //We really don't need to go inside a method's body
        //super.visit(that);
    }

    /** Create and store the metamodel info for an attribute. */
    @SuppressWarnings("unchecked")
    public void encodeAttribute(Value d) {
        Map<String, Object> m = new HashMap<String, Object>();
        Map<String, Object> parent;
        if (d.isToplevel() || d.isMember()) {
            parent = findParent(d);
            if (parent == null) {
                return;
            }
            if (!d.isToplevel()) {
                if (!parent.containsKey(KEY_ATTRIBUTES)) {
                    parent.put(KEY_ATTRIBUTES, new HashMap<String,Object>());
                }
                parent = (Map<String,Object>)parent.get(KEY_ATTRIBUTES);
            }
        } else {
            //Ignore attributes inside control blocks, methods, etc.
            return;
        }
        m.put(KEY_NAME, d.getName());
        m.put(KEY_METATYPE, METATYPE_ATTRIBUTE);
        m.put(KEY_TYPE, typeMap(d.getType()));
        encodeSharedActualFormalDefault(d, m);
        if (d.isVariable()) {
            m.put("var", "1");
        }
        parent.put(d.getName(), m);
    }

    @SuppressWarnings("unchecked")
    public void encodeClass(com.redhat.ceylon.compiler.typechecker.model.Class d) {
        Map<String, Object> parent = findParent(d);
        if (d.isToplevel() || d.isMember()) {
            if (!d.isToplevel()) {
                if (!parent.containsKey(KEY_CLASSES)) {
                    parent.put(KEY_CLASSES, new HashMap<String,Object>());
                }
                parent = (Map<String,Object>)parent.get(KEY_CLASSES);
            }
        } else {
            return;
        }
        Map<String, Object> m = new HashMap<String, Object>();
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

        //Certain annotations
        encodeSharedActualFormalDefault(d, m);
        if (d.isAbstract()) {
            m.put("abstract", "1");
        }
        if (d.isAnonymous()) {
            m.put("$anon", "1");
        }
        parent.put(d.getName(), m);
    }

    @SuppressWarnings("unchecked")
    public void encodeInterface(Interface d) {
        Map<String, Object> parent = findParent(d);
        if (d.isToplevel() || d.isMember()) {
            if (!d.isToplevel()) {
                if (!parent.containsKey(KEY_INTERFACES)) {
                    parent.put(KEY_INTERFACES, new HashMap<String,Object>());
                }
                parent = (Map<String,Object>)parent.get(KEY_INTERFACES);
            }
        } else {
            return;
        }
        Map<String, Object> m = new HashMap<String, Object>();
        m.put(KEY_METATYPE, METATYPE_INTERFACE);
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
        //satisfies
        encodeTypes(d.getSatisfiedTypes(), m, "satisfies");
        //Case types
        encodeTypes(d.getCaseTypes(), m, "of");
        //Certain annotations
        if (d.isShared()) {
            m.put(ANN_SHARED, "1");
        }
        parent.put(d.getName(), m);
    }

    @SuppressWarnings("unchecked")
    public void encodeObject(Value d) {
        Map<String, Object> parent = findParent(d);
        if (d.isToplevel() || d.isMember()) {
            if (!d.isToplevel()) {
                if (!parent.containsKey(KEY_OBJECTS)) {
                    parent.put(KEY_OBJECTS, new HashMap<String, Object>());
                }
                parent = (Map<String,Object>)parent.get(KEY_OBJECTS);
            }
        } else {
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
        if (d.isShared()) {
            m.put(ANN_SHARED, "1");
        }
        parent.put(d.getName(), m);
    }

    @SuppressWarnings("unchecked")
    public void encodeGetter(Getter d) {
        Map<String, Object> m = new HashMap<String, Object>();
        Map<String, Object> parent;
        if (d.isToplevel() || d.isMember()) {
            parent = findParent(d);
            if (parent == null) {
                return;
            }
            if (!d.isToplevel()) {
                if (!parent.containsKey(KEY_ATTRIBUTES)) {
                    parent.put(KEY_ATTRIBUTES, new HashMap<String, Object>());
                }
                parent = (Map<String,Object>)parent.get(KEY_ATTRIBUTES);
            }
        } else {
            //Ignore attributes inside control blocks, methods, etc.
            return;
        }
        m.put(KEY_NAME, d.getName());
        m.put(KEY_METATYPE, METATYPE_GETTER);
        m.put(KEY_TYPE, typeMap(d.getType()));
        encodeSharedActualFormalDefault(d, m);
        parent.put(d.getName(), m);
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

    private void encodeSharedActualFormalDefault(Declaration d, Map<String, Object> m) {
        if (d.isShared()) {
            m.put(ANN_SHARED, "1");
        }
        if (d.isActual()) {
            m.put(ANN_ACTUAL, "1");
        }
        if (d.isFormal()) {
            m.put(ANN_FORMAL, "1");
        }
        if (d.isDefault()) {
            m.put(ANN_DEFAULT, "1");
        }
    }

}
