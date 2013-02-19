package com.redhat.ceylon.compiler.java.language;

import java.util.Arrays;

import ceylon.language.Array;
import ceylon.language.Boolean;
import ceylon.language.Callable;
import ceylon.language.Category;
import ceylon.language.Category$impl;
import ceylon.language.Cloneable$impl;
import ceylon.language.Collection$impl;
import ceylon.language.Comparison;
import ceylon.language.Container$impl;
import ceylon.language.Correspondence$impl;
import ceylon.language.Entry;
import ceylon.language.Integer;
import ceylon.language.Iterable;
import ceylon.language.Iterable$impl;
import ceylon.language.Iterator;
import ceylon.language.Iterator$impl;
import ceylon.language.List;
import ceylon.language.List$impl;
import ceylon.language.Map;
import ceylon.language.Null;
import ceylon.language.Ranged;
import ceylon.language.Ranged$impl;
import ceylon.language.Sequence;
import ceylon.language.Sequence$impl;
import ceylon.language.Sequential;
import ceylon.language.Sequential$impl;
import ceylon.language.empty_;
import ceylon.language.finished_;

import com.redhat.ceylon.compiler.java.metadata.Annotation;
import com.redhat.ceylon.compiler.java.metadata.Annotations;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ignore
@Ceylon(major = 4)
@Class(extendsType="ceylon.language::Object")
@SatisfiedTypes("ceylon.language::Sequence<Element>")
public class ArraySequence<Element> implements Sequence<Element>, ReifiedType {
    private final ceylon.language.Category$impl $ceylon$language$Category$this;
    private final ceylon.language.Container$impl<Element,java.lang.Object> $ceylon$language$Container$this;
    private final ceylon.language.Iterable$impl<Element,java.lang.Object> $ceylon$language$Iterable$this;
    private final ceylon.language.Collection$impl<Element> $ceylon$language$Collection$this;
    private final ceylon.language.Correspondence$impl<Integer,Element> $ceylon$language$Correspondence$this;
    private final ceylon.language.List$impl<Element> $ceylon$language$List$this;
    private final ceylon.language.Sequential$impl<Element> $ceylon$language$Sequential$this;
    private final ceylon.language.Sequence$impl<Element> $ceylon$language$Sequence$this;
    private final ceylon.language.Ranged$impl<Integer,List<? extends Element>> $ceylon$language$Ranged$this;
    private final ceylon.language.Cloneable$impl $ceylon$language$Cloneable$this;

    protected final Element[] array;
    protected final long first;
    @Ignore
    private TypeDescriptor $reifiedElement;

    public ArraySequence(@Ignore TypeDescriptor $reifiedElement, Element... array) {
        this($reifiedElement, array,0);
    }

    @Ignore
    public ArraySequence(@Ignore TypeDescriptor $reifiedElement, Element[] array, long first) {
        this.$ceylon$language$Category$this = new ceylon.language.Category$impl(this);
        this.$ceylon$language$Container$this = new ceylon.language.Container$impl<Element,java.lang.Object>($reifiedElement, TypeDescriptor.NothingType, this);
        this.$ceylon$language$Iterable$this = new ceylon.language.Iterable$impl<Element,java.lang.Object>($reifiedElement, TypeDescriptor.NothingType, this);
        this.$ceylon$language$Collection$this = new ceylon.language.Collection$impl<Element>($reifiedElement, this);
        this.$ceylon$language$Correspondence$this = new ceylon.language.Correspondence$impl<Integer,Element>(Integer.$TypeDescriptor, $reifiedElement, this);
        this.$ceylon$language$List$this = new ceylon.language.List$impl<Element>($reifiedElement, this);
        this.$ceylon$language$Sequence$this = new ceylon.language.Sequence$impl<Element>($reifiedElement, this);
        this.$ceylon$language$Sequential$this = new ceylon.language.Sequential$impl<Element>($reifiedElement, this);
        this.$ceylon$language$Ranged$this = new ceylon.language.Ranged$impl<Integer,List<? extends Element>>(Integer.$TypeDescriptor, TypeDescriptor.klass(Sequence.class, $reifiedElement), (Ranged)this);
        this.$ceylon$language$Cloneable$this = new ceylon.language.Cloneable$impl(TypeDescriptor.klass(Sequence.class, $reifiedElement), this);
    	if (array.length==0 || array.length<=first) {
    		throw new IllegalArgumentException("ArraySequence may not have zero elements");
    	}
        this.array = array;
        this.first = first;
        this.$reifiedElement = $reifiedElement;
    }

