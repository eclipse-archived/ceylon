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

import java.util.Map;

import com.redhat.ceylon.compiler.loader.mirror.AnnotationMirror;
import com.redhat.ceylon.compiler.loader.mirror.TypeMirror;
import com.redhat.ceylon.compiler.loader.mirror.VariableMirror;
import com.sun.tools.javac.code.Symbol.VarSymbol;

public class JavacVariable implements VariableMirror {

    private VarSymbol varSymbol;
    
    private TypeMirror type;
    private Map<String, AnnotationMirror> annotations;

    public JavacVariable(VarSymbol varSymbol) {
        this.varSymbol = varSymbol;
    }
    
    public String toString() {
        return getClass().getSimpleName() + " of " + varSymbol;
    }

    @Override
    public AnnotationMirror getAnnotation(String type) {
        if (annotations == null) {
            annotations = JavacUtil.getAnnotations(varSymbol);
        }
        return annotations.get(type);
    }

    @Override
    public TypeMirror getType() {
        if (type == null) {
            type = new JavacType(varSymbol.type);
        }
        return type;
    }

    @Override
    public String getName() {
        return varSymbol.name.toString();
    }
}
