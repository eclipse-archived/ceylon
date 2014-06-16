package ceylon.language;

import static com.redhat.ceylon.compiler.java.Util.toInt;
import static com.redhat.ceylon.compiler.java.runtime.metamodel.Metamodel.getTypeDescriptor;

import java.lang.ref.SoftReference;
import java.util.Arrays;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.language.AbstractIterator;
import com.redhat.ceylon.compiler.java.metadata.Annotation;
import com.redhat.ceylon.compiler.java.metadata.Annotations;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.FunctionalParameter;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.Transient;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 7)
@Class(extendsType = "ceylon.language::Object", basic = false, identifiable = false)
@Annotations({
        @Annotation(
                value = "doc",
                arguments = {"A _tuple_ is a typed linked list. Each instance of \n`Tuple` represents the value and type of a single link.\nThe attributes `first` and `rest` allow us to retrieve\na value form the list without losing its static type \ninformation.\n\n    value point = Tuple(0.0, Tuple(0.0, Tuple(\"origin\")));\n    Float x = point.first;\n    Float y = point.rest.first;\n    String label = point.rest.rest.first;\n\nUsually, we abbreviate code involving tuples.\n\n    [Float,Float,String] point = [0.0, 0.0, \"origin\"];\n    Float x = point[0];\n    Float y = point[1];\n    String label = point[2];\n\nA list of types enclosed in brackets is an abbreviated \ntuple type. An instance of `Tuple` may be constructed \nby surrounding a value list in brackets:\n\n    [String,String] words = [\"hello\", \"world\"];\n\nThe index operator with a literal integer argument is a \nshortcut for a chain of evaluations of `rest` and \n`first`. For example, `point[1]` means `point.rest.first`.\n\nA _terminated_ tuple type is a tuple where the type of\nthe last link in the chain is `Empty`. An _unterminated_ \ntuple type is a tuple where the type of the last link\nin the chain is `Sequence` or `Sequential`. Thus, a \nterminated tuple type has a length that is known\nstatically. For an unterminated tuple type only a lower\nbound on its length is known statically.\n\nHere, `point` is an unterminated tuple:\n\n    String[] labels = ... ;\n    [Float,Float,String*] point = [0.0, 0.0, *labels];\n    Float x = point[0];\n    Float y = point[1];\n    String? firstLabel = point[2];\n    String[] allLabels = point[2...];"}),
        @Annotation(
                value = "by",
                arguments = {"Gavin"}),
        @Annotation("shared"),
        @Annotation("final")})
@SatisfiedTypes({
        "ceylon.language::Sequence<Element>"})
@TypeParameters({
        @TypeParameter(
                value = "Element",
                variance = Variance.OUT,
                satisfies = {},
                caseTypes = {}),
        @TypeParameter(
                value = "First",
                variance = Variance.OUT,
                satisfies = {"Element"},
                caseTypes = {}),
        @TypeParameter(
                value = "Rest",
                variance = Variance.OUT,
                satisfies = {"ceylon.language::Sequential<Element>"},
                caseTypes = {},
                defaultValue = "ceylon.language::Empty")})
