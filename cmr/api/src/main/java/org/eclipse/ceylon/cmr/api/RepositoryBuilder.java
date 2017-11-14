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

import java.io.File;
import java.net.Proxy;

import org.eclipse.ceylon.common.log.Logger;

/**
 * Build repository from token.
 * 
 * All implementations of this interface (except for 2 built-in classes) should be registered in
 * <code>api/src/main/resources/META-INF/services/org.eclipse.ceylon.cmr.api.RepositoryBuilder</code>.
 * The must also do their work only when encountering their specific prefix in the token passed to
 * <code>buildRespository()</code> and return <code>null</code> in all other cases. Exceptions may
 * only be thrown when the token prefix matches but some other error condition is encountered.
 * Care must be taken to choose a unique prefix.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 * @author Tako Schotanus (tako@ceylon-lang.org)
 */
public interface RepositoryBuilder {
    
    static class RepositoryBuilderConfig {
        public final Logger log;
        public final boolean offline;
        public final int timeout;
        public final Proxy proxy;
        public final String currentDirectory;
        
        public RepositoryBuilderConfig(Logger log, boolean offline, int timeout, Proxy proxy, String currentDirectory) {
            this.log = log;
            this.offline = offline;
            this.timeout = timeout;
            this.proxy = proxy;
            this.currentDirectory = currentDirectory;
        }
    }
    
    public static final RepositoryBuilderConfig EMPTY_CONFIG = new RepositoryBuilderConfig(null, false, -1, null, null);
    
    /**
     * If the token is path based this method will return the token
     * with any path elements made absolute using the folder being
     * passed as the current working directory. Returns null if the
     * token wasn't meant for this repository
     *
     * @param cwd the folder to use as the current working directory
     * @param token the token to make absolute
     * @return boolean if token is accepted or not
     */
    String absolute(File cwd, String token) throws Exception;
    
    /**
     * Build repository.
     *
     * @param token the token used to create the repository
     * @return repository or null if this builder doesn't support the given token
     * @throws Exception for any error
     */
    CmrRepository[] buildRepository(String token) throws Exception;
    
    /**
     * Build repository.
     *
     * @param token the token used to create the repository
     * @param config additional configuration that can be used to create the repository
     * @return repository or null if this builder doesn't support the given token
     * @throws Exception for any error
     */
    CmrRepository[] buildRepository(String token, RepositoryBuilderConfig config) throws Exception;
}
