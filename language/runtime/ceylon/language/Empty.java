package ceylon.language;

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
	
    @Override
    public long getSize(); 
    
    @Override
    @TypeInfo("ceylon.language.Nothing")
    public Iterator getIterator();
    
    @Override
    @TypeInfo("ceylon.language.Nothing")
    public java.lang.Object item(@Name("key") @TypeInfo("ceylon.language.Integer")
    Integer key);

    @Override
    @TypeInfo("ceylon.language.Empty")
    public Empty segment(@Name("from") @TypeInfo("ceylon.language.Integer")
    Comparable from,
    @Name("length") @TypeInfo("ceylon.language.Integer")
    Comparable length);

    @Override
    @TypeInfo("ceylon.language.Empty")
    public Empty span(@Name("from") @TypeInfo("ceylon.language.Integer")
    Comparable from,
    @Name("to") @TypeInfo("ceylon.language.Integer|ceylon.language.Nothing")
    Comparable length);

    @Override
    public java.lang.String toString();
    
    @Override
    @TypeInfo("ceylon.language.Nothing")
    public Integer getLastIndex();

    @Override
    public Empty getClone();
    
    @Override
    public boolean contains(@Name("element") @TypeInfo("ceylon.language.Object")
    java.lang.Object element);

    @Override
    public long count(@Name("element") @TypeInfo("ceylon.language.Object")
    java.lang.Object element);

    @Override
    public boolean defines(@Name("key") @TypeInfo("ceylon.language.Integer")
    Integer key);

}
