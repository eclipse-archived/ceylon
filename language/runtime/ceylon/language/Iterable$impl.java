package ceylon.language;

import java.util.Arrays;
import java.util.Comparator;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ignore
public final class Iterable$impl<Element> {
    private final Iterable<Element> $this;
    public Iterable$impl(Iterable<Element> $this) {
        this.$this = $this;
    }
    public boolean getEmpty(){
        return Iterable$impl._getEmpty($this);
    }
    static <Element> boolean _getEmpty(Iterable<Element> $this){
        return $this.getIterator().next() instanceof Finished;
    }

    public Element getFirst(){
        return Iterable$impl.<Element>_getFirst($this);
    }
    @SuppressWarnings("unchecked")
    static <Element> Element _getFirst(Iterable<Element> $this){
        java.lang.Object first = $this.getIterator().next();
        if (first instanceof Finished) {
            return null;
        }
        else {
            return (Element) first;
        }
    }
    public Element getLast() {
        return Iterable$impl.<Element>_getLast($this);
    }
    @SuppressWarnings("unchecked")
    static <Element> Element _getLast(Iterable<Element> $this) {
        java.lang.Object last = null;
        java.lang.Object next = null;
        for (Iterator<? extends Element> iter = $this.getIterator(); (next = iter.next()) != exhausted_.getExhausted();) {
            last = next;
        }
        return (Element)last;
    }

