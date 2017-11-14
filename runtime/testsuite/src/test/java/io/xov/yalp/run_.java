

package io.xov.yalp;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class run_ {
    public static void main(String[] args) throws Exception {
        Class<run_> run_class = run_.class;
        ClassLoader runclassClassLoader = run_class.getClassLoader();
        System.err.println(runclassClassLoader.loadClass("org.jboss.vfs.VFS"));
        System.err.println(runclassClassLoader.loadClass("javax.net.ssl.KeyManager"));
    }
}
