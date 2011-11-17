/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */

package com.redhat.ceylon.compiler.loader;

import java.util.LinkedList;
import java.util.List;

import javax.tools.JavaFileObject.Kind;

import com.redhat.ceylon.compiler.loader.ModelLoader.DeclarationType;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Package;
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
    @Override
    public Declaration getDirectMember(String name) {
        String pkgName = getQualifiedNameString();
        // we need its package ready first
        PackageSymbol javaPkg = reader.enterPackage(names.fromString(pkgName ));
        javaPkg.complete();
        String className = getQualifiedName(pkgName, name);
        ClassSymbol classSymbol = modelLoader.lookupClassSymbol(className);
        // only get it from the classpath if we're not compiling it
        if(classSymbol != null && classSymbol.classfile.getKind() != Kind.SOURCE)
            return modelLoader.convertToDeclaration(className, DeclarationType.VALUE);
        return super.getDirectMember(name);
    }
    
    private String getQualifiedName(final String pkgName, String name) {
        // FIXME: some refactoring needed
        name = Util.quoteIfJavaKeyword(name);
        String className = pkgName.isEmpty() ? name : pkgName + "." + name;
        return className;
    }
    
    // FIXME: This is only here for wildcard imports, and we should be able to make it lazy like the rest
    // with a bit of work in the typechecker
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
}
