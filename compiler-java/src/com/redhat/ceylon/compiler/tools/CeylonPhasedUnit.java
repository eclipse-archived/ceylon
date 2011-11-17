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

package com.redhat.ceylon.compiler.tools;

import javax.tools.JavaFileObject;

import com.redhat.ceylon.compiler.typechecker.analyzer.ModuleBuilder;
import com.redhat.ceylon.compiler.typechecker.context.Context;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.io.VirtualFile;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.CompilationUnit;
import com.sun.tools.javac.util.Position.LineMap;

public class CeylonPhasedUnit extends PhasedUnit {

    private JavaFileObject fileObject;
    private LineMap lineMap;

    public CeylonPhasedUnit(VirtualFile unitFile, VirtualFile srcDir,
            CompilationUnit cu, Package p, ModuleBuilder moduleBuilder,
            Context context, JavaFileObject fileObject, LineMap map) {
        super(unitFile, srcDir, cu, p, moduleBuilder, context);
        this.fileObject = fileObject;
        this.lineMap = map;
    }

    public JavaFileObject getFileObject() {
        return fileObject;
    }

    public LineMap getLineMap() {
        return lineMap;
    }
}
