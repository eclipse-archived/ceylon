import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;


/** Runs the test() function in each js module that was generated.
 * 
 * @author Enrique Zamudio
 */
public class NodeTest {

    /** Simple filter for .js files */
    private static class JsExtFilter implements FilenameFilter {
        @Override
        public boolean accept(File arg0, String name) {
            return name.endsWith(".js");
        }
    }

    /** A thread dedicated to reading from a stream and storing the result to return it as a String. */
    private static class ReadStream extends Thread {
        private final InputStream stream;
        StringBuilder sb = new StringBuilder();
        private final byte[] buf;
        private ReadStream(InputStream stream, byte[] buffer) {
            this.stream = stream;
            buf = buffer;
        }
        public void run() {
            try {
                int count = stream.read(buf);
                while (count > 0) {
                    sb.append(new String(buf, 0, count));
                    count = stream.read(buf);
                }
            } catch (IOException ex) {
                if (ex.getMessage() == null || ex.getMessage().indexOf("tream closed") < 0) {
                    sb.append("\n\n\nBIG PILE OF FAIL: ");
                    sb.append(ex.getClass().getName());
                    sb.append(", ").append(ex.getMessage());
                }
            }
        }
        public String getResult() {
            return sb.toString();
        }
    }

    /** Finds the full path to the node.js executable. */
    public static String findNode() {
        //TODO add windows executable
        String[] paths = { "/usr/bin/node", "/usr/local/bin/node", "/bin/node", "/opt/bin/node" };
        for (String p : paths) {
            File f = new File(p);
            if (f.exists() && f.canExecute()) {
                return p;
            }
        }
        return null;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        File dir = new File(args[0]);
        if (!(dir.exists() && dir.isDirectory() && dir.canRead())) {
            System.out.printf("%s is not a readable directory%n", dir);
            System.exit(1);
        }
        for (File jsf : dir.listFiles(new JsExtFilter())) {
            System.out.printf("RUNNING %s%n", jsf.getName());
            String nodePath = findNode();
            if (nodePath == null) {
                System.err.println("Could not find 'node' executable. Please install node.js and retry.");
                System.exit(1);
            }
            byte[] b1 = new byte[16834];
            byte[] b2 = new byte[16834];
            String eval = String.format("require('%s/%s').test();", dir.getName(), jsf.getName());
            Process proc = new ProcessBuilder(nodePath, "-e", eval).directory(dir.getParentFile()).start();
            ReadStream readOut = new ReadStream(proc.getInputStream(), b1);
            ReadStream readErr = new ReadStream(proc.getErrorStream(), b2);
            readOut.start();
            readErr.start();
            int xv = proc.waitFor();
            proc.getInputStream().close();
            proc.getErrorStream().close();
            if (xv != 0) {
                System.out.printf("ERROR abnormal termination of node: %s%n", xv);
            }
            if (readOut.getResult().length() > 0) {
                System.out.println(readOut.getResult().trim());
            }
            if (readErr.getResult().length() > 0) {
                System.out.println();
                System.out.println("ERRORS:");
                System.out.println(readErr.getResult().trim());
            }
            System.out.println("------------------------------------------------------");
        }
    }
}
