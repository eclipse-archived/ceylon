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
import java.util.Set;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.type.TypeMirror;

public class ElementFacade implements Element {

    protected org.eclipse.ceylon.javax.lang.model.element.Element f;

    public ElementFacade(org.eclipse.ceylon.javax.lang.model.element.Element f) {
        this.f = f;
    }

    @Override
    public <R, P> R accept(ElementVisitor<R, P> v, P p) {
        return f.accept(Wrappers.wrap(v), p);
    }

    @Override
    public TypeMirror asType() {
        return Facades.facade(f.asType());
    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
        return Facades.facadeAnnotation(f.getAnnotation(annotationType));
    }

    @Override
    public List<? extends AnnotationMirror> getAnnotationMirrors() {
        return Facades.facadeAnnotationMirrors(f.getAnnotationMirrors());
    }

    @Override
    public List<? extends Element> getEnclosedElements() {
        return Facades.facadeElementList(f.getEnclosedElements());
    }

    @Override
    public Element getEnclosingElement() {
        return Facades.facade(f.getEnclosingElement());
    }

    @Override
    public ElementKind getKind() {
        return Facades.facade(f.getKind());
    }

    @Override
    public Set<Modifier> getModifiers() {
        return Facades.facadeModifiers(f.getModifiers());
    }

    @Override
    public Name getSimpleName() {
        return Facades.facade(f.getSimpleName());
    }

    // Java 8 method
//    @Override
    public <A extends Annotation> A[] getAnnotationsByType(Class<A> arg0) {
        // must use reflection for it to work on Java 7
        try {
            Method method = org.eclipse.ceylon.javax.lang.model.element.Element.class.getMethod("getAnnotationsByType", Class.class);
            return Facades.facadeAnnotations((A[]) method.invoke(f, arg0));
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ElementFacade == false)
            return false;
        return f.equals(((ElementFacade)obj).f);
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
