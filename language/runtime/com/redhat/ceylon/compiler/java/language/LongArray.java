package com.redhat.ceylon.compiler.java.language;

import javax.annotation.Generated;

import ceylon.language.Array;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Defaulted;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.ValueType;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

import ceylon.language.AssertionError;
import ceylon.language.Callable;
import ceylon.language.Category$impl;
import ceylon.language.Comparison;
import ceylon.language.Entry;
import ceylon.language.Finished;
import ceylon.language.Iterable;
import ceylon.language.Iterable$impl;
import ceylon.language.Iterator;
import ceylon.language.List;
import ceylon.language.Null;
import ceylon.language.Sequential;
import ceylon.language.empty_;
import ceylon.language.finished_;
import ceylon.language.impl.BaseIterable;

/*
 * THIS IS A GENERATED FILE - DO NOT EDIT 
 */
/**
 * A type representing Java primitive arrays of type 
 * {@code long[]}.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
/*
 * THIS IS A GENERATED FILE - DO NOT EDIT 
 */
//This type is never actually instantiated, it is always
//replaced by the Java object array type long[].
//
//The operations which call 
//Util.makeJavaArrayWrapperException() are completely 
//erased to Java array operators, or Util.fillArray() 
//in the case of the constructor if the initial element is 
//specified.
//
//Only the value type static methods are really invoked.
@Ceylon(major = 8)
@Class
@ValueType
@Generated(value="ant")
public final class LongArray implements ReifiedType {
    
    @Ignore
    public final static TypeDescriptor $TypeDescriptor$ = 
    TypeDescriptor.klass(LongArray.class);
    
    /**
     * Create a new array of the given {@code size}, with
     * all elements initialized to the given {@code element}.
     * 
     * @throws NegativeArraySizeException if {@code size}
     *         is negative
     * @param size the size of the array
     * @param element the initial value of the array elements
     */
    public LongArray(
            /**
             * The size of the new array.
             */
            @Name("size") int size, 
            /**
             * The initial value of the array elements.
             */
            @TypeInfo("ceylon.language::Integer") 
            @Defaulted @Name("element") 
            long element){
        throw Util.makeJavaArrayWrapperException();
    }

