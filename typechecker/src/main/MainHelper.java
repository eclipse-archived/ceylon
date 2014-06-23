package main;
import java.io.File;
import java.util.Arrays;

import com.redhat.ceylon.compiler.typechecker.io.ClosableVirtualFile;
import com.redhat.ceylon.compiler.typechecker.io.VFS;

/**
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class MainHelper {

    public static final ClosableVirtualFile getLatestZippedLanguageSourceFile() {
        VFS vfs = new VFS();
        File langDir = new File(System.getProperty("user.home"), ".ceylon/repo/ceylon/language");
        if (!langDir.exists()) {
            System.err.println("Unable to test language module, not found in repository: " + langDir);
            System.exit(-1);
        }
        String[] versions = langDir.list();
        Arrays.sort(versions);
        String version = versions[versions.length-1]; //last
        return vfs.getFromZipFile( new File(langDir, version + "/ceylon.language-" + version + ".src") );
    }
}
