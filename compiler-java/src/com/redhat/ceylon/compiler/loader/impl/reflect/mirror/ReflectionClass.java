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
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.compiler.loader.AbstractModelLoader;
import com.redhat.ceylon.compiler.loader.ModelResolutionException;
import com.redhat.ceylon.compiler.loader.mirror.AnnotationMirror;
import com.redhat.ceylon.compiler.loader.mirror.ClassMirror;
import com.redhat.ceylon.compiler.loader.mirror.FieldMirror;
import com.redhat.ceylon.compiler.loader.mirror.MethodMirror;
import com.redhat.ceylon.compiler.loader.mirror.PackageMirror;
import com.redhat.ceylon.compiler.loader.mirror.TypeMirror;
import com.redhat.ceylon.compiler.loader.mirror.TypeParameterMirror;
import com.redhat.ceylon.compiler.typechecker.model.Module;

public class ReflectionClass implements ClassMirror {

    public final Class<?> klass;
    private ArrayList<FieldMirror> fields;
    private ArrayList<MethodMirror> methods;
    private ArrayList<TypeMirror> interfaces;
    private List<TypeParameterMirror> typeParameters;
    private ReflectionPackage pkg;
    private boolean superClassSet;
    private ReflectionType superClass;
    private boolean enclosingClassSet;
    private ClassMirror enclosingClass;
    private boolean enclosingMethodSet;
    private MethodMirror enclosingMethod;
    private LinkedList<ClassMirror> innerClasses;
    private String cacheKey;
    private Map<String, AnnotationMirror> annotations;

    public ReflectionClass(Class<?> klass) {
        this.klass = klass;
    }

    @Override
    public AnnotationMirror getAnnotation(String type) {
        return getAnnotations().get(type);
    }

    private boolean isAnnotationPresent(String type) {
        return getAnnotations().containsKey(type);
    }

    private Map<String, AnnotationMirror> getAnnotations() {
        // profiling revealed we need to cache this
        if(annotations == null){
            annotations = ReflectionUtils.getAnnotations(klass);
        }
        return annotations;
    }

    @Override
    public boolean isPublic() {
        return Modifier.isPublic(klass.getModifiers());
    }

    @Override
    public boolean isProtected() {
        return Modifier.isProtected(klass.getModifiers());
    }

    @Override
    public boolean isDefaultAccess() {
        return !Modifier.isPrivate(klass.getModifiers())
                && !Modifier.isPublic(klass.getModifiers())
                && !Modifier.isProtected(klass.getModifiers());
    }

    @Override
    public String getQualifiedName() {
        return klass.getName();
    }

    @Override
    public String getFlatName() {
        // FIXME: is this really the same?
        return klass.getName();
    }

    @Override
    public String getName() {
        return klass.getSimpleName();
    }

    @Override
    public PackageMirror getPackage() {
        if(pkg != null)
            return pkg;
        pkg = new ReflectionPackage(klass);
        return pkg;
    }

    @Override
    public boolean isInterface() {
        return klass.isInterface();
    }
    
    @Override
    public boolean isAnnotationType() {
        return klass.isAnnotation();
    }

    @Override
    public boolean isAbstract() {
        return Modifier.isAbstract(klass.getModifiers());
    }

    @Override
    public boolean isStatic() {
        return Modifier.isStatic(klass.getModifiers());
    }

    @Override
    public boolean isFinal() {
        return Modifier.isFinal(klass.getModifiers());
    }

    @Override
    public List<MethodMirror> getDirectMethods() {
        if(methods != null)
            return methods;
        Method[] directMethods;
        Constructor<?>[] directConstructors;
        try{
            directMethods = klass.getDeclaredMethods();
            directConstructors = klass.getDeclaredConstructors();
        }catch(NoClassDefFoundError x){
            throw new ModelResolutionException("Failed to load methods in "+getQualifiedName(), x);
        }
        methods = new ArrayList<MethodMirror>(directMethods.length + directConstructors.length);
        for(Method directMethod : directMethods){
            // Note: unlike JavacClass and JDTClass we return private members, because the runtime manager
            // depends on them
            if(!directMethod.isSynthetic() && !directMethod.isBridge())
                methods.add(new ReflectionMethod(this, directMethod));
        }
        for(Constructor<?> directConstructor : directConstructors){
            // Note: unlike JavacClass and JDTClass we return private members, because the runtime manager
            // depends on them
            if(!directConstructor.isSynthetic())
                methods.add(new ReflectionMethod(this, directConstructor));
        }
        return methods;
    }

