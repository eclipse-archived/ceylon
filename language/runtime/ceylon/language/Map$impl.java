package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ignore;

@Ignore
public final class Map$impl<Key,Item> {
    private final Map<Key, Item> $this;

    public Map$impl(Map<Key,Item> $this) {
        this.$this = $this;
    }
    
    public long count(java.lang.Object element) {
        return _count($this, element);
    }
    static <Key,Item> long _count(final Map<Key,Item> $this, java.lang.Object element) {
        if (element instanceof Entry) {
            Entry e = (Entry) element;
            java.lang.Object item = $this.item(e.getKey());
            if (item!=null) {
                return item.equals(e.getItem()) ? 1 : 0;
            }
            else {
                return 0;
            }
        }
        else {
            return 0;
        }
    }
    
    public boolean equals(java.lang.Object that) {
        return _equals($this, that);
    }
    static <Key,Item> boolean _equals(final Map<Key,Item> $this, java.lang.Object that) {
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
    static <Key,Item> int _hashCode(final Map<Key,Item> $this) {
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
    static <Key,Item> Set<? extends Key> _getKeys(final Map<Key,Item> $this){
        class keySet implements Set<Key>{

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
                return bottom.getBottom();
            }

            @Override
            public long getSize() {
                return $this.getSize();
            }
            
            @Override
            public java.lang.String toString() {
                return "";
            }

            @Override
            public <Other> Set<? extends Object> union(Set<? extends Other> set) {
                return bottom.getBottom();
            }

            @Override
            public <Other> Set<? extends Object> intersection(Set<? extends Other> set) {
                return bottom.getBottom();
            }

            @Override
            public <Other> Set<? extends Object> exclusiveUnion(Set<? extends Other> set) {
                return bottom.getBottom();
            }

            @Override
            public <Other> Set<? extends Key> complement(Set<? extends Other> set) {
                return bottom.getBottom();
            }

            // concrete interface methods:
            
            @Override
            public boolean getEmpty() {
                return Collection$impl._getEmpty(this);
            }

            @Override
            public boolean contains(java.lang.Object element) {
                return Collection$impl._contains(this, element);
            }

            @Override
            public long count(java.lang.Object element) {
                return Set$impl._count(this, element);
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
            public Iterable<?> containsEvery$elements() {
                return $empty.getEmpty();
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
            public Iterable<?> containsAny$elements() {
                return $empty.getEmpty();
            }

            @Override
            public boolean superset(Set<? extends java.lang.Object> set) {
                return Set$impl._superset(this, set);
            }

            @Override
            public boolean subset(Set<? extends java.lang.Object> set) {
                return Set$impl._subset(this, set);
            }
    @Override public Iterable<? extends Key> getSequence() { return Iterable$impl._getSequence(this); }
    @Override public Key find(Callable<? extends Boolean> f) { return Iterable$impl._find(this, f); }
    @Override public <Result> Iterable<Result> map(Callable<? extends Result> f) { return new MapIterable<Key, Result>(this, f); }
    @Override public Iterable<? extends Key> filter(Callable<? extends Boolean> f) { return new FilterIterable<Key>(this, f); }
    @Override public <Result> Result fold(Result ini, Callable<? extends Result> f) { return Iterable$impl._fold(this, ini, f); }
        }
        return new keySet();
    }
    
    public Collection<? extends Item> getValues(){
        return _getValues($this);
    }
    static <Key,Item> Collection<? extends Item> _getValues(final Map<Key,Item> $this){
        class valueCollection implements Collection<Item> {

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
                return bottom.getBottom();
            }

            @Override
            public long getSize() {
                return $this.getSize();
            }
            
            @Override
            public java.lang.String toString() {
                return "";
            }

            // concrete interface methods:
            
            @Override
            public boolean containsEvery(Iterable<?> elements) {
                return Category$impl._containsEvery(this, elements);
            }
            
            @Override
            public boolean containsEvery() {
                return Category$impl._containsEvery(this, $empty.getEmpty());
            }
            
            @Override
            public Iterable<?> containsEvery$elements() {
                return $empty.getEmpty();
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
            public Iterable<?> containsAny$elements() {
                return $empty.getEmpty();
            }

            @Override
            public boolean getEmpty() {
                return Collection$impl._getEmpty(this);
            }

            @Override
            public boolean contains(java.lang.Object element) {
                return Collection$impl._contains(this, element);
            }

            @Override
            public long count(java.lang.Object element) {
                return Collection$impl._count(this, element);
            }
    @Override public Iterable<? extends Item> getSequence() { return Iterable$impl._getSequence(this); }
    @Override public Item find(Callable<? extends Boolean> f) { return Iterable$impl._find(this, f); }
    @Override public <Result> Iterable<Result> map(Callable<? extends Result> f) { return new MapIterable<Item, Result>(this, f); }
    @Override public Iterable<? extends Item> filter(Callable<? extends Boolean> f) { return new FilterIterable<Item>(this, f); }
    @Override public <Result> Result fold(Result ini, Callable<? extends Result> f) { return Iterable$impl._fold(this, ini, f); }
        }
        return new valueCollection();
    }
    
    public Map<? extends Item, ? extends Set<? extends Key>> getInverse(){
        return _getInverse($this);
    }
    static <Key,Item> Map<? extends Item, ? extends Set<? extends Key>> _getInverse(final Map<Key,Item> $this){
        class inverse implements Map<Item, Set<Key>>{

            @Override
            public Collection<? extends Entry<? extends Item, ? extends Set<Key>>> getClone() {
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
            public Set<Key> item(java.lang.Object key) {
                return bottom.getBottom();
            }

            @Override
            public Iterator<? extends Entry<? extends Item, ? extends Set<Key>>> getIterator() {
                return bottom.getBottom();
            }

            @Override
            public long getSize() {
                return $this.getSize();
            }

            @Override
            public java.lang.String toString() {
                return "";
            }
            
            // concrete interface methods:
            
            @Override
            public boolean defines(java.lang.Object key) {
                return Correspondence$impl._defines(this, key);
            }

            @Override
            public boolean definesEvery(Iterable<? extends java.lang.Object> keys) {
                return Correspondence$impl._definesEvery(this, keys);
            }
            
            @Override
            public boolean definesEvery() {
                return Correspondence$impl._definesEvery(this, $empty.getEmpty());
            }
            
            @Override
            public Iterable<? extends java.lang.Object> definesEvery$keys() {
                return $empty.getEmpty();
            }

            @Override
            public boolean definesAny(Iterable<? extends java.lang.Object> keys) {
                return Correspondence$impl._definesAny(this, keys);
            }
            
            @Override
            public boolean definesAny() {
                return Correspondence$impl._definesAny(this, $empty.getEmpty());
            }
            
            @Override
            public Iterable<? extends java.lang.Object> definesAny$keys() {
                return $empty.getEmpty();
            }

            @Override
            public Iterable<? extends Set<Key>> items(Iterable<? extends java.lang.Object> keys) {
                return Correspondence$impl._items(this, keys);
            }
            
            @Override
            public Iterable<? extends Set<Key>> items() {
                return Correspondence$impl._items(this, $empty.getEmpty());
            }
            
            @Override
            public Iterable<? extends java.lang.Object> items$keys() {
                return $empty.getEmpty();
            }

            @Override
            public boolean getEmpty() {
                return Collection$impl._getEmpty(this);
            }

            @Override
            public boolean contains(java.lang.Object element) {
                return Collection$impl._contains(this, element);
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
            public Iterable<?> containsEvery$elements() {
                return $empty.getEmpty();
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
            public Iterable<?> containsAny$elements() {
                return $empty.getEmpty();
            }

            @Override
            public long count(java.lang.Object element) {
                return Map$impl._count(this, element);
            }

            @Override
            public Set<? extends Item> getKeys() {
                return Map$impl._getKeys(this);
            }

            @Override
            public Collection<? extends Set<Key>> getValues() {
                return Map$impl._getValues(this);
            }

            @Override
            public Map<? extends Set<Key>, ? extends Set<? extends Item>> getInverse() {
                return Map$impl._getInverse(this);
            }
    @Override public Iterable<? extends Entry<? extends Item, ? extends Set<Key>>> getSequence() { return Iterable$impl._getSequence(this); }
    @Override public Entry<? extends Item, ? extends Set<Key>> find(Callable<? extends Boolean> f) { return Iterable$impl._find(this, f); }
    @Override public <Result> Iterable<Result> map(Callable<? extends Result> f) { return new MapIterable<Entry<? extends Item, ? extends Set<Key>>, Result>(this, f); }
    @Override public Iterable<? extends Entry<? extends Item, ? extends Set<Key>>> filter(Callable<? extends Boolean> f) { return new FilterIterable<Entry<? extends Item, ? extends Set<Key>>>(this, f); }
    @Override public <Result> Result fold(Result ini, Callable<? extends Result> f) { return Iterable$impl._fold(this, ini, f); }
        }
        return new inverse();
    }
}
