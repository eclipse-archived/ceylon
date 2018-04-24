/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.cmr.repository.webdav;

import java.io.InputStream;

import org.eclipse.ceylon.aether.github.sardine.impl.io.ContentLengthInputStream;

public class WebDAVInputStream {

    private ContentLengthInputStream src;

    WebDAVInputStream(ContentLengthInputStream src) {
        this.src = src;
    }

    public Long getLength() {
        return src.getLength();
    }

    public InputStream getInputStream() {
        return src;
    }

}
