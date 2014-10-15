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
    public void $serialize$(Callable<? extends Deconstructor> deconstructor) {
        
    }
    @Override
    public void $deserialize$(Deconstructed deconstructed) {
        this.t = (T)((DeconstructedGineaPig)deconstructed).getValue(null, null);
    }
}

class DeconstructedGineaPig implements Deconstructed {
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
    public Iterable$impl<? extends Sequence<? extends Object>, ? extends Object> $ceylon$language$Iterable$impl() {
        return iterable$this;
    }

    @Override
    public boolean any(Callable<? extends Boolean> arg0) {
        return iterable$this.any(arg0);
    }

    @Override
    public Iterable<? extends Sequence<? extends Object>, ? extends Object> by(
            long arg0) {
        return iterable$this.by(arg0);
    }

    @Override
    public <Other, OtherAbsent> Iterable chain(TypeDescriptor arg0,
            TypeDescriptor arg1,
            Iterable<? extends Other, ? extends OtherAbsent> arg2) {
        return iterable$this.chain(arg0, arg1, arg2);
    }

    @Override
    public <Result> Sequential<? extends Result> collect(TypeDescriptor arg0,
            Callable<? extends Result> arg1) {
        return iterable$this.collect(arg0, arg1);
    }

    @Override
    public boolean contains(Object arg0) {
        return iterable$this.contains(arg0);
    }

    @Override
    public long count(Callable<? extends Boolean> arg0) {
        return iterable$this.count(arg0);
    }

    @Override
    public <Default> Iterable<? extends Object, ? extends Object> defaultNullElements(
            TypeDescriptor arg0, Default arg1) {
        return iterable$this.defaultNullElements(arg0, arg1);
    }

    @Override
    public boolean every(Callable<? extends Boolean> arg0) {
        return iterable$this.every(arg0);
    }

    @Override
    public Iterable<? extends Sequence<? extends Object>, ? extends Object> filter(
            Callable<? extends Boolean> arg0) {
        return iterable$this.filter(arg0);
    }

    @Override
    public Sequence<? extends Object> find(Callable<? extends Boolean> arg0) {
        return iterable$this.find(arg0);
    }

    @Override
    public Sequence<? extends Object> findLast(Callable<? extends Boolean> arg0) {
        return iterable$this.findLast(arg0);
    }

    @Override
    public <Result, OtherAbsent> Iterable<? extends Result, ? extends Object> flatMap(
            TypeDescriptor arg0,
            TypeDescriptor arg1,
            Callable<? extends Iterable<? extends Result, ? extends OtherAbsent>> arg2) {
        return iterable$this.flatMap(arg0, arg1, arg2);
    }

    @Override
    public <Result> Callable<? extends Result> fold(TypeDescriptor arg0,
            Result arg1) {
        return iterable$this.fold(arg0, arg1);
    }

    @Override
    public <Other> Iterable<? extends Object, ? extends Object> follow(
            TypeDescriptor arg0, Other arg1) {
        return iterable$this.follow(arg0, arg1);
    }

    @Override
    public Iterable<? extends Sequence<? extends Object>, ? extends Object> getCoalesced() {
        return iterable$this.getCoalesced();
    }

    @Override
    public Iterable<? extends Sequence<? extends Object>, ? extends Object> getCycled() {
        return iterable$this.getCycled();
    }

    @Override
    public boolean getEmpty() {
        return iterable$this.getEmpty();
    }

    @Override
    public Iterable<? extends Sequence<? extends Object>, ? extends Object> getExceptLast() {
        return iterable$this.getExceptLast();
    }

    @Override
    public Object getFirst() {
        return iterable$this.getFirst();
    }

    @Override
    public Sequence<? extends Object> getFromFirst(long arg0) {
        return iterable$this.getFromFirst(arg0);
    }

    @Override
    public Iterable<? extends Entry<? extends Integer, ? extends Sequence<? extends Object>>, ? extends Object> getIndexed() {
        return iterable$this.getIndexed();
    }

    @Override
    public Object getLast() {
        return iterable$this.getLast();
    }

    @Override
    public Iterable<? extends Sequence<? extends Sequence<? extends Object>>, ? extends Object> getPaired() {
        return iterable$this.getPaired();
    }

    @Override
    public Iterable<? extends Sequence<? extends Object>, ? extends Object> getRest() {
        return iterable$this.getRest();
    }

    @Override
    public long getSize() {
        return iterable$this.getSize();
    }

    @Override
    public <Other> Iterable<? extends Object, ? extends Object> interpose(
            TypeDescriptor arg0, Other arg1) {
        return iterable$this.interpose(arg0, arg1);
    }

    @Override
    public <Other> Iterable<? extends Object, ? extends Object> interpose(
            TypeDescriptor arg0, Other arg1, long arg2) {
        return iterable$this.interpose(arg0, arg1, arg2);
    }

