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

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic.Kind;

import org.eclipse.ceylon.javax.annotation.processing.Messager;

public class MessagerFacade implements javax.annotation.processing.Messager {

    private Messager f;

    public MessagerFacade(Messager f) {
        this.f = f;
    }

    @Override
    public void printMessage(Kind arg0, CharSequence arg1) {
        f.printMessage(Wrappers.wrap(arg0), arg1);
    }

    @Override
    public void printMessage(Kind arg0, CharSequence arg1, Element arg2) {
        f.printMessage(Wrappers.wrap(arg0), arg1, Facades.unfacade(arg2));
    }

    @Override
    public void printMessage(Kind arg0, CharSequence arg1, Element arg2, AnnotationMirror arg3) {
        f.printMessage(Wrappers.wrap(arg0), arg1, Facades.unfacade(arg2), Facades.unfacade(arg3));
    }

    @Override
    public void printMessage(Kind arg0, CharSequence arg1, Element arg2, AnnotationMirror arg3, AnnotationValue arg4) {
        f.printMessage(Wrappers.wrap(arg0), arg1, Facades.unfacade(arg2), Facades.unfacade(arg3), Facades.unfacade(arg4));
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof MessagerFacade == false)
            return false;
        return f.equals(((MessagerFacade)obj).f);
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
