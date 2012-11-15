package ceylon.language;

import com.redhat.ceylon.compiler.java.language.AbstractCallable;
import com.redhat.ceylon.compiler.java.metadata.Annotation;
import com.redhat.ceylon.compiler.java.metadata.Annotations;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;

@Ceylon(major = 3)
@TypeParameters({
    @TypeParameter(value="Key", variance=Variance.OUT, satisfies="ceylon.language::Object"),
    @TypeParameter(value="Item", variance=Variance.OUT, satisfies="ceylon.language::Object")
})
@SatisfiedTypes("ceylon.language::Map<Key,Item>")
public class LazyMap<Key, Item> implements Map<Key, Item> {

    private final Iterable<? extends Entry<? extends Key,? extends Item>> entries;
    private final Map$impl<Key,Item> map$impl = new Map$impl<Key,Item>(this);
    private final Correspondence$impl<java.lang.Object,Item> corr$impl = new Correspondence$impl<java.lang.Object,Item>(this);
    private final Category$impl cat$impl = new Category$impl((Category)this);

    @Ignore @SuppressWarnings("unchecked")
    public LazyMap() {
        this.entries = (Iterable)empty_.getEmpty$();
    }
    public LazyMap(@Name("entries")
            @TypeInfo("ceylon.language::Iterable<ceylon.language::Entry<Key,Item>>")
            Iterable<? extends Entry<? extends Key,? extends Item>> entries) {
        this.entries = entries;
    }

    @Ignore
    @Override
    public Correspondence$impl<? super java.lang.Object,? extends Item> $ceylon$language$Correspondence$impl(){
        return corr$impl;
    }

    @Override
    @Ignore
    public Correspondence$impl<? super java.lang.Object, ? extends Item>.Items Items$new(Sequence<? extends java.lang.Object> keys) {
        return corr$impl.Items$new(keys);
    }

    @Override
    @Annotations(@Annotation("actual"))
    @TypeInfo("ceylon.language::Nothing|Item")
    public Item item(@Name("key")
            @TypeInfo("ceylon.language::Object")
            final java.lang.Object key) {
        Entry<? extends Key, ? extends Item> item = entries.find(new AbstractCallable<Boolean>("LazyMap_item") {
            @SuppressWarnings("rawtypes")
            @Override
            public Boolean $call(java.lang.Object e) {
                return Boolean.instance(((Entry)e).getKey().equals(key));
            }
        });
        return item == null ? null : item.getItem();
    }

    @Ignore @Override @SuppressWarnings("unchecked")
    public boolean defines(java.lang.Object key) {
        return corr$impl.defines((Key)key);
    }

    @Ignore @Override @SuppressWarnings("unchecked")
    public boolean definesEvery(Iterable<? extends java.lang.Object> keys) {
        return corr$impl.definesEvery((Iterable<? extends Key>)keys);
    }

    @Override @Ignore
    public boolean definesEvery() {
        return corr$impl.definesEvery();
    }

    @Override @Ignore
    public Iterable<? extends java.lang.Object> definesEvery$keys() {
        return corr$impl.definesEvery$keys();
    }

    @Ignore @Override @SuppressWarnings("unchecked")
    public boolean definesAny(Iterable<? extends java.lang.Object> keys) {
        return corr$impl.definesAny((Iterable<? extends Key>)keys);
    }

    @Override @Ignore
    public boolean definesAny() {
        return corr$impl.definesAny();
    }

    @Override @Ignore
    public Iterable<? extends java.lang.Object> definesAny$keys() {
        return corr$impl.definesAny$keys();
    }

    @Ignore @Override @SuppressWarnings("unchecked")
    public List<? extends Item> items(
            Iterable<? extends java.lang.Object> keys) {
        return corr$impl.items((Iterable<? extends Key>)keys);
    }

    @Override @Ignore
    public Iterable<? extends Item> items() {
        return corr$impl.items();
    }

    @Override @Ignore
    public Iterable<? extends java.lang.Object> items$keys() {
        return corr$impl.items$keys();
    }

    @Ignore @Override
    public boolean getEmpty() {
        return entries.getEmpty();
    }

    @Ignore @Override
    public boolean contains(final java.lang.Object element) {
        return element instanceof Entry && entries.find(new AbstractCallable<Boolean>("LazyMap_item") {
            @Override
            public Boolean $call(java.lang.Object e) {
                return Boolean.instance(e.equals(element));
            }
        }) != null;
    }

    @Override
    @Annotations(@Annotation("actual"))
    @TypeInfo("ceylon.language::Iterator<ceylon.language::Entry<Key,Item>>")
    public Iterator<? extends Entry<? extends Key, ? extends Item>> getIterator() {
        return entries.getIterator();
    }

    @Override
    @Annotations(@Annotation("actual"))
    @TypeInfo("ceylon.language::Boolean")
    public boolean equals(@Name("that")
            @TypeInfo("ceylon.language::Object")
            final java.lang.Object that) {
        return map$impl.equals(that);
    }

    @Override
    @Annotations(@Annotation("actual"))
    @TypeInfo("ceylon.language::Integer")
    public int hashCode() {
        return map$impl.hashCode();
    }

    @Ignore @Override
    public Entry<? extends Key, ? extends Item> getFirst() {
        return entries.getFirst();
    }

    @Ignore @Override
    public Entry<? extends Key, ? extends Item> getLast() {
        return entries.getLast();
    }

