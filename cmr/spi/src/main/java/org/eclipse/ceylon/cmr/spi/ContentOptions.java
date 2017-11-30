/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.cmr.spi;

/**
 * Content options.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public interface ContentOptions {

    /**
     * Default content options.
     */
    static final ContentOptions DEFAULT = new ContentOptions() {
        public boolean forceOperation() {
            return false;
        }

        public boolean forceDescriptorCheck() {
            return false;
        }
    };

    /**
     * Do we force operation.
     *
     * @return true if forced, false otherwise
     */
    boolean forceOperation();

    /**
     * Do we force descriptor check.
     *
     * @return true if forced, false otherwise
     */
    boolean forceDescriptorCheck();
}
