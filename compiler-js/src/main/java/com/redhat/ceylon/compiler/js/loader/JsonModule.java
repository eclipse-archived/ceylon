package com.redhat.ceylon.compiler.js.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.redhat.ceylon.common.Backend;
import com.redhat.ceylon.common.Backends;
import com.redhat.ceylon.model.typechecker.model.Annotation;
import com.redhat.ceylon.model.typechecker.model.Module;
import com.redhat.ceylon.model.typechecker.model.ModuleImport;
import com.redhat.ceylon.model.typechecker.model.Package;

public class JsonModule extends Module {

    private Map<String,Object> model;
    private boolean loaded = false;

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
        final String binVersion = (String)model.get("$mod-bin");
        final int dotidx = binVersion.indexOf('.');
        setJsMajor(Integer.parseInt((String)binVersion.substring(0,dotidx), 10));
        setJsMinor(Integer.parseInt((String)binVersion.substring(dotidx+1), 10));
        if (model.get("$mod-pa") != null) {
            int bits = (int)model.get("$mod-pa");
            setNativeBackends(JsonPackage.hasAnnotationBit(bits, "native") ? Backend.JavaScript.asSet() : Backends.ANY);
        }
        @SuppressWarnings("unchecked")
        Map<String,Object> moduleAnns = (Map<String,Object>)model.get("$mod-anns");
        if (moduleAnns != null) {
            for (Map.Entry<String, Object> e : moduleAnns.entrySet()) {
                String name = e.getKey();
                Annotation ann = new Annotation();
                ann.setName(name);
                for (String arg : (List<String>)e.getValue()) {
                    ann.addPositionalArgment(arg);
                }
                getAnnotations().add(ann);
            }
        }
    }
    public Map<String, Object> getModel() {
        return model;
    }

    void loadDeclarations() {
        if (loaded)return;
        if (model != null) {
            if (!loaded) {
                loaded=true;
                ArrayList<JsonPackage> pks = new ArrayList<>(model.size());
                for (Map.Entry<String, Object> e : model.entrySet()) {
                    if (!e.getKey().startsWith("$mod-")) {
                        JsonPackage p = new JsonPackage(e.getKey());
                        p.setModule(this);
                        pks.add(p);
                        getPackages().add(p);
                    }
                }
                for (JsonPackage p : pks) {
                    p.loadDeclarations();
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String,Object> getModelForPackage(String name) {
        return model == null ? null : (Map<String,Object>)model.get(name);
    }

    @Override
    public Package getPackage(String name) {
        if ("default".equals(name)) {
            name = "";
        }
        Package p = getDirectPackage(name);
        if (p != null) {
            return p;
        }
        for (ModuleImport imp : getImports()) {
            final Module mod = imp.getModule();
            p = mod.getDirectPackage(name);
            if (p != null) {
                return p;
            }
            for (ModuleImport im2 : mod.getImports()) {
                if (im2.isExport()) {
                    p = im2.getModule().getPackage(name);
                }
                if (p != null) {
                    return p;
                }
            }
        }
        return null;
    }

    public List<ModuleImport> getImports() {
        final List<ModuleImport> s = super.getImports();
        ArrayList<ModuleImport> r = new ArrayList<>(s.size());
        r.addAll(s);
        return r;
    }

}
