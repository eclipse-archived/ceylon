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
package com.redhat.ceylon.compiler.java.codegen;

import com.redhat.ceylon.model.typechecker.model.Type;
import com.redhat.ceylon.model.typechecker.model.ProducedTypedReference;
import com.redhat.ceylon.model.typechecker.model.TypedDeclaration;

public class CompilerBoxingDeclarationVisitor extends BoxingDeclarationVisitor {
    private AbstractTransformer transformer;
    
    public CompilerBoxingDeclarationVisitor(AbstractTransformer transformer){
        this.transformer = transformer;
    }

    @Override
    protected boolean isCeylonBasicType(Type type) {
        return transformer.isCeylonBasicType(type);
    }

    @Override
    protected boolean isNull(Type type) {
        return transformer.isNull(type);
    }

    @Override
    protected boolean isObject(Type type) {
        return transformer.isCeylonObject(type);
    }

    @Override
    protected boolean willEraseToObject(Type type) {
        return transformer.willEraseToObject(type);
    }

    @Override
    protected boolean isCallable(Type type) {
        return transformer.isCeylonCallable(type);
    }

    @Override
    protected boolean hasErasure(Type type) {
        return transformer.hasErasure(type);
    }

    @Override
    protected boolean isRaw(Type type) {
        return transformer.isTurnedToRaw(type);
    }

    @Override
    protected boolean isWideningTypedDeclaration(TypedDeclaration typedDeclaration) {
        return transformer.isWideningTypeDecl(typedDeclaration);
    }

    @Override
    protected boolean hasSubstitutedBounds(Type type) {
        return transformer.hasSubstitutedBounds(type);
    }
}
