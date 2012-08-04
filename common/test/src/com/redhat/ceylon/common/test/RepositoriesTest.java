package com.redhat.ceylon.common.test;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.redhat.ceylon.common.CeylonConfig;
import com.redhat.ceylon.common.ConfigParser;
import com.redhat.ceylon.common.Repositories;
import com.redhat.ceylon.common.Repositories.Repository;

public class RepositoriesTest {

    CeylonConfig testConfig;
    Repositories repos;
    Repositories defaultRepos;
    Repositories overriddenRepos;
    
    @Before
    public void setup() throws IOException {
        testConfig = ConfigParser.loadConfigFromFile(new File("test/src/com/redhat/ceylon/common/test/repos.config"));
        if (CeylonConfig.getInstallDir() == null) {
            // Set a fake installation folder
            System.setProperty("ceylon.home", "fake-install-dir");
        }
        repos = Repositories.withConfig(testConfig);
        
        CeylonConfig fakeConfig = new CeylonConfig();
        defaultRepos = Repositories.withConfig(fakeConfig);
        
        CeylonConfig overriddenConfig = ConfigParser.loadConfigFromFile(new File("test/src/com/redhat/ceylon/common/test/overridden.config"));
        overriddenRepos = Repositories.withConfig(overriddenConfig);
    }
    
    @Test
    public void testGetRepository() {
        assertRepository(repos.getRepository("One"), "One", "foobar", null, null);
        assertRepository(repos.getRepository("Two"), "Two", "foobar", "pietjepluk", "noencryptionfornow!");
    }
    
    @Test
    public void testGetBootstrapRepository() {
        assertRepository(repos.getBootstrapRepository(), "One", "foobar", null, null);
    }
    
    @Test
    public void testGetDeafultBootstrapRepository() {
        File dir = new File(CeylonConfig.getInstallDir(), "repo");
        assertRepository(defaultRepos.getBootstrapRepository(), "INSTALL", dir.getAbsolutePath(), null, null);
    }
    
    @Test
    public void testGetOverriddenBootstrapRepository() {
        assertRepository(overriddenRepos.getBootstrapRepository(), "INSTALL", "install", null, null);
    }
    
    @Test
    public void testGetOutputRepository() {
        assertRepository(repos.getOutputRepository(), "Two", "foobar", "pietjepluk", "noencryptionfornow!");
    }
    
    @Test
    public void testGetDefaultOutputRepository() {
        assertRepository(defaultRepos.getOutputRepository(), "LOCAL", "./modules", null, null);
    }
    
    @Test
    public void testGetOverriddenOutputRepository() {
        assertRepository(overriddenRepos.getOutputRepository(), "LOCAL", "local", null, null);
    }
    
    @Test
    public void testGetCacheRepository() {
        assertRepository(repos.getCacheRepository(), "Three", "foobar", null, null);
    }
    
    @Test
    public void testGetDefaultCacheRepository() {
        File dir = defaultRepos.getCacheRepoDir();
        assertRepository(defaultRepos.getCacheRepository(), "CACHE", dir.getAbsolutePath(), null, null);
    }
    
    @Test
    public void testGetOverriddenCacheRepository() {
        assertRepository(overriddenRepos.getCacheRepository(), "CACHE", "cache", null, null);
    }
    
    @Test
    public void testGetLookupRepositories() {
        Repository[] lookup = repos.getLookupRepositories();
        Assert.assertTrue(lookup.length == 4);
        assertRepository(lookup[0], "Two", "foobar", "pietjepluk", "noencryptionfornow!");
        assertRepository(lookup[1], "Three", "foobar", null, null);
        assertRepository(lookup[2], "Four", "foobar", null, null);
        assertRepository(lookup[3], "%lookup-4", "foobar", null, null);
    }
    
    @Test
    public void testGetDefaultLookupRepositories() {
        Repository[] lookup = defaultRepos.getLookupRepositories();
        Assert.assertTrue(lookup.length == 3);
        assertRepository(lookup[0], "LOCAL", "./modules", null, null);
        File userDir = defaultRepos.getUserRepoDir();
        assertRepository(lookup[1], "USER", userDir.getAbsolutePath(), null, null);
        assertRepository(lookup[2], "REMOTE", Repositories.REPO_URL_CEYLON, null, null);
    }
    
    @Test
    public void testGetOverriddenLookupRepositories() {
        Repository[] lookup = overriddenRepos.getLookupRepositories();
        Assert.assertTrue(lookup.length == 3);
        assertRepository(lookup[0], "LOCAL", "local", null, null);
        assertRepository(lookup[1], "USER", "user", null, null);
        assertRepository(lookup[2], "REMOTE", "http://remote", null, null);
    }
    
    private void assertRepository(Repository repo, String name, String url, String user, String password) {
        Assert.assertNotNull(repo);
        Assert.assertEquals(name, repo.getName());
        Assert.assertEquals(url, repo.getUrl());
        Assert.assertEquals(user, repo.getUser());
        Assert.assertEquals(password, repo.getPassword());
    }
}