    @Override
    public List<FieldMirror> getDirectFields() {
        if(fields != null)
            return fields;
        Field[] directFields = klass.getDeclaredFields();
        fields = new ArrayList<FieldMirror>(directFields.length);
        for(Field field : directFields)
            if(!field.isSynthetic())
                fields.add(new ReflectionField(field));
        return fields;
    }

    @Override
    public TypeMirror getSuperclass() {
        if(superClassSet)
            return superClass;
        Type sup = klass.getGenericSuperclass();
        if(sup != null)
            superClass = new ReflectionType(sup);
        superClassSet = true;
        return superClass;
    }

    @Override
    public List<TypeMirror> getInterfaces() {
        if(interfaces != null)
            return interfaces;
        Type[] javaInterfaces = klass.getGenericInterfaces();
        interfaces = new ArrayList<TypeMirror>(javaInterfaces.length);
        for(Type javaInterface : javaInterfaces)
            interfaces.add(new ReflectionType(javaInterface));
        return interfaces;
    }

    @Override
    public List<TypeParameterMirror> getTypeParameters() {
        if(typeParameters != null)
            return typeParameters;
        typeParameters = ReflectionUtils.getTypeParameters(klass);
        return typeParameters;
    }

    @Override
    public boolean isCeylonToplevelAttribute() {
        return !isInnerClass() && isAnnotationPresent(AbstractModelLoader.CEYLON_ATTRIBUTE_ANNOTATION);
    }

    @Override
    public boolean isCeylonToplevelObject() {
        return !isInnerClass() && isAnnotationPresent(AbstractModelLoader.CEYLON_OBJECT_ANNOTATION);
    }

    @Override
    public boolean isCeylonToplevelMethod() {
        return !isInnerClass() && isAnnotationPresent(AbstractModelLoader.CEYLON_METHOD_ANNOTATION);
    }

    @Override
    public boolean isLoadedFromSource() {
        return false;
    }

    @Override
    public String toString() {
        return "[ReflectionClass: "+klass.toString()+"]";
    }

    @Override
    public boolean isInnerClass() {
        return klass.isMemberClass() || isAnnotationPresent(AbstractModelLoader.CEYLON_CONTAINER_ANNOTATION);
    }

    @Override
    public boolean isLocalClass() {
        return klass.isLocalClass() || isAnnotationPresent(AbstractModelLoader.CEYLON_LOCAL_DECLARATION_ANNOTATION);
    }

    @Override
    public List<ClassMirror> getDirectInnerClasses() {
        if(innerClasses == null){
            innerClasses = new LinkedList<ClassMirror>();
            for(Class<?> innerClass : klass.getDeclaredClasses()){
                innerClasses.add(new ReflectionClass(innerClass));
            }
        }
        return innerClasses;
    }

    @Override
    public boolean isAnonymous() {
        return klass.isAnonymousClass();
    }

    @Override
    public boolean isJavaSource() {
        return false;
    }

    @Override
    public ClassMirror getEnclosingClass() {
        if(enclosingClassSet)
            return enclosingClass;
        Class<?> encl = klass.getEnclosingClass();
        if(encl != null)
            enclosingClass = new ReflectionClass(encl);
        enclosingClassSet = true;
        return enclosingClass;
    }

    @Override
    public MethodMirror getEnclosingMethod() {
        if(enclosingMethodSet)
            return enclosingMethod;
        Member encl = klass.getEnclosingMethod();
        if(encl == null)
            encl = klass.getEnclosingConstructor();
        if(encl != null){
            Class<?> declaringClass = encl.getDeclaringClass();
            ReflectionClass declaringClassMirror = new ReflectionClass(declaringClass);
            enclosingMethod = new ReflectionMethod(declaringClassMirror, encl);
        }
        enclosingMethodSet = true;
        return enclosingMethod;
    }

    @Override
    public boolean isEnum() {
        return klass.isEnum();
    }

    @Override
    public String getCacheKey(Module module) {
        if(cacheKey == null){
            String className = getQualifiedName();
            cacheKey = AbstractModelLoader.getCacheKeyByModule(module, className);
        }
        return cacheKey;
    }
}
