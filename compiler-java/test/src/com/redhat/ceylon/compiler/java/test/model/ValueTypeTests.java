package com.redhat.ceylon.compiler.java.test.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.ValueType;

public class ValueTypeTests {

    @Test
    public void testValueTypes() {
        validateValueType(ceylon.language.Boolean.class);
        validateValueType(ceylon.language.Byte.class);
        validateValueType(ceylon.language.Character.class);
        validateValueType(ceylon.language.Float.class);
        validateValueType(ceylon.language.Integer.class);
        validateValueType(ceylon.language.String.class);
        validateValueType(com.redhat.ceylon.compiler.java.language.BooleanArray.class);
        validateValueType(com.redhat.ceylon.compiler.java.language.ByteArray.class);
        validateValueType(com.redhat.ceylon.compiler.java.language.CharArray.class);
        validateValueType(com.redhat.ceylon.compiler.java.language.DoubleArray.class);
        validateValueType(com.redhat.ceylon.compiler.java.language.FloatArray.class);
        validateValueType(com.redhat.ceylon.compiler.java.language.IntArray.class);
        validateValueType(com.redhat.ceylon.compiler.java.language.LongArray.class);
        validateValueType(com.redhat.ceylon.compiler.java.language.ObjectArray.class);
        validateValueType(com.redhat.ceylon.compiler.java.language.ShortArray.class);
    }

    private void validateValueType(Class<?> clazz) {
        String className = clazz.getName();
        Assert.assertTrue(className + " not a Ceylon class", isCeylonClass(clazz));
        Assert.assertTrue(className + " not a Value Type", isValueType(clazz));
        Method[] methods = clazz.getMethods();
        for (Method m : methods) {
            validateVTMethod(clazz, m);
        }
    }

    private boolean isCeylonClass(Class<?> clazz) {
        return clazz.getAnnotation(Ceylon.class) != null;
    }
    
    private boolean isValueType(Class<?> clazz) {
        return clazz.getAnnotation(ValueType.class) != null;
    }
    
    // Check if the given method should have a static companion method
    // and if so check that it exists and adheres to all the rules
    private void validateVTMethod(Class<?> clazz, Method classMethod) {
        if (classMethod.getAnnotation(Ignore.class) != null
                || (classMethod.getModifiers() & Modifier.STATIC) != 0
                || (classMethod.getModifiers() & Modifier.VOLATILE) != 0
                || classMethod.getDeclaringClass() == Object.class) {
            // We skip static methods and the ones marked with @Ignore
            // We also skip "volatile" methods, whatever they are
            // And finally we skip all methods defined on Object
            return;
        }
        validateVTMethodUnchecked(clazz, classMethod, classMethod);
        
        // See if it has any overloads that need checking
        Method[] overloads = findVTMethodOverloads(clazz, classMethod);
        for (Method m : overloads) {
            if (m.getAnnotation(Ignore.class) == null
                    || (m.getModifiers() & Modifier.STATIC) != 0
                    || (m.getModifiers() & Modifier.VOLATILE) != 0) {
                // We skip static methods and "volatile" methods
                // and those NOT marked with @Ignore
                continue;
            }
            validateVTMethodUnchecked(clazz, m, classMethod);
        }
    }
    
    // Check if the given method should have a static companion method
    // and if so check that it exists and adheres to all the rules
    private void validateVTMethodUnchecked(Class<?> clazz, Method classMethod, Method typedMethod) {
        String mthName = clazz.getName() + "::" + classMethod.getName() + "()";
        System.err.println(mthName);
        Assert.assertTrue("Overloaded method " + mthName + " can't have more parameters than the method it overloads", classMethod.getParameterTypes().length <= typedMethod.getParameterTypes().length);
        Method staticMethod = findStaticCompanionMethod(clazz, classMethod, typedMethod);
        Assert.assertNotNull("Static companion for " + mthName + " not found", staticMethod);
        TypeInfo returnTypeInfo = typedMethod.getAnnotation(TypeInfo.class);
        Assert.assertEquals("Returns types for static and class methods " + mthName + " do not coincide", getUnboxedType(classMethod.getReturnType(), returnTypeInfo), staticMethod.getReturnType());
    }

    // Tries to find the companion method for the given class method
    // by looking for a static method with the same parameters plus
    // an extra first parameter. Also the static version will always
    // have the "unboxed" version of any Value Type that appears as
    // either parameter or return type
    private Method findStaticCompanionMethod(Class<?> clazz, Method classMethod, Method typedMethod) {
        Class<?>[] instancePTs = classMethod.getParameterTypes();
        Annotation[][] instanceAnnos = typedMethod.getParameterAnnotations();
        TypeParameters tps = typedMethod.getAnnotation(TypeParameters.class);
        int tpsCount = (tps != null) ? tps.value().length : 0;
        Class<?>[] staticPTs = new Class<?>[instancePTs.length + 1];
        staticPTs[tpsCount] = getUnboxedType(clazz, clazz.getAnnotation(TypeInfo.class));
        for (int i = 0; i < instancePTs.length; i++) {
            TypeInfo typeInfo = findAnnotation(instanceAnnos[i], TypeInfo.class);
            int idx = (i < tpsCount) ? i : i + 1;
            staticPTs[idx] = getUnboxedType(instancePTs[i], typeInfo);
        }
        try {
            return clazz.getMethod(classMethod.getName(), staticPTs);
        } catch (NoSuchMethodException|SecurityException e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T findAnnotation(Annotation[] annotations, Class<T> annoClass) {
        for (Annotation a : annotations) {
            if (a.annotationType() == annoClass) {
                return (T)a;
            }
        }
        return null;
    }

    // Given a type this method returns the unboxed version.
    // For this the type must be a Ceylon Value Type. It's "unboxed" type
    // will be taken from the parameter of its instance() method.
    // Also if the type has associated TypeInfo the type it defines
    // must be exactly the type of the Value Type itself otherwise
    // no unboxing will be performed
    private Class<?> getUnboxedType(Class<?> clazz, TypeInfo typeInfo) {
        if (isCeylonClass(clazz) && isValueType(clazz)) {
            if (typeInfo != null) {
                // If we have Ceylon type information available we will only
                // unbox if it's exactly the same as the Value Type itself
                String type = typeInfo.value().replace("::", ".");
                if (!clazz.getName().equals(type)) {
                    return clazz;
                }
            }
            Method instanceMethod = findInstanceMethod(clazz);
            Assert.assertNotNull("Required static method instance() for " + clazz.getName() + " not found", instanceMethod);
            Class<?>[] pts = instanceMethod.getParameterTypes();
            if (pts.length == 1) {
                return pts[0];
            }
        }
        return clazz;
    }

    // Tries to find overloads of the given method
    private Method[] findVTMethodOverloads(Class<?> clazz, Method classMethod) {
        ArrayList<Method> overrides = new ArrayList<Method>();
        Method[] methods = clazz.getMethods();
        for (Method m : methods) {
            if (!m.equals(classMethod)
                    && m.getName().equals(classMethod.getName())
                    && (m.getModifiers() & Modifier.STATIC) == 0) {
                overrides.add(m);
            }
        }
        Method[] result = new Method[overrides.size()];
        return overrides.toArray(result);
    }

    // Finds the instance() method of a Value Type
    private Method findInstanceMethod(Class<?> clazz) {
        Method[] methods = clazz.getMethods();
        for (Method m : methods) {
            if ("instance".equals(m.getName()) && (m.getModifiers() & Modifier.STATIC) != 0) {
                return m;
            }
        }
        return null;
    }
}
