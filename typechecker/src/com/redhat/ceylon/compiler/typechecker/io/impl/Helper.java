package com.redhat.ceylon.compiler.typechecker.io.impl;

import java.util.zip.ZipEntry;

/**
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
class Helper {
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
}
