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

import org.eclipse.ceylon.compiler.java.ArrayBuilderTest;
import org.eclipse.ceylon.compiler.java.TypeDescriptorTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class) 
@SuiteClasses({
    FloatTest.class,
    IntegerTest.class,
    ArrayBuilderTest.class,
    TypeDescriptorTest.class,
    PrimitiveArrayIterableTest.class
})
public class LanguageUnitTestSuite {

}
