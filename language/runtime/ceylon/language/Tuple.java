package ceylon.language;

import java.lang.ref.SoftReference;
import java.util.Arrays;

import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@com.redhat.ceylon.compiler.java.metadata.Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class(extendsType = "ceylon.language::Object")
@com.redhat.ceylon.compiler.java.metadata.Annotations({
        @com.redhat.ceylon.compiler.java.metadata.Annotation(
                value = "doc",
                arguments = {"A _tuple_ is a typed linked list. Each instance of \n`Tuple` represents the value and type of a single link.\nThe attributes `first` and `rest` allow us to retrieve\na value form the list without losing its static type \ninformation.\n\n    value point = Tuple(0.0, Tuple(0.0, Tuple(\"origin\")));\n    Float x = point.first;\n    Float y = point.rest.first;\n    String label = point.rest.rest.first;\n\nUsually, we abbreviate code involving tuples.\n\n    [Float,Float,String] point = [0.0, 0.0, \"origin\"];\n    Float x = point[0];\n    Float y = point[1];\n    String label = point[2];\n\nA list of types enclosed in brackets is an abbreviated \ntuple type. An instance of `Tuple` may be constructed \nby surrounding a value list in brackets:\n\n    [String,String] words = [\"hello\", \"world\"];\n\nThe index operator with a literal integer argument is a \nshortcut for a chain of evaluations of `rest` and \n`first`. For example, `point[1]` means `point.rest.first`.\n\nA _terminated_ tuple type is a tuple where the type of\nthe last link in the chain is `Empty`. An _unterminated_ \ntuple type is a tuple where the type of the last link\nin the chain is `Sequence` or `Sequential`. Thus, a \nterminated tuple type has a length that is known\nstatically. For an unterminated tuple type only a lower\nbound on its length is known statically.\n\nHere, `point` is an unterminated tuple:\n\n    String[] labels = ... ;\n    [Float,Float,String*] point = [0.0, 0.0, *labels];\n    Float x = point[0];\n    Float y = point[1];\n    String? firstLabel = point[2];\n    String[] allLabels = point[2...];"}),
        @com.redhat.ceylon.compiler.java.metadata.Annotation(
                value = "by",
                arguments = {"Gavin"}),
        @com.redhat.ceylon.compiler.java.metadata.Annotation("shared"),
        @com.redhat.ceylon.compiler.java.metadata.Annotation("final")})
@com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes({
        "ceylon.language::Sequence<Element>",
        "ceylon.language::Cloneable<ceylon.language::Tuple<Element,First,Rest>>"})
@com.redhat.ceylon.compiler.java.metadata.TypeParameters({
        @com.redhat.ceylon.compiler.java.metadata.TypeParameter(
                value = "Element",
                variance = com.redhat.ceylon.compiler.java.metadata.Variance.OUT,
                satisfies = {},
                caseTypes = {}),
        @com.redhat.ceylon.compiler.java.metadata.TypeParameter(
                value = "First",
                variance = com.redhat.ceylon.compiler.java.metadata.Variance.OUT,
                satisfies = {"Element"},
                caseTypes = {}),
        @com.redhat.ceylon.compiler.java.metadata.TypeParameter(
                value = "Rest",
                variance = com.redhat.ceylon.compiler.java.metadata.Variance.OUT,
                satisfies = {"ceylon.language::Sequential<Element>"},
                caseTypes = {},
                defaultValue = "ceylon.language::Empty")})
