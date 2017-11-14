

package org.eclipse.ceylon.cmr.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.WeakHashMap;

import org.eclipse.ceylon.cmr.spi.ContentTransformer;

/**
 * Caching content transformer.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class CachingContentTransformer implements ContentTransformer {

    private final Map<Class, Object> cache = new WeakHashMap<Class, Object>();

    @SuppressWarnings({"unchecked"})
    public synchronized <T> T transform(Class<T> contentType, InputStream stream) throws IOException {
        T cached = (T) cache.get(contentType);
        if (cached == null) {
            cached = IOUtils.fromStream(contentType, stream);
            cache.put(contentType, cached);
        }
        return cached;
    }

    /**
     * Clear cache.
     * <p/>
     * If contentType parameter is set, only that content is removed,
     * otherwise whole cache is cleared.
     *
     * @param contentType the content type
     */
    public void clear(Class<?> contentType) {
        if (contentType != null)
            cache.remove(contentType);
        else
            cache.clear();
    }
}
