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

import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

/**
 * A visitor which marks TypeParameter declarations of methods as captured if any local
 * type is found recursively in there.
 * 
 * This allows us to properly refer to reified type parameters which are "hidden":
 * 
 * <pre><code>
 * void f&lt;M>(){
 *  class C&ltM>(){}
 * }
 * </code></pre>
 * 
 * Where <code>f.C.$getType</code> cannot qualify <code>f.M</code> so we must give it a unique
 * name, because <code>$reified$M</code> will override it in <code>f.C</code>.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class TypeParameterCaptureVisitor extends Visitor {
    
    @Override
    public void visit(Tree.ClassOrInterface that){
        ClassOrInterface model = that.getDeclarationModel();
        if(model != null 
                && !model.isAlias()
                && !model.isToplevel()
                && !model.isMember()){
            // it's a local type, capture!
            captureTypeParameters(model);
        }
        super.visit(that);
    }

    private void captureTypeParameters(ClassOrInterface model) {
        Scope container = model.getContainer();
        while(container != null
                && container instanceof Package == false){
            // only Method type parameters are marked as captured
            if(container instanceof Method){
                for(TypeParameter tp : ((Method) container).getTypeParameters()){
                    tp.setCaptured(true);
                }
            }
            // move up
            container = container.getContainer();
        }
    }
}
