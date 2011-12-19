/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package com.redhat.ceylon.cmr.impl;

import com.redhat.ceylon.cmr.spi.OpenNode;
import com.redhat.ceylon.cmr.spi.StructureBuilder;

import java.io.File;
import java.net.URI;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * Root builder.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class RootBuilder {

    private StructureBuilder structureBuilder;
    
    public RootBuilder(String token) throws Exception {
        if (token == null)
            throw new IllegalArgumentException("Null root token");

        final String key = (token.startsWith("${") ? token.substring(2, token.length() - 1) : token);
        final String temp = AccessController.doPrivileged(new PrivilegedAction<String>() {
            public String run() {
                return System.getProperty(key);
            }
        });
        if (temp != null)
            token = temp;
        
        if (token.startsWith("http")) {
            structureBuilder = new RemoteContentStore(token);
        } else {
            final File file = (token.startsWith("file") ? new File(new URI(token)) : new File(token));
            if (file.exists() == false)
                throw new IllegalArgumentException("Token does not point to an existing directory: " + token);
            if (file.isDirectory() == false)
                throw new IllegalArgumentException("Token does not point to a directory: " + token);
            
            structureBuilder = new FileContentStore(file);
        }
    }
    
    public OpenNode buildRoot() {
        return structureBuilder.createRoot();       
    }    
}