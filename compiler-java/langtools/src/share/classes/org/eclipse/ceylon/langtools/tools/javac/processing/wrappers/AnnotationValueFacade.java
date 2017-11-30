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
package org.eclipse.ceylon.langtools.tools.javac.processing.wrappers;

import java.util.List;

import javax.lang.model.element.AnnotationValueVisitor;

import org.eclipse.ceylon.javax.lang.model.element.AnnotationMirror;
import org.eclipse.ceylon.javax.lang.model.element.AnnotationValue;
import org.eclipse.ceylon.javax.lang.model.element.VariableElement;
import org.eclipse.ceylon.javax.lang.model.type.TypeMirror;

public class AnnotationValueFacade implements javax.lang.model.element.AnnotationValue {

    protected AnnotationValue f;

    public AnnotationValueFacade(AnnotationValue f) {
        this.f = f;
    }

    @Override
    public <R, P> R accept(AnnotationValueVisitor<R, P> v, P p) {
        return f.accept(Wrappers.wrap(v), p);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object getValue() {
        /* <ul><li> a wrapper class (such as {@link Integer}) for a primitive type
        *     <li> {@code String}
        *     <li> {@code TypeMirror}
        *     <li> {@code VariableElement} (representing an enum constant)
        *     <li> {@code AnnotationMirror}
        *     <li> {@code List<? extends AnnotationValue>}
        *              (representing the elements, in declared order, if the value is an array)
        *   */
        Object value = f.getValue();
        if(value == null)
            return null;
        if(value instanceof String
                || value instanceof Boolean
                || value instanceof Character
                || value instanceof Byte
                || value instanceof Short
                || value instanceof Integer
                || value instanceof Long
                || value instanceof Float
                || value instanceof Double
                )
            return value;
        if(value instanceof TypeMirror)
            return Facades.facade((TypeMirror)value);
        if(value instanceof VariableElement)
            return Facades.facade((VariableElement)value);
        if(value instanceof AnnotationMirror)
            return Facades.facade((AnnotationMirror)value);
        if(value instanceof List)
            return Facades.facadeAnnotationValueList((List<? extends AnnotationValue>) value);
        throw new RuntimeException("Don't know how to facade value type "+value);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof AnnotationValueFacade == false)
            return false;
        return f.equals(((AnnotationValueFacade)obj).f);
    }
    
    @Override
    public int hashCode() {
        return f.hashCode();
    }
    
    @Override
    public String toString() {
        return f.toString();
    }
}
