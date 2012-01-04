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
package com.redhat.ceylon.compiler.loader.mirror;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.type.TypeKind;

import com.redhat.ceylon.compiler.modelloader.mirror.TypeMirror;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.Type.ArrayType;

public class JavacType implements TypeMirror {

    private Type type;

    public JavacType(Type type) {
        this.type = type;
    }

    @Override
    public String getQualifiedName() {
        return type.tsym.getQualifiedName().toString();
    }

    @Override
    public List<TypeMirror> getTypeArguments() {
        List<TypeMirror> args = new ArrayList<TypeMirror>(type.getTypeArguments().size());
        for(Type typeArg : type.getTypeArguments()){
            args.add(new JavacType(typeArg));
        }
        return args;
    }

    @Override
    public TypeKind getKind() {
        return type.getKind();
    }

    @Override
    public TypeMirror getComponentType() {
        Type componentType = ((ArrayType)type).getComponentType();
        return componentType != null ? new JavacType(componentType) : null;
    }

    @Override
    public boolean isPrimitive() {
        return type.isPrimitive();
    }

}
