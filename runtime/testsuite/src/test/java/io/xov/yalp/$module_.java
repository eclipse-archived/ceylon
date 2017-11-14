

package io.xov.yalp;

import org.eclipse.ceylon.compiler.java.metadata.Import;
import org.eclipse.ceylon.compiler.java.metadata.Module;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
@Module(name = "io.xov.yalp",
        version = "11.0.2.Final",
        dependencies = {
                @Import(name = "org.jboss:jboss-vfs", version = "3.1.0.Final"),
                @Import(name = "java.tls", version = "7")
        })
public class $module_ {
}
