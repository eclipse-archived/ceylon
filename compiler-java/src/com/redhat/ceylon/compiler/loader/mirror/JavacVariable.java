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
package com.redhat.ceylon.compiler.loader.mirror;

import com.redhat.ceylon.compiler.modelloader.mirror.AnnotationMirror;
import com.redhat.ceylon.compiler.modelloader.mirror.TypeMirror;
import com.redhat.ceylon.compiler.modelloader.mirror.VariableMirror;
import com.sun.tools.javac.code.Symbol.VarSymbol;

public class JavacVariable implements VariableMirror {

    private VarSymbol varSymbol;

    public JavacVariable(VarSymbol varSymbol) {
        this.varSymbol = varSymbol;
    }

    @Override
    public AnnotationMirror getAnnotation(String type) {
        return JavacUtil.getAnnotation(varSymbol, type);
    }

    @Override
    public TypeMirror getType() {
        return new JavacType(varSymbol.type);
    }

    @Override
    public String getName() {
        return varSymbol.name.toString();
    }

}
