package com.redhat.ceylon.compiler.typechecker.io.impl;

import java.io.File;
import java.util.zip.ZipEntry;

import com.redhat.ceylon.compiler.typechecker.io.VirtualFile;

/**
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class Helper {
    public static String getSimpleName(ZipEntry entry) {
        return getSimpleName(entry.getName());
    }

    public static String getSimpleName(String entryName) {
        if ( entryName.endsWith("/") ) {
            final int lastIndex = entryName.length() - 2;
            final int firstIndex = entryName.lastIndexOf("/", lastIndex);// -1 as last index and -1 to remove /
            return firstIndex == -1 ? entryName.substring(0, lastIndex + 1) : entryName.substring(firstIndex + 1, lastIndex + 1);
        }
        else {
            final int firstIndex = entryName.lastIndexOf("/");
            return firstIndex == -1 ? entryName : entryName.substring(firstIndex+1);
        }
    }

    public static File getHomeRepository() {
        File repo;
        String ceylonUserRepo = System.getProperty("ceylon.user.repo");
        if (ceylonUserRepo == null) {
            File home = new File( System.getProperty("user.home") );
            File ceylon = new File( home, ".ceylon" );
            repo = new File( ceylon, "repo" );
        } else {
            repo = new File( ceylonUserRepo );
        }
        repo.mkdirs();
        return repo;
    }

    public static String computeRelativePath(VirtualFile unitFile, VirtualFile srcDir) {
        final String rawRelativePath = unitFile.getPath().substring( srcDir.getPath().length() );
        if ( rawRelativePath.startsWith("/") ) {
            return rawRelativePath.substring(1);
        }
        else if ( rawRelativePath.startsWith("!/") ) {
            return rawRelativePath.substring(2);
        }
        else {
            return rawRelativePath;
        }
    }
}
