package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Ceylon;
import com.redhat.ceylon.compiler.metadata.java.Class;
import com.redhat.ceylon.compiler.metadata.java.Name;
import com.redhat.ceylon.compiler.metadata.java.SatisfiedTypes;
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
@Class(extendsType="ceylon.language.Object")
@SatisfiedTypes("ceylon.language.Equality")
public class Entry<Key, Item> {
	
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
			Entry entry = (Entry) that;
			return this.key.equals(entry.key) && 
					this.item.equals(entry.item);
		} 
		else {
			return false;
		}
	}
	
	@Override
	public java.lang.String toString() {
		return key.toString() + "->" + item.toString();
	}
}
