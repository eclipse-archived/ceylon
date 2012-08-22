package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Object;

@Ceylon(major = 3) @Object
//TODO: extends Iterator<Bottom>
public class emptyIterator_ implements Iterator {
    
    private final static emptyIterator_ emptyIterator = new emptyIterator_();
    
    public static emptyIterator_ getEmptyIterator(){
        return emptyIterator;
    }
    
    @Override
    public java.lang.Object next() {
        return exhausted_.getExhausted();
    }

    @Override
    public java.lang.String toString() {
        return "emptyIterator";
    }
}
