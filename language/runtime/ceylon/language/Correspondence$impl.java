package ceylon.language;


public class Correspondence$impl {

    public static <Key,Item> boolean defines(Correspondence<Key,Item> $this, Key key){
        return $this.item(key) != null;
    }

    public static <Key,Item> Category getKeys(final Correspondence<Key,Item> $this){
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
                return Category$impl.containsEvery(this, elements);
            }

            @Override
            public boolean containsAny(ceylon.language.Iterable<?> elements) {
                return Category$impl.containsAny(this, elements);
            }
        };
        return keys;
    }

    public static <Key,Item> boolean definesEvery(Correspondence<Key,Item> $this, ceylon.language.Iterable<? extends Key> keys){
        for (ceylon.language.Iterator<? extends Key> $key$iter$0 = keys.getIterator(); $key$iter$0 != null; $key$iter$0 = $key$iter$0.getTail()) {
            final Key key = $key$iter$0.getHead();
            if (!$this.defines(key)) {
                return false;
            }
        }
        return true;
    }

    public static <Key,Item> boolean definesAny(Correspondence<Key,Item> $this, ceylon.language.Iterable<? extends Key> keys) {
        for (ceylon.language.Iterator<? extends Key> $key$iter$0 = keys.getIterator(); $key$iter$0 != null; $key$iter$0 = $key$iter$0.getTail()) {
            final Key key = $key$iter$0.getHead();
            if ($this.defines(key)) {
                return true;
            }
        }
        return false;
    }

    public static <Key,Item> Iterable<? extends Item> items(Correspondence<Key,Item> $this, ceylon.language.Iterable<? extends Key> keys) {
        if (keys instanceof Sequence) {
            final ceylon.language.Sequence<? extends Key> $keys$1 = (ceylon.language.Sequence<? extends Key>)keys;
            return new Correspondence.Items($this, $keys$1.getClone());
        }
        else {
            return $empty.getEmpty();
        }
    }
}