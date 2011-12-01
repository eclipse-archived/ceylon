package ceylon.language;

import java.util.Arrays;

import com.redhat.ceylon.compiler.metadata.java.TypeInfo;

public class ArraySequence<Element> implements Sequence<Element> {

	private final java.lang.Object[] array;
	private final long first;

	public ArraySequence(Element... array) {
		this.array = array;
		this.first = 0;
	}

    ArraySequence(java.lang.Object[] array, long first) {
        this.array = array;
        this.first = first;
    }

	@Override
    @TypeInfo("ceylon.language.Natural")
	public long getLastIndex() {
		return getEmpty() ? null : array.length - first - 1;
	}

	@Override
	public Element getFirst() {
		return (Element) (getEmpty() ? null : array[(int) first]);
	}

	@Override
	public Iterable getRest() {
		if (first+1==array.length) {
			return $empty.getEmpty();
		}
		else {
			return new ArraySequence<Element>(array, first + 1);
		}
	}

	@Override
	public boolean getEmpty() {
		return array.length <= first;
	}

	@Override
    @TypeInfo("ceylon.language.Natural")
	public long getSize() {
		return getEmpty() ? 0 : array.length - first;
	}

	@Override
	public Element getLast() {
		return (Element) (getEmpty() ? null : array[array.length - 1]);
	}

	@Override
	public boolean defines(Natural index) {
		return index.value+first<array.length;
	}

	@Override
	public Iterator<Element> getIterator() {
		return getEmpty() ? null : 
			new ArraySequenceIterator<Element>(this);
	}

	public static class ArraySequenceIterator<Element> implements Iterator<Element> {

		private final Sequence<? extends Element> sequence;

		public ArraySequenceIterator(Sequence<? extends Element> sequence) {
			this.sequence = sequence;
		}

		@Override
		public Element getHead() {
			return sequence.getFirst();
		}

		@Override
		public Iterator<Element> getTail() {
			Iterable rest = sequence.getRest();
			return rest.getEmpty() ? null :
				new ArraySequenceIterator<Element>((Sequence<? extends Element>) rest);
		}
		
		@Override
		public java.lang.String toString() {
			return "SequenceIterator";
		}

	}

	@Override
	public Element item(Natural key) {
		return (Element) (key.longValue() >= getSize() ? 
		        null : array[(int) (key.longValue() - first)]);
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
	public Sequence<? extends Element> span(long from, long to) {
		if(from >= to)
			return new ArraySequence<Element>();
		if(to > getLastIndex())
			return new ArraySequence<Element>(array, from);
		java.lang.Object[] newArray = Arrays.copyOfRange(array, (int)from, (int)to);
		return new ArraySequence<Element>(newArray, 0);
	}
	
	@Override
	public java.lang.String toString() {
		return Sequence$impl.toString(this);
	}

	/*@Override
	public Ordered<Element> segment(long from, long length) {
		return Ordered$Impl.segment(this, from, length);
	}*/
}