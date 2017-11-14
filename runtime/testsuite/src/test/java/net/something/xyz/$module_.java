

package net.something.xyz;

import org.eclipse.ceylon.compiler.java.metadata.Import;
import org.eclipse.ceylon.compiler.java.metadata.Module;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
@Module(name = "net.something.xyz",
        version = "1.0.0.Final",
        dependencies = {
                @Import(name = "org.jboss.acme",
                        version = "1.0.0.CR1",
                        optional = true),
                @Import(name = "si.alesj.ceylon",
                        version = "1.0.0.GA",
                        optional = true)
        })
public class $module_ {
}
