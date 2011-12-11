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
import si.alesj.ceylon.module;
import si.alesj.ceylon.test.Touch;

/**
 * On demand tests.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class OnDemandTestCase extends ModulesTest {
    @Test
    public void testBasic() throws Throwable {
        JavaArchive module = ShrinkWrap.create(JavaArchive.class, "net.something.xyz-1.0.0.Final.car");
        module.addClasses(net.something.xyz.module.class, net.something.xyz.run.class);

        JavaArchive lib1 = ShrinkWrap.create(JavaArchive.class, "org.jboss.acme-1.0.0.CR1.car");
        lib1.addClass(org.jboss.acme.module.class);
        lib1.addClass(org.jboss.acme.run.class);

        JavaArchive lib2 = ShrinkWrap.create(JavaArchive.class, "si.alesj.ceylon-1.0.0.GA.car");
        lib2.addClass(module.class);
        lib2.addClass(Touch.class);
        lib2.addAsResource("si/alesj/ceylon/test/config.xml");

        testArchive(module, lib1, lib2);
    }
}
