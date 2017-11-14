

package org.gae.ceylon;

import org.eclipse.ceylon.compiler.java.metadata.Import;
import org.eclipse.ceylon.compiler.java.metadata.Module;


/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
@Module(
    name = "org.gae.ceylon", version = "1.0.0",
    dependencies = {@Import(name = "com.google.appengine:appengine-api-1.0-sdk", version = "1.9.6")})
public class $module_ {
    public static void run() throws Throwable {
        run_.main(new String[0]);
    }
}
