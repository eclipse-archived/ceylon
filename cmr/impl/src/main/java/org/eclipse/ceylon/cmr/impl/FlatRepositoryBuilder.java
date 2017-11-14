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

import org.eclipse.ceylon.cmr.api.CmrRepository;
import org.eclipse.ceylon.cmr.api.RepositoryBuilder;
import org.eclipse.ceylon.common.FileUtil;

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
