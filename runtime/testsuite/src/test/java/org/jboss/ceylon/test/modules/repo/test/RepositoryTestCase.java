/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.jboss.ceylon.test.modules.repo.test;

import java.util.ArrayList;
import java.util.List;

import ceylon.modules.spi.Argument;
import ceylon.modules.spi.Constants;
import org.jboss.ceylon.test.modules.ModulesTest;
import org.junit.Test;

/**
 * Test different repository usage.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class RepositoryTestCase extends ModulesTest {
    @Test
    public void testDummy() {
        // fix cars!
    }

    @Test
    public void testMultipleRepositories() throws Throwable {
        List<String> extra = new ArrayList<String>();
        // alternative
        extra.add(Constants.CEYLON_ARGUMENT_PREFIX + Argument.REPOSITORY.toString());
        extra.add(getAlternative().getPath());
        // repo
        extra.add(Constants.CEYLON_ARGUMENT_PREFIX + Argument.REPOSITORY.toString());
        extra.add(getRepo().getPath());

        execute("moduletest/0.1", extra);
    }

}
