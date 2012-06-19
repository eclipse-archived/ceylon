import java.io.File;
import java.io.IOException;

import com.redhat.ceylon.compiler.js.Runner;


/** Runs the test() function in each js module that was generated.
 * 
 * @author Enrique Zamudio
 */
public class NodeTest {

    public static void main(String[] args) throws IOException, InterruptedException {
        File root = new File(args[0]);
        if (!(root.exists() && root.isDirectory() && root.canRead())) {
            System.out.printf("%s is not a readable directory%n", root);
            System.exit(1);
        }
        for (File subdir : root.listFiles()) {
            if (subdir.isDirectory() && !(subdir.getName().equals("ceylon"))) {
                File jsf = subdir.getName().equals("default") ? new File(subdir, "default.js") :
                    new File(subdir, "0.1/" + subdir.getName() + "-0.1.js");
                System.out.printf("RUNNING %s%n", jsf.getName());
                String nodePath = Runner.findNode(); //if not found, prints error and exits
                String path = jsf.getPath();
                String eval = String.format("setTimeout(function(){}, 50);require('%s').test();",
                        path.substring(path.indexOf(args[0])));
                Process proc = new ProcessBuilder(nodePath, "-e", eval).directory(root.getParentFile()).start();
                new Runner.ReadStream(proc.getInputStream(), System.out).start();
                new Runner.ReadStream(proc.getErrorStream(), System.err).start();
                int xv = proc.waitFor();
                proc.getInputStream().close();
                proc.getErrorStream().close();
                if (xv != 0) {
                    System.out.printf("ERROR abnormal termination of node: %s%n", xv);
                }
                System.out.println("------------------------------------------------------");
            }
        }
    }
}
