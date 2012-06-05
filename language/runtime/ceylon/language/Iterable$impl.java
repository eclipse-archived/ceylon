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
        final SequenceBuilder<Element> sb = new SequenceBuilder<Element>();
        sb.appendAll($this);
        return sb.getSequence();
    }

    public <Result> Iterable<Result> map(Callable<Result> collecting) {
        return new MapIterable<Element, Result>($this, collecting);
    }

    public Iterable<? extends Element> filter(Callable<Boolean> selecting) {
        return new FilterIterable<Element>($this, selecting);
    }

    public <Result> Result fold(Result initial, Callable<Result> accumulating) {
        return Iterable$impl._fold($this, initial, accumulating);
    }
    static <Result> Result _fold(Iterable<?> $this, Result initial, Callable<Result> accum) {
        Iterator<?> iter = $this.getIterator();
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

class MapIterable<Element, Result> implements Iterable<Result> {
    final Iterable<Element> iterable;
    final Callable<Result> sel;
    MapIterable(Iterable<Element> iterable, Callable<Result> collecting) {
        this.iterable = iterable;
        sel = collecting;
    }

    class MapIterator implements Iterator<Result> {
        final Iterator<? extends Element> orig = iterable.getIterator();
        java.lang.Object elem;
        public java.lang.Object next() {
            elem = orig.next();
            if (!(elem instanceof Finished)) {
                return sel.$call(elem);
            }
            return elem;
        }
    }
    public Iterator<Result> getIterator() { return new MapIterator(); }
    public boolean getEmpty() { return getIterator().next() instanceof Finished; }

    @Override public Iterable<? extends Result> getSequence() { return Iterable$impl._getSequence(this); }
    @Override public Result find(Callable<Boolean> f) { return Iterable$impl._find(this, f); }
    @Override public <R2> Iterable<R2> map(Callable<R2> f) { return new MapIterable<Result, R2>(this, f); }
    @Override public Iterable<Result> filter(Callable<Boolean> f) { return new FilterIterable<Result>(this, f); }
    @Override public <R2> R2 fold(R2 ini, Callable<R2> f) { return Iterable$impl._fold(this, ini, f); }
}

class FilterIterable<Element> implements Iterable<Element> {
    final Iterable<Element> iterable;
    final Callable<Boolean> f;
    FilterIterable(Iterable<Element> iterable, Callable<Boolean> selecting) {
        this.iterable = iterable;
        f = selecting;
    }

    class FilterIterator implements Iterator<Element> {
        final Iterator<? extends Element> iter = iterable.getIterator();
        public java.lang.Object next() {
            java.lang.Object elem = iter.next();
            boolean flag = elem instanceof Finished ? true : f.$call(elem).booleanValue();
            while (!flag) {
                elem = iter.next();
                flag = elem instanceof Finished ? true : f.$call(elem).booleanValue();
            }
            return elem;
        }
    }
    public Iterator<Element> getIterator() { return new FilterIterator(); }
    public boolean getEmpty() { return getIterator().next() instanceof Finished; }
    @Override public Iterable<? extends Element> getSequence() { return Iterable$impl._getSequence(this); }
    @Override public Element find(Callable<Boolean> f) { return Iterable$impl._find(this, f); }
    @Override public <Result> Iterable<Result> map(Callable<Result> f) { return new MapIterable(this, f); }
    @Override public Iterable<? extends Element> filter(Callable<Boolean> f) { return new FilterIterable(this, f); }
    @Override public <Result> Result fold(Result ini, Callable<Result> f) { return Iterable$impl._fold(this, ini, f); }
}
