package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Attribute;
import com.redhat.ceylon.compiler.metadata.java.Ceylon;

@Ceylon @Attribute
public class $empty {
    private final static Empty value = new Empty(){

        @Override
        public boolean defines(Natural key) {
            return Correspondence$impl.defines(this, key);
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
        public Iterable<? extends java.lang.Object> items(Iterable<? extends Natural> keys) {
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
        public java.lang.Object item(Natural key) {
            return Empty$impl.item(this, key);
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
        public Empty segment(long from, long length) {
        	return this;
        }
        
        @Override
        public Empty span(long from, long to) {
        	return this;
        }

    };
    
    public static Empty getEmpty(){
        return value;
    }
    
}