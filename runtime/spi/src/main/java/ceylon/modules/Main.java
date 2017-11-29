/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package ceylon.modules;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import ceylon.modules.spi.ArgumentType;
import ceylon.modules.spi.Constants;
import ceylon.modules.spi.Executable;

/**
 * Main Ceylon Modules entry point.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 * @author Stephane Epardaud
 */
public class Main {
    /**
     * Ceylon Runtime main entry point.
     *
     * @param args the args
     * @throws Throwable
     */
    public static void main(String[] args) throws Throwable {
        try {
            execute(args);
        } catch (CeylonRuntimeException cre) {
            throw cre;
        } catch (Throwable t) {
            // Get rid of unwanted stack elements
            Throwable t2 = (t.getCause() != null) ? t.getCause() : t;
            if (t2 instanceof InvocationTargetException) {
                t2 = (t.getCause() != null) ? t2.getCause() : t2;
            }
            throw t2;
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
        if (exe == null) {
            throw new CeylonRuntimeException("Missing -executable argument");
        }

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

    public static <T> T createInstance(final Class<T> expectedType, final String defaultImpl) throws Exception {
        if (expectedType == null)
            throw new CeylonRuntimeException("Null expected type");
        if (defaultImpl == null)
            throw new CeylonRuntimeException("Null default impl");

        return SecurityActions.instantiate(expectedType, defaultImpl);
    }
}
