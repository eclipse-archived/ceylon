/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package ceylon.modules.jboss.repository;

import org.jboss.modules.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Car archive entry.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
final class CarEntryResource implements Resource {
    private final JarFile jarFile;
    private final JarEntry entry;
    private final URL resourceURL;

    CarEntryResource(final JarFile jarFile, final JarEntry entry, final URL resourceURL) {
        this.jarFile = jarFile;
        this.entry = entry;
        this.resourceURL = resourceURL;
    }

    public String getName() {
        return entry.getName();
    }

    public URL getURL() {
        return resourceURL;
    }

    public InputStream openStream() throws IOException {
        return jarFile.getInputStream(entry);
    }

    public long getSize() {
        final long size = entry.getSize();
        return size == -1 ? 0 : size;
    }
}
