package com.redhat.ceylon.compiler.js;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.compiler.SimpleJsonEncoder;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Getter;
import com.redhat.ceylon.compiler.typechecker.model.IntersectionType;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.UnionType;
import com.redhat.ceylon.compiler.typechecker.model.Value;

/** Generates the metamodel for all objects in a module.
 * 
 * @author Enrique Zamudio
 */
public class MetamodelGenerator extends Visitor {

    private final Map<String, Object> model = new HashMap<String, Object>();
    private final Module module;

    public MetamodelGenerator(Module module) {
        this.module = module;
        model.put("module-name", module.getNameAsString());
        model.put("module-version", module.getVersion());
    }

    /** Returns the in-memory model as a collection of maps.
     * The top-level map represents the module. */
    public Map<String, Object> getModel() {
        return Collections.unmodifiableMap(model);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> findParent(Declaration d) {
        if (d.isToplevel()) {
            return model;
        }
        ArrayList<String> names = new ArrayList<String>();
        Scope sc = d.getContainer();
        while (sc.getContainer() != null) {
            if (sc instanceof TypeDeclaration) {
                names.add(0, ((TypeDeclaration) sc).getName());
            }
            sc = sc.getContainer();
        }
        Map<String, Object> last = model;
        for (String name : names) {
            if (last == model) {
                last = (Map<String, Object>)last.get(name);
            } else if (last.containsKey("methods") && ((Map<String,Object>)last.get("methods")).containsKey(name)) {
                last = (Map<String,Object>)((Map<String,Object>)last.get("methods")).get(name);
            } else if (last.containsKey("attrs") && ((Map<String,Object>)last.get("attrs")).containsKey(name)) {
                last = (Map<String,Object>)((Map<String,Object>)last.get("attrs")).get(name);
            } else if (last.containsKey("classes") && ((Map<String,Object>)last.get("classes")).containsKey(name)) {
                last = (Map<String,Object>)((Map<String,Object>)last.get("classes")).get(name);
            } else if (last.containsKey("ifaces") && ((Map<String,Object>)last.get("ifaces")).containsKey(name)) {
                last = (Map<String,Object>)((Map<String,Object>)last.get("ifaces")).get(name);
            } else if (last.containsKey("objs") && ((Map<String,Object>)last.get("objs")).containsKey(name)) {
                last = (Map<String,Object>)((Map<String,Object>)last.get("ifaces")).get(name);
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
            m.put("types", subs);
            return m;
        }
        m.put("name", d.getName());
        com.redhat.ceylon.compiler.typechecker.model.Package pkg = d.getUnit().getPackage();
        m.put("pkg", pkg.getNameAsString());
        if (!pkg.getModule().equals(module)) {
            m.put("mod", d.getUnit().getPackage().getModule().getNameAsString());
        }
        putTypeParameters(m, pt);
        return m;
    }

    /** Returns a map with the same info as {@link #typeParameterMap(ProducedType)} but with
     * an additional key "variance" if it's covariant ("out") or contravariant ("in"). */
    private Map<String, Object> typeParameterMap(TypeParameter tp) {
        Map<String, Object> map = typeParameterMap(tp.getType());
        if (tp.isCovariant()) {
            map.put("variance", "out");
        } else if (tp.isContravariant()) {
            map.put("variance", "in");
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
        m.put("mt", "tparam");
        if (d instanceof UnionType || d instanceof IntersectionType) {
            List<ProducedType> subtipos = d instanceof UnionType ? d.getCaseTypes() : d.getSatisfiedTypes();
            List<Map<String,Object>> subs = new ArrayList<Map<String,Object>>(subtipos.size());
            for (ProducedType sub : subtipos) {
                subs.add(typeMap(sub));
            }
            m.put("comp", d instanceof UnionType ? "u" : "i");
            m.put("types", subs);
            return m;
        }
        m.put("name", d.getName());
        com.redhat.ceylon.compiler.typechecker.model.Package pkg = d.getUnit().getPackage();
        m.put("pkg", pkg.getNameAsString());
        if (!pkg.getModule().equals(module)) {
            m.put("mod", d.getUnit().getPackage().getModule().getNameAsString());
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
            container.put("tparams", list);
        }
    }

    /** Create a list of maps from the list of type parameters.
     * @see #typeParameterMap(TypeParameter) */
    private List<Map<String, Object>> typeParameters(Tree.TypeParameterList tpl) {
        if (tpl != null && !tpl.getTypeParameterDeclarations().isEmpty()) {
            List<Map<String, Object>> l = new ArrayList<Map<String,Object>>(tpl.getTypeParameterDeclarations().size());
            for (Tree.TypeParameterDeclaration tp : tpl.getTypeParameterDeclarations()) {
                l.add(typeParameterMap(tp.getDeclarationModel()));
            }
            return l;
        }
        return null;
    }
    /** Create a list of maps from the list of type constraints. Each map includes
     * the satisfies or "of" rules (satisfied types or case types), which are in turn
     * maps generated with {@link #typeMap(ProducedType)}. */
    private List<Map<String, Object>> typeConstraints(Tree.TypeConstraintList tcl) {
        if (tcl != null && !tcl.getTypeConstraints().isEmpty()) {
            List<Map<String, Object>> l = new ArrayList<Map<String,Object>>(tcl.getTypeConstraints().size());
            for (Tree.TypeConstraint tcon : tcl.getTypeConstraints()) {
                Map<String, Object> c = typeMap(tcon.getDeclarationModel().getType());
                c.put("mt", "constraint");
                if (tcon.getSatisfiedTypes() != null && !tcon.getSatisfiedTypes().getTypes().isEmpty()) {
                    List<Map<String, Object>> sats = new ArrayList<Map<String,Object>>(tcon.getSatisfiedTypes().getTypes().size());
                    for (Tree.SimpleType st : tcon.getSatisfiedTypes().getTypes()) {
                        sats.add(typeMap(st.getTypeModel()));
                    }
                    c.put("satisfies", sats);
                } else if (tcon.getCaseTypes() != null && !tcon.getCaseTypes().getTypes().isEmpty()) {
                    List<Map<String, Object>> ofs = new ArrayList<Map<String,Object>>(tcon.getCaseTypes().getTypes().size());
                    for (Tree.SimpleType st : tcon.getCaseTypes().getTypes()) {
                        ofs.add(typeMap(st.getTypeModel()));
                    }
                    c.put("of", ofs);
                }
                l.add(c);
            }
            return l;
        }
        return null;
    }

    /** Create a list of maps for the parameter list. Each map will be a parameter, including
     * name, type, default value (if any), and whether it's sequenced. */
    private List<Map<String,Object>> parameterListMap(Tree.ParameterList plist) {
        List<Tree.Parameter> parms = plist.getParameters();
        if (parms.size() > 0) {
            List<Map<String,Object>> p = new ArrayList<Map<String,Object>>(parms.size());
            for (Tree.Parameter parm : parms) {
                Map<String, Object> pm = new HashMap<String, Object>();
                pm.put("name", parm.getDeclarationModel().getName());
                if (parm.getDeclarationModel().isSequenced()) {
                    pm.put("seq", "1");
                }
                pm.put("type", typeMap(parm.getType().getTypeModel()));
                pm.put("mt", "param");
                //TODO do these guys need anything else?
                if (parm.getDefaultArgument() != null) {
                    //This could be compiled to JS...
                    pm.put("def", parm.getDefaultArgument().getSpecifierExpression().getExpression().getTerm().getText());
                }
                p.add(pm);
            }
            return p;
        }
        return null;
    }
    /** Create and store the model of a method definition. */
    @SuppressWarnings("unchecked")
    @Override public void visit(Tree.MethodDefinition that) {
        com.redhat.ceylon.compiler.typechecker.model.Method d = that.getDeclarationModel();
        Map<String, Object> parent;
        if (d.isToplevel()) {
            parent = model;
        } else if (d.isMember()) {
            parent = findParent(that.getDeclarationModel());
            if (parent == null) {
                System.out.println("orphaned method - How the hell did this happen? " + that.getLocation() + " @ " + that.getUnit().getFilename());
                return;
            }
            parent = (Map<String, Object>)parent.get("methods");
        } else {
            return;
        }
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("mt", "method");
        m.put("name", d.getName());
        Map<String, Object> returnType = typeMap(that.getType().getTypeModel());
        if (that.getParameterLists().size() > 1) {
            //Calculate return type for nested functions
            for (int i = that.getParameterLists().size()-1; i>0; i--) {
                Tree.ParameterList plist = that.getParameterLists().get(i);
                List<Map<String, Object>> paramtypes = new ArrayList<Map<String,Object>>(plist.getParameters().size()+1);
                paramtypes.add(returnType);
                for (Tree.Parameter p : plist.getParameters()) {
                    paramtypes.add(typeMap(p.getType().getTypeModel()));
                }
                returnType = new HashMap<String, Object>();
                returnType.put("name", "Callable");
                returnType.put("pkg", "ceylon.language");
                returnType.put("mod", "ceylon.language");
                returnType.put("tparams", paramtypes);
            }
        }
        m.put("type", returnType);
        //Now the type parameters, if any
        List<Map<String, Object>> tpl = typeParameters(that.getTypeParameterList());
        if (tpl != null) {
            m.put("tparams", tpl);
        }

        //Type constraints, if any
        tpl = typeConstraints(that.getTypeConstraintList());
        if (tpl != null) {
            m.put("constraints", tpl);
        }

        //Now the parameters
        List<Map<String,Object>> parms = parameterListMap(that.getParameterLists().get(0));
        if (parms != null && parms.size() > 0) {
            m.put("params", parms);
        }
        //Certain annotations
        if (d.isShared()) {
            m.put("shared", "1");
        }
        if (d.isActual()) {
            m.put("actual", "1");
        }
        if (d.isFormal()) {
            m.put("formal", "1");
        }
        if (d.isDefault()) {
            m.put("def", "1");
        }
        parent.put(that.getDeclarationModel().getName(), m);
        //We really don't need to go inside a method's body
        //super.visit(that);
    }

    /** Create and store the metamodel info for an attribute. */
    @SuppressWarnings("unchecked")
    @Override public void visit(Tree.AttributeDeclaration that) {
        Map<String, Object> m = new HashMap<String, Object>();
        Value d = that.getDeclarationModel();
        Map<String, Object> parent;
        if (d.isToplevel()) {
            parent = model;
        } else if (d.isMember()) {
            parent = findParent(d);
            if (parent == null) {
                System.out.println("orphaned attribute - How the hell did this happen? " + that.getLocation() + " @ " + that.getUnit().getFilename());
                return;
            }
            parent = (Map<String,Object>)parent.get("attrs");
        } else {
            //if (d.getName().equals("hash")) {
                System.out.println("ATTRIB! " + that);
            //}
            //Ignore attributes inside control blocks, methods, etc.
            return;
        }
        m.put("name", d.getName());
        m.put("mt", "attr");
        m.put("type", typeMap(that.getType().getTypeModel()));
        if (d.isShared()) {
            m.put("shared", "1");
        }
        if (d.isVariable()) {
            m.put("var", "1");
        }
        if (d.isFormal()) {
            m.put("formal", "1");
        }
        parent.put(d.getName(), m);
        super.visit(that);
    }

    @Override @SuppressWarnings("unchecked")
    public void visit(Tree.ClassDefinition that) {
        com.redhat.ceylon.compiler.typechecker.model.Class d = that.getDeclarationModel();
        Map<String, Object> parent = findParent(d);
        if (parent == null) {
            System.out.println("orphaned class - how the hell did this happen? " + that.getLocation() + " @ " + that.getUnit().getFilename());
        } else if (!d.isToplevel()) {
            parent = (Map<String,Object>)parent.get("classes");
        }
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("mt", "class");
        m.put("name", d.getName());
        //Extends
        m.put("super", typeMap(d.getExtendedType()));
        //Satisfies
        if (d.getSatisfiedTypes() != null && !d.getSatisfiedTypes().isEmpty()) {
            List<Map<String,Object>> sats = new ArrayList<Map<String,Object>>(d.getSatisfiedTypes().size());
            for (ProducedType sat : d.getSatisfiedTypes()) {
                sats.add(typeMap(sat));
            }
            m.put("satisfies", sats);
        }
        //Type parameters
        List<Map<String, Object>> tpl = typeParameters(that.getTypeParameterList());
        if (tpl != null) {
            m.put("tparams", tpl);
        }
        //Type constraints
        tpl = typeConstraints(that.getTypeConstraintList());
        if (tpl != null) {
            m.put("constraints", tpl);
        }
        //Initializer parameters
        List<Map<String,Object>> inits = parameterListMap(that.getParameterList());
        if (inits != null && !inits.isEmpty()) {
            m.put("params", inits);
        }
        //Case types
        if (that.getCaseTypes() != null) {
            List<Map<String,Object>> cases = new ArrayList<Map<String,Object>>();
            if (that.getCaseTypes().getTypes().isEmpty()) {
                for (Tree.BaseMemberExpression bme : that.getCaseTypes().getBaseMemberExpressions()) {
                    Map<String,Object> obj = new HashMap<String, Object>();
                    obj.put("name", bme.getIdentifier().getText());
                    obj.put("obj", "y");
                    cases.add(obj);
                }
            } else {
                for (Tree.SimpleType ct : that.getCaseTypes().getTypes()) {
                    cases.add(typeMap(ct.getTypeModel()));
                }
            }
            m.put("of", cases);
        }
        //Certain annotations
        if (d.isShared()) {
            m.put("shared", "1");
        }
        if (d.isAbstract()) {
            m.put("abstract", "1");
        }
        m.put("methods", new HashMap<String, Object>());
        m.put("classes", new HashMap<String, Object>());
        m.put("attrs", new HashMap<String, Object>());
        m.put("ifaces", new HashMap<String, Object>());
        m.put("objs", new HashMap<String, Object>());
        parent.put(d.getName(), m);
        super.visit(that);
    }

    @Override @SuppressWarnings("unchecked")
    public void visit(Tree.InterfaceDefinition that) {
        com.redhat.ceylon.compiler.typechecker.model.Interface d = that.getDeclarationModel();
        Map<String, Object> parent = findParent(d);
        if (parent == null) {
            System.out.println("orphaned interface - how the hell did this happen? " + that.getLocation() + " @ " + that.getUnit().getFilename());
        } else if (!d.isToplevel()) {
            parent = (Map<String,Object>)parent.get("ifaces");
        }
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("mt", "iface");
        m.put("name", d.getName());

        //Certain annotations
        if (d.isShared()) {
            m.put("shared", "1");
        }
        m.put("methods", new HashMap<String, Object>());
        m.put("classes", new HashMap<String, Object>());
        m.put("attrs", new HashMap<String, Object>());
        m.put("ifaces", new HashMap<String, Object>());
        m.put("objs", new HashMap<String, Object>());
        parent.put(d.getName(), m);
        super.visit(that);
    }

    @Override @SuppressWarnings("unchecked")
    public void visit(Tree.ObjectDefinition that) {
        com.redhat.ceylon.compiler.typechecker.model.Value d = that.getDeclarationModel();
        Map<String, Object> parent = findParent(d);
        if (parent == null) {
            System.out.println("orphaned object - how the hell did this happen? " + that);
        } else if (!d.isToplevel()) {
            parent = (Map<String,Object>)parent.get("objects");
        }
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("mt", "object");
        m.put("name", d.getName());

        //Certain annotations
        if (d.isShared()) {
            m.put("shared", "1");
        }
        m.put("methods", new HashMap<String, Object>());
        m.put("classes", new HashMap<String, Object>());
        m.put("attrs", new HashMap<String, Object>());
        m.put("ifaces", new HashMap<String, Object>());
        m.put("objs", new HashMap<String, Object>());
        parent.put(d.getName(), m);
        super.visit(that);
    }

    @Override @SuppressWarnings("unchecked")
    public void visit(Tree.AttributeGetterDefinition that) {
        Map<String, Object> m = new HashMap<String, Object>();
        Getter d = that.getDeclarationModel();
        Map<String, Object> parent;
        if (d.isToplevel()) {
            parent = model;
        } else if (d.isMember()) {
            parent = findParent(d);
            if (parent == null) {
                System.out.println("orphaned getter WTF!!! " + that.getLocation() + " @ " + that.getUnit().getFilename());
                return;
            }
            parent = (Map<String,Object>)parent.get("attrs");
        } else {
            //Ignore attributes inside control blocks, methods, etc.
            return;
        }
        m.put("name", d.getName());
        m.put("mt", "attr");
        m.put("type", typeMap(that.getType().getTypeModel()));
        if (d.isShared()) {
            m.put("shared", "1");
        }
        if (d.isActual()) {
            m.put("actual", "1");
        }
        if (d.isFormal()) {
            m.put("formal", "1");
        }
        parent.put(d.getName(), m);
        super.visit(that);
    }

}
