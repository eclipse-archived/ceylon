package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ignore;

@Ignore
public final class Some$impl<Element> {
    private final Some<Element> $this;
    public Some$impl(Some<Element> $this) {
        this.$this = $this;
    }
    
    public Element getFirst(){
        return _getFirst($this);
    }
    static <Element> Element _getFirst(Some<Element> $this){
        java.lang.Object first = $this.getIterator().next();
        if (first instanceof Finished) {
            throw new Exception(null, null);
        }
        else {
            return (Element) first;
        }
    }
    public boolean getEmpty(){
        return _getEmpty($this);
    }
    static <Element> boolean _getEmpty(Some<Element> $this){
        return false;
    }
}