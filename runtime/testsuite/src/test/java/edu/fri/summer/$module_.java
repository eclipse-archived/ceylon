

package edu.fri.summer;

import org.eclipse.ceylon.compiler.java.metadata.Import;
import org.eclipse.ceylon.compiler.java.metadata.Module;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
@Module(name = "edu.fri.summer",
        version = "1.0.0.Beta23",
        dependencies = {
                @Import(name = "org.jboss.modules:jboss-modules",
                        version = "1.4.4.Final")
        })
public class $module_ {
}
