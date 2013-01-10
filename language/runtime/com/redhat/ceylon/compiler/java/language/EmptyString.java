package com.redhat.ceylon.compiler.java.language;

import ceylon.language.Boolean;
import ceylon.language.Null;
import ceylon.language.Callable;
import ceylon.language.Character;
import ceylon.language.Comparison;
import ceylon.language.Entry;
import ceylon.language.Integer;
import ceylon.language.Iterable;
import ceylon.language.Sequential;
import ceylon.language.Map;
import ceylon.language.Sequence;
import ceylon.language.String;
import ceylon.language.empty_;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;

@Ignore
@Ceylon(major = 3)
public class EmptyString extends String {

    public static EmptyString instance = new EmptyString();

    private EmptyString() {
        super("");
    }

    @Override
    public long getSize() {
        return 0;
    }

    @Override
    public boolean getEmpty() {
        return true;
    }

    @Override
    public Iterable<? extends Character, ? extends java.lang.Object> getRest() {
        return this;
    }

    @Override
    public Character getFirst() {
        return null;
    }
    @Override public Character getLast() {
        return null;
    }

    @Override
    @Ignore
    public Sequential<? extends Character> getSequence() {
        return this.getSequence();
    }
    @Override @Ignore
    public Character find(Callable<? extends Boolean> f) {
        return null;
    }
    @Override @Ignore
    public Character findLast(Callable<? extends Boolean> f) {
        return null;
    }
    @Override
    @Ignore
    public Sequential<? extends Character> sort(Callable<? extends Comparison> f) {
        return (Sequential)empty_.getEmpty$();
    }
    @Override
    @Ignore
    public Iterable<? extends Character, ? extends java.lang.Object> filter(Callable<? extends Boolean> f) {
        return this;
    }
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override @Ignore
    public <Result> Sequential<? extends Result> collect(Callable<? extends Result> f) {
        return (Sequential)empty_.getEmpty$();
    }
    @Override @Ignore
    public Sequential<? extends Character> select(Callable<? extends Boolean> f) {
        return (Sequential)empty_.getEmpty$();
    }
    @Override
    @Ignore
    public <Result> Result fold(Result ini, Callable<? extends Result> f) {
        return ini;
    }
    @Override @Ignore
    public boolean any(Callable<? extends Boolean> f) {
        return false;
    }
    @Override @Ignore
    public boolean every(Callable<? extends Boolean> f) {
        return false;
    }
    @Override @Ignore
    public Iterable<? extends Character, ? extends java.lang.Object> skipping(long skip) {
        return this;
    }
    @Override @Ignore
    public Iterable<? extends Character, ? extends java.lang.Object> taking(long take) {
        return this;
    }
    @Override @Ignore
    public Iterable<? extends Character, ? extends java.lang.Object> by(long step) {
        return this;
    }
    @Override @Ignore
    public long count(Callable<? extends Boolean> f) {
        return 0;
    }
    @Override @Ignore
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Iterable<? extends Entry<? extends Integer, ? extends Character>, ? extends java.lang.Object> getIndexed() { return (Iterable)this; }
    @SuppressWarnings("rawtypes")
    @Override @Ignore public <Other>Iterable chain(Iterable<? extends Other, ? extends java.lang.Object> other) { return other; }
    @Override @Ignore
    public <Key> Map<? extends Key, ? extends Sequence<? extends Character>> group(Callable<? extends Key> grouping) {
        return new InternalMap<Key, Sequence<? extends Character>>(java.util.Collections.<Key,Sequence<Character>>emptyMap());
    }

    @SuppressWarnings("rawtypes")
    @Override @Ignore public <Other>Sequence withLeading(Other e) {
        return $ceylon$language$List$this.withLeading(e);
    }
    @SuppressWarnings("rawtypes")
    @Override @Ignore public <Other>Sequence withTrailing(Other e) {
        return $ceylon$language$List$this.withTrailing(e);
    }
}
