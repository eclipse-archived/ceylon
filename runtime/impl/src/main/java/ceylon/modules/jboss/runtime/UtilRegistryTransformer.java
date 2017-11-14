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

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import org.eclipse.ceylon.cmr.api.RepositoryManager;
import org.eclipse.ceylon.common.ModuleUtil;
import org.eclipse.ceylon.model.cmr.ArtifactResult;
import org.jboss.modules.ModuleIdentifier;

/**
 * Per module Util registry.
 * <p/>
 * A bit of a hack / workaround to register stuff on first class load.
 * <p/>
 * Check this again when re-linking usage is more common!
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class UtilRegistryTransformer implements ClassFileTransformer {
    private volatile boolean done;

    private final ModuleIdentifier mi;
    private final ArtifactResult result;

    public UtilRegistryTransformer(ModuleIdentifier mi, ArtifactResult result) {
        this.mi = mi;
        this.result = result;
    }

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        register(loader);
        return classfileBuffer;
    }

    static void registerModule(final String name, String _version, final ArtifactResult result, final ClassLoader cl, boolean async) {
        // transform "null" into null version for the default module
        final String version = RepositoryManager.DEFAULT_MODULE.equals(name) ? null : _version;
        // FIXME: this hack is required to make sure we never try to load modules in the metamodel with the
        // classloader lock, because this causes deadlocks: https://github.com/ceylon/ceylon.language/issues/429
        if(async){
            if(cl instanceof CeylonModuleClassLoader){
                ((CeylonModuleClassLoader) cl).registerThreadRunning();
            }
            new Thread(){
                @Override
                public void run() {
                    org.eclipse.ceylon.compiler.java.Util.loadModule(name, version, result, cl);
                    if(cl instanceof CeylonModuleClassLoader){
                        ((CeylonModuleClassLoader) cl).registerThreadDone();
                    }
                }
            }.start();
        }else
            org.eclipse.ceylon.compiler.java.Util.loadModule(name, version, result, cl);
    }

    public void register(ClassLoader loader) {
        if (done == false) {
            synchronized (this) {
                if (done == false) {
                    done = true;
                    String name = ModuleUtil.getModuleNameFromUri(mi.getName());
                    registerModule(name, mi.getSlot(), result, loader, true);
                }
            }
        }
    }
}
