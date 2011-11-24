package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Ceylon;
import com.redhat.ceylon.compiler.metadata.java.Method;
import com.redhat.ceylon.compiler.metadata.java.Name;
import com.redhat.ceylon.compiler.metadata.java.Sequenced;
import com.redhat.ceylon.compiler.metadata.java.TypeInfo;

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
