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
import java.util.List;

import com.redhat.ceylon.compiler.modelloader.mirror.AnnotationMirror;
import com.redhat.ceylon.compiler.modelloader.mirror.TypeParameterMirror;
import com.sun.tools.javac.code.Attribute.Compound;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Symbol.TypeSymbol;

public class JavacUtil {

    public static AnnotationMirror getAnnotation(Symbol symbol, String type) {
        com.sun.tools.javac.util.List<Compound> annotations = symbol.getAnnotationMirrors();
        for(Compound annotation : annotations){
            if(annotation.type.tsym.getQualifiedName().toString().equals(type))
                return new JavacAnnotation(annotation);
        }
        return null;
    }

    public static List<TypeParameterMirror> getTypeParameters(Symbol symbol) {
        com.sun.tools.javac.util.List<TypeSymbol> typeParameters = symbol.getTypeParameters();
        List<TypeParameterMirror> ret = new ArrayList<TypeParameterMirror>(typeParameters.size());
        for(TypeSymbol typeParameter : typeParameters)
            ret.add(new JavacTypeParameter(typeParameter));
        return ret;
    }

}
