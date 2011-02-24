package com.redhat.ceylon.compiler.typechecker.io.impl;

import com.redhat.ceylon.compiler.typechecker.io.VirtualFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;

/**
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
class ZipFolderVirtualFile implements VirtualFile {

    private final String name;
    private final String path;
    private final String entryName;
    private final List<VirtualFile> children = new ArrayList<VirtualFile>();

    public ZipFolderVirtualFile(ZipEntry entry, String rootPath) {
        this.name = Helper.getSimpleName(entry);
        this.entryName = entry.getName();
        String tempPath = rootPath + "!/" + entry.getName();
        this.path = tempPath.endsWith("/") ? tempPath.substring(0, tempPath.length() - 1 ) : tempPath;
    }

    @Override
    public boolean isFolder() {
        return true;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public InputStream getInputStream() {
        throw new IllegalStateException("Cannot call getInputStream() on a directory: " + getPath() );
    }

    @Override
    public List<VirtualFile> getChildren() {
        return Collections.unmodifiableList( children );
    }

    public void addChild(VirtualFile file) {
        children.add(file);
    }

    public String getEntryName() {
        return entryName;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ZipFolderVirtualFile");
        sb.append("{name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
