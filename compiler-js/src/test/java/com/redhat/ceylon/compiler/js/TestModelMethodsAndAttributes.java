package com.redhat.ceylon.compiler.js;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import util.ModelUtils;

import com.redhat.ceylon.compiler.typechecker.TypeChecker;
import com.redhat.ceylon.compiler.typechecker.TypeCheckerBuilder;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;

public class TestModelMethodsAndAttributes {

    static TypeChecker tc;
    private static Map<String, Object> model;

    @BeforeClass
    public static void initTypechecker() {
        if (model == null) {
            TypeCheckerBuilder builder = new TypeCheckerBuilder();
            builder.addSrcDirectory(new File("src/test/resources/modeltests"));
            tc = builder.getTypeChecker();
            tc.process();
            MetamodelGenerator mmg = null;
            for (PhasedUnit pu : tc.getPhasedUnits().getPhasedUnits()) {
                if (pu.getPackage().getModule().getNameAsString().equals("t1")) {
                    if (mmg == null) {
                        mmg = new MetamodelGenerator(pu.getPackage().getModule());
                    }
                    pu.getCompilationUnit().visit(mmg);
                }
            }
            model = mmg.getModel();
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
    public void testSimpleMethods() {
        System.out.println("top-level elements:" + model.keySet());
        //simple1
        Map<String, Object> method = (Map<String, Object>)model.get("simple1");
        Assert.assertNotNull(method);
        ModelUtils.checkMap(method, MetamodelGenerator.KEY_NAME, "simple1",
                MetamodelGenerator.KEY_METATYPE, MetamodelGenerator.METATYPE_METHOD);
        ModelUtils.checkType(method, "ceylon.language.Void");

        method = (Map<String, Object>)model.get("simple2");
        ModelUtils.checkMap(method, MetamodelGenerator.KEY_NAME, "simple2",
                MetamodelGenerator.KEY_METATYPE, MetamodelGenerator.METATYPE_METHOD, "shared", "1");
        ModelUtils.checkType(method, "ceylon.language.Integer");

        method = (Map<String, Object>)model.get("simple3");
        ModelUtils.checkMap(method, MetamodelGenerator.KEY_NAME, "simple3",
                MetamodelGenerator.KEY_METATYPE, MetamodelGenerator.METATYPE_METHOD);
        ModelUtils.checkType(method, "ceylon.language.Void");
        ModelUtils.checkParam(method, 0, "p1", "ceylon.language.Integer", null, false);
        ModelUtils.checkParam(method, 1, "p2", "ceylon.language.String", null, false);
    }

    @Test @SuppressWarnings("unchecked")
    public void testDefaultedAndSequencedMethods() {
        Map<String, Object> method = (Map<String, Object>)model.get("defaulted1");
        ModelUtils.checkParam(method, 0, "p1", "ceylon.language.Integer", null, false);
        ModelUtils.checkParam(method, 1, "p2", "ceylon.language.Integer", "5", false);

        method = (Map<String, Object>)model.get("sequenced1");
        ModelUtils.checkParam(method, 0, "p1", "ceylon.language.Integer", null, false);
        ModelUtils.checkParam(method, 1, "p2", "ceylon.language.String", null, true);

        method = (Map<String, Object>)model.get("sequencedDefaulted");
        ModelUtils.checkParam(method, 0, "s", "ceylon.language.String", "\"x\"", false);
        ModelUtils.checkParam(method, 1, "ints", "ceylon.language.Integer", null, true);
    }

    @Test @SuppressWarnings("unchecked")
    public void testMultipleParameterLists() {
        Map<String, Object> method = (Map<String, Object>)model.get("mpl1");
        ModelUtils.checkParam(method, 0, "a", "ceylon.language.String", null, false);
        ModelUtils.checkType(method, "ceylon.language.Callable<ceylon.language.Integer,ceylon.language.String>");

        method = (Map<String, Object>)model.get("mpl2");
        ModelUtils.checkParam(method, 0, "a", "ceylon.language.Integer", null, false);
        ModelUtils.checkType(method, "ceylon.language.Callable<ceylon.language.Callable<ceylon.language.String,ceylon.language.Float>,ceylon.language.Object>");
    }

    @Test @SuppressWarnings("unchecked")
    public void testParameterTypes() {
        Map<String, Object> method = (Map<String, Object>)model.get("parmtypes1");
        ModelUtils.checkParam(method, 0, "x", "ceylon.language.Sequence<ceylon.language.Integer>", null, false);

        method = (Map<String, Object>)model.get("parmtypes2");
        ModelUtils.checkParam(method, 0, "xx",
                "ceylon.language.Sequence<ceylon.language.Iterable<ceylon.language.String>>", null, false);

        method = (Map<String, Object>)model.get("parmtypes3");
        ModelUtils.checkType(method, "ceylon.language.Sequence<ceylon.language.String>");

        method = (Map<String, Object>)model.get("parmtypes4");
        ModelUtils.checkType(method, "t1.SomethingElse");
        List<Map<String, Object>> cons = (List<Map<String, Object>>)method.get(MetamodelGenerator.KEY_TYPE_CONSTR);
        Assert.assertNotNull("parmtypes4 should have constraints", cons);
        ModelUtils.checkTypeParameters(0, (List<Map<String,Object>>)cons.get(0).get("satisfies"),
                "ceylon.language.Comparable<t1.Something>");
        ModelUtils.checkTypeParameters(0, (List<Map<String,Object>>)cons.get(1).get("satisfies"), "t1.Something");

        method = (Map<String, Object>)model.get("parmtypes5");
        ModelUtils.checkParam(method, 0, "x", "t1.Value", null, false);
        cons = (List<Map<String, Object>>)method.get(MetamodelGenerator.KEY_TYPE_CONSTR);
        Assert.assertNotNull("parmtypes5 should have constraints", cons);
        Assert.assertNotNull("parmtypes5 should have case types", cons.get(0).get("of"));
        ModelUtils.checkTypeParameters(0, (List<Map<String,Object>>)cons.get(0).get("of"),
                "ceylon.language.Integer,ceylon.language.Float");
    }

    @Test @SuppressWarnings("unchecked")
    public void testIntersectionTypes() {
        Map<String, Object> method = (Map<String, Object>)model.get("intersector1");
        ModelUtils.checkParam(method, 0, "inters", "ceylon.language.Category&ceylon.language.Container", null, false);
        method = (Map<String,Object>)model.get("intersector2");
        ModelUtils.checkParam(method, 0, "beast",
                "ceylon.language.Category&ceylon.language.Iterable<ceylon.language.Container>", null, false);
    }

    @Test @SuppressWarnings("unchecked")
    public void testAttributes() {
        Map<String, Object> attrib = (Map<String, Object>)model.get("i1");
        ModelUtils.checkType(attrib, "ceylon.language.Integer");
        Assert.assertEquals("Wrong model type for i1", MetamodelGenerator.METATYPE_ATTRIBUTE,
                attrib.get(MetamodelGenerator.KEY_METATYPE));
        attrib = (Map<String, Object>)model.get("s1");
        ModelUtils.checkType(attrib, "ceylon.language.String");
        Assert.assertEquals("s1 should be shared", "1", attrib.get("shared"));
        attrib = (Map<String, Object>)model.get("pi");
        ModelUtils.checkType(attrib, "ceylon.language.Float");
        Assert.assertEquals("pi should be variable", "1", attrib.get("var"));
        attrib = (Map<String, Object>)model.get("seq");
        ModelUtils.checkType(attrib, "ceylon.language.Sequence<ceylon.language.Integer>");
        Assert.assertEquals("seq should be shared", "1", attrib.get("shared"));
        Assert.assertEquals("seq should be variable", "1", attrib.get("var"));
        attrib = (Map<String, Object>)model.get("union");
        ModelUtils.checkType(attrib, "ceylon.language.Integer|ceylon.language.String");
        attrib = (Map<String, Object>)model.get("useq");
        ModelUtils.checkType(attrib, "ceylon.language.ContainerWithFirstElement<ceylon.language.Singleton<ceylon.language.Integer>|ceylon.language.String,ceylon.language.Nothing>");
    }

}
