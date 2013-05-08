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
import com.redhat.ceylon.compiler.loader.mirror.FieldMirror;
import com.redhat.ceylon.compiler.loader.mirror.TypeMirror;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Symbol.VarSymbol;
import com.sun.tools.javac.code.Type;

public class JavacField implements FieldMirror {

    private VarSymbol fieldSymbol;
    private Map<String, AnnotationMirror> annotations;
    private TypeMirror type;

    public JavacField(VarSymbol sym) {
        this.fieldSymbol = sym;
    }
    
    public String toString() {
        return getClass().getSimpleName() + " of " + fieldSymbol;
    }

    @Override
    public AnnotationMirror getAnnotation(String type) {
        if (annotations == null) {
            annotations = JavacUtil.getAnnotations(fieldSymbol);
        }
        return annotations.get(type);
    }

    @Override
    public String getName() {
        return fieldSymbol.name.toString();
    }

    @Override
    public boolean isStatic() {
        return fieldSymbol.isStatic();
    }

    @Override
    public boolean isPublic() {
        return (fieldSymbol.flags() & Flags.PUBLIC) != 0;
    }
    
    @Override
    public boolean isProtected() {
        return (fieldSymbol.flags() & Flags.PROTECTED) != 0;
    }
    
    @Override
    public boolean isDefaultAccess() {
        return (fieldSymbol.flags() & (Flags.PROTECTED | Flags.PUBLIC | Flags.PRIVATE)) == 0;
    }

    @Override
    public boolean isFinal() {
        return (fieldSymbol.flags() & Flags.FINAL) != 0;
    }

    @Override
    public TypeMirror getType() {
        if(type == null){
            Type retType = fieldSymbol.type;
            if (retType != null) {
                type = new JavacType(retType);
            }
        }
        return type;
    }

}
