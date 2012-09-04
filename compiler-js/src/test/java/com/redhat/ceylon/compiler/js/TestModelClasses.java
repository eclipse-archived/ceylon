package com.redhat.ceylon.compiler.js;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import util.ModelUtils;

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
        ModelUtils.checkMap(cls, "name", "SimpleClass1");
        cls = (Map<String, Object>)cls.get("super");
        ModelUtils.checkType(cls, "ceylon.language.IdentifiableObject");

        cls = (Map<String, Object>)model.get("SimpleClass2");
        ModelUtils.checkMap(cls, "name", "SimpleClass2", "abstract", "1");
        ModelUtils.checkParam(cls, 0, "name", "ceylon.language.String", null, false);
        cls = (Map<String, Object>)cls.get("super");
        ModelUtils.checkMap(cls, "name", "IdentifiableObject", "mod", "ceylon.language");

        cls = (Map<String, Object>)model.get("SimpleClass3");
        ModelUtils.checkMap(cls, "name", "SimpleClass3");
        Map<String,Map<String, Object>> m2 = (Map<String,Map<String, Object>>)cls.get("methods");
        Assert.assertEquals("SimpleClass3 should have 1 method", 1, m2.size());
        ModelUtils.checkMap(m2.get("equals"), "name", "equals", "shared", "1", "actual", "1", "mt", "method");
        ModelUtils.checkType(m2.get("equals"), "ceylon.language.Boolean");
        m2 = (Map<String,Map<String, Object>>)cls.get("attrs");
        Assert.assertEquals("SimpleClass3 should have 1 attribute", 1, m2.size());
        ModelUtils.checkMap(m2.get("hash"), "name", "hash", "actual", "1", "shared", "1", "mt", "attr");
        ModelUtils.checkType(m2.get("hash"), "ceylon.language.Integer");
        cls = (Map<String, Object>)cls.get("super");
        ModelUtils.checkType(cls, "ceylon.language.Object");

        cls = (Map<String, Object>)model.get("SimpleClass4");
        ModelUtils.checkMap(cls, "name", "SimpleClass4", "mt", "class", "shared", "1");
        ModelUtils.checkType((Map<String,Object>)cls.get("super"), "t2.SimpleClass2");
        ModelUtils.checkParam(cls, 0, "x", "ceylon.language.Integer", "1", false);
    }

    @Test @SuppressWarnings("unchecked")
    public void testSatisfies() {
        Map<String,Object> cls = (Map<String,Object>)model.get("Satisfy1");
        ModelUtils.checkType((Map<String, Object>)cls.get("super"), "ceylon.language.IdentifiableObject");
        List<Map<String, Object>> ps = (List<Map<String, Object>>)cls.get("satisfies");
        Assert.assertEquals("Satisfy1 should satisfy 1 interface", 1, ps.size());
        ModelUtils.checkType(ps.get(0), "ceylon.language.Comparable<t2.Satisfy1>");
        Map<String,Map<String,Object>> m2 = (Map<String,Map<String,Object>>)cls.get("methods");
        Assert.assertEquals("Satisfy1 should implement 1 method", 1, m2.size());
        ModelUtils.checkType(m2.get("compare"), "ceylon.language.Comparison");
        ModelUtils.checkParam(m2.get("compare"), 0, "other", "t2.Satisfy1", null, false);

        cls = (Map<String, Object>)model.get("Satisfy2");
        ModelUtils.checkType((Map<String, Object>)cls.get("super"), "ceylon.language.IdentifiableObject");
        ps = (List<Map<String, Object>>)cls.get("satisfies");
        Assert.assertEquals("Satisfy1 should satisfy 2 interfaces", 2, ps.size());
        ModelUtils.checkType(ps.get(0), "ceylon.language.Iterable<ceylon.language.Integer>");
        ModelUtils.checkType(ps.get(1), "ceylon.language.Cloneable<t2.Satisfy2>");
        m2 = (Map<String,Map<String,Object>>)cls.get("methods");
        Assert.assertEquals("Satisfy2 should have no methods", 0, m2.size());
        m2 = (Map<String,Map<String,Object>>)cls.get("attrs");
        Assert.assertEquals("Satisfy2 should have 2 attributes", 2, m2.size());
        ModelUtils.checkType(m2.get("iterator"), "ceylon.language.Iterator<ceylon.language.Integer>");
        ModelUtils.checkType(m2.get("clone"), "t2.Satisfy2");
    }

    @Test @SuppressWarnings("unchecked")
    public void testParameterTypes() {
        Map<String,Object> cls = (Map<String,Object>)model.get("ParmTypes1");
        List<Map<String, Object>> ps = (List<Map<String, Object>>)cls.get("tparams");
        Assert.assertEquals("ParmTypes1 must have 1 parameter type", 1, ps.size());
        ModelUtils.checkType(ps.get(0), "t2.Element");
        ModelUtils.checkParam(cls, 0, "x", "t2.Element", null, false);

        cls = (Map<String, Object>)model.get("ParmTypes2");
        ps = (List<Map<String, Object>>)cls.get("tparams");
        Assert.assertEquals("ParmTypes2 must have 1 parameter type", 1, ps.size());
        ModelUtils.checkType(ps.get(0), "t2.Element");
        ModelUtils.checkMap(ps.get(0), "variance", "out");
        ModelUtils.checkParam(cls, 0, "x", "t2.Element", null, true);
        ps = (List<Map<String, Object>>)cls.get("constraints");
        Assert.assertEquals("ParmTypes2 must have 1 type constraint", 1, ps.size());
        ModelUtils.checkMap(ps.get(0), "mt", "constraint");
        ModelUtils.checkType(((List<Map<String, Object>>)ps.get(0).get("satisfies")).get(0), "ceylon.language.Object");

        cls = (Map<String, Object>)model.get("ParmTypes3");
        ps = (List<Map<String, Object>>)cls.get("tparams");
        Assert.assertEquals("ParmTypes3 must have 2 parameter types", 2, ps.size());
        ModelUtils.checkType(ps.get(0), "t2.Type1");
        ModelUtils.checkType(ps.get(1), "t2.Type2");
        ModelUtils.checkParam(cls, 0, "a1", "t2.Type1", null, false);
        ModelUtils.checkParam(cls, 1, "a2", "t2.Type2", null, false);
        ps = (List<Map<String, Object>>)cls.get("constraints");
        Assert.assertEquals("ParmTypes3 must have 2 type constraints", 2, ps.size());
        ModelUtils.checkType(((List<Map<String, Object>>)ps.get(0).get("satisfies")).get(0), "ceylon.language.Number");
        ModelUtils.checkType(((List<Map<String, Object>>)ps.get(1).get("of")).get(0),
                "ceylon.language.String");
        ModelUtils.checkType(((List<Map<String, Object>>)ps.get(1).get("of")).get(1),
                "ceylon.language.Singleton<ceylon.language.String>");

        cls = (Map<String, Object>)model.get("ParmTypes4");
        ps = (List<Map<String, Object>>)cls.get("tparams");
        Assert.assertEquals("ParmTypes4 must have 1 parameter type", 1, ps.size());
        ModelUtils.checkType(ps.get(0), "t2.Element");
        ModelUtils.checkMap(ps.get(0), "variance", "out");
        ModelUtils.checkParam(cls, 0, "elems", "t2.Element", null, true);
        ps = (List<Map<String, Object>>)cls.get("satisfies");
        Assert.assertEquals("ParmTypes4 should satisfy 1 interface", 1, ps.size());
        ModelUtils.checkType(ps.get(0), "ceylon.language.Iterable<t2.Element>");
        Map<String,Map<String,Object>> m2 = (Map<String,Map<String,Object>>)cls.get("attrs");
        ModelUtils.checkType(m2.get("primero"), "ceylon.language.Nothing|t2.Element");
    }

    @Test @SuppressWarnings("unchecked")
    public void testNested() {
        Map<String,Object> cls = (Map<String,Object>)model.get("Nested1");
        ModelUtils.checkMap(cls, "name", "Nested1", "shared", "1", "mt", "class");
        cls = ((Map<String,Map<String,Object>>)cls.get("classes")).get("Nested2");
        ModelUtils.checkMap(cls, "name", "Nested2", "shared", "1", "mt", "class");
        cls = ((Map<String,Map<String,Object>>)cls.get("methods")).get("innerMethod1");
        ModelUtils.checkMap(cls, "name", "innerMethod1", "mt", "method");
        ModelUtils.checkType(cls, "ceylon.language.Void");
    }

    @Test @SuppressWarnings("unchecked")
    public void testAlgebraics() {
        Map<String,Object> cls = (Map<String,Object>)model.get("Algebraic1");
        ModelUtils.checkMap(cls, "name", "Algebraic1", "mt", "class", "abstract", "1", "shared", "1");
        //"name" is an initializer parameter...
        ModelUtils.checkParam(cls, 0, "name", "ceylon.language.String", null, false);
        //and also a shared attribute
        Map<String, Map<String, Object>> m2 = (Map<String, Map<String, Object>>)cls.get("attrs");
        ModelUtils.checkMap(m2.get("name"), "name", "name", "mt", "attr", "shared", "1");
        ModelUtils.checkType(m2.get("name"), "ceylon.language.String");
        List<Map<String, Object>> types = (List<Map<String, Object>>)cls.get("of");
        Assert.assertNotNull("Algebraic1 should have case types", types);
        Assert.assertEquals("Algebraic1 should have 3 case types", 3, types.size());
        ModelUtils.checkType(types.get(0), "t2.AlgOne");
        ModelUtils.checkType(types.get(1), "t2.AlgTwo");
        ModelUtils.checkType(types.get(2), "t2.AlgThree");
        for (Map<String, Object> m3 : types) {
            cls = (Map<String, Object>)model.get((String)m3.get("name"));
            Assert.assertNotNull("Missing top-level class " + m3.get("name"), cls);
            ModelUtils.checkMap(cls, "name", (String)m3.get("name"), "mt", "class", "shared", "1");
            cls = (Map<String,Object>)cls.get("super");
            ModelUtils.checkType(cls, "t2.Algebraic1");
        }

        cls = (Map<String, Object>)model.get("Algebraic2");
        //"name" is an initializer parameter...
        ModelUtils.checkParam(cls, 0, "name", "ceylon.language.String", null, false);
        //and also a shared attribute
        m2 = (Map<String, Map<String, Object>>)cls.get("attrs");
        ModelUtils.checkMap(m2.get("name"), "name", "name", "mt", "attr", "shared", "1");
        ModelUtils.checkType(m2.get("name"), "ceylon.language.String");
        types = (List<Map<String, Object>>)cls.get("of");
        Assert.assertNotNull("Algebraic2 should have case types", types);
        Assert.assertEquals("Algebraic2 should have 2 case types", 2, types.size());
        for (Map<String,Object> m3 : types) {
            //Get the object
            cls = (Map<String, Object>)model.get((String)m3.get("name"));
            Assert.assertNotNull("Missing top-level object " + m3.get("name"), cls);
            ModelUtils.checkMap(cls, "name", (String)m3.get("name"), "mt", "object", "shared", "1");
            types = (List<Map<String,Object>>)cls.get("satisfies");
            ModelUtils.checkType(types.get(0), "ceylon.language.Iterable<ceylon.language.Integer>");
            ModelUtils.checkType((Map<String,Object>)cls.get("super"), "t2.Algebraic2");
            m2 = (Map<String, Map<String, Object>>)cls.get("attrs");
            ModelUtils.checkMap(m2.get("iterator"), "name", "iterator", "mt", "attr", "shared", "1", "actual", "1");
            ModelUtils.checkType(m2.get("iterator"), "ceylon.language.Iterator<ceylon.language.Integer>");
        }
    }

}
