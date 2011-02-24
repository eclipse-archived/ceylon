package com.redhat.ceylon.compiler.typechecker.io.impl;

import java.util.zip.ZipEntry;

/**
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
class Helper {
    public static String getSimpleName(ZipEntry entry) {
        final String name = entry.getName();
        if ( name.endsWith("/") ) {
            final int lastIndex = name.length() - 2;
            final int firstIndex = name.lastIndexOf("/", lastIndex);// -1 as last index and -1 to remove /
            return firstIndex == -1 ? name.substring(0, lastIndex + 1) : name.substring(firstIndex + 1, lastIndex + 1);
        }
        else {
            final int firstIndex = name.lastIndexOf("/");
            return firstIndex == -1 ? name : name.substring(firstIndex+1);
        }
    }
}
