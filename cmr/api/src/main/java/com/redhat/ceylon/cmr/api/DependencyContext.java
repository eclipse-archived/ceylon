/*
 * Copyright 2014 Red Hat inc. and third party contributors as noted
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
public interface DependencyContext {
    /**
     * Get current artifact,
     * whose dependencies we're trying to read.
     *
     * @return current artifact
     */
    ArtifactResult result();

    /**
     * Do we ignore inner descriptors.
     * e.g. could be used to override inner decriptor
     *
     * @return true if yes, false otherwise
     */
    boolean ignoreInner();

    /**
     * Do we ignore external descriptors.
     * e.g. must be applied when looking at a flat classpath
     *
     * @return true if yes, false otherwise
     */
    boolean ignoreExternal();
}
