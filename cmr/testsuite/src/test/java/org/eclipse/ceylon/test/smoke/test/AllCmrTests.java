/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.test.smoke.test;

import org.eclipse.ceylon.test.maven.test.AetherTestCase;
import org.eclipse.ceylon.test.maven.test.ResolverTestCase;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
        SmokeTestCase.class,
        HerdTestCase.class,
        BuilderTestCase.class,
        CallbackTestCase.class,
        AetherTestCase.class,
        ResolverTestCase.class
})
public class AllCmrTests {
}
