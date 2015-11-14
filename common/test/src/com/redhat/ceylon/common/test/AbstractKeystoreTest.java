package com.redhat.ceylon.common.test;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

import org.junit.Assert;

import com.redhat.ceylon.common.config.CeylonConfig;
import com.redhat.ceylon.common.config.Keystores;
import com.redhat.ceylon.common.config.Keystores.Store;

public abstract class AbstractKeystoreTest extends AbstractConfigTest {

    protected Store createStore(CeylonConfig testConfig, File storeFile, String keystoreSectionName)
            throws Exception, GeneralSecurityException, IOException {
        Assert.assertFalse(storeFile.exists());
        Keystores keystores = Keystores.withConfig(testConfig);
        Keystores.set(keystores);
        Store store = keystores.getStore(keystoreSectionName);
        return store;
    }
    
    protected Store createStore(CeylonConfig testConfig, String keystoreSectionName, String storeFilename,
            File storeFile, String repoPass, String alias, String storePass, String entryPass)
            throws Exception, GeneralSecurityException, IOException {
        Store store = createStore(testConfig, storeFile, keystoreSectionName);
        store.setPassword(alias, storePass.toCharArray(), entryPass.toCharArray(), repoPass.toCharArray());
        Assert.assertTrue(storeFile.exists());
        return store;
    }
    
}
