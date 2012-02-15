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
import com.redhat.ceylon.cmr.api.ArtifactResult;
import com.redhat.ceylon.cmr.api.Repository;
import com.redhat.ceylon.cmr.spi.Node;
import com.redhat.ceylon.cmr.spi.OpenNode;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Maven repository helper.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class MavenArtifactContextAdapter extends AbstractArtifactContextAdapter {
    protected MavenArtifactContextAdapter(OpenNode root) {
        super(root);
    }

    public String getArtifactName(ArtifactContext context) {
        String name = context.getName();
        final int p = name.lastIndexOf(".");
        return getArtifactName(p >= 0 ? name.substring(p + 1) : name, context.getVersion(), ArtifactContext.JAR);
    }

    public ArtifactResult getArtifactResult(Repository repository, final Node node) {
        return new ArtifactResult() {
            public File artifact() throws IOException {
                return node.getContent(File.class);
            }

            public List<ArtifactResult> dependecies() throws IOException {
                return Collections.emptyList(); // dunno how to grab deps
            }
        };
    }
}
