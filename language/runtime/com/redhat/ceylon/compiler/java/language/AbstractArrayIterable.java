package com.redhat.ceylon.compiler.java.language;

import ceylon.language.AssertionError;
import ceylon.language.Iterator;
import ceylon.language.Null;
import ceylon.language.emptyIterator_;
import ceylon.language.impl.BaseIterable;

import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

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
                get(this.array, this.start);
    }

    @Override
    public Element getLast() {
        return this.getEmpty() ? 
                null : 
                get(this.array, this.start+this.step*this.len-1);
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
        return Math.max(0, this.len);
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
        return newInstance(this.array, 
                this.start+(int)skip*this.step, 
                this.len-(int)skip, 
                this.step);
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

}
