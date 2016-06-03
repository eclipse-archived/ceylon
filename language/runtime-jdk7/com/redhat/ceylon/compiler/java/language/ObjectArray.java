package com.redhat.ceylon.compiler.java.language;

import ceylon.language.Array;
import ceylon.language.AssertionError;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Defaulted;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.ValueType;
import com.redhat.ceylon.compiler.java.metadata.Variance;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

/**
 * A type representing Java object arrays of type 
 * {@code T[]}, where {@link T} may not be a union, 
 * intersection, or bottom type.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
// This type is never actually instantiated, it is always
// replaced by the Java object array type T[].
// 
// The operations which call 
// Util.makeJavaArrayWrapperException() are completely 
// erased to Java array operators, or Util.fillArray() 
// in the case of the constructor if the initial element is 
// specified.
// 
// Only the value type static methods are really invoked.
@Ceylon(major = 8)
@Class
@ValueType
@TypeParameters(@TypeParameter(value="T", 
                variance=Variance.OUT,
                satisfies="ceylon.language::Object"))
public final class ObjectArray<T> implements ReifiedType {
    
    /**
     * Create a new array of the given {@code size}, with
     * all elements initialized to the given {@code element}.
     * 
     * @throws AssertionError if {@link T} is a union, 
     *         intersection, or bottom type
     * @throws NegativeArraySizeException if {@code size}
     *         is negative
     * @param size the size of the array
     * @param element the initial value of the array elements
     */
    public ObjectArray(@Ignore TypeDescriptor $reifiedT, 
            /**
             * The size of the new array.
             */
    		@Name("size") int size, 
            /**
             * The initial value of the array elements.
             */
    		@TypeInfo("T|ceylon.language::Null") 
            @Defaulted @Name("element") 
            T element){
        throw Util.makeJavaArrayWrapperException();
    }

    /**
     * Create a new array of the given {@code size}, with
     * all elements initialized to {@code null}.
     * 
     * @throws AssertionError if {@link T} is a union, 
     *         intersection, or bottom type
     * @throws NegativeArraySizeException if {@code size}
     *         is negative
     * @param size the size of the array
     */
    @Ignore
    public ObjectArray(
            /**
             * The size of the new array.
             */
            @Name("size") int size){
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    // For consistency with the rules for ValueTypes
    public static <T> ObjectArray<T> instance(T[] value){
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
    @TypeInfo(value = "T", uncheckedNull = true) //for consistency with other Java methods
    public T get(@Name("index") int index) {
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public static <T> T get(T[] value, int index) {
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
            @TypeInfo("ceylon.language::Anything") 
            java.lang.Object element) {
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public static <T> void set(
            T[] value,
            int index,
            java.lang.Object element) {
        throw Util.makeJavaArrayWrapperException();
    }

    /**
     * The size of this Java object array.
     */
    @Name("size")
    public final int length = 0;
    
    /**
     * A view of this array as a Ceylon {@code Array<T>}.
     */
    @TypeInfo("ceylon.language::Array<T|ceylon.language::Null>")
    public ceylon.language.Array<T> getArray(){
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public static <T> ceylon.language.Array<T> getArray(T[] array){
        return Array.instance(array);
    }

    /**
     * Efficiently copy a measure of this Java object array 
     * to the given Java object array.
     * 
     * @param destination the array into which to copy the
     *        elements of this array
     * @param sourcePosition the starting position within
     *        this array
     * @param destinationPosition the starting position 
     *        within the {@code destination} array
     * @param length the number of elements to copy
     * @throws ArrayStoreException if an element of this array
     *         can not be stored in the given array 
     */
    public void copyTo(
            @Name("destination") java.lang.Object[] destination, 
            @Name("sourcePosition") @Defaulted int sourcePosition, 
            @Name("destinationPosition") @Defaulted int destinationPosition, 
            @Name("length") @Defaulted int length){
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public static <T> void copyTo(T[] array, 
            java.lang.Object[] destination){
        System.arraycopy(array, 0, destination, 0, array.length);
    }

    @Ignore
    public static <T> void copyTo(T[] array, 
            java.lang.Object[] destination, 
            int sourcePosition){
        System.arraycopy(array, sourcePosition, destination, 
                0, array.length-sourcePosition);
    }

    @Ignore
    public static <T> void copyTo(T[] array, 
            java.lang.Object[] destination, 
            int sourcePosition, int destinationPosition){
        System.arraycopy(array, sourcePosition, destination, 
                destinationPosition, array.length-sourcePosition);
    }

    @Ignore
    public static <T> void copyTo(T[] array, 
            java.lang.Object[] destination, 
            int sourcePosition, int destinationPosition, 
            int length){
        System.arraycopy(array, sourcePosition, destination, 
                destinationPosition, length);
    }

    @Ignore
    public int copyTo$sourcePosition(java.lang.Object[] destination){
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public int copyTo$destinationPosition(java.lang.Object[] destination, 
            int sourcePosition){
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public int copyTo$length(java.lang.Object[] destination, 
            int sourcePosition, int destinationPosition){
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public void copyTo(java.lang.Object[] destination){
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public void copyTo(java.lang.Object[] destination, 
                       int sourcePosition){
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public void copyTo(java.lang.Object[] destination, 
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
    public static <T> boolean equals(T[] value, java.lang.Object that) {
        return value.equals(that);
    }

    @Override
    public int hashCode() {
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public static <T> int hashCode(T[] value) {
        return value.hashCode();
    }

    @Override
    public java.lang.String toString() {
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public static <T> java.lang.String toString(T[] value) {
        return value.toString();
    }
    
    /**
     * A clone of this Java object array.
     */
    @TypeInfo("java.lang::ObjectArray<T>")
    public Object[] $clone() {
        throw Util.makeJavaArrayWrapperException();
    }
    
    @Ignore
    public static <T> T[] $clone(T[] value) {
        return value.clone();
    }
    
    /**
     * A Ceylon {@code Iterable<T?>} containing the elements 
     * of this Java object array.
     */
    @TypeInfo("ceylon.language::Iterable<T|ceylon.language::Null,ceylon.language::Null>")
    public ceylon.language.Iterable<T,ceylon.language.Null> getIterable() {
        throw Util.makeJavaArrayWrapperException();
    }
    
    @Ignore
    public static <T> ceylon.language.Iterable<T,ceylon.language.Null> getIterable(T[] value) {
        return new ObjectArrayIterable<T>(value, value.length);
    }
}
