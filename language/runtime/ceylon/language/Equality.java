package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Name;

public interface Equality {
    public boolean equals(@Name("that") java.lang.Object that);
    public int hashCode();
}
