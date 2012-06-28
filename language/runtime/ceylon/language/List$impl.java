package ceylon.language;

public final class List$impl<Element> {
    private final List<Element> $this;

    public List$impl(List<Element> $this) {
        this.$this = $this;
    }
    
    public boolean getEmpty(){
        return List$impl.<Element>getEmpty($this);
    }
    static <Element> boolean getEmpty(List<Element> $this){
        return false;
    }

    public long getSize(){
        return List$impl.<Element>getSize($this);
    }
    static <Element> long getSize(List<Element> $this){
        Integer lastIndex = $this.getLastIndex();
        return lastIndex==null ? 0 : lastIndex.longValue()+1;
    }

    public boolean defines(Integer key){
        return List$impl.<Element>_defines($this, key);
    }
    static <Element> boolean _defines(List<Element> $this, Integer key){
        Integer lastIndex = $this.getLastIndex();
        return lastIndex==null ? false : key.longValue() <= lastIndex.longValue();
    }

    public Iterator<? extends Element> getIterator(){
        return List$impl.<Element>_getIterator($this);
    }
    static <Element> Iterator<? extends Element> _getIterator(final List<Element> $this){
        class ListIterator implements Iterator<Element> {
            private long index=0;
            public final java.lang.Object next() { 
                Integer lastIndex = $this.getLastIndex();
                if (lastIndex!=null && index <= lastIndex.longValue()) {
                    return $this.item(Integer.instance(index++));
                } 
                else {
                    return exhausted.getExhausted();
                }
            }
            public final java.lang.String toString() {
                return "listIterator";
            }
        }
        return new ListIterator();
    }
    
    public boolean equals(java.lang.Object that) {
        return List$impl.<Element>_equals($this, that);
    }
    static <Element> boolean _equals(final List<Element> $this, java.lang.Object that) {
        if (that instanceof List) {
            List other = (List) that;
            if (other.getSize()==$this.getSize()) {
                for (int i=0; i<$this.getSize(); i++) {
                    Element x = $this.item(Integer.instance(i));
                    java.lang.Object y = ((List) that).item(Integer.instance(i));
                    if (x==y || x!=null && y!=null && x.equals(y)) {
                        continue;
                    }
                    return false;
                }
                return true;
            }
        }
        return false;
    }
    
    public int hashCode() {
        return List$impl.<Element>_hashCode($this);
    }
    static <Element> int _hashCode(final List<Element> $this) {
        int hashCode = 1;
        java.lang.Object elem;
        for (Iterator<? extends Element> iter=$this.getIterator(); 
                !((elem = iter.next()) instanceof Finished);) {
            hashCode *= 31;
            if (elem != null) {
                hashCode += elem.hashCode();
            }
        }
        return hashCode;
    }
    
    public java.lang.String toString(List<Element> $this) {
        return List$impl.<Element>_toString($this);
    }
    static <Element> java.lang.String _toString(List<Element> $this) {
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

    public Element findLast(Callable<? extends Boolean> sel) {
        return List$impl.<Element>_findLast($this, sel);
    }
    public static <Element> Element _findLast(List<Element> $this, Callable<? extends Boolean> sel) {
        Integer last = $this.getLastIndex();
        if (last != null) {
            while (!last.getNegative()) {
                Element e = $this.item(last);
                if (e != null && sel.$call(e).booleanValue()) {
                    return e;
                }
                last = last.getPredecessor();
            }
        }
        return null;
    }

}