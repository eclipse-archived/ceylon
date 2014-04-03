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
package com.redhat.ceylon.compiler.loader.model;

import java.util.Collections;
import java.util.List;

import com.redhat.ceylon.compiler.typechecker.model.Functional;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.Unit;

/**
 * Wrapper class which pretends a function or value is an interface, so that they can
 * be used to qualify local types in runtime reified checks.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class FunctionOrValueInterface extends Interface {

    private final TypedDeclaration declaration;

    public FunctionOrValueInterface(TypedDeclaration declaration){
        this.declaration = declaration;
    }
    
    @Override
    public String getQualifier() {
        return declaration.getQualifier();
    }
    
    @Override
    public String getName() {
        return declaration.getName();
    }
    
    @Override
    public Scope getContainer() {
        return declaration.getContainer();
    }
    
    @Override
    public List<TypeParameter> getTypeParameters() {
        return declaration instanceof Functional 
                ? ((Functional) declaration).getTypeParameters() 
                : Collections.<TypeParameter>emptyList();
    }

    @Override
    public Unit getUnit() {
        return declaration.getUnit();
    }
    
    public TypedDeclaration getUnderlyingDeclaration() {
        return declaration;
    }
}
