package com.redhat.ceylon.compiler.loader;

import java.util.Map;

public class JsonPackage extends com.redhat.ceylon.compiler.typechecker.model.Package {

    private final Map<String,Object> model;

    public JsonPackage(Map<String, Object> metamodel) {
        model = metamodel;
        setShared(model.get("$pkg-shared")!=null);
    }

}
