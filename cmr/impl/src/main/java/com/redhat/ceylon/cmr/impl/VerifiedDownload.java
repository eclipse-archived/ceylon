package com.redhat.ceylon.cmr.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.redhat.ceylon.cmr.api.ArtifactCallback;
import com.redhat.ceylon.cmr.api.ArtifactCallbackStream;
import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.spi.Node;
import com.redhat.ceylon.cmr.spi.OpenNode;
import com.redhat.ceylon.common.FileUtil;
import com.redhat.ceylon.common.log.Logger;

/**
 * A transactional approach to downloading files which uses temporary files.
 * 
 * This should be sufficient to protect against dodgy downloads when a 
 * single process is accessing the filesystem, but won't be robust 
 * against multiple concurrent processess. 
 */
class VerifiedDownload {
    protected final Logger log;
    protected final ArtifactContext context;
    protected final Node parent;
    protected final Node node;
    protected final FileContentStore fileContentStore;
    protected final Node tempNode;
    protected final File tempFile;
    
    /** 
     * Prepare for the download 
     */
    public VerifiedDownload(Logger log, ArtifactContext context, FileContentStore fileContentStore, Node node) {
        this.log = log;
        this.context = context;
        // Make a temporary node+file
        parent = NodeUtils.firstParent(node);
        if (parent == null) {
            throw new IllegalArgumentException("Parent should not be null: " + node);
        }
        this.node = node;
        this.fileContentStore = fileContentStore;
        tempNode = parent.getChild(node.getLabel() + AbstractNodeRepositoryManager.VALIDATING);
        tempFile = fileContentStore.getFile(tempNode);
        FileUtil.delete(tempFile);
    }
    
    /** 
     * Perform the download to a temporary file and verify it
     * @throws IOException 
     */
    public void fetch(ArtifactCallback callback, InputStream stream, long length) throws IOException {
        log.debug("  FETCH: saving " + node + " to " + tempFile);
        final File file;
        try {
            if (callback != null) {
                callback.start(NodeUtils.getFullPath(node), length != -1 ? length : node.getSize(), node.getStoreDisplayString());
                stream = new ArtifactCallbackStream(callback, stream);
            }
            log.debug("  Saving content of " + node + " to " + fileContentStore.getFile(tempNode));
            fileContentStore.putContent(tempNode, stream, context); // stream should be closed closer to API call
            file = fileContentStore.getFile(tempNode); // re-get
            assert(file.getPath().equals(tempFile.getPath()));
            if (callback != null) {
                callback.done(file);
            }
        } catch (Throwable t) {
            if (callback != null) {
                callback.error(fileContentStore.getFile(node), t);
            }
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            } else {
                throw IOUtils.toIOException(t);
            }
        }
        
        if (context.isIgnoreSHA() == false && node instanceof OpenNode) {
            verify((OpenNode) node);
        } else {
            log.debug("  Not validating checksum: "+tempNode);
        }
    }
    
    /**
     * Verify the download by comparing the remote sha1 with a 
     * locally computed sha1
     * @throws IOException
     */
    protected void verify(final OpenNode on) throws IOException {
        log.debug("  VERIFY: " + tempFile);
        // Now validate the temporary file has a sha1 which matches the remote sha1
        final String computedSha1 = IOUtils.sha1(new FileInputStream(tempFile));
        if (computedSha1 != null) {
            log.debug("    Computed sha1(" + tempFile + "): " + computedSha1);
            ByteArrayInputStream shaStream = new ByteArrayInputStream(computedSha1.getBytes("ASCII"));
            Node shaNode = parent.getChild(on.getLabel() + AbstractNodeRepositoryManager.SHA1);
            if (shaNode == null) {
                log.debug("    Remote sha1 for (" + on + ") does not exist ");
                // put it to ext node as well, if supported
                on.addContent(AbstractNodeRepositoryManager.SHA1, shaStream, context);
                shaStream.reset(); // reset, for next read
            } else if (shaNode.hasBinaries()) {
                final String retrievedSha1 = IOUtils.readSha1(shaNode.getInputStream());
                if (retrievedSha1.length() != 40
                        || !retrievedSha1.matches("[a-z0-9]+")) {
                    throw new IOException("Remote SHA1 for " + on + " was corrupt: " + retrievedSha1);
                }
                log.debug("    Retrieved " + shaNode +": "+ retrievedSha1);
                if (computedSha1.equals(retrievedSha1)) {
                    log.debug("    Yay, sha1's of " + tempFile +" match");
                } else {
                    throw new IOException("Remote SHA1 for "+ on + " differs from computed SHA1: " + retrievedSha1 + " != " + computedSha1);
                }
            } else {
                log.warning("    Remote sha1 for (" + on + ") exists, but lacks content!");
            }
            // create empty marker node
            OpenNode sl = ((OpenNode) parent).addNode(on.getLabel() + AbstractNodeRepositoryManager.SHA1 + AbstractNodeRepositoryManager.LOCAL);
            // put sha to local store as well
            fileContentStore.putContent(sl, shaStream, context);
        } else {
            log.debug("  Could not calculate sha1 of : "+tempFile);
        }
    }
    
    /** Rollback the download (delete the temporary file) */
    public void rollback(Throwable t) {
        log.debug("  ROLLBACK: deleting " + tempFile + " due to " + t);
        try {
            fileContentStore.delete(tempFile, node);
        } catch (Exception e) {
            t.addSuppressed(e);
            //log.warning("Error while removing new content: " + tempFile);
        }
    }
    
    /** 
     * Commit the download by renaming the temporary file to the correct 
     * final filename. 
     */
    public File commit() throws IOException {
        File finalFile = fileContentStore.getFile(node);
        assert(!finalFile.getPath().equals(tempFile.getPath()));
        
        // delete the final file if it already exists (since this could 
        // prevent the rename)
        FileUtil.delete(finalFile);
        
        log.debug("  COMMIT: renaming " + tempFile + " to " + finalFile);
        if (!tempFile.renameTo(finalFile)) {
            throw new IOException("Renaming "+ tempFile + " to " + finalFile + " failed");
        }
        return finalFile;
    }
}
