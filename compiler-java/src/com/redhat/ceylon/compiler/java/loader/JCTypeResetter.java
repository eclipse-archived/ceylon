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

import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.Type.ClassType;
import com.sun.tools.javac.code.Type.MethodType;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCArrayTypeTree;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCImport;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCModifiers;
import com.sun.tools.javac.tree.JCTree.JCTypeApply;
import com.sun.tools.javac.tree.JCTree.JCTypeParameter;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.tree.JCTree.JCWildcard;

/**
 * Recursively resets a Javac AST for bootstrapping
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class JCTypeResetter extends JCTree.Visitor {
    @Override
    public void visitTree(JCTree that) {
        // do not throw
        that.type = null;
    }
    
    @Override
    public void visitTopLevel(JCCompilationUnit that) {
        super.visitTopLevel(that);
        // do all toplevels
        for(JCTree tree : that.defs)
            tree.accept(this);
        that.namedImportScope = null;
        that.starImportScope = null;
        that.packge = null;
    }
    
    @Override
    public void visitImport(JCImport that) {
        super.visitImport(that);
        that.qualid.accept(this);
        that.type = null;
    }
    
    @Override
    public void visitClassDef(JCClassDecl that) {
        super.visitClassDef(that);
        that.sym = null;
        // reset this type
        if(that.extending != null)
            that.extending.accept(this);
        for(JCTree impl : that.implementing){
            impl.accept(this);
        }
        for(JCTree tyParam : that.typarams){
            tyParam.accept(this);
        }
        // do the class body
        for(JCTree def : that.defs){
            def.accept(this);
        }
        that.mods.accept(this);
    }

    @Override
    public void visitModifiers(JCModifiers that) {
        super.visitModifiers(that);
        for(JCTree annotation : that.annotations){
            annotation.accept(this);
        }
    }
    
    @Override
    public void visitAnnotation(JCAnnotation that) {
        super.visitAnnotation(that);
        for(JCTree arg : that.args){
            arg.accept(this);
        }
    }
    
    @Override
    public void visitTypeArray(JCArrayTypeTree that) {
        super.visitTypeArray(that);
        that.elemtype.accept(this);
    }
    
    @Override
    public void visitIdent(JCIdent that) {
        super.visitIdent(that);
        that.sym = null;
    }
    
    @Override
    public void visitMethodDef(JCMethodDecl that) {
        super.visitMethodDef(that);
        that.sym = null;
        // do arguments
        for(JCTree param : that.params){
            param.accept(this);
        }
        // return type
        if(that.restype != null)
            that.restype.accept(this);
        // type params
        for(JCTree tyParam : that.typarams){
            tyParam.accept(this);
        }
        that.mods.accept(this);
    }
    
    @Override
    public void visitVarDef(JCVariableDecl that) {
        super.visitVarDef(that);
        that.sym = null;
        that.vartype.accept(this);
        that.mods.accept(this);
    }
    
    @Override
    public void visitSelect(JCFieldAccess that) {
        super.visitSelect(that);
        that.sym = null;
        that.selected.accept(this);
    }

    @Override
    public void visitTypeApply(JCTypeApply that) {
        super.visitTypeApply(that);
        that.clazz.accept(this);
        for(JCTree arg : that.arguments){
            arg.accept(this);
        }
    }
    
    @Override
    public void visitTypeParameter(JCTypeParameter that) {
        super.visitTypeParameter(that);
        for(JCTree bound : that.bounds){
            bound.accept(this);
        }
    }
    
    @Override
    public void visitWildcard(JCWildcard that) {
        super.visitWildcard(that);
        if(that.inner != null)
            that.inner.accept(this);
    }
}
