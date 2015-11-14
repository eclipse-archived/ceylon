/*
 * Copyright 2011 Red Hat inc. and third party contributors as noted 
 * by the author tags.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.redhat.ceylon.cmr.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.WeakHashMap;

import com.redhat.ceylon.cmr.spi.ContentTransformer;

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
