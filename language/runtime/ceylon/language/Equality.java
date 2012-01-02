package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon
public interface Equality {
    public boolean equals(@Name("that") @TypeInfo("ceylon.language.Equality") java.lang.Object that);
    public int hashCode();
}
