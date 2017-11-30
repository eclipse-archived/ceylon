/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.java.wrapping;

import java.io.Serializable;

import org.eclipse.ceylon.compiler.java.wrapping.Wrapping;

/**
 * A conversion from instances of one type to instances of another.
 */
public interface Wrapping<From, To> 
        extends Serializable {
    /** Convert the given element */
    To wrap(From from);
    /** A Wrapping that applies the inverse conversion, or null if no such Wrapping exists. */
    Wrapping<To,From> inverted();
}