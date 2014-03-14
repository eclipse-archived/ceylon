package com.redhat.ceylon.compiler.java.codegen;

import java.util.IdentityHashMap;
import java.util.Map;

import com.redhat.ceylon.compiler.typechecker.model.Setter;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

/**
 * A visitor which constructs a map associating getters and setters
 */
class GetterSetterPairingVisitor extends Visitor {

    private static final int GETTER_SLOT = 0;
    private static final int SETTER_SLOT = 1;
    
    private Map<Value, Tree.TypedDeclaration[]> map = new IdentityHashMap<>();
    
    public void visit(Tree.AttributeSetterDefinition that) {
        if (that.getDeclarationModel() != null) {
            Tree.TypedDeclaration[] getterSetter = map.get(that.getDeclarationModel().getGetter());
            if (getterSetter == null) {
                getterSetter = new TypedDeclaration[2]; 
                map.put(that.getDeclarationModel().getGetter(), getterSetter);
            }
            getterSetter[SETTER_SLOT] = that;
        }
        super.visit(that);
    }
    
    public void visit(Tree.AttributeGetterDefinition that) {
        if (that.getDeclarationModel().getSetter() != null) {
            Tree.TypedDeclaration[] getterSetter = map.get(that.getDeclarationModel().getSetter().getGetter());
            if (getterSetter == null) {
                getterSetter = new TypedDeclaration[2]; 
                map.put(that.getDeclarationModel().getSetter().getGetter(), getterSetter);
            }
            getterSetter[GETTER_SLOT] = that;
        }
        super.visit(that);
    }
    
    public void visit(Tree.AttributeDeclaration that) {
        if (that.getDeclarationModel().getSetter() != null) {
            Tree.TypedDeclaration[] getterSetter = map.get(that.getDeclarationModel().getSetter().getGetter());
            if (getterSetter == null) {
                getterSetter = new TypedDeclaration[2]; 
                map.put(that.getDeclarationModel().getSetter().getGetter(), getterSetter);
            }
            getterSetter[GETTER_SLOT] = that;
        }
        super.visit(that);
    }
    
    /**
     * Get the tree node of the getter corresponding to the given setter.
     * This could return null if the tree has errors.
     */
    public Tree.AnyAttribute getGetter(Tree.AttributeSetterDefinition that) {
        return getGetter(that.getDeclarationModel());
    }
    
    /**
     * Get the tree node of the getter corresponding to the given setter.
     * This could return null if the tree has errors.
     */
    public Tree.AnyAttribute getGetter(Setter setter) {
        TypedDeclaration[] getterSetter = map.get(setter.getGetter());
        if (getterSetter == null) {
            return null;
        }
        return (Tree.AnyAttribute)getterSetter[GETTER_SLOT];
    }
    
    /**
     * Get the tree node of the setter corresponding to the given getter
     */
    public Tree.AttributeSetterDefinition getSetter(Tree.AnyAttribute that) {
        return getSetter((Value)that.getDeclarationModel());
    }
    
    /**
     * Get the tree node of the setter corresponding to the given getter
     */
    public Tree.AttributeSetterDefinition getSetter(Value getter) {
        TypedDeclaration[] getterSetter = map.get(getter);
        if (getterSetter == null) {
            return null;
        }
        return (Tree.AttributeSetterDefinition)getterSetter[SETTER_SLOT];
    }
    
}
