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
    
    @SuppressWarnings("rawtypes")
    public <Other>Sequence withLeading() { return $this; }
    @SuppressWarnings("rawtypes")
    public <Other>Sequence withTrailing() { return $this; }
    
    public <Other>Iterable<? extends Other> withLeading$elements() { return (Iterable)$empty.getEmpty(); }
    public <Other>Iterable<? extends Other> withTrailing$elements() { return (Iterable)$empty.getEmpty(); }

    @SuppressWarnings("rawtypes")
    public <Other> List withLeading(Iterable<? extends Other> elements) {
        return List$impl._withLeading($this, elements);
    }
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <Element,Other> Sequence _withLeading(Sequence<? extends Element> orig, Iterable<? extends Other> elems) {
        SequenceBuilder sb = new SequenceBuilder();
        sb.appendAll(elems);
        sb.appendAll(orig);
        return (Sequence)sb.getSequence();
    }
    @SuppressWarnings("rawtypes")
    public <Other> List withTrailing(Iterable<? extends Other> elements) {
        return List$impl._withTrailing($this, elements);
    }
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <Element,Other> Sequence _withTrailing(Sequence<? extends Element> orig, Iterable<? extends Other> elems) {
        SequenceBuilder sb = new SequenceBuilder();
        sb.appendAll(orig);
        sb.appendAll(elems);
        return (Sequence)sb.getSequence();
    }

}    