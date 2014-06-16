package com.redhat.ceylon.compiler.java.language;

import ceylon.language.Boolean;
import ceylon.language.Callable;
import ceylon.language.Category$impl;
import ceylon.language.Comparison;
import ceylon.language.Entry;
import ceylon.language.Integer;
import ceylon.language.Iterable;
import ceylon.language.Iterable$impl;
import ceylon.language.Iterator;
import ceylon.language.List;
import ceylon.language.Null;
import ceylon.language.Object;
import ceylon.language.Sequential;
import ceylon.language.finished_;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 7)
@Class
@SatisfiedTypes("ceylon.language::Iterable<ceylon.language::Integer,ceylon.language::Null>")
public class StringInclusions implements Iterable<Integer,java.lang.Object>, ReifiedType {
    @Ignore
    private final Iterable$impl<Integer,java.lang.Object> 
    $ceylon$language$Iterable$this;
    @Ignore
    private final Category$impl<java.lang.Object> 
    $ceylon$language$Category$this;
    
    private final java.lang.String str;
    private final java.lang.String oc;

    public StringInclusions(java.lang.String str, java.lang.String oc) {
        this.$ceylon$language$Iterable$this = 
        		new Iterable$impl<Integer,java.lang.Object>(Integer.$TypeDescriptor$, 
        				Null.$TypeDescriptor$, this);
        this.$ceylon$language$Category$this = 
        		new Category$impl<java.lang.Object>(Object.$TypeDescriptor$,this);
        this.str = str;
        this.oc = oc;
    }

    @Ignore
    @Override
    public Category$impl<java.lang.Object> 
    $ceylon$language$Category$impl(){
        return $ceylon$language$Category$this;
    }

    @Ignore
    @Override
    public Iterable$impl<Integer,java.lang.Object> 
    $ceylon$language$Iterable$impl(){
        return $ceylon$language$Iterable$this;
    }

    @Override
    public Iterator<? extends Integer> iterator() {
        class InclusionIterator extends AbstractIterator<Integer> 
        implements ReifiedType {
            public InclusionIterator() {
                super(Integer.$TypeDescriptor$);
            }

            private int pos=0;

            @Override
            public java.lang.Object next() {
                int idx = str.indexOf(oc, pos);
                if (idx >= pos) {
                    pos = idx+oc.length();
                    return Integer.instance(idx);
                }
                return finished_.get_();
            }

            @Override
            @Ignore
            public TypeDescriptor $getType$() {
                return TypeDescriptor.klass(InclusionIterator.class);
            }
        }

        return new InclusionIterator();
    }
    
    @Override
    public java.lang.String toString() {
        return $ceylon$language$Iterable$this.toString();
    }

    @Override
    public long getSize() {
        return $ceylon$language$Iterable$this.getSize();
    }

    @Override
    public boolean getEmpty() {
        return iterator().next() == finished_.get_();
    }
    
    @Override
    @Ignore
    public Iterable<? extends Integer,?> getCycled() {
        return $ceylon$language$Iterable$this.getCycled();
    }

    @Override
    @Ignore
    public Iterable<? extends Integer,?> cycle(long times) {
        return $ceylon$language$Iterable$this.cycle(times);
    }
    
    @Override
    @Ignore
    public List<? extends Integer> repeat(long times) {
        return $ceylon$language$Iterable$this.repeat(times);
    }
    
    @Override
    @Ignore
    public Integer getFirst() {
    	return (Integer) $ceylon$language$Iterable$this.getFirst();
    }
    @Override @Ignore
    public Integer getLast() {
        return (Integer) $ceylon$language$Iterable$this.getLast();
    }

    @Override
    @Ignore
    public Iterable<? extends Integer, ?> 
    getRest() {
    	return $ceylon$language$Iterable$this.getRest();
    }

    @Override @Ignore
    public Iterable<? extends Integer, ?> 
    takeWhile(Callable<? extends Boolean> take) {
        return $ceylon$language$Iterable$this.takeWhile(take);
    }
    
    @Override @Ignore
    public Iterable<? extends Integer, ?> 
    skipWhile(Callable<? extends Boolean> skip) {
        return $ceylon$language$Iterable$this.skipWhile(skip);
    }
    
