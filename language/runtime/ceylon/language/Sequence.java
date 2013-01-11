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

@Ceylon(major = 3)
@TypeParameters(@TypeParameter(value = "Element", variance = Variance.OUT))
@SatisfiedTypes({
    "ceylon.language::Sequential<Element>",
    "ceylon.language::Container<Element,ceylon.language::Nothing>",
    "ceylon.language::Cloneable<ceylon.language::Sequence<Element>>"
})
public interface Sequence<Element> 
        extends Sequential<Element> {
    
    @Annotations({@Annotation("actual"), @Annotation("formal")})
    @Override
    @TypeInfo("ceylon.language::Integer")
    public Integer getLastIndex();
    
    @Annotations({@Annotation("actual"), @Annotation("formal")})
    @Override
    public Element getFirst();
    
    @Annotations(@Annotation("default"))
    public Element getLast();

    @Annotations({@Annotation("actual"), @Annotation("formal")})
    @Override
    @TypeInfo("ceylon.language::Sequential<Element>")
    public Sequential<? extends Element> getRest();
    
    @Annotations({@Annotation("actual"), @Annotation("formal")})
    @Override
    @TypeInfo("ceylon.language::Sequence<Element>")
    public Sequence<? extends Element> getReversed();
    
    @Annotations({@Annotation("actual"), @Annotation("default")})
    @Override
    @TypeInfo("ceylon.language::Sequence<Element>")
    public Sequence<? extends Element> getSequence();
    
    @Annotations({@Annotation("actual")})
    @Override
    @TypeInfo("ceylon.language::Boolean")
    public boolean getEmpty();
    
    /*@Override
    @TypeInfo("ceylon.language::Sequential<Element>")
    public Sequential<? extends Element> span(@Name("from") Integer from, 
            @TypeInfo("ceylon.language::Null|ceylon.language::Integer")
            @Name("to") Integer to);
    
    @Override
    @TypeInfo("ceylon.language::Sequential<Element>")
    public Sequential<? extends Element> segment(@Name("from") Integer from, 
            @Name("length") Integer length);*/

    @Annotations({@Annotation("actual"), @Annotation("default")})
    @Override
    @TypeInfo("ceylon.language::Sequence<Element>")
    public Sequence<? extends Element> sort(@Name("comparing")
            @TypeInfo("ceylon.language::Callable<ceylon.language::Null|ceylon.language::Comparison,ceylon.language::Tuple<Element,Element,ceylon.language::Tuple<Element,Element,ceylon.language::Empty>>>")
            Callable<? extends Comparison> comparing);

    @Annotations({@Annotation("actual"), @Annotation("default")})
    @Override
    @TypeParameters(@TypeParameter("Result"))
    @TypeInfo("ceylon.language::Sequence<Result>")
    public <Result> Sequence<? extends Result> collect(@Name("collecting")
            @TypeInfo("ceylon.language::Callable<Result,ceylon.language::Tuple<Element,Element,ceylon.language::Empty>>")
            Callable<? extends Result> collecting);

}
