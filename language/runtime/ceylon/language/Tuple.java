package ceylon.language;

import com.redhat.ceylon.compiler.java.language.ArraySequence;
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
import com.redhat.ceylon.compiler.java.metadata.Variance;

@Ceylon(major = 3)
@TypeParameters({
    @TypeParameter(value = "Element", variance = Variance.OUT),
    @TypeParameter(value = "First", variance = Variance.OUT, 
            satisfies = "Element"),
    @TypeParameter(value = "Rest", variance = Variance.OUT, 
            satisfies = "ceylon.language::Sequential<Element>")
 })
@Class(extendsType="ceylon.language::Object")
@SatisfiedTypes("ceylon.language::Sequence<Element>")
public class Tuple<Element, First extends Element, Rest extends Sequential<? extends Element>> 
        implements Sequence<Element> {

    private final Iterable$impl<Element> iterable$impl = new Iterable$impl<Element>(this);
    private final Sequence$impl<Element> sequence$impl = new Sequence$impl<Element>(this);
    private final List$impl<Element> list$impl = new List$impl<Element>(this);
    private final Correspondence$impl<Integer,Element> correspondence$impl = new Correspondence$impl<Integer,Element>(this);
    private final Category$impl category$impl = new Category$impl(this);
    private final Collection$impl<Element> collection$impl = new Collection$impl<Element>(this);
	private final First first;
	private final Rest rest;
	
	public Tuple(@TypeInfo(value="First") 
	             @Name("first") First first,
	             @TypeInfo(value="Rest")
			     @Name("rest") Rest rest) {
		this.first = first;
		this.rest = rest;
	}
	
	@TypeInfo(value="First")
	public First getFirst() {
		return first;
	}
	
	@TypeInfo(value="Rest")
	public Rest getRest() {
		return rest;
	}

	@Override
	@Annotations(@Annotation("actual"))
	public long getSize() {
		return list$impl.getSize();
	}

	@Override
	@Annotations(@Annotation("actual"))
	public boolean defines(Integer key) {
		return correspondence$impl.defines(key);
	}

	@Override
	@Annotations(@Annotation("actual"))
	@TypeInfo("ceylon.language::Null|Element")
	public Element item(@Name("index") Integer key) {
	    final long idx = key.value;
	    if (idx > 0) {
            return rest.item(key.getPredecessor());
	    } else if (idx == 0) {
	        return first;
	    }
		return null;
	}

	@Override
	@Annotations(@Annotation("actual"))
	@TypeInfo("ceylon.language::Iterator<Element>")
	public Iterator<? extends Element> getIterator() {
		return list$impl.getIterator();
	}

	@Override
	@Annotations(@Annotation("actual"))
	@TypeInfo("Element|ceylon.language::Null")
	public Element findLast(
	        @TypeInfo("ceylon.language::Callable<ceylon.language::Boolean,ceylon.language::Tuple<Element,Element,ceylon.language::Empty>>")
	        Callable<? extends Boolean> selecting) {
		return iterable$impl.findLast(selecting);
	}

	@Override @SuppressWarnings("rawtypes")
	@Annotations(@Annotation("actual"))
	@TypeParameters(@TypeParameter("Other"))
	@TypeInfo("ceylon.language::Sequence<Element|Other>")
	public <Other> Sequence withLeading(@Name("element")
            @TypeInfo("Other") Other element) {
		return list$impl.withLeading(element);
	}

	@Override @SuppressWarnings("rawtypes")
	@Annotations(@Annotation("actual"))
	@TypeParameters(@TypeParameter("Other"))
	@TypeInfo("ceylon.language::Sequence<Element|Other>")
	public <Other> Sequence withTrailing(@Name("element")
            @TypeInfo("Other") Other element) {
		return list$impl.withTrailing(element);
	}

	@Override
	@Annotations(@Annotation("actual"))
	public boolean getEmpty() {
		return false;
	}

	@Override
	@Annotations(@Annotation("actual"))
	public boolean contains(@Name("element")
	        @TypeInfo("ceylon.language::Object")
	        java.lang.Object element) {
		return collection$impl.contains(element);
	}

	@Override
	@Annotations(@Annotation("default"))
	@TypeInfo("ceylon.language::Iterable<Result>")
	@TypeParameters(@TypeParameter("Result"))
	public <Result> Iterable<? extends Result> map(
	        @TypeInfo("ceylon.language::Callable<Result,ceylon.language::Tuple<Element,Element,ceylon.language::Empty>>")
			Callable<? extends Result> collecting) {
		return iterable$impl.map(collecting);
	}

	@Override
	@Annotations(@Annotation("default"))
	@TypeInfo("ceylon.language::Iterable<Element>")
	public Iterable<? extends Element> filter(
	        @TypeInfo("ceylon.language::Callable<ceylon.language::Boolean,ceylon.language::Tuple<Element,Element,ceylon.language::Empty>>")
			Callable<? extends Boolean> selecting) {
		return iterable$impl.filter(selecting);
	}

	@Override
	@Annotations(@Annotation("default"))
	@TypeInfo("Result")
	@TypeParameters(@TypeParameter("Result"))
	public <Result> Result fold(@Name("initial")
            @TypeInfo("Result") Result initial,
            @Name("accumulating")
            @TypeInfo("ceylon.language::Callable<Result,ceylon.language::Tuple<Result|Element,Result,ceylon.language::Tuple<Element,Element,ceylon.language::Empty>>>")
            Callable<? extends Result> accumulating) {
		return iterable$impl.fold(initial, accumulating);
	}

	@Override
	@Annotations(@Annotation("default"))
	@TypeInfo("Element|ceylon.language::Null")
	public Element find(
	        @TypeInfo("ceylon.language::Callable<ceylon.language::Boolean,ceylon.language::Tuple<Element,Element,ceylon.language::Empty>>")
	        Callable<? extends Boolean> selecting) {
		return iterable$impl.find(selecting);
	}

	@Override
	@Annotations(@Annotation("default"))
	@TypeInfo("ceylon.language::Sequential<Element>")
	public Sequential<? extends Element> select(
	        @TypeInfo("ceylon.language::Callable<ceylon.language::Boolean,ceylon.language::Tuple<Element,Element,ceylon.language::Empty>>")
	        Callable<? extends Boolean> selecting) {
		return iterable$impl.select(selecting);
	}

	@Override
	@Annotations(@Annotation("default"))
	@TypeInfo("ceylon.language::Boolean")
	public boolean any(
	        @TypeInfo("ceylon.language::Callable<ceylon.language::Boolean,ceylon.language::Tuple<Element,Element,ceylon.language::Empty>>")
	        Callable<? extends Boolean> selecting) {
		return iterable$impl.any(selecting);
	}

	@Override
	@Annotations(@Annotation("default"))
	@TypeInfo("ceylon.language::Boolean")
	public boolean every(
	        @TypeInfo("ceylon.language::Callable<ceylon.language::Boolean,ceylon.language::Tuple<Element,Element,ceylon.language::Empty>>")
	        Callable<? extends Boolean> selecting) {
		return iterable$impl.every(selecting);
	}

	@Override
	@Annotations(@Annotation("default"))
	@TypeInfo("ceylon.language::Iterable<Element>")
	public Iterable<? extends Element> skipping(long skip) {
		return iterable$impl.skipping(skip);
	}

	@Override
	@Annotations(@Annotation("default"))
	@TypeInfo("ceylon.language::Iterable<Element>")
	public Iterable<? extends Element> taking(long take) {
		return iterable$impl.taking(take);
	}

	@Override
	@Annotations(@Annotation("default"))
	@TypeInfo("ceylon.language::Iterable<Element>")
	public Iterable<? extends Element> by(long step) {
		return iterable$impl.by(step);
	}

	@Override
	@Annotations(@Annotation("default"))
	@TypeInfo("ceylon.language::Integer")
	public long count(
	        @TypeInfo("ceylon.language::Callable<ceylon.language::Boolean,ceylon.language::Tuple<Element,Element,ceylon.language::Empty>>")
	        Callable<? extends Boolean> selecting) {
		return iterable$impl.count(selecting);
	}

	@Override
	@Annotations(@Annotation("default"))
	@TypeInfo("ceylon.language::Iterable<Element&ceylon.language::Object>")
	public Iterable<? extends Element> getCoalesced() {
		return iterable$impl.getCoalesced();
	}

	@Override
	@Annotations(@Annotation("default"))
	@TypeInfo("ceylon.language::Iterable<ceylon.language::Entry<ceylon.language::Integer,Element&ceylon.language::Object>>")
	public Iterable<? extends Entry<? extends Integer, ? extends Element>> getIndexed() {
		return iterable$impl.getIndexed();
	}

	@Override @SuppressWarnings("rawtypes")
	@Annotations(@Annotation("default"))
	@TypeInfo("ceylon.language::Iterable<Element|Other>")
	@TypeParameters(@TypeParameter("Other"))
	public <Other> Iterable chain(@Name("other")
            @TypeInfo("ceylon.language::Iterable<Other>")
        	Iterable<? extends Other> other) {
		return iterable$impl.chain(other);
	}

	@Override
	@Annotations(@Annotation("default"))
	@TypeParameters(@TypeParameter(value = "Grouping", satisfies = "ceylon.language::Object"))
	@TypeInfo("ceylon.language::Map<Grouping,ceylon.language::Sequence<Element>>")
	public <Key> Map<? extends Key, ? extends Sequence<? extends Element>> group(@Name("grouping")
	        @TypeInfo("ceylon.language::Callable<Grouping,ceylon.language::Tuple<Element,Element,ceylon.language::Empty>>")
			Callable<? extends Key> grouping) {
		return iterable$impl.group(grouping);
	}

	@Override
	@Annotations(@Annotation("default"))
	public boolean containsEvery(@Sequenced @Name("elements")
	        @TypeInfo("ceylon.language::Sequential<ceylon.language::Object>")
	Sequential<?> elements) {
		return category$impl.containsEvery(elements);
	}

	@Override
	@Ignore
	public boolean containsEvery() {
		return category$impl.containsEvery();
	}

	@Override
	@Ignore
	public Sequential<?> containsEvery$elements() {
		return category$impl.containsEvery$elements();
	}

	@Override
	@Annotations(@Annotation("default"))
	public boolean containsAny(@Sequenced @Name("elements")
	        @TypeInfo("ceylon.language::Sequential<ceylon.language::Object>")
	Sequential<?> elements) {
		return category$impl.containsAny(elements);
	}

	@Override
	@Ignore
	public boolean containsAny() {
		return category$impl.containsAny();
	}

	@Override
	@Ignore
	public Sequential<?> containsAny$elements() {
		return category$impl.containsAny$elements();
	}

	@Override
	@Annotations(@Annotation("actual"))
	@TypeInfo("ceylon.language::Sequence<Element>")
	public Sequence<? extends Element> getClone() {
		return this;
	}

	@Override
	@Annotations(@Annotation("default"))
	public Category getKeys() {
		return correspondence$impl.getKeys();
	}

	@Override
	@Annotations(@Annotation("default"))
	public boolean definesEvery(@Sequenced @Name("keys")
            @TypeInfo("ceylon.language::Sequential<ceylon.language::Integer>")
	        Sequential<? extends Integer> keys) {
		return correspondence$impl.definesEvery(keys);
	}

	@Override
	@Ignore
	public boolean definesEvery() {
		return correspondence$impl.definesEvery();
	}

	@Override
	@Ignore
	public Sequential<? extends Integer> definesEvery$keys() {
		return correspondence$impl.definesEvery$keys();
	}

	@Override
	@Annotations(@Annotation("default"))
	public boolean definesAny(@Sequenced @Name("keys")
            @TypeInfo("ceylon.language::Sequential<ceylon.language::Integer>")
	        Sequential<? extends Integer> keys) {
		return correspondence$impl.definesAny(keys);
	}

	@Override
	@Ignore
	public boolean definesAny() {
		return correspondence$impl.definesAny();
	}

	@Override
	@Ignore
	public Sequential<? extends Integer> definesAny$keys() {
		return correspondence$impl.definesAny$keys();
	}

	@Override
	@Annotations(@Annotation("default"))
	@TypeInfo("ceylon.language::Sequential<Element|ceylon.language::Null>")
	public Sequential<? extends Element> items(@Sequenced @Name("keys")
            @TypeInfo("ceylon.language::Sequential<ceylon.language::Integer>")
	        Sequential<? extends Integer> keys) {
		return correspondence$impl.items(keys);
	}

	@Override
	@Ignore
	public Sequential<? extends Element> items() {
		return correspondence$impl.items();
	}

	@Override
	@Ignore
	public Sequential<? extends Integer> items$keys() {
		return correspondence$impl.items$keys();
	}

	@Override
	@Ignore
	public Correspondence$impl<? super Integer, ? extends Element> $ceylon$language$Correspondence$impl() {
		return correspondence$impl;
	}

	@Override
	@Ignore
	public Correspondence$impl<? super Integer, ? extends Element>.Items Items$new(Sequence<? extends Integer> keys) {
		return correspondence$impl.Items$new(keys);
	}

    @Override
    @Annotations(@Annotation("default"))
    public Sequential<? extends Element> spanFrom(Integer from) {
        return span(from, Integer.instance(getSize()));
    }

    @Override
    @Annotations(@Annotation("default"))
    public Sequential<? extends Element> spanTo(Integer to) {
        if (to.longValue() < 0) {
            return (Sequential<Element>)empty_.getEmpty$();
        }
        return span(Integer.instance(0), to);
    }

    @Override
	@Annotations(@Annotation("default"))
	public Sequential<? extends Element> span(Integer from,
	        @Name("to") Integer to) {
        if (from.longValue() < 0 && to.longValue() < 0) {
            return (Sequential<Element>)empty_.getEmpty$();
        }
	    long end = to.value;
	    long _from = from.value;
	    if (end < 0) {
	        end = 0L;
	    }
	    if (_from < 0) {
	        _from = 0L;
	    }
        return _from<=end ? this.segment(from,end-_from+1)
                : this.segment(Integer.instance(end),_from-end+1).getReversed().getSequence();
	}

    @Override @SuppressWarnings("unchecked")
	@Annotations(@Annotation("default"))
	public Sequential<? extends Element> segment(Integer from, long length) {
        if(length < 0)
            return (Sequential<? extends Element>) empty_.getEmpty$();
	    long _from = from.value;
	    if(_from < 0)
	        _from = 0;
        if (_from == 0) {
            return length==1 ? new ArraySequence<Element>(first)
                : rest.segment(Integer.instance(0),length+_from-1).withLeading(first);
        }
        return (Sequential<? extends Element>) rest.segment(from.getPredecessor(),length);
	}

	@Override
	@Annotations(@Annotation("actual"))
	@TypeInfo("ceylon.language::Integer")
	public Integer getLastIndex() {
	    Integer li = rest.getLastIndex();
		return li == null ? Integer.instance(0) : li.getSuccessor();
	}

	@Override
	@Annotations(@Annotation("default"))
	public Element getLast() {
		return sequence$impl.getLast();
	}

    @Override @SuppressWarnings("unchecked")
	@Annotations(@Annotation("actual"))
    @TypeInfo("ceylon.language::Sequence<Element>")
	public Sequence<? extends Element> getReversed() {
		return rest.getReversed().withTrailing(first);
	}

	@Override
	@Annotations(@Annotation("actual"))
    @TypeInfo("ceylon.language::Sequence<Element>")
	public Sequence<? extends Element> getSequence() {
		return this;
	}

	@Override
	@Annotations(@Annotation("actual"))
	@TypeInfo("ceylon.language::Sequence<Element>")
	public Sequence<? extends Element> sort(
            @TypeInfo("ceylon.language::Callable<ceylon.language::Null|ceylon.language::Comparison,ceylon.language::Tuple<Element,Element,ceylon.language::Tuple<Element,Element,ceylon.language::Empty>>>")
			Callable<? extends Comparison> comparing) {
		return sequence$impl.sort(comparing);
	}

	@Override
	@Annotations(@Annotation("actual"))
	@TypeParameters(@TypeParameter("Result"))
	@TypeInfo("ceylon.language::Sequence<Result>")
	public <Result> Sequence<? extends Result> collect(
            @TypeInfo("ceylon.language::Callable<Result,ceylon.language::Tuple<Element,Element,ceylon.language::Empty>>")
			Callable<? extends Result> collecting) {
	    return sequence$impl.collect(collecting);
	}

	@Override @Ignore
	public boolean equals(java.lang.Object obj) {
	    return list$impl.equals(obj);
	}
	
    @Override
	public java.lang.String toString() {
	    StringBuilder b = new StringBuilder().append("[ ");
	    boolean first = true;
	    Iterator<? extends Element> iterator = getIterator();
	    java.lang.Object elem;
	    while((elem = iterator.next()) != finished_.getFinished$()){
	        if(first){
	            first = false;
	        }else{
	            b.append(", ");
	        }
	        b.append(elem != null ? elem.toString() : "null");
	    }
	    return b.append(" ]").toString();
	}
}
