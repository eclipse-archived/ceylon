package com.redhat.ceylon.tools.new_;

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
