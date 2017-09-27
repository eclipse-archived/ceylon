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

package org.eclipse.ceylon.compiler.java.tools;

import org.eclipse.ceylon.compiler.typechecker.analyzer.ModuleSourceMapper;
import org.eclipse.ceylon.compiler.typechecker.context.Context;
import org.eclipse.ceylon.compiler.typechecker.context.PhasedUnit;
import org.eclipse.ceylon.compiler.typechecker.io.VirtualFile;
import org.eclipse.ceylon.compiler.typechecker.tree.Node;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree.CompilationUnit;
import org.eclipse.ceylon.javax.tools.JavaFileObject;
import org.eclipse.ceylon.langtools.tools.javac.util.Position.LineMap;
import org.eclipse.ceylon.model.loader.ModelResolutionException;
import org.eclipse.ceylon.model.typechecker.model.Package;
import org.eclipse.ceylon.model.typechecker.util.ModuleManager;

public class CeylonPhasedUnit extends PhasedUnit {

    private JavaFileObject fileObject;
    private LineMap lineMap;

    public CeylonPhasedUnit(VirtualFile unitFile, VirtualFile srcDir,
            CompilationUnit cu, Package p, ModuleManager moduleManager,
            ModuleSourceMapper moduleSourceMapper,
            Context context, JavaFileObject fileObject, LineMap map) {
        super(unitFile, srcDir, cu, p, moduleManager, moduleSourceMapper, context, null);
        this.fileObject = fileObject;
        this.lineMap = map;
    }

    public CeylonPhasedUnit(PhasedUnit original, JavaFileObject fileObject, LineMap map) {
        super(original);
        this.fileObject = fileObject;
        this.lineMap = map;
    }
    
    public JavaFileObject getFileObject() {
        return fileObject;
    }

    public LineMap getLineMap() {
        return lineMap;
    }
    
    @Override
    public boolean handleException(Exception e, Node that) {
        // this is better than pretending it's a visitor crash, since we don't lose the node
        if(e instanceof ModelResolutionException){
            that.addError(e.getMessage());
            return true;
        }
        return super.handleException(e, that);
    }
}
