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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Source entry resource.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
class FileEntryResource implements Resource {
    private final String name;
    private final File file;
    private final URL url;

    FileEntryResource(final String name, final File file, final URL url) {
        this.name = name;
        this.file = file;
        this.url = url;
    }

    public long getSize() {
        return file.length();
    }

    public String getName() {
        return name;
    }

    public URL getURL() {
        return url;
    }

    public InputStream openStream() throws IOException {
        return new FileInputStream(file);
    }
}
