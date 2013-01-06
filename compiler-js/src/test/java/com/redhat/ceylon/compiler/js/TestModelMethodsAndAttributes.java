package com.redhat.ceylon.compiler.js;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import util.ModelUtils;

import com.redhat.ceylon.compiler.loader.MetamodelGenerator;
import com.redhat.ceylon.compiler.typechecker.TypeChecker;
import com.redhat.ceylon.compiler.typechecker.TypeCheckerBuilder;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;

public class TestModelMethodsAndAttributes {

    static TypeChecker tc;
    private static Map<String, Object> topLevelModel;
    private static Map<String, Object> model;

    public static void initTypechecker() {
        if (model == null) {
            TypeCheckerBuilder builder = new TypeCheckerBuilder();
            builder.addSrcDirectory(new File("src/test/resources/modeltests"));
            tc = builder.getTypeChecker();
            tc.process();
        }
    }
    @SuppressWarnings("unchecked")
    @BeforeClass
    public static void initModel() {
        initTypechecker();
        MetamodelVisitor mmg = null;
        for (PhasedUnit pu : tc.getPhasedUnits().getPhasedUnits()) {
            if (pu.getPackage().getModule().getNameAsString().equals("t1")) {
                if (mmg == null) {
                    mmg = new MetamodelVisitor(pu.getPackage().getModule());
                }
                pu.getCompilationUnit().visit(mmg);
            }
        }
        topLevelModel = mmg.getModel();
        model = (Map<String,Object>)topLevelModel.get("t1");
    }

    @Test
    public void testTopLevelElements() {
        Assert.assertNotNull("Missing package t1", model);
        String[] tops = { "simple1", "simple2", "simple3", "defaulted1", "sequenced1", "sequencedDefaulted",
                "mpl1", "mpl2", "nested", "parmtypes1", "parmtypes2", "parmtypes3", "parmtypes4",
                "parmtypes5", "intersector1", "intersector2", "i1", "s1", "pi", "seq", "union", "useq" };
        for (String tle : tops) {
            Assert.assertNotNull("Missing top-level element " + tle, model.get(tle));
        }
    }

    @Test @SuppressWarnings("unchecked")
    public void testSimpleMethods() {
        //simple1
        Map<String, Object> method = (Map<String, Object>)model.get("simple1");
        Assert.assertNotNull(method);
        ModelUtils.checkMap(method, MetamodelGenerator.KEY_NAME, "simple1",
                MetamodelGenerator.KEY_METATYPE, MetamodelGenerator.METATYPE_METHOD);
        ModelUtils.checkType(method, "ceylon.language::Anything");

        method = (Map<String, Object>)model.get("simple2");
        ModelUtils.checkMap(method, MetamodelGenerator.KEY_NAME, "simple2",
                MetamodelGenerator.KEY_METATYPE, MetamodelGenerator.METATYPE_METHOD);
        ModelUtils.checkAnnotations(method, "shared");
        ModelUtils.checkType(method, "ceylon.language::Integer");

        method = (Map<String, Object>)model.get("simple3");
        ModelUtils.checkMap(method, MetamodelGenerator.KEY_NAME, "simple3",
                MetamodelGenerator.KEY_METATYPE, MetamodelGenerator.METATYPE_METHOD);
        ModelUtils.checkType(method, "ceylon.language::Anything");
        ModelUtils.checkParam(method, 0, "p1", "ceylon.language::Integer", false, false);
        ModelUtils.checkParam(method, 1, "p2", "ceylon.language::String", false, false);
    }

    @Test @SuppressWarnings("unchecked")
    public void testDefaultedAndSequencedMethods() {
        Map<String, Object> method = (Map<String, Object>)model.get("defaulted1");
        ModelUtils.checkParam(method, 0, "p1", "ceylon.language::Integer", false, false);
        ModelUtils.checkParam(method, 1, "p2", "ceylon.language::Integer", true, false);

        method = (Map<String, Object>)model.get("sequenced1");
        ModelUtils.checkParam(method, 0, "p1", "ceylon.language::Integer", false, false);
        ModelUtils.checkParam(method, 1, "p2", "ceylon.language::String", false, true);

        method = (Map<String, Object>)model.get("sequencedDefaulted");
        ModelUtils.checkParam(method, 0, "s", "ceylon.language::String", true, false);
        ModelUtils.checkParam(method, 1, "ints", "ceylon.language::Integer", false, true);
    }

