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
 * A Ceylon schema for a Java float[]
 *
 * This type is never instantiated, it is completely erased to float[].
 * 
 * The "get", "set", "size" methods and the constructor are also completely erased to Java array operators, or "Util.fillArray"
 * in the case of the constructor if the initial element is specified.
 * 
 * Only the value type static methods are really invoked.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
@Ceylon(major = 4)
@Class(extendsType="ceylon.language::Object")
@ValueType
public final class FloatArray implements ReifiedType {

    @Ignore
    public final static TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(float[].class);

    public FloatArray(@Name("size") int size, @TypeInfo("ceylon.language::Float") @Defaulted @Name("element") float element){
        throw Util.makeJavaArrayWrapperException();
    }

    public float get(int index) {
        throw Util.makeJavaArrayWrapperException();
    }

    public void set(int index, float element) {
        throw Util.makeJavaArrayWrapperException();
    }

    @Name("size")
    public final int length = 0;

    @Ignore
    public static ceylon.language.Array<ceylon.language.Float> getArray(float[] array){
        return Array.instance(array);
    }

    @TypeInfo("ceylon.language::Array<ceylon.language::Float>")
    public ceylon.language.Array<ceylon.language.Float> getArray(){
        throw Util.makeJavaArrayWrapperException();
    }

    @Override
    public TypeDescriptor $getType() {
        throw Util.makeJavaArrayWrapperException();
    }
}
