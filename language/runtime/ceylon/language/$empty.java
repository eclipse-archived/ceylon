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
        public Iterable<? extends java.lang.Object> items(Iterable keys) {
            return Correspondence$impl.items(this, keys);
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
        public Empty segment(Integer from, Integer length) {
        	return this;
        }
        
        @Override
        public Empty span(Integer from, Integer to) {
        	return this;
        }

    };
    
    public static Empty getEmpty(){
        return value;
    }
    
}