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

package com.redhat.ceylon.test.maven.test;

import com.redhat.ceylon.cmr.api.Logger;
import com.redhat.ceylon.cmr.api.Repository;
import com.redhat.ceylon.cmr.impl.ArtifactContextAdapter;
import com.redhat.ceylon.cmr.impl.JULLogger;
import com.redhat.ceylon.cmr.impl.MavenRepositoryHelper;
import com.redhat.ceylon.cmr.impl.SimpleRepository;
import com.redhat.ceylon.cmr.maven.AetherContentStore;
import com.redhat.ceylon.cmr.spi.StructureBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Aether tests.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class AetherTestCase {

    private Logger log = new JULLogger();

    @Test
    public void testSimpleTest() throws Throwable {
        StructureBuilder structureBuilder = new AetherContentStore(log);
        ArtifactContextAdapter adapter = MavenRepositoryHelper.getMavenArtifactContextAdapter(structureBuilder);
        Repository repository = new SimpleRepository(adapter, log);
        File artifact = repository.getArtifact("org.slf4j.slf4j-api", "1.6.4");
        boolean exists = false;
        try {
            Assert.assertNotNull(artifact);
            Assert.assertTrue(artifact.exists());
            exists = true;
        } finally {
            if (exists) {
                Assert.assertTrue(artifact.delete()); // delete this one
            }
        }
    }

}
