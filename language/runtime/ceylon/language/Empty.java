package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Ceylon;
import com.redhat.ceylon.compiler.metadata.java.Name;
import com.redhat.ceylon.compiler.metadata.java.SatisfiedTypes;
import com.redhat.ceylon.compiler.metadata.java.TypeInfo;

@Ceylon
@SatisfiedTypes({
    "ceylon.language.Correspondence<ceylon.language.Integer,ceylon.language.Bottom>",
    "ceylon.language.Ordered<ceylon.language.Bottom>",
    "ceylon.language.Ranged<ceylon.language.Integer,ceylon.language.Empty>",
    "ceylon.language.Sized"
})
public interface Empty 
        extends Correspondence, Ordered, Sized, 
                Ranged<Integer,Empty> {
	
    @TypeInfo("ceylon.language.Integer")
    public long getSize(); 
    
    public boolean getEmpty();
    
    @TypeInfo("ceylon.language.Nothing")
    public Iterator getIterator();
    
    @TypeInfo("ceylon.language.Nothing")
    public java.lang.Object item(@Name("key") java.lang.Object key);
    
    @TypeInfo("ceylon.language.Nothing")
    public java.lang.Object getFirst();
}
