package com.redhat.ceylon.compiler.java.language;

import ceylon.language.ArraySequence;
import ceylon.language.AssertionError;
import ceylon.language.Finished;
import ceylon.language.Iterable;
import ceylon.language.Iterator;
import ceylon.language.Sequential;
import ceylon.language.empty_;

import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

public class SequenceBuilder<Element> implements ReifiedType {

    private final static int MIN_CAPACITY = 5;
    private final static int MAX_CAPACITY = java.lang.Integer.MAX_VALUE;
    
    /** What will become the backing array of the ArraySequence we're building */
    public java.lang.Object[] array;
    
    /** The number of elements (from start) currently in {@link array} */
    int length = 0;
    
    /* Invariant: 0 <= start <= array.length */
    /* Invariant: 0 <= committed <= length */
    /* Invariant: 0 <= start + length <= array.length */
    protected final TypeDescriptor $reifiedElement;
    
    public SequenceBuilder(TypeDescriptor $reifiedElement) {
        this.$reifiedElement = $reifiedElement;
    }
    
    public SequenceBuilder(TypeDescriptor $reifiedElement, 
    		int initialCapacity) {
        this($reifiedElement);
        if (initialCapacity >= 0) {
            resize$priv$(initialCapacity);
        }
    }
    
    /** Ensures the array has at least the given capacity (it may allocate more) */
    private void ensureCapacity$priv$(long capacity) {
        
        if ((array == null && capacity > 0)
                || (array != null && capacity > array.length)) {
            // Always have about 50% more capacity than requested
            long newcapacity = capacity+(capacity>>1);
            if (newcapacity < MIN_CAPACITY) {
                newcapacity = MIN_CAPACITY;
            } else if (newcapacity > MAX_CAPACITY) {
                newcapacity = capacity;
                if (newcapacity > MAX_CAPACITY) {
                    throw new AssertionError("can't allocate array bigger than " + MAX_CAPACITY);
                }
            }
            resize$priv$(newcapacity);
        }
    }
    /** Resizes the array to the given size */
    private void resize$priv$(long newcapacity) {
        java.lang.Object[] newarray = new java.lang.Object[(int)newcapacity];
        if (array != null) {
            System.arraycopy(array, 0, newarray, 0, length);
        }
        array = newarray;
    }
    /** Trims the array so it's just big enough */
    public SequenceBuilder<Element> trim$priv() {
        if (array.length != length) {
            resize$priv$(length);
        }
        return this;
    }
    
    public Sequential<? extends Element> getSequence() {
        if (array==null || length == 0) {
            return (Sequential<? extends Element>)empty_.get_();
        }
        else {
            return new ArraySequence($reifiedElement, 
            		(Element[])array, 0, length, false);
        }
    }
    
    public SequenceBuilder<Element> append(Element element) {
        ensureCapacity$priv$(length+1);
    	array[length] = element;
    	length+=1;
    	return this;
    }
    
    public SequenceBuilder<Element> appendAll(Iterable<? extends Element, ?> elements) {
        if (elements instanceof ArraySequence) {
            ArraySequence<? extends Element> as = (ArraySequence<? extends Element>)elements;
            int size = (int)as.getSize();
            ensureCapacity$priv$(length + size);
            java.lang.Object[] a = as.$getArray$();
            System.arraycopy(a, as.$getFirst$(), array, length, size);
            length += size;
        } else {
        	java.lang.Object elem;
        	int index = length;
        	for (Iterator<? extends Element> iter=elements.iterator(); 
        			!((elem = iter.next()) instanceof Finished);) {
        	    // In general, Iterable.getSize() could cause an iteration 
                // through all the elements, so we can't allocate before the loop 
        	    ensureCapacity$priv$(length + 1);
        	    array[index] = elem;
        	    index++;
        	    length++;
        	}
        }
        
    	return this;
    }
    
    public final SequenceBuilder<Element> appendAll() {
        return this;
    }
    
    public final long getSize() {
        return length;
    }
    
    public final boolean getEmpty() {
        return length == 0;
    }
     
    @Override
    public TypeDescriptor $getType$() {
        return TypeDescriptor.klass(SequenceBuilder.class, 
        		$reifiedElement);
    }
}
