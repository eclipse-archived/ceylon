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

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.redhat.ceylon.compiler.loader.mirror.AnnotationMirror;
import com.redhat.ceylon.compiler.loader.mirror.MethodMirror;
import com.redhat.ceylon.compiler.loader.mirror.TypeMirror;
import com.redhat.ceylon.compiler.loader.mirror.TypeParameterMirror;
import com.redhat.ceylon.compiler.loader.mirror.VariableMirror;

public class ReflectionMethod implements MethodMirror {

    private Member method;
    private ArrayList<VariableMirror> parameters;
    private List<TypeParameterMirror> typeParameters;
    private Boolean overridingMethod;
    private ReflectionType returnType;

    public ReflectionMethod(Member method) {
        this.method = method;
    }

    @Override
    public AnnotationMirror getAnnotation(String type) {
        return ReflectionUtils.getAnnotation((AnnotatedElement)method, type);
    }

    @Override
    public String getName() {
        return method.getName();
    }

    @Override
    public boolean isStatic() {
        return Modifier.isStatic(method.getModifiers());
    }

    @Override
    public boolean isPublic() {
        return Modifier.isPublic(method.getModifiers());
    }
    
    @Override
    public boolean isProtected() {
        return Modifier.isProtected(method.getModifiers());
    }
    
    @Override
    public boolean isDefaultAccess() {
        return !Modifier.isPrivate(method.getModifiers())
                && !Modifier.isPublic(method.getModifiers())
                && !Modifier.isProtected(method.getModifiers());
    }

    @Override
    public boolean isConstructor() {
        return method instanceof Constructor;
    }

    @Override
    public boolean isStaticInit() {
        return false;
    }

    @Override
    public boolean isVariadic() {
        return method instanceof Method ?
                ((Method)method).isVarArgs()
                : ((Constructor<?>)method).isVarArgs();
    }
    
    @Override
    public List<VariableMirror> getParameters() {
        if(parameters != null)
            return parameters;
        Type[] javaParameters;
        Annotation[][] annotations;
        if(method instanceof Method){
            javaParameters = ((Method)method).getGenericParameterTypes();
            annotations = ((Method)method).getParameterAnnotations();
        }else{
            javaParameters = ((Constructor<?>)method).getGenericParameterTypes();
            annotations = ((Constructor<?>)method).getParameterAnnotations();
        }
        parameters = new ArrayList<VariableMirror>(annotations.length);
        int extraParameters = javaParameters.length - annotations.length;
        int parametersCount = Math.min(javaParameters.length, annotations.length);
        for(int i=0;i<parametersCount;i++)
            parameters.add(new ReflectionVariable(javaParameters[i+extraParameters], annotations[i]));
        return parameters;
    }

    @Override
    public boolean isAbstract() {
        return Modifier.isAbstract(method.getModifiers());
    }

    @Override
    public boolean isFinal() {
        return Modifier.isFinal(method.getModifiers());
    }

    @Override
    public TypeMirror getReturnType() {
        if(returnType != null)
            return returnType;
        returnType = new ReflectionType(((Method)method).getGenericReturnType());
        return returnType;
    }
    
    @Override
    public boolean isDeclaredVoid() {
        return method instanceof Method 
                && Void.TYPE == ((Method)method).getReturnType();
    }

    @Override
    public List<TypeParameterMirror> getTypeParameters() {
        if(typeParameters != null)
            return typeParameters;
        typeParameters = ReflectionUtils.getTypeParameters((GenericDeclaration) method);
        return typeParameters;
    }

    public boolean isOverridingMethod() {
        if(overridingMethod != null)
            return overridingMethod.booleanValue();
        
        String name = method.getName();
        Class<?>[] parameterTypes;
        if(method instanceof Method)
            parameterTypes = ((Method)method).getParameterTypes();
        else
            parameterTypes = ((Constructor<?>)method).getParameterTypes();
        Class<?> declaringClass = method.getDeclaringClass();
        // try the superclass first
        Class<?> superclass = declaringClass.getSuperclass();
        if(superclass != null){
            try {
                superclass.getMethod(name, parameterTypes);
                // present
                overridingMethod = Boolean.TRUE;
                return true;
            } catch (Exception e) {
                // not present
            }
        }
        // now try the interfaces
        for(Class<?> interfce : declaringClass.getInterfaces()){
            try {
                interfce.getMethod(name, parameterTypes);
                // present
                overridingMethod = Boolean.TRUE;
                return true;
            } catch (Exception e) {
                // not present
            }
        }
        // not overriding anything
        overridingMethod = Boolean.FALSE;
        return false;
    }

    @Override
    public String toString() {
        return "[ReflectionMethod: "+method.toString()+"]";
    }
}
