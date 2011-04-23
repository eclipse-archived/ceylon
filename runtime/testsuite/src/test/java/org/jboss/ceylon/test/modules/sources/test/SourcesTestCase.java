/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
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

package org.jboss.ceylon.test.modules.sources.test;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.jboss.ceylon.test.modules.ModulesTest;

import ceylon.modules.spi.Constants;
import org.junit.Test;

/**
 * Test module usage from sources.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class SourcesTestCase extends ModulesTest
{
   protected String getTestsuiteDir() throws Exception
   {
      URL url = getClass().getProtectionDomain().getCodeSource().getLocation();
      File dir = new File(url.toURI());
      String path = dir.getCanonicalPath();
      int p = path.lastIndexOf("testsuite/");
      return path.substring(0, p + "testsuite/".length());
   }

   @Test
   public void testSimpleUsage() throws Exception
   {
      Map<Constants, String> extra = new HashMap<Constants, String>();
      extra.put(Constants.SOURCES, "src/test/java");
      extra.put(Constants.CLASSES, "target/test-classes-bak");

      src("org.jboss.acme", getTestsuiteDir(), extra);
   }

   @Test
   public void testDependencyUsage() throws Exception
   {
      Map<Constants, String> extra = new HashMap<Constants, String>();
      extra.put(Constants.SOURCES, "src/test/java");
      extra.put(Constants.CLASSES, "target/test-classes-bak");

      src("com.foobar.qwert", getTestsuiteDir(), extra);
   }
}
