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

import com.redhat.ceylon.compiler.typechecker.tree.NaturalVisitor;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.CompilerAnnotation;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.model.typechecker.model.FunctionOrValue;

/**
 * Visitor that marks every Tree.TypedDeclaration that is a FunctionOrValue as captured
 * if it has a \@captured compiler annotation.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class ForcedCaptureVisitor extends Visitor implements NaturalVisitor{
    
    public void visit(Tree.TypedDeclaration that) {
        if(isForcedCapture(that) && that.getDeclarationModel() instanceof FunctionOrValue)
            ((FunctionOrValue) that.getDeclarationModel()).setCaptured(true);
        super.visit(that);
    }

    private boolean isForcedCapture(Tree.TypedDeclaration that) {
        if(that.getCompilerAnnotations() == null)
            return false;
        for(CompilerAnnotation anno : that.getCompilerAnnotations()){
            if(anno.getIdentifier() != null && anno.getIdentifier().getText().equals("captured"))
                return true;
        }
        return false;
    }
}
