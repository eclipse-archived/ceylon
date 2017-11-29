/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.jboss.ceylon.test.modules;

import org.jboss.ceylon.test.modules.defaultx.test.DefaultTestCase;
import org.jboss.ceylon.test.modules.examples.test.ExamplesTestCase;
import org.jboss.ceylon.test.modules.interop.test.AetherInteropTestCase;
import org.jboss.ceylon.test.modules.interop.test.InteropTestCase;
import org.jboss.ceylon.test.modules.logging.test.LoggingDependencyTestCase;
import org.jboss.ceylon.test.modules.ondemand.test.OnDemandTestCase;
import org.jboss.ceylon.test.modules.repo.test.RepositoryTestCase;
import org.jboss.ceylon.test.modules.services.test.ServicesTestCase;
import org.jboss.ceylon.test.modules.smoke.test.SmokeTestCase;
import org.jboss.ceylon.test.modules.sources.test.SourcesTestCase;
import org.jboss.ceylon.test.modules.tool.test.RunToolTestCase;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
    SmokeTestCase.class,
    DefaultTestCase.class,
    ExamplesTestCase.class,
    InteropTestCase.class,
    AetherInteropTestCase.class,
    OnDemandTestCase.class,
    RepositoryTestCase.class,
    SourcesTestCase.class,
    ServicesTestCase.class,
    LoggingDependencyTestCase.class,
    RunToolTestCase.class,
})
public class AllRuntimeTests {
}
