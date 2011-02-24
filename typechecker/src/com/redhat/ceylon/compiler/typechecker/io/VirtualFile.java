package com.redhat.ceylon.compiler.typechecker.io;

import java.io.InputStream;
import java.util.List;

/**
 * Represents a file on the abstracted file system.
 * A file can be a folder in which case:
 *  - getInputStream() is unavailable
 *  - getChildren() returns the folder files
 *
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public interface VirtualFile {
    /**
     * Is the file a folder?
     */
    boolean isFolder();

    /**
     * File simple name
     */
    String getName();

    /**
     * Full file path
     */
    //should it be getURI instead?
    String getPath();

    /**
     * InputStream representing the file.
     * Must be closed by the caller.
     * @throws exception when is a folder
     */
    InputStream getInputStream();

    /**
     * Unmodifiable list of folder children
     */
    List<VirtualFile> getChildren();
}
