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

import com.redhat.ceylon.model.loader.AbstractModelLoader;
import com.redhat.ceylon.model.typechecker.model.Module;
import com.redhat.ceylon.model.typechecker.model.Package;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCImport;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCTypeApply;
import com.sun.tools.javac.tree.JCTree.JCTypeParameter;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.tree.JCTree.JCWildcard;
import com.sun.tools.javac.util.List;

/**
 * Scans Java AST to load imported packages before we enter the "enter" phase
 * where we would not be able to load these package's annotations.
 * See https://github.com/ceylon/ceylon-compiler/issues/2024
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
final class ImportScanner extends JCTree.Visitor {

    private final AbstractModelLoader modelLoader;
    private Module importingModule;

    ImportScanner(AbstractModelLoader modelLoader) {
        this.modelLoader = modelLoader;
    }

    @Override
    public void visitImport(JCImport that) {
        if(that.qualid instanceof JCTree.JCFieldAccess){
            // skip what we import and get to its package
            JCExpression selected = ((JCTree.JCFieldAccess)that.qualid).selected;
            if(selected instanceof JCTree.JCFieldAccess){
                if(that.staticImport){
                    // skip one more level since we can't static import toplevels
                    selected = ((JCFieldAccess)selected).selected;
                    if(selected instanceof JCFieldAccess == false)
                        return;
                }
                importPackage((JCTree.JCFieldAccess)selected);
            }
        }
    }

    private void importPackage(JCFieldAccess selected) {
        String importedPkg = selected.toString();
        if(importedPkg.isEmpty())
            return;
        Package importedPackage = importingModule.getPackage(importedPkg);
        if(importedPackage == null){
            // try one level up to skip potential types
            if(selected.selected instanceof JCFieldAccess){
                importPackage((JCFieldAccess) selected.selected);
            }
        }
    }

    @Override
    public void visitClassDef(JCClassDecl that) {
        // we have to do field types, method signatures, type parameters and extends/implements, that's all
        visit(that.defs);
        visitType(that.extending);
        for (JCExpression impl : that.implementing) {
            visitType(impl);
        }
        visitTypeParameters(that.typarams);
    }

    private void visitTypeParameters(List<JCTypeParameter> typarams) {
        for(JCTypeParameter param : typarams){
            for(JCExpression bound : param.bounds){
                visitType(bound);
            }
        }
    }

    private void visitType(JCExpression type) {
        if(type instanceof JCFieldAccess){
            importPackage((JCFieldAccess) type);
        }else if(type instanceof JCTypeApply){
            JCTypeApply parameterisedType = (JCTypeApply) type;
            visitType(parameterisedType.clazz);
            for(JCExpression arg : parameterisedType.arguments){
                visitType(arg);
            }
        }else if(type instanceof JCWildcard){
            JCTree inner = ((JCWildcard)type).inner;
            if(inner instanceof JCExpression)
                visitType((JCExpression) inner);
        }
    }

    @Override
    public void visitVarDef(JCVariableDecl that) {
        visitType(that.vartype);
    }
    
    @Override
    public void visitMethodDef(JCMethodDecl that) {
        visitType(that.restype);
        for(JCVariableDecl param : that.params){
            visitVarDef(param);
        }
    }
    
    @Override
    public void visitTree(JCTree thatceylonEnter) {
        // do nothing
    }

    @Override
    public void visitTopLevel(JCCompilationUnit that) {
        JCExpression pid = that.pid;
        String pkgName;
        if(pid instanceof JCFieldAccess){
            pkgName = ((JCFieldAccess)pid).toString();
        }else if(pid instanceof JCIdent){
            pkgName = ((JCIdent)pid).toString();
        }else{
            // default package
            pkgName = "";
        }
        Package thisPackage = modelLoader.findPackage(pkgName);
        if(thisPackage == null)
            return; // give up
        importingModule = thisPackage.getModule();
        // Ugly special case where we skip the test when we're compiling the language module itself
        if (importingModule == modelLoader.getLanguageModule())
            return;
        visit(that.defs);
    }

    private void visit(List<JCTree> trees) {
        for (List<? extends JCTree> l = trees; l.nonEmpty(); l = l.tail)
            l.head.accept(this);
    }
}