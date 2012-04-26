package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ignore;

@Ignore
public final class FixedSized$impl<Element> {
    private final FixedSized<Element> $this;
    public FixedSized$impl(FixedSized<Element> $this) {
        this.$this = $this;
    }
    public Element getFirst(){
        return FixedSized$impl.<Element>_getFirst($this);
    }
    static <Element> Element _getFirst(FixedSized<Element> $this){
        java.lang.Object first = $this.getIterator().next();
        if (first instanceof Finished) {
            return null;
        }
        else {
            return (Element) first;
        }
    }
}