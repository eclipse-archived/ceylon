/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.model.loader;

import java.net.URI;
import java.util.Collection;
import java.util.List;

import org.eclipse.ceylon.model.cmr.ArtifactResult;

public interface ContentAwareArtifactResult extends ArtifactResult {
    Collection<String> getPackages();
    Collection<String> getEntries();
    byte[] getContents(String path);
    URI getContentUri(String path);
    List<String> getFileNames(String path);
}