package ceylon.language;

import static com.redhat.ceylon.compiler.java.runtime.metamodel.Metamodel.getTypeDescriptor;

import java.lang.ref.SoftReference;
import java.util.Arrays;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.metadata.Annotation;
import com.redhat.ceylon.compiler.java.metadata.Annotations;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
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
        extends ArraySequence<Element>
        implements ReifiedType, Sequence<Element> {

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
        super($reifiedElement, elements, 0, elements.length, false);
    }
    
    @Ignore
    public Tuple(TypeDescriptor $reifiedElement, 
    		java.lang.Object[] elements, 
    		Sequential<?> tail) {
        this($reifiedElement, makeArray(elements, tail));
    }
    
    @Ignore
    private Tuple(TypeDescriptor $reifiedElement, 
    		java.lang.Object[] array, 
    		long first, long length, boolean copy) {
        super($reifiedElement, array, first, length, copy);
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
    @Override
    protected Tuple<Element, ? extends First, ? extends Rest> 
    backedBy$hidden(Element[] array, long first, long length) {
        return new Tuple<Element, First, Rest>($getReifiedElement$(), 
        		array, first, length, false);
    }
    
    @Ignore
    @Override
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
        return (First)super.getFirst();
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
    public final long getSize() {
        return super.getSize();
    }
    
    @Annotations({
            @Annotation("shared"),
            @Annotation("actual")})
    @Override
    @TypeInfo("ceylon.language::Null|Element")
    public final Element get(@Name("index")
    @TypeInfo("ceylon.language::Integer")
    final ceylon.language.Integer index) {
        return super.get(index);
    }
    
    @Annotations({
            @Annotation("shared"),
            @Annotation("actual")})
    @Override
    @TypeInfo("ceylon.language::Integer")
    @Transient
    public final ceylon.language.Integer getLastIndex() {
        return super.getLastIndex();
    }
    
    @Annotations({
            @Annotation("shared"),
            @Annotation("actual")})
    @Override
    @TypeInfo("Element")
    @Transient
    public final Element getLast() {
        return super.getLast();
    }
    
    @Annotations({
            @Annotation("shared"),
            @Annotation("actual")})
    @Override
    @TypeInfo("ceylon.language::Sequence<Element>")
    public final ceylon.language.Sequence<? extends Element> 
    getReversed() {
        return super.getReversed();
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
    final long length) {
        return super.segment(from, length);
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
        return super.span(from, end);
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
        return super.spanTo(to);
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
        return super.spanFrom(from);
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
        return super.iterator();
    }
    
    @Annotations({
            @Annotation("shared"),
            @Annotation("actual")})
    @Override
    @TypeInfo("ceylon.language::Boolean")
    public boolean contains(@Name("element")
    @TypeInfo("ceylon.language::Object")
    final java.lang.Object element) {
        return super.contains(element);
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

}