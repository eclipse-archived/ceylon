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

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.eclipse.ceylon.javax.lang.model.element.AnnotationMirror;
import org.eclipse.ceylon.javax.lang.model.type.TypeMirror;

public class TypeMirrorFacade implements javax.lang.model.type.TypeMirror {

    protected TypeMirror f;

    public TypeMirrorFacade(TypeMirror f) {
        this.f = f;
    }

    @Override
    public <R, P> R accept(javax.lang.model.type.TypeVisitor<R, P> v, P p) {
        return f.accept(Wrappers.wrap(v), p);
    }

    @Override
    public javax.lang.model.type.TypeKind getKind() {
        return Facades.facade(f.getKind());
    }

    // Java 8 method    
//    @Override
    public <A extends Annotation> A getAnnotation(Class<A> arg0) {
        // must use reflection for it to work on Java 7
        try {
            Method method = TypeMirror.class.getMethod("getAnnotation", Class.class);
            return Facades.facadeAnnotation((A) method.invoke(f, arg0));
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    // Java 8 method
//    @Override
    public List<? extends javax.lang.model.element.AnnotationMirror> getAnnotationMirrors() {
        // must use reflection for it to work on Java 7
        try {
            Method method = TypeMirror.class.getMethod("getAnnotationMirrors");
            return Facades.facadeAnnotationMirrors((List<AnnotationMirror>) method.invoke(f));
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
    
    // Java 8 method
//    @Override
    public <A extends Annotation> A[] getAnnotationsByType(Class<A> arg0) {
        // must use reflection for it to work on Java 7
        try {
            Method method = TypeMirror.class.getMethod("getAnnotationsByType", Class.class);
            return Facades.facadeAnnotations((A[]) method.invoke(f, arg0));
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof TypeMirrorFacade == false)
            return false;
        return f.equals(((TypeMirrorFacade)obj).f);
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
