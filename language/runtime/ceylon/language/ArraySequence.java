package ceylon.language;

import java.util.Arrays;

import com.redhat.ceylon.compiler.metadata.java.TypeInfo;

public class ArraySequence<Element> implements Sequence<Element> {

	private final Element[] array;
	private final long first;

	public ArraySequence(Element... array) {
		this.array = array;
		this.first = 0;
	}

    public ArraySequence(Element[] array, long first) {
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
		return getEmpty() ? null : array[(int) first];
	}

	@Override
	public Sequence<? extends Element> getRest() {
		return new ArraySequence<Element>(array, first + 1);
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
		return getEmpty() ? null : array[array.length - 1];
	}

	@Override
	public boolean defines(Natural index) {
		throw new RuntimeException("Not implemented yet");
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
			Sequence<? extends Element> rest = sequence.getRest();
			return rest.getEmpty() ? null :
				new ArraySequenceIterator<Element>(rest);
		}
		
		@Override
		public java.lang.String toString() {
			return "SequenceIterator";
		}

	}

	@Override
	public Element item(Natural key) {
		return key.longValue() >= getSize() ? 
		        null : array[(int) (key.longValue() - first)];
	}

	@Override
	public Category getKeys() {
		throw new RuntimeException("Not implemented yet");
	}

	@Override
	public boolean definesEvery(Iterable<? extends Natural> keys) {
		throw new RuntimeException("Not implemented yet");
	}

	@Override
	public boolean definesAny(Iterable<? extends Natural> keys) {
		throw new RuntimeException("Not implemented yet");
	}

	@Override
	public Sequence<Element> items(Iterable<? extends Natural> keys) {
		throw new RuntimeException("Not implemented yet");
	}

	@Override
	public Sequence<Element> getClone() {
		throw new RuntimeException("Not implemented yet");
	}

    @Override
    public java.lang.String getElementsString() {
        return Sequence$impl.getElementsString(this);
    }

	@Override
	public Sequence<? extends Element> span(long from, long to) {
		if(from >= to)
			return new ArraySequence<Element>();
		if(to > getLastIndex())
			return new ArraySequence<Element>(array, from);
		Element[] newArray = Arrays.copyOfRange(array, (int)from, (int)to);
		return new ArraySequence<Element>(newArray);
	}
	
	@Override
	public java.lang.String toString() {
		if (getEmpty()) return "{}";
		StringBuilder result = new StringBuilder("{ ");
		for (Element elem: array) {
			result.append(elem)
				.append(", ");
		}
		result.setLength(result.length()-2);
		result.append(" }");
		return result.toString();
	}

	/*@Override
	public Ordered<Element> segment(long from, long length) {
		return Ordered$Impl.segment(this, from, length);
	}*/
}