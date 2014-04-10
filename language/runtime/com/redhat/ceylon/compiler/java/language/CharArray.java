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
 * A Ceylon schema for a Java <code>char[]</code>.
 *
 * This type is never instantiated, it is completely erased to 
 * <code>char[]</code>.
 * 
 * The {@link #get(int)}, {@link #set(int,char)}, {@link #length size} 
 * methods and the constructor are also completely erased to Java array 
 * operators, or {@link Util#fillArray(char[],char)} in the case that 
 * an initial element is specified.
 * 
 * Only the value type static methods are really invoked.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
@Ceylon(major = 6)
@Class
@ValueType
public final class CharArray implements ReifiedType {

    @Ignore
    public final static TypeDescriptor $TypeDescriptor$ = TypeDescriptor.klass(CharArray.class);

    public CharArray(@Name("size") int size, @TypeInfo("ceylon.language::Character") @Defaulted @Name("element") char element){
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public CharArray(@Name("size") int size){
        throw Util.makeJavaArrayWrapperException();
    }

    public char get(int index) {
        throw Util.makeJavaArrayWrapperException();
    }

    public void set(int index, char element) {
        throw Util.makeJavaArrayWrapperException();
    }

    @Name("size")
    public final int length = 0;

    @Ignore
    public static ceylon.language.Array<java.lang.Character> getArray(char[] array){
        return Array.instance(array);
    }

    @TypeInfo("ceylon.language::Array<java.lang::Character>")
    public ceylon.language.Array<java.lang.Character> getArray(){
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public static void copyTo(char[] array, char[] destination){
        System.arraycopy(array, 0, destination, 0, array.length);
    }

    @Ignore
    public static void copyTo(char[] array, char[] destination, int sourcePosition){
        System.arraycopy(array, sourcePosition, destination, 0, array.length-sourcePosition);
    }

    @Ignore
    public static void copyTo(char[] array, char[] destination, int sourcePosition, int destinationPosition){
        System.arraycopy(array, sourcePosition, destination, destinationPosition, array.length-sourcePosition);
    }

    @Ignore
    public static void copyTo(char[] array, char[] destination, int sourcePosition, int destinationPosition, int length){
        System.arraycopy(array, sourcePosition, destination, destinationPosition, length);
    }

    @Ignore
    public int copyTo$sourcePosition(char[] destination){
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public int copyTo$destinationPosition(char[] destination, int sourcePosition){
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public int copyTo$length(char[] destination, int sourcePosition, int destinationPosition){
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public void copyTo(char[] destination){
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public void copyTo(char[] destination, 
                       int sourcePosition){
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public void copyTo(char[] destination, 
                       int sourcePosition, 
                       int destinationPosition){
        throw Util.makeJavaArrayWrapperException();
    }

    public void copyTo(@Name("destination") char[] destination, 
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
    public static boolean equals(char[] value, java.lang.Object that) {
        return value.equals(that);
    }

    @Override
    public int hashCode() {
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public static int hashCode(char[] value) {
        return value.hashCode();
    }

    @Override
    public java.lang.String toString() {
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    public static java.lang.String toString(char[] value) {
        return value.toString();
    }
}
