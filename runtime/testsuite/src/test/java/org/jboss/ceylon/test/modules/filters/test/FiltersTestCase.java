/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.jboss.ceylon.test.modules.filters.test;

import java.io.File;
import java.util.List;

import ceylon.modules.spi.Argument;
import ceylon.modules.spi.Constants;
import org.gae.ceylon.$module_;
import org.gae.ceylon.run_;
import org.jboss.ceylon.test.modules.ModulesTest;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class FiltersTestCase extends ModulesTest {
    @Override
    protected void execute(String module, List<String> extra) throws Throwable {
        extra.add(Constants.CEYLON_ARGUMENT_PREFIX + Argument.REPOSITORY.toString());
        extra.add("aether"); // enable Aether repository

        extra.add(Constants.CEYLON_ARGUMENT_PREFIX + Argument.OVERRIDES.toString());
        File overrides = new File(getClass().getClassLoader().getResource("filters/overrides.xml").toURI());
        extra.add(overrides.getAbsolutePath()); // add overrides

        super.execute(module, extra);
    }

    @Test
    public void testUsage() throws Throwable {
        JavaArchive module = ShrinkWrap.create(JavaArchive.class, "org.gae.ceylon-1.0.0.car");
        module.addClasses($module_.class);
        module.addClasses(run_.class);

        testArchive(module);
    }
}
