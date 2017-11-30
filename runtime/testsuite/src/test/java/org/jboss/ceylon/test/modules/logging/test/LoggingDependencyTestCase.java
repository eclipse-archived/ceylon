/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.jboss.ceylon.test.modules.logging.test;

import net.acme.logging.$module_;
import net.acme.logging.run_;
import org.jboss.ceylon.test.modules.ModulesTest;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;

/**
 * @author <a href="mailto:matejonnet@gmail.com">Matej Lazar</a>
 */
public class LoggingDependencyTestCase extends ModulesTest {

    @Test
    public void testLoggingDependency() throws Throwable {

       // System.setProperty("java.util.logging.manager","org.jboss.logmanager.LogManager");

        JavaArchive module = ShrinkWrap.create(JavaArchive.class, "net.acme.logging-1.0.0.CR1.car");
        module.addClasses($module_.class, run_.class);
        testArchive(module);
    }

}
