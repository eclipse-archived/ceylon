package com.redhat.ceylon.compiler.java.language;

import ceylon.language.Boolean;
import ceylon.language.Callable;
import ceylon.language.Collection;
import ceylon.language.Comparison;
import ceylon.language.Correspondence$impl;
import ceylon.language.Entry;
import ceylon.language.Integer;
import ceylon.language.Iterable;
import ceylon.language.Iterator;
import ceylon.language.Map;
import ceylon.language.Sequence;
import ceylon.language.Sequential;
import ceylon.language.Set;

import com.redhat.ceylon.compiler.java.metadata.Annotation;
import com.redhat.ceylon.compiler.java.metadata.Annotations;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;

/** An immutable map returned by certain methods and functions of the language module.
 * This is only to avoid depending on ceylon.collection.
 * 
 * @author Enrique Zamudio
 */
public class InternalMap<Key, Item> implements Map<Key, Item> {

    private final java.util.Map<? extends Key, ? extends Item> m;
    private final ceylon.language.Correspondence$impl<java.lang.Object, Item> corr$impl = new ceylon.language.Correspondence$impl<java.lang.Object, Item>(this);
    private final ceylon.language.Map$impl<Key, Item> map$impl = new ceylon.language.Map$impl<Key, Item>(this);
    private final ceylon.language.Collection$impl<Entry<? extends Key,? extends Item>> coll$impl = new ceylon.language.Collection$impl<Entry<? extends Key,? extends Item>>(this);
    private final ceylon.language.Iterable$impl<Entry<? extends Key,? extends Item>> iter$impl = new ceylon.language.Iterable$impl<Entry<? extends Key,? extends Item>>(this);
    private final ceylon.language.Category$impl cat$impl = new ceylon.language.Category$impl(this);

    public InternalMap(final java.util.Map<? extends Key, ? extends Item> map) {
        this.m = map;
    }

    @Override
    @Annotations(@Annotation("formal"))
    @TypeInfo("Item|ceylon.language::Nothing")
    public Item item(Object key) {
        return m.get(key);
    }

    @Override
    @Annotations(@Annotation("default"))
    public boolean defines(Object key) {
        return m.containsKey(key);
    }

    @Override
    @Annotations(@Annotation("default"))
    public boolean definesEvery(Sequential<? extends java.lang.Object> keys) {
        return corr$impl.definesEvery(keys);
    }

    @Override
    @Ignore
    public boolean definesEvery() {
        return corr$impl.definesEvery();
    }

    @Override
    @Ignore
    public Sequential<? extends Object> definesEvery$keys() {
        return corr$impl.definesEvery$keys();
    }

    @Override
    @Annotations(@Annotation("default"))
    public boolean definesAny(Sequential<? extends Object> keys) {
        return corr$impl.definesAny(keys);
    }

    @Override
    @Ignore
    public boolean definesAny() {
        return corr$impl.definesAny();
    }

    @Override
    @Ignore
    public Sequential<? extends Object> definesAny$keys() {
        return corr$impl.definesAny$keys();
    }

    @Override
    @Annotations(@Annotation("default"))
    @TypeInfo("ceylon.language::Sequential<Item|ceylon.language::Nothing>")
    public Sequential<? extends Item> items(Sequential<? extends Object> keys) {
        return corr$impl.items(keys);
    }

    @Override
    @Ignore
    public Sequential<? extends Item> items() {
        return corr$impl.items();
    }

    @Override
    @Ignore
    public Sequential<? extends Object> items$keys() {
        return corr$impl.items$keys();
    }

    @Override
    @Annotations({ @Annotation("actual"), @Annotation("default") })
    public boolean getEmpty() {
        return m.isEmpty();
    }

    @Override
    @Annotations({ @Annotation("actual"), @Annotation("default") })
    public boolean contains(Object element) {
        return coll$impl.contains(element);
    }

    @Override
    @Annotations(@Annotation("formal"))
    @TypeInfo("ceylon.language::Iterator<Element>")
    public Iterator<? extends Entry<? extends Key, ? extends Item>> getIterator() {
        return new Iterator<Entry<? extends Key, ? extends Item>>(){
            private final java.util.Iterator<? extends java.util.Map.Entry<? extends Key, ? extends Item>> inner = m.entrySet().iterator();
            @Override
            @Annotations(@Annotation("formal"))
            @TypeInfo(value="Element|ceylon.language::Finished", erased=true)
            public Object next() {
                if (inner.hasNext()) {
                    java.util.Map.Entry<? extends Key, ? extends Item> e = inner.next();
                    return new ceylon.language.Entry<Key, Item>(e.getKey(), e.getValue());
                }
                return ceylon.language.finished_.getFinished$();
            }
        };
    }

