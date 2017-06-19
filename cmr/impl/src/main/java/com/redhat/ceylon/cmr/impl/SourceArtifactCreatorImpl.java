package com.redhat.ceylon.cmr.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.api.SourceArtifactCreator;
import com.redhat.ceylon.cmr.api.SourceStream;
import com.redhat.ceylon.cmr.util.JarUtils;
import com.redhat.ceylon.common.FileUtil;
import com.redhat.ceylon.common.log.Logger;

/** Creates a .src archive in the specified location, containing the
 * specified source files, avoiding duplicates.
 * 
 * @author Enrique Zamudio
 */
public class SourceArtifactCreatorImpl implements SourceArtifactCreator {

    private final ArtifactContext srcContext;
    private final RepositoryManager repoManager;
    private File originalSrcFile;
    private boolean verbose;
    private Logger log;
    private final Iterable<? extends File> sourcePaths;

    public SourceArtifactCreatorImpl(RepositoryManager repoManager, Iterable<? extends File> sourcePaths, String moduleName, String moduleVersion, boolean verbose, Logger log) throws IOException {
        this.repoManager = repoManager;
        this.verbose = verbose;
        this.log = log;
        this.sourcePaths = sourcePaths;
        this.srcContext = new ArtifactContext(null, moduleName, moduleVersion, ArtifactContext.SRC);
        this.originalSrcFile = repoManager.getArtifact(srcContext);
    }

//    private void setupSrcOutput() throws IOException {
//    }

    
    /** Copy the specified source streams, avoiding duplicate entries. */
    public Set<String> copyStreams(Collection<SourceStream> sourceStreams) throws IOException {
        final Set<String> copiedFiles = new HashSet<String>();
        File outputSrcFile = File.createTempFile("ceylon-", ".src");
        try (JarOutputStream srcOutputStream = new JarOutputStream(new FileOutputStream(outputSrcFile))) {
            final Set<SourceStream> uniqueSources = new HashSet<SourceStream>(sourceStreams);
            final Set<String> folders = new HashSet<String>();
            for (SourceStream sourceStream : uniqueSources) {
                // must remove the prefix first
                String sourceFile = sourceStream.getSourceRelativePath();
                if (!copiedFiles.contains(sourceFile)) {
                    srcOutputStream.putNextEntry(new ZipEntry(sourceFile));
                    try {
                        InputStream inputStream = sourceStream.getInputStream();
                        try {
                            JarUtils.copy(inputStream, srcOutputStream);
                        } finally {
                            inputStream.close();
                        }
                    } finally {
                        srcOutputStream.closeEntry();
                    }
                    copiedFiles.add(sourceFile);
                    String folder = JarUtils.getFolder(sourceFile);
                    if(folder != null)
                        folders.add(folder);
                }
            }
            JarUtils.finishUpdatingJar(originalSrcFile, outputSrcFile, srcContext, srcOutputStream, new JarUtils.JarEntryFilter() {
                @Override
                public boolean avoid(String entryFullName) {
                    return copiedFiles.contains(entryFullName);
                }
            }, repoManager, verbose, log, folders);
        } finally {
            FileUtil.deleteQuietly(outputSrcFile);
        }
        return copiedFiles;
        
    }

    public Set<String> copy(Collection<String> sources) throws IOException {
        List<SourceStream> sourceStreams = new ArrayList<>(sources.size());
        for (final String prefixedSourceFile : sources) {
            // must remove the prefix first
            sourceStreams.add(new SourceStream() {
                @Override
                public String getSourceRelativePath() {
                    return JarUtils.toPlatformIndependentPath(sourcePaths, prefixedSourceFile);
                }
                
                @Override
                public InputStream getInputStream() throws IOException {
                    return new FileInputStream(prefixedSourceFile);
                }
                
                @Override
                public boolean equals(Object obj) {
                    return prefixedSourceFile.equals(obj);
                }
                
                @Override
                public int hashCode() {
                    return prefixedSourceFile.hashCode();
                }
            });
        }
        return copyStreams(sourceStreams);
    }

    public Iterable<? extends File> getPaths() {
        return sourcePaths;
    }

}
