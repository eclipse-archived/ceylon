/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.jboss.ceylon.test.modules.interop.test;

import java.util.Collections;
import java.util.List;

import ceylon.modules.spi.Argument;
import ceylon.modules.spi.Constants;
import io.xov.yalp.$module_;
import io.xov.yalp.run_;
import org.jboss.ceylon.test.modules.ModulesTest;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;

/**
 * Aether enabled tests.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class AetherInteropTestCase extends ModulesTest {
    // Note: for this test to work, you need JBoss VFS and its dependencies in your Maven repository
    // See: io.xov.yalp.module_.java for more details

    @Test
    public void testMultiJarUsage() throws Throwable {
        JavaArchive module = ShrinkWrap.create(JavaArchive.class, "io.xov.yalp-11.0.2.Final.car");
        module.addClasses($module_.class, run_.class);
        testArchive(module);
    }

    @Test
    public void testCamel() throws Throwable {
        // TODO -- exclude it until it's updated in resources/
        // car("camel/1.0.0", Collections.<String, String>emptyMap());
    }

    @Override
    protected void execute(String module, List<String> extra) throws Throwable {
        extra.add(Constants.CEYLON_ARGUMENT_PREFIX + Argument.REPOSITORY.toString());
        extra.add("aether"); // enable Aether repository
        super.execute(module, extra);
    }
}
