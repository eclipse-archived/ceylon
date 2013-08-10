package com.redhat.ceylon.compiler.java.language;

import ceylon.language.Boolean;
import ceylon.language.Callable;
import ceylon.language.Category$impl;
import ceylon.language.Comparison;
import ceylon.language.Container$impl;
import ceylon.language.Entry;
import ceylon.language.Finished;
import ceylon.language.Integer;
import ceylon.language.Iterable;
import ceylon.language.Iterable$impl;
import ceylon.language.Iterator;
import ceylon.language.List;
import ceylon.language.Null;
import ceylon.language.Sequential;

import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

/** A convenience class for the Iterable.map() method.
 * 
 * @author Enrique Zamudio
 */
public class MapIterable<Element, Absent, Result> implements Iterable<Result,Absent>, ReifiedType {
    private final ceylon.language.Iterable$impl<Result,Absent> $ceylon$language$Iterable$this;
    private final ceylon.language.Container$impl<Result,Absent> $ceylon$language$Container$this;
    private final ceylon.language.Category$impl $ceylon$language$Category$this;
    
    final Iterable<? extends Element, ? extends Absent> iterable;
    final Callable<? extends Result> sel;
    @Ignore
    private TypeDescriptor $reifiedResult;
    @Ignore
    private TypeDescriptor $reifiedElement;
    @Ignore
    private TypeDescriptor $reifiedAbsent;
    
    public MapIterable(@Ignore TypeDescriptor $reifiedElement, @Ignore TypeDescriptor $reifiedAbsent, @Ignore TypeDescriptor $reifiedResult,
            Iterable<? extends Element, ? extends Absent> iterable, Callable<? extends Result> collecting) {
        this.$ceylon$language$Iterable$this = new ceylon.language.Iterable$impl<Result,Absent>($reifiedResult, $reifiedAbsent, this);
        this.$ceylon$language$Container$this = new ceylon.language.Container$impl<Result,Absent>($reifiedResult, $reifiedAbsent, this);
        this.$ceylon$language$Category$this = new ceylon.language.Category$impl(this);
        this.iterable = iterable;
        this.sel = collecting;
        this.$reifiedElement = $reifiedElement;
        this.$reifiedResult = $reifiedResult;
        this.$reifiedAbsent = $reifiedAbsent;
    }

    @Ignore
    @Override
    public Category$impl $ceylon$language$Category$impl(){
        return $ceylon$language$Category$this;
    }

    @Ignore
    @Override
    public Container$impl<Result,Absent> $ceylon$language$Container$impl(){
        return $ceylon$language$Container$this;
    }

    @Ignore
    @Override
    public Iterable$impl<Result,Absent> $ceylon$language$Iterable$impl(){
        return $ceylon$language$Iterable$this;
    }

    class MapIterator extends AbstractIterator<Result> {
        public MapIterator() {
            super($reifiedResult);
        }

        final Iterator<? extends Element> orig = iterable.iterator();
        java.lang.Object elem;
        
        public java.lang.Object next() {
            elem = orig.next();
            if (!(elem instanceof Finished)) {
                return sel.$call(elem);
            }
            return elem;
        }
        @Override
        @Ignore
        public TypeDescriptor $getType() {
            return TypeDescriptor.klass(MapIterator.class);
        }
    }
    public Iterator<? extends Result> iterator() { return new MapIterator(); }
    public boolean getEmpty() { return iterator().next() instanceof Finished; }
    
    @Override
    @Ignore
    public String toString() {
        return $ceylon$language$Iterable$this.toString();
    }

    @Override
    @Ignore
    public long getSize() {
        return $ceylon$language$Iterable$this.getSize();
    }

    @Override
    @Ignore
    public Result getFirst() {
    	return (Result) $ceylon$language$Iterable$this.getFirst();
    }
    @Override @Ignore 
    public Result getLast(){
        return (Result) $ceylon$language$Iterable$this.getLast();
    }

    @Override
    @Ignore
    public Iterable<? extends Result, ? extends java.lang.Object> getRest() {
    	return $ceylon$language$Iterable$this.getRest();
    }

    @Override @Ignore
    public Iterable<? extends Result, ? extends java.lang.Object> takingWhile(Callable<? extends Boolean> take) {
        return $ceylon$language$Iterable$this.takingWhile(take);
    }
    
    @Override @Ignore
    public Iterable<? extends Result, ? extends java.lang.Object> skippingWhile(Callable<? extends Boolean> skip) {
        return $ceylon$language$Iterable$this.skippingWhile(skip);
    }
    
