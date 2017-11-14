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

import org.eclipse.ceylon.compiler.typechecker.tree.Tree;
import org.eclipse.ceylon.compiler.typechecker.tree.Visitor;
import org.eclipse.ceylon.model.typechecker.model.Declaration;
import org.eclipse.ceylon.model.typechecker.model.FunctionOrValue;
import org.eclipse.ceylon.model.typechecker.model.Setter;
import org.eclipse.ceylon.model.typechecker.model.Type;
import org.eclipse.ceylon.model.typechecker.model.Value;

/**
 * Sets the underlying type of declarations that have been marked as 
 * {@code small} during type analysis.
 */
public class SmallDeclarationVisitor extends Visitor {
    
    /**
     * Return a type that's the same as the given type, but with the 
     * underlying type set.
     */
    static Type smallUnderlyingType(Type type) {
        Type result;
        if (type.isInteger()) {
            result = type.withoutUnderlyingType();
            result.setUnderlyingType("int");
            return result;
        } else if (type.isFloat()) {
            result = type.withoutUnderlyingType();
            result.setUnderlyingType("float");
        } else if (type.isCharacter()) {
            result = type.withoutUnderlyingType();
            result.setUnderlyingType("char");
        } else {
            result = type;
        }
        return result;
    }
    
    /** 
     * If the declaration is {@code small} set the declaration's type's 
     * underlying type to {@code int} or {@code float}.
     */
    static void smallDeclaration(Declaration d) {
        if (Decl.isSmall(d)) {
            FunctionOrValue f = (FunctionOrValue)d;
            setSmallType(f);
        }
    }

    protected static void setSmallType(FunctionOrValue f) {
        Type t = smallUnderlyingType(f.getType());
        f.setType(t);
        if (f instanceof Value) {
            Setter s = ((Value)f).getSetter();
            if (s != null) {
                s.getParameter().getModel().setType(t);
            }
        }
    }
    
    @Override
    public void visit(Tree.Parameter that) {
        super.visit(that);
        smallDeclaration(that.getParameterModel().getModel());
    }
    
    @Override
    public void visit(Tree.ParameterDeclaration that) {
        super.visit(that);
        smallDeclaration(that.getTypedDeclaration().getDeclarationModel());
    }
    
    @Override
    public void visit(Tree.AttributeSetterDefinition that) {
        Value getter = that.getDeclarationModel().getGetter();
        if (getter!=null && getter.isSmall()) {
            // Setter parameter is small if getter is small
            smallDeclaration(that.getDeclarationModel().getParameter().getModel());
        }
        super.visit(that);
    }
    
    @Override
    public void visit(Tree.AnyMethod that) {
        //Declaration prevd = declaration;
        //declaration = that.getDeclarationModel();
        //if (that.getDeclarationModel().getRefinedDeclaration())
        if (Decl.isSmall(that.getDeclarationModel())) {
            smallDeclaration(that.getDeclarationModel());
        }
        super.visit(that);
        //declaration = prevd;
    }

    
    @Override
    public void visit(Tree.AnyAttribute that) {
        //Declaration prevd = declaration;
        //declaration = that.getDeclarationModel();
        if (Decl.isSmall(that.getDeclarationModel())) {
            smallDeclaration(that.getDeclarationModel());
        }
        super.visit(that);
        //declaration = prevd;
    }
    
    @Override
    public void visit(Tree.SpecifierStatement that) {
        Declaration d = that.getDeclaration();
        if (d == null) {
            if (that.getBaseMemberExpression() instanceof Tree.MemberOrTypeExpression) {
                d = ((Tree.MemberOrTypeExpression)that.getBaseMemberExpression()).getDeclaration();
            }
        }
        
        //Declaration prevd = declaration;
        //declaration = d;
        smallDeclaration(d);
        super.visit(that);
        //declaration = prevd;
    }
    

    @Override
    public void visit(Tree.AttributeArgument that) {
        if (Decl.isSmall(that.getParameter().getModel())) {
            that.getDeclarationModel().setSmall(true);
            smallDeclaration(that.getDeclarationModel());
        }
        super.visit(that);
    }
}
