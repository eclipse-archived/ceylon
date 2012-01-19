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

package com.redhat.ceylon.compiler.loader.impl.reflect.mirror;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.type.TypeKind;

import com.redhat.ceylon.compiler.loader.mirror.TypeMirror;

public class ReflectionType implements TypeMirror {

    private Type type;
    private List<TypeMirror> typeArguments;
    private ReflectionType componentType;

    public ReflectionType(Type type) {
        this.type = type;
    }

    @Override
    public String getQualifiedName() {
        if(type instanceof ParameterizedType){
            return ((Class<?>)((ParameterizedType)type).getRawType()).getName();
        }
        if(type instanceof TypeVariable)
            return ((TypeVariable<?>)type).getName();
        return ((Class<?>)type).getName();
    }

    @Override
    public List<TypeMirror> getTypeArguments() {
        if(typeArguments != null)
            return typeArguments;
        if(type instanceof ParameterizedType){
            Type[] javaTypeArguments = ((ParameterizedType)type).getActualTypeArguments();
            typeArguments = new ArrayList<TypeMirror>(javaTypeArguments.length);
            for(Type typeArgument : javaTypeArguments)
                typeArguments.add(new ReflectionType(typeArgument));
            return typeArguments;
        }else
            typeArguments = Collections.<TypeMirror>emptyList(); 
        return typeArguments;
    }

    private static final Map<Class<?>, TypeKind> primitives = new HashMap<Class<?>, TypeKind>();
    static{
        primitives.put(Boolean.TYPE, TypeKind.BOOLEAN);
        primitives.put(Byte.TYPE, TypeKind.BYTE);
        primitives.put(Character.TYPE, TypeKind.CHAR);
        primitives.put(Short.TYPE, TypeKind.SHORT);
        primitives.put(Integer.TYPE, TypeKind.INT);
        primitives.put(Long.TYPE, TypeKind.LONG);
        primitives.put(Float.TYPE, TypeKind.FLOAT);
        primitives.put(Double.TYPE, TypeKind.DOUBLE);
        primitives.put(Void.TYPE, TypeKind.VOID);
    }
    
    @Override
    public TypeKind getKind() {
        if(type instanceof ParameterizedType)
            return TypeKind.DECLARED;
        if(type instanceof GenericArrayType)
            return TypeKind.ARRAY;
        if(type instanceof TypeVariable)
            return TypeKind.TYPEVAR;
        if(type instanceof WildcardType)
            return TypeKind.WILDCARD;
        if(type instanceof Class){
            TypeKind kind = primitives.get(type);
            return kind != null ? kind : TypeKind.DECLARED;
        }
        throw new RuntimeException("Unknown type: "+type);
    }

    @Override
    public TypeMirror getComponentType() {
        if(componentType != null)
            return componentType;
        Type ct = ((GenericArrayType)type).getGenericComponentType();
        componentType = new ReflectionType(ct);
        return componentType;
    }

    @Override
    public boolean isPrimitive() {
        return primitives.containsKey(type);
    }

    @Override
    public String toString() {
        return "[ReflectionType: "+type.toString()+"]";
    }
}
