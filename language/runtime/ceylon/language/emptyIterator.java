package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Object;

@Ceylon(major = 1) @Object
//TODO: extends Iterator<Bottom>
public class emptyIterator implements Iterator {
    
    private final static emptyIterator emptyIterator = new emptyIterator();
    
    public static emptyIterator getEmptyIterator(){
        return emptyIterator;
    }
    
    @Override
    public java.lang.Object next() {
        return exhausted.getExhausted();
    }

    @Override
    public java.lang.String toString() {
        return "emptyIterator";
    }
}
