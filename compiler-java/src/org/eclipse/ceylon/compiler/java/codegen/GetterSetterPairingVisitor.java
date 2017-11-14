/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package org.eclipse.ceylon.compiler.java.codegen;

import java.util.IdentityHashMap;
import java.util.Map;

import org.eclipse.ceylon.compiler.typechecker.tree.Tree;
import org.eclipse.ceylon.compiler.typechecker.tree.Visitor;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree.TypedDeclaration;
import org.eclipse.ceylon.model.typechecker.model.Setter;
import org.eclipse.ceylon.model.typechecker.model.Value;

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
