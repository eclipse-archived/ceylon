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

import java.io.File;
import java.io.IOException;

/**
 * Resolver API.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public interface Resolver {

    /**
     * Resolve dependencies for the context.
     *
     * @param name    the module name
     * @param version the module version
     * @return all dependencies, null if they cannot be found
     * @throws IOException for any I/O error
     */
    File[] resolve(String name, String version) throws IOException;

    /**
     * Resolve dependencies for the context.
     *
     * @param context the artifact context to resolve
     * @return all dependencies, null if they cannot be found
     * @throws IOException for any I/O error
     */
    File[] resolve(ArtifactContext context) throws IOException;

}
