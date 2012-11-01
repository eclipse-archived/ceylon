package ceylon.language;

import ceylon.language.Correspondence$impl.Items;

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
    @TypeParameter(value = "First", variance = Variance.OUT),
    @TypeParameter(value = "Rest", variance = Variance.OUT)
 })
@Class(extendsType="ceylon.language.Object")
@SatisfiedTypes("ceylon.language.Sequence<Element>")
public class Tuple<Element, First, Rest> 
        implements Sequence<Element> {
	
	private final Element first;
	private final List<Element> rest;
	
	public Tuple(@TypeInfo("First&Element") 
	             @Name("first") Element first,
	             @TypeInfo("Rest&Empty|Rest&Sequence<Element>")
			     @Name("rest") List<Element> rest) {
		this.first = first;
		this.rest = rest;
	}
	
	@TypeInfo("First&Element")
	public Element getFirst() {
		return first;
	}
	
	@TypeInfo("Rest&Empty|Rest&Sequence<Element>")
	public List<Element> getRest() {
		return rest;
	}

	@Override
	@Annotations(@Annotation("actual"))
	public long getSize() {
		return 0;
	}

	@Override
	@Annotations(@Annotation("actual"))
	public boolean defines(Integer key) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	@Annotations(@Annotation("actual"))
	@TypeInfo("ceylon.language::Nothing|Element")
	public Element item(Integer key) {
		return null;
	}

	@Override
	@Annotations(@Annotation("actual"))
	@TypeInfo("ceylon.language::Iterator<Element>")
	public Iterator<? extends Element> getIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Annotations(@Annotation("actual"))
	@TypeInfo("Element|ceylon.language.Nothing")
	public Element findLast(Callable<? extends Boolean> selecting) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Annotations(@Annotation("actual"))
	@TypeParameters(@TypeParameter("Other"))
	@TypeInfo("ceylon.language::Sequence<Element|Other>")
	public <Other> Sequence withLeading(Other element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Annotations(@Annotation("actual"))
	@TypeParameters(@TypeParameter("Other"))
	@TypeInfo("ceylon.language::Sequence<Element|Other>")
	public <Other> Sequence withTrailing(Other element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Annotations(@Annotation("actual"))
	public boolean getEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	@Annotations(@Annotation("actual"))
	public boolean contains(java.lang.Object element) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	@Annotations(@Annotation("default"))
	@TypeInfo("ceylon.language::Iterable<Result>")
	@TypeParameters(@TypeParameter("Result"))
	public <Result> Iterable<? extends Result> map(
			Callable<? extends Result> collecting) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Annotations(@Annotation("default"))
	@TypeInfo("ceylon.language::Iterable<Element>")
	public Iterable<? extends Element> filter(
			Callable<? extends Boolean> selecting) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Annotations(@Annotation("default"))
	@TypeInfo("Result")
	@TypeParameters(@TypeParameter("Result"))
	public <Result> Result fold(Result initial,
			Callable<? extends Result> accumulating) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Annotations(@Annotation("default"))
	@TypeInfo("Element|ceylon.language::Nothing")
	public Element find(Callable<? extends Boolean> selecting) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Annotations(@Annotation("default"))
	@TypeInfo("ceylon.language::Empty|ceylon.language::Sequence<Element>")
	public List<? extends Element> select(Callable<? extends Boolean> selecting) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Annotations(@Annotation("default"))
	@TypeInfo("ceylon.language::Boolean")
	public boolean any(Callable<? extends Boolean> selecting) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	@Annotations(@Annotation("default"))
	@TypeInfo("ceylon.language::Boolean")
	public boolean every(Callable<? extends Boolean> selecting) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	@Annotations(@Annotation("default"))
	@TypeInfo("ceylon.language::Iterable<Element>")
	public Iterable<? extends Element> skipping(long skip) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Annotations(@Annotation("default"))
	@TypeInfo("ceylon.language::Iterable<Element>")
	public Iterable<? extends Element> taking(long take) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Annotations(@Annotation("default"))
	@TypeInfo("ceylon.language::Iterable<Element>")
	public Iterable<? extends Element> by(long step) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Annotations(@Annotation("default"))
	@TypeInfo("ceylon.language::Integer")
	public long count(Callable<? extends Boolean> selecting) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	@Annotations(@Annotation("default"))
	@TypeInfo("ceylon.language::Iterable<Element&ceylon.language::Object>")
	public Iterable<? extends Element> getCoalesced() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Annotations(@Annotation("default"))
	@TypeInfo("ceylon.language::Iterable<ceylon.language::Entry<ceylon.language::Integer,Element&ceylon.language::Object>>")
	public Iterable<? extends Entry<? extends Integer, ? extends Element>> getIndexed() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Annotations(@Annotation("default"))
	@TypeInfo("ceylon.language::Iterable<Element|Other>")
	@TypeParameters(@TypeParameter("Other"))
	public <Other> Iterable chain(Iterable<? extends Other> other) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Annotations(@Annotation("default"))
	@TypeParameters(@TypeParameter(value = "Grouping", satisfies = "ceylon.language::Object"))
	@TypeInfo("ceylon.language::Map<Grouping,ceylon.language::Sequence<Element>>")
	public <Key> Map<? extends Key, ? extends Sequence<? extends Element>> group(
			Callable<? extends Key> grouping) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Annotations(@Annotation("default"))
	public boolean containsEvery(
			@Sequenced @Name("elements") @TypeInfo("ceylon.language::Iterable<ceylon.language::Object>") Iterable<?> elements) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	@Ignore
	public boolean containsEvery() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	@Ignore
	public Iterable<?> containsEvery$elements() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Annotations(@Annotation("default"))
	public boolean containsAny(
			@Sequenced @Name("elements") @TypeInfo("ceylon.language::Iterable<ceylon.language::Object>") Iterable<?> elements) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	@Ignore
	public boolean containsAny() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	@Ignore
	public Iterable<?> containsAny$elements() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Annotations(@Annotation("formal"))
	public Collection<? extends Element> getClone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Annotations(@Annotation("default"))
	public Category getKeys() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Annotations(@Annotation("default"))
	public boolean definesEvery(Iterable<? extends Integer> keys) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	@Ignore
	public boolean definesEvery() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	@Ignore
	public Iterable<? extends Integer> definesEvery$keys() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Annotations(@Annotation("default"))
	public boolean definesAny(Iterable<? extends Integer> keys) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	@Ignore
	public boolean definesAny() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	@Ignore
	public Iterable<? extends Integer> definesAny$keys() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Annotations(@Annotation("default"))
	@TypeInfo("ceylon.language::Empty|ceylon.language::Sequence<Item|ceylon.language::Nothing>")
	public List<? extends Element> items(Iterable<? extends Integer> keys) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Ignore
	public Iterable<? extends Element> items() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Ignore
	public Iterable<? extends Integer> items$keys() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Ignore
	public Correspondence$impl<? super Integer, ? extends Element> $ceylon$language$Correspondence$impl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Ignore
	public Items Items$new(Sequence<? extends Integer> keys) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Annotations(@Annotation("default"))
	public List<? extends Element> span(Integer from, Integer to) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Annotations(@Annotation("default"))
	public List<? extends Element> segment(Integer from, long length) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Annotations(@Annotation("actual"))
	@TypeInfo("ceylon.language::Integer")
	public Integer getLastIndex() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Annotations(@Annotation("default"))
	public Element getLast() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Annotations(@Annotation("actual"))
	public Sequence<? extends Element> getReversed() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Annotations(@Annotation("actual"))
	public Sequence<? extends Element> getSequence() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Annotations(@Annotation("actual"))
	@TypeInfo("ceylon.language::Sequence<Element>")
	public Sequence<? extends Element> sort(
			Callable<? extends Comparison> comparing) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Annotations(@Annotation("actual"))
	@TypeParameters(@TypeParameter("Result"))
	@TypeInfo("ceylon.language::Sequence<Result>")
	public <Result> Sequence<? extends Result> collect(
			Callable<? extends Result> collecting) {
		// TODO Auto-generated method stub
		return null;
	}

}
