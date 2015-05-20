package com.redhat.ceylon.compiler.typechecker.io.impl;

import com.redhat.ceylon.compiler.typechecker.io.VirtualFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class FileSystemVirtualFile implements VirtualFile {
    private final File file;
    

    public FileSystemVirtualFile(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    @Override
    public boolean isFolder() {
        return file.isDirectory();
    }

    @Override
    public String getName() {
        return file.getName();
    }

    @Override
    public String getPath() {
        if ('\\' == File.separatorChar){
            return file.getPath().replace('\\', '/');
        }
        return file.getPath();
    }

    @Override
    public InputStream getInputStream() {
        try {
            return new FileInputStream( file );
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<VirtualFile> getChildren() {
        List<VirtualFile> files;
        final File[] fsFiles = file.listFiles();
        final List<VirtualFile> localFiles;
        if (fsFiles == null) {
            localFiles = new ArrayList<VirtualFile>(0);
        }
        else {
            localFiles = new ArrayList<VirtualFile>(fsFiles.length);
            for (File f : fsFiles) {
                localFiles.add( new FileSystemVirtualFile(f) );
            }
        }
        files = Collections.unmodifiableList(localFiles);
    
        return files;
    }

    @Override
    public int hashCode() {
        return getPath().hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof VirtualFile) {
            return ((VirtualFile) obj).getPath().equals(getPath());
        }
        else {
            return super.equals(obj);
        }
    }

    @Override
    public int compareTo(VirtualFile o) {
        return getPath().compareTo(o.getPath());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("FileSystemVirtualFile");
        sb.append("{name='").append( file.getName() ).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
