package com.redhat.ceylon.module.loader.test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.redhat.ceylon.cmr.api.CmrRepository;
import com.redhat.ceylon.cmr.api.Overrides;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.api.RepositoryManagerBuilder;
import com.redhat.ceylon.cmr.ceylon.CeylonUtils;
import com.redhat.ceylon.cmr.ceylon.loader.ModuleLoader;
import com.redhat.ceylon.cmr.ceylon.loader.ModuleNotFoundException;
import com.redhat.ceylon.cmr.impl.IOUtils;
import com.redhat.ceylon.cmr.impl.SimpleRepositoryManager;
import com.redhat.ceylon.cmr.maven.AetherRepository;
import com.redhat.ceylon.common.log.Logger;
import com.redhat.ceylon.common.log.StderrLogger;
import com.redhat.ceylon.model.cmr.ModuleScope;
import com.redhat.ceylon.module.loader.FlatpathModuleLoader;

public class ModuleLoaderTest {

    private final static Logger log = new StderrLogger();
    private final static String settings = "test/"+ModuleLoaderTest.class.getPackage().getName().replace('.', '/')+
            "/settings.xml";
    private final static String overrides = "test/"+ModuleLoaderTest.class.getPackage().getName().replace('.', '/')+
            "/overrides.xml";
    
    @Test
    public void testModuleLoader() throws ModuleNotFoundException {
        CmrRepository repository = AetherRepository.createRepository(log, false, 60000);
        RepositoryManager manager = new SimpleRepositoryManager(repository, log);
        Map<String, String> extraModules = new HashMap<>();
        extraModules.put("org.springframework.boot:spring-boot-starter-web", "1.3.5.RELEASE");
        extraModules.put("org.springframework.boot:spring-boot-starter-undertow", "1.3.5.RELEASE");
        extraModules.put("org.springframework.boot:spring-boot-starter-data-jpa", "1.3.5.RELEASE");
        extraModules.put("org.springframework.cloud:spring-cloud-starter-eureka", "1.1.2.RELEASE");
        extraModules.put("org.postgresql:postgresql", "9.4.1208");
        extraModules.put("org.liquibase:liquibase-core", "3.4.2");
        TestableModuleLoader moduleLoader = new TestableModuleLoader(manager, null, extraModules, false);
        
        moduleLoader.loadModule("org.liquibase:liquibase-core", "3.4.2", ModuleScope.RUNTIME);
        
        // Check that we got the latest version
        Assert.assertEquals("4.2.6.RELEASE", moduleLoader.getModuleVersion("org.springframework:spring-core"));
        // Check that this one did not get removed
        Assert.assertEquals("4.2.6.RELEASE", moduleLoader.getModuleVersion("org.springframework:spring-context"));
        // Those two should not be there
        Assert.assertNull(moduleLoader.getModuleVersion("javax.servlet:servlet-api"));
        Assert.assertNull(moduleLoader.getModuleVersion("org.springframework:spring"));
        // Check that we got the runtime dep
        Assert.assertEquals("3.3.6.Final", moduleLoader.getModuleVersion("org.jboss.xnio:xnio-nio"));
        
        moduleLoader.cleanup();

        moduleLoader.loadModule("org.liquibase:liquibase-core", "3.4.2", ModuleScope.COMPILE);
        
        // Check that we got the latest version
        Assert.assertEquals("4.2.6.RELEASE", moduleLoader.getModuleVersion("org.springframework:spring-core"));
        // Check that this one did not get removed
        Assert.assertEquals("4.2.6.RELEASE", moduleLoader.getModuleVersion("org.springframework:spring-context"));
        // Those two should not be there
        Assert.assertNull(moduleLoader.getModuleVersion("javax.servlet:servlet-api"));
        Assert.assertNull(moduleLoader.getModuleVersion("org.springframework:spring"));
        // Check that we have no runtime dep
        Assert.assertNull(moduleLoader.getModuleVersion("org.jboss.xnio:xnio-nio"));
        
    }
    
    @Test
    public void testModuleLoaderDirectImportsNotExcluded() throws ModuleNotFoundException {
        CmrRepository repository = AetherRepository.createRepository(log, settings, false, 60000, null);
        RepositoryManager manager = new SimpleRepositoryManager(repository, log);
        Map<String, String> extraModules = new HashMap<>();
        extraModules.put("org.springframework.cloud:spring-cloud-starter-eureka-server", "1.1.0.RC1");
        TestableModuleLoader moduleLoader = new TestableModuleLoader(manager, null, extraModules, false);
        
        moduleLoader.loadModule("org.springframework.cloud:spring-cloud-starter-eureka-server", "1.1.0.RC1", ModuleScope.RUNTIME);
        
        // Those should not be there
        Assert.assertNull(moduleLoader.getModuleVersion("jackson-dataformat-xml:com.fasterxml.jackson.dataformat"));

        moduleLoader.cleanup();
        
        // now add a direct import
        moduleLoader.loadModule("com.fasterxml.jackson.dataformat:jackson-dataformat-xml", "2.6.5", ModuleScope.RUNTIME);
        
        // Should be there
        Assert.assertEquals("2.6.5", moduleLoader.getModuleVersion("com.fasterxml.jackson.dataformat:jackson-dataformat-xml"));
    }

