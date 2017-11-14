

package ceylon.modules;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedExceptionAction;

/**
 * Security actions.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
final class SecurityActions {
    static String getProperty(final String key) {
        final SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            return AccessController.doPrivileged(new PrivilegedAction<String>() {
                public String run() {
                    return System.getProperty(key);
                }
            });
        } else {
            return System.getProperty(key);
        }
    }

    static void exit(final int status) {
        final SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            AccessController.doPrivileged(new PrivilegedAction<Object>() {
                public Object run() {
                    System.exit(status);
                    return null;
                }
            });
        } else {
            System.exit(status);
        }
    }

    static <T> T instantiate(final Class<T> expectedType, final String defaultImpl) throws Exception {
        final SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            return AccessController.doPrivileged(new PrivilegedExceptionAction<T>() {
                public T run() throws Exception {
                    return instantiateInternal(expectedType, defaultImpl);
                }
            });
        } else {
            return instantiateInternal(expectedType, defaultImpl);
        }
    }

    private static <T> T instantiateInternal(final Class<T> expectedType, final String defaultImpl) throws Exception {
        final String impl = System.getProperty(expectedType.getName(), defaultImpl);
        final ClassLoader cl = expectedType.getClassLoader();
        final Class<?> clazz = cl.loadClass(impl);
        final Object result = clazz.newInstance();
        return expectedType.cast(result);
    }
}
