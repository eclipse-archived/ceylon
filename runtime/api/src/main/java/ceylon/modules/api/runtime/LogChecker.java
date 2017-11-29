/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package ceylon.modules.api.runtime;

import java.util.List;

import org.eclipse.ceylon.cmr.api.ModuleDependencyInfo;
import org.eclipse.ceylon.model.cmr.ArtifactResult;

/**
 * This allows for external checkers to replace current log module dependency.
 * e.g. JBoss Logging can unify all logging configuration -- same as it's done in WildFly
 *
 * @author Matej Lazar
 * @author Ales Justin
 */
public interface LogChecker {
    /**
     * Return list of module infos that replace this current logging dependency.
     * Or return null if you cannot determine if this is a logging module.
     *
     * @param dependency the current dependency
     * @return null if not determined, otherwise list of module info dependencies
     * @throws java.lang.IllegalArgumentException if empty list is returned
     */
    List<ModuleDependencyInfo> handle(ArtifactResult dependency);
}
