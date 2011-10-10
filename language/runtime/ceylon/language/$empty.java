package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Attribute;
import com.redhat.ceylon.compiler.metadata.java.Ceylon;
import com.redhat.ceylon.compiler.metadata.java.TypeInfo;

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
        public Sequence<? extends java.lang.Object> items(Iterable<? extends Natural> keys) {
            return Correspondence$impl.items(this, keys);
        }

        @Override
        @TypeInfo("ceylon.language.Natural")
        public long getSize() {
            return Empty$impl.getSize(this).longValue();
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

    };
    
    public static Empty getEmpty(){
        return value;
    }
}