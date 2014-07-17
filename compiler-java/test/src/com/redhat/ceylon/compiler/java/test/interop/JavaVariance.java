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
package com.redhat.ceylon.compiler.java.test.interop;

abstract class Class<T>{
    public abstract T classMethod(T t);
}
interface Interface<T>{
    public T interfaceMethod(T t);
}

public class JavaVariance extends Class<Class<?>> implements Interface<Interface<?>>{

    @Override
    public Interface<?> interfaceMethod(Interface<?> t) {
        return null;
    }

    @Override
    public Class<?> classMethod(Class<?> t) {
        return null;
    }
    
    public <T extends Class<?>> T method(T t){
        return t;
    }
    
    public final Class<?> roField = null;
    public Class<?> rwField = null;
    
    public Class<?> getRoProperty(){ return null; }

    public Class<?> getRwProperty(){ return null; }
    public void setRwProperty(Class<?> v){}
    
    public Class<? extends Object> getRwCovariantProperty(){ return null; }
    public void setRwCovariantProperty(Class<? extends Object> v){ }

    public Class<? super JavaVariance> getRwContravariantProperty(){ return null; }
    public void setRwContravariantProperty(Class<? super JavaVariance> v){ }

    public Class getRwRawProperty(){ return null; }
    public void setRwRawProperty(Class v){ }
}