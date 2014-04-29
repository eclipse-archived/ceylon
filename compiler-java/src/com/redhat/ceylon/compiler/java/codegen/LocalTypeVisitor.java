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

import java.util.HashSet;
import java.util.Set;

import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.Setter;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

/**
 * Visits everything, stops at types (aliases, objects, class, interface) and collects
 * their Java class name if they are local.
 * 
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class LocalTypeVisitor extends Visitor {

    private HasTypeVisitor hasTypeVisitor = new HasTypeVisitor();
    
    private Set<String> locals = new HashSet<String>();
    private Set<String> ignored = new HashSet<String>();
    private Set<String> localCompanionClasses = new HashSet<String>();
    private Set<Interface> localInterfaces = new HashSet<Interface>();
    
    public Set<String> getLocals() {
        locals.removeAll(ignored);
        return locals;
    }

    public Set<Interface> getLocalInterfaces() {
        return localInterfaces;
    }

    private void collect(Node that, Declaration model) {
        if(model != null && !model.isMember()){
            String name = model.getName();
            Set<String> locals = this.locals;
            // FIXME: better name processing
            if(model instanceof Value
                    && !model.isToplevel())
                name = Naming.suffixName(Naming.Suffix.$getter$, name);
            if(model instanceof TypedDeclaration || model.isAnonymous())
                name += "_";
            else if(model instanceof Interface){
                // for interfaces we point to the toplevel interface prefixed with ::
                // no need to find the local java class
                localInterfaces.add((Interface) model);
                name = Naming.suffixName(Naming.Suffix.$impl, name);
                locals = localCompanionClasses;
            }
            // find an unused name
            int i;
            String prefixedName;
            for(i=1;locals.contains(prefixedName = i+name);i++){}
            // add it
            locals.add(prefixedName);
            // only keep it if it contains locals
            if(model instanceof TypedDeclaration && !hasTypeVisitor.hasType(that))
                ignored.add(prefixedName);
        }
    }

    @Override
    public void visit(Tree.AnyMethod that){
        Method model = that.getDeclarationModel();
        collect(that, model);
        // stop at locals, who get a type generated for them
        if(model.isMember())
            super.visit(that);
    }

    @Override
    public void visit(Tree.AttributeGetterDefinition that){
        Value model = that.getDeclarationModel();
        collect(that, model);
        // stop at locals, who get a type generated for them
        if(model.isMember())
            super.visit(that);
    }
    
    @Override
    public void visit(Tree.AttributeSetterDefinition that){
        Setter model = that.getDeclarationModel();
        // do not collect setters, they are always referenced by the getter
        // stop at locals, who get a type generated for them
        if(model.isMember())
            super.visit(that);
    }

    @Override
    public void visit(Tree.TypeAliasDeclaration that){
        // stop at aliases, do not collect them since we can never create any instance of them
        // and they are useless at runtime
    }

    @Override
    public void visit(Tree.ClassOrInterface that){
        ClassOrInterface model = that.getDeclarationModel();
        // stop at aliases, do not collect them since we can never create any instance of them
        // and they are useless at runtime
        if(!model.isAlias())
            collect(that, model);
    }

    @Override
    public void visit(Tree.ObjectDefinition that){
        Value model = that.getDeclarationModel();
        if(model != null)
            collect(that, model.getTypeDeclaration());
    }

    @Override
    public void visit(Tree.ObjectArgument that){
        Value model = that.getDeclarationModel();
        if(model != null)
            collect(that, model.getTypeDeclaration());
    }
}
