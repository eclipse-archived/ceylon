package com.redhat.ceylon.compiler.loader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.redhat.ceylon.compiler.typechecker.model.Annotation;
import com.redhat.ceylon.compiler.typechecker.model.Value;

public class TestBitAnnotations {

    @Test
    public void testEncode() {
        final Value v = new Value();
        int b = MetamodelGenerator.encodeAnnotations(v, null, null);
        Assert.assertEquals(0, b);
        v.getAnnotations().add(new Annotation("shared"));
        b = MetamodelGenerator.encodeAnnotations(v, null, null);
        Assert.assertEquals(1, b);
        v.getAnnotations().add(new Annotation("actual"));
        b = MetamodelGenerator.encodeAnnotations(v, null, null);
        Assert.assertEquals(3, b);
        v.getAnnotations().add(new Annotation("default"));
        b = MetamodelGenerator.encodeAnnotations(v, null, null);
        Assert.assertEquals(11, b);
    }

    private Annotation doc(String text) {
        final Annotation d = new Annotation("doc");
        d.addPositionalArgment(text);
        return d;
    }

    @Test
    public void testSplitDocs() {
        final Value v1 = new Value();
        final Value v2 = new Value();
        v1.getAnnotations().add(doc("value 1"));
        v2.getAnnotations().add(doc("value 2"));
        final HashMap<String, Object> m = new HashMap<>();
        final ArrayList<String> docs = new ArrayList<>(2);
        MetamodelGenerator.encodeAnnotations(v1, m, docs);
        Assert.assertNotNull(m.get(MetamodelGenerator.KEY_ANNOTATIONS));
        Assert.assertNotNull("0", ((Map<String,List<String>>)m.get(MetamodelGenerator.KEY_ANNOTATIONS)).get("doc").get(0));
        MetamodelGenerator.encodeAnnotations(v2, m, docs);
        Assert.assertNotNull(m.get(MetamodelGenerator.KEY_ANNOTATIONS));
        Assert.assertNotNull("1", ((Map<String,List<String>>)m.get(MetamodelGenerator.KEY_ANNOTATIONS)).get("doc").get(0));
        Assert.assertEquals(2, docs.size());
        Assert.assertEquals("value 1", docs.get(0));
        Assert.assertEquals("value 2", docs.get(1));
    }

    @Test
    public void testBits() {
        Assert.assertTrue(JsonPackage.hasAnnotationBit(11, "shared"));
        Assert.assertTrue(JsonPackage.hasAnnotationBit(11, "actual"));
        Assert.assertTrue(JsonPackage.hasAnnotationBit(11, "default"));
        Assert.assertFalse(JsonPackage.hasAnnotationBit(11, "late"));
        Assert.assertFalse(JsonPackage.hasAnnotationBit(11, "formal"));
        Assert.assertFalse(JsonPackage.hasAnnotationBit(11, "abstract"));
        Assert.assertFalse(JsonPackage.hasAnnotationBit(11, "final"));
        Assert.assertFalse(JsonPackage.hasAnnotationBit(11, "native"));
        Assert.assertFalse(JsonPackage.hasAnnotationBit(11, "sealed"));
    }

}
