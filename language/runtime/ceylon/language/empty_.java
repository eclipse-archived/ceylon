package ceylon.language;

import com.redhat.ceylon.compiler.java.language.ArraySequence;
import com.redhat.ceylon.compiler.java.language.InternalMap;
import com.redhat.ceylon.compiler.java.metadata.Attribute;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;

@Ceylon(major = 3) @Attribute
public class empty_ {
    private static final class EmptyValue implements Empty {
        private final ceylon.language.Category$impl $ceylon$language$Category$this;
        private final ceylon.language.Collection$impl $ceylon$language$Collection$this;
        private final ceylon.language.Correspondence$impl $ceylon$language$Correspondence$this;

        private EmptyValue() {
            this.$ceylon$language$Category$this = new ceylon.language.Category$impl(this);
            this.$ceylon$language$Collection$this = new ceylon.language.Collection$impl(this);
            this.$ceylon$language$Correspondence$this = new ceylon.language.Correspondence$impl(this);
        }

        private Correspondence$impl<Integer,java.lang.Object> correspondence$impl = new Correspondence$impl<Integer,java.lang.Object>(this);
        
        @Ignore
        @Override
        public Correspondence$impl<? super Integer,? extends java.lang.Object> $ceylon$language$Correspondence$impl(){
            return correspondence$impl;
        }

        @Override
        @Ignore
        public Correspondence$impl<? super Integer, ? extends java.lang.Object>.Items Items$new(Sequence<? extends Integer> keys) {
            return correspondence$impl.Items$new(keys);
        }

        @Override
        @Ignore
        public Category getKeys() {
            return $ceylon$language$Correspondence$this.getKeys();
        }

        @Override
        @SuppressWarnings({"rawtypes"})
        public boolean definesEvery(List keys) {
            return false;
        }
        @Override
        public boolean definesEvery() {
            return false;
        }
        @Override
        @SuppressWarnings({"rawtypes", "unchecked"})
        public List definesEvery$keys() {
            return this;
        }

        @Override
        @SuppressWarnings({"rawtypes"})
        public boolean definesAny(List keys) {
            return false;
        }
        @Override
        public boolean definesAny() {
            return false;
        }
        @Override
        @SuppressWarnings({"rawtypes", "unchecked"})
        public List definesAny$keys() {
            return this;
        }

        @Override
        @SuppressWarnings({"rawtypes"})
        public List<? extends java.lang.Object> items(List keys) {
            return this;
        }
        @Override
        public List<? extends java.lang.Object> items() {
            return this;
        }
        @Override
        @SuppressWarnings({"rawtypes", "unchecked"})
        public List items$keys() {
            return this;
        }
        
        @Override
        @Ignore
        public boolean containsEvery(ceylon.language.List<?> elements) {
            return $ceylon$language$Category$this.containsEvery(elements);
        }
        @Override
        @Ignore
        public boolean containsEvery() {
            return false;
        }
        @Override
        @Ignore
        @SuppressWarnings({"rawtypes", "unchecked"})
        public List containsEvery$elements() {
            return this;
        }

        @Override
        @Ignore
        public boolean containsAny(ceylon.language.List<?> elements) {
            return $ceylon$language$Category$this.containsAny(elements);
        }
        @Override
        @Ignore
        public boolean containsAny() {
            return false;
        }
        @Override
        @Ignore
        @SuppressWarnings({"rawtypes", "unchecked"})
        public List containsAny$elements() {
            return this;
        }

        @Override
        @Ignore
        public long getSize() {
            return Empty$impl._getSize(this);
        }

        @Override
        @Ignore
        public boolean getEmpty() {
            return $ceylon$language$Collection$this.getEmpty();
        }

        @Override
        @SuppressWarnings({"rawtypes", "unchecked"})
        public Iterable getRest() {
            return this;
        }

        @Override
        @Ignore
        @SuppressWarnings({"rawtypes", "unchecked"})
        public Iterator getIterator() {
            return None$impl._getIterator(this);
        }

        @Override
        @Ignore
        public java.lang.Object item(Integer key) {
            return Empty$impl._item(this, key);
        }

