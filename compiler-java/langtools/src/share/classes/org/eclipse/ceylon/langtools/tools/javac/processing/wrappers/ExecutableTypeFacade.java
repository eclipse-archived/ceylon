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

import org.eclipse.ceylon.javax.lang.model.type.ExecutableType;
import org.eclipse.ceylon.javax.lang.model.type.TypeMirror;
import org.eclipse.ceylon.javax.lang.model.util.Elements;

public class ExecutableTypeFacade extends TypeMirrorFacade implements javax.lang.model.type.ExecutableType {

    public ExecutableTypeFacade(ExecutableType type) {
        super(type);
    }

    @Override
    public List<? extends javax.lang.model.type.TypeMirror> getParameterTypes() {
        return Facades.facadeTypeMirrorList(((ExecutableType)f).getParameterTypes());
    }

    @Override
    public javax.lang.model.type.TypeMirror getReturnType() {
        return Facades.facade(((ExecutableType)f).getReturnType());
    }

    @Override
    public List<? extends javax.lang.model.type.TypeMirror> getThrownTypes() {
        return Facades.facadeTypeMirrorList(((ExecutableType)f).getThrownTypes());
    }

    @Override
    public List<? extends javax.lang.model.type.TypeVariable> getTypeVariables() {
        return Facades.facadeTypeVariableList(((ExecutableType)f).getTypeVariables());
    }

    // Java 8 method
//    @Override
    public javax.lang.model.type.TypeMirror getReceiverType() {
        // must use reflection for it to work on Java 7
        try {
            Method method = ExecutableType.class.getMethod("getReceiverType");
            return Facades.facade((TypeMirror) method.invoke(f));
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ExecutableTypeFacade == false)
            return false;
        return f.equals(((ExecutableTypeFacade)obj).f);
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
