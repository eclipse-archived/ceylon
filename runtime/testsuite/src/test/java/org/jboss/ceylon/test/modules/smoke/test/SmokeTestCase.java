/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.jboss.ceylon.test.modules.smoke.test;

import org.jboss.acme.$module_;
import org.jboss.acme.run_;
import org.jboss.ceylon.test.modules.ModulesTest;
import org.jboss.filtered.api.SomeAPI;
import org.jboss.filtered.impl.SomeImpl;
import org.jboss.filtered.spi.SomeSPI;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class SmokeTestCase extends ModulesTest {
    @Test
    public void singleModule() throws Throwable {
        JavaArchive module = ShrinkWrap.create(JavaArchive.class, "org.jboss.acme-1.0.0.CR1.car");
        module.addClasses($module_.class, run_.class);
        testArchive(module);
    }

    @Test
    public void swingModule() throws Throwable {
        JavaArchive module = ShrinkWrap.create(JavaArchive.class, "org.jboss.swing-1.0.0.CR1.car");
        module.addClasses(org.jboss.swing.$module_.class, org.jboss.swing.run_.class);
        testArchive(module);
    }

    @Test
    public void filteredAndCycleModule() throws Throwable {
        JavaArchive module = ShrinkWrap.create(JavaArchive.class, "eu.cloud.clazz-1.0.0.GA.car");
        module.addClasses(eu.cloud.clazz.$module_.class, eu.cloud.clazz.run_.class);

        JavaArchive lib = ShrinkWrap.create(JavaArchive.class, "org.jboss.filtered-1.0.0.Alpha1.car");
        lib.addClass(org.jboss.filtered.$module_.class);
        lib.addClass(SomeSPI.class);
        lib.addClass(SomeAPI.class);
        lib.addClass(SomeImpl.class);

        testArchive(module, lib);
    }

    @Test
    public void transitiveModule() throws Throwable {
        JavaArchive module = ShrinkWrap.create(JavaArchive.class, "cz.brno.as8-8.0.0.Alpha1.car");
        module.addClasses(cz.brno.as8.$module_.class, cz.brno.as8.run_.class);

        JavaArchive lib1 = ShrinkWrap.create(JavaArchive.class, "com.foobar.qwert-1.0.0.GA.car");
        lib1.addClasses(com.foobar.qwert.$module_.class, com.foobar.qwert.run_.class);

        JavaArchive lib2 = ShrinkWrap.create(JavaArchive.class, "org.jboss.acme-1.0.0.CR1.car");
        lib2.addClasses($module_.class, run_.class);

        JavaArchive lib3 = ShrinkWrap.create(JavaArchive.class, "eu.cloud.clazz-1.0.0.GA.car");
        lib3.addClasses(eu.cloud.clazz.$module_.class, eu.cloud.clazz.run_.class);

        JavaArchive lib4 = ShrinkWrap.create(JavaArchive.class, "org.jboss.filtered-1.0.0.Alpha1.car");
        lib4.addClass(org.jboss.filtered.$module_.class);
        lib4.addClass(SomeSPI.class);
        lib4.addClass(SomeAPI.class);
        lib4.addClass(SomeImpl.class);

        testArchive(module, lib1, lib2, lib3, lib4);
    }
}
