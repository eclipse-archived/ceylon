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

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

/**
 * Contract for a component that can create a compilation artifact.
 * 
 * @author Enrique Zamudio
 */
public interface SourceArtifactCreator extends ArtifactCreator {

    /** Copy the specified source streams, avoiding duplicate entries. */
    public Set<String> copyStreams(Collection<SourceStream> sourceStreams) throws IOException;
}
