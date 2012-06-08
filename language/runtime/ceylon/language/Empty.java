package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Annotation;
import com.redhat.ceylon.compiler.java.metadata.Annotations;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon
@SatisfiedTypes({
    "ceylon.language.List<ceylon.language.Bottom>",
    "ceylon.language.None<ceylon.language.Bottom>",
    "ceylon.language.Ranged<ceylon.language.Integer,ceylon.language.Empty>",
    "ceylon.language.Cloneable<ceylon.language.Empty>"
})
public interface Empty 
        extends List, None {
	
    @Annotations(@Annotation("actual"))
    @Override
    public long getSize(); 
    
    @Annotations(@Annotation("actual"))
    @Override
    @TypeInfo("ceylon.language.Nothing")
    public Iterator getIterator();
    
    @Annotations(@Annotation("actual"))
    @Override
    @TypeInfo("ceylon.language.Nothing")
    public java.lang.Object item(@Name("key") @TypeInfo("ceylon.language.Integer")
    Integer key);

    @Annotations(@Annotation("actual"))
    @Override
    @TypeInfo("ceylon.language.Empty")
    public Empty segment(@Name("from") @TypeInfo("ceylon.language.Integer")
    Comparable from,
    @Name("length") @TypeInfo("ceylon.language.Integer")
    long length);

    @Annotations(@Annotation("actual"))
    @Override
    @TypeInfo("ceylon.language.Empty")
    public Empty span(@Name("from") @TypeInfo("ceylon.language.Integer")
    Comparable from,
    @Name("to") @TypeInfo("ceylon.language.Integer|ceylon.language.Nothing")
    Comparable length);

    @Annotations(@Annotation("actual"))
    @Override
    public java.lang.String toString();
    
    @Annotations(@Annotation("actual"))
    @Override
    @TypeInfo("ceylon.language.Nothing")
    public Integer getLastIndex();

    @Annotations(@Annotation("actual"))
    @Override
    public Empty getClone();
    
    @Annotations(@Annotation("actual"))
    @Override
    public boolean contains(@Name("element") @TypeInfo("ceylon.language.Object")
    java.lang.Object element);

    @Annotations(@Annotation("actual"))
    @Override
    public long count(@Name("element") @TypeInfo("ceylon.language.Object")
    java.lang.Object element);

    @Annotations(@Annotation("actual"))
    @Override
    public boolean defines(@Name("key") @TypeInfo("ceylon.language.Integer")
    Integer key);

    @Override public Empty getSequence();
    @Override public java.lang.Object find(Callable f);
    @Override public Empty map(Callable f);
    @Override public Empty filter(Callable f);
    @Override public java.lang.Object fold(java.lang.Object ini, Callable f);
}
