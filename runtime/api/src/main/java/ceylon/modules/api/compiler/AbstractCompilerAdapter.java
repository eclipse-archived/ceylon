/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package ceylon.modules.api.compiler;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

/**
 * Abstract compiler adapter.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public abstract class AbstractCompilerAdapter implements CompilerAdapter {
    private String languageSuffix;

    /**
     * Create new cmopiler adapter.
     * The *true* lang suffix must contain a dot.
     *
     * @param languageSuffix the language suffix
     */
    protected AbstractCompilerAdapter(String languageSuffix) {
        if (languageSuffix == null)
            throw new IllegalArgumentException("Null language suffix");
        this.languageSuffix = languageSuffix;
    }

    public String languageSuffix() {
        return languageSuffix;
    }

    public File findSource(File root, String name) {
        File file = new File(root, name + languageSuffix);
        return file.exists() ? file : null;
    }

    /**
     * Get class's path.
     *
     * @param className the class name
     * @return the class' path
     */
    protected String toPath(String className) {
        return toPath(className, languageSuffix);
    }

    /**
     * Get class's path.
     *
     * @param className the class name
     * @param suffix    the suffix
     * @return the class' path
     */
    protected static String toPath(String className, String suffix) {
        return className.replace(".", File.separator) + suffix;
    }

    /**
     * Safe close.
     *
     * @param closeable the closeable
     */
    protected static void safeClose(final Closeable closeable) {
        if (closeable != null) try {
            closeable.close();
        } catch (IOException e) {
            // ignore
        }
    }
}
