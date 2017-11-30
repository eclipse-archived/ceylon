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

import org.eclipse.ceylon.javax.tools.JavaFileObject;

public class JavaFileObjectFacade extends FileObjectFacade implements javax.tools.JavaFileObject {

    public JavaFileObjectFacade(JavaFileObject f) {
        super(f);
    }

    @Override
    public javax.lang.model.element.Modifier getAccessLevel() {
        return Facades.facade(((JavaFileObject)f).getAccessLevel());
    }

    @Override
    public Kind getKind() {
        return Facades.facade(((JavaFileObject)f).getKind());
    }

    @Override
    public javax.lang.model.element.NestingKind getNestingKind() {
        return Facades.facade(((JavaFileObject)f).getNestingKind());
    }

    @Override
    public boolean isNameCompatible(String arg0, Kind arg1) {
        return ((JavaFileObject)f).isNameCompatible(arg0, Wrappers.wrap(arg1));
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof JavaFileObjectFacade == false)
            return false;
        return f.equals(((JavaFileObjectFacade)obj).f);
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
