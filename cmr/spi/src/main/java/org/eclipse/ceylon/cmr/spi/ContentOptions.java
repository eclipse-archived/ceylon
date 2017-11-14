

package org.eclipse.ceylon.cmr.spi;

/**
 * Content options.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public interface ContentOptions {

    /**
     * Default content options.
     */
    static final ContentOptions DEFAULT = new ContentOptions() {
        public boolean forceOperation() {
            return false;
        }

        public boolean forceDescriptorCheck() {
            return false;
        }
    };

    /**
     * Do we force operation.
     *
     * @return true if forced, false otherwise
     */
    boolean forceOperation();

    /**
     * Do we force descriptor check.
     *
     * @return true if forced, false otherwise
     */
    boolean forceDescriptorCheck();
}
