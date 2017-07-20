package com.redhat.ceylon.compiler.js.loader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.model.typechecker.model.Class;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Function;
import com.redhat.ceylon.model.typechecker.model.Module;
import com.redhat.ceylon.model.typechecker.model.Parameter;
import com.redhat.ceylon.model.typechecker.model.ParameterList;
import com.redhat.ceylon.model.typechecker.model.Type;
import com.redhat.ceylon.model.typechecker.model.Unit;
import com.redhat.ceylon.model.typechecker.model.Value;
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
                // TODO: it looks like this needs to be a special 
                // class that can return any member in a fashion 
                // similar to this package
                d = new Class();
                ParameterList plist = new ParameterList();
                plist.setNamedParametersSupported(true);
                plist.setFirst(true);
                for (int i=0; i<10; i++) {
                    Parameter p = new Parameter();
                    p.setName("arg" + i);
                    Value v = new Value();
                    v.setUnit(d.getUnit());
                    v.setType(getUnit().getUnknownType());
                    v.setDynamic(true);
                    v.setDynamicallyTyped(true);
                    v.setInitializerParameter(p);
                    v.setContainer((Class)d);
                    p.setModel(v);
                    p.setDeclaration(d);
                    p.setDefaulted(true);
                    plist.getParameters().add(p);
                }
                ((Class)d).setParameterList(plist);
            } else {
                d = new Function();
                ((Function)d).setDynamicallyTyped(true);
            }
            d.setDynamic(true);
            d.setName(name);
            d.setUnit(getUnit());
            d.setShared(true);
            d.setContainer(this);
            d.setScope(this);
            decs.put(name, d);
        }
        return d;
    }

}
