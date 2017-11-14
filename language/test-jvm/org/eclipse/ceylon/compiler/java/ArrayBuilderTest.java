/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.java;

import org.eclipse.ceylon.compiler.java.Util.*;
import org.junit.Assert;
import org.junit.Test;

public class ArrayBuilderTest {
    
    @Test
    public void testIntArrayBuilder() {
        IntArrayBuilder builder = new IntArrayBuilder(0);
        builder.appendArray(new int[0]);
        int[] built = builder.build();
        Assert.assertArrayEquals(new int[0], built);
        builder.appendArray(new int[]{0});
        built = builder.build();
        Assert.assertArrayEquals(new int[]{0}, built);
        builder.appendArray(new int[]{1, 2});
        built = builder.build();
        Assert.assertArrayEquals(new int[]{0, 1, 2}, built);
        builder.appendInt(3);
        built = builder.build();
        Assert.assertArrayEquals(new int[]{0, 1, 2, 3}, built);
        builder.appendInt(4);
        built = builder.build();
        Assert.assertArrayEquals(new int[]{0, 1, 2, 3, 4}, built);
        builder.appendInt(5);
        built = builder.build();
        Assert.assertArrayEquals(new int[]{0, 1, 2, 3, 4, 5}, built);
        
        builder.appendArray(new int[]{6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20});
        built = builder.build();
        Assert.assertArrayEquals(new int[]{0, 1, 2, 3, 4, 5,6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20}, built);
    }
    
    @Test
    public void testReflectingObjectArrayBuilder() {
        ReflectingObjectArrayBuilder<String> builder = new ReflectingObjectArrayBuilder<String>(0, String.class);
        builder.appendArray(new String[0]);
        String[] built = builder.build();
        Assert.assertArrayEquals(new String[0], built);
        builder.appendArray(new String[]{"0"});
        built = builder.build();
        Assert.assertArrayEquals(new String[]{"0"}, built);
        builder.appendArray(new String[]{"1", "2"});
        built = builder.build();
        Assert.assertArrayEquals(new String[]{"0", "1", "2"}, built);
        builder.appendRef("3");
        built = builder.build();
        Assert.assertArrayEquals(new String[]{"0", "1", "2", "3"}, built);
        builder.appendRef("4");
        built = builder.build();
        Assert.assertArrayEquals(new String[]{"0", "1", "2", "3", "4"}, built);
        builder.appendRef("5");
        built = builder.build();
        Assert.assertArrayEquals(new String[]{"0", "1", "2", "3", "4", "5"}, built);
        
        builder.appendArray(new String[]{"6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"});
        built = builder.build();
        Assert.assertArrayEquals(new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"}, built);
    }
}
