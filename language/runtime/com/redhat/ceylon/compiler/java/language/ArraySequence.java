package com.redhat.ceylon.compiler.java.language;

import static java.lang.Long.MAX_VALUE;

import java.util.Arrays;

import ceylon.language.Boolean;
import ceylon.language.Callable;
import ceylon.language.Category;
import ceylon.language.Comparison;
import ceylon.language.Correspondence$impl;
import ceylon.language.Entry;
import ceylon.language.Integer;
import ceylon.language.Iterable;
import ceylon.language.Iterator;
import ceylon.language.Sequential;
import ceylon.language.Map;
import ceylon.language.Sequence;
import ceylon.language.empty_;
import ceylon.language.finished_;

import com.redhat.ceylon.compiler.java.metadata.Annotation;
import com.redhat.ceylon.compiler.java.metadata.Annotations;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ignore
@Ceylon(major = 3)
@Class(extendsType="ceylon.language::Object")
@SatisfiedTypes("ceylon.language::Sequence<Element>")
public class ArraySequence<Element> implements Sequence<Element> {
    private final ceylon.language.Category$impl $ceylon$language$Category$this;
    private final ceylon.language.Collection$impl $ceylon$language$Collection$this;
    private final ceylon.language.Correspondence$impl $ceylon$language$Correspondence$this;
    private final ceylon.language.Iterable$impl<Element> $ceylon$language$Iterable$this;
    private final ceylon.language.List$impl<Element> $ceylon$language$List$this;
    private final ceylon.language.Sequence$impl<Element> $ceylon$language$Sequence$this;

    protected final Element[] array;
    protected final long first;

    public ArraySequence(Element... array) {
        this(array,0);
    }

    @Ignore
    public ArraySequence(Element[] array, long first) {
        this.$ceylon$language$Category$this = new ceylon.language.Category$impl(this);
        this.$ceylon$language$Collection$this = new ceylon.language.Collection$impl(this);
        this.$ceylon$language$Correspondence$this = new ceylon.language.Correspondence$impl(this);
        this.$ceylon$language$Iterable$this = new ceylon.language.Iterable$impl<Element>(this);
        this.$ceylon$language$List$this = new ceylon.language.List$impl<Element>(this);
        this.$ceylon$language$Sequence$this = new ceylon.language.Sequence$impl<Element>(this);
    	if (array.length==0 || array.length<=first) {
    		throw new IllegalArgumentException("ArraySequence may not have zero elements");
    	}
        this.array = array;
        this.first = first;
    }

    @Ignore
    public ArraySequence(java.util.List<Element> list) {
        this.$ceylon$language$Category$this = new ceylon.language.Category$impl(this);
        this.$ceylon$language$Collection$this = new ceylon.language.Collection$impl(this);
        this.$ceylon$language$Correspondence$this = new ceylon.language.Correspondence$impl(this);
        this.$ceylon$language$Iterable$this = new ceylon.language.Iterable$impl<Element>(this);
        this.$ceylon$language$List$this = new ceylon.language.List$impl<Element>(this);
        this.$ceylon$language$Sequence$this = new ceylon.language.Sequence$impl<Element>(this);
    	if (list.size()==0) {
    		throw new IllegalArgumentException("ArraySequence may not have zero elements");
    	}
        this.array = (Element[]) list.toArray();
        this.first = 0;
    }

    private Correspondence$impl<Integer,Element> correspondence$impl = new Correspondence$impl<Integer,Element>(this);
    
    @Ignore
    @Override
    public Correspondence$impl<? super Integer,? extends Element> $ceylon$language$Correspondence$impl(){
        return correspondence$impl;
    }

    @Override
    @Ignore
    public Correspondence$impl<? super Integer, ? extends Element>.Items Items$new(Sequence<? extends Integer> keys) {
        return correspondence$impl.Items$new(keys);
    }

    @Override
    public Element getFirst() {
        return array[(int) first];
    }

    @Override
    public Sequential<? extends Element> getRest() {
        if (first+1==array.length) {
            return (Sequential)empty_.getEmpty$();
        }
        else {
            return new ArraySequence<Element>(array, first + 1);
        }
    }

    @Override
    public boolean getEmpty() {
        return false;
    }

    @Override
    public Element getLast() {
        return array[array.length - 1];
    }

    @Override
    public Sequential<? extends Element> spanTo(Integer to) {
        return to.longValue() < 0 ? (Sequential)empty_.getEmpty$() : span(Integer.instance(0), to);
    }
    @Override
    public Sequential<? extends Element> spanFrom(Integer from) {
        return span(from, Integer.instance(getSize()));
    }

