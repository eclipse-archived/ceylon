

package org.jogl.ceylon;

import java.lang.reflect.Method;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class run_ {
    public static void main(String[] args) throws Throwable {
        Class<?> clazz = run_.class.getClassLoader().loadClass("javax.media.opengl.GLProfile");
        Method method = clazz.getMethod("getDefault");
        Object result = method.invoke(null);
        System.out.println("GL instance = " + result);
    }
}
