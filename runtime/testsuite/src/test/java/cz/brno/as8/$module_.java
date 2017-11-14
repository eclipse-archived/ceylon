

package cz.brno.as8;

import org.eclipse.ceylon.compiler.java.metadata.Import;
import org.eclipse.ceylon.compiler.java.metadata.Module;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
@Module(name = "cz.brno.as8",
        version = "8.0.0.Alpha1",
        dependencies = {
                @Import(name = "com.foobar.qwert", version = "1.0.0.GA")
        })
public class $module_ {
}
