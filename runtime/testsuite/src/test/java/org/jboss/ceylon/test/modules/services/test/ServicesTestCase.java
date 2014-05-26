/*
 * Copyright 2014 Red Hat inc. and third party contributors as noted
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

package org.jboss.ceylon.test.modules.services.test;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;

import ceylon.paris.f2f.iface.IService;
import ceylon.paris.f2f.impl.ServiceImpl;
import org.jboss.ceylon.test.modules.ModulesTest;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class ServicesTestCase extends ModulesTest {
    @Test
    public void testLoadServices() throws Throwable {
        JavaArchive module = ShrinkWrap.create(JavaArchive.class, "ceylon.paris.f2f.ext-1.0.0.Final.car");
        module.addClasses(ceylon.paris.f2f.ext.module_.class, ceylon.paris.f2f.ext.run_.class);

        JavaArchive lib1 = ShrinkWrap.create(JavaArchive.class, "ceylon.paris.f2f.iface-1.0.0.Final.car");
        lib1.addClass(ceylon.paris.f2f.iface.module_.class);
        lib1.addClass(IService.class);

        JavaArchive lib2 = ShrinkWrap.create(JavaArchive.class, "ceylon.paris.f2f.impl-1.0.0.Final.car");
        lib2.addClass(ceylon.paris.f2f.impl.module_.class);
        lib2.addClass(ServiceImpl.class);
        lib2.addAsServiceProvider(IService.class, ServiceImpl.class);
        lib2.addAsResource(new StringAsset("some.prop=some_value"), "META-INF/services/org/apache/camel/config.properties");

        System.out.println("lib2 = " + lib2.toString(true));

        testArchive(module, lib1, lib2);
    }

    @Test
    public void testAudioMixerServices() throws Throwable {
        Mixer.Info[] mixers = AudioSystem.getMixerInfo();
        int plainCount = mixers.length;
        System.out.println("Number of mixers using plain Java = " + plainCount);
        System.setProperty("ceylon.runtime.test.services.audiotest", String.valueOf(plainCount));
        
        JavaArchive module = ShrinkWrap.create(JavaArchive.class, "ceylon.audiotest-1.0.0.car");
        module.addClasses(ceylon.audiotest.module_.class, ceylon.audiotest.run_.class);

        testArchive(module);
    }
}
