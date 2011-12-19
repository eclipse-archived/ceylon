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

import ceylon.language.Boolean;
import ceylon.language.Iterable;
import ceylon.language.Iterator;
import ceylon.language.Quoted;

import java.util.Arrays;
import java.util.List;

/**
 * Java to Ceylon helper methods.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public final class JavaToCeylon {

    private JavaToCeylon() {
    }

    /**
     * Get string from string.
     *
     * @param string string
     * @return string or null
     */
    public static ceylon.language.String toString(String string) {
        return (string != null) ? ceylon.language.String.instance(string) : null;
    }

    /**
     * Get string from string.
     *
     * @param string string
     * @return quoted or null
     */
    public static ceylon.language.Quoted toQuoted(String string) {
        return (string != null) ? Quoted.instance(string) : null;
    }

    /**
     * To Ceylon boolean.
     *
     * @param b boolean value
     * @return celyon boolean
     */
    public static Boolean toBoolean(boolean b) {
        return Boolean.instance(b);
    }

    /**
     * To ceylon iterable.
     *
     * @param elts the elts
     * @return iterable
     */
    public static <T> ceylon.language.Iterable<T> toIterable(final T... elts) {
        if (elts == null)
            throw new IllegalArgumentException("Null elts");

        return new Iterable<T>() {
            @Override
            public boolean getEmpty() {
                return (elts.length == 0);
            }

            @Override
            public Iterator<? extends T> getIterator() {
                return toIterator(elts);
            }
        };
    }

    /**
     * To ceylon iterable.
     *
     * @param elts the elts
     * @return iterable
     */
    public static <T> ceylon.language.Iterator<T> toIterator(T... elts) {
        if (elts == null)
            throw new IllegalArgumentException("Null elts");

        final List<T> list = Arrays.asList(elts);
        return toIterator(list);
    }

    /**
     * To ceylon iterable.
     *
     * @param list the elts
     * @return iterable
     */
    private static <T> ceylon.language.Iterator<T> toIterator(final List<T> list) {
        return new Iterator<T>() {
            @Override
            public T getHead() {
                return (list.isEmpty()) ? null : list.get(0);
            }

            @Override
            public Iterator<? extends T> getTail() {
                if (list.size() <= 1)
                    return null;
                //noinspection unchecked                
                return (Iterator<? extends T>) toIterator(list.subList(1, list.size()).toArray());
            }
        };
    }
}
