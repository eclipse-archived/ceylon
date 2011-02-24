package com.redhat.ceylon.compiler.typechecker.io;

import com.redhat.ceylon.compiler.typechecker.io.impl.FileSystemVirtualFile;
import com.redhat.ceylon.compiler.typechecker.io.impl.ZipFileVirtualFile;

import java.io.File;
import java.util.zip.ZipFile;

/**
 * Bootstrap API providing a VirtualFile object from "real" files
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class VFS {

    public VirtualFile getFromZipFile(ZipFile file) {
        return new ZipFileVirtualFile(file);
    }

    public VirtualFile getFromFile(File file) {
        return new FileSystemVirtualFile(file);
    }
}
