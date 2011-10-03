package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Ceylon;
import com.redhat.ceylon.compiler.metadata.java.TypeParameter;
import com.redhat.ceylon.compiler.metadata.java.TypeParameters;
import com.redhat.ceylon.compiler.metadata.java.Variance;

@Ceylon
@TypeParameters({
    @TypeParameter(value = "Key", variance = Variance.OUT),
    @TypeParameter(value = "Item", variance = Variance.OUT)
 })
public class Entry<Key extends Equality, 
                    Item extends Equality> 
    extends Object implements Equality {
	private Key key;
	private Item item;
	
	public Entry(Key key, Item item) {
		this.key = key;
		this.item = item;
	}
	
	public Key getKey() {
		return key;
	}
	
	public Item getElement() {
		return item;
	}

	@Override
	public int hashCode() {
	    return key.hashCode()/2 + item.hashCode()/2;// TODO: really should be xor
	}
	
	@Override
	public boolean equals(java.lang.Object that) {
		if (that instanceof Entry) {
			Entry<Key, Item> that2 = (Entry)that;
			return (this.key.equals(that2.key) && this.item.equals(that2.item));
		} else {
			return false;
		}
	}
}
