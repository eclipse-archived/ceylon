package com.redhat.ceylon.compiler.java.language;

import ceylon.language.ArraySequence;
import ceylon.language.Boolean;
import ceylon.language.Callable;
import ceylon.language.Category$impl;
import ceylon.language.Comparison;
import ceylon.language.Container$impl;
import ceylon.language.Entry;
import ceylon.language.Integer;
import ceylon.language.Iterable;
import ceylon.language.Iterable$impl;
import ceylon.language.Iterator;
import ceylon.language.List;
import ceylon.language.Null;
import ceylon.language.Sequence;
import ceylon.language.Sequential;
import ceylon.language.finished_;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ignore
@Ceylon(major = 5)
@Class(extendsType="ceylon.language::Object")
@SatisfiedTypes("ceylon.language::Iterable<Element,Absent>")
public class ArrayIterable<Element,Absent> implements Iterable<Element,Absent>, ReifiedType {
    @Ignore
    protected final ceylon.language.Iterable$impl<Element, Absent> $ceylon$language$Iterable$this;
    @Ignore
    protected final ceylon.language.Container$impl<Element,Absent> $ceylon$language$Container$this;
    @Ignore
    protected final ceylon.language.Category$impl $ceylon$language$Category$this;

    protected final Element[] array;
    protected final Iterable<? extends Element, ? extends java.lang.Object> rest;
    protected final long first;
    @Ignore
    private TypeDescriptor $reifiedElement;
    @Ignore
    private TypeDescriptor $reifiedAbsent;

    private ArrayIterable(TypeDescriptor $reifiedElement, TypeDescriptor $reifiedAbsent, 
            Iterable<? extends Element, ? extends java.lang.Object> rest, Element... array) {
        this($reifiedElement, $reifiedAbsent, rest, array, 0);
    }

    @Ignore
    private ArrayIterable(TypeDescriptor $reifiedElement, TypeDescriptor $reifiedAbsent,
            Iterable<? extends Element, ? extends java.lang.Object> rest, Element[] array, long first) {
        this.$ceylon$language$Iterable$this = new ceylon.language.Iterable$impl<Element,Absent>($reifiedElement, $reifiedAbsent, this);
        this.$ceylon$language$Container$this = new ceylon.language.Container$impl<Element,Absent>($reifiedElement, $reifiedAbsent, this);
        this.$ceylon$language$Category$this = new ceylon.language.Category$impl(this);
    	if (array.length==0 || array.length<=first) {
    		throw new IllegalArgumentException("ArrayIterable may not have zero elements (array)");
    	}
        this.array = array;
        this.first = first;
        this.rest = rest;
        this.$reifiedElement = $reifiedElement;
        this.$reifiedAbsent = $reifiedAbsent;
    }

    @Ignore
    @Override
    public Category$impl $ceylon$language$Category$impl(){
        return $ceylon$language$Category$this;
    }

    @Ignore
    @Override
    public Container$impl<Element,Absent> $ceylon$language$Container$impl(){
        return $ceylon$language$Container$this;
    }

    @Ignore
    @Override
    public Iterable$impl<Element,Absent> $ceylon$language$Iterable$impl(){
        return $ceylon$language$Iterable$this;
    }

    @Ignore
    public static <Element> Iterable<? extends Element, ? extends java.lang.Object> instance(TypeDescriptor $reifiedElement, Iterable<? extends Element, ? extends java.lang.Object> rest, Element... array){
        // make sure we dont' create ArrayIterables with no fixed elements
        if(array.length == 0)
            return rest;
        return new ArrayIterable<Element, Null>($reifiedElement, Null.$TypeDescriptor, rest, array);
    }
    
    @Override
    public long getSize() {
        return array.length;
    }
    
    @Override
    public boolean getEmpty() {
        return false;
    }

    @Override
    public Element getFirst() {
        return array[(int) first];
    }

    @Override
    public Element getLast() {
        if(rest.getEmpty()){
            return array[array.length - 1];
        }
        return (Element) rest.getLast();
    }

    @Override
    public Iterable<? extends Element, ? extends java.lang.Object> getRest() {
        if(first + 1 < array.length){
            return new ArrayIterable<Element,  Null>($reifiedElement, Null.$TypeDescriptor, rest, array, first + 1);
        }else{
            return rest;
        }
    }

    @Override
    @Ignore
    public Sequence<? extends Element> getSequence() {
        Sequential<? extends Element> restSequence = rest.getSequence();
        // copy our part of the array
        int inArray = (int) (array.length - first);
        java.lang.Object[] elems = new java.lang.Object[(int) (inArray + restSequence.getSize())];
        System.arraycopy(array, (int)first, elems, 0, inArray);
        // then copy the rest
        Iterator<?> iterator = restSequence.iterator();
        Object val;
        int i = inArray;
        while((val = iterator.next()) != finished_.get_()){
            elems[i++] = val;
        }
        return ArraySequence.<Element>instance($reifiedElement, elems);
    }

    @Override
    public Iterator<? extends Element> iterator() {
        return new ArrayIterableIterator();
    }

    public class ArrayIterableIterator extends AbstractIterator<Element> {
        
        public ArrayIterableIterator() {
            super($reifiedElement);
        }

        private long idx = first;
        private Iterator<Element> restIterator;

        @Override
        public java.lang.Object next() {
            if (first + idx < array.length) {
                // we're still in the array
                return array[(int) idx++];
            }else if(restIterator == null){
                // we just moved to the iterator
                restIterator = (Iterator<Element>) rest.iterator();
            }
            // no need to keep increasing idx 
            return restIterator.next();
        }

        @Override
        public java.lang.String toString() {
            return "ArrayIterableIterator";
        }

