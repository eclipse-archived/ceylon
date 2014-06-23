package ceylon.language;

import static com.redhat.ceylon.compiler.java.Util.toInt;
import static com.redhat.ceylon.compiler.java.runtime.metamodel.Metamodel.getTypeDescriptor;
import static java.lang.System.arraycopy;

import java.lang.ref.SoftReference;
import java.util.Arrays;

import ceylon.language.impl.BaseIterator;
import ceylon.language.impl.BaseSequence;

import com.redhat.ceylon.compiler.java.Util;
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
import com.redhat.ceylon.compiler.java.runtime.metamodel.Metamodel;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 7)
@Class(extendsType = "ceylon.language::Object", 
       basic = false, identifiable = false)
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
        extends BaseSequence<Element>
        implements Sequence<Element> {

    /** 
     * A backing array. May be shared between many Tuple instances
     * (Flyweight pattern).
     */
    @Ignore
    final java.lang.Object[] array;
    
    @Ignore
    private TypeDescriptor $reifiedElement;
    
    /** 
     * The rest of the elements, after those in the array.
     * This should never be another Tuple instance though.
     */
    private Sequential rest;

    /**
     * The ultimate constructor
     * @param $reifiedElement
     * @param array A backing array
     * @param first index into the {@code array} of the first element of this tuple
     * @param length number of elements in the {@code array} that are part of this tuple
     * @param rest index into the {@code array} of the first element of the {@code rest} of this tuple
     * @param copy whether to copy the array of not.
     */
    @Ignore
    public Tuple(@Ignore TypeDescriptor $reifiedElement, 
            java.lang.Object[] array, Sequential rest, boolean copy) {
        super($reifiedElement);
        int length = array.length;
        if (array.length==0 || 
                length == 0 ||
                array.length <= 0) {
            throw new AssertionError("Tuple may not have zero elements");
        }
        if (length > array.length) {
            throw new AssertionError("Overflow :" + 
                    (length) + " > " + array.length);
        }
        this.$reifiedElement = $reifiedElement;
        if (copy) {
            this.array = (Element[])Arrays.copyOfRange(array, 
                  0, Util.toInt(length));
            
        } else {
            this.array = (Element[])array;
        }
        this.rest = rest;
        
        if (this.array == null) {
            throw new AssertionError("");
        }
        if (this.array.length == 0) {
            throw new AssertionError("");
        }
        if (this.rest == null) {
            throw new AssertionError("");
        }
    }
    
    /**
     * The Ceylon initializer constructor
     */
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
        this($reifiedElement, makeArray(first, rest), makeRest(rest));
    }
    // where
    @Ignore
    private Tuple(TypeDescriptor $reifiedElement, java.lang.Object[] array,
            Sequential<?> rest) {
        this($reifiedElement, array, rest, false);
    }
    private static java.lang.Object[] makeArray(java.lang.Object first,
            Sequential<?> rest) {
        final java.lang.Object[] array;
        if (rest instanceof Tuple) {
            Tuple other = (Tuple)rest;
            array = new java.lang.Object[1 + other.array.length];
            array[0] = first;
            System.arraycopy(other.array, 0, array, 1, other.array.length);
            rest = other.rest;
        } else {
            array = new java.lang.Object[]{first};
        }
        return array;
    }
    private static Sequential<?> makeRest(Sequential<?> rest) {
        if (rest instanceof Tuple) {
            return ((Tuple)rest).rest;
        }
        return rest;
    }
    
    @Ignore
    public Tuple(TypeDescriptor $reifiedElement, 
            java.lang.Object[] elements) {
        this($reifiedElement, elements, empty_.get_(), false);
    }
    

    /**
     * The constructor used by the transformation of {@code [foo, bar *baz]}
     */
    @Ignore
    public static Tuple instance(TypeDescriptor $reifiedElement, 
            java.lang.Object[] elements, 
            Sequential<?> tail) {
        Sequential<?> rest;
        java.lang.Object[] array;
        if (tail instanceof Tuple) {
            Tuple<?,?,?> other = (Tuple<?,?,?>)tail;
            array = new java.lang.Object[elements.length + other.array.length];
            System.arraycopy(elements, 0, array, 0, elements.length);
            System.arraycopy(other.array, 0, array, elements.length, other.array.length);
            rest = other.rest;
        } else {
            array = elements;
            rest = tail;
        }
        return new Tuple($reifiedElement, array, rest, false);
    }
    
    public static Tuple instance(TypeDescriptor $reifiedElement, 
            java.lang.Object[] elements) {
        return instance($reifiedElement, elements, empty_.get_());
    }
    
    @Ignore
    protected TypeDescriptor $getReifiedElement$() {
        return ((TypeDescriptor.Class)$getType$()).getTypeArguments()[0];
    }
    
    @Annotations({
            @Annotation("shared"),
            @Annotation("actual")})
    @Override
    @TypeInfo("First")
    @SuppressWarnings("unchecked")
    public final First getFirst() {
        return (First)array[0];
    }
    
    @Annotations({
            @Annotation("shared"),
            @Annotation("actual")})
    @Override
    @TypeInfo("Rest")
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Rest getRest() {
        if (getSize()==1) {
            return (Rest)empty_.get_();
        }
        else {
            
            return (Rest) new Tuple(((TypeDescriptor.Class)((TypeDescriptor.Class)$getType$()).getTypeArguments()[2]).getTypeArguments()[0],
                    Arrays.copyOfRange(this.array, 1, this.array.length), rest, false);
        }
    }

    @Annotations({
            @Annotation("shared"),
            @Annotation("actual")})
    @Override
    @TypeInfo("ceylon.language::Integer")
    @Transient
    public final long getSize() {
        return array.length + rest.getSize();
    }
    
    @Annotations({
            @Annotation("shared"),
            @Annotation("actual")})
    @Override
    @TypeInfo("ceylon.language::Null|Element")
    public final Element getFromFirst(@Name("index")
    @TypeInfo("ceylon.language::Integer")
    final long index) {
        if (index < 0) {
            return null;
        } else if (index >= array.length) {
            return (Element)rest.getFromFirst(index-array.length);
        }
        else {
            return (Element)array[toInt(index)];
        }
    }
    
    @Ignore
    @Override
    public Element getFromLast(long index) {
        if (index < 0) {
            return null;
        } else if (index >= array.length) {
            return (Element)rest.getFromLast(index - this.array.length);
        }
        return (Element)array[array.length-1-toInt(index)];
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
        return Integer.instance(array.length + rest.getSize() - 1);
    }
    
    @Annotations({
            @Annotation("shared"),
            @Annotation("actual")})
    @Override
    @TypeInfo("Element")
    @Transient
    public final Element getLast() {
        if (!rest.getEmpty()) {
            return (Element)rest.getLast();
        }
        return (Element)array[array.length - 1];
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
        final long lastIndex = getSize()-1;
        
        if (fromIndex > lastIndex || length <= 0) {
            return (Sequential<? extends Element>)empty_.get_();
        }
        long l;
        if (length > lastIndex-fromIndex) {
            l = lastIndex-fromIndex+1;
        } else {
            l = length;
        }
        return new Tuple($reifiedElement, Arrays.copyOfRange(this.array, toInt(fromIndex), toInt(fromIndex+l)), empty_.get_(), false);
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
        long lastIndex = getSize()-1;
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
            int fromInt = Util.toInt(fromIndex);
            int toInt = Util.toInt(toIndex);
            java.lang.Object[] reversed = new java.lang.Object[toInt-fromInt+1];
            for (int ii = toInt, jj = 0; ii >= fromIndex; ii--, jj++) {
                reversed[jj] = getFromFirst(ii);
            }
            return new Tuple($reifiedElement, reversed, empty_.get_(), false);
        }
        else {
            return new Tuple<Element, First, Rest>($getReifiedElement$(), 
                    Arrays.copyOfRange(this.array, toInt(fromIndex), toInt(toIndex)+1), empty_.get_(), false);
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
        return new TupleIterator();
    }
    
    @Annotations({
            @Annotation("shared"),
            @Annotation("actual")})
    @Override
    @TypeInfo("ceylon.language::Boolean")
    public boolean contains(@Name("element")
    @TypeInfo("ceylon.language::Object")
    final java.lang.Object element) {
        for (int ii = 0; ii < array.length; ii++) {
            java.lang.Object x = array[ii];
            if (x!=null && element.equals(x)) return true;
        }
        return rest.contains(element);
    }
    
    @Ignore
    private SoftReference<TypeDescriptor> $cachedType = null;
    
    @Override
    @Ignore
    public TypeDescriptor $getType$() {
        TypeDescriptor type = $cachedType != null ? 
                $cachedType.get() : null;
        if (type == null) {
            TypeDescriptor restType = getTypeDescriptor(rest);
            TypeDescriptor elementType = 
                    Metamodel.getIteratedTypeDescriptor(restType);
            for (int ii = array.length-1; ii >= 0; ii--) {
                TypeDescriptor elemType = $getElementType(ii);
                elementType = TypeDescriptor.union(elementType, elemType);
                restType = TypeDescriptor.klass(Tuple.class, 
                        elementType, elemType, restType);
            }
            type = restType;
            $cachedType = new SoftReference<TypeDescriptor>(type);
        }
        return type;
    }
    
    /*
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
    */
    
    @Ignore
    private TypeDescriptor $getElementType(int index) {
        return getTypeDescriptor(array[index]);
    }
    
    // The array length is the first element in the array
    @Ignore
    private static final long USE_ARRAY_SIZE = -10L;
    
    @Ignore
    @Override
    public boolean defines(@Name("key") Integer key) {
        long ind = key.longValue();
        return ind>=0 && ind<array.length || rest.defines(Integer.instance(ind-this.array.length));
    }

    @Ignore
    private class TupleIterator 
            extends BaseIterator<Element> {
        
        private TupleIterator() {
            super($getReifiedElement$());
        }
        
        private long idx = 0;
        
        private Iterator<Element> restIter = rest.iterator(); 
        
        @Override
        public java.lang.Object next() {
            if (idx < array.length) {
                return array[Util.toInt(idx++)];
            }
            else {
                return restIter.next();
            }
        }
        
        @Override
        public java.lang.String toString() {
            return "TupleIterator";
        }
        
    }

    @Ignore
    @Override
    public long count(
            @TypeInfo("ceylon.language::Callable<ceylon.language::Boolean,ceylon.language::Tuple<Element,Element,ceylon.language::Empty>>")
            @Name("selecting")@FunctionalParameter("(element)") Callable<? extends Boolean> f) {
        int count=0;
        for (int ii = 0; ii < array.length; ii++) {
            java.lang.Object x = array[ii];
            if (x!=null && f.$call$(x).booleanValue()) count++;
        }
        return count + rest.count(f);
    }

    @Override
    @Ignore
    public Sequence<? extends Element> sequence() {
        return this;//$ceylon$language$Sequence$this.sequence();
    }

    @Override @Ignore
    public boolean longerThan(long length) {
        return this.array.length+rest.getSize() > length;
    }
    @Override @Ignore
    public boolean shorterThan(long length) {
        return this.array.length+rest.getSize() < length;
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Annotations({
        @Annotation("shared"),
        @Annotation("actual")})
    @Override
    @TypeInfo("ceylon.language::Tuple<Element|Other,Other,ceylon.language::Tuple<Element,First,Rest>>")
    public final <Other> Tuple 
    withLeading(@Ignore TypeDescriptor $reifiedOther, @Name("element") Other e) {
        int length = this.array.length;
        java.lang.Object[] array = new java.lang.Object[length+1];
        array[0] = e;
        arraycopy(this.array, 0, array, 1, length);
        return new Tuple(TypeDescriptor.union($reifiedElement,$reifiedOther), array);
    }
    
    @SuppressWarnings({ "rawtypes" })
    @Annotations({
        @Annotation("shared"),
        @Annotation("actual")})
    @Override
    @TypeInfo("ceylon.language::Tuple<Element|Other,First,ceylon.language::Sequence<Element|Other>>")
    public <Other> Tuple 
    withTrailing(@Ignore TypeDescriptor $reifiedOther, @Name("element") Other e) {
        int length = this.array.length;
        java.lang.Object[] array = new java.lang.Object[length+1];
        arraycopy(this.array, 0, array, 0, length);
        array[length] = e;
        return new Tuple(TypeDescriptor.union($reifiedElement,$reifiedOther), array);
    }
    
    @SuppressWarnings({ "rawtypes" })
    @Annotations({
        @Annotation("shared"),
        @Annotation("actual")})
    @Override
    @TypeInfo("ceylon.language::Tuple<Element|Other,First,ceylon.language::Sequential<Element|Other>>")
    public <Other> Tuple
    append(@Ignore TypeDescriptor $reifiedOther, @Name("elements") Iterable<? extends Other, ?> es) {
        Sequential<?> sequence = es.sequence();
        int length = this.array.length;
        java.lang.Object[] array = new java.lang.Object[length+Util.toInt(sequence.getSize())];
        arraycopy(this.array, 0, array, 0, length);
        int ii = length;
        Iterator iter = sequence.iterator();
        java.lang.Object o;
        while (!((o = iter.next()) instanceof Finished)) {
            array[ii++] = o;
        }
        return new Tuple(TypeDescriptor.union($reifiedElement,$reifiedOther), array);
    }

    /** Gets the underlying array. Used for iteration using a C-style for */
    @Ignore
    public java.lang.Object[] $getArray$() {
        if (rest instanceof Empty) {
            return array;
        } 
        return null;
    }
    
    /** Gets the underlying first index. Used for iteration using a C-style for */
    @Ignore
    public int $getFirst$() {
        return 0;
    }
    
    /** Gets the underlying length. Used for iteration using a C-style for */
    @Ignore
    public int $getLength$() {
        return Util.toInt(array.length + rest.getSize());
    }
    
    
}