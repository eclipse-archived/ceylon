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

import com.redhat.ceylon.cmr.spi.Node;

import java.io.File;

/**
 * Node utils.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public final class NodeUtils {

    /**
     * Get first parent.
     *
     * @param node the node
     * @return first parent or null
     */
    public static Node firstParent(Node node) {
        final Iterable<? extends Node> parents = node.getParents();
        //noinspection LoopStatementThatDoesntLoop
        for (Node parent : parents) {
            return parent;
        }
        return null;
    }

    /**
     * Get root.
     * 
     * @param node the node
     * @return the root
     */
    public static Node getRoot(Node node) {
        Node root = firstParent(node);
        return (root == null) ? node : getRoot(root);
    }
    
    /**
     * Get full node path; using default File.separator
     *
     * @param node the node
     * @return full path
     */
    public static String getFullPath(Node node) {
        return getFullPath(node, File.separator);
    }

    /**
     * Get full node path.
     *
     * @param node the node
     * @param separator the separator
     * @return full path
     */
    public static String getFullPath(Node node, String separator) {
        final StringBuilder path = new StringBuilder();
        buildFullPath(node, path, separator, false);
        return path.toString();
    }

    protected static void buildFullPath(Node node, StringBuilder path, String separator, boolean appendSeparator) {
        final Iterable<? extends Node> parents = node.getParents();
        //noinspection LoopStatementThatDoesntLoop
        for (Node parent : parents) {
            buildFullPath(parent, path, separator, true);
            break; // just use the first one
        }
        path.append(node.getLabel());
        if (appendSeparator && node.hasContent() == false)
            path.append(separator);
    }

}
