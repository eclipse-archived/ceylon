/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.ceylon.test.modules.repo.test;

import ceylon.modules.spi.Argument;
import ceylon.modules.spi.Constants;
import org.jboss.ceylon.test.modules.ModulesTest;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Test different repository usage.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class RepositoryTestCase extends ModulesTest {

    @Test
    public void testMultipleRepositories() throws Throwable {
        List<String> extra = new ArrayList<String>();
        // alternative
        extra.add(Constants.CEYLON_ARGUMENT_PREFIX + Argument.REPOSITORY.toString());
        extra.add(getAlternative().getPath());
        // repo
        extra.add(Constants.CEYLON_ARGUMENT_PREFIX + Argument.REPOSITORY.toString());
        extra.add(getRepo().getPath());
        // runnable
        extra.add(Constants.CEYLON_ARGUMENT_PREFIX + Argument.RUN.toString());
        extra.add("moduletest.helloworld");

        execute("moduletest/0.1", extra);
    }

}
