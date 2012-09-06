package com.redhat.ceylon.compiler.loader;

import java.util.Map;

import com.redhat.ceylon.compiler.typechecker.model.Module;

public class JsonModule extends Module {

    private Map<String,Object> model;
    private final JsModuleManager modman;

    JsonModule(JsModuleManager manager) {
        modman = manager;
    }

    public void setModel(Map<String, Object> value) {
        if (model != null) {
            throw new IllegalStateException("JsonModule should only receive model once");
        }
        model = value;
        for (Map.Entry<String, Object> e : model.entrySet()) {
            String k = e.getKey();
            if (!k.startsWith("$mod-")) {
                @SuppressWarnings("unchecked")
                JsonPackage pkg = new JsonPackage(modman, k, (Map<String,Object>)e.getValue());
                pkg.setModule(this);
                getPackages().add(pkg);
            }
        }
    }
    public Map<String, Object> getModel() {
        return model;
    }

}
