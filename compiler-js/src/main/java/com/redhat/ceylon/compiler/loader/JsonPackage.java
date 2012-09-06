package com.redhat.ceylon.compiler.loader;

import java.util.List;
import java.util.Map;

import com.redhat.ceylon.compiler.js.MetamodelGenerator;
import com.redhat.ceylon.compiler.typechecker.analyzer.ModuleManager;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.Unit;

public class JsonPackage extends com.redhat.ceylon.compiler.typechecker.model.Package {

    private final Map<String,Object> model;
    private final Unit unit = new Unit();
    private final JsModuleManager modman;

    public JsonPackage(JsModuleManager manager, String pkgname, Map<String, Object> metamodel) {
        model = metamodel;
        modman = manager;
        setName(ModuleManager.splitModuleName(pkgname));
        setShared(model.get("$pkg-shared") != null);
        addUnit(unit);
        for (Map.Entry<String,Object> e : model.entrySet()) {
            String k = e.getKey();
            if (!k.startsWith("$pkg-")) {
                @SuppressWarnings("unchecked")
                Map<String,Object> m = (Map<String,Object>)e.getValue();
                String metatype = (String)m.get(MetamodelGenerator.KEY_METATYPE);
                if (metatype == null) {
                    throw new IllegalArgumentException("Missing metatype from entry " + m);
                }
                if (metatype.equals(MetamodelGenerator.METATYPE_ATTRIBUTE)) {
                    parseAttribute(k, m, this);
                } else if (metatype.equals(MetamodelGenerator.METATYPE_CLASS)) {
                    parseClass(k, m, this);
                } else if (metatype.equals(MetamodelGenerator.METATYPE_INTERFACE)) {
                    parseInterface(k, m, this);
                } else if (metatype.equals(MetamodelGenerator.METATYPE_METHOD)) {
                    parseMethod(k, m, this);
                } else if (metatype.equals(MetamodelGenerator.METATYPE_OBJECT)) {
                    parseObject(k, m, this);
                }
            }
        }
    }

    @Override
    public List<Declaration> getMembers() {
        return super.getMembers();
    }

    private void parseClass(String name, Map<String, Object> m, Scope parent) {
        com.redhat.ceylon.compiler.typechecker.model.Class cls = new com.redhat.ceylon.compiler.typechecker.model.Class();
        cls.setAbstract(m.containsKey("abstract"));
        cls.setActual(m.containsKey("actual"));
        cls.setAnonymous(m.containsKey("$anon"));
        cls.setContainer(parent);
        cls.setDefault(m.containsKey("$def"));
        cls.setName(name);
        cls.setShared(m.containsKey("shared"));
        cls.setUnit(unit);
        unit.addDeclaration(cls);
        //TODO extended type, param types etc
    }

    private void parseMethod(String name, Map<String, Object> m, Scope parent) {
        System.out.println(this +" should parse method " + name);
    }

    private void parseAttribute(String name, Map<String, Object> m, Scope parent) {
        System.out.println(this + " should parse attribute " + name);
    }

    private void parseInterface(String name, Map<String, Object> m, Scope parent) {
        System.out.println(this + " should parse interface " + name);
    }

    private void parseObject(String name, Map<String, Object> m, Scope parent) {
        System.out.println(this + " should parse interface " + name);
    }

}
