package ceylon.language;

import java.util.Arrays;
import java.util.List;

import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ignore
@Class(extendsType="ceylon.language.Object")
@SatisfiedTypes("ceylon.language.Sequence<Element>")
public class ArraySequence<Element> implements Sequence<Element> {

    private final Element[] array;
    private final long first;

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
    ArraySequence(List<Element> list) {
    	array = (Element[]) list.toArray();
    	if (array.length==0) {
    		throw new IllegalArgumentException("ArraySequence may not have zero elements");
    	}
        this.first = 0;
    }

    @Override
    @TypeInfo("ceylon.language.Integer")
    public long getLastIndex() {
        return array.length - first - 1;
    }

    @Override
    public Element getFirst() {
        return array[(int) first];
    }

    @Override
    public Iterable<? extends Element> getRest() {
        if (first+1==array.length) {
            return $empty.getEmpty();
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
    @TypeInfo("ceylon.language.Integer")
    public long getSize() {
        return array.length - first;
    }

    @Override
    public Element getLast() {
        return array[array.length - 1];
    }

    @Override
    public boolean defines(Integer index) {
        long ind = index.longValue();
		return ind>=0 && ind+first<array.length;
    }

    @Override
    public Iterator<Element> getIterator() {
        return new ArraySequenceIterator();
    }

    public class ArraySequenceIterator implements Iterator<Element> {
        private long idx = first;
        
        @Override
        public java.lang.Object next() {
            if (idx <= getLastIndex()+first) {
                return array[(int) idx++];
            } 
            else {
                return exhausted.getExhausted();
            }
        }

        @Override
        public java.lang.String toString() {
            return "ArraySequenceIterator";
        }

    }

    @Override
    public Element item(Integer key) {
        long index = key.longValue()+first;
        return index<0 || index >= array.length ? 
                null : array[(int) index];
    }

    @Override
    public Category getKeys() {
        return Correspondence$impl.getKeys(this);
    }

    @Override
    public boolean definesEvery(Iterable<? extends Integer> keys) {
        return Correspondence$impl.definesEvery(this, keys);
    }

    @Override
    public boolean definesAny(Iterable<? extends Integer> keys) {
        return Correspondence$impl.definesAny(this, keys);
    }

    @Override
    public Iterable<? extends Element> items(Iterable<? extends Integer> keys) {
        return Correspondence$impl.items(this, keys);
    }

    @Override
    public Sequence<Element> getClone() {
        return this;
    }

    @Override
    public ceylon.language.List<? extends Element> span(Integer from, Integer to) {
    	long fromIndex = from.longValue();
    	if (fromIndex<0) fromIndex=0;
    	long toIndex = to==null ? array.length-1 : to.longValue();
        long lastIndex = getLastIndex();
		if (fromIndex>lastIndex||toIndex<fromIndex) {
            return $empty.getEmpty();
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
    public ceylon.language.List<? extends Element> segment(Integer from, Integer length) {
        long fromIndex = from.longValue();
    	if (fromIndex<0) fromIndex=0;
		long resultLength = length.longValue();
		long lastIndex = getLastIndex();
		if (fromIndex>lastIndex||resultLength==0) {
            return $empty.getEmpty();
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
    public java.lang.String toString() {
        return Sequence$impl.toString(this);
    }

    public Element[] toArray() {
        return array;
    }
    
    @Override
    public boolean equals(java.lang.Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Sequence))
            return false;
        @SuppressWarnings("unchecked")
        Sequence<? extends Element> other = (Sequence<? extends Element>) obj;
        if (getSize() != other.getSize()) {
            return false;
        }
        Iterator<? extends Element> thisIterator = getIterator();
        Iterator<? extends Element> otherIterator = other.getIterator();
        java.lang.Object thisHead = thisIterator.next();
        java.lang.Object otherHead = otherIterator.next();
        while (thisHead != exhausted.getExhausted() && otherHead != exhausted.getExhausted()) {
            if (!thisHead.equals(otherHead)) {
                return false;
            }
            thisHead = thisIterator.next();
            otherHead = otherIterator.next();
        }
        return (thisHead == exhausted.getExhausted() && otherHead == exhausted.getExhausted());
    }

    @Override
    public boolean contains(java.lang.Object element) {
        for (Element x: array) {
            if (element.equals(x)) return true;
        }
        return false;
    }

    @Override
    public boolean containsEvery(Iterable<?> elements) {
        return false;
    }

    @Override
    public boolean containsAny(Iterable<?> elements) {
        // TODO Auto-generated method stub
        return false;
    }
    
}