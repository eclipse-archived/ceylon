/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.tools.new_;

import java.util.HashMap;
import java.util.Map;

public class Environment {

    private Map<String, String> map = new HashMap<String, String>();
    
    public String get(String key) {
        return map.get(key);
    }
    
    public void put(String key, String value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException();
        }
        map.put(key, value);
    }
    
    public String toString() {
        return map.toString();
    }
    
}
