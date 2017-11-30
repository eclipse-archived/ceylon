/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.common;

/**
 * Believe it or not, but auto-boxing returns instances of Boolean which are not == Boolean.TRUE
 * so we need something smarter :(
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class BooleanUtil {
    /**
     * @return true if b is not null and is true
     */
    public static boolean isTrue(Boolean b){
        return b != null && b.booleanValue();
    }

    /**
     * @return true if b is not null and is false
     */
    public static boolean isFalse(Boolean b){
        return b != null && !b.booleanValue();
    }

    /**
     * @return true if b is null or is true
     */
    public static boolean isNotFalse(Boolean b){
        return b == null || b.booleanValue();
    }
}
