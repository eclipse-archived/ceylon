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
    public T get(int index) {
        throw Util.makeJavaArrayWrapperException();
    }

    public void set(int index, @TypeInfo("T|ceylon.language::Null") T element) {
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
}
