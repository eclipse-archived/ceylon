import java.io.File;
import java.io.IOException;

import com.redhat.ceylon.compiler.js.CeylonRunJsTool;


/** Runs the test() function in each js module that was generated.
 * 
 * @author Enrique Zamudio
 */
public class NodeTest {

    /** Gets a JavaScript file from a directory under the modules dir. */
    private static File getJavaScript(final File subdir) {
        return subdir.getName().equals("default") ? new File(subdir, "default.js") :
            new File(subdir, "0.1" + File.separator + subdir.getName() + "-0.1.js");
    }

    private static void run(final String nodePath, final File subdir) throws IOException, InterruptedException {
        final File root = subdir.getParentFile();
        final File jsf = getJavaScript(subdir);
        if (!jsf.exists())return;
        final String path = jsf.getPath();
        String subpath = path.substring(root.getPath().length()+1);
        System.out.printf("RUNNING %s/%s%n", root.getName(), subdir.getName());
        if (File.separatorChar=='\\') {
            subpath = subpath.replace(File.separatorChar, '/');
        }
        final String eval = String.format("setTimeout(function(){}, 50);require('%s').test();", subpath);
        ProcessBuilder pb = new ProcessBuilder(nodePath, "-e", eval).directory(root.getParentFile());
        pb.environment().put("NODE_PATH", root.getPath());
        Process proc = pb.start();
        new CeylonRunJsTool.ReadStream(proc.getInputStream(), System.out).start();
        new CeylonRunJsTool.ReadErrorStream(proc.getErrorStream(), System.out, true).start();
        int xv = proc.waitFor();
        proc.getInputStream().close();
        if (xv != 0) {
            System.out.printf("ERROR abnormal termination of node: %s%n", xv);
        }
        System.out.println("------------------------------------------------------");
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        if (args.length < 2) {
            System.out.println("You must specify the two directories containing the compiled JS (normal and optimized)");
        }
        File root1 = new File(args[0]);
        File root2 = new File(args[1]);
        if (!(root1.exists() && root1.isDirectory() && root1.canRead())) {
            System.out.printf("%s is not a readable directory%n", root1);
            System.exit(1);
        }
        if (!(root2.exists() && root2.isDirectory() && root2.canRead())) {
            System.out.printf("%s is not a readable directory%n", root2);
            System.exit(1);
        }
        final String nodePath = CeylonRunJsTool.findNode();
        for (File subdir1 : root1.listFiles()) {
            final String modname = subdir1.getName();
            if (subdir1.isDirectory()) { //skip language module
                File subdir2 = new File(root2, modname);
                run(nodePath, subdir1);
                run(nodePath, subdir2);
            }
        }
    }
}
