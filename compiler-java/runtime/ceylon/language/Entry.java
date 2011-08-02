package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Ceylon;
import com.redhat.ceylon.compiler.metadata.java.TypeParameter;
import com.redhat.ceylon.compiler.metadata.java.TypeParameters;
import com.redhat.ceylon.compiler.metadata.java.Variance;

@Ceylon
@TypeParameters({
    @TypeParameter(value = "K", variance = Variance.OUT),
    @TypeParameter(value = "E", variance = Variance.OUT)
 })
public class Entry<K extends java.lang.Object /* Equality */, E extends java.lang.Object /* Equality */> extends Object implements Equality {
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
	public boolean equals(Equality that) {
		if (that instanceof Entry) {
			Entry<K, E> that2 = (Entry)that;
			return (this.key.equals(that2.key) && this.element.equals(that2.element));
		} else {
			return false;
		}
	}
}
