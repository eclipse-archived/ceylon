/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.cmr.impl;

import java.io.File;

import org.eclipse.ceylon.cmr.api.CmrRepository;
import org.eclipse.ceylon.cmr.api.RepositoryBuilder;
import org.eclipse.ceylon.common.FileUtil;
import org.eclipse.ceylon.common.log.Logger;

/**
 * Repository builder for NpmRepository
 *
 * @author Tako Schotanus (tako@ceylon-lang.org)
 */
public class NpmRepositoryBuilder implements RepositoryBuilder {

    @Override
    public String absolute(File cwd, String token) {
        if (token.equals("npm:") || token.equals("npm:/#")) {
            return "npm:";
        } else if (token.startsWith("npm:")) {
            token = token.substring(4);
            File f = FileUtil.absoluteFile(FileUtil.applyCwd(cwd, new File(token)));
            token = f.getAbsolutePath();
            return "npm:" + token;
        } else {
            return null;
        }
    }

    @Override
    public CmrRepository[] buildRepository(String token) throws Exception {
        return buildRepository(token, EMPTY_CONFIG);
    }

    @Override
    public CmrRepository[] buildRepository(String token, RepositoryBuilderConfig config) throws Exception {
        if (token.equals("npm:/#")) {
            token = "npm:";
        }
        if (token.startsWith("npm:")) {
            CmrRepository repo = createNpmRepository(token, config.log, config.offline, config.currentDirectory);
            return new CmrRepository[] { repo };
        } else {
            return null;
        }
    }
    
    public static CmrRepository createNpmRepository(String token, Logger log, boolean offline, String currentDirectory) {
        File local = new File(currentDirectory, "node_modules");
        
        String nodePath = token.substring(4);
        if (nodePath.isEmpty()) {
            nodePath = System.getenv("NODE_PATH");
            if (nodePath == null || nodePath.isEmpty()) {
                nodePath = local.getAbsolutePath();
            }
        }

        File[] roots = FileUtil.pathToFileArray(currentDirectory, nodePath);
        
        // If we have a single root to look up NPM modules we assume
        // we can use it for output as well. If we have several we
        // take the output to be "node_modules"
        // TODO: is this heuristic correct/useful?
        File out;
        if (roots.length > 1) {
            out = local;
        } else {
            out = roots[0];
        }
        
        NpmContentStore cs = new NpmContentStore(roots, out, log, offline);
        return new NpmRepository(cs.createRoot());
    }
}
