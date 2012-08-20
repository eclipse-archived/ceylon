package com.redhat.ceylon.compiler.java.language;

import ceylon.language.Boolean;
import ceylon.language.Callable;
import ceylon.language.Comparison;
import ceylon.language.Entry;
import ceylon.language.Finished;
import ceylon.language.Integer;
import ceylon.language.Iterable;
import ceylon.language.Iterable$impl;
import ceylon.language.Iterator;
import ceylon.language.Map;
import ceylon.language.Sequence;

import com.redhat.ceylon.compiler.java.metadata.Ignore;

/** A convenience class for the Iterable.map() method.
 * 
 * @author Enrique Zamudio
 */
public class MapIterable<Element, Result> implements Iterable<Result> {
    final Iterable<? extends Element> iterable;
    final Callable<? extends Result> sel;
    public MapIterable(Iterable<? extends Element> iterable, Callable<? extends Result> collecting) {
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
    public Result getFirst() {
    	return Iterable$impl._getFirst(this);
    }
    @Override @Ignore public Result getLast(){
        return Iterable$impl._getLast(this);
    }

    @Override
    @Ignore
    public Iterable<? extends Result> getRest() {
    	return Iterable$impl._getRest(this);
    }

    @Override
    @Ignore
    public Iterable<? extends Result> getSequence() {
        return Iterable$impl._getSequence(this);
    }
    @Override
    @Ignore
    public Result find(Callable<? extends Boolean> f) {
        return Iterable$impl._find(this, f);
    }
    @Override @Ignore
    public Result findLast(Callable<? extends Boolean> f) {
        return Iterable$impl._findLast(this, f);
    }
    @Override @Ignore
    public Iterable<? extends Result> sort(Callable<? extends Comparison> f) {
        return Iterable$impl._sort(this, f);
    }
    @Override @Ignore
    public <R2> Iterable<? extends R2> collect(Callable<? extends R2> f) {
        return Iterable$impl._collect(this, f);
    }
    @Override @Ignore
    public Iterable<? extends Result> select(Callable<? extends Boolean> f) {
        return Iterable$impl._select(this, f);
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
        return Iterable$impl._fold(this, ini, f);
    }
    @Override @Ignore
    public boolean any(Callable<? extends Boolean> f) {
        return Iterable$impl._any(this, f);
    }
    @Override @Ignore
    public boolean every(Callable<? extends Boolean> f) {
        return Iterable$impl._every(this, f);
    }
    @Override @Ignore
    public Iterable<? extends Result> skipping(long skip) {
        return Iterable$impl._skipping(this, skip);
    }
    @Override @Ignore
    public Iterable<? extends Result> taking(long take) {
        return Iterable$impl._taking(this, take);
    }
    @Override @Ignore
    public Iterable<? extends Result> by(long step) {
        return Iterable$impl._by(this, step);
    }
    @Override @Ignore
    public long count(Callable<? extends Boolean> f) {
        return Iterable$impl._count(this, f);
    }
    @Override @Ignore
    public Iterable<? extends Result> getCoalesced() {
        return Iterable$impl._getCoalesced(this);
    }
    @Override @Ignore
    public Iterable<? extends Entry<? extends Integer, ? extends Result>> getIndexed() {
        return Iterable$impl._getIndexed(this);
    }
    @SuppressWarnings("rawtypes") @Override @Ignore
    public <Other> Iterable chain(Iterable<? extends Other> other) {
        return Iterable$impl._chain(this, other);
    }
    @Override @Ignore
    public <Key> Map<? extends Key, ? extends Sequence<? extends Result>> group(Callable<? extends Key> grouping) {
        return Iterable$impl._group(this, grouping);
    }
}