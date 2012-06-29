package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ignore;

@Ignore
public final class Sequence$impl<Element> {
    
    private final Sequence<Element> $this;
    public Sequence$impl(Sequence<Element> $this) {
        this.$this = $this;
    }

    public Element getLast(){
        return _getLast($this);
    }
    static <Element> Element _getLast(Sequence<Element> $this){
        Element x = $this.item(Integer.instance($this.getLastIndex().longValue()));
        if (x != null) {
            return x;
        }
        else {
            return $this.getFirst(); //actually never occurs
        } 
    }
    
    public Sequence<? extends Element> getSequence() {
        return _getSequence($this);
    }
    static <Element> Sequence<? extends Element> _getSequence(Sequence<Element> $this) {
        return $this;
    }
    
}    