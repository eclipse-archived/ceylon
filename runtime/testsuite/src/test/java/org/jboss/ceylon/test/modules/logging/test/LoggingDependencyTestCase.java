package org.jboss.ceylon.test.modules.logging.test;

import net.acme.logging.module_;
import net.acme.logging.run_;
import org.jboss.ceylon.test.modules.ModulesTest;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;

/**
 * @author <a href="mailto:matejonnet@gmail.com">Matej Lazar</a>
 */
public class LoggingDependencyTestCase extends ModulesTest {

    @Test
    public void testLoggingDependency() throws Throwable {

       // System.setProperty("java.util.logging.manager","org.jboss.logmanager.LogManager");

        JavaArchive module = ShrinkWrap.create(JavaArchive.class, "net.acme.logging-1.0.0.CR1.car");
        module.addClasses(module_.class, run_.class);
        testArchive(module);
    }

}
