package com.redhat.ceylon.compiler.java.language;

import ceylon.language.Boolean;
import ceylon.language.Category$impl;
import ceylon.language.Container$impl;
import ceylon.language.Iterable$impl;
import ceylon.language.Iterator$impl;
import ceylon.language.Null;
import ceylon.language.Callable;
import ceylon.language.Comparison;
import ceylon.language.Entry;
import ceylon.language.Integer;
import ceylon.language.Iterable;
import ceylon.language.Iterator;
import ceylon.language.Map;
import ceylon.language.Sequence;
import ceylon.language.Sequential;
import ceylon.language.finished_;

import com.redhat.ceylon.compiler.java.ReifiedType;
import com.redhat.ceylon.compiler.java.TypeDescriptor;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ignore
@Ceylon(major = 4)
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

    public ArrayIterable(TypeDescriptor $reifiedElement, TypeDescriptor $reifiedAbsent, 
            Iterable<? extends Element, ? extends java.lang.Object> rest, Element... array) {
        this($reifiedElement, $reifiedAbsent, rest, array, 0);
    }

    @Ignore
    public ArrayIterable(TypeDescriptor $reifiedElement, TypeDescriptor $reifiedAbsent,
            Iterable<? extends Element, ? extends java.lang.Object> rest, Element[] array, long first) {
        this.$ceylon$language$Iterable$this = new ceylon.language.Iterable$impl<Element,Absent>($reifiedElement, $reifiedAbsent, this);
        this.$ceylon$language$Container$this = new ceylon.language.Container$impl<Element,Absent>($reifiedElement, $reifiedAbsent, this);
        this.$ceylon$language$Category$this = new ceylon.language.Category$impl(this);
    	if (array.length==0 || array.length<=first) {
    		throw new IllegalArgumentException("ArrayIterable may not have zero elements (array)");
    	}
        if (rest.getEmpty()) {
            throw new IllegalArgumentException("ArrayIterable may not have zero elements (rest)");
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
        // make sure we don't create ArrayIterables with empty rests
        if(rest.getEmpty())
            return new ArraySequence<Element>($reifiedElement, array);
        return new ArrayIterable<Element,  Null>($reifiedElement, Null.$TypeDescriptor, rest, array);
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
        Element[] elems = (Element[]) new Object[(int) (inArray + restSequence.getSize())];
        System.arraycopy(array, (int)first, elems, 0, inArray);
        // then copy the rest
        Iterator<?> iterator = restSequence.getIterator();
        Object val;
        int i = inArray;
        while((val = iterator.next()) != finished_.getFinished$()){
            elems[i++] = (Element) val;
        }
        return new ArraySequence<Element>($reifiedElement, elems);
    }

    @Override
    public Iterator<? extends Element> getIterator() {
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
                restIterator = (Iterator<Element>) rest.getIterator();
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
    public <Result> Iterable<? extends Result, ? extends java.lang.Object> map(@Ignore TypeDescriptor $reifiedResult, 
            Callable<? extends Result> f) {
        return new MapIterable<Element, Result>($reifiedElement, $reifiedResult, this, f);
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
    public Iterable<? extends Element, ? extends java.lang.Object> by(long step) {
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
    @Ignore
    public java.lang.String toString() {
        StringBuilder sb = new StringBuilder("{");
        Iterator<? extends Element> iterator = getIterator();
        Object elem;
        boolean first = true;
        while((elem = iterator.next()) != finished_.getFinished$()){
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
        Iterator<? extends Element> myIterator = getIterator();
        Iterator otherIterator = other.getIterator();
        Object myElem;
        while((myElem = myIterator.next()) != finished_.getFinished$()){
            Object otherElem;
            if((otherElem = otherIterator.next()) != finished_.getFinished$()){
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
        return otherIterator.next() == finished_.getFinished$();
    }

    @Override
    @Ignore
    public int hashCode() {
        Iterator<? extends Element> iterator = getIterator();
        Object elem;
        int hash = 17;
        while((elem = iterator.next()) != finished_.getFinished$()){
            hash = 31 * hash + (elem != null ? elem.hashCode() : 0);
        }
        return hash;
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
    public TypeDescriptor $getType() {
        return TypeDescriptor.klass(ArrayIterable.class, $reifiedElement);
    }
}
