

package ceylon.modules.bootstrap.loader;

import org.jboss.modules.ModuleIdentifier;
import org.jboss.modules.ModuleLoader;

import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;

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

    static String getProperty(final String key, final String defaultValue) {
        final SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            return AccessController.doPrivileged(new PrivilegedAction<String>() {
                public String run() {
                    return System.getProperty(key, defaultValue);
                }
            });
        } else {
            return System.getProperty(key, defaultValue);
        }
    }

    static boolean getBoolean(final String key) {
        final SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            return AccessController.doPrivileged(new PrivilegedAction<Boolean>() {
                public Boolean run() {
                    return Boolean.getBoolean(key);
                }
            });
        } else {
            return Boolean.getBoolean(key);
        }
    }

    static Method findModule() {
        final SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            return AccessController.doPrivileged(new PrivilegedAction<Method>() {
                public Method run() {
                    return findModuleInternal();
                }
            });
        } else {
            return findModuleInternal();
        }
    }

    private static Method findModuleInternal() throws RuntimeException {
        try {
            final Method declaredMethod = ModuleLoader.class.getDeclaredMethod("findModule", ModuleIdentifier.class);
            declaredMethod.setAccessible(true);
            return declaredMethod;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
