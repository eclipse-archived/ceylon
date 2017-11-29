/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.model.cmr;

/**
 * Filter used to determine whether a path should be included or excluded from imports and exports.
 *
 * @author John Bailey
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public interface PathFilter {

    /**
     * Determine whether a path should be accepted.  The given name is a path separated
     * by "{@code /}" characters.
     *
     * @param path the path to check
     * @return true if the path should be accepted, false if not
     */
    boolean accept(String path);
}