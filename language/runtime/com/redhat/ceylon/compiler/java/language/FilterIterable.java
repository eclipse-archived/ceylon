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

/** A convenience class for the Iterable.filter() method.
 * 
 * @author Enrique Zamudio
 */
public class FilterIterable<Element> implements Iterable<Element> {
    private final ceylon.language.Iterable$impl<Element> $ceylon$language$Iterable$this;
    
    final Iterable<? extends Element> iterable;
    final Callable<? extends Boolean> f;
    
    public FilterIterable(Iterable<? extends Element> iterable, Callable<? extends Boolean> selecting) {
        this.$ceylon$language$Iterable$this = new ceylon.language.Iterable$impl<Element>(this);
        this.iterable = iterable;
        f = selecting;
    }

    class FilterIterator implements Iterator<Element> {
        final Iterator<? extends Element> iter = iterable.getIterator();
        public java.lang.Object next() {
            java.lang.Object elem = iter.next();
            boolean flag = elem instanceof Finished ? true : f.$call(elem).booleanValue();
            while (!flag) {
                elem = iter.next();
                flag = elem instanceof Finished ? true : f.$call(elem).booleanValue();
            }
            return elem;
        }
    }
    public Iterator<? extends Element> getIterator() { return new FilterIterator(); }
    public boolean getEmpty() { return getIterator().next() instanceof Finished; }
    @Override
    @Ignore
    public Element getFirst() {
    	return $ceylon$language$Iterable$this.getFirst();
    }
    @Override @Ignore public Element getLast(){
        return $ceylon$language$Iterable$this.getLast();
    }
    @Override
    @Ignore
    public Iterable<? extends Element> getRest() {
    	return $ceylon$language$Iterable$this.getRest();
    }
    @Override
    @Ignore
    public Sequential<? extends Element> getSequence() {
        return $ceylon$language$Iterable$this.getSequence();
    }
    @Override
    @Ignore
    public Element find(Callable<? extends Boolean> f) {
        return $ceylon$language$Iterable$this.find(f);
    }
    @Override @Ignore
    public Element findLast(Callable<? extends Boolean> f) {
        return $ceylon$language$Iterable$this.findLast(f);
    }
    @Override @Ignore
    public Sequential<? extends Element> sort(Callable<? extends Comparison> f) {
        return $ceylon$language$Iterable$this.sort(f);
    }
    @Override @Ignore
    public <Result> Sequential<? extends Result> collect(Callable<? extends Result> f) {
        return $ceylon$language$Iterable$this.collect( f);
    }
    @Override @Ignore
    public Sequential<? extends Element> select(Callable<? extends Boolean> f) {
        return $ceylon$language$Iterable$this.select( f);
    }
    @Override
    @Ignore
    public <Result> Iterable<Result> map(Callable<? extends Result> f) {
        return new MapIterable<Element, Result>(this, f);
    }
    @Override
    @Ignore
    public Iterable<? extends Element> filter(Callable<? extends Boolean> f) {
        return new FilterIterable<Element>(this, f);
    }
    @Override
    @Ignore
    public <Result> Result fold(Result ini, Callable<? extends Result> f) {
        return $ceylon$language$Iterable$this.fold(ini, f);
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
    public Iterable<? extends Element> skipping(long skip) {
        return $ceylon$language$Iterable$this.skipping(skip);
    }
    @Override @Ignore
    public Iterable<? extends Element> taking(long take) {
        return $ceylon$language$Iterable$this.taking(take);
    }
    @Override @Ignore
    public Iterable<? extends Element> by(long step) {
        return $ceylon$language$Iterable$this.by(step);
    }
    @Override @Ignore
    public long count(Callable<? extends Boolean> f) {
        return $ceylon$language$Iterable$this.count(f);
    }
    @Override @Ignore
    public Iterable<? extends Element> getCoalesced() {
        return $ceylon$language$Iterable$this.getCoalesced();
    }
    @Override @Ignore
    public Iterable<? extends Entry<? extends Integer, ? extends Element>> getIndexed() {
        return $ceylon$language$Iterable$this.getIndexed();
    }
    @Override @Ignore @SuppressWarnings("rawtypes")
    public <Other> Iterable chain(Iterable<? extends Other> other) {
        return $ceylon$language$Iterable$this.chain(other);
    }
    @Override @Ignore
    public <Key> Map<? extends Key, ? extends Sequence<? extends Element>> group(Callable<? extends Key> grouping) {
        return $ceylon$language$Iterable$this.group(grouping);
    }
}