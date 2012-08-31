package com.redhat.ceylon.compiler.js;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import util.ModelUtils;

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
        ModelUtils.checkMap(method, "name", "simple1", "mt", "method");
        ModelUtils.checkType(method, "ceylon.language.Void", null);

        method = (Map<String, Object>)model.get("simple2");
        ModelUtils.checkMap(method, "name", "simple2", "mt", "method", "shared", "1");
        ModelUtils.checkType(method, "ceylon.language.Integer", null);

        method = (Map<String, Object>)model.get("simple3");
        ModelUtils.checkMap(method, "name", "simple3", "mt", "method");
        ModelUtils.checkType(method, "ceylon.language.Void", null);
        ModelUtils.checkParam(method, 0, "p1", "ceylon.language.Integer", null, false);
        ModelUtils.checkParam(method, 1, "p2", "ceylon.language.String", null, false);

        method = (Map<String, Object>)model.get("defaulted1");
        ModelUtils.checkParam(method, 0, "p1", "ceylon.language.Integer", null, false);
        ModelUtils.checkParam(method, 1, "p2", "ceylon.language.Integer", "5", false);

        method = (Map<String, Object>)model.get("sequenced1");
        ModelUtils.checkParam(method, 0, "p1", "ceylon.language.Integer", null, false);
        Map<String, Object> tmap = ModelUtils.checkParam(method, 1, "p2", "ceylon.language.String", null, true);
        ModelUtils.checkType(tmap, "ceylon.language.String", null);

        method = (Map<String, Object>)model.get("sequencedDefaulted");
        ModelUtils.checkParam(method, 0, "s", "ceylon.language.String", "\"x\"", false);
        ModelUtils.checkParam(method, 1, "ints", "ceylon.language.Integer", null, true);

        method = (Map<String, Object>)model.get("mpl1");
        ModelUtils.checkParam(method, 0, "a", "ceylon.language.String", null, false);
        ModelUtils.checkType(method, "ceylon.language.Callable", null); //TODO check inner types

        method = (Map<String, Object>)model.get("mpl2");
        ModelUtils.checkParam(method, 0, "a", "ceylon.language.Integer", null, false);
        ModelUtils.checkType(method, "ceylon.language.Callable", null); //TODO check inner types

        method = (Map<String, Object>)model.get("parmtypes1");
        tmap = ModelUtils.checkParam(method, 0, "x", "ceylon.language.Sequence", null, false); //TODO check inner types

        method = (Map<String, Object>)model.get("parmtypes2");
        tmap = ModelUtils.checkParam(method, 0, "xx", "ceylon.language.Sequence", null, false);//TODO check inner types

        method = (Map<String, Object>)model.get("parmtypes3");
        ModelUtils.checkType(method, "ceylon.language.Sequence", null);//TODO check inner types

        method = (Map<String, Object>)model.get("parmtypes4");
        ModelUtils.checkType(method, "t1.SomethingElse", null);//TODO check inner types

        method = (Map<String, Object>)model.get("parmtypes5");
        System.out.println(method);
        ModelUtils.checkParam(method, 0, "x", "t1.Value", null, false);
}

}
