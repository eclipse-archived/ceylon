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

import java.util.Iterator;
import java.util.List;

import com.redhat.ceylon.compiler.java.util.Util;
import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ParameterList;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Setter;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AnyAttribute;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AnyMethod;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AttributeSetterDefinition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ClassDefinition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.FunctionArgument;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Variable;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

public class BoxingDeclarationVisitor extends Visitor {
    
    private AbstractTransformer transformer;
    
    public BoxingDeclarationVisitor(AbstractTransformer transformer){
        this.transformer = transformer;
    }
    
    @Override
    public void visit(FunctionArgument that) {
        super.visit(that);
        boxMethod(that.getDeclarationModel());
    }
    
    @Override
    public void visit(AnyMethod that) {
        super.visit(that);
        boxMethod(that.getDeclarationModel());
    }

    private void boxMethod(Method method) {
        // deal with invalid input
        if(method == null)
            return;
        Method refinedMethod = (Method) Util.getTopmostRefinedDeclaration(method);
        // deal with invalid input
        if(refinedMethod == null)
            return;
        List<ParameterList> methodParameterLists = method.getParameterLists();
        List<ParameterList> refinedParameterLists = refinedMethod.getParameterLists();
        // A Callable, which never have primitive parameters
        if (methodParameterLists.size() > 1)
            return;        
        setBoxingState(method, refinedMethod);
        
        // deal with invalid input
        if(methodParameterLists.isEmpty()
                || refinedParameterLists.isEmpty())
            return;

        Iterator<Parameter> parameters = methodParameterLists.get(0).getParameters().iterator();
        for(Parameter refinedParam : refinedParameterLists.get(0).getParameters()){
            Parameter param = parameters.next();
            setBoxingState(param, refinedParam);
        }
    }
    
    @Override
    public void visit(ClassDefinition that) {
        super.visit(that);
        Class klass = that.getDeclarationModel();
        // deal with invalid input
        if(klass == null)
            return;
        List<ParameterList> parameterLists = klass.getParameterLists();
        // deal with invalid input
        if(parameterLists.isEmpty())
            return;
        List<Parameter> parameters = parameterLists.get(0).getParameters();
        for(Parameter param : parameters){
            setBoxingState(param, param);
        }
    }
    
    private void setBoxingState(TypedDeclaration declaration, TypedDeclaration refinedDeclaration) {
        ProducedType type = declaration.getType();
        if(type == null){
            // an error must have already been reported
            return;
        }
        // inherit underlying type constraints
        if(refinedDeclaration != declaration && type.getUnderlyingType() == null)
            type.setUnderlyingType(refinedDeclaration.getType().getUnderlyingType());
        
        // abort if our boxing state has already been set
        if(declaration.getUnboxed() != null)
            return;
        
        if(refinedDeclaration != declaration){
            // make sure refined declarations have already been set
            if(refinedDeclaration.getUnboxed() == null)
                setBoxingState(refinedDeclaration, refinedDeclaration);
            // inherit
            declaration.setUnboxed(refinedDeclaration.getUnboxed());
        }else if(transformer.isCeylonBasicType(type)
           && !(refinedDeclaration.getTypeDeclaration() instanceof TypeParameter)){
            declaration.setUnboxed(true);
        }else
            declaration.setUnboxed(false);
    }

    @Override
    public void visit(AnyAttribute that) {
        super.visit(that);
        TypedDeclaration declaration = that.getDeclarationModel();
        // deal with invalid input
        if(declaration == null)
            return;
        TypedDeclaration refinedDeclaration = (TypedDeclaration)Util.getTopmostRefinedDeclaration(declaration);
        // deal with invalid input
        if(refinedDeclaration == null)
            return;
        setBoxingState(declaration, refinedDeclaration);
    }

    @Override
    public void visit(AttributeSetterDefinition that) {
        super.visit(that);
        Setter declarationModel = that.getDeclarationModel();
        // deal with invalid input
        if(declarationModel == null)
            return;
        TypedDeclaration declaration = declarationModel.getParameter();
        // deal with invalid input
        if(declaration == null)
            return;
        TypedDeclaration refinedDeclaration = (TypedDeclaration)Util.getTopmostRefinedDeclaration(declaration);
        // deal with invalid input
        if(refinedDeclaration == null)
            return;
        setBoxingState(declaration, refinedDeclaration);
    }

    @Override
    public void visit(Variable that) {
        super.visit(that);
        TypedDeclaration declaration = that.getDeclarationModel();
        // deal with invalid input
        if(declaration == null)
            return;
        setBoxingState(declaration, declaration);
    }
}
