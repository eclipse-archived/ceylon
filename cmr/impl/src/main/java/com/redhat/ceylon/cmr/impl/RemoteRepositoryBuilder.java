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
    public CmrRepository buildRepository(String token) throws Exception {
        return buildRepository(token, EMPTY_CONFIG);
    }

    @Override
    public CmrRepository buildRepository(String token, RepositoryBuilderConfig config) throws Exception {
        if (token.startsWith("http:") || token.startsWith("https:")) {
            RemoteContentStore cs = new RemoteContentStore(token, config.log, config.offline, config.timeout, config.proxy);
            return new DefaultRepository(cs.createRoot());
        } else {
            return null;
        }
    }
}
