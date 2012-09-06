package com.redhat.ceylon.compiler.loader;

import java.util.Map;

import com.redhat.ceylon.compiler.typechecker.analyzer.ModuleManager;
import com.redhat.ceylon.compiler.typechecker.model.Module;

public class JsonModule extends Module {

    private Map<String,Object> model;

    public void setModel(Map<String, Object> value) {
        if (model != null) {
            throw new IllegalStateException("JsonModule should only receive model once");
        }
        System.out.println(this + " me dan modelo");
        model = value;
        for (Map.Entry<String, Object> e : model.entrySet()) {
            String k = e.getKey();
            if (!k.startsWith("$mod-")) {
                @SuppressWarnings("unchecked")
                JsonPackage pkg = new JsonPackage((Map<String,Object>)e.getValue());
                pkg.setName(ModuleManager.splitModuleName(k));
                pkg.setModule(this);
                getPackages().add(pkg);
            }
        }
    }
    public Map<String, Object> getModel() {
        return model;
    }

}
