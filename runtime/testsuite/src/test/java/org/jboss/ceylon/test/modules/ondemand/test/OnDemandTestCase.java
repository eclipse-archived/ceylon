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

package org.jboss.ceylon.test.modules.ondemand.test;

import org.jboss.ceylon.test.modules.ModulesTest;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

import org.junit.Test;

/**
 * On demand tests.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class OnDemandTestCase extends ModulesTest
{
   @Test
   public void testBasic() throws Exception
   {
      JavaArchive module = ShrinkWrap.create(JavaArchive.class, "net.something.xyz-1.0.0.Final.car");
      module.addClass(net.something.xyz.Module.class);

      JavaArchive lib1 = ShrinkWrap.create(JavaArchive.class, "org.jboss.acme-1.0.0.CR1.car");
      lib1.addClass(org.jboss.acme.Module.class);

      JavaArchive lib2 = ShrinkWrap.create(JavaArchive.class, "si.alesj.ceylon-1.0.0.GA.car");
      lib2.addClass(si.alesj.ceylon.Module.class);
      lib2.addResource("si/alesj/ceylon/test/config.xml");

      testArchive(module, lib1, lib2);
   }
}