    @Override
    public Sequential<? extends Element> span(Integer from, Integer to) {
        long fromIndex = from.longValue();
        long toIndex = to==null ? getSize() : to.longValue();
        long lastIndex = getLastIndex().longValue();
        
        boolean reverse = toIndex<fromIndex;
        if (reverse) {
        	if (fromIndex<0 || toIndex>lastIndex) {
        		return (Sequential)empty_.getEmpty$();
        	}
        	if (toIndex<0) {
        		toIndex=0;
        	}
        	if (fromIndex>lastIndex) {
        		fromIndex = lastIndex;
        	}
        }
        else {
        	if (toIndex<0 || fromIndex>lastIndex) {
        		return (Sequential)empty_.getEmpty$();
        	}
        	if (fromIndex<0) {
        		fromIndex=0;
        	}
        	if (toIndex>=lastIndex) {
        		return new ArraySequence<Element>(array, fromIndex);
        	}
        }

        final Element[] sub;
        if (reverse) {
        	sub = Arrays.copyOfRange(array,
        			(int)toIndex, (int)fromIndex+1);
        	for (int i = 0, j=(int)fromIndex; i < sub.length; i++, j--) {
        		sub[i] = array[j];
        	}
        } 
        else {
        	sub = Arrays.copyOfRange(array,
        			(int)fromIndex, (int)toIndex+1);
        }
        return new ArraySequence<Element>(sub, 0);
    }

    @Override
    public Sequential<? extends Element> segment(Integer from, long length) {
        long fromIndex = from.longValue();
        if (fromIndex<0) fromIndex=0;
        long resultLength = length;
        long lastIndex = getLastIndex().longValue();
        if (fromIndex>lastIndex||resultLength<=0) {
            return (Sequential)empty_.getEmpty$();
        }
        else if (fromIndex+resultLength>lastIndex) {
            return new ArraySequence<Element>(array, fromIndex);
        }
        else {
            return new ArraySequence<Element>(Arrays.copyOfRange(array,
                    (int)fromIndex, (int)(fromIndex + resultLength)), 0);
        }
    }

    @Override
    @TypeInfo("ceylon.language::Integer")
    public Integer getLastIndex() {
        return Integer.instance(array.length - first - 1);
    }

    @Override
    @TypeInfo("ceylon.language::Integer")
    public long getSize() {
        return array.length - first;
    }

    @Override
    public ArraySequence<? extends Element> getReversed() {
    	Element[] reversed = (Element[]) java.lang.reflect.Array.newInstance(array.getClass()
    			.getComponentType(), array.length);
    	for (int i=0; i<array.length; i++) {
    		reversed[array.length-1-i] = array[i];
    	}
		return new ArraySequence<Element>(reversed, 0);
    }

    @Override
    public boolean defines(Integer key) {
        long ind = key.longValue();
        return ind>=0 && ind+first<array.length;
    }

    @Override
    public Iterator<Element> getIterator() {
        return new ArrayListIterator();
    }

    public class ArrayListIterator
            implements Iterator<Element> {
        private long idx = first;

        @Override
        public java.lang.Object next() {
            if (idx <= getLastIndex().longValue()+first) {
                return array[(int) idx++];
            }
            else {
                return finished_.getFinished$();
            }
        }

        @Override
        public java.lang.String toString() {
            return "ArrayArrayListIterator";
        }

    }

    @Override
    public Element item(Integer key) {
        long index = key.longValue()+first;
        return index<0 || index >= array.length ?
                null : array[(int) index];
    }

    @Override
    @Ignore
    public Category getKeys() {
        return $ceylon$language$Correspondence$this.getKeys();
    }

    @Override
    @Ignore
    public boolean definesEvery(Sequential<? extends Integer> keys) {
        return $ceylon$language$Correspondence$this.definesEvery(keys);
    }

    @Override
    @Ignore @SuppressWarnings({"unchecked", "rawtypes"})
    public boolean definesEvery() {
        return $ceylon$language$Correspondence$this.definesEvery((Sequential)empty_.getEmpty$());
    }
    @Override
    @Ignore @SuppressWarnings({"unchecked", "rawtypes"})
    public Sequential<? extends Integer> definesEvery$keys() {
        return (Sequential)empty_.getEmpty$();
    }

    @Override
    @Ignore
    public boolean definesAny(Sequential<? extends Integer> keys) {
        return $ceylon$language$Correspondence$this.definesAny(keys);
    }

    @Override
    @Ignore @SuppressWarnings({"unchecked", "rawtypes"})
    public boolean definesAny() {
        return $ceylon$language$Correspondence$this.definesAny((Sequential)empty_.getEmpty$());
    }

    @Override @SuppressWarnings({"unchecked", "rawtypes"})
    public Sequential<? extends Integer> definesAny$keys() {
        return (Sequential)empty_.getEmpty$();
    }

