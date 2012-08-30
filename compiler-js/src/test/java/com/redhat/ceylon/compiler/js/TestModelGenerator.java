package com.redhat.ceylon.compiler.js;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.redhat.ceylon.compiler.typechecker.TypeChecker;
import com.redhat.ceylon.compiler.typechecker.TypeCheckerBuilder;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;

public class TestModelGenerator {

    private static TypeChecker tc;

    @BeforeClass
    public static void initTypechecker() {
        TypeCheckerBuilder builder = new TypeCheckerBuilder();
        builder.addSrcDirectory(new File("src/test/resources/modeltests"));
        tc = builder.getTypeChecker();
        tc.process();
    }

    /** Asserts that very specified key is in the map and that it equals the corresponding value. */
    private void checkMap(Map<String, Object> map, String... keysValues) {
        for (int i = 0; i < keysValues.length; i+=2) {
            Assert.assertEquals(keysValues[i+1], (String)map.get(keysValues[i]));
        }
    }
    /** Asserts that a method contains the expected parameter at the expected position with optional default value. */
    private void checkParam(Map<String, Object> method, int pos, String name, String type, String defValue) {
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> params = (List<Map<String, Object>>)method.get("params");
        Assert.assertNotNull(params);
        Assert.assertTrue(params.size() > pos);
        Map<String, Object> parm = params.get(pos);
        checkMap(parm, "mt", "param", "name", name, "type", type);
        if (defValue == null) {
            Assert.assertNull(parm.get("def"));
        } else {
            Assert.assertEquals(defValue, parm.get("def"));
        }
    }

    private Map<String, Object> makeMap(String... keysValues) {
        HashMap<String, Object> m = new HashMap<String, Object>();
        for (int i = 0; i < keysValues.length; i+=2) {
            m.put(keysValues[i], keysValues[i+1]);
        }
        return m;
    }

    @Test @SuppressWarnings("unchecked")
    public void testMethodsAndAttributes() {
        MetamodelGenerator mmg = null;
        for (PhasedUnit pu : tc.getPhasedUnits().getPhasedUnits()) {
            if (pu.getPackage().getModule().getNameAsString().equals("t1")) {
                if (mmg == null) {
                    mmg = new MetamodelGenerator(pu.getPackage().getModule());
                }
                pu.getCompilationUnit().visit(mmg);
            }
        }
        Assert.assertNotNull(mmg);
        Map<String, Object> model = mmg.getModel();
        System.out.println("Methods and attributes: " + model);
        //simple1
        Map<String, Object> method = (Map<String, Object>)model.get("simple1");
        Assert.assertNotNull(method);
        checkMap(method, "name", "simple1", "mt", "method", "type", "ceylon.language.Void");
        method = (Map<String, Object>)model.get("simple2");
        checkMap(method, "name", "simple2", "mt", "method", "type", "ceylon.language.Integer", "shared", "1");
        method = (Map<String, Object>)model.get("simple3");
        checkMap(method, "name", "simple3", "mt", "method", "type", "ceylon.language.Void");
        checkParam(method, 0, "p1", "ceylon.language.Integer", null);
        checkParam(method, 1, "p2", "ceylon.language.String", null);
    }

}
