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

/**
 * Compiler adapter.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public interface CompilerAdapter {
    /**
     * Get the language suffix.
     *
     * @return the language suffix.
     */
    String languageSuffix();

    /**
     * Find soource.
     *
     * @param root the source root
     * @param name the resource name
     * @return found source or null if not found
     */
    File findSource(File root, String name);

    /**
     * Compile source.
     *
     * @param source      the source
     * @param name        the resource name
     * @param classesRoot the classes root
     * @return compiled file
     * @throws IOException for any I/O error
     */
    File compile(File source, String name, File classesRoot) throws IOException;
}
