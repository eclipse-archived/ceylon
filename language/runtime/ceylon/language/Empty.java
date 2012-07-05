package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Annotation;
import com.redhat.ceylon.compiler.java.metadata.Annotations;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon(major = 2)
@SatisfiedTypes({
    "ceylon.language.List<ceylon.language.Bottom>",
    "ceylon.language.None<ceylon.language.Bottom>",
    "ceylon.language.Ranged<ceylon.language.Integer,ceylon.language.Empty>",
    "ceylon.language.Cloneable<ceylon.language.Empty>"
})
public interface Empty 
        extends List<java.lang.Object>, None<java.lang.Object> {
	
    @Annotations(@Annotation("actual"))
    @Override
    public long getSize(); 
    
    @Annotations(@Annotation("actual"))
    @Override
    @TypeInfo("ceylon.language.Iterator<ceylon.language.Bottom>")
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
    Integer from,
    @Name("length") @TypeInfo("ceylon.language.Integer")
    long length);

    @Annotations(@Annotation("actual"))
    @Override
    @TypeInfo("ceylon.language.Empty")
    public Empty span(@Name("from") @TypeInfo("ceylon.language.Integer")
    Integer from,
    @Name("to") @TypeInfo("ceylon.language.Integer|ceylon.language.Nothing")
    Integer length);

    @Annotations(@Annotation("actual"))
    @Override
    public java.lang.String toString();
    
    @Annotations(@Annotation("actual"))
    @Override
    @TypeInfo("ceylon.language.Nothing")
    public Integer getLastIndex();

    @Annotations({@Annotation("actual")})
    @Override
    public Empty getReversed();
    
    @Annotations(@Annotation("actual"))
    @Override
    public Empty getClone();
    
    @Annotations({@Annotation("actual")})
    @Override
    public Empty getSequence();
    
    @Annotations(@Annotation("actual"))
    @Override
    public boolean contains(@Name("element") @TypeInfo("ceylon.language.Object")
    java.lang.Object element);

    @Annotations(@Annotation("actual"))
    @Override
    public boolean defines(@Name("key") @TypeInfo("ceylon.language.Integer")
    Integer key);

}
