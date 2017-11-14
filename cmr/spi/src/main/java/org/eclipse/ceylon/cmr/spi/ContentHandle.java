

package org.eclipse.ceylon.cmr.spi;

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
     * @deprecated see getBinariesAsSizedStream
     */
    InputStream getBinariesAsStream() throws IOException;

    /**
     * Get node content as sized stream.
     * Return null if there is no binaries.
     *
     * @return the node's sized stream
     * @throws IOException for any I/O error
     */
    SizedInputStream getBinariesAsSizedStream() throws IOException;

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
