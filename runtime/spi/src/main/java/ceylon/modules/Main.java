/*
 * Copyright 2011 Red Hat inc. and third party contributors as noted 
 * by the author tags.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ceylon.modules;

import ceylon.modules.spi.ArgumentType;
import ceylon.modules.spi.Constants;
import ceylon.modules.spi.Executable;

import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.Arrays;

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
                conf.arguments = Arrays.copyOfRange(args, i + 1, args.length);
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
