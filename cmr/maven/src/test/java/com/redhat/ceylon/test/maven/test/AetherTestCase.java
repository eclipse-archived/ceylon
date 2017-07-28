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

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.NavigableSet;

import org.junit.Assert;
import org.junit.Test;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.CmrRepository;
import com.redhat.ceylon.cmr.api.MavenArtifactContext;
import com.redhat.ceylon.cmr.api.ModuleDependencyInfo;
import com.redhat.ceylon.cmr.api.ModuleQuery.Type;
import com.redhat.ceylon.cmr.api.ModuleVersionDetails;
import com.redhat.ceylon.cmr.api.ModuleVersionQuery;
import com.redhat.ceylon.cmr.api.ModuleVersionResult;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.api.RepositoryManagerBuilder;
import com.redhat.ceylon.cmr.impl.MavenRepository;
import com.redhat.ceylon.cmr.impl.MavenRepositoryHelper;
import com.redhat.ceylon.cmr.impl.SimpleRepositoryManager;
import com.redhat.ceylon.cmr.maven.AetherContentStore;
import com.redhat.ceylon.cmr.maven.AetherRepository;
import com.redhat.ceylon.cmr.spi.StructureBuilder;
import com.redhat.ceylon.common.ModuleUtil;
import com.redhat.ceylon.model.cmr.ArtifactResult;
import com.redhat.ceylon.model.cmr.ModuleScope;

