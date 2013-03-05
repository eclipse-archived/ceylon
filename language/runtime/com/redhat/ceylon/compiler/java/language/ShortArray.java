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
 * A Ceylon schema for a Java short[]
 *
 * This type is never instantiated, it is completely erased to short[].
 * 
 * The "get", "set", "size" methods and the constructor are also completely erased to Java array operators, or "Util.fillArray"
 * in the case of the constructor if the initial element is specified.
 * 
 * Only the value type static methods are really invoked.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
@Ceylon(major = 4)
@Class
@ValueType
public final class ShortArray implements ReifiedType {
    
    @Ignore
    public final static TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(short[].class);
    
    public ShortArray(@Name("size") int size, @TypeInfo("ceylon.language::Integer") @Defaulted @Name("element") short element){
        throw Util.makeJavaArrayWrapperException();
    }

    public short get(int index) {
        throw Util.makeJavaArrayWrapperException();
    }

    public void set(int index, short element) {
        throw Util.makeJavaArrayWrapperException();
    }

    @Name("size")
    public final int length = 0;

    @Ignore
    public static ceylon.language.Array<ceylon.language.Integer> getArray(short[] array){
        return Array.instance(array);
    }

    @TypeInfo("ceylon.language::Array<ceylon.language::Integer>")
    public ceylon.language.Array<ceylon.language.Integer> getArray(){
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    @Override
    public TypeDescriptor $getType() {
        throw Util.makeJavaArrayWrapperException();
    }
}
