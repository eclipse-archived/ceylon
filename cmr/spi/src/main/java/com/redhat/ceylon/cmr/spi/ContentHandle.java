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

package com.redhat.ceylon.cmr.spi;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Content handle.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public interface ContentHandle {
    /**
     * Do we have binaries behind this contant.
     * e.g. it can be a folder, where we know how to get File, but no real content
     *
     * @return true if there is real content, false otherwise
     */
    boolean hasBinaries();

    /**
     * Get node content as stream.
     * Return null if there is no binaries.
     *
     * @return the node's stream
     * @throws IOException for any I/O error
     */
    InputStream getBinariesAsStream() throws IOException;

    /**
     * Get node content as file.
     *
     * @return the node's stream
     * @throws IOException for any I/O error
     */
    File getContentAsFile() throws IOException;

    /**
     * Get last modified timestamp.
     * If last modified is undefined, return -1.
     *
     * @return the last modified, or -1 if undefined
     * @throws IOException for any I/O error
     */
    long getLastModified() throws IOException;

    /**
     * Get size.
     * If size cannot be determined, return -1.
     *
     * @return the last modified, or -1 if undefined
     * @throws IOException for any I/O error
     */
    long getSize() throws IOException;

    /**
     * Cleanup content.
     */
    void clean();
}
