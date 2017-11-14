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
import java.io.FileInputStream;
import java.io.InputStream;

import org.eclipse.ceylon.cmr.spi.Node;
import org.eclipse.ceylon.model.cmr.RepositoryException;

/**
 * Helper class.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class Helper {
    public static InputStream toInputStream(Node node) throws RepositoryException {
        try {
            return node.getInputStream();
        } catch (RepositoryException e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryException(e);
        }
    }

    public static InputStream toInputStream(File file) throws RepositoryException {
        try {
            return new FileInputStream(file);
        } catch (RepositoryException e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryException(e);
        }
    }
}
