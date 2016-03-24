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

package ceylon.modules.api.compiler;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Compiler adapter factory.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class CompilerAdapterFactory {
    private static CompilerAdapterFactory instance = new CompilerAdapterFactory();
    private List<CompilerAdapter> adapters = new CopyOnWriteArrayList<CompilerAdapter>();

    private CompilerAdapterFactory() {
        // add default compilers
        addCompilerAdapter(new CopyCompilerAdapter());
        addCompilerAdapter(JavaCompilerAdapter.INSTANCE);
    }

    /**
     * Get compiler adapter factory instance.
     *
     * @return the cmopiler adapter factory instance
     */
    public static CompilerAdapterFactory getInstance() {
        return instance;
    }

    /**
     * Find the source and compile it.
     *
     * @param sourceRoot  the source root
     * @param name        the resource name
     * @param classesRoot the classes root
     * @return compiled file or null if no compilation happened
     * @throws IOException for any I/O error
     */
    public File findAndCompile(File sourceRoot, String name, File classesRoot) throws IOException {
        File source = null;
        CompilerAdapter adapter = null;
        for (CompilerAdapter ca : adapters) {
            source = ca.findSource(sourceRoot, name);
            if (source != null) {
                adapter = ca;
                break;
            }
        }

        if (adapter == null)
            return null;

        return adapter.compile(source, name, classesRoot);
    }

    /**
     * Compile the source.
     *
     * @param source      the source file
     * @param name        the resource name
     * @param classesRoot the classes root
     * @return compiled file or null if no compilation happened
     * @throws IOException for any I/O error
     */
    public File compile(File source, String name, File classesRoot) throws IOException {
        for (CompilerAdapter ca : adapters) {
            File output = ca.compile(source, name, classesRoot);
            if (output != null)
                return output;
        }

        return null;
    }

    /**
     * Add compiler adapter.
     *
     * @param adapter the compiler adapter
     */
    public void addCompilerAdapter(CompilerAdapter adapter) {
        if (adapter == null)
            throw new IllegalArgumentException("Null adapter");
        adapters.add(0, adapter); // making sure copy is always the last one
    }

    /**
     * Remove compiler adapter.
     *
     * @param adapter the compiler adapter
     */
    public void removeCompilerAdapter(CompilerAdapter adapter) {
        if (adapter == null)
            throw new IllegalArgumentException("Null adapter");
        adapters.remove(adapter);
    }
}
