/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package ceylon.modules.jboss.runtime;

import ceylon.modules.Configuration;

import org.eclipse.ceylon.cmr.api.RepositoryManager;
import org.jboss.modules.ModuleLoader;

/**
 * Default Ceylon Modules runtime.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class JBossRuntime extends AbstractJBossRuntime {
    protected ModuleLoader createModuleLoader(Configuration conf) throws Exception {
        RepositoryManager repository = createRepository(conf);
        return new CeylonModuleLoader(repository, conf.autoExportMavenDependencies);
    }
}
