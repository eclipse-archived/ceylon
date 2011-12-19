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

package org.jboss.ceylon.test.modules.smoke.test;

import org.jboss.acme.module;
import org.jboss.acme.run;
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
        module.addClasses(module.class, run.class);
        testArchive(module);
    }

    @Test
    public void transitiveModule() throws Throwable {
        JavaArchive module = ShrinkWrap.create(JavaArchive.class, "com.foobar.qwert-1.0.0.GA.car");
        module.addClasses(com.foobar.qwert.module.class, com.foobar.qwert.run.class);

        JavaArchive lib = ShrinkWrap.create(JavaArchive.class, "org.jboss.acme-1.0.0.CR1.car");
        lib.addClasses(module.class, run.class);

        testArchive(module, lib);
    }

    @Test
    public void filteredModule() throws Throwable {
        JavaArchive module = ShrinkWrap.create(JavaArchive.class, "eu.cloud.clazz-1.0.0.GA.car");
        module.addClasses(eu.cloud.clazz.module.class, eu.cloud.clazz.run.class);

        JavaArchive lib = ShrinkWrap.create(JavaArchive.class, "org.jboss.filtered-1.0.0.Alpha1.car");
        lib.addClass(org.jboss.filtered.module.class);
        lib.addClass(SomeSPI.class);
        lib.addClass(SomeAPI.class);
        lib.addClass(SomeImpl.class);

        testArchive(module, lib);
    }
}
