/*
 * Copyright 2012 Red Hat inc. and third party contributors as noted
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

package org.jboss.ceylon.test.modules.interop.test;

import java.util.List;

import ceylon.modules.spi.Argument;
import ceylon.modules.spi.Constants;
import edu.fri.summer.$module_;
import edu.fri.summer.run_;
import org.jboss.ceylon.test.modules.ModulesTest;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;

/**
 * Simple Maven repository is enabled.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class InteropTestCase extends ModulesTest {
    @Test
    public void testJarUsage() throws Throwable {
        JavaArchive module = ShrinkWrap.create(JavaArchive.class, "edu.fri.summer-1.0.0.Beta23.car");
        module.addClasses($module_.class, run_.class);
        // Note: if this test fails, you might be missing JBoss Modules in your Maven repository
        // See: edu.fri.summer.module_.java for more details
        testArchive(module);
    }

    @Override
    protected void execute(String module, List<String> extra) throws Throwable {
        extra.add(Constants.CEYLON_ARGUMENT_PREFIX + Argument.REPOSITORY.toString());
        extra.add("mvn"); // enable Maven repository
        super.execute(module, extra);
    }
}
