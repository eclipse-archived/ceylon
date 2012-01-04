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
        if(value.getClass().isArray()){
            Object[] array = (Object[])value;
            List<Object> values = new ArrayList<Object>(array.length);
            for(Object val : array)
                values.add(convertValue(val));
            return values;
        }
        if(value instanceof Annotation){
            return new ReflectionAnnotation((Annotation) value);
        }
        if(value instanceof Enum){
            return ((Enum<?>)value).name();
        }
        return value;
    }

    @Override
    public Object getValue() {
        return getValue("default");
    }

}
