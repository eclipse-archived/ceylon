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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

import com.redhat.ceylon.compiler.loader.mirror.AnnotationMirror;
import com.redhat.ceylon.compiler.loader.mirror.FieldMirror;
import com.redhat.ceylon.compiler.loader.mirror.TypeMirror;

public class ReflectionField implements FieldMirror {

    private Field field;
    private ReflectionType type;
    private Map<String, AnnotationMirror> annotations;

    public ReflectionField(Field field) {
        this.field = field;
    }

    @Override
    public AnnotationMirror getAnnotation(String type) {
        return getAnnotations().get(type);
    }
    
    private Map<String, AnnotationMirror> getAnnotations() {
        // profiling revealed we need to cache this
        if(annotations == null){
            annotations = ReflectionUtils.getAnnotations(field);
        }
        return annotations;
    }

    @Override
    public String getName() {
        return field.getName();
    }

    @Override
    public boolean isStatic() {
        return Modifier.isStatic(field.getModifiers());
    }

    @Override
    public boolean isPublic() {
        return Modifier.isPublic(field.getModifiers());
    }
    
    @Override
    public boolean isProtected() {
        return Modifier.isProtected(field.getModifiers());
    }
    
    @Override
    public boolean isDefaultAccess() {
        return !Modifier.isPrivate(field.getModifiers())
                && !Modifier.isPublic(field.getModifiers())
                && !Modifier.isProtected(field.getModifiers());
    }

    @Override
    public boolean isFinal() {
        return Modifier.isFinal(field.getModifiers());
    }

    @Override
    public TypeMirror getType() {
        if(type != null)
            return type;
        type = new ReflectionType(field.getGenericType());
        return type;
    }

    @Override
    public String toString() {
        return "[ReflectionField: "+field.toString()+"]";
    }
}
