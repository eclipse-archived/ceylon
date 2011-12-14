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

package com.redhat.ceylon.cmr.api;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Repository API.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public interface Repository {
    static final String NO_VERSION = "**NO_VERSION**";

    File getArtifact(String name, String version) throws IOException;
    File getArtifact(ArtifactContext context) throws IOException;

    void putArtifact(String name, String version, InputStream content) throws IOException;
    void putArtifact(String name, String version, File content) throws IOException;
    void putArtifact(ArtifactContext context, InputStream content) throws IOException;
    void putArtifact(ArtifactContext context, File content) throws IOException;

    void removeArtifact(String name, String version) throws IOException;
    void removeArtifact(ArtifactContext context) throws IOException;
}
