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

import org.eclipse.ceylon.common.Constants;
import org.eclipse.ceylon.common.config.CeylonConfig;
import org.eclipse.ceylon.common.config.CeylonConfigFinder;
import org.junit.After;
import org.junit.Before;

public abstract class AbstractConfigTest {
    protected final File testDir = new File("build/test-stores");
    protected String userDir;
    
    @Before
    public void setup() throws IOException {
        delete(testDir);
        testDir.mkdirs();
        userDir = System.getProperty(Constants.PROP_CEYLON_USER_DIR);
        System.setProperty("ceylon.user.dir", testDir.getAbsolutePath());
    }
    
    protected CeylonConfig loadConfig(String filename) throws IOException {
        return CeylonConfigFinder.loadConfigFromFile(new File(filename));
    }
    
    @After
    public void teardown() {
        if (userDir == null) {
            System.getProperties().remove(Constants.PROP_CEYLON_USER_DIR);
        } else {
            System.setProperty(Constants.PROP_CEYLON_USER_DIR, userDir);
        }
    }
    
    private void delete(File f) {
        if (f.isDirectory()) {
            for (File c : f.listFiles()) {
                delete(c);
            }
        }
        f.delete();
    }
}
