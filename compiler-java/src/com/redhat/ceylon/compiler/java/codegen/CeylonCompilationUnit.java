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

import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.CompilationUnit;
import com.redhat.ceylon.javax.tools.JavaFileObject;
import com.redhat.ceylon.langtools.tools.javac.code.Scope.ImportScope;
import com.redhat.ceylon.langtools.tools.javac.code.Scope.StarImportScope;
import com.redhat.ceylon.langtools.tools.javac.code.Symbol.PackageSymbol;
import com.redhat.ceylon.langtools.tools.javac.tree.JCTree;
import com.redhat.ceylon.langtools.tools.javac.tree.JCTree.JCCompilationUnit;
import com.redhat.ceylon.langtools.tools.javac.util.List;

/**
 * Stupid wrapper to store a ceylon CU in a javac CU.
 */
public class CeylonCompilationUnit extends JCCompilationUnit {

    public final CompilationUnit ceylonTree;
    public final PhasedUnit phasedUnit;

    protected CeylonCompilationUnit(List<JCAnnotation> packageAnnotations, JCExpression pid, List<JCTree> defs, JavaFileObject sourcefile, PackageSymbol packge, ImportScope namedImportScope, StarImportScope starImportScope, Tree.CompilationUnit ceylonTree, PhasedUnit phasedUnit) {
        super(packageAnnotations, pid, defs, sourcefile, packge, namedImportScope, starImportScope);
        this.ceylonTree = ceylonTree;
        this.phasedUnit = phasedUnit;
    }

}
