package com.redhat.ceylon.compiler.typechecker.transform;

import java.util.Arrays;
import java.util.List;

public class JavaObjectType extends JavaType {
    
    private List<String> name;
    
    public JavaObjectType(String... name) {
        this.name = Arrays.asList(name);
    }
    
    public JavaObjectType(List<String> name) {
        this.name = name;
    }
    
    @Override
    public List<String> getQualifiedName() {
        return name;
    }
    
    @Override
    public boolean isPrimitive() {
        return false;
    }

}
