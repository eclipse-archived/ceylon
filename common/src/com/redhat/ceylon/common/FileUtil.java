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
    
    /**
     * The OS-specific directory where global application data can be stored
     */
    public static File getSystemConfigDir() throws IOException {
        File configDir = null;
        String os = System.getProperty("os.name").toLowerCase();
        if (os.startsWith("windows")) {
            String appDir = System.getenv("ALLUSERSPROFILE");
            if (appDir != null) {
                configDir = new File(appDir, "ceylon");
            }
        } else if (os.startsWith("mac")) {
            configDir = new File("/etc/ceylon");
        } else {
            // Assume a "regular" unix OS
            configDir = new File("/etc/ceylon");
        }
        return configDir;
    }
    
}
