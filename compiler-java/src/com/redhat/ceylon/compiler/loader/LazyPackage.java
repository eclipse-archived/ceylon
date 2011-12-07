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

package com.redhat.ceylon.compiler.loader;

import java.util.LinkedList;
import java.util.List;

import javax.tools.JavaFileObject.Kind;

import com.redhat.ceylon.compiler.loader.ModelLoader.DeclarationType;
import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.Setter;
import com.redhat.ceylon.compiler.util.Util;
import com.sun.tools.javac.code.Symbol.ClassSymbol;
import com.sun.tools.javac.code.Symbol.PackageSymbol;
import com.sun.tools.javac.jvm.ClassReader;
import com.sun.tools.javac.util.Name.Table;

public class LazyPackage extends Package {
    
    private ClassReader reader;
    private CeylonModelLoader modelLoader;
    private Table names;
    private List<Declaration> compiledDeclarations = new LinkedList<Declaration>();
    
    LazyPackage(CeylonModelLoader modelLoader, ClassReader reader, Table names){
        this.modelLoader = modelLoader;
        this.reader = reader;
        this.names = names;
    }
    
    @Override
    public Declaration getDirectMemberOrParameter(String name) {
        // FIXME: what's the difference?
        return getDirectMember(name);
    }
    
    // FIXME: redo this method better: https://github.com/ceylon/ceylon-spec/issues/90
    @Override
    public Declaration getDirectMember(String name) {
        String pkgName = getQualifiedNameString();
        // we need its package ready first
        
        PackageSymbol javaPkg = null;
        if (pkgName.equals("")) {
            javaPkg = modelLoader.syms().unnamedPackage;
        }
        else {
            javaPkg = reader.enterPackage(names.fromString(pkgName ));
        }
        
        javaPkg.complete();
        String className = getQualifiedName(pkgName, name);
        ClassSymbol classSymbol = modelLoader.lookupClassSymbol(className);
        // only get it from the classpath if we're not compiling it
        if(classSymbol != null && classSymbol.classfile.getKind() != Kind.SOURCE)
            return modelLoader.convertToDeclaration(className, DeclarationType.VALUE);
        return getDirectMemberFromSource(name);
    }

    private Declaration getDirectMemberFromSource(String name) {
        for (Declaration d: super.getMembers()) {
            if (isResolvable(d) && /*d.isShared() &&*/ isNamed(name, d)) {
                return d;
            }
        }
        return null;
    }

    private String getQualifiedName(final String pkgName, String name) {
        // FIXME: some refactoring needed
        name = Util.quoteIfJavaKeyword(name);
        String className = pkgName.isEmpty() ? name : pkgName + "." + name;
        return className;
    }
    
    // FIXME: This is only here for wildcard imports, and we should be able to make it lazy like the rest
    // with a bit of work in the typechecker
    // FIXME: redo this method better: https://github.com/ceylon/ceylon-spec/issues/90
    @Override
    public List<Declaration> getMembers() {
        // make sure the package is loaded
        modelLoader.loadPackage(getQualifiedNameString());
        List<Declaration> sourceDeclarations = super.getMembers();
        LinkedList<Declaration> ret = new LinkedList<Declaration>();
        ret.addAll(sourceDeclarations);
        ret.addAll(compiledDeclarations);
        return ret;
    }

    public void addMember(Declaration d) {
        compiledDeclarations.add(d);
    }

    // FIXME: remove those two when they are public in typechecker's model.Util
    static boolean isResolvable(Declaration declaration) {
        return declaration.getName()!=null &&
            !(declaration instanceof Setter) && //return getters, not setters
            !(declaration instanceof Class && 
                    Character.isLowerCase(declaration.getName().charAt(0))); //don't return the type associated with an object dec 
    }
    
    static boolean isNamed(String name, Declaration d) {
        return d.getName()!=null && d.getName().equals(name);
    }
    
}
