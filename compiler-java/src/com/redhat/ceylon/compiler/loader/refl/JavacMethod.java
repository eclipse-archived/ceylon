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

import com.redhat.ceylon.compiler.modelloader.refl.ReflAnnotation;
import com.redhat.ceylon.compiler.modelloader.refl.ReflMethod;
import com.redhat.ceylon.compiler.modelloader.refl.ReflType;
import com.redhat.ceylon.compiler.modelloader.refl.ReflTypeParameter;
import com.redhat.ceylon.compiler.modelloader.refl.ReflVariable;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Symbol.MethodSymbol;
import com.sun.tools.javac.code.Symbol.VarSymbol;
import com.sun.tools.javac.code.Type;

public class JavacMethod implements ReflMethod {

    public MethodSymbol methodSymbol;

    public JavacMethod(MethodSymbol sym) {
        this.methodSymbol = sym;
    }

    @Override
    public ReflAnnotation getAnnotation(String type) {
        return JavacUtil.getAnnotation(methodSymbol, type);
    }

    @Override
    public String getName() {
        return methodSymbol.name.toString();
    }

    @Override
    public boolean isStatic() {
        return methodSymbol.isStatic();
    }

    @Override
    public boolean isPublic() {
        return (methodSymbol.flags() & Flags.PUBLIC) != 0;
    }

    @Override
    public boolean isConstructor() {
        return methodSymbol.isConstructor();
    }

    @Override
    public List<ReflVariable> getParameters() {
        com.sun.tools.javac.util.List<VarSymbol> typeParameters = methodSymbol.getParameters();
        List<ReflVariable> ret = new ArrayList<ReflVariable>(typeParameters.size());
        for(VarSymbol typeParameter : typeParameters)
            ret.add(new JavacVariable(typeParameter));
        return ret;
    }

    @Override
    public boolean isAbstract() {
        return (methodSymbol.flags() & Flags.ABSTRACT) != 0;
    }

    @Override
    public boolean isFinal() {
        return (methodSymbol.flags() & Flags.FINAL) != 0;
    }

    @Override
    public ReflType getReturnType() {
        Type returnType = methodSymbol.getReturnType();
        return returnType != null ? new JavacType(returnType) : null;
    }

    @Override
    public List<ReflTypeParameter> getTypeParameters() {
        return JavacUtil.getTypeParameters(methodSymbol);
    }
    
}
