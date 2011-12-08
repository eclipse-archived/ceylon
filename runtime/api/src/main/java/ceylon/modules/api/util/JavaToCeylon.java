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