    public Iterable<? extends Element> getRest() {
        return Iterable$impl._getRest($this);
    }
    static <Element> Iterable<? extends Element> _getRest(final Iterable<Element> $this) {
        return $this.skipping(1);
    }
    public Iterable<? extends Element> getSequence() {
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

    public Iterable<? extends Element> filter(Callable<? extends Boolean> selecting) {
        return new FilterIterable<Element>($this, selecting);
    }

    public <Result> Result fold(Result initial, Callable<? extends Result> accumulating) {
        return Iterable$impl._fold($this, initial, accumulating);
    }
    static <Result> Result _fold(Iterable<?> $this, Result initial, Callable<? extends Result> accum) {
        java.lang.Object elem;
        for (Iterator<?> iter = $this.getIterator(); !((elem = iter.next()) instanceof Finished); ) {
            initial = accum.$call(initial, elem);
        }
        return initial;
    }

    public Element find(Callable<? extends Boolean> selecting) {
        return Iterable$impl._find($this, selecting);
    }
    @SuppressWarnings("unchecked")
    static <Element> Element _find(Iterable<? extends Element> $this, Callable<? extends Boolean> sel) {
        java.lang.Object elem;
        for (Iterator<? extends Element> iter = $this.getIterator(); !((elem = iter.next()) instanceof Finished);) {
            if (sel.$call(elem).booleanValue()) {
                return (Element)elem;
            }
        }
        return null;
    }

    public Element findLast(Callable<? extends Boolean> selecting) {
        return Iterable$impl._findLast($this, selecting);
    }
    @SuppressWarnings("unchecked")
    static <Element> Element _findLast(Iterable<? extends Element> $this, Callable<? extends Boolean> sel) {
        java.lang.Object elem;
        java.lang.Object last = null;
        for (Iterator<? extends Element> iter = $this.getIterator(); !((elem = iter.next()) instanceof Finished);) {
            if (sel.$call(elem).booleanValue()) {
                last = elem;
            }
        }
        return (Element)last;
    }

    public Iterable<? extends Element> sort(Callable<? extends Comparison> comparing) {
        return Iterable$impl._sort($this, comparing);
    }
    @SuppressWarnings("unchecked")
    static <Element> Iterable<? extends Element> _sort(Iterable<? extends Element> $this, final Callable<? extends Comparison> comp) {
        if ($this.getEmpty()) {
            return (Iterable<? extends Element>) empty_.getEmpty();
        }
        Element[] array = Util.toArray($this, (Class<Element>) java.lang.Object.class);
        Arrays.sort(array, new Comparator<Element>() {
            public int compare(Element x, Element y) {
                Comparison result = comp.$call(x, y);
                if (result.largerThan()) return 1;
                if (result.smallerThan()) return -1;
                return 0;
            }
        });
        return new ArraySequence<Element>(array,0);
    }

    public <Result> Iterable<? extends Result> collect(Callable<? extends Result> collecting) {
        return Iterable$impl._collect($this, collecting);
    }
    static <Element,Result> Iterable<? extends Result> _collect(Iterable<? extends Element> $this, Callable<? extends Result> f) {
        return new MapIterable<Element, Result>($this, f).getSequence();
    }

    public Iterable<? extends Element> select(Callable<? extends Boolean> selecting) {
        return Iterable$impl._select($this, selecting);
    }
    static <Element> Iterable<? extends Element> _select(Iterable<? extends Element> $this, Callable<? extends Boolean> f) {
        return new FilterIterable<Element>($this, f).getSequence();
    }

    public boolean any(Callable<? extends Boolean> selecting) {
        return Iterable$impl._any($this, selecting);
    }
    static <Element> boolean _any(Iterable<? extends Element> $this, Callable<? extends Boolean> sel) {
        Iterator<? extends Element> iter = $this.getIterator();
        java.lang.Object elem;
        while (!((elem = iter.next()) instanceof Finished)) {
            if (sel.$call(elem).booleanValue()) {
                return true;
            }
        }
        return false;
    }

    public boolean every(Callable<? extends Boolean> selecting) {
        return Iterable$impl._every($this, selecting);
    }
    static <Element> boolean _every(Iterable<? extends Element> $this, Callable<? extends Boolean> sel) {
        Iterator<? extends Element> iter = $this.getIterator();
        java.lang.Object elem;
        while (!((elem = iter.next()) instanceof Finished)) {
            if (!sel.$call(elem).booleanValue()) {
                return false;
            }
        }
        return true;
    }
    public Iterable<? extends Element> skipping(long skip) {
        return Iterable$impl._skipping($this, skip);
    }
    static <Element> Iterable<? extends Element> _skipping(final Iterable<? extends Element> $this, final long skip) {
        return skip==0 ? $this : new AbstractIterable<Element>() {
            public final Iterator<? extends Element> getIterator() {
                final Iterator<? extends Element> iter = $this.getIterator();
                for (int i = 0; i < skip; i++) { iter.next(); }
                return iter;
			}
        };
    }
    public Iterable<? extends Element> taking(long take) {
        return Iterable$impl._taking($this, take);
    }
    static <Element> Iterable<? extends Element> _taking(final Iterable<? extends Element> $this, final long take) {
        if (take == 0) {
            return (Iterable)empty_.getEmpty();
        }
        else return new AbstractIterable<Element>() {
            @Override
            public final Iterator<? extends Element> getIterator() {
                return new Iterator<Element>() {
                    private final Iterator<? extends Element> iter = $this.getIterator();
                    private int i=0;
                    @Override
                    public java.lang.Object next() {
                        while (i++ < take) {
                            return iter.next();
                        }
                        return exhausted_.getExhausted();
                    }
                };
            }
        };
    }
    public Iterable<? extends Element> by(long step) {
        return Iterable$impl._by($this, step);
    }
    static <Element> Iterable<? extends Element> _by(final Iterable<? extends Element> $this, final long step) {
        if (step == 1) {
            return $this;
        } else if (step <= 0) {
            throw new Exception(String.instance("step size must be greater than zero"));
        } else {
            return new AbstractIterable<Element>() {
                @Override
                public Iterator<? extends Element> getIterator() {
                    return new Iterator<Element>() {
                        private final Iterator<? extends Element> orig = $this.getIterator();
                        @Override
                        public java.lang.Object next() {
                            java.lang.Object e = orig.next();
                            for (int i = 1; i < step; i++) {
                                orig.next();
                            }
                            return e;
                        }
                    };
                }
            };
        }
    }

    public long count(Callable<? extends Boolean> f) {
        return Iterable$impl._count($this, f);
    }
    public static <Element> long _count(final Iterable<? extends Element> $this, Callable<? extends Boolean> f) {
        Iterator<? extends Element> iter = $this.getIterator();
        java.lang.Object elem;
        long c = 0;
        while (!((elem = iter.next()) instanceof Finished)) {
            if (f.$call(elem).booleanValue()) {
                c++;
            }
        }
        return c;
    }

    public Iterable<? extends Element> getCoalesced() {
        return Iterable$impl._getCoalesced($this);
    }
    public static <Element> Iterable<? extends Element> _getCoalesced(final Iterable<? extends Element> $this) {
        final class NotNullIterator implements Iterator<Element> {
            private final Iterator<? extends Element> orig = $this.getIterator();
            @Override public java.lang.Object next() {
                java.lang.Object tmp = null;
                while ((tmp = orig.next()) == null);
                return tmp;
            }
        }
        return new AbstractIterable<Element>() {
            @Override public Iterator<? extends Element> getIterator() { return new NotNullIterator(); }
        };
    }

    public Iterable<? extends Entry<? extends Integer, ? extends Element>> getIndexed() {
        return Iterable$impl._getIndexed($this);
    }
    public static <Element> Iterable<? extends Entry<? extends Integer, ? extends Element>> _getIndexed(final Iterable<? extends Element> $this) {
        final class EntryIterator implements Iterator<Entry<? extends Integer, ? extends Element>> {
            private long i=0;
            private final Iterator<? extends Element> orig = $this.getIterator();
            @Override public java.lang.Object next() {
                java.lang.Object tmp = null;
                while ((tmp = orig.next()) == null) { i++; }
                return tmp == exhausted_.getExhausted() ? tmp : new Entry<Integer, Element>(Integer.instance(i++), (Element)tmp);
            }

        }
        return new AbstractIterable<Entry<? extends Integer, ? extends Element>>() {
            @Override
            public Iterator<? extends Entry<? extends Integer, ? extends Element>> getIterator() {
                return new EntryIterator();
            }
        };
    }

    @SuppressWarnings("rawtypes")
    public <Other> Iterable chain(Iterable<? extends Other> other) {
        return Iterable$impl._chain($this, other);
    }
    @SuppressWarnings("rawtypes")
    public static <Element,Other> Iterable _chain(final Iterable<? extends Element> one, final Iterable<? extends Other> two) {
        return new AbstractIterable() {
            @Override @SuppressWarnings("unchecked")
            @TypeInfo("ceylon.language.Iterator<Element|Other>")
            public Iterator getIterator() {
                return new ChainedIterator(one, two);
            }
        };
    }
}

class MapIterable<Element, Result> implements Iterable<Result> {
    final Iterable<? extends Element> iterable;
    final Callable<? extends Result> sel;
    MapIterable(Iterable<? extends Element> iterable, Callable<? extends Result> collecting) {
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
    public Iterator<? extends Result> getIterator() { return new MapIterator(); }
    public boolean getEmpty() { return getIterator().next() instanceof Finished; }

    @Override
    @Ignore
    public Result getFirst() {
    	return Iterable$impl._getFirst(this);
    }
    @Override @Ignore public Result getLast(){
        return Iterable$impl._getLast(this);
    }

    @Override
    @Ignore
    public Iterable<? extends Result> getRest() {
    	return Iterable$impl._getRest(this);
    }

    @Override
    @Ignore
    public Iterable<? extends Result> getSequence() {
        return Iterable$impl._getSequence(this);
    }
    @Override
    @Ignore
    public Result find(Callable<? extends Boolean> f) {
        return Iterable$impl._find(this, f);
    }
    @Override @Ignore
    public Result findLast(Callable<? extends Boolean> f) {
        return Iterable$impl._findLast(this, f);
    }
    @Override @Ignore
    public Iterable<? extends Result> sort(Callable<? extends Comparison> f) {
        return Iterable$impl._sort(this, f);
    }
    @Override @Ignore
    public <R2> Iterable<? extends R2> collect(Callable<? extends R2> f) {
        return Iterable$impl._collect(this, f);
    }
    @Override @Ignore
    public Iterable<? extends Result> select(Callable<? extends Boolean> f) {
        return Iterable$impl._select(this, f);
    }
    @Override
    @Ignore
    public <R2> Iterable<R2> map(Callable<? extends R2> f) {
        return new MapIterable<Result, R2>(this, f);
    }
    @Override
    @Ignore
    public Iterable<? extends Result> filter(Callable<? extends Boolean> f) {
        return new FilterIterable<Result>(this, f);
    }
    @Override
    @Ignore
    public <R2> R2 fold(R2 ini, Callable<? extends R2> f) {
        return Iterable$impl._fold(this, ini, f);
    }
    @Override @Ignore
    public boolean any(Callable<? extends Boolean> f) {
        return Iterable$impl._any(this, f);
    }
    @Override @Ignore
    public boolean every(Callable<? extends Boolean> f) {
        return Iterable$impl._every(this, f);
    }
    @Override @Ignore
    public Iterable<? extends Result> skipping(long skip) {
        return Iterable$impl._skipping(this, skip);
    }
    @Override @Ignore
    public Iterable<? extends Result> taking(long take) {
        return Iterable$impl._taking(this, take);
    }
    @Override @Ignore
    public Iterable<? extends Result> by(long step) {
        return Iterable$impl._by(this, step);
    }
    @Override @Ignore
    public long count(Callable<? extends Boolean> f) {
        return Iterable$impl._count(this, f);
    }
    @Override @Ignore
    public Iterable<? extends Result> getCoalesced() {
        return Iterable$impl._getCoalesced(this);
    }
    @Override @Ignore
    public Iterable<? extends Entry<? extends Integer, ? extends Result>> getIndexed() {
        return Iterable$impl._getIndexed(this);
    }
    @SuppressWarnings("rawtypes")
    public <Other> Iterable chain(Iterable<? extends Other> other) {
        return Iterable$impl._chain(this, other);
    }
}

class FilterIterable<Element> implements Iterable<Element> {
    final Iterable<? extends Element> iterable;
    final Callable<? extends Boolean> f;
    FilterIterable(Iterable<? extends Element> iterable, Callable<? extends Boolean> selecting) {
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
    public Iterator<? extends Element> getIterator() { return new FilterIterator(); }
    public boolean getEmpty() { return getIterator().next() instanceof Finished; }
    @Override
    @Ignore
    public Element getFirst() {
    	return Iterable$impl._getFirst(this);
    }
    @Override @Ignore public Element getLast(){
        return Iterable$impl._getLast(this);
    }
    @Override
    @Ignore
    public Iterable<? extends Element> getRest() {
    	return Iterable$impl._getRest(this);
    }
    @Override
    @Ignore
    public Iterable<? extends Element> getSequence() {
        return Iterable$impl._getSequence(this);
    }
    @Override
    @Ignore
    public Element find(Callable<? extends Boolean> f) {
        return Iterable$impl._find(this, f);
    }
    @Override @Ignore
    public Element findLast(Callable<? extends Boolean> f) {
        return Iterable$impl._findLast(this, f);
    }
    @Override @Ignore
    public Iterable<? extends Element> sort(Callable<? extends Comparison> f) {
        return Iterable$impl._sort(this, f);
    }
    @Override @Ignore
    public <Result> Iterable<? extends Result> collect(Callable<? extends Result> f) {
        return Iterable$impl._collect(this,  f);
    }
    @Override @Ignore
    public Iterable<? extends Element> select(Callable<? extends Boolean> f) {
        return Iterable$impl._select(this,  f);
    }
    @Override
    @Ignore
    public <Result> Iterable<Result> map(Callable<? extends Result> f) {
        return new MapIterable<Element, Result>(this, f);
    }
    @Override
    @Ignore
    public Iterable<? extends Element> filter(Callable<? extends Boolean> f) {
        return new FilterIterable<Element>(this, f);
    }
    @Override
    @Ignore
    public <Result> Result fold(Result ini, Callable<? extends Result> f) {
        return Iterable$impl._fold(this, ini, f);
    }
    @Override @Ignore
    public boolean any(Callable<? extends Boolean> f) {
        return Iterable$impl._any(this, f);
    }
    @Override @Ignore
    public boolean every(Callable<? extends Boolean> f) {
        return Iterable$impl._every(this, f);
    }
    @Override @Ignore
    public Iterable<? extends Element> skipping(long skip) {
        return Iterable$impl._skipping(this, skip);
    }
    @Override @Ignore
    public Iterable<? extends Element> taking(long take) {
        return Iterable$impl._taking(this, take);
    }
    @Override @Ignore
    public Iterable<? extends Element> by(long step) {
        return Iterable$impl._by(this, step);
    }
    @Override @Ignore
    public long count(Callable<? extends Boolean> f) {
        return Iterable$impl._count(this, f);
    }
    @Override @Ignore
    public Iterable<? extends Element> getCoalesced() {
        return Iterable$impl._getCoalesced(this);
    }
    @Override @Ignore
    public Iterable<? extends Entry<? extends Integer, ? extends Element>> getIndexed() {
        return Iterable$impl._getIndexed(this);
    }
    @Override @Ignore
    @SuppressWarnings("rawtypes")
    public <Other> Iterable chain(Iterable<? extends Other> other) {
        return Iterable$impl._chain(this, other);
    }
}
