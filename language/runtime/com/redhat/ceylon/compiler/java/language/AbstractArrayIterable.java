package com.redhat.ceylon.compiler.java.language;

import ceylon.language.Callable;
import ceylon.language.Category$impl;
import ceylon.language.Comparison;
import ceylon.language.Entry;
import ceylon.language.Iterable;
import ceylon.language.Iterable$impl;
import ceylon.language.Iterator;
import ceylon.language.List;
import ceylon.language.Null;
import ceylon.language.Sequential;
import ceylon.language.empty_;
import ceylon.language.sequence_;

import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

/**
 * Abstract implementation of ceylon.language::Iterable which iterates over 
 * an array (it could be an array of primitives, or an array of references, 
 * we don't know). 
 * Various methods (such as {@link #take(long)}, {@link #skip(long)} and
 * {@link #by(long)} are overridden so that they iterate over the same 
 * array instance.
 *
 * @param <Element> The element type
 * @param <ArrayType> The array type
 */
public abstract class AbstractArrayIterable<Element, ArrayType> implements ReifiedType, Iterable<Element, ceylon.language.Null> {
    private final Category$impl<Object> $ceylon$language$Category$this = new Category$impl<Object>(ceylon.language.Object.$TypeDescriptor$, this);
    private final Iterable$impl<Element, Null> $ceylon$language$Iterable$this;

    /** The array (could be an array or primitives or references) */
    private final ArrayType array;
    private final TypeDescriptor $reified$Element;
    /** The index where iteration starts */
    protected final int start;
    /** The step size of iteration */
    protected final int step;
    /** The number of elements in the iteration */
    protected final int len;

    @Override
    public TypeDescriptor $getType$() {
        return TypeDescriptor.klass(AbstractArrayIterable.class, this.$reified$Element);
    }

    @Ignore
    public AbstractArrayIterable(TypeDescriptor $reified$Element, ArrayType array, int length) {
        this($reified$Element, array, 0, length, 1);
    }
    /** Constructor used internally via {@link #newInstance(Object, int, int, int)} */
    @Ignore
    protected AbstractArrayIterable(TypeDescriptor $reified$Element, ArrayType array, int start, int len, int step) {
        if (start < 0) {
            throw new ceylon.language.AssertionError("start must be non-negative");
        }
        if (len < 0) {
            throw new ceylon.language.AssertionError("len must be non-negative");
        }
        if (step <= 0) {
            throw new ceylon.language.AssertionError("step size must be greater than zero");
        }

        this.array = array;
        this.$reified$Element = $reified$Element;
        this.$ceylon$language$Iterable$this = new Iterable$impl<Element, Null>(
                this.$reified$Element, Null.$TypeDescriptor$, this);
        this.start = start;
        this.len = len;
        this.step = step;
    }
    /** Factory for creating a new instance */
    protected abstract AbstractArrayIterable<Element, ArrayType> newInstance(ArrayType array, int start, int len, int step);
    
    protected abstract Element get(ArrayType array, int index);

    @Ignore
    public ArrayType arrayValue() {
        return array;
    }

    @Override
    public Category$impl<? super Object> $ceylon$language$Category$impl() {
        return $ceylon$language$Category$this;
    }

    @Override
    public Iterable$impl<? extends Element, ? extends Null> $ceylon$language$Iterable$impl() {
        return $ceylon$language$Iterable$this;
    }

    @Override
    public boolean containsAny(Iterable<? extends Object, ? extends Object> arg0) {
        return $ceylon$language$Category$this.containsAny(arg0);
    }

    @Override
    public boolean containsEvery(
            Iterable<? extends Object, ? extends Object> arg0) {
        return $ceylon$language$Category$this.containsEvery(arg0);
    }

    @Override
    public boolean any(Callable<? extends ceylon.language.Boolean> arg0) {
        return $ceylon$language$Iterable$this.any(arg0);
    }

    @Override
    public AbstractArrayIterable<Element, ArrayType> by(long step) {
        return newInstance(this.array, 
                this.start, 
                (int)((this.len+step-1)/step), //ceiling division
                this.step*(int)step);
    }

    @Override
    public <Other, OtherAbsent> Iterable<?,?> chain(
            @Ignore
            TypeDescriptor $reified$Other,
            @Ignore
            TypeDescriptor $reified$OtherAbsent,
            Iterable<? extends Other, ? extends OtherAbsent> other) {
        return $ceylon$language$Iterable$this.chain($reified$Other, $reified$OtherAbsent, other);
    }

    @Override
    public <Result> Sequential<? extends Result> collect(
            @Ignore
            TypeDescriptor $reified$Result,
            Callable<? extends Result> collecting) {
        return $ceylon$language$Iterable$this.collect($reified$Result, collecting);
    }

    @Override
    public boolean contains(Object item) {
        return $ceylon$language$Iterable$this.contains(item);
    }

    @Override
    public long count(Callable<? extends ceylon.language.Boolean> selecting) {
        return $ceylon$language$Iterable$this.count(selecting);
    }

    @Override
    public Iterable<? extends Element, ? extends Null> cycle(long times) {
        return $ceylon$language$Iterable$this.cycle(times);
    }

    @Override
    public <Default> Iterable<? extends Object, ? extends Null> defaultNullElements(
            @Ignore
            TypeDescriptor $reified$Default, 
            Default defaultValue) {
        return this;
    }

