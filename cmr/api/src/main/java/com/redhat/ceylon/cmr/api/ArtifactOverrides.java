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

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class ArtifactOverrides {
    private static final Logger log = Logger.getLogger(ArtifactOverrides.class.getName());

    private ArtifactContext owner;
    private Set<DependencyOverride> add = new HashSet<>();
    private Set<DependencyOverride> remove = new HashSet<>();
    private DependencyOverride replace;
    private String filter;

    public ArtifactOverrides(ArtifactContext owner) {
        this.owner = owner;
    }

    public void addOverride(DependencyOverride override) {
        switch (override.getType()) {
            case ADD:
                add.add(override);
                break;
            case REMOVE:
                remove.add(override);
                break;
            case REPLACE:
                if (replace != null) {
                    log.warning(String.format("Replace for %s is already defined: %s, current: %s", owner, replace.getArtifactContext(), override.getArtifactContext()));
                }
                replace = override;
                break;
        }
    }

    public ArtifactContext getOwner() {
        return owner;
    }

    public Set<DependencyOverride> getAdd() {
        return add;
    }

    public boolean isRemoved(ArtifactContext mc) {
        for (DependencyOverride override : remove) {
            // match with optional version
            if (override.matches(mc)) {
                return true;
            }
        }
        return false;
    }

    public boolean isAddedOrUpdated(ArtifactContext mc) {
        for (DependencyOverride override : add) {
            // match just the name, so we can update with another version
            if (override.matchesName(mc)) {
                return true;
            }
        }
        return false;
    }

    public DependencyOverride getReplace() {
        return replace;
    }

    public String getFilter() {
        return filter;
    }

    void setFilter(String filter) {
        this.filter = filter;
    }
}