    @Test @SuppressWarnings("unchecked")
    public void testMultipleParameterLists() {
        Map<String, Object> method = (Map<String, Object>)model.get("mpl1");
        ModelUtils.checkParam(method, 0, "a", "ceylon.language::String", false, false);
        ModelUtils.checkType(method, "ceylon.language::Integer");

        method = (Map<String, Object>)model.get("mpl2");
        ModelUtils.checkParam(method, 0, "a", "ceylon.language::Integer", false, false);
        ModelUtils.checkType(method, "ceylon.language::String");
    }

    @Test @SuppressWarnings("unchecked")
    public void testParameterTypes() {
        Map<String, Object> method = (Map<String, Object>)model.get("parmtypes1");
        ModelUtils.checkParam(method, 0, "x", "ceylon.language::Sequence<ceylon.language::Integer>", false, false);

        method = (Map<String, Object>)model.get("parmtypes2");
        ModelUtils.checkParam(method, 0, "xx",
                "ceylon.language::Sequence<ceylon.language::Iterable<ceylon.language::String>>", false, false);

        method = (Map<String, Object>)model.get("parmtypes3");
        ModelUtils.checkType(method, "ceylon.language::Sequence<ceylon.language::String>");

        method = (Map<String, Object>)model.get("parmtypes4");
        ModelUtils.checkType(method, "SomethingElse");
        List<Map<String, Object>> cons = (List<Map<String, Object>>)method.get(MetamodelGenerator.KEY_TYPE_PARAMS);
        ModelUtils.checkTypeParameters(0, (List<Map<String,Object>>)cons.get(0).get("satisfies"),
                "ceylon.language::Comparable<Something>");
        ModelUtils.checkTypeParameters(0, (List<Map<String,Object>>)cons.get(1).get("satisfies"), "Something");

        method = (Map<String, Object>)model.get("parmtypes5");
        ModelUtils.checkParam(method, 0, "x", "Value", false, false);
        cons = (List<Map<String, Object>>)method.get(MetamodelGenerator.KEY_TYPE_PARAMS);
        Assert.assertNotNull("parmtypes5 should have parameters", cons);
        Assert.assertNotNull("parmtypes5 should have case types", cons.get(0).get("of"));
        ModelUtils.checkTypeParameters(0, (List<Map<String,Object>>)cons.get(0).get("of"),
                "ceylon.language::Integer,ceylon.language::Float");
    }

    @Test @SuppressWarnings("unchecked")
    public void testIntersectionTypes() {
        Map<String, Object> method = (Map<String, Object>)model.get("intersector1");
        ModelUtils.checkParam(method, 0, "inters", "ceylon.language::Category&ceylon.language::Container", false, false);
        method = (Map<String,Object>)model.get("intersector2");
        System.out.println(method);
        ModelUtils.checkParam(method, 0, "beast",
                "ceylon.language::Category&ceylon.language::Iterable<ceylon.language::Container>", false, false);
    }

    @Test @SuppressWarnings("unchecked")
    public void testAttributes() {
        Map<String, Object> attrib = (Map<String, Object>)model.get("i1");
        ModelUtils.checkType(attrib, "ceylon.language::Integer");
        Assert.assertEquals("Wrong model type for i1", MetamodelGenerator.METATYPE_ATTRIBUTE,
                attrib.get(MetamodelGenerator.KEY_METATYPE));
        attrib = (Map<String, Object>)model.get("s1");
        ModelUtils.checkType(attrib, "ceylon.language::String");
        ModelUtils.checkAnnotations(attrib, "shared");
        attrib = (Map<String, Object>)model.get("pi");
        ModelUtils.checkType(attrib, "ceylon.language::Float");
        ModelUtils.checkAnnotations(attrib, "variable");
        attrib = (Map<String, Object>)model.get("seq");
        ModelUtils.checkType(attrib, "ceylon.language::Sequence<ceylon.language::Integer>");
        ModelUtils.checkAnnotations(attrib, "shared", "variable");
        attrib = (Map<String, Object>)model.get("union");
        ModelUtils.checkType(attrib, "ceylon.language::Integer|ceylon.language::String");
        attrib = (Map<String, Object>)model.get("useq");
        ModelUtils.checkType(attrib, "ceylon.language::Container<ceylon.language::Singleton<ceylon.language::Integer>|ceylon.language::String>");
    }

    @Test @SuppressWarnings("unchecked")
    public void testSequentials() {
        Map<String, Object> attrib = (Map<String, Object>)model.get("sequentialAttribute1");
        ModelUtils.checkType(attrib, "ceylon.language::Sequential<ceylon.language::Integer>");
        attrib = (Map<String, Object>)model.get("sequentialAttribute2");
        ModelUtils.checkType(attrib, "ceylon.language::Sequential<ceylon.language::Integer>");
    }

}
