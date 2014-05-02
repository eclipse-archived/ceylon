package com.redhat.ceylon.cmr.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.jar.Pack200;
import java.util.jar.Pack200.Packer;
import java.util.jar.Pack200.Unpacker;
import java.util.zip.ZipEntry;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.Logger;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.impl.ShaSigner;
import com.redhat.ceylon.common.FileUtil;

public final class JarUtils {

    /** A simple filter to determine if an entry should be included in an archive. */
    public static interface JarEntryFilter {
        public boolean avoid(String entryFullName);
    }

    public static void finishUpdatingJar(File originalFile, File outputFile, ArtifactContext context, 
            JarOutputStream jarOutputStream, JarEntryFilter filter,
            RepositoryManager repoManager, boolean verbose, Logger log,
            Set<String> folders) throws IOException {
        finishUpdatingJar(originalFile, outputFile, context, jarOutputStream, filter, repoManager, verbose, log, folders, false);
    }
    
    public static void finishUpdatingJar(File originalFile, File outputFile, ArtifactContext context, 
            JarOutputStream jarOutputStream, JarEntryFilter filter,
            RepositoryManager repoManager, boolean verbose, Logger log,
            Set<String> folders, boolean pack200) throws IOException {
        // now copy all previous jar entries
        if (originalFile != null) {
            JarFile jarFile = new JarFile(originalFile);
            Enumeration<JarEntry> entries = jarFile.entries();
            while(entries.hasMoreElements()){
                JarEntry entry = entries.nextElement();
                // skip the old entry if we overwrote it
                if(filter.avoid(entry.getName()))
                    continue;
                // only preserve directories if we did not write to them
                if(entry.isDirectory() && folders.contains(entry.getName()))
                    continue;
                ZipEntry copiedEntry = new ZipEntry(entry.getName());
                // Preserve the modification time and comment
                copiedEntry.setTime(entry.getTime());
                copiedEntry.setComment(entry.getComment());
                jarOutputStream.putNextEntry(copiedEntry);
                if(!entry.isDirectory()){
                    InputStream inputStream = jarFile.getInputStream(entry);
                    copy(inputStream, jarOutputStream);
                    inputStream.close();
                }
                jarOutputStream.closeEntry();
            }
            jarFile.close();
        }
        // now write all the required directories
        for(String folder : folders){
            ZipEntry dir = new ZipEntry(folder);
            jarOutputStream.putNextEntry(dir);
            jarOutputStream.closeEntry();
        }
        jarOutputStream.flush();
        jarOutputStream.close();
        if(verbose){
            log.info("[done writing to jar: "+outputFile.getPath()+"]");
            //Log.printLines(log.noticeWriter, "[done writing to jar: "+outputFile.getPath()+"]");
        }
        
        if (pack200) {
            repack(outputFile, log);
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

    /**
     * Takes the jar generated file and repacks it using pack200 in an attempt 
     * to reduce the file size. This is only worth doing on jars containing class files.
     */
    private static void repack(File outputFile, Logger log) throws IOException,
            FileNotFoundException {
        Packer packer = Pack200.newPacker();
        packer.properties().put(Packer.EFFORT, "9");
        packer.properties().put(Packer.KEEP_FILE_ORDER, Packer.FALSE);
        packer.properties().put(Packer.DEFLATE_HINT, Packer.TRUE);
        packer.properties().put(Packer.SEGMENT_LIMIT, "-1");
        packer.properties().put(Packer.MODIFICATION_TIME, Packer.LATEST);
        File tmp = File.createTempFile("ceylon", "pack200", outputFile.getParentFile());
        try {
            try (OutputStream out = new FileOutputStream(tmp)) {
                try (JarFile in = new JarFile(outputFile)) {
                    packer.pack(in, out);
                }
            }
            
            try (JarOutputStream outStream = new JarOutputStream(new FileOutputStream(outputFile))) {
                outStream.setLevel(9);
                Unpacker unpacker = Pack200.newUnpacker();
                unpacker.unpack(tmp, outStream);
            }
        } finally {
            tmp.delete();
        }
        log.debug("[repacked jar: "+outputFile.getPath()+"]");
    }

    public static String toPlatformIndependentPath(Iterable<? extends File> sourcePaths, String prefixedSourceFile) {
        String sourceFile = FileUtil.relativeFile(sourcePaths, prefixedSourceFile);
        // zips are UNIX-friendly
        sourceFile = sourceFile.replace(File.separatorChar, '/');
        return sourceFile;
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
        } catch (Exception ex) {
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

    public static String getFolder(String fileName) {
        int lastSep = fileName.lastIndexOf('/');
        // toplevel does not need a folder
        if(lastSep == -1)
            return null;
        // include the last slash to create a folder
        return fileName.substring(0, lastSep+1);
    }
}
