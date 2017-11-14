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

import javax.lang.model.element.Name;

public class NameFacade implements Name {

    private org.eclipse.ceylon.javax.lang.model.element.Name f;

    public NameFacade(org.eclipse.ceylon.javax.lang.model.element.Name f) {
        this.f = f;
    }

    @Override
    public char charAt(int arg0) {
        return f.charAt(arg0);
    }

    @Override
    public int length() {
        return f.length();
    }

    @Override
    public CharSequence subSequence(int arg0, int arg1) {
        return f.subSequence(arg0, arg1);
    }

    @Override
    public boolean contentEquals(CharSequence arg0) {
        return f.contentEquals(arg0);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof NameFacade == false)
            return false;
        return f.equals(((NameFacade)obj).f);
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
