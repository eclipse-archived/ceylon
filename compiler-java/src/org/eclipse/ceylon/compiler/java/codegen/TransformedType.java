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
package org.eclipse.ceylon.compiler.java.codegen;

import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCAnnotation;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCExpression;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCIdent;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCPrimitiveTypeTree;
import org.eclipse.ceylon.langtools.tools.javac.util.List;

/**
 * A type expression and {@code @TypeInfo} and/or nullability annotations
 */
public class TransformedType {
    
    private final JCExpression typeExpression;
    private final JCAnnotation typeInfo;
    private final JCAnnotation nullability;
    
    public TransformedType(JCExpression typeExpression, JCAnnotation typeInfo, JCAnnotation nullability) {
        super();
        this.typeExpression = typeExpression;
        this.typeInfo = typeInfo;
        this.nullability = nullability;
    }
    
    public TransformedType(JCExpression typeExpression, JCAnnotation typeInfo) {
        this(typeExpression, typeInfo, null);
    }
    
    public TransformedType(JCExpression typeExpression) {
        this(typeExpression, null);
    }

    public JCExpression getTypeExpression() {
        return typeExpression;
    }

    public JCAnnotation getTypeInfo() {
        return typeInfo;
    }

    public JCAnnotation getNullability() {
        return nullability;
    }
    
    /**
     * The {@code @TypeInfo}, {@code @Nullable}, and/or {@code @NonNull} 
     * annotations.
     */
    public List<JCAnnotation> getTypeAnnotations() {
        List<JCAnnotation> result = List.nil();
        if (typeInfo != null) {
            result = result.prepend(typeInfo);
        }
        if (nullability != null && !isPrimitiveOrVoid()) {
            result = result.prepend(nullability);
        }
        return result;
    }

    
    public boolean isPrimitive() {
        return isPrimitive(typeExpression);
    }
    
    public static boolean isPrimitive(JCExpression typeExpression) {
        if (typeExpression instanceof JCIdent) {
            JCIdent ident = (JCIdent)typeExpression;
            if (ident.sym != null
                    && ident.sym.type.isPrimitive()) {
                return true;
            }
        } else if (typeExpression instanceof JCPrimitiveTypeTree) {
            return true;
        }
        return false;
    }
    
    public boolean isPrimitiveOrVoid() {
        return isPrimitiveOrVoid(typeExpression);
    }
    
    public static boolean isPrimitiveOrVoid(JCExpression typeExpression) {
        if (typeExpression instanceof JCIdent) {
            JCIdent ident = (JCIdent)typeExpression;
            if (ident.sym != null
                    && ident.sym.type.isPrimitiveOrVoid()) {
                return true;
            }
        } else if (typeExpression instanceof JCPrimitiveTypeTree) {
            return true;
        }
        return false;
    }
}
