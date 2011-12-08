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

package ceylon.modules.api.util;

import ceylon.language.Quoted;

import java.util.Collections;
import java.util.Iterator;

/**
 * Ceylon to Java helper methods.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public final class CeylonToJava {

    private CeylonToJava() {
    }

    /**
     * Get string from quoted.
     *
     * @param quoted the quoted
     * @return string or null
     */
    public static String toString(Quoted quoted) {
        return (quoted != null) ? quoted.toString() : null;
    }

    /**
     * Get iterable.
     *
     * @param iterable the iterable
     * @return new java iterable
     */
    public static <T> Iterable<T> toIterable(final ceylon.language.Iterable<T> iterable) {
        if (iterable == null)
            return Collections.emptyList();

        return new Iterable<T>() {

            private T head = iterable.getIterator().getHead();
            private ceylon.language.Iterator<? extends T> tail = iterable.getIterator().getTail();

            @Override
            public Iterator<T> iterator() {
                return new Iterator<T>() {
                    @Override
                    public boolean hasNext() {
                        return (head != null);
                    }

                    @Override
                    public T next() {
                        T elt = head;
                        if (tail != null) {
                            head = tail.getHead();
                            tail = tail.getTail();
                        } else {
                            head = null;
                        }
                        return elt;
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }
}
