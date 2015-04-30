package com.redhat.ceylon.compiler.loader;

import org.junit.Assert;
import org.junit.Test;

import com.redhat.ceylon.model.typechecker.model.Annotation;
import com.redhat.ceylon.model.typechecker.model.Value;

public class TestBitAnnotations {

    @Test
    public void testEncode() {
        final Value v = new Value();
        int b = MetamodelGenerator.encodeAnnotations(v, null);
        Assert.assertEquals(0, b);
        v.getAnnotations().add(new Annotation("shared"));
        b = MetamodelGenerator.encodeAnnotations(v, null);
        Assert.assertEquals(1, b);
        v.getAnnotations().add(new Annotation("actual"));
        b = MetamodelGenerator.encodeAnnotations(v, null);
        Assert.assertEquals(3, b);
        v.getAnnotations().add(new Annotation("default"));
        b = MetamodelGenerator.encodeAnnotations(v, null);
        Assert.assertEquals(11, b);
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
