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
package com.redhat.ceylon.compiler.loader.refl;

import java.util.ArrayList;
import java.util.List;

import com.redhat.ceylon.compiler.modelloader.refl.ReflType;
import com.redhat.ceylon.compiler.modelloader.refl.ReflTypeParameter;
import com.sun.tools.javac.code.Symbol.TypeSymbol;
import com.sun.tools.javac.code.Type;

public class JavacTypeParameter implements ReflTypeParameter {

    private TypeSymbol typeSymbol;

    public JavacTypeParameter(TypeSymbol typeSymbol) {
        this.typeSymbol = typeSymbol;
    }

    @Override
    public String getName() {
        return typeSymbol.getQualifiedName().toString();
    }

    @Override
    public List<ReflType> getBounds() {
        com.sun.tools.javac.util.List<Type> bounds = typeSymbol.getBounds();
        List<ReflType> ret = new ArrayList<ReflType>(bounds.size());
        for(Type type : bounds)
            ret.add(new JavacType(type));
        return ret;

    }

}
