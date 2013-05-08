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
package com.redhat.ceylon.compiler.java.loader.mirror;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.redhat.ceylon.compiler.loader.mirror.TypeMirror;
import com.redhat.ceylon.compiler.loader.mirror.TypeParameterMirror;
import com.sun.tools.javac.code.Symbol.TypeSymbol;
import com.sun.tools.javac.code.Type;

public class JavacTypeParameter implements TypeParameterMirror {

    private TypeSymbol typeSymbol;
    private List<TypeMirror> bounds;

    public JavacTypeParameter(TypeSymbol typeSymbol) {
        this.typeSymbol = typeSymbol;
    }
    
    public String toString() {
        return getClass().getSimpleName() + " of " + typeSymbol;
    }

    @Override
    public String getName() {
        return typeSymbol.getQualifiedName().toString();
    }

    @Override
    public List<TypeMirror> getBounds() {
        if (bounds == null) {
            com.sun.tools.javac.util.List<Type> bnds = typeSymbol.getBounds();
            List<TypeMirror> ret = new ArrayList<TypeMirror>(bnds.size());
            for(Type type : bnds)
                ret.add(new JavacType(type));
            bounds = Collections.unmodifiableList(ret);
        }
        return bounds;
    }

}
