package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon
public interface Identifiable {

    public boolean equals(@Name("that") @TypeInfo("ceylon.language.Object") 
    java.lang.Object that);
    
    @Override
    public int hashCode();
    
}