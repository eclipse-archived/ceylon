package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Annotation;
import com.redhat.ceylon.compiler.java.metadata.Annotations;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;

@Ceylon(major = 3)
@SatisfiedTypes({
    "ceylon.language::Sequential<ceylon.language::Bottom>",
    "ceylon.language::None<ceylon.language::Bottom>",
    "ceylon.language::Ranged<ceylon.language::Integer,ceylon.language::Empty>",
    "ceylon.language::Cloneable<ceylon.language::Empty>"
})
public interface Empty 
        extends Sequential<java.lang.Object>, None<java.lang.Object> {
    
    @Annotations(@Annotation("actual"))
    @Override
    @TypeInfo("ceylon.language::Iterator<ceylon.language::Bottom>")
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Iterator getIterator();
    
    @Annotations(@Annotation("actual"))
    @Override
    @TypeInfo("ceylon.language::Nothing")
    public java.lang.Object item(@Name("key") @TypeInfo("ceylon.language::Integer")
    Integer key);

    @Annotations(@Annotation("actual"))
    @Override
    @TypeInfo("ceylon.language::Empty")
    public Empty segment(@Name("from") @TypeInfo("ceylon.language::Integer")
    Integer from,
    @Name("length") @TypeInfo("ceylon.language::Integer")
    long length);

    @Annotations(@Annotation("actual"))
    @Override
    @TypeInfo("ceylon.language::Empty")
    public Empty span(@Name("from") @TypeInfo("ceylon.language::Integer")
    Integer from,
    @Name("to") @TypeInfo("ceylon.language::Integer")
    Integer length);
    
    @Annotations(@Annotation("actual"))
    @Override
    @TypeInfo("ceylon.language::Empty")
    public Empty spanTo(@Name("to") @TypeInfo("ceylon.language::Integer")
    Integer to);
    
    @Annotations(@Annotation("actual"))
    @Override
    @TypeInfo("ceylon.language::Empty")
    public Empty spanFrom(@Name("from") @TypeInfo("ceylon.language::Integer")
    Integer from);
    
    @Annotations(@Annotation("actual"))
    @Override
    public long getSize(); 

    @Annotations({@Annotation("actual")})
    @Override
    public Empty getReversed();
    
    @Annotations({@Annotation("actual")})
    @Override
    public Empty getSequence();

    @Annotations(@Annotation("actual"))
    @Override
    public java.lang.String toString();
    
    @Annotations(@Annotation("actual"))
    @Override
    @TypeInfo("ceylon.language::Nothing")
    public Integer getLastIndex();

    @Annotations(@Annotation("actual"))
    @Override
    @TypeInfo("ceylon.language.Nothing")
    public java.lang.Object getFirst();

    @Annotations(@Annotation("actual"))
    @Override
    @TypeInfo("ceylon.language.Nothing")
    public java.lang.Object getLast();
    
    @Annotations(@Annotation("actual"))
    @Override
    public Empty getClone();
    
    @Annotations(@Annotation("actual"))
    @Override
    public Empty getCoalesced();
    
    @Annotations(@Annotation("actual"))
    @TypeInfo("ceylon.language::Empty")
    public Iterable getIndexed();

    @Annotations(@Annotation("actual"))
    @TypeInfo("ceylon.language::Iterable<Other>")
    @TypeParameters(@TypeParameter("Other"))
    public <Other> Iterable chain(@Name("other")
            @TypeInfo("ceylon.language::Iterable<Other>")
            Iterable<? extends Other> other);
    
    @Annotations(@Annotation("actual"))
    @Override
    public boolean contains(@Name("element") @TypeInfo("ceylon.language::Object")
    java.lang.Object element);

    @Annotations(@Annotation("actual"))
    @TypeInfo("ceylon.language::Integer")
    public long count(@Name("selecting")
        @TypeInfo("ceylon.language::Callable<ceylon.language::Boolean,ceylon.language::Tuple<ceylon.language::Bottom,ceylon.language::Bottom,ceylon.language::Empty>>")
        Callable<? extends Boolean> selecting);

    @Annotations(@Annotation("actual"))
    @Override
    public boolean defines(@Name("key") @TypeInfo("ceylon.language::Integer")
    Integer key);
    
    @Annotations(@Annotation("actual"))
    @TypeInfo("ceylon.language::Empty")
    public <Result> Iterable<? extends Result> map(@Name("collecting")
        @TypeInfo("ceylon.language::Callable<Result,ceylon.language::Tuple<ceylon.language::Bottom,ceylon.language::Bottom,ceylon.language::Empty>>")
        Callable<? extends Result> collecting);
    
    @Annotations(@Annotation("actual"))
    @TypeInfo("ceylon.language::Empty")
    public Empty filter(@Name("selecting")
        @TypeInfo("ceylon.language::Callable<ceylon.language::Boolean,ceylon.language::Tuple<ceylon.language::Bottom,ceylon.language::Bottom,ceylon.language::Empty>>")
        Callable<? extends Boolean> selecting);

    @Annotations(@Annotation("actual"))
    @TypeInfo("Result")
    @TypeParameters(@TypeParameter("Result"))
    public <Result> Result fold(@Name("initial")
        @TypeInfo("Result") Result initial,
        @Name("accumulating")
        @TypeInfo("ceylon.language::Callable<Result,ceylon.language::Tuple<ceylon.language::Bottom,Result,ceylon.language::Tuple<ceylon.language::Bottom,ceylon.language::Bottom,ceylon.language::Empty>>>")
        Callable<? extends Result> accumulating);

    @Annotations(@Annotation("actual"))
    @TypeInfo("ceylon.language::Nothing")
    public java.lang.Object find(@Name("selecting")
        @TypeInfo("ceylon.language::Callable<ceylon.language::Boolean,ceylon.language::Tuple<ceylon.language::Bottom,ceylon.language::Bottom,ceylon.language::Empty>>")
        Callable<? extends Boolean> selecting);

    @Annotations(@Annotation("actual"))
    @TypeInfo("ceylon.language::Empty")
    public Empty sort(@Name("comparing")
        @TypeInfo("ceylon.language::Callable<ceylon.language::Nothing|ceylon.language::Comparison,ceylon.language::Tuple<ceylon.language::Bottom,ceylon.language::Bottom,ceylon.language::Tuple<ceylon.language::Bottom,ceylon.language::Bottom,ceylon.language::Empty>>>")
        Callable<? extends Comparison> comparing);

    @Annotations(@Annotation("actual"))
    @TypeInfo("ceylon.language::Empty")
    public <Result> Sequential<? extends Result> collect(@Name("collecting")
        @TypeInfo("ceylon.language::Callable<Result,ceylon.language::Tuple<ceylon.language::Bottom,ceylon.language::Bottom,ceylon.language::Empty>>")
        Callable<? extends Result> collecting);
    
    @Annotations(@Annotation("actual"))
    @TypeInfo("ceylon.language::Empty")
    public Empty select(@Name("selecting")
        @TypeInfo("ceylon.language::Callable<ceylon.language::Boolean,ceylon.language::Tuple<ceylon.language::Bottom,ceylon.language::Bottom,ceylon.language::Empty>>")
        Callable<? extends Boolean> selecting);

    @Annotations(@Annotation("actual"))
    @TypeInfo("ceylon.language::Boolean")
    public boolean any(@Name("selecting")
        @TypeInfo("ceylon.language::Callable<ceylon.language::Boolean,ceylon.language::Tuple<ceylon.language::Bottom,ceylon.language::Bottom,ceylon.language::Empty>>")
        Callable<? extends Boolean> selecting);

    @Annotations(@Annotation("actual"))
    @TypeInfo("ceylon.language::Boolean")
    public boolean every(@Name("selecting")
        @TypeInfo("ceylon.language::Callable<ceylon.language::Boolean,ceylon.language::Tuple<ceylon.language::Bottom,ceylon.language::Bottom,ceylon.language::Empty>>")
        Callable<? extends Boolean> selecting);

    @Annotations(@Annotation("actual"))
    @TypeInfo("ceylon.language::Empty")
    public Empty skipping(@Name("skip")
        @TypeInfo("ceylon.language::Integer")
        long skip);

    @Annotations(@Annotation("actual"))
    @TypeInfo("ceylon.language::Empty")
    public Empty taking(@Name("take")
        @TypeInfo("ceylon.language::Integer")
        long take);

    @Annotations(@Annotation("actual"))
    @TypeInfo("ceylon.language::Empty")
    public Empty by(@Name("step")
        @TypeInfo("ceylon.language::Integer")
        long step);
    
    @SuppressWarnings("rawtypes")
    @Annotations(@Annotation("actual"))
    @TypeParameters(@TypeParameter("Other"))
    @TypeInfo("ceylon.language::Sequence<Other>")
    public <Other> Sequence withLeading(@Name("element")
            @TypeInfo("Other")
            Other element);

    @SuppressWarnings("rawtypes")
    @Annotations(@Annotation("actual"))
    @TypeParameters(@TypeParameter("Other"))
    @TypeInfo("ceylon.language::Sequence<Other>")
    public <Other> Sequence withTrailing(@Name("element")
            @TypeInfo("Other")
            Other element);

}
