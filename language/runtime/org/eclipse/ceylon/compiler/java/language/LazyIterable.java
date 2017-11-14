/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.java.language;

import ceylon.language.Iterable;
import ceylon.language.Iterator;
import ceylon.language.finished_;
import ceylon.language.impl.BaseIterable;
import ceylon.language.impl.BaseIterator;

import org.eclipse.ceylon.compiler.java.metadata.Ignore;
import org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor;

import org.eclipse.ceylon.compiler.java.language.ConstantIterable;
import org.eclipse.ceylon.compiler.java.language.LazyIterable;

/**
 * Base class for the iterables used for enumerated stream
 * instantiation and the sequenced argument of a named 
 * argument invocation, where the listed expressions in the 
 * stream are evaluated lazily.
 * 
 * @see ConstantIterable 
 */
public abstract class LazyIterable<Element, Absent> 
extends BaseIterable<Element, Absent>{
    
    private final class LazyIterator 
    extends BaseIterator<Element> {
        
        int index = 0;
        Iterator<? extends Element> rest = null;
        
        private LazyIterator() {
            super($reifiedElement);
        }
        
        @SuppressWarnings({ "unchecked" })
        public Iterator<? extends Element> flatten() {
            if ($spread && index == $numExpressions - 1) {
                Iterable<? extends Element, ?> iterable =
                        (Iterable<? extends Element,?>) $evaluate$(index);
                return iterable.iterator();
            }
            else {
                return null;
            }
        }

        @SuppressWarnings({ "unchecked" })
        @Override
        public Object next() {
            if (index < $numExpressions) {
                Object result = $evaluate$(index++);
                if ($spread && index == $numExpressions) {
                    Iterable<? extends Element, ?> iterable = 
                            (Iterable<? extends Element,?>) result;
                    rest = iterable.iterator();
                } else {
                    return result;
                }
            }

            if (rest == null) {
                return finished_.get_();
            }

            while (rest instanceof LazyIterable.LazyIterator) {
                LazyIterable<Element, ?>.LazyIterator lazyRest =
                        (LazyIterable<Element, ?>.LazyIterator) rest;
                Iterator<? extends Element> replacement = lazyRest.flatten();
                if (replacement != null) {
                    rest = replacement;
                }
                else {
                    break;
                }
            }

            return rest.next();
        }
        
        @Override
        public String toString() {
            return LazyIterable.this.toString() + ".iterator()";
        }
    }
    
    private final TypeDescriptor $reifiedElement;
    private final int $numExpressions;
    private final boolean $spread;
    
    public LazyIterable(
            @Ignore TypeDescriptor $reifiedElement, 
            @Ignore TypeDescriptor $reifiedAbsent, 
            int $numMethods, 
            boolean $spread) {
        super($reifiedElement, $reifiedAbsent);
        this.$reifiedElement = $reifiedElement;
        this.$numExpressions = $numMethods;
        this.$spread = $spread;
    }
    
    @Override
    public Iterator<? extends Element> iterator() {
        return new LazyIterator();
    }
    
    /**
     * Evaluate the expression at the given index.
     */
    @Ignore
    protected abstract Object $evaluate$(int $index$);
    
    @Override
    public boolean getEmpty() {
        if ($numExpressions == 0) {
            return true;
        }
        // we have at least one expression, but is it spread?
        else if ($spread) {
            // do we have at least one non-spread expression?
            return $numExpressions > 1 ? 
                    false : super.getEmpty(); // with spread we just don't know
        }
        else{
            // we have at least one non-spread expression
            return false;
        }
    }
    
    @Override
    public long getSize() {
        if ($spread) {
            return super.getSize(); // too hazardous
        }
        // safe
        return $numExpressions;
    }
    
    @Override
    public boolean longerThan(long length) {
        if ($spread && length >= $numExpressions) {
            return super.longerThan(length);
        }
        else {
            return $numExpressions > length;
        }
    }
    
    @Override
    public boolean shorterThan(long length) {
        if ($spread && length >= $numExpressions) {
            return super.shorterThan(length);
        }
        else {
            return $numExpressions < length;
        }
    }
    
    protected Object writeReplace() {
        return sequence();
    }
}