    /**
     * Create a new array of the given {@code size}, with
     * all elements initialized to {@code null}.
     * 
     * @throws NegativeArraySizeException if {@code size}
     *         is negative
     * @param size the size of the array
     */
    @Ignore
    public LongArray(
            /**
             * The size of the new array.
             */
            @Name("size") int size){
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    // For consistency with the rules for ValueTypes
    public static LongArray instance(long[] value){
        throw Util.makeJavaArrayWrapperException();
    }

    /**
     * Get the element with the given {@code index}.
     * 
     * @param index the index within this array
     * @return the element of this array at the given 
     *         {@code index}
     * @throws ArrayIndexOutOfBoundsException if the index
     *         does not refer to an element of this array
     */
    public long get(@Name("index") int index) {
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public static long get(long[] value, int index) {
        throw Util.makeJavaArrayWrapperException();
    }

    /**
     * Set the element with the given {@code index} to the
     * given {@code element} value.
     * 
     * @param index the index within this array
     * @param element the new element value
     * @throws ArrayIndexOutOfBoundsException if the index
     *         does not refer to an element of this array
     * @throws ArrayStoreException if the given element can
     *         not be stored in the array. 
     */
    public void set(
            /**
             * The index within the array.
             */
            @Name("index") 
            int index, 
            /**
             * The new value of the array element.
             */
            @Name("element") 
            long element) {
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public static void set(long[] value, int index, 
            long element) {
        throw Util.makeJavaArrayWrapperException();
    }
    
    /**
     * The size of this Java primitive array.
     */
    @Name("size")
    public final int length = 0;

    /**
     * A view of this array as a Ceylon
     * {@code Array<java.lang::Long>}, where
     * {@code java.lang::Long} is the Java wrapper type
     * corresponding to the primitive type
     * {@code long} of elements of this Java
     * array.
     */
    @TypeInfo("ceylon.language::Array<java.lang::Long>")
    public ceylon.language.Array<java.lang.Long> getArray(){
        throw Util.makeJavaArrayWrapperException();
    }
    
    @Ignore
    public static ceylon.language.Array<java.lang.Long> 
    getArray(long[] array){
        return Array.instance(array);
    }

    /**
     * A view of this array as a Ceylon
     * {@code Array<ceylon.language::Integer>}
     * where {@code ceylon.language::Integer}
     * is the Ceylon type corresponding to the
     * primitive type {@code long}
     * of elements of this Java array.
     */
    @TypeInfo("ceylon.language::Array<ceylon.language::Integer>")
    public ceylon.language.Array<ceylon.language.Integer> 
    getIntegerArray(){
        throw Util.makeJavaArrayWrapperException();
    }
    
    @Ignore
    public static ceylon.language.Array<ceylon.language.Integer> 
    getIntegerArray(long[] array){
        return Array.instanceForIntegers(array);
    }
    
    /**
     * Efficiently copy a measure of this Java primitive 
     * array to the given Java primitive array.
     * 
     * @param destination the array into which to copy the
     *        elements of this array
     * @param sourcePosition the starting position within
     *        this array
     * @param destinationPosition the starting position 
     *        within the {@code destination} array
     * @param length the number of elements to copy
     */
    public void copyTo(@Name("destination") long[] destination, 
                       @Name("sourcePosition") @Defaulted int sourcePosition, 
                       @Name("destinationPosition") @Defaulted int destinationPosition, 
                       @Name("length") @Defaulted int length){
        throw Util.makeJavaArrayWrapperException();
    }
    
    @Ignore
    public static void copyTo(long[] array, 
            long[] destination){
        System.arraycopy(array, 0, destination, 0, array.length);
    }

    @Ignore
    public static void copyTo(long[] array, 
            long[] destination, 
            int sourcePosition){
        System.arraycopy(array, sourcePosition, destination, 
                0, array.length-sourcePosition);
    }

    @Ignore
    public static void copyTo(long[] array, 
            long[] destination, 
            int sourcePosition, int destinationPosition){
        System.arraycopy(array, sourcePosition, destination, 
                destinationPosition, array.length-sourcePosition);
    }

    @Ignore
    public static void copyTo(long[] array, 
            long[] destination, 
            int sourcePosition, int destinationPosition, 
            int length){
        System.arraycopy(array, sourcePosition, destination, 
                destinationPosition, length);
    }

    @Ignore
    public int copyTo$sourcePosition(long[] destination){
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public int copyTo$destinationPosition(long[] destination, 
            int sourcePosition){
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public int copyTo$length(long[] destination, 
            int sourcePosition, int destinationPosition){
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public void copyTo(long[] destination){
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public void copyTo(long[] destination, 
                       int sourcePosition){
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public void copyTo(long[] destination, 
                       int sourcePosition, 
                       int destinationPosition){
        throw Util.makeJavaArrayWrapperException();
    }
    
    @Ignore
    @Override
    public TypeDescriptor $getType$() {
        throw Util.makeJavaArrayWrapperException();
    }

    @Override
    public boolean equals(@Name("that") java.lang.Object that) {
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public static boolean equals(long[] value, 
            java.lang.Object that) {
        return value.equals(that);
    }

    @Override
    public int hashCode() {
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public static int hashCode(long[] value) {
        return value.hashCode();
    }

    @Override
    public java.lang.String toString() {
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public static java.lang.String toString(long[] value) {
        return value.toString();
    }
    
    /**
     * A clone of this primitive Java array.
     */
    public long[] $clone() {
        throw Util.makeJavaArrayWrapperException();
    }
    
    @Ignore
    public static long[] $clone(long[] value) {
        return value.clone();
    }
    
    /**
     * A Ceylon {@code Iterable<ceylon.language.Integer>} containing the 
     * elements of this primitive Java array.
     */
    @TypeInfo("ceylon.language::Iterable<ceylon.language::Integer,ceylon.language::Null>")
    public ceylon.language.Iterable<ceylon.language.Integer,ceylon.language.Null> getIterable() {
        throw Util.makeJavaArrayWrapperException();
    }
    
    @Ignore
    public static ceylon.language.Iterable<ceylon.language.Integer,ceylon.language.Null> getIterable(long[] value) {
        return new LongArrayIterable(value);
    }
}

/* Implement Iterable */
class LongArrayIterable 
extends BaseIterable<ceylon.language.Integer, ceylon.language.Null> {
    
    /** The array over which we iterate */
    private final long[] array;
    /** The index where iteration starts */
    private final int start;
    /** The step size of iteration */
    private final int step;
    /** The index one beyond where iteration ends */
    private final int end;
    
    @Ignore
    public LongArrayIterable(long[] array) {
        this(array, 0, array.length, 1);
    }
    
    @Ignore
    private LongArrayIterable(long[] array, 
            int start, int end, int step) {
        super(ceylon.language.Integer.$TypeDescriptor$, Null.$TypeDescriptor$);
        if (start < 0) {
            throw new ceylon.language.AssertionError("start must be positive");
        }
        if (end < 0) {
            throw new ceylon.language.AssertionError("end must be positive");
        }
        if (step <= 0) {
            throw new ceylon.language.AssertionError("step size must be greater than zero");
        }
        
        this.array = array;
        this.start = start;
        this.end = end;
        this.step = step;
    }
    
    @Override
    public boolean containsAny(Iterable<? extends Object, ? extends Object> arg0) {
        Iterator<? extends Object> iter = arg0.iterator();
        Object item;
        while (!((item = iter.next()) instanceof Finished)) {
            if (item instanceof ceylon.language.Integer) {
                for (int ii = this.start; ii < this.end; ii+=this.step) {
                    if (array[ii] == ((ceylon.language.Integer)item).longValue()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    @Override
    public boolean containsEvery(
            Iterable<? extends Object, ? extends Object> arg0) {
        Iterator<? extends Object> iter = arg0.iterator();
        Object item;
        OUTER: while (!((item = iter.next()) instanceof Finished)) {
            if (item instanceof ceylon.language.Integer) {
                for (int ii = this.start; ii < this.end; ii+=this.step) {
                    if (array[ii] == ((ceylon.language.Integer)item).longValue()) {
                        continue OUTER;
                    }
                }
            } 
            return false;
        }
        return true;
    }
    
    @Override
    public boolean any(Callable<? extends ceylon.language.Boolean> arg0) {
        for (int ii=this.start; ii < this.end; ii+=this.step) {
            if (arg0.$call$(ceylon.language.Integer.instance(array[ii])).booleanValue()) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean contains(Object item) {
        for (int ii = this.start; ii < this.end; ii+=this.step) {
            if (item instanceof ceylon.language.Integer 
                    && array[ii] == ((ceylon.language.Integer)item).longValue()) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public <Default> Iterable<? extends Object, ? extends Null> defaultNullElements(
            @Ignore
            TypeDescriptor $reified$Default, 
            Default defaultValue) {
        return this;
    }
    
    @Override
    public Iterable<? extends ceylon.language.Integer, ? extends Object> getCoalesced() {
        return this;
    }
    
    @Override
    public boolean getEmpty() {
        return this.end <= this.start;
    }
    
    @Override
    public long getSize() {
        return java.lang.Math.max(0, (this.end-this.start+this.step-1)/this.step);
    }
    
    @Override
    public ceylon.language.Integer getFirst() {
        return this.getEmpty() ? null : ceylon.language.Integer.instance(this.array[this.start]);
    }
    
    
    @Override
    public ceylon.language.Integer getLast() {
        return this.getEmpty() ? null : ceylon.language.Integer.instance(this.array[this.end-1]);
    }
    
    @Override
    public LongArrayIterable getRest() {
        return new LongArrayIterable(this.array, this.start+1, this.end, this.step);
    }
    
    @Override
    public Sequential<? extends ceylon.language.Integer> sequence() {
        // Note: Sequential is immutable, and we don't know where the array
        // came from, so however we create the sequence we must take a copy
        //TODO: use a more efficient imple, like in List.sequence()
        return super.sequence();
    }
    
    @Override
    public Iterator<? extends ceylon.language.Integer> iterator() {
        if (this.getEmpty()) {
            return (Iterator)ceylon.language.emptyIterator_.get_();
        }
        return new Iterator<ceylon.language.Integer>() {
            
            private int index = LongArrayIterable.this.start;
            
            @Override
            public Object next() {
                if (index < LongArrayIterable.this.end) {
                    ceylon.language.Integer result = ceylon.language.Integer.instance(LongArrayIterable.this.array[index]);
                    index += LongArrayIterable.this.step;
                    return result;
                } else {
                    return finished_.get_();
                }
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
    public LongArrayIterable by(long step) {
        return new LongArrayIterable(this.array, 
                this.start, 
                this.end, 
                com.redhat.ceylon.compiler.java.Util.toInt(this.step*step));
    }
    
    @Override
    public LongArrayIterable skip(long skip) {
        if (skip <= 0) {
            return this;
        }
        return new LongArrayIterable(this.array, 
                com.redhat.ceylon.compiler.java.Util.toInt(this.start+skip*this.step), 
                this.end, 
                this.step);
    }
    
    @Override
    public LongArrayIterable take(long take) {
        if (take >= this.getSize()) {
            return this;
        }
        return new LongArrayIterable(this.array, 
                this.start, 
                com.redhat.ceylon.compiler.java.Util.toInt(this.start+take*this.step), 
                this.step);
    }
    
}
