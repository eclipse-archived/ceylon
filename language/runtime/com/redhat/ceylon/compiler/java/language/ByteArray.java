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
 * {@code byte[]}.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
/*
 * THIS IS A GENERATED FILE - DO NOT EDIT 
 */
//This type is never actually instantiated, it is always
//replaced by the Java object array type byte[].
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
public final class ByteArray implements ReifiedType {
    
    @Ignore
    public final static TypeDescriptor $TypeDescriptor$ = 
    TypeDescriptor.klass(ByteArray.class);
    
    /**
     * Create a new array of the given {@code size}, with
     * all elements initialized to the given {@code element}.
     * 
     * @throws NegativeArraySizeException if {@code size}
     *         is negative
     * @param size the size of the array
     * @param element the initial value of the array elements
     */
    public ByteArray(
            /**
             * The size of the new array.
             */
            @Name("size") int size, 
            /**
             * The initial value of the array elements.
             */
            @TypeInfo("ceylon.language::Byte") 
            @Defaulted @Name("element") 
            byte element){
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
    public ByteArray(
            /**
             * The size of the new array.
             */
            @Name("size") int size){
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    // For consistency with the rules for ValueTypes
    public static ByteArray instance(byte[] value){
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
    public byte get(@Name("index") int index) {
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public static byte get(byte[] value, int index) {
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
            byte element) {
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public static void set(byte[] value, int index, 
            byte element) {
        throw Util.makeJavaArrayWrapperException();
    }
    
    /**
     * The size of this Java primitive array.
     */
    @Name("size")
    public final int length = 0;

    /**
     * A view of this array as a Ceylon
     * {@code Array<java.lang::Byte>}, where
     * {@code java.lang::Byte} is the Java wrapper type
     * corresponding to the primitive type
     * {@code byte} of elements of this Java
     * array.
     */
    @TypeInfo("ceylon.language::Array<java.lang::Byte>")
    public ceylon.language.Array<java.lang.Byte> getArray(){
        throw Util.makeJavaArrayWrapperException();
    }
    
    @Ignore
    public static ceylon.language.Array<java.lang.Byte> 
    getArray(byte[] array){
        return Array.instance(array);
    }

    /**
     * A view of this array as a Ceylon
     * {@code Array<ceylon.language::Byte>}
     * where {@code ceylon.language::Byte}
     * is the Ceylon type corresponding to the
     * primitive type {@code byte}
     * of elements of this Java array.
     */
    @TypeInfo("ceylon.language::Array<ceylon.language::Byte>")
    public ceylon.language.Array<ceylon.language.Byte> 
    getByteArray(){
        throw Util.makeJavaArrayWrapperException();
    }
    
    @Ignore
    public static ceylon.language.Array<ceylon.language.Byte> 
    getByteArray(byte[] array){
        return Array.instanceForBytes(array);
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
    public void copyTo(@Name("destination") byte[] destination, 
                       @Name("sourcePosition") @Defaulted int sourcePosition, 
                       @Name("destinationPosition") @Defaulted int destinationPosition, 
                       @Name("length") @Defaulted int length){
        throw Util.makeJavaArrayWrapperException();
    }
    
    @Ignore
    public static void copyTo(byte[] array, 
            byte[] destination){
        System.arraycopy(array, 0, destination, 0, array.length);
    }

    @Ignore
    public static void copyTo(byte[] array, 
            byte[] destination, 
            int sourcePosition){
        System.arraycopy(array, sourcePosition, destination, 
                0, array.length-sourcePosition);
    }

    @Ignore
    public static void copyTo(byte[] array, 
            byte[] destination, 
            int sourcePosition, int destinationPosition){
        System.arraycopy(array, sourcePosition, destination, 
                destinationPosition, array.length-sourcePosition);
    }

    @Ignore
    public static void copyTo(byte[] array, 
            byte[] destination, 
            int sourcePosition, int destinationPosition, 
            int length){
        System.arraycopy(array, sourcePosition, destination, 
                destinationPosition, length);
    }

    @Ignore
    public int copyTo$sourcePosition(byte[] destination){
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public int copyTo$destinationPosition(byte[] destination, 
            int sourcePosition){
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public int copyTo$length(byte[] destination, 
            int sourcePosition, int destinationPosition){
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public void copyTo(byte[] destination){
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public void copyTo(byte[] destination, 
                       int sourcePosition){
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public void copyTo(byte[] destination, 
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
    public static boolean equals(byte[] value, 
            java.lang.Object that) {
        return value.equals(that);
    }

    @Override
    public int hashCode() {
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public static int hashCode(byte[] value) {
        return value.hashCode();
    }

    @Override
    public java.lang.String toString() {
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public static java.lang.String toString(byte[] value) {
        return value.toString();
    }
    
    /**
     * A clone of this primitive Java array.
     */
    public byte[] $clone() {
        throw Util.makeJavaArrayWrapperException();
    }
    
    @Ignore
    public static byte[] $clone(byte[] value) {
        return value.clone();
    }
    
    /**
     * A Ceylon {@code Iterable<ceylon.language.Byte>} containing the 
     * elements of this primitive Java array.
     */
    @TypeInfo("ceylon.language::Iterable<ceylon.language::Byte,ceylon.language::Null>")
    public ceylon.language.Iterable<ceylon.language.Byte,ceylon.language.Null> getIterable() {
        throw Util.makeJavaArrayWrapperException();
    }
    
    @Ignore
    public static ceylon.language.Iterable<ceylon.language.Byte,ceylon.language.Null> getIterable(byte[] value) {
        return new ByteArrayIterable(value);
    }
}

/* Implement Iterable */
class ByteArrayIterable 
extends BaseIterable<ceylon.language.Byte, ceylon.language.Null> {
    
    /** The array over which we iterate */
    private final byte[] array;
    /** The index where iteration starts */
    private final int start;
    /** The step size of iteration */
    private final int step;
    /** The index one beyond where iteration ends */
    private final int end;
    
    @Ignore
    public ByteArrayIterable(byte[] array) {
        this(array, 0, array.length, 1);
    }
    
    @Ignore
    private ByteArrayIterable(byte[] array, 
            int start, int end, int step) {
        super(ceylon.language.Byte.$TypeDescriptor$, Null.$TypeDescriptor$);
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
            if (item instanceof ceylon.language.Byte) {
                for (int ii = this.start; ii < this.end; ii+=this.step) {
                    if (array[ii] == ((ceylon.language.Byte)item).byteValue()) {
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
            if (item instanceof ceylon.language.Byte) {
                for (int ii = this.start; ii < this.end; ii+=this.step) {
                    if (array[ii] == ((ceylon.language.Byte)item).byteValue()) {
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
            if (arg0.$call$(ceylon.language.Byte.instance(array[ii])).booleanValue()) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean contains(Object item) {
        for (int ii = this.start; ii < this.end; ii+=this.step) {
            if (item instanceof ceylon.language.Byte 
                    && array[ii] == ((ceylon.language.Byte)item).byteValue()) {
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
    public Iterable<? extends ceylon.language.Byte, ? extends Object> getCoalesced() {
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
    public ceylon.language.Byte getFirst() {
        return this.getEmpty() ? null : ceylon.language.Byte.instance(this.array[this.start]);
    }
    
    
    @Override
    public ceylon.language.Byte getLast() {
        return this.getEmpty() ? null : ceylon.language.Byte.instance(this.array[this.end-1]);
    }
    
    @Override
    public ByteArrayIterable getRest() {
        return new ByteArrayIterable(this.array, this.start+1, this.end, this.step);
    }
    
    @Override
    public Sequential<? extends ceylon.language.Byte> sequence() {
        // Note: Sequential is immutable, and we don't know where the array
        // came from, so however we create the sequence we must take a copy
        //TODO: use a more efficient imple, like in List.sequence()
        return super.sequence();
    }
    
    @Override
    public Iterator<? extends ceylon.language.Byte> iterator() {
        if (this.getEmpty()) {
            return (Iterator)ceylon.language.emptyIterator_.get_();
        }
        return new Iterator<ceylon.language.Byte>() {
            
            private int index = ByteArrayIterable.this.start;
            
            @Override
            public Object next() {
                if (index < ByteArrayIterable.this.end) {
                    ceylon.language.Byte result = ceylon.language.Byte.instance(ByteArrayIterable.this.array[index]);
                    index += ByteArrayIterable.this.step;
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
    public ByteArrayIterable by(long step) {
        return new ByteArrayIterable(this.array, 
                this.start, 
                this.end, 
                com.redhat.ceylon.compiler.java.Util.toInt(this.step*step));
    }
    
    @Override
    public ByteArrayIterable skip(long skip) {
        if (skip <= 0) {
            return this;
        }
        return new ByteArrayIterable(this.array, 
                com.redhat.ceylon.compiler.java.Util.toInt(this.start+skip*this.step), 
                this.end, 
                this.step);
    }
    
    @Override
    public ByteArrayIterable take(long take) {
        if (take >= this.getSize()) {
            return this;
        }
        return new ByteArrayIterable(this.array, 
                this.start, 
                com.redhat.ceylon.compiler.java.Util.toInt(take*this.step+1), 
                this.step);
    }
    
}
