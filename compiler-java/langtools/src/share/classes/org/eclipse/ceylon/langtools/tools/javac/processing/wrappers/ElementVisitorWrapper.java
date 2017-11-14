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

import org.eclipse.ceylon.javax.lang.model.element.Element;
import org.eclipse.ceylon.javax.lang.model.element.ElementVisitor;
import org.eclipse.ceylon.javax.lang.model.element.ExecutableElement;
import org.eclipse.ceylon.javax.lang.model.element.PackageElement;
import org.eclipse.ceylon.javax.lang.model.element.TypeElement;
import org.eclipse.ceylon.javax.lang.model.element.TypeParameterElement;
import org.eclipse.ceylon.javax.lang.model.element.VariableElement;

public class ElementVisitorWrapper<R, P> implements ElementVisitor<R, P> {

    private javax.lang.model.element.ElementVisitor<R, P> d;

    public ElementVisitorWrapper(javax.lang.model.element.ElementVisitor<R, P> d) {
        this.d = d;
    }

    @Override
    public R visit(Element e, P p) {
        return d.visit(Facades.facade(e), p);
    }

    @Override
    public R visit(Element e) {
        return d.visit(Facades.facade(e));
    }

    @Override
    public R visitPackage(PackageElement e, P p) {
        return d.visitPackage(Facades.facade(e), p);
    }

    @Override
    public R visitType(TypeElement e, P p) {
        return d.visitType(Facades.facade(e), p);
    }

    @Override
    public R visitVariable(VariableElement e, P p) {
        return d.visitVariable(Facades.facade(e), p);
    }

    @Override
    public R visitExecutable(ExecutableElement e, P p) {
        return d.visitExecutable(Facades.facade(e), p);
    }

    @Override
    public R visitTypeParameter(TypeParameterElement e, P p) {
        return d.visitTypeParameter(Facades.facade(e), p);
    }

    @Override
    public R visitUnknown(Element e, P p) {
        try{
            return d.visitUnknown(Facades.facade(e), p);
        }catch(javax.lang.model.element.UnknownElementException x){
            throw Wrappers.wrap(x);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ElementVisitorWrapper == false)
            return false;
        return d.equals(((ElementVisitorWrapper)obj).d);
    }
    
    @Override
    public int hashCode() {
        return d.hashCode();
    }
    
    @Override
    public String toString() {
        return d.toString();
    }
}
