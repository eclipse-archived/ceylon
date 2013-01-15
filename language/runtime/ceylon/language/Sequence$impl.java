package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ignore;

@Ignore
public final class Sequence$impl<Element> {
    private final ceylon.language.Iterable$impl<Element, ? extends java.lang.Object> $ceylon$language$Iterable$this;
    private final ceylon.language.List$impl<Element> $ceylon$language$List$this;
    private final ceylon.language.Collection$impl<Element> $ceylon$language$Collection$this;
    
    private final Sequence<Element> $this;
    
    public Sequence$impl(Sequence<Element> $this) {
        this.$ceylon$language$Iterable$this = new ceylon.language.Iterable$impl<Element,java.lang.Object>($this);
        this.$ceylon$language$List$this = new ceylon.language.List$impl<Element>($this);
        this.$ceylon$language$Collection$this = new ceylon.language.Collection$impl<Element>($this);
        this.$this = $this;
    }

    public long getSize(){
        return $ceylon$language$List$this.getSize();
    }
    
    public Iterator<? extends Element> getIterator(){
        return $ceylon$language$List$this.getIterator();
    }
    
    public boolean contains(java.lang.Object element){
        return $ceylon$language$Collection$this.contains(element);
    }
    
    public Element findLast(Callable<? extends Boolean> selecting){
        return $ceylon$language$List$this.findLast(selecting);
    }

    public Element getLast(){
        return _getLast($this);
    }
    private static <Element> Element _getLast(Sequence<Element> $this){
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
    private static <Element> Sequence<? extends Element> _getSequence(Sequence<Element> $this) {
        return $this;
    }
    
    public boolean getEmpty() {
        return false;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Sequence<? extends Element> sort(Callable<? extends Comparison> f) {
        return (Sequence)$ceylon$language$Iterable$this.sort(f).getSequence();
    }
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static <Element> Sequence<? extends Element> _sort(Sequence<? extends Element> $this, Callable<? extends Comparison> f) {
        return (new Sequence$impl($this)).sort(f).getSequence();
    }
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public <Result> Sequence<? extends Result> collect(Callable<? extends Result> f) {
        return (Sequence)$ceylon$language$Iterable$this.collect(f);
    }
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static <Element,Result> Sequence<? extends Result> _collect(Sequence<? extends Element> $this, Callable<? extends Result> f) {
        return (new Sequence$impl($this)).collect(f);
    }

}    