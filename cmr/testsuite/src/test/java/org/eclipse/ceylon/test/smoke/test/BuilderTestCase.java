/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.test.smoke.test;

import java.lang.reflect.Field;

import org.eclipse.ceylon.cmr.api.RepositoryManagerBuilder;
import org.eclipse.ceylon.cmr.impl.RepositoryManagerBuilderImpl;
import org.eclipse.ceylon.test.smoke.support.RepositoryManagerBuilderTester;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Akberc
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class BuilderTestCase extends AbstractTest {
    @Test
    public void testDefault() throws Exception {
        try {
            // property not set
            RepositoryManagerBuilder builder = getRepositoryManagerBuilder(false, 60000, java.net.Proxy.NO_PROXY);
            assertBuilder(builder, RepositoryManagerBuilderImpl.class);

            // blank
            System.setProperty("ceylon.module.resolver.builder", "");
            builder = getRepositoryManagerBuilder(false, 60000, java.net.Proxy.NO_PROXY);
            assertBuilder(builder, RepositoryManagerBuilderImpl.class);
        } finally {
            System.clearProperty("ceylon.module.resolver.builder");
        }
    }

    @Test(expected = Exception.class)
    public void testBogusFqn() throws Exception {
        try {
            // invalid name
            System.setProperty("ceylon.module.resolver.builder", "some.fake.name");
            RepositoryManagerBuilder builder = getRepositoryManagerBuilder(false, 60000, java.net.Proxy.NO_PROXY);
            assertBuilder(builder, RepositoryManagerBuilderImpl.class);
        } finally {
            System.clearProperty("ceylon.module.resolver.builder");
        }
    }

    @Test(expected = Exception.class)
    public void testNotAnInstance() throws Exception {
        try {
            // invalid name
            System.setProperty("ceylon.module.resolver.builder", BuilderTestCase.class.getName());
            RepositoryManagerBuilder builder = getRepositoryManagerBuilder(false, 60000, java.net.Proxy.NO_PROXY);
            assertBuilder(builder, RepositoryManagerBuilderImpl.class);
        } finally {
            System.clearProperty("ceylon.module.resolver.builder");
        }
    }

    @Test
    public void testImpl() throws Exception {
        try {
            System.setProperty("ceylon.module.resolver.builder", org.eclipse.ceylon.test.smoke.support.RepositoryManagerBuilderTester.class.getName());
            RepositoryManagerBuilder builder = getRepositoryManagerBuilder(false, 60000, java.net.Proxy.NO_PROXY);
            assertBuilder(builder, RepositoryManagerBuilderTester.class);
        } finally {
            System.clearProperty("ceylon.module.resolver.builder");
        }
    }

    private void assertBuilder(RepositoryManagerBuilder builder, Class<? extends RepositoryManagerBuilder> expectedType) throws Exception {
        RepositoryManagerBuilder delegate = getDelegate(builder);
        Assert.assertTrue(expectedType.isInstance(delegate));
    }

    private RepositoryManagerBuilder getDelegate(RepositoryManagerBuilder builder) throws Exception {
        Assert.assertNotNull(builder);
        Field f = builder.getClass().getDeclaredField("delegate");
        f.setAccessible(true);
        return (RepositoryManagerBuilder) f.get(builder);
    }
}