    @Ignore
    public ArraySequence(@Ignore TypeDescriptor $reifiedElement, java.util.List<Element> list) {
        this($reifiedElement, (Element[]) list.toArray(), 0);
    }

    @Ignore
    @Override
    public Category$impl $ceylon$language$Category$impl(){
        return $ceylon$language$Category$this;
    }

    @Ignore
    @Override
    public Container$impl<Element,java.lang.Object> $ceylon$language$Container$impl(){
        return $ceylon$language$Container$this;
    }

    @Ignore
    @Override
    public Iterable$impl<Element,java.lang.Object> $ceylon$language$Iterable$impl(){
        return $ceylon$language$Iterable$this;
    }

    @Ignore
    @Override
    public Collection$impl<Element> $ceylon$language$Collection$impl(){
        return $ceylon$language$Collection$this;
    }

    @Ignore
    @Override
    public List$impl<Element> $ceylon$language$List$impl(){
        return $ceylon$language$List$this;
    }

    @Ignore
    @Override
    public Correspondence$impl<Integer,Element> $ceylon$language$Correspondence$impl(){
        return $ceylon$language$Correspondence$this;
    }

    @Ignore
    @Override
    public Ranged$impl $ceylon$language$Ranged$impl(){
        return $ceylon$language$Ranged$this;
    }

    @Ignore
    @Override
    public Sequential$impl<Element> $ceylon$language$Sequential$impl(){
        return $ceylon$language$Sequential$this;
    }

    @Ignore
    @Override
    public Sequence$impl<Element> $ceylon$language$Sequence$impl(){
        return $ceylon$language$Sequence$this;
    }

