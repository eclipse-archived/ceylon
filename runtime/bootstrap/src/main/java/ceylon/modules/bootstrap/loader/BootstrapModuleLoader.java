/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package ceylon.modules.bootstrap.loader;

import org.jboss.modules.ModuleLoader;

import java.io.File;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * Abstract bootstrap module loader.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
abstract class BootstrapModuleLoader extends ModuleLoader {

    /**
     * Get Ceylon repository.
     *
     * @return the ceylon repository
     */
    protected static String getCeylonRepository() {
        return AccessController.doPrivileged(new PrivilegedAction<String>() {
            public String run() {
                final String defaultCeylonRepository = System.getProperty("user.home") + File.separator + ".ceylon" + File.separator + "repo";
                return System.getProperty("ceylon.repo", defaultCeylonRepository);
            }
        });
    }

}
