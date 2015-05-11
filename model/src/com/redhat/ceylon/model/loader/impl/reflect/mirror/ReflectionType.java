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

package com.redhat.ceylon.model.loader.impl.reflect.mirror;

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

import com.redhat.ceylon.model.loader.mirror.ClassMirror;
import com.redhat.ceylon.model.loader.mirror.TypeMirror;
import com.redhat.ceylon.model.loader.mirror.TypeParameterMirror;

public class ReflectionType implements TypeMirror {

    private Type type;
    private List<TypeMirror> typeArguments;
    private ReflectionType componentType;
    private ReflectionType upperBound;
    private ReflectionType lowerBound;
    private boolean lowerBoundSet;
    private boolean upperBoundSet;
    private ReflectionClass declaredClass;
    private boolean declaredClassSet;
    private boolean typeParameterSet;
    private ReflectionTypeParameter typeParameter;
    private ReflectionType qualifyingType;
    private boolean qualifyingTypeSet;

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
        Class<?> klass = (Class<?>)type;
        return klass.getName();
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
            if(kind != null)
                return kind;
            return ((Class<?>)type).isArray() ? TypeKind.ARRAY : TypeKind.DECLARED;
        }
        throw new RuntimeException("Unknown type: "+type);
    }

    @Override
    public TypeMirror getComponentType() {
        if(componentType != null)
            return componentType;
        Type ct;
        if(type instanceof Class)
            ct = ((Class<?>)type).getComponentType(); 
        else
            ct = ((GenericArrayType)type).getGenericComponentType();
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

    @Override
    public TypeMirror getUpperBound() {
        if(upperBoundSet)
            return upperBound;
        if(type instanceof WildcardType){
            // so the JavaDoc for WildcardType says that the upper bound can still be set to Object
            // even if we have a lower bound set. In order to detect this, we check the lower bound.
            // if there is no lower bound we will get null so all works out
            if(getLowerBound() == null){
                Type[] ct = ((WildcardType)type).getUpperBounds();
                // I don't see how there can possibly be more than one bound here according to the spec and grammar, for a wildcard type
                if(ct.length != 1)
                    throw new RuntimeException("Not one upper bound in wildcard type: "+ct.length);
                upperBound = new ReflectionType(ct[0]);
            }else
                upperBound = null;
        }
        upperBoundSet = true;
        return upperBound;
    }

    @Override
    public TypeMirror getLowerBound() {
        if(lowerBoundSet)
            return lowerBound;
        Type[] ct = ((WildcardType)type).getLowerBounds();
        if(ct.length == 0)
            return null;
        // I don't see how there can possibly be more than one bound here according to the spec and grammar, for a wildcard type
        if(ct.length > 1)
            throw new RuntimeException("More than one lower bound in wildcard type: "+ct.length);
        lowerBound = new ReflectionType(ct[0]);
        lowerBoundSet = true;
        return lowerBound;
    }

    @Override
    public boolean isRaw() {
        if(type instanceof ParameterizedType){
            // we're raw if our type is a parameterised type that should have type params
            ParameterizedType ptype = ((ParameterizedType)type);
            Class<?> klass = (Class<?>) ptype.getRawType();
            return klass.getTypeParameters().length != ptype.getActualTypeArguments().length;
        }
        if(type instanceof GenericArrayType)
            return getComponentType().isRaw();
        if(type instanceof TypeVariable)
            return false;
        if(type instanceof WildcardType)
            return false;
        if(type instanceof Class){
            // we're raw if our type is a parameterised type that should have type params
            return ((Class<?>)type).getTypeParameters().length != 0;
        }
        throw new RuntimeException("Unknown type: "+type);
    }

    @Override
    public ClassMirror getDeclaredClass() {
        if(!declaredClassSet){
            if(type instanceof Class){
                declaredClass = new ReflectionClass((Class<?>) type);
            }else if(type instanceof ParameterizedType){
                declaredClass = new ReflectionClass((Class<?>) ((ParameterizedType) type).getRawType());
            }
            declaredClassSet = true;
        }
        return declaredClass;
    }

    @Override
    public TypeParameterMirror getTypeParameter() {
        if(getKind() != TypeKind.TYPEVAR)
            return null;
        if(!typeParameterSet){
            typeParameter = new ReflectionTypeParameter(type);
            typeParameterSet = true;
        }
        return typeParameter;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public TypeMirror getQualifyingType() {
        if(!qualifyingTypeSet){
            Type ownerType = null;
            if(type instanceof Class){
                ownerType  = ((Class) type).getEnclosingClass();
            }else if(type instanceof ParameterizedType){
                ownerType = ((ParameterizedType) type).getOwnerType();
            }
            if(ownerType != null){
                qualifyingType = new ReflectionType(ownerType);
            }
            qualifyingTypeSet = true;
        }
        return qualifyingType;
    }
}
