package ceylon.language;

import java.util.HashSet;

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

@Ceylon(major = 4)
@TypeParameters(@TypeParameter(value="Element", variance=Variance.OUT,
    satisfies="ceylon.language::Object"))
@SatisfiedTypes("ceylon.language::Set<Element>")
public class LazySet<Element> implements Set<Element> {
    @Ignore
    protected final ceylon.language.Category$impl $ceylon$language$Category$this;
    @Ignore
    protected final ceylon.language.Iterable$impl<Element,java.lang.Object> $ceylon$language$Iterable$this;
    @Ignore
    protected final ceylon.language.Container$impl<Element,java.lang.Object> $ceylon$language$Container$this;
    @Ignore
    protected final ceylon.language.Collection$impl<Element> $ceylon$language$Collection$this;
    @Ignore
    protected final ceylon.language.Set$impl<Element> $ceylon$language$Set$this;
    @Ignore
    protected final ceylon.language.Cloneable$impl $ceylon$language$Cloneable$this;

    private final Iterable<? extends Element, ? extends java.lang.Object> elems;

    @Ignore @SuppressWarnings("unchecked")
    public LazySet() {
        this((Iterable<? extends Element, ? extends java.lang.Object>)empty_.getEmpty$());
    }
    public LazySet(@Name("elems")
            @TypeInfo("ceylon.language::Iterable<Element,ceylon.language::Null>")
            Iterable<? extends Element, ? extends java.lang.Object> elems) {
        this.$ceylon$language$Category$this = new ceylon.language.Category$impl(this);
        this.$ceylon$language$Container$this = new ceylon.language.Container$impl<Element,java.lang.Object>(this);
        this.$ceylon$language$Iterable$this = new ceylon.language.Iterable$impl<Element,java.lang.Object>(this);
        this.$ceylon$language$Collection$this = new ceylon.language.Collection$impl<Element>(this);
        this.$ceylon$language$Set$this = new ceylon.language.Set$impl<Element>(this);
        this.$ceylon$language$Cloneable$this = new ceylon.language.Cloneable$impl(this);
        this.elems = elems;
    }
    

    @Ignore
    @Override
    public Category$impl $ceylon$language$Category$impl(){
        return $ceylon$language$Category$this;
    }

    @Ignore
    @Override
    public Container$impl<Element,java.lang.Object> $ceylon$language$Container$impl(){
        return $ceylon$language$Container$this;
    }

    @Ignore
    @Override
    public Iterable$impl<Element,java.lang.Object> $ceylon$language$Iterable$impl(){
        return $ceylon$language$Iterable$this;
    }

    @Ignore
    @Override
    public Collection$impl<Element> $ceylon$language$Collection$impl(){
        return $ceylon$language$Collection$this;
    }

    @Ignore
    @Override
    public Set$impl<Element> $ceylon$language$Set$impl(){
        return $ceylon$language$Set$this;
    }

    @Ignore
    @Override
    public Cloneable$impl $ceylon$language$Cloneable$impl(){
        return $ceylon$language$Cloneable$this;
    }

    @Override
    @Annotations(@Annotation("actual"))
    @TypeInfo("ceylon.language::Set<Element>")
    public Set<? extends Element> getClone() {
        return new LazySet<Element>(elems);
    }

    @Override
    @Annotations(@Annotation("actual"))
    @TypeInfo("ceylon.language::Integer")
    public long getSize() {
        //This is to avoid counting duplicates
        HashSet<Element> s = new HashSet<Element>();
        java.lang.Object $tmp;
        for (Iterator<? extends Element> i = elems.getIterator(); !(($tmp = i.next()) instanceof Finished);) {
            s.add((Element)$tmp);
        }
        return s.size();
    }

