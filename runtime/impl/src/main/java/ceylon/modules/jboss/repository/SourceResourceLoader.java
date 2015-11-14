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
import org.jboss.modules.ClassSpec;
import org.jboss.modules.PackageSpec;
import org.jboss.modules.Resource;
import org.jboss.modules.ResourceLoader;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * Source resource loader.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
class SourceResourceLoader implements ResourceLoader {
    private File sourcesRoot;
    private File classesRoot;
    private String rootName;
    private Manifest manifest;

    SourceResourceLoader(File sourcesRoot, File classesRoot, String rootName) {
        if (sourcesRoot == null)
            throw new IllegalArgumentException("Null sources root");
        if (classesRoot == null)
            throw new IllegalArgumentException("Null classes root");
        if (rootName == null)
            throw new IllegalArgumentException("Null root name");

        this.sourcesRoot = sourcesRoot;
        this.classesRoot = classesRoot;
        this.rootName = rootName;

        final File manifestFile = new File(sourcesRoot, "META-INF" + File.separator + "MANIFEST.MF");
        manifest = readManifestFile(manifestFile);
    }

    private static Manifest readManifestFile(final File manifestFile) {
        try {
            InputStream is = new FileInputStream(manifestFile);
            try {
                return new Manifest(is);
            } finally {
                safeClose(is);
            }
        } catch (IOException e) {
            return null;
        }
    }

    public String getRootName() {
        return rootName;
    }

    public ClassSpec getClassSpec(String name) throws IOException {
        File file = new File(classesRoot, name);
        if (file.exists()) {
            return toClassSpec(file);
        } else {
            CompilerAdapterFactory factory = CompilerAdapterFactory.getInstance();
            file = factory.findAndCompile(sourcesRoot, name, classesRoot);
            return file != null ? toClassSpec(file) : null;
        }
    }

    /**
     * To class spec.
     *
     * @param file the file
     * @return new class spec instance
     * @throws IOException for any I/O error
     */
    protected ClassSpec toClassSpec(File file) throws IOException {
        final long size = file.length();
        final ClassSpec spec = new ClassSpec();
        final InputStream is = new FileInputStream(file);
        try {
            if (size <= (long) Integer.MAX_VALUE) {
                final int castSize = (int) size;
                byte[] bytes = new byte[castSize];
                int a = 0, res;
                while ((res = is.read(bytes, a, castSize - a)) > 0) {
                    a += res;
                }
                // done
                is.close();
                spec.setBytes(bytes);
                return spec;
            } else {
                throw new IOException("Resource is too large to be a valid class file");
            }
        } finally {
            safeClose(is);
        }
    }

    private static void safeClose(final Closeable closeable) {
        if (closeable != null) try {
            closeable.close();
        } catch (IOException e) {
            // ignore
        }
    }

    /**
     * Get the file name of a class.
     *
     * @param className  the class name
     * @param typeSuffix the language type suffix
     * @return the name of the corresponding class file
     */
    static String fileNameOfClass(final String className, String typeSuffix) {
        return className.replace('.', '/') + "." + typeSuffix;
    }

    public PackageSpec getPackageSpec(String name) throws IOException {
        final PackageSpec spec = new PackageSpec();
        final Manifest manifest = this.manifest;
        if (manifest == null) {
            return spec;
        }

        final Attributes mainAttribute = manifest.getAttributes(name);
        final Attributes entryAttribute = manifest.getAttributes(name);
        spec.setSpecTitle(getDefinedAttribute(Attributes.Name.SPECIFICATION_TITLE, entryAttribute, mainAttribute));
        spec.setSpecVersion(getDefinedAttribute(Attributes.Name.SPECIFICATION_VERSION, entryAttribute, mainAttribute));
        spec.setSpecVendor(getDefinedAttribute(Attributes.Name.SPECIFICATION_VENDOR, entryAttribute, mainAttribute));
        spec.setImplTitle(getDefinedAttribute(Attributes.Name.IMPLEMENTATION_TITLE, entryAttribute, mainAttribute));
        spec.setImplVersion(getDefinedAttribute(Attributes.Name.IMPLEMENTATION_VERSION, entryAttribute, mainAttribute));
        spec.setImplVendor(getDefinedAttribute(Attributes.Name.IMPLEMENTATION_VENDOR, entryAttribute, mainAttribute));
        if (Boolean.parseBoolean(getDefinedAttribute(Attributes.Name.SEALED, entryAttribute, mainAttribute))) {
            spec.setSealBase(classesRoot.toURI().toURL());
        }
        return spec;
    }

    private static String getDefinedAttribute(Attributes.Name name, Attributes entryAttribute, Attributes mainAttribute) {
        final String value = entryAttribute == null ? null : entryAttribute.getValue(name);
        return value == null ? mainAttribute == null ? null : mainAttribute.getValue(name) : value;
    }

    public Resource getResource(String name) {
        try {
            File file = new File(classesRoot, name);
            if (file.exists()) {
                return new FileEntryResource(name, file, file.toURI().toURL());
            } else {
                file = new File(sourcesRoot, name);
                if (file.exists() == false)
                    return null;

                // TODO -- fix url!
                return new SourceEntryResource(name, file, file.toURI().toURL(), classesRoot);
            }
        } catch (MalformedURLException e) {
            // must be invalid...?  (TODO: check this out)
            return null;
        }
    }

    public String getLibrary(String name) {
        // Source cannot have lib
        return null;
    }

    public Collection<String> getPaths() {
        List<String> paths = new ArrayList<String>();
        buildIndex(paths, sourcesRoot, "");
        return paths;
    }

    private void buildIndex(final List<String> index, final File root, final String pathBase) {
        for (File file : root.listFiles()) {
            if (file.isDirectory()) {
                index.add(pathBase + file.getName());
                buildIndex(index, file, pathBase + file.getName() + File.separator);
            }
        }
    }
}
