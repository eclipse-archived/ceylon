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

import java.util.Objects;
import java.util.Set;

/**
 * Module info.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public final class ModuleInfo {
    private String filter;
    private Set<ModuleDependencyInfo> dependencies;

    public ModuleInfo(String filter, Set<ModuleDependencyInfo> dependencies) {
        this.filter = filter;
        this.dependencies = dependencies;
    }
    
    public Set<ModuleDependencyInfo> getDependencies() {
        return dependencies;
    }
    
    public String getFilter() {
        return filter;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ModuleInfo that = (ModuleInfo) o;
        return Objects.equals(filter, that.filter)
                && dependencies.equals(that.dependencies);
    }

    @Override
    public int hashCode() {
        int ret = 17;
        ret = 37 * ret + (filter == null ? 0 : filter.hashCode());
        ret = 37 * ret + dependencies.hashCode();
        return ret;
    }

    @Override
    public String toString() {
        return "[filter: "+filter+", dependencies: "+dependencies+"]";
    }
}
