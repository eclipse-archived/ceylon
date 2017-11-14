

package com.foobar.qwert;

import org.eclipse.ceylon.compiler.java.metadata.Import;
import org.eclipse.ceylon.compiler.java.metadata.Module;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
@Module(name = "com.foobar.qwert",
        version = "1.0.0.GA",
        dependencies = {
                @Import(name = "org.jboss.acme", version = "1.0.0.CR1", export = true),
                @Import(name = "eu.cloud.clazz", version = "1.0.0.GA")
        })
public class $module_ {
}
