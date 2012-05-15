package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;

@Ceylon
@TypeParameters({
    @TypeParameter(value = "Key", variance = Variance.IN,
            satisfies="ceylon.language.Object"),
    @TypeParameter(value = "Item", variance = Variance.OUT)
})
public interface Correspondence<Key,Item> {
    
    @TypeInfo("Item|ceylon.language.Nothing")
    public Item item(@Name("key") Key key);

    public boolean defines(@Name("key") Key key);

    public Category getKeys();

    public boolean definesEvery(@Sequenced @Name("keys") 
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<Key>")
    Iterable<? extends Key> keys);
    @Ignore
    public boolean definesEvery();

    public boolean definesAny(@Sequenced @Name("keys") 
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<Key>")
    Iterable<? extends Key> keys);
    @Ignore
    public boolean definesAny();

    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<Item|ceylon.language.Nothing>")
    public Iterable<? extends Item> items(@Sequenced @Name("keys") 
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<Key>")
    Iterable<? extends Key> keys);
    @Ignore
    public Iterable<? extends Item> items();

    @Ignore
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
        public java.lang.String toString() {
            return List$impl._toString(this);		
        }
        public final Sequence<Item> getClone() {
            return this;
        }
        @Override
        public Category getKeys() {
            return Correspondence$impl._getKeys(this);
        }
        @Override
        public boolean definesEvery(Iterable<? extends Integer> keys) {
            return Correspondence$impl._definesEvery(this, keys);
        }
        @Override
        public boolean definesEvery() {
            return Correspondence$impl._definesEvery(this, $empty.getEmpty());
        }
        @Override
        public boolean definesAny(Iterable<? extends Integer> keys) {
            return Correspondence$impl._definesAny(this, keys);
        }
        @Override
        public boolean definesAny() {
            return Correspondence$impl._definesAny(this, $empty.getEmpty());
        }
        @Override
        public List<? extends Item> items(Iterable<? extends Integer> keys) {
            return Correspondence$impl._items(this, keys);
        }
        @Override
        public List<? extends Item> items() {
            return Correspondence$impl._items(this, $empty.getEmpty());
        }
        @Override
        public boolean getEmpty() {
            return Some$impl._getEmpty(this);
        }
        @Override
        public long getSize() {
            return List$impl.getSize(this);
        }
        @Override
        public Item getLast() {
            return Sequence$impl._getLast(this);
        }
        @Override
        public boolean defines(Integer key) {
            return List$impl._defines(this, key);
        }
        @Override
        public Iterator<? extends Item> getIterator() {
            return List$impl._getIterator(this);
        }
        @Override
        public List<? extends Item> segment(Integer from, long length) {
        	Iterable<? extends Key> keys = (Iterable<? extends Key>) this.keys.segment(from, length);
        	if (keys.getEmpty()) {
        		return $empty.getEmpty();
        	}
        	else {
        		return new Items<Key,Item>($this, (Sequence<? extends Key>)keys);
        	}
        }
        @Override
        public List<? extends Item> span(Integer from, Integer to) {
        	Iterable<? extends Key> keys = (Iterable<? extends Key>) this.keys.span(from, to);
        	if (keys.getEmpty()) {
        		return $empty.getEmpty();
        	}
        	else {
        		return new Items<Key,Item>($this, (Sequence<? extends Key>)keys);
        	}
        }
        @Override
        public boolean contains(java.lang.Object element) {
            return Collection$impl._contains(this, element);
        }
        @Override
        public long count(java.lang.Object element) {
            return Collection$impl._count(this, element);
        }
        @Override
        public boolean containsEvery(Iterable<?> elements) {
            return Category$impl._containsEvery(this, elements);
        }
        @Override
        public boolean containsEvery() {
            return Category$impl._containsEvery(this, $empty.getEmpty());
        }
        @Override
        public boolean containsAny(Iterable<?> elements) {
            return Category$impl._containsAny(this, elements);
        }
        @Override
        public boolean containsAny() {
            return Category$impl._containsAny(this, $empty.getEmpty());
        }
        @Override
        public boolean equals(java.lang.Object obj) {
            return List$impl._equals(this, obj);
        }
        @Override
        public int hashCode() {
            return keys.hashCode();
        }
    }
}
