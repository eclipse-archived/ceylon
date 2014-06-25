package com.redhat.ceylon.compiler.java.test.model;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.junit.Assert;
import org.junit.Test;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.ValueType;

public class ValueTypeTest {

    @Test
    public void testValueTypes() {
        validateValueType(ceylon.language.Boolean.class);
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
        String mthName = clazz.getName() + "::" + classMethod.getName() + "()";
        Method staticMethod = findStaticCompanionMethod(clazz, classMethod);
        Assert.assertNotNull("Static companion for " + mthName + " not found", staticMethod);
    }

    private Method findStaticCompanionMethod(Class<?> clazz, Method classMethod) {
        Class<?>[] instancePTs = classMethod.getParameterTypes();
        Class<?>[] staticPTs = new Class<?>[instancePTs.length + 1];
        staticPTs[0] = getUnboxedType(clazz);
        for (int i = 0; i < instancePTs.length; i++) {
            staticPTs[i + 1] = getUnboxedType(instancePTs[i]);
        }
        try {
            return clazz.getMethod(classMethod.getName(), staticPTs);
        } catch (NoSuchMethodException|SecurityException e) {
            return null;
        }
    }

    private Class<?> getUnboxedType(Class<?> clazz) {
        if (isCeylonClass(clazz) && isValueType(clazz)) {
            Method instanceMethod = findInstanceMethod(clazz);
            Assert.assertNotNull("Required static method instance() for " + clazz.getName() + " not found", instanceMethod);
            Class<?>[] pts = instanceMethod.getParameterTypes();
            if (pts.length == 1) {
                return pts[0];
            }
        }
        return clazz;
    }

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
