package com.redhat.ceylon.common.test;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.redhat.ceylon.common.config.CeylonConfig;
import com.redhat.ceylon.common.config.Keystores;
import com.redhat.ceylon.common.config.Keystores.Store;

public class KeystoresTest extends AbstractKeystoreTest {
    
    protected CeylonConfig testConfig;

    @Before
    public void loadConfig() throws IOException {
        this.testConfig = super.loadConfig("test/src/com/redhat/ceylon/common/test/keystores.config");
    }
    
    protected Store roundTrip(String keystoreSectionName, String storeFilename,
            File storeFile, String repoPass, String alias, String storePass, String entryPass)
            throws Exception, GeneralSecurityException, IOException {
        Store store = super.createStore(testConfig, storeFile, keystoreSectionName);
        Assert.assertEquals(storeFilename, store.getFilename());
        Assert.assertEquals("jceks", store.getKeyStoreType());
        Assert.assertEquals("SunJCE", store.getKeyStoreProvider());
        Assert.assertEquals("PBEWithMD5AndDES", store.getKeyFactoryAlgorithm());
        Assert.assertEquals("SunJCE", store.getKeyFactoryProvider());

        store.setPassword(alias, storePass.toCharArray(), entryPass.toCharArray(), repoPass.toCharArray());
        
        Assert.assertTrue(storeFile.exists());
        
        store = Keystores.withConfig(testConfig).getStore(keystoreSectionName);
        store.getPassword(alias, storePass.toCharArray(), entryPass.toCharArray());
        
        return store;
    }
    
    @Test
    public void testRoundTripDefault() throws Exception {
        String keystoreSectionName = null;
        String storeFilename = "foofile";
        File storeFile = new File(testDir, storeFilename);
        String repoPass = "foopassword";
        String alias = "fooalias";
        String storePass = "foostorepass";
        
        roundTrip(keystoreSectionName, storeFilename, storeFile, repoPass, alias,
                storePass, storePass);
    }
    
    @Test
    public void testRoundTripBar() throws Exception {
        String keystoreSectionName = "bar";
        String storeFilename = "barfile";
        File storeFile = new File(testDir, storeFilename);
        String repoPass = "barpassword";
        String alias = "baralias";
        String storePass = "barstorepass";
        
        roundTrip(keystoreSectionName, storeFilename, storeFile, repoPass, alias,
                storePass, storePass);
    }
    
    @Test
    public void testDeleteBar() throws Exception {
        String keystoreSectionName = "bar";
        String storeFilename = "barfile";
        File storeFile = new File(testDir, storeFilename);
        String repoPass = "barpassword";
        String alias = "baralias";
        String storePass = "barstorepass";
        
        Store store = roundTrip(keystoreSectionName, storeFilename, storeFile, repoPass, alias,
                storePass, storePass);
        Assert.assertNotNull(store.getPassword(alias, storePass.toCharArray()));
        store.deletePassword(alias, storePass.toCharArray());
        Assert.assertNull(store.getPassword(alias, storePass.toCharArray()));
    }

}
