package com.redhat.ceylon.cmr.maven;

import java.io.File;

/**
 * Utility class which does not depend on anything Aether (optional dep), so can be safely public
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class MavenUtils {
    
    public static String getDefaultMavenSettings() {
        String path = System.getProperty("maven.repo.local");
        if (path != null) {
            File file = new File(path, "settings.xml");
            if (file.exists())
                return file.getAbsolutePath();
        }

        path = System.getProperty("user.home");
        if (path != null) {
            File file = new File(path, ".m2/settings.xml");
            if (file.exists())
                return file.getAbsolutePath();
        }

        path = System.getenv("M2_HOME");
        if (path != null) {
            File file = new File(path, "conf/settings.xml");
            if (file.exists())
                return file.getAbsolutePath();
        }

        return "classpath:settings.xml";
    }

}
