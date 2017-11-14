

package org.jboss.ceylon.test.modules.services.test;

import javax.sound.sampled.AudioFileFormat.Type;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;

import ceylon.paris.f2f.iface.IService;
import ceylon.paris.f2f.impl.ServiceImpl;
import org.jboss.ceylon.test.modules.ModulesTest;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class ServicesTestCase extends ModulesTest {
    @Test
    public void testLoadServices() throws Throwable {
        JavaArchive module = ShrinkWrap.create(JavaArchive.class, "ceylon.paris.f2f.ext-1.0.0.Final.car");
        module.addClasses(ceylon.paris.f2f.ext.$module_.class, ceylon.paris.f2f.ext.run_.class);

        JavaArchive lib1 = ShrinkWrap.create(JavaArchive.class, "ceylon.paris.f2f.iface-1.0.0.Final.car");
        lib1.addClass(ceylon.paris.f2f.iface.$module_.class);
        lib1.addClass(IService.class);

        JavaArchive lib2 = ShrinkWrap.create(JavaArchive.class, "ceylon.paris.f2f.impl-1.0.0.Final.car");
        lib2.addClass(ceylon.paris.f2f.impl.$module_.class);
        lib2.addClass(ServiceImpl.class);
        lib2.addAsServiceProvider(IService.class, ServiceImpl.class);
        lib2.addAsResource(new StringAsset("some.prop=some_value"), "META-INF/services/org/apache/camel/config.properties");

        System.out.println("lib2 = " + lib2.toString(true));

        testArchive(module, lib1, lib2);
    }

    @Test @Ignore("See https://github.com/ceylon/ceylon/issues/4856")
    public void testAudioMixerServices() throws Throwable {
        Mixer mixer = AudioSystem.getMixer(null);
        Mixer.Info[] mixers = AudioSystem.getMixerInfo();
        Type[] fileTypes = AudioSystem.getAudioFileTypes();
        boolean plainHasMixer = mixer != null;
        int plainMixerCount = mixers.length;
        int plainFileTypeCount = fileTypes.length;
        System.out.println("Number of mixers/filetypes using plain Java = " + plainMixerCount + "/" + plainFileTypeCount);
        System.setProperty("ceylon.runtime.test.services.audiotest.hasmixer", String.valueOf(plainHasMixer));
        System.setProperty("ceylon.runtime.test.services.audiotest.mixers", String.valueOf(plainMixerCount));
        System.setProperty("ceylon.runtime.test.services.audiotest.filetypes", String.valueOf(plainFileTypeCount));
        
        JavaArchive module = ShrinkWrap.create(JavaArchive.class, "ceylon.audiotest-1.0.0.car");
        module.addClasses(ceylon.audiotest.$module_.class, ceylon.audiotest.run_.class);

        testArchive(module);
    }
}
