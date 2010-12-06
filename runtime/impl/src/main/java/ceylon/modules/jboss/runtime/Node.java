/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
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

package ceylon.modules.jboss.runtime;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple node impl.
 *
 * @param <T> value type
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
class Node<T>
{
   private T value;
   private String name;
   private Node<T> parent;
   private Map<String, Node<T>> children;

   Node()
   {
   }

   Node(T value)
   {
      this.value = value;
   }

   Node<T> addChild(String token)
   {
      return addChild(token, null);
   }

   Node<T> addChild(String token, T value)
   {
      if (children == null)
         children = new ConcurrentHashMap<String, Node<T>>();

      Node<T> child = children.get(token);
      if (child == null)
      {
         child = new Node<T>(value);
         child.name = token;
         child.parent = this;
         children.put(token, child);
      }
      return child;
   }

   Node<T> getChild(String token)
   {
      return children != null ? children.get(token) : null;
   }

   T getValue()
   {
      return value;
   }

   void setValue(T value)
   {
      this.value = value;
   }

   boolean isEmpty()
   {
      return (children == null || children.isEmpty());
   }

   void remove()
   {
      setValue(null);
      
      if (parent != null)
      {
         Map<String, Node<T>> nodes = parent.children;
         if (nodes != null)
            nodes.remove(name);
      }
   }
}
