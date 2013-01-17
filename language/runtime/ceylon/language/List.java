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
@TypeParameters(@TypeParameter(value = "Element", variance = Variance.OUT))
@SatisfiedTypes({"ceylon.language::Collection<Element>",
                 "ceylon.language::Correspondence<ceylon.language::Integer,Element>",
                 "ceylon.language::Ranged<ceylon.language::Integer,ceylon.language::List<Element>>",
                 "ceylon.language::Cloneable<ceylon.language::List<Element>>"})
public interface List<Element>
        extends Collection<Element>,
                Correspondence<Integer,Element>,
                Ranged<Integer, List<? extends Element>> {

    @Annotations(@Annotation("formal"))
    @TypeInfo("ceylon.language::Null|ceylon.language::Integer")
    public Integer getLastIndex();

    @Annotations({@Annotation("actual"), @Annotation("default")})
    @Override
    public long getSize();

    @Annotations({@Annotation("actual"), @Annotation("default")})
    @Override
    public boolean defines(@Name("index") Integer key);

    @Annotations({@Annotation("actual"), @Annotation("formal")})
    @TypeInfo("ceylon.language::Null|Element")
    @Override
    public Element item(@Name("index") Integer key);

    @Annotations({@Annotation("actual"), @Annotation("default")})
    @TypeInfo("ceylon.language::Iterator<Element>")
    @Override
    public Iterator<? extends Element> getIterator();

    @Annotations({@Annotation("actual"), @Annotation("default")})
    @Override
    public boolean equals(@Name("that") @TypeInfo("ceylon.language::Object")
    java.lang.Object that);

    @Annotations({@Annotation("formal")})
    @TypeInfo("ceylon.language::List<Element>")
    public List<? extends Element> getReversed();

    @Annotations({@Annotation("actual"), @Annotation("default")})
    @Override
    public int hashCode();

    @Annotations({@Annotation("actual"), @Annotation("default")})
    @TypeInfo("Element|ceylon.language::Null")
    public Element findLast(@Name("selecting")
        @TypeInfo("ceylon.language::Callable<ceylon.language.Boolean,ceylon.language::Tuple<Element,Element,ceylon.language::Empty>>")
        Callable<? extends Boolean> selecting);

    @Annotations({@Annotation("actual"), @Annotation("default")})
    @TypeInfo("Element|ceylon.language::Null")
    public Element getFirst();

    @Annotations({@Annotation("actual"), @Annotation("default")})
    @TypeInfo("Element|ceylon.language::Null")
    public Element getLast();
    
    @SuppressWarnings("rawtypes")
    @Annotations({@Annotation("actual"), @Annotation("default")})
    @TypeParameters(@TypeParameter("Other"))
    @TypeInfo("ceylon.language::Sequence<Element|Other>")
    public <Other> Sequence withLeading(@Name("element")
            @TypeInfo("Other")
            Other element);

    @SuppressWarnings("rawtypes")
    @Annotations({@Annotation("actual"), @Annotation("default")})
    @TypeParameters(@TypeParameter("Other"))
    @TypeInfo("ceylon.language::Sequence<Element|Other>")
    public <Other> Sequence withTrailing(@Name("element")
            @TypeInfo("Other")
            Other element);

}
