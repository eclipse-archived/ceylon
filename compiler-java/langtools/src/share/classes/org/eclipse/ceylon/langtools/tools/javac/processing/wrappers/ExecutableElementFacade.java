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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.eclipse.ceylon.javax.lang.model.element.ExecutableElement;
import org.eclipse.ceylon.javax.lang.model.type.TypeMirror;
import org.eclipse.ceylon.javax.lang.model.util.Elements;

public class ExecutableElementFacade extends ElementFacade implements javax.lang.model.element.ExecutableElement {

    public ExecutableElementFacade(ExecutableElement f) {
        super(f);
    }

    @Override
    public javax.lang.model.element.AnnotationValue getDefaultValue() {
        return Facades.facade(((ExecutableElement)f).getDefaultValue());
    }

    @Override
    public List<? extends javax.lang.model.element.VariableElement> getParameters() {
        return Facades.facadeVariableElementList(((ExecutableElement)f).getParameters());
    }

    @Override
    public javax.lang.model.type.TypeMirror getReturnType() {
        return Facades.facade(((ExecutableElement)f).getReturnType());
    }

    @Override
    public List<? extends javax.lang.model.type.TypeMirror> getThrownTypes() {
        return Facades.facadeTypeMirrorList(((ExecutableElement)f).getThrownTypes());
    }

    @Override
    public List<? extends javax.lang.model.element.TypeParameterElement> getTypeParameters() {
        return Facades.facadeTypeParameterElementList(((ExecutableElement)f).getTypeParameters());
    }

    @Override
    public boolean isVarArgs() {
        return ((ExecutableElement)f).isVarArgs();
    }

    // Java 8 method
//    @Override
    public javax.lang.model.type.TypeMirror getReceiverType() {
        // must use reflection for it to work on Java 7
        try {
            Method method = ExecutableElement.class.getMethod("getReceiverType");
            return Facades.facade((TypeMirror) method.invoke(f));
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    // Java 8 method
//    @Override
    public boolean isDefault() {
        // must use reflection for it to work on Java 7
        try {
            Method method = ExecutableElement.class.getMethod("isDefault");
            return (Boolean) method.invoke(f);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ExecutableElementFacade == false)
            return false;
        return f.equals(((ExecutableElementFacade)obj).f);
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
