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

import org.eclipse.ceylon.common.Backend;
import org.eclipse.ceylon.compiler.typechecker.analyzer.MissingNativeVisitor;
import org.eclipse.ceylon.compiler.typechecker.tree.Node;
import org.eclipse.ceylon.model.typechecker.model.Declaration;

/**
 * Visitor which checks that every native declaration is provided, and that every
 * use-site of these native declarations is also resolved.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 * @author Tako Schotanus <tako@ceylon-lang.org>
 */
public class JvmMissingNativeVisitor extends MissingNativeVisitor {
//    private final AbstractModelLoader loader;
    
    public JvmMissingNativeVisitor(/*AbstractModelLoader loader*/) {
        super(Backend.Java);
//        this.loader = loader;
    }
    
    protected boolean checkNative(Node node, Declaration model) {
//      String pkgName = Util.quoteJavaKeywords(pkg.getNameAsString());
//      String qualifiedName = Naming.toplevelClassName(pkgName, model);
//      ClassMirror classMirror = loader.lookupClassMirror(pkg.getModule(), qualifiedName);
//      ok = ok && (classMirror != null);
        return false;
    }
}
