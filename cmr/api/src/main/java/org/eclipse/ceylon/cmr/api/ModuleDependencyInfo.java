/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.cmr.api;

import org.eclipse.ceylon.common.Backends;
import org.eclipse.ceylon.common.ModuleUtil;
import org.eclipse.ceylon.model.cmr.ModuleScope;

/**
 * Module info.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public final class ModuleDependencyInfo implements Comparable<ModuleDependencyInfo> {
    private String namespace;
    private String name;
    private String version;
    private boolean optional;
    private boolean shared;
    private ModuleScope scope;
    private Backends backends;

    public ModuleDependencyInfo(String namespace, String name, String version,
            boolean optional, boolean shared) {
        this(namespace, name, version, optional, shared, Backends.ANY);
    }
    
    public ModuleDependencyInfo(String namespace, String name, String version, 
            boolean optional, boolean shared, Backends backends) {
        this(namespace, name, version, optional, shared, backends, ModuleScope.COMPILE);
    }
    
    public ModuleDependencyInfo(String namespace, String name, String version,
            boolean optional, boolean shared, Backends backends, ModuleScope scope) {
        this.namespace = namespace;
        this.name = name;
        this.version = version;
        this.optional = optional;
        this.shared = shared;
        this.scope = scope;
        this.backends = backends != null ? backends : Backends.ANY;
        assert(ModuleUtil.validNamespace(namespace));
    }

    public String getNamespace() {
        return namespace;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public boolean isOptional() {
        return optional;
    }

    public boolean isExport() {
        return shared;
    }

    public ModuleScope getModuleScope() {
        return scope;
    }

    public Backends getNativeBackends() {
        return backends;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ModuleDependencyInfo that = (ModuleDependencyInfo) o;
        return that.name.equals(name)
            && that.version.equals(version)
            && that.shared == shared
            && that.scope == scope
            && that.optional == optional;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public int compareTo(ModuleDependencyInfo that) {
        int res = name.compareTo(that.name);
        if (res == 0) {
            res = version.compareTo(that.version);
            if (res == 0) {
                res = Boolean.compare(shared, that.shared);
                if (res == 0) {
                    res = Boolean.compare(optional, that.optional);
                    if (res == 0) {
                        res = scope.compareTo(that.scope);
                    }
                }
            }
        }
        return res;
    }

    @Override
    public String toString() {
        return ((shared) ? "shared " : "") +
                ((optional) ? "optional " : "") +
                ((namespace != null) ? namespace + ":" : "") +
                getModuleName() +
                ((scope != ModuleScope.COMPILE) ? " "+scope : "")
                ;
    }

    public String getModuleName() {
        return name + "/" + version;
    }
}
