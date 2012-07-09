package ceylon.language;

import java.util.Arrays;

import com.redhat.ceylon.compiler.java.metadata.Annotation;
import com.redhat.ceylon.compiler.java.metadata.Annotations;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ignore
@Ceylon(major = 2)
@Class(extendsType="ceylon.language.Object")
@SatisfiedTypes("ceylon.language.Sequence<Element>")
public class ArraySequence<Element> implements Sequence<Element> {

    protected final Element[] array;
    protected final long first;

    public ArraySequence(Element... array) {
        this(array,0);
    }
    
    @Ignore
    ArraySequence(Element[] array, long first) {
    	if (array.length==0 || array.length<=first) {
    		throw new IllegalArgumentException("ArraySequence may not have zero elements");
    	}
        this.array = array;
        this.first = first;
    }

    @Ignore
    ArraySequence(java.util.List<Element> list) {
    	if (list.size()==0) {
    		throw new IllegalArgumentException("ArraySequence may not have zero elements");
    	}
        this.array = (Element[]) list.toArray();
        this.first = 0;
    }

    @Override
    public Element getFirst() {
        return array[(int) first];
    }

    @Override
    public FixedSized<? extends Element> getRest() {
        if (first+1==array.length) {
            return (FixedSized)$empty.getEmpty();
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
    public List<? extends Element> span(Integer from, Integer to) {
        long fromIndex = from.longValue();
        if (fromIndex<0) fromIndex=0;
        long toIndex = to==null ? array.length-1 : to.longValue();
        long lastIndex = getLastIndex().longValue();
        if (fromIndex>lastIndex||toIndex<fromIndex) {
            return (List)$empty.getEmpty();
        }
        else if (toIndex>lastIndex) {
            return new ArraySequence<Element>(array, fromIndex);
        }
        else {
            return new ArraySequence<Element>(Arrays.copyOfRange(array, 
                    (int)fromIndex, (int)toIndex+1), 0);
        }
    }
    
    @Override
    public List<? extends Element> segment(Integer from, long length) {
        long fromIndex = from.longValue();
        if (fromIndex<0) fromIndex=0;
        long resultLength = length;
        long lastIndex = getLastIndex().longValue();
        if (fromIndex>lastIndex||resultLength<=0) {
            return (List)$empty.getEmpty();
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
    @TypeInfo("ceylon.language.Integer")
    public Integer getLastIndex() {
        return Integer.instance(array.length - first - 1);
    }

    @Override
    @TypeInfo("ceylon.language.Integer")
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
                return exhausted.getExhausted();
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
        return Correspondence$impl._getKeys(this);
    }

    @Override
    @Ignore
    public boolean definesEvery(Iterable<? extends Integer> keys) {
        return Correspondence$impl._definesEvery(this, keys);
    }
    
    @Override
    @Ignore
    public boolean definesEvery() {
        return Correspondence$impl._definesEvery(this, (Iterable)$empty.getEmpty());
    }
    @Override
    @Ignore
    public Iterable<? extends Integer> definesEvery$keys() {
        return (Iterable)$empty.getEmpty();
    }

    @Override
    @Ignore
    public boolean definesAny(Iterable<? extends Integer> keys) {
        return Correspondence$impl._definesAny(this, keys);
    }
    
    @Override
    @Ignore
    public boolean definesAny() {
        return Correspondence$impl._definesAny(this, (Iterable)$empty.getEmpty());
    }
    
    @Override
    public Iterable<? extends Integer> definesAny$keys() {
        return (Iterable)$empty.getEmpty();
    }

    @Override
    @Ignore
    public ceylon.language.List<? extends Element> items(Iterable<? extends Integer> keys) {
        return Correspondence$impl._items(this, keys);
    }
    
    @Override
    @Ignore
    public ceylon.language.List<? extends Element> items() {
        return Correspondence$impl._items(this, (Iterable)$empty.getEmpty());
    }
    @Override
    public Iterable<? extends Integer> items$keys() {
        return (Iterable)$empty.getEmpty();
    }

    @Override
    public ArraySequence<Element> getClone() {
        return this;
    }
    
    @Override
    @Ignore
    public java.lang.String toString() {
        return Collection$impl._toString(this);
    }

    @Override
    @Ignore
    public boolean equals(java.lang.Object that) {
        return List$impl._equals(this, that);
    }

    @Override
    @Ignore
    public int hashCode() {
        return List$impl._hashCode(this);
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
    public boolean containsEvery(Iterable<?> elements) {
        return Category$impl._containsEvery(this, elements);
    }
    
    @Override
    @Ignore
    public boolean containsEvery() {
        return Category$impl._containsEvery(this, $empty.getEmpty());
    }
    
    @Override
    @Ignore
    public Iterable<?>containsEvery$elements() {
        return $empty.getEmpty();
    }

    @Override
    @Ignore
    public boolean containsAny(Iterable<?> elements) {
        return Category$impl._containsAny(this, elements);
    }
    
    @Override
    @Ignore
    public boolean containsAny() {
        return Category$impl._containsAny(this, $empty.getEmpty());
    }
    
    @Override 
    @Ignore
    public Sequence<? extends Element> getSequence() { 
        return Sequence$impl._getSequence(this);
    }
    
    @Override @Ignore
    public Element find(Callable<? extends Boolean> f) {
        return Iterable$impl._find(this, f);
    }
    @Override @Ignore
    public Element findLast(Callable<? extends Boolean> f) {
        return List$impl._findLast(this, f);
    }
    @Override
    @Ignore
    public Iterable<? extends Element> sorted(Callable<? extends Comparison> f) {
        return Iterable$impl._sorted(this, f);
    }
    
    @Override
    public <Result> Iterable<Result> map(Callable<? extends Result> f) {
        return new MapIterable<Element, Result>(this, f);
    }
    
    @Override
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
    public Iterable<? extends Element> getCoalesced() {
        return Iterable$impl._getCoalesced(this);
    }
    @Override @Ignore
    public Iterable<? extends Entry<? extends Integer, ? extends Element>> getIndexed() {
        return Iterable$impl._getIndexed(this);
    }
    @Override @Ignore public <Other>Iterable chain(Iterable<? extends Other> other) {
        return Iterable$impl._chain(this, other);
    }

    @Override
    @Ignore
    public Iterable<?>containsAny$elements() {
        return $empty.getEmpty();
    }

    @Override @Ignore public <Other>ArraySequence withLeading() { return this; }
    @Override @Ignore public <Other>ArraySequence withTrailing() { return this; }
    @Override @Ignore public <Other>ArraySequence withLeading$elements() { return this; }
    @Override @Ignore public <Other>ArraySequence withTrailing$elements() { return this; }

    @Override
    @Annotations({ @Annotation("actual") })
    @SuppressWarnings({"rawtypes", "unchecked"})
    public <Other>Sequence withLeading(Iterable<? extends Other> elems) {
        SequenceBuilder sb = new SequenceBuilder();
        sb.appendAll(elems);
        sb.appendAll(this);
        return (Sequence)sb.getSequence();
    }
    @Override
    @Annotations({ @Annotation("actual") })
    @SuppressWarnings({"rawtypes", "unchecked"})
    public <Other>Sequence withTrailing(Iterable<? extends Other> elems) {
        SequenceBuilder sb = new SequenceBuilder();
        sb.appendAll(this);
        sb.appendAll(elems);
        return (Sequence)sb.getSequence();
    }
}
