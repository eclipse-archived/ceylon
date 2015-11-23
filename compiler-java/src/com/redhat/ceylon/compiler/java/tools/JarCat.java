package com.redhat.ceylon.compiler.java.tools;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

import com.redhat.ceylon.cmr.util.JarUtils;
import com.redhat.ceylon.cmr.util.JarUtils.JarEntryFilter;

/**
 * Concatenate Jars. First-in wins policy for duplicates. 
 * Does not treat MANIFEST as special, so it's up to the caller to add it 
 * first if that's where the need it to be.
 */
public class JarCat implements Closeable {

    private final HashSet<String> entries;
    private final JarOutputStream jout;
    
    public JarCat(File out) throws IOException {
        jout = new JarOutputStream(new FileOutputStream(out));
        entries = new HashSet<>();
    }
    
    public void cat(File file) throws IOException {
        cat(file, null);
    }
    
    public void cat(File file, JarEntryFilter filter) throws IOException {
        if (file != null) {
            try (JarFile j = new JarFile(file)) {
                Enumeration<JarEntry> inEntries = j.entries();
                while (inEntries.hasMoreElements()) {
                    JarEntry entry = inEntries.nextElement();
                    String name = entry.getName();
                    if (filter != null && filter.avoid(name)) {
                        continue;
                    }
                    if (!entries.contains(name)) {// first in wins
                        makeParentDirs(name);
                        ZipEntry ze = new ZipEntry(name);
                        jout.putNextEntry(ze);
                        entries.add(name);
                        if (!entry.isDirectory()) {
                            try (InputStream in = j.getInputStream(entry)) {
                                JarUtils.copy(in, jout);
                            }
                        }
                        jout.closeEntry();
                    }
                }
            }
        }
    }

    private void makeParentDirs(String name) throws IOException {
        int index = name.lastIndexOf('/', name.endsWith("/") ? name.length()-2 : name.length()-1);
        if (index != -1) {
            String pname = name.substring(0, index+1);
            if (!entries.contains(pname)) {
                ZipEntry ze = new ZipEntry(pname);
                jout.putNextEntry(ze);
                entries.add(pname);
                jout.closeEntry();
            }
        }
    }

    @Override
    public void close() throws IOException {
        jout.close();
    }
    
}
