package com.redhat.ceylon.compiler.js.loader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.model.typechecker.model.Class;
import com.redhat.ceylon.model.typechecker.model.Constructor;
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
                //It looks like this needs to be a special class that can return any member
                //in a fashion similar to this package
                d = new Class();
                ((Class)d).setDynamic(true);
                ParameterList plist = new ParameterList();
                plist.setNamedParametersSupported(true);
                plist.setFirst(true);
                Parameter p0 = new Parameter();
                p0.setName("args");
                p0.setSequenced(true);
                p0.setDeclaredAnything(true);
                Value v = new Value();
                v.setType(getUnit().getUnknownType());
                p0.setModel(v);
                plist.getParameters().add(p0);
                //Add a default constructor with jsNew set
                Constructor defcon = new Constructor();
                defcon.addParameterList(plist);
                defcon.setDynamic(true);
                defcon.setShared(true);
                defcon.setUnit(getUnit());
                defcon.setContainer((Class)d);
                defcon.setScope((Class)d);
                Function cf = new Function();
                cf.setDynamicallyTyped(true);
                cf.addParameterList(plist);
                cf.setContainer((Class)d);
                cf.setScope((Class)d);
                cf.setUnit(d.getUnit());
                cf.setVisibleScope(defcon.getVisibleScope());
                cf.setShared(true);
                cf.setDynamic(true);
                ((Class)d).setConstructors(true);
                ((Class)d).addMember(defcon);
                ((Class)d).addMember(cf);
            } else {
                d = new Function();
                ((Function)d).setDynamicallyTyped(true);
            }
            d.setName(name);
            d.setUnit(getUnit());
            d.setShared(true);
            d.setContainer(this);
            decs.put(name, d);
        }
        return d;
    }

}
