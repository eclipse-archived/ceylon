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

    public Iterable<? extends Element> _getSequence() {
        return Iterable$impl._getSequence($this);
    }
    static <Element> Iterable<? extends Element> _getSequence(Iterable<Element> $this) {
        final SequenceBuilder<Element> sb = new SequenceBuilder();
        sb.appendAll($this);
        return sb.getSequence();
    }

    public <Result> Iterable<Result> map(Callable<Result> collecting) {
        return Iterable$impl._map($this, collecting);
    }
    static <Result> Iterable<Result> _map(final Iterable $this, final Callable<Result> collecting) {
        class MapIterator implements Iterator<Result> {
            final Iterator orig = $this.getIterator();
            java.lang.Object elem;
            public java.lang.Object next() {
                elem = orig.next();
                if (!(elem instanceof Finished)) {
                    return collecting.$call(elem);
                }
                return elem;
            }
        }
        //TODO return an Iterable with this iterator
        return null;
    }

    public Iterable<? extends Element> filter(Callable<Boolean> selecting) {
        return Iterable$impl._filter($this, selecting);
    }
    static <Element> Iterable<? extends Element> _filter(final Iterable<? extends Element> $this, final Callable<Boolean> selecting) {
        class FilterIterator implements Iterator<Element> {
            final Iterator<? extends Element> iter = $this.getIterator();
            public java.lang.Object next() {
                java.lang.Object elem = iter.next();
                boolean flag = elem instanceof Finished ? true : selecting.$call(elem).booleanValue();
                while (!flag) {
                    elem = iter.next();
                    flag = elem instanceof Finished ? true : selecting.$call(elem).booleanValue();
                }
                return elem;
            }
        }
        //TODO return an Iterable with this iterator
        return null;
    }

    public <Result> Result fold(Result initial, Callable<Result> accumulating) {
        return Iterable$impl._fold($this, initial, accumulating);
    }
    static <Result> Result _fold(Iterable $this, Result initial, Callable<Result> accum) {
        Iterator iter = $this.getIterator();
        java.lang.Object elem;
        while (!((elem = iter.next()) instanceof Finished)) {
            initial = accum.$call(initial, elem);
        }
        return initial;
    }

    public Element find(Callable<Boolean> selecting) {
        return Iterable$impl._find($this, selecting);
    }
    static <Element> Element _find(Iterable<? extends Element> $this, Callable<Boolean> sel) {
        Iterator<? extends Element> iter = $this.getIterator();
        java.lang.Object elem;
        while (!((elem = iter.next()) instanceof Finished)) {
            if (sel.$call(elem).booleanValue()) {
                return (Element)elem;
            }
        }
        return null;
    }

}
