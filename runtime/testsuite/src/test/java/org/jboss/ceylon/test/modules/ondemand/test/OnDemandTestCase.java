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

package org.jboss.ceylon.test.modules.ondemand.test;

import net.something.xyz.run_;
import org.jboss.acme.Qwert;
import org.jboss.ceylon.test.modules.ModulesTest;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import si.alesj.ceylon.$module_;
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
        module.addClasses(net.something.xyz.$module_.class, run_.class, Qwert.class);

        JavaArchive lib1 = ShrinkWrap.create(JavaArchive.class, "org.jboss.acme-1.0.0.CR1.car");
        lib1.addClass(org.jboss.acme.$module_.class);
        lib1.addClass(org.jboss.acme.run_.class);

        JavaArchive lib2 = ShrinkWrap.create(JavaArchive.class, "si.alesj.ceylon-1.0.0.GA.car");
        lib2.addClass($module_.class);
        lib2.addClass(Touch.class);
        lib2.addAsResource(new StringAsset("<config/>"), "si/alesj/ceylon/test/config.xml");

        testArchive(module, lib1, lib2);
    }
}
