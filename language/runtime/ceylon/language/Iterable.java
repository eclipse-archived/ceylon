package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Annotation;
import com.redhat.ceylon.compiler.java.metadata.Annotations;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;

@Ceylon(major = 4)
@TypeParameters({@TypeParameter(value = "Element", variance = Variance.OUT),
                 @TypeParameter(value = "Absent", variance = Variance.OUT, 
                     defaultValue = "ceylon.language::Null", 
                     satisfies = "ceylon.language::Null")})
@SatisfiedTypes("ceylon.language::Container<Element,Absent>")
public interface Iterable<Element,Absent> extends Container<Element,Absent> {

    @Annotations(@Annotation("default"))
    public long getSize();

    @Annotations({@Annotation("actual"), @Annotation("default")})
    @Override
    public boolean getEmpty();
    
    @Annotations(@Annotation("formal"))
    @TypeInfo("ceylon.language::Iterator<Element>")
    public Iterator<? extends Element> getIterator();

    @Annotations({@Annotation("actual"), @Annotation("default")})
    @TypeInfo("Absent|Element")
    public Element getFirst();

    @Annotations({@Annotation("actual"), @Annotation("default")})
    @TypeInfo("Absent|Element")
    public Element getLast();

    @Annotations(@Annotation("default"))
    @TypeInfo("ceylon.language::Iterable<Element, Null>")
    public Iterable<? extends Element, ? extends java.lang.Object> getRest();

    @Annotations(@Annotation("default"))
    @TypeInfo("ceylon.language::Sequential<Element>")
    public Sequential<? extends Element> getSequence();

    @Annotations(@Annotation("default"))
    @TypeInfo("ceylon.language::Iterable<Result,ceylon.language::Null>")
    @TypeParameters(@TypeParameter("Result"))
    public <Result> Iterable<? extends Result, ? extends java.lang.Object> map(@Name("collecting")
        @TypeInfo("ceylon.language::Callable<Result,ceylon.language::Tuple<Element,Element,ceylon.language::Empty>>")
        Callable<? extends Result> collecting);

    @Annotations(@Annotation("default"))
    @TypeInfo("ceylon.language::Iterable<Element,ceylon.language::Null>")
    public Iterable<? extends Element, ? extends java.lang.Object> filter(@Name("selecting")
        @TypeInfo("ceylon.language::Callable<ceylon.language::Boolean,ceylon.language::Tuple<Element,Element,ceylon.language::Empty>>")
        Callable<? extends Boolean> selecting);

    @Annotations(@Annotation("default"))
    @TypeInfo("Result")
    @TypeParameters(@TypeParameter("Result"))
    public <Result> Result fold(@Name("initial")
		@TypeInfo("Result") Result initial,
		@Name("accumulating")
        @TypeInfo("ceylon.language::Callable<Result,ceylon.language::Tuple<Result|Element,Result,ceylon.language::Tuple<Element,Element,ceylon.language::Empty>>>")
        Callable<? extends Result> accumulating);

    @Annotations(@Annotation("default"))
    @TypeInfo("Element|ceylon.language::Null")
    public Element find(@Name("selecting")
        @TypeInfo("ceylon.language::Callable<ceylon.language::Boolean,ceylon.language::Tuple<Element,Element,ceylon.language::Empty>>")
        Callable<? extends Boolean> selecting);

    @Annotations(@Annotation("default"))
    @TypeInfo("Element|ceylon.language::Null")
    public Element findLast(@Name("selecting")
        @TypeInfo("ceylon.language::Callable<ceylon.language::Boolean,ceylon.language::Tuple<Element,Element,ceylon.language::Empty>>")
        Callable<? extends Boolean> selecting);

    @Annotations(@Annotation("default"))
    @TypeInfo("ceylon.language::Sequential<Element>")
    public Sequential<? extends Element> sort(@Name("comparing")
        @TypeInfo("ceylon.language::Callable<ceylon.language::Null|ceylon.language::Comparison,ceylon.language::Tuple<Element,Element,ceylon.language::Tuple<Element,Element,ceylon.language::Empty>>>")
        Callable<? extends Comparison> comparing);

