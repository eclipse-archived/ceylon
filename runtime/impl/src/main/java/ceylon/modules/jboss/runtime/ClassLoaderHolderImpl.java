/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package ceylon.modules.jboss.runtime;

import ceylon.modules.spi.runtime.ClassLoaderHolder;
import org.jboss.modules.Module;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
class ClassLoaderHolderImpl implements ClassLoaderHolder {
    private final Module module;

    ClassLoaderHolderImpl(Module module) {
        this.module = module;
    }

    public ClassLoader getClassLoader() {
        return SecurityActions.getClassLoader(module);
    }

    public String getVersion() {
        return module.getIdentifier().getSlot();
    }

    @Override
    public String toString() {
        return module.toString();
    }
}
