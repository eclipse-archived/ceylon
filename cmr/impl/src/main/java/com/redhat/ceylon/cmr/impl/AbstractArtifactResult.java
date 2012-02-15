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

import com.redhat.ceylon.cmr.api.ArtifactResult;
import com.redhat.ceylon.cmr.api.ArtifactResultType;
import com.redhat.ceylon.cmr.api.Repository;
import org.jboss.jandex.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Abstract, use Jandex to read off Module info.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public abstract class AbstractArtifactResult implements ArtifactResult {

    private static final DotName MODULE_ANNOTATION = DotName.createSimple("");

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
        List<ModuleInfo> infos = readModuleInformation();
        if (infos.isEmpty())
            return Collections.emptyList();

        final List<ArtifactResult> results = new ArrayList<ArtifactResult>();
        for (ModuleInfo mi : infos)
            results.add(repository.getArtifactResult(mi.name, mi.version));
        return results;
    }

    protected List<ModuleInfo> readModuleInformation() throws IOException {
        final File jarFile = artifact();

        final File indexFile = new File(jarFile.getAbsolutePath().replace(".jar", "-jar") + ".idx");
        if (indexFile.exists() == false) {
            JarIndexer.createJarIndex(jarFile, new Indexer(), false, false, false);
        }

        final Index index;
        final InputStream stream = new FileInputStream(indexFile);
        try {
            index = new IndexReader(stream).read();
        } finally {
            stream.close();
        }

        final DotName moduleClassName = DotName.createSimple(name + ".module");
        final ClassInfo moduleClass = index.getClassByName(moduleClassName);
        if (moduleClass == null)
            return Collections.emptyList();

        List<AnnotationInstance> annotations = moduleClass.annotations().get(MODULE_ANNOTATION);
        if (annotations == null || annotations.isEmpty())
            return Collections.emptyList();

        final AnnotationInstance ai = annotations.get(0);
        return Collections.emptyList(); // TODO
    }

    private static class ModuleInfo {
        private String name;
        private String version;
    }
}

