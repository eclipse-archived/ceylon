package net.acme.logging;

import org.eclipse.ceylon.compiler.java.metadata.Import;
import org.eclipse.ceylon.compiler.java.metadata.Module;

/**
 * @author <a href="mailto:matejonnet@gmail.com">Matej Lazar</a>
 */
@Module(
    name = "net.acme.logging",
    version = "1.0.0.CR1",
    dependencies = {
        @Import(name = "java.logging", version = "7"),
    }
)
public class $module_ {
}
