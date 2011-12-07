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

package com.redhat.ceylon.cmr.spi;

import java.io.IOException;
import java.io.InputStream;

/**
 * Content store.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public interface ContentStore {

    /**
     * Just check if the content is there for node.
     *
     * @param node the node to check
     * @return content handle or null if content doesn't exist
     */
    ContentHandle popContent(Node node);

    /**
     * Get the content handle.
     * Throw IO exception if the content is not there.
     *
     * @param node the node to check
     * @return content handle
     * @throws IOException if content is not found or any other I/O error
     */
    ContentHandle getContent(Node node) throws IOException;

    /**
     * Put content for node.
     *
     * @param node the node to put against
     * @param stream the content stream
     * @return content handle
     * @throws IOException for any I/O error
     */
    ContentHandle putContent(Node node, InputStream stream) throws IOException;

}
