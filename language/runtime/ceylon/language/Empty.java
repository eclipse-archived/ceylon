package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Name;
import com.redhat.ceylon.compiler.metadata.java.SatisfiedTypes;
import com.redhat.ceylon.compiler.metadata.java.TypeInfo;

@SatisfiedTypes({
    "ceylon.language.Correspondence<ceylon.language.Natural,ceylon.language.Bottom>",
    "ceylon.language.Ordered<ceylon.language.Bottom>",
    "ceylon.language.Ranged<ceylon.language.Natural,ceylon.language.Empty>",
    "ceylon.language.Sized"
})
public interface Empty 
        extends Correspondence<Natural, java.lang.Object>, 
                Ordered, Sized, Ranged<Natural,Empty> {
	
    @TypeInfo("ceylon.language.Natural")
    public long getSize(); 
    
    public boolean getEmpty();
    
    @TypeInfo("ceylon.language.Nothing")
    public Iterator getIterator();
    
    @TypeInfo("ceylon.language.Nothing")
    public java.lang.Object item(@Name("key") Natural key);
    
    @TypeInfo("ceylon.language.Nothing")
    public java.lang.Object getFirst();
}
