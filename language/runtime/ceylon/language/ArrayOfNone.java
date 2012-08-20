package ceylon.language;

import com.redhat.ceylon.compiler.java.language.InternalMap;
import com.redhat.ceylon.compiler.java.metadata.Annotation;
import com.redhat.ceylon.compiler.java.metadata.Annotations;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;

@Ignore
@Ceylon(major = 2)
class ArrayOfNone<Element> extends Array<Element> implements None<Element> {

    @Ignore
    protected ArrayOfNone(java.lang.Object array) {
        super(array);
    }
    
    @Ignore
    public static <T> Array<T> instance(java.lang.Class<T> typeClass) {
        return instance(typeClass, null);
    }
    
    @Override
    public long getSize() {
        return 0;
    }
    
    @Override
    public boolean getEmpty() {
        return true;
    }
    
    @Override
    public Element getFirst() {
        return null;
    }
    @Override public Element getLast() { return null; }
    
    @Override
    public Iterable<? extends Element> getRest() {
        return this;
    }

    @Override
    @Annotations({ @Annotation("actual") })
    public Array<? extends Element> getReversed() {
    	return this;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override @Ignore public Iterable<? extends Element> getSequence() { return (Iterable)empty_.getEmpty(); }
    @Override @Ignore public Element find(Callable<? extends Boolean> f) { return null; }
    @Override @Ignore public Element findLast(Callable<? extends Boolean> f) { return null; }
    @Override @Ignore public Iterable<? extends Element> sort(Callable<? extends Comparison> f) { return this; }
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override @Ignore public <Result> Iterable<Result> map(Callable<? extends Result> f) { return (Iterable)empty_.getEmpty(); }
    @Override @Ignore public Iterable<? extends Element> filter(Callable<? extends Boolean> f) { return this; }
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override @Ignore public <Result> Iterable<Result> collect(Callable<? extends Result> f) { return (Iterable)empty_.getEmpty(); }
    @Override @Ignore public Iterable<? extends Element> select(Callable<? extends Boolean> f) { return this; }
    @Override @Ignore public <Result> Result fold(Result ini, Callable<? extends Result> f) { return ini; }
    @Override @Ignore public boolean any(Callable<? extends Boolean> f) { return false; }
    @Override @Ignore public boolean every(Callable<? extends Boolean> f) { return false; }
    @Override @Ignore public Iterable<? extends Element> skipping(long step) { return this; }
    @Override @Ignore public Iterable<? extends Element> taking(long t) { return this; }
    @Override @Ignore public Iterable<? extends Element> by(long s) { return this; }
    @Override @Ignore public long count(Callable<? extends Boolean> f) { return 0; }
    @Override @Ignore public Iterable<? extends Element> getCoalesced() { return this; }
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override @Ignore public Iterable<? extends Entry<? extends Integer, ? extends Element>> getIndexed() { return (Iterable)this; }
    @SuppressWarnings("rawtypes")
    @Override @Ignore public <Other>Iterable chain(Iterable<? extends Other> other) { return other; }
    @Override @Ignore
    public <Key> Map<? extends Key, ? extends Sequence<? extends Element>> group(Callable<? extends Key> grouping) {
        return new InternalMap<Key, Sequence<? extends Element>>(java.util.Collections.<Key,Sequence<Element>>emptyMap());
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override @Ignore public <Other>Sequence withLeading(Other e) {
        return new ArraySequence<Other>(e);
    }
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override @Ignore public <Other>Sequence withTrailing(Other e) {
        return new ArraySequence<Other>(e);
    }

}