public final class Tuple<Element, First extends Element, 
                Rest extends Sequential<? extends Element>>
        implements ReifiedType, Sequence<Element> {

    @Ignore
    public Tuple(@Ignore TypeDescriptor $reifiedElement, 
            java.lang.Object[] array, long first, long length, boolean copy) {
        this.$ceylon$language$Category$this = 
                new Category$impl<java.lang.Object>(Object.$TypeDescriptor$, this);
        this.$ceylon$language$Iterable$this = 
                new Iterable$impl<Element,java.lang.Object>($reifiedElement, 
                        TypeDescriptor.NothingType, this);
        this.$ceylon$language$Collection$this = 
                new Collection$impl<Element>($reifiedElement, this);
        this.$ceylon$language$Correspondence$this = 
                new Correspondence$impl<Integer,Element>(Integer.$TypeDescriptor$, 
                        $reifiedElement, this);
        this.$ceylon$language$List$this = 
                new List$impl<Element>($reifiedElement, this);
        this.$ceylon$language$Sequence$this = 
                new Sequence$impl<Element>($reifiedElement, this);
        this.$ceylon$language$Sequential$this = 
                new Sequential$impl<Element>($reifiedElement, this);
        if (length == USE_ARRAY_SIZE) {
            length = array.length;
        }
        if (array.length==0 || 
                length == 0 ||
                array.length <= first) {
            throw new AssertionError("Tuple may not have zero elements");
        }
        if (first + length > array.length) {
            throw new AssertionError("Overflow :" + 
                    (first + length) + " > " + array.length);
        }
        this.$reifiedElement = $reifiedElement;
        if (copy) {
            this.array = (Element[])Arrays.copyOfRange(array, 
                  Util.toInt(first), Util.toInt(length));
            this.first = 0;
            
        } else {
            this.array = (Element[])array;
            this.first = Util.toInt(first);
        }
        this.length = Util.toInt(length);
    }
    
    public Tuple(@Ignore
            final TypeDescriptor $reifiedElement, 
            @Ignore
            final TypeDescriptor $reifiedFirst, 
            @Ignore
            final TypeDescriptor $reifiedRest, 
            @Name("first")
            @TypeInfo("First")
            final First first, @Name("rest")
            @TypeInfo("Rest")
            final Rest rest) {
        this($reifiedElement, makeArray(first, rest));
    }
    
    @Ignore
    public Tuple(TypeDescriptor $reifiedElement, 
    		java.lang.Object[] elements) {
        this($reifiedElement, elements, 0, elements.length, false);
    }
    
    @Ignore
    public Tuple(TypeDescriptor $reifiedElement, 
    		java.lang.Object[] elements, 
    		Sequential<?> tail) {
        this($reifiedElement, makeArray(elements, tail));
    }
    
    private static java.lang.Object[] makeArray(java.lang.Object first, 
    		Sequential<?> rest) {
        java.lang.Object[] elements = new java.lang.Object[Util.toInt(rest.getSize() + 1)];
        elements[0] = first;
        copyToArray(rest, elements, 1);
        return elements;
    }
    
    private static java.lang.Object[] makeArray(java.lang.Object[] array, 
    		Sequential<?> tail) {
        java.lang.Object[] elements = Arrays.copyOf(array, 
        		array.length + Util.toInt(tail.getSize()));
        copyToArray(tail, elements, array.length);
        return elements;
    }
    
    private static void copyToArray(Sequential<?> seq, 
    		java.lang.Object[] array, int offset) {
        Iterator<?> iter = seq.iterator();
        int i = offset;
        for (java.lang.Object elem = iter.next(); 
        		elem != ceylon.language.finished_.get_(); 
        		elem = iter.next()) {
            array[i++] = elem;
        }
    }
    
    @Ignore
    protected Tuple<Element, ? extends First, ? extends Rest> 
    backedBy$hidden(Element[] array, long first, long length) {
        return new Tuple<Element, First, Rest>($getReifiedElement$(), 
        		array, first, length, false);
    }
    
    @Ignore
    protected TypeDescriptor $getReifiedElement$() {
        return $getUnionOfAllType(0);
    }
    
    @Annotations({
            @Annotation("shared"),
            @Annotation("actual")})
    @Override
    @TypeInfo("First")
    @SuppressWarnings("unchecked")
    public final First getFirst() {
        return (First)array[first];
    }
    
    @Annotations({
            @Annotation("shared"),
            @Annotation("actual")})
    @Override
    @TypeInfo("Rest")
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Rest getRest() {
        if (length==1) {
            return (Rest)empty_.get_();
        }
        else {
        	return (Rest) new Tuple($getUnionOfAllType(1),
            		array, first+1, length-1, false);
        }
    }

    @Annotations({
            @Annotation("shared"),
            @Annotation("actual")})
    @Override
    @TypeInfo("ceylon.language::Integer")
    @Transient
    public final long getSize() {
        return length;
    }
    
    @Annotations({
            @Annotation("shared"),
            @Annotation("actual")})
    @Override
    @TypeInfo("Element|ceylon.language::Null")
    public final Element getFromFirst(@Name("index")
    @TypeInfo("ceylon.language::Integer")
    final long index) {
        if (index < 0 || index >= length) {
            return null;
        }
        else {
            return (Element)array[toInt(index)+first];
        }
    }
    
    @Ignore
    @Override
    public Element getFromLast(long index) {
        if (index < 0 || index >= length) {
            return null;
        }
        return (Element)array[length-1-toInt(index)+first];
    }

    @Ignore
    @Override
    public final Element get(Integer index) {
        return getFromFirst(index.value);
    }
    
    @Annotations({
            @Annotation("shared"),
            @Annotation("actual")})
    @Override
    @TypeInfo("ceylon.language::Integer")
    @Transient
    public final ceylon.language.Integer getLastIndex() {
        return Integer.instance(length - 1);
    }
    
    @Annotations({
            @Annotation("shared"),
            @Annotation("actual")})
    @Override
    @TypeInfo("Element")
    @Transient
    public final Element getLast() {
        return (Element)array[first + length - 1];
    }
    
    @Annotations({
            @Annotation("shared"),
            @Annotation("actual")})
    @Override
    @TypeInfo("ceylon.language::Sequence<Element>")
    @Transient
    public final ceylon.language.Sequence<? extends Element> 
    getReversed() {
        Element[] reversed = reversedCopy$priv$((Element[])array, 
                first, length);
        return backedBy$hidden(reversed, 0, length);
    }
    
    @Override
    @Ignore
    public List<? extends Element> sublistFrom(long index) {
        return $ceylon$language$List$this.sublistFrom(index);
    }
    
    @Override
    @Ignore
    public List<? extends Element> sublistTo(long index) {
        return $ceylon$language$List$this.sublistTo(index);
    }
    
    @Override
    @Ignore
    public List<? extends Element> sublist(long from, long to) {
        return $ceylon$language$List$this.sublist(from, to);
    }
    
    @Annotations({
            @Annotation("shared"),
            @Annotation("actual")})
    @Override
    @TypeInfo("ceylon.language::Sequential<Element>")
    public final ceylon.language.Sequential<? extends Element> 
    segment(@Name("from")
    @TypeInfo("ceylon.language::Integer")
    final ceylon.language.Integer from, @Name("length")
    @TypeInfo("ceylon.language::Integer")
    long length) {
        long fromIndex = from.longValue();
        if (fromIndex < 0) {
            length = length+fromIndex;
            fromIndex = 0;
        }
        final long lastIndex = getLastIndex().longValue();
        
        if (fromIndex > lastIndex || length <= 0) {
            return (Sequential<? extends Element>)empty_.get_();
        }
        long l;
        if (length > lastIndex-fromIndex) {
            l = lastIndex-fromIndex+1;
        } else {
            l = length;
        }
        return backedBy$hidden((Element[])array, 
                fromIndex+first, l);
    }
    
    @Annotations({
            @Annotation("shared"),
            @Annotation("actual")})
    @Override
    @TypeInfo("ceylon.language::Sequential<Element>")
    public final ceylon.language.Sequential<? extends Element> 
    span(@Name("from")
    @TypeInfo("ceylon.language::Integer")
    final ceylon.language.Integer from, @Name("end")
    @TypeInfo("ceylon.language::Integer")
    final ceylon.language.Integer end) {
        long fromIndex = Util.toInt(from.longValue());
        long toIndex = end==null ? getSize() : end.longValue();
        long lastIndex = getLastIndex().longValue();
        boolean reverse = toIndex<fromIndex;
        if (reverse) {
            long tmp = fromIndex;
            fromIndex = toIndex;
            toIndex = tmp;
        }
        if (toIndex<0 || fromIndex>lastIndex) {
            return (Sequential<? extends Element>)empty_.get_();
        }
        fromIndex= Math.max(fromIndex, 0);
        toIndex = Math.min(toIndex, lastIndex);
        if (reverse) {
            Element[] sub = reversedCopy$priv$((Element[])array, 
                    Util.toInt(first+fromIndex), Util.toInt(toIndex-fromIndex+1));
            return backedBy$hidden(sub, 0, sub.length);
        }
        else {
            return backedBy$hidden((Element[])array, 
                    first+fromIndex, 
                    toIndex-fromIndex+1);
        }
    }
    
    @Annotations({
            @Annotation("shared"),
            @Annotation("actual")})
    @Override
    @TypeInfo("ceylon.language::Sequential<Element>")
    public final ceylon.language.Sequential<? extends Element> 
    spanTo(@Name("to")
    @TypeInfo("ceylon.language::Integer")
    final ceylon.language.Integer to) {
        return to.longValue() < 0 ? 
                (Sequential<? extends Element>)empty_.get_() : 
                span(Integer.instance(0), to);
    }
    
    @Annotations({
            @Annotation("shared"),
            @Annotation("actual")})
    @Override
    @TypeInfo("ceylon.language::Sequential<Element>")
    public final ceylon.language.Sequential<? extends Element> 
    spanFrom(@Name("from")
    @TypeInfo("ceylon.language::Integer")
    final ceylon.language.Integer from) {
        return span(from, Integer.instance(getSize()));
    }
    
    @Annotations({
            @Annotation("shared"),
            @Annotation("actual")})
    @Override
    @TypeInfo("ceylon.language::Tuple<Element,First,Rest>")
    public final Tuple<Element, ? extends First, ? extends Rest> $clone() {
        return this;
    }
    
    @Annotations({
            @Annotation("shared"),
            @Annotation("actual")})
    @Override
    @TypeInfo("ceylon.language::Iterator<Element>")
    public ceylon.language.Iterator<Element> iterator() {
        return new ArrayListIterator();
    }
    
    @Annotations({
            @Annotation("shared"),
            @Annotation("actual")})
    @Override
    @TypeInfo("ceylon.language::Boolean")
    public boolean contains(@Name("element")
    @TypeInfo("ceylon.language::Object")
    final java.lang.Object element) {
        for (int ii = 0; ii < length; ii++) {
            java.lang.Object x = array[first+ii];
            if (x!=null && element.equals(x)) return true;
        }
        return false;
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Annotations({
        @Annotation("shared"),
        @Annotation("actual")})
    @Override
    @TypeInfo("ceylon.language::Tuple<Element|Other,Other,ceylon.language::Tuple<Element,First,Rest>>")
    public final <Other>Tuple 
    withLeading(@Ignore TypeDescriptor $reifiedOther, @Name("element") Other e) {
        return new Tuple(
                $reifiedOther, 
                $reifiedOther, $reifiedOther, 
                e, this);
    }
    
    @Ignore
    private SoftReference<TypeDescriptor> $cachedType = null;
    
    @Override
    @Ignore
    public TypeDescriptor $getType$() {
        TypeDescriptor type = $cachedType != null ? 
        		$cachedType.get() : null;
        if (type == null) {
            type = $getType(0);
            $cachedType = new SoftReference<TypeDescriptor>(type);
        }
        return type;
    }
    
    @Ignore
    private TypeDescriptor $getType(int offset) {
        if (offset < getSize()) {
            return TypeDescriptor.klass(Tuple.class, 
                    $getUnionOfAllType(offset), 
                    $getElementType(offset), 
                    $getType(offset + 1));
        } else {
            return empty_.$TypeDescriptor$;
        }
    }
    
    @Ignore
    private TypeDescriptor $getUnionOfAllType(int offset) {
        TypeDescriptor[] types = 
        		new TypeDescriptor[Util.toInt(getSize() - offset)];
        for (int i = 0; i < getSize() - offset; i++) {
            types[i] = $getElementType(offset + i);
        }
        return TypeDescriptor.union(types);
    }
    
    @Ignore
    private TypeDescriptor $getElementType(int index) {
        return getTypeDescriptor(array[first + index]);
    }
    
    
    
    
    // The array length is the first element in the array
    @Ignore
    private static final long USE_ARRAY_SIZE = -10L;

    @Ignore
    private final Category$impl<java.lang.Object> 
    $ceylon$language$Category$this;
    @Ignore
    private final Iterable$impl<Element,java.lang.Object> 
    $ceylon$language$Iterable$this;
    @Ignore
    private final Collection$impl<Element> 
    $ceylon$language$Collection$this;
    @Ignore
    private final Correspondence$impl<Integer,Element> 
    $ceylon$language$Correspondence$this;
    @Ignore
    private final List$impl<Element> 
    $ceylon$language$List$this;
    @Ignore
    private final Sequential$impl<Element> 
    $ceylon$language$Sequential$this;
    @Ignore
    private final Sequence$impl<Element> 
    $ceylon$language$Sequence$this;

    /** 
     * A backing array. Maybe shared between many ArraySequence instances
     * (Flyweight pattern).
     */
    @Ignore
    final java.lang.Object[] array;
    /** The index into {@link #array} that holds the first element of this sequence */
    @Ignore
    final int first;
    /** The number of elements in {@link #array} that are in this sequence */
    @Ignore
    final int length;
    
    @Ignore
    private TypeDescriptor $reifiedElement;
    
    @Ignore
    @Override
    public Category$impl<java.lang.Object> 
    $ceylon$language$Category$impl(){
        return $ceylon$language$Category$this;
    }

    @Ignore
    @Override
    public Iterable$impl<Element,java.lang.Object> 
    $ceylon$language$Iterable$impl(){
        return $ceylon$language$Iterable$this;
    }

    @Ignore
    @Override
    public Collection$impl<Element> 
    $ceylon$language$Collection$impl(){
        return $ceylon$language$Collection$this;
    }

    @Ignore
    @Override
    public List$impl<Element> $ceylon$language$List$impl(){
        return $ceylon$language$List$this;
    }

    @Ignore
    @Override
    public Correspondence$impl<Integer,Element> 
    $ceylon$language$Correspondence$impl(){
        return $ceylon$language$Correspondence$this;
    }

    @Ignore
    @Override
    public Sequential$impl<Element> 
    $ceylon$language$Sequential$impl(){
        return $ceylon$language$Sequential$this;
    }

    @Ignore
    @Override
    public Sequence$impl<Element> 
    $ceylon$language$Sequence$impl(){
        return $ceylon$language$Sequence$this;
    }

    @Ignore
    @Override
    public boolean getEmpty() {
        return false;
    }

    /** 
     * A copy of the given elements of the given array, but in reversed order. 
     */
    private static <Element> Element[] 
            reversedCopy$priv$(Element[] array, int first, 
                    int length) {
        java.lang.Object[] reversed = new java.lang.Object[length];
        for (int i = 0; i < length; i++) {
            reversed[length-1-i] = array[first+i];
        }
        return (Element[])reversed;
    }
    
    @Ignore
    @Override
    public boolean defines(@Name("key") Integer key) {
        long ind = key.longValue();
        return ind>=0 && ind<length;
    }

    @Ignore
    private class ArrayListIterator 
            extends AbstractIterator<Element> {

        private ArrayListIterator() {
            super($getReifiedElement$());
        }

        private long idx = first;

        @Override
        public java.lang.Object next() {
            if (idx <= getLastIndex().longValue()+first) {
                return array[Util.toInt(idx++)];
            }
            else {
                return finished_.get_();
            }
        }

        @Override
        public java.lang.String toString() {
            return "ArrayArrayListIterator";
        }

    }

    @Override
    @Ignore
    public Sequence<Integer> getKeys() {
        return (Sequence)$ceylon$language$Sequence$this.getKeys();
    }

    @Override
    @Ignore
    public boolean 
    definesEvery(Iterable<? extends Integer, ?> keys) {
        return $ceylon$language$Correspondence$this.definesEvery(keys);
    }

    @Override
    @Ignore
    public boolean 
    definesAny(Iterable<? extends Integer, ?> keys) {
        return $ceylon$language$Correspondence$this.definesAny(keys);
    }
    
    @Override
    @Ignore
    public Sequential<? extends Element> 
    items(Iterable<? extends Integer,?> keys) {
        return $ceylon$language$Correspondence$this.items(keys);
    }

    @Override
    @Ignore
    public java.lang.String toString() {
        return $ceylon$language$Sequence$this.toString();
    }

    @Ignore
    @Override
    public boolean equals(java.lang.Object that) {
        return $ceylon$language$List$this.equals(that);
    }

    @Ignore
    @Override
    public int hashCode() {
        return $ceylon$language$List$this.hashCode();
    }

    @Ignore
    @Override
    public long count(
            @TypeInfo("ceylon.language::Callable<ceylon.language::Boolean,ceylon.language::Tuple<Element,Element,ceylon.language::Empty>>")
            @Name("selecting")@FunctionalParameter("(element)") Callable<? extends Boolean> f) {
        int count=0;
        for (int ii = 0; ii < length; ii++) {
            java.lang.Object x = array[first+ii];
            if (x!=null && f.$call$(x).booleanValue()) count++;
        }
        return count;
    }

    @Override
    @Ignore
    public boolean containsEvery(Iterable<?,?> elements) {
        return $ceylon$language$Category$this.containsEvery(elements);
    }

    @Override
    @Ignore
    public boolean containsAny(Iterable<?,?> elements) {
        return $ceylon$language$Category$this.containsAny(elements);
    }

    @Override
    @Ignore
    public Iterable<? extends Integer,?> inclusions(List<?> element) {
        return $ceylon$language$List$this.inclusions(element);
    }

    @Override
    @Ignore
    public Integer firstInclusion(List<?> element) {
        return $ceylon$language$List$this.firstInclusion(element);
    }

    @Override
    @Ignore
    public Integer lastInclusion(List<?> element) {
        return $ceylon$language$List$this.lastInclusion(element);
    }

    @Override
    @Ignore
    public Iterable<? extends Integer,?> occurrences(java.lang.Object element) {
        return $ceylon$language$List$this.occurrences(element);
    }

    @Override
    @Ignore
    public Integer firstOccurrence(java.lang.Object element) {
        return $ceylon$language$List$this.firstOccurrence(element);
    }

    @Override
    @Ignore
    public Integer lastOccurrence(java.lang.Object element) {
        return $ceylon$language$List$this.lastOccurrence(element);
    }

    @Override
    @Ignore
    public boolean occurs(java.lang.Object element) {
        return $ceylon$language$List$this.occurs(element);
    }
    
    @Override
    @Ignore
    public boolean occursAt(long index, java.lang.Object element) {
        return $ceylon$language$List$this.occursAt(index, element);
    }

    @Override
    @Ignore
    public boolean includesAt(long index, List<?> element) {
        return $ceylon$language$List$this.includesAt(index, element);
    }
        
    @Override
    @Ignore
    public boolean includes(List<?> element) {
        return $ceylon$language$List$this.includes(element);
    }
        
    @Override
    @Ignore
    public boolean startsWith(List<?> element) {
        return $ceylon$language$List$this.startsWith(element);
    }
        
    @Override
    @Ignore
    public boolean endsWith(List<?> element) {
        return $ceylon$language$List$this.endsWith(element);
    }
    
    @Override
    @Ignore
    public Sequence<? extends Element> sequence() {
        return $ceylon$language$Sequence$this.sequence();
    }

    @Override @Ignore
    public Element find(Callable<? extends Boolean> f) {
        return $ceylon$language$Iterable$this.find(f);
    }
    @Override @Ignore
    public Element findLast(Callable<? extends Boolean> f) {
        return $ceylon$language$Iterable$this.findLast(f);
    }
    @Override
    @Ignore
    public Sequence<? extends Element> sort(Callable<? extends Comparison> f) {
        return $ceylon$language$Sequence$this.sort(f);
    }

    @Override
    @Ignore
    public <Result> Iterable<? extends Result, ?> 
    map(@Ignore TypeDescriptor $reifiedResult, Callable<? extends Result> f) {
        return $ceylon$language$Iterable$this.map($reifiedResult, f);
    }
    @Override
    @Ignore
    public Iterable<? extends Element, ?> 
    filter(Callable<? extends Boolean> f) {
        return $ceylon$language$Iterable$this.filter(f);
    }
    @Override
    @Ignore
    public Iterable<? extends Integer, ?> 
    indexesWhere(Callable<? extends Boolean> f) {
        return $ceylon$language$List$this.indexesWhere(f);
    }
    @Override
    @Ignore
    public Integer 
    firstIndexWhere(Callable<? extends Boolean> f) {
        return $ceylon$language$List$this.firstIndexWhere(f);
    }
    @Override
    @Ignore
    public Integer 
    lastIndexWhere(Callable<? extends Boolean> f) {
        return $ceylon$language$List$this.lastIndexWhere(f);
    }
    @Override
    @Ignore
    public <Result> Sequence<? extends Result> 
    collect(@Ignore TypeDescriptor $reifiedResult, 
            Callable<? extends Result> f) {
        return $ceylon$language$Sequence$this.collect($reifiedResult, f);
    }

    @Override
    @Ignore
    public Sequential<? extends Element> 
    select(Callable<? extends Boolean> f) {
        return $ceylon$language$Iterable$this.select(f);
    }

    @Override
    @Ignore
    public <Result> Result 
    fold(@Ignore TypeDescriptor $reifiedResult, 
            Result ini, Callable<? extends Result> f) {
        return $ceylon$language$Iterable$this.fold($reifiedResult, 
                ini, f);
    }
    
    @Override
    @Ignore
    public <Result> java.lang.Object 
    reduce(@Ignore TypeDescriptor $reifiedResult, 
            Callable<? extends Result> f) {
        return $ceylon$language$Iterable$this.reduce($reifiedResult, f);
    }
    
    @Override @Ignore
    public boolean any(Callable<? extends Boolean> f) {
        return $ceylon$language$Iterable$this.any(f);
    }
    @Override @Ignore
    public boolean every(Callable<? extends Boolean> f) {
        return $ceylon$language$Iterable$this.every(f);
    }
    @Override @Ignore
    public boolean longerThan(long length) {
        return this.length>length;
    }
    @Override @Ignore
    public boolean shorterThan(long length) {
        return this.length<length;
    }
    @Override @Ignore
    public Iterable<? extends Element, ?> 
    skip(long skip) {
        return $ceylon$language$Iterable$this.skip(skip);
    }
    @Override @Ignore
    public Iterable<? extends Element, ?> 
    take(long take) {
        return $ceylon$language$Iterable$this.take(take);
    }
    @Override @Ignore
    public Iterable<? extends Element, ?> 
    by(long step) {
        return $ceylon$language$Iterable$this.by(step);
    }
    @Override @Ignore
    public Iterable<? extends Element, ?> 
    getCoalesced() {
        return $ceylon$language$Iterable$this.getCoalesced();
    }
    @Override @Ignore
    public Iterable<? extends Entry<? extends Integer, ? extends Element>, 
            ?> 
    getIndexed() {
        return $ceylon$language$Iterable$this.getIndexed();
    }
    @SuppressWarnings("rawtypes")
    @Override @Ignore 
    public <Other,Absent>Iterable 
    chain(@Ignore TypeDescriptor $reifiedOther, 
            @Ignore TypeDescriptor $reifiedOtherAbsent, 
            Iterable<? extends Other, ? extends Absent> other) {
        return $ceylon$language$Iterable$this.chain($reifiedOther, 
                $reifiedOtherAbsent, other);
    }
    @Override @Ignore 
    public <Other> Tuple<java.lang.Object,? extends Other,? extends Sequence<? extends Element>> 
    following(@Ignore TypeDescriptor $reifiedOther, Other other) {
        return $ceylon$language$Sequence$this.following($reifiedOther, 
                other);
    }
    @Override @Ignore
    public <Default>Iterable<?,?> 
    defaultNullElements(@Ignore TypeDescriptor $reifiedDefault, 
            Default defaultValue) {
        return $ceylon$language$Iterable$this.defaultNullElements($reifiedDefault, 
                defaultValue);
    }
    @Override
    @Ignore
    @SuppressWarnings("rawtypes")
    public <Other>Sequence 
    withTrailing(@Ignore TypeDescriptor $reifiedOther, Other e) {
        return $ceylon$language$List$this.withTrailing($reifiedOther, e);
    }
    
    @Override @Ignore
    public Sequential<? extends Element> 
    trim(Callable<? extends Boolean> characters) {
        return $ceylon$language$Sequential$this.trim(characters);
    }

    @Override @Ignore
    public Sequential<? extends Element> 
    trimLeading(Callable<? extends Boolean> characters) {
        return $ceylon$language$Sequential$this.trimLeading(characters);
    }

    @Override @Ignore
    public Sequential<? extends Element> 
    trimTrailing(Callable<? extends Boolean> characters) {
        return $ceylon$language$Sequential$this.trimTrailing(characters);
    }
    
    @Override @Ignore
    public Sequential<? extends Element> 
    initial(long length) {
        return $ceylon$language$Sequential$this.initial(length);
    }
    
    @Override @Ignore
    public Sequential<? extends Element> terminal(long length) {
        return $ceylon$language$Sequential$this.terminal(length);
    }
    
    @Override @Ignore
    public Iterable<? extends Element, ?> 
    takeWhile(Callable<? extends Boolean> take) {
        return $ceylon$language$Iterable$this.takeWhile(take);
    }
    
    @Override @Ignore
    public Iterable<? extends Element, ?> 
    skipWhile(Callable<? extends Boolean> skip) {
        return $ceylon$language$Iterable$this.skipWhile(skip);
    }
    
    @Override
    @Ignore
    public Iterable<? extends Element,?> getCycled() {
        return $ceylon$language$Iterable$this.getCycled();
    }

    @Override
    @Ignore
    public Iterable<? extends Element,?> cycle(long times) {
        return $ceylon$language$Iterable$this.cycle(times);
    }
    
    @Override
    @Ignore
    public Sequential<? extends Element> repeat(long times) {
        return $ceylon$language$Sequential$this.repeat(times);
    }
    
    /** Gets the underlying array. Used for iteration using a C-style for */
    @Ignore
    public java.lang.Object[] $getArray$() {
        return array;
    }
    
    /** Gets the underlying first index. Used for iteration using a C-style for */
    @Ignore
    public int $getFirst$() {
        return first;
    }
    
    /** Gets the underlying length. Used for iteration using a C-style for */
    @Ignore
    public int $getLength$() {
        return length;
    }

    @Override @Ignore
    public final <Result,Args extends Sequential<? extends java.lang.Object>> Callable<? extends Iterable<? extends Result, ?>>
    spread(TypeDescriptor $reifiedResult,TypeDescriptor $reifiedArgs, Callable<? extends Callable<? extends Result>> method) {
    	return $ceylon$language$Iterable$this.spread($reifiedResult, $reifiedArgs, method);
    }
    
}