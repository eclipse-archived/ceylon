package ceylon.language;

import com.redhat.ceylon.compiler.java.language.AbstractCallable;
import com.redhat.ceylon.compiler.java.metadata.Annotation;
import com.redhat.ceylon.compiler.java.metadata.Annotations;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;

@Ceylon(major = 3)
@TypeParameters(@TypeParameter(value = "Element", variance = Variance.OUT))
@SatisfiedTypes("ceylon.language::List<Element>")
public class LazyList<Element> implements List<Element> {

    private final Iterable<? extends Element> elems;
    private final List$impl<Element> list$impl = new List$impl<Element>(this);
    private final Category$impl cat$impl = new Category$impl(this);
    private final Correspondence$impl<Integer,Element> corr$impl = new Correspondence$impl<Integer,Element>(this);

    @Ignore @SuppressWarnings("unchecked")
    public LazyList() {
        this.elems = (Iterable<? extends Element>)empty_.getEmpty$();
    }
    public LazyList(@Name("elems")
            @TypeInfo("ceylon.language::Iterable<Element>")
            Iterable<? extends Element> elems) {
        this.elems = elems;
    }

    @Ignore
    @Override
    public Correspondence$impl<? super Integer,? extends Element> $ceylon$language$Correspondence$impl(){
        return corr$impl;
    }

    @Override
    @Ignore
    public Correspondence$impl<? super Integer, ? extends Element>.Items Items$new(Sequence<? extends Integer> keys) {
        return corr$impl.Items$new(keys);
    }

    @Override @Ignore
    public boolean getEmpty() {
        return elems.getEmpty();
    }

    @Override @Ignore
    public boolean contains(final java.lang.Object element) {
        return elems.find(new AbstractCallable<Boolean>("List_contains"){
            @Override
            public Boolean $call(java.lang.Object e) {
                return Boolean.instance(e.equals(element));
            }
        }) != null;
    }

    @Override
    @Annotations({@Annotation("actual"), @Annotation("default")})
    @TypeInfo("ceylon.language::Nothing|Element")
    public Element getFirst() {
        return elems.getFirst();
    }

    @Override
    @Annotations({@Annotation("actual"), @Annotation("default")})
    @TypeInfo("ceylon.language::Nothing|Element")
    public Element getLast() {
        return elems.getLast();
    }

    @Override @Ignore
    public Iterable<? extends Element> getRest() {
        return elems.getRest();
    }

    @Override @Ignore
    public List<? extends Element> getSequence() {
        return elems.getSequence();
    }

    @Override @Ignore
    public <Result> Iterable<? extends Result> map(
            Callable<? extends Result> collecting) {
        return elems.map(collecting);
    }

    @Override @Ignore
    public Iterable<? extends Element> filter(
            Callable<? extends Boolean> selecting) {
        return elems.filter(selecting);
    }

    @Override @Ignore
    public <Result> Result fold(Result initial,
            Callable<? extends Result> accumulating) {
        return elems.fold(initial, accumulating);
    }

    @Override @Ignore
    public Element find(Callable<? extends Boolean> selecting) {
        return elems.find(selecting);
    }

    @Override
    @Annotations({@Annotation("actual"), @Annotation("default")})
    @TypeInfo("ceylon.language::Nothing|Element")
    public Element findLast(
            @TypeInfo("ceylon.language::Callable<ceylon.language::Boolean,ceylon.language::Tuple<Element,Element,ceylon.language::Empty>>")
            Callable<? extends Boolean> selecting) {
        return elems.findLast(selecting);
    }

    @Override @Ignore
    public List<? extends Element> sort(
            Callable<? extends Comparison> comparing) {
        return elems.sort(comparing);
    }

    @Override @Ignore
    public <Result> List<? extends Result> collect(
            Callable<? extends Result> collecting) {
        return elems.collect(collecting);
    }

    @Override @Ignore
    public List<? extends Element> select(
            Callable<? extends Boolean> selecting) {
        return elems.select(selecting);
    }

