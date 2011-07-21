package ceylon.language;

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
	public Natural getLastIndex() {
		return (isEmpty() == $true.getTrue()) ? null : Natural.instance(array.length - first - 1);
	}

	@Override
	public Element getFirst() {
		return (isEmpty() == $true.getTrue()) ? null : array[(int) first];
	}

	@Override
	public Sequence<Element> getRest() {
		return new ArraySequence<Element>(array, first + 1);
	}

	@Override
	public Boolean isEmpty() {
		return Boolean.instance(array.length <= first);
	}

	@Override
	public Natural getSize() {
		return Natural.instance((isEmpty() == $true.getTrue()) ? 0 : array.length - first);
	}

	@Override
	public Element getLast() {
		return (isEmpty() == $true.getTrue()) ? null : array[array.length - 1];
	}

	@Override
	public Boolean defines(Natural index) {
		throw new RuntimeException("Not implemented yet");
	}

	@Override
	public Iterator<Element> iterator() {
		return new ArraySequenceIterator<Element>(this);
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
		return key.longValue() >= getSize().longValue() ? null : array[(int) (key.longValue() - first)];
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
}