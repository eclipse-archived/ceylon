import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;


/** Runs the test() function in each js module that was generated.
 * 
 * @author Enrique Zamudio
 */
public class NodeTest {

    private static class JsExtFilter implements FilenameFilter {
        @Override
        public boolean accept(File arg0, String name) {
            return name.endsWith(".js");
        }
    }

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
        //File tmpscript = new File(dir.getParentFile(), "__tmp");
        for (File jsf : dir.listFiles(new JsExtFilter())) {
            System.out.printf("Running tests for %s%n", jsf.getName());
            //Create a tmp file
            //PrintStream pout = new PrintStream(new FileOutputStream(tmpscript, false));
            //pout.close();
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
                System.out.println("abnormal termination of node: " + xv);
            }
            System.out.println("RESULT:");
            System.out.println(readOut.getResult());
            if (readErr.getResult().length() > 0) {
                System.out.println("ERRORS:");
                System.out.println(readErr.getResult());
            }
        }
    }
}