    @Override @Ignore
    public boolean any(Callable<? extends Boolean> selecting) {
        return elems.any(selecting);
    }

    @Override @Ignore
    public boolean every(Callable<? extends Boolean> selecting) {
        return elems.every(selecting);
    }

    @Override @Ignore
    public Iterable<? extends Element> skipping(long skip) {
        return elems.skipping(skip);
    }

    @Override @Ignore
    public Iterable<? extends Element> taking(long take) {
        return elems.taking(take);
    }

    @Override @Ignore
    public Iterable<? extends Element> by(long step) {
        return elems.by(step);
    }

    @Override @Ignore
    public long count(Callable<? extends Boolean> selecting) {
        return elems.count(selecting);
    }

    @Override @Ignore
    public Iterable<? extends Element> getCoalesced() {
        return elems.getCoalesced();
    }

    @Override @Ignore
    public Iterable<? extends Entry<? extends Integer, ? extends Element>> getIndexed() {
        return elems.getIndexed();
    }

    @SuppressWarnings("rawtypes")
    @Override @Ignore
    public <Other> Iterable chain(Iterable<? extends Other> other) {
        return elems.chain(other);
    }

    @Override @Ignore
    public <Key> Map<? extends Key, ? extends Sequence<? extends Element>> group(
            Callable<? extends Key> grouping) {
        return elems.group(grouping);
    }

    @Override @Ignore
    public boolean containsEvery(
            @Sequenced @Name("elements") @TypeInfo("ceylon.language::Iterable<ceylon.language::Object>") Iterable<?> elements) {
        return cat$impl.containsEvery(elements);
    }

    @Override
    @Ignore
    public boolean containsEvery() {
        return cat$impl.containsEvery();
    }

    @Override
    @Ignore
    public Iterable<?> containsEvery$elements() {
        return cat$impl.containsEvery$elements();
    }

    @Override @Ignore
    public boolean containsAny(
            @Sequenced @Name("elements") @TypeInfo("ceylon.language::Iterable<ceylon.language::Object>") Iterable<?> elements) {
        return cat$impl.containsAny(elements);
    }

    @Override
    @Ignore
    public boolean containsAny() {
        return cat$impl.containsAny();
    }

    @Override
    @Ignore
    public Iterable<?> containsAny$elements() {
        return cat$impl.containsAny$elements();
    }

    @Override
    @Annotations({@Annotation("actual"), @Annotation("default")})
    @TypeInfo("ceylon.language::List<Element>")
    public Collection<? extends Element> getClone() {
        return new LazyList<Element>(elems);
    }

    @Override @Ignore
    public Category getKeys() {
        return corr$impl.getKeys();
    }

    @Override @Ignore
    public boolean definesEvery(Iterable<? extends Integer> keys) {
        return corr$impl.definesEvery(keys);
    }

    @Override
    @Ignore
    public boolean definesEvery() {
        return corr$impl.definesEvery();
    }

    @Override
    @Ignore
    public Iterable<? extends Integer> definesEvery$keys() {
        return corr$impl.definesEvery$keys();
    }

    @Override @Ignore
    public boolean definesAny(Iterable<? extends Integer> keys) {
        return corr$impl.definesAny(keys);
    }

    @Override
    @Ignore
    public boolean definesAny() {
        return corr$impl.definesAny();
    }

    @Override
    @Ignore
    public Iterable<? extends Integer> definesAny$keys() {
        return corr$impl.definesAny$keys();
    }

    @Override @Ignore
    public List<? extends Element> items(Iterable<? extends Integer> keys) {
        return corr$impl.items(keys);
    }

    @Override
    @Ignore
    public Iterable<? extends Element> items() {
        return corr$impl.items();
    }

    @Override
    @Ignore
    public Iterable<? extends Integer> items$keys() {
        return corr$impl.items$keys();
    }

