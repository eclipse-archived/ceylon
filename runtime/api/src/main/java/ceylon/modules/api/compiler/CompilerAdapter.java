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
