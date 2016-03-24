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
