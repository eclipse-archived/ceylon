package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon
@Method
public final class by
{
    public static Nothing by(@Name("authors") @Sequenced
            @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<ceylon.language.String>")
            final ceylon.language.Iterable<? extends ceylon.language.String> authors) {
        return null;
    }
    private by(){}
}
