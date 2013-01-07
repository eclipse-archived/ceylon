package ceylon.language;

import com.redhat.ceylon.compiler.java.language.AbstractIterable;
import com.redhat.ceylon.compiler.java.language.FilterIterable;
import com.redhat.ceylon.compiler.java.language.MapIterable;
import com.redhat.ceylon.compiler.java.metadata.Ignore;

@Ignore
public final class Map$impl<Key,Item> {
    private final Map<Key, Item> $this;

    public Map$impl(Map<Key,Item> $this) {
        this.$this = $this;
    }

    public java.lang.String toString() {
        return (new ceylon.language.Collection$impl($this)).toString();
    }
    public boolean equals(java.lang.Object that) {
        return _equals($this, that);
    }
    private static <Key,Item> boolean _equals(final Map<Key,Item> $this, java.lang.Object that) {
        if (that instanceof Map) {
            Map other = (Map) that;
            if (other.getSize()==$this.getSize()) {
                java.lang.Object elem;
                for (ceylon.language.Iterator<? extends Entry<? extends Key,? extends Item>> iter = $this.getIterator();
                        !((elem = iter.next()) instanceof Finished);) {
                    Entry<Key,Item> entry = (Entry<Key,Item>) elem;
                    java.lang.Object y = other.item(entry.getKey());
                    Item x = entry.getItem();
                    if (x==y || x!=null && y!=null && x.equals(y)) {
                        continue;
                    }
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        return _hashCode($this);
    }
    private static <Key,Item> int _hashCode(final Map<Key,Item> $this) {
        int hashCode = 1;
        java.lang.Object elem;
        for (Iterator<? extends Entry<? extends Key,? extends Item>> iter=$this.getIterator(); !((elem = iter.next()) instanceof Finished);) {
            hashCode *= 31;
            if (elem != null) {
                hashCode += elem.hashCode();
            }
        }
        return hashCode;
    }

    public Set<? extends Key> getKeys(){
        return _getKeys($this);
    }
    private static <Key,Item> Set<? extends Key> _getKeys(final Map<Key,Item> $this){
        class keySet implements Set<Key>{
            private final ceylon.language.Category$impl $ceylon$language$Category$this;
            private final ceylon.language.Collection$impl<Key> $ceylon$language$Collection$this;
            private final ceylon.language.Iterable$impl<Key> $ceylon$language$Iterable$this;
            private final ceylon.language.Set$impl<Key> $ceylon$language$Set$this;

            private keySet() {
                this.$ceylon$language$Category$this = new ceylon.language.Category$impl(this);
                this.$ceylon$language$Collection$this = new ceylon.language.Collection$impl<Key>(this);
                this.$ceylon$language$Iterable$this = new ceylon.language.Iterable$impl<Key>(this);
                this.$ceylon$language$Set$this = new ceylon.language.Set$impl<Key>(this);
            }

            @Override
            public Collection<? extends Key> getClone() {
                return this;
            }

            @Override
            public boolean equals(java.lang.Object obj) {
                return false;
            }

            @Override
            public int hashCode() {
                return (int) $this.getSize();
            }

            @Override
            public Iterator<? extends Key> getIterator() {
                return new Iterator<Key>() {
                    final Iterator<? extends Entry<? extends Key, ? extends Item>> orig = $this.getIterator();
                    @SuppressWarnings("unchecked")
                    public java.lang.Object next() {
                        java.lang.Object tmp = orig.next();
                        if (tmp instanceof Finished) {
                            return tmp;
                        }
                        return ((Entry<? extends Key, ? extends Item>)tmp).getKey();
                    }
                };
            }

            @Override
            public long getSize() {
                return $this.getSize();
            }

            @Override
            public java.lang.String toString() {
                return $ceylon$language$Collection$this.toString();
            }

            @Override
            public <Other> Set<? extends Object> union(Set<? extends Other> set) {
                return (Set)bottom_.getBottom$();
            }

            @Override
            public <Other> Set<? extends Object> intersection(Set<? extends Other> set) {
                return (Set)bottom_.getBottom$();
            }

            @Override
            public <Other> Set<? extends Object> exclusiveUnion(Set<? extends Other> set) {
                return (Set)bottom_.getBottom$();
            }

            @Override
            public <Other> Set<? extends Key> complement(Set<? extends Other> set) {
                return (Set)bottom_.getBottom$();
            }

            // concrete interface methods:

            @Override
            @Ignore
            public Iterable<? extends Key> getRest() {
                return $ceylon$language$Iterable$this.getRest();
            }

            @Override
            @Ignore
            public Key getFirst() {
                return $ceylon$language$Iterable$this.getFirst();
            }
            @Override @Ignore public Key getLast() {
                return $ceylon$language$Iterable$this.getLast();
            }

            @Override
            @Ignore
            public boolean getEmpty() {
                return $ceylon$language$Collection$this.getEmpty();
            }

            @Override
            @Ignore
            public boolean contains(java.lang.Object element) {
                return $ceylon$language$Collection$this.contains(element);
            }

            @Override
            @Ignore
            public boolean containsEvery(Sequential<?> elements) {
                return $ceylon$language$Category$this.containsEvery(elements);
            }

            @Override
            @Ignore
            public boolean containsEvery() {
                return $ceylon$language$Category$this.containsEvery(empty_.getEmpty$());
            }

            @Override
            @Ignore
            public Sequential<?> containsEvery$elements() {
                return empty_.getEmpty$();
            }

            @Override
            @Ignore
            public boolean containsAny(Sequential<?> elements) {
                return $ceylon$language$Category$this.containsAny(elements);
            }

            @Override
            @Ignore
            public boolean containsAny() {
                return $ceylon$language$Category$this.containsAny(empty_.getEmpty$());
            }

            @Override
            @Ignore
            public Sequential<?> containsAny$elements() {
                return empty_.getEmpty$();
            }

            @Override
            @Ignore
            public boolean superset(Set<? extends java.lang.Object> set) {
                return $ceylon$language$Set$this.superset(set);
            }

            @Override
            @Ignore
            public boolean subset(Set<? extends java.lang.Object> set) {
                return $ceylon$language$Set$this.subset(set);
            }
            @Override
            @Ignore
            public Sequential<? extends Key> getSequence() {
                return $ceylon$language$Iterable$this.getSequence();
            }
            @Override @Ignore
            public Key find(Callable<? extends Boolean> f) {
                return $ceylon$language$Iterable$this.find(f);
            }
            @Override @Ignore
            public Key findLast(Callable<? extends Boolean> f) {
                return $ceylon$language$Iterable$this.findLast(f);
            }
            @Override
            @Ignore
            public Sequential<? extends Key> sort(Callable<? extends Comparison> f) {
                return $ceylon$language$Iterable$this.sort(f);
            }
            @Override
            @Ignore
            public <Result> Iterable<Result> map(Callable<? extends Result> f) {
                return new MapIterable<Key, Result>(this, f);
            }
            @Override
            @Ignore
            public Iterable<? extends Key> filter(Callable<? extends Boolean> f) {
                return new FilterIterable<Key>(this, f);
            }
            @Override @Ignore
            public <Result> Sequential<? extends Result> collect(Callable<? extends Result> f) {
                return new MapIterable<Key, Result>(this, f).getSequence();
            }
            @Override @Ignore
            public Sequential<? extends Key> select(Callable<? extends Boolean> f) {
                return new FilterIterable<Key>(this, f).getSequence();
            }
            @Override
            @Ignore
            public <Result> Result fold(Result ini, Callable<? extends Result> f) {
                return $ceylon$language$Iterable$this.fold(ini, f);
            }
            @Override @Ignore
            public boolean any(Callable<? extends Boolean> f) {
                return $ceylon$language$Iterable$this.any(f);
            }
            @Override @Ignore
            public boolean every(Callable<? extends Boolean> f) {
                return $ceylon$language$Iterable$this.every(f);
            }
			@Override @Ignore
			public Iterable<? extends Key> skipping(long skip) {
				return $ceylon$language$Iterable$this.skipping(skip);
			}

			@Override @Ignore
			public Iterable<? extends Key> taking(long take) {
				return $ceylon$language$Iterable$this.taking(take);
			}

			@Override @Ignore
			public Iterable<? extends Key> by(long step) {
				return $ceylon$language$Iterable$this.by(step);
			}
            @Override @Ignore
            public long count(Callable<? extends Boolean> f) {
                return $ceylon$language$Iterable$this.count(f);
            }
            @Override @Ignore
            public Iterable<? extends Key> getCoalesced() {
                return $ceylon$language$Iterable$this.getCoalesced();
            }
            @Override @Ignore
            public Iterable<? extends Entry<? extends Integer, ? extends Key>> getIndexed() {
                return $ceylon$language$Iterable$this.getIndexed();
            }
			@Override @Ignore public <Other>Iterable chain(Iterable<? extends Other> other) {
				return $ceylon$language$Iterable$this.chain(other);
			}
		    @Override @Ignore
		    public <Key2> Map<? extends Key2, ? extends Sequence<? extends Key>> group(Callable<? extends Key2> grouping) {
		        return $ceylon$language$Iterable$this.group(grouping);
		    }
        }
        return new keySet();
    }

    public Collection<? extends Item> getValues(){
        return _getValues($this);
    }
    private static <Key,Item> Collection<? extends Item> _getValues(final Map<Key,Item> $this){
        class valueCollection implements Collection<Item> {
            private final ceylon.language.Category$impl $ceylon$language$Category$this;
            private final ceylon.language.Collection$impl<Item> $ceylon$language$Collection$this;
            private final ceylon.language.Iterable$impl<Item> $ceylon$language$Iterable$this;

            private valueCollection() {
                this.$ceylon$language$Category$this = new ceylon.language.Category$impl(this);
                this.$ceylon$language$Collection$this = new ceylon.language.Collection$impl<Item>(this);
                this.$ceylon$language$Iterable$this = new ceylon.language.Iterable$impl<Item>(this);
            }

            @Override
            public Collection<? extends Item> getClone() {
                return this;
            }

            @Override
            public boolean equals(java.lang.Object obj) {
                return false;
            }

            @Override
            public int hashCode() {
                return $this.hashCode();
            }

            @Override
            public Iterator<? extends Item> getIterator() {
                final Iterator<? extends Entry<? extends Key, ? extends Item>> orig = $this.getIterator();
                return new Iterator<Item>() {
                    @SuppressWarnings("unchecked")
                    public java.lang.Object next() {
                        java.lang.Object tmp = orig.next();
                        if (tmp instanceof Finished) {
                            return tmp;
                        }
                        return ((Entry<? extends Key, ? extends Item>)tmp).getItem();
                    }
                };
            }

            @Override
            public long getSize() {
                return $this.getSize();
            }

            @Override
            public java.lang.String toString() {
                return $ceylon$language$Collection$this.toString();
            }

            // concrete interface methods:

            @Override
            @Ignore
            public Iterable<? extends Item> getRest() {
                return $ceylon$language$Iterable$this.getRest();
            }

            @Override
            @Ignore
            public Item getFirst() {
                return $ceylon$language$Iterable$this.getFirst();
            }
            @Override @Ignore public Item getLast() {
                return $ceylon$language$Iterable$this.getLast();
            }

            @Override
            @Ignore
            public boolean containsEvery(Sequential<?> elements) {
                return $ceylon$language$Category$this.containsEvery(elements);
            }

            @Override
            @Ignore
            public boolean containsEvery() {
                return $ceylon$language$Category$this.containsEvery(empty_.getEmpty$());
            }

            @Override
            @Ignore
            public Sequential<?> containsEvery$elements() {
                return empty_.getEmpty$();
            }

            @Override
            @Ignore
            public boolean containsAny(Sequential<?> elements) {
                return $ceylon$language$Category$this.containsAny(elements);
            }

            @Override
            @Ignore
            public boolean containsAny() {
                return $ceylon$language$Category$this.containsAny(empty_.getEmpty$());
            }

            @Override
            @Ignore
            public Sequential<?> containsAny$elements() {
                return empty_.getEmpty$();
            }

            @Override
            @Ignore
            public boolean getEmpty() {
                return $ceylon$language$Collection$this.getEmpty();
            }

            @Override
            @Ignore
            public boolean contains(java.lang.Object element) {
                return $ceylon$language$Collection$this.contains(element);
            }

            @Override
            @Ignore
            public Sequential<? extends Item> getSequence() {
                return $ceylon$language$Iterable$this.getSequence();
            }
            @Override @Ignore
            public Item find(Callable<? extends Boolean> f) {
                return $ceylon$language$Iterable$this.find(f);
            }
            @Override @Ignore
            public Item findLast(Callable<? extends Boolean> f) {
                return $ceylon$language$Iterable$this.findLast(f);
            }
            @Override
            @Ignore
            public Sequential<? extends Item> sort(Callable<? extends Comparison> f) {
                return $ceylon$language$Iterable$this.sort(f);
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
            public <Result> Sequential<? extends Result> collect(Callable<? extends Result> f) {
                return new MapIterable<Item, Result>(this, f).getSequence();
            }
            @Override @Ignore
            public Sequential<? extends Item> select(Callable<? extends Boolean> f) {
                return new FilterIterable<Item>(this, f).getSequence();
            }
            @Override
            @Ignore
            public <Result> Result fold(Result ini, Callable<? extends Result> f) {
                return $ceylon$language$Iterable$this.fold(ini, f);
            }
            @Override @Ignore
            public boolean any(Callable<? extends Boolean> f) {
                return $ceylon$language$Iterable$this.any(f);
            }
            @Override @Ignore
            public boolean every(Callable<? extends Boolean> f) {
                return $ceylon$language$Iterable$this.every(f);
            }
			@Override @Ignore
			public Iterable<? extends Item> skipping(long skip) {
				return $ceylon$language$Iterable$this.skipping(skip);
			}

			@Override @Ignore
			public Iterable<? extends Item> taking(long take) {
				return $ceylon$language$Iterable$this.taking(take);
			}

			@Override @Ignore
			public Iterable<? extends Item> by(long step) {
				return $ceylon$language$Iterable$this.by(step);
			}
            @Override @Ignore
            public long count(Callable<? extends Boolean> f) {
                return $ceylon$language$Iterable$this.count(f);
            }
            @Override @Ignore
            public Iterable<? extends Item> getCoalesced() {
                return $ceylon$language$Iterable$this.getCoalesced();
            }
            @Override @Ignore
            public Iterable<? extends Entry<? extends Integer, ? extends Item>> getIndexed() {
                return $ceylon$language$Iterable$this.getIndexed();
            }
			@Override @Ignore public <Other>Iterable chain(Iterable<? extends Other> other) {
				return $ceylon$language$Iterable$this.chain(other);
			}
		    @Override @Ignore
		    public <Key2> Map<? extends Key2, ? extends Sequence<? extends Item>> group(Callable<? extends Key2> grouping) {
		        return $ceylon$language$Iterable$this.group(grouping);
		    }
        }
        return new valueCollection();
    }

    public Map<? extends Item, ? extends Set<? extends Key>> getInverse(){
        return _getInverse($this);
    }
    private static <Key,Item> Map<? extends Item, ? extends Set<? extends Key>> _getInverse(final Map<Key,Item> $this){
        class inverse implements Map<Item, Set<? extends Key>>{

            private Correspondence$impl<java.lang.Object, Set<? extends Key>> correspondence$impl = new Correspondence$impl(this);
            private final ceylon.language.Category$impl $ceylon$language$Category$this;
            private final ceylon.language.Collection$impl<Item> $ceylon$language$Collection$this;
            private final ceylon.language.Correspondence$impl $ceylon$language$Correspondence$this;
            private final ceylon.language.Iterable$impl<Entry<? extends Item, ? extends Set<? extends Key>>> $ceylon$language$Iterable$this;

            private inverse() {
                this.$ceylon$language$Category$this = new ceylon.language.Category$impl(this);
                this.$ceylon$language$Collection$this = new ceylon.language.Collection$impl(this);
                this.$ceylon$language$Correspondence$this = new ceylon.language.Correspondence$impl(this);
                this.$ceylon$language$Iterable$this = new ceylon.language.Iterable$impl<Entry<? extends Item, ? extends Set<? extends Key>>>(this);
            }
            
            @Ignore
            @Override
            public Correspondence$impl<? super java.lang.Object, ? extends Set<? extends Key>> $ceylon$language$Correspondence$impl(){
                return correspondence$impl;
            }

            @Override
            @Ignore
            public Correspondence$impl<? super java.lang.Object, ? extends Set<? extends Key>>.Items Items$new(Sequence<? extends java.lang.Object> keys) {
                return correspondence$impl.Items$new(keys);
            }

            @Override
            public Collection<? extends Entry<? extends Item, ? extends Set<? extends Key>>> getClone() {
                return this;
            }

            @Override
            public boolean equals(java.lang.Object obj) {
                return _equals(this, obj);
            }

            @Override
            public int hashCode() {
                return (int) $this.getSize();
            }

            @Override
            public Set<? extends Key> item(final java.lang.Object key) {
                return new LazySet<Key>(new AbstractIterable<Key>(){
                    public Iterator<? extends Key> getIterator() {
                        return new Iterator<Key>(){
                            final Iterator<? extends Entry<? extends Key, ? extends Item>> orig = $this.getIterator();
                            public java.lang.Object next() {
                                java.lang.Object tmp = orig.next();
                                while (!(tmp instanceof Finished)) {
                                    @SuppressWarnings("unchecked")
                                    Entry<? extends Key, ? extends Item> e = (Entry<? extends Key, ? extends Item>)tmp;
                                    if (e.getItem().equals(key)) {
                                        return e.getKey();
                                    }
                                    tmp = orig.next();
                                }
                                return tmp;
                            }
                        };
                    }
                });
            }

            @Override
            public Iterator<? extends Entry<? extends Item, ? extends Set<? extends Key>>> getIterator() {
                return new Iterator<Entry<? extends Item, ? extends Set<Key>>>(){
                    private final Iterator<? extends Entry<? extends Key, ? extends Item>> orig = $this.getIterator();
                    @SuppressWarnings("unchecked")
                    public java.lang.Object next() {
                        java.lang.Object tmp = orig.next();
                        if (tmp instanceof Finished) {
                            return tmp;
                        }
                        final Item item = ((Entry<? extends Key, ? extends Item>)tmp).getItem();
                        return new Entry<Item, Set<? extends Key>>(item, item(item));
                    }
                };
            }

            @Override
            public long getSize() {
                return $this.getSize();
            }

            @Override
            public java.lang.String toString() {
                return $ceylon$language$Collection$this.toString();
            }

            // concrete interface methods:

            @Override
            @Ignore
            public boolean defines(java.lang.Object key) {
                return $ceylon$language$Correspondence$this.defines(key);
            }

            @Override
            @Ignore
            public boolean definesEvery(Sequential<? extends java.lang.Object> keys) {
                return $ceylon$language$Correspondence$this.definesEvery(keys);
            }

            @Override
            @Ignore
            public boolean definesEvery() {
                return $ceylon$language$Correspondence$this.definesEvery(empty_.getEmpty$());
            }

            @Override
            @Ignore
            public Sequential<? extends java.lang.Object> definesEvery$keys() {
                return empty_.getEmpty$();
            }

            @Override
            @Ignore
            public boolean definesAny(Sequential<? extends java.lang.Object> keys) {
                return $ceylon$language$Correspondence$this.definesAny(keys);
            }

            @Override
            @Ignore
            public boolean definesAny() {
                return $ceylon$language$Correspondence$this.definesAny(empty_.getEmpty$());
            }

            @Override
            @Ignore
            public Sequential<? extends java.lang.Object> definesAny$keys() {
                return empty_.getEmpty$();
            }

            @Override
            @Ignore
            public Sequential<? extends Set<? extends Key>> items(Sequential<? extends java.lang.Object> keys) {
                return $ceylon$language$Correspondence$this.items(keys);
            }

            @Override
            @Ignore
            public Sequential<? extends Set<? extends Key>> items() {
                return $ceylon$language$Correspondence$this.items(empty_.getEmpty$());
            }

            @Override
            @Ignore
            public Sequential<? extends java.lang.Object> items$keys() {
                return empty_.getEmpty$();
            }

            @Override
            @Ignore
            public boolean getEmpty() {
                return $ceylon$language$Collection$this.getEmpty();
            }

            @Override
            @Ignore
            public boolean contains(java.lang.Object element) {
                return $ceylon$language$Collection$this.contains(element);
            }

            @Override
            @Ignore
            public boolean containsEvery(Sequential<?> elements) {
                return $ceylon$language$Category$this.containsEvery(elements);
            }

            @Override
            @Ignore
            public boolean containsEvery() {
                return $ceylon$language$Category$this.containsEvery(empty_.getEmpty$());
            }

            @Override
            @Ignore
            public Sequential<?> containsEvery$elements() {
                return empty_.getEmpty$();
            }

            @Override
            @Ignore
            public boolean containsAny(Sequential<?> elements) {
                return $ceylon$language$Category$this.containsAny(elements);
            }

            @Override
            @Ignore
            public boolean containsAny() {
                return $ceylon$language$Category$this.containsAny(empty_.getEmpty$());
            }

            @Override
            @Ignore
            public Sequential<?> containsAny$elements() {
                return empty_.getEmpty$();
            }

            @Override
            @Ignore
            public Set<? extends Item> getKeys() {
                return Map$impl._getKeys(this);
            }

            @Override
            @Ignore
            public Collection<? extends Set<? extends Key>> getValues() {
                return Map$impl._getValues(this);
            }

            @Override
            @Ignore
            public Map<? extends Set<? extends Key>, ? extends Set<? extends Item>> getInverse() {
                return Map$impl._getInverse(this);
            }
            @Override
            @Ignore
            public Sequential<? extends Entry<? extends Item, ? extends Set<? extends Key>>> getSequence() {
                    return $ceylon$language$Iterable$this.getSequence();
            }
            @Override
            @Ignore
            public Iterable<? extends Entry<? extends Item, ? extends Set<? extends Key>>> getRest() {
                return $ceylon$language$Iterable$this.getRest();
            }
            @Override
            @Ignore
            public Entry<? extends Item, ? extends Set<? extends Key>> getFirst() {
                return $ceylon$language$Iterable$this.getFirst();
            }
            @Override @Ignore
            public Entry<? extends Item, ? extends Set<? extends Key>> getLast() {
                return $ceylon$language$Iterable$this.getLast();
            }
            @Override @Ignore
            public Entry<? extends Item, ? extends Set<? extends Key>> find(Callable<? extends Boolean> f) {
                return $ceylon$language$Iterable$this.find(f);
            }
            @Override @Ignore
            public Entry<? extends Item, ? extends Set<? extends Key>> findLast(Callable<? extends Boolean> f) {
                return $ceylon$language$Iterable$this.findLast(f);
            }
            @Override
            @Ignore
            public Sequential<? extends Entry<? extends Item, ? extends Set<? extends Key>>> sort(Callable<? extends Comparison> f) {
                return $ceylon$language$Iterable$this.sort(f);
            }
            @Override
            @Ignore
            public <Result> Iterable<? extends Result> map(Callable<? extends Result> f) {
                return new MapIterable<Entry<? extends Item, ? extends Set<? extends Key>>, Result>(this, f);
            }
            @Override
            @Ignore
            public Iterable<? extends Entry<? extends Item, ? extends Set<? extends Key>>> filter(Callable<? extends Boolean> f) {
                return new FilterIterable<Entry<? extends Item, ? extends Set<? extends Key>>>(this, f);
            }
            @Override @Ignore
            public <Result> Sequential<? extends Result> collect(Callable<? extends Result> f) {
                return new MapIterable<Entry<? extends Item, ? extends Set<? extends Key>>, Result>(this, f).getSequence();
            }
            @Override
            @Ignore
            public Sequential<? extends Entry<? extends Item, ? extends Set<? extends Key>>> select(Callable<? extends Boolean> f) {
                return new FilterIterable<Entry<? extends Item, ? extends Set<? extends Key>>>(this, f).getSequence();
            }
            @Override
            @Ignore
            public <Result> Result fold(Result ini, Callable<? extends Result> f) {
                return $ceylon$language$Iterable$this.fold(ini, f);
            }
            @Override @Ignore
            public boolean any(Callable<? extends Boolean> f) {
                return $ceylon$language$Iterable$this.any(f);
            }
            @Override @Ignore
            public boolean every(Callable<? extends Boolean> f) {
                return $ceylon$language$Iterable$this.every(f);
            }
			@Override @Ignore
			public Iterable<? extends Entry<? extends Item, ? extends Set<? extends Key>>> skipping(long skip) {
				return $ceylon$language$Iterable$this.skipping(skip);
			}

			@Override @Ignore
			public Iterable<? extends Entry<? extends Item, ? extends Set<? extends Key>>> taking(long take) {
				return $ceylon$language$Iterable$this.taking(take);
			}

			@Override @Ignore
			public Iterable<? extends Entry<? extends Item, ? extends Set<? extends Key>>> by(long step) {
				return $ceylon$language$Iterable$this.by(step);
			}
            @Override @Ignore
            public long count(Callable<? extends Boolean> f) {
                return $ceylon$language$Iterable$this.count(f);
            }
            @Override @Ignore
            public Iterable<? extends Entry<? extends Item, ? extends Set<? extends Key>>> getCoalesced() {
                return $ceylon$language$Iterable$this.getCoalesced();
            }
            @Override @Ignore
            public Iterable<? extends Entry<? extends Integer, ? extends Entry<? extends Item, ? extends Set<? extends Key>>>> getIndexed() {
                return $ceylon$language$Iterable$this.getIndexed();
            }
            @Override @Ignore public <Other>Iterable chain(Iterable<? extends Other> other) {
                return $ceylon$language$Iterable$this.chain(other);
            }
            @Override @Ignore
            public <Key2> Map<? extends Key2, ? extends Sequence<? extends Entry<? extends Item, ? extends Set<? extends Key>>>> group(Callable<? extends Key2> grouping) {
                return $ceylon$language$Iterable$this.group(grouping);
            }

			@Override @Ignore
			public <Result> Map<? extends Item, ? extends Result> mapItems(Callable<? extends Result> mapping) {
			    return Map$impl._mapItems(this, mapping);
			}
        }
        return new inverse();
    }

    public <Result> Map<? extends Key, ? extends Result> mapItems(Callable<Result> mapping) {
        return Map$impl._mapItems($this, mapping);
    }

    private static <Key,Item, Result> Map<? extends Key, ? extends Result> _mapItems(
            final Map<? extends Key, ? extends Item> $this, final Callable<Result> mapping) {
        class mapItems implements Map<Key, Result> {

            private Correspondence$impl<java.lang.Object, Result> correspondence$impl = new Correspondence$impl(this);
            private final ceylon.language.Category$impl $ceylon$language$Category$this;
            private final ceylon.language.Collection$impl $ceylon$language$Collection$this;
            private final ceylon.language.Correspondence$impl $ceylon$language$Correspondence$this;
            private final ceylon.language.Iterable$impl<Entry<? extends Key, ? extends Result>> $ceylon$language$Iterable$this;

            private mapItems() {
                this.$ceylon$language$Category$this = new ceylon.language.Category$impl(this);
                this.$ceylon$language$Collection$this = new ceylon.language.Collection$impl(this);
                this.$ceylon$language$Correspondence$this = new ceylon.language.Correspondence$impl(this);
                this.$ceylon$language$Iterable$this = new ceylon.language.Iterable$impl<Entry<? extends Key, ? extends Result>>(this);
            }
            
            @Ignore
            @Override
            public Correspondence$impl<? super java.lang.Object, ? extends Result> $ceylon$language$Correspondence$impl(){
                return correspondence$impl;
            }

            @Override
            @Ignore
            public Correspondence$impl<? super java.lang.Object, ? extends Result>.Items Items$new(Sequence<? extends java.lang.Object> keys) {
                return correspondence$impl.Items$new(keys);
            }

            @Override @Ignore
            public Result item(java.lang.Object key) {
                Item e = $this.item(key);
                return e == null ? null : mapping.$call(key, e);
            }

            @Override @Ignore
            public boolean defines(java.lang.Object key) {
                return $this.defines(key);
            }

            @Override @Ignore
            public boolean definesEvery(
                    Sequential<? extends java.lang.Object> keys) {
                return $this.definesEvery(keys);
            }

            @Override @Ignore
            public boolean definesEvery() {
                return $this.definesEvery();
            }

            @Override @Ignore
            public Sequential<? extends java.lang.Object> definesEvery$keys() {
                return $this.definesEvery$keys();
            }

            @Override @Ignore
            public boolean definesAny(Sequential<? extends java.lang.Object> keys) {
                return $this.definesAny(keys);
            }

            @Override @Ignore
            public boolean definesAny() {
                return $this.definesAny();
            }

            @Override @Ignore
            public Sequential<? extends java.lang.Object> definesAny$keys() {
                return $this.definesAny$keys();
            }

            @Override
            public Sequential<? extends Result> items(
                    Sequential<? extends java.lang.Object> keys) {
                return $ceylon$language$Correspondence$this.items(keys);
            }

            @Override
            public Sequential<? extends Result> items() {
                return $ceylon$language$Correspondence$this.items(empty_.getEmpty$());
            }

            @Override
            public Sequential<? extends java.lang.Object> items$keys() {
                return empty_.getEmpty$();
            }

            @Override
            public boolean getEmpty() {
                return $this.getEmpty();
            }

            @Override
            public boolean contains(java.lang.Object element) {
                return $ceylon$language$Collection$this.contains(element);
            }

            @Override
            public Iterator<? extends Entry<? extends Key, ? extends Result>> getIterator() {
                final Iterator<? extends Entry<? extends Key, ? extends Item>> iter = $this.getIterator();
                return new Iterator<Entry<Key, Result>>(){
                    @Override @Ignore
                    public java.lang.Object next() {
                        java.lang.Object e = iter.next();
                        return e == exhausted_.getExhausted$() ? e : new Entry(((Entry)e).getKey(),
                                mapping.$call(((Entry)e).getKey(), ((Entry)e).getItem()));
                    }
                };
            }

            @Override
            public Sequential<? extends Entry<? extends Key, ? extends Result>> getSequence() {
                return $ceylon$language$Iterable$this.getSequence();
            }

            @Override
            @Ignore
            public Iterable<? extends Entry<? extends Key, ? extends Result>> getRest() {
                return $ceylon$language$Iterable$this.getRest();
            }

            @Override
            @Ignore
            public Entry<? extends Key, ? extends Result> getFirst() {
                return $ceylon$language$Iterable$this.getFirst();
            }
            @Override @Ignore
            public Entry<? extends Key, ? extends Result> getLast() {
                return $ceylon$language$Iterable$this.getLast();
            }

            @Override
            public <R2> Iterable<? extends R2> map(
                    Callable<? extends R2> collecting) {
                return new MapIterable<Entry<? extends Key, ? extends Result>, R2>(this, collecting);
            }

            @Override
            public Iterable<? extends Entry<? extends Key, ? extends Result>> filter(
                    Callable<? extends Boolean> selecting) {
                return new FilterIterable<Entry<? extends Key, ? extends Result>>(this, selecting);
            }
            @Override
            public <R2> Sequential<? extends R2> collect(
                    Callable<? extends R2> collecting) {
                return new MapIterable<Entry<? extends Key, ? extends Result>, R2>(this, collecting).getSequence();
            }
            @Override
            public Sequential<? extends Entry<? extends Key, ? extends Result>> select(
                    Callable<? extends Boolean> selecting) {
                return new FilterIterable<Entry<? extends Key, ? extends Result>>(this, selecting).getSequence();
            }

            @Override
            public <R2> R2 fold(R2 initial,
                    Callable<? extends R2> accumulating) {
                return $ceylon$language$Iterable$this.fold(initial, accumulating);
            }

            @Override @Ignore
            public Entry<? extends Key, ? extends Result> find(
                    Callable<? extends Boolean> selecting) {
                return $ceylon$language$Iterable$this.find(selecting);
            }

            @Override @Ignore
            public Entry<? extends Key, ? extends Result> findLast(
                    Callable<? extends Boolean> selecting) {
                return $ceylon$language$Iterable$this.findLast(selecting);
            }

            @Override
            public Sequential<? extends Entry<? extends Key, ? extends Result>> sort(
                    Callable<? extends Comparison> comparing) {
                return $ceylon$language$Iterable$this.sort(comparing);
            }

            @Override
            public boolean any(Callable<? extends Boolean> selecting) {
                return $ceylon$language$Iterable$this.any(selecting);
            }

            @Override
            public boolean every(Callable<? extends Boolean> selecting) {
                return $ceylon$language$Iterable$this.every(selecting);
            }

            @Override
            public Iterable<? extends Entry<? extends Key, ? extends Result>> skipping(
                    long skip) {
                return $ceylon$language$Iterable$this.skipping(skip);
            }

            @Override
            public Iterable<? extends Entry<? extends Key, ? extends Result>> taking(
                    long take) {
                return $ceylon$language$Iterable$this.taking(take);
            }

            @Override
            public Iterable<? extends Entry<? extends Key, ? extends Result>> by(
                    long step) {
                return $ceylon$language$Iterable$this.by(step);
            }
            @Override @Ignore
            public long count(Callable<? extends Boolean> f) {
                return $ceylon$language$Iterable$this.count(f);
            }
            @Override @Ignore
            public Iterable<? extends Entry<? extends Key, ? extends Result>> getCoalesced() {
                return $ceylon$language$Iterable$this.getCoalesced();
            }
            @Override @Ignore
            public Iterable<? extends Entry<? extends Integer, ? extends Entry<? extends Key, ? extends Result>>> getIndexed() {
                return $ceylon$language$Iterable$this.getIndexed();
            }
            @Override @Ignore public <Other>Iterable chain(Iterable<? extends Other> other) {
                return $ceylon$language$Iterable$this.chain(other);
            }
            @Override @Ignore
            public <Key2> Map<? extends Key2, ? extends Sequence<? extends Entry<? extends Key, ? extends Result>>> group(Callable<? extends Key2> grouping) {
                return $ceylon$language$Iterable$this.group(grouping);
            }

            @Override
            public long getSize() {
                return $this.getSize();
            }

            @Override
            public boolean containsEvery(Sequential<?> elements) {
                return $ceylon$language$Category$this.containsEvery(elements);
            }

            @Override
            public boolean containsEvery() {
                return $ceylon$language$Category$this.containsEvery(empty_.getEmpty$());
            }

            @Override
            public Sequential<?> containsEvery$elements() {
                return empty_.getEmpty$();
            }

            @Override
            public boolean containsAny(Sequential<?> elements) {
                return $ceylon$language$Category$this.containsAny(elements);
            }

            @Override
            public boolean containsAny() {
                return $ceylon$language$Category$this.containsAny(empty_.getEmpty$());
            }

            @Override
            public Sequential<?> containsAny$elements() {
                return empty_.getEmpty$();
            }

            @Override
            public Collection<? extends Entry<? extends Key, ? extends Result>> getClone() {
                return this;
            }

            @Override
            public Set<? extends Key> getKeys() {
                return $this.getKeys();
            }

            @Override
            public Collection<? extends Result> getValues() {
                return _getValues(this);
            }

            @Override
            public Map<? extends Result, ? extends Set<? extends Key>> getInverse() {
                return _getInverse(this);
            }

            @Override
            public <R2> Map<? extends Key, ? extends R2> mapItems(
                    Callable<? extends R2> mapping) {
                return _mapItems(this, mapping);
            }

            @Override
            public java.lang.String toString() {
            	return $ceylon$language$Collection$this.toString();
            }

        }
        return new mapItems();
    }
}
