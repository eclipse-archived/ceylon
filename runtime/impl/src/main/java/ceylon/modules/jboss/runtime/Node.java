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
