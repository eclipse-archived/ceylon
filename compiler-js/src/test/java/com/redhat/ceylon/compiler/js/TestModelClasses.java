package com.redhat.ceylon.compiler.js;

import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;

public class TestModelClasses {

    private static Map<String, Object> model;

    @BeforeClass
    public static void initTests() {
        TestModelMethodsAndAttributes.initTypechecker();
        MetamodelGenerator mmg = null;
        for (PhasedUnit pu : TestModelMethodsAndAttributes.tc.getPhasedUnits().getPhasedUnits()) {
            if (pu.getPackage().getModule().getNameAsString().equals("t2")) {
                if (mmg == null) {
                    mmg = new MetamodelGenerator(pu.getPackage().getModule());
                }
                pu.getCompilationUnit().visit(mmg);
            }
        }
        model = mmg.getModel();
    }

    @Test @SuppressWarnings("unchecked")
    public void testSimpleClasses() {
        System.out.println("top-level elements:" + model.keySet());
        Map<String,Object> cls = (Map<String,Object>)model.get("SimpleClass1");
        System.out.println(cls);
        cls = (Map<String, Object>)model.get("SimpleClass2");
        System.out.println(cls);
        cls = (Map<String, Object>)model.get("SimpleClass3");
        System.out.println(cls);
        cls = (Map<String, Object>)model.get("SimpleClass4");
        System.out.println(cls);
    }

    @Test @SuppressWarnings("unchecked")
    public void testSatisfies() {
        Map<String,Object> cls = (Map<String,Object>)model.get("Satisfy1");
        System.out.println(cls);
        cls = (Map<String, Object>)model.get("Satisfy2");
        System.out.println(cls);
    }

    @Test @SuppressWarnings("unchecked")
    public void testParameterTypes() {
        Map<String,Object> cls = (Map<String,Object>)model.get("ParmTypes1");
        System.out.println(cls);
        cls = (Map<String, Object>)model.get("ParmTypes2");
        System.out.println(cls);
        cls = (Map<String, Object>)model.get("ParmTypes3");
        System.out.println(cls);
        cls = (Map<String, Object>)model.get("ParmTypes4");
        System.out.println(cls);
    }

    @Test @SuppressWarnings("unchecked")
    public void testNested() {
        Map<String,Object> cls = (Map<String,Object>)model.get("Nested1");
        System.out.println(cls);
    }
}
