/*
 * Copyright 2011 Red Hat inc. and third party contributors as noted 
 * by the author tags.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.redhat.ceylon.cmr.impl;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import com.redhat.ceylon.cmr.api.Repository;
import com.redhat.ceylon.cmr.spi.Node;
import com.redhat.ceylon.cmr.spi.OpenNode;

/**
 * Node utils.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public final class NodeUtils {

    private static final String INFO = ".repository";

    /**
     * Navigate to node.
     *
     * @param root   the root
     * @param tokens the tokens
     * @return found node or null
     */
    public static Node getNode(Node root, Iterable<String> tokens) {
        Node current = root;
        for (String token : tokens) {
            current = current.getChild(token);
            if (current == null) {
                return null;
            }
        }
        return current;
    }

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
     * @param node      the node
     * @param separator the separator
     * @return full path
     */
    public static String getFullPath(Node node, String separator) {
        final StringBuilder path = new StringBuilder();
        buildFullPath(node, path, separator, false);
        return path.toString();
    }

    /**
     * Return path queue, with root being on top.
     *
     * @param node the node
     * @return paths queue
     */
    public static List<String> toLabelPath(Node node) {
        final LinkedList<String> paths = new LinkedList<String>();
        Node current = node;
        Node parent = firstParent(node);
        // ignore root path, should equal to ""
        while (parent != null) {
            paths.addFirst(current.getLabel());
            current = parent;
            parent = firstParent(current);
        }
        return paths;
    }

    /**
     * Is root ancestor from artifact.
     *
     * @param root     the root
     * @param artifact the artifact
     * @return true if ancestor, false otherwise
     */
    public static boolean isAncestor(Node root, Node artifact) {
        Node current = artifact;
        while (current != null) {
            if (root == current)
                return true;
            current = firstParent(current);
        }
        return false;
    }

    /**
     * Keep the repository info.
     *
     * @param node       the node
     * @param repository the repository
     */
    public static void keepRepository(Node node, Repository repository) {
        if (node instanceof OpenNode) {
            final OpenNode on = (OpenNode) node;
            on.addNode(INFO, repository);
        }
    }

    /**
     * Get repository info.
     *
     * @param node the node
     * @return repository info
     */
    public static Repository getRepository(Node node) {
        if (node instanceof OpenNode) {
            final OpenNode on = (OpenNode) node;
            final Node info = on.peekChild(INFO);
            return (info != null) ? info.getValue(Repository.class) : null;
        }
        return null;
    }

    public static final String UNKNOWN_REPOSITORY = "Unknown repository";
    
    /**
     * Get repository display string.
     *
     * @param node the node
     * @return repository display string
     */
    public static String getRepositoryDisplayString(Node node) {
        Repository repo = getRepository(node);
        return repo != null ? repo.getDisplayString() : UNKNOWN_REPOSITORY;
    }

    protected static void buildFullPath(Node node, StringBuilder path, String separator, boolean appendSeparator) {
        final Iterable<? extends Node> parents = node.getParents();
        //noinspection LoopStatementThatDoesntLoop
        for (Node parent : parents) {
            buildFullPath(parent, path, separator, true);
            break; // just use the first one
        }
        path.append(node.getLabel());
        if (appendSeparator)
            path.append(separator);
    }
}
