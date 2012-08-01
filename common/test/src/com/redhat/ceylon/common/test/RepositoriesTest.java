package com.redhat.ceylon.common.test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.redhat.ceylon.common.CeylonConfig;
import com.redhat.ceylon.common.ConfigParser;
import com.redhat.ceylon.common.PromptedPassword.PasswordPrompt;
import com.redhat.ceylon.common.Repositories;
import com.redhat.ceylon.common.Repositories.Repository;

public class RepositoriesTest {

    static class MockPasswordPrompt implements PasswordPrompt {
        
        final Map<String, String> prompts = new HashMap<String, String>();
        final List<String> seenPrompts = new ArrayList<String>();
        
        public MockPasswordPrompt() {
        }
        
        @Override
        public char[] getPassword(String prompt) {
            this.seenPrompts.add(prompt);
            String password = prompts.get(prompt);
            return password != null ? password.toCharArray() : null;
        }
        
        public void assertSeenPrompts(String... prompts) {
            Assert.assertEquals(Arrays.asList(prompts), seenPrompts);
        }
    }
    
    CeylonConfig testConfig;
    Repositories repos;
    Repositories defaultRepos;
    Repositories overriddenRepos;
    MockPasswordPrompt passwordPrompt;
    
    @Before
    public void setup() throws IOException {
        testConfig = ConfigParser.loadConfigFromFile(new File("test/src/com/redhat/ceylon/common/test/repos.config"));
        if (testConfig.getInstallDir() == null) {
            // Set a fake installation folder
            System.setProperty("ceylon.home", "fake-install-dir");
        }
        passwordPrompt = new MockPasswordPrompt();
        testConfig.setPasswordPrompt(passwordPrompt);
        repos = Repositories.withConfig(testConfig);
        
        CeylonConfig fakeConfig = new CeylonConfig();
        defaultRepos = Repositories.withConfig(fakeConfig);
        
        CeylonConfig overriddenConfig = ConfigParser.loadConfigFromFile(new File("test/src/com/redhat/ceylon/common/test/overridden.config"));
        overriddenRepos = Repositories.withConfig(overriddenConfig);
    }
    
    @Test
    public void testGetRepository() {
        Assert.assertTrue(testRepository(repos.getRepository("One"), "One", "foobar", null, null));
        Assert.assertTrue(testRepository(repos.getRepository("Two"), "Two", "foobar", "pietjepluk", "noencryptionfornow!"));
    }
    
    @Test
    public void testGetBootstrapRepository() {
        Assert.assertTrue(testRepository(repos.getBootstrapRepository(), "One", "foobar", null, null));
    }
    
    @Test
    public void testGetDeafultBootstrapRepository() {
        File dir = new File(testConfig.getInstallDir(), "repo");
        Assert.assertTrue(testRepository(defaultRepos.getBootstrapRepository(), "INSTALL", dir.getAbsolutePath(), null, null));
    }
    
    @Test
    public void testGetOverriddenBootstrapRepository() {
        Assert.assertTrue(testRepository(overriddenRepos.getBootstrapRepository(), "INSTALL", "install", null, null));
    }
    
    @Test
    public void testGetOutputRepository() {
        Assert.assertTrue(testRepository(repos.getOutputRepository(), "Two", "foobar", "pietjepluk", "noencryptionfornow!"));
    }
    
    @Test
    public void testGetDefaultOutputRepository() {
        Assert.assertTrue(testRepository(defaultRepos.getOutputRepository(), "LOCAL", "./modules", null, null));
    }
    
    @Test
    public void testGetOverriddenOutputRepository() {
        Assert.assertTrue(testRepository(overriddenRepos.getOutputRepository(), "LOCAL", "local", null, null));
    }
    
    @Test
    public void testGetCacheRepository() {
        Assert.assertTrue(testRepository(repos.getCacheRepository(), "Three", "foobar", null, null));
    }
    
    @Test
    public void testGetDefaultCacheRepository() {
        File dir = defaultRepos.getCacheRepoDir();
        Assert.assertTrue(testRepository(defaultRepos.getCacheRepository(), "CACHE", dir.getAbsolutePath(), null, null));
    }
    
    @Test
    public void testGetOverriddenCacheRepository() {
        Assert.assertTrue(testRepository(overriddenRepos.getCacheRepository(), "CACHE", "cache", null, null));
    }
    
    @Test
    public void testGetLookupRepositories() {
        Repository[] lookup = repos.getLookupRepositories();
        passwordPrompt.prompts.put("Password for userfoo at foobar: ", "passwordfoo");
        Assert.assertTrue(lookup.length == 5);
        Assert.assertTrue(testRepository(lookup[0], "Two", "foobar", "pietjepluk", "noencryptionfornow!"));
        Assert.assertTrue(testRepository(lookup[1], "Three", "foobar", null, null));
        Assert.assertTrue(testRepository(lookup[2], "Four", "foobar", null, null));
        Assert.assertTrue(testRepository(lookup[3], "Five", "foobar", "userfoo", "passwordfoo"));
        Assert.assertTrue(testRepository(lookup[4], "%lookup-5", "foobar", null, null));
        passwordPrompt.assertSeenPrompts("Password for userfoo at foobar: ");
    }
    
    @Test
    public void testGetDefaultLookupRepositories() {
        Repository[] lookup = defaultRepos.getLookupRepositories();
        Assert.assertTrue(lookup.length == 3);
        Assert.assertTrue(testRepository(lookup[0], "LOCAL", "./modules", null, null));
        File userDir = defaultRepos.getUserRepoDir();
        Assert.assertTrue(testRepository(lookup[1], "USER", userDir.getAbsolutePath(), null, null));
        Assert.assertTrue(testRepository(lookup[2], "REMOTE", Repositories.REPO_URL_CEYLON, null, null));
    }
    
    @Test
    public void testGetOverriddenLookupRepositories() {
        Repository[] lookup = overriddenRepos.getLookupRepositories();
        Assert.assertTrue(lookup.length == 3);
        Assert.assertTrue(testRepository(lookup[0], "LOCAL", "local", null, null));
        Assert.assertTrue(testRepository(lookup[1], "USER", "user", null, null));
        Assert.assertTrue(testRepository(lookup[2], "REMOTE", "http://remote", null, null));
    }
    
    private boolean testRepository(Repository repo, String name, String url, String user, String password) {
        return (repo != null)
                && repo.getName().equals(name)
                && repo.getUrl().equals(url)
                && testEq(repo.getUser(), user)
                && testEq(repo.getPassword(), password);
    }

    private boolean testEq(String value1, String value2) {
        return (value1 == null) && (value2 == null) || (value1 != null && value2 != null && value1.equals(value2));
    }
}
