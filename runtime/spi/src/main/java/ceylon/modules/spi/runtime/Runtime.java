/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package ceylon.modules.spi.runtime;

import ceylon.modules.Configuration;
import ceylon.modules.spi.Executable;

/**
 * Ceylon Modules runtime spi.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public interface Runtime extends Executable {
    /**
     * Create modular ClassLoader.
     *
     * @param name    the module name
     * @param version the module version
     * @param conf    the runtime configuration
     * @return holder classloader holder instance
     * @throws Exception for ay error
     */
    ClassLoaderHolder createClassLoader(String name, String version, Configuration conf) throws Exception;
}
