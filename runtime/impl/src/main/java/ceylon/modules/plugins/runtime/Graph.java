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

package ceylon.modules.plugins.runtime;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Simple graph impl.
 *
 * @param <K> exact key type
 * @param <V> exact vertex value type
 * @param <E> exact edge cost type
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
class Graph<K, V, E>
{
   private ConcurrentMap<K, Vertex<V, E>> vertices = new ConcurrentHashMap<K, Vertex<V,E>>();

   public Vertex<V, E> createVertex(K key, V value)
   {
      Vertex<V, E> v = new Vertex<V,E>(value);
      Vertex<V, E> previous = vertices.putIfAbsent(key, v);
      return (previous != null) ? previous : v;
   }

   public Vertex<V, E> getVertex(K key)
   {
      return vertices.get(key);  
   }   
   
   public Collection<Vertex<V, E>> getVertices()
   {
      return vertices != null ? Collections.unmodifiableCollection(vertices.values()) : Collections.<Vertex<V,E>>emptySet();
   }

   public static class Vertex<V, E>
   {
      private V value;
      private Set<Edge<V, E>> in;
      private Set<Edge<V, E>> out;

      Vertex(V value)
      {
         this.value = value;
      }

      public V getValue()
      {
         return value;
      }

      private void addIn(Edge<V, E> edge)
      {
         if (in == null)
            in = new HashSet<Edge<V, E>>();
         
         in.add(edge);
      }

      private void addOut(Edge<V, E> edge)
      {
         if (out == null)
            out = new HashSet<Edge<V, E>>();
         
         out.add(edge);
      }

      public Set<Edge<V, E>> getIn()
      {
         return in != null ? Collections.unmodifiableSet(in) : Collections.<Edge<V, E>>emptySet();
      }

      public Set<Edge<V, E>> getOut()
      {
         return out != null ? Collections.unmodifiableSet(out) : Collections.<Edge<V, E>>emptySet();
      }

      public int hashCode()
      {
         return value.hashCode();
      }

      @SuppressWarnings({"unchecked"})
      public boolean equals(Object obj)
      {
         if (obj instanceof Vertex == false)
            return false;

         Vertex<V, E> other = (Vertex<V, E>) obj;
         return value.equals(other.value);
      }
   }

   public static class Edge<V, E>
   {
      private E cost;
      private Vertex<V, E> from;
      private Vertex<V, E> to;

      public static <V, E> void create(E cost, Vertex<V, E> from, Vertex<V, E> to)
      {
         new Edge<V, E>(cost, from, to);
      }

      private Edge(E cost, Vertex<V, E> from, Vertex<V, E> to)
      {
         if (from == null)
            throw new IllegalArgumentException("Null from");
         if (to == null)
            throw new IllegalArgumentException("Null to");

         this.cost = cost;
         this.from = from;
         this.to = to;

         from.addOut(this);
         to.addIn(this);
      }

      public E getCost()
      {
         return cost;
      }

      public Vertex<V, E> getFrom()
      {
         return from;
      }

      public Vertex<V, E> getTo()
      {
         return to;
      }
   }
}