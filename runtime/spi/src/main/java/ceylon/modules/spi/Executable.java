/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package ceylon.modules.spi;

import ceylon.modules.Configuration;

/**
 * Simple executable.
 * It takes configuration and starts a new process.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public interface Executable {
    /**
     * Execute process with configuration.
     *
     * @param conf the configuration
     * @throws Exception for any error
     */
    void execute(Configuration conf) throws Exception;
}
