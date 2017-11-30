/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
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