    @Override
    public boolean every(Callable<? extends ceylon.language.Boolean> selecting) {
        return $ceylon$language$Iterable$this.every(selecting);
    }

    @Override
    public Iterable<? extends Element, ? extends Object> filter(
            Callable<? extends ceylon.language.Boolean> selecting) {
        return $ceylon$language$Iterable$this.filter(selecting);
    }

    @Override
    public Element find(Callable<? extends ceylon.language.Boolean> selecting) {
        return $ceylon$language$Iterable$this.find(selecting);
    }

    @Override
    public Element findLast(Callable<? extends ceylon.language.Boolean> selecting) {
        return $ceylon$language$Iterable$this.findLast(selecting);
    }

    @Override
    public <Result> Result fold(
            @Ignore
            TypeDescriptor $reified$Result, 
            Result initial,
            Callable<? extends Result> accumulating) {
        return $ceylon$language$Iterable$this.fold($reified$Result, initial, accumulating);
    }

    @Override
    public <Other> Iterable<? extends Object, ? extends Object> following(
            @Ignore
            TypeDescriptor $reified$Other, 
            Other head) {
        return $ceylon$language$Iterable$this.following($reified$Other, head);
    }

    @Override
    public Iterable<? extends Element, ? extends Object> getCoalesced() {
        return this;
    }

    @Override
    public Iterable<? extends Element, ? extends Null> getCycled() {
        return $ceylon$language$Iterable$this.getCycled();
    }

    @Override
    public boolean getEmpty() {
        return this.len == 0;
    }
    
    @Override
    public Element getFirst() {
        return this.getEmpty() ? null : get(this.array, this.start);
    }

    @Override
    public Iterable<? extends Entry<? extends ceylon.language.Integer, ? extends Element>, ? extends Object> getIndexed() {
        return $ceylon$language$Iterable$this.getIndexed();
    }

    @Override
    public Element getLast() {
        return this.getEmpty() ? null : get(this.array, this.start+this.step*this.len-1);
    }

    @Override
    public AbstractArrayIterable<Element, ArrayType> getRest() {
        return getEmpty() ? this : newInstance(this.array, this.start+this.step, this.len-1, this.step);
    }

    @Override
    public Sequential<? extends Element> sequence() {
        // Note: Sequential is immutable, and we don't know where the array
        // came from, so however we create the sequence we must take a copy
        Object result = sequence_.sequence($reified$Element, Null.$TypeDescriptor$, this);
        if (result == null) {
            return (Sequential)empty_.get_();
        } else {
            return (Sequential)result;
        }
    }

    @Ignore
    @Override
    public long getSize() {
        return java.lang.Math.max(0, this.len);
    }

    @Override
    public Iterator<? extends Element> iterator() {
        if (this.getEmpty()) {
            return (Iterator)ceylon.language.emptyIterator_.get_();
        }
        return new AbstractArrayIterator<Element>($reified$Element, start, len, step) {
            protected Element get(int index) {
                return AbstractArrayIterable.this.get(array, index);
            }
        };
    }

    @Override
    public boolean longerThan(long length) {
        return this.getSize() > length;
    }

    @Override
    public <Result> Iterable<? extends Result, ? extends Null> map(
            @Ignore
            TypeDescriptor $reified$Result, 
            Callable<? extends Result> collecting) {
        return $ceylon$language$Iterable$this.map($reified$Result, collecting);
    }

    @Override
    public <Result> Object reduce(
            @Ignore
            TypeDescriptor $reified$Result,
            Callable<? extends Result> accumulating) {
        return $ceylon$language$Iterable$this.reduce($reified$Result, accumulating);
    }

    @Override
    public List<? extends Element> repeat(long times) {
        return $ceylon$language$Iterable$this.repeat(times);
    }

    @Override
    public Sequential<? extends Element> select(Callable<? extends ceylon.language.Boolean> selecting) {
        return $ceylon$language$Iterable$this.select(selecting);
    }

    @Override
    public boolean shorterThan(long length) {
        return this.getSize() < length;
    }

    @Override
    public AbstractArrayIterable<Element, ArrayType> skip(long skip) {
        if (skip <= 0) {
            return this;
        }
        return newInstance(this.array, 
                this.start+(int)skip*this.step, 
                this.len-(int)skip, 
                this.step);
    }

    @Override
    public Iterable<? extends Element, ? extends Object> skipWhile(
            Callable<? extends ceylon.language.Boolean> skip) {
        return $ceylon$language$Iterable$this.skipWhile(skip);
    }

    @Override
    public Sequential<? extends Element> sort(
            final Callable<? extends Comparison> comparing) {
        return $ceylon$language$Iterable$this.sort(comparing);
    }

    @Override
    public AbstractArrayIterable<Element, ArrayType> take(long take) {
        if (take >= this.getSize()) {
            return this;
        }
        return newInstance(this.array, 
                this.start, 
                (int)take, 
                this.step);
    }

    @Override
    public Iterable<? extends Element, ? extends Object> takeWhile(
            Callable<? extends ceylon.language.Boolean> take) {
        return $ceylon$language$Iterable$this.takeWhile(take);
    }
    
    
    @Override
    public String toString() {
        return $ceylon$language$Iterable$this.toString();
    }

}
