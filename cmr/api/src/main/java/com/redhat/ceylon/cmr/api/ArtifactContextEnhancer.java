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
 * Artifact context enhancer -- prepare artifact context per custom repo.
 * e.g. Maven has different naming for artifacts + default suffix is .jar
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public interface ArtifactContextEnhancer {
    /**
     * Build artifact path.
     *
     * @param context the context
     * @param addLeaf the add leaf flag
     * @return artifact path
     */
    Iterable<String> buildArtifactTokens(ArtifactContext context, boolean addLeaf);
}
