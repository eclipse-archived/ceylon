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

import com.redhat.ceylon.compiler.java.codegen.Decl;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Annotation;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.model.typechecker.model.FunctionOrValue;
import com.redhat.ceylon.model.typechecker.model.Module;
import com.redhat.ceylon.model.typechecker.model.Type;
import com.redhat.ceylon.model.typechecker.model.TypeDeclaration;

/**
 * Visitor that marks every Tree.TypedDeclaration that has non-language-module annotations
 * as captured.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class ForcedCaptureVisitor extends Visitor {
    
    public void visit(Tree.TypedDeclaration that) {
        if(isForcedCapture(that) && that.getDeclarationModel() instanceof FunctionOrValue)
            ((FunctionOrValue) that.getDeclarationModel()).setCaptured(true);
        super.visit(that);
    }

    private boolean isForcedCapture(Tree.TypedDeclaration that) {
        if(that.getAnnotationList() == null)
            return false;
        for(Annotation anno : that.getAnnotationList().getAnnotations()){
            Type type = anno.getTypeModel();
            if(type == null || !type.isClassOrInterface())
                continue;
            TypeDeclaration decl = type.getDeclaration();
            if(decl == null)
                continue;
            Module module = Decl.getModule(decl);
            if(module == null)
                continue;
            if(module.getLanguageModule() == module)
                continue;
            // does not come from the language module
            return true;
        }
        return false;
    }
}
