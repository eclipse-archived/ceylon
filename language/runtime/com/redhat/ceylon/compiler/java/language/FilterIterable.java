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
import ceylon.language.List;
import ceylon.language.Map;
import ceylon.language.Sequence;

import com.redhat.ceylon.compiler.java.metadata.Ignore;

/** A convenience class for the Iterable.filter() method.
 * 
 * @author Enrique Zamudio
 */
public class FilterIterable<Element> implements Iterable<Element> {
    final Iterable<? extends Element> iterable;
    final Callable<? extends Boolean> f;
    public FilterIterable(Iterable<? extends Element> iterable, Callable<? extends Boolean> selecting) {
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
    	return Iterable$impl._getFirst(this);
    }
    @Override @Ignore public Element getLast(){
        return Iterable$impl._getLast(this);
    }
    @Override
    @Ignore
    public Iterable<? extends Element> getRest() {
    	return Iterable$impl._getRest(this);
    }
    @Override
    @Ignore
    public List<? extends Element> getSequence() {
        return Iterable$impl._getSequence(this);
    }
    @Override
    @Ignore
    public Element find(Callable<? extends Boolean> f) {
        return Iterable$impl._find(this, f);
    }
    @Override @Ignore
    public Element findLast(Callable<? extends Boolean> f) {
        return Iterable$impl._findLast(this, f);
    }
    @Override @Ignore
    public List<? extends Element> sort(Callable<? extends Comparison> f) {
        return Iterable$impl._sort(this, f);
    }
    @Override @Ignore
    public <Result> List<? extends Result> collect(Callable<? extends Result> f) {
        return Iterable$impl._collect(this,  f);
    }
    @Override @Ignore
    public List<? extends Element> select(Callable<? extends Boolean> f) {
        return Iterable$impl._select(this,  f);
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
    public Iterable<? extends Element> skipping(long skip) {
        return Iterable$impl._skipping(this, skip);
    }
    @Override @Ignore
    public Iterable<? extends Element> taking(long take) {
        return Iterable$impl._taking(this, take);
    }
    @Override @Ignore
    public Iterable<? extends Element> by(long step) {
        return Iterable$impl._by(this, step);
    }
    @Override @Ignore
    public long count(Callable<? extends Boolean> f) {
        return Iterable$impl._count(this, f);
    }
    @Override @Ignore
    public Iterable<? extends Element> getCoalesced() {
        return Iterable$impl._getCoalesced(this);
    }
    @Override @Ignore
    public Iterable<? extends Entry<? extends Integer, ? extends Element>> getIndexed() {
        return Iterable$impl._getIndexed(this);
    }
    @Override @Ignore @SuppressWarnings("rawtypes")
    public <Other> Iterable chain(Iterable<? extends Other> other) {
        return Iterable$impl._chain(this, other);
    }
    @Override @Ignore
    public <Key> Map<? extends Key, ? extends Sequence<? extends Element>> group(Callable<? extends Key> grouping) {
        return Iterable$impl._group(this, grouping);
    }
}