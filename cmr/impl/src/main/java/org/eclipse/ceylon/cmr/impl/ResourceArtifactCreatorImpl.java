/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.cmr.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.ceylon.cmr.api.ArtifactContext;
import org.eclipse.ceylon.cmr.api.ArtifactCreator;
import org.eclipse.ceylon.cmr.api.RepositoryManager;
import org.eclipse.ceylon.common.Constants;
import org.eclipse.ceylon.common.FileUtil;
import org.eclipse.ceylon.common.ModuleUtil;
import org.eclipse.ceylon.common.log.Logger;

/** Creates a "module-resource" folder artifact in the specified location,
 * containing the specified resource files.
 * 
 * @author Tako Schotanus
 */
public class ResourceArtifactCreatorImpl implements ArtifactCreator {

    private final RepositoryManager repoManager;
    private final Iterable<? extends File> sourcePaths;
    private final Iterable<? extends File> resourcePaths;
    private final String resourceRootName;
    private final String moduleName;
    private final String moduleVersion;
//    private boolean verbose;
//    private Logger log;

    public ResourceArtifactCreatorImpl(RepositoryManager repoManager, Iterable<? extends File> sourcePaths, Iterable<? extends File> resourcePaths, String resourceRootName, String moduleName, String moduleVersion, boolean verbose, Logger log) throws IOException {
        this.repoManager = repoManager;
        this.sourcePaths = sourcePaths;
        this.resourcePaths = resourcePaths;
        this.resourceRootName = resourceRootName;
        this.moduleName = moduleName;
        this.moduleVersion = moduleVersion;
//        this.verbose = verbose;
//        this.log = log;
    }

    public Set<String> copy(Collection<String> resFiles) throws IOException {
        if (resFiles == null  || resFiles.isEmpty()) {
            return Collections.emptySet();
        }
        Map<String,File> toCopy = new HashMap<String,File>();
        for (String res : resFiles) {
            File relRes = getDestinationFile(moduleName, res);
            if (relRes != null) {
                toCopy.put(res, relRes);
            }
        }
        if(toCopy.isEmpty())
            return Collections.emptySet();
        
        final ArtifactContext ac = new ArtifactContext(null, moduleName, moduleVersion, ArtifactContext.RESOURCES);
        ac.setThrowErrorIfMissing(false);
        
        File resDir = Files.createTempDirectory("ceylon-resources-").toFile();
        try {
            for (Map.Entry<String, File> res : toCopy.entrySet()) {
                // Copy the file to the resource dir
                FileUtil.copy(null, new File(res.getKey()), resDir, res.getValue());
            }
            
            repoManager.putArtifact(ac, resDir);
        } finally {
            FileUtil.deleteQuietly(resDir);
        }
        
        return new HashSet<String>(resFiles);
    }

    private File getDestinationFile(String moduleName, String file) {
        File relRes = new File(FileUtil.relativeFile(resourcePaths, file));
        // Check if the resource should be added for this module
        String resModName = ModuleUtil.moduleName((Iterable<File>) sourcePaths, relRes);
        if (resModName.equals(moduleName)) {
            return handleRoot(moduleName, relRes);
        } else  {
            return null;
        }
    }

    private File handleRoot(String moduleName, File relRes) {
        if (!ModuleUtil.isDefaultModule(moduleName)) {
            String rootName = resourceRootName;
            if (rootName == null) {
                rootName = Constants.DEFAULT_RESOURCE_ROOT;
            }
            if (!rootName.isEmpty()) {
                File modulePath = ModuleUtil.moduleToPath(moduleName);
                File rrp = new File(modulePath, rootName);
                if (relRes.toPath().startsWith(rrp.toPath())) {
                    relRes = rrp.toPath().relativize(relRes.toPath()).toFile();
                }
            }
        }
        return relRes;
    }
    
    public Iterable<? extends File> getPaths() {
        return resourcePaths;
    }

}