        @Override
        @Ignore
        public java.lang.Object getFirst() {
            return None$impl._getFirst(this);
        }
        @Override @Ignore public java.lang.Object getLast() {
            return None$impl._getLast(this);
        }

        @Override
        @Ignore
        public java.lang.String toString() {
        	return Empty$impl._toString(this);
        }
        
        @Override
        @Ignore
        public boolean contains(java.lang.Object element) {
            return Empty$impl._contains(this, element);
        }

        @Override
        @Ignore
        public Empty getClone() {
            return Empty$impl._getClone(this);
        }

        @Override
        @Ignore
        public Empty getReversed() {
            return Empty$impl._getReversed(this);
        }

        @Override
        @Ignore
        public Integer getLastIndex() {
            return Empty$impl._getLastIndex(this);
        }

        @Override
        @Ignore
        public boolean defines(Integer key) {
            return Empty$impl._defines(this, key);
        }

        @Override
        public boolean equals(java.lang.Object that) {
            return that instanceof List ?
                    ((List) that).getEmpty() : false;
        }
        
        @Override
        public int hashCode() {
            return 1;
        }

        @Override
        @Ignore
        public Empty span(Integer from, Integer to) {
            return Empty$impl._span(this, (Integer)from, (Integer)to);
        }

        @Override
        @Ignore
        public Empty segment(Integer from, long length) {
            return Empty$impl._segment(this, (Integer)from, length);
        }

        @Override @Ignore public Empty getSequence() { return this; }
        @SuppressWarnings("rawtypes")
        @Override @Ignore public Empty find(Callable f) { return null; }
        @SuppressWarnings("rawtypes")
        @Override @Ignore public Empty findLast(Callable f) { return null; }
        @SuppressWarnings("rawtypes")
        @Override @Ignore public Empty map(Callable f) { return this; }
        @SuppressWarnings("rawtypes")
        @Override @Ignore public Empty sort(Callable f) { return this; }
        @SuppressWarnings("rawtypes")
        @Override @Ignore public Empty filter(Callable f) { return this; }
        @SuppressWarnings("rawtypes")
        @Override @Ignore public Empty collect(Callable f) { return this; }
        @SuppressWarnings("rawtypes")
        @Override @Ignore public Empty select(Callable f) { return this; }
        @SuppressWarnings({"rawtypes", "unchecked"})
        @Override @Ignore public java.lang.Object fold(java.lang.Object ini, Callable f) { return ini; }
        @Override @Ignore public boolean any(Callable<? extends Boolean> f) { return false; }
        @Override @Ignore public boolean every(Callable<? extends Boolean> f) { return false; }
        @Override @Ignore public Empty skipping(long s) { return this; }
        @Override @Ignore public Empty taking(long s) { return this; }
        @Override @Ignore public Empty by(long s) { return this; }
        @Override @Ignore public long count(Callable<? extends Boolean> f) { return 0; }
        @Override @Ignore public Empty getCoalesced() { return this; }
        @SuppressWarnings({"rawtypes", "unchecked"})
        @Override @Ignore public Iterable getIndexed() { return this; }
        @SuppressWarnings("rawtypes")
        @Override @Ignore public <Other>Iterable chain(Iterable<? extends Other> other) { return other; }
        @SuppressWarnings({"rawtypes", "unchecked"})
        @Override @Ignore public <Other>Sequence withLeading(Other e) {
            return new ArraySequence<Other>(e);
        }
        @SuppressWarnings({"rawtypes", "unchecked"})
        @Override @Ignore public <Other>Sequence withTrailing(Other e) {
            return new ArraySequence<Other>(e);
        }
        @Override @Ignore
        public <Key> Map<? extends Key, ? extends Sequence<? extends java.lang.Object>> group(Callable<? extends Key> grouping) {
            return new InternalMap<Key, Sequence<? extends java.lang.Object>>(java.util.Collections.<Key,Sequence<java.lang.Object>>emptyMap());
        }
    };
    private final static Empty value = new EmptyValue();
    
    public static Empty getEmpty$(){
        return value;
    }

}
