/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.jboss.ceylon.test.modules.defaultx.test;

import java.io.File;

import biz.wiz.quiz.$module_;
import biz.wiz.quiz.run_;
import org.jboss.ceylon.test.modules.ModulesTest;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ZipImporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class DefaultTestCase extends ModulesTest {

    @Test
    public void testVisibility() throws Throwable {
        JavaArchive module = ShrinkWrap.create(JavaArchive.class, "biz.wiz.quiz-1.0.0.GA.car");
        module.addClasses($module_.class, run_.class);

        File defaultFile = new File(getRepo(), "default/default.car");
        Assert.assertTrue(defaultFile.exists());

        JavaArchive lib = ShrinkWrap.create(JavaArchive.class, "default.car");
        ZipImporter zipImporter = lib.as(ZipImporter.class);
        zipImporter.importFrom(defaultFile);

        testArchive(module, lib);
    }

    @Test
    public void testDefaultRun() throws Throwable {
        JavaArchive module = ShrinkWrap.create(JavaArchive.class, "default.car");
        module.addClasses(org.jboss.acme.$module_.class);
        module.addClasses(org.jboss.acme.run_.class);

        testArchive(module, "org.jboss.acme.run");
    }

}
