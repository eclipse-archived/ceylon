

package ceylon.modules.spi;

import ceylon.modules.Configuration;

/**
 * Simple executable.
 * It takes configuration and starts a new process.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public interface Executable {
    /**
     * Execute process with configuration.
     *
     * @param conf the configuration
     * @throws Exception for any error
     */
    void execute(Configuration conf) throws Exception;
}
