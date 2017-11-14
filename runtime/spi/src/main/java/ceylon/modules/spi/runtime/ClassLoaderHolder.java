
package ceylon.modules.spi.runtime;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public interface ClassLoaderHolder {
    ClassLoader getClassLoader();
    String getVersion();
}
