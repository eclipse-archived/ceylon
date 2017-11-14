/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.cmr.api;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class DependencyOverride {
    public static enum Type {
        ADD,
        REMOVE,
        REPLACE
    }

    private ArtifactContext ctx;
    private Type type;
    private boolean shared;
    private boolean optional;

    public DependencyOverride(ArtifactContext mvn, Type type, boolean shared, boolean optional) {
        this.ctx = mvn;
        this.type = type;
        this.shared = shared;
        this.optional = optional;
    }

    public ArtifactContext getArtifactContext() {
        return ctx;
    }

    public Type getType() {
        return type;
    }
    
    public boolean isShared(){
        return shared;
    }

    public boolean isOptional() {
        return optional;
    }

    @Override
    public int hashCode() {
        return ctx.hashCode() + 7 * type.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DependencyOverride == false) {
            return false;
        } else {
            DependencyOverride doo = (DependencyOverride) obj;
            return ctx.equals(doo.ctx) && (type == doo.type);
        }
    }

    public boolean matches(ArtifactContext other) {
        // name must match
        if(!ctx.getName().equals(other.getName()))
            return false;
        if(ctx.getVersion() == null)
            return true;
        return ctx.getVersion().equals(other.getVersion());
    }

    public boolean matchesName(ArtifactContext other) {
        // name must match
        return ctx.getName().equals(other.getName());
    }
}
