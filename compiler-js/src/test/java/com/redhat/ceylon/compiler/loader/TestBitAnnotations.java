package com.redhat.ceylon.compiler.loader;

import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;

import com.redhat.ceylon.compiler.typechecker.model.Annotation;
import com.redhat.ceylon.compiler.typechecker.model.Value;

public class TestBitAnnotations {

    @Test
    public void testEncode() {
        final Value v = new Value();
        final HashMap<String,Object> m = new HashMap<>();
        MetamodelGenerator.encodeAnnotations(v, m);
        Assert.assertNull(m.get(MetamodelGenerator.KEY_PACKED_ANNS));
        v.getAnnotations().add(new Annotation("shared"));
        MetamodelGenerator.encodeAnnotations(v, m);
        Assert.assertEquals(1, m.get(MetamodelGenerator.KEY_PACKED_ANNS));
        v.getAnnotations().add(new Annotation("actual"));
        MetamodelGenerator.encodeAnnotations(v, m);
        Assert.assertEquals(3, m.get(MetamodelGenerator.KEY_PACKED_ANNS));
        v.getAnnotations().add(new Annotation("default"));
        MetamodelGenerator.encodeAnnotations(v, m);
        Assert.assertEquals(11, m.get(MetamodelGenerator.KEY_PACKED_ANNS));
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
