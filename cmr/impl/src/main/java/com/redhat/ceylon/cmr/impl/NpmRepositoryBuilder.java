/*
 * Copyright 2011 Red Hat inc. and third party contributors as noted 
 * by the author tags.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.redhat.ceylon.cmr.impl;

import java.io.File;

import com.redhat.ceylon.cmr.api.CmrRepository;
import com.redhat.ceylon.cmr.api.RepositoryBuilder;
import com.redhat.ceylon.common.FileUtil;

/**
 * Repository builder for AetherRepository
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
    public CmrRepository buildRepository(String token) throws Exception {
        return buildRepository(token, EMPTY_CONFIG);
    }

    @Override
    public CmrRepository buildRepository(String token, RepositoryBuilderConfig config) throws Exception {
        if (token.equals("npm:") || token.equals("npm:/#")) {
            return createNpmRepository("npm:");
        } else if (token.startsWith("npm:")) {
            return createNpmRepository(token);
        } else {
            return null;
        }
    }
    
    private CmrRepository createNpmRepository(String token) {
        String nodePath = token.substring(4);
        if (nodePath.isEmpty()) {
            nodePath = System.getenv("NODE_PATH");
            if (nodePath == null || nodePath.isEmpty()) {
                File local = new File("node_modules");
                nodePath = local.getAbsolutePath();
            }
        }

        FileContentStore cs = new FileContentStore(new File(nodePath));
        return new NpmRepository(cs.createRoot());
    }
}
