/*
 * Copyright 2011 Red Hat inc. and third party contributors as noted
 * by the author tags.
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

package com.redhat.ceylon.cmr.api;

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
