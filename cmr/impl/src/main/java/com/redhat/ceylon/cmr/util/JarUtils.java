package com.redhat.ceylon.cmr.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.Logger;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.impl.ShaSigner;

public final class JarUtils {

    /** A simple filter to determine if an entry should be included in an archive. */
    public static interface JarEntryFilter {
        public boolean avoid(String entryFullName);
    }

    public static void finishUpdatingJar(File originalFile, File outputFile, ArtifactContext context, 
            JarOutputStream jarOutputStream, JarEntryFilter filter,
            RepositoryManager repoManager, boolean verbose, Logger log) throws IOException {
        // now copy all previous jar entries
        if (originalFile != null) {
            JarFile jarFile = new JarFile(originalFile);
            Enumeration<JarEntry> entries = jarFile.entries();
            while(entries.hasMoreElements()){
                JarEntry entry = entries.nextElement();
                // skip the old entry if we overwrote it
                if(filter.avoid(entry.getName()))
                    continue;
                ZipEntry copiedEntry = new ZipEntry(entry.getName());
                // Preserve the modification time and comment
                copiedEntry.setTime(entry.getTime());
                copiedEntry.setComment(entry.getComment());
                jarOutputStream.putNextEntry(copiedEntry);
                InputStream inputStream = jarFile.getInputStream(entry);
                copy(inputStream, jarOutputStream);
                inputStream.close();
                jarOutputStream.closeEntry();
            }
            jarFile.close();
        }
        jarOutputStream.flush();
        jarOutputStream.close();
        if(verbose){
            log.info("[done writing to jar: "+outputFile.getPath()+"]");
            //Log.printLines(log.noticeWriter, "[done writing to jar: "+outputFile.getPath()+"]");
        }
        File sha1File = ShaSigner.sign(outputFile, log, verbose);
        try {
            context.setForceOperation(true);
            repoManager.putArtifact(context, outputFile);
            ArtifactContext sha1Context = context.getSha1Context();
            sha1Context.setForceOperation(true);
            repoManager.putArtifact(sha1Context, sha1File);
        } catch(RuntimeException x) {
            log.error("Failed to write module to repository: "+x.getMessage());
            // fatal errors go all the way up but don't print anything if we logged an error
            throw x;
        } finally {
            // now cleanup
            outputFile.delete();
            sha1File.delete();
        }
    }

    public static String toPlatformIndependentPath(Iterable<? extends File> sourcePaths, String prefixedSourceFile) {
        String sourceFile = getSourceFilePath(sourcePaths, prefixedSourceFile);
        // zips are UNIX-friendly
        sourceFile = sourceFile.replace(File.separatorChar, '/');
        return sourceFile;
    }

    private static String getSourceFilePath(Iterable<? extends File> sourcePaths, String file){
        // find the matching source prefix
        int srcDirLength = 0;
        for (File prefixFile : sourcePaths) {
            String prefix = prefixFile.getPath();
            if (file.startsWith(prefix) && prefix.length() > srcDirLength) {
                srcDirLength = prefix.length();
            }
            String absPrefix = prefixFile.getAbsolutePath();
            if (file.startsWith(absPrefix) && absPrefix.length() > srcDirLength) {
                srcDirLength = absPrefix.length();
            }
        }
        
        String path = file.substring(srcDirLength);
        if(path.startsWith(File.separator))
            path = path.substring(1);
        return path;
    }

    public static void copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[4096];
        int read;
        while((read = inputStream.read(buffer)) != -1){
            outputStream.write(buffer, 0, read);
        }
        outputStream.flush();
    }
    
    public static long oldestFileTime(File file) {
        long mtime = Long.MAX_VALUE;
        JarFile jarFile = null;
        try {
            jarFile = new JarFile(file);
            Enumeration<JarEntry> entries = jarFile.entries();
            while(entries.hasMoreElements()){
                JarEntry entry = entries.nextElement();
                if (entry.getTime() < mtime) {
                    mtime = entry.getTime();
                }
            }
        } catch (IOException ex) {
            mtime = Long.MIN_VALUE;
        } finally {
            if (jarFile != null) {
                try {
                    jarFile.close();
                } catch (IOException e) {
                    // Ignore
                }
            }
        }
        return mtime;
    }

}
