/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.jboss.filtered.impl;

import org.jboss.filtered.api.SomeAPI;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class SomeImpl extends SomeAPI {
    protected String onward() {
        return "Onward ";
    }

    public String go(String arg) {
        return onward() + arg + "!";
    }
}
