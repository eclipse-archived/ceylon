

package cz.brno.as8;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class run_ {
    public static void main(String[] args) {
        org.jboss.acme.$module_.run(); // should be able to run this
        ping("eu.cloud.clazz.run_");
        ping("org.jboss.filtered.api.SomeAPI");
    }

    private static void ping(String className) {
        try {
            run_.class.getClassLoader().loadClass(className);
            throw new RuntimeException("Should not be here");
        } catch (ClassNotFoundException cnfe) {
            System.out.println("CNFE = " + cnfe);
        }
    }
}
