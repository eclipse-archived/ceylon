/*
 * Copyright 2011 Red Hat inc. and third party contributors as noted 
 * by the author tags.
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

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.ArtifactContextEnhancer;
import com.redhat.ceylon.cmr.api.Repository;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Maven repository helper.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
class MavenRepositoryHelper {

    static final ArtifactContextEnhancer ENHANCER = new ArtifactContextEnhancer() {
        public Iterable<String> buildArtifactTokens(ArtifactContext context, boolean addLeaf) {
            String name = context.getName();
            final List<String> tokens = new ArrayList<String>();
            tokens.addAll(Arrays.asList(name.split("\\.")));
            final String version = context.getVersion();
            if (Repository.DEFAULT_MODULE.equals(name) == false)
                tokens.add(version); // add version
            if (addLeaf) {
                final int p = name.lastIndexOf(".");
                if (p >= 0)
                    name = name.substring(p + 1);
                tokens.add(name + "-" + context.getVersion() + ArtifactContext.JAR);
            }
            return tokens;
        }
    };

    static File getMavenHome() {
        File mvnHome = new File(System.getenv("MAVEN_HOME"), "repository");
        if (mvnHome.exists())
            return mvnHome;

        mvnHome = new File(System.getProperty("user.home"), ".m2/repository");
        if (mvnHome.exists())
            return mvnHome;

        final String property = System.getProperty("maven.home");
        if (property != null) {
            mvnHome = new File(property, "repository");
            if (mvnHome.exists())
                return mvnHome;
        }

        throw new IllegalArgumentException("No Maven home specified!");
    }
}
