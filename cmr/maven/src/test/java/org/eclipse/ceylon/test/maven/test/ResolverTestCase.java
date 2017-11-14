

package org.eclipse.ceylon.test.maven.test;

import java.io.File;
import java.util.Collections;
import java.util.List;

import org.eclipse.ceylon.cmr.api.CmrRepository;
import org.eclipse.ceylon.cmr.api.MavenArtifactContext;
import org.eclipse.ceylon.cmr.api.ModuleInfo;
import org.eclipse.ceylon.cmr.api.RepositoryManager;
import org.eclipse.ceylon.cmr.impl.AbstractArtifactResult;
import org.eclipse.ceylon.cmr.impl.SimpleRepositoryManager;
import org.eclipse.ceylon.cmr.maven.MavenDependencyResolver;
import org.eclipse.ceylon.model.cmr.ArtifactResult;
import org.eclipse.ceylon.model.cmr.ArtifactResultType;
import org.eclipse.ceylon.model.cmr.RepositoryException;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class ResolverTestCase extends AbstractAetherTest {
    @Test
    public void testMavenDependecyResolver() throws Exception {
        final MavenDependencyResolver resolver = new MavenDependencyResolver();
        doTest(new Tester() {
            public void run(CmrRepository repository, final File artifact) {
                ModuleInfo infos = resolver.resolve(new TestArtifactResult(repository, "org.apache.camel:camel-core", "2.9.2", artifact), null);
                Assert.assertNotNull(infos);
                Assert.assertEquals(String.valueOf(infos), 3, infos.getDependencies().size());
            }
        });
    }

    private void doTest(Tester tester) throws Exception {
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
            tester.run(repository, artifact);
        } finally {
            if (exists) {
                Assert.assertTrue(artifact.delete()); // delete this one
            }
        }

    }

    private static interface Tester {
        void run(CmrRepository repository, File artifact);
    }

    private static class TestArtifactResult extends AbstractArtifactResult {
        private final File artifact;

        private TestArtifactResult(CmrRepository repository, String name, String version, File artifact) {
            super(repository, repository.getNamespace(), name, version);
            this.artifact = artifact;
        }

        @Override
        protected File artifactInternal() {
            return artifact;
        }

        @Override
        public ArtifactResultType type() {
            return ArtifactResultType.MAVEN;
        }

        @Override
        public List<ArtifactResult> dependencies() throws RepositoryException {
            return Collections.emptyList();
        }

        @Override
        public String repositoryDisplayString() {
            return "Test!";
        }
        
        @Override
        public String groupId() {
            return null;
        }
        
        @Override
        public String artifactId() {
            return null;
        }
    }
}
