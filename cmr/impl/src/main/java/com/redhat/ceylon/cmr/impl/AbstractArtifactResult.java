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
import com.redhat.ceylon.cmr.api.ArtifactResultType;
import com.redhat.ceylon.cmr.api.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Abstract, use Jandex to read off Module info.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public abstract class AbstractArtifactResult implements ArtifactResult {

    private Repository repository;
    private String name;

    protected AbstractArtifactResult(Repository repository, String name) {
        this.repository = repository;
        this.name = name;
    }

    public ArtifactResultType type() {
        return ArtifactResultType.CEYLON;
    }

    public List<ArtifactResult> dependecies() throws IOException {
        List<ModuleInfo> infos = BytecodeUtils.readModuleInformation(name, artifact());
        if (infos.isEmpty())
            return Collections.emptyList();

        final List<ArtifactResult> results = new ArrayList<ArtifactResult>();
        for (ModuleInfo mi : infos) {
            final ArtifactContext context = new ArtifactContext(mi.getName(), mi.getVersion());
            context.setThrowErrorIfMissing(mi.isOptional() == false);
            final ArtifactResult result = repository.getArtifactResult(context);
            if (result != null)
                results.add(result);
        }
        return results;
    }

}

