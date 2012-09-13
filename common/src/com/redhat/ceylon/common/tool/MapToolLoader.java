package com.redhat.ceylon.common.tool;

import java.util.HashMap;
import java.util.Map;

public class MapToolLoader extends ToolLoader {

    private final Map<String, String> map;

    public MapToolLoader(Map<Class<? extends Tool>, String> map) {
        this.map = new HashMap<>(map.size());
        for (Map.Entry<Class<? extends Tool>, String> entry : map.entrySet()) {
            this.map.put(entry.getKey().getName(), entry.getValue());
        }
    }

    @Override
    protected String getToolName(String className) {
        return map.get(className);
    }

    @Override
    protected Iterable<String> toolClassNames() {
        return map.keySet();
    }
}
