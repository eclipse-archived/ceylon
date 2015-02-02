package com.redhat.ceylon.compiler.java.runtime.serialization;

import org.junit.Test;
import org.junit.Assert;


import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

import ceylon.language.AssertionError;
import ceylon.language.Boolean;
import ceylon.language.Callable;
import ceylon.language.Category$impl;
import ceylon.language.Comparison;
import ceylon.language.Entry;
import ceylon.language.Integer;
import ceylon.language.Iterable;
import ceylon.language.Iterable$impl;
import ceylon.language.Iterator;
import ceylon.language.Sequence;
import ceylon.language.Sequential;
import ceylon.language.Singleton;
import ceylon.language.Tuple;
import ceylon.language.impl.BaseIterable;
import ceylon.language.meta.declaration.TypeParameter;
import ceylon.language.meta.declaration.ValueDeclaration;
import ceylon.language.meta.model.ClassModel;
import ceylon.language.meta.model.Type;
import ceylon.language.serialization.Deconstructed;
import ceylon.language.serialization.Deconstructor;
import ceylon.language.serialization.Reference;

class GuineaPig<T> implements Serializable {
    private final String s;
    private T t = null;
    GuineaPig(String s) {
        this.s = s;
    }
    public String toString() {
        return s;
    }
    public T getT() {
        return t;
    }
    @Override
    public void $serialize$(Deconstructor deconstructor) {
        
    }
    @Override
    public void $deserialize$(Deconstructed deconstructed) {
        this.t = (T)((DeconstructedGineaPig)deconstructed).getValue(null, null);
    }
}