    @Override
    @Annotations({@Annotation("actual"), @Annotation("default")})
    @TypeInfo("ceylon.language::List<Element>")
    public List<? extends Element> span(Integer from, Integer to) {
        long p0 = from.value;
        if (to == null) {
            Iterable<? extends Element> els = p0>0 ? elems.skipping(p0) : elems;
            return new LazyList<Element>(els);
        } else {
            long p1 = to.value;
            if (p1 >= p0) {
                Iterable<? extends Element> els = p0>0 ? elems.skipping(p0) : elems;
                return new LazyList<Element>(els.taking(p1-p0+1));
            } else {
                Iterable<? extends Element> els = p1>0 ? elems.skipping(p1) : elems;
                return new LazyList<Element>(els.taking(p0-p1+1).getSequence().getReversed());
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    @Annotations({@Annotation("actual"), @Annotation("default")})
    @TypeInfo("ceylon.language::List<Element>")
    public List<? extends Element> segment(Integer from, long length) {
        if (length > 0) {
            long p = from.value;
            Iterable<? extends Element> els = p>0 ? elems.skipping(p) : elems;
            return new LazyList<Element>(els.taking(length));
        } else {
            return (List<? extends Element>)empty_.getEmpty$();
        }
    }

    @Override
    @Annotations({@Annotation("actual"), @Annotation("default")})
    @TypeInfo("ceylon.language::Nothing|ceylon.language::Integer")
    public Integer getLastIndex() {
        long c = elems.count(new AbstractCallable<Boolean>("LazyList_lastIndex") {
            @Override
            public Boolean $call(java.lang.Object e) {
                return true_.getTrue$();
            }
        });
        return c>0 ? Integer.instance(c-1) : null;
    }

    @Override @Ignore
    public long getSize() {
        return elems.count(new AbstractCallable<Boolean>("LazyList_size") {
            @Override
            public Boolean $call(java.lang.Object e) {
                return true_.getTrue$();
            }
        });
    }

    @Override @Ignore
    public boolean defines(Integer key) {
        return corr$impl.defines(key);
    }

    @Override
    @Annotations({@Annotation("actual"), @Annotation("default")})
    @TypeInfo("ceylon.language::Nothing|Element")
    public Element item(Integer key) {
        long l = key.value;
        return l==0 ? elems.getFirst() : elems.skipping(l).getFirst();
    }

    @Override
    @Annotations({@Annotation("actual"), @Annotation("default")})
    @TypeInfo("ceylon.language::Iterator<Element>")
    public Iterator<? extends Element> getIterator() {
        return elems.getIterator();
    }

    @Override
    @Annotations({@Annotation("actual"), @Annotation("default")})
    @TypeInfo("ceylon.language::List<Element>")
    public List<? extends Element> getReversed() {
        return ((List<? extends Element>)elems.getSequence()).getReversed();
    }

    @SuppressWarnings("rawtypes")
    @Override @Ignore
    public <Other> Sequence withLeading(Other element) {
        return list$impl.withLeading(element);
    }

    @SuppressWarnings("rawtypes")
    @Override @Ignore
    public <Other> Sequence withTrailing(Other element) {
        return list$impl.withTrailing(element);
    }

    @Override
    @Annotations({@Annotation("actual"), @Annotation("default")})
    @TypeInfo("ceylon.language::Boolean")
    public boolean equals(java.lang.Object obj) {
        if (obj instanceof List) {
            @SuppressWarnings("rawtypes")
            List other = (List)obj;
            long c = elems.count(new AbstractCallable<Boolean>("LazyList_lastIndex") {
                @Override
                public Boolean $call(java.lang.Object e) {
                    return true_.getTrue$();
                }
            });
            if (other.getSize()==c) {
                Iterator<? extends Element> iter = elems.getIterator();
                for (int i = 0; i<c;i++) {
                    java.lang.Object x = other.item(Integer.instance(i));
                    java.lang.Object y = iter.next();
                    if (x!=y && (x==null || y==null || !x.equals(y))) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    @Annotations({@Annotation("actual"), @Annotation("default")})
    @TypeInfo("ceylon.language::Integer")
    public int hashCode() {
        return list$impl.hashCode();
    }

}
