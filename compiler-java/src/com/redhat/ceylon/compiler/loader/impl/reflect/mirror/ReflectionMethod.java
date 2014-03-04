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

    public final Member method;
    private ArrayList<VariableMirror> parameters;
    private List<TypeParameterMirror> typeParameters;
    private Boolean overridingMethod;
    private Boolean overloadingMethod;
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
        int parameterCount;
        if(method instanceof Method){
            javaParameters = ((Method)method).getGenericParameterTypes();
            annotations = ((Method)method).getParameterAnnotations();
            // only getParameterTypes always reliably include synthetic parameters for constructors
            parameterCount = ((Method)method).getParameterTypes().length;
        }else{
            javaParameters = ((Constructor<?>)method).getGenericParameterTypes();
            annotations = ((Constructor<?>)method).getParameterAnnotations();
            // only getParameterTypes always reliably include synthetic parameters for constructors
            parameterCount = ((Constructor<?>)method).getParameterTypes().length;
        }
        parameters = new ArrayList<VariableMirror>(parameterCount);
        // some compilers will only include non-synthetic parameters in getGenericParameterTypes(), so we need to know if
        // we have less, we should substract synthetic parameters
        int parametersOffset = javaParameters.length - parameterCount;
        // if at least one parameter is annotated, java reflection will only include non-synthetic parameters in 
        // getParameterAnnotations(), so we need to know if we have less, we should substract synthetic parameters
        int annotationsOffset = annotations.length - parameterCount;
        int start = 0;
        if(method instanceof Constructor){
            // enums will always add two synthetic parameters (string and int) and always be static so none more
            Class<?> declaringClass = method.getDeclaringClass();
            if(declaringClass.isEnum())
                start = 2;
            // inner classes will always add a synthetic parameter to the constructor, unless they are static
            // FIXME: local and anonymous classes may add more but we don't know how to find out
            else if((declaringClass.isMemberClass()
                        || declaringClass.isAnonymousClass()
                        || declaringClass.isLocalClass())
                    && !Modifier.isStatic(declaringClass.getModifiers()))
                start = 1;
        }
        // skip synthetic parameters
        for(int i=start;i<parameterCount;i++){
            // apply offsets for parameters and annotations if synthetic parameters are not included
            parameters.add(new ReflectionVariable(javaParameters[i+parametersOffset], annotations[i+annotationsOffset]));
        }
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
        
        if(method instanceof Method)
            overridingMethod = ReflectionUtils.isOverridingMethod((Method) method);
        else
            overridingMethod = false;
        return overridingMethod;
    }

    public boolean isOverloadingMethod() {
        if(overloadingMethod != null)
            return overloadingMethod.booleanValue();
        
        if(method instanceof Method)
            overloadingMethod = ReflectionUtils.isOverloadingMethod((Method) method);
        else
            overloadingMethod = false;
        return overloadingMethod;
    }

    @Override
    public String toString() {
        return "[ReflectionMethod: "+method.toString()+"]";
    }

    @Override
    public boolean isDefault() {
        return ((Method)method).getDefaultValue() != null;
    }
}
