package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Ceylon;
import com.redhat.ceylon.compiler.metadata.java.Name;
import com.redhat.ceylon.compiler.metadata.java.TypeInfo;

@Ceylon
public interface Equality {
    public boolean equals(@Name("that") @TypeInfo("ceylon.language.Equality") java.lang.Object that);
    public int hashCode();
}
