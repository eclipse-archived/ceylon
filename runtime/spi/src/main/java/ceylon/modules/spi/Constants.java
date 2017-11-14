

package ceylon.modules.spi;


/**
 * Ceylon constants.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public enum Constants {
    IMPL_ARGUMENT_PREFIX("+"),
    CEYLON_ARGUMENT_PREFIX("-"),
    DEFAULT("default"),
    DEFAULT_VERSION("main"),
    ARGUMENTS("args"),
    SOURCES("sources"),
    CLASSES("classes"),
    MODULE_PATH("-mp"),
    CEYLON_RUNTIME_MODULE("ceylon.runtime"),
    MERGE_STRATEGY("org.eclipse.ceylon.cmr.spi.MergeStrategy"),
    CONTENT_TRANSFORMER("org.eclipse.ceylon.cmr.spi.ContentTransformer");

    private String value;

    Constants(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