    @Ignore @Override
    @TypeInfo("ceylon.language::Iterable<Element>")
    public Iterable<? extends Entry<? extends Key, ? extends Item>> getRest() {
        return entries.getRest();
    }

    @Ignore @Override
    @TypeInfo("ceylon.language::Empty|ceylon.language::Sequence<Element>")
    public List<? extends Entry<? extends Key, ? extends Item>> getSequence() {
        return entries.getSequence();
    }

    @Ignore @Override
    public <Result> Iterable<? extends Result> map(
            Callable<? extends Result> collecting) {
        return entries.map(collecting);
    }

    @Ignore @Override
    public Iterable<? extends Entry<? extends Key, ? extends Item>> filter(
            Callable<? extends Boolean> selecting) {
        return entries.filter(selecting);
    }

    @Ignore @Override
    public <Result> Result fold(Result initial,
            Callable<? extends Result> accumulating) {
        return entries.fold(initial, accumulating);
    }

    @Ignore @Override
    public Entry<? extends Key, ? extends Item> find(
            Callable<? extends Boolean> selecting) {
        return entries.find(selecting);
    }

    @Ignore @Override
    public Entry<? extends Key, ? extends Item> findLast(
            Callable<? extends Boolean> selecting) {
        return entries.findLast(selecting);
    }

    @Ignore @Override
    public List<? extends Entry<? extends Key, ? extends Item>> sort(
            Callable<? extends Comparison> comparing) {
        return entries.sort(comparing);
    }

    @Ignore @Override
    public <Result> List<? extends Result> collect(
            Callable<? extends Result> collecting) {
        return entries.collect(collecting);
    }

    @Ignore @Override
    public List<? extends Entry<? extends Key, ? extends Item>> select(
            Callable<? extends Boolean> selecting) {
        return entries.select(selecting);
    }

    @Ignore @Override
    public boolean any(Callable<? extends Boolean> selecting) {
        return entries.any(selecting);
    }

    @Ignore @Override
    public boolean every(Callable<? extends Boolean> selecting) {
        return entries.every(selecting);
    }

    @Ignore @Override
    public Iterable<? extends Entry<? extends Key, ? extends Item>> skipping(
            long skip) {
        return entries.skipping(skip);
    }

    @Ignore @Override
    public Iterable<? extends Entry<? extends Key, ? extends Item>> taking(
            long take) {
        return entries.taking(take);
    }

    @Ignore @Override
    public Iterable<? extends Entry<? extends Key, ? extends Item>> by(long step) {
        return entries.by(step);
    }

    @Ignore @Override
    public long count(Callable<? extends Boolean> selecting) {
        return entries.count(selecting);
    }

    @Ignore @Override
    public Iterable<? extends Entry<? extends Key, ? extends Item>> getCoalesced() {
        return entries.getCoalesced();
    }

    @Ignore @Override
    public Iterable<? extends Entry<? extends Integer, ? extends Entry<? extends Key, ? extends Item>>> getIndexed() {
        return entries.getIndexed();
    }

    @Ignore @Override @SuppressWarnings("rawtypes")
    public <Other> Iterable chain(Iterable<? extends Other> other) {
        return entries.chain(other);
    }

    @Ignore @Override
    public <K2> Map<? extends K2, ? extends Sequence<? extends Entry<? extends Key, ? extends Item>>> group(
            Callable<? extends K2> grouping) {
        return entries.group(grouping);
    }

    @Override
    @Annotations(@Annotation("actual"))
    @TypeInfo("ceylon.language::Integer")
    public long getSize() {
        //TODO Does this need to eliminate duplicate keys?
        return entries.count(new AbstractCallable<Boolean>("LazyMap_size") {
            @Override
            public Boolean $call(java.lang.Object e) {
                return true_.getTrue$();
            }
        });
    }

    @Ignore @Override
    public boolean containsEvery(Iterable<?> elements) {
        return cat$impl.containsEvery(elements);
    }

    @Override @Ignore
    public boolean containsEvery() {
        return cat$impl.containsEvery();
    }

    @Override @Ignore
    public Iterable<?> containsEvery$elements() {
        return cat$impl.containsEvery$elements();
    }

    @Ignore @Override
    public boolean containsAny(
            @Sequenced @Name("elements") @TypeInfo("ceylon.language::Iterable<ceylon.language::Object>") Iterable<?> elements) {
        return cat$impl.containsAny(elements);
    }

    @Override @Ignore
    public boolean containsAny() {
        return cat$impl.containsAny();
    }

    @Override @Ignore
    public Iterable<?> containsAny$elements() {
        return cat$impl.containsAny$elements();
    }

    @Override
    @Annotations(@Annotation("actual"))
    @TypeInfo("ceylon.language::LazyMap<Key,Item>")
    public Collection<? extends Entry<? extends Key, ? extends Item>> getClone() {
        return new LazyMap<Key,Item>(entries);
    }

    @Ignore @Override
    public Set<? extends Key> getKeys() {
        return map$impl.getKeys();
    }

    @Ignore @Override
    public Collection<? extends Item> getValues() {
        return map$impl.getValues();
    }

    @Ignore @Override
    public Map<? extends Item, ? extends Set<? extends Key>> getInverse() {
        return map$impl.getInverse();
    }

    @Ignore @Override
    public <Result> Map<? extends Key, ? extends Result> mapItems(
            Callable<? extends Result> mapping) {
        return map$impl.mapItems(mapping);
    }

    @Override @Ignore
    public java.lang.String toString() {
        return map$impl.toString();
    }
}
