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

package com.redhat.ceylon.compiler.java.loader;

import com.redhat.ceylon.compiler.java.tools.LanguageCompiler;
import com.redhat.ceylon.compiler.typechecker.context.Context;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.IntersectionType;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.UnionType;
import com.redhat.ceylon.compiler.typechecker.model.Unit;
import com.redhat.ceylon.compiler.typechecker.model.Util;

public class TypeFactory extends Unit {
    private Context context;

    public static TypeFactory instance(com.sun.tools.javac.util.Context context) {
        TypeFactory instance = context.get(TypeFactory.class);
        if (instance == null) {
            instance = new TypeFactory(LanguageCompiler.getCeylonContextInstance(context));
            context.put(TypeFactory.class, instance);
        }
        return instance;
    }
    
    public TypeFactory(Context context) {
        this.context = context;
    }
    
    public Context getContext() {
        return context;
    }
    
    /**
     * Gets the declaration of {@code ArraySequence}
     * @return The declaration
     */
    public TypeDeclaration getDefaultSequenceDeclaration() {
        return ((TypeDeclaration) getLanguageModuleDeclaration("ArraySequence"));
    }

    /**
     * Gets the declaration of {@code Iterator}
     * @return The declaration
     */
    public Interface getIteratorDeclaration() {
        return (Interface) ((TypeDeclaration) getLanguageModuleDeclaration("Iterator"));
    }

    /**
     * Determines whether the given ProducedType is a union
     * @param pt 
     * @return whether the type is a union type
     */
    public boolean isUnion(ProducedType pt) {
        TypeDeclaration tdecl = pt.getDeclaration();
        return (tdecl instanceof UnionType && tdecl.getCaseTypes().size() > 1);
    }

    /**
     * Determines whether the given ProducedType is an intersection
     * @param pt 
     * @return whether the type is an intersection type
     */
    public boolean isIntersection(ProducedType pt) {
        TypeDeclaration tdecl = pt.getDeclaration();
        return (tdecl instanceof IntersectionType && tdecl.getSatisfiedTypes().size() > 1);
    }

    /**
     * Returns a ProducedType corresponding to {@code ArraySequence<T>}
     * @param et The ProducedType corresponding to {@code T}
     * @return The ProducedType corresponding to {@code ArraySequence<T>}
     */
    public ProducedType getDefaultSequenceType(ProducedType et) {
        return Util.producedType(getDefaultSequenceDeclaration(), et);
    }

    /**
     * Returns a ProducedType corresponding to {@code Iterator<T>}
     * @param et The ProducedType corresponding to {@code T}
     * @return The ProducedType corresponding to {@code Iterator<T>}
     */
    public ProducedType getIteratorType(ProducedType et) {
        return Util.producedType(getIteratorDeclaration(), et);
    }

    /**
     * Returns a ProducedType corresponding to {@code Range<T>}
     * @param rt The ProducedType corresponding to {@code T}
     * @return The ProducedType corresponding to {@code Range<T>}
     */
    public ProducedType getRangeType(ProducedType rt) {
        return Util.producedType(getRangeDeclaration(), rt);
    }

    public ProducedType getNonemptyIterableType(ProducedType pt) {
        return pt.minus(getEmptyDeclaration()).getSupertype(getIterableDeclaration());
    }
}
