/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.loader;

import org.eclipse.ceylon.model.typechecker.model.Annotation;
import org.eclipse.ceylon.model.typechecker.model.Value;
import org.junit.Assert;
import org.junit.Test;

import org.eclipse.ceylon.compiler.js.loader.JsonPackage;
import org.eclipse.ceylon.compiler.js.loader.MetamodelGenerator;

public class TestBitAnnotations {

    @Test
    public void testEncode() {
        final Value v = new Value();
        int b = MetamodelGenerator.encodeAnnotations(v.getAnnotations(), v, null);
        Assert.assertEquals(0, b);
        v.getAnnotations().add(new Annotation("shared"));
        b = MetamodelGenerator.encodeAnnotations(v.getAnnotations(), v, null);
        Assert.assertEquals(1, b);
        v.getAnnotations().add(new Annotation("actual"));
        b = MetamodelGenerator.encodeAnnotations(v.getAnnotations(), v, null);
        Assert.assertEquals(3, b);
        v.getAnnotations().add(new Annotation("default"));
        b = MetamodelGenerator.encodeAnnotations(v.getAnnotations(), v, null);
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