    @Override
    public <Other> long interpose$step(TypeDescriptor arg0, Other arg1) {
        return iterable$this.interpose$step(arg0, arg1);
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
    public boolean longerThan(long arg0) {
        return iterable$this.longerThan(arg0);
    }

    @Override
    public <Result> Iterable<? extends Result, ? extends Object> map(
            TypeDescriptor arg0, Callable<? extends Result> arg1) {
        return iterable$this.map(arg0, arg1);
    }

    @Override
    public Object max(Callable<? extends Comparison> arg0) {
        return iterable$this.max(arg0);
    }

    @Override
    public Iterable<? extends Sequence<? extends Sequence<? extends Object>>, ? extends Object> partition(
            long arg0) {
        return iterable$this.partition(arg0);
    }

    @Override
    public <Other, OtherAbsent> Iterable<? extends Sequence<? extends Object>, ? extends Object> product(
            TypeDescriptor arg0, TypeDescriptor arg1,
            Iterable<? extends Other, ? extends OtherAbsent> arg2) {
        return iterable$this.product(arg0, arg1, arg2);
    }

    @Override
    public <Result> Object reduce(TypeDescriptor arg0,
            Callable<? extends Result> arg1) {
        return iterable$this.reduce(arg0, arg1);
    }

    @Override
    public Iterable<? extends Sequence<? extends Object>, ? extends Object> repeat(
            long arg0) {
        return iterable$this.repeat(arg0);
    }

    @Override
    public <Result> Callable<? extends Iterable<? extends Result, ? extends Object>> scan(
            TypeDescriptor arg0, Result arg1) {
        return iterable$this.scan(arg0, arg1);
    }

    @Override
    public Sequential<? extends Sequence<? extends Object>> select(
            Callable<? extends Boolean> arg0) {
        return iterable$this.select(arg0);
    }

    @Override
    public Sequential<? extends Sequence<? extends Object>> sequence() {
        return iterable$this.sequence();
    }

    @Override
    public boolean shorterThan(long arg0) {
        return iterable$this.shorterThan(arg0);
    }

    @Override
    public Iterable<? extends Sequence<? extends Object>, ? extends Object> skip(
            long arg0) {
        return iterable$this.skip(arg0);
    }

    @Override
    public Iterable<? extends Sequence<? extends Object>, ? extends Object> skipWhile(
            Callable<? extends Boolean> arg0) {
        return iterable$this.skipWhile(arg0);
    }

    @Override
    public Sequential<? extends Sequence<? extends Object>> sort(
            Callable<? extends Comparison> arg0) {
        return iterable$this.sort(arg0);
    }

    @Override
    public <Result, Args extends Sequential<? extends Object>> Callable<? extends Iterable<? extends Result, ? extends Object>> spread(
            TypeDescriptor arg0, TypeDescriptor arg1,
            Callable<? extends Callable<? extends Result>> arg2) {
        return iterable$this.spread(arg0, arg1, arg2);
    }

    @Override
    public Iterable<? extends Sequence<? extends Object>, ? extends Object> take(
            long arg0) {
        return iterable$this.take(arg0);
    }

    @Override
    public Iterable<? extends Sequence<? extends Object>, ? extends Object> takeWhile(
            Callable<? extends Boolean> arg0) {
        return iterable$this.takeWhile(arg0);
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
        DeserializingReference<GuineaPig> a = new DeserializingReference<GuineaPig>(null, "a", cm, new GuineaPig("A"));
        DeserializingReference<GuineaPig> b = new DeserializingReference<GuineaPig>(null, "b", cm, new GuineaPig("B"));
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
        DeserializingReference<GuineaPig> a = new DeserializingReference<GuineaPig>(null, "a", cm, new GuineaPig("A"));
        a.deserialize(new DeconstructedGineaPig(null, a));
        a.instance();
        Assert.assertEquals(3, a.getState());
    }
    
    /** a -> b -> a*/
    @Test
    public void testCycle2() {
        @SuppressWarnings("rawtypes")
        ClassModel cm = null;
        DeserializingReference<GuineaPig> a = new DeserializingReference<GuineaPig>(null, "a", cm, new GuineaPig("A"));
        DeserializingReference<GuineaPig> b = new DeserializingReference<GuineaPig>(null, "b", cm, new GuineaPig("B"));
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
        DeserializingReference<GuineaPig> a = new DeserializingReference<GuineaPig>(null, "a", cm, new GuineaPig("A"));
        DeserializingReference<GuineaPig> b = new DeserializingReference<GuineaPig>(null, "b", cm, new GuineaPig("B"));
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
        DeserializingReference<GuineaPig> a = new DeserializingReference<GuineaPig>(null, "a", cm, new GuineaPig("A"));
        DeserializingReference<GuineaPig> b = new DeserializingReference<GuineaPig>(null, "b", cm, new GuineaPig("B"));
        DeserializingReference<GuineaPig> c = new DeserializingReference<GuineaPig>(null, "c", cm, new GuineaPig("C"));
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
