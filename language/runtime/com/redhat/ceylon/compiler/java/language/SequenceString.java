package com.redhat.ceylon.compiler.java.language;

import ceylon.language.Boolean;
import ceylon.language.Callable;
import ceylon.language.Character;
import ceylon.language.Comparison;
import ceylon.language.Entry;
import ceylon.language.Integer;
import ceylon.language.Iterable;
import ceylon.language.Map;
import ceylon.language.Sequence;
import ceylon.language.Sequential;
import ceylon.language.String;
import ceylon.language.string_;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;

@Ignore
@Ceylon(major = 4)
public class SequenceString extends String implements Sequence<Character> {

    public SequenceString(java.lang.String s) {
        super(s);
    }

    @Override
    public boolean getEmpty() {
        return false;
    }

    @Override
    public Sequential<? extends Character> getRest() {
        return span(Integer.instance(1), null);
    }

    @Override
    @Ignore
    public Character getFirst() {
        return item(Integer.instance(0));
    }
    @Override @Ignore
    public Character getLast() {
        return item(getLastIndex());
    }

    @Override
    @Ignore
    public Sequence<? extends Character> getSequence() {
        return (Sequence<Character>)$ceylon$language$Iterable$this.getSequence();
    }
    @Override @Ignore
    public Character find(Callable<? extends Boolean> f) {
        return $ceylon$language$Iterable$this.find(f);
    }
    @Override @Ignore
    public Character findLast(Callable<? extends Boolean> f) {
        return $ceylon$language$Iterable$this.findLast(f);
    }
    @Override
    @Ignore
    public Sequence<? extends Character> sort(Callable<? extends Comparison> f) {
        return (Sequence)String.instance(string_.string($ceylon$language$Iterable$this.sort(f)));
    }
    @Override
    @Ignore
    public Iterable<? extends Character, ? extends java.lang.Object> filter(Callable<? extends Boolean> f) {
        return String.instance(string_.string((new FilterIterable<Character,java.lang.Object>(this, f)).getSequence()));
    }
    @Override @Ignore
    public <Result> Sequence<? extends Result> collect(Callable<? extends Result> f) {
        return (Sequence<? extends Result>)new MapIterable<Character,Result>(this, f).getSequence();
    }
    @Override @Ignore
    public Sequential<? extends Character> select(Callable<? extends Boolean> f) {
        return String.instance(string_.string((new FilterIterable<Character,java.lang.Object>(this, f)).getSequence()));
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
    public Iterable<? extends Character, ? extends java.lang.Object> skipping(long skip) {
        return this.segment(Integer.instance(skip), this.getSize());
    }
    @Override @Ignore
    public Iterable<? extends Character, ? extends java.lang.Object> taking(long take) {
        return this.segment(Integer.instance(0), take);
    }
    @Override @Ignore
    public Iterable<? extends Character, ? extends java.lang.Object> by(long step) {
        return String.instance(string_.string($ceylon$language$Iterable$this.by(step).getSequence()));
    }
    @Override @Ignore
    public long count(Callable<? extends Boolean> f) {
        return $ceylon$language$Iterable$this.count(f);
    }
    @Override @Ignore
    public Iterable<? extends Entry<? extends Integer, ? extends Character>, ? extends java.lang.Object> getIndexed() {
        return $ceylon$language$Iterable$this.getIndexed();
    }
    @SuppressWarnings("rawtypes")
    @Override @Ignore public <Other>Iterable chain(Iterable<? extends Other, ? extends java.lang.Object> other) {
        return $ceylon$language$Iterable$this.chain(other);
    }
    @Override @Ignore
    public <Key> Map<? extends Key, ? extends Sequence<? extends Character>> group(Callable<? extends Key> grouping) {
        return $ceylon$language$Iterable$this.group(grouping);
    }

    @Override @Ignore
    @SuppressWarnings("rawtypes")
    public <Other> Sequence withLeading(Other e) {
        return $ceylon$language$List$this.withLeading(e);
    }
    @Override @Ignore
    @SuppressWarnings("rawtypes")
    public <Other> Sequence withTrailing(Other e) {
        return $ceylon$language$List$this.withTrailing(e);
    }
    @Override @Ignore
    public SequenceString getReversed() {
        return (SequenceString)super.getReversed();
    }
    @Override
    public SequenceString getClone() {
    	return this;
    }
}
