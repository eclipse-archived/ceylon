package com.redhat.ceylon.compiler.java.language;

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

/**
 * A Ceylon schema for a Java <code>T[]</code>
 *
 * This type is never instantiated, it is completely erased to 
 * <code>T[]</code>.
 * 
 * The {@link #get(int)}, {@link #set(int,T)}, {@link #length size} 
 * methods and the constructor are also completely erased to Java 
 * array operators, or {@link Util#fillArray(T[],T)} in the case of 
 * the constructor if the initial element is specified.
 * 
 * Only the value type static methods are really invoked.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
@Ceylon(major = 7)
@Class
@ValueType
public final class ObjectArray<T> implements ReifiedType {
    
    public ObjectArray(@Ignore TypeDescriptor $reifiedT, 
    		@Name("size") int size, 
    		@TypeInfo("T|ceylon.language::Null") @Defaulted 
    		@Name("element") T element){
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public ObjectArray(@Name("size") int size){
        throw Util.makeJavaArrayWrapperException();
    }

    @TypeInfo(value = "T", uncheckedNull = true) //for consistency with other Java methods
    public T get(
            @Name("index")
            int index) {
        throw Util.makeJavaArrayWrapperException();
    }

    public void set(
            @Name("index")
            int index,
            @Name("element")
            @TypeInfo("T|ceylon.language::Null") 
            T element) {
        throw Util.makeJavaArrayWrapperException();
    }

    @Name("size")
    public final int length = 0;
    
    @Ignore
    public static <T> ceylon.language.Array<T> getArray(T[] array){
        return Array.instance(array);
    }

    @TypeInfo("ceylon.language::Array<T|ceylon.language::Null>")
    public ceylon.language.Array<T> getArray(){
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public static <T> void copyTo(T[] array, T[] destination){
        System.arraycopy(array, 0, destination, 0, array.length);
    }

    @Ignore
    public static <T> void copyTo(T[] array, T[] destination, int sourcePosition){
        System.arraycopy(array, sourcePosition, destination, 0, array.length-sourcePosition);
    }

    @Ignore
    public static <T> void copyTo(T[] array, T[] destination, int sourcePosition, int destinationPosition){
        System.arraycopy(array, sourcePosition, destination, destinationPosition, array.length-sourcePosition);
    }

    @Ignore
    public static <T> void copyTo(T[] array, T[] destination, int sourcePosition, int destinationPosition, int length){
        System.arraycopy(array, sourcePosition, destination, destinationPosition, length);
    }

    @Ignore
    public int copyTo$sourcePosition(T[] destination){
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public int copyTo$destinationPosition(T[] destination, int sourcePosition){
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public int copyTo$length(T[] destination, int sourcePosition, int destinationPosition){
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public void copyTo(T[] destination){
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public void copyTo(T[] destination, 
                       int sourcePosition){
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public void copyTo(T[] destination, 
                       int sourcePosition, 
                       int destinationPosition){
        throw Util.makeJavaArrayWrapperException();
    }

    public void copyTo(@Name("destination") T[] destination, 
                       @Name("sourcePosition") @Defaulted int sourcePosition, 
                       @Name("destinationPosition") @Defaulted int destinationPosition, 
                       @Name("length") @Defaulted int length){
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
    
    @TypeInfo("java.lang::ObjectArray<T>")
    public Object[] $clone() {
        throw Util.makeJavaArrayWrapperException();
    }
    
    @Ignore
    public static <T> T[] $clone(T[] value) {
        return value.clone();
    }
    
    @TypeInfo("ceylon.language::Iterable<T|ceylon.language::Null,ceylon.language::Null>")
    public ObjectArrayIterable<T> getIterable() {
        throw Util.makeJavaArrayWrapperException();
    }
    
    @Ignore
    public static <T> ObjectArrayIterable<T> getIterable(T[] value) {
        return new ObjectArrayIterable<T>(value, value.length);
    }
    
    /* Implement Iterable */

    public static final class ObjectArrayIterable<T> extends AbstractArrayIterable<T, T[]> {

        public ObjectArrayIterable(T[] array, int start, int len, int step) {
            super(TypeDescriptor.klass(array.getClass().getComponentType()), array, start, len, step);
        }

        public ObjectArrayIterable(T[] array, int length) {
            super(TypeDescriptor.klass(array.getClass().getComponentType()), array, length);
        }

        @Override
        protected ObjectArrayIterable<T> newInstance(T[] array, int start,
                int len, int step) {
            return new ObjectArrayIterable<T>(array, start, len, step);
        }

        @Override
        protected T get(T[] array, int index) {
            return array[index];
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
