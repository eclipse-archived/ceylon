/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package com.redhat.ceylon.cmr.impl;

import com.redhat.ceylon.cmr.spi.ContentTransformer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.WeakHashMap;

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
     *
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
