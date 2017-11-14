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

import java.util.Map;

import org.eclipse.ceylon.javax.lang.model.element.AnnotationMirror;

public class AnnotationMirrorFacade implements javax.lang.model.element.AnnotationMirror {

    protected AnnotationMirror f;

    public AnnotationMirrorFacade(AnnotationMirror f) {
        this.f = f;
    }

    @Override
    public javax.lang.model.type.DeclaredType getAnnotationType() {
        return Facades.facade(f.getAnnotationType());
    }

    @Override
    public Map<? extends javax.lang.model.element.ExecutableElement, ? extends javax.lang.model.element.AnnotationValue> getElementValues() {
        return Facades.facadeElementValues(f.getElementValues());
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof AnnotationMirrorFacade == false)
            return false;
        return f.equals(((AnnotationMirrorFacade)obj).f);
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
