package com.redhat.ceylon.compiler.java.language;

import ceylon.language.Array;

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
 * <code>T[]</code>.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
// This type is never instantiated, it is completely erased 
// to <code>T[]</code>.
// 
// The {@link #get(int)}, {@link #set(int,T)}, {@link #length size} 
// methods and the constructor are also completely erased to 
// Java array operators, or {@link Util#fillArray(T[],T)} in 
// the case of the constructor if the initial element is 
// specified.
// 
// Only the value type static methods are really invoked.
@Ceylon(major = 8)
@Class
@ValueType
@TypeParameters(@TypeParameter(value="T", variance=Variance.OUT,
                satisfies="ceylon.language::Object"))
public final class ObjectArray<T> implements ReifiedType {
    
    public ObjectArray(@Ignore TypeDescriptor $reifiedT, 
            /**
             * The size of the array.
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

    @Ignore
    public ObjectArray(
            /**
             * The size of the array.
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
     * Get the element with the given {@link index}.
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
     * Set the element with the given {@link index} to the
     * given {@link element} value.
     * @throws ArrayStoreException if the given element can
     *         not be stored in the array. 
     */
    public void set(
            @Name("index")
            int index,
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
     * A view of this array as a Ceylon <code>Array&lt;T?&gt;</code>.
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
     * A Ceylon <code>Iterable&lt;T?&gt;<code> containing 
     * the elements of this Java object array.
     */
    @TypeInfo("ceylon.language::Iterable<T|ceylon.language::Null,ceylon.language::Null>")
    public ObjectArrayIterable<T> getIterable() {
        throw Util.makeJavaArrayWrapperException();
    }
    
    @Ignore
    public static <T> ObjectArrayIterable<T> getIterable(T[] value) {
        return new ObjectArrayIterable<T>(value, value.length);
    }
    
    /* Implement Iterable */

    public static final class ObjectArrayIterable<T> 
    extends AbstractArrayIterable<T, Object[]> {

        public ObjectArrayIterable(Object[] array, int start, int len, int step) {
            super(TypeDescriptor.klass(array.getClass().getComponentType()), 
                    array, start, len, step);
        }

        public ObjectArrayIterable(Object[] array, int length) {
            super(TypeDescriptor.klass(array.getClass().getComponentType()),
                    array, length);
        }
        
        public ObjectArrayIterable(TypeDescriptor reifiedElement, Object[] array) {
            super(reifiedElement, array, array.length);
        }

        @Override
        protected ObjectArrayIterable<T> newInstance(Object[] array, int start,
                int len, int step) {
            return new ObjectArrayIterable<T>(array, start, len, step);
        }

        @Override
        protected T get(Object[] array, int index) {
            return (T)array[index];
        }
        
        @Override
        public ObjectArrayIterable<T> take(long take) {
            return (ObjectArrayIterable<T>)super.take(take);
        }
        
        @Override
        public ObjectArrayIterable<T> skip(long take) {
            return (ObjectArrayIterable<T>)super.skip(take);
        }
        
        @Override
        public ObjectArrayIterable<T> by(long take) {
            return (ObjectArrayIterable<T>)super.by(take);
        }
    }

}
