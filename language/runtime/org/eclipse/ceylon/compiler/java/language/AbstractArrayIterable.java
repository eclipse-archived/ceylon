/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.java.language;

import org.eclipse.ceylon.compiler.java.metadata.Ignore;
import org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor;

import org.eclipse.ceylon.compiler.java.language.AbstractArrayIterable;
import org.eclipse.ceylon.compiler.java.language.AbstractArrayIterator;

import ceylon.language.AssertionError;
import ceylon.language.Boolean;
import ceylon.language.Callable;
import ceylon.language.Iterator;
import ceylon.language.Null;
import ceylon.language.emptyIterator_;
import ceylon.language.impl.BaseIterable;

/**
 * Abstract implementation of {@link ceylon.language.Iterable} 
 * which iterates over an array (it could be an array of 
 * primitives, or an array of references, we don't know).
 *  
 * Various methods (such as {@link #take(long)}, 
 * {@link #skip(long)} and {@link #by(long)} are overridden 
 * so that they iterate over the same array instance.
 *
 * @param <Element> The element type
 * @param <ArrayType> The array type
 */
public abstract class AbstractArrayIterable<Element, ArrayType>
extends BaseIterable<Element, ceylon.language.Null> {
    
    private final TypeDescriptor $reified$Element;
    
    /** The array (could be an array or primitives or references) */
    private final ArrayType array;
    /** The index where iteration starts */
    protected final int start;
    /** The step size of iteration */
    protected final int step;
    /** The number of elements in the iteration */
    protected final int len;

    @Override
    public TypeDescriptor $getType$() {
        return TypeDescriptor.klass(AbstractArrayIterable.class, 
                this.$reified$Element);
    }

    @Ignore
    public AbstractArrayIterable(TypeDescriptor $reified$Element, 
            ArrayType array, int length) {
        this($reified$Element, array, 0, length, 1);
    }
    
    /** 
     * Constructor used internally via 
     * {@link #newInstance(Object, int, int, int)} 
     */
    @Ignore
    protected AbstractArrayIterable(TypeDescriptor $reified$Element, 
            ArrayType array, int start, int len, int step) {
        super($reified$Element, Null.$TypeDescriptor$);
        
        if (start < 0) {
            throw new AssertionError("start must be non-negative");
        }
        if (len < 0) {
            throw new AssertionError("len must be non-negative");
        }
        if (step <= 0) {
            throw new AssertionError("step size must be greater than zero");
        }

        this.array = array;
        this.$reified$Element = $reified$Element;
        this.start = start;
        this.len = len;
        this.step = step;
    }
    /** Factory for creating a new instance */
    protected abstract AbstractArrayIterable<Element, ArrayType> 
    newInstance(ArrayType array, int start, int len, int step);
    
    protected abstract Element get(ArrayType array, int index);

    Element unsafeGet(int index) {
        return get(this.array, this.start + this.step * index);
    }

    @Ignore
    public ArrayType arrayValue() {
        return array;
    }

    @Override
    public boolean getEmpty() {
        return this.len == 0;
    }
    
    @Override
    public Element getFirst() {
        return this.getEmpty() ? 
                null : 
                unsafeGet(0);
    }

    @Override
    public Element getLast() {
        return this.getEmpty() ? 
                null : 
                unsafeGet(this.len - 1);
    }

    @Override
    public AbstractArrayIterable<Element, ArrayType> getRest() {
        return getEmpty() ? 
                this :
                newInstance(this.array, 
                        this.start+this.step, 
                        this.len-1, 
                        this.step);
    }
    
    @Ignore
    @Override
    public long getSize() {
        return this.len;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Iterator<? extends Element> iterator() {
        if (this.getEmpty()) {
            return (Iterator<? extends Element>) 
                    emptyIterator_.get_();
        }
        return new AbstractArrayIterator<Element>($reified$Element, 
                start, len, step) {
            protected Element get(int index) {
                return AbstractArrayIterable.this.get(array, index);
            }
        };
    }

    @Override
    public boolean longerThan(long length) {
        return this.getSize() > length;
    }
    
    @Override
    public boolean shorterThan(long length) {
        return this.getSize() < length;
    }
    
    @Override
    public AbstractArrayIterable<Element, ArrayType> 
    skip(long skip) {
        if (skip <= 0) {
            return this;
        }
        int start = this.start+(int)skip*step;
        int len = this.len-(int)skip;
        if (len<0) len = 0;
        int step = this.step;
        return newInstance(this.array, start, len, step);
    }
    
    @Override
    public AbstractArrayIterable<Element, ArrayType> 
    take(long take) {
        if (take >= this.getSize()) {
            return this;
        }
        return newInstance(this.array, 
                this.start, 
                (int)take, 
                this.step);
    }
    
    @Override
    public AbstractArrayIterable<Element, ArrayType> 
    by(long step) {
        return newInstance(this.array, 
                this.start, 
                (int)((this.len+step-1)/step), //ceiling division
                this.step*(int)step);
    }
    
    @Override
    public java.lang.Object each(
            Callable<? extends java.lang.Object> step) {
        for (int i=0; i<len; i++) {
            step.$call$(unsafeGet(i));
        }
        return null;
    }
    
    @Override
    public long count(
    Callable<? extends Boolean> selecting) {
        // FIXME Very inefficient for primitive types due to boxing
        int count=0;
        for (int i=0; i<len; i++) {
            if (selecting.$call$(unsafeGet(i)).booleanValue()) {
                count++;
            }
        }
        return count;
    }

    @Override
    public boolean any(
    Callable<? extends Boolean> selecting) {
        for (int i=0; i<len; i++) {
            if (selecting.$call$(unsafeGet(i)).booleanValue()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean every(
    Callable<? extends Boolean> selecting) {
        for (int i=0; i<len; i++) {
            if (!selecting.$call$(unsafeGet(i)).booleanValue()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Element find(
    Callable<? extends Boolean> selecting) {
        for (int i=0; i<len; i++) {
            Element elem = unsafeGet(i);
            if (elem!=null 
                    && selecting.$call$(elem)
                        .booleanValue()) {
                return elem;
            }
        }
        return null;
    }
    
    @Override
    public Element findLast(
    Callable<? extends Boolean> selecting) {
        for (int i=len-1; i>=0; i--) {
            Element elem = unsafeGet(i);
            if (elem!=null 
                    && selecting.$call$(elem)
                        .booleanValue()) {
                return elem;
            }
        }
        return null;
    }
    
    @Override
    public <Result> java.lang.Object 
    reduce(@Ignore TypeDescriptor $reifiedResult,
            Callable<? extends Result> accumulating) {
        if (len==0) {
            return null;
        }
        java.lang.Object partial = unsafeGet(0);
        for (int i=1; i<len; i++) {
            partial = accumulating.$call$(partial,
                    unsafeGet(i));
        }
        return partial;
    }

}
