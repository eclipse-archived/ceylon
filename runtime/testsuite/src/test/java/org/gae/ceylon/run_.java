

package org.gae.ceylon;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class run_ {
    public static void main(String[] args) throws Throwable {
        run_.class.getClassLoader().loadClass("com.google.appengine.api.LifecycleManager");
        try {
            run_.class.getClassLoader().loadClass("javax.mail.Version");
            throw new IllegalStateException("Should not be here!");
        } catch (ClassNotFoundException ignored) {
        }
    }
}
