package com.redhat.ceylon.compiler.java.test.fordebug;

import java.io.File;
import java.util.List;

public class Path {

    static String path(List<String> paths) {
        StringBuilder result = new StringBuilder();
        for (String path : paths) {
            result.append(path).append(File.pathSeparator);
        }
        if (result.length() > 0) {
            result.setLength(result.length() - File.pathSeparator.length());
        }
        return result.toString();
    }
    
    
}
