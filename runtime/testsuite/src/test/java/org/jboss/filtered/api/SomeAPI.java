

package org.jboss.filtered.api;

import org.jboss.filtered.spi.SomeSPI;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public abstract class SomeAPI implements SomeSPI {
    protected abstract String onward();
}
