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
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.redhat.ceylon.compiler.loader.mirror.AnnotationMirror;

public class ReflectionAnnotation implements AnnotationMirror {

    private Annotation annotation;

    public ReflectionAnnotation(Annotation annotation) {
        this.annotation = annotation;
    }

    @Override
    public Object getValue(String fieldName) {
        try {
            Method method = annotation.getClass().getMethod(fieldName);
            return convertValue(method.invoke(annotation));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Object convertValue(Object value) {
        Class<? extends Object> valueClass = value.getClass();
        if(valueClass.isArray()){
            if (valueClass.getComponentType().isPrimitive()) {
                Class<?> componentType = valueClass.getComponentType();
                if (componentType == short.class) {
                    short[] array = (short[])value;
                    List<Object> values = new ArrayList<Object>(array.length);
                    for(short val : array)
                        values.add(val);
                    return values;
                } else if (componentType == int.class) {
                    int[] array = (int[])value;
                    List<Object> values = new ArrayList<Object>(array.length);
                    for(int val : array)
                        values.add(val);
                    return values;
                } else if (componentType == long.class) {
                    long[] array = (long[])value;
                    List<Object> values = new ArrayList<Object>(array.length);
                    for(long val : array)
                        values.add(val);
                    return values;
                } else if (componentType == boolean.class) {
                    boolean[] array = (boolean[])value;
                    List<Object> values = new ArrayList<Object>(array.length);
                    for(boolean val : array)
                        values.add(val);
                    return values;
                } else if (componentType == char.class) {
                    char[] array = (char[])value;
                    List<Object> values = new ArrayList<Object>(array.length);
                    for(char val : array)
                        values.add(val);
                    return values;
                } else if (componentType == float.class) {
                    float[] array = (float[])value;
                    List<Object> values = new ArrayList<Object>(array.length);
                    for(float val : array)
                        values.add(val);
                    return values;
                } else if (componentType == double.class) {
                    double[] array = (double[])value;
                    List<Object> values = new ArrayList<Object>(array.length);
                    for(double val : array)
                        values.add(val);
                    return values;
                }
            } else {
                Object[] array = (Object[])value;
                List<Object> values = new ArrayList<Object>(array.length);
                for(Object val : array)
                    values.add(convertValue(val));
                return values;
            }
        }
        if(value instanceof Annotation){
            return new ReflectionAnnotation((Annotation) value);
        }
        if(value instanceof Enum){
            return ((Enum<?>)value).name();
        }
        if(value instanceof Class){
            return new ReflectionType((Class<?>)value);
        }
        return value;
    }

    @Override
    public Object getValue() {
        return getValue("value");
    }

}
