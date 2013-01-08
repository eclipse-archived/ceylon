package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ignore;

@Ignore
public final class List$impl<Element> {
    private final List<Element> $this;

    public List$impl(List<Element> $this) {
        this.$this = $this;
    }
    
    public boolean getEmpty(){
        return List$impl.<Element>getEmpty($this);
    }
    private static <Element> boolean getEmpty(List<Element> $this){
        return false;
    }

    public long getSize(){
        return List$impl.<Element>getSize($this);
    }
    private static <Element> long getSize(List<Element> $this){
        Integer lastIndex = $this.getLastIndex();
        return lastIndex==null ? 0 : lastIndex.longValue()+1;
    }

    public boolean defines(Integer key){
        return List$impl.<Element>_defines($this, key);
    }
    private static <Element> boolean _defines(List<Element> $this, Integer key){
        Integer lastIndex = $this.getLastIndex();
        return lastIndex==null ? false : key.longValue() <= lastIndex.longValue();
    }

    public Iterator<? extends Element> getIterator(){
        return List$impl.<Element>_getIterator($this);
    }
    private static <Element> Iterator<? extends Element> _getIterator(final List<Element> $this){
        class ListIterator implements Iterator<Element> {
            private long index=0;
            public final java.lang.Object next() { 
                Integer lastIndex = $this.getLastIndex();
                if (lastIndex!=null && index <= lastIndex.longValue()) {
                    return $this.item(Integer.instance(index++));
                } 
                else {
                    return finished_.getFinished$();
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
    private static <Element> boolean _equals(final List<Element> $this, java.lang.Object that) {
        if (that instanceof List) {
            List other = (List) that;
            if (other.getSize()==$this.getSize()) {
                for (int i=0; i<$this.getSize(); i++) {
                    Element x = $this.item(Integer.instance(i));
                    java.lang.Object y = ((List) that).item(Integer.instance(i));
                    if (x!=y && (x==null || y==null || !x.equals(y))) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }
    
    public int hashCode() {
        return List$impl.<Element>_hashCode($this);
    }
    private static <Element> int _hashCode(final List<Element> $this) {
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
    
    public Element findLast(Callable<? extends Boolean> sel) {
        return List$impl.<Element>_findLast($this, sel);
    }
    private static <Element> Element _findLast(List<Element> $this, Callable<? extends Boolean> sel) {
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

    public Element getFirst(){
        return List$impl._getFirst($this);
    }

    private static <Element> Element _getFirst(List<Element> $this){
        return $this.item(Integer.instance(0));
    }

    public Element getLast(){
        return List$impl._getLast($this);
    }

    private static <Element> Element _getLast(List<Element> $this){
        Integer lastIndex = $this.getLastIndex();
        return lastIndex == null ? null : $this.item(lastIndex);
    }

    @SuppressWarnings("rawtypes")
    public <Other> Sequence withLeading(Other element) {
        return List$impl._withLeading($this, element);
    }
    @SuppressWarnings({"rawtypes", "unchecked"})
    private static <Element,Other> Sequence _withLeading(List<? extends Element> orig, Other elem) {
        SequenceBuilder sb = new SequenceBuilder();
        sb.append(elem);
        sb.appendAll(orig.getSequence());
        return (Sequence)sb.getSequence();
    }
    @SuppressWarnings("rawtypes")
    public <Other> Sequence withTrailing(Other element) {
        return List$impl._withTrailing($this, element);
    }
    @SuppressWarnings({"rawtypes", "unchecked"})
    private static <Element,Other> Sequence _withTrailing(List<? extends Element> orig, Other elem) {
        SequenceBuilder sb = new SequenceBuilder();
        sb.appendAll(orig.getSequence());
        sb.append(elem);
        return (Sequence)sb.getSequence();
    }

}
