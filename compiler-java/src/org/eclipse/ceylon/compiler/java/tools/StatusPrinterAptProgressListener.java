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

import org.eclipse.ceylon.cmr.api.ArtifactContext;
import org.eclipse.ceylon.common.ModuleSpec;
import org.eclipse.ceylon.common.StatusPrinter;
import org.eclipse.ceylon.model.cmr.ArtifactResult;

/**
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class StatusPrinterAptProgressListener {

    StatusPrinter sp;

    public StatusPrinterAptProgressListener(StatusPrinter sp) {
        this.sp = sp;
    }

    protected long getNumberOfModulesResolved(){
        return 0;
    }

    public void retrievingModuleArtifact(ModuleSpec module, ArtifactContext artifactContext) {
        long done = getNumberOfModulesResolved();
        long total = done;
        sp.clearLine();
        sp.log("["+total+"]: ");
        int moduleSize = sp.remainingForPercentage(0.4);
        int versionSize = sp.remainingForPercentage(0.1);
        sp.log(module.getName(), moduleSize);
        sp.log("/");
        sp.log(module.getVersion(), versionSize);
        sp.captureLine();
        artifactContext.setCallback(new StatusPrinterArtifactCallback(sp));
    }

    public void retrievingModuleArtifactFailed(ModuleSpec module, ArtifactContext artifactContext) {
        sp.logCapturedLine();
        sp.log(" FAIL");
    }

    public void retrievingModuleArtifactSuccess(ModuleSpec module, ArtifactResult artifact) {
        sp.logCapturedLine();
        sp.log(" OK");
    }
}