    @Override
    @Annotations({ @Annotation("actual"), @Annotation("default") })
    @TypeInfo("ceylon.language::Nothing|Element")
    public Entry<? extends Key, ? extends Item> getFirst() {
        return iter$impl.getFirst();
    }

    @Override
    @Annotations({ @Annotation("actual"), @Annotation("default") })
    @TypeInfo("ceylon.language::Nothing|Element")
    public Entry<? extends Key, ? extends Item> getLast() {
        return iter$impl.getLast();
    }

    @Override
    @Annotations(@Annotation("default"))
    @TypeInfo("ceylon.language::Iterable<Element>")
    public Iterable<? extends Entry<? extends Key, ? extends Item>> getRest() {
        return iter$impl.getRest();
    }

    @Override
    @Annotations(@Annotation("default"))
    @TypeInfo("ceylon.language::Sequential<Element>")
    public Sequential<? extends Entry<? extends Key, ? extends Item>> getSequence() {
        return iter$impl.getSequence();
    }

    @Override
    @Annotations(@Annotation("default"))
    @TypeInfo("ceylon.language::Iterable<Result>")
    @TypeParameters(@TypeParameter("Result"))
    public <Result> Iterable<? extends Result> map(
            Callable<? extends Result> collecting) {
        return iter$impl.map(collecting);
    }

    @Override
    @Annotations(@Annotation("default"))
    @TypeInfo("ceylon.language::Iterable<Element>")
    public Iterable<? extends Entry<? extends Key, ? extends Item>> filter(
            Callable<? extends Boolean> selecting) {
        return iter$impl.filter(selecting);
    }

    @Override
    @Annotations(@Annotation("default"))
    @TypeInfo("Result")
    @TypeParameters(@TypeParameter("Result"))
    public <Result> Result fold(Result initial,
            Callable<? extends Result> accumulating) {
        return iter$impl.fold(initial, accumulating);
    }

    @Override
    @Annotations(@Annotation("default"))
    @TypeInfo("Element|ceylon.language::Nothing")
    public Entry<? extends Key, ? extends Item> find(
            Callable<? extends Boolean> selecting) {
        return iter$impl.find(selecting);
    }

    @Override
    @Annotations(@Annotation("default"))
    @TypeInfo("Element|ceylon.language::Nothing")
    public Entry<? extends Key, ? extends Item> findLast(
            Callable<? extends Boolean> selecting) {
        return iter$impl.findLast(selecting);
    }

    @Override
    @Annotations(@Annotation("default"))
    @TypeInfo("ceylon.language::Sequential<Element>")
    public Sequential<? extends Entry<? extends Key, ? extends Item>> sort(
            Callable<? extends Comparison> comparing) {
        return iter$impl.sort(comparing);
    }

    @Override
    @Annotations(@Annotation("default"))
    @TypeInfo("ceylon.language::Sequential<Result>")
    @TypeParameters(@TypeParameter("Result"))
    public <Result> Sequential<? extends Result> collect(
            Callable<? extends Result> collecting) {
        return iter$impl.collect(collecting);
    }

    @Override
    @Annotations(@Annotation("default"))
    @TypeInfo("ceylon.language::Sequential<Element>")
    public Sequential<? extends Entry<? extends Key, ? extends Item>> select(
            Callable<? extends Boolean> selecting) {
        return iter$impl.select(selecting);
    }

    @Override
    @Annotations(@Annotation("default"))
    @TypeInfo("ceylon.language::Boolean")
    public boolean any(Callable<? extends Boolean> selecting) {
        return iter$impl.any(selecting);
    }

    @Override
    @Annotations(@Annotation("default"))
    @TypeInfo("ceylon.language::Boolean")
    public boolean every(Callable<? extends Boolean> selecting) {
        return iter$impl.every(selecting);
    }

    @Override
    @Annotations(@Annotation("default"))
    @TypeInfo("ceylon.language::Iterable<Element>")
    public Iterable<? extends Entry<? extends Key, ? extends Item>> skipping(
            long skip) {
        return iter$impl.skipping(skip);
    }

    @Override
    @Annotations(@Annotation("default"))
    @TypeInfo("ceylon.language::Iterable<Element>")
    public Iterable<? extends Entry<? extends Key, ? extends Item>> taking(
            long take) {
        return iter$impl.taking(take);
    }

