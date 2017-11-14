/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.tools.classpath;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.ceylon.cmr.api.ModuleQuery;
import org.eclipse.ceylon.cmr.ceylon.loader.ModuleGraph;
import org.eclipse.ceylon.cmr.ceylon.loader.ModuleGraph.Module;
import org.eclipse.ceylon.common.ModuleSpec;
import org.eclipse.ceylon.common.Versions;
import org.eclipse.ceylon.common.tool.Argument;
import org.eclipse.ceylon.common.tool.Description;
import org.eclipse.ceylon.common.tool.Option;
import org.eclipse.ceylon.common.tool.Summary;
import org.eclipse.ceylon.tools.moduleloading.ModuleLoadingTool;

@Summary("Prints a classpath suitable for passing to Java tools to run a given Ceylon module")
@Description("Will print a classpath for a given set of Ceylon modules, suitable for use with Java tools to " +
        "run a given Ceylon module outside of the regular JBoss Modules container used in `ceylon run`. " + 
        "If you need to force inclusion of optional modules, you can specify them as additional modules " + 
        "after your main module.")
public class CeylonClasspathTool extends ModuleLoadingTool {

    private List<ModuleSpec> modules;
    private boolean force;

    @Argument(argumentName="module", multiplicity="+")
    public void setModules(List<String> modules) {
        setModuleSpecs(ModuleSpec.parseEachList(modules));
    }
    
    public void setModuleSpecs(List<ModuleSpec> modules) {
        this.modules = modules;
    }

    @Option(longName="force")
    @Description("Force generation of classpath with multiple versions of the same module.")
    public void setForce(boolean force) {
        this.force = force;
    }
    
    @Override
    public void run() throws Exception {
        // we do depend on having a Main
        loadModule(null, "org.eclipse.ceylon.java.main", Versions.CEYLON_VERSION_NUMBER);

        for (ModuleSpec module : modules) {
            String moduleName = module.getName();
            String version = checkModuleVersionsOrShowSuggestions(
                    moduleName,
                    module.isVersioned() ? module.getVersion() : null,
                    ModuleQuery.Type.JVM,
                    Versions.JVM_BINARY_MAJOR_VERSION,
                    Versions.JVM_BINARY_MINOR_VERSION,
                    null, null, // JS binary but don't care since JVM
                    null);
            if(version == null)
                continue;
            loadModule(null, moduleName, version);
            if(!force)
                errorOnConflictingModule(moduleName, version);
        }
        loader.resolve();
        
        loader.visitModules(new ModuleGraph.Visitor() {
            boolean once = true;
            @Override
            public void visit(Module module) {
                if(module.artifact != null){
                    File file = module.artifact.artifact();
                    try{
                        if(file != null){
                            if(once)
                                once = false;
                            else
                                append(File.pathSeparator);
                            append(file.getAbsolutePath());
                        }
                    }catch(IOException x){
                        // lame
                        throw new RuntimeException(x);
                    }
                }
            }
        });
        flush();
    }
}