    @Override
    @Annotations(@Annotation("actual"))
    @TypeInfo("ceylon.language::Iterator<Element>")
    public Iterator<? extends Element> getIterator() {
        return elems.getIterator();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    @Annotations(@Annotation("actual"))
    @TypeParameters(@TypeParameter(value="Other", satisfies="ceylon.language::Object"))
    @TypeInfo("ceylon.language::Set<Element|Other>")
    public <Other> Set union(Set<? extends Other> set) {
        return new LazySet(elems.chain(set));
    }
    @Override @Ignore
    public <Default>Iterable<?,?> defaultNullElements(Default defaultValue) {
        return $ceylon$language$Iterable$this.defaultNullElements(defaultValue);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    @Annotations(@Annotation("actual"))
    @TypeParameters(@TypeParameter(value="Other", satisfies="ceylon.language::Object"))
    @TypeInfo("ceylon.language::Set<Element&Other>")
    public <Other> Set intersection(Set<? extends Other> set) {
        return new LazySet(set.filter(new AbstractCallable<Boolean>("Set_intersection"){
            @Override
            public Boolean $call(final java.lang.Object e) {
                return Boolean.instance(elems.find(new AbstractCallable<Boolean>("Set_find2"){
                    @Override
                    public Boolean $call(java.lang.Object o) {
                        return Boolean.instance(e.equals(o));
                    }
                }) != null);
            }
        }));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    @Annotations(@Annotation("actual"))
    @TypeParameters(@TypeParameter(value="Other", satisfies="ceylon.language::Object"))
    @TypeInfo("ceylon.language::Set<Element|Other>")
    public <Other> Set exclusiveUnion(final Set<? extends Other> set) {
        Iterable<? extends Element, ? extends java.lang.Object> hereNotThere = elems.filter(new AbstractCallable<Boolean>("Set_xor1"){
            @Override
            public Boolean $call(java.lang.Object e) {
                return Boolean.instance(!set.contains(e));
            };
        });
        Iterable<? extends Other, ? extends java.lang.Object> thereNotHere = set.filter(new AbstractCallable<Boolean>("Set_xor2"){
            @Override
            public Boolean $call(final java.lang.Object e) {
                return Boolean.instance(elems.find(new AbstractCallable<Boolean>("Set_find2"){
                    @Override
                    public Boolean $call(java.lang.Object o) {
                        return Boolean.instance(e.equals(o));
                    }
                }) == null);
            }
        });
        return new LazySet(hereNotThere.chain(thereNotHere));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    @Annotations(@Annotation("actual"))
    @TypeParameters(@TypeParameter(value="Other", satisfies="ceylon.language::Object"))
    @TypeInfo("ceylon.language::Set<Element>")
    public <Other> Set<? extends Element> complement(final Set<? extends Other> set) {
        return new LazySet(this.filter(new AbstractCallable<Boolean>("Set_xor2"){
            @Override
            public Boolean $call(final java.lang.Object e) {
                return Boolean.instance(!set.contains(e));
            }
        }));
    }

    @Override
    @Annotations({@Annotation("actual"), @Annotation("default")})
    @TypeInfo("ceylon.language::Boolean")
    public boolean equals(java.lang.Object other) {
        return $ceylon$language$Set$this.equals(other);
    }
    @Override
    @Annotations({@Annotation("actual"), @Annotation("default")})
    @TypeInfo("ceylon.language::Integer")
    public int hashCode() {
        return $ceylon$language$Set$this.hashCode();
    }

    @Override @Ignore
    public boolean getEmpty() {
        return elems.getEmpty();
    }

    @Override @Ignore
    public boolean contains(final java.lang.Object element) {
        return elems.find(new AbstractCallable<Boolean>("Set_contains"){
            @Override
            public Boolean $call(java.lang.Object e) {
                return Boolean.instance(e.equals(element));
            }
        }) != null;
    }

    @Override @Ignore
    public Element getFirst() {
        return (Element) elems.getFirst();
    }

    @Override @Ignore
    public Element getLast() {
        return (Element) elems.getLast();
    }

    @Override @Ignore
    public Iterable<? extends Element, ? extends java.lang.Object> getRest() {
        return elems.getRest();
    }

    @Override @Ignore
    public Sequential<? extends Element> getSequence() {
        return elems.getSequence();
    }

    @Override @Ignore
    public <Result> Iterable<? extends Result, ? extends java.lang.Object> map(
            Callable<? extends Result> collecting) {
        return elems.map(collecting);
    }

    @Override @Ignore
    public Iterable<? extends Element, ? extends java.lang.Object> filter(
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

    @Override @Ignore
    public Element findLast(Callable<? extends Boolean> selecting) {
        return elems.findLast(selecting);
    }

    @Override @Ignore
    public Sequential<? extends Element> sort(
            Callable<? extends Comparison> comparing) {
        return elems.sort(comparing);
    }

    @Override @Ignore
    public <Result> Sequential<? extends Result> collect(
            Callable<? extends Result> collecting) {
        return elems.collect(collecting);
    }

    @Override @Ignore
    public Sequential<? extends Element> select(
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
    public Iterable<? extends Element, ? extends java.lang.Object> skipping(long skip) {
        return elems.skipping(skip);
    }

    @Override @Ignore
    public Iterable<? extends Element, ? extends java.lang.Object> taking(long take) {
        return elems.taking(take);
    }

    @Override @Ignore
    public Iterable<? extends Element, ? extends java.lang.Object> by(long step) {
        return elems.by(step);
    }

    @Override @Ignore
    public long count(Callable<? extends Boolean> selecting) {
        return elems.count(selecting);
    }

    @Override @Ignore
    public Iterable<? extends Element, ? extends java.lang.Object> getCoalesced() {
        return elems.getCoalesced();
    }

    @Override @Ignore
    public Iterable<? extends Entry<? extends Integer, ? extends Element>, ? extends java.lang.Object> getIndexed() {
        return elems.getIndexed();
    }

    @SuppressWarnings("rawtypes")
    @Override @Ignore
    public <Other> Iterable chain(Iterable<? extends Other, ? extends java.lang.Object> other) {
        return elems.chain(other);
    }

    /*@Override @Ignore
    public <Key> Map<? extends Key, ? extends Sequence<? extends Element>> group(
            Callable<? extends Key> grouping) {
        return elems.group(grouping);
    }*/

    @Override @Ignore
    public boolean containsEvery(
            @Name("elements")
            @TypeInfo("ceylon.language::Iterable<ceylon.language::Object,ceylon.language::Null>")
            Iterable<?,?> elements) {
        return $ceylon$language$Category$this.containsEvery(elements);
    }

//    @Override @Ignore
//    public boolean containsEvery() {
//        return cat$impl.containsEvery();
//    }
//
//    @Override @Ignore
//    public Sequential<?> containsEvery$elements() {
//        return cat$impl.containsEvery$elements();
//    }

    @Override @Ignore
    public boolean containsAny(
            @Name("elements")
            @TypeInfo("ceylon.language::Iterable<ceylon.language::Object,ceylon.language::Null>")
            Iterable<?,?> elements) {
        return $ceylon$language$Category$this.containsAny(elements);
    }

//    @Override @Ignore
//    public boolean containsAny() {
//        return cat$impl.containsAny();
//    }
//
//    @Override @Ignore
//    public Sequential<?> containsAny$elements() {
//        return cat$impl.containsAny$elements();
//    }

    @Override @Ignore
    public boolean superset(Set<? extends java.lang.Object> set) {
        return $ceylon$language$Set$this.superset(set);
    }

    @Override @Ignore
    public boolean subset(Set<? extends java.lang.Object> set) {
        return $ceylon$language$Set$this.subset(set);
    }

    @Override @Ignore
    public java.lang.String toString() {
        return $ceylon$language$Collection$this.toString();
    }
}
