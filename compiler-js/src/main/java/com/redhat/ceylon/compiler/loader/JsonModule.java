package com.redhat.ceylon.compiler.loader;

import java.util.Map;
import java.util.Objects;

import com.redhat.ceylon.compiler.js.CompilerErrorException;
import com.redhat.ceylon.compiler.typechecker.model.Module;

public class JsonModule extends Module {

    private Map<String,Object> model;

    public void setModel(Map<String, Object> value) {
        if (model != null) {
            final String modName = (String)model.get("$mod-name");
            final String modVers = (String)model.get("$mod-version");
            if (!(Objects.equals(modName, value.get("$mod-name"))
                    && Objects.equals(modVers, value.get("$mod-version")))) {
                throw new IllegalStateException("JsonModule " + modName+"/"+modVers
                        + " receives new module " + value.get("$mod-name")+"/"+value.get("$mod-version"));
            }
            return;
        }
        model = value;
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
        if ("default".equals(name)) {
            name = "";
        }
        final com.redhat.ceylon.compiler.typechecker.model.Package p = super.getPackage(name);
        if (p == null) {
            throw new CompilerErrorException("Package " + name + " not available");
        }
        return p;
    }

}
