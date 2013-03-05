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
 * A Ceylon schema for a Java char[]
 *
 * This type is never instantiated, it is completely erased to char[].
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
public final class CharArray implements ReifiedType {

    @Ignore
    public final static TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(char[].class);

    public CharArray(@Name("size") int size, @TypeInfo("ceylon.language::Character") @Defaulted @Name("element") char element){
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
    public static ceylon.language.Array<ceylon.language.Character> getArray(char[] array){
        return Array.instance(array);
    }

    @TypeInfo("ceylon.language::Array<ceylon.language::Character>")
    public ceylon.language.Array<ceylon.language.Character> getArray(){
        throw Util.makeJavaArrayWrapperException();
    }

    @Ignore
    @Override
    public TypeDescriptor $getType() {
        throw Util.makeJavaArrayWrapperException();
    }
}
