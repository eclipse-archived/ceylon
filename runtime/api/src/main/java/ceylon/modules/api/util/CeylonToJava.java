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
        if (iterable == null || iterable.getEmpty())
            return Collections.emptyList();

        final ceylon.language.Iterator<? extends T> iterator = iterable.getIterator();
        return new Iterable<T>() {

            private T head = iterator.getHead();
            private ceylon.language.Iterator<? extends T> tail = iterator.getTail();

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
