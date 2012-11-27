package ceylon.language;

import static java.lang.Long.MAX_VALUE;

import com.redhat.ceylon.compiler.java.language.AbstractIterable;
import com.redhat.ceylon.compiler.java.language.FilterIterable;
import com.redhat.ceylon.compiler.java.language.MapIterable;
import com.redhat.ceylon.compiler.java.metadata.Annotation;
import com.redhat.ceylon.compiler.java.metadata.Annotations;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;

@Ceylon(major = 3)
@Class(extendsType="ceylon.language::Object")
@SatisfiedTypes({"ceylon.language::Sequence<Element>",
	             "ceylon.language::Category"})
@TypeParameters(@TypeParameter(value="Element",
    satisfies={"ceylon.language::Comparable<Element>",
		       "ceylon.language::Ordinal<Element>"}))
public class Range<Element extends Comparable<? super Element> & Ordinal<? super Element>>
    implements Sequence<Element>, Category {
    private final ceylon.language.Category$impl $ceylon$language$Category$this;
    private final ceylon.language.Correspondence$impl $ceylon$language$Correspondence$this;
    private final ceylon.language.Iterable$impl<Element> $ceylon$language$Iterable$this;

    private final Element first;
    private final Element last;
    private final long size;

    public Range(@Name("first") Element first,
    		     @Name("last") Element last) {
        this.$ceylon$language$Category$this = new ceylon.language.Category$impl(this);
        this.$ceylon$language$Correspondence$this = new ceylon.language.Correspondence$impl(this);
        this.$ceylon$language$Iterable$this = new ceylon.language.Iterable$impl<Element>(this);
        this.first = first;
        this.last = last;
        this.size = Math.abs(last.distanceFrom(first))+1;
    }

    private Correspondence$impl<Integer,Element> correspondence$impl = new Correspondence$impl<Integer,Element>(this);
    
    @Ignore
    @Override
    public Correspondence$impl<? super Integer,? extends Element> $ceylon$language$Correspondence$impl(){
        return correspondence$impl;
    }

    @Override
    @Ignore
    public Correspondence$impl<? super Integer, ? extends Element>.Items Items$new(Sequence<? extends Integer> keys) {
        return correspondence$impl.Items$new(keys);
    }

    @Override
    public final Element getFirst(){
        return first;
    }

    @Override
    public final Element getLast(){
        return last;
    }

    @Override
    public final java.lang.String toString(){
        return first.toString() + ".." + last.toString();
    }

    public final boolean getDecreasing(){
        return last.compare(first).smallerThan();
    }

    @SuppressWarnings("unchecked")
    @TypeInfo("Element")
    private final Element next(@Name("x") @TypeInfo("Element") Element x){
        return (Element) (getDecreasing() ?
        		x.getPredecessor() : x.getSuccessor());
    }

    @Override
    @TypeInfo("ceylon.language::Integer")
    public final long getSize(){
        return size;
    }

    @Override
    @TypeInfo("ceylon.language::Integer")
    public final Integer getLastIndex(){
        return Integer.instance(size - 1);
    }

    @Override @SuppressWarnings({ "unchecked", "rawtypes" })
    @TypeInfo("ceylon.language::Empty|ceylon.language::Sequence<Element>")
    public List<? extends Element> getRest() {
    	if (first.equals(last)) {
    	    return (List)empty_.getEmpty$();
    	}
    	else {
            return new Range<Element>(next(getFirst()), getLast());
    	}
    }

    @TypeInfo("ceylon.language::Nothing|Element")
    @Override
    public Element item(@Name("key") Integer key) {
        long index = 0;
        Element x = first;
        while (index<key.longValue()) {
        	if (x.equals(last)){
        		return null;
        	}
        	else {
                ++index;
                x=next(x);
        	}
        }
        return x;
    }

    @Override @SuppressWarnings("unchecked")
    @TypeInfo("ceylon.language::Iterator<Element>")
	public Iterator<Element> getIterator() {
        if (getDecreasing()) {
            if (first instanceof Integer && last instanceof Integer) {
                return new Iterator<Element>() {
                    private long current = ((Integer)first).value;
                    private boolean go = true;

                    @TypeInfo(value="Element|ceylon.language::Finished", erased=true)
                    public java.lang.Object next() {
                        if (!go) return exhausted_.getExhausted$();
                        long result = current;
                        if (result > ((Integer)last).value) {
                            current--;
                        } else {
                            go = false;
                        }
                        return Integer.instance(result);
                    }

                    @Override
                    public java.lang.String toString() {
                        return "IntegerRangeIterator";
                    }
                };
            }
            return new Iterator<Element>() {
                private java.lang.Object current = first;
                private boolean go = true;

                @TypeInfo(value="Element|ceylon.language::Finished", erased=true)
                public java.lang.Object next() {
                    if (!go) return exhausted_.getExhausted$();
                    java.lang.Object result = current;
                    if (((Comparable<Element>)result).compare(getLast()) == larger_.getLarger$()) {
                        current = ((Element) current).getPredecessor();
                    } else {
                        go = false;
                    }
                    return result;
                }

                @Override
                public java.lang.String toString() {
                    return "RangeIterator";
                }
            };
        } else {
            if (first instanceof Integer && last instanceof Integer) {
                return new Iterator<Element>() {
                    private long current = ((Integer)first).value;
                    private boolean go = true;

                    @TypeInfo(value="Element|ceylon.language::Finished", erased=true)
                    public java.lang.Object next() {
                        if (!go) return exhausted_.getExhausted$();
                        long result = current;
                        if (result < ((Integer)last).value) {
                            current++;
                        } else {
                            go = false;
                        }
                        return Integer.instance(result);
                    }

                    @Override
                    public java.lang.String toString() {
                        return "IntegerRangeIterator";
                    }
                };
            }
            return new Iterator<Element>() {
                private java.lang.Object current = first;
                private boolean go = true;

                @TypeInfo(value="Element|ceylon.language::Finished", erased=true)
                public java.lang.Object next() {
                    if (!go) return exhausted_.getExhausted$();
                    java.lang.Object result = current;
                    if (((Comparable<Element>)result).compare(getLast()) == smaller_.getSmaller$()) {
                        current = ((Element) current).getSuccessor();
                    } else {
                        go = false;
                    }
                    return result;
                }

                @Override
                public java.lang.String toString() {
                    return "RangeIterator";
                }
            };
        }
    }

    @Override @SuppressWarnings("unchecked")
    public final boolean contains(@Name("element") java.lang.Object element) {
        // FIXME
    	try {
    		return element != null /*&& value instanceof Element*/ ?
	             includes((Element) element) : false;
    	}
    	catch (ClassCastException cce) { //ugly hack
    		return false;
    	}
    }

    public final boolean includes(@Name("x") Element x){
        if (getDecreasing()){
            return x.compare(first).asSmallAs() &&
                    x.compare(last).asLargeAs();
        }
        else {
            return x.compare(first).asLargeAs() &&
                    x.compare(last).asSmallAs();
        }
    }

    @Override
    @Ignore
    public final boolean equals(@Name("that") java.lang.Object that){
        if (that instanceof Range) {
            @SuppressWarnings("unchecked")
            Range<Element> $that = (Range<Element>) that;
            return $that.getFirst().equals(getFirst()) && $that.getLast().equals(getLast());
        }
        else {
            return List$impl._equals(this, that);
        }
    }

    @Override
    @Ignore
    public int hashCode(){
        return List$impl._hashCode(this);
    }

    @Override
    public Range<Element> getClone() {
        return this;
    }

    /*@Override
    public Ordered<Element> segment(long from, long length) {
        throw new RuntimeException("Not implemented"); //todo!
    }*/

    @Override
    @Ignore
    public Category getKeys() {
        return $ceylon$language$Correspondence$this.getKeys();
    }

    @Override
    @Ignore
    public boolean definesEvery(@Sequenced @Name("keys")
    @TypeInfo("ceylon.language::Sequential<ceylon.language::Integer>")
    List<? extends Integer> keys) {
        return $ceylon$language$Correspondence$this.definesEvery(keys);
    }
    @Override
    @Ignore @SuppressWarnings({"unchecked", "rawtypes"})
    public boolean definesEvery() {
        return $ceylon$language$Correspondence$this.definesEvery((List)empty_.getEmpty$());
    }
    @Override
    @Ignore @SuppressWarnings({"unchecked", "rawtypes"})
    public List<? extends Integer> definesEvery$keys() {
        return (List)empty_.getEmpty$();
    }

    @Override
    @Ignore
    public boolean definesAny(@Sequenced @Name("keys")
    @TypeInfo("ceylon.language::Sequential<ceylon.language::Integer>")
    List<? extends Integer> keys) {
        return $ceylon$language$Correspondence$this.definesAny(keys);
    }
    @Override
    @Ignore @SuppressWarnings({"unchecked", "rawtypes"})
    public boolean definesAny() {
        return $ceylon$language$Correspondence$this.definesAny((List)empty_.getEmpty$());
    }
    @Override
    @Ignore @SuppressWarnings({"unchecked", "rawtypes"})
    public List<? extends Integer> definesAny$keys() {
        return (List)empty_.getEmpty$();
    }

    @Override
    @Ignore
    public ceylon.language.List<? extends Element> items(@Sequenced @Name("keys")
    @TypeInfo("ceylon.language::Sequential<ceylon.language::Integer>")
    List<? extends Integer> keys) {
        return $ceylon$language$Correspondence$this.items(keys);
    }

    @Override
    @Ignore @SuppressWarnings({"unchecked", "rawtypes"})
    public ceylon.language.List<? extends Element> items() {
        return $ceylon$language$Correspondence$this.items((List)empty_.getEmpty$());
    }

    @Override
    @Ignore @SuppressWarnings({"unchecked", "rawtypes"})
    public List<? extends Integer> items$keys() {
        return (List)empty_.getEmpty$();
    }

    @Override
    @Ignore
    public boolean containsEvery(@Sequenced @Name("elements")
    @TypeInfo("ceylon.language::Sequential<ceylon.language::Object>")
    List<?> elements) {
        return $ceylon$language$Category$this.containsEvery(elements);
    }
    @Override
    @Ignore
    public boolean containsEvery() {
        return $ceylon$language$Category$this.containsEvery(empty_.getEmpty$());
    }
    @Override
    @Ignore
    public List<?> containsEvery$elements() {
        return empty_.getEmpty$();
    }

    @Override
    @Ignore
    public boolean containsAny(@Sequenced @Name("elements")
    @TypeInfo("ceylon.language::Sequential<ceylon.language::Object>")
    List<?> elements) {
        return $ceylon$language$Category$this.containsAny(elements);
    }
    @Override
    @Ignore
    public boolean containsAny() {
        return $ceylon$language$Category$this.containsAny(empty_.getEmpty$());
    }
    @Override
    @Ignore
    public List<?> containsAny$elements() {
        return empty_.getEmpty$();
    }

    @Override
    @Ignore
    public boolean getEmpty() {
        return Some$impl._getEmpty(this);
    }

    @Override
    @Ignore
    public boolean defines(@Name("key") Integer key) {
        return List$impl._defines(this, key);
    }

    @Annotations({@Annotation("actual")})
    @Override
    @TypeInfo("ceylon.language::Range<Element>")
    public Range<? extends Element> getReversed() {
    	return new Range<Element>(last, first);
    }

    @Override @SuppressWarnings({ "unchecked", "rawtypes" })
    @TypeInfo("ceylon.language::Empty|ceylon.language::Range<Element>")
    public ceylon.language.List<? extends Element> segment(
    		@Name("from") final Integer from,
    		@Name("length") final long length) {
        //only positive length for now
        if (length<=0 || from.value>getLastIndex().value) {
        	return (ceylon.language.List)empty_.getEmpty$();
        }
        Element x = first;
        for (int i=0; i < from.value; i++) { x = next(x); }
        Element y = x;
        for (int i=1; i < length && y.compare(last).smallerThan(); i++) { y = next(y); }
        return new Range<Element>(x, y);
    }

    @Override @SuppressWarnings({ "unchecked", "rawtypes" })
    @TypeInfo("ceylon.language::Empty|ceylon.language::Range<Element>")
    public ceylon.language.List<? extends Element> span(
    		@Name("from") Integer from,
    		@TypeInfo("ceylon.language::Nothing|ceylon.language::Integer")
    		@Name("to") Integer to) {
        Integer lastIndex = getLastIndex();
		to = to==null ? Integer.instance(MAX_VALUE) : to;
        if (to.value<0) {
        	if (from.value<0) {
        		return (ceylon.language.List)empty_.getEmpty$();
        	}
        	to = Integer.instance(0);
        }
        else if (to.value>lastIndex.value) {
        	if (from.value>lastIndex.value) {
        		return (ceylon.language.List)empty_.getEmpty$();
        	}
        	to = lastIndex;
        }
        if (from.value<0) {
        	from = Integer.instance(0);
        }
        else if (from.value>lastIndex.value) {
        	from = lastIndex;
        }
        Element x = first;
        for (int i=0; i < from.value; i++) { x = next(x); }
        Element y = first;
        for (int i=0; i < to.value; i++) { y = next(y); }
        return new Range<Element>(x, y);
    }

    @Override
    @Ignore
    public Iterable<? extends Element> by(final long step) {
        if (step > 1 && first instanceof Integer && last instanceof Integer) {
            //Optimize for Integer ranges
            return new AbstractIterable<Element>() {
                @Override
                @Annotations(@Annotation("formal"))
                @TypeInfo("ceylon.language::Iterator<Element>")
                public Iterator<? extends Element> getIterator() {

                    return new Iterator<Element>() {
                        long current = ((Integer)first).value;
                        final long lim = ((Integer)last).value;
                        boolean inverse = lim < current;

                        @TypeInfo(value="Element|ceylon.language::Finished", erased=true)
                        public java.lang.Object next() {
                            long result = current;
                            if (inverse) {
                                // avoid current < lim etc: possibility of overflow
                                if (current-lim < 0) return exhausted_.getExhausted$();
                                current-=step;
                            } else {
                                if (current-lim > 0) return exhausted_.getExhausted$();
                                current+=step;
                            }
                            return Integer.instance(result);
                        }

                        @Override
                        public java.lang.String toString() {
                            return "RangeIterator";
                        }
                    };
                }
            };
        }
    	return $ceylon$language$Iterable$this.by(step);
    }

    @Override
    @Ignore
    public Sequence<? extends Element> getSequence() {
        return Sequence$impl._getSequence(this);
    }
    @Override @Ignore
    public Element find(Callable<? extends Boolean> f) {
        Element e = first;
        while (includes(e)) {
            if (f.$call(e).booleanValue()) return e;
            e = next(e);
        }
        return null;
    }
    @Override @Ignore
    public Element findLast(Callable<? extends Boolean> f) {
        return this.getReversed().find(f);
    }
    @Override
    @Ignore
    public Sequence<? extends Element> sort(Callable<? extends Comparison> f) {
        return Sequence$impl._sort(this, f);
    }
    @Override
    @Ignore
    public <Result> Iterable<? extends Result> map(Callable<? extends Result> f) {
        return new MapIterable<Element, Result>(this, f);
    }
    @Override
    @Ignore
    public Iterable<? extends Element> filter(Callable<? extends Boolean> f) {
        return new FilterIterable<Element>(this, f);
    }
    @Override @Ignore
    public <Result> Sequence<? extends Result> collect(Callable<? extends Result> f) {
        return Sequence$impl._collect(this, f);
    }
    @Override @Ignore
    public List<? extends Element> select(Callable<? extends Boolean> f) {
        return new FilterIterable<Element>(this, f).getSequence();
    }
    @Override
    @Ignore
    public <Result> Result fold(Result ini, Callable<? extends Result> f) {
        return $ceylon$language$Iterable$this.fold(ini, f);
    }
    @Override @Ignore
    public boolean any(Callable<? extends Boolean> f) {
        return $ceylon$language$Iterable$this.any(f);
    }
    @Override @Ignore
    public boolean every(Callable<? extends Boolean> f) {
        return $ceylon$language$Iterable$this.every(f);
    }
    @Override @SuppressWarnings("unchecked")
    @TypeInfo("ceylon.language::Range<Element>|ceylon.language::Empty")
    public Iterable<? extends Element> skipping(@Name("take") long skip) {
        long x=0;
        Element e=first;
        while (x++<skip) {
            e=next(e);
        }
        return this.includes(e) ? new Range<Element>(e, last) : (Iterable)empty_.getEmpty$();
    }
    @Override @SuppressWarnings({ "unchecked", "rawtypes" })
    @TypeInfo("ceylon.language::Range<Element>|ceylon.language::Empty")
    public Iterable<? extends Element> taking(@Name("take") long take) {
        if (take == 0) {
            return (Iterable)empty_.getEmpty$();
        }
        long x=0;
        Element e=first;
        while (++x<take) {
            e=next(e);
        }
        return this.includes(e) ? new Range<Element>(first, e) : this;
    }

    @Override @Ignore
    public long count(Callable<? extends Boolean> f) {
        Element e = first;
        long c = 0;
        while (includes(e)) {
            if (f.$call(e).booleanValue()) c++;
            e = next(e);
        }
        return c;
    }
    @TypeInfo("ceylon.language::Range<Element>")
    @Override
    public Range<? extends Element> getCoalesced() {
        return this; //There can be no nulls in a Range
    }
    @Override @Ignore
    public Iterable<? extends Entry<? extends Integer, ? extends Element>> getIndexed() {
        return $ceylon$language$Iterable$this.getIndexed();
    }
    @SuppressWarnings("rawtypes")
    @Override @Ignore public <Other>Iterable chain(Iterable<? extends Other> other) {
        return $ceylon$language$Iterable$this.chain(other);
    }
    @Override @Ignore
    public <Key> Map<? extends Key, ? extends Sequence<? extends Element>> group(Callable<? extends Key> grouping) {
        return $ceylon$language$Iterable$this.group(grouping);
    }

    @Annotations(@Annotation("actual"))
    @TypeParameters(@TypeParameter("Other"))
    @TypeInfo("ceylon.language::Sequence<Element|Other>")
    @Override @SuppressWarnings("rawtypes")
    public <Other>Sequence withLeading(Other e) {
        return List$impl._withLeading(this, e);
    }
    @Annotations(@Annotation("actual"))
    @TypeParameters(@TypeParameter("Other"))
    @TypeInfo("ceylon.language::Sequence<Element|Other>")
    @Override @SuppressWarnings("rawtypes")
    public <Other>Sequence withTrailing(Other e) {
        return List$impl._withTrailing(this, e);
    }

}
