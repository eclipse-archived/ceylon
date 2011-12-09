package ceylon.language;

import java.util.Arrays;

import com.redhat.ceylon.compiler.metadata.java.Ignore;
import com.redhat.ceylon.compiler.metadata.java.TypeInfo;

@Ignore
public class ArraySequence<Element> implements Sequence<Element> {

    private final java.lang.Object[] array;
    private final long first;

    public ArraySequence(Element... array) {
        this(array,0);
    }
    
    ArraySequence(java.lang.Object[] array, long first) {
    	if (array.length==0 || array.length<=first) {
    		throw new IllegalArgumentException("ArraySequence may not have zero elements");
    	}
        this.array = array;
        this.first = first;
    }

    @Override
    @TypeInfo("ceylon.language.Natural")
    public long getLastIndex() {
        return array.length - first - 1;
    }

    @Override
    public Element getFirst() {
        return (Element) array[(int) first];
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
    @TypeInfo("ceylon.language.Natural")
    public long getSize() {
        return array.length - first;
    }

    @Override
    public Element getLast() {
        return (Element) array[array.length - 1];
    }

    @Override
    public boolean defines(Natural index) {
        return index.longValue()+first<array.length;
    }

    @Override
    public Iterator<Element> getIterator() {
        return new ArraySequenceIterator();
    }

    public class ArraySequenceIterator implements Iterator<Element> {

        @Override
        public Element getHead() {
            return getFirst();
        }

        @Override
        public Iterator<Element> getTail() {
            Iterable rest = getRest();
            return rest.getEmpty() ? null : rest.getIterator();
        }
        
        @Override
        public java.lang.String toString() {
            return "SequenceIterator";
        }

    }

    @Override
    public Element item(Natural key) {
        long index = key.longValue()+first;
        return (Element) (index >= array.length ? 
                null : array[(int) index]);
    }

    @Override
    public Category getKeys() {
        return Correspondence$impl.getKeys(this);
    }

    @Override
    public boolean definesEvery(Iterable<? extends Natural> keys) {
        return Correspondence$impl.definesEvery(this, keys);
    }

    @Override
    public boolean definesAny(Iterable<? extends Natural> keys) {
        return Correspondence$impl.definesAny(this, keys);
    }

    @Override
    public Iterable<? extends Element> items(Iterable<? extends Natural> keys) {
        return Correspondence$impl.items(this, keys);
    }

    @Override
    public Sequence<Element> getClone() {
        return this;
    }

    @Override
    public Iterable<? extends Element> span(Natural from, Natural to) {
    	long fromIndex = from.longValue();
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
    public Iterable<? extends Element> segment(Natural from, Natural length) {
        long fromIndex = from.longValue();
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
    
}