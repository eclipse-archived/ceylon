package ceylon.language;

import com.redhat.ceylon.compiler.java.language.ArraySequence;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;

@Ceylon(major = 3)
@Method
public final class zip_ {
    
    private zip_() {}
    
    @TypeParameters({@TypeParameter(value="Key", satisfies="ceylon.language::Object"),
                     @TypeParameter(value="Item", satisfies="ceylon.language::Object")})
    @TypeInfo("ceylon.language::Empty|ceylon.language::Sequence<ceylon.language::Entry<Key,Item>>")
    public static <Key,Item> List<? extends Entry<? extends Key, ? extends Item>> zip(
    @Name("keys")
    @TypeInfo("ceylon.language::Iterable<Key>")
    final ceylon.language.Iterable<? extends Key> keys,
    @Name("items")
    @TypeInfo("ceylon.language::Iterable<Item>")
    final ceylon.language.Iterable<? extends Item> items) {
		java.util.List<Entry<? extends Key,? extends Item>> list = new java.util.ArrayList<Entry<? extends Key,? extends Item>>();
		Iterator<? extends Key> keyIter = keys.getIterator();
		Iterator<? extends Item> itemIter = items.getIterator();
		Key key = (Key) keyIter.next();
        Item item = (Item) itemIter.next();
		while (key != exhausted_.getExhausted$() && item != exhausted_.getExhausted$()) {
			list.add(new Entry<Key,Item>(key, item));
	        key = (Key) keyIter.next();
	        item = (Item) itemIter.next();
		}
		if (list.isEmpty()) {
		    return (List) empty_.getEmpty$();
		}
		else {
		    return new ArraySequence<Entry<? extends Key,? extends Item>>(list);
		}
    }
}
