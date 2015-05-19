package com.redhat.ceylon.compiler.java.runtime.serialization;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import ceylon.language.Anything;
import ceylon.language.AssertionError;
import ceylon.language.Category$impl;
import ceylon.language.Integer;
import ceylon.language.Iterable;
import ceylon.language.Iterable$impl;
import ceylon.language.Iterator;
import ceylon.language.Sequence;
import ceylon.language.Sequential;
import ceylon.language.Singleton;
import ceylon.language.Tuple;
import ceylon.language.impl.BaseIterable;
import ceylon.language.impl.rethrow_;
import ceylon.language.meta.type_;
import ceylon.language.meta.declaration.NestableDeclaration;
import ceylon.language.meta.declaration.ValueDeclaration;
import ceylon.language.meta.model.Class;
import ceylon.language.meta.model.Type;
import ceylon.language.serialization.DeserializationContext;
import ceylon.language.serialization.deserialization_;
import ceylon.language.serialization.Deconstructor;
import ceylon.language.serialization.Reference;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major=8, minor=0)
@com.redhat.ceylon.compiler.java.metadata.Class
@TypeParameters(@TypeParameter("T"))
class GuineaPig<T> implements Serializable, ReifiedType {
    private final String s;
    private T t = null;
    GuineaPig(@Name("s")String s) {
        this.s = s;
    }
    public String toString() {
        return s;
    }
    @TypeInfo("T")
    public T getT() {
        return t;
    }
    @Override
    public void $serialize$(Deconstructor deconstructor) {
        
    }
    @Override
    public Collection $references$() {
        return Arrays.asList("com.redhat.ceylon.compiler.java.runtime.serialization::GuineaPig.t",
                "com.redhat.ceylon.compiler.java.runtime.serialization::GuineaPig.s");
    }
    @Override
    public void $set$(Object $reference, Object ref) {
        try {
            switch ((String)$reference) {
            case "com.redhat.ceylon.compiler.java.runtime.serialization::GuineaPig.t":
                t = (T)ref;
                break;
            case "com.redhat.ceylon.compiler.java.runtime.serialization::GuineaPig.s":
                MethodHandles.lookup().findSetter(GuineaPig.class, "s", String.class).invoke(this, (String)ref);
                break;
            default:
                throw new RuntimeException();
            }
        } catch (Throwable t) {
            rethrow_.rethrow(t);
        }
        
    }
    @Override
    public TypeDescriptor $getType$() {
        return TypeDescriptor.klass(GuineaPig.class);
    }
}

public class DeserializationTests {
    @BeforeClass
    public static void initMetamodel() {
        M
    }
    @Test
    public void test1() {
        DeserializationContext<Object> dc = deserialization_.deserialization(Integer.$TypeDescriptor$);
        
        GuineaPig gp = new GuineaPig("");
        Class<? extends GuineaPig, ? super Sequential<? extends Object>> type = (Class)type_.type(Anything.$TypeDescriptor$, gp);
        ValueDeclaration s = type.getDeclaration().getMemberDeclaration(ValueDeclaration.$TypeDescriptor$, "s");
        ValueDeclaration t = type.getDeclaration().getMemberDeclaration(ValueDeclaration.$TypeDescriptor$, "t");
        dc.instanceValue(1, "hello, world");
        dc.attribute(2, s, 1);
        dc.instance(2, type);
        GuineaPig gp2 = dc.reconstruct(gp.$getType$(), 2);
        
    }
}