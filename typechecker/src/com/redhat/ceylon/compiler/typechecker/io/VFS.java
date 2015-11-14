package com.redhat.ceylon.compiler.typechecker.io;

import com.redhat.ceylon.compiler.typechecker.io.impl.FileSystemVirtualFile;
import com.redhat.ceylon.compiler.typechecker.io.impl.ZipFileVirtualFile;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipFile;

/**
 * Bootstrap API providing a VirtualFile object from "real" files
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class VFS {

    public VirtualFile getFromZipFile(ZipFile file) {
        return new ZipFileVirtualFile(file);
    }

    public ClosableVirtualFile getFromZipFile(File file) {
        try {
            return new ZipFileVirtualFile(file);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public VirtualFile getFromFile(File file) {
        return new FileSystemVirtualFile(file);
    }

    //experimental
    public ClosableVirtualFile openAsContainer(VirtualFile file) {
        if (file instanceof ZipFileVirtualFile) {
            return (ZipFileVirtualFile) file;
        }
        else if (file instanceof FileSystemVirtualFile) {
            final File realFile = ((FileSystemVirtualFile) file).getFile();
            return getFromZipFile( realFile );
        }
        throw new RuntimeException( "Unknown type: " + file.getClass() );
    }
}
