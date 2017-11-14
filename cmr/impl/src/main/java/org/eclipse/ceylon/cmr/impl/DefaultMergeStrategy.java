/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.cmr.impl;

import org.eclipse.ceylon.cmr.spi.MergeStrategy;
import org.eclipse.ceylon.cmr.spi.OpenNode;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class DefaultMergeStrategy implements MergeStrategy {
    public void conflict(OpenNode previous, OpenNode current) {
        throw new IllegalArgumentException("Cannot merge, dup node: " + previous + " vs. " + current);
    }
}
