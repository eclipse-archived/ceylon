package com.redhat.ceylon.compiler.js.loader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Function;
import com.redhat.ceylon.model.typechecker.model.Module;
import com.redhat.ceylon.model.typechecker.model.Type;
import com.redhat.ceylon.model.typechecker.model.Unit;
import com.redhat.ceylon.model.typechecker.util.ModuleManager;

public class NpmPackage extends LazyPackage {

    private final Map<String,Declaration> decs = new HashMap<>();

    public NpmPackage(Module mod, String name) {
        setModule(mod);
        setShared(true);
        setName(ModuleManager.splitModuleName(name));
        setUnit(new Unit());
        getUnit().setPackage(this);
    }

    @Override
    protected void loadIfNecessary() {
    }

    public Declaration getDirectMember(String name, List<Type> signature, boolean variadic) {
        Declaration d = decs.get(name);
        if (d == null) {
            if (Character.isUpperCase(name.charAt(0))) {
            } else {
                d = new Function();
                d.setName(name);
                d.setUnit(this.getUnit());
                System.out.println("QUE SI TENGO " + name + " sig " + signature);
            }
            d.setShared(true);
            d.setContainer(this);
        }
        return d;
    }

}
