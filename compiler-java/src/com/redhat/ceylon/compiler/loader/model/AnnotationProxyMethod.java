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

package com.redhat.ceylon.compiler.loader.model;

import com.redhat.ceylon.model.typechecker.model.Method;

/**
 * Used for annotation interop.
 * 
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class AnnotationProxyMethod extends Method {
    
    private AnnotationProxyClass proxyClass;
    
    private OutputElement annotationTarget;
    
    public AnnotationProxyClass getProxyClass() {
        return proxyClass;
    }
    
    public void setProxyClass(AnnotationProxyClass proxyClass) {
        this.proxyClass = proxyClass;
    }

    public void setAnnotationTarget(OutputElement annotationTarget) {
        this.annotationTarget = annotationTarget;
    }
    
    /**
     * If this is a disambiguating proxy annotation method, then this is the 
     * Java program element that the constructor targets. Otherwise null
     */
    public OutputElement getAnnotationTarget() {
        return this.annotationTarget;
    }

}
