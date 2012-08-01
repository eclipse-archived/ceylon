package ceylon.language;

import com.redhat.ceylon.compiler.java.language.FilterIterable;
import com.redhat.ceylon.compiler.java.language.MapIterable;
import com.redhat.ceylon.compiler.java.metadata.Annotation;
import com.redhat.ceylon.compiler.java.metadata.Annotations;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;

@Ceylon(major = 2)
@TypeParameters({
    @TypeParameter(value = "Key", variance = Variance.IN,
            satisfies="ceylon.language.Object"),
    @TypeParameter(value = "Item", variance = Variance.OUT)
})
public interface Correspondence<Key,Item> {

    @Annotations(@Annotation("formal"))
    @TypeInfo("Item|ceylon.language.Nothing")
    public Item item(@Name("key") Key key);

    @Annotations(@Annotation("default"))
    public boolean defines(@Name("key") Key key);

    @Annotations(@Annotation("default"))
    public Category getKeys();

    @Annotations(@Annotation("default"))
    public boolean definesEvery(@Sequenced @Name("keys")
    @TypeInfo("ceylon.language.Iterable<Key>")
    Iterable<? extends Key> keys);
    @Ignore
    public boolean definesEvery();
    @Ignore
    public Iterable<? extends Key> definesEvery$keys();

    @Annotations(@Annotation("default"))
    public boolean definesAny(@Sequenced @Name("keys")
    @TypeInfo("ceylon.language.Iterable<Key>")
    Iterable<? extends Key> keys);
    @Ignore
    public boolean definesAny();
    @Ignore
    public Iterable<? extends Key> definesAny$keys();

    @Annotations(@Annotation("default"))
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<Item|ceylon.language.Nothing>")
    public Iterable<? extends Item> items(@Sequenced @Name("keys")
    @TypeInfo("ceylon.language.Iterable<Key>")
    Iterable<? extends Key> keys);
    @Ignore
    public Iterable<? extends Item> items();
    @Ignore
    public Iterable<? extends Key> items$keys();

    @Ignore
    @Ceylon(major = 2)
    class Items<Key,Item>
    implements Sequence<Item> {
        private Sequence<? extends Key> keys;
        private Correspondence<Key, Item> $this;
        Items(Correspondence<Key,Item> $this, Sequence<? extends Key> keys){
            this.keys = keys;
            this.$this = $this;
        }
        public final Integer getLastIndex() {
            return keys.getLastIndex();
        }
        public final Item getFirst() {
            return $this.item((Key)keys.getFirst());
        }
        public final FixedSized<? extends Item> getRest() {
            return (FixedSized<? extends Item>) $this.items(keys.getRest());
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
            return new Items<Key,Item>($this, keys.getReversed());
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
        public boolean definesEvery(Iterable<? extends Integer> keys) {
            return Correspondence$impl._definesEvery(this, keys);
        }
        @Override @Ignore
        @SuppressWarnings({"rawtypes", "unchecked"})
        public boolean definesEvery() {
            return Correspondence$impl._definesEvery(this, (Iterable)$empty.getEmpty());
        }
        @Override @Ignore
        @SuppressWarnings({"rawtypes", "unchecked"})
        public Iterable<? extends Integer> definesEvery$keys() {
            return (Iterable)$empty.getEmpty();
        }
        @Override
        @Ignore
        public boolean definesAny(Iterable<? extends Integer> keys) {
            return Correspondence$impl._definesAny(this, keys);
        }
        @Override @Ignore
        @SuppressWarnings({"rawtypes", "unchecked"})
        public boolean definesAny() {
            return Correspondence$impl._definesAny(this, (Iterable)$empty.getEmpty());
        }
        @Override @Ignore
        @SuppressWarnings({"rawtypes", "unchecked"})
        public Iterable<? extends Integer> definesAny$keys() {
            return (Iterable)$empty.getEmpty();
        }
        @Override
        @Ignore
        public List<? extends Item> items(Iterable<? extends Integer> keys) {
            return Correspondence$impl._items(this, keys);
        }
        @Override @Ignore
        @SuppressWarnings({"rawtypes", "unchecked"})
        public List<? extends Item> items() {
            return Correspondence$impl._items(this, (Iterable)$empty.getEmpty());
        }
        @Override @Ignore
        @SuppressWarnings({"rawtypes", "unchecked"})
        public Iterable<? extends Integer> items$keys() {
            return (Iterable)$empty.getEmpty();
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
                return (List)$empty.getEmpty();
            }
            else {
                return new Items<Key,Item>($this, (Sequence<? extends Key>)keys);
            }
        }
        @Override
        @SuppressWarnings({"rawtypes", "unchecked"})
        public List<? extends Item> span(Integer from, Integer to) {
            Iterable<? extends Key> keys = (Iterable<? extends Key>) this.keys.span(from, to);
            if (keys.getEmpty()) {
                return (List)$empty.getEmpty();
            }
            else {
                return new Items<Key,Item>($this, (Sequence<? extends Key>)keys);
            }
        }
        @Override
        @Ignore
        public boolean contains(java.lang.Object element) {
            return Collection$impl._contains(this, element);
        }
        @Override
        @Ignore
        public boolean containsEvery(Iterable<?> elements) {
            return Category$impl._containsEvery(this, elements);
        }
        @Override
        @Ignore
        public boolean containsEvery() {
            return Category$impl._containsEvery(this, $empty.getEmpty());
        }
        @Override
        @Ignore
        public Iterable<?> containsEvery$elements() {
            return $empty.getEmpty();
        }
        @Override
        @Ignore
        public boolean containsAny(Iterable<?> elements) {
            return Category$impl._containsAny(this, elements);
        }
        @Override
        @Ignore
        public boolean containsAny() {
            return Category$impl._containsAny(this, $empty.getEmpty());
        }
        @Override
        @Ignore
        public Iterable<?> containsAny$elements() {
            return $empty.getEmpty();
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
        public Iterable<? extends Item> select(Callable<? extends Boolean> f) {
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
    }
}