        @Override
        @Ignore
        public TypeDescriptor $getType() {
            return TypeDescriptor.klass(ArrayIterableIterator.class, $reifiedElement);
        }
    }

    @Override
    public <Result> Iterable<? extends Result, ? extends Absent> map(@Ignore TypeDescriptor $reifiedResult, 
            Callable<? extends Result> f) {
        return new MapIterable<Element, Absent, Result>($reifiedElement, $reifiedAbsent, $reifiedResult, this, f);
    }
    
    @Override
    public Iterable<? extends Element, ? extends java.lang.Object> filter(Callable<? extends Boolean> f) {
        return new FilterIterable<Element,  Null>($reifiedElement, Null.$TypeDescriptor, this, f);
    }

    @Override
    @Ignore
    public <Result> Result fold(@Ignore TypeDescriptor $reifiedResult, Result ini, Callable<? extends Result> f) {
        return $ceylon$language$Iterable$this.fold($reifiedResult, ini, f);
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
    public Sequential<? extends Element> sort(Callable<? extends Comparison> f) {
        return $ceylon$language$Iterable$this.sort(f);
    }

    @Override
    public <Result> Sequential<? extends Result> collect(@Ignore TypeDescriptor $reifiedResult, Callable<? extends Result> f) {
        return $ceylon$language$Iterable$this.collect($reifiedResult, f);
    }

    @Override
    public Sequential<? extends Element> select(Callable<? extends Boolean> f) {
        return new FilterIterable<Element,  Null>($reifiedElement, Null.$TypeDescriptor, this, f).getSequence();
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
    public boolean longerThan(long length) {
        return array.length>length;
    }

    @Override @Ignore
    public boolean shorterThan(long length) {
        return array.length<length;
    }

    @Override @Ignore
    public Iterable<? extends Element, ? extends Absent> by(long step) {
        return $ceylon$language$Iterable$this.by(step);
    }

    @Override
    public long count(Callable<? extends Boolean> f) {
        return $ceylon$language$Iterable$this.count(f);
    }

    @Override @Ignore
    public Iterable<? extends Element, ?> getCoalesced() {
        return $ceylon$language$Iterable$this.getCoalesced();
    }
    @Override @Ignore
    public Iterable<? extends Entry<? extends Integer, ? extends Element>, ?> getIndexed() {
        return $ceylon$language$Iterable$this.getIndexed();
    }
    
    @SuppressWarnings("rawtypes")
    @Override @Ignore 
    public <Other,Absent>Iterable chain(@Ignore TypeDescriptor $reifiedOther, @Ignore TypeDescriptor $reifiedOtherAbsent, Iterable<? extends Other, ? extends Absent> other) {
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
    public <Key> Map<? extends Key, ? extends Sequence<? extends Element>> group(Callable<? extends Key> grouping) {
        return $ceylon$language$Iterable$this.group(grouping);
    }*/

    @Override
    @Ignore
    public java.lang.String toString() {
        StringBuilder sb = new StringBuilder("{");
        Iterator<? extends Element> iterator = iterator();
        Object elem;
        boolean first = true;
        while((elem = iterator.next()) != finished_.get_()){
            if(!first)
                sb.append(",");
            else
                first = false;
            sb.append(elem != null ? elem.toString() : "null");
        }
        return sb.append("}").toString();
    }

    @Override
    @Ignore
    public boolean equals(java.lang.Object that) {
        if(this == that)
            return true;
        if(that instanceof Iterable == false)
            return false;
        Iterable other = (Iterable) that;
        Iterator<? extends Element> myIterator = iterator();
        Iterator otherIterator = other.iterator();
        Object myElem;
        while((myElem = myIterator.next()) != finished_.get_()){
            Object otherElem;
            if((otherElem = otherIterator.next()) != finished_.get_()){
                if(myElem == null){
                    if(otherElem != null)
                        return false;
                }else if(otherElem == null)
                    return false;
                else if(!myElem.equals(otherElem))
                    return false;
            }else // other was depleted too soon
                return false;
        }
        // we depleted our iterator, make sure the other one is depleted too
        return otherIterator.next() == finished_.get_();
    }

    @Override
    @Ignore
    public int hashCode() {
        Iterator<? extends Element> iterator = iterator();
        Object elem;
        int hash = 17;
        while((elem = iterator.next()) != finished_.get_()){
            hash = 31 * hash + (elem != null ? elem.hashCode() : 0);
        }
        return hash;
    }
    
    @Override @Ignore
    public Iterable<? extends Element, ? extends java.lang.Object> takingWhile(Callable<? extends Boolean> take) {
        return $ceylon$language$Iterable$this.takingWhile(take);
    }
    
    @Override @Ignore
    public Iterable<? extends Element, ? extends java.lang.Object> skippingWhile(Callable<? extends Boolean> skip) {
        return $ceylon$language$Iterable$this.skippingWhile(skip);
    }
    
    @Override @Ignore
    public boolean contains(java.lang.Object element) {
        return $ceylon$language$Iterable$this.contains(element);
    }
    @Override @Ignore
    public boolean containsEvery(Iterable<?,?> elements) {
        return $ceylon$language$Category$this.containsEvery(elements);
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
    public Iterable<? extends Element,? extends Absent> getCycled() {
        return $ceylon$language$Iterable$this.getCycled();
    }
    @Override
    @Ignore
    public Iterable<? extends Element,? extends Absent> cycle(long times) {
        return $ceylon$language$Iterable$this.cycle(times);
    }
    @Override
    @Ignore
    public List<? extends Element> repeat(long times) {
        return $ceylon$language$Iterable$this.repeat(times);
    }
    @Override
    @Ignore
    public TypeDescriptor $getType() {
        return TypeDescriptor.klass(ArrayIterable.class, $reifiedElement);
    }
}
