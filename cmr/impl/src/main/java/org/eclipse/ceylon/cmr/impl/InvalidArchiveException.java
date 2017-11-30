/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.cmr.impl;

/**
 * Thrown when a module artifact has an invalid SHA1 signature.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
@SuppressWarnings("serial")
public class InvalidArchiveException extends RuntimeException {

    private String path;
    private String repository;

    public InvalidArchiveException(String message, String path, String repository) {
        super(message);
        this.path = path;
        this.repository = repository;
    }

    public String getPath(){
        return path;
    }

    public String getRepository() {
        return repository;
    }
}
