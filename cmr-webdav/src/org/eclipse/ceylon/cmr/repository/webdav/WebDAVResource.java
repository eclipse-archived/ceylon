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

import java.util.Date;

import org.eclipse.ceylon.aether.github.sardine.DavResource;

public class WebDAVResource {

    private DavResource res;

    WebDAVResource(DavResource res) {
        this.res = res;
    }

    public String getName() {
        return res.getName();
    }

    public boolean isDirectory() {
        return res.isDirectory();
    }

    public Long getContentLength() {
        return res.getContentLength();
    }

    public Date getModified() {
        return res.getModified();
    }

}
