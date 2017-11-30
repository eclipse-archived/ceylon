/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package ceylon.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class IntegerTest {

    @Test
    public void testConversionToInteger() {
        assertEquals(0.0, Integer.instance(0).getFloat(), 0.0);
        assertEquals(1.0, Integer.instance(1).getFloat(), 1.0);
        assertEquals(9007199254740991.0, Integer.instance(9007199254740991L).getFloat(), 0.0);
        assertEquals(-9007199254740991.0, Integer.instance(-9007199254740991L).getFloat(), 0.0);
        
        try {
            Integer.instance(9007199254740992L).getFloat();
            fail("OverflowException expected");
        } catch (OverflowException e) {
            // Checking that this is thrown
        }
        
        try {
            Integer.instance(-9007199254740992L).getFloat();
            fail("OverflowException expected");
        } catch (OverflowException e) {
            // Checking that this is thrown
        }
    }
}
