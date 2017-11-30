/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
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
