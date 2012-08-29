package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ignore;

@Ignore
public final class Collection$impl<Element> {
    private final Collection<Element> $this;
    public Collection$impl(Collection<Element> $this) {
        this.$this = $this;
    }
    public boolean getEmpty(){
        return Collection$impl.<Element>_getEmpty(this.$this);
    }
    static <Element> boolean _getEmpty(Collection<Element> $this){
        return $this.getSize() == 0;
    }
    
    public boolean contains(java.lang.Object element){
        return Collection$impl.<Element>_contains(this.$this, element);
    }
    static <Element> boolean _contains(Collection<Element> $this, java.lang.Object element){
        java.lang.Object elem;
        for (ceylon.language.Iterator<?> $element$iter$1 = $this.getIterator(); !((elem = $element$iter$1.next()) instanceof Finished);) {
            if (elem!=null && element.equals(elem)) {
                return true;
            }
        }
        return false;
    }

    public java.lang.String toString() {
        return Collection$impl.<Element>_toString($this);
    }
    static <Element> java.lang.String _toString(Collection<Element> $this) {
        if ($this.getEmpty()) return "{}";
        java.lang.StringBuilder result = new java.lang.StringBuilder("{ ");
        java.lang.Object elem;
        boolean first=true;
        for (Iterator<? extends Element> iter=$this.getIterator(); 
                !((elem = iter.next()) instanceof Finished);) {
            if (first) {
                first = false;
            }
            else {
                result.append(", ");
            }
            result.append(elem);
        }
        result.append(" }");
        return result.toString();
    }

}
