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

import java.util.List;

import org.eclipse.ceylon.model.typechecker.model.Type;

public class TypeVisitor {
    public void visitType(Type t) {
        Type qt = t.getQualifyingType();
        if (qt!=null) {
            visitQualifyingType(t, qt);
        }
        if (t.isUnknown()) {
            visitUnknown();
        }
        else if (t.isUnion()) {
            visitUnion(t.getCaseTypes());
        }
        else if (t.isIntersection()) {
            visitIntersection(t.getSatisfiedTypes());
        }
        else if (t.isNothing()) {
            visitNothing();
        }
        else if (t.isTypeAlias()) {
            visitTypeAlias(t);
        }
        else if (t.isClass()) {
            visitClass(t);
        }
        else if (t.isInterface()) {
            visitInterface(t);
        }
        
    }
    
    public void visitClassOrInterface(Type t) {
        if (!t.getTypeArgumentList().isEmpty()) {
            visitTypeArguments(t, t.getTypeArgumentList());
        }
    }
    
    public void visitClass(Type t) {
        visitClassOrInterface(t);
    }

    public void visitInterface(Type t) {
        visitClassOrInterface(t);
    }

    public void visitTypeAlias(Type t) {
    }

    public void visitTypeArguments(Type typeConstructor, List<Type> typeArguments) {
        for (Type at: typeArguments) {
            visitType(at);
        }
    }

    public void visitNothing() {
    }

    public void visitIntersection(List<Type> intersectionTypes) {
        for (Type st: intersectionTypes) {
            visitType(st);
        }
    }

    public void visitUnion(List<Type> unionTypes) {
        for (Type ct: unionTypes) {
            visitType(ct);
        }
    }

    public void visitUnknown() {
    }

    public void visitQualifyingType(Type qualified, Type qualifying) {
        visitType(qualifying);
    }
    
}
