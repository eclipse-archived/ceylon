package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ignore;

@Ignore
public final class Correspondence$impl<Key,Item> {

    private final Correspondence<Key, Item> $this;

    public Correspondence$impl(Correspondence<Key,Item> $this) {
        this.$this = $this;
    }
    
    public boolean defines(Key key){
        return Correspondence$impl.<Key,Item>_defines(this.$this, key);
    }
    static <Key,Item> boolean _defines(Correspondence<Key,Item> $this, Key key){
        return $this.item(key) != null;
    }

    public Category getKeys(){
        return Correspondence$impl.<Key,Item>_getKeys(this.$this);
    }
    static <Key,Item> Category _getKeys(final Correspondence<Key,Item> $this){
        Category keys = new Category() {
            public boolean contains(java.lang.Object value) {
                // FIXME
                if (true/*value instanceof Key*/) {
                    return $this.defines((Key)value);
                }
                else {
                    return false;
                }
            }

            @Override
            public boolean containsEvery(ceylon.language.Iterable<?> elements) {
                return Category$impl._containsEvery(this, elements);
            }
            @Override
            public boolean containsEvery() {
                return Category$impl._containsEvery(this, $empty.getEmpty());
            }
            @Override
            public ceylon.language.Iterable<?> containsEvery$elements() {
                return $empty.getEmpty();
            }

            @Override
            public boolean containsAny(ceylon.language.Iterable<?> elements) {
                return Category$impl._containsAny(this, elements);
            }
            @Override
            public boolean containsAny() {
                return Category$impl._containsAny(this, $empty.getEmpty());
            }
            @Override
            public ceylon.language.Iterable<?> containsAny$elements() {
                return $empty.getEmpty();
            }
        };
        return keys;
    }

    public boolean definesEvery(ceylon.language.Iterable<? extends Key> keys){
        return Correspondence$impl.<Key,Item>_definesEvery(this.$this, keys);
    }
    public boolean definesEvery(){
        return Correspondence$impl.<Key,Item>_definesEvery(this.$this, $empty.getEmpty());
    }
    public ceylon.language.Iterable<? extends Key> definesEvery$keys(){
        return $empty.getEmpty();
    }
    static <Key,Item> boolean _definesEvery(Correspondence<Key,Item> $this, ceylon.language.Iterable<? extends Key> keys){
        java.lang.Object elem;
        for (ceylon.language.Iterator<? extends Key> $key$iter$0 = keys.getIterator(); !((elem = $key$iter$0.next()) instanceof Finished);) {
            final Key key = (Key) elem;
            if (!$this.defines(key)) {
                return false;
            }
        }
        return true;
    }

    public boolean definesAny(ceylon.language.Iterable<? extends Key> keys) {
        return Correspondence$impl.<Key,Item>_definesAny(this.$this, keys);
    }
    public boolean definesAny() {
        return Correspondence$impl.<Key,Item>_definesAny(this.$this, $empty.getEmpty());
    }
    public ceylon.language.Iterable<? extends Key> definesAny$keys() {
        return $empty.getEmpty();
    }
    static <Key,Item> boolean _definesAny(Correspondence<Key,Item> $this, ceylon.language.Iterable<? extends Key> keys) {
        java.lang.Object elem;
        for (ceylon.language.Iterator<? extends Key> $key$iter$0 = keys.getIterator(); !((elem = $key$iter$0.next()) instanceof Finished);) {
            final Key key = (Key) elem;
            if ($this.defines(key)) {
                return true;
            }
        }
        return false;
    }

    public ceylon.language.List<? extends Item> items(ceylon.language.Iterable<? extends Key> keys) {
        return Correspondence$impl.<Key,Item>_items(this.$this, keys);
    }
    public ceylon.language.List<? extends Item> items() {
        return Correspondence$impl.<Key,Item>_items(this.$this, $empty.getEmpty());
    }
    public ceylon.language.Iterable<? extends Key> items$keys() {
        return $empty.getEmpty();
    }
    static <Key,Item> ceylon.language.List<? extends Item> _items(Correspondence<Key,Item> $this, ceylon.language.Iterable<? extends Key> keys) {
        if (keys instanceof Sequence) {
            final ceylon.language.Sequence<? extends Key> $keys$1 = (Sequence<? extends Key>)keys;
            return new Correspondence.Items($this, (Sequence<? extends Key>)$keys$1.getClone());
        }
        else {
            return $empty.getEmpty();
        }
    }
}