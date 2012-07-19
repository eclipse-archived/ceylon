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
    
    @Before
    public void setup() throws IOException {
        testConfig = ConfigParser.loadConfigFromFile(new File("test-src/com/redhat/ceylon/common/test/repos.config"));
        repos = Repositories.withConfig(testConfig);
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
    public void testGetOutputRepository() {
        Assert.assertTrue(testRepository(repos.getOutputRepository(), "Two", "foobar", "pietjepluk", "noencryptionfornow!"));
    }
    
    @Test
    public void testGetCacheRepository() {
        Assert.assertTrue(testRepository(repos.getCacheRepository(), "Three", "foobar", null, null));
    }
    
    @Test
    public void testGetLookupRepositories() {
        Repository[] lookup = repos.getLookupRepositories();
        Assert.assertTrue(lookup.length == 3);
        Assert.assertTrue(testRepository(lookup[0], "Two", "foobar", "pietjepluk", "noencryptionfornow!"));
        Assert.assertTrue(testRepository(lookup[1], "Three", "foobar", null, null));
        Assert.assertTrue(testRepository(lookup[2], "Four", "foobar", null, null));
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
