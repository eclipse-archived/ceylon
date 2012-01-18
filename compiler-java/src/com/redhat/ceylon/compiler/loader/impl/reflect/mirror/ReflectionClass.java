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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.redhat.ceylon.compiler.loader.mirror.AnnotationMirror;
import com.redhat.ceylon.compiler.loader.mirror.ClassMirror;
import com.redhat.ceylon.compiler.loader.mirror.FieldMirror;
import com.redhat.ceylon.compiler.loader.mirror.MethodMirror;
import com.redhat.ceylon.compiler.loader.mirror.PackageMirror;
import com.redhat.ceylon.compiler.loader.mirror.TypeMirror;
import com.redhat.ceylon.compiler.loader.mirror.TypeParameterMirror;

public class ReflectionClass implements ClassMirror {

    private Class<?> klass;

    public ReflectionClass(Class<?> klass) {
        this.klass = klass;
    }

    @Override
    public AnnotationMirror getAnnotation(String type) {
        return ReflectionUtils.getAnnotation(klass, type);
    }

    @Override
    public boolean isPublic() {
        return Modifier.isPublic(klass.getModifiers());
    }

    @Override
    public String getQualifiedName() {
        return klass.getName();
    }

    @Override
    public String getSimpleName() {
        return klass.getSimpleName();
    }

    @Override
    public PackageMirror getPackage() {
        return new ReflectionPackage(klass.getPackage());
    }

    @Override
    public boolean isInterface() {
        return klass.isInterface();
    }

    @Override
    public boolean isAbstract() {
        return Modifier.isAbstract(klass.getModifiers());
    }

    @Override
    public List<MethodMirror> getDirectMethods() {
        Method[] directMethods = klass.getDeclaredMethods();
        Constructor<?>[] directConstructors = klass.getDeclaredConstructors();
        List<MethodMirror> methods = new ArrayList<MethodMirror>(directMethods.length + directConstructors.length);
        for(Method directMethod : directMethods)
            methods.add(new ReflectionMethod(directMethod));
        for(Constructor<?> directConstructor : directConstructors)
            methods.add(new ReflectionMethod(directConstructor));
        return methods;
    }

    @Override
    public List<FieldMirror> getDirectFields() {
        Field[] directFields = klass.getDeclaredFields();
        List<FieldMirror> fields = new ArrayList<FieldMirror>(directFields.length);
        for(Field field : directFields)
            fields.add(new ReflectionField(field));
        return fields;
    }

    @Override
    public TypeMirror getSuperclass() {
        Type superclass = klass.getGenericSuperclass();
        return superclass != null ? new ReflectionType(superclass) : null;
    }

    @Override
    public List<TypeMirror> getInterfaces() {
        Type[] javaInterfaces = klass.getGenericInterfaces();
        List<TypeMirror> interfaces = new ArrayList<TypeMirror>(javaInterfaces.length);
        for(Type javaInterface : javaInterfaces)
            interfaces.add(new ReflectionType(javaInterface));
        return interfaces;
    }

    @Override
    public List<TypeParameterMirror> getTypeParameters() {
        return ReflectionUtils.getTypeParameters(klass);
    }

    @Override
    public boolean isCeylonToplevelAttribute() {
        return klass.isAnnotationPresent(com.redhat.ceylon.compiler.java.metadata.Attribute.class);
    }

    @Override
    public boolean isCeylonToplevelObject() {
        return klass.isAnnotationPresent(com.redhat.ceylon.compiler.java.metadata.Object.class);
    }

    @Override
    public boolean isCeylonToplevelMethod() {
        return klass.isAnnotationPresent(com.redhat.ceylon.compiler.java.metadata.Method.class);
    }

    @Override
    public boolean isLoadedFromSource() {
        return false;
    }
}
