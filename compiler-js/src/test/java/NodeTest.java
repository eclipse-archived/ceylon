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

    private static int run(final String nodePath, final File subdir) throws IOException, InterruptedException {
        final File root = subdir.getParentFile();
        final File jsf = getJavaScript(subdir);
        if (!jsf.exists())return 0;
        final String path = jsf.getPath();
        String subpath = path.substring(root.getPath().length()+1);
        System.out.printf("RUNNING %s/%s%n", root.getName(), subdir.getName());
        if (File.separatorChar=='\\') {
            subpath = subpath.replace(File.separatorChar, '/');
        }
        final String eval = String.format("setTimeout(function(){}, 50);require('%s').test();", subpath);
        ProcessBuilder pb = new ProcessBuilder(nodePath, "-e", eval).directory(root.getParentFile());
        pb.environment().put("NODE_PATH", new File(root.getParentFile().getParentFile(),"runtime").getPath()+":"+root.getPath());
        Process proc = pb.start();
        new CeylonRunJsTool.ReadStream(proc.getInputStream(), System.out).start();
        new CeylonRunJsTool.ReadErrorStream(proc.getErrorStream(), System.out, true).start();
        int xv = proc.waitFor();
        proc.getInputStream().close();
        if (xv != 0 && xv != 1) {
            System.out.printf("ERROR abnormal termination of node: %s%n", xv);
        }
        System.out.println("------------------------------------------------------");
        return xv;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        if (args.length < 1) {
            System.out.println("You must specify the directory containing the compiled JS");
        }
        File root = new File(args[0]);
        if (!(root.exists() && root.isDirectory() && root.canRead())) {
            System.out.printf("%s is not a readable directory%n", root);
            System.exit(1);
        }
        final String nodePath = CeylonRunJsTool.findNode();
        boolean errs = false;
        for (File subdir : root.listFiles()) {
            if (subdir.isDirectory()) { //skip language module
                errs |= run(nodePath, subdir) != 0;
            }
        }
        if (errs) {
            System.exit(-1);
        }
    }
}
