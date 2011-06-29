package ceylon.language;

public class Entry<K extends Equality, E extends Equality> extends Object implements Equality {
	private K key;
	private E element;
	
	public Entry(K key, E element) {
		this.key = key;
		this.element = element;
	}
	
	public K getKey() {
		return key;
	}
	
	public E getElement() {
		return element;
	}
	
	// shared actual Integer hash
	
	@Override
	public Boolean equals(Equality that) {
		if (that instanceof Entry) {
			Entry<K, E> that2 = (Entry)that;
			return Boolean.instance((this.key.equals(that2.key).booleanValue() && this.element.equals(that2.element).booleanValue()));
		} else {
			return Boolean.instance(false);
		}
	}
}
