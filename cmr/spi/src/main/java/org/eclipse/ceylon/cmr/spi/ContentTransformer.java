

package org.eclipse.ceylon.cmr.spi;

import java.io.IOException;
import java.io.InputStream;

/**
 * Transform stream into content instance.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public interface ContentTransformer {
    <T> T transform(Class<T> contentType, InputStream stream) throws IOException;
}