    @Test
    public void testNoDots() throws ModuleNotFoundException {
        CmrRepository repository = AetherRepository.createRepository(log, false, 60000);
        RepositoryManager manager = new SimpleRepositoryManager(repository, log);
        Map<String, String> extraModules = new HashMap<>();
        extraModules.put("maven:aopalliance:aopalliance", "1.0");
        TestableModuleLoader moduleLoader = new TestableModuleLoader(manager, null, extraModules, true);
        // org.antlr:stringtemplate:3.2.1
        // com.google.inject:guice:4.0
       
        moduleLoader.loadModule("maven:antlr:antlr", "2.7.7", ModuleScope.RUNTIME);
        
        // Check that we got them
        Assert.assertEquals("2.7.7", moduleLoader.getModuleVersion("maven:antlr:antlr"));
        Assert.assertEquals("1.0", moduleLoader.getModuleVersion("maven:aopalliance:aopalliance"));
        
    }

    @Test
    public void testNoDotsFromMaven() throws ModuleNotFoundException {
        CmrRepository repository = AetherRepository.createRepository(log, false, 60000);
        RepositoryManager manager = new SimpleRepositoryManager(repository, log);
        Map<String, String> extraModules = new HashMap<>();
        extraModules.put("org.antlr:stringtemplate", "3.2.1");
        TestableModuleLoader moduleLoader = new TestableModuleLoader(manager, null, extraModules, true);
       
        moduleLoader.loadModule("com.google.inject:guice", "4.0", ModuleScope.RUNTIME);
        
        // Check that we got them
        Assert.assertEquals("2.7.7", moduleLoader.getModuleVersion("maven:antlr:antlr"));
        Assert.assertEquals("1.0", moduleLoader.getModuleVersion("maven:aopalliance:aopalliance"));
        
    }

    @Test
    public void testNoDotsFromMavenWithOverrides() throws ModuleNotFoundException {
        CmrRepository repository = AetherRepository.createRepository(log, false, 60000);
        Overrides ov = RepositoryManagerBuilder.parseOverrides(overrides);
        
        Assert.assertTrue(ov.getAddedArtifacts().isEmpty());
        
        RepositoryManager manager = new SimpleRepositoryManager(repository, log, ov);
        Map<String, String> extraModules = new HashMap<>();
        extraModules.put("org.antlr:stringtemplate", "3.2.1");
        // this one has an override that adds aopalliance:aopalliance/1.0
        extraModules.put("org.postgresql:postgresql", "9.4.1208");
        TestableModuleLoader moduleLoader = new TestableModuleLoader(manager, null, extraModules, true);
       
        moduleLoader.loadModule("com.google.inject:guice", "4.0", ModuleScope.RUNTIME);
        
        // Check that we got them
        Assert.assertEquals("2.7.7", moduleLoader.getModuleVersion("maven:antlr:antlr"));
        Assert.assertEquals("1.0", moduleLoader.getModuleVersion("maven:aopalliance:aopalliance"));
    }

    @Test
    public void testBug7084() throws IOException {
        Path flatRepo = Files.createTempDirectory("flat-repo");
        try{
            RepositoryManager repositoryManager = CeylonUtils.repoManager()
                    .systemRepo("../dist/dist/repo")
                    .userRepos(Arrays.asList("flat:"+flatRepo.toAbsolutePath().toString()))
                    .buildManager();

            File artifact = repositoryManager.getArtifact(null, "ceylon.collection", "1.3.2");
            Assert.assertNotNull(artifact);
            Files.copy(artifact.toPath(), flatRepo.resolve("ceylon.collection-1.3.2.jar"));

            ModuleLoader moduleLoader = new FlatpathModuleLoader(repositoryManager, null, Collections.<String,String>emptyMap(), true);
            try {
                moduleLoader.loadModule("ceylon.collection", "1.3.2");
            } catch (ModuleNotFoundException e) {
                throw new RuntimeException(e);
            }
        }finally{
            IOUtils.deleteRecursively(flatRepo.toFile());
        }
    }

    @Test
    public void testBug7062() throws ModuleNotFoundException {
        RepositoryManager repositoryManager = CeylonUtils.repoManager()
                .systemRepo("../dist/dist/repo")
                .overrides("test/"+ModuleLoaderTest.class.getPackage().getName().replace('.', '/')+
                        "/bug7062-overrides.xml")
                .buildManager();
        Map<String, String> extraModules = new HashMap<>();
        TestableModuleLoader moduleLoader = new TestableModuleLoader(repositoryManager, null, extraModules, false);
        
        moduleLoader.loadModule("ceylon.interop.persistence", "1.3.1", ModuleScope.RUNTIME);
    }
}
