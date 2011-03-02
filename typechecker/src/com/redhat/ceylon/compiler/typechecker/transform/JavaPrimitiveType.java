package com.redhat.ceylon.compiler.typechecker.transform;

import java.util.Collections;
import java.util.List;


public class JavaPrimitiveType extends JavaType {
    
    private String name;
    
    JavaPrimitiveType(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    @Override
    public boolean isPrimitive() {
        return true;
    }
    
    @Override
    public List<String> getQualifiedName() {
        return Collections.singletonList(name);
    }

}