    @Override
    @Ignore
    public Sequential<? extends Integer> sequence() {
        return $ceylon$language$Iterable$this.sequence();
    }
    @Override @Ignore
    public Integer find(Callable<? extends Boolean> f) {
        return $ceylon$language$Iterable$this.find(f);
    }
    @Override @Ignore
    public Integer findLast(Callable<? extends Boolean> f) {
        return $ceylon$language$Iterable$this.findLast(f);
    }
    @Override
    @Ignore
    public Sequential<? extends Integer> 
    sort(Callable<? extends Comparison> f) {
        return $ceylon$language$Iterable$this.sort(f);
    }
    @Override
    @Ignore
    public <Result> Iterable<? extends Result, ?> 
    map(@Ignore TypeDescriptor $reifiedResult, Callable<? extends Result> f) {
        return $ceylon$language$Iterable$this.map($reifiedResult, f);
    }
    @Override
    @Ignore
    public Iterable<? extends Integer, ?> 
    filter(Callable<? extends Boolean> f) {
        return $ceylon$language$Iterable$this.filter(f);
    }
    @Override @Ignore
    public <Result> Sequential<? extends Result> 
    collect(@Ignore TypeDescriptor $reifiedResult, 
    		Callable<? extends Result> f) {
        return $ceylon$language$Iterable$this.collect($reifiedResult, f);
    }
    @Override @Ignore
    public Sequential<? extends Integer> 
    select(Callable<? extends Boolean> f) {
        return $ceylon$language$Iterable$this.select(f);
    }
    @Override
    @Ignore
    public <Result> Result 
    fold(@Ignore TypeDescriptor $reifiedResult, 
    		Result ini, Callable<? extends Result> f) {
        return $ceylon$language$Iterable$this.fold($reifiedResult, ini, f);
    }
    @Override
    @Ignore
    public <Result> java.lang.Object 
    reduce(@Ignore TypeDescriptor 
    		$reifiedResult, Callable<? extends Result> f) {
        return $ceylon$language$Iterable$this.reduce($reifiedResult, f);
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
    public Iterable<? extends Integer, ?> 
    skip(long n) {
        return $ceylon$language$Iterable$this.skip(n);
    }
    @Override @Ignore
    public Iterable<? extends Integer, ?> 
    take(long n) {
        return $ceylon$language$Iterable$this.take(n);
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
    public Iterable<? extends Integer, ?> 
    by(long n) {
        return $ceylon$language$Iterable$this.by(n);
    }
    @Override @Ignore
    public long count(Callable<? extends Boolean> f) {
        return $ceylon$language$Iterable$this.count(f);
    }
    @Override @Ignore
    public Iterable<? extends Integer, ?> 
    getCoalesced() {
        return $ceylon$language$Iterable$this.getCoalesced();
    }
    @Override @Ignore
    public Iterable<? extends Entry<? extends Integer, ? extends Integer>, 
    		?> 
    getIndexed() {
        return $ceylon$language$Iterable$this.getIndexed();
    }
    @Override @Ignore 
    public <Other,Absent>Iterable 
    chain(@Ignore TypeDescriptor $reifiedOther, @Ignore TypeDescriptor $reifiedOtherAbsent, 
    		Iterable<? extends Other, ? extends Absent> other) {
        return $ceylon$language$Iterable$this.chain($reifiedOther, 
        		$reifiedOtherAbsent, other);
    }
    @Override @Ignore 
    public <Other> Iterable 
    following(@Ignore TypeDescriptor $reifiedOther, Other other) {
        return $ceylon$language$Iterable$this.following($reifiedOther, other);
    }
    @Override @Ignore
    public <Default>Iterable<?,?> 
    defaultNullElements(@Ignore TypeDescriptor $reifiedDefault, 
    		Default defaultValue) {
        return $ceylon$language$Iterable$this.defaultNullElements($reifiedDefault, 
        		defaultValue);
    }
    @Override @Ignore
    public boolean contains(@Name("element") java.lang.Object element) {
        return $ceylon$language$Iterable$this.contains(element);
    }
    @Override @Ignore
    public boolean containsEvery(
            @Name("elements") 
            @TypeInfo("ceylon.language::Iterable<ceylon.language::Object,ceylon.language::Null>") 
            Iterable<?,?> elements) {
        return $ceylon$language$Category$this.containsEvery(elements);
    }
    @Override @Ignore
    public boolean containsAny(
            @Name("elements") 
            @TypeInfo("ceylon.language::Iterable<ceylon.language::Object,ceylon.language::Null>") 
            Iterable<?,?> elements) {
        return $ceylon$language$Category$this.containsAny(elements);
    }
    @Override @Ignore
    public final <Result,Args extends Sequential<? extends java.lang.Object>> Callable<? extends Iterable<? extends Result, ?>>
    spread(TypeDescriptor $reifiedResult,TypeDescriptor $reifiedArgs, Callable<? extends Callable<? extends Result>> method) {
    	return $ceylon$language$Iterable$this.spread($reifiedResult, $reifiedArgs, method);
    }
    
    @Override
    @Ignore
    public TypeDescriptor $getType$() {
        return TypeDescriptor.klass(StringInclusions.class);
    }
}