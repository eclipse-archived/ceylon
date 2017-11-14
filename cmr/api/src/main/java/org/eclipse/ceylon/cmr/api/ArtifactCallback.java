

package org.eclipse.ceylon.cmr.api;

import java.io.File;

/**
 * Artifact callback.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public interface ArtifactCallback {
    /**
     * 
     * The callback is invoked before streaming of a node.
     * it gives the estimated size of the artifact, or -1 if it cannot be determined.
     * e.g. to be able to display % of downloaded artifact.
     *
     * @param nodeFullPath the full path of the node
     * @param size the size
     * @param contentStore the content store this artifact will be downloaded from
     */
    void start(String nodeFullPath, long size, String contentStore);

    /**
     * Invoked for every byte stream read.
     *
     * @param bytes  the bytes read
     * @param length the bytes read length
     */
    void read(byte[] bytes, int length);

    /**
     * Invoked once streaming is successfully finished.
     *
     * @param localFile the downloaded file
     */
    void done(File localFile);

    /**
     * For any error during streaming.
     *
     * @param err the cause
     * @param localFile the downloaded file (even if not finished)
     */
    void error(File localFile, Throwable err);
}
