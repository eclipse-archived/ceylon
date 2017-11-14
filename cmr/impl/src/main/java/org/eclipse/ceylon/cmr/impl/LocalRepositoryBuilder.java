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
import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.ceylon.cmr.api.CmrRepository;
import org.eclipse.ceylon.cmr.api.RepositoryBuilder;
import org.eclipse.ceylon.common.FileUtil;

/**
 * Repository builder for local uses of the DefaultRepository
 *
 * @author Tako Schotanus (tako@ceylon-lang.org)
 */
public class LocalRepositoryBuilder implements RepositoryBuilder {

    @Override
    public String absolute(File cwd, String token) throws URISyntaxException {
        final File file = (token.startsWith("file:") ? new File(new URI(token)) : new File(token));
        File absfile = FileUtil.absoluteFile(FileUtil.applyCwd(cwd, file));
        return absfile.getAbsolutePath();
    }

    @Override
    public CmrRepository[] buildRepository(String token) throws Exception {
        return buildRepository(token, EMPTY_CONFIG);
    }

    @Override
    public CmrRepository[] buildRepository(String token, RepositoryBuilderConfig config) throws Exception {
        final File file = (token.startsWith("file:") ? new File(new URI(token)) : new File(token));
        if (file.exists() == false)
            throw new IllegalArgumentException("Directory does not exist: " + token);
        if (file.isDirectory() == false)
            throw new IllegalArgumentException("Repository exists but is not a directory: " + token);

        FileContentStore cs = new FileContentStore(file);
        return new CmrRepository[] { new DefaultRepository(cs.createRoot()) };
    }
}
