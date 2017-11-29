/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.common.tool;

import java.util.HashMap;
import java.util.Map;

public class MapToolLoader extends ToolLoader {

    private final Map<String, String> map;

    private MapToolLoader() {
        this.map = new HashMap<>();
    }
    
    public MapToolLoader(Map<Class<? extends Tool>, String> map) {
        this();
        for (Map.Entry<Class<? extends Tool>, String> entry : map.entrySet()) {
            this.map.put(entry.getKey().getName(), entry.getValue());
        }
    }
    
    @SafeVarargs
    public static MapToolLoader fromClassNames(Class<? extends Tool>... classes) {
        MapToolLoader result = new MapToolLoader();
        for (Class<? extends Tool> cls : classes) {
            result.map.put(cls.getName(), result.camelCaseToDashes(cls.getSimpleName()));
        }
        return result;
    }

    @Override
    public String getToolName(String className) {
        return map.get(className);
    }

    @Override
    protected Iterable<String> toolClassNames() {
        return map.keySet();
    }
}
