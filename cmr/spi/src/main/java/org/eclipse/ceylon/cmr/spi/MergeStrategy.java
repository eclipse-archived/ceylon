

package org.eclipse.ceylon.cmr.spi;

/**
 * Merge strategy.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public interface MergeStrategy {

    void conflict(OpenNode previous, OpenNode current);

}
