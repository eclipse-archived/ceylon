package com.redhat.ceylon.cmr.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.Logger;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.api.SourceArchiveCreator;
import com.redhat.ceylon.cmr.util.JarUtils;

/** Creates a .src archive in the specified location, containing the
 * specified source files, avoiding duplicates.
 * 
 * @author Enrique Zamudio
 */
public class SourceArchiveCreatorImpl implements SourceArchiveCreator {

    private final ArtifactContext srcContext;
    private final RepositoryManager repoManager;
    private File originalSrcFile;
    private File outputSrcFile;
    private JarOutputStream srcOutputStream;
    private boolean verbose;
    private Logger log;
    private final Iterable<? extends File> sourcePaths;

    public SourceArchiveCreatorImpl(RepositoryManager repoManager, Iterable<? extends File> sourcePaths, String moduleName, String moduleVersion, boolean verbose, Logger log) throws IOException {
        this.repoManager = repoManager;
        this.verbose = verbose;
        this.log = log;
        this.sourcePaths = sourcePaths;
        this.srcContext = new ArtifactContext(moduleName, moduleVersion, ArtifactContext.SRC);
        setupSrcOutput();
    }

    private void setupSrcOutput() throws IOException {
        File targetSrcFile = repoManager.getArtifact(srcContext);
        outputSrcFile = File.createTempFile("src", ".tmp");
        originalSrcFile = targetSrcFile;
        srcOutputStream = new JarOutputStream(new FileOutputStream(outputSrcFile));
    }

    public Set<String> copySourceFiles(Set<String> sources) throws IOException {
        final Set<String> copiedFiles = new HashSet<String>();
        for (String prefixedSourceFile : sources) {
            // must remove the prefix first
            String sourceFile = JarUtils.toPlatformIndependentPath(sourcePaths, prefixedSourceFile);
            if (!copiedFiles.contains(sourceFile)) {
                srcOutputStream.putNextEntry(new ZipEntry(sourceFile));
                try {
                    InputStream inputStream = new FileInputStream(prefixedSourceFile);
                    try {
                        JarUtils.copy(inputStream, srcOutputStream);
                    } finally {
                        inputStream.close();
                    }
                } finally {
                    srcOutputStream.closeEntry();
                }
                copiedFiles.add(sourceFile);
            }
        }
        JarUtils.finishUpdatingJar(originalSrcFile, outputSrcFile, srcContext, srcOutputStream, new JarUtils.JarEntryFilter() {
            @Override
            public boolean avoid(String entryFullName) {
                return copiedFiles.contains(entryFullName);
            }
        }, repoManager, verbose, log);
        return copiedFiles;
    }

    public Iterable<? extends File> getSourcePaths() {
        return sourcePaths;
    }

}
