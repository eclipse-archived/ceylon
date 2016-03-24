package com.redhat.ceylon.common.tool;

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
