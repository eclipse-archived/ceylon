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
package com.redhat.ceylon.compiler.java.loader.mirror;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.lang.model.type.TypeKind;

import com.redhat.ceylon.compiler.loader.mirror.ClassMirror;
import com.redhat.ceylon.compiler.loader.mirror.TypeMirror;
import com.sun.tools.javac.code.Symbol.ClassSymbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.Type.ArrayType;
import com.sun.tools.javac.code.Type.TypeVar;
import com.sun.tools.javac.code.Type.WildcardType;

public class JavacType implements TypeMirror {

    private Type type;
    private List<TypeMirror> typeArguments;
    private boolean componentTypeSet;
    private TypeMirror componentType;
    private boolean upperBoundSet;
    private JavacType upperBound;
    private boolean lowerBoundSet;
    private JavacType lowerBound;
    private JavacClass declaredClass;
    private boolean declaredClassSet;

    public JavacType(Type type) {
        this.type = type;
    }
    
    public String toString() {
        return getClass().getSimpleName() + " of " + type;
    }

    @Override
    public String getQualifiedName() {
        return type.tsym.getQualifiedName().toString();
    }

    @Override
    public List<TypeMirror> getTypeArguments() {
        if (typeArguments == null) {
            List<TypeMirror> args = new ArrayList<TypeMirror>(type.getTypeArguments().size());
            for(Type typeArg : type.getTypeArguments()){
                args.add(new JavacType(typeArg));
            }
            typeArguments = Collections.unmodifiableList(args);
        }
        return typeArguments;
    }

    @Override
    public TypeKind getKind() {
        return type.getKind();
    }

    @Override
    public TypeMirror getComponentType() {
        if (!componentTypeSet
                && type instanceof ArrayType) {
            Type compType = ((ArrayType)type).getComponentType();
            if (compType != null) {
                componentType = new JavacType(compType);
            }
            componentTypeSet = true;
        }
        return componentType;
    }

    @Override
    public boolean isPrimitive() {
        return type.isPrimitive();
    }

    @Override
    public TypeMirror getUpperBound() {
        if (!upperBoundSet){
            if(type instanceof WildcardType) {
                Type bound = ((WildcardType)type).getExtendsBound();
                if (bound != null) {
                    upperBound = new JavacType(bound);
                }
            }else if(type instanceof TypeVar){
                Type bound = ((TypeVar)type).getUpperBound();
                // FIXME: the javadoc says that this can be a fake compound class whose real union bounds
                // are in its implemented interfaces
                if (bound != null) {
                    upperBound = new JavacType(bound);
                }
            }
            upperBoundSet = true;
        }
        return upperBound;
    }

    @Override
    public TypeMirror getLowerBound() {
        if (!lowerBoundSet
                && type instanceof WildcardType) {
            Type bound = ((WildcardType)type).getSuperBound();
            if (bound != null) {
                lowerBound = new JavacType(bound);
            }
            lowerBoundSet = true;
        }
        return lowerBound;
    }

    @Override
    public boolean isRaw() {
        return type.isRaw();
    }
    
    @Override
    public ClassMirror getDeclaredClass(){
        if(!declaredClassSet){
            if(type.tsym instanceof ClassSymbol){
                declaredClass = new JavacClass((ClassSymbol) type.tsym);
            }
            declaredClassSet = true;
        }
        return declaredClass;
    }
}
