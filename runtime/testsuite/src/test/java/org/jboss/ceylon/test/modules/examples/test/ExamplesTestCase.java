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

package org.jboss.ceylon.test.modules.examples.test;

import java.util.Collections;

import org.jboss.ceylon.test.modules.ModulesTest;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class ExamplesTestCase extends ModulesTest {
    @Test
    public void testDummy() {
        // fix cars!
    }

    @Test
    public void testHello() throws Throwable {
        car("hello/1.0.0", Collections.<String, String>emptyMap());
    }

    @Test
    @Ignore("@Steph -- weird Range ctor usage")
    public void testIssue29() throws Throwable {
        car("test.ceylon.dbc/0.5", Collections.<String, String>emptyMap());
    }

    @Test
    @Ignore // TODO -- provide a client-1.0.0.car which depends on hello-1.0.0 module_
    public void testClient() throws Throwable {
        car("client/1.0.0", Collections.<String, String>emptyMap());
    }

    @Test
    @Ignore // TODO
    public void testDefault() throws Throwable {
        car("default", Collections.<String, String>emptyMap());
    }

}