class DeconstructedGineaPig extends BaseIterable<Sequence<? extends Object>, Object>
         implements Deconstructed {
    Iterable$impl<? extends Sequence<? extends Object>, ? extends Object> iterable$this 
        = new Iterable$impl(ceylon.language.Object.$TypeDescriptor$, 
                ceylon.language.Null.$TypeDescriptor$, 
                this);
    Category$impl<? super Object> category$this 
    = new Category$impl(ceylon.language.Object.$TypeDescriptor$,
            this);
    private Type<? extends Object> typeArgument;
    private Object value;

    public DeconstructedGineaPig(Type<? extends Object> typeArgument, Object value) {
        super(ceylon.language.Object.$TypeDescriptor$, 
                ceylon.language.Null.$TypeDescriptor$);
        this.typeArgument = typeArgument;
        this.value = value;
    }

    @Override
    public <Instance> Reference<Instance> $getOuterInstance(TypeDescriptor arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <Instance> Object getElement(TypeDescriptor arg0, long arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Type<? extends Object> getTypeArgument(TypeParameter arg0) {
        return typeArgument;
    }

    @Override
    public <Instance> Object getValue(TypeDescriptor arg0, ValueDeclaration arg1) {
        return value;
    }

    @Override
    public Iterator<? extends Sequence<? extends Object>> iterator() {
        ValueDeclaration valueDecl = null;
        return new Singleton(null, new Tuple(null, new Object[]{
                valueDecl,
                value
        })).iterator();
    }



    @Override
    public Category$impl<? super Object> $ceylon$language$Category$impl() {
        return category$this;
    }

    @Override
    public boolean containsAny(Iterable<? extends Object, ? extends Object> arg0) {
        return category$this.containsAny(arg0);
    }

    @Override
    public boolean containsEvery(
            Iterable<? extends Object, ? extends Object> arg0) {
        return category$this.containsEvery(arg0);
    }
}

public class DeserializingReferenceTest {

    DeserializationContextImpl dc = new DeserializationContextImpl();
    
    /** a -> b */
    @Test
    public void testPair() {
        @SuppressWarnings("rawtypes")
        ClassModel cm = null;
        DeserializingReference<GuineaPig> a = new DeserializingReference<GuineaPig>(null, "a", cm, DeserializingReference.ST_STATELESS, new GuineaPig("A"));
        DeserializingReference<GuineaPig> b = new DeserializingReference<GuineaPig>(null, "b", cm, DeserializingReference.ST_STATELESS, new GuineaPig("B"));
        a.deserialize(new DeconstructedGineaPig(null, b));
        b.deserialize(new DeconstructedGineaPig(null, null));
        a.instance();
        Assert.assertEquals(3, a.getState());
        Assert.assertEquals(3, b.getState());
    }
    
    /** a -> a */
    @Test
    public void testCycle1() {
        @SuppressWarnings("rawtypes")
        ClassModel cm = null;
        DeserializingReference<GuineaPig> a = new DeserializingReference<GuineaPig>(null, "a", cm, DeserializingReference.ST_STATELESS, new GuineaPig("A"));
        a.deserialize(new DeconstructedGineaPig(null, a));
        a.instance();
        Assert.assertEquals(3, a.getState());
    }
    
    /** a -> b -> a*/
    @Test
    public void testCycle2() {
        @SuppressWarnings("rawtypes")
        ClassModel cm = null;
        DeserializingReference<GuineaPig> a = new DeserializingReference<GuineaPig>(null, "a", cm, DeserializingReference.ST_STATELESS, new GuineaPig("A"));
        DeserializingReference<GuineaPig> b = new DeserializingReference<GuineaPig>(null, "b", cm, DeserializingReference.ST_STATELESS, new GuineaPig("B"));
        a.deserialize(new DeconstructedGineaPig(null, b));
        b.deserialize(new DeconstructedGineaPig(null, a));
        a.instance();
        Assert.assertEquals(3, a.getState());
        Assert.assertEquals(3, b.getState());
    }
    
    @Test
    public void testMissingDeser() {
        @SuppressWarnings("rawtypes")
        ClassModel cm = null;
        DeserializingReference<GuineaPig> a = new DeserializingReference<GuineaPig>(null, "a", cm, DeserializingReference.ST_STATELESS, new GuineaPig("A"));
        DeserializingReference<GuineaPig> b = new DeserializingReference<GuineaPig>(null, "b", cm, DeserializingReference.ST_STATELESS, new GuineaPig("B"));
        a.deserialize(new DeconstructedGineaPig(null, b));
        try {
            a.instance();
            Assert.fail();
        } catch (ceylon.language.AssertionError e) {
            Assert.assertEquals("reference b has not been deserialized", e.getMessage());
        }
    }
    
    
    /** 
     * a -> b(ERROR) -> c
     * d -> c
     */
    @Test
    public void testError() {
        @SuppressWarnings("rawtypes")
        ClassModel cm = null;
        DeserializingReference<GuineaPig> a = new DeserializingReference<GuineaPig>(null, "a", cm, DeserializingReference.ST_STATELESS, new GuineaPig("A"));
        DeserializingReference<GuineaPig> b = new DeserializingReference<GuineaPig>(null, "b", cm, DeserializingReference.ST_STATELESS, new GuineaPig("B"));
        DeserializingReference<GuineaPig> c = new DeserializingReference<GuineaPig>(null, "c", cm, DeserializingReference.ST_STATELESS, new GuineaPig("C"));
        a.deserialize(new DeconstructedGineaPig(null, b));
        b.deserialize(new DeconstructedGineaPig(null, c) {
            @Override
            public <Instance> Object getValue(TypeDescriptor arg0, ValueDeclaration arg1) {
                throw new RuntimeException();
            }
        });
        c.deserialize(new DeconstructedGineaPig(null, null));
        try {
            a.instance();
        } catch (Exception e) {}
        Assert.assertEquals(4, a.getState());
        Assert.assertEquals(4, b.getState());
        try {
            a.instance();
            Assert.fail();
        } catch (AssertionError e) {}
        
        Assert.assertEquals(4, a.getState());
        Assert.assertEquals(4, b.getState());
    }
}
