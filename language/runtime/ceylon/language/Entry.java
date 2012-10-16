package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;

@Ceylon(major = 3)
@TypeParameters({
    @TypeParameter(value = "Key", variance = Variance.OUT, 
            satisfies="ceylon.language::Object"),
    @TypeParameter(value = "Item", variance = Variance.OUT, 
            satisfies="ceylon.language::Object")
 })
@Class(extendsType="ceylon.language::Object")
public class Entry<Key, Item> {
	
	private final Key key;
	private final Item item;
	
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
	    return (31 + key.hashCode()) * 31 + item.hashCode();
	}
	
	@Override
	public boolean equals(@Name("that") java.lang.Object that) {
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
