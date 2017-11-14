/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.common.test;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

import org.eclipse.ceylon.common.config.CeylonConfig;
import org.eclipse.ceylon.common.config.Keystores;
import org.eclipse.ceylon.common.config.Keystores.Store;
import org.junit.Assert;

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
