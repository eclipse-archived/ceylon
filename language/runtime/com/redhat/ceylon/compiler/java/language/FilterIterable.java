package com.redhat.ceylon.compiler.java.language;

import ceylon.language.Boolean;
import ceylon.language.Callable;
import ceylon.language.Category$impl;
import ceylon.language.Comparison;
import ceylon.language.Container$impl;
import ceylon.language.Entry;
import ceylon.language.Finished;
import ceylon.language.Integer;
import ceylon.language.Iterable;
import ceylon.language.Iterable$impl;
import ceylon.language.Iterator;
import ceylon.language.List;
import ceylon.language.Null;
import ceylon.language.Sequential;

import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

/** A convenience class for the Iterable.filter() method.
 * 
 * @author Enrique Zamudio
 */
public class FilterIterable<Element,Absent> implements Iterable<Element,Absent>, ReifiedType {
    private final ceylon.language.Iterable$impl<Element,Absent> $ceylon$language$Iterable$this;
    private final ceylon.language.Container$impl<Element,Absent> $ceylon$language$Container$this;
    private final ceylon.language.Category$impl $ceylon$language$Category$this;
    
    final Iterable<? extends Element, ? extends java.lang.Object> iterable;
    final Callable<? extends Boolean> f;
    @Ignore
    private TypeDescriptor $reifiedElement;
    @Ignore
    private TypeDescriptor $reifiedAbsent;
    
    public FilterIterable(@Ignore TypeDescriptor $reifiedElement, @Ignore TypeDescriptor $reifiedAbsent,
            Iterable<? extends Element, ? extends java.lang.Object> iterable, Callable<? extends Boolean> selecting) {
        this.$ceylon$language$Iterable$this = new ceylon.language.Iterable$impl<Element,Absent>($reifiedElement, $reifiedAbsent, this);
        this.$ceylon$language$Container$this = new ceylon.language.Container$impl<Element,Absent>($reifiedElement, $reifiedAbsent, this);
        this.$ceylon$language$Category$this = new ceylon.language.Category$impl(this);
        this.iterable = iterable;
        f = selecting;
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

    class FilterIterator extends AbstractIterator<Element> {
        public FilterIterator() {
            super($reifiedElement);
        }

        final Iterator<? extends Element> iter = iterable.iterator();

        public java.lang.Object next() {
            java.lang.Object elem = iter.next();
            boolean flag = elem instanceof Finished ? true : f.$call$(elem).booleanValue();
            while (!flag) {
                elem = iter.next();
                flag = elem instanceof Finished ? true : f.$call$(elem).booleanValue();
            }
            return elem;
        }
        @Override
        @Ignore
        public TypeDescriptor $getType$() {
            return TypeDescriptor.klass(FilterIterator.class, $reifiedElement);
        }
    }
    public Iterator<? extends Element> iterator() { return new FilterIterator(); }
    public boolean getEmpty() { return iterator().next() instanceof Finished; }

    @Override
    @Ignore
    public String toString() {
        return $ceylon$language$Iterable$this.toString();
    }

    @Override @Ignore
    public Iterable<? extends Element, ? extends java.lang.Object> takingWhile(Callable<? extends Boolean> take) {
        return $ceylon$language$Iterable$this.takingWhile(take);
    }
    
    @Override @Ignore
    public Iterable<? extends Element, ? extends java.lang.Object> skippingWhile(Callable<? extends Boolean> skip) {
        return $ceylon$language$Iterable$this.skippingWhile(skip);
    }
    
    @Override
    @Ignore
    public long getSize() {
        return $ceylon$language$Iterable$this.getSize();
    }
    @Override
    @Ignore
    public Element getFirst() {
    	return (Element) $ceylon$language$Iterable$this.getFirst();
    }
    @Override 
    @Ignore 
    public Element getLast(){
        return (Element) $ceylon$language$Iterable$this.getLast();
    }
    @Override
    @Ignore
    public Iterable<? extends Element, ? extends java.lang.Object> getRest() {
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
    public <Result> Sequential<? extends Result> collect(@Ignore TypeDescriptor $reifiedResult, Callable<? extends Result> f) {
        return $ceylon$language$Iterable$this.collect($reifiedResult, f);
    }
    @Override @Ignore
    public Sequential<? extends Element> select(Callable<? extends Boolean> f) {
        return $ceylon$language$Iterable$this.select( f);
    }
    @Override
    @Ignore
    public <Result> Iterable<Result, ? extends Absent> map(@Ignore TypeDescriptor $reifiedResult, Callable<? extends Result> f) {
        return new MapIterable<Element, Absent, Result>($reifiedElement, $reifiedAbsent, $reifiedResult, this, f);
    }
    @Override
    @Ignore
    public Iterable<? extends Element, ? extends java.lang.Object> filter(Callable<? extends Boolean> f) {
        return new FilterIterable<Element,  Null>($reifiedElement, Null.$TypeDescriptor$, this, f);
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
    public boolean longerThan(long length) {
        return $ceylon$language$Iterable$this.longerThan(length);
    }
    @Override @Ignore
    public boolean shorterThan(long length) {
        return $ceylon$language$Iterable$this.shorterThan(length);
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
    public Iterable<? extends Element, ? extends Absent> by(long step) {
        return $ceylon$language$Iterable$this.by(step);
    }
    @Override @Ignore
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
    @Override @Ignore @SuppressWarnings("rawtypes")
    public <Other,Absent> Iterable chain(@Ignore TypeDescriptor $reifiedOther, @Ignore TypeDescriptor $reifiedOtherAbsent, Iterable<? extends Other, ? extends Absent> other) {
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
    @Override @Ignore
    public boolean contains(@Name("element") java.lang.Object element) {
        return $ceylon$language$Iterable$this.contains(element);
    }
    @Override @Ignore
    public boolean containsEvery(Iterable<?,?> elements) {
        return $ceylon$language$Category$this.containsEvery(elements);
    }
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
    public TypeDescriptor $getType$() {
        return TypeDescriptor.klass(FilterIterable.class, $reifiedElement);
    }
}