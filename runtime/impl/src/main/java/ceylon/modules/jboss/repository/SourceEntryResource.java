/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
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
