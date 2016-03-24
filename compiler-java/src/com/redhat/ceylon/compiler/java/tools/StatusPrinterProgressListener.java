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

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.common.StatusPrinter;
import com.redhat.ceylon.compiler.typechecker.analyzer.ModuleValidator;
import com.redhat.ceylon.compiler.typechecker.analyzer.ModuleValidator.ProgressListener;
import com.redhat.ceylon.model.cmr.ArtifactResult;
import com.redhat.ceylon.model.typechecker.model.Module;

/**
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class StatusPrinterProgressListener implements ProgressListener {

    private ModuleValidator validator;
    StatusPrinter sp;

    public StatusPrinterProgressListener(ModuleValidator validator, StatusPrinter sp) {
        this.validator = validator;
        this.sp = sp;
    }

    @Override
    public void retrievingModuleArtifact(Module module, ArtifactContext artifactContext) {
        long done = validator.numberOfModulesAlreadySearched();
        long todo = validator.numberOfModulesNotAlreadySearched();
        long total = done + todo;
        sp.clearLine();
        sp.log("["+(done+1)+"/"+total+"]: ");
        int moduleSize = sp.remainingForPercentage(0.4);
        int versionSize = sp.remainingForPercentage(0.1);
        sp.log(module.getNameAsString(), moduleSize);
        sp.log("/");
        sp.log(module.getVersion(), versionSize);
        sp.captureLine();
        artifactContext.setCallback(new StatusPrinterArtifactCallback(sp));
    }

    @Override
    public void resolvingModuleArtifact(Module module, ArtifactResult artifactResult) {
    }

    @Override
    public void retrievingModuleArtifactFailed(Module module, ArtifactContext artifactContext) {
        sp.logCapturedLine();
        sp.log(" FAIL");
    }

    @Override
    public void retrievingModuleArtifactSuccess(Module module, ArtifactResult artifact) {
        sp.logCapturedLine();
        sp.log(" OK");
    }
}
