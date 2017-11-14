

package ceylon.paris.f2f.ext;

import org.eclipse.ceylon.compiler.java.metadata.Import;
import org.eclipse.ceylon.compiler.java.metadata.Module;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
@Module(name = "ceylon.paris.f2f.ext", version = "1.0.0.Final",
    dependencies = {
        @Import(name = "ceylon.paris.f2f.iface", version = "1.0.0.Final"),
        @Import(name = "ceylon.paris.f2f.impl", version = "1.0.0.Final")
    }
)
public class $module_ {
}
