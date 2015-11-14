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

package com.redhat.ceylon.compiler.java.tools;

import com.redhat.ceylon.common.StatusPrinter;
import com.redhat.ceylon.compiler.java.codegen.CeylonCompilationUnit;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.TaskEvent;

/**
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class StatusPrinterTaskListener implements CeylonTaskListener {

    private StatusPrinter sp;

    public StatusPrinterTaskListener(StatusPrinter sp) {
        this.sp = sp;
    }

    @Override
    public void started(TaskEvent e) {
        CompilationUnitTree compilationUnit = e.getCompilationUnit();
        String path = e.getSourceFile().toUri().toString();
        sp.clearLine();
        switch(e.getKind()){
        case ANALYZE:
            sp.log("Javac [typecheck]: "+path);
            break;
        case ENTER:
            sp.log("Javac [enter]: "+path);
            break;
        case GENERATE:
            sp.log("Javac [generate]: "+path);
            break;
        case PARSE:
            // do not log parsing for Ceylon files
            if(compilationUnit instanceof CeylonCompilationUnit == false)
                sp.log("Javac [parse]: "+path);
            break;
        default:
            break;
        }

    }

    @Override
    public void finished(TaskEvent e) {
    }

    @Override
    public void moduleCompiled(String name, String version) {
        // make sure we clear progress when we start printing created modules
        sp.clearLine();
    }

}
