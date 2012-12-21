package com.redhat.ceylon.compiler.java.language;

import ceylon.language.Boolean;
import ceylon.language.Callable;
import ceylon.language.Comparison;
import ceylon.language.Entry;
import ceylon.language.Finished;
import ceylon.language.Integer;
import ceylon.language.Iterable;
import ceylon.language.Iterator;
import ceylon.language.Map;
import ceylon.language.Sequence;
import ceylon.language.Sequential;

import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

/** A convenience class for the Iterable.map() method.
 * 
 * @author Enrique Zamudio
 */
public class MapIterable<Element, Result> implements Iterable<Result> {
    private final ceylon.language.Iterable$impl<Result> $ceylon$language$Iterable$this;
    private final ceylon.language.Category$impl $ceylon$language$Category$this;
    
    final Iterable<? extends Element> iterable;
    final Callable<? extends Result> sel;
    
    public MapIterable(Iterable<? extends Element> iterable, Callable<? extends Result> collecting) {
        this.$ceylon$language$Iterable$this = new ceylon.language.Iterable$impl<Result>(this);
        this.$ceylon$language$Category$this = new ceylon.language.Category$impl(this);
        this.iterable = iterable;
        sel = collecting;
    }

    class MapIterator implements Iterator<Result> {
        final Iterator<? extends Element> orig = iterable.getIterator();
        java.lang.Object elem;
        public java.lang.Object next() {
            elem = orig.next();
            if (!(elem instanceof Finished)) {
                return sel.$call(elem);
            }
            return elem;
        }
    }
    public Iterator<? extends Result> getIterator() { return new MapIterator(); }
    public boolean getEmpty() { return getIterator().next() instanceof Finished; }
    
    @Override
    @Ignore
    public long getSize() {
        return $ceylon$language$Iterable$this.getSize();
    }

    @Override
    @Ignore
    public Result getFirst() {
    	return $ceylon$language$Iterable$this.getFirst();
    }
    @Override @Ignore public Result getLast(){
        return $ceylon$language$Iterable$this.getLast();
    }

    @Override
    @Ignore
    public Iterable<? extends Result> getRest() {
    	return $ceylon$language$Iterable$this.getRest();
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
    public <R2> Sequential<? extends R2> collect(Callable<? extends R2> f) {
        return $ceylon$language$Iterable$this.collect(f);
    }
    @Override @Ignore
    public Sequential<? extends Result> select(Callable<? extends Boolean> f) {
        return $ceylon$language$Iterable$this.select(f);
    }
    @Override
    @Ignore
    public <R2> Iterable<R2> map(Callable<? extends R2> f) {
        return new MapIterable<Result, R2>(this, f);
    }
    @Override
    @Ignore
    public Iterable<? extends Result> filter(Callable<? extends Boolean> f) {
        return new FilterIterable<Result>(this, f);
    }
    @Override
    @Ignore
    public <R2> R2 fold(R2 ini, Callable<? extends R2> f) {
        return (R2) $ceylon$language$Iterable$this.fold(ini, f);
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
    public Iterable<? extends Result> skipping(long skip) {
        return $ceylon$language$Iterable$this.skipping(skip);
    }
    @Override @Ignore
    public Iterable<? extends Result> taking(long take) {
        return $ceylon$language$Iterable$this.taking(take);
    }
    @Override @Ignore
    public Iterable<? extends Result> by(long step) {
        return $ceylon$language$Iterable$this.by(step);
    }
    @Override @Ignore
    public long count(Callable<? extends Boolean> f) {
        return $ceylon$language$Iterable$this.count(f);
    }
    @Override @Ignore
    public Iterable<? extends Result> getCoalesced() {
        return $ceylon$language$Iterable$this.getCoalesced();
    }
    @Override @Ignore
    public Iterable<? extends Entry<? extends Integer, ? extends Result>> getIndexed() {
        return $ceylon$language$Iterable$this.getIndexed();
    }
    @SuppressWarnings("rawtypes") @Override @Ignore
    public <Other> Iterable chain(Iterable<? extends Other> other) {
        return $ceylon$language$Iterable$this.chain(other);
    }
    @Override @Ignore
    public <Key> Map<? extends Key, ? extends Sequence<? extends Result>> group(Callable<? extends Key> grouping) {
        return $ceylon$language$Iterable$this.group(grouping);
    }
    @Override @Ignore
    public boolean contains(@Name("element") java.lang.Object element) {
        return $ceylon$language$Iterable$this.contains(element);
    }
    @Override @Ignore
    public boolean containsEvery(
            @Sequenced @Name("elements") @TypeInfo("ceylon.language::Sequential<ceylon.language::Object>") Sequential<?> elements) {
        return $ceylon$language$Category$this.containsEvery(elements);
    }
    @Override @Ignore
    public boolean containsEvery() {
        return $ceylon$language$Category$this.containsEvery();
    }
    @Override @Ignore
    public Sequential<?> containsEvery$elements() {
        return $ceylon$language$Category$this.containsEvery$elements();
    }
    @Override @Ignore
    public boolean containsAny(
            @Sequenced @Name("elements") @TypeInfo("ceylon.language::Sequential<ceylon.language::Object>") Sequential<?> elements) {
        return $ceylon$language$Category$this.containsAny(elements);
    }
    @Override @Ignore
    public boolean containsAny() {
        return $ceylon$language$Category$this.containsAny();
    }
    @Override @Ignore
    public Sequential<?> containsAny$elements() {
        return $ceylon$language$Category$this.containsAny$elements();
    }
}