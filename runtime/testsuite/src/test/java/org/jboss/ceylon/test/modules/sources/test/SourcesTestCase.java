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

package org.jboss.ceylon.test.modules.sources.test;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import ceylon.modules.spi.Argument;
import ceylon.modules.spi.Constants;
import org.jboss.ceylon.test.modules.ModulesTest;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Test module_ usage from sources.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class SourcesTestCase extends ModulesTest {
    protected String getTestsuiteDir() throws Throwable {
        URL url = getClass().getProtectionDomain().getCodeSource().getLocation();
        File dir = new File(url.toURI());
        String path = dir.getCanonicalPath();
        int p = path.lastIndexOf("testsuite/");
        return path.substring(0, p + "testsuite/".length());
    }

    @Test
    public void testDummy() {
        // ignore atm
    }

    @Test
    @Ignore // TODO -- enable sources repo
    public void testSimpleUsage() throws Throwable {
        Map<String, String> extra = new HashMap<String, String>();
        extra.put(Constants.CEYLON_ARGUMENT_PREFIX + Argument.SOURCE.toString(), "src/test/java");

        src("org.jboss.acme", getTestsuiteDir(), extra);
    }

    @Test
    @Ignore // TODO -- enable sources repo
    public void testDependencyUsage() throws Throwable {
        Map<String, String> extra = new HashMap<String, String>();
        extra.put(Constants.CEYLON_ARGUMENT_PREFIX + Argument.SOURCE.toString(), "src/test/java");

        src("com.foobar.qwert", getTestsuiteDir(), extra);
    }
}
