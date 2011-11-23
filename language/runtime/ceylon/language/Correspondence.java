package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Ceylon;
import com.redhat.ceylon.compiler.metadata.java.Name;
import com.redhat.ceylon.compiler.metadata.java.SatisfiedTypes;
import com.redhat.ceylon.compiler.metadata.java.TypeInfo;
import com.redhat.ceylon.compiler.metadata.java.TypeParameter;
import com.redhat.ceylon.compiler.metadata.java.TypeParameters;
import com.redhat.ceylon.compiler.metadata.java.Variance;

@Ceylon
@TypeParameters({
    @TypeParameter(value = "Key", variance = Variance.IN),
    @TypeParameter(value = "Item", variance = Variance.OUT)
 })
 public interface Correspondence<Key extends Equality,Item> {
    
    @TypeInfo("Item|ceylon.language.Nothing")
    public Item item(@Name("key") Key key);

    public boolean defines(@Name("key") Key key);

    public Category getKeys();

    public boolean definesEvery(@Name("keys") Iterable<? extends Key> keys);

    public boolean definesAny(@Name("keys") Iterable<? extends Key> keys);

    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<Item|ceylon.language.Nothing>")
    public Sequence<? extends Item> items(@Name("keys") Iterable<? extends Key> keys);

    @SatisfiedTypes("ceylon.language.Sequence<Item|ceylon.language.Nothing>")
    class Entries<Key extends Equality,Item>
    extends Object
    implements Sequence<Item> {
        private Sequence<Key> keys;
        private Correspondence<Key, Item> $this;
        Entries(Correspondence<Key,Item> $this, Sequence<Key> keys){
            this.keys = keys;
            this.$this = $this;
        }
        @TypeInfo("ceylon.language.Natural")
        public final long getLastIndex() {
            return keys.getLastIndex();
        }
        @TypeInfo("Item|ceylon.language.Nothing")
        public final Item getFirst() {
            return $this.item(keys.getFirst());
        }
        @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<Item|ceylon.language.Nothing>")
        public final ceylon.language.Sequence<? extends Item> getRest() {
            return $this.items(keys.getRest());
        }
        @TypeInfo("Item|ceylon.language.Nothing")
        public final Item item(Natural index) {
            Key key = keys.item(index);
            if (key != null) {
                return $this.item(key);
            }
            else {
                return null;
            }
        }
        @Override
        @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<Item|ceylon.language.Nothing>")
        public Sequence<? extends Item> span(long from, long to) {
            throw new RuntimeException("Not implemented"); //todo!
        }
        /*@Override
        public Ordered<Item> segment(long from, long length) {
            throw new RuntimeException("Not implemented"); //todo!
        }*/
        @Override
        public java.lang.String toString() {
            return "Entries";//todo			
        }
        @TypeInfo("ceylon.language.Sequence<Item|ceylon.language.Nothing>")
        public final Sequence<Item> getClone() {
            return this;
        }
        @Override
        public Category getKeys() {
            return Correspondence$impl.getKeys(this);
        }
        @Override
        public boolean definesEvery(Iterable<? extends Natural> keys) {
            return Correspondence$impl.definesEvery(this, keys);
        }
        @Override
        public boolean definesAny(Iterable<? extends Natural> keys) {
            return Correspondence$impl.definesAny(this, keys);
        }
        @Override
        @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<Item|ceylon.language.Nothing>")
        public Sequence<? extends Item> items(Iterable<? extends Natural> keys) {
            return Correspondence$impl.items(this, keys);
        }
        @Override
        public boolean getEmpty() {
            return Sequence$impl.getEmpty(this);
        }
        @Override
        @TypeInfo("ceylon.language.Natural")
        public long getSize() {
            return Sequence$impl.getSize(this);
        }
        @Override
        public Item getLast() {
            return Sequence$impl.getLast(this);
        }
        @Override
        public boolean defines(Natural index) {
            return Sequence$impl.defines(this, index);
        }
        @Override
        public Iterator<Item> getIterator() {
            return Sequence$impl.getIterator(this);
        }
        @Override
        public java.lang.String getElementsString() {
            return Sequence$impl.getElementsString(this);
        }
    }
}
