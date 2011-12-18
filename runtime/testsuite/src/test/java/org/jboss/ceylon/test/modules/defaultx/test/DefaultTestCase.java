/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.ceylon.test.modules.defaultx.test;

import biz.wiz.quiz.module;
import biz.wiz.quiz.run;
import org.jboss.ceylon.test.modules.ModulesTest;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ZipImporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class DefaultTestCase extends ModulesTest {

    @Test
    public void testVisibility() throws Throwable {
        JavaArchive module = ShrinkWrap.create(JavaArchive.class, "biz.wiz.quiz-1.0.0.GA.car");
        module.addClasses(module.class, run.class);

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
        module.addClasses(org.jboss.acme.run.class);

        testArchive(module, org.jboss.acme.run.class.getName());
    }

}
