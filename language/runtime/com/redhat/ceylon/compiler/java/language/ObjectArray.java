package com.redhat.ceylon.compiler.java.language;

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
 * A Ceylon schema for a Java T[]
 *
 * This type is never instantiated, it is completely erased to T[].
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
public final class ObjectArray<T> implements ReifiedType {
    
    public ObjectArray(@Name("size") int size, @TypeInfo("T|ceylon.language::Null") @Defaulted @Name("element") T element){
        throw Util.makeJavaArrayWrapperException();
    }

    @TypeInfo(value = "T", uncheckedNull = true)
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
        return null;
    }

    @TypeInfo("ceylon.language::Array<T>")
    public ceylon.language.Array<T> getArray(){
        return null;
    }

    @Ignore
    @Override
    public TypeDescriptor $getType() {
        throw Util.makeJavaArrayWrapperException();
    }
}
