

package eu.cloud.clazz;

import org.eclipse.ceylon.compiler.java.metadata.Import;
import org.eclipse.ceylon.compiler.java.metadata.Module;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
@Module(name = "eu.cloud.clazz",
        version = "1.0.0.GA",
        dependencies = {
                @Import(name = "org.jboss.filtered", version = "1.0.0.Alpha1"),
                @Import(name = "ceylon.io", version = "0.5")
        })
public class $module_ {
}
