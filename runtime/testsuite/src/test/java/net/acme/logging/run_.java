package net.acme.logging;

/**
 * @author <a href="mailto:matejonnet@gmail.com">Matej Lazar</a>
 */
public class run_ {
    public static void main(String[] args) throws ClassNotFoundException {
        run_.class.getClassLoader().loadClass("java.util.logging.Logger");
        // run_.class.getClassLoader().loadClass("org.jboss.logmanager.LogManager");
        System.out.println(run_.class.getName() + ": run_ ...");
    }
}
