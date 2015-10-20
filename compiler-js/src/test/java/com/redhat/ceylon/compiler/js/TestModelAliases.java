package com.redhat.ceylon.compiler.js;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import util.ModelUtils;

import com.redhat.ceylon.compiler.js.loader.MetamodelGenerator;
import com.redhat.ceylon.compiler.js.loader.MetamodelVisitor;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;

/** Tests for type aliases in the model
 * 
 * @author Enrique Zamudio
 */
public class TestModelAliases {

    private static Map<String, Object> topLevelModel;
    private static Map<String, Object> model;

    @SuppressWarnings("unchecked")
    @BeforeClass
    public static void initTests() {
        TestModelMethodsAndAttributes.initTypechecker();
        MetamodelVisitor mmg = null;
        for (PhasedUnit pu : TestModelMethodsAndAttributes.tc.getPhasedUnits().getPhasedUnits()) {
            if (pu.getPackage().getModule().getNameAsString().equals("t3")) {
                if (mmg == null) {
                    mmg = new MetamodelVisitor(pu.getPackage().getModule());
                }
                pu.getCompilationUnit().visit(mmg);
            }
        }
        topLevelModel = mmg.getModel();
        model = (Map<String, Object>)topLevelModel.get("t3");
    }

    @Test
    public void testTopLevelElements() {
        Assert.assertNotNull("Missing package t3", model);
        String[] tops = { "Strinteger", "Verbostring", "Matrix", "LS", "ls", "si", "Mimpl", "Numbers", "Objecton" };
        for (String tle : tops) {
            Assert.assertNotNull("Missing top-level element " + tle, model.get(tle));
        }
    }

    @Test @SuppressWarnings("unchecked")
    public void testTypeAlias() {
        Map<String,Object> ta = (Map<String,Object>)model.get("Strinteger");
        Assert.assertNotNull("Missing alias Stringteger", ta);
        ModelUtils.checkMap(ta, MetamodelGenerator.KEY_NAME, "Strinteger");
        Map<String,Object> parent = (Map<String, Object>)ta.get("$alias");
        ModelUtils.checkType(parent, "ceylon.language::String|ceylon.language::Integer");

        ta = (Map<String,Object>)model.get("Verbostring");
        Assert.assertNotNull("Missing alias Verbostring", ta);
        ModelUtils.checkMap(ta, MetamodelGenerator.KEY_NAME, "Verbostring");
        parent = (Map<String, Object>)ta.get("$alias");
        ModelUtils.checkType(parent, "ceylon.language::Sequence<ceylon.language::Character>&ceylon.language::String");

        ta = (Map<String,Object>)model.get("Numbers");
        Assert.assertNotNull("Missing alias Numbers", ta);
        ModelUtils.checkMap(ta, MetamodelGenerator.KEY_NAME, "Numbers");
        parent = (Map<String, Object>)ta.get("$alias");
        ModelUtils.checkType(parent, "ceylon.language::Sequential<ceylon.language::Integer>");

        ta = (Map<String,Object>)model.get("Objecton");
        Assert.assertNotNull("Missing alias Objecton", ta);
        ModelUtils.checkMap(ta, MetamodelGenerator.KEY_NAME, "Objecton");
        parent = (Map<String, Object>)ta.get("$alias");
        ModelUtils.checkType(parent, "T|ceylon.language::Singleton<T>");
        List<Map<String, Object>> ps = (List<Map<String, Object>>)ta.get(MetamodelGenerator.KEY_TYPE_PARAMS);
        Assert.assertEquals("Objecton must have 1 parameter type", 1, ps.size());
        ModelUtils.checkType(ps.get(0), "T");
        ps = (List<Map<String, Object>>)ps.get(0).get(MetamodelGenerator.KEY_SATISFIES);
        ModelUtils.checkType(((List<Map<String, Object>>)ps).get(0), "ceylon.language::Object");
    }

    @Test @SuppressWarnings("unchecked")
    public void testClassAlias() {
        Map<String, Object> ca = (Map<String,Object>)model.get("LS");
        Assert.assertNotNull("Missing class alias LS", ca);
        Assert.assertEquals("Not an alias", 1, ca.get("$alias"));
        ModelUtils.checkMap(ca, MetamodelGenerator.KEY_NAME, "LS");
        Map<String,Object> parent = (Map<String, Object>)ca.get("super");
        ModelUtils.checkType(parent, ".::LazyList<ceylon.language::Singleton<T>>");
        ModelUtils.checkParam(ca, 0, "ss", "ceylon.language::Singleton<T>", false, true);
        ModelUtils.checkAnnotations(ca, "shared");
        List<Map<String, Object>> ps = (List<Map<String, Object>>)ca.get(MetamodelGenerator.KEY_TYPE_PARAMS);
        Assert.assertEquals("LS must have 1 parameter type", 1, ps.size());
        ModelUtils.checkType(ps.get(0), "T");
        ps = (List<Map<String, Object>>)ps.get(0).get(MetamodelGenerator.KEY_SATISFIES);
        ModelUtils.checkType(((List<Map<String, Object>>)ps).get(0), "ceylon.language::Object");
    }

    @Test @SuppressWarnings("unchecked")
    public void testInterfaceAlias() {
        Map<String, Object> ia = (Map<String, Object>)model.get("Matrix");
        Assert.assertNotNull("Missing iface alias Matrix", ia);
        ModelUtils.checkMap(ia, MetamodelGenerator.KEY_NAME, "Matrix");
        List<Map<String, Object>> ps = (List<Map<String, Object>>)ia.get(MetamodelGenerator.KEY_TYPE_PARAMS);
        Assert.assertEquals("Matrix must have 1 parameter type", 1, ps.size());
        ModelUtils.checkType(ps.get(0), "Cell");
        ModelUtils.checkAnnotations(ia, "shared");
        Map<String,Object> parent = (Map<String, Object>)ia.get("$alias");
        ModelUtils.checkType(parent, "ceylon.language::List<ceylon.language::List<Cell>>");
    }

}
