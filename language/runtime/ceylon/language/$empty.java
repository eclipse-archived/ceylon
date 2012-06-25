package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Attribute;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;

@Ceylon(major = 1) @Attribute
public class $empty {
    private final static Empty value = new Empty() {

        @Override
        @Ignore
        public Category getKeys() {
            return Correspondence$impl._getKeys(this);
        }

        @Override
        public boolean definesEvery(Iterable keys) {
            return false;
        }
        @Override
        public boolean definesEvery() {
            return false;
        }
        @Override
        public Iterable definesEvery$keys() {
            return this;
        }

        @Override
        public boolean definesAny(Iterable keys) {
            return false;
        }
        @Override
        public boolean definesAny() {
            return false;
        }
        @Override
        public Iterable definesAny$keys() {
            return this;
        }

        @Override
        public List<? extends java.lang.Object> items(Iterable keys) {
            return this;
        }
        @Override
        public List<? extends java.lang.Object> items() {
            return this;
        }
        @Override
        public Iterable items$keys() {
            return this;
        }
        
        @Override
        @Ignore
        public boolean containsEvery(ceylon.language.Iterable<?> elements) {
            return Category$impl._containsEvery(this, elements);
        }
        @Override
        @Ignore
        public boolean containsEvery() {
            return false;
        }
        @Override
        @Ignore
        public Iterable containsEvery$elements() {
            return this;
        }

        @Override
        @Ignore
        public boolean containsAny(ceylon.language.Iterable<?> elements) {
            return Category$impl._containsAny(this, elements);
        }
        @Override
        @Ignore
        public boolean containsAny() {
            return false;
        }
        @Override
        @Ignore
        public Iterable containsAny$elements() {
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
            return Collection$impl._getEmpty(this);
        }

        @Override
        @Ignore
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
        public long count(java.lang.Object element) {
            return Empty$impl._count(this, element);
        }

        @Override
        @Ignore
        public Empty getClone() {
            return Empty$impl._getClone(this);
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
        
        public boolean equals(Object that) {
            return that instanceof List ?
                    ((List) that).getEmpty() : false;
        }
        
        @Override
        public int hashCode() {
            return 0;
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

    @Override public Empty getSequence() { return this; }
    @Override public Empty find(Callable f) { return null; }
    @Override public Empty map(Callable f) { return this; }
    @Override public Empty sorted(Callable f) { return this; }
    @Override public Empty filter(Callable f) { return this; }
    @Override public java.lang.Object fold(java.lang.Object ini, Callable f) { return ini; }
    };
    
    public static Empty getEmpty(){
        return value;
    }
    
}
