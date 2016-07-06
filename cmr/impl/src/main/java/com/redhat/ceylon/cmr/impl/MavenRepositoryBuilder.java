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
import java.lang.reflect.Method;

import com.redhat.ceylon.cmr.api.CmrRepository;
import com.redhat.ceylon.cmr.api.RepositoryBuilder;
import com.redhat.ceylon.common.FileUtil;
import com.redhat.ceylon.common.log.Logger;

/**
 * Repository builder for AetherRepository
 *
 * @author Tako Schotanus (tako@ceylon-lang.org)
 */
public class MavenRepositoryBuilder implements RepositoryBuilder {

    @Override
    public String absolute(File cwd, String token) {
        if (token.equals("aether") || token.equals("aether:") || token.equals("aether:/#")
                || token.equals("mvn") || token.equals("mvn:") || token.equals("mvn:/#")) {
            return token;
        } else if (token.startsWith("aether:")) {
            return absolute(cwd, token, "aether:");
        } else if (token.startsWith("mvn:")) {
            return absolute(cwd, token, "mvn:");
        } else {
            return null;
        }
    }

    private String absolute(File cwd, String token, String prefix) {
        token = token.substring(prefix.length());
        File f = FileUtil.absoluteFile(FileUtil.applyCwd(cwd, new File(token)));
        token = f.getAbsolutePath();
        return prefix + token;
    }

    @Override
    public CmrRepository buildRepository(String token) throws Exception {
        return buildRepository(token, EMPTY_CONFIG);
    }

    @Override
    public CmrRepository buildRepository(String token, RepositoryBuilderConfig config) throws Exception {
        if (token.equals("aether") || token.equals("aether:") || token.equals("aether:/#")
                || token.equals("mvn") || token.equals("mvn:") || token.equals("mvn:/#")) {
            return createMavenRepository(token, null, config);
        } else if (token.startsWith("aether:")) {
            return createMavenRepository(token, "aether:", config);
        } else if (token.startsWith("mvn:")) {
            return createMavenRepository(token, "mvn:", config);
        } else {
            return null;
        }
    }

    private CmrRepository createMavenRepository(String token, String prefix, RepositoryBuilderConfig config) throws Exception {
        String settingsXml = null;
        if (prefix != null) {
            String settings = token.substring(prefix.length());
            // backwards compat: ignore overrides from here, previously located after | symbol
            int p = settings.indexOf("|");
            if (p < 0) {
                settingsXml = settings;
            } else {
                settingsXml = settings.substring(0, p);
            }
        }
        Class<?> aetherRepositoryClass = Class.forName("com.redhat.ceylon.cmr.maven.AetherRepository");
        Method createRepository = aetherRepositoryClass.getMethod("createRepository", Logger.class, String.class, boolean.class, int.class, String.class);
        return (CmrRepository) createRepository.invoke(null, config.log, settingsXml, config.offline, config.timeout, config.currentDirectory);
    }
}
