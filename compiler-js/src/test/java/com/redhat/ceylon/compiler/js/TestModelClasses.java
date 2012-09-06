package com.redhat.ceylon.compiler.js;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import util.ModelUtils;

import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;

public class TestModelClasses {

    private static Map<String, Object> topLevelModel;
    private static Map<String, Object> model;

    @SuppressWarnings("unchecked")
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
        topLevelModel = mmg.getModel();
        model = (Map<String, Object>)topLevelModel.get("t2");
    }

    @Test
    public void testTopLevelElements() {
        Assert.assertNotNull("Missing package t2", model);
        String[] tops = { "Algebraic1", "Algebraic2", "algobj1", "algobj2", "AlgOne", "AlgTwo", "AlgThree",
                "ParmTypes1", "ParmTypes2", "ParmTypes3", "ParmTypes4",
                "SimpleClass1", "SimpleClass2", "SimpleClass3", "SimpleClass4",
                "Nested1", "Satisfy2", "Satisfy1"};
        for (String tle : tops) {
            Assert.assertNotNull("Missing top-level element " + tle, model.get(tle));
        }
    }

    @Test @SuppressWarnings("unchecked")
    public void testSimpleClasses() {
        Map<String,Object> cls = (Map<String,Object>)model.get("SimpleClass1");
        ModelUtils.checkMap(cls, MetamodelGenerator.KEY_NAME, "SimpleClass1");
        cls = (Map<String, Object>)cls.get("super");
        ModelUtils.checkType(cls, "ceylon.language.IdentifiableObject");

        cls = (Map<String, Object>)model.get("SimpleClass2");
        ModelUtils.checkMap(cls, MetamodelGenerator.KEY_NAME, "SimpleClass2", "abstract", "1");
        ModelUtils.checkParam(cls, 0, "name", "ceylon.language.String", null, false);
        cls = (Map<String, Object>)cls.get("super");
        ModelUtils.checkMap(cls, MetamodelGenerator.KEY_NAME, "IdentifiableObject",
                MetamodelGenerator.KEY_MODULE, "ceylon.language");

        cls = (Map<String, Object>)model.get("SimpleClass4");
        ModelUtils.checkMap(cls, MetamodelGenerator.KEY_NAME, "SimpleClass4",
                MetamodelGenerator.KEY_METATYPE, MetamodelGenerator.METATYPE_CLASS, "shared", "1");
        ModelUtils.checkType((Map<String,Object>)cls.get("super"), "t2.SimpleClass2");
        ModelUtils.checkParam(cls, 0, "x", "ceylon.language.Integer", "1", false);
    }

    @Test @SuppressWarnings("unchecked")
    public void testSimpleClassWithMethods() {

        Map<String, Object> cls = (Map<String, Object>)model.get("SimpleClass3");
        ModelUtils.checkMap(cls, MetamodelGenerator.KEY_NAME, "SimpleClass3");
        Map<String,Map<String, Object>> m2 = (Map<String,Map<String, Object>>)cls.get(MetamodelGenerator.KEY_METHODS);
        Assert.assertEquals("SimpleClass3 should have 1 method", 1, m2.size());
        ModelUtils.checkMap(m2.get("equals"), MetamodelGenerator.KEY_NAME, "equals", "shared", "1", "actual", "1",
                MetamodelGenerator.KEY_METATYPE, MetamodelGenerator.METATYPE_METHOD);
        ModelUtils.checkType(m2.get("equals"), "ceylon.language.Boolean");
        m2 = (Map<String,Map<String, Object>>)cls.get(MetamodelGenerator.KEY_ATTRIBUTES);
        Assert.assertEquals("SimpleClass3 should have 1 attribute", 1, m2.size());
        ModelUtils.checkMap(m2.get("hash"), MetamodelGenerator.KEY_NAME, "hash", "actual", "1", "shared", "1",
                MetamodelGenerator.KEY_METATYPE, MetamodelGenerator.METATYPE_ATTRIBUTE);
        ModelUtils.checkType(m2.get("hash"), "ceylon.language.Integer");
        cls = (Map<String, Object>)cls.get("super");
        ModelUtils.checkType(cls, "ceylon.language.Object");
    }

    @Test @SuppressWarnings("unchecked")
    public void testSatisfies1() {
        Map<String,Object> cls = (Map<String,Object>)model.get("Satisfy1");
        ModelUtils.checkType((Map<String, Object>)cls.get("super"), "ceylon.language.IdentifiableObject");
        List<Map<String, Object>> ps = (List<Map<String, Object>>)cls.get("satisfies");
        Assert.assertEquals("Satisfy1 should satisfy 1 interface", 1, ps.size());
        ModelUtils.checkType(ps.get(0), "ceylon.language.Comparable<t2.Satisfy1>");
        Map<String,Map<String,Object>> m2 = (Map<String,Map<String,Object>>)cls.get(MetamodelGenerator.KEY_METHODS);
        Assert.assertEquals("Satisfy1 should implement 1 method", 1, m2.size());
        ModelUtils.checkType(m2.get("compare"), "ceylon.language.Comparison");
        ModelUtils.checkParam(m2.get("compare"), 0, "other", "t2.Satisfy1", null, false);
    }

    @Test @SuppressWarnings("unchecked")
    public void testSatisfies2() {
        Map<String,Object> cls = (Map<String, Object>)model.get("Satisfy2");
        ModelUtils.checkType((Map<String, Object>)cls.get("super"), "ceylon.language.IdentifiableObject");
        List<Map<String, Object>> ps = (List<Map<String, Object>>)cls.get("satisfies");
        Assert.assertEquals("Satisfy1 should satisfy 2 interfaces", 2, ps.size());
        ModelUtils.checkType(ps.get(0), "ceylon.language.Iterable<ceylon.language.Integer>");
        ModelUtils.checkType(ps.get(1), "ceylon.language.Cloneable<t2.Satisfy2>");
        Map<String,Map<String,Object>> m2 = (Map<String,Map<String,Object>>)cls.get("methods");
        Assert.assertNull("Satisfy2 should have no methods", m2);
        m2 = (Map<String,Map<String,Object>>)cls.get(MetamodelGenerator.KEY_ATTRIBUTES);
        Assert.assertEquals("Satisfy2 should have 2 attributes", 2, m2.size());
        ModelUtils.checkType(m2.get("iterator"), "ceylon.language.Iterator<ceylon.language.Integer>");
        ModelUtils.checkType(m2.get("clone"), "t2.Satisfy2");
    }

    @Test @SuppressWarnings("unchecked")
    public void testParameterTypes1() {
        Map<String,Object> cls = (Map<String,Object>)model.get("ParmTypes1");
        List<Map<String, Object>> ps = (List<Map<String, Object>>)cls.get(MetamodelGenerator.KEY_TYPE_PARAMS);
        Assert.assertEquals("ParmTypes1 must have 1 parameter type", 1, ps.size());
        ModelUtils.checkType(ps.get(0), "t2.Element");
        ModelUtils.checkParam(cls, 0, "x", "t2.Element", null, false);
    }

    @Test @SuppressWarnings("unchecked")
    public void testParameterTypes2() {
        Map<String,Object> cls = (Map<String, Object>)model.get("ParmTypes2");
        List<Map<String, Object>> ps = (List<Map<String, Object>>)cls.get(MetamodelGenerator.KEY_TYPE_PARAMS);
        Assert.assertEquals("ParmTypes2 must have 1 parameter type", 1, ps.size());
        ModelUtils.checkType(ps.get(0), "t2.Element");
        ModelUtils.checkMap(ps.get(0), "variance", "out");
        ModelUtils.checkParam(cls, 0, "x", "t2.Element", null, true);
        ps = (List<Map<String, Object>>)cls.get(MetamodelGenerator.KEY_TYPE_CONSTR);
        Assert.assertEquals("ParmTypes2 must have 1 type constraint", 1, ps.size());
        ModelUtils.checkMap(ps.get(0), MetamodelGenerator.KEY_METATYPE, MetamodelGenerator.METATYPE_TYPE_CONSTRAINT);
        ModelUtils.checkType(((List<Map<String, Object>>)ps.get(0).get("satisfies")).get(0), "ceylon.language.Object");
    }

    @Test @SuppressWarnings("unchecked")
    public void testParameterTypes3() {
        Map<String,Object> cls = (Map<String, Object>)model.get("ParmTypes3");
        List<Map<String, Object>> ps = (List<Map<String, Object>>)cls.get(MetamodelGenerator.KEY_TYPE_PARAMS);
        Assert.assertEquals("ParmTypes3 must have 2 parameter types", 2, ps.size());
        ModelUtils.checkType(ps.get(0), "t2.Type1");
        ModelUtils.checkType(ps.get(1), "t2.Type2");
        ModelUtils.checkParam(cls, 0, "a1", "t2.Type1", null, false);
        ModelUtils.checkParam(cls, 1, "a2", "t2.Type2", null, false);
        ps = (List<Map<String, Object>>)cls.get(MetamodelGenerator.KEY_TYPE_CONSTR);
        Assert.assertEquals("ParmTypes3 must have 2 type constraints", 2, ps.size());
        ModelUtils.checkType(((List<Map<String, Object>>)ps.get(0).get("satisfies")).get(0), "ceylon.language.Number");
        ModelUtils.checkType(((List<Map<String, Object>>)ps.get(1).get("of")).get(0),
                "ceylon.language.String");
        ModelUtils.checkType(((List<Map<String, Object>>)ps.get(1).get("of")).get(1),
                "ceylon.language.Singleton<ceylon.language.String>");
    }

    @Test @SuppressWarnings("unchecked")
    public void testParameterTypes4() {
        Map<String,Object> cls = (Map<String, Object>)model.get("ParmTypes4");
        List<Map<String, Object>> ps = (List<Map<String, Object>>)cls.get(MetamodelGenerator.KEY_TYPE_PARAMS);
        Assert.assertEquals("ParmTypes4 must have 1 parameter type", 1, ps.size());
        ModelUtils.checkType(ps.get(0), "t2.Element");
        ModelUtils.checkMap(ps.get(0), "variance", "out");
        ModelUtils.checkParam(cls, 0, "elems", "t2.Element", null, true);
        ps = (List<Map<String, Object>>)cls.get("satisfies");
        Assert.assertEquals("ParmTypes4 should satisfy 1 interface", 1, ps.size());
        ModelUtils.checkType(ps.get(0), "ceylon.language.Iterable<t2.Element>");
        Map<String,Map<String,Object>> m2 = (Map<String,Map<String,Object>>)cls.get(MetamodelGenerator.KEY_ATTRIBUTES);
        ModelUtils.checkType(m2.get("primero"), "ceylon.language.Nothing|t2.Element");
    }

    @Test @SuppressWarnings("unchecked")
    public void testNested() {
        Map<String,Object> cls = (Map<String,Object>)model.get("Nested1");
        ModelUtils.checkMap(cls, MetamodelGenerator.KEY_NAME, "Nested1", "shared", "1",
                MetamodelGenerator.KEY_METATYPE, MetamodelGenerator.METATYPE_CLASS);
        cls = ((Map<String,Map<String,Object>>)cls.get(MetamodelGenerator.KEY_CLASSES)).get("Nested2");
        ModelUtils.checkMap(cls, MetamodelGenerator.KEY_NAME, "Nested2", "shared", "1",
                MetamodelGenerator.KEY_METATYPE, MetamodelGenerator.METATYPE_CLASS);
        cls = ((Map<String,Map<String,Object>>)cls.get(MetamodelGenerator.KEY_METHODS)).get("innerMethod1");
        ModelUtils.checkMap(cls, MetamodelGenerator.KEY_NAME, "innerMethod1",
                MetamodelGenerator.KEY_METATYPE, MetamodelGenerator.METATYPE_METHOD);
        ModelUtils.checkType(cls, "ceylon.language.Void");
    }

    @Test @SuppressWarnings("unchecked")
    public void testAlgebraicClasses() {
        Map<String,Object> cls = (Map<String,Object>)model.get("Algebraic1");
        ModelUtils.checkMap(cls, MetamodelGenerator.KEY_NAME, "Algebraic1",
                MetamodelGenerator.KEY_METATYPE, MetamodelGenerator.METATYPE_CLASS, "abstract", "1", "shared", "1");
        //"name" is an initializer parameter...
        ModelUtils.checkParam(cls, 0, "name", "ceylon.language.String", null, false);
        //and also a shared attribute
        Map<String, Map<String, Object>> m2 = (Map<String, Map<String, Object>>)cls.get(MetamodelGenerator.KEY_ATTRIBUTES);
        ModelUtils.checkMap(m2.get("name"), MetamodelGenerator.KEY_NAME, "name",
                MetamodelGenerator.KEY_METATYPE, MetamodelGenerator.METATYPE_ATTRIBUTE, "shared", "1");
        ModelUtils.checkType(m2.get("name"), "ceylon.language.String");
        List<Map<String, Object>> types = (List<Map<String, Object>>)cls.get("of");
        Assert.assertNotNull("Algebraic1 should have case types", types);
        Assert.assertEquals("Algebraic1 should have 3 case types", 3, types.size());
        ModelUtils.checkType(types.get(0), "t2.AlgOne");
        ModelUtils.checkType(types.get(1), "t2.AlgTwo");
        ModelUtils.checkType(types.get(2), "t2.AlgThree");
        for (Map<String, Object> m3 : types) {
            cls = (Map<String, Object>)model.get((String)m3.get(MetamodelGenerator.KEY_NAME));
            Assert.assertNotNull("Missing top-level class " + m3.get(MetamodelGenerator.KEY_NAME), cls);
            ModelUtils.checkMap(cls, MetamodelGenerator.KEY_NAME, (String)m3.get(MetamodelGenerator.KEY_NAME),
                    MetamodelGenerator.KEY_METATYPE, MetamodelGenerator.METATYPE_CLASS, "shared", "1");
            cls = (Map<String,Object>)cls.get("super");
            ModelUtils.checkType(cls, "t2.Algebraic1");
        }
    }

    @Test @SuppressWarnings("unchecked")
    public void testAlgebraicObjects() {
        Map<String,Object> cls = (Map<String, Object>)model.get("Algebraic2");
        //"name" is an initializer parameter...
        ModelUtils.checkParam(cls, 0, "name", "ceylon.language.String", null, false);
        //and also a shared attribute
        Map<String, Map<String, Object>> m2 = (Map<String, Map<String, Object>>)cls.get(MetamodelGenerator.KEY_ATTRIBUTES);
        ModelUtils.checkMap(m2.get("name"), MetamodelGenerator.KEY_NAME, "name",
                MetamodelGenerator.KEY_METATYPE, MetamodelGenerator.METATYPE_ATTRIBUTE, "shared", "1");
        ModelUtils.checkType(m2.get("name"), "ceylon.language.String");
        List<Map<String, Object>> types = (List<Map<String, Object>>)cls.get("of");
        Assert.assertNotNull("Algebraic2 should have case types", types);
        Assert.assertEquals("Algebraic2 should have 2 case types", 2, types.size());
        for (Map<String,Object> m3 : types) {
            //Get the object
            cls = (Map<String, Object>)model.get((String)m3.get(MetamodelGenerator.KEY_NAME));
            Assert.assertNotNull("Missing top-level object " + m3.get(MetamodelGenerator.KEY_NAME), cls);
            ModelUtils.checkMap(cls, MetamodelGenerator.KEY_NAME, (String)m3.get(MetamodelGenerator.KEY_NAME),
                    MetamodelGenerator.KEY_METATYPE, MetamodelGenerator.METATYPE_OBJECT, "shared", "1");
            types = (List<Map<String,Object>>)cls.get("satisfies");
            ModelUtils.checkType(types.get(0), "ceylon.language.Iterable<ceylon.language.Integer>");
            ModelUtils.checkType((Map<String,Object>)cls.get("super"), "t2.Algebraic2");
            m2 = (Map<String, Map<String, Object>>)cls.get(MetamodelGenerator.KEY_ATTRIBUTES);
            ModelUtils.checkMap(m2.get("iterator"), MetamodelGenerator.KEY_NAME, "iterator",
                    MetamodelGenerator.KEY_METATYPE, MetamodelGenerator.METATYPE_ATTRIBUTE, "shared", "1", "actual", "1");
            ModelUtils.checkType(m2.get("iterator"), "ceylon.language.Iterator<ceylon.language.Integer>");
        }
    }

}
