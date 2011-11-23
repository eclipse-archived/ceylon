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

package com.redhat.ceylon.compiler.codegen;

import java.util.Iterator;
import java.util.List;

import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AnyAttribute;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ClassDefinition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.MethodDefinition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Variable;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.compiler.util.Util;

public class BoxingDeclarationVisitor extends Visitor {
    
    private AbstractTransformer transformer;
    
    public BoxingDeclarationVisitor(AbstractTransformer transformer){
        this.transformer = transformer;
    }
    
    @Override
    public void visit(MethodDefinition that) {
        super.visit(that);
        // FIXME: we need to set those in the model loader as well
        Method method = that.getDeclarationModel();
        Method refinedMethod = (Method) Util.getTopmostRefinedDeclaration(method);
        if(isPrimitive(method, refinedMethod)) {
            Util.markUnBoxed(method);
            Util.markUnBoxed(refinedMethod);
        }
        Iterator<Parameter> parameters = method.getParameterLists().get(0).getParameters().iterator();
        for(Parameter refinedParam : refinedMethod.getParameterLists().get(0).getParameters()){
            Parameter param = parameters.next();
            if(isPrimitive(param, refinedParam)) {
                Util.markUnBoxed(param);
                Util.markUnBoxed(refinedParam);
            }
        }
    }
    
    @Override
    public void visit(ClassDefinition that) {
        super.visit(that);
        Class klass = that.getDeclarationModel();
        List<Parameter> parameters = klass.getParameterLists().get(0).getParameters();
        for(Parameter param : parameters){
            if(isPrimitive(param, param))
                Util.markUnBoxed(param);
        }
    }
    
    private boolean isPrimitive(TypedDeclaration declaration, TypedDeclaration refinedDeclaration) {
        if(declaration.getType() == null){
            // an error must have already been reported
            return false;
        }
        return transformer.isCeylonBasicType(declaration.getType())
                && !(refinedDeclaration.getTypeDeclaration() instanceof TypeParameter);
    }

    @Override
    public void visit(AnyAttribute that) {
        super.visit(that);
        TypedDeclaration declaration = that.getDeclarationModel();
        TypedDeclaration refinedDeclaration = Util.getTopmostRefinedDeclaration(declaration);
        if(isPrimitive(declaration, refinedDeclaration)) {
            Util.markUnBoxed(declaration);
            Util.markUnBoxed(refinedDeclaration);
        }
    }

    @Override
    public void visit(Variable that) {
        super.visit(that);
        TypedDeclaration declaration = that.getDeclarationModel();
        if(isPrimitive(declaration, declaration))
            Util.markUnBoxed(declaration);
    }
}
