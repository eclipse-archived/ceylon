package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;

@Ceylon
@TypeParameters({@TypeParameter(value = "Key", variance = Variance.OUT,
                               satisfies = "ceylon.language.Object"),
                 @TypeParameter(value = "Item", variance = Variance.OUT, 
                               satisfies = "ceylon.language.Object")})
@SatisfiedTypes({"ceylon.language.Collection<ceylon.language.Entry<Key,Item>>",
                 "ceylon.language.Correspondence<ceylon.language.Object,Item>",
                 "ceylon.language.Cloneable<ceylon.language.Map<Key,Item>>"})
public interface Map<Key,Item> 
        extends Correspondence<java.lang.Object, Item>, 
                Collection<Entry<? extends Key,? extends Item>> {

    @Override
    public long count(@Name("element") @TypeInfo("ceylon.language.Object") 
    java.lang.Object element);
    
    @Override
    public boolean equals(@Name("that") @TypeInfo("ceylon.language.Object")
    java.lang.Object that);
    
    @Override
    public int hashCode();
    
    @Override
    @TypeInfo("ceylon.language.Set<Key>")
    public Set<? extends Key> getKeys();
    
    @TypeInfo("ceylon.language.Collection<Item>")
    public Collection<? extends Item> getValues();
    
    @TypeInfo("ceylon.language.Map<Item,ceylon.language.Set<Key>>")
    public Map<? extends Item, ? extends Set<? extends Key>> getInverse();
    
    @Ignore
    public static final class Map$impl {
        public static <Key,Item> long count(final Map<Key,Item> $this, java.lang.Object element) {
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
        public static <Key,Item> boolean equals(final Map<Key,Item> $this, java.lang.Object that) {
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
        public static <Key,Item> int hashCode(final Map<Key,Item> $this) {
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

        public static <Key,Item> Set<? extends Key> getKeys(final Map<Key,Item> $this){
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
                    return Collection$impl.getEmpty(this);
                }

                @Override
                public boolean contains(java.lang.Object element) {
                    return Collection$impl.contains(this, element);
                }

                @Override
                public long count(java.lang.Object element) {
                    return Set$impl.count(this, element);
                }

                @Override
                public boolean containsEvery(Iterable<?> elements) {
                    return Category$impl.containsEvery(this, elements);
                }

                @Override
                public boolean containsAny(Iterable<?> elements) {
                    return Category$impl.containsAny(this, elements);
                }

                @Override
                public boolean superset(Set<? extends java.lang.Object> set) {
                    return Set$impl.superset(this, set);
                }

                @Override
                public boolean subset(Set<? extends java.lang.Object> set) {
                    return Set$impl.subset(this, set);
                }
            }
            return new keySet();
        }
        
        public static <Key,Item> Collection<? extends Item> getValues(final Map<Key,Item> $this){
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
                    return Category$impl.containsEvery(this, elements);
                }

                @Override
                public boolean containsAny(Iterable<?> elements) {
                    return Category$impl.containsAny(this, elements);
                }


                @Override
                public boolean getEmpty() {
                    return Collection$impl.getEmpty(this);
                }

                @Override
                public boolean contains(java.lang.Object element) {
                    return Collection$impl.contains(this, element);
                }

                @Override
                public long count(java.lang.Object element) {
                    return Collection$impl.count(this, element);
                }
            }
            return new valueCollection();
        }
        
        public static <Key,Item> Map<? extends Item, ? extends Set<? extends Key>> getInverse(final Map<Key,Item> $this){
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
                    return Correspondence$impl.defines(this, key);
                }

                @Override
                public boolean definesEvery(Iterable<? extends java.lang.Object> keys) {
                    return Correspondence$impl.definesEvery(this, keys);
                }

                @Override
                public boolean definesAny(Iterable<? extends java.lang.Object> keys) {
                    return Correspondence$impl.definesAny(this, keys);
                }

                @Override
                public Iterable<? extends Set<Key>> items(Iterable<? extends java.lang.Object> keys) {
                    return Correspondence$impl.items(this, keys);
                }

                @Override
                public boolean getEmpty() {
                    return Collection$impl.getEmpty(this);
                }

                @Override
                public boolean contains(java.lang.Object element) {
                    return Collection$impl.contains(this, element);
                }

                @Override
                public boolean containsEvery(Iterable<?> elements) {
                    return Category$impl.containsEvery(this, elements);
                }

                @Override
                public boolean containsAny(Iterable<?> elements) {
                    return Category$impl.containsAny(this, elements);
                }

                @Override
                public long count(java.lang.Object element) {
                    return Map$impl.count(this, element);
                }

                @Override
                public Set<? extends Item> getKeys() {
                    return Map$impl.getKeys(this);
                }

                @Override
                public Collection<? extends Set<Key>> getValues() {
                    return Map$impl.getValues(this);
                }

                @Override
                public Map<? extends Set<Key>, ? extends Set<? extends Item>> getInverse() {
                    return Map$impl.getInverse(this);
                }
            }
            return new inverse();
        }
    }
}
