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
 * A Ceylon schema for a Java long[]
 *
 * This type is never instantiated, it is completely erased to long[].
 * 
 * The "get", "set", "size" methods and the constructor are also completely erased to Java array operators, or "Util.fillArray"
 * in the case of the constructor if the initial element is specified.
 * 
 * Only the value type static methods are really invoked.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
@Ceylon(major = 6)
@Class
@ValueType
public final class LongArray implements ReifiedType {
    
    @Ignore
    public final static TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(long[].class);

    public LongArray(@Name("size") int size, @TypeInfo("ceylon.language::Integer") @Defaulted @Name("element") long element){
        throw Util.makeJavaArrayWrapperException();
    }

    public int get(int index) {
        throw Util.makeJavaArrayWrapperException();
    }

    public void set(int index, int element) {
        throw Util.makeJavaArrayWrapperException();
    }

    @Name("size")
    public final int length = 0;

    @Ignore
    public static ceylon.language.Array<ceylon.language.Integer> getArray(long[] array){
        return Array.instance(array);
    }

    @TypeInfo("ceylon.language::Array<ceylon.language::Integer>")
    public ceylon.language.Array<ceylon.language.Integer> getArray(){
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public static void copyTo(long[] array, long[] destination){
        System.arraycopy(array, 0, destination, 0, array.length);
    }

    @Ignore
    public static void copyTo(long[] array, long[] destination, int sourcePosition){
        System.arraycopy(array, sourcePosition, destination, 0, array.length-sourcePosition);
    }

    @Ignore
    public static void copyTo(long[] array, long[] destination, int sourcePosition, int destinationPosition){
        System.arraycopy(array, sourcePosition, destination, destinationPosition, array.length-sourcePosition);
    }

    @Ignore
    public static void copyTo(long[] array, long[] destination, int sourcePosition, int destinationPosition, int length){
        System.arraycopy(array, sourcePosition, destination, destinationPosition, length);
    }

    @Ignore
    public int copyTo$sourcePosition(long[] destination){
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public int copyTo$destinationPosition(long[] destination, int sourcePosition){
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public int copyTo$length(long[] destination, int sourcePosition, int destinationPosition){
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

    public void copyTo(@Name("destination") long[] destination, 
                       @Name("sourcePosition") @Defaulted int sourcePosition, 
                       @Name("destinationPosition") @Defaulted int destinationPosition, 
                       @Name("length") @Defaulted int length){
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    @Override
    public TypeDescriptor $getType() {
        throw Util.makeJavaArrayWrapperException();
    }
}
