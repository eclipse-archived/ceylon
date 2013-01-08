package ceylon.language;

import java.util.Arrays;
import java.util.Comparator;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.language.AbstractIterable;
import com.redhat.ceylon.compiler.java.language.ArraySequence;
import com.redhat.ceylon.compiler.java.language.FilterIterable;
import com.redhat.ceylon.compiler.java.language.InternalMap;
import com.redhat.ceylon.compiler.java.language.MapIterable;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ignore
public final class Iterable$impl<Element> {
    private final Iterable<Element> $this;
    public Iterable$impl(Iterable<Element> $this) {
        this.$this = $this;
    }
    public long getSize() {
        long count = 0;
        for (Iterator<? extends Element> iter = $this.getIterator(); iter.next() != finished_.getFinished$();) {
            count++;
        }
        return count;
    }
    public boolean getEmpty(){
        return Iterable$impl._getEmpty($this);
    }
    private static <Element> boolean _getEmpty(Iterable<Element> $this){
        return $this.getIterator().next() instanceof Finished;
    }

    public Element getFirst(){
        return Iterable$impl.<Element>_getFirst($this);
    }
    @SuppressWarnings("unchecked")
    private static <Element> Element _getFirst(Iterable<Element> $this){
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
    private static <Element> Element _getLast(Iterable<Element> $this) {
        java.lang.Object last = null;
        java.lang.Object next = null;
        for (Iterator<? extends Element> iter = $this.getIterator(); (next = iter.next()) != finished_.getFinished$();) {
            last = next;
        }
        return (Element)last;
    }

    public Iterable<? extends Element> getRest() {
        return Iterable$impl._getRest($this);
    }
    private static <Element> Iterable<? extends Element> _getRest(final Iterable<Element> $this) {
        return $this.skipping(1);
    }
    public Sequential<? extends Element> getSequence() {
        return Iterable$impl._getSequence($this);
    }
    private static <Element> Sequential<? extends Element> _getSequence(Iterable<Element> $this) {
        final SequenceBuilder<Element> sb = new SequenceBuilder<Element>();
        java.lang.Object next = null;
        for (Iterator<? extends Element> iter = $this.getIterator(); (next = iter.next()) != finished_.getFinished$();) {
            sb.append((Element) next);
        }
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
    private static <Result> Result _fold(Iterable<?> $this, Result initial, Callable<? extends Result> accum) {
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
    private static <Element> Element _find(Iterable<? extends Element> $this, Callable<? extends Boolean> sel) {
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
    private static <Element> Element _findLast(Iterable<? extends Element> $this, Callable<? extends Boolean> sel) {
        java.lang.Object elem;
        java.lang.Object last = null;
        for (Iterator<? extends Element> iter = $this.getIterator(); !((elem = iter.next()) instanceof Finished);) {
            if (sel.$call(elem).booleanValue()) {
                last = elem;
            }
        }
        return (Element)last;
    }

    public Sequential<? extends Element> sort(Callable<? extends Comparison> comparing) {
        return Iterable$impl._sort($this, comparing);
    }
    @SuppressWarnings("unchecked")
    private static <Element> Sequential<? extends Element> _sort(Iterable<? extends Element> $this, final Callable<? extends Comparison> comp) {
        if ($this.getEmpty()) {
            return (Sequential<? extends Element>) empty_.getEmpty$();
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

    public <Result> Sequential<? extends Result> collect(Callable<? extends Result> collecting) {
        return Iterable$impl._collect($this, collecting);
    }
    private static <Element,Result> Sequential<? extends Result> _collect(Iterable<? extends Element> $this, Callable<? extends Result> f) {
        return new MapIterable<Element, Result>($this, f).getSequence();
    }

    public Sequential<? extends Element> select(Callable<? extends Boolean> selecting) {
        return Iterable$impl._select($this, selecting);
    }
    private static <Element> Sequential<? extends Element> _select(Iterable<? extends Element> $this, Callable<? extends Boolean> f) {
        return new FilterIterable<Element>($this, f).getSequence();
    }

    public boolean any(Callable<? extends Boolean> selecting) {
        return Iterable$impl._any($this, selecting);
    }
    private static <Element> boolean _any(Iterable<? extends Element> $this, Callable<? extends Boolean> sel) {
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
    private static <Element> boolean _every(Iterable<? extends Element> $this, Callable<? extends Boolean> sel) {
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
    private static <Element> Iterable<? extends Element> _skipping(final Iterable<? extends Element> $this, final long skip) {
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
    private static <Element> Iterable<? extends Element> _taking(final Iterable<? extends Element> $this, final long take) {
        if (take == 0) {
            return (Iterable)empty_.getEmpty$();
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
                        return finished_.getFinished$();
                    }
                };
            }
        };
    }
    public Iterable<? extends Element> by(long step) {
        return Iterable$impl._by($this, step);
    }
    private static <Element> Iterable<? extends Element> _by(final Iterable<? extends Element> $this, final long step) {
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
    private static <Element> long _count(final Iterable<? extends Element> $this, Callable<? extends Boolean> f) {
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
    private static <Element> Iterable<? extends Element> _getCoalesced(final Iterable<? extends Element> $this) {
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
    private static <Element> Iterable<? extends Entry<? extends Integer, ? extends Element>> _getIndexed(final Iterable<? extends Element> $this) {
        final class EntryIterator implements Iterator<Entry<? extends Integer, ? extends Element>> {
            private long i=0;
            private final Iterator<? extends Element> orig = $this.getIterator();
            @Override public java.lang.Object next() {
                java.lang.Object tmp = null;
                while ((tmp = orig.next()) == null) { i++; }
                return tmp == finished_.getFinished$() ? tmp : new Entry<Integer, Element>(Integer.instance(i++), (Element)tmp);
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
    private static <Element,Other> Iterable _chain(final Iterable<? extends Element> one, final Iterable<? extends Other> two) {
        return new AbstractIterable() {
            @Override @SuppressWarnings("unchecked")
            @TypeInfo("ceylon.language::Iterator<Element|Other>")
            public Iterator getIterator() {
                return new ChainedIterator(one, two);
            }
        };
    }

    public <Key> Map<? extends Key, ? extends Sequence<? extends Element>> group(Callable<? extends Key> grouping) {
        return Iterable$impl._group($this, grouping);
    }
    private static <Element,Key> Map<? extends Key, ? extends Sequence<? extends Element>> _group(Iterable<? extends Element> $this, Callable<? extends Key> grouping) {
        java.util.HashMap<Key, SequenceBuilder<Element>> m = new java.util.HashMap<Key, SequenceBuilder<Element>>();
        java.lang.Object $tmp;
        for (Iterator<? extends Element> i = $this.getIterator(); !(($tmp = i.next()) instanceof Finished);) {
            Key k = grouping.$call($tmp);
            if (!m.containsKey(k)) {
                m.put(k, new SequenceBuilder<Element>());
            }
            m.get(k).append((Element)$tmp);
        }
        java.util.HashMap<Key, Sequence<? extends Element>> m2 = new java.util.HashMap<Key, Sequence<? extends Element>>(m.size());
        for (java.util.Map.Entry<Key, SequenceBuilder<Element>> e : m.entrySet()) {
            e.getValue().getSequence();
            m2.put(e.getKey(), (Sequence<? extends Element>)e.getValue().getSequence());
        }
        return new InternalMap<Key, Sequence<? extends Element>>(m2);
    }

    public boolean contains(java.lang.Object element) {
        Iterator<? extends Element> iter = $this.getIterator();
        java.lang.Object elem;
        while (!((elem = iter.next()) instanceof Finished)) {
            if (elem.equals(element)) {
                return true;
            }
        }
        return false;
    }
}