public final class Tuple<Element, First extends Element, Rest extends ceylon.language.Sequential<? extends Element>>
        extends ceylon.language.ArraySequence<Element>
        implements com.redhat.ceylon.compiler.java.runtime.model.ReifiedType,
        ceylon.language.Sequence<Element> {

    public Tuple(@com.redhat.ceylon.compiler.java.metadata.Ignore
    final com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor $reifiedElement, @com.redhat.ceylon.compiler.java.metadata.Ignore
    final com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor $reifiedFirst, @com.redhat.ceylon.compiler.java.metadata.Ignore
    final com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor $reifiedRest, @com.redhat.ceylon.compiler.java.metadata.Name("first")
    @com.redhat.ceylon.compiler.java.metadata.TypeInfo("First")
    final First first, @com.redhat.ceylon.compiler.java.metadata.Name("rest")
    @com.redhat.ceylon.compiler.java.metadata.TypeInfo("Rest")
    final Rest rest) {
        this($reifiedElement, makeArray(first, rest));
    }
    
    @Ignore
    public Tuple(TypeDescriptor $reifiedElement, java.lang.Object[] elements) {
        super($reifiedElement, elements, 0, elements.length, false);
    }
    
    @Ignore
    public Tuple(TypeDescriptor $reifiedElement, java.lang.Object[] elements, ceylon.language.Sequential tail) {
        this($reifiedElement, makeArray(elements, tail));
    }
    
    @Ignore
    private Tuple(TypeDescriptor $reifiedElement, java.lang.Object[] array, long first, long length, boolean copy) {
        super($reifiedElement, array, first, length, copy);
    }
    
    private static java.lang.Object[] makeArray(java.lang.Object first, ceylon.language.Sequential rest) {
        java.lang.Object[] elements = new java.lang.Object[(int)rest.getSize() + 1];
        elements[0] = first;
        copyToArray(rest, elements, 1);
        return elements;
    }
    
    private static java.lang.Object[] makeArray(java.lang.Object[] array, ceylon.language.Sequential tail) {
        java.lang.Object[] elements = Arrays.copyOf(array, array.length + (int)tail.getSize());
        copyToArray(tail, elements, array.length);
        return elements;
    }
    
    private static void copyToArray(ceylon.language.Sequential seq, java.lang.Object[] array, int offset) {
        ceylon.language.Iterator iter = seq.iterator();
        int i = offset;
        for (java.lang.Object elem = iter.next(); elem != ceylon.language.finished_.get_(); elem = iter.next()) {
            array[i++] = elem;
        }
    }
    
    @Ignore
    @java.lang.Override
    protected Tuple<Element, ? extends First, ? extends Rest> backedBy$hidden(Element[] array, long first, long length) {
        return new Tuple<Element, First, Rest>($reifiedElement, array, first, length, false);
    }
    
    @com.redhat.ceylon.compiler.java.metadata.Annotations({
            @com.redhat.ceylon.compiler.java.metadata.Annotation("shared"),
            @com.redhat.ceylon.compiler.java.metadata.Annotation("actual")})
    @java.lang.Override
    @com.redhat.ceylon.compiler.java.metadata.TypeInfo("First")
    public final First getFirst() {
        return (First)super.getFirst();
    }
    
    @com.redhat.ceylon.compiler.java.metadata.Annotations({
            @com.redhat.ceylon.compiler.java.metadata.Annotation("shared"),
            @com.redhat.ceylon.compiler.java.metadata.Annotation("actual")})
    @java.lang.Override
    @com.redhat.ceylon.compiler.java.metadata.TypeInfo("Rest")
    public final Rest getRest() {
        return (Rest)super.getRest();
    }
    
    @com.redhat.ceylon.compiler.java.metadata.Annotations({
            @com.redhat.ceylon.compiler.java.metadata.Annotation("shared"),
            @com.redhat.ceylon.compiler.java.metadata.Annotation("actual")})
    @java.lang.Override
    @com.redhat.ceylon.compiler.java.metadata.TypeInfo("ceylon.language::Integer")
    public final long getSize() {
        return super.getSize();
    }
    
    @com.redhat.ceylon.compiler.java.metadata.Annotations({
            @com.redhat.ceylon.compiler.java.metadata.Annotation("shared"),
            @com.redhat.ceylon.compiler.java.metadata.Annotation("actual")})
    @java.lang.Override
    @com.redhat.ceylon.compiler.java.metadata.TypeInfo("ceylon.language::Null|Element")
    public final Element get(@com.redhat.ceylon.compiler.java.metadata.Name("index")
    @com.redhat.ceylon.compiler.java.metadata.TypeInfo("ceylon.language::Integer")
    final ceylon.language.Integer index) {
        return super.get(index);
    }
    
    @com.redhat.ceylon.compiler.java.metadata.Annotations({
            @com.redhat.ceylon.compiler.java.metadata.Annotation("shared"),
            @com.redhat.ceylon.compiler.java.metadata.Annotation("actual")})
    @java.lang.Override
    @com.redhat.ceylon.compiler.java.metadata.TypeInfo("ceylon.language::Integer")
    public final ceylon.language.Integer getLastIndex() {
        return super.getLastIndex();
    }
    
    @com.redhat.ceylon.compiler.java.metadata.Annotations({
            @com.redhat.ceylon.compiler.java.metadata.Annotation("shared"),
            @com.redhat.ceylon.compiler.java.metadata.Annotation("actual")})
    @java.lang.Override
    @com.redhat.ceylon.compiler.java.metadata.TypeInfo("Element")
    public final Element getLast() {
        return super.getLast();
    }
    
    @com.redhat.ceylon.compiler.java.metadata.Annotations({
            @com.redhat.ceylon.compiler.java.metadata.Annotation("shared"),
            @com.redhat.ceylon.compiler.java.metadata.Annotation("actual")})
    @java.lang.Override
    @com.redhat.ceylon.compiler.java.metadata.TypeInfo("ceylon.language::Sequence<Element>")
    public final ceylon.language.Sequence<? extends Element> getReversed() {
        return super.getReversed();
    }
    
    @com.redhat.ceylon.compiler.java.metadata.Annotations({
            @com.redhat.ceylon.compiler.java.metadata.Annotation("shared"),
            @com.redhat.ceylon.compiler.java.metadata.Annotation("actual")})
    @java.lang.Override
    @com.redhat.ceylon.compiler.java.metadata.TypeInfo("ceylon.language::Sequential<Element>")
    public final ceylon.language.Sequential<? extends Element> segment(@com.redhat.ceylon.compiler.java.metadata.Name("from")
    @com.redhat.ceylon.compiler.java.metadata.TypeInfo("ceylon.language::Integer")
    final ceylon.language.Integer from, @com.redhat.ceylon.compiler.java.metadata.Name("length")
    @com.redhat.ceylon.compiler.java.metadata.TypeInfo("ceylon.language::Integer")
    final long length) {
        return super.segment(from, length);
    }
    
    @com.redhat.ceylon.compiler.java.metadata.Annotations({
            @com.redhat.ceylon.compiler.java.metadata.Annotation("shared"),
            @com.redhat.ceylon.compiler.java.metadata.Annotation("actual")})
    @java.lang.Override
    @com.redhat.ceylon.compiler.java.metadata.TypeInfo("ceylon.language::Sequential<Element>")
    public final ceylon.language.Sequential<? extends Element> span(@com.redhat.ceylon.compiler.java.metadata.Name("from")
    @com.redhat.ceylon.compiler.java.metadata.TypeInfo("ceylon.language::Integer")
    final ceylon.language.Integer from, @com.redhat.ceylon.compiler.java.metadata.Name("end")
    @com.redhat.ceylon.compiler.java.metadata.TypeInfo("ceylon.language::Integer")
    final ceylon.language.Integer end) {
        return super.span(from, end);
    }
    
    @com.redhat.ceylon.compiler.java.metadata.Annotations({
            @com.redhat.ceylon.compiler.java.metadata.Annotation("shared"),
            @com.redhat.ceylon.compiler.java.metadata.Annotation("actual")})
    @java.lang.Override
    @com.redhat.ceylon.compiler.java.metadata.TypeInfo("ceylon.language::Sequential<Element>")
    public final ceylon.language.Sequential<? extends Element> spanTo(@com.redhat.ceylon.compiler.java.metadata.Name("to")
    @com.redhat.ceylon.compiler.java.metadata.TypeInfo("ceylon.language::Integer")
    final ceylon.language.Integer to) {
        return super.spanTo(to);
    }
    
    @com.redhat.ceylon.compiler.java.metadata.Annotations({
            @com.redhat.ceylon.compiler.java.metadata.Annotation("shared"),
            @com.redhat.ceylon.compiler.java.metadata.Annotation("actual")})
    @java.lang.Override
    @com.redhat.ceylon.compiler.java.metadata.TypeInfo("ceylon.language::Sequential<Element>")
    public final ceylon.language.Sequential<? extends Element> spanFrom(@com.redhat.ceylon.compiler.java.metadata.Name("from")
    @com.redhat.ceylon.compiler.java.metadata.TypeInfo("ceylon.language::Integer")
    final ceylon.language.Integer from) {
        return super.spanFrom(from);
    }
    
    @com.redhat.ceylon.compiler.java.metadata.Annotations({
            @com.redhat.ceylon.compiler.java.metadata.Annotation("shared"),
            @com.redhat.ceylon.compiler.java.metadata.Annotation("actual")})
    @java.lang.Override
    @com.redhat.ceylon.compiler.java.metadata.TypeInfo("ceylon.language::Tuple<Element,First,Rest>")
    public final Tuple<Element, ? extends First, ? extends Rest> getClone() {
        return (Tuple<Element, ? extends First, ? extends Rest>) super.getClone();
    }
    
    @com.redhat.ceylon.compiler.java.metadata.Annotations({
            @com.redhat.ceylon.compiler.java.metadata.Annotation("shared"),
            @com.redhat.ceylon.compiler.java.metadata.Annotation("actual"),
            @com.redhat.ceylon.compiler.java.metadata.Annotation("default")})
    @java.lang.Override
    @com.redhat.ceylon.compiler.java.metadata.TypeInfo("ceylon.language::Iterator<Element>")
    public ceylon.language.Iterator<Element> iterator() {
        return super.iterator();
    }
    
    @com.redhat.ceylon.compiler.java.metadata.Annotations({
            @com.redhat.ceylon.compiler.java.metadata.Annotation("shared"),
            @com.redhat.ceylon.compiler.java.metadata.Annotation("actual"),
            @com.redhat.ceylon.compiler.java.metadata.Annotation("default")})
    @java.lang.Override
    @com.redhat.ceylon.compiler.java.metadata.TypeInfo("ceylon.language::Boolean")
    public boolean contains(@com.redhat.ceylon.compiler.java.metadata.Name("element")
    @com.redhat.ceylon.compiler.java.metadata.TypeInfo("ceylon.language::Object")
    final java.lang.Object element) {
        return super.contains(element);
    }
    
    @com.redhat.ceylon.compiler.java.metadata.Ignore
    private SoftReference<TypeDescriptor> $cachedType = null;
    
    @java.lang.Override
    @com.redhat.ceylon.compiler.java.metadata.Ignore
    public com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor $getType() {
        TypeDescriptor type = ($cachedType != null) ? $cachedType.get() : null;
        if (type == null) {
            type = $getType(0);
            $cachedType = new SoftReference<TypeDescriptor>(type);
        }
        return type;
    }
    
    @com.redhat.ceylon.compiler.java.metadata.Ignore
    private TypeDescriptor $getType(int offset) {
        if (offset < getSize()) {
            return TypeDescriptor.klass(Tuple.class, $getUnionOfAllType(offset), $getElementType(offset), $getType(offset + 1));
        } else {
            return empty_.$TypeDescriptor;
        }
    }
    
    @com.redhat.ceylon.compiler.java.metadata.Ignore
    private TypeDescriptor $getUnionOfAllType(int offset) {
        TypeDescriptor[] types = new TypeDescriptor[(int)getSize() - offset];
        for (int i = 0; i < getSize() - offset; i++) {
            types[i] = $getElementType(offset + i);
        }
        return TypeDescriptor.union(types);
    }
    
    @com.redhat.ceylon.compiler.java.metadata.Ignore
    private TypeDescriptor $getElementType(int index) {
        ReifiedType element = (ReifiedType)array[first + index];
        return (element != null) ? element.$getType() : Null.$TypeDescriptor;
    }
}