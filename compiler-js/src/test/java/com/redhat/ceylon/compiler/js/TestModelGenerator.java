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
    /** Asserts that a method contains the expected parameter at the expected position with optional default value.
     * @return The type map of the parameter. */
    @SuppressWarnings("unchecked")
    private Map<String, Object> checkParam(Map<String, Object> method, int pos, String name, String type, String defValue, boolean sequenced) {
        List<Map<String, Object>> params = (List<Map<String, Object>>)method.get("params");
        Assert.assertNotNull(params);
        Assert.assertTrue(params.size() > pos);
        Map<String, Object> parm = params.get(pos);
        checkMap(parm, "mt", "param", "name", name);
        if (defValue == null) {
            Assert.assertNull("Param " + name + " of method " + method.get("name") + " shouldn't have default value",
                    parm.get("def"));
        } else {
            Assert.assertEquals("Default value of param " + name + " of method name " + method.get("name"),
                    defValue, parm.get("def"));
        }
        Map<String, Object> tmap = (Map<String, Object>)parm.get("type");
        Assert.assertNotNull(tmap);
        if (sequenced) {
            Assert.assertEquals("Param " + name + " of method " + method.get("name") + " should be sequenced",
                    "1", parm.get("seq"));
            Assert.assertEquals("Sequenced parameter should be last", params.size()-1, pos);
            Assert.assertEquals("ceylon.language.Iterable", String.format("%s.%s", tmap.get("pkg"), tmap.get("name")));
            List<Map<String, Object>> pts = (List<Map<String, Object>>)tmap.get("paramtypes");
            Assert.assertNotNull("Sequenced param of " + method.get("name") + " should have parameter type", tmap);
            Assert.assertFalse(tmap.isEmpty());
            tmap = pts.get(0);
            Assert.assertEquals("typeparam", tmap.get("mt"));
            Assert.assertEquals("Wrong param type for sequenced param of " + method.get("name"),
                    type, String.format("%s.%s", tmap.get("pkg"), tmap.get("name")));
        } else {
            Assert.assertNull("Param " + name + " of method " + method.get("name") + " should not be sequenced",
                    parm.get("seq"));
            Assert.assertEquals("Wrong type for param " + name + " of method " + method.get("name"),
                    type, String.format("%s.%s", tmap.get("pkg"), tmap.get("name")));
        }
        return tmap;
    }

    /** Check that the map either contains the specified type or is the specified type. */
    @SuppressWarnings("unchecked")
    private void checkType(Map<String, Object> map, String name, Object parameterTypes) {
        Map<String, Object> tmap = (Map<String, Object>)map.get("type");
        if (tmap == null) {
            tmap = map;
        }
        Assert.assertEquals(name, String.format("%s.%s", tmap.get("pkg"), tmap.get("name")));
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
        System.out.println("top-level elements:" + model.keySet());
        //simple1
        Map<String, Object> method = (Map<String, Object>)model.get("simple1");
        Assert.assertNotNull(method);
        checkMap(method, "name", "simple1", "mt", "method");
        checkType(method, "ceylon.language.Void", null);

        method = (Map<String, Object>)model.get("simple2");
        checkMap(method, "name", "simple2", "mt", "method", "shared", "1");
        checkType(method, "ceylon.language.Integer", null);

        method = (Map<String, Object>)model.get("simple3");
        checkMap(method, "name", "simple3", "mt", "method");
        checkType(method, "ceylon.language.Void", null);
        checkParam(method, 0, "p1", "ceylon.language.Integer", null, false);
        checkParam(method, 1, "p2", "ceylon.language.String", null, false);

        method = (Map<String, Object>)model.get("defaulted1");
        checkParam(method, 0, "p1", "ceylon.language.Integer", null, false);
        checkParam(method, 1, "p2", "ceylon.language.Integer", "5", false);

        method = (Map<String, Object>)model.get("sequenced1");
        checkParam(method, 0, "p1", "ceylon.language.Integer", null, false);
        Map<String, Object> tmap = checkParam(method, 1, "p2", "ceylon.language.String", null, true);
        checkType(tmap, "ceylon.language.String", null);

        method = (Map<String, Object>)model.get("sequencedDefaulted");
        checkParam(method, 0, "s", "ceylon.language.String", "\"x\"", false);
        checkParam(method, 1, "ints", "ceylon.language.Integer", null, true);

        method = (Map<String, Object>)model.get("mpl1");
        checkParam(method, 0, "a", "ceylon.language.String", null, false);
        checkType(method, "ceylon.language.Callable", null); //TODO check inner types

        method = (Map<String, Object>)model.get("mpl2");
        checkParam(method, 0, "a", "ceylon.language.Integer", null, false);
        checkType(method, "ceylon.language.Callable", null); //TODO check inner types

        method = (Map<String, Object>)model.get("parmtypes1");
        tmap = checkParam(method, 0, "x", "ceylon.language.Sequence", null, false); //TODO check inner types

        method = (Map<String, Object>)model.get("parmtypes2");
        tmap = checkParam(method, 0, "xx", "ceylon.language.Sequence", null, false);//TODO check inner types

        method = (Map<String, Object>)model.get("parmtypes3");
        checkType(method, "ceylon.language.Sequence", null);//TODO check inner types

        method = (Map<String, Object>)model.get("parmtypes4");
        checkType(method, "t1.SomethingElse", null);//TODO check inner types

        method = (Map<String, Object>)model.get("parmtypes5");
        System.out.println(method);
        checkParam(method, 0, "x", "t1.Value", null, false);
}

}
