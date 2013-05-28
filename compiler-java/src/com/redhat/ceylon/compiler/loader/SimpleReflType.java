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
package com.redhat.ceylon.compiler.loader;

import java.util.Arrays;
import java.util.List;

import javax.lang.model.type.TypeKind;

import com.redhat.ceylon.compiler.loader.mirror.ClassMirror;
import com.redhat.ceylon.compiler.loader.mirror.TypeMirror;

/**
 * Simple Type Mirror.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class SimpleReflType implements TypeMirror {
 
    public enum Module {
        CEYLON, JDK;
    }

    private String name;
    private TypeKind kind;
    private TypeMirror[] typeParameters;
    private Module module;

    public SimpleReflType(String name, Module module, TypeKind kind, TypeMirror... typeParameters) {
        this.name = name;
        this.kind = kind;
        this.typeParameters = typeParameters;
        this.module = module;
    }
    
    public String toString() {
        String p = Arrays.toString(typeParameters);
        return getClass().getSimpleName() + " of " + name + "<" + p.substring(1, p.length()-1) + ">";
    }

    @Override
    public String getQualifiedName() {
        return name;
    }

    @Override
    public List<TypeMirror> getTypeArguments() {
        return Arrays.asList(typeParameters);
    }

    @Override
    public TypeKind getKind() {
        return kind;
    }

    @Override
    public TypeMirror getComponentType() {
        return null;
    }

    @Override
    public boolean isPrimitive() {
        return kind.isPrimitive();
    }

    @Override
    public TypeMirror getUpperBound() {
        return null;
    }

    @Override
    public TypeMirror getLowerBound() {
        return null;
    }

    @Override
    public boolean isRaw() {
        return false;
    }

    @Override
    public ClassMirror getDeclaredClass() {
        return null;
    }

    public Module getModule() {
        return module;
    }
}
