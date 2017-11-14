/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package ceylon.modules.jboss.runtime;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple node impl.
 *
 * @param <T> value type
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
class Node<T> {
    private T value;
    private String name;
    private Node<T> parent;
    private Map<String, Node<T>> children;

    Node() {
    }

    Node(T value) {
        this.value = value;
    }

    Node<T> addChild(String token) {
        return addChild(token, null);
    }

    Node<T> addChild(String token, T value) {
        if (children == null)
            children = new ConcurrentHashMap<String, Node<T>>();

        Node<T> child = children.get(token);
        if (child == null) {
            child = new Node<T>(value);
            child.name = token;
            child.parent = this;
            children.put(token, child);
        }
        return child;
    }

    Node<T> getChild(String token) {
        return children != null ? children.get(token) : null;
    }

    T getValue() {
        return value;
    }

    void setValue(T value) {
        this.value = value;
    }

    boolean isEmpty() {
        return (children == null || children.isEmpty());
    }

    void remove() {
        setValue(null);

        if (parent != null) {
            Map<String, Node<T>> nodes = parent.children;
            if (nodes != null)
                nodes.remove(name);
        }
    }
}
