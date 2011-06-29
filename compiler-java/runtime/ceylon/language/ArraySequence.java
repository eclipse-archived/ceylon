package ceylon.language;

public class ArraySequence<Element> implements Sequence<Element> {

	private final Element[] array;
	private final int first;

	public ArraySequence(Element[] array, int first) {
		this.array = array;
		this.first = first;
	}

	public Long lastIndex() {
		return (getEmpty() == $true.getTrue()) ? null : new Long(array.length - first - 1);
	}

	public Element value(long index) {
		return index >= getSize().longValue() ? null : array[(int) index - first];
	}

	@Override
	public Boolean getEmpty() {
		return Boolean.instance(array.length <= first);
	}

	@Override
	public Element getFirst() {
		return (getEmpty() == $true.getTrue()) ? null : array[first];
	}

	@Override
	public Iterator<Element> iterator() {
		return new ArraySequenceIterator<Element>(this);
	}

	@Override
	public Element getLast() {
		return (getEmpty() == $true.getTrue()) ? null : array[array.length - 1];
	}

	@Override
	public Sequence<Element> getRest() {
		return new ArraySequence<Element>(array, first + 1);
	}

	@Override
	public Natural getSize() {
		return Natural.instance((getEmpty() == $true.getTrue()) ? 0 : array.length - first);
	}

	public static class ArraySequenceIterator<Element> implements Iterator<Element> {

		private final Sequence<Element> sequence;

		public ArraySequenceIterator(Sequence<Element> sequence) {
			this.sequence = sequence;
		}

		@Override
		public Element getHead() {
			return sequence.getFirst();
		}

		@Override
		public Iterator<Element> getTail() {
			return new ArraySequenceIterator<Element>(sequence.getRest());
		}

	}

	@Override
	public Element value(Natural key) {
		throw new RuntimeException("Not implemented yet");
	}

	@Override
	public Category getKeys() {
		throw new RuntimeException("Not implemented yet");
	}

	@Override
	public Boolean definesEvery(Natural... keys) {
		throw new RuntimeException("Not implemented yet");
	}

	@Override
	public Boolean definesAny(Natural... keys) {
		throw new RuntimeException("Not implemented yet");
	}

	@Override
	public Element[] values(Natural... keys) {
		throw new RuntimeException("Not implemented yet");
	}

	@Override
	public Sequence<Element> getClone() {
		throw new RuntimeException("Not implemented yet");
	}

	@Override
	public Natural getLastIndex() {
		throw new RuntimeException("Not implemented yet");
	}

	@Override
	public Boolean defines(Natural index) {
		throw new RuntimeException("Not implemented yet");
	}

}