package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon
public interface Closeable {
    void open();
    void close(@Name("exception") 
    @TypeInfo("ceylon.language.Nothing|ceylon.language.Exception") 
    Exception exception);
}
