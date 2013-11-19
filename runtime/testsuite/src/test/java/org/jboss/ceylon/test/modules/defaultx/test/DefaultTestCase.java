/*
 * Copyright 2011 Red Hat inc. and third party contributors as noted 
 * by the author tags.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.ceylon.test.modules.defaultx.test;

import java.io.File;

import biz.wiz.quiz.module_;
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
        module.addClasses(module_.class, run_.class);

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
        module.addClasses(org.jboss.acme.module_.class);
        module.addClasses(org.jboss.acme.run_.class);

        testArchive(module, "org.jboss.acme.run");
    }

}
