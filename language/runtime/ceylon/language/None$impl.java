package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ignore;

@Ignore
public final class None$impl<Element> {
    private final None<Element> $this;
    public None$impl(None<Element> $this) {
        this.$this = $this;
    }
    
    public Element getFirst(){
        return _getFirst($this);
    }
    static <Element> Element _getFirst(None<Element> $this){
        return null;
    }
    public Element getLast(){
        return _getLast($this);
    }
    static <Element> Element _getLast(None<Element> $this){
        return null;
    }
    
    public Iterator<Element> getIterator(){
        return _getIterator($this);
    }
    static <Element> Iterator<Element> _getIterator(None<Element> $this){
        return emptyIterator_.getEmptyIterator$();
    }
    
    public long getSize(){
        return _getSize($this);
    }
    static <Element> long _getSize(None<Element> $this){
        return 0;
    }
    
    public boolean getEmpty(){
        return _getEmpty($this);
    }
    static <Element> boolean _getEmpty(None<Element> $this){
        return true;
    }
}