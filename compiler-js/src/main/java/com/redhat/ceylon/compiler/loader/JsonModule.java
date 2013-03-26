package com.redhat.ceylon.compiler.loader;

import java.util.Map;

import com.redhat.ceylon.compiler.typechecker.model.Module;

public class JsonModule extends Module {

    private Map<String,Object> model;
    private final JsModuleManager modman;

    JsonModule(JsModuleManager manager) {
        modman = manager;
    }

    @SuppressWarnings("unchecked")
    public void setModel(Map<String, Object> value) {
        if (model != null) {
            throw new IllegalStateException("JsonModule should only receive model once");
        }
        model = value;
        for (Map.Entry<String, Object> e : model.entrySet()) {
            String k = e.getKey();
            if (!k.startsWith("$mod-")) {
                com.redhat.ceylon.compiler.typechecker.model.Package pkg = getDirectPackage(k);
                if (pkg == null) {
                    pkg = modman.createPackage(k, this);
                }
                if (pkg instanceof JsonPackage) {
                    ((JsonPackage) pkg).setModel((Map<String,Object>)e.getValue());
                }
            }
        }
    }
    public Map<String, Object> getModel() {
        return model;
    }

    void loadDeclarations() {
        if (model != null) {
            for (com.redhat.ceylon.compiler.typechecker.model.Package pkg : getPackages()) {
                if (pkg instanceof JsonPackage) {
                    ((JsonPackage) pkg).loadDeclarations();
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String,Object> getModelForPackage(String name) {
        return model == null ? null : (Map<String,Object>)model.get(name);
    }

    @Override
    public com.redhat.ceylon.compiler.typechecker.model.Package getPackage(String name) {
        final com.redhat.ceylon.compiler.typechecker.model.Package p = super.getPackage(name);
        if (p == null) {
            System.out.println(this +" don't yet have " + name);
        }
        return p;
    }

}
