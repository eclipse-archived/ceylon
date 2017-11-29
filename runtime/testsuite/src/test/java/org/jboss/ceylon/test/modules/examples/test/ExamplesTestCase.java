/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
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
