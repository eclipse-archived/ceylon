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

/**
 * Repository builder for remote uses of the DefaultRepository
 *
 * @author Tako Schotanus (tako@ceylon-lang.org)
 */
public class RemoteRepositoryBuilder implements RepositoryBuilder {

    @Override
    public String absolute(File cwd, String token) {
        if (token.startsWith("http:") || token.startsWith("https:")) {
            return token;
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
        if (token.startsWith("http:") || token.startsWith("https:")) {
            RemoteContentStore cs = new RemoteContentStore(token, config.log, config.offline, config.timeout, config.proxy);
            return new CmrRepository[] { new DefaultRepository(cs.createRoot()) };
        } else {
            return null;
        }
    }
}
