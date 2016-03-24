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

import ceylon.modules.api.compiler.CompilerAdapterFactory;
import org.jboss.modules.Resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Source entry resource.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
class SourceEntryResource implements Resource {
    private String name;
    private File sourceFile;
    private File classFile;
    private URL url;
    private File classesRoot;

    public SourceEntryResource(String name, File sourceFile, URL url, File classesRoot) {
        this.name = name;
        this.sourceFile = sourceFile;
        this.url = url;
        this.classesRoot = classesRoot;
    }

    /**
     * Get class file; can be plain resource as well.
     *
     * @return the class file
     * @throws IOException for any I/O error
     */
    protected synchronized File getClassFile() throws IOException {
        if (classFile == null) {
            CompilerAdapterFactory factory = CompilerAdapterFactory.getInstance();
            classFile = factory.compile(sourceFile, name, classesRoot);
        }
        return classFile;
    }

    public String getName() {
        return name;
    }

    public URL getURL() {
        return url;
    }

    public InputStream openStream() throws IOException {
        return new FileInputStream(getClassFile());
    }

    public long getSize() {
        try {
            return getClassFile().length();
        } catch (IOException ignored) {
            return 0;
        }
    }
}
