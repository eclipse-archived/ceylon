

package org.jogl.ceylon;

import org.eclipse.ceylon.compiler.java.metadata.Import;
import org.eclipse.ceylon.compiler.java.metadata.Module;


/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
@Module(
    name = "org.jogl.ceylon", version = "1.0.0",
    dependencies = {@Import(name = "org.jogamp.jogl:jogl-all", version = "2.1.2")})
public class $module_ {
    public static void run() throws Throwable {
        run_.main(new String[0]);
    }
}
