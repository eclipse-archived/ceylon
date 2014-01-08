/*
 * Copyright 2011 Red Hat inc. and third party contributors as noted 
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

package com.redhat.ceylon.test.smoke.test;

import java.lang.reflect.Field;

import com.redhat.ceylon.cmr.api.RepositoryManagerBuilder;
import com.redhat.ceylon.cmr.impl.RepositoryManagerBuilderImpl;
import com.redhat.ceylon.test.smoke.support.RepositoryManagerBuilderTester;
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
            RepositoryManagerBuilder builder = getRepositoryManagerBuilder(false);
            assertBuilder(builder, RepositoryManagerBuilderImpl.class);

            // blank
            System.setProperty("ceylon.module.resolver.builder", "");
            builder = getRepositoryManagerBuilder(false);
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
            RepositoryManagerBuilder builder = getRepositoryManagerBuilder(false);
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
            RepositoryManagerBuilder builder = getRepositoryManagerBuilder(false);
            assertBuilder(builder, RepositoryManagerBuilderImpl.class);
        } finally {
            System.clearProperty("ceylon.module.resolver.builder");
        }
    }

    @Test
    public void testImpl() throws Exception {
        try {
            System.setProperty("ceylon.module.resolver.builder", com.redhat.ceylon.test.smoke.support.RepositoryManagerBuilderTester.class.getName());
            RepositoryManagerBuilder builder = getRepositoryManagerBuilder(false);
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