    @Override
    @Annotations(@Annotation("default"))
    @TypeInfo("ceylon.language::Iterable<Element>")
    public Iterable<? extends Entry<? extends Key, ? extends Item>> by(long step) {
        return iter$impl.by(step);
    }

    @Override
    @Annotations(@Annotation("default"))
    @TypeInfo("ceylon.language::Integer")
    public long count(Callable<? extends Boolean> selecting) {
        return iter$impl.count(selecting);
    }

    @Override
    @Annotations(@Annotation("default"))
    @TypeInfo("ceylon.language::Iterable<Element&ceylon.language::Object>")
    public Iterable<? extends Entry<? extends Key, ? extends Item>> getCoalesced() {
        return iter$impl.getCoalesced();
    }

    @Override
    @Annotations(@Annotation("default"))
    @TypeInfo("ceylon.language::Iterable<ceylon.language::Entry<ceylon.language::Integer,Element&ceylon.language::Object>>")
    public Iterable<? extends Entry<? extends Integer, ? extends Entry<? extends Key, ? extends Item>>> getIndexed() {
        return iter$impl.getIndexed();
    }

    @Override
    @Annotations(@Annotation("default"))
    @TypeInfo("ceylon.language::Iterable<Element|Other>")
    @TypeParameters(@TypeParameter("Other"))
    public <Other> Iterable chain(Iterable<? extends Other> other) {
        return iter$impl.chain(other);
    }
    @Override @Ignore
    public <Key2> Map<? extends Key2, ? extends Sequence<? extends Entry<? extends Key, ? extends Item>>> group(Callable<? extends Key2> grouping) {
        return iter$impl.group(grouping);
    }

    @Override
    @Annotations(@Annotation("formal"))
    public long getSize() {
        return m.size();
    }

    @Override
    @Annotations(@Annotation("default"))
    public boolean containsEvery(
            @Sequenced @Name("elements") @TypeInfo("ceylon.language::Sequential<ceylon.language::Object>") Sequential<?> elements) {
        return cat$impl.containsEvery(elements);
    }

    @Override
    @Ignore
    public boolean containsEvery() {
        return cat$impl.containsEvery();
    }

    @Override
    @Ignore
    public Sequential<?> containsEvery$elements() {
        return cat$impl.containsEvery$elements();
    }

    @Override
    @Annotations(@Annotation("default"))
    public boolean containsAny(
            @Sequenced @Name("elements") @TypeInfo("ceylon.language::Sequential<ceylon.language::Object>") Sequential<?> elements) {
        return cat$impl.containsAny(elements);
    }

    @Override
    @Ignore
    public boolean containsAny() {
        return cat$impl.containsAny();
    }

    @Override
    @Ignore
    public Sequential<?> containsAny$elements() {
        return cat$impl.containsAny$elements();
    }

    @Override
    @Annotations(@Annotation("formal"))
    public InternalMap<? extends Key, ? extends Item> getClone() {
        return new InternalMap<Key,Item>(java.util.Collections.unmodifiableMap(m));
    }

    @Override
    @Annotations({ @Annotation("actual"), @Annotation("default") })
    @TypeInfo("ceylon.language::Set<Key>")
    public Set<? extends Key> getKeys() {
        return map$impl.getKeys();
    }

    @Override
    @Annotations(@Annotation("default"))
    @TypeInfo("ceylon.language::Collection<Item>")
    public Collection<? extends Item> getValues() {
        return map$impl.getValues();
    }

    @Override
    @Annotations(@Annotation("default"))
    @TypeInfo("ceylon.language::Map<Item,ceylon.language::Set<Key>>")
    public Map<? extends Item, ? extends Set<? extends Key>> getInverse() {
        return map$impl.getInverse();
    }

    @Override
    @Annotations(@Annotation("default"))
    @TypeInfo("ceylon.language::Map<Key,Result>")
    @TypeParameters(@TypeParameter(value="Result", satisfies="ceylon.language::Object"))
    public <Result> Map<? extends Key, ? extends Result> mapItems(
            Callable<? extends Result> mapping) {
        return map$impl.mapItems(mapping);
    }

    @Override
    @Ignore
    public Correspondence$impl<java.lang.Object, ? extends Item> $ceylon$language$Correspondence$impl() {
        return corr$impl;
    }

    @Override
    @Ignore
    public Correspondence$impl<? super java.lang.Object, ? extends Item>.Items Items$new(Sequence<? extends java.lang.Object> keys) {
        return corr$impl.Items$new(keys);
    }
}
