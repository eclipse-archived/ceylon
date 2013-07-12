package com.redhat.ceylon.compiler.js;

import java.util.Map;

import com.redhat.ceylon.compiler.loader.MetamodelGenerator;
import com.redhat.ceylon.compiler.typechecker.analyzer.UsageWarning;
import com.redhat.ceylon.compiler.typechecker.model.Annotation;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.tree.Message;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

/** Generates the metamodel for all objects in a module.
 * 
 * @author Enrique Zamudio
 */
public class MetamodelVisitor extends Visitor {

    final MetamodelGenerator gen;

    public MetamodelVisitor(Module module) {
        this.gen = new MetamodelGenerator(module);
    }

    /** Returns the in-memory model as a collection of maps.
     * The top-level map represents the module. */
    public Map<String, Object> getModel() {
        return gen.getModel();
    }

    @Override public void visit(Tree.MethodDeclaration that) {
        if (errorFree(that)) {
            gen.encodeMethod(that.getDeclarationModel());
        }
    }

    /** Create and store the model of a method definition. */
    @Override public void visit(Tree.MethodDefinition that) {
        if (errorFree(that)) {
            gen.encodeMethod(that.getDeclarationModel());
            super.visit(that);
        }
    }

    /** Create and store the metamodel info for an attribute. */
    @Override public void visit(Tree.AttributeDeclaration that) {
        if (errorFree(that)) {
            gen.encodeAttribute(that.getDeclarationModel());
            super.visit(that);
        }
    }

    @Override
    public void visit(Tree.ClassDefinition that) {
        if (errorFree(that)) {
            gen.encodeClass(that.getDeclarationModel());
            super.visit(that);
        }
    }

    @Override
    public void visit(Tree.InterfaceDefinition that) {
        if (errorFree(that)) {
            gen.encodeInterface(that.getDeclarationModel());
            super.visit(that);
        }
    }

    @Override
    public void visit(Tree.ObjectDefinition that) {
        if (errorFree(that)) {
            gen.encodeObject(that.getDeclarationModel());
            super.visit(that);
        }
    }

    @Override
    public void visit(Tree.ObjectArgument that) {
        if (errorFree(that)) {
            gen.encodeObject(that.getDeclarationModel());
            super.visit(that);
        }
    }

    @Override
    public void visit(Tree.AttributeGetterDefinition that) {
        if (errorFree(that)) {
            gen.encodeAttribute(that.getDeclarationModel());
            super.visit(that);
        }
    }

    @Override
    public void visit(Tree.TypeAliasDeclaration that) {
        if (errorFree(that)) {
            gen.encodeTypeAlias(that.getDeclarationModel());
        }
    }
    @Override
    public void visit(Tree.ClassDeclaration that) {
        if (errorFree(that)) {
            gen.encodeClass(that.getDeclarationModel());
        }
    }
    @Override
    public void visit(Tree.InterfaceDeclaration that) {
        if (errorFree(that)) {
            gen.encodeInterface(that.getDeclarationModel());
        }
    }

    public boolean errorFree(Node node) {
        if (node.getErrors() != null) {
            for (Message m : node.getErrors()) {
                if (!(m instanceof UsageWarning)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void visit(Tree.SpecifierStatement st) {
        TypedDeclaration d = ((Tree.SpecifierStatement) st).getDeclaration();
        //Just add shared and actual annotations to this declaration
        Annotation ann = new Annotation();
        ann.setName("shared");
        d.getAnnotations().add(ann);
        ann = new Annotation();
        ann.setName("actual");
        d.getAnnotations().add(ann);
        if (d instanceof Method) {
            gen.encodeMethod((Method)d);
        } else if (d instanceof Value) {
            gen.encodeAttribute((Value)d);
        } else {
            throw new RuntimeException("JS compiler doesn't know how to encode " +
                    d.getClass().getName() + " into model");
        }
    }

}
