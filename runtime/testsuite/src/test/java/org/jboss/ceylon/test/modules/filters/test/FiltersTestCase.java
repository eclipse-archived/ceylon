/*
 * Copyright 2014 Red Hat inc. and third party contributors as noted
 * by the author tags.
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

package org.jboss.ceylon.test.modules.filters.test;

import java.io.File;
import java.util.List;

import ceylon.modules.spi.Argument;
import ceylon.modules.spi.Constants;
import org.gae.ceylon.module_;
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

        extra.add(Constants.CEYLON_ARGUMENT_PREFIX + Argument.MAVEN_OVERRIDES.toString());
        File overrides = new File(getClass().getClassLoader().getResource("filters/overrides.xml").toURI());
        extra.add(overrides.getAbsolutePath()); // add overrides

        super.execute(module, extra);
    }

    @Test
    public void testUsage() throws Throwable {
        JavaArchive module = ShrinkWrap.create(JavaArchive.class, "org.gae.ceylon-1.0.0.car");
        module.addClasses(module_.class);
        module.addClasses(run_.class);

        testArchive(module);
    }
}
