package com.redhat.ceylon.compiler.js;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.compiler.SimpleJsonEncoder;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AttributeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ParameterList;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.Value;

/** Generates the metamodel for all objects in a module.
 * 
 * @author Enrique Zamudio
 */
public class MetamodelGenerator extends Visitor {

    private final Map<String, Object> model = new HashMap<String, Object>();
    private final SimpleJsonEncoder json = new SimpleJsonEncoder();
    private final Module module;

    public MetamodelGenerator(Module module) {
        this.module = module;
        model.put("module-name", module.getNameAsString());
        model.put("module-version", module.getVersion());
    }

    /** Writes a JSON description of the metamodel to the specified output. */
    public void writeModel(Writer out) throws IOException {
        out.write("exports.$$metamodel$$=");
        json.encode(model, out);
        out.write(";\n");
    }

    /** Returns the in-memory model as a collection of maps.
     * The top-level map represents the module. */
    public Map<String, Object> getModel() {
        return Collections.unmodifiableMap(model);
    }

    private Map<String, Object> findParent(Declaration d) {
        return model;
    }

    /** Create a map for the specified ProducedType.
     * Includes name, package, module and type parameters. */
    private Map<String, Object> typeMap(ProducedType pt) {
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("name", pt.getDeclaration().getName());
        com.redhat.ceylon.compiler.typechecker.model.Package pkg = pt.getDeclaration().getUnit().getPackage();
        m.put("pkg", pkg.getNameAsString());
        if (!pkg.getModule().equals(module)) {
            m.put("mod", pt.getDeclaration().getUnit().getPackage().getModule().getNameAsString());
        }
        putTypeParameters(m, pt);
        return m;
    }

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
     * Includes name, package, module and type parameters. */
    private Map<String, Object> typeParameterMap(ProducedType pt) {
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("name", pt.getDeclaration().getName());
        m.put("mt", "typeparam");
        com.redhat.ceylon.compiler.typechecker.model.Package pkg = pt.getDeclaration().getUnit().getPackage();
        m.put("pkg", pkg.getNameAsString());
        if (!pkg.getModule().equals(module)) {
            m.put("mod", pt.getDeclaration().getUnit().getPackage().getModule().getNameAsString());
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
            container.put("paramtypes", list);
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
    /** Create a list of maps from the list of type constraints. */
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

    /** Create and store the model of a method definition. */
    @Override public void visit(Tree.MethodDefinition that) {
        Method d = that.getDeclarationModel();
        Map<String, Object> parent;
        if (d.isToplevel()) {
            parent = model;
        } else if (d.isMember()) {
            parent = findParent(that.getDeclarationModel());
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
                returnType.put("paramtypes", paramtypes);
            }
        }
        m.put("type", returnType);
        //Now the type parameters, if any
        List<Map<String, Object>> tpl = typeParameters(that.getTypeParameterList());
        if (tpl != null) {
            m.put("paramtypes", tpl);
        }

        //Type constraints, if any
        tpl = typeConstraints(that.getTypeConstraintList());
        if (tpl != null) {
            m.put("constraints", tpl);
        }

        //Now the parameters
        List<Tree.Parameter> parms = that.getParameterLists().get(0).getParameters();
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
                    pm.put("def", parm.getDefaultArgument().getSpecifierExpression().getExpression().getTerm().getText());
                }
                p.add(pm);
            }
            m.put("params", p);
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
        super.visit(that);
    }

    /** Create and store the metamodel info for an attribute. */
    @Override public void visit(AttributeDeclaration that) {
        Map<String, Object> m = new HashMap<String, Object>();
        Value d = that.getDeclarationModel();
        Map<String, Object> parent;
        if (d.isToplevel()) {
            parent = model;
        } else {
            parent = findParent(d);
        }
        m.put("mt", "attr");
        m.put("type", typeMap(that.getType().getTypeModel()));
        if (d.isShared()) {
            m.put("shared", "1");
        }
        if (d.isVariable()) {
            m.put("var", "1");
        }
        parent.put(d.getName(), m);
        super.visit(that);
    }

}
