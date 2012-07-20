package com.redhat.ceylon.common;

import java.io.File;
import java.io.IOException;

public class FileUtil {
    
    public static File makeTempDir(String prefix){
        try {
            File dir = File.createTempFile(prefix, "");
            if(!dir.delete() || !dir.mkdirs()) {
                throw new RuntimeException("Failed to create tmp dir: "+dir);
            }
            return dir;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void delete(File f){
        if (f.isDirectory()) {
            for (File c : f.listFiles()) {
                delete(c);
            }
        }
        if (!f.delete()) {
            throw new RuntimeException("Failed to delete file: " + f.getPath());
        }
    }
    
}
