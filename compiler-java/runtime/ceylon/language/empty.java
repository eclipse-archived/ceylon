package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Attribute;
import com.redhat.ceylon.compiler.metadata.java.Ceylon;

@Ceylon @Attribute
public class $empty {
    private final static Empty value = new Empty(){

        @Override
        public boolean defines(Equality key) {
            return false;
        }

        @Override
        public Category getKeys() {
            throw new RuntimeException("Not yet implemented");
        }

        @Override
        public boolean definesEvery(Equality... keys) {
            return false;
        }

        @Override
        public boolean definesAny(Equality... keys) {
            return false;
        }

        @Override
        public java.lang.Object[] values(Equality... keys) {
            return null;
        }

        @Override
        public Iterator iterator() {
            return new Iterator() {

                @Override
                public java.lang.Object getHead() {
                    return null;
                }

                @Override
                public Iterator getTail() {
                    return this;
                }
                
            };
        }

        @Override
        public Natural getSize() {
            return Natural.instance(0);
        }

        @Override
        public boolean getEmpty() {
            return false;
        }

        @Override
        public java.lang.Object value(Equality key) {
            return null;
        }

        @Override
        public java.lang.Object getFirst() {
            return null;
        }
    };
    
    public static Empty getEmpty(){
        return value;
    }
}