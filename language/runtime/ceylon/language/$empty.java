package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Attribute;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;

@Ceylon @Attribute
public class $empty {
    private final static Empty value = new Empty() {

        @Override
        public boolean defines(java.lang.Object key) {
            return Correspondence$impl.defines(this, (Integer)key);
        }

        @Override
        public Category getKeys() {
            return Correspondence$impl.getKeys(this);
        }

        @Override
        public boolean definesEvery(Iterable keys) {
            return Correspondence$impl.definesEvery(this, keys);
        }

        @Override
        public boolean definesAny(Iterable keys) {
            return Correspondence$impl.definesAny(this, keys);
        }

        @Override
        public List<? extends java.lang.Object> items(Iterable keys) {
            return Correspondence$impl.items(this, keys);
        }
        
        @Override
        public boolean containsEvery(ceylon.language.Iterable<?> elements) {
            return Category$impl.containsEvery(this, elements);
        }

        @Override
        public boolean containsAny(ceylon.language.Iterable<?> elements) {
            return Category$impl.containsAny(this, elements);
        }

        @Override
        public long getSize() {
            return Empty$impl.getSize(this);
        }

        @Override
        public boolean getEmpty() {
            return Empty$impl.getEmpty(this);
        }

        @Override
        public Iterator<java.lang.Object> getIterator() {
            return Empty$impl.getIterator(this);
        }

        @Override
        public java.lang.Object item(java.lang.Object key) {
            return Empty$impl.item(this, (Integer)key);
        }

        @Override
        public java.lang.Object getFirst() {
            return Empty$impl.getFirst(this);
        }

        @Override
        public java.lang.String toString() {
        	return Empty$impl.toString(this);
        }
        
        @Override
        public Empty segment(Comparable from, Comparable length) {
            return Empty$impl.segment(this, from, length);
        }
        
        @Override
        public Empty span(Comparable from, Comparable to) {
            return Empty$impl.span(this, from, to);
        }

        @Override
        public Empty span(Integer from, Integer to) {
            return Empty$impl.span(this, from, to);
        }

        @Override
        public Empty segment(Integer from, Integer length) {
            return Empty$impl.segment(this, from, length);
        }

        @Override
        public boolean contains(java.lang.Object element) {
            return Empty$impl.contains(this, element);
        }

        @Override
        public long count(java.lang.Object element) {
            return Empty$impl.count(this, element);
        }

        @Override
        public Cloneable getClone() {
            return Empty$impl.getClone(this);
        }

        @Override
        public Integer getLastIndex() {
            return Empty$impl.getLastIndex(this);
        }

        @Override
        public boolean defines(Integer index) {
            return Empty$impl.defines(this, index);
        }
        
        public boolean equals(Object that) {
            return that instanceof List ?
                    ((List) that).getEmpty() : false;
        }
        
        @Override
        public int hashCode() {
            return 0;
        }

    };
    
    public static Empty getEmpty(){
        return value;
    }
    
}