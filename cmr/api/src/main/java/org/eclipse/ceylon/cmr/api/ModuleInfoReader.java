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

import org.eclipse.ceylon.cmr.api.ModuleVersionDetails;
import org.eclipse.ceylon.cmr.api.Overrides;

public interface ModuleInfoReader {

    public boolean matchesModuleInfo(String moduleName, String version, File moduleArchive, String query, Overrides overrides);

    public ModuleVersionDetails readModuleInfo(String moduleName, String version, File moduleArchive, boolean includeMembers, Overrides overrides);

    public int[] getBinaryVersions(String moduleName, String version, File moduleArchive);

}
