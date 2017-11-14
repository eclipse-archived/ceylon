/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.cmr.spi;

import java.io.InputStream;

/**
 * An InputStream holder which may know its size
 */
public class SizedInputStream {

    private final InputStream inputStream;
    
    private final long size;
    
    public SizedInputStream(InputStream inputStream, long size){
        this.inputStream = inputStream;
        this.size = size;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    /**
     * Can be -1 if we don't know its size
     */
    public long getSize() {
        return size;
    }
}
