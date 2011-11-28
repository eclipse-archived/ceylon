package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Ceylon;
import com.redhat.ceylon.compiler.metadata.java.Name;
import com.redhat.ceylon.compiler.metadata.java.TypeInfo;
import com.redhat.ceylon.compiler.metadata.java.TypeParameter;
import com.redhat.ceylon.compiler.metadata.java.TypeParameters;
import com.redhat.ceylon.compiler.metadata.java.Variance;

@Ceylon
@TypeParameters({
    @TypeParameter(value = "Key", variance = Variance.OUT, 
            satisfies="ceylon.language.Equality"),
    @TypeParameter(value = "Item", variance = Variance.OUT, 
            satisfies="ceylon.language.Equality")
 })
public class Entry<Key, Item> 
    extends Object implements Equality {
	private Key key;
	private Item item;
	
	public Entry(@Name("key") Key key, 
			     @Name("item") Item item) {
		this.key = key;
		this.item = item;
	}
	
	public Key getKey() {
		return key;
	}
	
	public Item getItem() {
		return item;
	}

	@Override
	public int hashCode() {
	    return key.hashCode()/2 + item.hashCode()/2;// TODO: really should be xor
	}
	
	@Override
	public boolean equals(@Name("that") @TypeInfo("ceylon.language.Equality") java.lang.Object that) {
		if (that instanceof Entry) {
			Entry<Key, Item> that2 = (Entry)that;
			return (this.key.equals(that2.key) && this.item.equals(that2.item));
		} else {
			return false;
		}
	}
	
	@Override
	public java.lang.String toString() {
		return key.toString() + "->" + item.toString();
	}
}
