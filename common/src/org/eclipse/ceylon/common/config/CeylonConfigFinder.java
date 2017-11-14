/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.common.config;

import java.io.File;
import java.io.IOException;

import org.eclipse.ceylon.common.Constants;

public class CeylonConfigFinder {
    public static final ConfigFinder DEFAULT = new ConfigFinder(Constants.CEYLON_CONFIG_FILE, Constants.PROP_CEYLON_CONFIG_FILE);
    
    public static CeylonConfig loadDefaultConfig(File localDir) {
        return DEFAULT.loadDefaultConfig(localDir);
    }
    
    public static CeylonConfig loadLocalConfig(File dir) throws IOException {
        return DEFAULT.loadLocalConfig(dir);
    }
    
    public static CeylonConfig loadConfigFromFile(File configFile) throws IOException {
        return DEFAULT.loadConfigFromFile(configFile);
    }
    
    public static CeylonConfig loadOriginalConfigFromFile(File configFile) throws IOException {
        return DEFAULT.loadOriginalConfigFromFile(configFile);
    }
}
