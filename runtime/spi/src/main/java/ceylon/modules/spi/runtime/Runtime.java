

package ceylon.modules.spi.runtime;

import ceylon.modules.Configuration;
import ceylon.modules.spi.Executable;

/**
 * Ceylon Modules runtime spi.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public interface Runtime extends Executable {
    /**
     * Create modular ClassLoader.
     *
     * @param name    the module name
     * @param version the module version
     * @param conf    the runtime configuration
     * @return holder classloader holder instance
     * @throws Exception for ay error
     */
    ClassLoaderHolder createClassLoader(String name, String version, Configuration conf) throws Exception;
}
