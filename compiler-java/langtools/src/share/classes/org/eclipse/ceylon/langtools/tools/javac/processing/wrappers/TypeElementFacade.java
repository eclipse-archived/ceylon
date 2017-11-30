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

import org.eclipse.ceylon.javax.lang.model.element.TypeElement;

public class TypeElementFacade extends ElementFacade implements javax.lang.model.element.TypeElement {

    public TypeElementFacade(TypeElement f) {
        super(f);
    }

    @Override
    public List<? extends javax.lang.model.type.TypeMirror> getInterfaces() {
        return Facades.facadeTypeMirrorList(((TypeElement)f).getInterfaces());
    }

    @Override
    public javax.lang.model.element.NestingKind getNestingKind() {
        return Facades.facade(((TypeElement)f).getNestingKind());
    }

    @Override
    public javax.lang.model.element.Name getQualifiedName() {
        return Facades.facade(((TypeElement)f).getQualifiedName());
    }

    @Override
    public javax.lang.model.type.TypeMirror getSuperclass() {
        return Facades.facade(((TypeElement)f).getSuperclass());
    }

    @Override
    public List<? extends javax.lang.model.element.TypeParameterElement> getTypeParameters() {
        return Facades.facadeTypeParameterElementList(((TypeElement)f).getTypeParameters());
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof TypeElementFacade == false)
            return false;
        return f.equals(((TypeElementFacade)obj).f);
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
