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
import com.redhat.ceylon.compiler.typechecker.model.Value;

/** Generates the metamodel for all objects in a module.
 * 
 * @author Enrique Zamudio
 */
public class MetamodelGenerator extends Visitor {

    private final Map<String, Object> model = new HashMap<String, Object>();
    private final SimpleJsonEncoder json = new SimpleJsonEncoder();

    public MetamodelGenerator(Module module) {
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

    /** Create and store the model of a method definition. */
    @Override public void visit(Tree.MethodDefinition that) {
        Method d = that.getDeclarationModel();
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("mt", "method");
        m.put("name", d.getName());
        if (that.getParameterLists().size() > 1) {
            //TODO return type for nested functions
        } else {
            m.put("type", d.getTypeDeclaration().getQualifiedNameString());
        }
        List<Tree.Parameter> parms = that.getParameterLists().get(0).getParameters();
        if (parms.size() > 0) {
            List<Map<String,Object>> p = new ArrayList<Map<String,Object>>(parms.size());
            for (Tree.Parameter parm : parms) {
                Map<String, Object> pm = new HashMap<String, Object>();
                pm.put("name", parm.getDeclarationModel().getName());
                pm.put("type", parm.getDeclarationModel().getTypeDeclaration().getQualifiedNameString());
                pm.put("mt", "param");
                //TODO do these guys need anything else?
                if (parm.getDefaultArgument() != null) {
                    pm.put("def", parm.getDefaultArgument().getText());
                }
                p.add(pm);
            }
            m.put("params", p);
        }
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
        Map<String, Object> parent;
        if (that.getDeclarationModel().isToplevel()) {
            parent = model;
        } else {
            parent = findParent(that.getDeclarationModel());
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
        m.put("type", d.getTypeDeclaration().getQualifiedNameString());
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
