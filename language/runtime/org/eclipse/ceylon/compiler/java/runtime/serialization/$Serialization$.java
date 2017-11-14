/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.java.runtime.serialization;

/**
 * <p>We need to be able to instantiate serializable classes bypassing their 
 * normal constructor. To do this the compiler creates a constructor
 * with a unique signature (and one which is not denoteable in Ceylon):
 * {@code <init>($Serialization$), ...} 
 * (where ... means some number of {code TypeDescriptor}s)</p>
 *  
 * <p>These constructors are always invoked with a null first argument.</p>
 */
public final class $Serialization$ {
    private $Serialization$() {}
}
