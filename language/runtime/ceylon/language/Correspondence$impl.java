package ceylon.language;

import com.redhat.ceylon.compiler.java.language.FilterIterable;
import com.redhat.ceylon.compiler.java.language.MapIterable;
import com.redhat.ceylon.compiler.java.metadata.Annotation;
import com.redhat.ceylon.compiler.java.metadata.Annotations;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Container;
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
    public static <Key,Item> Category _getKeys(final Correspondence<Key,Item> $this){
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
            @Ignore
            public boolean containsEvery(ceylon.language.List<?> elements) {
                return Category$impl._containsEvery(this, elements);
            }
            @Override
            @Ignore
            public boolean containsEvery() {
                return Category$impl._containsEvery(this, empty_.getEmpty$());
            }
            @Override
            @Ignore
            public ceylon.language.List<?> containsEvery$elements() {
                return empty_.getEmpty$();
            }

            @Override
            @Ignore
            public boolean containsAny(ceylon.language.List<?> elements) {
                return Category$impl._containsAny(this, elements);
            }
            @Override
            @Ignore
            public boolean containsAny() {
                return Category$impl._containsAny(this, empty_.getEmpty$());
            }
            @Override
            @Ignore
            public ceylon.language.List<?> containsAny$elements() {
                return empty_.getEmpty$();
            }
        };
        return keys;
    }

    public boolean definesEvery(ceylon.language.List<? extends Key> keys){
        return Correspondence$impl.<Key,Item>_definesEvery(this.$this, keys);
    }
    public boolean definesEvery(){
        return Correspondence$impl.<Key,Item>_definesEvery(this.$this, (List)empty_.getEmpty$());
    }
    public ceylon.language.List<? extends Key> definesEvery$keys(){
        return (List<? extends Key>) empty_.getEmpty$();
    }
    public static <Key,Item> boolean _definesEvery(Correspondence<Key,Item> $this, ceylon.language.List<? extends Key> keys){
        java.lang.Object elem;
        for (ceylon.language.Iterator<? extends Key> $key$iter$0 = keys.getIterator(); !((elem = $key$iter$0.next()) instanceof Finished);) {
            final Key key = (Key) elem;
            if (!$this.defines(key)) {
                return false;
            }
        }
        return true;
    }

    public boolean definesAny(ceylon.language.List<? extends Key> keys) {
        return Correspondence$impl.<Key,Item>_definesAny(this.$this, keys);
    }
    public boolean definesAny() {
        return Correspondence$impl.<Key,Item>_definesAny(this.$this, (List)empty_.getEmpty$());
    }
    public ceylon.language.List<? extends Key> definesAny$keys() {
        return (List)empty_.getEmpty$();
    }
    public static <Key,Item> boolean _definesAny(Correspondence<Key,Item> $this, ceylon.language.List<? extends Key> keys) {
        java.lang.Object elem;
        for (ceylon.language.Iterator<? extends Key> $key$iter$0 = keys.getIterator(); !((elem = $key$iter$0.next()) instanceof Finished);) {
            final Key key = (Key) elem;
            if ($this.defines(key)) {
                return true;
            }
        }
        return false;
    }

    public ceylon.language.List<? extends Item> items(ceylon.language.List<? extends Key> keys) {
        return Correspondence$impl.<Key,Item>_items(this.$this, keys);
    }
    public ceylon.language.List<? extends Item> items() {
        return Correspondence$impl.<Key,Item>_items(this.$this, (List)empty_.getEmpty$());
    }
    public ceylon.language.List<? extends Key> items$keys() {
        return (List)empty_.getEmpty$();
    }
    public static <Key,Item> ceylon.language.List<? extends Item> _items(Correspondence<Key,Item> $this, ceylon.language.List<? extends Key> keys) {
        if (keys instanceof Sequence) {
            final ceylon.language.Sequence<? extends Key> $keys$1 = (Sequence<? extends Key>)keys;
            return $this.Items$new((Sequence<? extends Key>)$keys$1.getClone());
        }
        else {
            return (List)empty_.getEmpty$();
        }
    }

    public Items Items$new(Sequence<? extends Key> keys) {
        return new Items(keys);
    }
    
    @Ceylon(major = 3)
    @Class
    @Container(name = "Correspondence", javaClass = "ceylon.language.Correspondence", packageName = "ceylon.language")
    public class Items
    implements Sequence<Item> {
        private Sequence<? extends Key> keys;
        private final Correspondence$impl<ceylon.language.Integer, Item> $ceylon$language$Correspondence$this;
        
        Items(Sequence<? extends Key> keys){
            this.keys = keys;
            this.$ceylon$language$Correspondence$this = new Correspondence$impl<ceylon.language.Integer, Item>(this);
        }
        public final Integer getLastIndex() {
            return keys.getLastIndex();
        }
        public final Item getFirst() {
            return $this.item((Key)keys.getFirst());
        }
        public final List<? extends Item> getRest() {
            return (List<? extends Item>) $this.items(keys.getRest());
        }
        public final Item item(Integer key) {
            Key keyFound = keys.item(key);
            if (keyFound != null) {
                return $this.item(keyFound);
            }
            else {
                return null;
            }
        }
        @Override
        @Annotations(@Annotation("formal"))
        public Sequence<? extends Item> getReversed() {
            return new Items(keys.getReversed());
        }
        @Override
        @Ignore
        public java.lang.String toString() {
            return Collection$impl._toString(this);
        }
        public final Sequence<Item> getClone() {
            return this;
        }
        @Override
        @Ignore
        public Category getKeys() {
            return Correspondence$impl._getKeys(this);
        }
        @Override
        @Ignore
        public boolean definesEvery(List<? extends Integer> keys) {
            return Correspondence$impl._definesEvery(this, keys);
        }
        @Override @Ignore
        @SuppressWarnings({"rawtypes", "unchecked"})
        public boolean definesEvery() {
            return Correspondence$impl._definesEvery(this, (List)empty_.getEmpty$());
        }
        @Override @Ignore
        @SuppressWarnings({"rawtypes", "unchecked"})
        public List<? extends Integer> definesEvery$keys() {
            return (List)empty_.getEmpty$();
        }
        @Override
        @Ignore
        public boolean definesAny(List<? extends Integer> keys) {
            return Correspondence$impl._definesAny(this, keys);
        }
        @Override @Ignore
        @SuppressWarnings({"rawtypes", "unchecked"})
        public boolean definesAny() {
            return Correspondence$impl._definesAny(this, (List)empty_.getEmpty$());
        }
        @Override @Ignore
        @SuppressWarnings({"rawtypes", "unchecked"})
        public List<? extends Integer> definesAny$keys() {
            return (List)empty_.getEmpty$();
        }
        @Override
        @Ignore
        public List<? extends Item> items(List<? extends Integer> keys) {
            return Correspondence$impl._items(this, keys);
        }
        @Override @Ignore
        @SuppressWarnings({"rawtypes", "unchecked"})
        public List<? extends Item> items() {
            return Correspondence$impl._items(this, (List)empty_.getEmpty$());
        }
        @Override @Ignore
        @SuppressWarnings({"rawtypes", "unchecked"})
        public List<? extends Integer> items$keys() {
            return (List)empty_.getEmpty$();
        }
        @Override
        @Ignore
        public boolean getEmpty() {
            return Some$impl._getEmpty(this);
        }
        @Override
        @Ignore
        public long getSize() {
            return List$impl.getSize(this);
        }
        @Override
        @Ignore
        public Item getLast() {
            return Sequence$impl._getLast(this);
        }
        @Override
        @Ignore
        public boolean defines(Integer key) {
            return List$impl._defines(this, key);
        }
        @Override
        @Ignore
        public Iterator<? extends Item> getIterator() {
            return List$impl._getIterator(this);
        }
        @Override @Ignore
        public Iterable<? extends Item> skipping(long skip) {
            return Iterable$impl._skipping(this, skip);
        }

        @Override @Ignore
        public Iterable<? extends Item> taking(long take) {
            return Iterable$impl._taking(this, take);
        }

        @Override @Ignore
        public Iterable<? extends Item> by(long step) {
            return Iterable$impl._by(this, step);
        }
        @Override
        @SuppressWarnings({"rawtypes", "unchecked"})
        public List<? extends Item> segment(Integer from, long length) {
            Iterable<? extends Key> keys = (Iterable<? extends Key>) this.keys.segment(from, length);
            if (keys.getEmpty()) {
                return (List)empty_.getEmpty$();
            }
            else {
                return new Items((Sequence<? extends Key>)keys);
            }
        }
        @Override
        @SuppressWarnings({"rawtypes", "unchecked"})
        public List<? extends Item> span(Integer from, Integer to) {
            Iterable<? extends Key> keys = (Iterable<? extends Key>) this.keys.span(from, to);
            if (keys.getEmpty()) {
                return (List)empty_.getEmpty$();
            }
            else {
                return new Items((Sequence<? extends Key>)keys);
            }
        }
        @Override
        @Ignore
        public boolean contains(java.lang.Object element) {
            return Collection$impl._contains(this, element);
        }
        @Override
        @Ignore
        public boolean containsEvery(List<?> elements) {
            return Category$impl._containsEvery(this, elements);
        }
        @Override
        @Ignore
        public boolean containsEvery() {
            return Category$impl._containsEvery(this, empty_.getEmpty$());
        }
        @Override
        @Ignore
        public List<?> containsEvery$elements() {
            return empty_.getEmpty$();
        }
        @Override
        @Ignore
        public boolean containsAny(List<?> elements) {
            return Category$impl._containsAny(this, elements);
        }
        @Override
        @Ignore
        public boolean containsAny() {
            return Category$impl._containsAny(this, empty_.getEmpty$());
        }
        @Override
        @Ignore
        public List<?> containsAny$elements() {
            return empty_.getEmpty$();
        }
        @Override
        @Ignore
        public boolean equals(java.lang.Object obj) {
            return List$impl._equals(this, obj);
        }
        @Override
        public int hashCode() {
            return keys.hashCode();
        }

        @Override
        @Ignore
        public Sequence<? extends Item> getSequence() {
            return Sequence$impl._getSequence(this);
        }
        @Override @Ignore
        public Item find(Callable<? extends Boolean> f) {
            return Iterable$impl._find(this, f);
        }
        @Override @Ignore
        public Item findLast(Callable<? extends Boolean> f) {
            return List$impl._findLast(this, f);
        }
        @Override
        @Ignore
        public Sequence<? extends Item> sort(Callable<? extends Comparison> f) {
            return Sequence$impl._sort(this, f);
        }
        @Override
        @Ignore
        public <Result> Iterable<? extends Result> map(Callable<? extends Result> f) {
            return new MapIterable<Item, Result>(this, f);
        }
        @Override
        @Ignore
        public Iterable<? extends Item> filter(Callable<? extends Boolean> f) {
            return new FilterIterable<Item>(this, f);
        }
        @Override @Ignore
        public <Result> Sequence<? extends Result> collect(Callable<? extends Result> f) {
            return Sequence$impl._collect(this, f);
        }
        @Override @Ignore
        public List<? extends Item> select(Callable<? extends Boolean> f) {
            return new FilterIterable<Item>(this, f).getSequence();
        }
        @Override
        @Ignore
        public <Result> Result fold(Result ini, Callable<? extends Result> f) {
            return Iterable$impl._fold(this, ini, f);
        }
        @Override @Ignore
        public boolean any(Callable<? extends Boolean> f) {
            return Iterable$impl._any(this, f);
        }
        @Override @Ignore
        public boolean every(Callable<? extends Boolean> f) {
            return Iterable$impl._every(this, f);
        }
        @Override @Ignore
        public long count(Callable<? extends Boolean> f) {
            return Iterable$impl._count(this, f);
        }
        @Override @Ignore
        public Iterable<? extends Item> getCoalesced() {
            return Iterable$impl._getCoalesced(this);
        }
        @Override @Ignore
        public Iterable<? extends Entry<? extends Integer, ? extends Item>> getIndexed() {
            return Iterable$impl._getIndexed(this);
        }
        @SuppressWarnings("rawtypes")
        @Override @Ignore public <Other>Iterable chain(Iterable<? extends Other> other) {
            return Iterable$impl._chain(this, other);
        }
        @Override @Ignore
        public <Key> Map<? extends Key, ? extends Sequence<? extends Item>> group(Callable<? extends Key> grouping) {
            return Iterable$impl._group(this, grouping);
        }
        @SuppressWarnings("rawtypes")
        @Override @Ignore public <Other>Sequence withLeading(Other e) {
            return List$impl._withLeading(this, e);
        }
        @SuppressWarnings("rawtypes")
        @Override @Ignore public <Other>Sequence withTrailing(Other e) {
            return List$impl._withTrailing(this, e);
        }
        @Override
        @Ignore
        public Correspondence$impl<? super Integer, ? extends Item> $ceylon$language$Correspondence$impl() {
            return $ceylon$language$Correspondence$this;
        }
        @Override
        @Ignore
        public Correspondence$impl<? super Integer,? extends Item>.Items Items$new(Sequence<? extends Integer> keys) {
            return $ceylon$language$Correspondence$this.Items$new(keys);
        }
    }
}