    @Override
    @Ignore
    public Sequential<? extends Result> getSequence() {
        return $ceylon$language$Iterable$this.getSequence();
    }
    @Override
    @Ignore
    public Result find(Callable<? extends Boolean> f) {
        return $ceylon$language$Iterable$this.find(f);
    }
    @Override @Ignore
    public Result findLast(Callable<? extends Boolean> f) {
        return $ceylon$language$Iterable$this.findLast(f);
    }
    @Override @Ignore
    public Sequential<? extends Result> sort(Callable<? extends Comparison> f) {
        return $ceylon$language$Iterable$this.sort(f);
    }
    @Override @Ignore
    public <R2> Sequential<? extends R2> collect(@Ignore TypeDescriptor $reifiedR2, Callable<? extends R2> f) {
        return $ceylon$language$Iterable$this.collect($reifiedR2, f);
    }
    @Override @Ignore
    public Sequential<? extends Result> select(Callable<? extends Boolean> f) {
        return $ceylon$language$Iterable$this.select(f);
    }
    @Override
    @Ignore
    public <R2> Iterable<R2, ? extends Absent> map(@Ignore TypeDescriptor $reifiedR2, Callable<? extends R2> f) {
        return new MapIterable<Result, Absent, R2>($reifiedResult, $reifiedAbsent, $reifiedR2, this, f);
    }
    @Override
    @Ignore
    public Iterable<? extends Result, ? extends java.lang.Object> filter(Callable<? extends Boolean> f) {
        return new FilterIterable<Result,java.lang.Object>($reifiedResult, Null.$TypeDescriptor, this, f);
    }
    @Override
    @Ignore
    public <R2> R2 fold(@Ignore TypeDescriptor $reifiedR2, R2 ini, Callable<? extends R2> f) {
        return (R2) $ceylon$language$Iterable$this.fold($reifiedR2, ini, f);
    }
    @Override @Ignore
    public boolean any(Callable<? extends Boolean> f) {
        return $ceylon$language$Iterable$this.any(f);
    }
    @Override @Ignore
    public boolean every(Callable<? extends Boolean> f) {
        return $ceylon$language$Iterable$this.every(f);
    }
    @Override @Ignore
    public boolean longerThan(long length) {
        return $ceylon$language$Iterable$this.longerThan(length);
    }
    @Override @Ignore
    public boolean shorterThan(long length) {
        return $ceylon$language$Iterable$this.shorterThan(length);
    }
    @Override @Ignore
    public Iterable<? extends Result, ? extends java.lang.Object> skipping(long skip) {
        return $ceylon$language$Iterable$this.skipping(skip);
    }
    @Override @Ignore
    public Iterable<? extends Result, ? extends java.lang.Object> taking(long take) {
        return $ceylon$language$Iterable$this.taking(take);
    }
    @Override @Ignore
    public Iterable<? extends Result, ? extends Absent> by(long step) {
        return $ceylon$language$Iterable$this.by(step);
    }
    @Override @Ignore
    public long count(Callable<? extends Boolean> f) {
        return $ceylon$language$Iterable$this.count(f);
    }
    @Override @Ignore
    public Iterable<? extends Result, ?> getCoalesced() {
        return $ceylon$language$Iterable$this.getCoalesced();
    }
    @Override @Ignore
    public Iterable<? extends Entry<? extends Integer, ? extends Result>, ?> getIndexed() {
        return $ceylon$language$Iterable$this.getIndexed();
    }
    @SuppressWarnings("rawtypes") @Override @Ignore
    public <Other,Absent> Iterable chain(@Ignore TypeDescriptor $reifiedOther, @Ignore TypeDescriptor $reifiedOtherAbsent, Iterable<? extends Other, ? extends Absent> other) {
        return $ceylon$language$Iterable$this.chain($reifiedOther, $reifiedOtherAbsent, other);
    }
    @SuppressWarnings("rawtypes")
    @Override @Ignore 
    public <Other> Iterable following(@Ignore TypeDescriptor $reifiedOther, Other other) {
        return $ceylon$language$Iterable$this.following($reifiedOther, other);
    }
    @Override @Ignore
    public <Default>Iterable<?,? extends Absent> defaultNullElements(@Ignore TypeDescriptor $reifiedDefault, Default defaultValue) {
        return $ceylon$language$Iterable$this.defaultNullElements($reifiedDefault, defaultValue);
    }
    /*@Override @Ignore
    public <Key> Map<? extends Key, ? extends Sequence<? extends Result>> group(Callable<? extends Key> grouping) {
        return $ceylon$language$Iterable$this.group(grouping);
    }*/
    @Override @Ignore
    public boolean contains(@Name("element") java.lang.Object element) {
        return $ceylon$language$Iterable$this.contains(element);
    }
    @Override @Ignore
    public boolean containsEvery(Iterable<?,?> elements) {
        return $ceylon$language$Category$this.containsEvery(elements);
    }
    @Override
    @Ignore
    public Iterable<? extends Result,? extends Absent> getCycled() {
        return $ceylon$language$Iterable$this.getCycled();
    }
    @Override
    @Ignore
    public Iterable<? extends Result,? extends Absent> cycle(long times) {
        return $ceylon$language$Iterable$this.cycle(times);
    }
    @Override
    @Ignore
    public List<? extends Result> repeat(long times) {
        return $ceylon$language$Iterable$this.repeat(times);
    }
//    @Override @Ignore
//    public boolean containsEvery() {
//        return $ceylon$language$Category$this.containsEvery();
//    }
//    @Override @Ignore
//    public Sequential<?> containsEvery$elements() {
//        return $ceylon$language$Category$this.containsEvery$elements();
//    }
    @Override @Ignore
    public boolean containsAny(Iterable<?,?> elements) {
        return $ceylon$language$Category$this.containsAny(elements);
    }
//    @Override @Ignore
//    public boolean containsAny() {
//        return $ceylon$language$Category$this.containsAny();
//    }
//    @Override @Ignore
//    public Sequential<?> containsAny$elements() {
//        return $ceylon$language$Category$this.containsAny$elements();
//    }
    @Override
    @Ignore
    public TypeDescriptor $getType() {
        return TypeDescriptor.klass(MapIterable.class, $reifiedElement, $reifiedResult);
    }
}