

package org.eclipse.ceylon.cmr.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.eclipse.ceylon.cmr.api.ArtifactCallback;
import org.eclipse.ceylon.cmr.api.ArtifactCallbackStream;
import org.eclipse.ceylon.cmr.api.ArtifactContext;
import org.eclipse.ceylon.cmr.api.CmrRepository;
import org.eclipse.ceylon.cmr.api.Overrides;
import org.eclipse.ceylon.cmr.spi.ContentStore;
import org.eclipse.ceylon.cmr.spi.Node;
import org.eclipse.ceylon.cmr.spi.OpenNode;
import org.eclipse.ceylon.cmr.spi.SizedInputStream;
import org.eclipse.ceylon.common.FileUtil;
import org.eclipse.ceylon.common.config.Repositories;
import org.eclipse.ceylon.common.log.Logger;
import org.eclipse.ceylon.model.cmr.ArtifactResult;
import org.eclipse.ceylon.model.cmr.RepositoryException;

/**
 * Root node -- main entry point into Ceylon repositories.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class RootRepositoryManager extends AbstractNodeRepositoryManager {
    private final FileContentStore fileContentStore;

    private static File getRootDir() {
        org.eclipse.ceylon.common.config.Repositories.Repository rootRepo = Repositories.get().getCacheRepository();
        return new File(rootRepo.getUrl());
    }

    public RootRepositoryManager(Logger log, Overrides overrides) {
        this(getRootDir(), log, overrides);
    }
    
    public RootRepositoryManager(Logger log, Overrides overrides, boolean upgradeDist) {
        this(getRootDir(), log, overrides, upgradeDist);
    }

    public RootRepositoryManager(File rootDir, Logger log, Overrides overrides) {
        this(rootDir, log, overrides, true);
    }
    public RootRepositoryManager(File rootDir, Logger log, Overrides overrides, boolean upgradeDist) {
        super(log, overrides, upgradeDist);
        if(rootDir != null){
            this.fileContentStore = new FileContentStore(rootDir);
            final CmrRepository aaca = new DefaultRepository(new RootNode(fileContentStore, fileContentStore));
            setCache(aaca);
        }else{
            this.fileContentStore = null;
        }
    }

    

    @Override
    protected ArtifactResult getArtifactResult(ArtifactContext context, Node node) throws RepositoryException {
        if (node.isRemote()) {
            final boolean forceOp = context.isForceOperation();
            try {
                context.setForceOperation(true); // just force the ops
                log.debug("Looking up artifact " + context + " from " + node + " to cache it");
                SizedInputStream sizedInputStream = node.getSizedInputStream();
                // temp fix for https://github.com/ceylon/ceylon-module-resolver/issues/60
                // in theory we should not have nodes with null streams, but at least provide a helpful exception
                if (sizedInputStream == null) {
                    throw new RepositoryException("Node " + node + " for repository " + this + " returned a null stream");
                }
                try {
                    log.debug(" -> Found it, now caching it");
                    final File file = putContent(context, node, sizedInputStream.getInputStream(), sizedInputStream.getSize());
                    log.debug("    Caching done: " + file);
                    String repositoryDisplayString = NodeUtils.getRepositoryDisplayString(node);
                    File originalRepoFile = new File(file.getParentFile(), file.getName().concat(ORIGIN));                        
                    FileWriter writer = new FileWriter(originalRepoFile, false);
                    try {
                        writer.write(repositoryDisplayString);
                        writer.close();
                    } catch(IOException e) {
                        log.error(e.toString());
                    }
                    // we expect the remote nodes to support Ceylon module info
                    return new FileArtifactResult(NodeUtils.getRepository(node), this, context.getName(), context.getVersion(),
                            file, repositoryDisplayString);
                } finally {
                    IOUtils.safeClose(sizedInputStream.getInputStream());
                }
            } catch (IOException e) {
                throw new RepositoryException(e);
            } finally {
                context.setForceOperation(forceOp);
            }
        } else {
            return toArtifactResult(node);
        }
    }

    @Override
    protected ArtifactResult artifactNotFound(ArtifactContext context) throws RepositoryException {
        boolean hasRemote = false;
        StringBuilder reps = new StringBuilder();
        for (CmrRepository rep : getRepositories()) {
            if (rep.getRoot().isRemote() && !isOffline(rep)) {
                hasRemote = true;
                reps.append(rep.getDisplayString());
                reps.append('\n');
            }
        }

        if (hasRemote && cache != null) {
            // Create a .missing file in the cache to mark that we tried to locate the file but it didn't exist 
            Node parent = cache.findParent(context);
            if (parent != null) {
                context.toNode(parent);
                try {
                    // fileContentStore cannot be null if we have a cache
                    File parentDir = fileContentStore.getFile(parent);
                    String[] names = cache.getArtifactNames(context);
                    File missingFile = new File(parentDir, names[0].concat(MISSING));
                    if (!missingFile.exists()) {
                        if (context.getSearchRepository() == cache) {
                            ArtifactContext unpreferred = new ArtifactContext(context.getNamespace(), context.getName(), context.getVersion(), context.getSuffixes());
                            unpreferred.copySettingsFrom(context);
                            return getArtifactResult(unpreferred);
                        } else {
                            FileUtil.mkdirs(parentDir);
                            try (FileWriter writer = new FileWriter(missingFile, false)) {
                                // We write the list of remote repositories we tried
                                // This is not currently used but might be useful in the future
                                writer.write(reps.toString());
                            } catch(IOException e) {
                                log.error(e.toString());
                            }
                        }
                    }
                } finally {
                    ArtifactContext.removeNode(parent);
                }
            }
        }
        
        return super.artifactNotFound(context);
    }
    
    private boolean isOffline(CmrRepository repo) {
        ContentStore cs = repo.getRoot().getService(ContentStore.class);
        return cs != null && cs.isOffline();
    }

    private File putContent(ArtifactContext context, Node node, InputStream stream, long length) throws IOException {
        log.debug("  Creating local copy of external node: " + node + " at repo: " + 
                (fileContentStore != null ? fileContentStore.getDisplayString() : null));
        if(fileContentStore == null)
            throw new IOException("No location to place node at: fileContentStore is null");
        
        ArtifactCallback callback = context.getCallback();
        if (callback == null) {
            callback = ArtifactCallbackStream.getCallback();
        }
        
        final File finalFile;
        VerifiedDownload dl = new VerifiedDownload(log, context, fileContentStore, node);
        try {
            dl.fetch(callback, stream, length);
            // don't commit it yet, as something might go wrong getting the descriptor...
            
            // only check for jars or forced checks
            if (ArtifactContext.JAR.equals(context.getSingleSuffix()) || context.forceDescriptorCheck()) {
                // transfer descriptor as well, if there is one
                final Node descriptor = Configuration.getResolvers(this).descriptor(node);
                if (descriptor != null && descriptor.hasBinaries()) {
                    VerifiedDownload descriptorDl = new VerifiedDownload(log, context, fileContentStore, descriptor);
                    try {
                        descriptorDl.fetch(callback, descriptor.getInputStream(), 40);
                        descriptorDl.commit();
                    } catch (RuntimeException e) {
                        dl.rollback(e);
                        throw e;
                    } catch (IOException e) {
                        dl.rollback(e);
                        throw e;
                    }
                }
            }
            // ... got descriptor OK, so can now commit
            finalFile = dl.commit();
        } catch (RuntimeException e) {
            dl.rollback(e);
            throw e;
        } catch (IOException e) {
            dl.rollback(e);
            throw e;
        }
        
        // refresh markers from root to this newly put node
        if(getCache() != null){
            final List<String> paths = NodeUtils.toLabelPath(node);
            OpenNode current = getCache();
            for (String path : paths) {
                if (current == null) {
                    break;
                }
                current.refresh(false);
                final Node tmp = current.peekChild(path);
                current = (tmp instanceof OpenNode) ? OpenNode.class.cast(tmp) : null;
            }
        }
        return finalFile;
    }

    @Override
    protected void addContent(ArtifactContext context, Node parent, String label, InputStream content) throws IOException {
        Node child;
        if (parent instanceof OpenNode) {
            OpenNode on = (OpenNode) parent;
            child = on.peekChild(label);
            if (child == null) {
                child = on.addNode(label);
            }
        } else {
            child = parent.getChild(label);
        }
        if (child != null) {
            putContent(context, child, content, -1);
        } else {
            throw new IOException("Cannot add child [" + label + "] content [" + content + "] on parent node: " + parent);
        }
    }

    @Override
    protected void removeNode(Node parent, String child) throws IOException {
        final Node node = parent.getChild(child);

        if(fileContentStore != null){
            try {
                if (node != null) {
                    final Node sl = parent.getChild(child + SHA1 + LOCAL);
                    if (sl != null) {
                        fileContentStore.removeFile(sl);
                    }
                    final Node origin = parent.getChild(child + ORIGIN);
                    if (origin != null) {
                        fileContentStore.removeFile(origin);
                    }
                    final Node descriptor = Configuration.getResolvers(this).descriptor(node);
                    if (descriptor != null) {
                        fileContentStore.removeFile(descriptor);
                    }
                }
            } catch (Exception ignored) {
            }
        }

        try {
            super.removeNode(parent, child);
        } finally {
            if (node != null) {
                fileContentStore.removeFile(node);
            }
        }
    }

    @Override
    public String toString() {
        return "RootRepositoryManager: " + fileContentStore;
    }
}