/**
 * Aether tests.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class AetherTestCase extends AbstractAetherTest {
    @Test
    public void testSimpleTest() throws Throwable {
        StructureBuilder structureBuilder = new AetherContentStore(log, null, null, false, 60000, new File("").getAbsolutePath());
        CmrRepository repository = MavenRepositoryHelper.getMavenRepository(structureBuilder);
        RepositoryManager manager = new SimpleRepositoryManager(repository, log);
        File artifact = manager.getArtifact(MavenArtifactContext.NAMESPACE, "org.slf4j:slf4j-api", "1.6.4");
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

    @Test
    public void testAether() throws Throwable {
        CmrRepository repository = AetherRepository.createRepository(log, false, 60000);
        RepositoryManager manager = new SimpleRepositoryManager(repository, log);
        ArtifactResult result = manager.getArtifactResult(MavenArtifactContext.NAMESPACE, "org.slf4j:slf4j-api", "1.6.4");
        Assert.assertNotNull(result);
        File artifact = result.artifact();
        boolean exists = false;
        try {
            Assert.assertNotNull(artifact);
            Assert.assertTrue(artifact.exists());
            exists = true;
            List<ArtifactResult> deps = result.dependencies();
            log.debug("deps = " + deps);
        } finally {
            if (exists) {
                Assert.assertTrue(artifact.delete()); // delete this one
            }
        }
    }

    @Test
    public void testAetherFetchingDependenciesWithUselessProperties() throws Throwable {
        CmrRepository repository = AetherRepository.createRepository(log, false, 60000);
        RepositoryManager manager = new SimpleRepositoryManager(repository, log);
        ArtifactResult result = manager.getArtifactResult(MavenArtifactContext.NAMESPACE, "org.springframework.cloud:spring-cloud-starter-eureka", "1.1.2.RELEASE");
        Assert.assertNotNull(result);
        File artifact = result.artifact();
        boolean exists = false;
        try {
            Assert.assertNotNull(artifact);
            Assert.assertTrue(artifact.exists());
            exists = true;
            List<ArtifactResult> deps = result.dependencies();
            Assert.assertEquals(10, deps.size());
            log.debug("deps = " + deps);
        } finally {
            if (exists) {
                Assert.assertTrue(artifact.delete()); // delete this one
            }
        }
    }

    @Test
    public void testAetherJarless() throws Throwable {
        CmrRepository repository = AetherRepository.createRepository(log, false, 60000);
        RepositoryManager manager = new SimpleRepositoryManager(repository, log);
        ArtifactResult result = manager.getArtifactResult(MavenArtifactContext.NAMESPACE, "javax.mail:mail", "1.3.2");
        Assert.assertNotNull(result);
        File artifact = result.artifact();
        boolean exists = false;
        try {
            Assert.assertNotNull(artifact);
            Assert.assertTrue(artifact.exists());
            Assert.assertTrue(ModuleUtil.isMavenJarlessModule(artifact));
            exists = true;
            List<ArtifactResult> deps = result.dependencies();
            Assert.assertEquals(1, deps.size());
            log.debug("deps = " + deps);
        } finally {
            if (exists) {
                Assert.assertTrue(artifact.delete()); // delete this one
            }
        }
    }

    @Test
    public void testWithSources() throws Throwable {
        CmrRepository repository = AetherRepository.createRepository(log, false, 60000);
        RepositoryManager manager = new SimpleRepositoryManager(repository, log);
        ArtifactResult result = manager.getArtifactResult(new ArtifactContext(MavenRepository.NAMESPACE, "org.slf4j:slf4j-api", "1.6.4", ArtifactContext.LEGACY_SRC));
        Assert.assertNotNull(result);
        File artifact = result.artifact();
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

    @Test
    public void testScopes() throws Throwable {
        CmrRepository repository = AetherRepository.createRepository(log, false, 60000);
        RepositoryManager manager = new SimpleRepositoryManager(repository, log);
        ArtifactResult artifact = manager.getArtifactResult(MavenArtifactContext.NAMESPACE, "org.jboss.xnio:xnio-api", "3.1.0.Beta7");
        File file = null;
        try {
            Assert.assertNotNull(artifact);
            file = artifact.artifact();
            Assert.assertTrue(file.exists());
            // http://search.maven.org/remotecontent?filepath=org/jboss/xnio/xnio-api/3.1.0.Beta7/xnio-api-3.1.0.Beta7.pom
            // there should be org.jboss.logging:jboss-logging
            Assert.assertTrue(artifact.dependencies().size() > 0);
        } finally {
            if (file != null && file.exists()) {
                Assert.assertTrue(file.delete()); // delete this one
            }
        }
    }

    @Test
    public void testAetherWithExternalSettings() throws Throwable {
        CmrRepository repository = createAetherRepository();
        RepositoryManager manager = new SimpleRepositoryManager(repository, log);
        ArtifactResult result = manager.getArtifactResult(MavenArtifactContext.NAMESPACE, "org.apache.camel:camel-core", "2.9.2");
        Assert.assertNotNull(result);
        Assert.assertEquals(result.name(), "org.apache.camel:camel-core");
        File artifact = result.artifact();
        boolean exists = false;
        try {
            Assert.assertNotNull(artifact);
            Assert.assertTrue(artifact.exists());
            exists = true;
            List<ArtifactResult> deps = result.dependencies();
            log.debug("deps = " + deps);
            List<ArtifactResult> compileDeps = new ArrayList<>(deps.size());
            for (ArtifactResult dep : deps) {
                if(dep.moduleScope() == ModuleScope.COMPILE
                        || dep.moduleScope() == ModuleScope.PROVIDED)
                    compileDeps.add(dep);
            }
            Assert.assertEquals(2, compileDeps.size());
            Assert.assertEquals("org.slf4j:slf4j-api", compileDeps.get(0).name());
            Assert.assertEquals("1.6.1", compileDeps.get(0).version());
            Assert.assertEquals("org.osgi:org.osgi.core", compileDeps.get(1).name());
            Assert.assertEquals("4.2.0", compileDeps.get(1).version());
            Assert.assertTrue(compileDeps.get(1).optional());
            log.debug("deps = " + compileDeps);
        } finally {
            if (exists) {
                Assert.assertTrue(artifact.delete()); // delete this one
            }
        }
    }

    @Test
    public void testAetherWithSemiColonModule() throws Throwable {
        CmrRepository repository = createAetherRepository();
        RepositoryManager manager = new SimpleRepositoryManager(repository, log);
        ArtifactResult result = manager.getArtifactResult(MavenArtifactContext.NAMESPACE, "org.restlet.jse:org.restlet", "2.0.10");
        Assert.assertNotNull(result);
        File artifact = result.artifact();
        boolean exists = false;
        try {
            Assert.assertNotNull(artifact);
            Assert.assertTrue(artifact.exists());
            exists = true;
            List<ArtifactResult> deps = result.dependencies();
            Assert.assertEquals(deps.size(), 1);
            Assert.assertEquals("org.osgi:org.osgi.core", deps.get(0).name());
            Assert.assertEquals("4.0.0", deps.get(0).version());
            log.debug("deps = " + deps);
        } finally {
            if (exists) {
                Assert.assertTrue(artifact.delete()); // delete this one
            }
        }
    }

    private String getOverridesFileName() throws URISyntaxException{
        URL overridessURL = getClass().getClassLoader().getResource("maven-settings/overrides.xml");
        return new File(overridessURL.toURI()).getPath();
    }
    
    @Test
    public void testAddRemoveOverrides() throws Throwable {
        CmrRepository repository = createAetherRepository();
        RepositoryManager manager = new SimpleRepositoryManager(repository, log, RepositoryManagerBuilder.parseOverrides(getOverridesFileName()));
        ArtifactResult result = manager.getArtifactResult(MavenArtifactContext.NAMESPACE, "org.restlet.jse:org.restlet", "2.0.10");
        Assert.assertNotNull(result);
        File artifact = result.artifact();
        boolean exists = false;
        try {
            Assert.assertNotNull(artifact);
            Assert.assertTrue(artifact.exists());
            exists = true;
            List<ArtifactResult> deps = result.dependencies();
            Assert.assertEquals(deps.size(), 1);
            Assert.assertEquals("org.slf4j:slf4j-api", deps.get(0).name());
            Assert.assertEquals("1.6.1", deps.get(0).version());
            log.debug("deps = " + deps);
        } finally {
            if (exists) {
                Assert.assertTrue(artifact.delete()); // delete this one
            }
        }
    }

    @Test
    public void testReplaceOverrides() throws Throwable {
        CmrRepository repository = createAetherRepository();
        RepositoryManager manager = new SimpleRepositoryManager(repository, log, RepositoryManagerBuilder.parseOverrides(getOverridesFileName()));
        ArtifactResult result = manager.getArtifactResult(MavenArtifactContext.NAMESPACE, "org.apache.camel:camel-core", "2.9.2");
        Assert.assertNotNull(result);
        Assert.assertEquals(result.name(), "org.osgi:org.osgi.core");
        File artifact = result.artifact();
        boolean exists = false;
        try {
            Assert.assertNotNull(artifact);
            Assert.assertTrue(artifact.exists());
            exists = true;
            List<ArtifactResult> deps = result.dependencies();
            Assert.assertEquals(deps.size(), 0);
        } finally {
            if (exists) {
                Assert.assertTrue(artifact.delete()); // delete this one
            }
        }
    }

    @Test
    public void testFilterOverrides() throws Throwable {
        CmrRepository repository = createAetherRepository();
        RepositoryManager manager = new SimpleRepositoryManager(repository, log, RepositoryManagerBuilder.parseOverrides(getOverridesFileName()));
        ArtifactResult result = manager.getArtifactResult(MavenArtifactContext.NAMESPACE, "org.osgi:org.osgi.core", "4.0.0");
        Assert.assertNotNull(result);
        Assert.assertEquals(result.name(), "org.osgi:org.osgi.core");
        File artifact = result.artifact();
        boolean exists = false;
        try {
            Assert.assertNotNull(artifact);
            Assert.assertTrue(artifact.exists());
            exists = true;
            Assert.assertNotNull(result.filter());
            Assert.assertTrue(result.filter().accept("org/osgi/test"));
            Assert.assertFalse(result.filter().accept("com/redhat/test"));
        } finally {
            if (exists) {
                Assert.assertTrue(artifact.delete()); // delete this one
            }
        }
    }

    @Test
    public void testListVersionsAether() throws Exception {
        CmrRepository repository = createAetherRepository();
        RepositoryManager manager = new SimpleRepositoryManager(repository, log);

        ModuleVersionQuery lookup = new ModuleVersionQuery("com.sparkjava:spark-core", "1.", Type.JAR);
        ModuleVersionResult result = manager.completeVersions(lookup);
        Assert.assertEquals(3, result.getVersions().size());
        Assert.assertNotNull(result.getVersions().get("1.0"));
        Assert.assertNotNull(result.getVersions().get("1.1"));
        Assert.assertNotNull(result.getVersions().get("1.1.1"));
        for(ModuleVersionDetails res : result.getVersions().values()){
            Assert.assertEquals("Spark\nA Sinatra inspired java web framework\nhttp://www.sparkjava.com", res.getDoc());
            Assert.assertEquals("The Apache Software License, Version 2.0\nhttp://www.apache.org/licenses/LICENSE-2.0.txt", res.getLicense());
            NavigableSet<ModuleDependencyInfo> deps = res.getDependencies();
            List<ModuleDependencyInfo> compileDeps = new ArrayList<>(deps.size());
            for (ModuleDependencyInfo dep : res.getDependencies()) {
                if(dep.getModuleScope() == ModuleScope.COMPILE
                        || dep.getModuleScope() == ModuleScope.PROVIDED)
                    compileDeps.add(dep);
            }
            Assert.assertEquals(4, compileDeps.size());
        }
        lookup = new ModuleVersionQuery("com.sparkjava:spark-core", null, Type.JAR);
        result = manager.completeVersions(lookup);
        // Count the version up to 2.3
        int cnt = 0;
        for (ModuleVersionDetails mvd : result.getVersions().values()) {
            cnt++;
            if ("2.3".equals(mvd.getVersion())) {
                break;
            }
        }
        Assert.assertEquals(7, cnt);
        
        // now check that we only downloaded the POMs for that, and not the jars
        File repo = new File("build/test-classes/maven-settings/repository");
        File folder = new File(repo, "com/sparkjava/spark-core/1.0");
        Assert.assertTrue(new File(folder, "spark-core-1.0.pom").exists());
        Assert.assertFalse(new File(folder, "spark-core-1.0.jar").exists());

        Assert.assertFalse(new File(repo, "org/eclipse/jetty/jetty-server/9.0.2.v20130417/jetty-server-9.0.2.v20130417.jar").exists());

        // this one has a conflict if we do resolve it non-lazily
        lookup = new ModuleVersionQuery("org.hibernate:hibernate-validator", "3.", Type.JAR);
        result = manager.completeVersions(lookup);
        Assert.assertEquals(4, result.getVersions().size());
    }

/*

    // @alesj - Added this test for some reason long ago ...

    @Test
    public void testOptionalDependency() throws Throwable {
        Repository repository = AetherRepository.createRepository(log, false, 60000);
        RepositoryManager manager = new SimpleRepositoryManager(repository, log);
        ArtifactResult result = manager.getArtifactResult("com.google.guava:guava", "20.0");
        Assert.assertNotNull(result);
        Assert.assertEquals(result.name(), "com.google.guava:guava");
        File artifact = result.artifact();
        boolean exists = false;
        try {
            Assert.assertNotNull(artifact);
            Assert.assertTrue(artifact.exists());
            exists = true;
            Assert.assertTrue(result.dependencies() == null || result.dependencies().isEmpty());
        } finally {
            if (exists) {
                Assert.assertTrue(artifact.delete()); // delete this one
            }
        }
    }

*/
}
