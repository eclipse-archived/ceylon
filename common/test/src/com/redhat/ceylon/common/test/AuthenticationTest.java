package com.redhat.ceylon.common.test;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.redhat.ceylon.common.config.Authentication;
import com.redhat.ceylon.common.config.CeylonConfig;
import com.redhat.ceylon.common.config.Keystores;
import com.redhat.ceylon.common.config.Repositories;
import com.redhat.ceylon.common.config.Authentication.UsernamePassword;
import com.redhat.ceylon.common.config.Repositories.Repository;

public class AuthenticationTest extends AbstractKeystoreTest {
    private Repositories repos;
    private MockPasswordPrompt mockPrompt;

    protected CeylonConfig testConfig;
    private Keystores keystores;

    @Before
    public void loadConfig() throws IOException {
        this.testConfig = super.loadConfig("test/src/com/redhat/ceylon/common/test/authentication.config");
        
    }
    
    @Before
    public void before() throws IOException {
        this.repos = Repositories.withConfig(testConfig);
        this.keystores = Keystores.withConfig(this.testConfig);
        mockPrompt = new MockPasswordPrompt();
        Authentication.setPasswordPrompt(mockPrompt);
    }
    
    @After
    public void after() {
        Authentication.setPasswordPrompt(null);
    }
    
    @Test
    public void testRepoWithPlaintext() {
        Repository repository = repos.getRepository("repo-with-plaintext");
        Assert.assertNotNull(repository);
        UsernamePassword up = Authentication.fromConfig(testConfig).getUsernameAndPassword(repository.getCredentials());
        Assert.assertNotNull(up);
        Assert.assertEquals("plaintext", new String(up.getPassword()));
        mockPrompt.assertSeenNoPrompts();
        Assert.assertEquals(repository.getCredentials().getUser(), up.getUser());
    }
    
    @Test
    public void testRepoWithPrompt() {
        mockPrompt.prompts.put("Password for bar at http://modules.ceylon-lang.org:", "repopassword");
        Repository repository = repos.getRepository("repo-with-prompted");
        Assert.assertNotNull(repository);
        UsernamePassword up = Authentication.fromConfig(testConfig).getUsernameAndPassword(repository.getCredentials());
        Assert.assertNotNull(up);
        Assert.assertEquals("repopassword", new String(up.getPassword()));
        mockPrompt.assertSeenOnlyGivenPrompts();
        Assert.assertEquals(repository.getCredentials().getUser(), up.getUser());
    }
    
    @Test
    public void testRepoWithFooAndKeystoreMissing() {
        Authentication.setPasswordPrompt(mockPrompt);
        Repository repository = repos.getRepository("repo-with-keystorefoo");
        Assert.assertNull(repository.getCredentials().getPassword());
        Assert.assertEquals("fooalias", repository.getCredentials().getAlias());
        UsernamePassword up = Authentication.fromConfig(testConfig).getUsernameAndPassword(repository.getCredentials());
        Assert.assertNotNull(up);
        mockPrompt.assertSeenNoPrompts();
        try {
            up.getPassword();
            Assert.fail();
        } catch (RuntimeException e) {
            if (!e.getMessage().contains("The default keystore (foofile) does not exist")) {
                throw e;
            }
        }
        mockPrompt.assertSeenNoPrompts();
        Assert.assertEquals(repository.getCredentials().getUser(), up.getUser());
    }

    @Test
    public void testRepoWithFooAndBadStorePass() throws Exception {
        String keystoreSectionName = null;
        String storeFilename = "foofile";
        File storeFile = new File(testDir, storeFilename);
        String repoPass = "foopassword";
        String alias = "fooalias";
        String storePass = "foostorepass";
        createStore(testConfig, keystoreSectionName, storeFilename, storeFile, repoPass, alias,
                storePass, storePass);
        mockPrompt.prompts.put("Password for default keystore (foofile):", storePass+" whoops");
        Authentication.setPasswordPrompt(mockPrompt);
        Repository repository = repos.getRepository("repo-with-keystorefoo");
        Assert.assertNull(repository.getCredentials().getPassword());
        Assert.assertEquals(alias, repository.getCredentials().getAlias());
        UsernamePassword up = Authentication.fromConfig(testConfig).getUsernameAndPassword(repository.getCredentials());
        Assert.assertNotNull(up);
        mockPrompt.assertSeenNoPrompts();
        try {
            up.getPassword();
            Assert.fail();
        } catch(RuntimeException e) {
            // Annoyingly the Keystore provider (well the jecks one at least)
            // throws an IOException in this case
            Assert.assertTrue(e.getMessage(), e.getMessage().contains("Keystore was tampered with, or password was incorrect"));            
        }
        mockPrompt.assertSeenOnlyGivenPrompts();
        Assert.assertEquals(repository.getCredentials().getUser(), up.getUser());
    }
    
    @Test
    public void testRepoWithFooAndEntryStorePass() throws Exception {
        String keystoreSectionName = null;
        String storeFilename = "foofile";
        File storeFile = new File(testDir, storeFilename);
        String repoPass = "foopassword";
        String alias = "fooalias";
        String storePass = "foostorepass";
        String entryPass = "fooentry";
        createStore(testConfig, keystoreSectionName, storeFilename, storeFile, repoPass, alias,
                storePass, entryPass);
        mockPrompt.prompts.put("Password for default keystore (foofile):", storePass);
        Authentication.setPasswordPrompt(mockPrompt);
        Repository repository = repos.getRepository("repo-with-keystorefoo");
        Assert.assertNull(repository.getCredentials().getPassword());
        Assert.assertEquals(alias, repository.getCredentials().getAlias());
        UsernamePassword up = Authentication.fromConfig(testConfig).getUsernameAndPassword(repository.getCredentials());
        Assert.assertNotNull(up);
        mockPrompt.assertSeenPrompts();
        try {
            up.getPassword();
            Assert.fail();
        } catch(RuntimeException e) {
            Assert.assertTrue(e.getMessage(), e.getMessage().contains("The given password cannot be used to access the alias fooalias in the default keystore (foofile)"));            
        }
        mockPrompt.assertSeenPrompts("Password for default keystore (foofile):");
        Assert.assertEquals(repository.getCredentials().getUser(), up.getUser());
    }
    
    @Test
    public void testRepoWithFoo() throws Exception {
        String keystoreSectionName = null;
        String storeFilename = "foofile";
        File storeFile = new File(testDir, storeFilename);
        String repoPass = "foopassword";
        String alias = "fooalias";
        String storePass = "foostorepass";
        String entryPass = "fooentrypass";
        createStore(testConfig, keystoreSectionName, storeFilename, storeFile, repoPass, alias,
                storePass, storePass);
        mockPrompt.prompts.put("Password for default keystore (foofile):", storePass);
        Authentication.setPasswordPrompt(mockPrompt);
        Repository repository = repos.getRepository("repo-with-keystorefoo");
        Assert.assertNull(repository.getCredentials().getPassword());
        Assert.assertEquals(alias, repository.getCredentials().getAlias());
        UsernamePassword up = Authentication.fromConfig(testConfig).getUsernameAndPassword(repository.getCredentials());
        Assert.assertNotNull(up);
        mockPrompt.assertSeenNoPrompts();
        Assert.assertEquals(repoPass, new String(up.getPassword()));
        mockPrompt.assertSeenOnlyGivenPrompts();
        Assert.assertEquals(repository.getCredentials().getUser(), up.getUser());
        // Get the password again
        up.getPassword();
        // Check we're not prompted again
        mockPrompt.assertSeenOnlyGivenPrompts();
    }
    
}
