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