    @Ignore
    @Override
    public Cloneable$impl $ceylon$language$Cloneable$impl(){
        return $ceylon$language$Cloneable$this;
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
            return new ArraySequence<Element>($reifiedElement, array, first + 1);
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
        		return new ArraySequence<Element>($reifiedElement, array, fromIndex);
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
        return new ArraySequence<Element>($reifiedElement, sub, 0);
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
            return new ArraySequence<Element>($reifiedElement, array, fromIndex);
        }
        else {
            return new ArraySequence<Element>($reifiedElement, Arrays.copyOfRange(array,
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
		return new ArraySequence<Element>($reifiedElement, reversed, 0);
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

    public class ArrayListIterator extends AbstractIterator<Element> {

        public ArrayListIterator() {
            super($reifiedElement);
        }

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
    public Element get(Integer key) {
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
    public boolean definesEvery(Iterable<? extends Integer,? extends java.lang.Object> keys) {
        return $ceylon$language$Correspondence$this.definesEvery(keys);
    }

//    @Override
//    @Ignore @SuppressWarnings({"unchecked", "rawtypes"})
//    public boolean definesEvery() {
//        return $ceylon$language$Correspondence$this.definesEvery((Sequential)empty_.getEmpty$());
//    }
//    @Override
//    @Ignore @SuppressWarnings({"unchecked", "rawtypes"})
//    public Sequential<? extends Integer> definesEvery$keys() {
//        return (Sequential)empty_.getEmpty$();
//    }

    @Override
    @Ignore
    public boolean definesAny(Iterable<? extends Integer,? extends java.lang.Object> keys) {
        return $ceylon$language$Correspondence$this.definesAny(keys);
    }

//    @Override
//    @Ignore @SuppressWarnings({"unchecked", "rawtypes"})
//    public boolean definesAny() {
//        return $ceylon$language$Correspondence$this.definesAny((Sequential)empty_.getEmpty$());
//    }

//    @Override @SuppressWarnings({"unchecked", "rawtypes"})
//    public Sequential<? extends Integer> definesAny$keys() {
//        return (Sequential)empty_.getEmpty$();
//    }

    @Override
    @Ignore
    public ceylon.language.Sequential<? extends Element> items(Iterable<? extends Integer,? extends java.lang.Object> keys) {
        return $ceylon$language$Correspondence$this.items(keys);
    }

//    @Override
//    @Ignore @SuppressWarnings({"unchecked", "rawtypes"})
//    public ceylon.language.Sequential<? extends Element> items() {
//        return $ceylon$language$Correspondence$this.items((Sequential)empty_.getEmpty$());
//    }
//    @Override @SuppressWarnings({"unchecked", "rawtypes"})
//    public Sequential<? extends Integer> items$keys() {
//        return (Sequential)empty_.getEmpty$();
//    }

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
    public boolean containsEvery(Iterable<?,?> elements) {
        return $ceylon$language$Category$this.containsEvery(elements);
    }

//    @Override
//    @Ignore
//    public boolean containsEvery() {
//        return $ceylon$language$Category$this.containsEvery(empty_.getEmpty$());
//    }
//
//    @Override
//    @Ignore
//    public Sequential<?>containsEvery$elements() {
//        return empty_.getEmpty$();
//    }

    @Override
    @Ignore
    public boolean containsAny(Iterable<?,?> elements) {
        return $ceylon$language$Category$this.containsAny(elements);
    }

//    @Override
//    @Ignore
//    public boolean containsAny() {
//        return $ceylon$language$Category$this.containsAny(empty_.getEmpty$());
//    }

//  @Override
//  @Ignore
//  public Sequential<?>containsAny$elements() {
//      return empty_.getEmpty$();
//  }
//
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
        return $ceylon$language$Iterable$this.findLast(f);
    }
    @Override
    @Ignore
    public Sequence<? extends Element> sort(Callable<? extends Comparison> f) {
        return $ceylon$language$Sequence$this.sort(f);
    }

    @Override
    public <Result> Iterable<? extends Result, ? extends java.lang.Object> map(@Ignore TypeDescriptor $reifiedResult, Callable<? extends Result> f) {
        return new MapIterable<Element, Result>($reifiedElement, $reifiedResult, this, f);
    }
    @Override
    public Iterable<? extends Element, ? extends java.lang.Object> filter(Callable<? extends Boolean> f) {
        return new FilterIterable<Element,  Null>($reifiedElement, Null.$TypeDescriptor, this, f);
    }
    @Override
    public <Result> Sequence<? extends Result> collect(@Ignore TypeDescriptor $reifiedResult, Callable<? extends Result> f) {
        return $ceylon$language$Sequence$this.collect($reifiedResult, f);
    }

    @Override
    public Sequential<? extends Element> select(Callable<? extends Boolean> f) {
        return new FilterIterable<Element,  Null>($reifiedElement, Null.$TypeDescriptor, this, f).getSequence();
    }

    @Override
    @Ignore
    public <Result> Result fold(@Ignore TypeDescriptor $reifiedResult, Result ini, Callable<? extends Result> f) {
        return $ceylon$language$Iterable$this.fold($reifiedResult, ini, f);
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
    public Iterable<? extends Element, ? extends java.lang.Object> skipping(long skip) {
        return $ceylon$language$Iterable$this.skipping(skip);
    }
    @Override @Ignore
    public Iterable<? extends Element, ? extends java.lang.Object> taking(long take) {
        return $ceylon$language$Iterable$this.taking(take);
    }
    @Override @Ignore
    public Iterable<? extends Element, ? extends java.lang.Object> by(long step) {
        return $ceylon$language$Iterable$this.by(step);
    }
    @Override @Ignore
    public Iterable<? extends Element, ? extends java.lang.Object> getCoalesced() {
        return $ceylon$language$Iterable$this.getCoalesced();
    }
    @Override @Ignore
    public Iterable<? extends Entry<? extends Integer, ? extends Element>, ? extends java.lang.Object> getIndexed() {
        return $ceylon$language$Iterable$this.getIndexed();
    }
    @SuppressWarnings("rawtypes")
    @Override @Ignore 
    public <Other>Iterable chain(@Ignore TypeDescriptor $reifiedOther, Iterable<? extends Other, ? extends java.lang.Object> other) {
        return $ceylon$language$Iterable$this.chain($reifiedOther, other);
    }
    @Override @Ignore
    public <Default>Iterable<?,?> defaultNullElements(@Ignore TypeDescriptor $reifiedDefault, Default defaultValue) {
        return $ceylon$language$Iterable$this.defaultNullElements($reifiedDefault, defaultValue);
    }
    /*@Override @Ignore
    public <Key> Map<? extends Key, ? extends Sequence<? extends Element>> group(Callable<? extends Key> grouping) {
        return $ceylon$language$Iterable$this.group(grouping);
    }*/

    @Override
    @Annotations({ @Annotation("actual") })
    @SuppressWarnings("rawtypes")
    public <Other>Sequence withLeading(@Ignore TypeDescriptor $reifiedOther, Other e) {
        return $ceylon$language$List$this.withLeading($reifiedOther, e);
    }
    @Override
    @Annotations({ @Annotation("actual") })
    @SuppressWarnings("rawtypes")
    public <Other>Sequence withTrailing(@Ignore TypeDescriptor $reifiedOther, Other e) {
        return $ceylon$language$List$this.withTrailing($reifiedOther, e);
    }

    @Override
    @Ignore
    public TypeDescriptor $getType() {
        return TypeDescriptor.klass(ArraySequence.class, $reifiedElement);
    }
}
