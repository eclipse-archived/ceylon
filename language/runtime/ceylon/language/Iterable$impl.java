package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ignore;

@Ignore
public final class Iterable$impl<Element> {
    private final Iterable<Element> $this;
    public Iterable$impl(Iterable<Element> $this) {
        this.$this = $this;
    }
    public boolean _getEmpty(){
        return _getEmpty($this);
    }
    static <Element> boolean _getEmpty(Iterable<Element> $this){
        return $this.getIterator().next() instanceof Finished;
    }

    public Iterable<? extends Element> getSequence() {
        return Iterable$impl._getSequence($this);
    }
    static <Element> Iterable<? extends Element> _getSequence(Iterable<Element> $this) {
        final SequenceBuilder<Element> sb = new SequenceBuilder();
        sb.appendAll($this);
        return sb.getSequence();
    }

    public <Result> Iterable<Result> map(Callable<Result> collecting) {
        return null;
    }

    public Iterable<? extends Element> filter(Callable<Boolean> selecting) {
        return null;
    }

    public <Result> Result fold(Callable<Result> accumulating) {
        return null;
    }

    public Element find(Callable<Boolean> selecting) {
        return null;
    }

}