    @Annotations(@Annotation("default"))
    @TypeInfo("ceylon.language::Sequential<Result>")
    @TypeParameters(@TypeParameter("Result"))
    public <Result> Sequential<? extends Result> collect(@Name("collecting")
        @TypeInfo("ceylon.language::Callable<Result,ceylon.language::Tuple<Element,Element,ceylon.language::Empty>>")
        Callable<? extends Result> collecting);

    @Annotations(@Annotation("default"))
    @TypeInfo("ceylon.language::Sequential<Element>")
    public Sequential<? extends Element> select(@Name("selecting")
        @TypeInfo("ceylon.language::Callable<ceylon.language::Boolean,ceylon.language::Tuple<Element,Element,ceylon.language::Empty>>")
        Callable<? extends Boolean> selecting);

    @Annotations(@Annotation("default"))
    @TypeInfo("ceylon.language::Boolean")
    public boolean any(@Name("selecting")
        @TypeInfo("ceylon.language::Callable<ceylon.language::Boolean,ceylon.language::Tuple<Element,Element,ceylon.language::Empty>>")
        Callable<? extends Boolean> selecting);

    @Annotations(@Annotation("default"))
    @TypeInfo("ceylon.language::Boolean")
    public boolean every(@Name("selecting")
        @TypeInfo("ceylon.language::Callable<ceylon.language::Boolean,ceylon.language::Tuple<Element,Element,ceylon.language::Empty>>")
        Callable<? extends Boolean> selecting);

    @Annotations(@Annotation("default"))
    @TypeInfo("ceylon.language::Iterable<Element,ceylon.language::Null>")
    public Iterable<? extends Element, ? extends java.lang.Object> skipping(@Name("skip")
        @TypeInfo("ceylon.language::Integer")
        long skip);

    @Annotations(@Annotation("default"))
    @TypeInfo("ceylon.language::Iterable<Element,ceylon.language::Null>")
    public Iterable<? extends Element, ? extends java.lang.Object> taking(@Name("take")
        @TypeInfo("ceylon.language::Integer")
        long take);

    @Annotations(@Annotation("default"))
    @TypeInfo("ceylon.language::Iterable<Element,ceylon.language::Null>")
    public Iterable<? extends Element, ? extends java.lang.Object> by(@Name("step")
        @TypeInfo("ceylon.language::Integer")
        long step);

    @Annotations(@Annotation("default"))
    @TypeInfo("ceylon.language::Integer")
    public long count(@Name("selecting")
        @TypeInfo("ceylon.language::Callable<ceylon.language::Boolean,ceylon.language::Tuple<Element,Element,ceylon.language::Empty>>")
        Callable<? extends Boolean> selecting);

    @Annotations(@Annotation("default"))
    @TypeInfo("ceylon.language::Iterable<Element&ceylon.language::Object,Absent>")
    public Iterable<? extends Element,? extends Absent> getCoalesced();

    @Annotations(@Annotation("default"))
    @TypeInfo("ceylon.language::Iterable<ceylon.language::Entry<ceylon.language::Integer,Element&ceylon.language::Object>,Absent>")
    public Iterable<? extends Entry<? extends Integer, ? extends Element>, ? extends Absent> getIndexed();

    @Annotations(@Annotation("default"))
    @TypeInfo("ceylon.language::Iterable<Element|Other,ceylon.language::Null>")
    @TypeParameters(@TypeParameter("Other"))
    public <Other> Iterable chain(@Name("other")
            @TypeInfo("ceylon.language::Iterable<Other,ceylon.language::Null>")
            Iterable<? extends Other, ? extends java.lang.Object> other);

    @Annotations(@Annotation("default"))
    @TypeParameters(@TypeParameter(value="Grouping",satisfies="ceylon.language::Object"))
    @TypeInfo("ceylon.language::Map<Grouping,ceylon.language::Sequence<Element>>")
    public <Key> Map<? extends Key, ? extends Sequence<? extends Element>> group(@Name("grouping")
            @TypeInfo("ceylon.language::Callable<Grouping,ceylon.language::Tuple<Element,Element,ceylon.language::Empty>>")
            Callable<? extends Key> grouping);

    @Override
    @Annotations({@Annotation("actual"), @Annotation("default")})
    public boolean contains(@Name("element") java.lang.Object element);

}
