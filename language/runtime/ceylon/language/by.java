package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon(major = 2)
@Method
public final class by
{
    public static Nothing by(@Name("authors") @Sequenced
            @TypeInfo("ceylon.language.Iterable<ceylon.language.String>")
            final ceylon.language.Iterable<? extends ceylon.language.String> authors) {
        return null;
    }
    @Ignore
    public static Nothing by() {
        return null;
    }
    private by(){}
}
