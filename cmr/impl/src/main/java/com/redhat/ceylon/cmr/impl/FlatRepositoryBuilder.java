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
 * Repository builder for FlatRepository
 *
 * @author Tako Schotanus (tako@ceylon-lang.org)
 */
public class FlatRepositoryBuilder implements RepositoryBuilder {

    @Override
    public String absolute(File cwd, String token) {
        if (token.startsWith("flat:")) {
            token = token.substring(5);
            File f = FileUtil.absoluteFile(FileUtil.applyCwd(cwd, new File(token)));
            token = f.getAbsolutePath();
            return "flat:" + token;
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
        if (token.startsWith("flat:")) {
            return new CmrRepository[] { createFlatRepository(token) };
        } else {
            return null;
        }
    }

    private CmrRepository createFlatRepository(String token) {
        final File file = new File(token.substring(5));
        if (file.exists() == false)
            throw new IllegalArgumentException("Directory does not exist: " + token);
        if (file.isDirectory() == false)
            throw new IllegalArgumentException("Repository exists but is not a directory: " + token);

        FileContentStore cs = new FileContentStore(file);
        return new FlatRepository(cs.createRoot());
    }
}
