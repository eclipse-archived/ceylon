/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package ceylon.modules;

import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.Arrays;

import ceylon.modules.spi.ArgumentType;
import ceylon.modules.spi.Constants;
import ceylon.modules.spi.Executable;

/**
 * Main Ceylon Modules entry point.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class Main {
    public static void main(String[] args) {
        try {
            execute(args);
        } catch (Throwable t) {
            System.err.println("Illegal args: " + t);
            throw new RuntimeException("Error using Ceylon Runtime.", t);
        }
    }

    /**
     * Execute arguments.
     *
     * @param args the input arguments
     * @throws Exception for any error
     */
    public static void execute(String[] args) throws Exception {
        Configuration conf = parseArgs(args);
        String exe = conf.executable;
        if (exe == null)
            throw new IllegalArgumentException("Missing -executable argument");

        Executable executable = createInstance(Executable.class, exe);
        executable.execute(conf);
    }

    private static Configuration parseArgs(String[] args) {
        Configuration conf = new Configuration();
        int n = args.length;
        for (int i = 0; i < n; i++) {
            final String arg = args[i];
            boolean implArg = arg.startsWith(Constants.IMPL_ARGUMENT_PREFIX.toString());
            boolean ceylonArg = arg.startsWith(Constants.CEYLON_ARGUMENT_PREFIX.toString());
            if (implArg || ceylonArg) {
                ArgumentType type = implArg ? ArgumentType.IMPL : ArgumentType.CEYLON; 
                i = conf.setArgument(arg.substring(1), type, args, i); 
            } else {
                // first argument is the module spec
                conf.module = arg;
                // the rest are program arguments
                conf.arguments = Arrays.copyOfRange(args, i+1, args.length);
                // we're done processing arguments
                break;
            }
        }
        conf.check();
        return conf;
    }

    private static <T> T createInstance(final Class<T> expectedType, final String defaultImpl) throws Exception {
        if (expectedType == null)
            throw new IllegalArgumentException("Null expected type");
        if (defaultImpl == null)
            throw new IllegalArgumentException("Null default impl");

        final SecurityManager sm = System.getSecurityManager();
        if (sm == null) {
            return instantiate(expectedType, defaultImpl);
        } else {
            return AccessController.doPrivileged(new PrivilegedExceptionAction<T>() {
                @Override
                public T run() throws Exception {
                    return instantiate(expectedType, defaultImpl);
                }
            });
        }
    }

    public static <T> T instantiate(Class<T> expectedType, String defaultImpl) throws Exception {
        final String impl = System.getProperty(expectedType.getName(), defaultImpl);
        final ClassLoader cl = expectedType.getClassLoader();
        final Class<?> clazz = cl.loadClass(impl);
        final Object result = clazz.newInstance();
        return expectedType.cast(result);
    }
}