    @Override
    @Ignore
    public ceylon.language.Sequential<? extends Element> items(Sequential<? extends Integer> keys) {
        return $ceylon$language$Correspondence$this.items(keys);
    }

    @Override
    @Ignore @SuppressWarnings({"unchecked", "rawtypes"})
    public ceylon.language.Sequential<? extends Element> items() {
        return $ceylon$language$Correspondence$this.items((Sequential)empty_.getEmpty$());
    }
    @Override @SuppressWarnings({"unchecked", "rawtypes"})
    public Sequential<? extends Integer> items$keys() {
        return (Sequential)empty_.getEmpty$();
    }

    @Override
    public ArraySequence<Element> getClone() {
        return this;
    }

    @Override
    @Ignore
    public java.lang.String toString() {
        return $ceylon$language$Collection$this.toString();
    }

    @Override
    @Ignore
    public boolean equals(java.lang.Object that) {
        return $ceylon$language$List$this.equals(that);
    }

    @Override
    @Ignore
    public int hashCode() {
        return $ceylon$language$List$this.hashCode();
    }

    @Override
    public boolean contains(java.lang.Object element) {
        for (Element x: array) {
            if (x!=null && element.equals(x)) return true;
        }
        return false;
    }

    @Override
    public long count(Callable<? extends Boolean> f) {
        int count=0;
        for (Element x: array) {
            if (x!=null && f.$call(x).booleanValue()) count++;
        }
        return count;
    }

    @Override
    @Ignore
    public boolean containsEvery(Sequential<?> elements) {
        return $ceylon$language$Category$this.containsEvery(elements);
    }

    @Override
    @Ignore
    public boolean containsEvery() {
        return $ceylon$language$Category$this.containsEvery(empty_.getEmpty$());
    }

    @Override
    @Ignore
    public Sequential<?>containsEvery$elements() {
        return empty_.getEmpty$();
    }

    @Override
    @Ignore
    public boolean containsAny(Sequential<?> elements) {
        return $ceylon$language$Category$this.containsAny(elements);
    }

    @Override
    @Ignore
    public boolean containsAny() {
        return $ceylon$language$Category$this.containsAny(empty_.getEmpty$());
    }

    @Override
    @Ignore
    public Sequence<? extends Element> getSequence() {
        return $ceylon$language$Sequence$this.getSequence();
    }

    @Override @Ignore
    public Element find(Callable<? extends Boolean> f) {
        return $ceylon$language$Iterable$this.find(f);
    }
    @Override @Ignore
    public Element findLast(Callable<? extends Boolean> f) {
        return $ceylon$language$List$this.findLast(f);
    }
    @Override
    @Ignore
    public Sequence<? extends Element> sort(Callable<? extends Comparison> f) {
        return $ceylon$language$Sequence$this.sort(f);
    }

    @Override
    public <Result> Iterable<? extends Result> map(Callable<? extends Result> f) {
        return new MapIterable<Element, Result>(this, f);
    }
    @Override
    public Iterable<? extends Element> filter(Callable<? extends Boolean> f) {
        return new FilterIterable<Element>(this, f);
    }
    @Override
    public <Result> Sequence<? extends Result> collect(Callable<? extends Result> f) {
        return $ceylon$language$Sequence$this.collect(f);
    }

    @Override
    public Sequential<? extends Element> select(Callable<? extends Boolean> f) {
        return new FilterIterable<Element>(this, f).getSequence();
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
    public Iterable<? extends Element> getCoalesced() {
        return $ceylon$language$Iterable$this.getCoalesced();
    }
    @Override @Ignore
    public Iterable<? extends Entry<? extends Integer, ? extends Element>> getIndexed() {
        return $ceylon$language$Iterable$this.getIndexed();
    }
    @SuppressWarnings("rawtypes")
    @Override @Ignore public <Other>Iterable chain(Iterable<? extends Other> other) {
        return $ceylon$language$Iterable$this.chain(other);
    }
    @Override @Ignore
    public <Key> Map<? extends Key, ? extends Sequence<? extends Element>> group(Callable<? extends Key> grouping) {
        return $ceylon$language$Iterable$this.group(grouping);
    }

    @Override
    @Ignore
    public Sequential<?>containsAny$elements() {
        return empty_.getEmpty$();
    }

    @Override
    @Annotations({ @Annotation("actual") })
    @SuppressWarnings("rawtypes")
    public <Other>Sequence withLeading(Other e) {
        return $ceylon$language$List$this.withLeading(e);
    }
    @Override
    @Annotations({ @Annotation("actual") })
    @SuppressWarnings("rawtypes")
    public <Other>Sequence withTrailing(Other e) {
        return $ceylon$language$List$this.withTrailing(e);
    }
}
