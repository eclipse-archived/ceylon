

package net.something.xyz;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class run_ {
    public static void main(String[] args) {
        // test resource on_demand
        ClassLoader cl = run_.class.getClassLoader();

        try {
            // test class on_demand
            cl.loadClass("org.jboss.acme.Qwert").newInstance();
/*
            Object m = cl.loadClass("org.jboss.acme.run_").newInstance();
            Class<?> clazz = m.getClass();
            Method run_ = clazz.getMethod("main", String[].class);
            @SuppressWarnings("UnnecessaryLocalVariable")
            Object fsa = args;
            run_.invoke(m, fsa);
*/

            cl.loadClass("si.alesj.ceylon.test.Touch"); // MC currently only works on classes
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String resource = "si/alesj/ceylon/test/config.xml";
        Object url = cl.getResource(resource);
        if (url == null)
            throw new IllegalArgumentException("Null url: " + resource);
        System.out.println("URL: " + url);
    }
}
