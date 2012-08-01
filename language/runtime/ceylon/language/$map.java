package ceylon.language;

import com.redhat.ceylon.compiler.java.language.InternalMap;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;

@Ceylon(major = 2)
@Method
public final class $map {
    private $map() {}

    @TypeParameters({@TypeParameter(value="Key"), @TypeParameter(value="Item")})
    @TypeInfo("ceylon.language.Map<Key,Item>") @Ignore
    public static final <Key, Item> Map<? extends Key, ? extends Item> map() {
        return new InternalMap<Key, Item>(java.util.Collections.<Key, Item>emptyMap());
    }

    @TypeParameters({@TypeParameter(value="Key"), @TypeParameter(value="Item")})
    @TypeInfo("ceylon.language.Map<Key,Item>")
    @SuppressWarnings("unchecked")
    public static final <Key, Item> Map<? extends Key, ? extends Item> map(
            @Name("entries") @Sequenced
            @TypeInfo("ceylon.language.Iterable<ceylon.language.Entry<Key,Item>>")
            Iterable<? extends Entry<? extends Key, ? extends Item>> entries) {
        java.util.HashMap<Key, Item> m = new java.util.HashMap<Key, Item>();
        java.lang.Object $tmp;
        for (Iterator<? extends Entry<? extends Key, ? extends Item>> i = entries.getIterator(); !(($tmp = i.next()) instanceof Finished);) {
            m.put(((Entry<? extends Key, ? extends Item>)$tmp).getKey(), ((Entry<? extends Key, ? extends Item>)$tmp).getItem());
        }
        return new InternalMap<Key, Item>(m);
    }

}
