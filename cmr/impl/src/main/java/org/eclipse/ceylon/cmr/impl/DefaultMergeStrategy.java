

package org.eclipse.ceylon.cmr.impl;

import org.eclipse.ceylon.cmr.spi.MergeStrategy;
import org.eclipse.ceylon.cmr.spi.OpenNode;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class DefaultMergeStrategy implements MergeStrategy {
    public void conflict(OpenNode previous, OpenNode current) {
        throw new IllegalArgumentException("Cannot merge, dup node: " + previous + " vs. " + current);
    }
}
